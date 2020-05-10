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
public class MainActivity extends AppCompatActivity{

    private HomePageFragment homePageFragment = null;
    private SongListFragment songListFragment = null;
    private MusicFragment musicFragment = null;
    
    private LinkedList<Fragment> fragmentLinkedList;//在用户的"眼里"，总共页面的"碎片"
    private Map<Fragment,Integer> map = new HashMap<>();//用于记录各个碎片所对应的值
    
    private User user;//记录登陆的用户
    private int songListId = 0;//记录用户点击的歌单的id
    
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
                    transaction.add(R.id.fragment_layout,homePageFragment);
                    map.put(homePageFragment,SHOW_HOME_PAGE);
                }else{
                    transaction.show(homePageFragment);
                }
                
                //如果List不包含，则添加碎片
                if(!fragmentLinkedList.contains(homePageFragment)){
                    fragmentLinkedList.addLast(homePageFragment);
                }
                break;
            case SHOW_SONG_LIST:
                if(songListFragment == null){
                    songListFragment = new SongListFragment();
                    transaction.add(R.id.fragment_layout,songListFragment);
                    map.put(songListFragment,SHOW_SONG_LIST);
                }else{
                    transaction.show(songListFragment);
                }

                //如果List不包含，则添加碎片
                if(!fragmentLinkedList.contains(songListFragment)){
                    fragmentLinkedList.addLast(songListFragment);
                }
                
                break;
            case SHOW_MUSIC:
                if(musicFragment == null){
                    musicFragment = new MusicFragment();
                    transaction.add(R.id.fragment_layout,musicFragment);
                    map.put(musicFragment,SHOW_MUSIC);
                }else{
                    transaction.show(musicFragment);
                }
                
                //如果List不包含，则添加碎片
                if(!fragmentLinkedList.contains(musicFragment)){
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
            musicFragment = null;
        }
    }
    
    /**
     * 展示用户的歌单.
     * */
    public void showSongList() {
        initFragment(SHOW_SONG_LIST);
    }
    
    /**
     * 展示某个歌单中的音乐.
     * */
    public void showMusics(int songListId){
        this.songListId = songListId;
        initFragment(SHOW_MUSIC);
    }
    
    /**
     * 碎片通过调用该方法可以得到登陆的用户.
     * */
    public User getUser(){
        return user;
    }
    
    /**
     * 碎片通过调用该方法获得用户点击的歌单的id.
     * */
    public int getSongListId(){
        return songListId;
    }
    
    @Override
    public void onBackPressed(){
        if(fragmentLinkedList.size() == 1){
            //只有一个碎片，说明只剩下主界面，接着就是关闭程序了
            super.onBackPressed();
        }else{
            //不止一个碎片，说明用户只是想返回上一个页面
            fragmentLinkedList.removeLast();
            Fragment fragment = fragmentLinkedList.getLast();
            int nowFragment = map.get(fragment);
            initFragment(nowFragment);
        }
    }
}
