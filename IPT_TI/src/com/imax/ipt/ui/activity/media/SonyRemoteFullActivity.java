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
import com.imax.ipt.ui.util.MySwitch;

public class SonyRemoteFullActivity extends MediaRemote implements OnClickListener {
    public static final String INPUT_ID = "INPUT_ID";

    public static final String PREFERENCE_DEFAULT_SONY_CONTROL_COMPLEXITY_FULL = "default_custom_sony_complexity_full";

    private IPTTextView mTxtTitle;
    private String mTitle;
    private String guid;
    private MySwitch btnSimpleorFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sony_remote_full);
        this.addMenuFragment();
        this.init();
    }

    protected void init() {
        this.mode = FULL;
        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
        this.mTitle = getIntent().getStringExtra(MEDIA_TITLE);
        this.guid = getIntent().getStringExtra(MEDIA_ID);
        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        this.mTxtTitle.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.FONT_LIGHT_PATH));
        this.mTxtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle.toUpperCase());


        this.mBtnAudioLanguage = (ImageButton) findViewById(R.id.btnNetVideo);
//        this.mBtnAudioLanguage = (ImageButton) findViewById(R.id.btnAudioLanguage);
        this.mBtnAudioLanguage.setOnClickListener(this);

        this.mBtnSubtitles = (ImageButton) findViewById(R.id.btnSubtitles);
        this.mBtnSubtitles.setOnClickListener(this);

        this.mBtnPopUpMenu = (ImageButton) findViewById(R.id.btnOptionMenu);
        this.mBtnPopUpMenu.setOnClickListener(this);

        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btnBackMenu); // this is  sepcific.
//        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btn_remote);
        this.mBtnPreviousMenu.setOnClickListener(this);

        this.mBtnRootMenu = (ImageButton) findViewById(R.id.btnMasterMenu);
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
        this.mBtnRew.setOnClickListener(this);

        this.mBtnFF = (ImageButton) findViewById(R.id.btnFF);
        this.mBtnFF.setOnClickListener(this);

//        this.mBtnStop = (ImageButton) findViewById(R.id.btnStop);
//        this.mBtnStop.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.BackMenu.toString()).getRequest());
//            }
//        });

//      this.mBtnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
//      this.mBtnPlayPause.setOnClickListener(this);

//        this.mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
//        this.mBtnPlay.setOnClickListener(this);

        this.mBtnPause = (ImageButton) findViewById(R.id.btnPause);
        this.mBtnPause.setOnClickListener(this);

        this.mBtnPause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Enter.toString()).getRequest());
            }
        });

        this.mBtnPrevious = (ImageButton) findViewById(R.id.btnPrev);
        this.mBtnPrevious.setOnClickListener(this);

        this.mBtnNext = (ImageButton) findViewById(R.id.btnNext);
        this.mBtnNext.setOnClickListener(this);

//        this.mBtnMultiview = (ImageButton) findViewById(R.id.btnMultiview);
//        this.mBtnMultiview.setOnClickListener(this);

//        this.mBtnVideoMode = (Button) findViewById(R.id.btnVideoMode);
//        this.mBtnVideoMode.setOnClickListener(this);

//        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                MovieLibraryActivity.fireToFront(SonyRemoteFullActivity.this);
//            }
//        });

        ImageButton zoomButton = (ImageButton) findViewById(R.id.btnZoom);
        zoomButton.setOnClickListener(this);

//        mBtnPlay.setVisibility(View.GONE);
//        mBtnVideoMode.setVisibility(View.GONE);
//        mBtnMultiview.setVisibility(View.GONE);


    }

    /**
     * /**
     *
     * @param context
     */
    public static void fire(Context context, String guid, String title, int inputId) {
        Intent intent = new Intent(context, SonyRemoteFullActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
