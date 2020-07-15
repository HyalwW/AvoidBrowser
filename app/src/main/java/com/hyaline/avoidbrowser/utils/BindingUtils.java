package com.hyaline.avoidbrowser.utils;

import android.view.View;

import androidx.databinding.BindingAdapter;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/3
 * Description: blablabla
 */
public class BindingUtils {
    @BindingAdapter(value = {"clickCommand", "clickData"}, requireAll = false)
    public static <T> void onClickCommand(View view, BindingCommand<T> command, T data) {
        view.setOnClickListener(v -> {
            if (command != null) {
                if (data != null) {
                    command.execute(data);
                } else {
                    command.execute();
                }
            }
        });
    }
}
