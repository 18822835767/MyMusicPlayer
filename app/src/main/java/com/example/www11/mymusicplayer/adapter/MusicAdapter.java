package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.www11.mymusicplayer.util.BitmapWorkertask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MusicAdapter extends ArrayAdapter<Music> {

    private ListView mListView;//adapter所对应的listview
    private boolean scrolling = false;//listview是否处于滚动状态

    public MusicAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Music> objects) {
        super(context, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (mListView == null) {
            mListView = (ListView) parent;
            //为listView设置滚动监听。
            mListView.setOnScrollListener(new MyScrListnear());
        }

        View view;
        ViewHolder viewHolder;
        Music music = getItem(position);
        String mImageUrl = null;

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
            mImageUrl = music.getPicUrl();
            //如果处于滚动状态，就设置图片内容为"空白"
            if (scrolling) {
                viewHolder.image.setImageResource(R.drawable.empty_photo);
            }
            //为image做个tag
            viewHolder.image.setTag(music.getId());
            viewHolder.musicName.setText(music.getName());
            viewHolder.singerName.setText(music.getSingerName());
        }
        
        //ImageCallback的回调。保证图片不错乱的关键代码.
        int requireWidth = viewHolder.image.getWidth();
        int requireHeight = viewHolder.image.getHeight();
        BitmapWorkertask task = new BitmapWorkertask(requireWidth, requireHeight, drawable -> {
            ImageView imageView = null;
            if (music != null) {
                imageView = mListView.findViewWithTag(music.getId());
            }

            if (imageView != null && drawable != null) {
                viewHolder.image.setImageDrawable(drawable);
            }
        });
        
        task.execute(mImageUrl);
        
        return view;
    }
    
    /**
     * listView滚动状态的监听.
     */
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
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
        }
    }

    static class ViewHolder {
        ImageView image;
        TextView musicName;
        TextView singerName;
    }

}
