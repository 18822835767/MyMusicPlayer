package com.example.www11.mymusicplayer.homepage;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mymusicplayer.R;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.www11.mymusicplayer.homepage.HomePageContract;
import com.example.www11.mymusicplayer.homepage.HomePagePresenterImpl;
import com.example.www11.mymusicplayer.widget.BannerViewPager;

import static com.example.www11.mymusicplayer.util.Constants.HomePage.ERROR;

/**
 * 音乐首页所对应的view.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener,
        HomePageContract.OnView {
    /**
     * “我的歌单”按钮
     */
    private Button mMySongList;

    private View view;

    /**
     * 碎片和活动通信的接口引用
     */
    private OnHomePageListener mCallback;

    /**
     * 搜索的按钮
     */
    private ImageButton mSearchBtn;

    private HomePageContract.Presenter mHomePagePresenter;

    private BannerViewPager banner;

    private Handler mHandler;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (OnHomePageListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_page, container, false);

        initData();
        initEvent();

        return view;
    }

    /**
     * 初始化轮播图数据.
     */
    private void initData() {
        banner = view.findViewById(R.id.banner_view_pager);
        mHomePagePresenter = new HomePagePresenterImpl(this);
        mHomePagePresenter.getBanner();


        mMySongList = view.findViewById(R.id.my_song_list);
        mSearchBtn = view.findViewById(R.id.search);

        mHandler = new UIHandler(this);
    }

    private void initEvent() {
        mMySongList.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
    public void showBanner(List<Drawable> drawableList) {
        banner.setData(drawableList);
    }

    @Override
    public void showError(String errorMsg) {
        Message message = Message.obtain();
        message.what = ERROR;
        mHandler.sendMessage(message);
    }


    private static class UIHandler extends Handler {
        WeakReference<HomePageFragment> mWeakFragment;

        UIHandler(HomePageFragment homePageFragment) {
            mWeakFragment = new WeakReference<>(homePageFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            HomePageFragment fragment = mWeakFragment.get();
            if (fragment != null) {
                if (msg.what == ERROR) {
                    if (fragment.getActivity() != null) {
                        Toast.makeText(fragment.getActivity(), "轮播图加载失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * 当用户点击"我的歌单"或者“本地歌单”的按钮时候调用.
     * MainActivity去实现，作为碎片和活动之间通信的回调接口.
     */
    public interface OnHomePageListener {
        void showSongList();//展示歌单界面

        void showSearchPage();//展示搜索界面
    }
}
