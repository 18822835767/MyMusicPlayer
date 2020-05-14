package model;

import org.json.JSONException;
import org.json.JSONObject;

import contract.LoginContract;
import entity.User;
import util.HttpCallbackListener;
import util.HttpUrlConnection;

/**
 * LoginModel的实现类.
 */
public class LoginModelImpl implements LoginContract.LoginModel {
    private static final String LOGIN_URL = "http://182.254.170.97:3000/login/cellphone?phone=";
    private User mUser;//登陆的用户

    @Override
    public void login(final LoginContract.OnLoginListener listener, String username, String password) {
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
