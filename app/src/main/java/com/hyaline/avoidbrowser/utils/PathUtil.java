package com.hyaline.avoidbrowser.utils;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public class PathUtil {
    private static RectF bounds;
    private static Region region;
    private static Region clip;

    static {
        bounds = new RectF();
        region = new Region();
        clip = new Region();
    }

    public static boolean isInside(Path path, int x, int y) {
        bounds.setEmpty();
        path.computeBounds(bounds, true);
        clip.set(((int) bounds.left), ((int) bounds.top), ((int) bounds.right), (int) bounds.bottom);
        region.setPath(path, clip);
        return region.contains(x, y);
    }
}
