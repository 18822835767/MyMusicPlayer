package entity;

public class Music {
    private String name;
    private long id;
    private String picUrl;
    private String singerName;
    private String musicURL = "";

    public Music() {
    }

    public Music(String name, long id, String picUrl,String singerName) {
        this.name = name;
        this.id = id;
        this.picUrl = picUrl;
        this.singerName = singerName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMusicURL() {
        return musicURL;
    }

    public void setMusicURL(String musicURL) {
        this.musicURL = musicURL;
    }
}
