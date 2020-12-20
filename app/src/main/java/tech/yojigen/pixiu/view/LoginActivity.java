package tech.yojigen.pixiu.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import tech.yojigen.pixiu.R;
import tech.yojigen.pixiu.viewmodel.LoginViewModel;
import tech.yojigen.pixiu.viewmodel.RouterViewModel;

public class LoginActivity extends AppCompatActivity {
    LoginViewModel viewModel;
    MaterialEditText et_username, et_password;
    Button btn_login, btn_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtils.initStatusBarStyle(this, false);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        initView();
    }

    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_regist = findViewById(R.id.btn_regist);
    }

    private void initViewEvent() {
        btn_login.setOnClickListener(v -> {
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();
            username = username.trim().toLowerCase();
            viewModel.login(username, password);
        });
    }
}