package com.hyaline.avoidbrowser.base;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public abstract class BaseViewModel extends AndroidViewModel {
    protected Context appContext;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        appContext = application;
        onCreate(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        onDestroy();
    }

    protected abstract void onCreate(Application application);

    protected abstract void onDestroy();
}
