package tech.yojigen.pixiu.network;

public interface PixivCallback {
    public abstract void onFailure();

    public abstract void onResponse(String body);
}
