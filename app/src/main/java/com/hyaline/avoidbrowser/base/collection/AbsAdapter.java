package com.hyaline.avoidbrowser.base.collection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/22
 * Description: blablabla
 */
public abstract class AbsAdapter<T, VH extends AbsViewHolder<T>> extends BaseAdapter {
    private List<T> list;

    public AbsAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        if (list == null || list.size() < position + 1) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VH viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutId(), parent, false);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VH) convertView.getTag();
        }
        viewHolder.bind(getItem(position));
        return convertView;
    }

    protected abstract int layoutId();

    protected abstract VH createViewHolder(View convertView);

    public void setData(List<T> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
