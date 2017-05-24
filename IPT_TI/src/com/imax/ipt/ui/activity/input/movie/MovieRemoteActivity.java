//package com.imax.ipt.ui.activity.input.movie;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.widget.*;
//import com.imax.ipt.R;
//import com.imax.ipt.controller.eventbus.handler.push.MediaLoadProgressChangedEvent;
//import com.imax.ipt.controller.eventbus.handler.push.MovieStartedEvent;
//import com.imax.ipt.ui.activity.media.MediaRemote;
//import com.imax.ipt.ui.activity.media.SonyRemoteFullActivity;
//import com.imax.ipt.ui.layout.IPTTextView;
//import com.imax.ipt.ui.util.MySwitch;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class MovieRemoteActivity extends MediaRemote {
//    //   private ToggleButton btnSimpleorFull;
//    private MySwitch btnSimpleorFull;
//    private IPTTextView txtTitle;
//    private String title;
//    private String guid;
//    private int inputId;
//
//    private ProgressDialog progress = null;
//    public static boolean isShowPrepareProgress = false;
//    private ProgressBar progresSeekBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movie_remote);
//        this.addMenuFragment();
//        this.init();
//
//        if(isShowPrepareProgress){
//            progress = new DialogMovieStarted(this);
//            progress.setCanceledOnTouchOutside(false);
//            progress.show();
//            isShowPrepareProgress = false;
//            stopShowPrepare();
//        }
//
//    }
//
//    /**
//     * @param context
//     */
//    public static void fire(Context context, String guid, String title, int inputId) {
//        Intent intent = new Intent(context, MovieRemoteActivity.class);
//        intent.putExtra(MediaRemote.MEDIA_ID, guid);
//        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
//        intent.putExtra(SonyRemoteFullActivity.INPUT_ID, inputId);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
//
//    protected void init() {
//        this.mode = SIMPLE;
//        this.initSimpleOrRemoteControl();
//        this.title = getIntent().getStringExtra(MEDIA_TITLE);
//        this.guid = getIntent().getStringExtra(MEDIA_ID);
//        this.inputId = getIntent().getIntExtra(SonyRemoteFullActivity.INPUT_ID, 0);
//        this.txtTitle = (IPTTextView) findViewById(R.id.txtTitle);
//        this.txtTitle.setText(getResources().getString(R.string.now_playing) + " " + title);
//
////        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
//        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
//        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
//        this.sendRequestGetVolume();
//
//        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
//        this.mBtnMute.setOnClickListener(this);
//        this.sendRequestGetMuteVolume();
//
//        this.mBtnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
//        this.mBtnPlayPause.setOnClickListener(this);
//
//        this.mBtnRew = (ImageButton) findViewById(R.id.btnRew);
//        this.mBtnRew.setOnClickListener(this);
//
//        this.mBtnFF = (ImageButton) findViewById(R.id.btnFF);
//        this.mBtnFF.setOnClickListener(this);
//
//        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                finish();
//                MovieLibraryActivity.fireToFront(MovieRemoteActivity.this);
//            }
//        });
//
//        progresSeekBar = (ProgressBar) findViewById(R.id.seekBarDuration);
//        progresSeekBar.setProgress(20);
//        progresSeekBar.setVisibility(View.GONE);
//
//        Button zoomButton = (Button) findViewById(R.id.btnZoom);
//        zoomButton.setOnClickListener(this);
//
//        volupButton = (ImageButton) findViewById(R.id.volumeUp);
//        voldownButton = (ImageButton) findViewById(R.id.volumeDown);
//        volupButton.setOnClickListener(this);
//        voldownButton.setOnClickListener(this);
//
//    }
//
//
//    public void onEvent(MediaLoadProgressChangedEvent mediaLoadProgressChangedEvent) {
//        progresSeekBar.setProgress(mediaLoadProgressChangedEvent.getmPorcentage());
//    }
//
//    public void onEvent(MovieStartedEvent movieStartedEvent){
//        if(progress != null) {
//            progress.dismiss();
//            isShowPrepareProgress = false;
//        }
//    }
//
//    /**
//     * stop show prepare to play movie progress incase of no MovieStartedEvent
//     */
//    private void stopShowPrepare(){
//        Timer timer = new Timer();
//        TimerTask countTimeTask = new TimerTask() {
//            @Override
//            public void run() {
//                if(progress != null) {
//                    progress.dismiss();
//                    isShowPrepareProgress = false;
//                }
//            }
//        };
//        timer.schedule(countTimeTask,15*1000);//15s
//
//    }
//
//    private void initSimpleOrRemoteControl() {
//        btnSimpleorFull = (MySwitch) findViewById(R.id.btnSimpleFull);
//        btnSimpleorFull.setImageResource(R.drawable.knob_outline, R.drawable.knob_outline, R.drawable.knob);
//
//        TextView btnSimple = (TextView) findViewById(R.id.btnSimple);
//        TextView btnFull = (TextView) findViewById(R.id.btnFull);
//        btnSimple.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                btnSimpleorFull.setSwitchState(false);
//            }
//        });
//        btnFull.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                btnSimpleorFull.setSwitchState(true);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                prefs.edit().putBoolean(SonyRemoteFullActivity.PREFERENCE_DEFAULT_SONY_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();
////                MovieRemoteFullActivity.fire(MovieRemoteActivity.this, guid, title, inputId);
//            }
//        });
//
//
//        btnSimple.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    btnSimpleorFull.setSwitchState(false);
//                }
//                return true;
//            }
//        });
//        btnFull.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    btnSimpleorFull.setSwitchState(true);
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    prefs.edit().putBoolean(SonyRemoteFullActivity.PREFERENCE_DEFAULT_SONY_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();
////                    MovieRemoteFullActivity.fire(MovieRemoteActivity.this, guid, title, inputId);
//                }
//                return true;
//            }
//        });
//
//    }
//
//}
