package tech.yojigen.pixiu.network;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.FormBody;

public class PixivData {
    private final Map<String, String> mFormMap;

    private PixivData(Builder builder) {
        this.mFormMap = builder.formMap;
    }

    public FormBody getForm() {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : mFormMap.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    public String getQuery() {
        StringBuilder builder = new StringBuilder();
        AtomicBoolean isFirst = new AtomicBoolean(true);
        for (String key : mFormMap.keySet()) {
            if (key != null && mFormMap.get(key) != null) {
                if (isFirst.compareAndSet(true, false)) {
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key).append("=").append(mFormMap.get(key));
            }
        }
        return builder.toString();
    }

    public boolean containsKey(String key) {
        return mFormMap.containsKey(key);
    }

    public static class Builder {
        private Map<String, String> formMap = new HashMap<>();

        public <T> Builder set(String key, T value) {
            formMap.put(key, String.valueOf(value));
            return this;
        }

        public PixivData build() {
            return new PixivData(this);
        }
    }
}