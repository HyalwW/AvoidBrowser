package com.hyaline.avoidbrowser.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import com.hyaline.avoidbrowser.R;

public abstract class BaseAlertDialog<DB extends ViewDataBinding, VM extends ViewModel> extends AlertDialog {
    protected Context mContext;
    protected DB dataBinding;
    protected VM viewModel;

    public BaseAlertDialog(Activity activityContext, VM viewModel) {
        super(activityContext);
        mContext = activityContext;
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId(), null, false);
        dataBinding.setVariable(viewModelId(), viewModel);
        setContentView(dataBinding.getRoot());
        initWindow();
        initView();
    }

    @Override
    public void show() {
        super.show();
    }

    private void initWindow() {
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(animId());
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawableResource(R.color.transparent);
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = fullScreen() ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = fullScreen() ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    public DB getDataBinding() {
        return dataBinding;
    }

    public VM getViewModel() {
        return viewModel;
    }

    protected abstract int layoutId();

    protected abstract void initView();

    protected abstract int viewModelId();

    protected abstract int animId();

    protected boolean fullScreen() {
        return false;
    }
}
