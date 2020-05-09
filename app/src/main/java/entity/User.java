package entity;

import java.io.Serializable;

/**
 * 用户的实体类.
 * */
public class User implements Serializable {
    private int id;
    private String nickName;

    public User() {
    }

    public User(int id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
