package tech.yojigen.pixiu.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.app.Util;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.databinding.ActivityIllustBinding;
import tech.yojigen.pixiu.dto.BundleIllustDTO;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;
import tech.yojigen.pixiu.viewmodel.IllustViewModel;
import tech.yojigen.util.YShare;
import tech.yojigen.util.YXToast;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class IllustActivity extends AppCompatActivity {
    private IllustViewModel viewModel;
    private ActivityIllustBinding viewBinding;

    BundleIllustDTO bundleIllustDTO;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        viewBinding = ActivityIllustBinding.inflate(getLayoutInflater());
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
        viewModel.setBundle(bundleIllustDTO);
    }

    private void initViewEvent() {
        viewBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position > (viewModel.getIllustList().getValue().size() - 10)) {
                    viewModel.getMoreData();
                }
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
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
            IllustDTO illust = illusts.get(position);
            boolean isBookmarked = illust.isBookmarked();
            if (PixiuApplication.getData().getFavouriteMap().containsKey(illust.getId())) {
                isBookmarked = PixiuApplication.getData().getFavouriteMap().get(illust.getId());
                illust.setBookmarked(isBookmarked);
            } else {
                PixiuApplication.getData().getFavouriteMap().put(illust.getId(), isBookmarked);
            }
            String bookmarkUrl = isBookmarked ? Value.URL_API + "/v1/illust/bookmark/delete" : Value.URL_API + "/v2/illust/bookmark/add";
            holder.like.setChecked(isBookmarked);
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
            holder.save.setOnClickListener(v -> Util.saveImage(holder.itemView.getContext(), illust));
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
                            YXToast.error("收藏失败");
                            holder.like.setChecked(false);
                        } else {
                            YXToast.error("取消收藏失败");
                            holder.like.setChecked(true);
                        }
                    }

                    @Override
                    public void onResponse(String body) {
                        if (illust.isBookmarked()) {
                            YXToast.success("取消收藏成功");
                            illust.setBookmarked(false);
                            holder.like.setChecked(false);
                            PixiuApplication.getData().getFavouriteMap().put(illust.getId(), false);
                        } else {
                            YXToast.success("收藏成功");
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
                            YXToast.error("悄悄收藏失败");
                        }

                        @Override
                        public void onResponse(String body) {
                            YXToast.success("悄悄收藏成功");
                            illust.setBookmarked(true);
                            holder.like.setChecked(true);
                            PixiuApplication.getData().getFavouriteMap().put(illust.getId(), true);
                        }
                    });
                }
                return true;
            });
            holder.info.setOnClickListener(v -> {
                Gson gson = new Gson();
                Intent intent = new Intent(IllustActivity.this, InfoActivity.class);
                Pair<View, String> title = new Pair<>(holder.title, ViewCompat.getTransitionName(holder.title));
                Pair<View, String> head = new Pair<>(holder.head, ViewCompat.getTransitionName(holder.head));
                Pair<View, String> at = new Pair<>(holder.at, ViewCompat.getTransitionName(holder.at));
                Pair<View, String> artist = new Pair<>(holder.artist, ViewCompat.getTransitionName(holder.artist));
                intent.putExtra(Value.BUNDLE_ILLUST, gson.toJson(illust));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(IllustActivity.this, title, head, at, artist);
                ActivityCompat.startActivity(IllustActivity.this, intent, options.toBundle());
            });
        }

        @Override
        public int getItemCount() {
            return illusts.size();
        }

        class ViewPagerViewHolder extends RecyclerView.ViewHolder {
            ImageView image, head, resend, save;
            TextView count, title, artist, at;
            ConstraintLayout info;
            ShineButton like;

            public ViewPagerViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                head = itemView.findViewById(R.id.head);
                count = itemView.findViewById(R.id.count);
                title = itemView.findViewById(R.id.title);
                at = itemView.findViewById(R.id.at);
                artist = itemView.findViewById(R.id.artist);
                resend = itemView.findViewById(R.id.resend);
                save = itemView.findViewById(R.id.save);
                like = itemView.findViewById(R.id.like);
                info = itemView.findViewById(R.id.info);
            }
        }
    }
}