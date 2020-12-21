package tech.yojigen.pixiu.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.xuexiang.xui.utils.StatusBarUtils;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.viewmodel.MainViewModel;
import tech.yojigen.pixiu.viewmodel.RouterViewModel;
import tech.yojigen.util.YSetting;

public class RouterActivity extends AppCompatActivity {
    RouterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);
        StatusBarUtils.initStatusBarStyle(this, false);
        viewModel = new ViewModelProvider(this).get(RouterViewModel.class);
        route();
    }

    void route() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String accessToken = YSetting.get(Value.SETTING_ACCESS_TOKEN, "");
            if (accessToken.equals("")) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        }, 1000);
    }
}