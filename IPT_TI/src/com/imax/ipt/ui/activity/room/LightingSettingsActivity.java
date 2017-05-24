package com.imax.ipt.ui.activity.room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.room.LigthingController;
import com.imax.ipt.ui.activity.menu.MenuMasterFragment;
import com.imax.ipt.ui.activity.settings.preferences.PreferencesActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.TypeFaces;
import com.imax.ipt.ui.util.VibrateUtil;

/**
 * Created by yanli on 2015/10/19.
 */
public class LightingSettingsActivity extends LightingActivity  implements View.OnClickListener, View.OnTouchListener{
    private static final String TAG = LightingSettingsActivity.class.getSimpleName();
//    private LigthingController mLigthingController;

    private IPTTextView txtNumber;
    Typeface faceHair = TypeFaces.get(this, "fonts/Montserrat-Hairline.otf");
    Typeface faceReg = TypeFaces.get(this, "fonts/Montserrat-Regular.otf");
//    Typeface faceLight = TypeFaces.get(this, "fonts/Montserrat-Light.otf");


    private Button poweronButton;
    private Button poweroffButton;
    private Button modeWelcomeButton;
    private Button modeMovieButton;
    private Button modePauseButton;
    private Button modeGameButton;
    private Button modeKaraokeButton;
    private Button modeLightingButton;
    private Button colorSelectionButton;

    private Button saveButton;
    private Button lightResetButton;
    private Button topOnLightButton ;
    private Button topOffLightButton;
    private Button screenOnButton;
    private Button screenOffButton;
    private Button wallOnButton;
    private Button wallOffButton;
    private Button footOnButton;
    private Button footOffButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lighting_set);
        setContentView(R.layout.activity_lighting_set);
        Log.d(TAG, "onCreate");
        this.addMenuFragment();
        this.initLightingSet();
    }

    public void addMenuFragment() {
        MenuMasterFragment mMasterMenuFragment = new MenuMasterFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("menu_name",getResources().getString(R.string.menu_name_light));
        bundle.putString("menu_name","");
        mMasterMenuFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.masterMenuLayout, mMasterMenuFragment);
        transaction.commitAllowingStateLoss();
    }


    protected void initLightingSet() {
        mLigthingController = new LigthingController(this);
        ImageButton backImageButton = (ImageButton) findViewById(R.id.btnBack);
//        ImageButton powerImageButton = (ImageButton) findViewById(R.id.lighting_power);
        poweronButton = (Button) findViewById(R.id.lighting_power_on);
        poweroffButton = (Button) findViewById(R.id.lighting_power_off);
        poweronButton.setTypeface(faceHair);
        poweroffButton.setTypeface(faceHair);

        ImageButton lightnessUpImageButton = (ImageButton) findViewById(R.id.lighting_lightness_up);
        ImageButton lightnessDownImageButton = (ImageButton) findViewById(R.id.lighting_lightness_down);

//        ImageButton lightDelayTimeUp = (ImageButton) findViewById(R.id.btnDelayTimeUp);
//        ImageButton lightDelayTimeDown = (ImageButton) findViewById(R.id.btnDelayTimeDown);
//        txtNumber = (IPTTextView) findViewById(R.id.txtNumber);

//        Button topLightButton = (Button) findViewById(R.id.lighting_top);
//        Button screenButton = (Button) findViewById(R.id.lighting_screen);
//        Button wallButton = (Button) findViewById(R.id.lighting_wall);
//        Button footButton = (Button) findViewById(R.id.lighting_foot);

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


//        Button colorChange1Button = (Button) findViewById(R.id.lighting_color_change1);
        Button colorChange2Button = (Button) findViewById(R.id.lighting_color_change2);
        Button colorChange3Button = (Button) findViewById(R.id.lighting_color_change3);
//        Button colorChange4Button = (Button) findViewById(R.id.lighting_color_change4);
//
//        Button custom1Button = (Button) findViewById(R.id.lighting_custom1);
//        Button custom2Button = (Button) findViewById(R.id.lighting_custom2);
//        Button custom3Button = (Button) findViewById(R.id.lighting_custom3);

        saveButton = (Button) findViewById(R.id.lighting_save);
        saveButton.setTypeface(faceHair);
        saveButton.setOnClickListener(this);

        backImageButton.setOnClickListener(this);
//        powerImageButton.setOnClickListener(this);

        poweronButton.setOnTouchListener(this);
        poweroffButton.setOnTouchListener(this);
        poweronButton.setOnClickListener(this);
        poweroffButton.setOnClickListener(this);

        lightnessUpImageButton.setOnClickListener(this);
        lightnessDownImageButton.setOnClickListener(this);

//        lightDelayTimeUp.setOnClickListener(this);
//        lightDelayTimeDown.setOnClickListener(this);
//        txtNumber.setText("" + mLightDelayNumber);


//        topLightButton.setOnClickListener(this);
//        screenButton.setOnClickListener(this);
//        wallButton.setOnClickListener(this);
//        footButton.setOnClickListener(this);

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

//        colorChange1Button.setOnClickListener(this);
        colorChange2Button.setOnClickListener(this);
        colorChange3Button.setOnClickListener(this);
//        colorChange4Button.setOnClickListener(this);

//        custom1Button.setOnClickListener(this);
//        custom2Button.setOnClickListener(this);
//        custom3Button.setOnClickListener(this);



        /** begin add for light mode custom and seperate light control */
//        ImageButton lightResetImageButton = (ImageButton) findViewById(R.id.lighting_pref_reset);
        lightResetButton = (Button) findViewById(R.id.lighting_pref_reset);
        topOnLightButton = (Button) findViewById(R.id.lighting_top_on);
        topOffLightButton = (Button) findViewById(R.id.lighting_top_off);
        screenOnButton = (Button) findViewById(R.id.lighting_screen_on);
         screenOffButton = (Button) findViewById(R.id.lighting_screen_off);
         wallOnButton = (Button) findViewById(R.id.lighting_wall_on);
         wallOffButton = (Button) findViewById(R.id.lighting_wall_off);
         footOnButton = (Button) findViewById(R.id.lighting_foot_on);
         footOffButton = (Button) findViewById(R.id.lighting_foot_off);

        lightResetButton.setTypeface(faceHair);
        topOnLightButton.setTypeface(faceHair);
        topOffLightButton.setTypeface(faceHair);
        screenOnButton.setTypeface(faceHair);
        screenOffButton.setTypeface(faceHair);
        wallOnButton.setTypeface(faceHair);
        wallOffButton.setTypeface(faceHair);
        footOnButton.setTypeface(faceHair);
        footOffButton.setTypeface(faceHair);

        modeWelcomeButton = (Button) findViewById(R.id.lighting_mode_welcome);
        modeMovieButton = (Button) findViewById(R.id.lighting_mode_movie);
        modePauseButton = (Button) findViewById(R.id.lighting_mode_pause);
        modeGameButton = (Button) findViewById(R.id.lighting_mode_game);
        modeKaraokeButton = (Button) findViewById(R.id.lighting_mode_karaoke);
        modeLightingButton = (Button) findViewById(R.id.lighting_mode_lighting);
        colorSelectionButton = (Button) findViewById(R.id.lighting_color_selection);

        modeWelcomeButton.setTypeface(faceHair);
        modeMovieButton.setTypeface(faceHair);
        modePauseButton.setTypeface(faceHair);
        modeGameButton.setTypeface(faceHair);
        modeKaraokeButton.setTypeface(faceHair);
        modeLightingButton.setTypeface(faceHair);
        colorSelectionButton.setTypeface(faceHair);

        lightResetButton.setOnClickListener(this);
        topOnLightButton.setOnClickListener(this);
        topOffLightButton.setOnClickListener(this);
        screenOnButton.setOnClickListener(this);
        screenOffButton.setOnClickListener(this);
        wallOnButton.setOnClickListener(this);
        wallOffButton.setOnClickListener(this);
        footOnButton.setOnClickListener(this);
        footOffButton.setOnClickListener(this);

        lightResetButton.setOnTouchListener(this);
        topOnLightButton.setOnTouchListener(this);
        topOffLightButton.setOnTouchListener(this);
        screenOnButton.setOnTouchListener(this);
        screenOffButton.setOnTouchListener(this);
        wallOnButton.setOnTouchListener(this);
        wallOffButton.setOnTouchListener(this);
        footOnButton.setOnTouchListener(this);
        footOffButton.setOnTouchListener(this);

        modeWelcomeButton.setOnTouchListener(this);
        modeMovieButton.setOnTouchListener(this);
        modePauseButton.setOnTouchListener(this);
        modeGameButton.setOnTouchListener(this);
        modeKaraokeButton.setOnTouchListener(this);
        modeLightingButton.setOnTouchListener(this);
        colorSelectionButton.setOnTouchListener(this);

        modeWelcomeButton.setOnClickListener(this);
        modeMovieButton.setOnClickListener(this);
        modePauseButton.setOnClickListener(this);
        modeGameButton.setOnClickListener(this);
        modeKaraokeButton.setOnClickListener(this);
        modeLightingButton.setOnClickListener(this);
        colorSelectionButton.setOnClickListener(this);

        /** edd add for light mode custom and seperate light control */

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, LightingSettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    case R.id.lighting_save:
                        saveButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_pref_reset:
                        lightResetButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_power_on:
                        poweronButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_power_off:
                        poweroffButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_mode_welcome:
                        modeWelcomeButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_mode_movie:
                        modeMovieButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_mode_pause:
                        modePauseButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_mode_game:
                        modeGameButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_mode_karaoke:
                        modeKaraokeButton.setTypeface(faceReg);
                          break;
                    case R.id.lighting_mode_lighting:
                        modeLightingButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_color_selection:
                        colorSelectionButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_top_on:
                        topOnLightButton.setTypeface(faceReg);
                          break;
                    case R.id.lighting_top_off:
                        topOffLightButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_screen_on:
                        screenOnButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_screen_off:
                        screenOffButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_wall_on:
                        wallOnButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_wall_off:
                        wallOffButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_foot_on:
                        footOnButton.setTypeface(faceReg);
                        break;
                    case R.id.lighting_foot_off:
                        footOffButton.setTypeface(faceReg);
                        break;
                    default:
                        //do nothing
                }
                break;
            case MotionEvent.ACTION_UP:
                switch (v.getId()) {
                    case R.id.lighting_save:
                        saveButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_pref_reset:
                        lightResetButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_power_on:
                        poweronButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_power_off:
                        poweroffButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_mode_welcome:
                        modeWelcomeButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_mode_movie:
                        modeMovieButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_mode_pause:
                        modePauseButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_mode_game:
                        modeGameButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_mode_karaoke:
                        modeKaraokeButton.setTypeface(faceHair);

                        break;
                    case R.id.lighting_mode_lighting:
                        modeLightingButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_color_selection:
                        colorSelectionButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_top_on:
                        topOnLightButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_top_off:
                        topOffLightButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_screen_on:
                        screenOnButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_screen_off:
                        screenOffButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_wall_on:
                        wallOnButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_wall_off:
                        wallOffButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_foot_on:
                        footOnButton.setTypeface(faceHair);
                        break;
                    case R.id.lighting_foot_off:
                        footOffButton.setTypeface(faceHair);
                        break;
                    default:
                        //do nothing
                }
                break;
        }

        return false; // return  false to not despatch the onclick.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                PreferencesActivity.fire(LightingSettingsActivity.this);
//                MovieLibraryActivity.fireToFront(LightingSettingsActivity.this);
                break;
            case R.id.lighting_power:
//                mLigthingController.controlLighting(4);
                break;
            case R.id.lighting_power_on:
                mLigthingController.controlLighting(4);
                break;
            case R.id.lighting_power_off:
                mLigthingController.controlLighting(8);

                break;
            case R.id.lighting_lightness_up:
                mLigthingController.controlLighting(2);

                break;
            case R.id.lighting_lightness_down:
                mLigthingController.controlLighting(3);
                break;
            /** begin add mode control and seperate light control */
            case R.id.lighting_pref_reset:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_RESET);
                break;
            // save custom mode value
            case R.id.lighting_mode_welcome:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_SAVE_WELCOME);
                break;
            case R.id.lighting_mode_movie:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_SAVE_MOVIE);
                break;
            case R.id.lighting_mode_pause:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_SAVE_PAUSE);
                break;
            case R.id.lighting_mode_game:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_SAVE_GAME);
                break;
            case R.id.lighting_mode_karaoke:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_SAVE_KARAOKE);
                break;
            case R.id.lighting_mode_lighting:
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_SAVE_LIGHTING);
                break;
            case R.id.lighting_color_selection:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_COLOR_SELECTION);
                break;
            case R.id.lighting_top_on:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_ON_TOP);
                break;
            case R.id.lighting_top_off:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_OFF_TOP);
                break;
            case R.id.lighting_screen_on:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_ON_SCREEN);
                break;
            case R.id.lighting_screen_off:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_OFF_SCREEN);
                break;
            case R.id.lighting_wall_on:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_ON_WALL);
                break;
            case R.id.lighting_wall_off:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_OFF_WALL);
                break;
            case R.id.lighting_foot_on:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_ON_FOOT);
                break;
            case R.id.lighting_foot_off:
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_OFF_FOOT);
                break;
            /** end  add mode control and seperate light control */

        /**
            case R.id.lighting_top:
                mLigthingController.controlLighting(28);
                break;

            case R.id.lighting_screen:
                mLigthingController.controlLighting(27);
                break;
            case R.id.lighting_wall:
                mLigthingController.controlLighting(29);
                break;
            case R.id.lighting_foot:
                mLigthingController.controlLighting(30);
                break;
         */
            case R.id.lighting_color0:
                mLigthingController.controlLighting(1);

                break;

            case R.id.lighting_color1:
                mLigthingController.controlLighting(5);

                break;
            case R.id.lighting_color2:
                mLigthingController.controlLighting(6);

                break;
            case R.id.lighting_color3:
                mLigthingController.controlLighting(7);

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
                mLigthingController.controlLighting(13);

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
                mLigthingController.controlLighting(Constants.LIGHTING_CONTROL_FULL_COLOR);
                break;
            case R.id.lighting_color_change1:
                mLigthingController.controlLighting(8);

                break;
            case R.id.lighting_color_change2:
                mLigthingController.controlLighting(12);

                break;
            case R.id.lighting_color_change3:
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
                mLigthingController.controlLighting(Constants.LIGHTING_MODE_SAVE);

                break;
            default:

                break;
        }
        VibrateUtil.vibrate(this, 100);
    }

    protected class ButtonOnTouchListener implements View.OnTouchListener{
        public ButtonOnTouchListener() {
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.getId();
//                    (Button)v.setTypeface(faceReg);
                    break;
                case MotionEvent.ACTION_UP:
//                    poweronButton.setTypeface(faceHair);
                    break;
            }
            return false;

        }
    }


}
