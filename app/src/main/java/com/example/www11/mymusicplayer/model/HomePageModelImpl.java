package com.example.www11.mymusicplayer.model;

import android.widget.ImageView;

import com.example.www11.mymusicplayer.contract.HomePageContract;
import com.example.www11.mymusicplayer.util.ApplicationContext;
import com.example.www11.mymusicplayer.util.AsyncImageTask;
import com.example.www11.mymusicplayer.util.HttpCallbackListener;
import com.example.www11.mymusicplayer.util.HttpUrlConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.www11.mymusicplayer.util.Constants.URLConstant.BANNER_URL;

public class HomePageModelImpl implements HomePageContract.HomePageModel {
    private List<String> mBannerUrl = new ArrayList<>();
    private List<ImageView> mImageList = new ArrayList<>();//存放轮播图得到的图片
   
    @Override
    public void getBanner(HomePageContract.OnHomePageListener onHomePageListener) {
        HttpUrlConnection.sendHttpUrlConnection(BANNER_URL, new HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                try{
                    handleBannerJson(dataMessage);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                //todo 一般不在非View层使用到View层的东西，如ImageView等控件
               for(int i=0;i<mBannerUrl.size();i++){
                   AsyncImageTask down = new AsyncImageTask(drawable -> {
                       ImageView imageView = new ImageView(ApplicationContext.getContext());
                       imageView.setImageDrawable(drawable);
                       mImageList.add(imageView);
                   });
                   down.execute(mBannerUrl.get(i));
               }
                
                onHomePageListener.onSuccess(mImageList);
            }

            @Override
            public void onFail() {

            }

            @Override
            public void onError(String errorMsg) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }
    
    private void handleBannerJson(String dataMessage) throws JSONException {
        JSONObject object = new JSONObject(dataMessage);
        JSONArray banners = object.getJSONArray("banners");
        
        for(int i=0;i < banners.length();i++){
            JSONObject banner = banners.getJSONObject(i);
            mBannerUrl.add(banner.getString("pic"));
        }
    }
}
