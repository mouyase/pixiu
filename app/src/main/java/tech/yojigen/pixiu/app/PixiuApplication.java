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
//        MobSDK.submitPolicyGrantResult(true, new OperationCallback<Void>() {
//            @Override
//            public void onComplete(Void data) {
//                System.out.println("隐私协议授权结果提交: 成功");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                System.err.println("隐私协议授权结果提交: 失败");
//            }
//        });
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
}
