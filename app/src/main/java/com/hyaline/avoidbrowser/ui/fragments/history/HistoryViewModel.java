package com.hyaline.avoidbrowser.ui.fragments.history;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import com.hyaline.avoidbrowser.base.BaseViewModel;
import com.hyaline.avoidbrowser.data.AppDatabase;
import com.hyaline.avoidbrowser.data.daos.BrowseHistoryDao;

public class HistoryViewModel extends BaseViewModel {
    private BrowseHistoryDao dao;
    private ObservableBoolean isEmpty;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        dao = AppDatabase.getDatabase().browseHistoryDao();
        isEmpty = new ObservableBoolean(true);
    }

    @Override
    protected void onDestroy() {

    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty.set(isEmpty);
    }

    @Override
    protected void parseIntent(Intent intent) {

    }

    public ObservableBoolean getIsEmpty() {
        return isEmpty;
    }

    public BrowseHistoryDao getDao() {
        return dao;
    }
}
