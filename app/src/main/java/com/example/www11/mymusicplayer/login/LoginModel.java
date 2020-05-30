package com.example.www11.mymusicplayer.login;

import com.example.www11.mymusicplayer.entity.User;

import org.json.JSONException;

/**
 * LoginModel接口.
 */
public interface LoginModel {
    void login(OnListener listener, String username, String password);
    void handleJson(String dataMessage) throws JSONException;
    
    /**
     * 这是将Model请求结果反馈给Presenter的Callback接口.
     * Presenter要去实现这个接口.
     */
    interface OnListener {
        /**
         * 登陆成功.
         */
        void onLoginSuccess(User user);

        /**
         * 登陆失败.
         */
        void onLoginFail();

        /**
         * '
         * 登陆错误(断网...).
         */
        void onLoginError(String errorMsg);

        /**
         * Presenter做开始请求时的业务处理.
         */
        void onStart();

        /**
         * Presenter做结束请求时的业务处理.
         */
        void onFinish();
    }
}
