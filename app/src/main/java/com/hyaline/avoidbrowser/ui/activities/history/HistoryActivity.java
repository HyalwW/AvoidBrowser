package com.hyaline.avoidbrowser.ui.activities.history;

import android.graphics.Typeface;
import android.view.Gravity;

import androidx.viewpager.widget.ViewPager;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.databinding.ActivityHistoryBinding;
import com.hyaline.avoidbrowser.ui.fragments.history.HistoryViewModel;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;

public class HistoryActivity extends BaseActivity<HistoryViewModel, ActivityHistoryBinding> {


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
        initTopBar();
        initViewPager();
        initTabBar();
    }

    private void initTopBar() {
        QMUICollapsingTopBarLayout collapse = dataBinding.collapseLayout;
        collapse.setCollapsedTitleGravity(Gravity.CENTER);
        collapse.setExpandedTitleGravity(Gravity.CENTER);
        collapse.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        collapse.setExpandedTitleColor(getColor(R.color.tomato));
        collapse.setExpandedTitleMarginTop(QMUIDisplayHelper.dp2px(this, 40));
        dataBinding.topBar.setTitle("浏览历史");
        dataBinding.topBar.showTitleView(false);
        dataBinding.topBar.setBottomDividerAlpha(0);
        QMUIAlphaImageButton backImageButton = dataBinding.topBar.addLeftBackImageButton();
        backImageButton.setOnClickListener(v -> finish());
    }

    private void initViewPager() {
        HistoryPagerAdapter adapter = new HistoryPagerAdapter(getSupportFragmentManager());
        dataBinding.viewPager.setAdapter(adapter);
        dataBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        dataBinding.topBar.setTitle("浏览历史");
                        dataBinding.topBar.showTitleView(false);
                        dataBinding.collapseLayout.setTitle("浏览历史");
                        break;
                    case 1:
                        dataBinding.topBar.setTitle("收藏/书签");
                        dataBinding.topBar.showTitleView(false);
                        dataBinding.collapseLayout.setTitle("收藏/书签");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabBar() {
        dataBinding.tab.setIndicator(new QMUITabIndicator(dp2px(2), false, true));
        QMUITabBuilder tabBuilder = dataBinding.tab.tabBuilder();
        dataBinding.tab.addTab(tabBuilder.setText("浏览历史").setColor(0x80FFFFFF, 0xFFFFFFFF).build(this));
        dataBinding.tab.addTab(tabBuilder.setText("收藏/书签").setColor(0x80FFFFFF, 0xFFFFFFFF).build(this));
        dataBinding.tab.setupWithViewPager(dataBinding.viewPager, false);
    }

    private int dp2px(int dp) {
        return QMUIDisplayHelper.dp2px(this, dp);
    }
}
