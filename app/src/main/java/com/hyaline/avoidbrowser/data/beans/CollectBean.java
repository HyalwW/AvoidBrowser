package com.hyaline.avoidbrowser.data.beans;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CollectBean {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof CollectBean) {
            CollectBean other = (CollectBean) obj;
            return id == other.getId();
        }
        return false;
    }
}
