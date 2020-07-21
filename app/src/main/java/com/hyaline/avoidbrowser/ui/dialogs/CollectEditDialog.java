package com.hyaline.avoidbrowser.ui.dialogs;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/21
 * Description: blablabla
 */
public class CollectEditDialog extends QMUIDialog.EditTextDialogBuilder implements TextWatcher {
    private QMUIDialog dialog;
    private String text;
    private OnDialogListener listener;

    public CollectEditDialog(Context context, String title, String hint) {
        super(context);
        dialog = setPlaceholder(hint)
                .setTitle(title)
                .setTextWatcher(this)
                .addAction("取消", (dialog, index) -> {
                    if (listener != null) {
                        listener.onCancel(dialog);
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEUTRAL, (dialog, index) -> {
                    if (listener != null) {
                        listener.onConfirm(dialog, text);
                    }
                }).create();
    }


    public CollectEditDialog listener(OnDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public void show(boolean clearText) {
        if (dialog != null) {
            if (clearText) {
                clearText();
            }
            dialog.show();
        }
    }

    private void clearText() {
        EditText editText = getEditText();
        if (editText != null) {
            editText.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        text = s.toString();
    }

    public interface OnDialogListener {
        void onCancel(QMUIDialog dialog);

        void onConfirm(QMUIDialog dialog, String text);
    }
}
