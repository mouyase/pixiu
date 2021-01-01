package tech.yojigen.pixiu.app;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.mob.MobSDK;
import com.tencent.bugly.Bugly;
import com.xuexiang.xui.XUI;

import java.util.HashMap;

import tech.yojigen.pixiu.dto.UserAccountDTO;
import tech.yojigen.pixiu.network.PixivClient;
import tech.yojigen.util.YSetting;
import tech.yojigen.util.YUtil;

public class PixiuApplication extends Application {
    private static Data data = new Data();

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
        initData();
    }

    private void initApplication() {
        XUI.init(this); //初始化UI框架

        //判断是否为Debug版本
        ApplicationInfo applicationInfo = getApplicationInfo();
        if ((applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            Bugly.init(getApplicationContext(), "0b3fb1af22", true);
            XUI.debug(true);
        } else {
            Bugly.init(getApplicationContext(), "0b3fb1af22", false);
            XUI.debug(false);
        }
//        Beta.autoCheckUpgrade = true;

//        XHttpSDK.setBaseUrl(SettingSPUtils.getInstance().getApiURL());  //设置网络请求的基础地址

        MobSDK.init(this);//初始化MobSDK
        YUtil.getInstance().init(this);
//        PixivClient.getInstance().changeMode(PixivClient.MODE_PROXY);
    }

    private void initData() {
        UserAccountDTO userAccountDTO = YSetting.getObject(Value.SETTING_ACCOUNT, UserAccountDTO.class);
        if (userAccountDTO != null) {
            data.setUserAccount(userAccountDTO);
            data.setAccessToken(userAccountDTO.getAccessToken());
            data.setRefreshToken(userAccountDTO.getRefreshToken());
            data.setDeviceToken(userAccountDTO.getDeviceToken());
            data.setUser(userAccountDTO.getUser());
        }
        data.setFavouriteMap(new HashMap<>());
        data.setPathUri(YSetting.get(Value.SETTING_PATH_URI, ""));
//        if (!TextUtils.isEmpty(data.getPathUri())) {
//            getContentResolver().takePersistableUriPermission(Uri.parse(data.getPathUri()),
//                    Intent.FLAG_GRANT_READ_URI_PERMISSION
//                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        }
        data.setNetworkMode(YSetting.get(Value.SETTING_NETWORK_MODE, PixivClient.MODE_NO_SNI));
        data.setSafeMode(YSetting.get(Value.SETTING_SAFE_MODE, true));
    }

    public static Data getData() {
        return data;
    }
}
