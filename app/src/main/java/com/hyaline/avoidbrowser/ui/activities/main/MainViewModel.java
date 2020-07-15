package com.hyaline.avoidbrowser.ui.activities.main;

import android.app.Application;

import androidx.annotation.NonNull;

import com.hyaline.avoidbrowser.base.BaseViewModel;
import com.hyaline.avoidbrowser.utils.BindingCommand;
import com.hyaline.avoidbrowser.utils.SingleLiveEvent;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/15
 * Description: blablabla
 */
public class MainViewModel extends BaseViewModel {
    private BindingCommand goSettingCommand;
    private SingleLiveEvent goSettingEvent;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        goSettingEvent = new SingleLiveEvent();
        goSettingCommand = new BindingCommand(() -> goSettingEvent.call());
    }

    @Override
    protected void onDestroy() {

    }

    public BindingCommand getGoSettingCommand() {
        return goSettingCommand;
    }

    public SingleLiveEvent getGoSettingEvent() {
        return goSettingEvent;
    }

}
