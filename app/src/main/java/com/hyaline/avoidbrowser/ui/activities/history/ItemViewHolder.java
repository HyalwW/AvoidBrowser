package com.hyaline.avoidbrowser.ui.activities.history;

import android.view.View;

import com.blankj.utilcode.util.TimeUtils;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.ui.activities.history.data.SectionItem;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.section.QMUIStickySectionAdapter;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/20
 * Description: blablabla
 */
public class ItemViewHolder extends QMUIStickySectionAdapter.ViewHolder {
    private QMUIAlphaTextView title, url, time;
    private SectionItem item;

    public ItemViewHolder(View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
        title = itemView.findViewById(R.id.title);
        url = itemView.findViewById(R.id.url);
    }

    public void bind(SectionItem item) {
        this.item = item;
        time.setText(getTime(item.item.getTime()));
        title.setText(item.item.getTitle());
        url.setText(item.item.getUrl());
    }

    public SectionItem getItem() {
        return item;
    }

    private String getTime(long time) {
        return TimeUtils.millis2String(time, "HH:mm");
    }
}
