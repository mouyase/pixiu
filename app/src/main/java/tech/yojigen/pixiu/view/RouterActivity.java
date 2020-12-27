package tech.yojigen.pixiu.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.xuexiang.xui.utils.StatusBarUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.dto.UserAccountDTO;
import tech.yojigen.pixiu.viewmodel.RouterViewModel;

public class RouterActivity extends AppCompatActivity {
    RouterViewModel viewModel;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getDataString();
        setContentView(R.layout.activity_router);
        StatusBarUtils.initStatusBarStyle(this, false);
        viewModel = new ViewModelProvider(this).get(RouterViewModel.class);
        route();
    }

    void route() {
        if (TextUtils.isEmpty(url)) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                UserAccountDTO userAccountDTO = PixiuApplication.getData().getUserAccount();
                if (userAccountDTO == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
            }, 1000);
        } else {
            System.out.println(url);
            if (url.contains("https://www.pixiv.net/artworks/") || url.contains("https://www.pixiv.net/member_illust.php")) {
                Matcher matcher = Pattern.compile("[0-9]+").matcher(url);
                if (matcher.find()) {
                    System.out.println(matcher.group(0));
                }
            } else if (url.contains("https://www.pixiv.net/users/")) {
            }
        }
    }
}