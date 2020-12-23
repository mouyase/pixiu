package tech.yojigen.pixiu.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;
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

import java.io.FileNotFoundException;
import java.io.IOException;
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

                }
            });
            return view;
        }
    };
    int WRITE_REQUEST_CODE = 0x0012;

    protected void initViewEvent() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        //intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
////        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, new Uri());
//        startActivityForResult(intent, WRITE_REQUEST_CODE);
//        DocumentFile child = DocumentFile
//                .fromTreeUri(this, Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AD"))
//                .createDirectory("child")
//                .createFile("text/plain", "text.txt");
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data == null || resultCode != Activity.RESULT_OK) return;
//        if (requestCode == WRITE_REQUEST_CODE) {
//            DocumentFile child = DocumentFile
//                    .fromTreeUri(this, data.getData())
//                    .createDirectory("child")
//                    .createFile("text/plain", "text.txt");
//            System.out.println(data.getData());
//            Uri uri = child.getUri();
//            try {
//                getContentResolver().openOutputStream(uri).write("成功".getBytes());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}