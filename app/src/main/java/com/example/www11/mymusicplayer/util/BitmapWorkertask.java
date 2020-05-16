package com.example.www11.mymusicplayer.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapWorkertask extends AsyncTask<String, Void, BitmapDrawable> {
    private ImageCallback mImageCallback;//回调接口
    private int mRequireWidth;//需要下载的图片的宽度
    private int mRequireHeight;//需要下载的图片的高度

    public BitmapWorkertask(int requireWidth, int requireHeight, ImageCallback imageCallback) {
        mImageCallback = imageCallback;
        mRequireWidth = requireWidth;
        mRequireHeight = requireHeight;
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
            bitmap = decodeBitmapFromStream(conn.getInputStream(), mRequireWidth, mRequireHeight);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }

    /**
     * 根据图片的实际大小以及所需要的大小计算图片的比率.
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int requireWidth,
                                      int requireHeight) {
        int width = options.outWidth;//图片本来的宽度
        int height = options.outHeight;//图片本来的高度
        int inSampleSize = 1;
        if (height > requireHeight || width > requireWidth) {
            int heightRatio = Math.round((float) height / (float) requireHeight);
            int widthRatio = Math.round((float) width / (float) requireWidth);
            //返回小的比率，确保下载到的图片大小 > 需要的图片大小
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        return inSampleSize;
    }

    /**
     * 返回经压缩后的图片.
     */
    private Bitmap decodeBitmapFromStream(InputStream is, int requireWidth,
                                          int requireHeight) throws IOException {
        byte[] bytes = getInputStreamBytes(is);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = calculateInSampleSize(options, requireWidth, requireHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * 通过InpustStream得到byte[]数组
     */
    private byte[] getInputStreamBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        baos.flush();
        return baos.toByteArray();
    }


    @FunctionalInterface
    public interface ImageCallback {
        void getDrawable(Drawable drawable);
    }

}