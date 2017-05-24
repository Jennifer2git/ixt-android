package com.imax.ipt.ui.activity.input.music;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.controller.inputs.MusicLibraryController;
import com.imax.ipt.model.Media;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.widget.gridview.TwoWayAbsListView;
import com.imax.ipt.ui.widget.gridview.TwoWayAbsListView.OnScrollListener;
import com.imax.ipt.ui.widget.gridview.TwoWayAdapterView;
import com.imax.ipt.ui.widget.gridview.TwoWayAdapterView.OnItemClickListener;
import com.imax.ipt.ui.widget.gridview.TwoWayGridWidget;

/**
 * @author rlopez
 */
public class MusicLibraryActivity extends BaseActivity implements OnItemClickListener, OnScrollListener, OnClickListener {
    private static final String TAG = "MusicLibraryActivity";
    private TwoWayGridWidget mGridView;
    private MusicLibraryController mMusicLibraryModel;
    private ImageButton mShareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_music_library);
        this.init();
        this.mMusicLibraryModel.setmCurrentEvent(MenuEvent.ALBUMS);

//      mMusicLibraryModel.getNowPlayingStatus();
    }

    @Override
    protected void onStart() {
        super.onStart();
//      this.setAutoCompleteEvent(MenuEvent.ALBUMS);
//      this.mMusicLibraryModel.setmCurrentEvent(MenuEvent.ALBUMS); 
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        mMusicLibraryModel.onActivityResume();
        // update now playing fragments
        mMusicLibraryModel.getNowPlayingStatus();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        mMusicLibraryModel.onActivityPause();
    }

    protected void init() {
        this.addMenuFragment();
        this.addPickerFragment(10, 10);
        this.addAutomplete(MenuEvent.ALBUMS);
        this.mMusicLibraryModel = new MusicLibraryController(this, mAutoCompleteFragment);

        this.initGrid();
        this.mMusicLibraryModel.init();

        this.mShareButton = (ImageButton) findViewById(R.id.shareButton);
        this.mShareButton.setOnClickListener(this);
        mShareButton.setVisibility(View.INVISIBLE);
    }

    /**
     * @param context
     */
    public static void fire(Context context) {
        Intent intent = new Intent(context, MusicLibraryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    /**
     *
     */
    private void initGrid() {
        mGridView = (TwoWayGridWidget) findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollListener(this);
        mGridView.setSelector(getResources().getDrawable(color.transparent));
    }

    private void showDialogMediaDescription(int position) {
        Media media = mMusicLibraryModel.getmMedias().get(position);
        if (media != null) {
            Bundle bundle = null;
            FragmentManager fm = getSupportFragmentManager();

            if (media.getMediaType() == null) {
                return;
            }
            switch (media.getMediaType()) {
                case ALBUMS:
                    bundle = new Bundle();
                    DialogMusicFragment mDialogMusicFragment = new DialogMusicFragment();
                    bundle.putString(DialogMusicFragment.MUSIC_ALBUM_ID, mMusicLibraryModel.getmMedias().get(position).getId());
                    mDialogMusicFragment.setArguments(bundle);
                    mDialogMusicFragment.show(fm, "fragment_music_dialog");
                    break;
                case ARTIST:
                    bundle = new Bundle();
                    bundle.putString(DialogMusicArtistFragment.ARTIST_ID, media.getId());
                    bundle.putString(DialogMusicArtistFragment.ARTIST_NAME, media.getTitle());
                    DialogMusicArtistFragment dialogMusicArtistFragment = new DialogMusicArtistFragment();
                    dialogMusicArtistFragment.setArguments(bundle);
                    dialogMusicArtistFragment.show(fm, "fragment_artist_dialog");
                    break;
                default:
                    Log.d(TAG, "Media type not handle" + media.getMediaType());
                    break;
            }
        }
    }

    public void addAutomplete(MenuEvent menuEvent) {
        this.addAutoCompleteFragment(menuEvent);
    }

    /**
     *
     */
    @Override
    public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
        if (position >= mMusicLibraryModel.getDummyCount() &&
                !mMusicLibraryModel.getmMedias().get(position).isLoading()) {
            showDialogMediaDescription(position);
        }
    }

    /**
     * @param index
     */
    public void onScroll(final int index) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "smoothScrollToPosition index" + index);
                //mGridView.setSelection((index - mMusicLibraryModel.getDummyCount()));
                mGridView.setSelection(index);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mMusicLibraryModel.onDestroy();
    }

    @Override
    public void onScrollStateChanged(TwoWayAbsListView view, int scrollState) {
        int position = view.getFirstVisiblePosition();
        if (scrollState == SCROLL_STATE_IDLE) {
            mMusicLibraryModel.displayOnScreen(position);
        }
    }

    @Override
    public void onScroll(TwoWayAbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//      firstVisibleItem = firstVisibleItem - mMusicLibraryModel.getDummyCount();
//      if (firstVisibleItem < 0)
//         firstVisibleItem = 0;

        mMusicLibraryModel.loadView(firstVisibleItem, visibleItemCount);
    }

    /**
     * Getters and setters
     ***/
    public TwoWayGridWidget getmGridView() {
        return mGridView;
    }

    public void setShareButton(final boolean shareButton) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (shareButton) {
//               mShareButton.setVisibility(View.VISIBLE);
                    mShareButton.setVisibility(View.INVISIBLE);
                } else {
                    mShareButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareButton:
                mMusicLibraryModel.screenShare();
                break;
        }
    }
}
