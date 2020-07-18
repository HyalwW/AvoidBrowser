package com.hyaline.avoidbrowser.data.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hyaline.avoidbrowser.data.beans.UserInfoBean;

@Dao
public interface UserInfoDao {
    @Query("select * from userinfobean where account = :account")
    UserInfoBean loadUser(String account);

    @Insert
    void insert(UserInfoBean bean);

    @Update
    void updateUser(UserInfoBean bean);
}
