package com.hyaline.avoidbrowser.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hyaline.avoidbrowser.data.beans.SearchHistoryBean;

import java.util.List;

@Dao
public interface SearchHistoryDao {
    @Query("select * from SearchHistoryBean order by updateTime desc")
    LiveData<List<SearchHistoryBean>> loadAllLiveHistoriesByTime();

    @Query("select * from SearchHistoryBean order by updateTime desc limit :count")
    LiveData<List<SearchHistoryBean>> loadLiveHistoriesByTime(int count);

    @Query("select * from SearchHistoryBean order by updateTime desc limit :count")
    List<SearchHistoryBean> loadHistoriesByTime(int count);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchHistoryBean bean);

    @Update
    void update(SearchHistoryBean bean);

    @Delete
    void delete(SearchHistoryBean bean);

    @Query("select * from searchhistorybean where keyword = :keyword")
    SearchHistoryBean exist(String keyword);
}
