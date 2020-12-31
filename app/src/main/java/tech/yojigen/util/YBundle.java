package tech.yojigen.util;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class YBundle {
    private static Map mDataMap = new HashMap();
    private static String BUNDLE_KEY = "378559733f1044f2bb10bd805f82af99";

    public static <T> String set(T data) {
        String key = YUUID.get();
        mDataMap.put(key, data);
        return key;
    }

    public static <T> T get(String key) {
        return (T) mDataMap.get(key);
    }

    public static <T> void set(Intent intent, T data) {
        intent.putExtra(BUNDLE_KEY, YBundle.set(data));
    }

    public static <T> T get(Intent intent) {
        return (T) mDataMap.get(intent.getStringExtra(BUNDLE_KEY));
    }
}
