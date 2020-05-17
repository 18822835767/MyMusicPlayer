package com.example.www11.mymusicplayer.contract;

/**
 * 搜索相关操作所对应的契约类.
 * */
public interface SearchContract {
    /**
     * SearchModel接口，被SearchPresenter调用.
     * */
    interface SearchModel{
        
    }
    
    /**
     * SearchPresenter接口，被SearchFragment调用.
     * */
    interface SearchPresenter{
        
    }
    
    /**
     * 这是将SearchModel请求结果反馈给SearchPresenter的Callback接口.
     * SearchPresenter要去实现这个接口.
     * */
    interface OnSearchListener{
        
    }

    /**
     * SearchPresenter将登陆结果反馈给SearchFragment的View接口.
     * 由SearchFragment去实现这个类.
     * */
    interface OnSearchView{
        
    }
}
