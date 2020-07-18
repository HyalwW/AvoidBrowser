package com.hyaline.avoidbrowser.utils;

import androidx.lifecycle.Observer;

public class Action<T> {
    private Observer<T> observer;

    public void observe(Observer<T> observer) {
        this.observer = observer;
    }

    public void setValue(T value) {
        if (observer != null) {
            observer.onChanged(value);
        }
    }

    public void call() {
        setValue(null);
    }
}
