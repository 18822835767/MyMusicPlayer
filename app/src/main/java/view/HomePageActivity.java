package view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import entity.User;
import fragment.MusicBarFragment;
import widget.BannerViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐首页所对应的view.
 */
public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    private User user;//记录登陆的用户
    private String TAG = "HomePageActivity";
    private Button mySongList;
    private MusicBarFragment musicBarFragment;

    public static final String MUSIC_BAR = "musicBar";
    public static final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        initData();
        initEvent();
    }

    private void initData() {
        //为轮播图设置数据
        BannerViewPager banner = (BannerViewPager) findViewById(R.id.banner_view_pager);
        List<Integer> imageUrl = new ArrayList<>();
        imageUrl.add(R.drawable.one);
        imageUrl.add(R.drawable.two);
        imageUrl.add(R.drawable.thr);
        banner.setData(imageUrl);

        mySongList = (Button) findViewById(R.id.my_song_list);

        //得到登陆的用户
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(LoginActivity.USER);

        //添加音乐播放栏
        musicBarFragment = new MusicBarFragment();
        setFragment(musicBarFragment);
    }

    private void initEvent() {
        mySongList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_music:
                break;
            case R.id.my_song_list:
                Intent intent = new Intent(HomePageActivity.this, SongListActivity.class);
                intent.putExtra(MUSIC_BAR, musicBarFragment);
                intent.putExtra(USER,user);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.music_bar_layout, fragment);
        transaction.commit();
    }
}
