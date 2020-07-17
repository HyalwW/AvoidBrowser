package com.hyaline.avoidbrowser.utils;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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

    @BindingAdapter(value = {"imgRes"})
    public static void setImageRes(ImageView view, int res) {
        view.setImageResource(res);
    }

    @BindingAdapter(value = {"viewVisible", "showAnim", "hideAnim"}, requireAll = false)
    public static void setViewVisible(View view, boolean visible, int showAnim, int hideAnim) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
                if (showAnim != 0) {
                    view.setAnimation(AnimationUtils.loadAnimation(view.getContext(), showAnim));
                }
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
                if (hideAnim != 0) {
                    view.setAnimation(AnimationUtils.loadAnimation(view.getContext(), hideAnim));
                }
            }
        }
    }
}
