package util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import entity.User;


/**
 * 发送网络请求.
 * */
public class HttpUrlConnection {
    private static String TAG = "HttpUrlConnection";
    private static final int UPDATE_IMAGE = 0;
    
    public static void sendHttpUrlConnection(final String requestUrl, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                
                InputStream inputStream = null;
                ByteArrayOutputStream byteArrayOutputStream = null;
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
                        inputStream = connection.getInputStream();
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        int readLength;
                        byte[] bytes = new byte[1024];//用于存放每次读取的数据
                        while((readLength = inputStream.read(bytes)) != -1){
                            byteArrayOutputStream.write(bytes,0,readLength);
                        }
                        String result = byteArrayOutputStream.toString();
                        
                        listener.onSuccess(result);
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
