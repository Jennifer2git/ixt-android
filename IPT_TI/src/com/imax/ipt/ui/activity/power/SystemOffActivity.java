package com.imax.ipt.ui.activity.power;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.common.PowerState;
import com.imax.ipt.controller.GlobalController;
import com.imax.ipt.controller.power.PowerController;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.util.DisplayUtil;
import com.imax.ipt.ui.util.VibrateUtil;
import com.imax.iptevent.EventBus;

public class SystemOffActivity extends BaseActivity {
    private static String POWER_STATE = "POWER_STATE";
    private ImageButton mBtnTurnOn;

    private EventBus mEventBus;

    private Button btnLightsOn;
    private Button btnLightsOff;
    private static boolean hasLighting;

    private PowerController mPowerController;
    private Boolean mLongClick = false;
    private long downTime = 0;
    private long upTime = 0;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_off);
        mPowerController = new PowerController(this);
//        mEventBus = IPT.getInstance().getEventBus();
//        mEventBus.register(this);

        this.init();


      /*
       * if (savedInstanceState != null) { powerState =
       * PowerState.getPowerState(savedInstanceState.getInt(POWER_STATE,
       * PowerState.Unknown.getValue())); }
       *
       * if (powerState == powerState.Off) { // display the PowerOn button
       * mBtnTurnOn.setVisibility(View.VISIBLE); } else { // retrieve the power
       * state
       *
       * }
       */
    }


    protected void init() {

        mBtnTurnOn = (ImageButton) findViewById(R.id.btn_turn_on);
        mBtnTurnOn.setOnTouchListener(btnPowerOnTouchListener);

        btnLightsOn = (Button) findViewById(R.id.btn_lights_on);
        btnLightsOff = (Button) findViewById(R.id.btn_lights_off);
        btnLightsOn.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_HAIRLINE_PATH));
        btnLightsOff.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_HAIRLINE_PATH));
        setLightings();
    }

    private void setLightings(){
        hasLighting = GlobalController.getInstance().getHasLighting();
        if (!hasLighting) {
            btnLightsOn.setVisibility(View.GONE);
            btnLightsOff.setVisibility(View.GONE);
            return;

        }else if(hasLighting){
            btnLightsOn.setVisibility(View.VISIBLE);
            btnLightsOff.setVisibility(View.VISIBLE);
        }
        btnLightsOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalController.getInstance().setLightsOn();
            }
        });
        btnLightsOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalController.getInstance().setLightsOff();
            }
        });
    }

    final Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            mPowerController.switchSystemPower(true);
            VibrateUtil.vibrate(SystemOffActivity.this, 100);
        }
    };

    OnTouchListener btnPowerOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                downTime = System.currentTimeMillis();
                mHandler.postDelayed(mRunnable, 3000);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                upTime = System.currentTimeMillis();

                if ((upTime - downTime) > 3000) {
//		            mEventBus.post(new SwitchSystemPowerHandler(false).getRequest()); //TODO we have to create a controller for this

                } else {
                    mHandler.removeCallbacks(mRunnable);
                    toastBtnPowerOn();
                }
            }
            return false;
        }

    };

    private void toastBtnPowerOn(){

        Toast toast = Toast.makeText(getApplicationContext(), R.string.toast_long_press_to_power_on, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, DisplayUtil.dip2px(getApplicationContext(), 90));//180px-90dp

        TextView textView = new TextView(getApplicationContext());
        textView.setBackgroundResource(R.drawable.toast_bg_long);
        textView.setTextColor(getResources().getColor(R.color.gray_light));
        textView.setTextSize(20);//2.22*28pt
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_LIGHT_PATH));
        textView.setText(R.string.toast_long_press_to_power_on);

        toast.setView(textView);
        toast.show();
    }

    /**
     * @param context
     */
    public static void fire(Context context) {
        Intent intent = new Intent(context, SystemOffActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    /**
     * @param context
     */
    public static void fire(Context context, PowerState powerState) {
        Intent intent = new Intent(context, SystemOffActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
         intent.putExtra(POWER_STATE, powerState.getValue());
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mEventBus.unregister(this);

    }

}
