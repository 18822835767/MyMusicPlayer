package com.example.www11.mymusicplayer.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.mymusicplayer.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.www11.mymusicplayer.contract.HomePageContract;
import com.example.www11.mymusicplayer.presenter.HomePagePresenterImpl;
import com.example.www11.mymusicplayer.widget.BannerViewPager;

/**
 * 音乐首页所对应的view.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener,
        HomePageContract.OnView {
    /**
     * “我的歌单”按钮
     * */
    private Button mMySongList;
    
    private View view;
    
    /**
     * 碎片和活动通信的接口引用
     * */
    private OnHomePageListener mCallback;
    
    /**
     * 搜索的按钮
     * */
    private ImageButton mSearchBtn;
    
    private HomePageContract.Presenter mHomePagePresenter;

    private BannerViewPager banner;
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (OnHomePageListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.home_page,container,false);  
        
        initData();
        initEvent();

        return view;
    }

    /**
     * 初始化轮播图数据.
     * */
    private void initData() {
        //为轮播图设置数据
//        BannerViewPager banner = view.findViewById(R.id.banner_view_pager);
//        List<Integer> imageUrl = new ArrayList<>();
//        imageUrl.add(R.drawable.one);
//        imageUrl.add(R.drawable.two);
//        imageUrl.add(R.drawable.thr);
//        banner.setData(imageUrl);
        banner = view.findViewById(R.id.banner_view_pager);
        mHomePagePresenter = new HomePagePresenterImpl(this);
        mHomePagePresenter.getBanner();
        

        mMySongList = view.findViewById(R.id.my_song_list);
        mSearchBtn = view.findViewById(R.id.search);
    }

    private void initEvent() {
        mMySongList.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_song_list:
                mCallback.showSongList();
                break;
            case R.id.search:
                mCallback.showSearchPage();
                break;
            default:
                break;
        }
    }

    @Override
    public void showBanner(List<ImageView> imageList) {
        banner.setData(imageList);
    }

    /**
     * 当用户点击"我的歌单"或者“本地歌单”的按钮时候调用.
     * MainActivity去实现，作为碎片和活动之间通信的回调接口.
     * */
    public interface OnHomePageListener {
        void showSongList();//展示歌单界面
        void showSearchPage();//展示搜索界面
    }
}
