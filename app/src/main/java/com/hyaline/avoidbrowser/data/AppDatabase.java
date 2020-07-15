//package com.hyaline.avoidbrowser.data;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import com.hyaline.avoidbrowser.BasicApp;
//
///**
// * Created by Wang.Wenhui
// * Date: 2020/7/9
// * Description: blablabla
// */
//@Database(entities = {}, version = 1, exportSchema = false)
//public abstract class AppDatabase extends RoomDatabase {
//    private static AppDatabase database;
//
//
//    public static AppDatabase getDatabase() {
//        return getDatabase(BasicApp.getContext());
//    }
//
//    public static AppDatabase getDatabase(Context context) {
//        if (database == null) {
//            database = Room.databaseBuilder(context, AppDatabase.class, "db_main")
//                    .allowMainThreadQueries()
//                    .build();
//        }
//        return database;
//    }
//}
