package com.imax.ipt.ui.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by yanli on 2015/7/22.
 */
public class DisplayUtil {
    //以下三个常量为基准参数，取决于开发界面时所使用的设备的屏幕参数，其余的设备都基于此参数进行适配
    public static final int BASE_WIDTH = 2560;//1280;		//屏幕的宽度
    public static final int BASE_HEIGHT =1600;// 720;		//屏幕的高度
    public static final int BASE_DENSITYDPI = 320;//160;	//屏幕的像素密度
    private static final String TAG = "DisplayUtil" ;

    //以下三个变量为适配比例，即需要适配的设备和基准设备的屏幕参数比例
    public static float widthRatio;			//宽度比例
    public static float heightRatio;			//高度比例
    public static float densityRatio;			//像素密度比例

    //屏幕参数类型，分别表示宽度、高度和密度
    public enum ScaleType {WIDTH, HEIGHT, DENSITY}

    /** 初始化比例参数 */
    public static void init(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        widthRatio = dm.widthPixels / BASE_WIDTH;
        heightRatio = dm.heightPixels / BASE_HEIGHT;
        densityRatio = BASE_DENSITYDPI / dm.densityDpi;
    }

    /** 根据比例参数进行计算，并返回适配后的值 */
    public static int resize(int size, ScaleType type) {
        int result = 0;
        switch (type) {
            case WIDTH:
                result = (int) (size * widthRatio);
                break;
            case HEIGHT:
                result = (int) (size * heightRatio);
                break;
            case DENSITY:
                result = (int) (size * densityRatio);
                break;
        }
        return result;
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue/scale + 0.5f);
    }

    public static int pt2sp(Context context, float ptValue) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return (int) (ptValue*scale*(1.0f/72));
    }

}
