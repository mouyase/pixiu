package tech.yojigen.pixiu.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.xuexiang.xui.utils.StatusBarUtils;

import tech.yojigen.pixiu.databinding.ActivityLoginBinding;
import tech.yojigen.pixiu.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    LoginViewModel viewModel;
    ActivityLoginBinding viewBinding;

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
    }

    protected void initView() {
        StatusBarUtils.initStatusBarStyle(this, false);
    }


    protected void initViewEvent() {
        viewBinding.btnLogin.setOnClickListener(v -> {
            String username = viewBinding.etUsername.getText().toString();
            String password = viewBinding.etPassword.getText().toString();
            viewModel.login(username, password);
        });
    }
}