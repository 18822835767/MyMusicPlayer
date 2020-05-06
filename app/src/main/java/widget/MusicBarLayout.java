package widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.example.mymusicplayer.R;

/**
 * 自定义的"音乐播放栏"控件.
 * */
public class MusicBarLayout extends RelativeLayout {

    public MusicBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.music_bar,this);
    }
}
