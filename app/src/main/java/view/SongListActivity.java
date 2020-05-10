package view;

import adapter.SongListAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import contract.SongListContract;
import entity.SongList;
import entity.User;
import fragment.MusicBarFragment;
import presenter.SongListPresenterImpl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mymusicplayer.R;

import java.util.List;

public class SongListActivity extends AppCompatActivity implements SongListContract.OnSongListView {
    private MusicBarFragment fragment;
    private SongListContract.SongListPresenter songListPresenter;
    private User user;
    private final String TAG = "SongListActivity";
    private ListView listView;
    private List<SongList> songLists;
    
    public static final String MUSIC = "music";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        initData();       
        initEvent();
    }
    
    private void initData(){
        //添加播放栏的碎片
        Intent intent = getIntent();
        fragment = (MusicBarFragment) intent.getSerializableExtra(HomePageActivity.MUSIC_BAR);
        setFragment(fragment);
        //存放用户实体
        user = (User) intent.getSerializableExtra(HomePageActivity.USER);

        listView = (ListView)findViewById(R.id.song_list);
        
        //初始化presenter
        songListPresenter = new SongListPresenterImpl(this);
        
        setListItem();
    }

    private void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongList songList = songLists.get(position);
                Intent intent = new Intent(SongListActivity.this,MusicActivity.class);
                intent.putExtra(MUSIC,songList.getId());
                startActivity(intent);
            }
        });
    }
    
    /**
     * 设置ListView中的数据.
     * */
    private void setListItem(){
        songListPresenter.getUserSongList(user.getId());
    }
    
    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.music_bar_layout,fragment);
        transaction.commit();
    }

    @Override
    public void showSongList(List<SongList> songLists) {
        this.songLists = songLists;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SongListAdapter adapter = new SongListAdapter(SongListActivity.this,
                        R.layout.song_list_item,songLists);
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void showError() {

    }
}
