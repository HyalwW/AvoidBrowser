package com.hyaline.avoidbrowser.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hyaline.avoidbrowser.BasicApp;
import com.hyaline.avoidbrowser.data.beans.BrowseHistoryBean;
import com.hyaline.avoidbrowser.data.beans.SearchHistoryBean;
import com.hyaline.avoidbrowser.data.beans.UserInfoBean;
import com.hyaline.avoidbrowser.data.daos.BrowseHistoryDao;
import com.hyaline.avoidbrowser.data.daos.SearchHistoryDao;
import com.hyaline.avoidbrowser.data.daos.UserInfoDao;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/9
 * Description: blablabla
 */
@Database(entities = {UserInfoBean.class, SearchHistoryBean.class, BrowseHistoryBean.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;

    //用户账户
    public abstract UserInfoDao userInfoDao();

    //搜索历史
    public abstract SearchHistoryDao searchHistoryDao();

    //浏览历史
    public abstract BrowseHistoryDao browseHistoryDao();


    public static AppDatabase getDatabase() {
        return getDatabase(BasicApp.getContext());
    }

    public static AppDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "avoid _db_main")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}
