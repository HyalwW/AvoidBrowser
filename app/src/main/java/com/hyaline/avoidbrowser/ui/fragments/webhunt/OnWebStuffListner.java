package com.hyaline.avoidbrowser.ui.fragments.webhunt;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/16
 * Description: blablabla
 */
public interface OnWebStuffListner {
    void onLoadStart();

    void onLoadFinish();

    void onReceiveUrl(String url);

    void onTitleChanged(String title);

    int getScrollHeight();
}
