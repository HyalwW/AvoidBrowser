package com.hyaline.avoidbrowser.ui.fragments.webhunt;

import android.graphics.Bitmap;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/17
 * Description: blablabla
 */
public class PageInfo {
    private String url;
    private ObservableField<String> title;
    private Bitmap icon;
    private ObservableField<Bitmap> cache;
    private ObservableBoolean isShow;

    public PageInfo() {
        title = new ObservableField<>("");
        cache = new ObservableField<>();
        isShow = new ObservableBoolean(true);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitleStr() {
        return title.get();
    }

    public ObservableField<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        if (this.icon != null && !this.icon.isRecycled()) {
            this.icon.recycle();
        }
        this.icon = icon;
    }

    public ObservableField<Bitmap> getCache() {
        return cache;
    }

    public void setCacheBitmap(Bitmap cache) {
        this.cache.set(cache);
    }

    public ObservableBoolean getIsShow() {
        return isShow;
    }

    public boolean isShow() {
        return isShow.get();
    }

    public void setIsShow(boolean isShow) {
        this.isShow.set(isShow);
    }

    public void destroyCache() {
        if (this.cache != null) {
            Bitmap cache = this.cache.get();
            if (cache != null && !cache.isRecycled()) {
                cache.recycle();
            }
            this.cache.set(null);
        }
    }
}
