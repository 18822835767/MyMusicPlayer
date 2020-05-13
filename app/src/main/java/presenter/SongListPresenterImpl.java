package presenter;

import java.util.List;

import contract.SongListContract;
import entity.SongList;
import model.SongListModelImpl;

public class SongListPresenterImpl implements SongListContract.SongListPresenter,
        SongListContract.OnSongListListener {
    private SongListContract.SongListModel mSongListModel;
    private SongListContract.OnSongListView mOnSongListView;
    
    public SongListPresenterImpl(SongListContract.OnSongListView onSongListView){
        this.mOnSongListView = onSongListView;
        mSongListModel = new SongListModelImpl();
    }
    
    @Override
    public void getUserSongList(int id) {
        mSongListModel.getUserSongList(this,id);
    }

    @Override
    public void onSuccess(List<SongList> songLists) {
        mOnSongListView.showSongList(songLists);        
    }

    @Override
    public void onFail() {
        mOnSongListView.showFail();
    }

    @Override
    public void onError() {
        mOnSongListView.showError();
    }
    
}
