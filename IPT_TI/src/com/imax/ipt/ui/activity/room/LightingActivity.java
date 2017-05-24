package com.imax.ipt.ui.activity.room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.room.LigthingController;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.util.VibrateUtil;

/**
 * @author Rodrigo Lopez
 */
public class LightingActivity extends BaseActivity implements OnClickListener, OnTouchListener {
    private static final String TAG = "LightingActivity";
    private static final String PREFERENCE_DEFAULT_LIGHTING_DELAY_TIME = "default_lighting_delay_time";

    public LigthingController mLigthingController;

    private Typeface mFaceHair;
    private Typeface mFaceReg;

    private Button btnPowerOn;
    private Button btnPowerOff;
    private Button lightingButton;
    private Button movieButton;
    private Button gameButton;
    private Button karaokeButton;
    private Button pauseButton;
    private Button welcomeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_lighting);
        this.addMenuFragment();
        this.init();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLigthingController.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLigthingController.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLigthingController.onDestroy();
    }

    protected void init() {
        mLigthingController = new LigthingController(this);
        mFaceHair = Typeface.createFromAsset(getAssets(),Constants.FONT_HAIRLINE_PATH);
        mFaceReg = Typeface.createFromAsset(getAssets(),Constants.FONT_REGULAR_PATH);

        ImageButton backImageButton = (ImageButton) findViewById(R.id.btnBack);
        ImageButton powerImageButton = (ImageButton) findViewById(R.id.lighting_power);
        btnPowerOn = (Button) findViewById(R.id.lighting_power_on);
        btnPowerOff = (Button) findViewById(R.id.lighting_power_off);
        btnPowerOn.setTypeface(mFaceHair);
        btnPowerOff.setTypeface(mFaceHair);

        ImageButton lightnessUpImageButton = (ImageButton) findViewById(R.id.lighting_lightness_up);
        ImageButton lightnessDownImageButton = (ImageButton) findViewById(R.id.lighting_lightness_down);

        /** remove set lighting delay time
        ImageButton lightDelayTimeUp = (ImageButton) findViewById(R.id.btnDelayTimeUp);
        ImageButton lightDelayTimeDown = (ImageButton) findViewById(R.id.btnDelayTimeDown);
        txtNumber = (IPTTextView) findViewById(R.id.txtNumber);
        **/

        /** hide function light button
        Button topLightButton = (Button) findViewById(R.id.lighting_top);
        Button screenButton = (Button) findViewById(R.id.lighting_screen);
        Button wallButton = (Button) findViewById(R.id.lighting_wall);
        Button footButton = (Button) findViewById(R.id.lighting_foot);
        **/
        lightingButton = (Button) findViewById(R.id.lighting_mode_lighting);
        movieButton = (Button) findViewById(R.id.lighting_mode_movie);
        gameButton = (Button) findViewById(R.id.lighting_mode_game);
        karaokeButton = (Button) findViewById(R.id.lighting_mode_karaoke);
        pauseButton = (Button) findViewById(R.id.lighting_mode_pause);
        welcomeButton = (Button) findViewById(R.id.lighting_mode_welcome);

        lightingButton.setTypeface(mFaceHair);
        movieButton.setTypeface(mFaceHair);
        gameButton.setTypeface(mFaceHair);
        karaokeButton.setTypeface(mFaceHair);
        pauseButton.setTypeface(mFaceHair);
        welcomeButton.setTypeface(mFaceHair);

        lightingButton.setOnTouchListener(this);
        movieButton.setOnTouchListener(this);
        gameButton.setOnTouchListener(this);
        karaokeButton.setOnTouchListener(this);
        pauseButton.setOnTouchListener(this);
        welcomeButton.setOnTouchListener(this);

        ImageButton color0Button = (ImageButton) findViewById(R.id.lighting_color0);
        ImageButton color1Button = (ImageButton) findViewById(R.id.lighting_color1);
        ImageButton color2Button = (ImageButton) findViewById(R.id.lighting_color2);
        ImageButton color3Button = (ImageButton) findViewById(R.id.lighting_color3);
        ImageButton color4Button = (ImageButton) findViewById(R.id.lighting_color4);
        ImageButton color5Button = (ImageButton) findViewById(R.id.lighting_color5);
        ImageButton color6Button = (ImageButton) findViewById(R.id.lighting_color6);
        ImageButton color7Button = (ImageButton) findViewById(R.id.lighting_color7);
        ImageButton color8Button = (ImageButton) findViewById(R.id.lighting_color8);
        ImageButton color9Button = (ImageButton) findViewById(R.id.lighting_color9);
        ImageButton color10Button = (ImageButton) findViewById(R.id.lighting_color10);
        ImageButton color11Button = (ImageButton) findViewById(R.id.lighting_color11);
        ImageButton color12Button = (ImageButton) findViewById(R.id.lighting_color12);
        ImageButton color13Button = (ImageButton) findViewById(R.id.lighting_color13);

        /** remove custom line: color change save user select
        Button colorChange1Button = (Button) findViewById(R.id.lighting_color_change1);
        Button colorChange2Button = (Button) findViewById(R.id.lighting_color_change2);
        Button colorChange3Button = (Button) findViewById(R.id.lighting_color_change3);
        Button colorChange4Button = (Button) findViewById(R.id.lighting_color_change4);

        Button custom1Button = (Button) findViewById(R.id.lighting_custom1);
        Button custom2Button = (Button) findViewById(R.id.lighting_custom2);
        Button custom3Button = (Button) findViewById(R.id.lighting_custom3);

        Button saveButton = (Button) findViewById(R.id.lighting_save);
 **/

        backImageButton.setOnClickListener(this);
        powerImageButton.setOnClickListener(this);
        btnPowerOn.setOnClickListener(this);
        btnPowerOff.setOnClickListener(this);
        btnPowerOn.setOnTouchListener(this);
        btnPowerOff.setOnTouchListener(this);

        lightnessUpImageButton.setOnClickListener(this);
        lightnessDownImageButton.setOnClickListener(this);

        /** remove set lighting delay time
        lightDelayTimeUp.setOnClickListener(this);
        lightDelayTimeDown.setOnClickListener(this);
        mLightDelayTime = getDelayTimeFromPreference();
        txtNumber.setText("" + mLightDelayTime);
        **/

        /** hide function light button
        topLightButton.setOnClickListener(this);
        screenButton.setOnClickListener(this);
        wallButton.setOnClickListener(this);
        footButton.setOnClickListener(this);
        **/
        lightingButton.setOnClickListener(this);
        movieButton.setOnClickListener(this);
        gameButton.setOnClickListener(this);
        karaokeButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        welcomeButton.setOnClickListener(this);

        color0Button.setOnClickListener(this);
        color1Button.setOnClickListener(this);
        color2Button.setOnClickListener(this);
        color3Button.setOnClickListener(this);
        color4Button.setOnClickListener(this);
        color5Button.setOnClickListener(this);
        color6Button.setOnClickListener(this);
        color7Button.setOnClickListener(this);
        color8Button.setOnClickListener(this);
        color9Button.setOnClickListener(this);
        color10Button.setOnClickListener(this);
        color11Button.setOnClickListener(this);
        color12Button.setOnClickListener(this);
        color13Button.setOnClickListener(this);

        /** remove custom line: color change save user select
        colorChange1Button.setOnClickListener(this);
        colorChange2Button.setOnClickListener(this);
        colorChange3Button.setOnClickListener(this);
        colorChange4Button.setOnClickListener(this);

        custom1Button.setOnClickListener(this);
        custom2Button.setOnClickListener(this);
        custom3Button.setOnClickListener(this);
        saveButton.setOnClickListener(this);
         **/
    }


    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, LightingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     *
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                MovieLibraryActivity.fireToFront(LightingActivity.this);
                break;
            case R.id.lighting_power:
                mLigthingController.controlLighting(4);
                break;
            case R.id.lighting_power_on:
                mLigthingController.controlLighting(4);
                break;
            case R.id.lighting_power_off:
                mLigthingController.controlLighting(8);
                break;
            case R.id.lighting_lightness_up:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_LIGHTNESS_UP);

                break;
            case R.id.lighting_lightness_down:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_LIGHTNESS_DOWN);

                break;
            /** remove set lighting delay time
            case R.id.btnDelayTimeUp:
                if (mLightDelayTime < 10) {
                    mLightDelayTime++;
                    txtNumber.setText("" + mLightDelayTime);
                    setDelayTime2Preference(mLightDelayTime);
                    mLigthingController.setLightingDelayTime(mLightDelayTime);

                }
                break;

            case R.id.btnDelayTimeDown:
                if (mLightDelayTime > 0) {
                    mLightDelayTime--;
                    txtNumber.setText("" + mLightDelayTime);
                    setDelayTime2Preference(mLightDelayTime);
                    mLigthingController.setLightingDelayTime(mLightDelayTime);
                }
                break;
             **/

            /** hide function light button
            case R.id.lighting_top:
    	        mLigthingController.controlLighting(28);
                break;
            case R.id.lighting_screen:
    	        mLigthingController.controlLighting(27);//screen
                break;
            case R.id.lighting_wall:
    	        mLigthingController.controlLighting(29);//wall
                break;
            case R.id.lighting_foot:
    	        mLigthingController.controlLighting(30);// foot
                break;
              hide function light button **/
            case R.id.lighting_mode_welcome:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_WELCOME);
                break;

            case R.id.lighting_mode_movie:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_MOVIE);
                break;
            case R.id.lighting_mode_pause:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_PAUSE);
                break;
            case R.id.lighting_mode_game:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_GAME);
                break;
            case R.id.lighting_mode_karaoke:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_KARAOKE);
                break;

            case R.id.lighting_mode_lighting:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_LIGHTING);
                break;
            case R.id.lighting_color0:
                mLigthingController.controlLighting(1);//white

                break;
            case R.id.lighting_color1:
                mLigthingController.controlLighting(5);//red

                break;
            case R.id.lighting_color2:
                mLigthingController.controlLighting(6);//green

                break;
            case R.id.lighting_color3:
                mLigthingController.controlLighting(7);//BLUE

                break;
            case R.id.lighting_color4:
                mLigthingController.controlLighting(9);

                break;
            case R.id.lighting_color5:
                mLigthingController.controlLighting(10);

                break;
            case R.id.lighting_color6:
                mLigthingController.controlLighting(11);

                break;
            case R.id.lighting_color7:
                mLigthingController.controlLighting(13);//YELLOW

                break;
            case R.id.lighting_color8:
                mLigthingController.controlLighting(14);

                break;
            case R.id.lighting_color9:
                mLigthingController.controlLighting(15);

                break;
            case R.id.lighting_color10:
                mLigthingController.controlLighting(17);

                break;
            case R.id.lighting_color11:
                mLigthingController.controlLighting(18);

                break;
            case R.id.lighting_color12:
                mLigthingController.controlLighting(19);

                break;
            case R.id.lighting_color13:
                mLigthingController.controlLighting(16);//full color change

                break;
            case R.id.lighting_color_change1://color change
                mLigthingController.controlLighting(8);

                break;
            case R.id.lighting_color_change2://warm color change
                mLigthingController.controlLighting(12);

                break;
            case R.id.lighting_color_change3:// full color change
                mLigthingController.controlLighting(16);

                break;
            case R.id.lighting_color_change4:
                mLigthingController.controlLighting(20);

                break;
            case R.id.lighting_custom1:
                mLigthingController.controlLighting(21);

                break;
            case R.id.lighting_custom2:
                mLigthingController.controlLighting(22);

                break;
            case R.id.lighting_custom3:
                mLigthingController.controlLighting(23);

                break;
            case R.id.lighting_save:
                mLigthingController.controlLighting(24);

                break;
            default:

                break;
        }
        VibrateUtil.vibrate(this, 100);
    }


    /**
     *
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()){
                    case R.id.lighting_power_on:
                        btnPowerOn.setTypeface(mFaceReg);
                        break;

                    case R.id.lighting_power_off:
                        btnPowerOff.setTypeface(mFaceReg);

                        break;
                    case R.id.lighting_mode_movie:
                        movieButton.setTypeface(mFaceReg);
                        break;
                    case R.id.lighting_mode_game:
                        gameButton.setTypeface(mFaceReg);
                        break;
                    case R.id.lighting_mode_karaoke:
                        karaokeButton.setTypeface(mFaceReg);
                        break;
                    case R.id.lighting_mode_pause:
                        pauseButton.setTypeface(mFaceReg);
                        break;

                    case R.id.lighting_mode_welcome:
                        welcomeButton.setTypeface(mFaceReg);
                        break;
                }

//         mBtnLight.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_light_lev_indicator_high_active));
                break;
            case MotionEvent.ACTION_UP:
                switch (v.getId()){
                    case R.id.lighting_power_on:
                        btnPowerOn.setTypeface(mFaceHair);
                        break;

                    case R.id.lighting_power_off:
                        btnPowerOff.setTypeface(mFaceHair);

                        break;
                    case R.id.lighting_mode_movie:
                        movieButton.setTypeface(mFaceHair);
                        break;
                    case R.id.lighting_mode_game:
                        gameButton.setTypeface(mFaceHair);
                        break;
                    case R.id.lighting_mode_karaoke:
                        karaokeButton.setTypeface(mFaceHair);
                        break;
                    case R.id.lighting_mode_pause:
                        pauseButton.setTypeface(mFaceHair);
                        break;

                    case R.id.lighting_mode_welcome:
                        welcomeButton.setTypeface(mFaceHair);
                        break;
                }


//         mBtnLight.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_light_lev_indicator_high_inactive));
                break;
        }
        return false;
    }

    private void setDelayTime2Preference(int time){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().putInt(PREFERENCE_DEFAULT_LIGHTING_DELAY_TIME, time).commit();
    }
    private int getDelayTimeFromPreference(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getInt(PREFERENCE_DEFAULT_LIGHTING_DELAY_TIME,5);
    }
}
