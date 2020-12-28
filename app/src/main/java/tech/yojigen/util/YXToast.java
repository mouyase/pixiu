package tech.yojigen.util;

import android.widget.Toast;

import com.xuexiang.xui.widget.toast.XToast;

public class YXToast {
    public static void info(String text) {
        YThread.runOnUiThread(() -> XToast.info(YUtil.getInstance().getContext(), text, Toast.LENGTH_LONG).show());
    }

    public static void error(String text) {
        YThread.runOnUiThread(() -> XToast.error(YUtil.getInstance().getContext(), text, Toast.LENGTH_LONG).show());
    }

    public static void success(String text) {
        YThread.runOnUiThread(() -> XToast.success(YUtil.getInstance().getContext(), text, Toast.LENGTH_LONG).show());
    }

    public static void warning(String text) {
        YThread.runOnUiThread(() -> XToast.warning(YUtil.getInstance().getContext(), text, Toast.LENGTH_LONG).show());
    }
}
