package com.hyaline.avoidbrowser.ui.activities.main;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.databinding.ActivityMainBinding;
import com.hyaline.avoidbrowser.ui.activities.search.SearchActivity;
import com.hyaline.avoidbrowser.ui.activities.set.SettingActivity;
import com.hyaline.avoidbrowser.ui.fragments.OnWebStuffListner;
import com.hyaline.avoidbrowser.ui.fragments.webhunt.PageInfo;
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
                        current.go2Page("https://www.baidu.com/", true);
                    }
                    break;
                case STACK:
                    checkPagesSheet();
                    break;
            }
        });
    }

    private void checkPagesSheet() {
        List<Fragment> fragments = fragmentManager.getFragments();
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(this)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    if (fragments.size() > 1) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        Fragment fragment = fragments.get(position);
                        transaction.hide(current).show(fragment).commit();
                        current = (WebHuntFragment) fragment;
                    }
                    dialog.dismiss();
                })
                .setAddCancelBtn(true)
                .setNeedRightMark(true);
        for (Fragment fragment : fragments) {
            PageInfo pageInfo = ((WebHuntFragment) fragment).getPageInfo();
            builder.addItem(new BitmapDrawable(getResources(), pageInfo.getIcon()), pageInfo.getTitle());
            if (fragment == current) {
                builder.setCheckedIndex(fragments.indexOf(fragment));
            }
        }
        builder.build().show();
    }

    private void initTop() {
        viewModel.getSearchEvent().observe(this, o -> {
            Intent intent = new Intent(this, SearchActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, dataBinding.searchPanel.searchLayout, getString(R.string.search_panel));
            startActivityForResult(intent, 207, compat.toBundle());
        });
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 207 && data != null) {
            int type = data.getIntExtra("type", 0);
            String url;
            if (type == 0) {
                String keyword = data.getStringExtra("keyword");
                url = "https://www.baidu.com/s?word=" + keyword;
            } else {
                url = data.getStringExtra("url");
            }
            go2Fragment(url, null);
        }
    }

    @Override
    public void onLoadStart() {
        viewModel.load(true);
    }

    @Override
    public void onLoadFinish() {
        viewModel.load(false);
    }

    @Override
    public int getScrollHeight() {
        return dataBinding.bottomPanel.base.getTop();
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

    @Override
    public void onBackPressed() {
        if (backHandler == null || !backHandler.onBackPressed()) {
            moveTaskToBack(true);
        }
    }
}
