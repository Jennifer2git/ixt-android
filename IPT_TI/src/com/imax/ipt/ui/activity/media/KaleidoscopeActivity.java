package com.imax.ipt.ui.activity.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.layout.IPTTextView;

public class KaleidoscopeActivity extends MediaRemote implements OnClickListener {
    public static final String INPUT_ID = "INPUT_ID";

    private static final String TAG = KaleidoscopeActivity.class.getSimpleName();

    private IPTTextView mTxtTitle;
    private String mTitle;
    private ImageButton mBtnMenuMaster;
    private ImageButton mBtnImax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaleidoscope);
        this.init();
    }

    protected void init() {
        this.mode = FULL;
        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
        this.mTitle = getIntent().getStringExtra(MEDIA_TITLE);
//        this.guid = getIntent().getStringExtra(MEDIA_ID);
        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitle);

        this.mTxtTitle.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.FONT_LIGHT_PATH));
        this.mTxtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle.toUpperCase());

        mBtnMenuMaster = (ImageButton) findViewById(R.id.btnMenuMaster);
        mBtnImax = (ImageButton) findViewById(R.id.btn_nav_Imax);
        mBtnMenuMaster.setOnClickListener(this);
        mBtnImax.setOnClickListener(this);


        ImageButton zoomButton = (ImageButton) findViewById(R.id.btnZoom);
        zoomButton.setOnClickListener(this);

        ImageButton numButton = (ImageButton) findViewById(R.id.btnnum);
        numButton.setVisibility(View.VISIBLE);
        numButton.setOnClickListener(this);


        this.mBtnAudioLanguage = (ImageButton) findViewById(R.id.btnAudioLanguage);
        this.mBtnAudioLanguage.setVisibility(View.GONE);
//        this.mBtnAudioLanguage.setOnClickListener(this);
        this.mBtnSubtitles = (ImageButton) findViewById(R.id.btnSubtitles);
        this.mBtnSubtitles.setVisibility(View.GONE);
//        this.mBtnSubtitles.setOnClickListener(this);

        this.mBtnPopUpMenu = (ImageButton) findViewById(R.id.btnPopUpMenu);
        this.mBtnPopUpMenu.setOnClickListener(this);

//        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btnPreviousMenu);
        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btn_remote);
        this.mBtnPreviousMenu.setOnClickListener(this);

        this.mBtnRootMenu = (ImageButton) findViewById(R.id.btnMovieMenu);
        this.mBtnRootMenu.setOnClickListener(this);

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

        this.mBtnStop = (ImageButton) findViewById(R.id.btnStop);
        this.mBtnStop.setVisibility(View.VISIBLE);
        this.mBtnStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Stop.toString()).getRequest());
            }
        });

        this.mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
        this.mBtnPlay.setVisibility(View.VISIBLE);
        this.mBtnPlay.setOnClickListener(this);

        this.mBtnPause = (ImageButton) findViewById(R.id.btnPause);
        this.mBtnPause.setVisibility(View.VISIBLE);
        this.mBtnPause.setOnClickListener(this);

        this.mBtnPrevious = (ImageButton) findViewById(R.id.btnPrev);
        this.mBtnPrevious.setVisibility(View.VISIBLE);
        this.mBtnPrevious.setOnClickListener(this);

        this.mBtnNext = (ImageButton) findViewById(R.id.btnNext);

        this.mBtnNext.setVisibility(View.VISIBLE);
        this.mBtnNext.setOnClickListener(this);

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
        Intent intent = new Intent(context, KaleidoscopeActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
