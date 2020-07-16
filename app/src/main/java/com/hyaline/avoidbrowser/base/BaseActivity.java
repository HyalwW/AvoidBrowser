package com.hyaline.avoidbrowser.base;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public abstract class BaseActivity<VM extends BaseViewModel, DB extends ViewDataBinding> extends AppCompatActivity {
    protected DB dataBinding;
    protected VM viewModel;

    protected int widthPixels, heightPixels;
    protected int statusBarHeight, navigationBarHeight;

    private OnBackHandler backHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, layoutId());
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        widthPixels = outMetrics.widthPixels;
        heightPixels = outMetrics.heightPixels;
        initViewModel();
//        QMUIStatusBarHelper.translucent(this);
        statusBarHeight = getStatusHeight();
        navigationBarHeight = getNavBarHeight();
        initData();
        initView();
    }

    @Override
    public void onBackPressed() {
        if (backHandler == null || !backHandler.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void setBackHandler(OnBackHandler backHandler) {
        this.backHandler = backHandler;
    }

    private void initViewModel() {
        int viewModelId = viewModelId();
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Class vmClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            viewModel = (VM) new ViewModelProvider(this).get(vmClass);
        }
        dataBinding.setVariable(viewModelId, viewModel);
    }

    public int getNavBarHeight() {
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    private int getStatusHeight() {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    protected abstract int layoutId();

    protected abstract int viewModelId();

    protected abstract void initData();

    protected abstract void initView();
}
