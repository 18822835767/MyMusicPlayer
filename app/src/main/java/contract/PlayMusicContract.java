package contract;

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
        void unRegistOnPlayView();
    }

    /**
     * 这是playPresenter实现类将结果反馈给PlayMusicFragment的view接口，由PlayMusicFragment实现，
     * 达到更新UI的目的.
     * */
    interface OnPlayView{
        void onPlayStateChange(int state);//播放状态改变了，通知view层更新UI
        void onSeekChange(int seek);//通知view层更新进度条UI
    }
}
