package model.network;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUrlConnection {
    private static String TAG = "HttpUrlConnection";

    public static void sendHttpUrlConnection(final String requestUrl, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(requestUrl);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    
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
                    listener.onError(e.getMessage());
                }
            }
        }).start();
    }
}
