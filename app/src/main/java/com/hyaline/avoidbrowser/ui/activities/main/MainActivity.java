package com.hyaline.avoidbrowser.ui.activities.main;

import android.content.Intent;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.databinding.ActivityMainBinding;
import com.hyaline.avoidbrowser.ui.activities.set.SettingActivity;

public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> {

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int viewModelId() {
        return BR.viewModel;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        viewModel.getGoSettingEvent().observe(this, o -> startActivity(new Intent(this, SettingActivity.class)));
    }
}
