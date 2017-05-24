package com.imax.ipt.ui.activity.settings.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.apk.update.util.RemoteUpdateUtil;
import com.imax.ipt.controller.eventbus.handler.rooms.SetLightingDelayTimeHandler;
import com.imax.ipt.ui.activity.room.LightingSettingsActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.widget.seekbar.TextMoveLayout;
import com.imax.iptevent.EventBus;

//public class GeneralFragment extends Fragment implements OnClickListener {
public class GeneralFragment extends Fragment implements OnClickListener {
    public static final String TAG = "GeneralFragment";
    private static final String PREFERENCE_DEFAULT_LIGHTING_DELAY_TIME = "default_lighting_delay_time";
    private static final int DEFAULT_LIGHTING_DELAY_TIME = 5;

    private View mGeneralLayout;
    private int mTouchPanelSleep = 1;
    private IPTTextView txtNumber;
    private ImageButton mBtnPrefUp;
    private ImageButton mBtnPrefDown;
    private Button btnCheckSoftwareUpdate;

    private ImageButton btnSetLighting;
    private IPTTextView txtLightingDelayTimeNumber;
//    private Button lightDelayTimeUp;
//    private Button lightDelayTimeDown;
    private int mLightDelayTime = 5;
    private EventBus mEventBus = new EventBus();

    private SeekBar mSleepTimeSeekBar;

    private String startTimeStr = "19:30:33";
    private String endTimeStr = "21:23:21";
    private TextView text, startTime, endTime;

    /**
     * 视频组中第一个和最后一个视频之间的总时长
     */
    private int totalSeconds = 0;

    /**
     * 屏幕宽度
     */
    private int screenWidth;

    /**
     * 自定义随着拖动条一起移动的空间
     */
    private TextMoveLayout textMoveLayout;

    private ViewGroup.LayoutParams layoutParams;

    private Context mContext ;
    /**
     * 托动条的移动步调
     */
    private float moveStep = 0;

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {

            mGeneralLayout = inflater.inflate(R.layout.fragment_pref_general_cn, null);
        } else {

            mGeneralLayout = inflater.inflate(R.layout.fragment_pref_general_en, null);
        }
        this.init(mGeneralLayout);
        return mGeneralLayout;
    }

    /**
     * @param view
     */
    private void init(View view) {
        txtNumber = (IPTTextView) view.findViewById(R.id.txtNumber);
        mBtnPrefUp = (ImageButton) view.findViewById(R.id.btnPrefUp);
        mBtnPrefUp.setOnClickListener(this);

        mBtnPrefDown = (ImageButton) view.findViewById(R.id.btnPrefDown);
        mBtnPrefDown.setOnClickListener(this);

        btnCheckSoftwareUpdate = (Button) view.findViewById(R.id.btnCheckSoftwareUpdate);
        btnCheckSoftwareUpdate.setOnClickListener(this);
        // begin add for set lighting jennifer 20151019
        btnSetLighting = (ImageButton) view.findViewById(R.id.btnSetLighting);
        btnSetLighting.setOnClickListener(this);
        ImageButton lightDelayTimeUp = (ImageButton) view.findViewById(R.id.btnDelayTimeUp);
        ImageButton lightDelayTimeDown = (ImageButton) view.findViewById(R.id.btnDelayTimeDown);
        txtLightingDelayTimeNumber = (IPTTextView) view.findViewById(R.id.txtDelayTimeNumber);
        lightDelayTimeUp.setOnClickListener(this);
        lightDelayTimeDown.setOnClickListener(this);

        mLightDelayTime = getDelayTimeFromPreference(getActivity());
        txtLightingDelayTimeNumber.setText("" + mLightDelayTime);

        mSleepTimeSeekBar = (SeekBar)view.findViewById(R.id.seekbar_pad_sleep_time);

        mContext = view.getContext();
//        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//        screenWidth = 1000;//getWindowManager().getDefaultDisplay().getWidth();
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.ll_seekbar);
        screenWidth = layout.getLayoutParams().width;
        Log.d(TAG,"CLL THE SCREEN WIDTH IS " +screenWidth );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        text = new TextView(mContext);
        text.setBackgroundColor(getResources().getColor(R.color.blue));
        text.setTextColor(getResources().getColor(R.color.gray_light));
        text.setTextSize(16);
        text.setSingleLine(true);
        text.setLayoutParams(params);
        layout.addView(text);
        mSleepTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int marginLeft = (int) (progress * screenWidth *0.75)/100;
                layoutParams.setMargins(marginLeft,0,0,0);
                Log.d(TAG,"CLL THE margin left IS " + marginLeft);
                text.setLayoutParams(layoutParams);
                text.setText(getCheckTimeBySeconds(progress, startTimeStr));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //todo:do send the value out
            }
        });
        setSleepTime();

        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);

        try {
            int milliseconds = Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            mTouchPanelSleep = (int) ((milliseconds / (1000 * 60)) % 60);
            txtNumber.setText("" + mTouchPanelSleep);
        } catch (SettingNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }

//        // why two btn?
//        MySwitch btnSimpleorFull1 = (MySwitch) view.findViewById(R.id.btnSimpleFull1);
//        btnSimpleorFull1.setImageResource(R.drawable.knob_outline, R.drawable.knob_outline, R.drawable.knob);
//
//
//        MySwitch btnSimpleorFull2 = (MySwitch) view.findViewById(R.id.btnSimpleFull2);
//        btnSimpleorFull2.setImageResource(R.drawable.knob_outline, R.drawable.knob_outline, R.drawable.knob);
//
//        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        boolean mode = sp.getBoolean("default_mode", true);
//        btnSimpleorFull2.setSwitchState(mode);
//        btnSimpleorFull2.setOnSwitchListener(new OnSwitchListener() {
//
//            @Override
//            public void onSwitched(boolean isSwitchOn) {
//                Editor editor = sp.edit();
//                editor.putBoolean("default_mode", isSwitchOn);
//                editor.apply();
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"cll onClick" );
        switch (v.getId()) {
            case R.id.btnPrefUp:
                if (mTouchPanelSleep < 15) {
                    mTouchPanelSleep++;
                    txtNumber.setText("" + mTouchPanelSleep);
                    updateSystemScreenOffTimeout();
                }
                break;

            case R.id.btnPrefDown:
                if (mTouchPanelSleep > 1) {
                    mTouchPanelSleep--;
                    txtNumber.setText("" + mTouchPanelSleep);
                    updateSystemScreenOffTimeout();
                }
                break;

            case R.id.btnCheckSoftwareUpdate:
                // Check if software update is available
                RemoteUpdateUtil remoteUpdateUtil = new RemoteUpdateUtil();
                remoteUpdateUtil.checkRemoteUpdate(getActivity(), true);
                break;
            case R.id.btnSetLighting:
//                //start lightingset activity
                LightingSettingsActivity.fire(getActivity());
                break;
            case R.id.btnDelayTimeUp:
                if (mLightDelayTime < 10) {
                    mLightDelayTime++;
                    txtLightingDelayTimeNumber.setText("" + mLightDelayTime);
                    setDelayTime2Preference(getActivity(), mLightDelayTime);
                    mEventBus.post(new SetLightingDelayTimeHandler(mLightDelayTime).getRequest());

                }
                break;

            case R.id.btnDelayTimeDown:
                if (mLightDelayTime > 0) {
                    mLightDelayTime--;
                    txtLightingDelayTimeNumber.setText("" + mLightDelayTime);
                    setDelayTime2Preference(getActivity(), mLightDelayTime);
                    mEventBus.post(new SetLightingDelayTimeHandler(mLightDelayTime).getRequest());
                }
                break;

        }

    }

    private void updateSystemScreenOffTimeout() {
        int value = mTouchPanelSleep * 60000;
        Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, value);
    }

    private void setDelayTime2Preference(Context context, int time){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        prefs.edit().putInt(PREFERENCE_DEFAULT_LIGHTING_DELAY_TIME, time).commit();
    }
    private int getDelayTimeFromPreference(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return prefs.getInt(PREFERENCE_DEFAULT_LIGHTING_DELAY_TIME,DEFAULT_LIGHTING_DELAY_TIME);
    }

     public void onEvent(final SetLightingDelayTimeHandler lightingDelayTimeHandler) {
        //do nothing
    }

    public void setSleepTime() {
//        startTime.setText(startTimeStr);
//        endTime.setText(endTimeStr);
        text.setText(startTimeStr);
        totalSeconds = totalSeconds(startTimeStr, endTimeStr);
        mSleepTimeSeekBar.setEnabled(true);
//        moveStep = (float) (((float) screenWidth / (float) totalSeconds) * 0.8);
        moveStep = (float) (((float) screenWidth / (float) totalSeconds) *1);

    }

    private static int totalSeconds(String startTime, String endTime) {

        String[] st = startTime.split(":");
        String[] et = endTime.split(":");

        int st_h = Integer.valueOf(st[0]);
        int st_m = Integer.valueOf(st[1]);
        int st_s = Integer.valueOf(st[2]);

        int et_h = Integer.valueOf(et[0]);
        int et_m = Integer.valueOf(et[1]);
        int et_s = Integer.valueOf(et[2]);

        int totalSeconds = (et_h - st_h) * 3600 + (et_m - st_m) * 60
                + (et_s - st_s);

        return totalSeconds;

    }

    private static String getCheckTimeBySeconds(int progress, String startTime) {

        String return_h = "", return_m = "", return_s = "";

        String[] st = startTime.split(":");

        int st_h = Integer.valueOf(st[0]);
        int st_m = Integer.valueOf(st[1]);
        int st_s = Integer.valueOf(st[2]);

        int h = progress / 3600;

        int m = (progress % 3600) / 60;

        int s = progress % 60;

        if ((s + st_s) >= 60) {

            int tmpSecond = (s + st_s) % 60;

            m = m + 1;

            if (tmpSecond >= 10) {
                return_s = tmpSecond + "";
            } else {
                return_s = "0" + (tmpSecond);
            }

        } else {
            if ((s + st_s) >= 10) {
                return_s = s + st_s + "";
            } else {
                return_s = "0" + (s + st_s);
            }

        }

        if ((m + st_m) >= 60) {

            int tmpMin = (m + st_m) % 60;

            h = h + 1;

            if (tmpMin >= 10) {
                return_m = tmpMin + "";
            } else {
                return_m = "0" + (tmpMin);
            }

        } else {
            if ((m + st_m) >= 10) {
                return_m = (m + st_m) + "";
            } else {
                return_m = "0" + (m + st_m);
            }

        }

        if ((st_h + h) < 10) {
            return_h = "0" + (st_h + h);
        } else {
            return_h = st_h + h + "";
        }

        return return_h + ":" + return_m + ":" + return_s;
    }


}
