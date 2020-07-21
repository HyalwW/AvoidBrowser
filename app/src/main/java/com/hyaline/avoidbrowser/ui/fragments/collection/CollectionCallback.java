package com.hyaline.avoidbrowser.ui.fragments.collection;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.hyaline.avoidbrowser.data.beans.CollectBean;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/21
 * Description: blablabla
 */
public class CollectionCallback extends DiffUtil.ItemCallback<CollectBean> {
    @Override
    public boolean areItemsTheSame(@NonNull CollectBean oldItem, @NonNull CollectBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull CollectBean oldItem, @NonNull CollectBean newItem) {
        boolean checkName = false, checkUrl = false;
        String oldName = oldItem.getName();
        String newName = newItem.getName();
        if (TextUtils.isEmpty(oldName) && TextUtils.isEmpty(newName)) {
            checkName = true;
        } else if (!TextUtils.isEmpty(oldName) && !TextUtils.isEmpty(newName) && oldName.equals(newName)) {
            checkName = true;
        }
        String oldUrl = oldItem.getUrl();
        String newUrl = newItem.getUrl();
        if (TextUtils.isEmpty(oldUrl) && TextUtils.isEmpty(newUrl)) {
            checkUrl = true;
        } else if (!TextUtils.isEmpty(oldUrl) && !TextUtils.isEmpty(newUrl) && oldUrl.equals(newUrl)) {
            checkUrl = true;
        }
        Log.e("wwh", "CollectionCallback --> areContentsTheSame: " + oldItem.getUrl() + " " + checkName + " " + checkUrl);
        return checkName && checkUrl;
    }
}
