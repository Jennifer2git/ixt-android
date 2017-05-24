package com.imax.ipt.ui.activity.input.music;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicAlbumHandler;
import com.imax.ipt.controller.eventbus.handler.music.PlayMusicTrackHandler;
import com.imax.ipt.controller.eventbus.handler.push.MediaLoadProgressChangedEvent;
import com.imax.ipt.model.MusicAlbum;
import com.imax.ipt.model.MusicTrack;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.MySwitch;
import com.imax.ipt.ui.util.MySwitch.OnSwitchListener;

public class MusicRemoteFullActivity extends MusicRemoteActivity implements OnClickListener {

    private MySwitch mBtnSimpleorFull;
    private IPTTextView mTxtTitle;
    private LoaderImage loaderImage;
    private MusicAlbum mMusicAlbum;
    private LinearLayout layoutTracks;

    private ProgressBar progresSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_remote_full);
        this.addMenuFragment();
        this.init();
    }

    private void init() {
        Log.d("JENNIFER", "Jennifer, MusicRemoteFullActivity init.");
        this.mode = FULL;

        mBtnSimpleorFull = (MySwitch) findViewById(R.id.btnSimpleFull);
        mBtnSimpleorFull.setImageResource(R.drawable.knob_outline, R.drawable.knob_outline, R.drawable.knob);
        mBtnSimpleorFull.setSwitchState(true);

        this.initSimpleOrRemoteControl();
        this.mTrackId = getIntent().getStringExtra(MUSIC_TRACK_ID);
        this.mAlbumId = getIntent().getStringExtra(MUSIC_ALBUM_ID);
        this.mTitle = getIntent().getStringExtra(MEDIA_TITLE);
        this.mArtist = getIntent().getStringExtra(MUSIC_ARTIST);
        this.mAlbum = getIntent().getStringExtra(MUSIC_ALBUM);
        this.mCover = getIntent().getStringExtra(MUSIC_COVER);

        this.mImgCover = (ImageView) findViewById(R.id.imgCoverAlbums);

        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        if (mTitle.trim().length() > 0) {
            this.mTxtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle);
        } else {
            this.mTxtTitle.setText(getResources().getString(R.string.now_playing) + " " + mAlbum);
        }

        this.txtArtist = (IPTTextView) findViewById(R.id.txtArtist);
        this.txtAlbum = (IPTTextView) findViewById(R.id.txtAlbumName);
        this.txtAlbum.setText(mAlbum);
        this.txtArtist.setText(mArtist);

//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
        this.sendRequestGetVolume();

        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
        this.mBtnMute.setOnClickListener(this);
        this.sendRequestGetMuteVolume();

        this.mBtnRew = (ImageButton) findViewById(R.id.btnRew);
        this.mBtnRew.setOnClickListener(this);

        this.mBtnFF = (ImageButton) findViewById(R.id.btnFF);
        this.mBtnFF.setOnClickListener(this);

        this.mBtnStop = (ImageButton) findViewById(R.id.btnStop);
        this.mBtnStop.setOnClickListener(this);

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

        layoutTracks = (LinearLayout) findViewById(R.id.layouTracks);

        loaderImage = new LoaderImage();
//      loaderImage.execute(mCover);
        loaderImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCover);

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MusicLibraryActivity.fire(MusicRemoteFullActivity.this);
                finish();
            }
        });

        // do not get music tracks if the album is being play from disc
        if (!mAlbumId.equals(Handler.DEFAULT_GUID))
            this.getMusicAlbum(mAlbumId);


        mBtnRew.setVisibility(View.VISIBLE);
        mBtnPlay.setVisibility(View.VISIBLE);

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
/*      mBtnSimpleorFull.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putBoolean(PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();
            
            MusicRemoteActivity.fire(MusicRemoteFullActivity.this, mAlbumId, mTrackId, mTitle, mArtist, mAlbum, mCover);
         }
      });*/

        TextView btnSimple = (TextView) findViewById(R.id.btnSimple);
        TextView btnFull = (TextView) findViewById(R.id.btnFull);
        btnSimple.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mBtnSimpleorFull.setSwitchState(false);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean(PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();

                MusicRemoteActivity.fire(MusicRemoteFullActivity.this, mAlbumId, mTrackId, mTitle, mArtist, mAlbum, mCover);
            }
        });
        btnFull.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mBtnSimpleorFull.setSwitchState(true);
            }
        });


        btnSimple.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mBtnSimpleorFull.setSwitchState(false);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putBoolean(PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();

                    MusicRemoteActivity.fire(MusicRemoteFullActivity.this, mAlbumId, mTrackId, mTitle, mArtist, mAlbum, mCover);
                }
                return true;
            }
        });
        btnFull.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mBtnSimpleorFull.setSwitchState(true);
                }
                return true;
            }
        });


        mBtnSimpleorFull.setOnSwitchListener(new OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                // TODO Auto-generated method stub
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean(PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL, Boolean.FALSE).commit();

                MusicRemoteActivity.fire(MusicRemoteFullActivity.this, mAlbumId, mTrackId, mTitle, mArtist, mAlbum, mCover);
            }
        });
    }

    /**
     * @param context
     */
    public static void fire(Context context, String albumId, String trackId, String title, String musicArtist, String musicAlbum, String musicCover) {
        Intent intent = new Intent(context, MusicRemoteFullActivity.class);
        intent.putExtra(MediaRemote.MUSIC_ALBUM_ID, albumId);
        intent.putExtra(MediaRemote.MUSIC_TRACK_ID, trackId);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(MediaRemote.MUSIC_ARTIST, musicArtist);
        intent.putExtra(MediaRemote.MUSIC_ALBUM, musicAlbum);
        intent.putExtra(MediaRemote.MUSIC_COVER, musicCover);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void getMusicAlbum(String musicAlbumId) {
        this.mEventBus.post(new GetMusicAlbumHandler(musicAlbumId, true).getRequest());
    }

    /**
     * @param getMusicAlbumHandler
     */
    public void onEvent(GetMusicAlbumHandler getMusicAlbumHandler) {
        this.mMusicAlbum = getMusicAlbumHandler.getMusicAlbum();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                build(mMusicAlbum);
            }
        });
    }

    /**
     * @param getMovieResponseEvent
     */
    public void build(MusicAlbum musicAlbum) {
        buildTracks(musicAlbum.getTracks());
    }

    private void buildTracks(MusicTrack[] tracks) {
        if (tracks != null) {
            int i = 1;
            for (MusicTrack musicTrack : tracks) {

                IPTTextView textViewmusicTrack = new IPTTextView(this);
                textViewmusicTrack.setText(i + ".  " + musicTrack.getTitle());
                textViewmusicTrack.setTextSize(16);
                textViewmusicTrack.setTextColor(Color.WHITE);
                textViewmusicTrack.setSingleLine(true);
                textViewmusicTrack.setEllipsize(TruncateAt.END);
                textViewmusicTrack.setTag(musicTrack);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                llp.setMargins(0, 40, 0, 0); // llp.setMargins(left, top, right,
                // bottom);
                textViewmusicTrack.setLayoutParams(llp);
                textViewmusicTrack.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MusicTrack musicTrack = (MusicTrack) v.getTag();
                        mEventBus.post(new PlayMusicTrackHandler(musicTrack.getId()).getRequest());
                        mTxtTitle.setText(getResources().getString(R.string.now_playing) + " " + musicTrack.getTitle());
                    }
                });
                layoutTracks.addView(textViewmusicTrack);
                i++;
            }
        }
    }


}
