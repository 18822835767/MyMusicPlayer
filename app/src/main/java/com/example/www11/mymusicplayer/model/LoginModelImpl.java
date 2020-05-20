package com.example.www11.mymusicplayer.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.www11.mymusicplayer.contract.LoginContract;
import com.example.www11.mymusicplayer.entity.User;
import com.example.www11.mymusicplayer.util.HttpCallbackListener;
import com.example.www11.mymusicplayer.util.HttpUrlConnection;

import static com.example.www11.mymusicplayer.util.Constants.URLConstant.LOGIN_URL;

/**
 * LoginModel的实现类.
 */
public class LoginModelImpl implements LoginContract.LoginModel {
    private User mUser;//登陆的用户

    @Override
    public void login(final LoginContract.OnLoginListener listener, String username, String password) {
        HttpUrlConnection.sendHttpUrlConnection(String.format(LOGIN_URL,username,password),
                new HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                try {
                    handleJson(dataMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onLoginSuccess(mUser);
            }

            @Override
            public void onFail() {
                listener.onLoginFail();
            }

            @Override
            public void onError(String errorMsg) {
                listener.onLoginError(errorMsg);
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

    @Override
    public void handleJson(String dataMessage) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataMessage);

        JSONObject accountJson = jsonObject.getJSONObject("account");
        long id = accountJson.getLong("id");

        JSONObject profileJson = jsonObject.getJSONObject("profile");
        String nickName = profileJson.getString("nickname");

        mUser = new User(id,nickName);
    }
    
}
