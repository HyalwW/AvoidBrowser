package com.hyaline.avoidbrowser.ui.activities.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseViewModel;
import com.hyaline.avoidbrowser.utils.BindingCommand;
import com.hyaline.avoidbrowser.utils.SingleLiveEvent;

import static com.hyaline.avoidbrowser.ui.activities.main.MainActivity.BACK;
import static com.hyaline.avoidbrowser.ui.activities.main.MainActivity.FORWARD;
import static com.hyaline.avoidbrowser.ui.activities.main.MainActivity.HOME;
import static com.hyaline.avoidbrowser.ui.activities.main.MainActivity.MENU;
import static com.hyaline.avoidbrowser.ui.activities.main.MainActivity.STACK;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/15
 * Description: blablabla
 */
public class MainViewModel extends BaseViewModel {
    private BindingCommand goSettingCommand;
    private SingleLiveEvent goSettingEvent;
    //top
    private ObservableInt refreshRes, collectRes;
    private ObservableField<String> searchText;
    private BindingCommand onCollectClick, onSearchClick, onRefreshClick;
    private SingleLiveEvent collectEvent, searchEvent, refreshEvent;
    //bottom
    private ObservableList<BottomItem> items;
    private BindingCommand<BottomItem> onBottomItemCLick;
    private SingleLiveEvent<BottomItem> onItemClickEvent;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        goSettingEvent = new SingleLiveEvent();
        goSettingCommand = new BindingCommand(() -> goSettingEvent.call());
        initTop();
        initBottom();
    }

    private void initTop() {
        refreshRes = new ObservableInt(R.drawable.refresh_white);
        collectRes = new ObservableInt(R.drawable.uncollect_white);
        searchText = new ObservableField<>("搜索内容、网址");
        collectEvent = new SingleLiveEvent();
        searchEvent = new SingleLiveEvent();
        refreshEvent = new SingleLiveEvent();
        onCollectClick = new BindingCommand(() -> collectEvent.call());
        onSearchClick = new BindingCommand(() -> searchEvent.call());
        onRefreshClick = new BindingCommand(() -> refreshEvent.call());
    }

    private void initBottom() {
        items = new ObservableArrayList<>();
        items.add(new BottomItem(BACK, R.drawable.goback_white));
        items.add(new BottomItem(FORWARD, R.drawable.goforward_white));
        items.add(new BottomItem(MENU, R.drawable.menu_white));
        items.add(new BottomItem(HOME, R.drawable.home_white));
        items.add(new BottomItem(STACK, R.drawable.stack_white));
        onBottomItemCLick = new BindingCommand<>(s -> onItemClickEvent.setValue(s));
        onItemClickEvent = new SingleLiveEvent<>();
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

    public ObservableInt getRefreshRes() {
        return refreshRes;
    }

    public void load(boolean ref) {
        refreshRes.set(ref ? R.drawable.close_white : R.drawable.refresh_white);
    }

    public boolean isLoad() {
        return refreshRes.get() == R.drawable.close_white;
    }

    public ObservableInt getCollectRes() {
        return collectRes;
    }

    public ObservableField<String> getSearchText() {
        return searchText;
    }

    public BindingCommand getOnCollectClick() {
        return onCollectClick;
    }

    public BindingCommand getOnSearchClick() {
        return onSearchClick;
    }

    public BindingCommand getOnRefreshClick() {
        return onRefreshClick;
    }

    public SingleLiveEvent getCollectEvent() {
        return collectEvent;
    }

    public SingleLiveEvent getSearchEvent() {
        return searchEvent;
    }

    public SingleLiveEvent getRefreshEvent() {
        return refreshEvent;
    }

    public ObservableList<BottomItem> getItems() {
        return items;
    }

    public BindingCommand<BottomItem> getOnBottomItemCLick() {
        return onBottomItemCLick;
    }

    public SingleLiveEvent<BottomItem> getOnItemClickEvent() {
        return onItemClickEvent;
    }
}
