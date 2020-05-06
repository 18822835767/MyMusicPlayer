package util;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

import util.HttpCallbackListener;

/**
 * 发送网络请求.
 * */
public class HttpUrlConnection {
    private static String TAG = "HttpUrlConnection";

    public static void sendHttpUrlConnection(final String requestUrl, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    listener.onStart();
                    
                    URL url = new URL(requestUrl);
                    connection = (HttpURLConnection)url.openConnection();
                    
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(false);
                    connection.setDoInput(true);
                    
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    
                    connection.connect();
                    
                    //获取响应状态
                    int responseCode = connection.getResponseCode();
                    
                    if(HttpURLConnection.HTTP_OK == responseCode){
                        listener.onSuccess();
                    }else{
                        listener.onFail();
                    }
                }catch (Exception e){
                    listener.onError();
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                    
                    listener.onFinish();
                }
            }
        }).start();
    }
    
}
