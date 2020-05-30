package com.example.www11.mymusicplayer.musiclist;

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
import com.example.www11.mymusicplayer.entity.Music;

import static com.example.www11.mymusicplayer.util.Constants.MusicConstant.ERROR;
import static com.example.www11.mymusicplayer.util.Constants.MusicConstant.FAIL;
import static com.example.www11.mymusicplayer.util.Constants.MusicConstant.SUCCESS;

/**
 * 用户点击歌单时，展示"音乐列表"的碎片.
 */
public class MusicListFragment extends Fragment implements MusicListContract.OnView{
    /**
     * 展示音乐列表.
     */
    private ListView mListView;

    private MusicListContract.Presenter mMusicPresenter;

    /**
     * 保存列表中的音乐.
     */
    private List<Music> mMusics = new ArrayList<>();

    private View view;

    /**
     * 碎片和活动通信的接口引用
     */
    private OnMusicListener mCallback;

    /**
     * listView适配器.
     */
    private MusicAdapter mAdapter;

    private UIHandler mHandler;

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
        mListView = view.findViewById(R.id.play_queue);

        mMusics = new ArrayList<>();

        mMusicPresenter = new MusicListPresenterImpl(this);

        //listView设置设置适配器
        if (getActivity() != null) {
            mAdapter = new MusicAdapter(getActivity(), R.layout.music_item, mMusics);
        }
        mHandler = new UIHandler(this);
        mListView.setAdapter(mAdapter);

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
    public void setMusicItem() {
        //先清空列表项数据
        mMusics.clear();
        mAdapter.notifyDataSetChanged();

        mMusicPresenter.getMusicList(mCallback.getSongListId());
    }

    /**
     * 展示音乐列表.
     */
    @Override
    public void showMusics(List<Music> musics) {
        mMusics.clear();
        mMusics.addAll(musics);
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
    public void showError(String errorMsg) {
        Message message = Message.obtain();
        message.what = ERROR;
        message.obj = errorMsg;
        mHandler.sendMessage(message);
    }

    private static class UIHandler extends Handler {
        WeakReference<MusicListFragment> mWeakFragment;

        UIHandler(MusicListFragment musicFragment) {
            mWeakFragment = new WeakReference<>(musicFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            MusicListFragment musicFragment = mWeakFragment.get();
            if (musicFragment != null) {
                switch (msg.what) {
                    case SUCCESS:
                        musicFragment.mAdapter.notifyDataSetChanged();
                        break;
                    case FAIL:
                        if (musicFragment.getActivity() != null) {
                            Toast.makeText(musicFragment.getActivity(), "请求失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case ERROR:
                        if (musicFragment.getActivity() != null) {
                            AlertDialog.Builder errorDialog = new AlertDialog.Builder(
                                    musicFragment.getActivity());
                            errorDialog.setTitle("错误");
                            errorDialog.setMessage("请求错误" + "\n" + msg.obj);
                            errorDialog.setPositiveButton("OK", (dialog, which) -> {
                            });
                            errorDialog.show();
                        }
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
