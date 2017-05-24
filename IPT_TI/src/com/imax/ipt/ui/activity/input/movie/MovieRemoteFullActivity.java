package com.imax.ipt.ui.activity.input.movie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.movie.GetMovieHandler;
import com.imax.ipt.controller.eventbus.handler.push.MovieStartedEvent;
import com.imax.ipt.model.Genre;
import com.imax.ipt.model.Movie;
import com.imax.ipt.ui.activity.LobbyActivity;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.ImageUtil;
import com.imax.ipt.ui.util.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class MovieRemoteFullActivity extends MediaRemote implements OnClickListener {

    public static final String TAG = MovieRemoteFullActivity.class.getSimpleName();
    public static final String INPUT_ID = "INPUT_ID";
    public static final String PREFERENCE_DEFAULT_MOVIE_CONTROL_COMPLEXITY_FULL = "default_custom_movie_complexity_full";
    private ProgressDialog progress = null;
    public static boolean isShowPrepareProgress = false;

    private IPTTextView mTxtTitle;
    private IPTTextView mTxtGenre;
    private IPTTextView mTxtRating;
    private IPTTextView mTxtYear;
    private ImageView mCoverArt;
    private String mTitle;
    private String mCoverArtPath;
    private String mRating;
    private Genre[] mGenre;
    private int mYear;
    private String guid;
    private LoaderImage loaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_remote_full_v3);
//        this.addMenuFragment();
        this.init();

        if(isShowPrepareProgress){
            progress = new DialogMovieStarted(this);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            isShowPrepareProgress = false;
            stopShowPrepare();
        }

    }


    protected void init() {
        this.mode = FULL;

        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
        this.mTitle = getIntent().getStringExtra(MEDIA_TITLE);
        this.guid = getIntent().getStringExtra(MEDIA_ID);
        mEventBus.post(new GetMovieHandler(guid).getRequest());

        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        this.mTxtGenre = (IPTTextView) findViewById(R.id.txtGenre);
        this.mTxtRating = (IPTTextView) findViewById(R.id.txtRating);
        this.mTxtYear = (IPTTextView) findViewById(R.id.txtYear);
        this.mCoverArt = (ImageView) findViewById(R.id.imgPoster);

        this.mBtnAudioLanguage = (ImageButton) findViewById(R.id.btnAudioLanguage);
        this.mBtnAudioLanguage.setOnClickListener(this);

        this.mBtnSubtitles = (ImageButton) findViewById(R.id.btnSubtitles);
        this.mBtnSubtitles.setOnClickListener(this);

        this.mBtnPopUpMenu = (ImageButton) findViewById(R.id.btnPopUpMenu);
        this.mBtnPopUpMenu.setOnClickListener(this);

//        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btnPreviousMenu);
        this.mBtnPreviousMenu = (ImageButton) findViewById(R.id.btn_remote);
        this.mBtnPreviousMenu.setOnClickListener(this);

        this.mBtnRootMenu = (ImageButton) findViewById(R.id.btnMovieMenu);
        this.mBtnRootMenu.setOnClickListener(this);

//        /** remove <^> </>control bar
        this.txtOk = (IPTTextView) findViewById(R.id.txtOK);
        this.txtOk.setOnClickListener(this);

        this.mBtnOk2 = (Button) findViewById(R.id.btnOk2);
        mBtnOk2.setOnClickListener(this);
        ImageView imgOkBg = (ImageView)findViewById(R.id.imgOkBg);
        mBtnOk2.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        imgOkBg.setImageDrawable(getResources().getDrawable(R.drawable.remote_enter_active_icn));
                        break;
                    case MotionEvent.ACTION_UP:
                        imgOkBg.setImageDrawable(getResources().getDrawable(R.drawable.remote_enter_icn));
                        break;
                }
                return false;
            }
        });

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
        this.mBtnStop.setOnClickListener(this);

//        this.mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
//        this.mBtnPlay.setOnClickListener(this);

        this.mBtnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
        this.mBtnPlayPause.setVisibility(View.VISIBLE);
        this.mBtnPlayPause.setOnClickListener(this);


        this.mBtnPause = (ImageButton) findViewById(R.id.btnPause);
        this.mBtnPause.setOnClickListener(this);

        this.mBtnPrevious = (ImageButton) findViewById(R.id.btnPrev);
        this.mBtnPrevious.setVisibility(View.VISIBLE);
        this.mBtnPrevious.setOnClickListener(this);

        this.mBtnNext = (ImageButton) findViewById(R.id.btnNext);
        this.mBtnNext.setVisibility(View.VISIBLE);
        this.mBtnNext.setOnClickListener(this);

//        this.mBtnMultiview = (ImageButton) findViewById(R.id.btnMultiview);
//        this.mBtnMultiview.setOnClickListener(this);

        /* remove videomode */
//        this.mBtnVideoMode = (Button) findViewById(R.id.btnVideoMode);
//        this.mBtnVideoMode.setOnClickListener(this);

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieLibraryActivity.fireToFront(MovieRemoteFullActivity.this);
                finish();
            }
        });

        findViewById(R.id.btnMenuMaster).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LobbyActivity.fire(MovieRemoteFullActivity.this);
            }
        });

        ImageButton zoomButton = (ImageButton) findViewById(R.id.btnZoom);
        zoomButton.setOnClickListener(this);

    }

    /**
     * get MovieStartedEvent from control pc to stop show progress
     * @param movieStartedEvent
     */
    public void onEvent(MovieStartedEvent movieStartedEvent){
        if(progress != null) {
            progress.dismiss();
            isShowPrepareProgress = false;
        }
    }

    public void onEvent(GetMovieHandler handler){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Movie movie = handler.getMovie();
                mTxtGenre.setText(getGenre(movie.getGenre()));
                mTxtTitle.setText(getResources().getString(R.string.now_playing)  + mTitle);
                mTxtYear.setText(Integer.toString(movie.getYear()));
                try {
                    mTxtRating.setText(movie.getRating());
                } catch (IllegalStateException e) {
                    Log.w(TAG, "Caught exception: " + e.toString());
                }
                loaderImage = new LoaderImage();
                loaderImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movie.getCoverArtPath());

            }
        });
    }

    private String getGenre(Genre[] genres) {
        String[] options = new String[genres.length];
        int i = 0;
        for (Genre genre : genres) {
            options[i] = genre.getName();
            i++;
        }
        return Utils.join(",", options);
    }

    /**
     * stop show prepare to play movie progress incase of no MovieStartedEvent
     */
    private void stopShowPrepare(){
        Timer timer = new Timer();
        TimerTask countTimeTask = new TimerTask() {
            @Override
            public void run() {
                if(progress != null) {
                    progress.dismiss();
                    isShowPrepareProgress = false;
                }
            }
        };
        timer.schedule(countTimeTask,15*1000);//15s

    }

    private Bitmap createCircleImage(Bitmap source, int min)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * ����һ��ͬ����С�Ļ���
         */
        Canvas canvas = new Canvas(target);
        /**
         * ���Ȼ���Բ��
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * ʹ��SRC_IN���ο������˵��
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * ����ͼƬ
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * /**
     *
     * @param context
     */
    public static void fire(Context context, String guid, String title, int inputId) {
//    public static void fire(Context context, Movie movie, int inputId) {
        Intent intent = new Intent(context, MovieRemoteFullActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
        intent.putExtra(INPUT_ID, inputId);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("media",movie);
//        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    class LoaderImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return ImageUtil.getImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
//                    createCircleImage(result,100);

                    BitmapDrawable background = new BitmapDrawable(getResources(), createCircleImage(result, result.getWidth()));
//                    mCoverArt.setImageDrawable(background);// why?
                    mBtnOk2.setBackground(background);
                } catch (IllegalStateException e) {
                    // Exception wil be thrown if Fragment is no longer attached to the activity
                    Log.w(TAG, "Caught exception: " + e.toString());
                }
            }
        }

        private Bitmap createCircleImage(Bitmap source, int min)
        {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
            /**
             * ����һ��ͬ����С�Ļ���
             */
            Canvas canvas = new Canvas(target);
            /**
             * ���Ȼ���Բ��
             */
            canvas.drawCircle(min / 2, min / 2, min / 2, paint);
            /**
             * ʹ��SRC_IN���ο������˵��
             */
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            /**
             * ����ͼƬ
             */
            canvas.drawBitmap(source, 0, 0, paint);
            return target;
        }

    }

}

