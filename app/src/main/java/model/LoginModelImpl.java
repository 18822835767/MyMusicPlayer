package model;

import java.net.HttpURLConnection;

import contract.LoginContract;
import util.HttpCallbackListener;
import util.HttpUrlConnection;

/**
 * LoginModel的实现类.
 */
public class LoginModelImpl implements LoginContract.LoginModel {
    private String loginUrl = "http://182.254.170.97:3000/login/cellphone?phone=";

    @Override
    public void login(final LoginContract.OnLoginListener listener, String username, String password) {
        HttpUrlConnection.sendHttpUrlConnection(loginUrl + username + "&password=" +
                password, new HttpCallbackListener() {
            @Override
            public void onSuccess() {
                listener.onLoginSuccess();
            }

            @Override
            public void onFail() {
                listener.onLoginFail();
            }

            @Override
            public void onError() {
                listener.onLoginError();
            }

            @Override
            public void onStart() {
                listener.onStart();
            }

            @Override
            public void onFinish() {
                listener.onFinish();
            }
        });
    }
}
