package view;

import android.content.Context;
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
import widget.BannerViewPager;

/**
 * 音乐首页所对应的view.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener{
    private Button mMySongList;
    private View view;
    private int mSongListId;//当用户点击歌单时，记录歌单的id
    private OnHomePageListener mCallback;//碎片和活动通信的接口引用

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (OnHomePageListener) context;
    }

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

        mMySongList = (Button) view.findViewById(R.id.my_song_list);
    }

    private void initEvent() {
        mMySongList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_songs:
                break;
            case R.id.my_song_list:
                mCallback.showSongList();
                break;
            default:
                break;
        }
    }
    
    /**
     * 当用户点击"我的歌单"或者“本地歌单”的按钮时候调用.
     * MainActivity去实现，作为碎片和活动之间通信的回调接口.
     * */
    public interface OnHomePageListener {
        void showSongList();
    }
}
