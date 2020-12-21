package tech.yojigen.pixiu.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import tech.yojigen.pixiu.databinding.ActivityLoginBinding;
import tech.yojigen.pixiu.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    LoginViewModel viewModel;
    ActivityLoginBinding viewBinding;
    MaterialDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initViewModel();
        initView();
        initViewEvent();
    }

    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getOnLoginSuccess().observe(this, o -> {
            startActivity(new Intent(this, MainActivity.class));
            loginDialog.cancel();
            finish();
        });
    }

    protected void initView() {
        StatusBarUtils.initStatusBarStyle(this, false);
    }


    protected void initViewEvent() {
        viewBinding.btnLogin.setOnClickListener(v -> {
            String username = viewBinding.etUsername.getText().toString();
            String password = viewBinding.etPassword.getText().toString();
            viewModel.login(username, password);
            loginDialog = new MaterialDialog.Builder(this)
                    .content("登录中，请稍后...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .cancelable(false)
                    .show();
        });
    }
}