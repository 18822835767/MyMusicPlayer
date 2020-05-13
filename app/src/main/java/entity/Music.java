package entity;

public class Music {
    private String name;
    private int id;
    private String picUrl;

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    private String singerName;

    public Music() {
    }

    public Music(String name, int id, String picUrl,String singerName) {
        this.name = name;
        this.id = id;
        this.picUrl = picUrl;
        this.singerName = singerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
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
}
