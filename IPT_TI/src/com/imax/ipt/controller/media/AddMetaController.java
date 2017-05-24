package com.imax.ipt.controller.media;

import android.util.Log;
import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.media.LoadMediaHandler;
import com.imax.ipt.model.Movie;
import com.imax.ipt.ui.activity.media.AddMetaActivity;

public class AddMetaController extends BaseController {

    private static final String TAG = AddMetaController.class.getSimpleName();
    private AddMetaActivity mAddMetaActivity;
//   private EventBus  mEventBus;

    /**
     *
     */
    public AddMetaController(AddMetaActivity metaActivity) {
        this.mAddMetaActivity = metaActivity;
//      this.mEventBus        = getEventBus();
//      this.mEventBus.registerSticky(this);

        mEventBus.register(this);
    }

    /**
     * @param movie
     */
    public void loadMedia(Movie movie, int mSelectedMetadataIndex, boolean stopPlayingMedia) {
        Log.d(TAG, " start to load movie: " + movie.getTitle());
        movie.setTitle(movie.getTitle());// why here set like this?
        mEventBus.post(new LoadMediaHandler(movie, mSelectedMetadataIndex, (stopPlayingMedia ? 1 : 0)).getRequest());
    }

    public void onEvent(LoadMediaHandler loadMediaHandler) {
        Log.d(TAG, "onEvent " + loadMediaHandler.getErrorCode());
        switch (loadMediaHandler.getErrorCode()) {
            case 0:
                // loading successfully started
                mAddMetaActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAddMetaActivity.backToMediaActivity();
                    }
                });

                break;

            case 1:
                // loading already in progress
                mAddMetaActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAddMetaActivity.displayLoadInProgressDialog();
                    }
                });
                break;

            case 2:
                // Unload of current playing media required
                mAddMetaActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAddMetaActivity.displayMediaPlayingInProgressDialog();
                    }
                });
                break;

            case 3:
                // no disc currently detected in the disc drive
                mAddMetaActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAddMetaActivity.displayNoDiscDetectedDialog();
                    }
                });
                break;
        }
    }

//   /**
//    * 
//    * @param movie
//    */
//   public void onEvent(List<Movie> movies)
//   {
//      mAddMetaActivity.setupFragments(movies);
//   }


    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }
}
