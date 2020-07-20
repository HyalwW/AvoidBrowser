package com.hyaline.avoidbrowser.ui.activities.history;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.hyaline.avoidbrowser.base.BaseViewModel;
import com.hyaline.avoidbrowser.data.AppDatabase;
import com.hyaline.avoidbrowser.data.daos.BrowseHistoryDao;

public class HistoryViewModel extends BaseViewModel {
    private BrowseHistoryDao dao;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        dao = AppDatabase.getDatabase().browseHistoryDao();
    }

    @Override
    protected void onDestroy() {

    }

    @Override
    protected void parseIntent(Intent intent) {

    }

    public BrowseHistoryDao getDao() {
        return dao;
    }
}
