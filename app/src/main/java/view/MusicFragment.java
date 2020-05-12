package view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mymusicplayer.MainActivity;
import com.example.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

import adapter.MusicAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import contract.MusicContract;
import entity.Music;
import presenter.MusicPresenterImpl;

public class MusicFragment extends Fragment implements MusicContract.OnMusicView {
    private ListView listView;
    private MusicContract.MusicPresenter musicPresenter;
    private List<Music> musics;
    private static final String TAG = "MusicActivity";
    private View view;
    
    private OnMusicListener mCallback;//碎片和活动通信的接口引用

    //网络请求的访问状态
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private static final int ERROR = 2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (OnMusicListener) context;
    }

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
        musicPresenter.getMusicList(mCallback.getSongListId());
    }
    
    @Override
    public void showMusics(List<Music> musics) {
        this.musics = musics;
        Message message = new Message();
        message.what = SUCCESS;
        handler.sendMessage(message);
    }

    @Override
    public void showFail() {
        Message message = new Message();
        message.what = FAIL;
        handler.sendMessage(message);
    }

    @Override
    public void showError() {
        Message message = new Message();
        message.what = ERROR;
        handler.sendMessage(message);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    if(getActivity() != null){
                        MusicAdapter adapter = new MusicAdapter(getActivity(),
                                R.layout.music_item,musics);
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
    
    public interface OnMusicListener{
        int getSongListId();
    }
    
}
