package view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import entity.User;
import util.HttpCallbackListener;
import widget.BannerViewPager;

/**
 * 音乐首页所对应的view.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener{
    private User user;//记录登陆的用户
    private Button mySongList;
    private View view;
    private int songListId;//当用户点击歌单时，记录歌单的id
    private HomePageCallbackListener callbackListener;

    public static final String MUSIC_BAR = "musicBar";
    public static final String USER = "user";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.home_page,container,false);  
        
        initData();
        initEvent();

        return view;
    }

    private void initData() {
        //为轮播图设置数据
        BannerViewPager banner = (BannerViewPager) view.findViewById(R.id.banner_view_pager);
        List<Integer> imageUrl = new ArrayList<>();
        imageUrl.add(R.drawable.one);
        imageUrl.add(R.drawable.two);
        imageUrl.add(R.drawable.thr);
        banner.setData(imageUrl);

        mySongList = (Button) view.findViewById(R.id.my_song_list);
    }

    private void initEvent() {
        mySongList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_songs:
                break;
            case R.id.my_song_list:
//                Intent intent = new Intent(HomePageActivity.this, SongListActivity.class);
//                intent.putExtra(MUSIC_BAR, musicBarFragment);
//                intent.putExtra(USER,user);
//                startActivity(intent);
                callbackListener.showSongList();
                break;
            default:
                break;
        }
    }
    
    public void setCallbackListener(HomePageCallbackListener callbackListener){
        this.callbackListener = callbackListener;
    }
    
    /**
     * MainActivity要实现的接口，当用户触发点击事件时，回调该接口.
     * */
    public interface HomePageCallbackListener {
        void showSongList();
    }
}
