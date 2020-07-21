package com.hyaline.avoidbrowser.ui.activities.search;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.hyaline.avoidbrowser.data.beans.SearchHistoryBean;

public class SearchCallback extends DiffUtil.ItemCallback<SearchHistoryBean> {
    @Override
    public boolean areItemsTheSame(@NonNull SearchHistoryBean oldItem, @NonNull SearchHistoryBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull SearchHistoryBean oldItem, @NonNull SearchHistoryBean newItem) {
        String oldItemKeyword = oldItem.getKeyword();
        String newItemKeyword = newItem.getKeyword();
        if (TextUtils.isEmpty(oldItemKeyword) && TextUtils.isEmpty(newItemKeyword)) {
            return true;
        } else
            return !TextUtils.isEmpty(oldItemKeyword) && !TextUtils.isEmpty(newItemKeyword) && oldItemKeyword.equals(newItemKeyword);
    }
}
