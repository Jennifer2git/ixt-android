package com.imax.ipt.ui.activity.settings.maintenance;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.settings.RebootController;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.VibrateUtil;

import java.util.Arrays;

public class RebootFragment extends Fragment implements OnTouchListener {
    private View mLayout;
    private RebootController mController;
    // "int[]" does not work for Arrays.asList().indexOf()
    private Integer[] mButtonIds = new Integer[]{R.id.btn_reboot_mozax,
            R.id.btn_reboot_sony, R.id.btn_reboot_hollywood,
            R.id.btn_reboot_projector1, R.id.btn_reboot_projector2};
    private Integer[] mPowerSockets = new Integer[]{
            Constants.POWERSOCKET_MOAZAEX,
            Constants.POWERSOCKET_SONY,
            Constants.POWERSOCKET_HOLLYWOOD,
            Constants.POWERSOCKET_LEFTPROJECTOR,
            Constants.POWERSOCKET_RIGHTPROJECTOR
    };

    private long downTime = 0;
    private long upTime = 0;
    private Handler mHandler = new Handler();
    private int viewId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mController = new RebootController(this);
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {

            mLayout = inflater.inflate(R.layout.fragment_maintenance_reboot_cn, null);
        } else {
            mLayout = inflater.inflate(R.layout.fragment_maintenance_reboot_en, null);

        }

        IPTTextView rebootPromt = (IPTTextView) mLayout.findViewById(R.id.tv_reboot_prompt);
        rebootPromt.setTypeface(Typeface.createFromAsset(getResources().getAssets(),Constants.FONT_LIGHT_PATH));

        for (int buttonId : mButtonIds) {
            ImageButton button = (ImageButton) mLayout.findViewById(buttonId);
            button.setOnTouchListener(this);
        }
        return mLayout;
    }

    public void onDestroy() {
        super.onDestroy();
        mController.destroy();
    }


    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            int index = Arrays.asList(mButtonIds).indexOf(viewId);
            int powerSocket = mPowerSockets[index];
            mController.RebootPowerSocket(powerSocket);
            VibrateUtil.vibrate(getActivity(), 100);
        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        viewId = v.getId();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downTime = System.currentTimeMillis();
            mHandler.postDelayed(mRunnable, 5000);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            upTime = System.currentTimeMillis();

            if ((upTime - downTime) > 5000) {

            } else {
//                mHandler.removeCallbacks(mRunnable);
//                Toast toast = Toast.makeText(getActivity(), R.string.tv_reboot_prompt, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 410, -650);
//                toast.show();
            }
        }

        return false;
    }

}
