package com.imax.ipt.ui.activity.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.layout.IPTTextView;

public class MymovieActivity extends MediaRemote implements OnClickListener {
    public static final String INPUT_ID = "INPUT_ID";

//    public static final String PREFERENCE_DEFAULT_OPPO_CONTROL_COMPLEXITY_FULL = "default_custom_oppo_complexity_full";
    private static final String TAG = "MymovieActivity";

    private IPTTextView mTxtTitle;
    private String mTitle;
    private String guid;
    private ImageButton mBtnMenuMaster;
    private ImageButton mBtnImax;
    private Switch btnSimpleorFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mymovie);
//        this.addMenuFragment();
        this.init();
    }

    protected void init() {
        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
        this.mTitle = getIntent().getStringExtra(MEDIA_TITLE);
        this.guid = getIntent().getStringExtra(MEDIA_ID);
        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitle);

        this.mTxtTitle.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.FONT_LIGHT_PATH));
        this.mTxtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle.toUpperCase());

        mBtnMenuMaster = (ImageButton) findViewById(R.id.btnMenuMaster);
        mBtnImax = (ImageButton) findViewById(R.id.btn_nav_Imax);
        mBtnMenuMaster.setOnClickListener(this);
        mBtnImax.setOnClickListener(this);


        ImageButton zoomButton = (ImageButton) findViewById(R.id.btnZoom);
        zoomButton.setVisibility(View.VISIBLE);
        zoomButton.setOnClickListener(this);

//        ImageButton numButton = (ImageButton) findViewById(R.id.btnnum);
//        numButton.setVisibility(View.VISIBLE);
//        numButton.setOnClickListener(this);

        this.mBtnAudioLanguage = (ImageButton) findViewById(R.id.btnAudioLanguage);
        this.mBtnAudioLanguage.setVisibility(View.INVISIBLE);
//        this.mBtnAudioLanguage.setOnClickListener(this);

        this.mBtnSubtitles = (ImageButton) findViewById(R.id.btnSubtitles);
        this.mBtnSubtitles.setVisibility(View.INVISIBLE);
//        this.mBtnSubtitles.setOnClickListener(this);

        this.mBtnPopUpMenu = (ImageButton) findViewById(R.id.btnPopUpMenu);
        this.mBtnPopUpMenu.setVisibility(View.INVISIBLE);
//        this.mBtnPopUpMenu.setOnClickListener(this);

//        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btnPreviousMenu);
        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btn_remote);
        this.mBtnPreviousMenu.setVisibility(View.GONE);
//        this.mBtnPreviousMenu.setOnClickListener(this);

        this.mBtnRootMenu = (ImageButton) findViewById(R.id.btnMovieMenu);
        this.mBtnRootMenu.setOnClickListener(this);

//        this.txtOk = (IPTTextView) findViewById(R.id.txtOK);
//        this.txtOk.setOnClickListener(this);
        this.mBtnOk2 = (Button) findViewById(R.id.btnOk2);
        this.mBtnOk2.setOnClickListener(this);

        this.mBtnUp = (ImageButton) findViewById(R.id.btnUp);
        this.mBtnUp.setOnClickListener(this);

        this.mBtnDown = (ImageButton) findViewById(R.id.btnDown);
        this.mBtnDown.setOnClickListener(this);

        this.mBtnRigth = (ImageButton) findViewById(R.id.btnRigth);
        this.mBtnRigth.setOnClickListener(this);

        this.mBtnLeft = (ImageButton) findViewById(R.id.btnLeft);
        this.mBtnLeft.setOnClickListener(this);

        this.mBtnRew = (ImageButton) findViewById(R.id.btnRew);
        this.mBtnRew.setVisibility(View.VISIBLE);
        this.mBtnRew.setOnClickListener(this);

        this.mBtnFF = (ImageButton) findViewById(R.id.btnFF);
        this.mBtnFF.setVisibility(View.VISIBLE);
        this.mBtnFF.setOnClickListener(this);

//        this.mBtnStop = (ImageButton) findViewById(R.id.btnStop);
//        this.mBtnStop.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Stop.toString()).getRequest());
//            }
//        });

        this.mBtnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
        this.mBtnPlayPause.setVisibility(View.VISIBLE);
        this.mBtnPlayPause.setOnClickListener(this);

//        this.mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
//        this.mBtnPlay.setVisibility(View.VISIBLE);
//        this.mBtnPlay.setOnClickListener(this);

//        this.mBtnPause = (ImageButton) findViewById(R.id.btnPause);
//        this.mBtnPause.setVisibility(View.VISIBLE);
//        this.mBtnPause.setOnClickListener(this);

//        this.mBtnPrevious = (ImageButton) findViewById(R.id.btnPrev);
//        this.mBtnPrevious.setVisibility(View.VISIBLE);
//        this.mBtnPrevious.setOnClickListener(this);

//        this.mBtnNext = (ImageButton) findViewById(R.id.btnNext);
//        this.mBtnNext.setVisibility(View.VISIBLE);
//        this.mBtnNext.setOnClickListener(this);

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.BackMenu.toString()).getRequest());
            }
        });

        ImageButton btnNavImax = (ImageButton) findViewById(R.id.btn_nav_Imax);
        btnNavImax.setVisibility(View.VISIBLE);
        IPTTextView menuTextView = (IPTTextView) findViewById(R.id.txtMenu);
        menuTextView.setVisibility(View.INVISIBLE);


    }

    /**
     * /**
     *
     * @param context
     */
    public static void fire(Context context, String guid, String title, int inputId) {
        Intent intent = new Intent(context, MymovieActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
