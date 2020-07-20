package com.hyaline.avoidbrowser.ui.activities.search;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseViewModel;
import com.hyaline.avoidbrowser.data.AppDatabase;
import com.hyaline.avoidbrowser.data.beans.SearchHistoryBean;
import com.hyaline.avoidbrowser.data.daos.SearchHistoryDao;
import com.hyaline.avoidbrowser.utils.BindingCommand;
import com.hyaline.avoidbrowser.utils.SingleLiveEvent;
import com.hyaline.avoidbrowser.utils.ThreadPool;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/17
 * Description: blablabla
 */
public class SearchViewModel extends BaseViewModel {
    private ObservableField<String> keyword, tempUrl;
    private BindingCommand clearEdit, onSearchClick, onCopyClick;
    private SingleLiveEvent<String> searchEvent, copyEvent, keyEvent;

    private BindingCommand<SearchHistoryBean> onSearchItemDelete, onSearchItemUp, onSearchItemClick;

    private ObservableList<SearchHistoryBean> shItems;
    private ItemBinding<SearchHistoryBean> shiBinding;

    private SearchHistoryDao searchHistoryDao;

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        searchHistoryDao = AppDatabase.getDatabase().searchHistoryDao();
        keyword = new ObservableField<>("");
        tempUrl = new ObservableField<>();
        clearEdit = new BindingCommand(() -> keyword.set(""));
        onSearchClick = new BindingCommand(() -> {
            if (!TextUtils.isEmpty(keyword.get())) {
                searchEvent.setValue(keyword.get());
                SearchHistoryBean exist = searchHistoryDao.exist(keyword.get());
                if (exist != null) {
                    exist.setUpdateTime(System.currentTimeMillis());
                    exist.setSearchCount(exist.getSearchCount() + 1);
                    searchHistoryDao.update(exist);
                } else {
                    exist = new SearchHistoryBean();
                    exist.setKeyword(keyword.get());
                    exist.setSearchCount(1);
                    searchHistoryDao.insert(exist);
                }
            }
        });
        searchEvent = new SingleLiveEvent<>();
        copyEvent = new SingleLiveEvent<>();
        onCopyClick = new BindingCommand(() -> {
            copyEvent.setValue(tempUrl.get());
            tempUrl.set("");
        });
        shiBinding = ItemBinding.of(BR.item, R.layout.item_search_history);
        shiBinding.bindExtra(BR.viewModel, this);
        shItems = new ObservableArrayList<>();
        onSearchItemDelete = new BindingCommand<>(bean -> ThreadPool.fixed().execute(() -> searchHistoryDao.delete(bean)));
        onSearchItemUp = new BindingCommand<>(bean -> keyword.set(bean.getKeyword()));
        keyEvent = new SingleLiveEvent<>();
        onSearchItemClick = new BindingCommand<>(bean -> {
            keyEvent.setValue(bean.getKeyword());
            bean.setUpdateTime(System.currentTimeMillis());
            bean.setSearchCount(bean.getSearchCount() + 1);
            searchHistoryDao.update(bean);
        });
    }

    @Override
    protected void onDestroy() {

    }

    @Override
    protected void parseIntent(Intent intent) {
        if (intent != null) {
            tempUrl.set(intent.getStringExtra("temp_url"));
        }
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

    public ObservableField<String> getTempUrl() {
        return tempUrl;
    }

    public BindingCommand getOnCopyClick() {
        return onCopyClick;
    }

    public SingleLiveEvent<String> getCopyEvent() {
        return copyEvent;
    }

    public BindingCommand<SearchHistoryBean> getOnSearchItemDelete() {
        return onSearchItemDelete;
    }

    public ObservableList<SearchHistoryBean> getShItems() {
        return shItems;
    }

    public ItemBinding<SearchHistoryBean> getShiBinding() {
        return shiBinding;
    }

    public SearchHistoryDao getSearchHistoryDao() {
        return searchHistoryDao;
    }

    public SingleLiveEvent<String> getKeyEvent() {
        return keyEvent;
    }

    public BindingCommand<SearchHistoryBean> getOnSearchItemUp() {
        return onSearchItemUp;
    }

    public BindingCommand<SearchHistoryBean> getOnSearchItemClick() {
        return onSearchItemClick;
    }

    public void setSearchHistoryBeans(List<SearchHistoryBean> searchHistoryBeans) {
        shItems.clear();
        shItems.addAll(searchHistoryBeans);
    }

    public void deleteHistory(int position) {
        searchHistoryDao.delete(shItems.remove(position));
    }
}
