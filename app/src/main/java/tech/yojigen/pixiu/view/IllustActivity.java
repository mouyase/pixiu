package tech.yojigen.pixiu.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.databinding.ActivityIllustBinding;
import tech.yojigen.pixiu.dto.BundleIllustDTO;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;
import tech.yojigen.pixiu.viewmodel.IllustViewModel;
import tech.yojigen.util.YShare;
import tech.yojigen.util.YToast;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class IllustActivity extends AppCompatActivity {
    private IllustViewModel viewModel;
    private ActivityIllustBinding viewBinding;

    String nextUrl;

    BundleIllustDTO bundleIllustDTO;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityIllustBinding.inflate(getLayoutInflater());


        postponeEnterTransition();
        setContentView(viewBinding.getRoot());

        bundleIllustDTO = BundleIllustDTO.fromJson(getIntent().getStringExtra(Value.BUNDLE_ILLUST_LIST));

        initViewModel();
        initView();
        initViewEvent();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(IllustViewModel.class);
        viewPagerAdapter = new ViewPagerAdapter(viewModel.getIllustList().getValue());
        viewModel.getIllustList().observe(this, illustDTOS -> {
            viewPagerAdapter.notifyItemInserted(illustDTOS.size());
        });
        viewModel.setIllustList(bundleIllustDTO.getIllustList());
    }

    private void initViewEvent() {
    }

    private void initView() {
        // 设置沉浸式状态栏
        StatusBarUtils.initStatusBarStyle(this, false);
        // 给Toolbar动态设置Margin，让开StatusBar
        ViewGroup.LayoutParams toolbarParams = viewBinding.titleBar.getLayoutParams();
        ViewGroup.MarginLayoutParams toolbarMarginParams;
        if (toolbarParams instanceof ViewGroup.MarginLayoutParams) {
            toolbarMarginParams = (ViewGroup.MarginLayoutParams) toolbarParams;
        } else {
            toolbarMarginParams = new ViewGroup.MarginLayoutParams(toolbarParams);
        }
        toolbarMarginParams.setMargins(0, StatusBarUtils.getStatusBarHeight(this), 0, 0);
        viewBinding.titleBar.setLayoutParams(toolbarMarginParams);

        // 给Toolbar动态设置Margin，让开StatusBar
        ViewGroup.LayoutParams pagerLayoutParams = viewBinding.viewPager.getLayoutParams();
        ViewGroup.MarginLayoutParams pagerMarginParams;
        if (toolbarParams instanceof ViewGroup.MarginLayoutParams) {
            pagerMarginParams = (ViewGroup.MarginLayoutParams) pagerLayoutParams;
        } else {
            pagerMarginParams = new ViewGroup.MarginLayoutParams(pagerLayoutParams);
        }
        pagerMarginParams.setMargins(0, StatusBarUtils.getStatusBarHeight(this), 0, 0);
        viewBinding.viewPager.setLayoutParams(pagerMarginParams);

        viewBinding.titleBar.setLeftClickListener(v -> ActivityCompat.finishAfterTransition(this));


        viewBinding.viewPager.setAdapter(viewPagerAdapter);
//        viewBinding.viewPager.setOffscreenPageLimit(-1);
        viewBinding.viewPager.setCurrentItem(bundleIllustDTO.getPosition(), false);


    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        return super.onCreatePanelView(featureId);
    }

    class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> {
        private List<IllustDTO> illusts;

        public ViewPagerAdapter(List<IllustDTO> illusts) {
            this.illusts = illusts;
        }

        @NonNull
        @Override
        public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_illust, parent, false);
            return new ViewPagerAdapter.ViewPagerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
            AtomicBoolean isSharing = new AtomicBoolean(false);
            AtomicBoolean isSaving = new AtomicBoolean(false);
            IllustDTO illust = illusts.get(position);
            boolean isBookmarked = illust.isBookmarked();
            if (PixiuApplication.getData().getFavouriteMap().containsKey(illust.getId())) {
                isBookmarked = PixiuApplication.getData().getFavouriteMap().get(illust.getId());
                illust.setBookmarked(isBookmarked);
            } else {
                PixiuApplication.getData().getFavouriteMap().put(illust.getId(), isBookmarked);
            }
            String bookmarkUrl = isBookmarked ? Value.URL_API + "/v1/illust/bookmark/delete" : Value.URL_API + "/v2/illust/bookmark/add";
            if (isBookmarked) {
                holder.like.setChecked(true);
            } else {
                holder.like.setChecked(false);
            }
            if (illust.isSingle()) {
                holder.count.setVisibility(View.GONE);
            } else {
                holder.count.setVisibility(View.VISIBLE);
                holder.count.setText(illust.getPageCount() + "P");
            }
            holder.title.setText(illust.getTitle());
            holder.artist.setText(illust.getUser().getName());
            Glide.with(holder.itemView.getContext())
                    .load(illust.getUser().getHeadImage())
                    .transition(withCrossFade(500))
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.head.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            holder.itemView.getViewTreeObserver().addOnPreDrawListener(() -> {
                ViewCompat.setTransitionName(holder.image, illust.getId());
                getWindow().getSharedElementEnterTransition().addListener(new android.transition.Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(android.transition.Transition transition) {
                        holder.info.setVisibility(View.GONE);
                    }

                    @Override
                    public void onTransitionEnd(android.transition.Transition transition) {
                        holder.info.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onTransitionCancel(android.transition.Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(android.transition.Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(android.transition.Transition transition) {

                    }
                });
                startPostponedEnterTransition();
                return true;
            });
            Glide.with(holder.itemView.getContext())
                    .load(illust.getImageUrls().getMedium())
                    .transition(withCrossFade(500))
                    .into(holder.image);
            holder.save.setOnClickListener(v -> {
                if (!isSaving.get()) {
                    isSaving.set(true);
                    if (TextUtils.isEmpty(PixiuApplication.getData().getPathUri())) {
                        Intent intent = new Intent(holder.itemView.getContext(), SettingActivity.class);
                        holder.itemView.getContext().startActivity(intent);
                        YToast.show("选择图片保存目录");
                        return;
                    }
                    Glide.with(holder.itemView.getContext()).asBitmap().load(illust.getOriginalList().get(0)).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Uri uri = DocumentFile
                                    .fromTreeUri(holder.itemView.getContext(), Uri.parse(PixiuApplication.getData().getPathUri()))
                                    .createFile("image/*", illust.getId() + ".png").getUri();
                            try {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                resource.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                holder.itemView.getContext().getContentResolver().openOutputStream(uri).write(byteArrayOutputStream.toByteArray());
                                String pathString = URLDecoder.decode(String.valueOf(PixiuApplication.getData().getPathUri()), "UTF-8");
                                pathString = pathString.replace("content://com.android.externalstorage.documents/tree/primary:", "");
                                XToast.success(holder.itemView.getContext(), "图片保存在: " + pathString + "/" + illust.getId() + ".png").show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            isSaving.set(false);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            XToast.error(holder.itemView.getContext(), "图片保存失败").show();
                            isSaving.set(false);
                        }
                    });
                }
            });
            holder.resend.setOnClickListener(v -> {
                if (!isSharing.get()) {
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
                }
            });
            holder.like.setOnClickListener(v -> {
                PixivData pixivData = new PixivData.Builder().set("illust_id", illust.getId()).set("restrict", "public").build();
                PixivClient.getInstance().post(bookmarkUrl, pixivData, new PixivCallback() {
                    @Override
                    public void onFailure() {
                        if (illust.isBookmarked()) {
                            YToast.show("收藏失败");
                            holder.like.setChecked(false);
                        } else {
                            YToast.show("取消收藏失败");
                            holder.like.setChecked(true);
                        }
                    }

                    @Override
                    public void onResponse(String body) {
                        if (illust.isBookmarked()) {
                            YToast.show("取消收藏成功");
                            illust.setBookmarked(false);
                            holder.like.setChecked(false);
                            PixiuApplication.getData().getFavouriteMap().put(illust.getId(), false);
                        } else {
                            YToast.show("收藏成功");
                            illust.setBookmarked(true);
                            holder.like.setChecked(true);
                            PixiuApplication.getData().getFavouriteMap().put(illust.getId(), true);
                        }
                    }
                });
            });
            holder.like.setOnLongClickListener(v -> {
                PixivData pixivData = new PixivData.Builder().set("illust_id", illust.getId()).set("restrict", "private").build();
                if (!illust.isBookmarked()) {
                    PixivClient.getInstance().post(bookmarkUrl, pixivData, new PixivCallback() {
                        @Override
                        public void onFailure() {
                            YToast.show("悄悄收藏失败");
                        }

                        @Override
                        public void onResponse(String body) {
                            YToast.show("悄悄收藏成功");
                            illust.setBookmarked(true);
                            holder.like.setChecked(true);
                            PixiuApplication.getData().getFavouriteMap().put(illust.getId(), true);
                        }
                    });
                }
                return true;
            });
//            holder.info.setOnClickListener(v -> {
//                Intent intent = new Intent(IllustActivity.this, InfoActivity.class);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(IllustActivity.this);
//                ActivityCompat.startActivity(IllustActivity.this, intent, options.toBundle());
//            });
        }

        @Override
        public int getItemCount() {
            return illusts.size();
        }

        class ViewPagerViewHolder extends RecyclerView.ViewHolder {
            ImageView image, head, resend, save;
            TextView count, title, artist;
            ConstraintLayout info;
            ShineButton like;

            public ViewPagerViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                head = itemView.findViewById(R.id.head);
                count = itemView.findViewById(R.id.count);
                title = itemView.findViewById(R.id.title);
                artist = itemView.findViewById(R.id.artist);
                resend = itemView.findViewById(R.id.resend);
                save = itemView.findViewById(R.id.save);
                like = itemView.findViewById(R.id.like);
                info = itemView.findViewById(R.id.info);
            }
        }
    }
}