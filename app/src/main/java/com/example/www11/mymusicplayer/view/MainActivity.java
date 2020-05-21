package com.example.www11.mymusicplayer.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.entity.User;
import com.example.www11.mymusicplayer.service.PlayService;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.util.ThreadPool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.www11.mymusicplayer.util.Constants.LoginConstant.USER;
import static com.example.www11.mymusicplayer.util.Constants.MainConstant.REQUEST_CODE;
import static com.example.www11.mymusicplayer.util.Constants.MainConstant.SHOW_HOME_PAGE;
import static com.example.www11.mymusicplayer.util.Constants.MainConstant.SHOW_MUSIC;
import static com.example.www11.mymusicplayer.util.Constants.MainConstant.SHOW_SEARCH;
import static com.example.www11.mymusicplayer.util.Constants.MainConstant.SHOW_SONG_LIST;

/**
 * todo
 *  1、对于歌单列表以及歌曲列表的图片，明明图片很小却总是完全加载，浪费流量，考虑下怎么优化
 *  2、对于歌单列表以及歌曲列表的图片，可以考虑做下三级缓存
 *  3、每次打开应用都得重新登录，考虑下怎么处理
 *  4、关闭应用的时候音乐服务仍在运行（理应这样子），这时候重开应用会crash，考虑下怎么解决。
 * 程序的主界面.
 */
public class MainActivity extends AppCompatActivity implements HomePageFragment.OnHomePageListener,
        SongListFragment.OnSongListListener, MusicFragment.OnMusicListener, SearchFragment.OnSearchListener {

    //三个碎片
    private HomePageFragment mHomePageFragment = null;
    private SongListFragment mSongListFragment = null;
    private MusicFragment mMusicFragment = null;
    private SearchFragment mSearchFragment = null;

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
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    /**
     * 初始化数据.
     */
    private void initData() {
        //得到登陆的用户
        Intent intent = getIntent();
        mUser = (User) intent.getSerializableExtra(USER);

        mFragmentLinkedList = new LinkedList<>();

        mPlayMusicFragment = (PlayMusicFragment) getSupportFragmentManager().findFragmentById
                (R.id.play_music_fragment);
    }

    /**
     * 开启服务.
     */
    private void initService() {
        Intent intent = new Intent(MainActivity.this, PlayService.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(intent);
        }else{
            startService(intent);
        }
    
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
            //展示音乐首页
            case SHOW_HOME_PAGE:
                if (mHomePageFragment == null) {
                    mHomePageFragment = new HomePageFragment();
                    transaction.add(R.id.fragment_layout, mHomePageFragment);
                    //保存碎片所对应的值
                    mMap.put(mHomePageFragment, SHOW_HOME_PAGE);
                } else {
                    transaction.show(mHomePageFragment);
                }

                addFragment(mHomePageFragment);
                break;
            //展示歌单
            case SHOW_SONG_LIST:
                if (mSongListFragment == null) {
                    mSongListFragment = new SongListFragment();
                    transaction.add(R.id.fragment_layout, mSongListFragment);
                    //保存碎片所对应的值
                    mMap.put(mSongListFragment, SHOW_SONG_LIST);
                } else {
                    transaction.show(mSongListFragment);
                }

                addFragment(mSongListFragment);
                break;
            //展示音乐列表
            case SHOW_MUSIC:
                if (mMusicFragment == null) {
                    mMusicFragment = new MusicFragment();
                    transaction.add(R.id.fragment_layout, mMusicFragment);
                    //保存碎片所对应的值
                    mMap.put(mMusicFragment, SHOW_MUSIC);
                } else {
                    transaction.show(mMusicFragment);
                }

                addFragment(mMusicFragment);
                break;
            //展示搜索界面
            case SHOW_SEARCH:
                if (mSearchFragment == null) {
                    mSearchFragment = new SearchFragment();
                    transaction.add(R.id.fragment_layout, mSearchFragment);
                    //保存碎片所对应的值
                    mMap.put(mSearchFragment, SHOW_SEARCH);
                } else {
                    transaction.show(mSearchFragment);
                }

                addFragment(mSearchFragment);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 往mFragmentLinkedList中添加碎片.
     */
    private void addFragment(Fragment fragment) {
        //如果List不包含，则添加碎片
        if (!mFragmentLinkedList.contains(fragment)) {
            mFragmentLinkedList.addLast(fragment);
        }
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
        if (mSearchFragment != null) {
            transaction.hide(mSearchFragment);
        }
    }

    /**
     * 展示用户的歌单的“碎片”.
     */
    @Override
    public void showSongList() {
        initFragment(SHOW_SONG_LIST);
    }

    @Override
    public void showSearchPage() {
        initFragment(SHOW_SEARCH);
    }

    /**
     * 展示某个歌单中的音乐"碎片".
     */
    @Override
    public void showMusics(long songListId) {
        mSongListId = songListId;
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
     */
    @Override
    public long getSongListId() {
        return mSongListId;
    }

    /**
     * 用户点击歌单中的音乐时，则调用碎片中相应的方法播放音乐.
     */
    @Override
    public void playMusics(List<Music> musics, int position) {
        if (mPlayMusicFragment != null) {
            mPlayMusicFragment.playMusics(musics, position);
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
            int nowFragment = -1;
            if (mMap != null) {
                Integer integer = mMap.get(fragment);
                if (integer != null) {
                    nowFragment = integer;
                }
            }
            initFragment(nowFragment);
        }
    }

    /**
     * 处理权限的结果.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "拒绝权限将无法使用该程序", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * 通过这个方式来启动活动.
     */
    public static void actionStart(Context context, User user) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER, user);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadPool.shutDownPool();
    }

}
