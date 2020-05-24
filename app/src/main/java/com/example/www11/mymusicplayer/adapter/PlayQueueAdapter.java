package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.util.ViewHolderTool;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlayQueueAdapter extends ArrayAdapter<Music> {
    /**
     * 当前音乐的播放位置标记为红色.
     * */
    private int mCurrentPosition = -1;
    
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
        
        if(music != null){
            musicName.setText(music.getName());
            singerName.setText(music.getSingerName());
            
            if(position == mCurrentPosition){
                //当前正在播放的歌曲设置为红色
                musicName.setTextColor(Color.RED);
                singerName.setTextColor(Color.RED);
            }else{
                //不是当前正在播放的歌曲，则设置为黑色
                musicName.setTextColor(Color.BLACK);
                singerName.setTextColor(Color.BLACK);
            }
            
        }
        
        return view;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
    }
}
