package com.hyaline.avoidbrowser.base.collection;

import android.view.View;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/22
 * Description: blablabla
 */
public abstract class AbsViewHolder<T> {
    protected View itemView;

    public AbsViewHolder(View itemView) {
        this.itemView = itemView;
    }

    protected <V extends View> V findViewById(int viewId) {
        return itemView.findViewById(viewId);
    }

    public abstract void bind(T item);
}
