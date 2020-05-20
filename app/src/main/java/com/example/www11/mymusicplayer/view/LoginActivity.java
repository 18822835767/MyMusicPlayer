package com.example.www11.mymusicplayer.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.www11.mymusicplayer.contract.LoginContract;
import com.example.www11.mymusicplayer.entity.User;
import com.example.www11.mymusicplayer.presenter.LoginPresenterImpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.util.Constants;

import java.lang.ref.WeakReference;

import static com.example.www11.mymusicplayer.util.Constants.LoginConstant.ERROR;
import static com.example.www11.mymusicplayer.util.Constants.LoginConstant.FAIL;
import static com.example.www11.mymusicplayer.util.Constants.LoginConstant.HIDE_LOADING;
import static com.example.www11.mymusicplayer.util.Constants.LoginConstant.SHOW_LOADING;
import static com.example.www11.mymusicplayer.util.Constants.LoginConstant.SUCCESS;
import static com.example.www11.mymusicplayer.util.Constants.LoginConstant.USER;

public class LoginActivity extends AppCompatActivity implements LoginContract.OnLoginView {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private LoginContract.LoginPresenter mLoginPresenter;
    private ProgressDialog mProgressDialog;
    private Handler mHandler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initData();
        initEvent();
    }

    private void initData(){
        mLoginPresenter = new LoginPresenterImpl(this);
        mHandler = new MyHandler(LoginActivity.this);
        
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
        Message message = Message.obtain();
        message.what = SUCCESS;
        message.obj = user;
        mHandler.sendMessage(message);
    }

    @Override
    public void showFail() {
        Message message = Message.obtain();
        message.what = FAIL;
        mHandler.sendMessage(message);
    }

    @Override
    public void showError() {
        Message message = Message.obtain();
        message.what = ERROR;
        mHandler.sendMessage(message);
    }

    @Override
    public void showLoading() {
        Message message = Message.obtain();
        message.what = SHOW_LOADING;
        message.obj = mProgressDialog;
        mHandler.sendMessage(message);
    }

    @Override
    public void hideLoading() {
        Message message = Message.obtain();
        message.what = HIDE_LOADING;
        message.obj = mProgressDialog;
        mHandler.sendMessage(message);
    }
    
    private static class MyHandler extends Handler{
        WeakReference<Activity> mWeakActivityRef;

        MyHandler(Activity activity) {
            mWeakActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final Activity activity = mWeakActivityRef.get();
            if(activity != null){
                switch (msg.what){
                    case SUCCESS:
                        Toast.makeText(activity,"登陆成功",Toast.LENGTH_SHORT).show();
                        //启动主活动
                        MainActivity.actionStart(activity,(User)msg.obj);
                        //销毁当前活动
                        activity.finish();
                        break;
                    case FAIL:
                        Toast.makeText(activity,"登陆失败",Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        AlertDialog.Builder errorDialog = new AlertDialog.Builder(activity);
                        errorDialog.setTitle("错误");
                        errorDialog.setMessage("请求错误");
                        errorDialog.setPositiveButton("OK", (dialog, which) -> {});
                        errorDialog.show();
                        break;
                    case SHOW_LOADING:
                        ProgressDialog progressDialog = (ProgressDialog) msg.obj;
                        progressDialog.setMessage("Logining...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        break;
                    case HIDE_LOADING:
                        ((ProgressDialog)msg.obj).dismiss();
                        break;
                    default:
                        break;

                }
            }
        }
    }
}
