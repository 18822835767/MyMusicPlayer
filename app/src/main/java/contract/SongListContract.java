package contract;

import org.json.JSONException;

import java.util.List;

import entity.SongList;

public interface SongListContract {
    /**
     * SongListModel接口，被SongListPresenter调用.
     * */
    interface SongListModel{
       void getUserSongList(SongListContract.onSongListListener onSongListListener, int userId);
       void handleJson(String dataMessage) throws JSONException;
    }
    
    /**
     * SongListPresenter接口，被SongListActivity调用.
     * */
    interface SongListPresenter{
        void getUserSongList(int userId);
    }
    
    /**
     * 这是将SongListModel请求结果反馈给SongListPresenter的Callback接口.
     * SongListPresenter要去实现这个接口.
     * */
    interface onSongListListener{
        /**
         * 成功得到歌单.
         * */
        void onSuccess(List<SongList> songLists);
        
        /**
         * 错误(断网...).
         * */
        void onError();
    }
    
    /**
     * SongListPresenter将结果反馈给SongListActivity的View接口.
     * 由SongListActivity去实现这个类.
     * */
    interface onSongListView{
        /**
         * 展示用户歌单.
         * */
        void showSongList(List<SongList> songLists);
        
        /**
         * 错误(断网等情况...).
         * */
        void showError();
    }
}
