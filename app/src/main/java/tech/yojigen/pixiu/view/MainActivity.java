package tech.yojigen.pixiu.view;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.databinding.ActivityLoginBinding;
import tech.yojigen.pixiu.databinding.ActivityMainBinding;
import tech.yojigen.pixiu.viewmodel.LoginViewModel;
import tech.yojigen.pixiu.viewmodel.MainViewModel;
import tech.yojigen.util.YView;

public class MainActivity extends AppCompatActivity {
    MainViewModel viewModel;
    ActivityMainBinding viewBinding;

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
    }


    protected void initViewEvent() {
        viewModel.getRecommendData();
    }
}