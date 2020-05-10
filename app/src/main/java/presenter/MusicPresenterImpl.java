package presenter;

import android.util.Log;

import java.util.List;

import contract.MusicContract;
import entity.Music;
import model.MusicModelImpl;

public class MusicPresenterImpl implements MusicContract.MusicPresenter, 
        MusicContract.OnMusicListener {
    private MusicContract.MusicModel musicModel;
    private MusicContract.OnMusicView onMusicView;

    public MusicPresenterImpl(MusicContract.OnMusicView onMusicView){
        this.onMusicView = onMusicView;
        musicModel = new MusicModelImpl();
    }
    
    @Override
    public void getMusicList(int songListId) {
        musicModel.getMusicList(this,songListId);
    }

    @Override
    public void onSuccess(List<Music> musics) {
        onMusicView.showMusics(musics);
    }

    @Override
    public void onError() {

    }
}
