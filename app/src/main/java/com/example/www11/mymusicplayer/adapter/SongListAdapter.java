package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.mymusicplayer.R;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.www11.mymusicplayer.entity.SongList;
import com.example.www11.mymusicplayer.util.BitmapWorkertask;


/**
 * 点击“我的歌单”时的ListView的适配器.
 * */
public class SongListAdapter extends ArrayAdapter<SongList>{
    private int mResourceId;//子项布局的id
    private ListView mListView;//歌单所在的listview
    private boolean scrolling = false;//listview是否处于滚动状态
    
    public SongListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<SongList> objects) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (mListView == null) {
            mListView = (ListView) parent;
            //为listView设置滚动监听。
            mListView.setOnScrollListener(new MyScrListnear());
        }
        
        SongList songList = getItem(position);
        View view;
        ViewHolder viewHolder;
        String mImageUrl = null;
        
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.image = view.findViewById(R.id.song_list_image);
            viewHolder.textView = view.findViewById(R.id.song_list_name);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        if (songList != null) {
            mImageUrl = songList.getCoverImgUrl();
            //如果处于滚动状态，就设置图片内容为"空白"
            if (scrolling) {
                viewHolder.image.setImageResource(R.drawable.empty_photo);
            }
            //为image做个tag
            viewHolder.image.setTag(songList.getId());
            
            viewHolder.textView.setText(songList.getName());
        }

        //ImageCallback的回调。保证图片不错乱的关键代码.
        int requireWidth = viewHolder.image.getWidth();
        int requireHeight = viewHolder.image.getHeight();
        BitmapWorkertask task = new BitmapWorkertask(requireWidth, requireHeight, drawable -> {
            ImageView imageView = null;
            if (songList != null) {
                imageView = mListView.findViewWithTag(songList.getId());
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
    
    static class ViewHolder{
        ImageView image;
        TextView textView;
    }
    
}
