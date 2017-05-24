package com.imax.ipt.ui.activity.battery;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

import com.imax.ipt.R;
import com.imax.ipt.ui.activity.BaseActivity;

/**
 * \
 *
 * @author rlopez
 */
public class BatteryActivity extends BaseActivity {
    public static final String TAG = "BatteryActivity";

    private int mBatteryStatus;
    public static final String BATTERY_STATUS = "BATTERY_STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        addMenuFragment();

    }

    public static void fire(Context context, int mBatteryStatus) {
        Intent intent = new Intent(context, BatteryActivity.class);
        intent.putExtra(BatteryActivity.BATTERY_STATUS, mBatteryStatus);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBatteryStatus = getIntent().getIntExtra(BATTERY_STATUS, mBatteryStatus);
        switch (mBatteryStatus) {
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
            case BatteryManager.BATTERY_STATUS_UNKNOWN:

                break;

            default:
                Log.d(TAG, "Battery Status " + mBatteryStatus);
                break;
        }

    }

}
