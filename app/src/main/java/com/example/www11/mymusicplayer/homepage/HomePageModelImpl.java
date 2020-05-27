package com.example.www11.mymusicplayer.homepage;

import android.graphics.drawable.Drawable;

import com.example.www11.mymusicplayer.homepage.HomePageContract;
import com.example.www11.mymusicplayer.util.AsyncImageTask;
import com.example.www11.mymusicplayer.util.HttpUrlConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.www11.mymusicplayer.util.URLConstant.BANNER_URL;

public class HomePageModelImpl implements HomePageModel {
    private List<String> mBannerUrl = new ArrayList<>();
    private List<Drawable> mDrawableList = new ArrayList<>();//存放轮播图得到的图片
   
    @Override
    public void getBanner(HomePageModel.OnListener onHomePageListener) {
        HttpUrlConnection.sendHttpUrlConnection(BANNER_URL, new HttpUrlConnection.HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                try{
                    handleBannerJson(dataMessage);
                }catch (JSONException e){
                    e.printStackTrace();
                }
               
               for(int i=0;i<mBannerUrl.size();i++){
                   AsyncImageTask down = new AsyncImageTask(drawable -> {
                       mDrawableList.add(drawable);
                       
                       if(mDrawableList.size() == mBannerUrl.size()){
                           onHomePageListener.onSuccess(mDrawableList);
                       }
                   });
                   down.execute(mBannerUrl.get(i));
               }
                
            }

            @Override
            public void onFail() {

            }

            @Override
            public void onError(String errorMsg) {
                onHomePageListener.onError(errorMsg);
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
