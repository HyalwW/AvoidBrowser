package com.hyaline.avoidbrowser.ui.fragments;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/16
 * Description: blablabla
 */
public interface OnWebStuffListner {
    void onLoadStart();

    void onLoadFinish();

    void onTitleChanged(String title);

    int getScrollHeight();
}
