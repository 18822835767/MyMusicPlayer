package model;

/**
 * 这是将LoginModel请求结果反馈给LoginPresenter的Callback接口.
 * LoginPresenter要去实现这个接口.
 * */
public interface OnLoginListener {
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
    void onLoginError(String errorMsg);
}
