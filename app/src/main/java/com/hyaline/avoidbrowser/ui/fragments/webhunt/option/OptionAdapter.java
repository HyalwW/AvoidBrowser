package com.hyaline.avoidbrowser.ui.fragments.webhunt.option;

import android.view.View;

import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.collection.AbsAdapter;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/22
 * Description: blablabla
 */
public class OptionAdapter extends AbsAdapter<String, OptionViewHolder> {
    @Override
    protected int layoutId() {
        return R.layout.item_option;
    }

    @Override
    protected OptionViewHolder createViewHolder(View convertView) {
        return new OptionViewHolder(convertView);
    }
}
