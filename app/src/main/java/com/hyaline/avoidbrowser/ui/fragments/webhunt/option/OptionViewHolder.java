package com.hyaline.avoidbrowser.ui.fragments.webhunt.option;

import android.view.View;

import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.collection.AbsViewHolder;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/22
 * Description: blablabla
 */
public class OptionViewHolder extends AbsViewHolder<String> {
    private QMUIAlphaTextView textView;

    public OptionViewHolder(View itemView) {
        super(itemView);
        textView = findViewById(R.id.text);
    }

    @Override
    public void bind(String item) {
        textView.setText(item);
    }
}
