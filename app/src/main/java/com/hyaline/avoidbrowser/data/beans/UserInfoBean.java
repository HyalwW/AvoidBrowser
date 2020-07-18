package com.hyaline.avoidbrowser.data.beans;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserInfoBean {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String account;
    private String alias;
    private String homeUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }
}
