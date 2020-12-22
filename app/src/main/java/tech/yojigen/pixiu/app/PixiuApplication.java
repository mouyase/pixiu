package tech.yojigen.pixiu.app;

import android.app.Application;
import android.graphics.YuvImage;

import com.mob.MobSDK;
import com.xuexiang.xui.XUI;

import tech.yojigen.pixiu.dto.UserAccountDTO;
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

    void initApplication() {
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
//        XHttpSDK.setBaseUrl(SettingSPUtils.getInstance().getApiURL());  //设置网络请求的基础地址

        MobSDK.init(this);//初始化MobSDK
        YUtil.getInstance().init(this);
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
    }

    public static Data getData() {
        return data;
    }

    public static void setData(UserAccountDTO userAccountDTO) {
        if (userAccountDTO != null) {
            YSetting.setObject(Value.SETTING_ACCOUNT, userAccountDTO);
            data.setUserAccount(userAccountDTO);
            data.setAccessToken(userAccountDTO.getAccessToken());
            data.setRefreshToken(userAccountDTO.getRefreshToken());
            data.setDeviceToken(userAccountDTO.getDeviceToken());
            data.setUser(userAccountDTO.getUser());
        }
    }
}
