package view;

import adapter.MusicAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import contract.MusicContract;
import entity.Music;
import fragment.MusicBarFragment;
import presenter.MusicPresenterImpl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements MusicContract.OnMusicView {
    private int songListId;//记录是哪一张歌单的音乐
    private ListView listView;
    private MusicContract.MusicPresenter musicPresenter;
    private List<Music> musics;
    private MusicBarFragment fragment;
    private static final String TAG = "MusicActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
   
        initData();
        initEvent();
    }
    
    /**
     * 初始化数据.
     * */
    private void initData(){
        Intent intent = getIntent();
        songListId = intent.getIntExtra(SongListActivity.MUSIC,-1);
        fragment = (MusicBarFragment) intent.getSerializableExtra(SongListActivity.Fragment);
        setFragment(fragment);
        
        listView = findViewById(R.id.music_list);
        
        musics = new ArrayList<>();
        
        musicPresenter = new MusicPresenterImpl(this);
        
        setMusicItem();
    }
    
    private void initEvent(){
        //当用户点击歌单中的歌曲时，进行监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });
    }
    
    /**
     * 设置歌单中的歌曲显示.
     * */
    private void setMusicItem(){
        musicPresenter.getMusicList(songListId);
    }

    /**
     * 引入底部播放栏的碎片.
     * */
    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.music_bar_layout,fragment);
        transaction.commit();
    }
    
    /**
     * 用于presenter的回调.
     * */
    @Override
    public void showMusics(List<Music> musics) {
        this.musics = musics;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MusicAdapter adapter = new MusicAdapter(MusicActivity.
                        this,R.layout.music_item,musics);
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void showError() {

    }
}
