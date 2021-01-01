package tech.yojigen.util;

import android.app.Activity;
import android.content.Intent;

public class YFile {
    public static final int PATH_REQUEST_CODE = 0x1000;

    public static void setContentPath(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
        activity.startActivityForResult(intent, PATH_REQUEST_CODE);
    }

    public static void onActivityResult(Activity activity, int requestCode, Intent data) {
        if (requestCode == PATH_REQUEST_CODE) {
            if (data.getData() != null) {
                activity.getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
//            YSetting.set(Value.SETTING_PATH_URI, String.valueOf(data.getData()));
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