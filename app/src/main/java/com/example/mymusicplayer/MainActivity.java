package com.example.mymusicplayer;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import entity.User;
import view.HomePageFragment;
import view.LoginActivity;
import view.MusicFragment;
import view.SongListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 程序的主界面.
 */
public class MainActivity extends AppCompatActivity implements 
        HomePageFragment.HomePageCallbackListener {

    private HomePageFragment homePageFragment = null;
    private SongListFragment songListFragment = null;
    private MusicFragment musicFragment = null;
    
    private LinkedList<Fragment> fragmentLinkedList;//在用户的"眼里"，总共页面的"碎片"
    private Map<Fragment,Integer> map = new HashMap<>();//用于记录各个碎片所对应的值
    private int count = 0;//在用户的"眼里"，目前页面的总数量
    private boolean firstOpenHomePageFragment = true;
    private boolean firstOpenSongListFragment = true;
    private boolean firstOpenMusicFragment = true;
    
    private User user;//记录登陆的用户
    
    private final int SHOW_HOME_PAGE = 0;
    private final int SHOW_SONG_LIST = 1;
    private final int SHOW_MUSIC = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initData();
        initFragment(SHOW_HOME_PAGE);
    }

    /**
     * 初始化资料.
     * */
    private void initData(){
        //得到登陆的用户
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(LoginActivity.USER);
        
        fragmentLinkedList = new LinkedList<>();
    }
    
    private void initFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index){
            case SHOW_HOME_PAGE:
                if(homePageFragment == null){
                    homePageFragment = new HomePageFragment();
                    homePageFragment.setCallbackListener(this);
                    transaction.add(R.id.fragment_layout,homePageFragment);
                    map.put(homePageFragment,SHOW_HOME_PAGE);
                    firstOpenHomePageFragment = false;
                }else{
                    transaction.show(homePageFragment);
                }
                
                fragmentLinkedList.addLast(homePageFragment);
                break;
            case SHOW_SONG_LIST:
                if(songListFragment == null){
                    songListFragment = new SongListFragment();
                    transaction.add(R.id.fragment_layout,songListFragment);
                    map.put(songListFragment,SHOW_SONG_LIST);
                    count ++;
                }else{
                    transaction.show(songListFragment);
                }
                
                fragmentLinkedList.addLast(songListFragment);
                break;
            case SHOW_MUSIC:
                if(musicFragment == null){
                    musicFragment = new MusicFragment();
                    transaction.add(R.id.fragment_layout,musicFragment);
                    map.put(musicFragment,SHOW_MUSIC);
                    count ++;
                }else{
                    transaction.show(musicFragment);
                }
                
                fragmentLinkedList.addLast(musicFragment);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 隐藏Fragment.
     * */
    private void hideFragment(FragmentTransaction transaction) {
        if(homePageFragment != null){
            transaction.hide(homePageFragment);
        }
        if(songListFragment != null){
            transaction.hide(songListFragment);
        }
        if(musicFragment != null){
            transaction.hide(musicFragment);
        }
    }

    @Override
    public void showSongList() {
        initFragment(SHOW_SONG_LIST);
    }
    
    public User getUser(){
        return user;
    }
    
    @Override
    public void onBackPressed(){
        if(count == 1){
            super.onBackPressed();
        }else{
            fragmentLinkedList.removeLast();
            count--;
            Fragment fragment = fragmentLinkedList.getLast();
            int nowFragment = map.get(fragment);
            initFragment(nowFragment);
        }
    }
}
