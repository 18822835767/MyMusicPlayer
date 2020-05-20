package com.example.www11.mymusicplayer.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mymusicplayer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.www11.mymusicplayer.adapter.MusicAdapter;
import com.example.www11.mymusicplayer.contract.MusicContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.presenter.MusicPresenterImpl;

import static com.example.www11.mymusicplayer.util.Constants.MusicConstant.ERROR;
import static com.example.www11.mymusicplayer.util.Constants.MusicConstant.FAIL;
import static com.example.www11.mymusicplayer.util.Constants.MusicConstant.SUCCESS;

/**
 * 展示音乐列表.
 */
public class MusicFragment extends Fragment implements MusicContract.OnMusicView, 
        AbsListView.OnScrollListener {
    private ListView mListView;//展示音乐列表.
    private MusicContract.MusicPresenter mMusicPresenter;
    private List<Music> mMusics = new ArrayList<>();//保存列表中的音乐.
    private View view;

    private OnMusicListener mCallback;//碎片和活动通信的接口引用
    private MusicAdapter mAdapter;//适配器的引用变量
    private MyHandler mHandler;//Handler

    /**
     * 得到回调接口.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (OnMusicListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_music, container, false);

        initData();
        initEvent();

        return view;
    }

    /**
     * 初始化数据.
     */
    private void initData() {
        mListView = view.findViewById(R.id.music_list);

        mMusics = new ArrayList<>();

        mMusicPresenter = new MusicPresenterImpl(this);

        //因为listview设置监听之前，需要先设置适配器，所以这里先设置适配器
        if(getActivity() != null){
            mAdapter = new MusicAdapter(getActivity(),R.layout.music_item,mMusics);
        }
        mHandler = new MyHandler(this);
        mListView.setAdapter(mAdapter);

        //设置监听
        mListView.setOnScrollListener(this);
        
        //初始化音乐列表
        setMusicItem();
    }

    private void initEvent() {
        mListView.setOnItemClickListener((parent, view, position, id) ->
                mCallback.playMusics(mMusics, position));
    }

    /**
     * 设置歌单中的歌曲显示.
     */
    private void setMusicItem() {
        mMusicPresenter.getMusicList(mCallback.getSongListId());
    }

    /**
     * 展示音乐列表.
     */
    @Override
    public void showMusics(List<Music> musics) {
        mMusics.clear();
        mMusics.addAll(musics);
        //创建一个新的适配器
        if(getActivity() != null){
            mAdapter = new MusicAdapter(getActivity(),R.layout.music_item,mMusics);
        }
        
        Message message = Message.obtain();
        message.what = SUCCESS;
        mHandler.sendMessage(message);
    }

    /**
     * 展示音乐列表失败.
     */
    @Override
    public void showFail() {
        Message message = Message.obtain();
        message.what = FAIL;
        mHandler.sendMessage(message);
    }

    /**
     * 出现错误.
     */
    @Override
    public void showError() {
        Message message = Message.obtain();
        message.what = ERROR;
        mHandler.sendMessage(message);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private static class MyHandler extends Handler {
        WeakReference<MusicFragment> mWeakFragment;
        
        MyHandler(MusicFragment musicFragment) {
            mWeakFragment = new WeakReference<>(musicFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            MusicFragment musicFragment = mWeakFragment.get();
            if (musicFragment != null) {
                switch (msg.what) {
                    case SUCCESS:
                        //设置新的适配器，刷新数据
                        musicFragment.mListView.setAdapter(musicFragment.mAdapter);
                        break;
                    case FAIL:
                        Toast.makeText(musicFragment.getActivity(), "请求失败",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        AlertDialog.Builder errorDialog = new AlertDialog.Builder(
                                musicFragment.getActivity());
                        errorDialog.setTitle("错误");
                        errorDialog.setMessage("请求错误");
                        errorDialog.setPositiveButton("OK", (dialog, which) -> {
                        });
                        errorDialog.show();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * MainActivity去实现，作为碎片和活动之间通信的回调接口.
     */
    public interface OnMusicListener {
        long getSongListId();

        //用户点击歌单中的歌曲时，就把歌单中的歌以及歌的位置传出去
        void playMusics(List<Music> musics, int position);
    }
}
