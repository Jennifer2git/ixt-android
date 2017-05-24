package com.imax.ipt.ui.activity.input.tv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.imax.ipt.R;
import com.imax.ipt.ui.layout.IPTTextView;

public class TVRemoteActivity extends IRControl {
    public static final String TITLE = "TITLE";

    protected Switch btnSimpleorFull;
    protected ImageButton mBtnTvFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_remote);
        this.mode = SIMPLE;
        this.gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        this.mPrevCh = (Button) findViewById(R.id.btnPrevCh);
        this.mPrevCh.setOnClickListener(this);
//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
        this.sendRequestGetVolume();

        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
        this.mBtnMute.setOnClickListener(this);
        this.sendRequestGetMuteVolume();

        this.mBtnTVChDown = (ImageButton) findViewById(R.id.btnTvChDown);
        this.mBtnTVChDown.setOnClickListener(this);

        this.mBtnTVChUp = (ImageButton) findViewById(R.id.btnTvChUp);
        this.mBtnTVChUp.setOnClickListener(this);

        this.mBtnTvFav = (ImageButton) findViewById(R.id.btnTvFav);
        this.mBtnTvFav.setOnClickListener(this);

        this.txtTitle = (IPTTextView) findViewById(R.id.txtTitle);

        setGridListener(this);

        this.addMenuFragment();
//      this.init();

        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
        this.mTitle = getIntent().getStringExtra(TITLE);
        this.txtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle);

        btnSimpleorFull = (Switch) findViewById(R.id.btnSimpleFull);
        this.initSimpleOrRemoteControl();

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                // MovieLibraryActivity.fireToFront(TVRemoteActivity.this);
            }
        });

    }

    /**
     * @param context
     */
    public static void fire(Context context, int inputId, String title) {
        Intent intent = new Intent(context, TVRemoteActivity.class);
        intent.putExtra(INPUT_ID, inputId);
        intent.putExtra(TITLE, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

//   protected void init()
//   {
//      this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
//      this.mTitle = getIntent().getStringExtra(TITLE);
//      this.txtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle);
//      this.initSimpleOrRemoteControl();
//   }

    /**
     *
     */
    private void initSimpleOrRemoteControl() {
        btnSimpleorFull.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean(PREFERENCE_DEFAULT_CUSTOM_INPUT_CONTROL_COMPLEXITY_FULL, Boolean.TRUE).commit();
                TVRemoteFullActivity.fire(TVRemoteActivity.this, inputId, mTitle);
            }
        });
    }
}
