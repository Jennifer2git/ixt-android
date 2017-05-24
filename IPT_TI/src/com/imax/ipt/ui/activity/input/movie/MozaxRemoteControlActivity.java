package com.imax.ipt.ui.activity.input.movie;

/**
 * Created by yanli on 2015/7/6.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.push.MediaLoadProgressChangedEvent;
import com.imax.ipt.ui.activity.LobbyActivity;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.layout.IPTTextView;

public class MozaxRemoteControlActivity extends MediaRemote implements View.OnClickListener {
    public static final String TAG = MozaxRemoteControlActivity.class.getSimpleName();
    public static final String INPUT_ID = "INPUT_ID";
    public static final String PREFERENCE_DEFAULT_MOVIE_CONTROL_COMPLEXITY_FULL = "default_custom_movie_complexity_full";

    private IPTTextView mTxtTitle;
    private String mTitle;
    private String guid;

    private ProgressBar progresSeekBar;
//        private ImageButton mbtnOptionsMenu;
    private Button mbtnOptionsMenu;

    /**
     * /**
     *
     * @param context
     */
    public static void fire(Context context, String guid, String title, int inputId) {
        Intent intent = new Intent(context, MozaxRemoteControlActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mozax_remote_control);
//        this.addMenuFragment();
        this.init();
    }

    protected void init() {
        Log.d(TAG, "init()");
        this.mode = FULL;
        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
        this.mTitle = getIntent().getStringExtra(MEDIA_TITLE);
        this.guid = getIntent().getStringExtra(MEDIA_ID);

//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
//        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
//        this.mSeekBarVolumen.setMax(Constants.MAX_VOLUME_VALUE);
//        this.sendRequestGetVolume();

//        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
//        this.mBtnMute.setOnClickListener(this);
//        this.sendRequestGetMuteVolume();

        this.mBtnAudioLanguage = (ImageButton) findViewById(R.id.btnAudioLanguage);
        this.mBtnAudioLanguage.setOnClickListener(this);

        this.mBtnSubtitles = (ImageButton) findViewById(R.id.btnSubtitles);
        this.mBtnSubtitles.setOnClickListener(this);

        this.mBtnPopUpMenu = (ImageButton) findViewById(R.id.btnPopUpMenu);
        this.mBtnPopUpMenu.setOnClickListener(this);

//        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btnPreviousMenu);
        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btn_remote);
        this.mBtnPreviousMenu.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_back2play_button));
        this.mBtnPreviousMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: should back to where? or nowplaying
                MovieLibraryActivity.fire(v.getContext());
            }
        });

        this.mBtnRootMenu = (ImageButton) findViewById(R.id.btnMovieMenu);
        this.mBtnRootMenu.setOnClickListener(this);

//        this.txtOk = (IPTTextView) findViewById(R.id.txtOK);
//        this.txtOk.setOnClickListener(this);

        this.mBtnOk = (ImageButton) findViewById(R.id.btnOk);
        this.mBtnOk.setOnClickListener(this);
        this.mBtnUp = (ImageButton) findViewById(R.id.btnUp);
        this.mBtnUp.setOnClickListener(this);

        this.mBtnDown = (ImageButton) findViewById(R.id.btnDown);
        this.mBtnDown.setOnClickListener(this);

        this.mBtnRigth = (ImageButton) findViewById(R.id.btnRigth);
        this.mBtnRigth.setOnClickListener(this);

        this.mBtnLeft = (ImageButton) findViewById(R.id.btnLeft);
        this.mBtnLeft.setOnClickListener(this);

//        this.mBtnRew = (ImageButton) findViewById(R.id.btnRew);
//        this.mBtnRew.setOnClickListener(this);

//        this.mBtnFF = (ImageButton) findViewById(R.id.btnFF);
//        this.mBtnFF.setOnClickListener(this);

//        this.mBtnStop = (ImageButton) findViewById(R.id.btnStop);
///        this.mBtnStop.setOnClickListener(new View.OnClickListener() {

//            @Override
//            public void onClick(View v) {
//                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Stop.toString()).getRequest());
//            finish();
//            }
//        });

//        this.mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
//        this.mBtnPlay.setOnClickListener(this);

//        this.mBtnPause = (ImageButton) findViewById(R.id.btnPause);
//        this.mBtnPause.setOnClickListener(this);

//        this.mBtnPrevious = (ImageButton) findViewById(R.id.btnPrev);
//        this.mBtnPrevious.setOnClickListener(this);

//        this.mBtnNext = (ImageButton) findViewById(R.id.btnNext);
//        this.mBtnNext.setOnClickListener(this);

        this.mBtnMultiview = (ImageButton) findViewById(R.id.btnMultiview);
        this.mBtnMultiview.setOnClickListener(this);

//        this.mBtnVideoMode = (Button) findViewById(R.id.btnVideoMode);
//        this.mBtnVideoMode.setOnClickListener(this);

        findViewById(R.id.btnMenuMaster).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LobbyActivity.fire(MozaxRemoteControlActivity.this);
                finish();
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieLibraryActivity.fireToFront(MozaxRemoteControlActivity.this);
                finish();
            }
        });


        //added by watershao
//        IPTTextView audioLanguageTextView = (IPTTextView) findViewById(R.id.audio_language);
//        IPTTextView subtitleTextView = (IPTTextView) findViewById(R.id.txt_subtitle);
        //begain removed by jennifer
//        IPTTextView previousMenuTextView = (IPTTextView) findViewById(R.id.previous_menu);
//        previousMenuTextView.setVisibility(View.VISIBLE);
//        mBtnPreviousMenu.setVisibility(View.VISIBLE);

//        mbtnOptionsMenu = (ImageButton) findViewById(R.id.btnOptionsMenu);
//        mbtnOptionsMenu = (Button) findViewById(R.id.btnOptionsMenu);
//        mbtnOptionsMenu.setOnClickListener(this);
        //end removed by jennifer

//        progresSeekBar = (ProgressBar) findViewById(R.id.seekBarDuration);
//        progresSeekBar.setProgress(20);
//        progresSeekBar.setVisibility(View.GONE);

        ImageButton zoomButton = (ImageButton) findViewById(R.id.btnZoom);
        zoomButton.setOnClickListener(this);

//        volupButton = (ImageButton) findViewById(R.id.volumeUp);
//        voldownButton = (ImageButton) findViewById(R.id.volumeDown);
//        volupButton.setOnClickListener(this);
//        voldownButton.setOnClickListener(this);

    }

    public void onEvent(MediaLoadProgressChangedEvent mediaLoadProgressChangedEvent) {
//        progresSeekBar.setProgress(mediaLoadProgressChangedEvent.getmPorcentage());

    }

}
