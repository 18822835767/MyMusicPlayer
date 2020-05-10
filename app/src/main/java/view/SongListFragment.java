package view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mymusicplayer.MainActivity;
import com.example.mymusicplayer.R;

import java.util.List;

import adapter.SongListAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import contract.SongListContract;
import entity.SongList;
import entity.User;
import presenter.SongListPresenterImpl;
import util.ApplicationContext;

public class SongListFragment extends Fragment implements SongListContract.OnSongListView{
    private View view;
    private User user;
    private SongListContract.SongListPresenter songListPresenter;
    private ListView listView;
    private List<SongList> songLists;

    private final String TAG = "SongListActivity";
    public static final String MUSIC = "music";
    public static final String Fragment = "fragment";
    
    //网络请求的访问状态
    private static final int SUCCESS = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_song_list, container, false);
        
        initData();
        initEvent();
        
        return view;
    }

    public void initData() {
        user = ((MainActivity)getActivity()).getUser();
        listView = view.findViewById(R.id.song_list);

        //初始化presenter
        songListPresenter = new SongListPresenterImpl(this);

        setListItem();
    }

    public void initEvent() {
        
    }

    private void setListItem() {
        songListPresenter.getUserSongList(user.getId());
    }

    @Override
    public void showSongList(List<SongList> songLists) {
        this.songLists = songLists;
        Message message = new Message();
        message.what = SUCCESS;
        handler.sendMessage(message);
    }

    @Override
    public void showError() {

    }

    public void setUser(User user){
        this.user = user;
    }
    
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    SongListAdapter adapter = new SongListAdapter(getActivity(),
                            R.layout.song_list_item, songLists);
                    listView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    
}
