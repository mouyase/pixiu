package tech.yojigen.pixiu.network;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

public class PixivForm {
    private final Map<String, String> mFormMap;

    private PixivForm(Builder builder) {
        this.mFormMap = builder.formMap;
    }

    public FormBody getForm() {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : mFormMap.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    public boolean containsKey(String key) {
        return mFormMap.containsKey(key);
    }

    public static class Builder {
        public  Map<String, String> formMap = new HashMap<>();

        public <T> Builder set(String key, T value) {
            formMap.put(key, String.valueOf(value));
            return this;
        }

        public PixivForm build() {
            return new PixivForm(this);
        }
    }
}