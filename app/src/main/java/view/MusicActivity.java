package view;

import adapter.MusicAdapter;
import androidx.appcompat.app.AppCompatActivity;
import contract.MusicContract;
import entity.Music;
import presenter.MusicPresenterImpl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private static final String TAG = "MusicActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
   
        initData();
        initEvent();
    }
    
    private void initData(){
        Intent intent = getIntent();
        songListId = intent.getIntExtra(SongListActivity.MUSIC,-1);
        
        listView = findViewById(R.id.music_list);
        
        musics = new ArrayList<>();
        
        musicPresenter = new MusicPresenterImpl(this);
        
        setMusicItem();
    }
    
    private void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });
    }
    
    private void setMusicItem(){
        musicPresenter.getMusicList(songListId);
    }
    
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
