package com.example.www11.mymusicplayer.presenter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.www11.mymusicplayer.contract.HomePageContract;
import com.example.www11.mymusicplayer.model.HomePageModelImpl;

import java.util.List;

public class HomePagePresenterImpl implements HomePageContract.Presenter,
        HomePageContract.OnListener {
    private HomePageContract.Model mHomePageModel;
    private HomePageContract.OnView mOnHomePageView;

    public HomePagePresenterImpl(HomePageContract.OnView onHomePageView){
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
}
