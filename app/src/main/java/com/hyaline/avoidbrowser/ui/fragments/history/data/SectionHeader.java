package com.hyaline.avoidbrowser.ui.fragments.history.data;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.qmuiteam.qmui.widget.section.QMUISection;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/20
 * Description: blablabla
 */
public class SectionHeader implements QMUISection.Model<SectionHeader> {
    private long timeStart;
    private String showTime;

    public SectionHeader(long timeStart) {
        this.timeStart = timeStart;
        showTime = createShowTime();
    }

    @Override
    public SectionHeader cloneForDiff() {
        return copy();
    }

    @Override
    public boolean isSameItem(SectionHeader other) {
        return timeStart == other.timeStart;
    }

    @Override
    public boolean isSameContent(SectionHeader other) {
        return true;
    }

    public SectionHeader copy() {
        return new SectionHeader(timeStart);
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public boolean isToday() {
        long millis = System.currentTimeMillis();
        return millis >= timeStart && millis < timeStart + TimeConstants.DAY;
    }

    public String createShowTime() {
        long time = System.currentTimeMillis();
        if (time >= timeStart && time < timeStart + TimeConstants.DAY) {
            return "今天";
        } else if (time - TimeConstants.DAY >= timeStart && time - TimeConstants.DAY < timeStart + TimeConstants.DAY) {
            return "昨天 ";
        } else if ((System.currentTimeMillis() - timeStart) < 90 * (long) TimeConstants.DAY) {
            return TimeUtils.millis2String(timeStart, "M月dd日");
        } else {
            return TimeUtils.millis2String(timeStart, "yyyy年MM月dd日");
        }
    }
}
