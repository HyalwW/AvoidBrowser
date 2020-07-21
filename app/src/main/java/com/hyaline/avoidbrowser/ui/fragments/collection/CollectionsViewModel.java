package com.hyaline.avoidbrowser.ui.fragments.collection;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseViewModel;
import com.hyaline.avoidbrowser.data.AppDatabase;
import com.hyaline.avoidbrowser.data.beans.CollectBean;
import com.hyaline.avoidbrowser.data.daos.CollectDao;
import com.hyaline.avoidbrowser.utils.BindingCommand;
import com.hyaline.avoidbrowser.utils.SingleLiveEvent;
import com.hyaline.avoidbrowser.utils.ThreadPool;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList;

public class CollectionsViewModel extends BaseViewModel {
    private CollectDao dao;
    private ItemBinding<CollectBean> itemBinding;
    private AsyncDiffObservableList<CollectBean> items;

    private BindingCommand<CollectBean> onItemClick, onItemLongClick, onUnCollectClick, onMoreClick;
    private SingleLiveEvent<CollectBean> clickEvent, longClickEvent, unCollectEvent, moreEvent;

    public CollectionsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        dao = AppDatabase.getDatabase().collectDao();
        items = new AsyncDiffObservableList<>(new CollectionCallback());
        itemBinding = ItemBinding.of(BR.item, R.layout.item_his_collect);
        itemBinding.bindExtra(BR.viewModel, this);
        clickEvent = new SingleLiveEvent<>();
        onItemClick = new BindingCommand<>(bean -> clickEvent.setValue(bean));
        longClickEvent = new SingleLiveEvent<>();
        onItemLongClick = new BindingCommand<>(bean -> {
            for (CollectBean item : items) {
                if (item.equals(bean)) {
                    longClickEvent.setValue(item);
                    return;
                }
            }
        });
        unCollectEvent = new SingleLiveEvent<>();
        onUnCollectClick = new BindingCommand<>(bean -> unCollectEvent.setValue(bean));
        moreEvent = new SingleLiveEvent<>();
        onMoreClick = new BindingCommand<>(bean -> {
            for (CollectBean item : items) {
                if (item.equals(bean)) {
                    moreEvent.setValue(item);
                    return;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {

    }

    @Override
    protected void parseIntent(Intent intent) {

    }

    public void setData(List<CollectBean> list) {
        items.update(list);
    }

    public CollectDao getDao() {
        return dao;
    }

    public ItemBinding<CollectBean> getItemBinding() {
        return itemBinding;
    }

    public ObservableList<CollectBean> getItems() {
        return items;
    }

    public BindingCommand getOnUnCollectClick() {
        return onUnCollectClick;
    }

    public BindingCommand<CollectBean> getOnItemClick() {
        return onItemClick;
    }

    public BindingCommand<CollectBean> getOnItemLongClick() {
        return onItemLongClick;
    }

    public SingleLiveEvent<CollectBean> getClickEvent() {
        return clickEvent;
    }

    public SingleLiveEvent<CollectBean> getLongClickEvent() {
        return longClickEvent;
    }

    public SingleLiveEvent<CollectBean> getUnCollectEvent() {
        return unCollectEvent;
    }

    public BindingCommand<CollectBean> getOnMoreClick() {
        return onMoreClick;
    }

    public SingleLiveEvent<CollectBean> getMoreEvent() {
        return moreEvent;
    }

    public void delete(CollectBean bean) {
        ThreadPool.fixed().execute(() -> dao.delete(bean));
    }
}
