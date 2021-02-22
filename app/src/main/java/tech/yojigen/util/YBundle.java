package tech.yojigen.util;

import android.content.Intent;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class YBundle {
    private static Map<String, String> mDataMap = new HashMap();
    private static String BUNDLE_KEY = YDigest.MD5(Build.DEVICE);
    private static Gson gson = new Gson();

    public static <T> String set(T data) {
        String key = YUUID.get();
        mDataMap.put(key, gson.toJson(data));
        YSetting.setObject(BUNDLE_KEY, mDataMap);
        return key;
    }

    public static <T> T get(String key) {
        mDataMap = YSetting.getObject(BUNDLE_KEY, new TypeToken<Map<String, String>>() {
        }.getType());
        return (T) mDataMap.get(key);
    }

    public static <T> void set(Intent intent, T data) {
        intent.putExtra(BUNDLE_KEY, YBundle.set(data));
    }

    public static <T> T get(Intent intent, Class<T> classOfT) {
        return (T) gson.fromJson(mDataMap.get(intent.getStringExtra(BUNDLE_KEY)), classOfT);
    }
}
