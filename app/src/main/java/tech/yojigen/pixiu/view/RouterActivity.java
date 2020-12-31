package tech.yojigen.pixiu.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.BundleIllustDTO;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.dto.SingleIllustDTO;
import tech.yojigen.pixiu.dto.UserAccountDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;
import tech.yojigen.pixiu.viewmodel.RouterViewModel;
import tech.yojigen.util.YBundle;
import tech.yojigen.util.YXToast;

public class RouterActivity extends AppCompatActivity {
    private RouterViewModel viewModel;
    private String url;
    private Gson gson = new Gson();

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
                    String id = matcher.group(0);
                    PixivData pixivData = new PixivData.Builder()
                            .set("filter", "for_android")
                            .set("illust_id", id)
                            .build();
                    PixivClient.getInstance().get(Value.URL_API + "/v1/illust/detail", pixivData, new PixivCallback() {
                        @Override
                        public void onFailure() {
                            YXToast.error("加载失败");
                            finish();
                        }

                        @Override
                        public void onResponse(String body) {
                            SingleIllustDTO singleIllustDTO = gson.fromJson(body, SingleIllustDTO.class);
                            List<IllustDTO> illustDTOList = new ArrayList<>();
                            illustDTOList.add(singleIllustDTO.getIllust());
                            BundleIllustDTO bundleIllustDTO = new BundleIllustDTO();
                            bundleIllustDTO.setPosition(0);
                            bundleIllustDTO.setIllustList(illustDTOList);
                            bundleIllustDTO.setMode(BundleIllustDTO.MODE_SINGLE);
                            Intent intent = new Intent(RouterActivity.this, IllustActivity.class);
                            YBundle.set(intent,bundleIllustDTO);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            } else if (url.contains("https://www.pixiv.net/users/")) {
            }
        }
    }
}