package tech.yojigen.pixiu.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.util.Random;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.RecommendDTO;
import tech.yojigen.pixiu.network.PixivCallback;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.pixiu.network.PixivData;

/**
 * Implementation of App Widget functionality.
 */
public class PixiuWidget extends AppWidgetProvider {
    Gson gson = new Gson();
    Random random = new Random();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
//        updateWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.pixiu_widget);
        PixivData pixivData = new PixivData.Builder()
                .set("filter", "for_android")
                .set("include_ranking_illusts", true)
                .set("include_privacy_policy", true)
                .build();
        PixivClient.getInstance().get(Value.URL_API + "/v1/illust/recommended", pixivData, new PixivCallback() {
            @Override
            public void onFailure() {
            }

            @Override
            public void onResponse(String body) {
                RecommendDTO recommendDTO = gson.fromJson(body, RecommendDTO.class);
                if (recommendDTO.getIllustList().size() > 0) {
                    int randomIndex = random.nextInt(recommendDTO.getIllustList().size() - 1);
                    String url = "pixiu://www.pixiv.net/artworks/" + recommendDTO.getIllustList().get(randomIndex).getId();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                    remoteViews.setOnClickPendingIntent(R.id.image, pendingIntent);
                    Glide.with(context)
                            .asBitmap()
                            .load(recommendDTO.getIllustList().get(randomIndex).getImageUrls().getLarge())
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    remoteViews.setImageViewBitmap(R.id.image, resource);
                                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }
            }
        });
    }
}