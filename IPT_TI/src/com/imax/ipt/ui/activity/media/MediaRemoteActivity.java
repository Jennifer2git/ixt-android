package com.imax.ipt.ui.activity.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import com.imax.ipt.R;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.layout.IPTTextView;

public class MediaRemoteActivity extends MediaRemote {
    private ToggleButton btnSimpleorFull;
    private IPTTextView txtTitle;
    private String title;
    private String guid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_remote);
        this.addMenuFragment();
        this.init();
    }

    /**
     * @param context
     */
    public static void fire(Context context, String guid, String title) {
        Intent intent = new Intent(context, MediaRemoteActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected void init() {
        this.mode = SIMPLE;
//      this.initSimpleOrRemoteControl();
        this.title = getIntent().getStringExtra(MEDIA_TITLE);
        this.guid = getIntent().getStringExtra(MEDIA_ID);
        this.txtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        this.txtTitle.setText(getResources().getString(R.string.now_playing) + " " + title);

//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
        this.sendRequestGetVolume();

        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
        this.mBtnMute.setOnClickListener(this);
        this.sendRequestGetMuteVolume();

        this.mBtnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
        this.mBtnPlayPause.setOnClickListener(this);

        this.mBtnRew = (ImageButton) findViewById(R.id.btnRew);
        this.mBtnRew.setOnClickListener(this);

        this.mBtnFF = (ImageButton) findViewById(R.id.btnFF);
        this.mBtnFF.setOnClickListener(this);

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MovieLibraryActivity.fireToFront(MediaRemoteActivity.this);
            }
        });
    }

//   /**
//    * 
//    */
//   private void initSimpleOrRemoteControl()
//   {
//      btnSimpleorFull = (ToggleButton) findViewById(R.id.btnSimpleFull);
//      btnSimpleorFull.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//         @Override
//         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//         {
//            MovieRemoteFullActivity.fire(MediaRemoteActivity.this, guid, title);
//         }
//      });
//   }

}
