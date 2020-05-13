package presenter;

import contract.LoginContract;
import entity.User;
import model.LoginModelImpl;

/**
 * 这是一个Presenter的实现类.
 * 
 * <p>
 *     既持有LoginModel的引用，请求访问数据；又持有LoginActivity实现的接口，向LoginActivity反馈结果.
 * </p>
 * 
 * */
public class LoginPresenterImpl implements LoginContract.LoginPresenter, LoginContract.OnLoginListener {
    private LoginContract.LoginModel mLoginModel;
    private LoginContract.OnLoginView mOnLoginView;
    
    public LoginPresenterImpl(LoginContract.OnLoginView onLoginView){
        this.mOnLoginView = onLoginView;
        mLoginModel = new LoginModelImpl();
    }

    @Override
    public void login(String username, String password) {
        mLoginModel.login(this,username,password);
    }
    
    @Override
    public void onLoginSuccess(User user) {
        mOnLoginView.showSuccess(user);
    }

    @Override
    public void onLoginFail() {
        mOnLoginView.showFail();
    }

    @Override
    public void onLoginError() {
        mOnLoginView.showError();
    }

    @Override
    public void onStart() {
        mOnLoginView.showLoading();
    }

    @Override
    public void onFinish() {
        mOnLoginView.hideLoading();
    }


}
