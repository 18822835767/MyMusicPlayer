package view;

import androidx.appcompat.app.AppCompatActivity;
import contract.LoginContract;
import entity.User;
import presenter.LoginPresenterImpl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymusicplayer.R;

public class LoginActivity extends AppCompatActivity implements LoginContract.OnLoginView {

    private User mUser;//记录登陆的用户
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private LoginContract.LoginPresenter mLoginPresenter;
    private ProgressDialog mProgressDialog;
    
    public static final String USER = "user";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initData();
        initEvent();
    }

    private void initData(){
        mLoginPresenter = new LoginPresenterImpl(this);
        
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText)findViewById(R.id.password);
        mLogin = (Button)findViewById(R.id.login);
        mProgressDialog = new ProgressDialog(LoginActivity.this);
    }
    
    private void initEvent(){
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.login(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });
    }
    
    @Override
    public void showSuccess(User user) {
        this.mUser = user;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();

                //跳转到音乐首页
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(USER,user);
                startActivity(intent);
                
                finish();
            }
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        });
    }
}
