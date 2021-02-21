package tech.yojigen.pixiu.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import tech.yojigen.pixiu.app.PixivOAuth;
import tech.yojigen.pixiu.databinding.ActivityLoginBinding;
import tech.yojigen.pixiu.viewmodel.LoginViewModel;
import tech.yojigen.util.YXToast;

public class LoginActivity extends AppCompatActivity {
    LoginViewModel viewModel;
    ActivityLoginBinding viewBinding;
    MaterialDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initView();
        initViewEvent();
        initViewModel();
        route();
    }

    void route() {
        Uri uri = getIntent().getData();
        if (uri != null && !TextUtils.isEmpty(uri.getQueryParameter("code"))) {
            mDialog = new MaterialDialog.Builder(this)
                    .content("登录中，请稍后...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .cancelable(false)
                    .show();
            viewModel.newLogin(uri.getQueryParameter("code"));
        }
    }

    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getOnLoginSuccess().observe(this, o -> {
            YXToast.success("登陆成功");
            mDialog.cancel();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        viewModel.getOnLoginFailed().observe(this, o -> {
            mDialog.cancel();
            YXToast.error("登陆失败");
        });
        viewModel.getOnRegistFailed().observe(this, o -> {
            mDialog.cancel();
            YXToast.error("账号创建失败");
        });
    }

    protected void initView() {
        StatusBarUtils.initStatusBarStyle(this, false);
    }


    protected void initViewEvent() {
//        viewBinding.etPassword.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_GO) {
//                String username = viewBinding.etUsername.getText().toString();
//                String password = viewBinding.etPassword.getText().toString();
//                viewModel.login(username, password);
//                mDialog = new MaterialDialog.Builder(this)
//                        .content("登录中，请稍后...")
//                        .progress(true, 0)
//                        .progressIndeterminateStyle(false)
//                        .cancelable(false)
//                        .show();
//            }
//            return false;
//        });
//        viewBinding.btnLogin.setOnClickListener(v -> {
//            String username = viewBinding.etUsername.getText().toString();
//            String password = viewBinding.etPassword.getText().toString();
//            viewModel.login(username, password);
//            mDialog = new MaterialDialog.Builder(this)
//                    .content("登录中，请稍后...")
//                    .progress(true, 0)
//                    .progressIndeterminateStyle(false)
//                    .cancelable(false)
//                    .show();
//        });
//        viewBinding.btnRegist.setOnClickListener(v -> {
////            String username = viewBinding.etUsername.getText().toString();
////            String password = viewBinding.etPassword.getText().toString();
////            viewModel.login(username, password);
////            loginDialog = new MaterialDialog.Builder(this)
////                    .content("登录中，请稍后...")
////                    .progress(true, 0)
////                    .progressIndeterminateStyle(false)
////                    .cancelable(false)
////                    .show();
//            new MaterialDialog.Builder(this)
//                    .title("创建账号")
//                    .inputType(InputType.TYPE_CLASS_TEXT)
//                    .input("请输入昵称", "", (dialog, input) -> {
//                    })
//                    .positiveText("创建")
//                    .onPositive((dialog, which) -> {
//                        viewModel.regist(dialog.getInputEditText().getText().toString());
//                        mDialog = new MaterialDialog.Builder(this)
//                                .content("注册中，请稍后...")
//                                .progress(true, 0)
//                                .progressIndeterminateStyle(false)
//                                .cancelable(false)
//                                .show();
//                    })
//                    .show();
//        });
        viewBinding.btnNewLogin.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://app-api.pixiv.net/web/v1/login?code_challenge=" +
                    PixivOAuth.getInstance().getChallenge() +
                    "&code_challenge_method=S256&client=pixiv-android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            finish();
        });
        viewBinding.btnNewRegist.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://app-api.pixiv.net/web/v1/login?code_challenge=" +
                    PixivOAuth.getInstance().getChallenge() +
                    "&code_challenge_method=S256&client=pixiv-android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            finish();
        });
    }
}