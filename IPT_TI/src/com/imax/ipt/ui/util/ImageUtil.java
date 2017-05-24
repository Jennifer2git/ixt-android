package com.imax.ipt.ui.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageUtil {
    public static final String TAG = "ImageUtil";

    public static Bitmap getImage(String url) {
        Log.d(TAG, url);
        Bitmap bm = null;
        BufferedInputStream bis = null;
        InputStream is = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.setUseCaches(true);
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            //TODO    Log.w(TAG, e.getMessage(),e);
            Log.w(TAG, "URL = " + url);
            Log.w(TAG, e.getMessage(), e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                //TODO enable it   Log.e(TAG, e.getMessage());
                Log.e(TAG, e.getMessage());
            }
        }
        return bm;
    }
}
