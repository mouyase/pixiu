package tech.yojigen.pixiu.app;

import android.content.Context;
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
import java.net.URLDecoder;
import java.util.ArrayList;

import tech.yojigen.pixiu.dto.IllustDTO;
import tech.yojigen.pixiu.view.SettingActivity;
import tech.yojigen.util.YUtil;
import tech.yojigen.util.YXToast;

public class Util {
    public static void saveImage(Context context, IllustDTO illust) {
        if (TextUtils.isEmpty(PixiuApplication.getData().getPathUri())) {
            Intent intent = new Intent(context, SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            YXToast.warning("请设置图片保存目录");
            return;
        }
        if (illust.isSingle()) {
            YXToast.info("正在保存...");
            Glide.with(YUtil.getInstance().getContext()).asBitmap().load(illust.getOriginalList().get(0)).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    String fileName = illust.getId() + ".png";
                    DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(PixiuApplication.getData().getPathUri()));
                    Uri uri;
                    if (documentFile.findFile(fileName) == null) {
                        uri = documentFile.createFile("image/*", illust.getId() + ".png").getUri();
                    } else {
                        uri = documentFile.findFile(fileName).getUri();
                    }
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        context.getContentResolver().openOutputStream(uri).write(byteArrayOutputStream.toByteArray());
                        String pathString = URLDecoder.decode(String.valueOf(PixiuApplication.getData().getPathUri()), "UTF-8");
                        pathString = pathString.replace("content://com.android.externalstorage.documents/tree/primary:", "");
                        XToast.success(context, "图片保存在: " + pathString + "/" + illust.getId() + ".png").show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    YXToast.error("图片保存失败");
                }
            });
        } else {
            ArrayList<String> pageList = new ArrayList<>();
            for (int i = 0; i < illust.getPageCount(); i++) {
                pageList.add(String.valueOf(i));
            }
            MaterialDialog pageDialog = new MaterialDialog.Builder(context)
                    .title("请选择分页")
                    .items(pageList.toArray(new String[]{}))
                    .itemsCallbackMultiChoice(
                            new Integer[]{},
                            (pagedialog, which, pageText) -> {
                                pagedialog.cancel();
                                if (which.length == 0) {
                                    return true;
                                }
                                YXToast.info("正在保存 " + which.length + " 个文件...");
                                for (int i = 0; i < which.length; i++) {
                                    int fileIndex = i;
                                    Glide.with(YUtil.getInstance().getContext()).asBitmap().load(illust.getOriginalList().get(i)).into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            synchronized (this) {
                                                String fileName = illust.getId() + "_" + fileIndex + ".png";
                                                DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(PixiuApplication.getData().getPathUri()));
                                                Uri uri;
                                                if (documentFile.findFile(fileName) == null) {
                                                    uri = documentFile.createFile("image/*", fileName).getUri();
                                                } else {
                                                    uri = documentFile.findFile(fileName).getUri();
                                                }
                                                try {
                                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                    resource.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                    context.getContentResolver().openOutputStream(uri).write(byteArrayOutputStream.toByteArray());
                                                    String pathString = URLDecoder.decode(String.valueOf(PixiuApplication.getData().getPathUri()), "UTF-8");
                                                    pathString = pathString.replace("content://com.android.externalstorage.documents/tree/primary:", "");
                                                    XToast.success(context, "图片保存在: " + pathString + "/" + fileName).show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            YXToast.error("图片保存失败");
                                        }
                                    });
                                }
                                return true;
                            })
                    .positiveText("保存")
                    .negativeText("取消")
                    .build();
            pageDialog.show();
        }
    }
}
