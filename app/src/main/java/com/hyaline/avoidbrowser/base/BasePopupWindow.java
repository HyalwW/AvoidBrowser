package com.hyaline.avoidbrowser.base;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

public abstract class BasePopupWindow<DB extends ViewDataBinding, VM extends ViewModel> extends PopupWindow {
    protected DB dataBinding;
    protected VM viewModel;
    protected Context mContext;

    public static final int DIRECTION_TOP = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final int DIRECTION_BOTTOM = 3;
    public static final int DIRECTION_LEFT = 4;

    public BasePopupWindow(Context context, VM viewModel) {
        super(context);
        mContext = context;
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId(), null, false);
        this.viewModel = viewModel;
        dataBinding.setVariable(viewModel(), viewModel);
        initData();
        initView();
        setContentView(dataBinding.getRoot());
        setWidth(width());
        setHeight(height());
        setFocusable(true);
        setAnimationStyle(animStyle());
    }

    /**
     * @param view      展示的依赖view
     * @param direction {@link #DIRECTION_TOP,#DIRECTION_RIGHT,#DIRECTION_BOTTOM,#DIRECTION_LEFT} 其一
     * @param marginDp  margin
     */
    public void showAtLocation(View view, int direction, float marginDp) {
        dataBinding.getRoot().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = dataBinding.getRoot().getMeasuredWidth();
        int popupHeight = dataBinding.getRoot().getMeasuredHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x, y;
        switch (direction) {
            case DIRECTION_LEFT:
                x = location[0] - popupWidth - dp2px(marginDp);
                y = (location[1] + view.getHeight() / 2) - popupHeight / 2;
                break;
            case DIRECTION_TOP:
                x = (location[0] + view.getWidth() / 2) - popupWidth / 2;
                y = location[1] - popupHeight - dp2px(marginDp);
                break;
            case DIRECTION_RIGHT:
                x = location[0] + view.getWidth() + dp2px(marginDp);
                y = (location[1] + view.getHeight() / 2) - popupHeight / 2;
                break;
            case DIRECTION_BOTTOM:
            default:
                x = (location[0] + view.getWidth() / 2) - popupWidth / 2;
                y = location[1] + view.getHeight() + dp2px(marginDp);
                break;
        }
        showAtLocation(view, Gravity.NO_GRAVITY, x, y);
        update();
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, mContext.getResources().getDisplayMetrics());
    }

    protected abstract int layoutId();

    protected abstract void initData();

    protected abstract void initView();


    protected abstract int animStyle();

    protected abstract int width();

    protected abstract int height();

    protected abstract int viewModel();
}
