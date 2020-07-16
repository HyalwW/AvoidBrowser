package com.hyaline.avoidbrowser.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/16
 * Description: blablabla
 */
public abstract class BaseFragment<VM extends BaseViewModel, DB extends ViewDataBinding> extends Fragment implements OnBackHandler {
    protected VM viewModel;
    protected DB dataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        initViewModel();
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setBackHandler(this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).setBackHandler(this);
            }
        }
    }

    private void initViewModel() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            Class aClass = (Class) types[0];
            viewModel = (VM) new ViewModelProvider(this).get(aClass);
        }
        dataBinding.setVariable(viewModelId(), viewModel);
    }

    protected abstract int layoutId();

    protected abstract int viewModelId();

    protected abstract void initData();

    protected abstract void initView();
}
