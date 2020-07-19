package com.hyaline.avoidbrowser.ui.activities.collections;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.databinding.ActivityCollectionsBinding;

public class CollectionsActivity extends BaseActivity<CollectionsViewModel, ActivityCollectionsBinding> {
    @Override
    protected int layoutId() {
        return R.layout.activity_collections;
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

    }
}
