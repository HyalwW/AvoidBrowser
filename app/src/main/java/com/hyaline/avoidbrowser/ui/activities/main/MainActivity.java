package com.hyaline.avoidbrowser.ui.activities.main;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.databinding.ActivityMainBinding;
import com.hyaline.avoidbrowser.ui.activities.search.SearchActivity;
import com.hyaline.avoidbrowser.ui.activities.set.SettingActivity;
import com.hyaline.avoidbrowser.ui.customviews.DynamicConstraintLayout;
import com.hyaline.avoidbrowser.ui.fragments.OnWebStuffListner;
import com.hyaline.avoidbrowser.ui.fragments.webhunt.WebHuntFragment;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.List;

public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> implements OnWebStuffListner {

    private FragmentManager fragmentManager;
    private WebHuntFragment current;
    private QMUIBottomSheet menuSheet;

    static final String BACK = "后退";
    static final String FORWARD = "前进";
    static final String MENU = "菜单";
    static final String HOME = "主页";
    static final String STACK = "切换";

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
        fragmentManager = getSupportFragmentManager();
        go2Fragment("https://www.baidu.com", null);
        initEvents();
    }

    private void initEvents() {
        initTop();
        viewModel.getGoSettingEvent().observe(this, o -> startActivity(new Intent(this, SettingActivity.class)));
        viewModel.getOnItemClickEvent().observe(this, bottomItem -> {
            switch (bottomItem.getTag()) {
                case BACK:
                    if (current != null) {
                        current.goBack();
                    }
                    break;
                case FORWARD:
                    if (current != null) {
                        current.goForward();
                    }
                    break;
                case MENU:
                    checkMenuSheet();
                    menuSheet.show();
                    break;
                case HOME:
                    if (current != null) {
                        current.go2Page("https://www.baidu.com", true);
                    }
                    break;
                case STACK:
                    break;
            }
        });
    }

    private void initTop() {
        viewModel.getSearchEvent().observe(this, o -> startActivity(new Intent(this, SearchActivity.class)));
        viewModel.getCollectEvent().observe(this, o -> {
            Log.e("wwh", "MainActivity --> initTop: " + "收藏");
        });
        viewModel.getCollectEvent().observe(this, o -> {
            if (current != null) {
                current.refresh();
            }
        });
        viewModel.getRefreshEvent().observe(this, o -> {
            if (current != null) {
                if (viewModel.isLoad()) {
                    current.stopLoad();
                } else {
                    current.refresh();
                }
            }
        });
    }

    private void go2Fragment(String url, Bundle extras) {
        List<Fragment> fragments = fragmentManager.getFragments();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (current != null) {
            transaction.hide(current);
        }
        WebHuntFragment huntFragment = new WebHuntFragment();
        if (extras == null) {
            extras = new Bundle();
        }
        extras.putString("url", url);
        huntFragment.setArguments(extras);
        transaction.add(R.id.web_container, huntFragment, "page" + fragments.size());
        current = huntFragment;
        transaction.commit();
    }

    @Override
    public void onLoadStart() {
        viewModel.load(true);
    }

    @Override
    public void onLoadFinish() {
        viewModel.load(false);
    }

    private ValueAnimator animator;
    private int baseHeight;

    @Override
    public void onWebScrollChanged(int xOffset, int yOffset) {
//        int height = dataBinding.searchPanel.base.getMeasuredHeight();
//        if (baseHeight == 0) {
//            baseHeight = height;
//        }
//        if (yOffset > 0 && height == baseHeight) {
//            anim(baseHeight, 0);
//        } else if (yOffset < 0 && height == 0) {
//            anim(0, baseHeight);
//        }
    }

    private void anim(int from, int to) {
        DynamicConstraintLayout base = dataBinding.searchPanel.base;
        if (animator == null) {
            animator = new ValueAnimator();
            animator.setDuration(200);
            animator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                base.setNewHeight(value);
            });
        }
        if (animator.isRunning())
            return;
        animator.setIntValues(from, to);
        animator.start();
    }

    private void checkMenuSheet() {
        if (menuSheet == null) {
            QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(this);
            builder.addItem(R.drawable.history, "历史记录", 0, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
            builder.addItem(R.drawable.collections, "书签/收藏", 0, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
            builder.addItem(R.drawable.download, "下载", 2, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
            builder.addItem(R.drawable.mode_night, "夜间模式", 3, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
            builder.addItem(R.drawable.set, "设置", 4, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE);
            builder.addItem(R.drawable.exit, "退出", 5, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE);
            builder.setAllowDrag(true);
            builder.setOnSheetItemClickListener((dialog, itemView) -> {
                switch (((int) itemView.getTag())) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        finish();
                        break;
                }
            });
            menuSheet = builder.build();
        }
    }
}
