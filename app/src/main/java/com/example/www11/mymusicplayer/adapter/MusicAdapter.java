package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.util.ApplicationContext;
import com.example.www11.mymusicplayer.util.BitmapWorkertask;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 采用imageView和task(BitmapWorkTask2)双关联的方式来避免图片的乱序与闪图.
 * imageView和task关联的方式式采用弱引用进行的，防止回收不了.
 * */
public class MusicAdapter extends ArrayAdapter<Music> {

    private ListView mListView;//adapter所对应的listview
    private Boolean scrolling = false;//listview是否处于滚动状态
    private Bitmap mLoadingBitmap;

    public MusicAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Music> objects) {
        super(context, textViewResourceId, objects);
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_photo);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (mListView == null) {
            mListView = (ListView) parent;
        }

        View view;
        ViewHolder viewHolder;
        Music music = getItem(position);
        String url = null;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.music_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = view.findViewById(R.id.music_image);
            viewHolder.musicName = view.findViewById(R.id.music_name);
            viewHolder.singerName = view.findViewById(R.id.singer_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        //设定歌曲名字和歌手名字
        if (music != null) {
            url = music.getPicUrl();
            viewHolder.musicName.setText(music.getName());
            viewHolder.singerName.setText(music.getSingerName());
        }

        /*
         * 请求图片的设定.
         * 若后台的任务是imageview在请求另外一张图片，则取消任务。
         * 若后台的任务请求的图片刚好和imageview需要的一致，则if下面的不执行.
         * 若该imageview后台无请求任务，cancelPo...返回true,则执行if里的语句.
         * */
        if (cancelPotentialWork(url, viewHolder.image)) {
            //新建请求图片的task，该task含有imageview的弱引用
            BitmapWorkerTask2 task = new BitmapWorkerTask2(viewHolder.image);
            //先给AsyncDrawable关联task的引用，imageview可以通过AsyncDrawable关联到task
            AsyncDrawable asyncDrawable = new AsyncDrawable(getContext().getResources(),
                    mLoadingBitmap, task);//先放入一张空白的图片
            //imageview设定该空白的图片
            viewHolder.image.setImageDrawable(asyncDrawable);
            //task根据url去请求图片
            task.execute(url);
        }
        return view;
    }

    /**
     * 取消其他图片的后台下载任务.
     * <p>
     * 若该imageView正在请求的下载任务和当前需要的图片不一致，则cancel掉该任务，并且返回true.
     * 若该imageView正在请求的下载任务和当前需要的图片一致，则继续该请求任务，返回false.
     * 若该imageView目前无下载任务，则返回true.
     * </p>
     */
    private boolean cancelPotentialWork(String url, ImageView imageView) {
        BitmapWorkerTask2 bitmapWorkerTask2 = getBitmapWorkerTask2(imageView);
        if (bitmapWorkerTask2 != null) {
            String imageUrl = bitmapWorkerTask2.imageUrl;
            //将正在请求的url和需要使用的图片的url进行对比.
            if (imageUrl == null || !imageUrl.equals(url)) {
                //url不一致的情况下，将任务取消
                bitmapWorkerTask2.cancel(true);
            } else {
                //url一致的情况下，任务正常进行
                return false;
            }
        }
        return true;
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
    private BitmapWorkerTask2 getBitmapWorkerTask2(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();//得到imageView当前的drawable
            if (drawable instanceof AsyncDrawable) {//此时正在执行图片请求的任务，有相关联的任务并返回
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask2();
            }
        }
        //没有相关联的任务，imageView没有在执行请求任务，返回null.
        return null;
    }

    /**
     * imageView关联task的媒介.
     * <p>
     *     当imageView正在等待task请求图片的过程中，imageView设定的drawable是这一个，内部实际上是一个空白图片.
     *     当imageView请求图片结束后，内部设定的drawable将不是这个，而是另外的一个BitmapDrawable。
     *     请求图片的过程中,imageView可以通过该AsyncDrawable得到实时的关联的task.
     * </p>
     */
    class AsyncDrawable extends BitmapDrawable {
        //task的弱引用
        private WeakReference<BitmapWorkerTask2> bitmapWorkerTask2WeakReference;

        AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask2 bitmapWorkerTask2) {
            super(res, bitmap);
            bitmapWorkerTask2WeakReference = new WeakReference<>(bitmapWorkerTask2);
        }

        //得到task的引用
        BitmapWorkerTask2 getBitmapWorkerTask2() {
            return bitmapWorkerTask2WeakReference.get();
        }
    }


    class BitmapWorkerTask2 extends AsyncTask<String, Void, BitmapDrawable> {
        String imageUrl;//task关联的图片的url
        private WeakReference<ImageView> imageViewReference;//task关联imageView的弱引用

        BitmapWorkerTask2(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);//构造函数中传入imageView
        }

        @Override
        protected BitmapDrawable doInBackground(String... strings) {
            String mImageUrl = strings[0];//图片的url
            //在后台开始下载图片
            Bitmap bitmap = downloadBitmap(mImageUrl);
            //返回得到的下载后的图片
            return new BitmapDrawable(ApplicationContext.getContext().getResources(),
                    bitmap);
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

    /**
     * 设定listView滚动的时候的状态.
     */
    public void setScrolling(Boolean scrolling) {
        this.scrolling = scrolling;
    }

    static class ViewHolder {
        ImageView image;
        TextView musicName;
        TextView singerName;
    }

}
