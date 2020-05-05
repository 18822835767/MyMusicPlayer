package presenter;

import model.LoginModel;
import model.LoginModelImpl;
import model.OnLoginListener;

/**
 * 这是一个Presenter的实现类.
 * 
 * <p>
 *     既持有LoginModel的引用，请求访问数据；又持有LoginActivity实现的接口，向LoginActivity反馈结果.
 * </p>
 * 
 * */
public class LoginPresenterImpl implements LoginPresenter, OnLoginListener {
    private LoginModel loginModel;
    private OnLoginView onLoginView;
    
    public LoginPresenterImpl(OnLoginView onLoginView){
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
    public void onLoginError(String errorMsg) {
        onLoginView.showError();
    }

    
}
