package tech.yojigen.pixiu.app;

import android.app.Application;
import android.graphics.YuvImage;

import com.mob.MobSDK;
import com.xuexiang.xui.XUI;

import tech.yojigen.util.YUtil;

public class PixiuApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
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
}
