package com.hyaline.avoidbrowser.ui.fragments.webhunt.option;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.hyaline.avoidbrowser.R;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIBasePopup;

import java.util.Arrays;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/22
 * Description: blablabla
 */
public class WebOptionPopup extends QMUIBasePopup<WebOptionPopup> {
    private OptionAdapter adapter;
    private ListView listView;
    private OnItemListener listener;
    private String url;

    public WebOptionPopup(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.popup_option, null);
        QMUILinearLayout base = contentView.findViewById(R.id.base);
        base.setRadiusAndShadow(QMUIDisplayHelper.dp2px(context, 3), QMUIDisplayHelper.dp2px(context, 5), 1);
        listView = contentView.findViewById(R.id.list_view);
        this.adapter = new OptionAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (listener != null) {
                listener.onItemClick(adapter.getItem(position), position, url);
            }
        });
        mWindow.setContentView(contentView);
        mWindow.setAnimationStyle(R.style.QMUI_Animation_PopDownMenu_Center);
    }

    public WebOptionPopup setStrs(String[] strs, String url) {
        adapter.setData(Arrays.asList(strs));
        this.url = url;
        listView.requestLayout();
        return this;
    }

    public WebOptionPopup listener(OnItemListener itemListener) {
        listener = itemListener;
        return this;
    }

    @Override
    public void showAtLocation(@NonNull View parent, int x, int y) {
        if (TextUtils.isEmpty(url) || adapter.isEmpty())
            return;
        super.showAtLocation(parent, x, y);
    }

    public interface OnItemListener {
        void onItemClick(String str, int position, String url);
    }
}
