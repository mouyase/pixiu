package tech.yojigen.pixiu.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.preview.view.SmoothImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.adapter.HotTagListAdapter;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.databinding.ActivityIllustBinding;
import tech.yojigen.pixiu.databinding.ActivitySearchBinding;
import tech.yojigen.pixiu.dto.BundleIllustDTO;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.viewmodel.IllustViewModel;
import tech.yojigen.pixiu.viewmodel.SearchViewModel;

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
            IllustDTO illust = illusts.get(position);
            if (illust.isSingle()) {
                holder.count.setVisibility(View.GONE);
            } else {
                holder.count.setVisibility(View.VISIBLE);
                holder.count.setText(illust.getPageCount() + "P");
            }

            Glide.with(holder.itemView.getContext())
                    .load(illust.getImageUrls().getMedium())
                    .transition(withCrossFade(500))
                    .into(holder.image);
        }

        @Override
        public int getItemCount() {
            return illusts.size();
        }

        class ViewPagerViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView count;

            public ViewPagerViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                count = itemView.findViewById(R.id.count);
            }
        }
    }
}