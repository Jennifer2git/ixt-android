package com.imax.ipt.ui.activity.room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.push.FanStateChangedEvent.FanState;
import com.imax.ipt.controller.eventbus.handler.rooms.climate.SetFanStateHandler;
import com.imax.ipt.controller.room.ClimateController;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.VibrateUtil;

public class ClimateActivity extends BaseActivity implements OnClickListener {
    //   private ImageView mImgFan;
    private ImageButton mBtnUp;
    private ImageButton mBtnDown;
    private IPTTextView mTxtSetPoint;
    private IPTTextView mTxtActualTemperature;
    private ClimateController mClimateController;
    //   private ToggleButton btnSimpleorFull;
    private String mUnit;

    private IPTTextView btnFanState;
    private IPTTextView txtFaultDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_climate);
        this.init();
    }

    /**
     *
     */
    private void init() {
        this.addMenuFragment();
        this.mTxtSetPoint = (IPTTextView) findViewById(R.id.txtSetPoint);
        this.mBtnUp = (ImageButton) findViewById(R.id.btnUp);
        this.mBtnUp.setOnClickListener(this);
        this.mBtnDown = (ImageButton) findViewById(R.id.btnDown);
        this.mBtnDown.setOnClickListener(this);
        this.mTxtActualTemperature = (IPTTextView) findViewById(R.id.txtActualTemerature);
//      this.mImgFan = (ImageView) findViewById(R.id.imgFan);
        this.mClimateController = new ClimateController(this);

        btnFanState = (IPTTextView) findViewById(R.id.btnFanState);
        btnFanState.setOnClickListener(this);

        txtFaultDescription = (IPTTextView) findViewById(R.id.txtFaultDescription);

/*      btnSimpleorFull = (ToggleButton) findViewById(R.id.btnFanOnOff);
      btnSimpleorFull.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            if (isChecked)
            {
               mClimateController.setFanStatus(SetFanStateHandler.FAN_STATE_ON);
               mImgFan.setVisibility(View.VISIBLE);
            }
            else
            {
               mClimateController.setFanStatus(SetFanStateHandler.FAN_STATE_AUTO);
               mImgFan.setVisibility(View.INVISIBLE);
            }
         }
      });*/
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, ClimateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * @param mUnit
     * @param mValueSetPoint
     * @param mCurrentTemperature
     * @param mFanState
     * @param mIsFault
     */
    public void updateHvacState(final int mUnit, final int mValueSetPoint, final int mCurrentTemperature, final FanState mFanState, final boolean mIsFault, final String faultDescription) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ClimateActivity.this.mUnit = mUnit == ClimateController.FANHRENHEIT ? getResources().getString(R.string.fanhhrenheit) : getResources().getString(R.string.celsius);
                mTxtActualTemperature.setText(Integer.toString(mCurrentTemperature) + ClimateActivity.this.mUnit);
                mTxtSetPoint.setText(Integer.toString(mValueSetPoint) + ClimateActivity.this.mUnit);

                updateFanState(mFanState);
                updateFaultDescription(mIsFault, faultDescription);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUp:
                mClimateController.adjustCurrentSetpoint(true);
                break;
            case R.id.btnDown:
                mClimateController.adjustCurrentSetpoint(false);
                break;

            case R.id.btnFanState:
                mClimateController.switchFanState();
                break;
        }
        VibrateUtil.vibrate(this, 100);
    }

    /**
     * @param setPoint
     */
    public void updateSetPoint(final int setPoint, final int mUnit) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ClimateActivity.this.mUnit = mUnit == ClimateController.FANHRENHEIT ? getResources().getString(R.string.fanhhrenheit) : getResources().getString(R.string.celsius);

                mTxtSetPoint.setText(Integer.toString(setPoint) + ClimateActivity.this.mUnit);
            }
        });
    }

    /**
     * @param setPoint
     */
    public void updateTemperature(final int currentTemperature, final int mUnit) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ClimateActivity.this.mUnit = mUnit == ClimateController.FANHRENHEIT ? getResources().getString(R.string.fanhhrenheit) : getResources().getString(R.string.celsius);

                mTxtActualTemperature.setText(Integer.toString(currentTemperature) + ClimateActivity.this.mUnit);
            }
        });
    }

    public void updateFanState(final FanState fanState) {
        if (fanState == null) {
            return;
        }
        switch (fanState) {
            case On:
                btnFanState.setText(R.string.climate_fan_on);
                break;
            case Auto:
                btnFanState.setText(R.string.climate_fan_auto);
                break;
        }
    }

    public void updateFaultDescription(boolean isFault, String faultDescription) {
        if (isFault) {
            txtFaultDescription.setVisibility(View.VISIBLE);
            txtFaultDescription.setText(faultDescription);
        } else {
            txtFaultDescription.setVisibility(View.INVISIBLE);
        }
    }
}
