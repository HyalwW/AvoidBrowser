package com.hyaline.avoidbrowser.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hyaline.avoidbrowser.data.beans.BrowseHistoryBean;

import java.util.List;

@Dao
public interface BrowseHistoryDao {
    @Query("select * from browsehistorybean order by time desc limit :count")
    LiveData<List<BrowseHistoryBean>> loadLiveHistoriesBytime(int count);

    @Query("select * from browsehistorybean order by time desc")
    LiveData<List<BrowseHistoryBean>> loadLiveHistoriesBytime();

    @Insert
    void insert(BrowseHistoryBean bean);

    @Delete
    void delete(BrowseHistoryBean bean);

    @Query("delete from browsehistorybean where time < :lastTime")
    void deleteBeforeTime(long lastTime);

    @Query("delete from browsehistorybean")
    void deleteAll();

    @Query("select * from browsehistorybean where url = :url")
    BrowseHistoryBean exist(String url);

    @Update
    void update(BrowseHistoryBean bean);
}
