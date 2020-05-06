package presenter;

/**
 * LoginPresenter将登陆结果反馈给LoginActivity的View接口.
 * 由LoginActivity去实现这个类.
 * */
public interface OnLoginView {
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
