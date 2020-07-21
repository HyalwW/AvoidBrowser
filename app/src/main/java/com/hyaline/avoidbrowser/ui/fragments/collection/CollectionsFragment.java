package com.hyaline.avoidbrowser.ui.fragments.collection;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.VibrateUtils;
import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseFragment;
import com.hyaline.avoidbrowser.data.beans.CollectBean;
import com.hyaline.avoidbrowser.databinding.FragmentCollectionsBinding;
import com.hyaline.avoidbrowser.ui.activities.main.MainActivity;
import com.hyaline.avoidbrowser.ui.dialogs.CollectEditDialog;
import com.hyaline.avoidbrowser.utils.ThreadPool;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;


public class CollectionsFragment extends BaseFragment<CollectionsViewModel, FragmentCollectionsBinding> implements QMUIQuickAction.OnClickListener {
    private QMUIQuickAction popup;
    private CollectBean selectBean;
    private CollectEditDialog dialog;

    @Override
    protected int layoutId() {
        return R.layout.fragment_collections;
    }

    @Override
    protected int viewModelId() {
        return BR.viewModel;
    }

    @Override
    protected void initData() {
        viewModel.getDao().loadLiveCollections().observe(this, collectBeans -> viewModel.setData(collectBeans));
    }

    @Override
    protected void initView() {
        viewModel.getClickEvent().observe(this, bean -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("extra", "fromCollection");
            intent.putExtra("url", bean.getUrl());
            startActivity(intent);
        });
        viewModel.getLongClickEvent().observe(this, bean -> {
            VibrateUtils.vibrate(10);
            showPopup(bean);
        });
        viewModel.getMoreEvent().observe(this, this::showPopup);
        viewModel.getUnCollectEvent().observe(this, bean -> selectBean = bean);
    }

    private void showPopup(CollectBean bean) {
        selectBean = bean;
        checkPopup();
        int position = viewModel.getItems().indexOf(bean);
        RecyclerView.ViewHolder viewHolder = dataBinding.recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            popup.show(viewHolder.itemView);
        }
    }

    private void checkPopup() {
        if (popup == null) {
            popup = QMUIPopups.quickAction(getContext(), QMUIDisplayHelper.dp2px(getContext(), 56), QMUIDisplayHelper.dp2px(getContext(), 56))
                    .shadow(true)
                    .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 10))
                    .bgColor(0xFFFFE4B5)
                    .addAction(newAction(R.drawable.edit, "编辑", this))
                    .addAction(newAction(R.drawable.delete, "删除", this));
        }
    }

    private QMUIQuickAction.Action newAction(int icon, String text, QMUIQuickAction.OnClickListener listener) {
        return new QMUIQuickAction.Action().icon(icon).text(text).onClick(listener);
    }

    @Override
    public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
        if (selectBean == null) {
            return;
        }
        switch (position) {
            case 0:
                quickAction.dismiss();
                checkDialog();
                dialog.show(true);
                break;
            case 1:
                viewModel.delete(selectBean);
                quickAction.dismiss();
                break;
        }
    }

    private void checkDialog() {
        if (dialog == null) {
            dialog = new CollectEditDialog(getContext(), "编辑书签", "在此输入书签名")
                    .listener(new CollectEditDialog.OnDialogListener() {
                        @Override
                        public void onCancel(QMUIDialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirm(QMUIDialog dialog, String text) {
                            if (!TextUtils.isEmpty(text)) {
                                dialog.dismiss();
                                CollectBean bean = new CollectBean();
                                bean.setId(selectBean.getId());
                                bean.setUrl(selectBean.getUrl());
                                bean.setName(text);
                                Toast.makeText(getContext(), "书签名已修改", Toast.LENGTH_SHORT).show();
                                ThreadPool.fixed().execute(() -> viewModel.getDao().update(bean));
                            } else {
                                Toast.makeText(getContext(), "书签名不能为空", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
