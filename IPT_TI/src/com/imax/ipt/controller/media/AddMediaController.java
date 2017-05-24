package com.imax.ipt.controller.media;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.eventbus.handler.movie.PlayMovieHandler;
import com.imax.ipt.controller.eventbus.handler.music.PlayMusicTrackHandler;
import com.imax.ipt.model.Movie;
import com.imax.ipt.ui.activity.media.AddMediaActivity;
import com.imax.ipt.ui.activity.media.AddMetaActivity;
import com.imax.iptevent.EventBus;

public class AddMediaController extends BaseController {
    public static final String TAG = "AddMediaController";

    //   private EventBus mEventBus;
    private AddMediaActivity mAddMediaActivity;
    private List<Movie> mMovies;

    public AddMediaController(AddMediaActivity mAddMediaActivity) {
        this.mAddMediaActivity = mAddMediaActivity;
//      this.mEventBus = getEventBus();
//      this.mEventBus.registerSticky(this);
    }

    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }

    /**
     * @param guid
     */
    public void playMovie() {
        mEventBus.post(new PlayMovieHandler(Handler.DEFAULT_GUID).getRequest());
    }

    public void playAlbum() {
        mEventBus.post(new PlayMusicTrackHandler(Handler.DEFAULT_GUID).getRequest());
    }


    /**
     *
     */
    public void addMetaActivity(boolean isMovieMetadata) {
//      this.mEventBus.postSticky(mMovies);
        AddMetaActivity.fire(mAddMediaActivity, (ArrayList<Movie>) mMovies, isMovieMetadata);
    }

//   /**
//    * 
//    * @param movie
//    */
//   public void onEvent(List<Movie> movies)
//   {
//      Log.d(TAG, "onEvent List<Movie>");
//      mMovies = movies;
//      
//      Movie mMovie = movies.size()!=0?movies.get(0):new Movie();      
//      this.mAddMediaActivity.updateMeta(mMovie.getTitle());
//
//   }

    public void setmMovies(List<Movie> mMovies) {
        this.mMovies = mMovies;

        Movie mMovie = mMovies.size() != 0 ? mMovies.get(0) : new Movie();
        this.mAddMediaActivity.updateMeta(mMovie.getTitle());
    }
}
