package com.hyaline.avoidbrowser.ui.activities.main.showStack;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.ui.fragments.webhunt.PageInfo;
import com.hyaline.avoidbrowser.utils.Action;
import com.hyaline.avoidbrowser.utils.BindingCommand;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class ShowStackModel extends ViewModel {
    private ItemBinding<PageInfo> pageBinding;
    private ObservableList<PageInfo> pages;
    private BindingCommand<PageInfo> onCloseClick, onPageClick;
    private Action<PageInfo> deleteAction, showAction;
    private Action dismissAction;
    private BindingCommand onBlankClick;

    public ShowStackModel() {
        pages = new ObservableArrayList<>();
        pageBinding = ItemBinding.of(BR.item, R.layout.item_page);
        pageBinding.bindExtra(BR.model, this);
        onCloseClick = new BindingCommand<>(pageInfo -> deleteAction.setValue(pageInfo));
        deleteAction = new Action<>();
        onPageClick = new BindingCommand<>(pageInfo -> {
            if (!pageInfo.isShow()) {
                showAction.setValue(pageInfo);
            }
            dismissAction.call();
        });
        showAction = new Action<>();
        dismissAction = new Action();
        onBlankClick = new BindingCommand(() -> dismissAction.call());
    }

    public ItemBinding<PageInfo> getPageBinding() {
        return pageBinding;
    }

    public ObservableList<PageInfo> getPages() {
        return pages;
    }

    public BindingCommand<PageInfo> getOnCloseClick() {
        return onCloseClick;
    }

    public void setList(List<PageInfo> list) {
        pages.clear();
        pages.addAll(list);
    }

    public Action<PageInfo> getDeleteAction() {
        return deleteAction;
    }

    public BindingCommand<PageInfo> getOnPageClick() {
        return onPageClick;
    }

    public Action<PageInfo> getShowAction() {
        return showAction;
    }

    public Action getDismissAction() {
        return dismissAction;
    }

    public BindingCommand getOnBlankClick() {
        return onBlankClick;
    }
}
