package tech.yojigen.util;

import android.content.Context;

public class YUtil {
    private static YUtil mYUtil = new YUtil();
    private Context mContext;

    public static YUtil getInstance() {
        return mYUtil;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public Context getContext() {
        return mContext;
    }

    public String getPackageName() {
        return mContext.getPackageName();
    }
}
