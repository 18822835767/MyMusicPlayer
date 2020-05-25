package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.util.ViewHolderTool;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PlayQueueAdapter extends ArrayAdapter<Music> implements View.OnClickListener{
    /**
     * 当前音乐的播放位置标记为红色.
     * */
    private int mCurrentPosition = -1;
    
    private InnerItemOnClickListener mListener = null;
    
    public PlayQueueAdapter(@NonNull Context context, int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        Music music = getItem(position);
        
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.play_queue_item,null);
        }else{
            view = convertView;
        }

        TextView musicName = ViewHolderTool.get(view,R.id.music_name);
        TextView singerName = ViewHolderTool.get(view,R.id.singer_name);
        ImageButton removeMusic = ViewHolderTool.get(view,R.id.remove_music);
        
        if(music != null){
            musicName.setText(music.getName());
            singerName.setText(music.getSingerName());
            
            if(position == mCurrentPosition){
                //当前正在播放的歌曲设置为红色
                musicName.setTextColor(Color.RED);
                singerName.setTextColor(Color.RED);
                removeMusic.setVisibility(GONE);
            }else{
                //不是当前正在播放的歌曲，则设置为黑色
                musicName.setTextColor(Color.BLACK);
                singerName.setTextColor(Color.BLACK);
                removeMusic.setVisibility(VISIBLE);
            }
        }
        
        removeMusic.setOnClickListener(this);
        removeMusic.setTag(position);//保存当前xx按钮所在的位置
        
        return view;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
    }

    /**
     * 为适配器设置回调接口.
     * */
    public void setListener(InnerItemOnClickListener listener) {
        mListener = listener;
    }

    /**
     * 回调接口中的方法.
     * */
    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.itemClick(v);
        }
    }

    /**
     * 当listView内部的xx被点击后回调这个接口.
     * */
    public interface InnerItemOnClickListener{
        void itemClick(View v);
    }
}
