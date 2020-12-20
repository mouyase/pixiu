package tech.yojigen.pixiu.view;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.xuexiang.xui.utils.StatusBarUtils;

import tech.yojigen.pixiu.R;
import tech.yojigen.util.YView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置为沉浸式状态栏
        StatusBarUtils.initStatusBarStyle(this, false);

        // 给Toolbar动态设置Margin，让开StatusBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
        ViewGroup.MarginLayoutParams toolbarMarginParams;
        if (toolbarParams instanceof ViewGroup.MarginLayoutParams) {
            toolbarMarginParams = (ViewGroup.MarginLayoutParams) toolbarParams;
        } else {
            toolbarMarginParams = new ViewGroup.MarginLayoutParams(toolbarParams);
        }
        toolbarMarginParams.setMargins(0, StatusBarUtils.getStatusBarHeight(this), 0, 0);
        toolbar.setLayoutParams(toolbarMarginParams);

        DrawerLayout drawerLayout = findViewById(R.id.main_drawerlayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}