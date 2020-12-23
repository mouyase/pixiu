package tech.yojigen.pixiu.adapter;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.listener.ImageListListener;
import tech.yojigen.util.YShare;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListHolder> {
    private List<IllustDTO> illusts;
    private int imageWidth;
    private int column;
    private ImageListListener imageListListener;

    private Boolean isSharing = false;

    public ImageListAdapter(List<IllustDTO> illusts, int column) {
        this.illusts = illusts;
        this.column = column >= 1 ? column : 0;
    }

    public void setListListener(ImageListListener listListener) {
        this.imageListListener = listListener;
    }

    @NonNull
    @Override
    public ImageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagelist, parent, false);
        imageWidth = parent.getWidth() / column;
        return new ImageListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageListHolder holder, int position) {
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
            this.imageListListener.onItemClick(v, illust, position);
        });
        holder.itemView.setOnLongClickListener(v -> {
            onLongClick(holder, position);
            return true;
        });
    }

    private void onLongClick(@NonNull ImageListHolder holder, int position) {
        IllustDTO illust = illusts.get(position);
        new MaterialDialog.Builder(holder.itemView.getContext())
                .items(new String[]{"收藏", "分享ID", "分享图片", "保存原图"})
                .itemsCallback((dialog, itemView, p, text) -> {
                    switch (p) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            isSharing = true;
                            MaterialDialog loginDialog = new MaterialDialog.Builder(holder.itemView.getContext())
                                    .content("图片加载中...")
                                    .progress(true, 0)
                                    .progressIndeterminateStyle(false)
                                    .show();
                            loginDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    isSharing = false;
                                }
                            });
                            Glide.with(holder.itemView.getContext()).asBitmap()
                                    .load(illust.getImageUrls().getLarge()).into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    loginDialog.cancel();
                                    if (isSharing) {
                                        YShare.image(resource);
                                        isSharing = false;
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    loginDialog.cancel();
                                    isSharing = false;
                                }
                            });
                            break;
                        case 3:
                            break;
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return illusts.size();
    }

    static class ImageListHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView count;

        public ImageListHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            count = itemView.findViewById(R.id.count);
        }
    }

}
