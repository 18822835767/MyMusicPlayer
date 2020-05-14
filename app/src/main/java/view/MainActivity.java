package view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import entity.Music;
import entity.User;
import service.PlayService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mymusicplayer.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 程序的主界面.
 */
public class MainActivity extends AppCompatActivity implements HomePageFragment.OnHomePageListener,
        SongListFragment.OnSongListListener, MusicFragment.OnMusicListener {

    //三个碎片
    private HomePageFragment mHomePageFragment = null;
    private SongListFragment mSongListFragment = null;
    private MusicFragment mMusicFragment = null;
    
    //播放音乐的碎片(底部的播放栏).
    private PlayMusicFragment mPlayMusicFragment;

    //在用户的"眼里"，总共页面的"碎片"
    private LinkedList<Fragment> mFragmentLinkedList;
    //用于记录各个碎片所对应的值
    private Map<Fragment, Integer> mMap = new HashMap<>();

    //记录登陆的用户
    private User mUser;
    //记录用户点击的歌单的id
    private long mSongListId = 0;

    //定义三个碎片所对应的值
    private final int SHOW_HOME_PAGE = 0;
    private final int SHOW_SONG_LIST = 1;
    private final int SHOW_MUSIC = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initService();
        initData();
        initFragment(SHOW_HOME_PAGE);

       checkPermission();
    }

    /**
     * 权限的判断
     * */
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    /**
     * 初始化数据.
     */
    private void initData() {
        //得到登陆的用户
        Intent intent = getIntent();
        mUser = (User) intent.getSerializableExtra(LoginActivity.USER);

        mFragmentLinkedList = new LinkedList<>();
        
        mPlayMusicFragment = (PlayMusicFragment) getSupportFragmentManager().findFragmentById
                (R.id.play_music_fragment);
    }

    /**
     * 开启服务.
     * */
    private void initService(){
        Intent intent = new Intent(MainActivity.this, PlayService.class);
        startService(intent);
    }


    /**
     * 展示碎片.
     * <p>
     * 碎片引用为null，则创建碎片.不为null,则直接通过transaction.show()
     * </p>
     */
    private void initFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case SHOW_HOME_PAGE:
                if (mHomePageFragment == null) {
                    mHomePageFragment = new HomePageFragment();
                    transaction.add(R.id.fragment_layout, mHomePageFragment);
                    //保存碎片所对应的值
                    mMap.put(mHomePageFragment, SHOW_HOME_PAGE);
                } else {
                    transaction.show(mHomePageFragment);
                }

                //如果List不包含，则添加碎片
                if (!mFragmentLinkedList.contains(mHomePageFragment)) {
                    mFragmentLinkedList.addLast(mHomePageFragment);
                }
                break;
            case SHOW_SONG_LIST:
                if (mSongListFragment == null) {
                    mSongListFragment = new SongListFragment();
                    transaction.add(R.id.fragment_layout, mSongListFragment);
                    //保存碎片所对应的值
                    mMap.put(mSongListFragment, SHOW_SONG_LIST);
                } else {
                    transaction.show(mSongListFragment);
                }

                //如果List不包含，则添加碎片
                if (!mFragmentLinkedList.contains(mSongListFragment)) {
                    mFragmentLinkedList.addLast(mSongListFragment);
                }

                break;
            case SHOW_MUSIC:
                if (mMusicFragment == null) {
                    mMusicFragment = new MusicFragment();
                    transaction.add(R.id.fragment_layout, mMusicFragment);
                    //保存碎片所对应的值
                    mMap.put(mMusicFragment, SHOW_MUSIC);
                } else {
                    transaction.show(mMusicFragment);
                }

                //如果List不包含，则添加碎片
                if (!mFragmentLinkedList.contains(mMusicFragment)) {
                    mFragmentLinkedList.addLast(mMusicFragment);
                }

                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 隐藏Fragment.
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (mHomePageFragment != null) {
            transaction.hide(mHomePageFragment);
        }
        if (mSongListFragment != null) {
            transaction.hide(mSongListFragment);
        }
        if (mMusicFragment != null) {
            transaction.hide(mMusicFragment);
            mMusicFragment = null;
        }
    }

    /**
     * 展示用户的歌单的“碎片”.
     */
    @Override
    public void showSongList() {
        initFragment(SHOW_SONG_LIST);
    }

    /**
     * 展示某个歌单中的音乐"碎片".
     */
    @Override
    public void showMusics(long songListId) {
        this.mSongListId = songListId;
        initFragment(SHOW_MUSIC);
    }

    /**
     * 碎片通过调用该方法可以得到登陆的用户.
     */
    @Override
    public User getUser() {
        return mUser;
    }

    /**
     * 碎片通过调用该方法获得用户点击的歌单的id.
     * @return
     */
    @Override
    public long getSongListId() {
        return mSongListId;
    }

    @Override
    public void playMusics(List<Music> musics, int position) {
        if(mPlayMusicFragment != null){
            mPlayMusicFragment.playMusics(musics,position);
        }
    }

    /**
     * 重写"返回键"的方法.
     */
    @Override
    public void onBackPressed() {
        if (mFragmentLinkedList.size() == 1) {
            //只有一个碎片，说明只剩下主界面，接着就是关闭程序了
            super.onBackPressed();
        } else {
            //不止一个碎片，说明用户只是想返回上一个页面
            mFragmentLinkedList.removeLast();
            Fragment fragment = mFragmentLinkedList.getLast();
            int nowFragment = mMap.get(fragment);
            initFragment(nowFragment);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "拒绝权限将无法使用该程序", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
