package tech.yojigen.pixiu.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.PixivUtil;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.listener.ListListener;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;
import tech.yojigen.util.YShare;
import tech.yojigen.util.YXToast;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private List<IllustDTO> illusts;
    private int imageWidth;
    private int column;
    private ListListener listListener;

    private AtomicBoolean isSharing = new AtomicBoolean(false);

    public ImageListAdapter(List<IllustDTO> illusts, int column) {
        this.illusts = illusts;
        this.column = column >= 1 ? column : 0;
    }

    public void setListListener(ListListener listListener) {
        this.listListener = listListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_list, parent, false);
        imageWidth = parent.getWidth() / column;
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IllustDTO illust = illusts.get(position);
        if (illust.isSingle()) {
            holder.count.setVisibility(View.GONE);
        } else {
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText(illust.getPageCount() + "P");
        }
        int height = (int) (((float) illust.getHeight() / (float) illust.getWidth()) * imageWidth);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = imageWidth;
        holder.itemView.setLayoutParams(layoutParams);
        Glide.with(holder.itemView.getContext())
                .load(illust.getImageUrls().getMedium())
                .transition(withCrossFade(500))
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            if (listListener != null) {
                listListener.onItemClick(v, illust, position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            onLongClick(holder, position);
            return true;
        });
    }

    private void onLongClick(@NonNull ViewHolder holder, int position) {
        IllustDTO illust = illusts.get(position);
        String bookmarkUrl = illust.isBookmarked() ? Value.URL_API + "/v1/illust/bookmark/delete" : Value.URL_API + "/v2/illust/bookmark/add";
        ArrayList<String> itemList = new ArrayList<>();
        if (illust.isBookmarked()) {
            itemList.add("取消收藏");
        } else {
            itemList.add("收藏");
            itemList.add("悄悄收藏");
        }
        itemList.add("复制ID");
        itemList.add("分享链接");
        itemList.add("分享图片");
        itemList.add("保存原图");
        new MaterialDialog.Builder(holder.itemView.getContext())
                .items(itemList.toArray(new String[]{}))
                .itemsCallback((dialog, itemView, p, text) -> {
                    if (text.equals("收藏")) {
                        PixivData pixivData = new PixivData.Builder().set("illust_id", illust.getId()).set("restrict", "public").build();
                        PixivClient.getInstance().post(bookmarkUrl, pixivData, new PixivCallback() {
                            @Override
                            public void onFailure() {
                                YXToast.error("收藏失败");
                            }

                            @Override
                            public void onResponse(String body) {
                                YXToast.success("收藏成功");
                            }
                        });
                    } else if (text.equals("悄悄收藏")) {
                        PixivData pixivData = new PixivData.Builder().set("illust_id", illust.getId()).set("restrict", "private").build();
                        PixivClient.getInstance().post(bookmarkUrl, pixivData, new PixivCallback() {
                            @Override
                            public void onFailure() {
                                YXToast.error("悄悄收藏失败");
                            }

                            @Override
                            public void onResponse(String body) {
                                YXToast.success("悄悄收藏成功");
                            }
                        });
                    } else if (text.equals("取消收藏")) {
                        PixivData pixivData = new PixivData.Builder().set("illust_id", illust.getId()).build();
                        PixivClient.getInstance().post(bookmarkUrl, pixivData, new PixivCallback() {
                            @Override
                            public void onFailure() {
                                YXToast.error("取消收藏失败");
                            }

                            @Override
                            public void onResponse(String body) {
                                YXToast.success("取消收藏成功");
                            }
                        });
                    } else if (text.equals("复制ID")) {
                        ClipboardManager clipboardManager = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData mClipData = ClipData.newPlainText("PixiuID", illust.getId());
                        clipboardManager.setPrimaryClip(mClipData);
                    } else if (text.equals("分享链接")) {
                        Document document = Jsoup.parse(illust.getCaption());
                        String caption = document.body().text();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(illust.getTitle() + "\n");
                        stringBuilder.append(illust.getUser().getName() + "\n\n");
                        if (!TextUtils.isEmpty(caption)) {
                            stringBuilder.append(caption + "\n\n");
                        }
                        stringBuilder.append("https://www.pixiv.net/artworks/" + illust.getId());
                        YShare.text(stringBuilder.toString());
                    } else if (text.equals("分享图片")) {
                        isSharing.set(true);
                        MaterialDialog loginDialog = new MaterialDialog.Builder(holder.itemView.getContext())
                                .content("图片加载中...")
                                .progress(true, 0)
                                .progressIndeterminateStyle(false)
                                .show();
                        loginDialog.setOnCancelListener(d -> isSharing.set(false));
                        Glide.with(holder.itemView.getContext()).asBitmap().load(illust.getImageUrls().getLarge()).into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                loginDialog.cancel();
                                if (isSharing.get()) {
                                    YShare.image(resource);
                                    isSharing.set(false);
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                loginDialog.cancel();
                                isSharing.set(false);
                            }
                        });
                    } else if (text.equals("保存原图")) {
                        PixivUtil.saveImage(holder.itemView.getContext(), illust);
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return illusts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            count = itemView.findViewById(R.id.count);
        }
    }

}
