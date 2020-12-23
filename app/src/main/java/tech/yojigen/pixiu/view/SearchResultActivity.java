package tech.yojigen.pixiu.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xuexiang.xui.utils.StatusBarUtils;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.adapter.ImageListAdapter;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.databinding.ActivityMainBinding;
import tech.yojigen.pixiu.databinding.ActivitySearchResultBinding;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.listener.ImageListListener;
import tech.yojigen.pixiu.viewmodel.MainViewModel;
import tech.yojigen.pixiu.viewmodel.SearchResultViewModel;
import tech.yojigen.util.YToast;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SearchResultActivity extends AppCompatActivity {
    private SearchResultViewModel viewModel;
    private ActivitySearchResultBinding viewBinding;
    private String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        searchKey = getIntent().getStringExtra(Value.BUNDLE_KEY_SEARCH);

        initViewModel();
        initView();
        initViewEvent();
    }

    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SearchResultViewModel.class);
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

        viewBinding.indicator.setTabTitles(new String[]{"本日", "本周", "本月", "本季", "本半年", "本年", "全部"});
        viewBinding.indicator.setViewPager(viewBinding.viewpager, mPagerAdapter);
    }

    private final PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return 7;
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
            View view = LayoutInflater.from(SearchResultActivity.this).inflate(R.layout.pager_search_result, container, false);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
            staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            RefreshLayout refreshLayout = view.findViewById(R.id.refreshlayout);
            ImageListAdapter imageListAdapter;
//            if (position == 0) {
            imageListAdapter = new ImageListAdapter(viewModel.getIllustListList().get(0).getValue(), 3);
//                refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//                    @Override
//                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                        viewModel.getRecommendData();
//                    }
//
//                    @Override
//                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                        viewModel.refreshRecommendData();
//                    }
//                });
            viewModel.getIllustListList().get(0).observe(SearchResultActivity.this, illusts -> {
                refreshLayout.finishLoadMore(true);
                refreshLayout.finishRefresh(true);
                imageListAdapter.notifyItemInserted(illusts.size());
            });
//            } else {
//                imageListAdapter = new ImageListAdapter(viewModel.getFollowedList().getValue(), 3);
//                refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//                    @Override
//                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                        viewModel.refreshFollowedData();
//                    }
//
//                    @Override
//                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                        viewModel.refreshFollowedData();
//                    }
//                });
//                viewModel.getFollowedList().observe(MainActivity.this, illusts -> {
//                    refreshLayout.finishLoadMore(true);
//                    refreshLayout.finishRefresh(true);
//                    imageListAdapter.notifyItemInserted(illusts.size());
//                });
//            }
            recyclerView.setAdapter(imageListAdapter);
//            imageListAdapter.setListListener(new ImageListListener() {
//                @Override
//                public void onItemClick(View view, IllustDTO illust, int position) {
//
//                }
//
//                @Override
//                public void onItemLongClick(View view, IllustDTO illust, int position) {
//
//                }
//            });
            return view;
        }
    };
    int WRITE_REQUEST_CODE = 0x0012;

    protected void initViewEvent() {
//        viewBinding.toolbar.inflateMenu(R.menu.menu_main);
//        viewBinding.toolbar.setOnMenuItemClickListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.action_search:
//                    YToast.show("aaa");
//                    break;
//                default:
//                    break;
//            }
//            return false;
//        });
//        viewModel.getData(0);
    }
}