package com.example.www11.mymusicplayer.homepage;

import android.graphics.drawable.Drawable;

import java.util.List;

public class HomePagePresenterImpl implements HomePageContract.Presenter,
        HomePageModel.OnListener {
    private HomePageModel mHomePageModel;
    private HomePageContract.OnView mOnHomePageView;

    HomePagePresenterImpl(HomePageContract.OnView onHomePageView){
        mOnHomePageView = onHomePageView;
        mHomePageModel = new HomePageModelImpl();
    }
    
    @Override
    public void getBanner() {
        mHomePageModel.getBanner(this);
    }

    @Override
    public void onSuccess(List<Drawable> drawableList) {
        mOnHomePageView.showBanner(drawableList);
    }

    @Override
    public void onError(String errorMsg) {
        mOnHomePageView.showError(errorMsg);
    }

}
