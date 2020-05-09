package view;

import adapter.SongListAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import entity.SongList;
import fragment.MusicBarFragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {
    private List<SongList> songLists = new ArrayList<>();
    private MusicBarFragment fragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        init();        
    }
    
    private void init(){
        SongList songList1 = new SongList(1,"歌单一",R.drawable.one);
        SongList songList2 = new SongList(2,"歌单二",R.drawable.two);
        SongList songList3 = new SongList(3,"歌单三",R.drawable.thr);
        songLists.add(songList1);
        songLists.add(songList2);
        songLists.add(songList3);

        SongListAdapter adapter = new SongListAdapter(SongListActivity.this,
                R.layout.song_list_item,songLists);
        ListView listView = (ListView)findViewById(R.id.song_list);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        fragment = (MusicBarFragment) intent.getSerializableExtra(HomePageActivity.MUSIC_BAR);
        setFragment(fragment);
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.music_bar_layout,fragment);
        transaction.commit();
    }
}
