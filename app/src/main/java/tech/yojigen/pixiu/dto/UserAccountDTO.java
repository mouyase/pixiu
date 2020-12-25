package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class UserAccountDTO {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("device_token")
    private String deviceToken;
    private UserDTO user;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public UserDTO getUser() {
        return user;
    }
}
