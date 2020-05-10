package view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mymusicplayer.R;

public class MusicActivity extends AppCompatActivity {
    private int songListId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
   
        initData();
    }
    
    private void initData(){
        Intent intent = getIntent();
        songListId = intent.getIntExtra(SongListActivity.MUSIC,-1);
    }
}
