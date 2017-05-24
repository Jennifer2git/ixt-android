package com.imax.ipt.ui.util;

import android.content.Context;
import android.os.Vibrator;

public class VibrateUtil {


    public static void vibrate(Context context, int mVibrationDuration) {
        Vibrator mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (mVibrator != null) {
            mVibrator.vibrate(mVibrationDuration);
        }
    }
}
