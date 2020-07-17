package com.hyaline.avoidbrowser.ui.activities.search;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.hyaline.avoidbrowser.base.BaseViewModel;
import com.hyaline.avoidbrowser.utils.BindingCommand;
import com.hyaline.avoidbrowser.utils.SingleLiveEvent;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/17
 * Description: blablabla
 */
public class SearchViewModel extends BaseViewModel {
    private ObservableField<String> keyword;
    private BindingCommand clearEdit, onSearchClick;
    private SingleLiveEvent<String> searchEvent;

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        keyword = new ObservableField<>("");
        clearEdit = new BindingCommand(() -> keyword.set(""));
        onSearchClick = new BindingCommand(() -> {
            if (!TextUtils.isEmpty(keyword.get())) {
                searchEvent.setValue(keyword.get());
            }
        });
        searchEvent = new SingleLiveEvent<>();
    }

    @Override
    protected void onDestroy() {

    }

    public ObservableField<String> getKeyword() {
        return keyword;
    }

    public BindingCommand getClearEdit() {
        return clearEdit;
    }

    public BindingCommand getOnSearchClick() {
        return onSearchClick;
    }

    public SingleLiveEvent<String> getSearchEvent() {
        return searchEvent;
    }
}
