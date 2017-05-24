package com.imax.ipt.ui.activity.media;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.MySwitch;
import com.imax.ipt.ui.util.MySwitch.OnSwitchListener;

public class SonyRemoteActivity extends MediaRemote {
    //   private ToggleButton btnSimpleorFull;
    private MySwitch btnSimpleorFull;
    private IPTTextView txtTitle;
    private String title;
    private String guid;
    private int inputId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sony_remote);
        this.addMenuFragment();
        this.init();
    }

    /**
     * @param context
     */
    public static void fire(Context context, String guid, String title, int inputId) {
        Intent intent = new Intent(context, SonyRemoteActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(SonyRemoteFullActivity.INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected void init() {
        this.mode = SIMPLE;
        this.initSimpleOrRemoteControl();
        this.title = getIntent().getStringExtra(MEDIA_TITLE);
        this.guid = getIntent().getStringExtra(MEDIA_ID);
        this.inputId = getIntent().getIntExtra(SonyRemoteFullActivity.INPUT_ID, 0);
        this.txtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        this.txtTitle.setText(getResources().getString(R.string.now_playing) + " " + title);

//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
        this.mSeekBarVolumen.setMax(Constants.MAX_VOLUME_VALUE);
        this.sendRequestGetVolume();

        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
        this.mBtnMute.setOnClickListener(this);
        this.sendRequestGetMuteVolume();

        this.mBtnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
//      this.mBtnPlayPause.setOnClickListener(this);
        this.mBtnPlayPause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Enter.toString()).getRequest());
            }
        });

        this.mBtnRew = (ImageButton) findViewById(R.id.btnRew);
        this.mBtnRew.setOnClickListener(this);

        this.mBtnFF = (ImageButton) findViewById(R.id.btnFF);
        this.mBtnFF.setOnClickListener(this);

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                MovieLibraryActivity.fireToFront(SonyRemoteActivity.this);
            }
        });

        Button zoomButton = (Button) findViewById(R.id.btnZoom);
        zoomButton.setOnClickListener(this);

        volupButton = (ImageButton) findViewById(R.id.volumeUp);
        voldownButton = (ImageButton) findViewById(R.id.volumeDown);
        volupButton.setOnClickListener(this);
        voldownButton.setOnClickListener(this);

    }

    //   /**
//    * 
//    */
    private void initSimpleOrRemoteControl() {
/*      btnSimpleorFull = (Switch) findViewById(R.id.btnSimpleFull);
      if (mode == SIMPLE) {
		btnSimpleorFull.setChecked(false);
      }
      btnSimpleorFull.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
        	 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
             prefs.edit().putBoolean(SonyRemoteFullActivity.PREFERENCE_DEFAULT_SONY_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();
            SonyRemoteFullActivity.fire(SonyRemoteActivity.this, guid, title,inputId);
         }
      });*/


        btnSimpleorFull = (MySwitch) findViewById(R.id.btnSimpleFull);
        btnSimpleorFull.setImageResource(R.drawable.knob_outline, R.drawable.knob_outline, R.drawable.knob);

        TextView btnSimple = (TextView) findViewById(R.id.btnSimple);
        TextView btnFull = (TextView) findViewById(R.id.btnFull);
        btnSimple.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                btnSimpleorFull.setSwitchState(false);

            }
        });
        btnFull.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                btnSimpleorFull.setSwitchState(true);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean(SonyRemoteFullActivity.PREFERENCE_DEFAULT_SONY_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();
                SonyRemoteFullActivity.fire(SonyRemoteActivity.this, guid, title, inputId);

            }
        });


        btnSimple.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSimpleorFull.setSwitchState(false);
                }
                return true;
            }
        });
        btnFull.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSimpleorFull.setSwitchState(true);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putBoolean(SonyRemoteFullActivity.PREFERENCE_DEFAULT_SONY_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();
                    SonyRemoteFullActivity.fire(SonyRemoteActivity.this, guid, title, inputId);
                }
                return true;
            }
        });


        if (mode == FULL) {
            btnSimpleorFull.setSwitchState(false);
        }
        btnSimpleorFull.setOnSwitchListener(new OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                // TODO Auto-generated method stub
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean(SonyRemoteFullActivity.PREFERENCE_DEFAULT_SONY_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();
                SonyRemoteFullActivity.fire(SonyRemoteActivity.this, guid, title, inputId);
            }
        });
    }

}
