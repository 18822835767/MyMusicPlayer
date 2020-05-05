package model;

import java.net.HttpURLConnection;

import util.HttpCallbackListener;
import util.HttpUrlConnection;

/**
 * LoginModel的实现类.
 */
public class LoginModelImpl implements LoginModel {
    private String loginUrl = "http://182.254.170.97:3000/login/cellphone?phone=";

    @Override
    public void login(final OnLoginListener listener, String username, String password) {
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
            public void onError(String errorMsg) {
                listener.onLoginError(errorMsg);
            }
        });
    }
}
