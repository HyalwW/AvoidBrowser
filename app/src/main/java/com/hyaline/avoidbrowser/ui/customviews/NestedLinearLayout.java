package com.hyaline.avoidbrowser.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.view.NestedScrollingParentHelper;

import com.qmuiteam.qmui.layout.QMUILinearLayout;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/17
 * Description: blablabla
 */
public class NestedLinearLayout extends QMUILinearLayout {
    private NestedScrollingParentHelper helper;

    public NestedLinearLayout(Context context) {
        super(context);
        init();
    }

    public NestedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        helper = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(size + getChildAt(0).getMeasuredHeight(), mode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        View child = getChildAt(0);
        int sy = getScrollY();
        int height = child.getMeasuredHeight();
        if (dy > 0) {
            if (sy < height) {
                if (dy > height - sy) {
                    consumed[1] = height - sy;
                } else {
                    consumed[1] = dy;
                }
                scrollBy(0, consumed[1]);
            }
        } else if (dy < 0) {
            if (sy > 0) {
                if (-dy > sy) {
                    consumed[1] = -sy;
                } else {
                    consumed[1] = dy;
                }
                scrollBy(0, consumed[1]);
            }
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        helper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        helper.onStopNestedScroll(child);
    }
}
