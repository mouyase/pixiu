package tech.yojigen.pixiu.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.xuexiang.xui.utils.StatusBarUtils;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.dto.UserAccountDTO;
import tech.yojigen.pixiu.viewmodel.RouterViewModel;

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
            UserAccountDTO userAccountDTO = PixiuApplication.getData().getUserAccount();
            if (userAccountDTO == null) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
//            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }, 1000);
    }
}