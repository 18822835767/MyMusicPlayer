package com.example.www11.mymusicplayer.entity;

/**
 * 歌单对应的实体类.
 * */
public class SongList {
    /**
     * 歌单id
     * */
    private long id;
    
    /**
     * 歌单名字
     * */
    private String name;
    
    /**
     * 歌单的封面图片
     * */
    private String coverImgUrl;

    public SongList() {
    }

    public SongList(long id, String name,String coverImgUrl) {
        this.id = id;
        this.name = name;
        this.coverImgUrl = coverImgUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }
}
