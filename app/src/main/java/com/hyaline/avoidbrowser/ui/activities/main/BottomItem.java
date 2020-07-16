package com.hyaline.avoidbrowser.ui.activities.main;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/16
 * Description: blablabla
 */
public class BottomItem {
    private String tag;
    private int imgRes;

    public BottomItem(String tag, int imgRes) {
        this.tag = tag;
        this.imgRes = imgRes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }
}
