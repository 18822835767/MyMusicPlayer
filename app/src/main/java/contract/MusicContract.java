package contract;

import org.json.JSONException;
import java.util.List;
import entity.Music;

public interface MusicContract {
    /**
     * MusicModel接口，被MusicPresenter调用.
     * */
    interface MusicModel{
        void getMusicList(OnMusicListener onMusicListener, int songListId);
        void handleJson(String dataMessage) throws JSONException;
    }

    /**
     * MusicPresenter接口，被MusicFragment调用.
     * */
    interface MusicPresenter{
        void getMusicList(int songListId);
    }

    /**
     * 这是将MusicModel请求结果反馈给MusicPresenter的Callback接口.
     * MusicPresenter要去实现这个接口.
     * */
    interface OnMusicListener {
        /**
         * 成功得到歌单中的歌曲.
         * */
        void onSuccess(List<Music> musics);

        /**
         * 没能成功得到歌单中的歌曲.
         * */
        void onFail();
        
        /**
         * 错误(断网...).
         * */
        void onError();
    }

    /**
     * MusicPresenter将结果反馈给MusicActivity的View接口.
     * 由MusicFragment去实现这个类.
     * */
    interface OnMusicView {
        /**
         * 展示用户歌单.
         * */
        void showMusics(List<Music> music);

        /**
         * 没能成功得到歌单中的歌曲.
         * */
        void showFail();
        
        /**
         * 错误(断网等情况...).
         * */
        void showError();
    }
}
