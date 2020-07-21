package com.hyaline.avoidbrowser.ui.dialogs.showStack;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.hyaline.avoidbrowser.ui.fragments.webhunt.PageInfo;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/21
 * Description: blablabla
 */
public class StackCallback extends DiffUtil.ItemCallback<PageInfo> {
    @Override
    public boolean areItemsTheSame(@NonNull PageInfo oldItem, @NonNull PageInfo newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull PageInfo oldItem, @NonNull PageInfo newItem) {
        return true;
    }
}
