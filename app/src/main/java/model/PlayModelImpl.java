package model;

import java.util.List;

import contract.PlayMusicContract;
import entity.Music;

public class PlayModelImpl implements PlayMusicContract.PlayModel {
    @Override
    public List<String> getMusicsUrl(List<Music> musics) {
        return null;
    }
}
