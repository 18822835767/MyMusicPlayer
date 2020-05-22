package com.example.www11.mymusicplayer.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.adapter.MusicAdapter;
import com.example.www11.mymusicplayer.contract.SearchContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.presenter.SearchPresenterImpl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.LOAD_SUCCESS;
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.SEARCH_ERROR;
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.SEARCH_FAIL;
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.SEARCH_SUCCESS;

public class SearchFragment extends Fragment implements SearchContract.OnSearchView,
        AbsListView.OnScrollListener {
    private View view;
    private SearchContract.SearchPresenter mSearchPresenter;
    private EditText mSearchContent;
    private Button mSearchBtn;
    private ListView mListView;

    /**
     * 存放搜索后得到的音乐列表
     */
    private static List<Music> mMusics = new ArrayList<>();

    /**
     * 碎片和活动通信的回调接口
     */
    private OnSearchListener mCallback;

    /**
     * 分页加载的数量
     */
    private int mPageSize = 20;

    /**
     * 搜索界面当前是在第几面
     */
    private int mCurrentPage = 1;

    /**
     * 记录当前搜索的音乐的名字
     */
    private String mMusicName;

    /**
     * 上拉刷新时，是否已加载歌曲结束的标志
     */
    private boolean loadFinishFlag = true;
    
    private MusicAdapter mAdapter;
    private MyHandler mHandler;

    /**
     * 记录搜索的歌曲还有多少未被加载
     */
    private int mRemaining = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (OnSearchListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search, container, false);

        initData();
        initEvent();

        return view;
    }

    private void initData() {
        mSearchPresenter = new SearchPresenterImpl(this);

        mSearchContent = view.findViewById(R.id.search_content);
        mSearchBtn = view.findViewById(R.id.search_btn);
        mListView = view.findViewById(R.id.play_queue);

        //因为listview设置监听之前，需要先设置适配器，所以这里先设置适配器
        if (getActivity() != null) {
            mAdapter = new MusicAdapter(getActivity(), R.layout.music_item, mMusics);
        }
        mHandler = new MyHandler(this);

        //设置监听
        mListView.setOnScrollListener(this);
    }

    private void initEvent() {
        //搜索按钮的点击事件
        mSearchBtn.setOnClickListener(v -> {
            mCurrentPage = 1;//每次搜索音乐时，重置当前所在的页数
            mMusicName = mSearchContent.getText().toString().trim();
            mSearchPresenter.searchOrLoadMusic(mMusicName, mPageSize, (mCurrentPage - 1) * mPageSize);
        });

        mListView.setOnItemClickListener((parent, view, position, id) ->
                mCallback.playMusics(mMusics, position));
    }

    /**
     * 用户搜索音乐时回调该方法.
     */
    @Override
    public void showSearchMusics(int songCount, List<Music> musics) {
        mMusics.clear();
        mMusics.addAll(musics);

        //记录剩余歌曲的总数
        mRemaining = songCount - mPageSize;

        //更新当前页面数
        mCurrentPage = 1;

        //创建一个新的适配器
        if (getActivity() != null) {
            mAdapter = new MusicAdapter(getActivity(), R.layout.music_item, mMusics);
        }

        Message message = Message.obtain();
        message.what = SEARCH_SUCCESS;
        mHandler.sendMessage(message);
    }

    /**
     * 用户上拉刷新时，展示更多的歌曲.
     */
    @Override
    public void loadMoreMusics(List<Music> musics) {
        mMusics.addAll(musics);
        Message message = Message.obtain();
        message.what = LOAD_SUCCESS;
        mHandler.sendMessage(message);

        loadFinishFlag = true;//标志加载已结束
        mCurrentPage++;//页数+1
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * 正在滚动的时候调用该方法.
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //得到当前可见的最后一个item的下标,该item完全需要完全显现
        int lastVisibleItem = mListView.getLastVisiblePosition();
        if (lastVisibleItem + 1 == totalItemCount && mMusics.size() != 0) {//滚动到了最后一个
            if (loadFinishFlag) {//开始调用方法,加载。此处的标志，是为了防止多次加载。
                if (mRemaining >= mPageSize) {
                    //未加载的歌曲数目充足的情况下
                    loadFinishFlag = false;
                    Toast.makeText(getActivity(), "加载新歌曲...", Toast.LENGTH_SHORT).show();
                    mSearchPresenter.searchOrLoadMusic(mMusicName, mPageSize,
                            (mPageSize - 1) * mPageSize);
                    mRemaining = mRemaining - mPageSize;//剩余的未加载的歌曲数目
                } else if (mRemaining > 0 && mRemaining < 20) {
                    //未加载的歌曲数目不足的情况下
                    loadFinishFlag = false;
                    Toast.makeText(getActivity(), "加载新歌曲...", Toast.LENGTH_SHORT).show();
                    mSearchPresenter.searchOrLoadMusic(mMusicName, mPageSize,
                            mRemaining);
                    mRemaining = 0;//剩余的未加载的歌曲数目
                }
            }
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<SearchFragment> mWeakRef;

        MyHandler(SearchFragment fragment) {
            mWeakRef = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            SearchFragment fragment = mWeakRef.get();
            if (fragment != null) {
                switch (msg.what) {
                    case SEARCH_SUCCESS:
                        //设置新的适配器，刷新数据
                        fragment.mListView.setAdapter(fragment.mAdapter);
                        break;
                    case SEARCH_FAIL:
                        break;
                    case SEARCH_ERROR:
                        break;
                    case LOAD_SUCCESS:
                        fragment.mAdapter.notifyDataSetChanged();
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
    public interface OnSearchListener {
        //用户点击搜索的歌曲时，就把搜索列表中的歌以及歌的位置传出去
        void playMusics(List<Music> musics, int position);
    }
}
