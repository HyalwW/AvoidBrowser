package com.hyaline.avoidbrowser.data.beans;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BrowseHistoryBean {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String url;
    private String title;
    private long time;
    //暂时不存储icon


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
