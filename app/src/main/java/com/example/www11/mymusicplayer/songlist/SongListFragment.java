package com.example.www11.mymusicplayer.songlist;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mymusicplayer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.example.www11.mymusicplayer.adapter.SongListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.www11.mymusicplayer.entity.SongList;
import com.example.www11.mymusicplayer.entity.User;

import static com.example.www11.mymusicplayer.util.Constants.SongListConstant.ERROR;
import static com.example.www11.mymusicplayer.util.Constants.SongListConstant.FAIL;
import static com.example.www11.mymusicplayer.util.Constants.SongListConstant.SUCCESS;

/**
 * 歌单列表的碎片.
 */
public class SongListFragment extends Fragment implements SongListContract.OnView {
    private View view;
    private User mUser;
    private SongListContract.Presenter mSongListPresenter;
    private ListView mListView;
    private List<SongList> mSongLists = new ArrayList<>();
    private Handler mHandler;

    private SongListAdapter mAdapter;

    /**
     * 标记歌单的加载是否成功.
     */
    private boolean mLoadSuccess = false;

    /**
     * 碎片和活动通信的接口引用
     */
    private OnSongListListener mCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (OnSongListListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_song_list, container, false);

        initData();
        initEvent();

        return view;
    }

    private void initData() {
        mUser = mCallback.getUser();
        mListView = view.findViewById(R.id.song_list);

        //初始化presenter
        mSongListPresenter = new SongListPresenterImpl(this);

        mHandler = new UIHandler(this);

        if (getActivity() != null) {
            mAdapter = new SongListAdapter(getActivity(),
                    R.layout.song_list_item, mSongLists);
            mListView.setAdapter(mAdapter);
        }

        setListItem();
    }

    private void initEvent() {
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            SongList songList = mSongLists.get(position);
            if (mCallback != null) {
                mCallback.showMusics(songList.getId());
            }
        });
    }

    /**
     * 设置歌单列表每一项的数据.
     */
    public void setListItem() {
        if (!mLoadSuccess) {
            mSongLists.clear();//先清空列表项数据
            mAdapter.notifyDataSetChanged();
            mSongListPresenter.getUserSongList(mUser.getId());
        }
    }

    @Override
    public void showSongList(List<SongList> songLists) {
        mLoadSuccess = true;//标记歌单加载成功，防止重复加载
        mSongLists.clear();
        mSongLists.addAll(songLists);
        Message message = Message.obtain();
        message.what = SUCCESS;
        mHandler.sendMessage(message);
    }

    @Override
    public void showFail() {
        Message message = Message.obtain();
        message.what = FAIL;
        mHandler.sendMessage(message);
    }

    @Override
    public void showError(String errorMsg) {
        Message message = Message.obtain();
        message.what = ERROR;
        message.obj = errorMsg;
        mHandler.sendMessage(message);
    }

    private static class UIHandler extends Handler {
        WeakReference<SongListFragment> mWeakRef;

        UIHandler(SongListFragment fragment) {
            mWeakRef = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            SongListFragment fragment = mWeakRef.get();
            if (fragment != null) {
                switch (msg.what) {
                    case SUCCESS:
                        if (fragment.getActivity() != null) {
                            fragment.mAdapter.notifyDataSetChanged();
                        }
                        break;
                    case FAIL:
                        if (fragment.getActivity() != null) {
                            Toast.makeText(fragment.getActivity(), "请求失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case ERROR:
                        if (fragment.getActivity() != null) {
                            AlertDialog.Builder errorDialog = new AlertDialog.Builder(fragment.getActivity());
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
     * 当用户点击某张"歌单"的时候调用.
     * MainActivity去实现，作为碎片和活动之间通信的回调接口.
     */
    public interface OnSongListListener {
        void showMusics(long songListId);

        User getUser();
    }

}
