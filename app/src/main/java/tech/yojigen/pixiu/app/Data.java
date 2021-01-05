package tech.yojigen.pixiu.app;

import java.util.HashMap;
import java.util.Map;

import tech.yojigen.pixiu.dto.UserAccountDTO;
import tech.yojigen.pixiu.dto.UserDTO;

public class Data {
    private UserAccountDTO userAccount;
    private UserDTO user;
    private String accessToken;
    private String refreshToken;
    private String deviceToken;
    private Map<String, Boolean> favouriteMap = new HashMap<>();
    private String pathUri;
    private int networkMode;
    private boolean isSafeMode;
    private boolean isCDNMode;

    public boolean isCDNMode() {
        return isCDNMode;
    }

    public void setCDNMode(boolean CDNMode) {
        isCDNMode = CDNMode;
    }

    public boolean isSafeMode() {
        return isSafeMode;
    }

    public void setSafeMode(boolean safeMode) {
        isSafeMode = safeMode;
    }

    public String getPathUri() {
        return pathUri;
    }

    public void setPathUri(String pathUri) {
        this.pathUri = pathUri;
    }

    public int getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(int networkMode) {
        this.networkMode = networkMode;
    }

    public Map<String, Boolean> getFavouriteMap() {
        return favouriteMap;
    }

    public void setFavouriteMap(Map<String, Boolean> favouriteMap) {
        this.favouriteMap = favouriteMap;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public UserAccountDTO getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccountDTO userAccount) {
        this.userAccount = userAccount;
    }
}
