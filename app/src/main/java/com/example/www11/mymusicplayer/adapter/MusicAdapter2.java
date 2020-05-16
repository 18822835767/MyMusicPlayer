package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.entity.Music;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MusicAdapter2 extends ArrayAdapter<Music> {

    private ListView mListView;
    private boolean scrolling = false;

    /**
     * 图片缓存.
     */
    private LruCache<String, BitmapDrawable> mMemoryCache;
    public MusicAdapter2(@NonNull Context context, int textViewResourceId, @NonNull List<Music> objects) {
        super(context, textViewResourceId, objects);
        //应用程序的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //给图片缓存分配的内存空间大小
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (mListView == null) {
            mListView = (ListView) parent;
            mListView.setOnScrollListener(new MyScrListnear());
        }

        String url = null;
        View view;
        ViewHolder viewHolder;
        Music music = getItem(position);

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
            if (scrolling) {
                viewHolder.image.setImageResource(R.drawable.empty_photo);
            }
            viewHolder.image.setTag(music.getPicUrl());
            viewHolder.musicName.setText(music.getName());
            viewHolder.singerName.setText(music.getSingerName());
        }

        BitmapDrawable drawable = getBitmapFromMemoryCache(url);
        if (drawable != null) {
            viewHolder.image.setImageDrawable(drawable);
        } else {
        BitmapWorkertask task = new BitmapWorkertask(viewHolder.image);
        task.execute(url);
        }
        return view;
    }

    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, drawable);
        }
    }

    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    class BitmapWorkertask extends AsyncTask<String, Void, BitmapDrawable> {

        private ImageView mImageView;
        String imageUrl;

        public BitmapWorkertask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            //在后台开始下载图片
            Bitmap bitmap = downloadBitmap(imageUrl);
            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            addBitmapToMemoryCache(imageUrl, drawable);
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            ImageView imageView = mListView.findViewWithTag(imageUrl);
            if (imageView != null && drawable != null) {
                mImageView.setImageDrawable(drawable);
            }
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

    static class ViewHolder {
        ImageView image;
        TextView musicName;
        TextView singerName;
    }


    public class MyScrListnear implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                    scrolling = false;
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                    scrolling = true;
                    break;
            }
        }
        
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //显示或者隐藏某些东西
        }
    }


}
