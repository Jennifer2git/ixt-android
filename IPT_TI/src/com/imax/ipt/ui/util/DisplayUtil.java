package com.imax.ipt.ui.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by yanli on 2015/7/22.
 */
public class DisplayUtil {
    //������������Ϊ��׼������ȡ���ڿ�������ʱ��ʹ�õ��豸����Ļ������������豸�����ڴ˲�����������
    public static final int BASE_WIDTH = 2560;//1280;		//��Ļ�Ŀ��
    public static final int BASE_HEIGHT =1600;// 720;		//��Ļ�ĸ߶�
    public static final int BASE_DENSITYDPI = 320;//160;	//��Ļ�������ܶ�
    private static final String TAG = "DisplayUtil" ;

    //������������Ϊ�������������Ҫ������豸�ͻ�׼�豸����Ļ��������
    public static float widthRatio;			//��ȱ���
    public static float heightRatio;			//�߶ȱ���
    public static float densityRatio;			//�����ܶȱ���

    //��Ļ�������ͣ��ֱ��ʾ��ȡ��߶Ⱥ��ܶ�
    public enum ScaleType {WIDTH, HEIGHT, DENSITY}

    /** ��ʼ���������� */
    public static void init(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        widthRatio = dm.widthPixels / BASE_WIDTH;
        heightRatio = dm.heightPixels / BASE_HEIGHT;
        densityRatio = BASE_DENSITYDPI / dm.densityDpi;
    }

    /** ���ݱ����������м��㣬������������ֵ */
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
