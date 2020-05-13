package contract;

import android.media.MediaPlayer;

import java.util.List;

import entity.Music;

public interface PlayMusicContract {
    /**
     * 这是PlayPresenter接口，给PlayMusicFragment调用.
     * */
    interface PlayPresenter{
        //表示播放状态
        public static final int PLAY_STATE_PLAY = 1;//播放
        public static final int PLAY_STATE_PAUSE = 2;//暂停
        public static final int PLAY_STATE_STOP = 3;//停止
        
        void playOrPause();//控制音乐的播放或者暂停
        void seekTo(int seek);//控制音乐的播放进度
        void registOnPlayView(OnPlayView onPlayView);
        MediaPlayer getMediaPlayer();
        void playNext();
        void playPre();
        void playMusic(List<Music> musics,int position);//用户点播音乐时,传入歌所在的列表,以及该歌的位置
    }

    /**
     * 这是playPresenter实现类将结果反馈给PlayMusicFragment的view接口，由PlayMusicFragment实现，
     * 达到更新UI的目的.
     * */
    interface OnPlayView{
        void onPlayStateChange(int state);//播放状态改变了，通知view层更新UI
        void onSeekChange(int seek);//通知view层更新进度条UI
        void showError();//播放出现错误时给用户提示
        void showFail(String msg);
    }
    
    interface PlayModel{
        //获取音乐播放的URL
        List<String> getMusicsUrl(List<Music> musics);
    }
}
