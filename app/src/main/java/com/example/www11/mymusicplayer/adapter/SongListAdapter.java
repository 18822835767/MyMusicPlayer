package com.example.www11.mymusicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mymusicplayer.R;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.www11.mymusicplayer.entity.SongList;
import com.example.www11.mymusicplayer.util.DownImage;

/**
 * 点击“我的歌单”时的ListView的适配器.
 * */
public class SongListAdapter extends ArrayAdapter<SongList>{
    private int mResourceId;//子项布局的id
    private DownImage downImage = DownImage.getInstance();//加载网络图片
    
    public SongListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<SongList> objects) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SongList songList = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.song_list_image);
            viewHolder.textView =  (TextView)view.findViewById(R.id.song_list_name);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        if (songList != null) {
            viewHolder.textView.setText(songList.getName());
            downImage.loadImage(songList.getCoverImgUrl(), drawable ->
                    viewHolder.imageView.setImageDrawable(drawable));
        }
        return view;
    }
    
    static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
    
}
