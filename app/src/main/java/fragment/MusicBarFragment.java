package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mymusicplayer.R;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * "音乐播放栏"的碎片.
 * */
public class MusicBarFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.music_bar,container,false);

        ImageButton imageButton = view.findViewById(R.id.play_or_pause);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton.setBackgroundResource(R.drawable.music_pause);
            }
        });
        return view;
    }
}
