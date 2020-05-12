package com.example.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import entity.User;
import service.PlayService;
import view.HomePageFragment;
import view.LoginActivity;
import view.MusicFragment;
import view.SongListFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 程序的主界面.
 */
public class MainActivity extends AppCompatActivity implements HomePageFragment.OnHomePageListener,
        SongListFragment.OnSongListListener, MusicFragment.OnMusicListener {

    //三个碎片
    private HomePageFragment homePageFragment = null;
    private SongListFragment songListFragment = null;
    private MusicFragment musicFragment = null;

    //在用户的"眼里"，总共页面的"碎片"
    private LinkedList<Fragment> fragmentLinkedList;
    //用于记录各个碎片所对应的值
    private Map<Fragment, Integer> map = new HashMap<>();

    //记录登陆的用户
    private User user;
    //记录用户点击的歌单的id
    private int songListId = 0;

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
        user = (User) intent.getSerializableExtra(LoginActivity.USER);

        fragmentLinkedList = new LinkedList<>();
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
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                    transaction.add(R.id.fragment_layout, homePageFragment);
                    //保存碎片所对应的值
                    map.put(homePageFragment, SHOW_HOME_PAGE);
                } else {
                    transaction.show(homePageFragment);
                }

                //如果List不包含，则添加碎片
                if (!fragmentLinkedList.contains(homePageFragment)) {
                    fragmentLinkedList.addLast(homePageFragment);
                }
                break;
            case SHOW_SONG_LIST:
                if (songListFragment == null) {
                    songListFragment = new SongListFragment();
                    transaction.add(R.id.fragment_layout, songListFragment);
                    //保存碎片所对应的值
                    map.put(songListFragment, SHOW_SONG_LIST);
                } else {
                    transaction.show(songListFragment);
                }

                //如果List不包含，则添加碎片
                if (!fragmentLinkedList.contains(songListFragment)) {
                    fragmentLinkedList.addLast(songListFragment);
                }

                break;
            case SHOW_MUSIC:
                if (musicFragment == null) {
                    musicFragment = new MusicFragment();
                    transaction.add(R.id.fragment_layout, musicFragment);
                    //保存碎片所对应的值
                    map.put(musicFragment, SHOW_MUSIC);
                } else {
                    transaction.show(musicFragment);
                }

                //如果List不包含，则添加碎片
                if (!fragmentLinkedList.contains(musicFragment)) {
                    fragmentLinkedList.addLast(musicFragment);
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
        if (homePageFragment != null) {
            transaction.hide(homePageFragment);
        }
        if (songListFragment != null) {
            transaction.hide(songListFragment);
        }
        if (musicFragment != null) {
            transaction.hide(musicFragment);
            musicFragment = null;
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
    public void showMusics(int songListId) {
        this.songListId = songListId;
        initFragment(SHOW_MUSIC);
    }

    /**
     * 碎片通过调用该方法可以得到登陆的用户.
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     * 碎片通过调用该方法获得用户点击的歌单的id.
     */
    @Override
    public int getSongListId() {
        return songListId;
    }

    /**
     * 重写"返回键"的方法.
     */
    @Override
    public void onBackPressed() {
        if (fragmentLinkedList.size() == 1) {
            //只有一个碎片，说明只剩下主界面，接着就是关闭程序了
            super.onBackPressed();
        } else {
            //不止一个碎片，说明用户只是想返回上一个页面
            fragmentLinkedList.removeLast();
            Fragment fragment = fragmentLinkedList.getLast();
            int nowFragment = map.get(fragment);
            initFragment(nowFragment);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用该程序", Toast.LENGTH_SHORT).show();
                    finish();
                } 
                break;
            default:
                break;
        }
    }
}
