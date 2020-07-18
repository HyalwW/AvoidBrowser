package com.hyaline.avoidbrowser.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hyaline.avoidbrowser.data.beans.CollectBean;

import java.util.List;

@Dao
public interface CollectDao {
    @Query("select * from collectbean limit :count")
    LiveData<List<CollectBean>> loadLiveCollections(int count);

    @Query("select * from collectbean")
    LiveData<List<CollectBean>> loadLiveCollections();

    @Insert
    void insert(CollectBean bean);

    @Update
    void update(CollectBean bean);

    @Delete
    void delete(CollectBean bean);
}
