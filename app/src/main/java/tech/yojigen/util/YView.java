package tech.yojigen.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import tech.yojigen.pixiu.R;

public class YView {
    public static int getStatusBarHeight(Activity activity) {
        int statusBarHeight = 0;
        int resourceId_status = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId_status > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId_status);
        }
        return statusBarHeight;
    }
}
