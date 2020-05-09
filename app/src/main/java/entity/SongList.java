package entity;

/**
 * 歌单对应的实体类.
 * */
public class SongList {
    private int id;
    private String name;
    private int coverImgUrl;//歌单的封面图片

    public SongList() {
    }

    public SongList(int id, String name,int coverImgUrl) {
        this.id = id;
        this.name = name;
        this.coverImgUrl = coverImgUrl;
    }

    public int getId() {
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

    public int getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(int coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }
}
