package presenter;

import contract.LoginContract;
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
    private LoginContract.LoginModel loginModel;
    private LoginContract.OnLoginView onLoginView;
    
    public LoginPresenterImpl(LoginContract.OnLoginView onLoginView){
        this.onLoginView = onLoginView;
        loginModel = new LoginModelImpl();
    }

    @Override
    public void login(String username, String password) {
        loginModel.login(this,username,password);
    }
    
    @Override
    public void onLoginSuccess() {
        onLoginView.showSuccess();
    }

    @Override
    public void onLoginFail() {
        onLoginView.showFail();
    }

    @Override
    public void onLoginError() {
        onLoginView.showError();
    }

    @Override
    public void onStart() {
        onLoginView.showLoading();
    }

    @Override
    public void onFinish() {
        onLoginView.hideLoading();
    }


}
