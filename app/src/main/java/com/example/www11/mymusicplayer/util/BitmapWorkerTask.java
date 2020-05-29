package com.example.www11.mymusicplayer.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 主要是为了加载ListView中的图片，保证不乱序.
 * */
public class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {
    /**
     * task关联的图片的url.
     * */
    public String mImageUrl;
    
    /**
     * task关联imageView的弱引用.
     * */
    private WeakReference<ImageView> imageViewReference;

    public BitmapWorkerTask(ImageView imageView) {
        imageViewReference = new WeakReference<>(imageView);//构造函数中传入imageView
    }

    @Override
    protected BitmapDrawable doInBackground(String... strings) {
        mImageUrl = strings[0];//图片的url
        //在后台开始下载图片
        Bitmap bitmap = downloadBitmap(mImageUrl);
        BitmapDrawable drawable = new BitmapDrawable(ApplicationContext.getContext().getResources(),
                bitmap);
        ImageMemoryCache.addBitmapToMemory(mImageUrl,drawable);
        //返回得到的下载后的图片
        return drawable;
    }

    /**
     * 根据得到的图片drawableBitmap为ImageView设定drawable.
     */
    @Override
    protected void onPostExecute(BitmapDrawable drawable) {
        imageViewReference.get().setImageDrawable(drawable);
    }
    
    /**
     * 执行图片的下载任务.
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
}