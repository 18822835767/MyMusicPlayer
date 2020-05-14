package adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
    private DownImage downImage = DownImage.getInstance();

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
            viewHolder = new ViewHolder();
            viewHolder.imageView = view.findViewById(R.id.music_image);
            viewHolder.musicName = view.findViewById(R.id.music_name);
            viewHolder.singerName = view.findViewById(R.id.singer_name);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (MusicAdapter.ViewHolder)view.getTag();
        }
        if (music != null) {
            viewHolder.musicName.setText(music.getName());
            viewHolder.singerName.setText(music.getSingerName());
            downImage.loadImage(music.getPicUrl(), drawable -> 
                    viewHolder.imageView.setImageDrawable(drawable));
        }
        return view;
    }

    static class ViewHolder{
        ImageView imageView;
        TextView musicName;
        TextView singerName;
    }
}
