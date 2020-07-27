package com.hyaline.avoidbrowser.ui.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseSurfaceView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/22
 * Description: 一个酷炫自定义菜单选择控件
 */
public class MenuView extends BaseSurfaceView {
    private float gap, radius;
    private float hbw;
    private static final double PI = Math.PI;
    private Menu isSelected;
    private List<Menu> menus;
    private boolean isReset;
    private Paint paint;
    private RectF dst;
    private Bitmap bb;
    private OnMenuStateChangedListener listener;

    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInit() {
        menus = new CopyOnWriteArrayList<>();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dst = new RectF();
        bb = BitmapFactory.decodeResource(getResources(), R.drawable.menu_white);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
    }

    @Override
    protected void onReady() {
        startAnim();
    }

    @Override
    protected void onDataUpdate() {
        if (isReset) {
            isReset = false;
            reset();
        }
        for (Menu menu : menus) {
            menu.move();
        }
    }

    @Override
    protected void onRefresh(Canvas canvas) {
        for (Menu menu : menus) {
            menu.draw(canvas, paint);
        }
        float cx = bx();
        float cy = by();
        paint.setColor(isTouch ? Color.CYAN : Color.BLUE);
        canvas.drawCircle(cx, cy, radius, paint);
        dst.set(cx - hbw, cy - hbw, cx + hbw, cy + hbw);
        canvas.drawBitmap(bb, null, dst, null);
    }

    @Override
    protected void draw(Canvas canvas, Object data) {

    }

    @Override
    protected void onDrawRect(Canvas canvas, Object data, Rect rect) {

    }

    @Override
    protected boolean preventClear() {
        return false;
    }

    public void addMenu(String text, Bitmap icon) {
        menus.add(new Menu(text, icon));
        isReset = true;
    }

    private void reset() {
        int c = 1;
        if (menus.size() > 0) {
            int loopCount = getLoopCount(menus.size());
            c += loopCount;
        }
        float tw = getMeasuredWidth() / (2f * c);
        radius = tw * 0.65f;
        hbw = tw * 0.25f;
        gap = tw * 0.2f;
        int loop = 1;
        int bi = 0;
        int count = 2;
        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            int mode = (i - bi) % count;
            double ag = (PI / 2) / count;
            double ma = 3 * PI / 2 - ag / 2;
            menu.init(ma - mode * ag, loop * (gap + radius) * 2);
            if (i - bi == count - 1) {
                loop++;
                bi += count;
                count += 2;
            }
        }
        if (isTouch) {
            for (Menu menu : menus) {
                menu.go(true);
            }
        }
    }

    private int getLoopCount(int size) {
        int loop = 0;
        int count = 0, sumCount = 0;
        while ((sumCount + count + 2) < size) {
            loop++;
            count += 2;
            sumCount += count;
        }
        return loop + 1;
    }

    private float dx, dy;
    private boolean isTouch;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = eventX;
                dy = eventY;
                float bx = bx();
                float by = by();
                if ((dx - bx) * (dx - bx) + (dy - by) * (dy - by) < radius * radius) {
                    isTouch = true;
                    if (listener != null) {
                        listener.onMenuOpened();
                    }
                    for (Menu menu : menus) {
                        menu.go(true);
                    }
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
            case MotionEvent.ACTION_MOVE:
                if (isSelected != null) {
                    if (!isSelected.isIn(eventX, eventY)) {
                        isSelected.choose(false);
                        if (listener != null) {
                            listener.onItemExited(isSelected.text);
                        }
                        isSelected = null;
                    }
                } else {
                    for (Menu menu : menus) {
                        if (menu.isIn(eventX, eventY) && isSelected != menu) {
                            isSelected = menu;
                            if (listener != null) {
                                listener.onItemEntered(menu.text);
                            }
                            menu.choose(true);
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouch = false;
                if (isSelected != null) {
                    if (listener != null) {
                        listener.onItemSelected(isSelected.text);
                    }
                    isSelected = null;
                }
                if (listener != null) {
                    listener.onMenuClosed();
                }
                for (Menu menu : menus) {
                    menu.go(false);
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    public float bx() {
        return getMeasuredWidth() - gap - radius;
    }

    public float by() {
        return getMeasuredHeight() - gap - radius;
    }

    public class Menu {
        String text;
        Bitmap icon;
        private double angle;
        private float bLen, cLen, tLen, li;

        Menu(String text, Bitmap icon) {
            this.text = text;
            this.icon = icon;
        }

        void init(double a, float bl) {
            angle = a;
            bLen = bl;
            li = bLen * 0.07f;
        }

        void go(boolean max) {
            tLen = max ? bLen : 0;
        }

        void choose(boolean choose) {
            tLen = choose ? bLen * 1.1f : bLen;
        }

        void move() {
            if (cLen != tLen) {
                if (tLen > cLen) {
                    cLen += li;
                    if (cLen > tLen) {
                        cLen = tLen;
                    }
                } else {
                    cLen -= li;
                    if (cLen < tLen) {
                        cLen = tLen;
                    }
                }
            }
        }

        public String getText() {
            return text;
        }

        public Bitmap getIcon() {
            return icon;
        }

        boolean isIn(float x, float y) {
            float bx = (float) (bx() + bLen * Math.cos(angle));
            float by = (float) (by() + bLen * Math.sin(angle));
            return (x - bx) * (x - bx) + (y - by) * (y - by) < radius * radius;
        }

        void draw(Canvas canvas, Paint paint) {
            canvas.save();
            float scale = cLen / bLen;
            float cx = (float) (bx() + cLen * Math.cos(angle));
            float cy = (float) (by() + cLen * Math.sin(angle));
            paint.setColor(isSelected == this ? Color.GREEN : Color.BLUE);
            canvas.scale(scale, scale, cx, cy);
            canvas.drawCircle(cx, cy, radius, paint);
            dst.set(cx - hbw, cy - hbw, cx + hbw, cy + hbw);
            canvas.drawBitmap(icon, null, dst, null);
            canvas.restore();
        }
    }

    public void setListener(OnMenuStateChangedListener listener) {
        this.listener = listener;
    }

    public static abstract class Listener implements OnMenuStateChangedListener {
        @Override
        public void onMenuOpened() {

        }

        @Override
        public void onItemEntered(String tag) {

        }

        @Override
        public void onItemExited(String tag) {

        }

        @Override
        public void onItemSelected(String tag) {

        }

        @Override
        public void onMenuClosed() {

        }
    }

    private interface OnMenuStateChangedListener {
        //菜单打开
        void onMenuOpened();

        //手指子菜单
        void onItemEntered(String tag);

        //手指移出子菜单
        void onItemExited(String tag);

        //手指离开时子菜单被选中
        void onItemSelected(String tag);

        //菜单关闭
        void onMenuClosed();
    }
}
