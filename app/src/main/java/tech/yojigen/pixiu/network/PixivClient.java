package tech.yojigen.pixiu.network;


import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import tech.yojigen.pixiu.BuildConfig;
import tech.yojigen.pixiu.app.PixiuApplication;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.pixiu.dto.UserAccountDTO;
import tech.yojigen.util.YDigest;
import tech.yojigen.util.YSetting;

public class PixivClient {
    private static PixivClient mPixivClient = new PixivClient();
    private Gson gson = new Gson();

    public static PixivClient getInstance() {
        return mPixivClient;
    }

    public OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(600, TimeUnit.SECONDS);
        builder.readTimeout(600, TimeUnit.SECONDS);
        builder.writeTimeout(600, TimeUnit.SECONDS);
        Interceptor headerInterceptor = chain -> {
            String pixivTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.CHINA).format(new Date());
            String pixivHash = String.valueOf(YDigest.MD5(pixivTime + Value.PIXIV_BASE_HASH));
            Request request = chain.request().newBuilder()
                    .header("User-Agent", Value.PIXIV_USER_AGENT)
                    .header("Accept-Language", Value.PIXIV_ACCEPT_LANGUAGE)
                    .header("App-OS", Value.PIXIV_APP_OS)
                    .header("App-OS-Version", Value.PIXIV_APP_OS_VERSION)
                    .header("App-Version", Value.PIXIV_APP_VERSION)
                    .header("Referer", Value.PIXIV_REFERER)
                    .header("X-Client-Time", pixivTime)
                    .header("X-Client-Hash", pixivHash)
                    .build();
            return chain.proceed(request);
        };
        Interceptor addTokenInterceptor = chain -> {
            Request request = chain.request();
            if (request.url().toString().contains(Value.URL_API + "/v1/walkthrough/illusts")) {
                return chain.proceed(request);
            }
            if (request.url().toString().contains(Value.URL_OAUTH)) {
                return chain.proceed(request);
            }
            String accessToken = PixiuApplication.getData().getAccessToken();
            if (!TextUtils.isEmpty(accessToken)) {
                request = chain.request().newBuilder()
                        .header("Authorization", "Bearer " + accessToken)
                        .build();
            }
            return chain.proceed(request);
        };
        Interceptor refreshTokenInterceptor = chain -> {
            Request request = chain.request();
            if (request.url().toString().contains(Value.URL_OAUTH)) {
                return chain.proceed(request);
            }
            Response response = chain.proceed(request);
            BufferedSource bufferedSource = response.body().source();
            bufferedSource.request(Long.MAX_VALUE);
            Buffer buffer = bufferedSource.getBuffer();
            String bodyString = buffer.clone().readString(StandardCharsets.UTF_8);
            if (bodyString.contains("Error occurred at the OAuth process.")) {
                synchronized (this) {
                    String refreshToken = PixiuApplication.getData().getRefreshToken();
                    if (!TextUtils.isEmpty(refreshToken)) {
                        PixivData pixivData = new PixivData.Builder()
                                .set("client_id", Value.PIXIV_CLIENT_ID)
                                .set("client_secret", Value.PIXIV_CLIENT_SECRET)
                                .set("grant_type", "refresh_token")
                                .set("refresh_token", refreshToken)
                                .set("device_token", "pixiv")
                                .set("get_secure_url", true)
                                .set("include_policy", false)
                                .build();
                        Request refreshRequest = new Request.Builder().url(Value.URL_OAUTH).post(pixivData.getForm()).build();
                        Response refreshResponse = getClient().newCall(refreshRequest).execute();
                        if (refreshResponse.isSuccessful()) {
                            System.out.println("刷新了token");
                            UserAccountDTO userAccountDTO = gson.fromJson(refreshResponse.body().string(), UserAccountDTO.class);
                            PixiuApplication.setData(userAccountDTO);
                            String accessToken = PixiuApplication.getData().getAccessToken();
                            Request newRequest = request.newBuilder()
                                    .removeHeader("Authorization")
                                    .header("Authorization", "Bearer " + accessToken)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    }
                }
            }
            return response;
        };
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(headerInterceptor);
        builder.addInterceptor(addTokenInterceptor);
        builder.addInterceptor(refreshTokenInterceptor);
        builder.addInterceptor(httpLoggingInterceptor);
        return builder.build();
    }

    public void post(String url, PixivData pixivData, PixivCallback pixivCallback) {
        Request request = new Request.Builder()
                .post(pixivData.getForm())
                .url(url)
                .build();
        Call call = getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pixivCallback.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    BufferedSource bufferedSource = response.body().source();
                    bufferedSource.request(Long.MAX_VALUE);
                    Buffer buffer = bufferedSource.getBuffer();
                    String bodyString = buffer.clone().readString(StandardCharsets.UTF_8);
                    pixivCallback.onResponse(bodyString);
                } else {
                    pixivCallback.onFailure();
                }
            }
        });
    }

    public void get(String url, PixivData pixivData, PixivCallback pixivCallback) {
        if (pixivData != null) {
            url = url + pixivData.getQuery();
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pixivCallback.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    BufferedSource bufferedSource = response.body().source();
                    bufferedSource.request(Long.MAX_VALUE);
                    Buffer buffer = bufferedSource.getBuffer();
                    String bodyString = buffer.clone().readString(StandardCharsets.UTF_8);
                    pixivCallback.onResponse(bodyString);
                } else {
                    pixivCallback.onFailure();
                }
            }
        });
    }
}
