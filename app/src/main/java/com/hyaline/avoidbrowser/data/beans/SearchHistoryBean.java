package com.hyaline.avoidbrowser.data.beans;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SearchHistoryBean {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String keyword;
    private int searchCount;
    private long updateTime;

    public SearchHistoryBean() {
        updateTime = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        SearchHistoryBean target = (SearchHistoryBean) obj;
        return id == target.id && keyword.equals(target.keyword);
    }
}
