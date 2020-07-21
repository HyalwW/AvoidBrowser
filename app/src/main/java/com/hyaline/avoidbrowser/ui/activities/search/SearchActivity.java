package com.hyaline.avoidbrowser.ui.activities.search;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.databinding.ActivitySearchBinding;
import com.hyaline.avoidbrowser.utils.ClipboardUtils;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;

import java.util.regex.Pattern;

public class SearchActivity extends BaseActivity<SearchViewModel, ActivitySearchBinding> {

    @Override
    protected int layoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected int viewModelId() {
        return BR.viewModel;
    }

    @Override
    protected void initData() {
        viewModel.getSearchHistoryDao().loadAllLiveHistoriesByTime().observe(this, searchHistoryBeans -> viewModel.setSearchHistoryBeans(searchHistoryBeans));
    }

    @Override
    protected void initView() {
        viewModel.getSearchEvent().observe(this, this::setResultAndFinish);
        viewModel.getCopyEvent().observe(this, s -> {
            ClipboardUtils.copyText(s);
            Toast.makeText(this, "网址复制成功~", Toast.LENGTH_SHORT).show();
        });
        viewModel.getKeyEvent().observe(this, this::setResultAndFinish);
        QMUIRVItemSwipeAction action = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                super.onSwiped(viewHolder, direction);
                int adapterPosition = viewHolder.getAdapterPosition();
                viewModel.deleteHistory(adapterPosition);
            }

        });
        action.attachToRecyclerView(dataBinding.list);
    }

    private void setResultAndFinish(String keyword) {
        Intent data = new Intent();
        String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
        Pattern pattern = Pattern.compile(regex);
        if (pattern.matcher(keyword).matches()) {
            data.putExtra("type", 1);
            data.putExtra("url", keyword);
        } else if (keyword.startsWith("www.") || keyword.startsWith("WWW.")) {
            keyword = "http://" + keyword;
            data.putExtra("type", 1);
            data.putExtra("url", keyword);
        } else if (keyword.startsWith("https://") || keyword.startsWith("http://")) {
            data.putExtra("type", 1);
            data.putExtra("url", keyword);
        } else {
            data.putExtra("type", 0);
            data.putExtra("keyword", keyword);
        }
        setResult(200, data);
        finish();
    }
}
