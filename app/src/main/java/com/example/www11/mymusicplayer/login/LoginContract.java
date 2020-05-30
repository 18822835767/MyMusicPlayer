package com.example.www11.mymusicplayer.login;

import com.example.www11.mymusicplayer.entity.User;

/**
 * "登陆"所对应的契约类.
 * */
public interface LoginContract {
    /**
     * LoginPresenter接口，被LoginActivity调用.
     * */
    interface Presenter {
        void login(String username,String password);
    }

    /**
     * LoginPresenter将登陆结果反馈给LoginActivity的View接口.
     * 由LoginActivity去实现这个类.
     * */
    interface OnView {
        /**
         * 登陆成功后LoginActivity做相应的显示.
         * */
        void showSuccess(User user);

        /**
         * 登陆失败后LoginActivity做相应的显示.
         * */
        void showFail();

        /**
         * 登陆出现错误后LoginActivity做相应的提示.
         * */
        void showError(String errorMsg);

        /**
         * LoginView做开始请求业务的UI处理.
         * */
        void showLoading();

        /**
         * LoginView做结束请求业务的UI处理.
         * */
        void hideLoading();
    }
}
