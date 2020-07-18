package com.hyaline.avoidbrowser.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hyaline.avoidbrowser.R;

public class MaxHeightRecyclerView extends RecyclerView {
    private float maxHeight;

    public MaxHeightRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public MaxHeightRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaxHeightRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView);
            maxHeight = array.getDimension(R.styleable.MaxHeightRecyclerView_maxHeight, 0);
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight != 0) {
            int h = MeasureSpec.getSize(heightSpec);
            int m = MeasureSpec.getMode(heightSpec);
            heightSpec = MeasureSpec.makeMeasureSpec(Math.min(h, (int) maxHeight), m);
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
