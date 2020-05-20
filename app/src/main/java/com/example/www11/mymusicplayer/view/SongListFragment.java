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
import android.widget.ListView;
import android.widget.Toast;

import com.example.mymusicplayer.R;

import java.lang.ref.WeakReference;
import java.util.List;

import com.example.www11.mymusicplayer.adapter.MusicAdapter;
import com.example.www11.mymusicplayer.adapter.SongListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.www11.mymusicplayer.contract.SongListContract;
import com.example.www11.mymusicplayer.entity.SongList;
import com.example.www11.mymusicplayer.entity.User;
import com.example.www11.mymusicplayer.presenter.SongListPresenterImpl;
import com.example.www11.mymusicplayer.util.Constants;

import static com.example.www11.mymusicplayer.util.Constants.SongListConstant.ERROR;
import static com.example.www11.mymusicplayer.util.Constants.SongListConstant.FAIL;
import static com.example.www11.mymusicplayer.util.Constants.SongListConstant.SUCCESS;

/**
 * 歌单列表的碎片.
 */
public class SongListFragment extends Fragment implements SongListContract.OnSongListView {
    private View view;
    private User mUser;
    private SongListContract.SongListPresenter mSongListPresenter;
    private ListView mListView;
    private static List<SongList> mSongLists;
    private OnSongListListener mCallback;//碎片和活动通信的接口引用
    private Handler mHandler;

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

        mHandler = new MyHandler(getActivity(),mListView);
        
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
    private void setListItem() {
        mSongListPresenter.getUserSongList(mUser.getId());
    }

    @Override
    public void showSongList(List<SongList> songLists) {
        mSongLists = songLists;
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

    private static class MyHandler extends Handler {
        WeakReference<Activity> mActivity;
        WeakReference<ListView> mListView;

        MyHandler(Activity activity, ListView listView) {
            mActivity = new WeakReference<>(activity);
            mListView = new WeakReference<>(listView);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final Activity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case SUCCESS:
                        SongListAdapter adapter = new SongListAdapter(activity,
                                R.layout.song_list_item, mSongLists);
                        mListView.get().setAdapter(adapter);
                        break;
                    case FAIL:
                        Toast.makeText(activity, "请求失败",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        AlertDialog.Builder errorDialog = new AlertDialog.Builder(activity);
                        errorDialog.setTitle("错误");
                        errorDialog.setMessage("请求错误"+"\n"+msg.obj);
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
     * 当用户点击某张"歌单"的时候调用.
     * MainActivity去实现，作为碎片和活动之间通信的回调接口.
     */
    public interface OnSongListListener {
        void showMusics(long songListId);

        User getUser();
    }

}
