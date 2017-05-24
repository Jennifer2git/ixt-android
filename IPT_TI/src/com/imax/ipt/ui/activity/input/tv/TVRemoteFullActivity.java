package com.imax.ipt.ui.activity.input.tv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ir.IRPulseEvent;
import com.imax.ipt.ui.layout.IPTTextView;

// It should not be correct for one Activity to inherit another activity
//    calling super.onCreate will causes the parent contentView to be set
public class TVRemoteFullActivity extends TVRemoteActivity {

//   private ToggleButton btnSimpleorFull;

    private int rew = 0;
    private int ff = 0;
    private boolean mute = false;
    private boolean play = true;

    //   protected ImageButton mBtnPlayPause;
    protected ImageButton mBtnStop;
    protected ImageButton mBtnPause;
    protected ImageButton mBtnPlay;
    protected ImageButton mBtnRew;
    protected ImageButton mBtnFF;
    protected Button mBtnGuide;
    protected Button mBtnInfo;
    protected Button mBtnRec;
    protected Button mBtnExit;
    protected Button mBtnMenu;

    protected ImageButton mBtnUp;
    protected ImageButton mBtnDown;
    protected ImageButton mBtnLeft;
    protected ImageButton mBtnRigth;
    protected TextView txtTvOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CLL", "CLL tvremotefullactivity ");

        setContentView(R.layout.activity_tv_remote_full);
        this.mode = FULL;
        this.addMenuFragment();
        this.init();

    }

    /**
     * @param context
     */
    public static void fire(Context context, int inputId, String title) {
        Intent intent = new Intent(context, TVRemoteFullActivity.class);
        intent.putExtra(INPUT_ID, inputId);
        intent.putExtra(TITLE, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected void init() {
        this.gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        this.setGridListener(this);

        this.mPrevCh = (Button) findViewById(R.id.btnPrevCh);
        this.mPrevCh.setOnClickListener(this);
//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);


        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
        this.mBtnMute.setOnClickListener(this);
        this.txtTitle = (IPTTextView) findViewById(R.id.txtTitle);

        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
        this.mTitle = getIntent().getStringExtra(TITLE);
        this.txtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle);
        this.initSimpleOrRemoteControl();

        this.mBtnAudioLanguage = (ImageButton) findViewById(R.id.btnAudioLanguage);
        this.mBtnAudioLanguage.setOnClickListener(this);

        this.mBtnSubtitles = (ImageButton) findViewById(R.id.btnSubtitles);
        this.mBtnSubtitles.setOnClickListener(this);

        this.sendRequestGetVolume();
        this.sendRequestGetMuteVolume();

        this.mBtnMultiview = (ImageButton) findViewById(R.id.btnMultiview);
        this.mBtnMultiview.setOnClickListener(this);

        mBtnVideoMode = (Button) findViewById(R.id.btnVideoMode);
        mBtnVideoMode.setOnClickListener(this);
      
/*      this.mBtnPlayPause = (ImageButton) findViewById(R.id.btnTvStopPlay);
      this.mBtnPlayPause.setOnClickListener(this);*/

        mBtnStop = (ImageButton) findViewById(R.id.btnTvStop);
        mBtnStop.setOnClickListener(this);

        mBtnPause = (ImageButton) findViewById(R.id.btnTvPause);
        mBtnPause.setOnClickListener(this);

        mBtnPlay = (ImageButton) findViewById(R.id.btnTvPlay);
        mBtnPlay.setOnClickListener(this);

        this.mBtnRew = (ImageButton) findViewById(R.id.btnTvRew);
        this.mBtnRew.setOnClickListener(this);

        this.mBtnGuide = (Button) findViewById(R.id.btnGuide);
        this.mBtnGuide.setOnClickListener(this);

        this.mBtnInfo = (Button) findViewById(R.id.btnInfo);
        this.mBtnInfo.setOnClickListener(this);

        this.mBtnRec = (Button) findViewById(R.id.btnRec);
        this.mBtnRec.setOnClickListener(this);

        this.mBtnExit = (Button) findViewById(R.id.btnExit);
        this.mBtnExit.setOnClickListener(this);

        mBtnMenu = (Button) findViewById(R.id.btnMenu);
        mBtnMenu.setOnClickListener(this);

        this.txtTvOk = (TextView) findViewById(R.id.txtTvOk);
        this.txtTvOk.setOnClickListener(this);

        this.mBtnUp = (ImageButton) findViewById(R.id.btnUp);
        this.mBtnUp.setOnClickListener(this);

        this.mBtnDown = (ImageButton) findViewById(R.id.btnDown);
        this.mBtnDown.setOnClickListener(this);

        this.mBtnRigth = (ImageButton) findViewById(R.id.btnRigth);
        this.mBtnRigth.setOnClickListener(this);

        this.mBtnLeft = (ImageButton) findViewById(R.id.btnLeft);
        this.mBtnLeft.setOnClickListener(this);

        this.mBtnTVChDown = (ImageButton) findViewById(R.id.btnTvChDown);
        this.mBtnTVChDown.setOnClickListener(this);

        this.mBtnTVChUp = (ImageButton) findViewById(R.id.btnTvChUp);
        this.mBtnTVChUp.setOnClickListener(this);

        this.mBtnTvFav = (ImageButton) findViewById(R.id.btnTvFav);
        this.mBtnTvFav.setOnClickListener(this);

        this.setListenerFunctionButtons();

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
//            TVRemoteActivity.fire(TVRemoteFullActivity.this, inputId, mTitle);
            }
        });

        btnSimpleorFull = (Switch) findViewById(R.id.btnSimpleFull);
        btnSimpleorFull.setChecked(true);
        this.initSimpleOrRemoteControl();
    }

    private void setListenerFunctionButtons() {
        LinearLayout linearButtons = (LinearLayout) findViewById(R.id.layoutButtons);
        for (int i = 0; i < linearButtons.getChildCount(); i++) {
            View view = linearButtons.getChildAt(i);
            view.setOnClickListener(this);
        }
    }

    /**
     * PlayPause = 0, Stop = 1, Pause = 2, Next = 3, Previous = 4, FastForward =
     * 5, Rewind = 6,
     * <p/>
     * RootMenu = 7, PopupMenu = 8,
     * <p/>
     * Audio = 9, Subtitles = 10, Enter = 11,
     * <p/>
     * DirectionUp = 12, DirectionDown = 13, DirectionLeft = 14, DirectionRight =
     * 15,
     * <p/>
     * // system volume VolumeUp = 16, VolumeDown = 17, VolumeMute = 18,
     * <p/>
     * // custom inputs (IR function support depends on device type and
     * manufacture IR signal implementation) Rec = 19, Power = 20, Numeric0 = 21,
     * Numeric1 = 22, Numeric2 = 23, Numeric3 = 24, Numeric4 = 25, Numeric5 = 26,
     * Numeric6 = 27, Numeric7 = 28, Numeric8 = 29, Numeric9 = 30, ChannelUp =
     * 31, ChannelDown = 32, Cancel = 33, Exit = 34, Info = 35, TvVideoInput =
     * 36, Guide = 37, Prev = 38 (or Back)
     */

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btnTvPrev:
                mEventBus.post(new IRPulseEvent(inputId, 4).getRequest());
                break;
            case R.id.btnTvRew:
                mEventBus.post(new IRPulseEvent(inputId, 6).getRequest());
                break;

            case R.id.btnTvPlay:
                mEventBus.post(new IRPulseEvent(inputId, 0).getRequest());
                break;
            case R.id.btnTvStop:
                mEventBus.post(new IRPulseEvent(inputId, 1).getRequest());
                break;
         
/*      case R.id.btnTvStopPlay:
         if (play)
         {
            mEventBus.post(new IRPulseEvent(inputId, 1).getRequest());
            play = false;
            mBtnPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_play_button));
            mBtnRew.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_rew_menu_button));
         }
         else
         {
            mEventBus.post(new IRPulseEvent(inputId, 0).getRequest());
            play = true;
            mBtnPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_pause_button));
         }
         break;*/

            case R.id.btnTvPause:
                mEventBus.post(new IRPulseEvent(inputId, 2).getRequest());
                break;
            case R.id.btnTvFF:
                mEventBus.post(new IRPulseEvent(inputId, 5).getRequest());
                break;
            case R.id.btnTvNext:
                mEventBus.post(new IRPulseEvent(inputId, 3).getRequest());
                break;
            case R.id.btnGuide:
                mEventBus.post(new IRPulseEvent(inputId, 37).getRequest());
                break;
            case R.id.btnInfo:
                mEventBus.post(new IRPulseEvent(inputId, 35).getRequest());
                break;
            case R.id.btnRec:
                mEventBus.post(new IRPulseEvent(inputId, 19).getRequest());
                break;
            case R.id.btnExit:
                mEventBus.post(new IRPulseEvent(inputId, 34).getRequest());
                break;
            case R.id.txtTvOk:
                mEventBus.post(new IRPulseEvent(inputId, 11).getRequest());
                break;
            case R.id.btnUp:
                mEventBus.post(new IRPulseEvent(inputId, 12).getRequest());
                break;
            case R.id.btnDown:
                mEventBus.post(new IRPulseEvent(inputId, 13).getRequest());
                break;
            case R.id.btnLeft:
                mEventBus.post(new IRPulseEvent(inputId, 14).getRequest());
                break;
            case R.id.btnRigth:
                mEventBus.post(new IRPulseEvent(inputId, 15).getRequest());
                break;
        }
    }

    /**
     *
     */
    private void initSimpleOrRemoteControl() {
        btnSimpleorFull.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean(PREFERENCE_DEFAULT_CUSTOM_INPUT_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();

                TVRemoteActivity.fire(TVRemoteFullActivity.this, inputId, mTitle);
            }
        });
    }
}
