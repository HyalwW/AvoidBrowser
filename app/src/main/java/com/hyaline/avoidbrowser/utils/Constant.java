package com.hyaline.avoidbrowser.utils;

import android.os.Environment;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/22
 * Description: blablabla
 */
public interface Constant {
    String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/avoidBrowser/";

    String IMAGE_PATH = BASE_PATH + "images/";
}
