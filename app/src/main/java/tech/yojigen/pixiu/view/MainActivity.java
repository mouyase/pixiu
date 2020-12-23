package tech.yojigen.pixiu.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.adapter.ImageListAdapter;
import tech.yojigen.pixiu.databinding.ActivityMainBinding;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.listener.ImageListListener;
import tech.yojigen.pixiu.viewmodel.MainViewModel;
import tech.yojigen.util.YShare;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private ActivityMainBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initViewModel();
        initView();
        initViewEvent();
    }

    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getCoverImageUrl().observe(this, s -> {
            Glide.with(this)
                    .load(s)
                    .transition(withCrossFade(500))
                    .into(viewBinding.coverImage);
        });
    }

    protected void initView() {
        // 设置沉浸式状态栏
        StatusBarUtils.initStatusBarStyle(this, false);
        // 给Toolbar动态设置Margin，让开StatusBar
        Toolbar toolbar = viewBinding.toolbar;
        ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
        ViewGroup.MarginLayoutParams toolbarMarginParams;
        if (toolbarParams instanceof ViewGroup.MarginLayoutParams) {
            toolbarMarginParams = (ViewGroup.MarginLayoutParams) toolbarParams;
        } else {
            toolbarMarginParams = new ViewGroup.MarginLayoutParams(toolbarParams);
        }
        toolbarMarginParams.setMargins(0, StatusBarUtils.getStatusBarHeight(this), 0, 0);
        toolbar.setLayoutParams(toolbarMarginParams);

        DrawerLayout drawerLayout = viewBinding.drawerlayout;
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        viewBinding.indicator.setTabTitles(new String[]{"推荐", "关注"});
        viewBinding.indicator.setViewPager(viewBinding.viewpager, mPagerAdapter);
    }

    private final PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pager_main, container, false);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
            staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            RefreshLayout refreshLayout = view.findViewById(R.id.refreshlayout);
            ImageListAdapter imageListAdapter;
            if (position == 0) {
                imageListAdapter = new ImageListAdapter(viewModel.getRecommendList().getValue(), 3);
                refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        viewModel.getRecommendData();
                    }

                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        viewModel.refreshRecommendData();
                    }
                });
                viewModel.getRecommendList().observe(MainActivity.this, illusts -> {
                    refreshLayout.finishLoadMore(true);
                    refreshLayout.finishRefresh(true);
                    imageListAdapter.notifyItemInserted(illusts.size());
                });
            } else {
                imageListAdapter = new ImageListAdapter(viewModel.getFollowedList().getValue(), 3);
                refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        viewModel.refreshFollowedData();
                    }

                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        viewModel.refreshFollowedData();
                    }
                });
                viewModel.getFollowedList().observe(MainActivity.this, illusts -> {
                    refreshLayout.finishLoadMore(true);
                    refreshLayout.finishRefresh(true);
                    imageListAdapter.notifyItemInserted(illusts.size());
                });
            }
            recyclerView.setAdapter(imageListAdapter);
            imageListAdapter.setListListener(new ImageListListener() {
                @Override
                public void onItemClick(View view, IllustDTO illust, int position) {

                }

                @Override
                public void onItemLongClick(View view, IllustDTO illust, int position) {
                    new MaterialDialog.Builder(MainActivity.this)
                            .items(new String[]{"收藏", "分享ID", "分享图片", "保存原图"})
                            .itemsCallback((dialog, itemView, p, text) -> {
                                switch (p) {
                                    case 0:
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        MaterialDialog loginDialog = new MaterialDialog.Builder(MainActivity.this)
                                                .content("图片加载中...")
                                                .progress(true, 0)
                                                .progressIndeterminateStyle(false)
                                                .cancelable(false)
                                                .show();
                                        Glide.with(MainActivity.this).asBitmap()
                                                .load(illust.getImageUrls().getLarge()).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                loginDialog.cancel();
                                                YShare.image(resource);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                loginDialog.cancel();
                                            }
                                        });
                                        break;
                                    case 3:
                                        break;
                                }
                            })
                            .show();
                }
            });
            return view;
        }
    };

    protected void initViewEvent() {

    }
}