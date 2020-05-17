package com.example.www11.mymusicplayer.view;

import androidx.appcompat.app.AppCompatActivity;
import com.example.www11.mymusicplayer.contract.LoginContract;
import com.example.www11.mymusicplayer.entity.User;
import com.example.www11.mymusicplayer.presenter.LoginPresenterImpl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymusicplayer.R;

import static com.example.www11.mymusicplayer.util.Constants.LoginConstant.USER;

public class LoginActivity extends AppCompatActivity implements LoginContract.OnLoginView {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private LoginContract.LoginPresenter mLoginPresenter;
    private ProgressDialog mProgressDialog;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initData();
        initEvent();
    }

    private void initData(){
        mLoginPresenter = new LoginPresenterImpl(this);
        
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        mProgressDialog = new ProgressDialog(LoginActivity.this);
    }
    
    private void initEvent(){
        mLogin.setOnClickListener(v -> mLoginPresenter.login(mUsername.getText().toString(), 
                mPassword.getText().toString()));
    }
    
    @Override
    public void showSuccess(User user) {
        //记录登陆的用户
        runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();

            //todo Activity的启动方式想想有没有更优雅的写法，降低activity之间的耦合
            //跳转到音乐首页
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(USER,user);
            startActivity(intent);
            
            //销毁当前活动
            finish();
        });
    }

    @Override
    public void showFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(LoginActivity.this);
                errorDialog.setTitle("错误");
                errorDialog.setMessage("请求错误");
                errorDialog.setPositiveButton("OK", (dialog, which) -> {});
                errorDialog.show();
            }
        });
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.setMessage("Logining...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        });
    }

    @Override
    public void hideLoading() {
        //todo 尽量不要直接就runOnUiThread，可以考虑设计一个线程调度工具。
        // 了解一下Handler
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        });
    }
}
