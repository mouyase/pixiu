package tech.yojigen.pixiu.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
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
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;

import java.util.ArrayList;

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
        viewModel.setKey(searchKey);
    }

    protected void initView() {
        // 去掉searchTextView默认的padding
        AppCompatEditText searchTextView = findViewById(R.id.searchTextView);
        searchTextView.setPadding(0, 0, 0, 0);

        viewBinding.titleBar.setTitle(searchKey);
        viewBinding.searchView.setVoiceSearch(false);
        viewBinding.searchView.setEllipsize(true);
        viewBinding.searchView.setSuggestions(new String[]{"aaa", "bbb", "ccc"});
        viewBinding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
                intent.putExtra(Value.BUNDLE_KEY_SEARCH, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        viewBinding.searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchTextView.setText(searchKey);
                searchTextView.setSelection(searchKey.length());
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
        viewBinding.searchView.setSubmitOnClick(true);

        viewBinding.titleBar.setOnTitleClickListener(v -> runOnUiThread(() -> viewBinding.searchView.showSearch()));
        viewBinding.titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_search) {
            @Override
            public void performAction(View view) {
                runOnUiThread(() -> viewBinding.searchView.showSearch());
            }
        });
        viewBinding.titleBar.setLeftClickListener(v -> finish());

        viewBinding.indicator.setTabTitles(new String[]{"周维度", "月维度", "季维度", "半年维度", "年维度", "全部"});
        viewBinding.indicator.setViewPager(viewBinding.viewpager, mPagerAdapter);
    }

    private final PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return 6;
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
            imageListAdapter = new ImageListAdapter(viewModel.getIllustListList().get(position).getValue(), 3);
            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    viewModel.getData(position);
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    viewModel.getIllustListList().get(position).getValue().clear();
                    imageListAdapter.notifyDataSetChanged();
                    viewModel.refreshData(position);
                }
            });
            viewModel.getIllustListList().get(position).observe(SearchResultActivity.this, illusts -> {
                refreshLayout.finishLoadMore(true);
                refreshLayout.finishRefresh(true);
                imageListAdapter.notifyItemInserted(illusts.size());
            });
            recyclerView.setAdapter(imageListAdapter);
            return view;
        }
    };

    protected void initViewEvent() {
    }

    @Override
    protected void onPause() {
        runOnUiThread(() -> viewBinding.searchView.closeSearch());
        super.onPause();
    }
}