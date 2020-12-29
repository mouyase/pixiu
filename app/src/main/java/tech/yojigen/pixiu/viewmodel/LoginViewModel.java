package tech.yojigen.pixiu.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import tech.yojigen.android.SingleLiveEvent;
import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.CreateAccountDTO;
import tech.yojigen.pixiu.dto.UserAccountDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;
import tech.yojigen.util.YSetting;

public class LoginViewModel extends ViewModel {
    private final Gson gson = new Gson();
    private final SingleLiveEvent onLoginSuccess = new SingleLiveEvent<>();
    private final SingleLiveEvent onLoginFailed = new SingleLiveEvent<>();
    private final SingleLiveEvent onRegistFailed = new SingleLiveEvent<>();

    public void login(String username, String password) {
        username = username.trim();
        PixivData pixivData = new PixivData.Builder()
                .set("username", username)
                .set("password", password)
                .set("client_id", Value.PIXIV_CLIENT_ID)
                .set("client_secret", Value.PIXIV_CLIENT_SECRET)
                .set("grant_type", "password")
                .set("device_token", "pixiv")
                .set("get_secure_url", true)
                .set("include_policy", true)
                .build();
        PixivClient.getInstance().post(Value.URL_OAUTH, pixivData, new PixivCallback() {
            @Override
            public void onFailure() {
                onLoginFailed.postValue();
            }

            @Override
            public void onResponse(String body) {
                UserAccountDTO userAccountDTO = gson.fromJson(body, UserAccountDTO.class);
                YSetting.setObject(Value.SETTING_ACCOUNT, userAccountDTO);
                PixiuApplication.getData().setUserAccount(userAccountDTO);
                PixiuApplication.getData().setAccessToken(userAccountDTO.getAccessToken());
                PixiuApplication.getData().setRefreshToken(userAccountDTO.getRefreshToken());
                PixiuApplication.getData().setDeviceToken(userAccountDTO.getDeviceToken());
                PixiuApplication.getData().setUser(userAccountDTO.getUser());
                onLoginSuccess.postValue();
            }
        });
    }

    public void regist(String username) {
        username = username.trim();
//        PixiuApplication.getData().setAccessToken("l-f9qZ0ZyqSwRyZs8-MymbtWBbSxmCu1pmbOlyisou8");
        PixivData pixivData = new PixivData.Builder()
                .set("user_name", username)
                .set("ref", "pixiv_android_app_provisional_account")
                .build();
        PixivClient.getInstance().post(Value.URL_ACCOUNT, pixivData, new PixivCallback() {
            @Override
            public void onFailure() {
                onRegistFailed.postValue();
            }

            @Override
            public void onResponse(String body) {
                CreateAccountDTO createAccountDTO = gson.fromJson(body, CreateAccountDTO.class);
                login(createAccountDTO.getUserData().getUsername(), createAccountDTO.getUserData().getPassword());
            }
        });
    }

    public SingleLiveEvent getOnLoginSuccess() {
        return onLoginSuccess;
    }

    public SingleLiveEvent getOnLoginFailed() {
        return onLoginFailed;
    }

    public SingleLiveEvent getOnRegistFailed() {
        return onRegistFailed;
    }
}
