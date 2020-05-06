package view;

import androidx.appcompat.app.AppCompatActivity;
import presenter.OnLoginView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mymusicplayer.R;

public class LoginActivity extends AppCompatActivity implements OnLoginView {

    private EditText username;
    private EditText password;
    private Button login;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void initView(){
        username = (EditText) findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
    }
    
    @Override
    public void showSuccess() {
        
    }

    @Override
    public void showFail() {

    }

    @Override
    public void showError() {

    }
}
