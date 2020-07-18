package com.hyaline.avoidbrowser.ui.activities.set;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.hyaline.avoidbrowser.base.BaseViewModel;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/15
 * Description: blablabla
 */
public class SettingViewModel extends BaseViewModel {
    private ObservableField<String> address;

    public SettingViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        address = new ObservableField<>("这是设置界面");
    }

    @Override
    protected void onDestroy() {

    }

    @Override
    protected void parseIntent(Intent intent) {

    }

    public ObservableField<String> getAddress() {
        return address;
    }
}
