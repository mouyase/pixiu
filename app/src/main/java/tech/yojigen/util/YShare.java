package tech.yojigen.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 处理分享的工具类
 */
public class YShare {
    public static void image(Bitmap bitmap) {
        try {
            String time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.CHINA).format(new Date());
            String fileName = String.valueOf(YDigest.MD5(time + new Random().nextInt()));
            String filePath = YUtil.getInstance().getContext().getExternalCacheDir() + "/.YShare/" + fileName + ".png";
            File file = new File(filePath);
            File path = new File(YUtil.getInstance().getContext().getExternalCacheDir() + "/.YShare/");
            if (!path.exists()) {
                path.mkdir();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            Bitmap newBitmap = obscure(bitmap);
            newBitmap.compress(Bitmap.CompressFormat.PNG, 95, bufferedOutputStream);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 95, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            Uri uri = FileProvider.getUriForFile(YUtil.getInstance().getContext(), YUtil.getInstance().getPackageName() + ".provider", file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("image/*");
            Intent shareIntent = Intent.createChooser(intent, "分享图片到");
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            YUtil.getInstance().getContext().startActivity(shareIntent);
        } catch (IOException e) {
            e.printStackTrace();
            YToast.show("图片分享失败");
        }
    }

    public static void text(String string) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, string);
        intent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(intent, "分享到");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        YUtil.getInstance().getContext().startActivity(shareIntent);
    }

    private static Bitmap obscure(Bitmap bitmap) {
        Random random = new Random();
        int mBitmapWidth = bitmap.getWidth();
        int mBitmapHeight = bitmap.getHeight();
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                int color = bitmap.getPixel(j, i);
                int R, G, B;
                R = Color.red(color);
                G = Color.green(color);
                B = Color.blue(color);
                R = random.nextBoolean() ? R + 1 : R - 1;
                G = random.nextBoolean() ? G + 1 : G - 1;
                B = random.nextBoolean() ? B + 1 : B - 1;
                R = Math.max(R, 0);
                R = Math.min(R, 255);
                G = Math.max(G, 0);
                G = Math.min(G, 255);
                B = Math.max(B, 0);
                B = Math.min(B, 255);
                bitmap.setPixel(j, i, Color.rgb(R, G, B));
            }
        }
        return bitmap;
    }
}
