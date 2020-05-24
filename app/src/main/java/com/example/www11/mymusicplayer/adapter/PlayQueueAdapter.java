package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
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
    
    public PlayQueueAdapter(@NonNull Context context, int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        Music music = getItem(position);
        
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.play_queue,null);
        }else{
            view = convertView;
        }

        TextView musicName = ViewHolderTool.get(view,R.id.music_name);
        TextView singerName = ViewHolderTool.get(view,R.id.singer_name);
        
        if(music != null){
            musicName.setText(music.getName());
            singerName.setText(music.getSingerName());
        }
        
        return view;
    }
}
