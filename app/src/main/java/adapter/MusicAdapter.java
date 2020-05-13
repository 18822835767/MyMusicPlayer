package adapter;

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
import entity.Music;
import util.DownImage;

public class MusicAdapter extends ArrayAdapter<Music> {
    private int mResourceId;//子项布局的id

    public MusicAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Music> objects) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Music music = getItem(position);
        View view;
        MusicAdapter.ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId,parent,false);
            viewHolder = new MusicAdapter.ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.music_image);
            viewHolder.textView =  (TextView)view.findViewById(R.id.music_name);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (MusicAdapter.ViewHolder)view.getTag();
        }
        if (music != null) {
            viewHolder.textView.setText(music.getName());
            DownImage downImage = new DownImage(music.getPicUrl());
            downImage.loadImage(drawable -> viewHolder.imageView.setImageDrawable(drawable));
        }
        return view;
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
