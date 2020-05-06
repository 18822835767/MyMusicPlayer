package contract;


public interface LoginContract {
    /**
     * LoginModel接口，被LoginPresenter调用.
     * */
    interface LoginModel {
        void login(OnLoginListener listener, String username, String password);
    }

    /**
     * LoginPresenter接口，被LoginActivity调用.
     * */
    interface LoginPresenter {
        void login(String username,String password);
    }

    /**
     * 这是将LoginModel请求结果反馈给LoginPresenter的Callback接口.
     * LoginPresenter要去实现这个接口.
     * */
    interface OnLoginListener {
        /**
         * 登陆成功.
         * */
        void onLoginSuccess();

        /**
         * 登陆失败.
         * */
        void onLoginFail();

        /**'
         * 登陆错误(断网...).
         * */
        void onLoginError();

        /**
         * Presenter做开始请求时的业务处理.
         * */
        void onStart();

        /**
         * Presenter做结束请求时的业务处理.
         * */
        void onFinish();
    }

    /**
     * LoginPresenter将登陆结果反馈给LoginActivity的View接口.
     * 由LoginActivity去实现这个类.
     * */
    interface OnLoginView {
        /**
         * 登陆成功后LoginActivity做相应的显示.
         * */
        void showSuccess();

        /**
         * 登陆失败后LoginActivity做相应的显示.
         * */
        void showFail();

        /**
         * 登陆错误后LoginActivity做相应的提示.
         * */
        void showError();

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
