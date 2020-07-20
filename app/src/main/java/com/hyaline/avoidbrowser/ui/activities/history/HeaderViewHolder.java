package com.hyaline.avoidbrowser.ui.activities.history;

import android.view.View;

import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.ui.activities.history.data.SectionHeader;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.section.QMUIStickySectionAdapter;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/20
 * Description: blablabla
 */
public class HeaderViewHolder extends QMUIStickySectionAdapter.ViewHolder {
    private QMUIAlphaTextView timeText;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        timeText = itemView.findViewById(R.id.time_text);
    }

    public void bind(SectionHeader header) {
        timeText.setText(header.getShowTime());
    }
}
