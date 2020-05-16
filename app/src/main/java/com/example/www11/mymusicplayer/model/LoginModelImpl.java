package com.example.www11.mymusicplayer.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.www11.mymusicplayer.contract.LoginContract;
import com.example.www11.mymusicplayer.entity.User;
import com.example.www11.mymusicplayer.util.HttpCallbackListener;
import com.example.www11.mymusicplayer.util.HttpUrlConnection;

/**
 * LoginModel的实现类.
 */
public class LoginModelImpl implements LoginContract.LoginModel {
    //todo 这里所有的url的host都是一样的，如果以后更换服务器ip，是否得在代码里每一处url都去改host
    // 可以考虑怎么解决。
    // 各个url分散在不同的ModelImpl里，也不是很好管理，想想有没有什么办法解决
    private static final String LOGIN_URL = "http://182.254.170.97:3000/login/cellphone?phone=";
    private User mUser;//登陆的用户

    @Override
    public void login(final LoginContract.OnLoginListener listener, String username, String password) {
        //todo 直接这样子参数拼接在使用上不方便，维护起来也有难度
        HttpUrlConnection.sendHttpUrlConnection(LOGIN_URL + username + "&password=" +
                password, new HttpCallbackListener() {
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
