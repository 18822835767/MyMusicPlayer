package com.example.www11.mymusicplayer.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.www11.mymusicplayer.adapter.SongListAdapter;

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
    private String mImageUrl;
    
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
        ImageView imageView = getAttachedImageView();
        if(imageView != null && drawable != null ){
            imageView.setImageDrawable(drawable);
        }
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

    /**
     * 取消其他图片的后台下载任务.
     * <p>
     * 若该imageView正在请求的下载任务和当前需要的图片不一致，则cancel掉该任务，并且返回true.
     * 若该imageView正在请求的下载任务和当前需要的图片一致，则继续该请求任务，返回false.
     * 若该imageView目前无下载任务，则返回true.
     * </p>
     */
    public static boolean cancelPotentialWork(String url, ImageView imageView) {
        BitmapWorkerTask task = getBitmapWorkerTask(imageView);
        if (task != null) {
            String imageUrl = task.mImageUrl;
            //将正在请求的url和需要使用的图片的url进行对比.
            if (imageUrl == null || !imageUrl.equals(url)) {
                //url不一致的情况下，将任务取消
                task.cancel(true);
            } else {
                //url一致的情况下，任务正常进行
                return false;
            }
        }
        return true;
    }

    /**
     * 获取当前BitmapWorkerTask所关联的ImageView.
     * */
    private ImageView getAttachedImageView(){
        ImageView imageView = imageViewReference.get();
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if(this == bitmapWorkerTask){
            return imageView;
        }
        return null;
    }
    
    /**
     * 通过imageView得到imageView关联的task.
     * <p>
     * 假如imageView正在请求图片，那么imageView的drawable将会是一个带有空白图片的AsyncDrawable，
     * 然后间接得到iamgeView关联的task,返回其关联的实时的task
     * 假如imageView没有在请求图片，即imageView内部的图片是BitmapDrawable，那么此时该imageView没有
     * 相关的task,返回null.
     * </p>
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();//得到imageView当前的drawable
            if (drawable instanceof AsyncDrawable) {//此时正在执行图片请求的任务，有相关联的任务并返回
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        //没有相关联的任务，imageView没有在执行请求任务，返回null.
        return null;
    }

    /**
     * imageView关联task的媒介.
     * <p>
     * 当imageView正在等待task请求图片的过程中，imageView设定的drawable是这一个，内部实际上是一个空白图片.
     * 当imageView请求图片结束后，内部设定的drawable将不是这个，而是另外的一个BitmapDrawable。
     * 请求图片的过程中,imageView可以通过该AsyncDrawable得到实时的关联的task.
     * </p>
     */
    public static class AsyncDrawable extends BitmapDrawable {
        //task的弱引用
        private WeakReference<BitmapWorkerTask> bitmapWorkerTaskWeakReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskWeakReference = new WeakReference<>(bitmapWorkerTask);
        }

        //得到task的引用
        BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskWeakReference.get();
        }
    }
}