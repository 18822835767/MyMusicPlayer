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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.adapter.MusicAdapter;
import com.example.www11.mymusicplayer.contract.SearchContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.presenter.SearchPresenterImpl;
import com.example.www11.mymusicplayer.util.Constants;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.ERROR;
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.FAIL;
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.SUCCESS;

public class SearchFragment extends Fragment implements SearchContract.OnSearchView {
    private View view;
    private SearchContract.SearchPresenter mSearchPresenter;
    private EditText mSearchContent;
    private Button mSearchBtn;
    private ListView mListView;
    
    private static List<Music> mMusics;//存放搜索后得到的音乐列表
    private OnSearchListener mCallback;//碎片和活动通信的回调接口
    private Handler mHandler;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (OnSearchListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search,container,false);
        
        initData();
        initEvent();
        
        return view;
    }
    
    private void initData(){
        mSearchPresenter = new SearchPresenterImpl(this);
        
        mSearchContent = view.findViewById(R.id.search_content);
        mSearchBtn = view.findViewById(R.id.search_btn);
        mListView = view.findViewById(R.id.music_list);
        
        mHandler = new MyHandler(getActivity(),mListView);
    }
    
    private void initEvent(){
        //搜索按钮的点击事件
        mSearchBtn.setOnClickListener(v -> {
            String content = mSearchContent.getText().toString().trim();
            mSearchPresenter.searchMusic(content);
        });
        
        mListView.setOnItemClickListener((parent, view, position, id) -> 
                mCallback.playMusics(mMusics,position));
        
        
    }

    @Override
    public void showMusics(List<Music> music) {
        mMusics = music;
        Message message = Message.obtain();
        message.what = Constants.MusicConstant.SUCCESS;
        mHandler.sendMessage(message);
    }

    private static class MyHandler extends Handler {
        WeakReference<Activity> mActivity;
        WeakReference<ListView> mListView;

        MyHandler(Activity activity,ListView listView) {
            mActivity = new WeakReference<>(activity);
            mListView = new WeakReference<>(listView);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final Activity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case SUCCESS:
                            MusicAdapter adapter = new MusicAdapter(activity,R.layout.music_item,
                                    mMusics);
                            mListView.get().setAdapter(adapter);
                        break;
                    case FAIL:
                        break;
                    case ERROR:
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * MainActivity去实现，作为碎片和活动之间通信的回调接口.
     * */
    public interface OnSearchListener{
        //用户点击搜索的歌曲时，就把搜索列表中的歌以及歌的位置传出去
        void playMusics(List<Music> musics,int position);
    }
}
