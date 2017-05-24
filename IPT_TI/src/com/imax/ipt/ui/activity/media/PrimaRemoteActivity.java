package com.imax.ipt.ui.activity.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.layout.IPTTextView;

import java.util.List;

public class PrimaRemoteActivity extends MediaRemote implements OnClickListener {
    public static final String INPUT_ID = "INPUT_ID";

    private IPTTextView mTxtTitle;
    private String mTitle;
    private String guid;
//   private ImageButton mBtnMultiview;

    private static final String ip = "172.31.1.17";
    private static final int port = 5556;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prima_remote);
        this.addMenuFragment();

        this.init();
        Button btnHPremiere = (Button)findViewById(R.id.btnHPremiere);;
        btnHPremiere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHPremiere();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
//		mEventBus.post("connect");
    }


    @Override
    protected void onPause() {
        super.onPause();
//		mEventBus.post("disconnect");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnUp:
                    mEventBus.post(PrimaHelp.PRIMA_UP);
                    break;
                case R.id.btnDown:
                    mEventBus.post(PrimaHelp.PRIMA_DOWN);
                    break;
                case R.id.btnLeft:
                    mEventBus.post(PrimaHelp.PRIMA_LEFT);
                    break;
                case R.id.btnRigth:
                    mEventBus.post(PrimaHelp.PRIMA_RIGHT);
                    break;
                case R.id.txtOK:
                    mEventBus.post(PrimaHelp.PRIMA_OK);
                    break;

                default:
                    break;
            }
        }


    };

    //onEventBackgroundThread,onEventMainThread,onEventAsync,
    public void onEventAsync(String cmd) {
        Log.d("PrimaRemoteActivity", "onEventMainThread=" + cmd);

        if (cmd.equalsIgnoreCase("connect")) {

            boolean connect = PrimaHelp.connect(ip, 5556);
            Log.d("PrimaRemoteActivity", "connect=" + connect);
            return;
        }

        if (cmd.equalsIgnoreCase("disconnect")) {
            boolean connect = PrimaHelp.disconnect();
            Log.d("PrimaRemoteActivity", "disconnect=" + connect);
            return;
        }
//	    	PrimaHelp.sendTCP(ip, 5556, cmd);
        boolean send = PrimaHelp.send(cmd);

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

        this.txtOk = (IPTTextView) findViewById(R.id.txtOK);
//      this.txtOk.setOnClickListener(onClickListener);
        this.txtOk.setOnClickListener(this);

        this.mBtnUp = (ImageButton) findViewById(R.id.btnUp);
//      this.mBtnUp.setOnClickListener(onClickListener);
        this.mBtnUp.setOnClickListener(this);

        this.mBtnDown = (ImageButton) findViewById(R.id.btnDown);
//      this.mBtnDown.setOnClickListener(onClickListener);
        this.mBtnDown.setOnClickListener(this);

        this.mBtnRigth = (ImageButton) findViewById(R.id.btnRigth);
//      this.mBtnRigth.setOnClickListener(onClickListener);
        this.mBtnRigth.setOnClickListener(this);

        this.mBtnLeft = (ImageButton) findViewById(R.id.btnLeft);
//      this.mBtnLeft.setOnClickListener(onClickListener);
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
                MovieLibraryActivity.fireToFront(PrimaRemoteActivity.this);
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
//      previousMenuTextView.setVisibility(View.VISIBLE);
//      mBtnPreviousMenu.setVisibility(View.VISIBLE);

        mBtnPlay.setVisibility(View.GONE);
        mBtnVideoMode.setVisibility(View.GONE);

        Button zoomButton = (Button) findViewById(R.id.btnZoom);
        zoomButton.setOnClickListener(this);

        volupButton = (ImageButton) findViewById(R.id.volumeUp);
        voldownButton = (ImageButton) findViewById(R.id.volumeDown);
        volupButton.setOnClickListener(this);
        voldownButton.setOnClickListener(this);

    }


    /**
     * /**
     *
     * @param context
     */
    public static void fire(Context context, String guid, String title, int inputId) {
        Intent intent = new Intent(context, PrimaRemoteActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void startHPremiere() {
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(Constants.IMAX_PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        List<ResolveInfo> resolveinfoList = getPackageManager().queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename
            String packageName = resolveinfo.activityInfo.packageName;
            // this is  the main activity 's class name which we want started. [packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            startActivity(intent);
        }
    }

}
