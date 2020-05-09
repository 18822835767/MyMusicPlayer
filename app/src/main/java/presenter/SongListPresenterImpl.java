package presenter;

import java.util.List;

import contract.SongListContract;
import entity.SongList;
import model.SongListModelImpl;

public class SongListPresenterImpl implements SongListContract.SongListPresenter,
        SongListContract.onSongListListener {
    private SongListContract.SongListModel songListModel;
    private SongListContract.onSongListView onSongListView;
    
    public SongListPresenterImpl(SongListContract.onSongListView onSongListView){
        this.onSongListView = onSongListView;
        songListModel = new SongListModelImpl();
    }
    
    @Override
    public void getUserSongList(int id) {
        songListModel.getUserSongList(this,id);
    }

    @Override
    public void onSuccess(List<SongList> songLists) {
        onSongListView.showSongList(songLists);        
    }

    @Override
    public void onError() {
        onSongListView.showError();
    }
}
