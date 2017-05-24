package com.imax.ipt.ui.activity.media;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.media.AddMetaController;
import com.imax.ipt.model.Genre;
import com.imax.ipt.model.Movie;
import com.imax.ipt.model.Person;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.activity.input.music.MusicLibraryActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rodrigo Lopez Ramos
 */
public class AddMetaActivity extends BaseActivity implements OnClickListener {
    public static String TAG = "AddMetaActivity";

    private static final String KEY_METADATA = "metadata";
    private static final String KEY_IS_MOVIE_METADATA = "isMovieMetadata";

    //   private AddMetaAdapter mMetaAdapter;
    private AddMetaController controller;

    private ImageView mImgPoster;
    private LoaderImage loaderImage;

    private EditText txtTitle;
    private EditText txtRating;
    private EditText txtYear;
    private EditText txtSynopsis;
    private EditText txtGenres;
    private EditText txtDirectors;
    private EditText txtActors;
    private EditText txtRunningTime;

    private IPTTextView lblTitle;
    private IPTTextView lblYear;
    private IPTTextView lblGenres;
    private IPTTextView lblRunningTime;
    private IPTTextView lblRating;
    private IPTTextView lblSynopsis;
    private IPTTextView lblActors;
    private IPTTextView lblDirectors;

    private Button btnBack;
    private Button btnPrevious;
    private Button btnNext;
    private Button btnLoad;

    private ArrayList<Movie> metadatas;
    private int selectedIndex;

    private boolean isMovieMetadata;
//    private boolean isRepeatSend = false;

    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media_library);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            metadatas = extras.getParcelableArrayList(KEY_METADATA);
            isMovieMetadata = extras.getBoolean(KEY_IS_MOVIE_METADATA);
            //mAddMediaController.setmMovies(metadatas);
//         setupFragments(metadatas);
        }

        controller = new AddMetaController(this);

        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtRating = (EditText) findViewById(R.id.txtRating);
        txtYear = (EditText) findViewById(R.id.txtYear);
        txtSynopsis = (EditText) findViewById(R.id.txtSynopsis);
        txtGenres = (EditText) findViewById(R.id.txtGenres);
        txtDirectors = (EditText) findViewById(R.id.txtDirectors);
        txtActors = (EditText) findViewById(R.id.txtActors);
        txtRunningTime = (EditText) findViewById(R.id.txtRunningTime);

        mImgPoster = (ImageView) findViewById(R.id.imgPoster);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setEnabled(true);
//        isRepeatSend = true;
        btnLoad.setOnClickListener(this);
//      this.init();

        if (!isMovieMetadata) {
            txtTitle.setVisibility(View.GONE);
            txtRating.setVisibility(View.GONE);
            txtYear.setVisibility(View.GONE);
            txtSynopsis.setVisibility(View.GONE);
            txtGenres.setVisibility(View.GONE);
            txtDirectors.setVisibility(View.GONE);
            txtActors.setVisibility(View.GONE);
            txtRunningTime.setVisibility(View.GONE);

            lblTitle = (IPTTextView) findViewById(R.id.lblTitle);
            lblYear = (IPTTextView) findViewById(R.id.lblYear);
            lblGenres = (IPTTextView) findViewById(R.id.lblGenres);
            lblRunningTime = (IPTTextView) findViewById(R.id.lblRunningTime);
            lblRating = (IPTTextView) findViewById(R.id.lblRating);
            lblSynopsis = (IPTTextView) findViewById(R.id.lblSynopsis);
            lblActors = (IPTTextView) findViewById(R.id.lblActors);
            lblDirectors = (IPTTextView) findViewById(R.id.lblDirectors);

            lblYear.setVisibility(View.GONE);
            lblGenres.setVisibility(View.GONE);
            lblRunningTime.setVisibility(View.GONE);
            lblRating.setVisibility(View.GONE);
            lblSynopsis.setVisibility(View.GONE);
            lblDirectors.setVisibility(View.GONE);
            lblActors.setVisibility(View.GONE);
        }

//        if (metadatas.size() > 0) {
//            Log.d(TAG, "call DisplayMetadata:" + metadatas.size());
//            DisplayMetadata(0);
//        } else {
//            Log.d(TAG, "metadatas.size <0,");
//        }

    }

    private void DisplayMetadata(int index) {
        txtTitle.setText(metadatas.get(index).getTitle());
        txtRating.setText(metadatas.get(index).getRating());
        txtYear.setText(Integer.toString(metadatas.get(index).getYear()));
        txtSynopsis.setText(metadatas.get(index).getSynopsis());

        StringBuilder genres = new StringBuilder();
        for (Genre genre : metadatas.get(index).getGenre()) {
            genres.append(genre.getName() + "\n");
        }
        String genresString = genres.toString();
        if (genresString.length() > 1)
            genresString = genresString.substring(0, genresString.length() - 1);
        txtGenres.setText(genresString);

        StringBuilder directors = new StringBuilder();
        for (Person person : metadatas.get(index).getDirectors()) {
            directors.append(person.fullName() + "\n");
        }
        String directorsString = directors.toString();
        if (directorsString.length() > 1)
            directorsString = directorsString.substring(0, directorsString.length() - 1);
        txtDirectors.setText(directorsString);

        StringBuilder actors = new StringBuilder();
        for (Person person : metadatas.get(index).getActors()) {
            actors.append(person.fullName() + "\n");
        }
        String actorsString = actors.toString();
        if (actorsString.length() > 1)
            actorsString = actorsString.substring(0, actorsString.length() - 1);
        txtActors.setText(actorsString);
        txtRunningTime.setText(Integer.toString(metadatas.get(index).getTime()));

        if (!isMovieMetadata) {
            // music metadata
            lblTitle.setText(getString(R.string.title) + " " + metadatas.get(index).getTitle());
        }

        loaderImage = new LoaderImage();
//      loaderImage.execute(metadatas.get(index).getCoverArtPath());
        loaderImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, metadatas.get(index).getCoverArtPath());

        btnPrevious.setEnabled(true);
        btnNext.setEnabled(true);
        if (index == 0) {
            btnPrevious.setEnabled(false);
        }
        if (index == (metadatas.size() - 1)) {
            btnNext.setEnabled(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        isRepeatSend = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
    }

    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex < 0 || selectedIndex >= metadatas.size())
            return;

        // save any changes made to the current editText
        updateCurrentChanges(this.selectedIndex);

        this.selectedIndex = selectedIndex;

        DisplayMetadata(selectedIndex);
    }

    private void updateCurrentChanges(int index) {
        Movie metadata = metadatas.get(index);

        metadata.setTitle(txtTitle.getText().toString());
        metadata.setYear(Integer.parseInt(txtYear.getText().toString()));
        metadata.setTime(Integer.parseInt(txtRunningTime.getText().toString()));

        String eol = System.getProperty("line.separator");
        String[] genreStrings = txtGenres.getText().toString().split(eol);
        Genre[] genres = new Genre[genreStrings.length];
        for (int i = 0; i < genres.length; i++) {
            genres[i] = new Genre(0, genreStrings[i]);
        }
        metadata.setGenre(genres);

        metadata.setRating(txtRating.getText().toString());
        metadata.setSynopsis(txtSynopsis.getText().toString());

        String[] actorStrings = txtActors.getText().toString().split(eol);
        Log.d(TAG,"actorStrings from ui:" + txtActors.getText().toString());
        List<Person> actors = new ArrayList<Person>();
        for (int i = 0; i < actorStrings.length; i++) {
            String actorString = actorStrings[i].trim();
            if (actorString.length() > 0) {
                Person actor = new Person();
                actor.setId(Handler.DEFAULT_GUID);
                actor.setNameByFullName(actorString);
                actor.setCoverArtPaths(new String[0]);
                actors.add(actor);
            }
        }
        Person[] actorArray = new Person[actors.size()];
        actors.toArray(actorArray);
        metadata.setActors(actorArray);

        String[] directorStrings = txtDirectors.getText().toString().split(eol);
        List<Person> directors = new ArrayList<Person>();
        for (int i = 0; i < directorStrings.length; i++) {
            String directorString = directorStrings[i].trim();
            if (directorString.length() > 0) {
                Person director = new Person();
                director.setId(Handler.DEFAULT_GUID);
                director.setNameByFullName(directorString);
                director.setCoverArtPaths(new String[0]);
                directors.add(director);
            }
        }
        Person[] directorArray = new Person[directors.size()];
        directors.toArray(directorArray);
        metadata.setDirectors(directorArray);
        Log.d(TAG,"update metadata is :" + metadata);// why insert to metadatas?
    }

    /**
     * /**
     *
     * @param context
     */
    public static void fire(Context context, ArrayList<Movie> metadatas, boolean isMovieMetadata) {
        Intent intent = new Intent(context, AddMetaActivity.class);
        intent.putParcelableArrayListExtra(KEY_METADATA, metadatas);
        intent.putExtra(KEY_IS_MOVIE_METADATA, isMovieMetadata);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
/*
   *//***
     *
     *//*
   private void init()
   {
      ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
      pager.setAdapter(mMetaAdapter);
   }

   */

    /***
     *
     *//*
   public void setupFragments(final List<Movie> mMovies)
   {
      runOnUiThread(new Runnable() 
      {
         @Override
         public void run()
         {
            ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        
            List<Fragment> fragments = new ArrayList<Fragment>();
            
            for (Movie movie : mMovies)
            {
               MetaFragment metaFragment = new MetaFragment();
               metaFragment.setmMovie(movie);
               fragments.add(metaFragment);
            }
            mMetaAdapter = new AddMetaAdapter(getSupportFragmentManager(), fragments);
            pager.setAdapter(mMetaAdapter);
            
         }
      });
   }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoad:
                // disable the load button so that a user cannot press multiple times before the results come back
//                btnLoad.setEnabled(false);
                updateCurrentChanges(selectedIndex);
//                sendMovieMetaThread.start();
                controller.loadMedia(metadatas.get(selectedIndex), 0, false);
                break;

            case R.id.btnPrevious:
                setSelectedIndex(selectedIndex - 1);
                break;

            case R.id.btnNext:
                setSelectedIndex(selectedIndex + 1);
                break;

            case R.id.btnBack:
                finish();
                break;
        }
    }

    private Thread sendMovieMetaThread = new Thread(){
        @Override
        public void run() {
            for(int i=0; i<8; i++) {
                try {
//                    if(!isRepeatSend) {
//                        break;
//                    }
                    controller.loadMedia(metadatas.get(selectedIndex), 0, false);
                    Log.d(TAG, "SEND COMMAND TO CONTROL PC + " + i);
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void loadMediaAndStopPlayingMedia() {
        controller.loadMedia(metadatas.get(selectedIndex), 0, true);
    }

    public void backToMediaActivity() {
        if (isMovieMetadata) {
            // if loading movie, go to movie browsing screen
            MovieLibraryActivity.fireToFront(this);
        } else {
            // if loading music, go to music browsing screen
            MusicLibraryActivity.fire(this);
        }
    }

    public void displayLoadInProgressDialog() {
        LoadingInProgressDialogFragment dialog = new LoadingInProgressDialogFragment();
        dialog.show(getFragmentManager(), "LoadingInProgressDialogFragment");
    }

    public void displayMediaPlayingInProgressDialog() {
        MediaPlayingInProgressDialogFragment dialog = new MediaPlayingInProgressDialogFragment();
        dialog.show(getFragmentManager(), "MediaPlayingInProgressDialogFragment");
    }

    public void displayNoDiscDetectedDialog() {
        NoDiscDetectedDialogFragment dialog = new NoDiscDetectedDialogFragment();
        dialog.show(getFragmentManager(), "NoDiscDetectedDialogFragment");
    }

    public static class LoadingInProgressDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(getString(R.string.add_media_please_try_again_later)).setTitle(getString(R.string.add_media_loading_in_progres))
                    .setPositiveButton(getString(R.string.response_ok), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // no action needed, user should wait or press the Back button on page to proceed
                        }
                    });

            return builder.create();
        }
    }

    public static class MediaPlayingInProgressDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(getString(R.string.add_media_stop_playing_and_start_loading)).setTitle(getString(R.string.add_media_media_playing_in_progress))
                    .setPositiveButton(getString(R.string.response_yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // stop the current media and begins loading
                            ((AddMetaActivity) getActivity()).loadMediaAndStopPlayingMedia();
                        }
                    }).setNegativeButton(getString(R.string.response_no), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // no action needed
                }
            });

            return builder.create();
        }
    }

    public static class NoDiscDetectedDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(getString(R.string.add_media_check_disc_drive)).setTitle(getString(R.string.add_media_no_disc_detected))
                    .setPositiveButton(getString(R.string.response_ok), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });

            return builder.create();
        }
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
                    BitmapDrawable background = new BitmapDrawable(getResources(), result);
                    mImgPoster.setImageDrawable(background);
                } catch (IllegalStateException e) {
                    // Exception wil be thrown if Fragment is no longer attached to the activity
                    Log.w(TAG, "Caught exception: " + e.toString());
                }
            }
        }

    }
}
