package util;

public interface HttpCallbackListener {
    /**
     * 请求成功.
     * */
    void onSuccess();

    /**
     * 请求失败.
     * */
    void onFail();

    /**
     * 请求错误.
     * */
    void onError(String errorMsg);
}
