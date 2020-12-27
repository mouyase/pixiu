package tech.yojigen.pixiu.app;

import android.app.Application;

import com.mob.MobSDK;
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
        XUI.debug(true);  //开启UI框架调试日志
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
        data.setPathUri(YSetting.get(Value.SETTING_PATH_URL, ""));
        data.setNetworkMode(YSetting.get(Value.SETTING_NETWORK_MODE, PixivClient.MODE_NO_SNI));
    }

    public static Data getData() {
        return data;
    }
}
