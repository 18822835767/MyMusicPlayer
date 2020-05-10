package view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mymusicplayer.MainActivity;
import com.example.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

import adapter.MusicAdapter;
import adapter.SongListAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import contract.MusicContract;
import entity.Music;
import presenter.MusicPresenterImpl;
import util.ApplicationContext;

public class MusicFragment extends Fragment implements MusicContract.OnMusicView {
    private ListView listView;
    private MusicContract.MusicPresenter musicPresenter;
    private List<Music> musics;
    private static final String TAG = "MusicActivity";
    private View view;

    //网络请求的访问状态
    private static final int SUCCESS = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_music, container, false);
        
        initData();
        initEvent();
        
        return view;
    }
    
    /**
     * 初始化数据.
     * */
    private void initData(){
        listView = view.findViewById(R.id.music_list);

        musics = new ArrayList<>();

        musicPresenter = new MusicPresenterImpl(this);
        
        setMusicItem();
    }

    private void initEvent(){
        
    }

    /**
     * 设置歌单中的歌曲显示.
     * */
    public void setMusicItem(){
        musicPresenter.getMusicList(((MainActivity)getActivity()).getSongListId());
    }
    
    @Override
    public void showMusics(List<Music> musics) {
        this.musics = musics;
        Message message = new Message();
        message.what = SUCCESS;
        handler.sendMessage(message);
    }

    @Override
    public void showError() {

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    MusicAdapter adapter = new MusicAdapter(getActivity(),
                            R.layout.music_item,musics);
                    listView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    
    
}
