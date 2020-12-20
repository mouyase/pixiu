package tech.yojigen.pixiu.network;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import tech.yojigen.pixiu.BuildConfig;
import tech.yojigen.pixiu.app.Value;
import tech.yojigen.util.YDigest;

public class PixivClient {
    public static OkHttpClient getClient() {
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
            if (request.url().toString().contains("https://oauth.secure.128512.xyz")) {
                return chain.proceed(request);
            }
            Response response = chain.proceed(request);
            return chain.proceed(request);
        };
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
//        builder.addInterceptor(accessInterceptor);
        builder.addNetworkInterceptor(pixivInterceptor);
        builder.addNetworkInterceptor(httpLoggingInterceptor);
        return builder.build();
    }
}
