package com.hyaline.avoidbrowser.ui.activities.imageviewer;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hyaline.avoidbrowser.base.BaseViewModel;
import com.hyaline.avoidbrowser.utils.BindingCommand;
import com.hyaline.avoidbrowser.utils.Constant;
import com.hyaline.avoidbrowser.utils.SingleLiveEvent;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/23
 * Description: blablabla
 */
public class ImageViewModel extends BaseViewModel {
    private ObservableBoolean showOption;
    private boolean isSaved;
    private BindingCommand onShareClick, onBackClick, onSaveClick;
    private SingleLiveEvent shareEvent, backEvent, saveEvent;

    public ImageViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        showOption = new ObservableBoolean(true);
        isSaved = false;
        backEvent = new SingleLiveEvent();
        saveEvent = new SingleLiveEvent();
        shareEvent = new SingleLiveEvent();
        onBackClick = new BindingCommand(() -> backEvent.call());
        onSaveClick = new BindingCommand(() -> saveEvent.call());
        onShareClick = new BindingCommand(() -> shareEvent.call());
    }

    @Override
    protected void onDestroy() {

    }

    @Override
    protected void parseIntent(Intent intent) {

    }

    public void saveImage(String url) {
        if (isSaved) {
            ToastUtils.showShort("图片已经保存啦~");
            return;
        }
        Disposable subscribe = Observable.create((ObservableOnSubscribe<String>) emitter -> {
            try {
                URL iconUrl = new URL(url);
                URLConnection conn = iconUrl.openConnection();
                HttpURLConnection http = (HttpURLConnection) conn;
                int length = http.getContentLength();
                conn.connect();
                // 获得图像的字符流
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is, length);
                bis.mark(0);
                int type = bis.read();
                bis.reset();
                switch (type) {
                    case 255://jpg
                    case 137://png
                        Bitmap bitmap = BitmapFactory.decodeStream(bis);
                        String path = Constant.IMAGE_PATH + "image" + System.currentTimeMillis() + (type == 255 ? ".jpg" : ".png");
                        ImageUtils.save(bitmap, path, type == 255 ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, true);
                        emitter.onNext("图片保存成功~");
                        break;
                    case 71://gif
                        byte[] bytes = toByteArray(bis);
                        File file = new File(Constant.IMAGE_PATH + "image" + System.currentTimeMillis() + ".gif");
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(bytes, 0, bytes.length);
                            fos.flush();
                            fos.close();
                            emitter.onNext("gif图片保存成功~");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                bis.close();
                is.close();

            } catch (Exception e) {
                emitter.onNext("图片保存失败！");
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(text -> {
                    if (text.contains("成功")) {
                        isSaved = true;
                    }
                    ToastUtils.showShort(text);
                });
    }

    private byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public ObservableBoolean getShowOption() {
        return showOption;
    }

    void showOption(boolean show) {
        showOption.set(show);
    }

    public BindingCommand getOnShareClick() {
        return onShareClick;
    }

    public BindingCommand getOnBackClick() {
        return onBackClick;
    }

    public BindingCommand getOnSaveClick() {
        return onSaveClick;
    }

    public SingleLiveEvent getShareEvent() {
        return shareEvent;
    }

    public SingleLiveEvent getBackEvent() {
        return backEvent;
    }

    public SingleLiveEvent getSaveEvent() {
        return saveEvent;
    }
}
