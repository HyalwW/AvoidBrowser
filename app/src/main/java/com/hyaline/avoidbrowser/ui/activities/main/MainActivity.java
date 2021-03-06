package com.hyaline.avoidbrowser.ui.activities.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.data.AppDatabase;
import com.hyaline.avoidbrowser.data.beans.CollectBean;
import com.hyaline.avoidbrowser.data.daos.CollectDao;
import com.hyaline.avoidbrowser.databinding.ActivityMainBinding;
import com.hyaline.avoidbrowser.ui.activities.history.HistoryActivity;
import com.hyaline.avoidbrowser.ui.activities.search.SearchActivity;
import com.hyaline.avoidbrowser.ui.activities.set.SettingActivity;
import com.hyaline.avoidbrowser.ui.customviews.MenuView;
import com.hyaline.avoidbrowser.ui.dialogs.CollectEditDialog;
import com.hyaline.avoidbrowser.ui.dialogs.showStack.ShowStackDialog;
import com.hyaline.avoidbrowser.ui.dialogs.showStack.ShowStackModel;
import com.hyaline.avoidbrowser.ui.fragments.webhunt.OnWebStuffListner;
import com.hyaline.avoidbrowser.ui.fragments.webhunt.PageInfo;
import com.hyaline.avoidbrowser.ui.fragments.webhunt.WebHuntFragment;
import com.hyaline.avoidbrowser.utils.ThreadPool;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> implements OnWebStuffListner, TextWatcher {

    private FragmentManager fragmentManager;
    private WebHuntFragment current;
    private String currentUrl;
    private QMUIBottomSheet menuSheet;
    private CollectEditDialog collectDialog;
    private CollectDao collectDao;

    static final String BACK = "后退";
    static final String FORWARD = "前进";
    static final String MENU = "菜单";
    static final String HOME = "主页";
    static final String STACK = "切换";

    private ShowStackDialog dialog;
    private String collectStr;
    private boolean isUrlCollected;

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
        collectDao = AppDatabase.getDatabase().collectDao();
    }

    @Override
    protected void initView() {
        fragmentManager = getSupportFragmentManager();
        go2Fragment("https://www.baidu.com", null);
        initEvents();
        String[] texts = new String[]{"历史", "书签", "下载", "夜间", "设置", "退出"};
        Bitmap[] bitmaps = new Bitmap[]{cb(R.drawable.history), cb(R.drawable.add_to_star), cb(R.drawable.download), cb(R.drawable.mode_night), cb(R.drawable.set), cb(R.drawable.exit)};
        for (int i = 0; i < texts.length; i++) {
            dataBinding.menuView.addMenu(texts[i], bitmaps[i]);
        }
    }

    private Bitmap cb(int icon) {
        return BitmapFactory.decodeResource(getResources(), icon);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            switch (intent.getStringExtra("extra")) {
                case "fromHistory":
                    String url = intent.getStringExtra("url");
                    go2Fragment(url, null);
                    break;
                case "fromCollection":
                    String url1 = intent.getStringExtra("url");
                    go2Fragment(url1, null);
                    break;
            }
        }
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
                    checkStackDialog();
                    dialog.show(getList());
                    break;
            }
        });
    }

    private List<PageInfo> getList() {
        List<Fragment> fragments = fragmentManager.getFragments();
        List<PageInfo> list = new ArrayList<>();
        for (Fragment fragment : fragments) {
            PageInfo pageInfo = ((WebHuntFragment) fragment).getPageInfo(true);
            list.add(pageInfo);
        }
        return list;
    }

    private void checkStackDialog() {
        if (dialog == null) {
            dialog = new ShowStackDialog(this, new ShowStackModel());
            dialog.setListener(new ShowStackDialog.OnPageChangedListener() {
                @Override
                public void onDelete(int delPos) {
                    List<Fragment> fragments = fragmentManager.getFragments();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.remove(fragments.get(delPos)).commit();
                }

                @Override
                public void onShow(int showPos) {
                    if (showPos < 0)
                        return;
                    List<Fragment> fragments = fragmentManager.getFragments();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Fragment fragment = fragments.get(showPos);
                    transaction.hide(current).show(fragment).commit();
                    current = (WebHuntFragment) fragment;
                }
            });
        }
    }


    private void initTop() {
        viewModel.getSearchEvent().observe(this, o -> {
            Intent intent = new Intent(this, SearchActivity.class);
            if (!TextUtils.isEmpty(currentUrl)) {
                intent.putExtra("temp_url", currentUrl);
            }
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, dataBinding.searchPanel.searchLayout, getString(R.string.search_panel));
            startActivityForResult(intent, 207, compat.toBundle());
        });
        viewModel.getCollectEvent().observe(this, o -> {
            String title = viewModel.getSearchText().get();
            String url = currentUrl;
            CollectBean exist = collectDao.exist(url);
            if (exist == null) {
                CollectBean bean = new CollectBean();
                bean.setName(title);
                bean.setUrl(url);
                ThreadPool.fixed().execute(() -> collectDao.insert(bean));
                viewModel.setIsCollect(true);
                Toast.makeText(this, "收藏成功~", Toast.LENGTH_SHORT).show();
                isUrlCollected = true;
            } else {
                ThreadPool.fixed().execute(() -> collectDao.delete(exist));
                viewModel.setIsCollect(false);
                Toast.makeText(this, "取消收藏成功~", Toast.LENGTH_SHORT).show();
                isUrlCollected = false;
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

    public void go2Fragment(String url, Bundle extras) {
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
        viewModel.setIsCollect(false);
    }

    @Override
    public void onLoadFinish() {
        viewModel.load(false);
    }

    @Override
    public void onReceiveUrl(String url) {
        currentUrl = url;
        isUrlCollected = collectDao.exist(url) != null;
        viewModel.setIsCollect(isUrlCollected);
    }

    @Override
    public void onTitleChanged(String title) {
        viewModel.setTitle(title);
    }

    @Override
    public int getScrollHeight() {
        return dataBinding.bottomPanel.base.getTop();
    }

    private void checkMenuSheet() {
        if (menuSheet == null) {
            QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(this);
            builder.addItem(R.drawable.history, "历史/收藏", 0, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
            builder.addItem(R.drawable.add_to_star, "添加书签", 1, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
            builder.addItem(R.drawable.download, "下载", 2, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
            builder.addItem(R.drawable.mode_night, "夜间模式", 3, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
            builder.addItem(R.drawable.set, "设置", 4, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE);
            builder.addItem(R.drawable.exit, "退出", 5, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE);
            builder.setAllowDrag(true);
            builder.setOnSheetItemClickListener((dialog, itemView) -> {
                switch (((int) itemView.getTag())) {
                    case 0:
                        startActivity(new Intent(this, HistoryActivity.class));
                        break;
                    case 1:
                        if (!isUrlCollected) {
                            checkCollectDialog();
                            collectDialog.show();
                        } else {
                            Toast.makeText(this, "该网页已经收藏！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        startActivity(new Intent(this, SettingActivity.class));
                        break;
                    case 5:
                        finish();
                        break;
                }
                dialog.dismiss();
            });
            menuSheet = builder.build();
        }
    }

    private void checkCollectDialog() {
        if (collectDialog == null) {
            collectDialog = new CollectEditDialog(this, "保存书签", "在此输入书签名")
                    .listener(new CollectEditDialog.OnDialogListener() {
                        @Override
                        public void onCancel(QMUIDialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirm(QMUIDialog dialog, String text) {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "网页书签收藏成功~", Toast.LENGTH_SHORT).show();
                            viewModel.setIsCollect(true);
                            isUrlCollected = true;
                            ThreadPool.fixed().execute(() -> {
                                CollectBean bean = new CollectBean();
                                bean.setName(TextUtils.isEmpty(collectStr) ? viewModel.getSearchText().get() : collectStr);
                                bean.setUrl(currentUrl);
                                collectDao.insert(bean);
                                collectStr = "";
                            });
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        if (backHandler == null || !backHandler.onBackPressed()) {
            moveTaskToBack(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        collectStr = s.toString();
    }
}
