package tech.yojigen.util;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;

public class YBundle {
    public static <T> void set(Intent intent, T data) {
        Bundle bundle = new Bundle();
        bundle.putBinder("YBundle", new YBinder(data));
        intent.putExtras(bundle);
    }

    public static <T> T get(Intent intent) {
        Bundle bundle = intent.getExtras();
        YBinder binder = (YBinder) bundle.getBinder("YBundle");
        return (T) binder.getData();
    }

    private static class YBinder<T> extends Binder {
        private T data;

        YBinder(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }
}
