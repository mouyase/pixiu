package tech.yojigen.pixiu.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xuexiang.xui.utils.StatusBarUtils;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.adapter.ImageListAdapter;
import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.databinding.ActivityMainBinding;
import tech.yojigen.pixiu.viewmodel.MainViewModel;

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
                    .into(viewBinding.cover);
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

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, viewBinding.drawerlayout, toolbar, R.string.app_name, R.string.app_name);
        viewBinding.drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        viewBinding.indicator.setTabTitles(new String[]{"推荐", "关注"});
        viewBinding.indicator.setViewPager(viewBinding.viewpager, mPagerAdapter);

        TextView username = viewBinding.navigation.getHeaderView(0).findViewById(R.id.username);
        username.setText(PixiuApplication.getData().getUser().getName());
        username.setTextSize(18);
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
                        viewModel.getRecommendList().getValue().clear();
                        imageListAdapter.notifyDataSetChanged();
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
                        viewModel.getFollowedData();
                    }

                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        viewModel.getFollowedList().getValue().clear();
                        imageListAdapter.notifyDataSetChanged();
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
            imageListAdapter.setListListener((v, illust, p) -> {
                Intent intent = new Intent(MainActivity.this, IllustActivity.class);
                if (position == 0) {
                    intent.putExtra(Value.BUNDLE_ILLUST_LIST, viewModel.getRecommendBundle(p));
                } else {
                    intent.putExtra(Value.BUNDLE_ILLUST_LIST, viewModel.getFollowBundle(p));
                }
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, v, illust.getId());
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            });
            return view;
        }
    };

    protected void initViewEvent() {
        viewBinding.toolbar.inflateMenu(R.menu.menu_main);
        viewBinding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    Intent intent = new Intent(this, SearchActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return false;
        });
        viewBinding.navigation.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_setting:
                    Intent intent = new Intent(this, SettingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return false;
        });
    }
}