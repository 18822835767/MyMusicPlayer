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

        if (music != null) {
            url = music.getPicUrl();
            viewHolder.musicName.setText(music.getName());
            viewHolder.singerName.setText(music.getSingerName());
        }
        
        if(cancelPotentialWork(url,viewHolder.image)){
            BitmapWorkerTask2 task = new BitmapWorkerTask2(viewHolder.image);
            AsyncDrawable asyncDrawable = new AsyncDrawable(getContext().getResources(),
                    mLoadingBitmap,task);
            viewHolder.image.setImageDrawable(asyncDrawable);
            task.execute(url);
        }
        
        return view;
    }

    class AsyncDrawable extends BitmapDrawable {
        private WeakReference<BitmapWorkerTask2> bitmapWorkerTask2WeakReference;
        
        AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask2 bitmapWorkerTask2){
            super(res, bitmap);
            bitmapWorkerTask2WeakReference = new WeakReference<BitmapWorkerTask2>(bitmapWorkerTask2);
        }

        BitmapWorkerTask2 getBitmapWorkerTask2() {
            return bitmapWorkerTask2WeakReference.get();
        }
    }
    
    private BitmapWorkerTask2 getBitmapWorkerTask2(ImageView imageView){
        if(imageView != null){
            Drawable drawable = imageView.getDrawable();
            if(drawable instanceof AsyncDrawable){
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask2();
            }
        }
        return null;
    }
    
    private boolean cancelPotentialWork(String url, ImageView imageView){
        BitmapWorkerTask2 bitmapWorkerTask2 = getBitmapWorkerTask2(imageView);
        if(bitmapWorkerTask2 != null){
            String imageUrl = bitmapWorkerTask2.imageUrl;
            if(imageUrl == null || !imageUrl.equals(url)){
                bitmapWorkerTask2.cancel(true);
            }else{
                return false;
            }
        }
        return true;
    }
    

    class BitmapWorkerTask2 extends AsyncTask<String, Void, BitmapDrawable> {
        String imageUrl;
        private WeakReference<ImageView> imageViewReference;

        BitmapWorkerTask2(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected BitmapDrawable doInBackground(String... strings) {
            String mImageUrl = strings[0];//图片url
            //在后台开始下载图片
            Bitmap bitmap = downloadBitmap(mImageUrl);
            return new BitmapDrawable(ApplicationContext.getContext().getResources(),
                    bitmap);
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            ImageView imageView = getAttachedImageView();
            if(imageView != null && drawable != null){
                imageView.setImageDrawable(drawable);
            }
        }

        private ImageView getAttachedImageView() {
            ImageView imageView = imageViewReference.get();
            BitmapWorkerTask2 bitmapWorkerTask2 = getBitmapWorkerTask2(imageView);
            if(this == bitmapWorkerTask2){
                return  imageView;
            }
            return null;
        }

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

    public void setScrolling(Boolean scrolling) {
        this.scrolling = scrolling;
    }

    static class ViewHolder {
        ImageView image;
        TextView musicName;
        TextView singerName;
    }

}
