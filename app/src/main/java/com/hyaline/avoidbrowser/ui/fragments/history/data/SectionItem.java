package com.hyaline.avoidbrowser.ui.fragments.history.data;

import com.hyaline.avoidbrowser.data.beans.BrowseHistoryBean;
import com.qmuiteam.qmui.widget.section.QMUISection;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/20
 * Description: blablabla
 */
public class SectionItem implements QMUISection.Model<SectionItem> {
    public BrowseHistoryBean item;

    public SectionItem(BrowseHistoryBean item) {
        this.item = item;
    }

    @Override
    public SectionItem cloneForDiff() {
        return new SectionItem(item.clone());
    }

    @Override
    public boolean isSameItem(SectionItem other) {
        return other.item.equals(item);
    }

    @Override
    public boolean isSameContent(SectionItem other) {
        return true;
    }
}
