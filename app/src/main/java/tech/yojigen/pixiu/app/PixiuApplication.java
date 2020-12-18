package tech.yojigen.pixiu.app;

import android.app.Application;
import android.util.Log;

import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xui.XUI;

public class PixiuApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    void initApplication() {
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
        XHttpSDK.init(this);   //初始化网络请求框架，必须首先执行
        XHttpSDK.debug("XHttp");  //需要调试的时候执行
//        XHttpSDK.setBaseUrl(SettingSPUtils.getInstance().getApiURL());  //设置网络请求的基础地址

        MobSDK.init(this);//初始化MobSDK
        MobSDK.submitPolicyGrantResult(true, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                System.out.println("隐私协议授权结果提交: 成功");
            }

            @Override
            public void onFailure(Throwable t) {
                System.err.println("隐私协议授权结果提交: 失败");
            }
        });
        MobPush.getRegistrationId(id -> System.out.println("推送ID: " + id));
    }
}
