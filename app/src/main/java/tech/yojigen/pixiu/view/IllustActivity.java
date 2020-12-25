package tech.yojigen.pixiu.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.ViewGroup;

import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import tech.yojigen.pixiu.adapter.HotTagListAdapter;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.databinding.ActivityIllustBinding;
import tech.yojigen.pixiu.databinding.ActivitySearchBinding;
import tech.yojigen.pixiu.viewmodel.IllustViewModel;
import tech.yojigen.pixiu.viewmodel.SearchViewModel;

public class IllustActivity extends AppCompatActivity {
    private IllustViewModel viewModel;
    private ActivityIllustBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityIllustBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        initViewModel();
        initView();
        initViewEvent();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(IllustViewModel.class);
    }

    private void initViewEvent() {
    }

    private void initView() {
        // 设置沉浸式状态栏
        StatusBarUtils.initStatusBarStyle(this, false);
        // 给Toolbar动态设置Margin，让开StatusBar
        TitleBar toolbar = viewBinding.titleBar;
        ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
        ViewGroup.MarginLayoutParams toolbarMarginParams;
        if (toolbarParams instanceof ViewGroup.MarginLayoutParams) {
            toolbarMarginParams = (ViewGroup.MarginLayoutParams) toolbarParams;
        } else {
            toolbarMarginParams = new ViewGroup.MarginLayoutParams(toolbarParams);
        }
        toolbarMarginParams.setMargins(0, StatusBarUtils.getStatusBarHeight(this), 0, 0);
        toolbar.setLayoutParams(toolbarMarginParams);
        viewBinding.titleBar.setLeftClickListener(v -> finish());
    }
}