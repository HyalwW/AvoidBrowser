package com.hyaline.avoidbrowser.ui.fragments.webhunt;

import android.graphics.Bitmap;

import androidx.databinding.ObservableField;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/17
 * Description: blablabla
 */
public class PageInfo {
    private String url;
    private String title;
    private Bitmap icon;
    private ObservableField<Bitmap> cache;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        if (this.cache == null) {
            this.cache = new ObservableField<>(null);
        }
        this.cache.set(cache);
    }

    public void destroyCache() {
        if (this.cache != null ) {
            Bitmap cache = this.cache.get();
            if (cache != null && !cache.isRecycled()) {
                cache.recycle();
            }
            this.cache.set(null);
        }
    }
}
