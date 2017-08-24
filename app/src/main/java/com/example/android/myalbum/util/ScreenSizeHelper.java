package com.example.android.myalbum.util;

import android.content.Context;

/**
 * Created by android on 17-8-24.
 */

public final class ScreenSizeHelper {

    public static int getScreenWidthPxiels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeigthPxiels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidthDip(Context context) {
        int widthPix = context.getResources().getDisplayMetrics().widthPixels;
        return ScreenSizeHelper.px2dip(context, widthPix);
    }

    public static int getScreenHeigthDip(Context context) {
        int heightPix = context.getResources().getDisplayMetrics().heightPixels;
        return ScreenSizeHelper.px2dip(context, heightPix);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dip2px(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue * fontScale + 0.5f);
    }
}
