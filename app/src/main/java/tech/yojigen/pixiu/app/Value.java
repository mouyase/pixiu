package tech.yojigen.pixiu.app;

import android.os.Build;

import tech.yojigen.pixiu.BuildConfig;

public class Value {
    public static final String APP_VERSION = BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")";

    public static final String SETTING_ACCESS_TOKEN = "access_token";
    public static final String SETTING_REFRESH_TOKEN = "refresh_token";
    public static final String SETTING_USERNAME = "username";
    public static final String SETTING_PASSWORD = "password";
    public static final String SETTING_ACCOUNT = "SETTING_ACCOUNT";
    public static final String SETTING_NETWORK_MODE = "SETTING_NETWORK_MODE";
    public static final String SETTING_PATH_URI = "SETTING_PATH_URI";
    public static final String SETTING_SAFE_MODE = "SETTING_SAFE_MODE";
    public static final String SETTING_CDN_MODE = "SETTING_CDN_MODE";
    public static final String SETTING_LAST_VERSION = "SETTING_LAST_VERSION";

    public static final String PIXIV_APP_VERSION = "5.0.200";
    public static final String PIXIV_ACCEPT_LANGUAGE = "zh_CN";
    public static final String PIXIV_APP_OS = "Android";
    public static final String PIXIV_APP_OS_VERSION = Build.VERSION.RELEASE;
    public static final String PIXIV_USER_AGENT = "PixivAndroidApp/" + PIXIV_APP_VERSION + " (Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + ")";
    public static final String PIXIV_REFERER = "https://www.pixiv.net";

    public static final String PIXIV_BASE_HASH = "28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c";
    public static final String PIXIV_CLIENT_ID = "MOBrBDS8blbauoSck0ZfDbtuzpyT";
    public static final String PIXIV_CLIENT_SECRET = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj";

    public static final String MODE_PROXY_URL_OAUTH = "https://oauth.secure.128512.xyz/auth/token";
    public static final String MODE_PROXY_URL_API = "https://app-api.128512.xyz";
    public static final String MODE_PROXY_URL_ACCOUNT = "https://accounts.128512.xyz";

    public static final String MODE_NORMAL_URL_OAUTH = "https://oauth.secure.pixiv.net/auth/token";
    public static final String MODE_NORMAL_URL_API = "https://app-api.pixiv.net";
    public static final String MODE_NORMAL_URL_ACCOUNT = "https://accounts.pixiv.net";

    public static String URL_OAUTH = "https://oauth.secure.pixiv.net/auth/token";
    public static String URL_API = "https://app-api.pixiv.net";
    public static String URL_ACCOUNT = "https://accounts.pixiv.net/api/provisional-accounts/create";
}
