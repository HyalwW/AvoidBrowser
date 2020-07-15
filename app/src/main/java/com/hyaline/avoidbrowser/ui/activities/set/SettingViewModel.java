package com.hyaline.avoidbrowser.ui.activities.set;

import android.app.Application;

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
        address.set("设置界面www.baidu.com");
    }

    @Override
    protected void onDestroy() {

    }

    public ObservableField<String> getAddress() {
        return address;
    }
}
