//package com.hyaline.avoidbrowser.ui.customviews;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//
///**
// * Created by Wang.Wenhui
// * Date: 2020/7/22
// * Description: blablabla
// */
//public class MenuView extends View {
//    private float gap, maxScale, radius;
//
//    public MenuView(Context context) {
//        this(context, null);
//    }
//
//    public MenuView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public MenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }
//
//    public static class Menu {
//        String text;
//        Bitmap icon;
//        private float cx, cy, cs, tx, ty, ts;
//
//        public Menu(String text, Bitmap icon) {
//            this.text = text;
//            this.icon = icon;
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public Bitmap getIcon() {
//            return icon;
//        }
//    }
//}
