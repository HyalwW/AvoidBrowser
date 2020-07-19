package com.hyaline.avoidbrowser.ui.activities.history;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.databinding.ActivityHistoryBinding;

public class HistoryActivity extends BaseActivity<HistoryViewMdodel, ActivityHistoryBinding> {
    @Override
    protected int layoutId() {
        return R.layout.activity_history;
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
