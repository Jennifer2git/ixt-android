package com.imax.ipt.ui.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.movie.GetAutoCompleteActorsHandler;
import com.imax.ipt.controller.eventbus.handler.movie.GetAutoCompleteMovieLiteHandler;
import com.imax.ipt.controller.eventbus.handler.movie.GetMovieAutoCompleteDirectorsHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicArtistsAutoCompleteHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicAlbumsAutoCompleteHandler;
import com.imax.ipt.controller.eventbus.handler.ui.AutoCompleteEvent;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.model.Media;
import com.imax.ipt.model.MovieLite;
import com.imax.ipt.model.MusicAlbum;
import com.imax.ipt.model.Person;
import com.imax.iptevent.EventBus;

public class AutoCompleteFragment extends Fragment {
    public static final String TAG = "AutoCompleteFragment";

    public static final String MENU_EVENT_ID = "MENU_EVENT_ID";

    private AutoCompleteTextView mAutoCompleteIPTTextView;
    private MenuEvent mMenuEvent;
    private EventBus mEventBus;
    private int genreId = 0;
    private Integer[] years = {};
    private Integer favorite = 0;

    private String mHint;
    private int orderBy = 0;


    private ArrayList<Media> mMedias = new ArrayList<Media>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mAutocompleteLayout = inflater.inflate(R.layout.fragment_autocomplete, null);
        this.mAutoCompleteIPTTextView = (AutoCompleteTextView) mAutocompleteLayout.findViewById(R.id.txtSearch);
        mAutoCompleteIPTTextView.setThreshold(1);

        //Hide OS Keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAutoCompleteIPTTextView.getWindowToken(), 0);

//      adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
//      mAutoCompleteIPTTextView.setAdapter(adapter);
        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);
        this.init();
        return mAutocompleteLayout;
    }

    private void init() {
        int ordinal = getArguments().getInt(MENU_EVENT_ID);
        mMenuEvent = MenuEvent.valueOf(ordinal);

        this.mAutoCompleteIPTTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do not search on empty string
                if (s != null & s.length() > 0)
                    search(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mAutoCompleteIPTTextView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                String searchString = new String();
                Media mMedia = mMedias.get(pos);
                if (mMedia instanceof MovieLite) {
                    searchString = mMedia.getTitle();
                } else if (mMedia instanceof Person) {
                    searchString = ((Person) mMedia).fullName();
                } else if (mMedia instanceof MusicAlbum) {
                    searchString = ((MusicAlbum) mMedia).getTitle();
                }
                mEventBus.post(new AutoCompleteEvent(searchString, mMenuEvent));
            }

        });
        this.setHint();


        mAutoCompleteIPTTextView.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mEventBus.post(new AutoCompleteEvent(v.getText().toString(), mMenuEvent));

                    InputMethodManager imm =
                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    mAutoCompleteIPTTextView.clearFocus();

                    handled = true;
                }

                return handled;
            }
        });
    }

    /**
     * @param search
     */
    private void search(String search) {
        Log.d(TAG, search);
        if (search == null) {
            return;
        }

        switch (mMenuEvent) {
            case TITLES:
                getMovieLite(search);
                break;
            case DIRECTORS:
                getDirectors(search);
                break;
            case ACTORS:
                getActors(search);
                break;
            case ALBUMS:
                getMusicAlbums(search);
                break;
            case ARTISTS:
                getMusicArtist(search);
                break;
            case FAVOURITES_MOVIE:
                getMovieLite(search);
                break;
            case GENRES_MOVIE:
                getMovieLite(search);
                break;
            case GENRES_MUSIC:
            case FAVOURITES_MUSIC:
//         getMovieLite(search);
                getMusicAlbums(search);
                break;
            case SHOW_GENRES:
                break;
            case YEARS:
                this.orderBy = 1;
                getMovieLite(search);
                break;
            default:
                Log.d(TAG, "Default value");
                break;
        }
    }

    /**
     *
     */
    private void setHint() {
        mAutoCompleteIPTTextView.setText("");
        switch (getmMenuEvent()) {
            case TITLES:
                this.mHint = getResources().getString(R.string.search_titles);
                break;
            case DIRECTORS:
                this.mHint = getResources().getString(R.string.search_directors);
                break;
            case ACTORS:
                this.mHint = getResources().getString(R.string.search_actors);
                break;
            case ALBUMS:
                this.mHint = getResources().getString(R.string.search_albums);
                break;
            case ARTISTS:
                this.mHint = getResources().getString(R.string.search_artist);
                break;
            case FAVOURITES_MUSIC:
            case FAVOURITES_MOVIE:
                this.mHint = getResources().getString(R.string.search_favourites);
                break;
            case SHOW_GENRES:
            case GENRES_MUSIC:
            case GENRES_MOVIE:
                this.mHint = getResources().getString(R.string.search_genres);
                break;

            case SETTINGS:
                this.mHint = getResources().getString(R.string.search_settings);
                break;
            case YEARS:
                this.mHint = getResources().getString(R.string.search_years);
                break;

            default:
                Log.d(TAG, "Default Case ");
                break;
        }
        mAutoCompleteIPTTextView.setHint(mHint);
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mEventBus.unregister(this);
    }

    /**
     * Event Response to get movies listing
     *
     * @param getMovieLiteEvent
     */

    private void setAutoCompleteOptions() {
        ArrayList<String> mOptions = new ArrayList<String>();
        for (Media media : mMedias) {
            mOptions.add(media.getTitle());
        }

        if (getActivity() == null)
            return;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mOptions);
        mAutoCompleteIPTTextView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    /**
     * @param menuMovieLibraryEvent
     */
    public void onEvent(final MediaMenuLibraryEvent menuMovieLibraryEvent) {
        this.mMenuEvent = menuMovieLibraryEvent.getEvent();
        this.setHint();

    }

    /****
     * Server interaction
     *******/
    private void getActors(String searchString) {
        GetAutoCompleteActorsHandler getAutoCompleteActorsHandler = new GetAutoCompleteActorsHandler();
        getAutoCompleteActorsHandler.setStartIndex(0);
        getAutoCompleteActorsHandler.setCount(20);
        getAutoCompleteActorsHandler.setSearchStringFirstName(searchString);
        getAutoCompleteActorsHandler.setSearchStringLastName(searchString);
        this.mEventBus.post(getAutoCompleteActorsHandler.getRequest());
    }

    /**
     * @param searchString
     */
    private void getDirectors(String searchString) {
        GetMovieAutoCompleteDirectorsHandler getMovieDirectorsHandler = new GetMovieAutoCompleteDirectorsHandler();
        getMovieDirectorsHandler.setStartIndex(0);
        getMovieDirectorsHandler.setCount(20);
        getMovieDirectorsHandler.setSearchStringFirstName(searchString);
        getMovieDirectorsHandler.setSearchStringLastName(searchString);
        getMovieDirectorsHandler.setAutoComplete(true);
        this.mEventBus.post(getMovieDirectorsHandler.getRequest());
    }

    /**
     * @param searchString
     */
    private void getMovieLite(String searchString) {
        GetAutoCompleteMovieLiteHandler getMovieLiteEvent = new GetAutoCompleteMovieLiteHandler();
        getMovieLiteEvent.setStartIndex(0);
        getMovieLiteEvent.setCount(20);
        getMovieLiteEvent.setSearchString(searchString);
        getMovieLiteEvent.setGenreId(genreId);
        getMovieLiteEvent.setYears(years);
        getMovieLiteEvent.setFavorite(favorite);
        getMovieLiteEvent.setOrderByOptions(orderBy);
        this.mEventBus.post(getMovieLiteEvent.getRequest());
    }

    /**
     * @param searchString
     */
    private void getMusicAlbums(String searchString) {
        GetMusicAlbumsAutoCompleteHandler getMusicAlbumsHandler = new GetMusicAlbumsAutoCompleteHandler();
        getMusicAlbumsHandler.setStartIndex(0);
        getMusicAlbumsHandler.setCount(20);
        getMusicAlbumsHandler.setSearchString(searchString);
        getMusicAlbumsHandler.setGenreId(genreId);
        getMusicAlbumsHandler.setAutoComplete(true);
        getMusicAlbumsHandler.setFavorite(favorite);
        this.mEventBus.post(getMusicAlbumsHandler.getRequest());
    }

    /**
     * @param searchString
     */
    private void getMusicArtist(String searchString) {
        GetMusicArtistsAutoCompleteHandler getMusicArtistAutoCompleteAlbumsHandler = new GetMusicArtistsAutoCompleteHandler();
        getMusicArtistAutoCompleteAlbumsHandler.setStartIndex(0);
        getMusicArtistAutoCompleteAlbumsHandler.setCount(20);
        getMusicArtistAutoCompleteAlbumsHandler.setSearchString(searchString);
        getMusicArtistAutoCompleteAlbumsHandler.setAutoComplete(true);
        this.mEventBus.post(getMusicArtistAutoCompleteAlbumsHandler.getRequest());
    }

    /**
     * @param getMusicAlbumsHandler
     */
    public void onEvent(GetMusicArtistsAutoCompleteHandler getMusicAlbumsHandler) {
        this.mMedias = new ArrayList<Media>(getMusicAlbumsHandler.getMusicArtists());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAutoCompleteOptions();
            }
        });
    }

    /**
     * @param getMusicAlbumsHandler
     */
    public void onEvent(GetMusicAlbumsAutoCompleteHandler getMusicAlbumsHandler) {
        this.mMedias = new ArrayList<Media>(getMusicAlbumsHandler.getMusicAlbums());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAutoCompleteOptions();
            }
        });
    }

    /**
     * @param getMovieDirectorsHandler
     */
    public void onEvent(GetMovieAutoCompleteDirectorsHandler getMovieDirectorsHandler) {
        this.mMedias = new ArrayList<Media>(getMovieDirectorsHandler.getPersons());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAutoCompleteOptions();
            }
        });

    }

    /**
     * Event Response to get movies listing
     *
     * @param getMovieLiteEvent
     */
    public void onEvent(GetAutoCompleteMovieLiteHandler getMovieLiteEvent) {
        this.mMedias = new ArrayList<Media>(getMovieLiteEvent.getMovieLites());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAutoCompleteOptions();
            }
        });
    }

    /**
     * @param getAutoCompleteActorsHandler
     */
    public void onEvent(GetAutoCompleteActorsHandler getAutoCompleteActorsHandler) {
        this.mMedias = new ArrayList<Media>(getAutoCompleteActorsHandler.getmActors());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAutoCompleteOptions();
            }
        });
    }


    /**
     * Getters and  Setters
     */

    /**
     * @return
     */
    public String getHint() {
        return mHint;
    }

    /**
     * @param mHint
     */
    public void setHint(String mHint) {
        this.mHint = mHint;
    }

    /**
     * @return
     */
    public MenuEvent getmMenuEvent() {
        return mMenuEvent;
    }

    /**
     * @param mMenuEvent
     */
    public void setmMenuEvent(MenuEvent mMenuEvent) {
        this.mMenuEvent = mMenuEvent;
        this.setHint();
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }
}
