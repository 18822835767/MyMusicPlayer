package util;

public interface HttpCallbackListener {
    /**
     * 请求成功.
     * */
    void onSuccess(String dataMessage);

    /**
     * 请求失败.
     * */
    void onFail();

    /**
     * 请求错误.
     * */
    void onError();
    
    /**
     * 开始请求业务.
     * */
    void onStart();
    
    /**
     * 结束请求业务.
     * */
    void onFinish();
}
