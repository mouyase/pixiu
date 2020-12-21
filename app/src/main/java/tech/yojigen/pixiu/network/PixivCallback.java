package tech.yojigen.pixiu.network;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public interface PixivCallback {
    public abstract void onFailure();

    public abstract void onResponse(String body);
}
