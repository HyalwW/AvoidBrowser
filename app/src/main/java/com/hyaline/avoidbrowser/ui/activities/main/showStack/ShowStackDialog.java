package com.hyaline.avoidbrowser.ui.activities.main.showStack;

import android.app.Activity;
import android.os.Bundle;

import androidx.databinding.ObservableList;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseAlertDialog;
import com.hyaline.avoidbrowser.databinding.DialogShowStackBinding;
import com.hyaline.avoidbrowser.ui.fragments.webhunt.PageInfo;

import java.util.List;

public class ShowStackDialog extends BaseAlertDialog<DialogShowStackBinding, ShowStackModel> {
    private OnPageChangedListener listener;

    public ShowStackDialog(Activity activityContext, ShowStackModel viewModel) {
        super(activityContext, viewModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_show_stack;
    }

    @Override
    protected void initView() {
        viewModel.getDeleteAction().observe(pageInfo -> {
            ObservableList<PageInfo> pages = viewModel.getPages();
            int deletePos = pages.indexOf(pageInfo);
            pages.remove(pageInfo);
            if (listener != null) {
                listener.onDelete(deletePos);
            }
        });
        viewModel.getShowAction().observe(pageInfo -> {
            int showPos =  viewModel.getPages().indexOf(pageInfo);
            if (listener != null) {
                listener.onShow(showPos);
            }
        });
        viewModel.getDismissAction().observe(o -> dismiss());
    }

    @Override
    protected int viewModelId() {
        return BR.model;
    }

    @Override
    protected int animId() {
        return 0;
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    public void show(List<PageInfo> list) {
        viewModel.setList(list);
        show();
    }

    public void setListener(OnPageChangedListener listener) {
        this.listener = listener;
    }

    public interface OnPageChangedListener {
        //标签页删除
        void onDelete(int delPos);

        //切换标签页
        void onShow(int showPos);
    }
}
