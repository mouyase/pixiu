package tech.yojigen.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.view.SettingActivity;

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