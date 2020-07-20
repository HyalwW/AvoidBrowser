package com.hyaline.avoidbrowser.data.beans;

import androidx.annotation.Nullable;
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


    public BrowseHistoryBean() {
        time = System.currentTimeMillis();
    }

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

    public BrowseHistoryBean clone() {
        BrowseHistoryBean clone = new BrowseHistoryBean();
        clone.setId(id);
        clone.setTime(time);
        clone.setTitle(title);
        clone.setUrl(url);
        return clone;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof BrowseHistoryBean) {
            BrowseHistoryBean target = (BrowseHistoryBean) obj;
            return id == target.id && time == target.time && url.equals(target.title);
        }
        return false;
    }
}
