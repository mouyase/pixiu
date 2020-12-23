package tech.yojigen.util;

import android.widget.Toast;

public class YToast {
    public static void show(String text) {
        YThread.runOnUiThread(() -> Toast.makeText(YUtil.getInstance().getContext(), text, Toast.LENGTH_LONG).show());
    }
}
