package com.example.www11.mymusicplayer.presenter;

import android.widget.ImageView;

import com.example.www11.mymusicplayer.contract.HomePageContract;
import com.example.www11.mymusicplayer.model.HomePageModelImpl;

import java.util.List;

public class HomePagePresenterImpl implements HomePageContract.HomePagePresenter,
        HomePageContract.OnHomePageListener {
    private HomePageContract.HomePageModel mHomePageModel;
    private HomePageContract.OnHomePageView mOnHomePageView;

    public HomePagePresenterImpl(HomePageContract.OnHomePageView onHomePageView){
        mOnHomePageView = onHomePageView;
        mHomePageModel = new HomePageModelImpl();
    }
    
    @Override
    public void getBanner() {
        mHomePageModel.getBanner(this);
    }

    @Override
    public void onSuccess(List<ImageView> imageList) {
        mOnHomePageView.showBanner(imageList);
    }
}
