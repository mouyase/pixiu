package tech.yojigen.pixiu.network;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import tech.yojigen.pixiu.BuildConfig;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.util.YDigest;

public class PixivClient {
    private static PixivClient mPixivClient = new PixivClient();

    public static PixivClient getInstance() {
        return mPixivClient;
    }

    public OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(600, TimeUnit.SECONDS);
        builder.writeTimeout(600, TimeUnit.SECONDS);
        Interceptor pixivInterceptor = chain -> {
            String pixivTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.CHINA).format(new Date());
            String pixivHash = String.valueOf(YDigest.MD5(pixivTime + Value.PIXIV_BASE_HASH));
            Request request = chain.request().newBuilder()
                    .addHeader("User-Agent", Value.PIXIV_USER_AGENT)
                    .addHeader("Accept-Language", Value.PIXIV_ACCEPT_LANGUAGE)
                    .addHeader("App-OS", Value.PIXIV_APP_OS)
                    .addHeader("App-OS-Version", Value.PIXIV_APP_OS_VERSION)
                    .addHeader("App-Version", Value.PIXIV_APP_VERSION)
                    .addHeader("Referer", Value.PIXIV_REFERER)
                    .addHeader("X-Client-Time", pixivTime)
                    .addHeader("X-Client-Hash", pixivHash)
                    .build();
            return chain.proceed(request);
        };
        Interceptor tokenInterceptor = chain -> {
            Request request = chain.request();
            if (request.url().toString().contains("https://oauth.secure.128512.xyz/auth/token")) {
                return chain.proceed(request);
            }
            Response response = chain.proceed(request);
            BufferedSource bufferedSource = response.body().source();
            bufferedSource.request(Long.MAX_VALUE);
            Buffer buffer = bufferedSource.getBuffer();
            String bodyString = buffer.clone().readString(StandardCharsets.UTF_8);
            if (bodyString.contains("Error occurred at the OAuth process.")) {
                System.out.println("需要刷token，以后再说");
            }
            return response;
        };
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
//        builder.addInterceptor(accessInterceptor);
        builder.addNetworkInterceptor(pixivInterceptor);
        builder.addNetworkInterceptor(httpLoggingInterceptor);
        return builder.build();
    }

    public void post(String url, PixivForm pixivForm, PixivCallback pixivCallback) {
        Request request = new Request.Builder()
                .post(pixivForm.getForm())
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
                BufferedSource bufferedSource = response.body().source();
                bufferedSource.request(Long.MAX_VALUE);
                Buffer buffer = bufferedSource.getBuffer();
                String bodyString = buffer.clone().readString(StandardCharsets.UTF_8);
                pixivCallback.onResponse(bodyString);
            }
        });
    }
}
