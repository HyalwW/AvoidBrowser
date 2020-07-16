package com.hyaline.avoidbrowser.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/16
 * Description: blablabla
 */
public class LoadingView extends View {
    private Paint paint;
    private double pos;
    private float xInc, persent, pInc;
    private boolean isLoading;
    private final double PI = Math.PI;
    private static final long rate = 10;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isLoading) {
                if (rgb[colorType] > nextColor) {
                    rgb[colorType]--;
                } else if (rgb[colorType] < nextColor) {
                    rgb[colorType]++;
                } else {
                    colorType = random.nextInt(3);
                    nextColor = randomRGB();
                }
                pos += xInc;
                if (pos % (2 * PI) == 0) {
                    pos = 0;
                }
                postDelayed(this, rate);
            } else {
                if (persent < 1) {
                    persent += pInc;
                    postDelayed(this, rate);
                } else {
                    if (listener != null) {
                        listener.onLoadAnimDone();
                    }
                }
            }
            invalidate();
        }
    };
    private Random random;
    private int[] rgb = new int[]{255, 255, 255};
    private int nextColor, colorType;

    private OnLoadListener listener;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xInc = (float) (PI / 100);
        pInc = 1 / 30f;
        random = new Random();
        nextColor = randomRGB();
        colorType = random.nextInt(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = (float) (getMeasuredWidth() / 2f + getMeasuredWidth() / 2f * Math.sin(pos));
        paint.setColor(Color.rgb(255 - rgb[0], 255 - rgb[1], 255 - rgb[2]));
        canvas.drawColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        float v = getMeasuredHeight() * 0.3f;
        float radius = isLoading ? v : Math.max(v, persent * getMeasuredWidth() * 2);
        canvas.drawCircle(x, getMeasuredHeight() >> 1, radius, paint);
        if (isLoading) {
            for (int i = 1; i < 3; i++) {
                float xx = (float) (getMeasuredWidth() / 2f + getMeasuredWidth() / 2f * Math.sin(pos - (PI / 16 * i)));
                paint.setColor(Color.rgb((255 - rgb[0]) / (i + 1), (255 - rgb[1]) / (i + 1), (255 - rgb[2]) / (i + 1)));
                radius = radius * 0.8f;
                canvas.drawCircle(xx, getMeasuredHeight() >> 1, radius, paint);
            }
        }
    }

    private int randomRGB() {
        return random.nextInt(255);
    }

    public void startLoading() {
        removeCallbacks(runnable);
        persent = 0;
        isLoading = true;
        post(runnable);
        if (listener != null) {
            listener.onLoadAnimStart();
        }
    }

    public void done() {
        isLoading = false;
    }

    public void destroy() {
        removeCallbacks(runnable);
    }

    public void setListener(OnLoadListener listener) {
        this.listener = listener;
    }

    public interface OnLoadListener {
        void onLoadAnimStart();

        void onLoadAnimDone();
    }
}
