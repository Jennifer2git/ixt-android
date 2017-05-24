package com.imax.ipt.ui.activity.media;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.media.AddMediaController;
import com.imax.ipt.model.Movie;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.activity.input.movie.MovieRemoteFullActivity;
import com.imax.ipt.ui.activity.input.music.MusicRemoteActivity;
import com.imax.ipt.ui.activity.input.music.MusicRemoteFullActivity;
import com.imax.ipt.ui.activity.input.tv.IRControl;
import com.imax.ipt.ui.layout.IPTTextView;

import java.util.ArrayList;

public class AddMediaActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = AddMediaActivity.class.getSimpleName();
    public static final String KEY_METADATA_MOVIE = "movieMetadata";
    public static final String KEY_METADATA_MUSIC_ALBUM_TILES = "musicAlbumTitlesMetadata";
    public static final String KEY_METADATA_MUSIC_ALBUM_COVERARTPATHS = "musicAlbumCoverArtPathsMetadata";

    private AddMediaController mAddMediaController;
    private ImageButton mBtnWatchMedia;
    private ImageButton mBtnAddMediaToLibrary;
    private ImageButton mBtnDoNothing;
    private IPTTextView mTxtTitle;

    private IPTTextView mTxtPlayMedia;
    private IPTTextView mTxtAddMedia;
    private ImageView mImgMediaTypeIcon;

    private Boolean isMovieDisc = true;
    ;
    private String coverArtPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_media);
        this.mAddMediaController = new AddMediaController(this);
        this.mBtnWatchMedia = (ImageButton) findViewById(R.id.btnPlayMedia);
        this.mBtnWatchMedia.setOnClickListener(this);
        this.mBtnAddMediaToLibrary = (ImageButton) findViewById(R.id.btnAddMediaToLibrary);
        this.mBtnAddMediaToLibrary.setOnClickListener(this);
        this.mBtnDoNothing = (ImageButton) findViewById(R.id.btnDoNothing);
        this.mBtnDoNothing.setOnClickListener(this);
        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitleMeta);
        mTxtPlayMedia = (IPTTextView) findViewById(R.id.txtPlayMedia);
        mTxtAddMedia = (IPTTextView) findViewById(R.id.txtAddMedia);
        mImgMediaTypeIcon = (ImageView) findViewById(R.id.imgMediaTypeIcon);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(KEY_METADATA_MOVIE)) {
                ArrayList<Movie> metadatas = extras.getParcelableArrayList(KEY_METADATA_MOVIE);
                mAddMediaController.setmMovies(metadatas);

                if (metadatas.size() > 0)
                    coverArtPath = metadatas.get(0).getCoverArtPath();
            } else {
                isMovieDisc = false;
                mTxtPlayMedia.setText(getString(R.string.play_album));
                mTxtAddMedia.setText(getString(R.string.add_music_album_library));
                mImgMediaTypeIcon.setImageResource(R.drawable.ipt_gui_asset_menu_input_music_btn_inactive);

                // music metadata available
                String[] musicAlbumTitles = extras.getStringArray(KEY_METADATA_MUSIC_ALBUM_TILES);
                String[] musicAlbumCoverArtPaths = extras.getStringArray(KEY_METADATA_MUSIC_ALBUM_COVERARTPATHS);

                ArrayList<Movie> metadatas = new ArrayList<Movie>();
                for (int i = 0; i < musicAlbumTitles.length; i++) {
                    Movie movie = new Movie();
                    movie.setTitle(musicAlbumTitles[i]);
                    movie.setCoverArtPath(musicAlbumCoverArtPaths[i]);
                    metadatas.add(movie);
                }
                if (metadatas.size() == 0) {
                    // add a dummy movie object if there is no metadata available
                    metadatas.add(new Movie());
                }
                mAddMediaController.setmMovies(metadatas);

                if (musicAlbumCoverArtPaths.length > 0)
                    coverArtPath = musicAlbumCoverArtPaths[0];
            }
        }
    }

    public void updateMeta(final String title) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTxtTitle = (IPTTextView) findViewById(R.id.txtTitleMeta);
                mTxtTitle.setText(title);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddMediaController.onDestroy();
    }

    /**
     * @param context
     */
    public static void fire(Context context, ArrayList<Movie> metadatas) {
        Log.d(TAG, "add movie fire.");
        Intent intent = new Intent(context, AddMediaActivity.class);
        intent.putParcelableArrayListExtra(KEY_METADATA_MOVIE, metadatas);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void fire(Context context, String[] albumTitles, String[] coverArtPaths) {
        Intent intent = new Intent(context, AddMediaActivity.class);
        intent.putExtra(KEY_METADATA_MUSIC_ALBUM_TILES, albumTitles);
        intent.putExtra(KEY_METADATA_MUSIC_ALBUM_COVERARTPATHS, coverArtPaths);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlayMedia:
                if (isMovieDisc) {
                    mAddMediaController.playMovie();

//            Map<FactoryDeviceTypeDrawable.DeviceKind, Vector<Input>> inputsByDeviceKind = (Map<FactoryDeviceTypeDrawable.DeviceKind, Vector<Input>>)IPT.getInstance().getIPTContext().get(IPT.INPUTS_BY_DEVICE_KIND);
//            int inputId = inputsByDeviceKind.get(FactoryDeviceTypeDrawable.DeviceKind.Movie).get(0).getId();
                    int inputId = 2;
//                    Movie movie = new Movie();
//                    movie.setTitle("Playing from DVD");
//                    movie.setId(Handler.DEFAULT_GUID);
//                    MovieRemoteFullActivity.fire(getApplicationContext(), movie, inputId);
                    MovieRemoteFullActivity.fire(getApplicationContext(), Handler.DEFAULT_GUID, "Playing from DVD", inputId);
                } else {
                    mAddMediaController.playAlbum();

                    // kh: To be fixed ...
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    boolean isControlComplexityFull = prefs.getBoolean(IRControl.PREFERENCE_DEFAULT_CUSTOM_INPUT_CONTROL_COMPLEXITY_FULL, Boolean.FALSE);

                    if (isControlComplexityFull) {
                        MusicRemoteFullActivity.fire(getApplicationContext(), Handler.DEFAULT_GUID, Handler.DEFAULT_GUID, "", "", mTxtTitle.getText().toString(), coverArtPath);
                    } else {
                        MusicRemoteActivity.fire(getApplicationContext(), Handler.DEFAULT_GUID, Handler.DEFAULT_GUID, "", "", mTxtTitle.getText().toString(), coverArtPath);
                    }
                }
                break;
            case R.id.btnAddMediaToLibrary:
                mAddMediaController.addMetaActivity(isMovieDisc);
                break;
            case R.id.btnDoNothing:
                finish();
                break;
        }
    }
}
