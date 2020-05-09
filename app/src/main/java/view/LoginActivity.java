package view;

import androidx.appcompat.app.AppCompatActivity;
import contract.LoginContract;
import entity.User;
import presenter.LoginPresenterImpl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymusicplayer.R;

public class LoginActivity extends AppCompatActivity implements LoginContract.OnLoginView {

    private User user;//记录登陆的用户
    private EditText username;
    private EditText password;
    private Button login;
    private LoginContract.LoginPresenter loginPresenter;
    private ProgressDialog progressDialog;
    
    public static final String USER = "user";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenter = new LoginPresenterImpl(this);
        initView();
        initEvent();
    }

    private void initView(){
        username = (EditText) findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        progressDialog = new ProgressDialog(LoginActivity.this);
    }
    
    private void initEvent(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.login(username.getText().toString(),password.getText().toString());
            }
        });
    }
    
    @Override
    public void showSuccess(User user) {
        this.user = user;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();

                //跳转到音乐首页
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
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
                errorDialog.setMessage("请检查设备是否联网");
                errorDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                });
                errorDialog.show();
            }
        });
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage("Logining...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }
}
