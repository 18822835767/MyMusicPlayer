package presenter;

import java.util.List;

import contract.MusicContract;
import entity.Music;
import model.MusicModelImpl;

public class MusicPresenterImpl implements MusicContract.MusicPresenter, 
        MusicContract.OnMusicListener {
    private MusicContract.MusicModel mMusicModel;
    private MusicContract.OnMusicView mOnMusicView;

    public MusicPresenterImpl(MusicContract.OnMusicView onMusicView){
        this.mOnMusicView = onMusicView;
        mMusicModel = new MusicModelImpl();
    }
    
    @Override
    public void getMusicList(long songListId) {
        mMusicModel.getMusicList(this,songListId);
    }

    @Override
    public void onSuccess(List<Music> musics) {
        mOnMusicView.showMusics(musics);
    }

    @Override
    public void onFail() {
        mOnMusicView.showFail();
    }

    @Override
    public void onError() {
        mOnMusicView.showError();
    }
}
