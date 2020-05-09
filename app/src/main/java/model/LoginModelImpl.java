package model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import contract.LoginContract;
import entity.User;
import util.HttpCallbackListener;
import util.HttpUrlConnection;

/**
 * LoginModel的实现类.
 */
public class LoginModelImpl implements LoginContract.LoginModel {
    private String loginUrl = "http://182.254.170.97:3000/login/cellphone?phone=";
    private User user;//登陆的用户

    @Override
    public void login(final LoginContract.OnLoginListener listener, String username, String password) {
        HttpUrlConnection.sendHttpUrlConnection(loginUrl + username + "&password=" +
                password, new HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                try {
                    JSONObject jsonObject = new JSONObject(dataMessage);
                    
                    JSONObject accountJson = jsonObject.getJSONObject("account");
                    int id = accountJson.getInt("id");
                    
                    JSONObject profileJson = jsonObject.getJSONObject("profile");
                    String nickName = profileJson.getString("nickname");

                    user = new User(id,nickName);
                    listener.onLoginSuccess(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
