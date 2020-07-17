package com.hyaline.avoidbrowser.ui.activities.search;

import android.content.Intent;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.databinding.ActivitySearchBinding;

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

    }

    @Override
    protected void initView() {
        viewModel.getSearchEvent().observe(this, keyword -> {
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
            } else {
                data.putExtra("type", 0);
                data.putExtra("keyword", keyword);
            }
            setResult(200, data);
            finish();
        });
    }
}
