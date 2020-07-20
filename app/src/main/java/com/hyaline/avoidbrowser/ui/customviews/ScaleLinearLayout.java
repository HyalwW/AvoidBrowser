package com.hyaline.avoidbrowser.ui.customviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ScaleLinearLayout extends LinearLayout {
    private ValueAnimator animator;
    private boolean isOut;
    private float scale;
    private static final float minScale = 0.95f;

    public ScaleLinearLayout(Context context) {
        this(context, null);
    }

    public ScaleLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        animator = new ValueAnimator();
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            scale = (float) animation.getAnimatedValue();
            invalidate();
        });
        scale = 1;
    }

    @Override
    public void draw(Canvas canvas) {
        if (scale != 0) {
            canvas.scale(scale, scale, getMeasuredWidth() >> 1, getMeasuredHeight() >> 1);
        }
        super.draw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float ex = event.getX(), ey = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                zoomOut();
                break;
            case MotionEvent.ACTION_MOVE:
                if (ex < 0 || ex > getMeasuredWidth() || ey < 0 || ey > getMeasuredHeight()) {
                    zoomIn();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                zoomIn();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private void zoomIn() {
        if (scale != 1 && !isOut) {
            isOut = true;
            animator.cancel();
            animator.setFloatValues(scale, 1f);
            animator.start();
        }
    }

    private void zoomOut() {
        isOut = false;
        scale = 0.999999f;
        animator.cancel();
        animator.setFloatValues(scale, minScale);
        animator.start();
    }
}
