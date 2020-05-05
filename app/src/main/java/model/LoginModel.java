package model;

/**
 * 被LoginPresenter调用.
 * */
public interface LoginModel {
    void login(OnLoginListener listener,String username,String password);
}
