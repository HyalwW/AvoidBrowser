package com.hyaline.avoidbrowser.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @BindingAdapter(value = {"imgBitmap"})
    public static void setImageBitmap(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
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

    @BindingAdapter(value = {"spanSize", "gaph", "gapv", "orientation"}, requireAll = false)
    public static void setRecyclerView(RecyclerView recyclerView, int spanSize, int gaph, int gapv, int orientation) {
        LinearLayoutManager layout;
        int gapH = gaph;
        int gapV = gapv;
        int ori = orientation == 0 ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;
        if (spanSize == 0) {
            layout = new LinearLayoutManager(recyclerView.getContext());
            recyclerView.addItemDecoration(new MyItemDecoration(gapH, gapV));
            layout.setOrientation(ori);
        } else {
            layout = new GridLayoutManager(recyclerView.getContext(), spanSize);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanSize, gapH, gapV, false));
            layout.setOrientation(ori);
        }
        recyclerView.setLayoutManager(layout);

    }

    private static class MyItemDecoration extends RecyclerView.ItemDecoration {

        /**
         * item横向和垂直方向间距
         */
        private final int gapH;
        private final int gapV;

        MyItemDecoration(int gapH, int gapV) {
            this.gapH = gapH;
            this.gapV = gapV;
            Log.d("MyItemDecoration", "gapH:" + gapH);
            Log.d("MyItemDecoration", "gapV:" + gapV);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = gapV / 2;
            outRect.top = gapV / 2;
            outRect.left = gapH / 2;
            outRect.right = gapH / 2;

        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount; //列数
        private int spacingH; //横向间隔
        private int spacingV; //纵向间隔
        private boolean includeEdge; //是否包含边缘

        public GridSpacingItemDecoration(int spanCount, int spacingH, int spacingV, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacingH = spacingH;
            this.spacingV = spacingV;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            //这里是关键，需要根据你有几列来判断
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacingH - column * spacingH / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacingH / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacingV;
                }
                outRect.bottom = spacingV; // item bottom
            } else {
                outRect.left = column * spacingH / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacingH - (column + 1) * spacingH / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacingV; // item top
                }
            }
        }
    }

}
