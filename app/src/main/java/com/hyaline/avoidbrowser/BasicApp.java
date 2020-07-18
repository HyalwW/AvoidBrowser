package com.hyaline.avoidbrowser;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public class BasicApp extends Application {
    private static Gson gson;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
        context = this;
        QMUISwipeBackActivityManager.init(this);
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.e("wwh", "BasicApp-->onCoreInitFinished(): " );
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.e("wwh", "BasicApp-->onViewInitFinished(): " + b);
            }
        });
    }

    public static Gson getGson() {
        return gson;
    }

    public static Context getContext() {
        return context;
    }
}
