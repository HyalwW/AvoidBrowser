package com.hyaline.avoidbrowser.ui.activities.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.ui.activities.history.data.SectionHeader;
import com.hyaline.avoidbrowser.ui.activities.history.data.SectionItem;
import com.qmuiteam.qmui.widget.section.QMUIDefaultStickySectionAdapter;
import com.qmuiteam.qmui.widget.section.QMUISection;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/20
 * Description: blablabla
 */
public class HistorySectionAdapter extends QMUIDefaultStickySectionAdapter<SectionHeader, SectionItem> {
    private LayoutInflater inflater;

    public HistorySectionAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateSectionHeaderViewHolder(@NonNull ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_his_header, viewGroup, false);
        return new HeaderViewHolder(view);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateSectionItemViewHolder(@NonNull ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_his_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeader(ViewHolder holder, int position, QMUISection<SectionHeader, SectionItem> section) {
        super.onBindSectionHeader(holder, position, section);
        ((HeaderViewHolder) holder).bind(section.getHeader());
    }

    @Override
    protected void onBindSectionItem(ViewHolder holder, int position, QMUISection<SectionHeader, SectionItem> section, int itemIndex) {
        super.onBindSectionItem(holder, position, section, itemIndex);
        ((ItemViewHolder) holder).bind(section.getItemAt(itemIndex));
    }
}
