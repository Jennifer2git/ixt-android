package com.imax.ipt.ui.activity.power;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.apk.update.util.RemoteUpdateUtil;
import com.imax.ipt.common.Constants;
import com.imax.ipt.common.PowerState;
import com.imax.ipt.controller.power.SystemCountUpDownController;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.animation.AnimationHelper;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.widget.circularseekbar.CircularSeekBar;
import com.imax.ipt.util.TimeUtil;

public class SystemCountUpDownActivity extends BaseActivity {

    public static final String POWER_STATUS = "POWER_STATUS";
    private static final String TAG = SystemCountUpDownActivity.class.getSimpleName() ;

    private CircularSeekBar mCircularSeekbar;
    private IPTTextView mIPTTextViewCount;
    private TextView mIPTTextViewMessage;
    private SystemCountUpDownController mSystemCountUpDownController;
    private ImageButton mBtnCancel;
    private PowerState mPowerStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_count_down);
        Log.d(TAG, "onCreate");

//        mIPTTextViewMessage = (IPTTextView) findViewById(R.id.txtCountTimeMsg);
        mIPTTextViewMessage = (TextView) findViewById(R.id.txtPowerMsg);
        Typeface customFont = Typeface.createFromAsset(getResources().getAssets(), Constants.FONT_LIGHT_PATH);
        mIPTTextViewMessage.setTypeface(customFont);
        mIPTTextViewMessage.setTextSize(20);

        this.mPowerStatus = PowerState.getPowerState(getIntent().getIntExtra(POWER_STATUS, PowerState.PoweringOn.getValue()));
        switch (mPowerStatus) {
            case PoweringOn:
//                mIPTTextViewMessage.setText(getResources().getString(R.string.count_down_power_on));
                mIPTTextViewMessage.setText(getResources().getString(R.string.power_on_msg));
                RemoteUpdateUtil remoteUpdateUtil = new RemoteUpdateUtil();
                remoteUpdateUtil.checkRemoteUpdate(this, false);
                break;
            case PoweringOff:
//                mIPTTextViewMessage.setText(getResources().getString(R.string.count_down_power_off));
                mIPTTextViewMessage.setText(getResources().getString(R.string.power_off_msg));
                // Check if software update is available move to power off.
//                remoteUpdateUtil.checkRemoteUpdate(this, false);
                break;
        }

        this.init();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // closes all dialogs on screen dismiss
        DialogFragment dialogFragment = (DialogFragment) getFragmentManager().findFragmentByTag(RemoteUpdateUtil.TAG_DIALOG_FRAGMENT_SOFTWARE_UPDATE_DIALOG_FRAGMENT);
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSystemCountUpDownController.onDestroy();
    }

    private void init() {
        mBtnCancel = (ImageButton) findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSystemCountUpDownController.cancel();
            }
        });
        mSystemCountUpDownController = new SystemCountUpDownController(this);
        mCircularSeekbar = (CircularSeekBar) findViewById(R.id.progressSeekBar);
        mIPTTextViewCount = (IPTTextView) findViewById(R.id.txtCountTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSystemCountUpDownController.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        this.mPowerStatus = PowerState.getPowerState(intent.getIntExtra(POWER_STATUS, PowerState.PoweringOn.getValue()));
        switch (mPowerStatus) {
            case PoweringOn:
                mIPTTextViewMessage.setText(getResources().getString(R.string.count_down_power_on));
                // Check if software update is available
//                RemoteUpdateUtil remoteUpdateUtil = new RemoteUpdateUtil();
//                remoteUpdateUtil.checkRemoteUpdate(this, false);
                break;
            case PoweringOff:
                mIPTTextViewMessage.setText(getResources().getString(R.string.count_down_power_off));
                break;
        }

//      mIPTTextViewCount.setText(TimeUtil.getDurationString(0));
//      mCircularSeekbar.setProgress(0);
//      mCircularSeekbar.invalidate();
    }

    public void updatePcabCalibration(int progress) {
//        Log.d("water", System.currentTimeMillis() + ",progress=" + progress);
        mIPTTextViewMessage.setText(getResources().getString(R.string.pcab_calibrating_prompt) + progress + "%");
    }


    /**
     * @param estimatedTime
     * @param progress
     */
    public void updateProgress(final int estimatedTime, final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float newProgress = progress / 100f;
                mIPTTextViewCount.setText(TimeUtil.getDurationString(estimatedTime));
                AnimationHelper.animate(mCircularSeekbar, newProgress, null);
            }
        });
    }

    /**
     * @param context
     */
    public static void fire(Context context, PowerState powerStatus) {
        Intent intent = new Intent(context, SystemCountUpDownActivity.class);
        intent.putExtra(POWER_STATUS, powerStatus.getValue());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // NEW_TASK is it only needed when it is called from a Service?
//      intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

}
