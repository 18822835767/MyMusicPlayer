package view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

public class SongListFragment extends Fragment implements SongListContract.OnSongListView{
    private View view;
    private User user;
    private SongListContract.SongListPresenter songListPresenter;
    private ListView listView;
    private List<SongList> songLists;
    private OnSongListListener mCallback;//碎片和活动通信的接口引用

    private final String TAG = "SongListActivity";
    public static final String MUSIC = "music";
    public static final String Fragment = "fragment";
    
    //网络请求的访问状态
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private static final int ERROR = 2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (OnSongListListener) context;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_song_list, container, false);
        
        initData();
        initEvent();
        
        return view;
    }

   

    private void initData() {
        user = mCallback.getUser();
        listView = view.findViewById(R.id.song_list);

        //初始化presenter
        songListPresenter = new SongListPresenterImpl(this);

        setListItem();
    }

    private void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongList songList = songLists.get(position);
                if(mCallback != null){
                    mCallback.showMusics(songList.getId());
                }
                
            }
        });
    }

    private void setListItem() {
        songListPresenter.getUserSongList(user.getId());
    }

    @Override
    public void showSongList(List<SongList> songLists) {
        this.songLists = songLists;
        Message message = Message.obtain();
        message.what = SUCCESS;
        handler.sendMessage(message);
    }

    @Override
    public void showFail() {
        Message message = Message.obtain();
        message.what = FAIL;
        handler.sendMessage(message);
    }

    @Override
    public void showError() {
        Message message = Message.obtain();
        message.what = ERROR;
        handler.sendMessage(message);
    }
    
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    if(getActivity() != null){
                        SongListAdapter adapter = new SongListAdapter(getActivity(),
                                R.layout.song_list_item, songLists);
                        listView.setAdapter(adapter);
                    }
                    break;
                case FAIL:
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),"请求失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ERROR:
                    if(getActivity() != null){
                        AlertDialog.Builder errorDialog = new AlertDialog.Builder(getActivity());
                        errorDialog.setTitle("错误");
                        errorDialog.setMessage("请检查设备是否联网");
                        errorDialog.setPositiveButton("OK", (dialog, which) -> {});
                        errorDialog.show();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 当用户点击某张"歌单"的时候调用.
     * MainActivity去实现，作为碎片和活动之间通信的回调接口.
     * */
    public interface OnSongListListener {
        void showMusics(int songListId);
        User getUser();
    }
    
}
