package com.hyaline.avoidbrowser.ui.activities.history.data;

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
        showTime = getShowTime(timeStart);
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

    public boolean isSameDay(long time) {
        return time >= timeStart && time < timeStart + TimeConstants.DAY;
    }

    public String getShowTime(long time) {
        if (isSameDay(time)) {
            return "今天";
        } else if (isSameDay(time + TimeConstants.DAY)) {
            return "昨天 ";
        } else if ((System.currentTimeMillis() - time) < 7 * TimeConstants.DAY) {
            return TimeUtils.millis2String(time, "EEE");
        } else if ((System.currentTimeMillis() - time) < 90 * (long) TimeConstants.DAY) {
            return TimeUtils.millis2String(time, "M月dd日");
        } else {
            return TimeUtils.millis2String(time, "yyyy年MM月dd日");
        }
    }
}
