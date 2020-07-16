package com.hyaline.avoidbrowser.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;

import com.qmuiteam.qmui.layout.QMUIConstraintLayout;

public class DynamicConstraintLayout extends QMUIConstraintLayout {
    private int newHeight = -1;

    public DynamicConstraintLayout(Context context) {
        super(context);
    }

    public DynamicConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (newHeight != -1) {
            int mode = MeasureSpec.getMode(heightMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, mode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setNewHeight(int newHeight) {
        this.newHeight = newHeight;
        requestLayout();
    }
}
