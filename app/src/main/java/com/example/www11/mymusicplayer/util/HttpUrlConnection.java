package com.example.www11.mymusicplayer.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 发送网络请求.
 * */
public class HttpUrlConnection {
    public static void sendHttpUrlConnection(String requestUrl, final HttpCallbackListener listener){
        ThreadPool.getThreadPool().execute(() -> {
            InputStream inputStream;
            ByteArrayOutputStream byteArrayOutputStream;
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

                    //判断提交给服务器的数据是否正确
                    if(handleCode(result) == 200){
                        listener.onSuccess(result);
                    }else{
                        listener.onFail();
                    }
                    
                }else{
                    listener.onFail();
                }
            }catch (Exception e){
                listener.onError(e.getMessage());
                e.printStackTrace();
            }finally {
                if(connection != null){
                    connection.disconnect();
                }
                
                listener.onFinish();
            }
        });
    }

    private static int handleCode(String dataMessage) throws JSONException {
        return new JSONObject(dataMessage).getInt("code");
    }
}
