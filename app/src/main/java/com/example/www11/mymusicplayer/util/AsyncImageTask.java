package com.example.www11.mymusicplayer.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 可以用于下载单个图片.
 * */
public class AsyncImageTask extends AsyncTask<String, Void, BitmapDrawable> {
    /**
     * 回调接口
     * */
    private ImageCallback mImageCallback;

    public AsyncImageTask(ImageCallback imageCallback) {
        mImageCallback = imageCallback;
    }

    /**
     * 后台任务，加载图片.
     */
    @Override
    protected BitmapDrawable doInBackground(String... params) {
        String mImageUrl = params[0];//图片url
        //在后台开始下载图片
        Bitmap bitmap = downloadBitmap(mImageUrl);
        return new BitmapDrawable(ApplicationContext.getContext().getResources(),
                bitmap);
    }

    /**
     * 回调到调用方设置图片.
     */
    @Override
    protected void onPostExecute(BitmapDrawable drawable) {
        mImageCallback.getDrawable(drawable);
    }

    /**
     * 加载图片的逻辑.
     */
    private Bitmap downloadBitmap(String imageUrl) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(imageUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10 * 1000);
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }
    
    @FunctionalInterface
    public interface ImageCallback {
        void getDrawable(Drawable drawable);
    }

}