package com.imax.ipt.ui.activity.input.music;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.*;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.push.MediaLoadProgressChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.PlayingMusicTrackChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.model.MusicAlbum;
import com.imax.ipt.model.MusicTrack;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.ImageUtil;
import com.imax.ipt.ui.util.MySwitch;
import com.imax.ipt.ui.util.MySwitch.OnSwitchListener;

public class MusicRemoteActivity extends MediaRemote {
    public static final String PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL = "default_custom_music_complexity_full";

    private MySwitch btnSimpleorFull;
    protected IPTTextView txtArtist;
    protected IPTTextView txtAlbum;

    protected ImageView mImgCover;
    protected String mAlbumId;
    protected String mTrackId;
    protected String mArtist;
    protected String mAlbum;
    protected String mCover;
    private LoaderImage loaderImage;

    private ProgressBar progresSeekBar;

    private boolean mPlay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_remote);
        this.addMenuFragment();
        this.init();

    }

    /**
     * @param context
     */
    public static void fire(Context context, String albumId, String trackId, String title, String musicArtist, String musicAlbum, String musicCover) {
        Intent intent = new Intent(context, MusicRemoteActivity.class);
        intent.putExtra(MediaRemote.MUSIC_ALBUM_ID, albumId);
        intent.putExtra(MediaRemote.MUSIC_TRACK_ID, trackId);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(MediaRemote.MUSIC_ARTIST, musicArtist);
        intent.putExtra(MediaRemote.MUSIC_ALBUM, musicAlbum);
        intent.putExtra(MediaRemote.MUSIC_COVER, musicCover);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void init() {

        this.mode = SIMPLE;
        this.initSimpleOrRemoteControl();
        this.mTrackId = getIntent().getStringExtra(MUSIC_TRACK_ID);
        this.mAlbumId = getIntent().getStringExtra(MUSIC_ALBUM_ID);
        this.mTitle = getIntent().getStringExtra(MEDIA_TITLE);
        this.mArtist = getIntent().getStringExtra(MUSIC_ARTIST);
        this.mAlbum = getIntent().getStringExtra(MUSIC_ALBUM);
        this.mCover = getIntent().getStringExtra(MUSIC_COVER);

        this.txtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        this.txtArtist = (IPTTextView) findViewById(R.id.txtArtist);
        this.txtAlbum = (IPTTextView) findViewById(R.id.txtAlbumName);
//      this.txtAlbum.setText(mAlbum);
//      this.txtArtist.setText(mArtist);

        this.mImgCover = (ImageView) findViewById(R.id.imgCoverAlbums);
        this.txtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle);

//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
        this.sendRequestGetVolume();

        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
        this.mBtnMute.setOnClickListener(this);
        this.sendRequestGetMuteVolume();

        // this.mBtnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
        // this.mBtnPlayPause.setOnClickListener(this);

        this.mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
        this.mBtnPlay.setOnClickListener(this);


        this.mBtnPause = (ImageButton) findViewById(R.id.btnPause);
//      this.mBtnPause.setOnClickListener(this);
        this.mBtnPause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlay) {
                    mPlay = false;
                    mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Pause.toString()).getRequest());
                } else {
                    mPlay = true;
                    mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.PlayPause.toString()).getRequest());
                }
            }
        });


        this.mBtnRew = (ImageButton) findViewById(R.id.btnRew);
        this.mBtnRew.setOnClickListener(this);

        this.mBtnFF = (ImageButton) findViewById(R.id.btnFF);
        this.mBtnFF.setOnClickListener(this);

        this.txtAlbum.setText(mAlbum);
        this.txtArtist.setText(mArtist);

        loaderImage = new LoaderImage();
//      loaderImage.execute(mCover);
        loaderImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCover);

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MusicLibraryActivity.fire(MusicRemoteActivity.this);
            }
        });

        mBtnRew.setVisibility(View.VISIBLE);
        mBtnPlay.setVisibility(View.GONE);

        progresSeekBar = (ProgressBar) findViewById(R.id.seekBarDuration);
        progresSeekBar.setProgress(20);
        progresSeekBar.setVisibility(View.GONE);

        Button zoomButton = (Button) findViewById(R.id.btnZoom);
        zoomButton.setOnClickListener(this);

        volupButton = (ImageButton) findViewById(R.id.volumeUp);
        voldownButton = (ImageButton) findViewById(R.id.volumeDown);
        volupButton.setOnClickListener(this);
        voldownButton.setOnClickListener(this);

    }


    public void onEvent(MediaLoadProgressChangedEvent mediaLoadProgressChangedEvent) {
        progresSeekBar.setProgress(mediaLoadProgressChangedEvent.getmPorcentage());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loaderImage != null) {
            loaderImage.cancel(true);
        }
    }

    /**
     *
     */
    private void initSimpleOrRemoteControl() {
        btnSimpleorFull = (MySwitch) findViewById(R.id.btnSimpleFull);
        btnSimpleorFull.setImageResource(R.drawable.knob_outline, R.drawable.knob_outline, R.drawable.knob);
        btnSimpleorFull.setSwitchState(false);

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
                prefs.edit().putBoolean(PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL, Boolean.TRUE).commit();

                MusicRemoteFullActivity.fire(MusicRemoteActivity.this, mAlbumId, mTrackId, mTitle, mArtist, mAlbum, mCover);

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
                    prefs.edit().putBoolean(PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL, Boolean.TRUE).commit();

                    MusicRemoteFullActivity.fire(MusicRemoteActivity.this, mAlbumId, mTrackId, mTitle, mArtist, mAlbum, mCover);
                }
                return true;
            }
        });


        btnSimpleorFull.setOnSwitchListener(new OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                // TODO Auto-generated method stub
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean(PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL, Boolean.TRUE).commit();

                MusicRemoteFullActivity.fire(MusicRemoteActivity.this, mAlbumId, mTrackId, mTitle, mArtist, mAlbum, mCover);
            }
        });
      
/*      btnSimpleorFull.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putBoolean(PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL, Boolean.TRUE).commit();
                        
            MusicRemoteFullActivity.fire(MusicRemoteActivity.this, mAlbumId, mTrackId, mTitle, mArtist, mAlbum, mCover);
         }
      });*/
    }

    public void onEventMainThread(PlayingMusicTrackChangedEvent nowPlayingEvent) {
        MusicAlbum musicAlbum = nowPlayingEvent.getmMusicAlbum();
        MusicTrack[] musicTracks = musicAlbum.getTracks();
        if (musicTracks.length > 0) {
            MusicTrack musicTrack = musicTracks[0];
            // somehow this.txtTitle is not the right text view we want
            // find it again anyway
            IPTTextView textView = (IPTTextView) findViewById(R.id.txtTitle);
            textView.setText(getResources().getString(R.string.now_playing) + " " + musicTrack.getTitle());
        }
    }

    /**
     * @author rlopez
     */
    class LoaderImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return ImageUtil.getImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                BitmapDrawable background = new BitmapDrawable(getResources(), result);
                mImgCover.setImageDrawable(background);
            }
        }

    }

}
