package com.example.www11.mymusicplayer.model;

import com.example.www11.mymusicplayer.contract.HomePageContract;
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
                
                onHomePageListener.onSuccess(mBannerUrl);
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
