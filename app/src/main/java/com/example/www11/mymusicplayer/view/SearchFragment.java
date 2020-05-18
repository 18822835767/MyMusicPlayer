package com.example.www11.mymusicplayer.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.ERROR;
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.FAIL;
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.SUCCESS;

public class SearchFragment extends Fragment implements SearchContract.OnSearchView, 
        AbsListView.OnScrollListener {
    private View view;
    private SearchContract.SearchPresenter mSearchPresenter;
    private EditText mSearchContent;
    private Button mSearchBtn;
    private ListView mListView;
    private View mFooterView;//上拉刷新时的底部view

    private static List<Music> mMusics = new ArrayList<>();//存放搜索后得到的音乐列表
    private OnSearchListener mCallback;//碎片和活动通信的回调接口
    private int pageSize = 20; //分页加载的数量
    private int currentPage = 1;//搜索界面当前是在第几面
    private String mMusicName;//记录当前搜索的音乐的名字
    private boolean loadFinishFlag = true;//上拉刷新时，是否已加载歌曲结束的标志
    private MusicAdapter mAdapter;
    private MyHandler mHandler;
    
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
        mFooterView = getLayoutInflater().inflate(R.layout.footer,null);
//        mFooterView = inflater.inflate(R.layout.footer,container,false);还不明白为什么这个不行
        
        initData();
        initEvent();

        return view;
    }

    private void initData() {
        mSearchPresenter = new SearchPresenterImpl(this);

        mSearchContent = view.findViewById(R.id.search_content);
        mSearchBtn = view.findViewById(R.id.search_btn);
        mListView = view.findViewById(R.id.music_list);

        //因为listview设置监听之前，需要先设置适配器，所以这里先设置适配器
        if(getActivity() != null){
            mAdapter = new MusicAdapter(getActivity(),R.layout.music_item,mMusics);
        }
        mHandler = new MyHandler(getActivity(), mListView);
        mHandler.setAdapter(mAdapter);

        //设置监听
        mListView.setOnScrollListener(this);
    }

    private void initEvent() {
        //搜索按钮的点击事件
        mSearchBtn.setOnClickListener(v -> {
            currentPage = 1;//每次搜索音乐时，重置当前所在的页数
            mMusicName = mSearchContent.getText().toString().trim();
            mSearchPresenter.searchMusic(mMusicName,pageSize,(currentPage-1)*pageSize);
        });

        mListView.setOnItemClickListener((parent, view, position, id) ->
                mCallback.playMusics(mMusics, position));
    }

    /**
     * 用户搜索音乐时回调该方法.
     * */
    @Override
    public void showSearchMusics(List<Music> musics) {
        mMusics.clear();
        mMusics.addAll(musics);
        //创建一个新的适配器
        if(getActivity() != null){
            mAdapter = new MusicAdapter(getActivity(),R.layout.music_item,mMusics);
        }
        //Handler类中设置新的适配器
        mHandler.setAdapter(mAdapter);

        Message message = Message.obtain();
        message.what =SUCCESS;
        mHandler.sendMessage(message);
    }

    /**
     * 用户上拉刷新时，展示更多的歌曲.
     * */
    @Override
    public void loadMoreMusics(List<Music> musics) {
        mMusics.addAll(musics);
        Message message = Message.obtain();
        message.what =SUCCESS;
        mHandler.sendMessage(message);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                mAdapter.setScrolling(false);
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                mAdapter.setScrolling(true);
                break;
        }
    }

    /**
     * 正在滚动的时候调用该方法.
     * */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //得到当前可见的最后一个item的下标,该item完全需要完全显现
        int lastVisibleItem = mListView.getLastVisiblePosition();
        if(lastVisibleItem + 1 == totalItemCount && mMusics.size() != 0){//滚动到了最后一个
            if(loadFinishFlag){//开始调用方法,加载。此处的标志，是为了防止多次加载。
                loadFinishFlag = false;
                mListView.addFooterView(mFooterView);
            }
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<Activity> mWeekActivity;
        WeakReference<ListView> mWeekListView;
        WeakReference<Adapter> mWeekAdapter;

        MyHandler(Activity activity, ListView listView) {
            mWeekActivity = new WeakReference<>(activity);
            mWeekListView = new WeakReference<>(listView);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final Activity activity = mWeekActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case SUCCESS:
                        //设置新的适配器，刷新数据
                        mWeekListView.get().setAdapter((MusicAdapter)mWeekAdapter.get());
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
        
        void setAdapter(MusicAdapter adapter){
            mWeekAdapter = new WeakReference<>(adapter);
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
