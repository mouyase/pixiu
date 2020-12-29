package tech.yojigen.util;

import android.app.Activity;
import android.content.Intent;

import tech.yojigen.pixiu.app.Value;

public class YFile {
    public static final int PATH_REQUEST_CODE = 0x1000;

    public static void setContentPath(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
        activity.startActivityForResult(intent, PATH_REQUEST_CODE);
    }

    public static void onActivityResult(int requestCode, Intent data) {
        if (requestCode == PATH_REQUEST_CODE) {
            YSetting.set(Value.SETTING_PATH_URL, String.valueOf(data.getData()));
//            PixiuApplication.getData().setPathUri(String.valueOf(data.getData()));
//            try {
//                pathString = URLDecoder.decode(String.valueOf(PixiuApplication.getData().getPathUri()), "UTF-8");
//                pathString = pathString.replace("content://com.android.externalstorage.documents/tree/primary:", "");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            pathSelect.setDetailText(pathString);
        }
    }

}