package tech.yojigen.pixiu.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import tech.yojigen.android.SingleLiveEvent;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.UserAccountDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivForm;
import tech.yojigen.util.YSetting;

public class LoginViewModel extends ViewModel {
    private final Gson gson = new Gson();
    private final SingleLiveEvent onLoginSuccess = new SingleLiveEvent<>();

    public void login(String username, String password) {
        username = username.trim().toLowerCase();
        PixivForm pixivForm = new PixivForm.Builder()
                .set("username", username)
                .set("password", password)
                .set("client_id", Value.PIXIV_CLIENT_ID)
                .set("client_secret", Value.PIXIV_CLIENT_SECRET)
                .set("grant_type", "password")
                .set("device_token", "")
                .set("get_secure_url", true)
                .set("include_policy", false)
                .build();
        PixivClient.getInstance().post(Value.URL_OAUTH, pixivForm, new PixivCallback() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onResponse(String body) {
                UserAccountDTO userAccountDTO = gson.fromJson(body, UserAccountDTO.class);
                YSetting.setObject(Value.SETTING_ACCOUNT, userAccountDTO);
                onLoginSuccess.postValue();
            }
        });
    }

    public SingleLiveEvent getOnLoginSuccess() {
        return onLoginSuccess;
    }
}
