package com.imax.ipt.ui.activity.input.gaming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.layout.IPTTextView;

public class GamingRemoteActivity extends MediaRemote implements OnClickListener {
    public static final String INPUT_ID = "INPUT_ID";

    private IPTTextView mTxtTitle;
    private String mTitle;
    private String guid;
//   private ImageButton mBtnMultiview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_remote_full);
        this.addMenuFragment();
        this.init();
    }

    protected void init() {
        this.mode = FULL;

        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
        this.mTitle = getIntent().getStringExtra(MEDIA_TITLE);
        this.guid = getIntent().getStringExtra(MEDIA_ID);
        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        this.mTxtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle);

//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
//        this.mSeekBarVolumen.setMax(100);
        this.mSeekBarVolumen.setMax(Constants.MAX_VOLUME_VALUE);
        this.sendRequestGetVolume();

        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
        this.mBtnMute.setOnClickListener(this);
        this.sendRequestGetMuteVolume();

        this.mBtnAudioLanguage = (ImageButton) findViewById(R.id.btnAudioLanguage);
        this.mBtnAudioLanguage.setOnClickListener(this);

        this.mBtnSubtitles = (ImageButton) findViewById(R.id.btnSubtitles);
        this.mBtnSubtitles.setOnClickListener(this);

        this.mBtnPopUpMenu = (ImageButton) findViewById(R.id.btnPopUpMenu);
        this.mBtnPopUpMenu.setOnClickListener(this);

        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btnPreviousMenu);
        this.mBtnPreviousMenu.setOnClickListener(this);

        this.mBtnRootMenu = (ImageButton) findViewById(R.id.btnMovieMenu);
        this.mBtnRootMenu.setOnClickListener(this);

//        this.txtOk = (IPTTextView) findViewById(R.id.txtOK);
//        this.txtOk.setOnClickListener(this);

        this.mBtnUp = (ImageButton) findViewById(R.id.btnUp);
        this.mBtnUp.setOnClickListener(this);

        this.mBtnDown = (ImageButton) findViewById(R.id.btnDown);
        this.mBtnDown.setOnClickListener(this);

        this.mBtnRigth = (ImageButton) findViewById(R.id.btnRigth);
        this.mBtnRigth.setOnClickListener(this);

        this.mBtnLeft = (ImageButton) findViewById(R.id.btnLeft);
        this.mBtnLeft.setOnClickListener(this);

        this.mBtnRew = (ImageButton) findViewById(R.id.btnRew);
        this.mBtnRew.setOnClickListener(this);

        this.mBtnFF = (ImageButton) findViewById(R.id.btnFF);
        this.mBtnFF.setOnClickListener(this);

        this.mBtnStop = (ImageButton) findViewById(R.id.btnStop);
        this.mBtnStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Stop.toString()).getRequest());
                finish();
            }
        });

//      this.mBtnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
//      this.mBtnPlayPause.setOnClickListener(this);

        this.mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
        this.mBtnPlay.setOnClickListener(this);

        this.mBtnPause = (ImageButton) findViewById(R.id.btnPause);
        this.mBtnPause.setOnClickListener(this);

        this.mBtnPrevious = (ImageButton) findViewById(R.id.btnPrev);
        this.mBtnPrevious.setOnClickListener(this);

        this.mBtnNext = (ImageButton) findViewById(R.id.btnNext);
        this.mBtnNext.setOnClickListener(this);

        this.mBtnMultiview = (ImageButton) findViewById(R.id.btnMultiview);
        this.mBtnMultiview.setOnClickListener(this);

        this.mBtnVideoMode = (Button) findViewById(R.id.btnVideoMode);
        this.mBtnVideoMode.setOnClickListener(this);

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                GamingActivity.fire(GamingRemoteActivity.this);
            }
        });


        //added by watershao
        IPTTextView audioLanguageTextView = (IPTTextView) findViewById(R.id.audio_language);
        IPTTextView subtitleTextView = (IPTTextView) findViewById(R.id.txt_subtitle);
        IPTTextView previousMenuTextView = (IPTTextView) findViewById(R.id.previous_menu);
        subtitleTextView.setVisibility(View.GONE);
        audioLanguageTextView.setVisibility(View.GONE);
        mBtnAudioLanguage.setVisibility(View.GONE);
        mBtnSubtitles.setVisibility(View.GONE);
        previousMenuTextView.setVisibility(View.VISIBLE);
        mBtnPreviousMenu.setVisibility(View.VISIBLE);

        mBtnPlay.setVisibility(View.GONE);
        mBtnVideoMode.setVisibility(View.GONE);

    }

    /**
     * /**
     *
     * @param context
     */
    public static void fire(Context context, String guid, String title, int inputId) {
        Intent intent = new Intent(context, GamingRemoteActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
