package com.imax.ipt.controller.inputs;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.R;
import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.eventbus.handler.input.GetNowPlayingInputHandler;
import com.imax.ipt.controller.eventbus.handler.media.DisplayScreenShareButtonHandler;
import com.imax.ipt.controller.eventbus.handler.media.ScreenShareHandler;
import com.imax.ipt.controller.eventbus.handler.movie.GetPlayingMovieHandler;
import com.imax.ipt.controller.eventbus.handler.music.DisplayMusicAlbumBrowseOnScreenHandler;
import com.imax.ipt.controller.eventbus.handler.music.DisplayMusicArtistBrowseOnScreenHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicAlbumsHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicAlphaListingHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicAlphaListingIndexHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicArtistAlphaIndexHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicArtistAlphaListingsHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicArtistHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicGenresHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetPlayingMusicTrackHandler;
import com.imax.ipt.controller.eventbus.handler.push.MusicTrackAddedEvent;
import com.imax.ipt.controller.eventbus.handler.push.NowPlayingChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.PlayingMovieChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.PlayingMusicTrackChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.ScreenShareButtonVisibilityChangedEvent;
import com.imax.ipt.controller.eventbus.handler.ui.AutoCompleteEvent;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.controller.eventbus.handler.ui.PickerEvent;
import com.imax.ipt.model.Actor;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.model.Genre;
import com.imax.ipt.model.Input;
import com.imax.ipt.model.Media;
import com.imax.ipt.model.MusicAlbum;
import com.imax.ipt.ui.activity.input.music.MusicLibraryActivity;
import com.imax.ipt.ui.fragments.AutoCompleteFragment;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;
import com.imax.ipt.ui.widget.gridview.GridAdapter;

public class MusicLibraryController extends BaseController {
    public static final String TAG = "MusicLibraryController";
    private MenuEvent mCurrentEvent = MenuEvent.ALBUMS;

    private MusicLibraryActivity mMusicLibraryActivity;
    private AutoCompleteFragment autoCompleteFragment;
    private ArrayList<Media> mMedias = new ArrayList<Media>();

    private int initialCount = 100;
    private int initialIndex = 0;
    private int count = initialCount;

    private GridAdapter mGridAdapter;

    private int startIndex = initialIndex;
    private String searchString = "";
    private int genreId = 0;
    //   private Integer[] years = {};
    private int favorite = 0;
    //   private String mFirstName;
//   private String mLastName;
    private boolean mLazyLoad = false;
    private boolean mIsLoading = false;

    private boolean isScreenShareActive = false;
    private boolean isActivityVisible = false;

    private int listViewDisplayStartIndex;
    private int listViewDisplayCount;

    public MusicLibraryController(MusicLibraryActivity mMusicLibraryActivity, AutoCompleteFragment autoCompleteFragment) {
        super();
        this.mMusicLibraryActivity = mMusicLibraryActivity;
        this.autoCompleteFragment = autoCompleteFragment;
        this.mEventBus.registerSticky(this);
        this.mGridAdapter = new GridAdapter(mMusicLibraryActivity, R.layout.item_music_library, mMedias, R.drawable.ipt_gui_asset_album_cover_default, 175, 175, 5, 6);
    }

    public void init() {
        this.getMusicAlbums();
        this.getGenres();
        this.getAlphaListing();
        this.mMusicLibraryActivity.getmGridView().setAdapter(mGridAdapter);
    }

    public void onActivityResume() {
        resetDisplayedStartIndex();

        displayScreenShareButton();
        isActivityVisible = true;

        autoCompleteFragment.setmMenuEvent(mCurrentEvent);
    }

    public void onActivityPause() {
        isActivityVisible = false;
    }

    private void initGetParameters() {
//      this.mFirstName = "";
//      this.mLastName = "";
        this.searchString = "";
        this.startIndex = listViewDisplayStartIndex; //initialIndex;
        this.count = initialCount;
        // this.genreId = 0;
//      this.years = new Integer[0];
        // this.favorite = 0;
        this.mGridAdapter.clear();
        this.mGridAdapter.notifyDataSetInvalidated();
        this.mLazyLoad = false;
    }

    public void onDestroy() {
        this.mEventBus.unregister(this);
        this.mGridAdapter.cleanup();
    }

    /**
     * @param index
     * @param count
     * @return
     */
    private boolean isElementsLoaded(int index, int count) {
        int size = (index + count);

        if (index < mGridAdapter.getEmptyElements()) {
            // size needs to exclude any dummy element displayed on screen
            size = size - (mGridAdapter.getEmptyElements() - index);
        }

        // for (int i = index + mGridAdapter.getEmptyElements(); i < size; i++)
        for (int i = index; i < size; i++) {
            Media media;
            try {
                // mMedias is not thread-safe
                media = mMedias.get(i + mGridAdapter.getEmptyElements());
            } catch (Exception e) {
                Log.w(TAG, e.toString());
                return true;
            }

            if (media != null && media.getId().equals(GridAdapter.POINTER_ID)) {
                return false;
            }
        }

//      int size = (index + count);
//      for (int i = index + mGridAdapter.getEmptyElements(); i < size; i++)
//      {
//         Media media = mMedias.get(i);
//         if (media != null && media.getId().equals(GridAdapter.POINTER_ID))
//         {
//            return false;
//         }
//      }
        return true;
    }

    /**
     * @param index
     * @param count
     */
    public void loadView(int index, int count) {
        listViewDisplayStartIndex = index;
        listViewDisplayCount = count;

        index = index - mGridAdapter.getEmptyElements();
        if (index < 0) index = 0;

        if (!isElementsLoaded(index, count) && !mIsLoading) {
            // searchString = "";
            startIndex = index;
            setmIsLoading(true);
            getMore();
        }
    }

    /**
     *
     */
    private void getMore() {
        switch (mCurrentEvent) {
            case ALBUMS:
            case GENRES_MUSIC:
                getMusicAlbums();
                break;
            case ARTISTS:
                getArtist();
                break;
            case FAVOURITES_MUSIC:
                getFavourites();
                break;
        }
    }

    /**
     * @return
     */
    public int getDummyCount() {
        return mGridAdapter.getEmptyElements();
    }

    /**
     * @param menuMovieLibraryEvent
     */
    public void onEvent(final MediaMenuLibraryEvent menuMovieLibraryEvent) {
        // list should starts at the beginning of the gridview if a new category
        // is selected
        listViewDisplayStartIndex = 0;

        this.mMusicLibraryActivity.runOnUiThread(new Runnable() {
            public void run() {
                MenuEvent tempMenuEvent = menuMovieLibraryEvent.getEvent();
                switch (tempMenuEvent) {
                    case ALBUMS:
                        mCurrentEvent = tempMenuEvent;

                        setGenreId(0);
                        setFavorite(0);
                        initGetParameters();
                        getMusicAlbums();
                        getAlphaListing();
//               mMusicLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                        displayOnScreen(0);
                        break;
                    case GENRES_MUSIC:
                        mCurrentEvent = tempMenuEvent;

                        setFavorite(0);
                        initGetParameters();
                        getGenreById(menuMovieLibraryEvent.getMenu().getId());
                        getAlphaListing();
//               mMusicLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                        displayOnScreen(0);
                        break;
                    case ARTISTS:
                        mCurrentEvent = tempMenuEvent;

                        setFavorite(0);
                        setGenreId(0);
                        initGetParameters();
                        getArtist();
                        getMusicArtistAlphaListing();
//               mMusicLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                        displayOnScreen(0);
                        break;
                    case FAVOURITES_MUSIC:
                        mCurrentEvent = tempMenuEvent;

                        setGenreId(0);
                        setFavorite(1);
                        initGetParameters();
                        getFavourites();
                        getAlphaListing();
//               mMusicLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                        displayOnScreen(0);
                        break;
                    // case SHOW_GENRES:
                    // break;
                    default:
                        Log.d(TAG, "Music Library default case for menu library");
                        break;
                }

            }

        });
    }

    /**
     * Server interaction
     */

    /***
     * Request
     */

    /**
     * @param alpha
     */
    private void getMusicAlbumAlphaListingIndex(String alpha) {
        GetMusicAlphaListingIndexHandler getMusicAlphaListingIndexHandler = new GetMusicAlphaListingIndexHandler(alpha);
        getMusicAlphaListingIndexHandler.setSearchString(searchString);
        getMusicAlphaListingIndexHandler.setGenreId(genreId);
        getMusicAlphaListingIndexHandler.setFavorite(favorite);
        this.mEventBus.post(getMusicAlphaListingIndexHandler.getRequest());
    }

    private void getMusicArtistAlphaListingIndex(String alpha) {
        GetMusicArtistAlphaIndexHandler getMusicArtistAlphaListingIndexHandler = new GetMusicArtistAlphaIndexHandler(alpha);
        getMusicArtistAlphaListingIndexHandler.setSearchString(searchString);
        this.mEventBus.post(getMusicArtistAlphaListingIndexHandler.getRequest());
    }

    /**
     * GetMovieAlphaListingIndex Response
     *
     * @param searchString
     */
    public void onEvent(final GetMusicAlphaListingIndexHandler getMusicAlphaListingIndexHandler) {
        int index = getMusicAlphaListingIndexHandler.getIndex();
        while (index % 3 != 0) {
            index--;
        }
        displayOnScreen(index);
        mMusicLibraryActivity.onScroll(index);
    }

    public void onEvent(final GetMusicArtistAlphaIndexHandler getMusicArtistAlphaListingIndexHandler) {
        int index = getMusicArtistAlphaListingIndexHandler.getIndex();
        while (index % 3 != 0) {
            index--;
        }
        displayOnScreen(index);
        mMusicLibraryActivity.onScroll(index);
    }

    /**
     * @param search
     */
    private void search(String search) {
        this.initGetParameters();
        this.searchString = search;
        this.getMusicAlbums();
    }

    /**
     * @param genreId
     */
    private void getGenreById(int genreId) {
        // this.genreId = genreId;
        setGenreId(genreId);
        this.getMusicAlbums();
    }

    /**
     *
     */
    private void getAlphaListing() {
        GetMusicAlphaListingHandler getMusicAlbumAlphaListing = new GetMusicAlphaListingHandler();

        getMusicAlbumAlphaListing.setMusicArtistId(Handler.DEFAULT_GUID);
        getMusicAlbumAlphaListing.setGenreId(genreId);
        getMusicAlbumAlphaListing.setSearchString(searchString);
        getMusicAlbumAlphaListing.setFavorite(favorite);

        this.mEventBus.post(getMusicAlbumAlphaListing.getRequest());
    }

    /**
     *
     */
    private void getMusicArtistAlphaListing() {
        GetMusicArtistAlphaListingsHandler getMusicArtistAlphaListingsHandler = new GetMusicArtistAlphaListingsHandler();
        getMusicArtistAlphaListingsHandler.setSearchString(searchString);
        this.mEventBus.post(getMusicArtistAlphaListingsHandler.getRequest());
    }

    /**
     *
     */
    private void getMusicAlbums() {
        GetMusicAlbumsHandler getMusicAlbumsHandler = new GetMusicAlbumsHandler();
        getMusicAlbumsHandler.setStartIndex(startIndex);
        getMusicAlbumsHandler.setCount(count);
        getMusicAlbumsHandler.setSearchString(searchString);
        getMusicAlbumsHandler.setGenreId(genreId);
        getMusicAlbumsHandler.setFavorite(favorite);
        this.mEventBus.post(getMusicAlbumsHandler.getRequest());
    }

    /**
     * Event Response to get movies listing
     *
     * @param getMovieLiteEvent
     */
    public void onEvent(final GetMusicAlbumsHandler getMusicAlbumsHandler) {
        int totalCount = getMusicAlbumsHandler.getTotalRecordsAvailable();
        if (totalCount == 0) {
            return;
        }
        if (!mLazyLoad) {
            this.mGridAdapter.setTotalCount(getMusicAlbumsHandler.getTotalRecordsAvailable());
            mLazyLoad = true;
        }
        final List<MusicAlbum> musicAlbums = getMusicAlbumsHandler.getMusicAlbums();
        mMusicLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridAdapter.addElements(musicAlbums, getMusicAlbumsHandler.getStartIndex());
                mGridAdapter.notifyDataSetChanged();
                setmIsLoading(false);
            }
        });
    }

    /**
     *
     */
    private void getArtist() {
        GetMusicArtistHandler getMusicArtistHandler = new GetMusicArtistHandler();
        getMusicArtistHandler.setStartIndex(startIndex);
        getMusicArtistHandler.setCount(count);
        getMusicArtistHandler.setSearchString(searchString);
        // getMusicArtistHandler.setSearchStringFirstName(mFirstName);
        // getMusicArtistHandler.setSearchStringLastName(mLastName);
        this.mEventBus.post(getMusicArtistHandler.getRequest());
    }

    /**
     * @param getMusicArtistHandler
     */
    public void onEvent(final GetMusicArtistHandler getMusicArtistHandler) {
        if (!mLazyLoad) {
            this.mGridAdapter.setTotalCount(getMusicArtistHandler.getTotalRecordsAvailable());
            mLazyLoad = true;
        }

        final List<Actor> persons = getMusicArtistHandler.getPersons();

        this.mMusicLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridAdapter.addElements(persons, getMusicArtistHandler.getStartIndex());
                mGridAdapter.notifyDataSetChanged();
                setmIsLoading(false);
            }
        });
    }

    /**
     *
     */
    private void getFavourites() {
        // this.favorite = 1;
        // setFavorite(1);
        getMusicAlbums();
    }

    /**
     *
     */
    private void getGenres() {
        GetMusicGenresHandler genresRequestEvent = new GetMusicGenresHandler();
        this.mEventBus.post(genresRequestEvent.getRequest());
    }

    /**
     * Event Response to Build Picker Options Menu . Years or Letters .
     *
     * @param getMovieLiteEvent
     */
    public void onEvent(GetMusicAlphaListingHandler alphaListingResponseEvent) {
        final String[] options = alphaListingResponseEvent.getOptions();
        mMusicLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMusicLibraryActivity.setOptionForPickerFragment(options, -100, 10);
            }
        });
    }

    public void onEvent(GetMusicArtistAlphaListingsHandler alphaListingResponseEvent) {
        final String[] options = alphaListingResponseEvent.getOptions();
        mMusicLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMusicLibraryActivity.setOptionForPickerFragment(options, -100, 10);
            }
        });
    }

    /**
     * @param genresResponseEvent
     */
    public void onEvent(GetMusicGenresHandler genresResponseEvent) {
        final Genre[] genres = genresResponseEvent.getGenres();
        mMusicLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<MenuLibraryElement> listGenres = null;
                if (genres != null) {
                    listGenres = new ArrayList<MenuLibraryElement>();
                    for (int i = 0; i < genres.length; i++) {
                        Genre genre = genres[i];
                        listGenres.add(new MenuLibraryElement(genre.getId(), genre.getName(), null, MenuEvent.GENRES_MUSIC));
                    }
                }
                List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();
                MenuLibraryElement menuAlbums = new MenuLibraryElement(-1, mMusicLibraryActivity.getResources().getString(R.string.albums), null, MenuEvent.ALBUMS);
                MenuLibraryElement menuGenres = new MenuLibraryElement(-2, mMusicLibraryActivity.getResources().getString(R.string.genres), listGenres, MenuEvent.SHOW_GENRES);
                MenuLibraryElement menuArtist = new MenuLibraryElement(-3, mMusicLibraryActivity.getResources().getString(R.string.artists), null, MenuEvent.ARTISTS);
                MenuLibraryElement menuFavourites = new MenuLibraryElement(-5, mMusicLibraryActivity.getResources().getString(R.string.favourites), null, MenuEvent.FAVOURITES_MUSIC);
                mMenuOptions.add(menuAlbums);
                mMenuOptions.add(menuGenres);
                mMenuOptions.add(menuArtist);
                mMenuOptions.add(menuFavourites);
                mMusicLibraryActivity.addMenuLibraryFragment(mMusicLibraryActivity.getResources().getString(R.string.music), mMenuOptions, 500);
            }
        });
    }

    /**
     * UI Interaction
     */
    public void onEvent(AutoCompleteEvent autoCompleteEvent) {

        switch (autoCompleteEvent.getmMenuEvent()) {
            case ALBUMS:
            case GENRES_MUSIC:
                initGetParameters();
                // search(autoCompleteEvent.getmMedia().getTitle());
                searchString = autoCompleteEvent.getSearchString();
                this.getMusicAlbums();
                getAlphaListing();
                displayOnScreen(0);
                break;

            case ARTISTS:
                initGetParameters();
                // Person person = (Person) autoCompleteEvent.getmMedia();
                // this.mFirstName = person.getFirstName();
                // this.mLastName = person.getLastName();
                searchString = autoCompleteEvent.getSearchString();
                getArtist();
                getMusicArtistAlphaListing();
                displayOnScreen(0);
                break;

            // case GENRES_MUSIC:
            // initGetParameters();
            // searchString = autoCompleteEvent.getSearchString();
            // getMusicAlbums();
            // getAlphaListing();
            // // mMusicLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
            // break;

            case FAVOURITES_MUSIC:
                initGetParameters();
                searchString = autoCompleteEvent.getSearchString();
                getFavourites();
                getAlphaListing();
                displayOnScreen(0);
                break;

            default:
                Log.d(TAG, "AutoComplete Event on Music " + autoCompleteEvent.getmMenuEvent().name());
                break;
        }
    }

    /**
     * @param pickerEvent
     */
    public void onEvent(PickerEvent pickerEvent) {
        if (isActivityVisible) {

            final String alpha = pickerEvent.getSelection();
            this.mMusicLibraryActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (mCurrentEvent) {
                        // case YEARS:
                        // Integer year = Integer.parseInt(alpha);
                        // initGetParameters();
                        // years = new Integer[1];
                        // years[0] = year;
                        // getMusicAlbums();
                        // break;
                        case ALBUMS:
                            getMusicAlbumAlphaListingIndex(alpha);
                            break;

                        case ARTISTS:
                            getMusicArtistAlphaListingIndex(alpha);
                            break;

                        case GENRES_MUSIC:
                            getMusicAlbumAlphaListingIndex(alpha);
                            break;

                        case FAVOURITES_MUSIC:
                            getMusicAlbumAlphaListingIndex(alpha);
                            break;
                    }
                }
            });
        }
    }

    /**
     *
     * @param nowPlayingEvent
     */
    /**
     * @param nowPlayingEvent
     */
    public void onEvent(PlayingMovieChangedEvent nowPlayingEvent) {
        mMusicLibraryActivity.addNowPlayingFragment(nowPlayingEvent.getMedia());
    }

    public void onEvent(GetPlayingMovieHandler getPlayingMovieHandler) {
        mMusicLibraryActivity.addNowPlayingFragment(getPlayingMovieHandler.getMovieLite());
    }

    /**
     * @param nowPlayingEvent
     */
    public void onEvent(PlayingMusicTrackChangedEvent nowPlayingEvent) {
        mMusicLibraryActivity.addNowPlayingFragment(nowPlayingEvent.getmMusicAlbum());
    }

    public void onEvent(GetPlayingMusicTrackHandler getPlayingMusicTrackHandler) {
        mMusicLibraryActivity.addNowPlayingFragment(getPlayingMusicTrackHandler.getmMusicAlbum());
    }

    public void onEvent(NowPlayingChangedEvent nowPlayingChangedEvent) {
        mMusicLibraryActivity.addNowPlayingFragment(nowPlayingChangedEvent.getInput());
    }

    public void getNowPlayingStatus() {
        this.mEventBus.post(new GetNowPlayingInputHandler().getRequest());
    }

    public void onEvent(GetNowPlayingInputHandler getNowPlayingInputHandler) {
        Input nowPlaying = getNowPlayingInputHandler.getNowPlayingInput();
        Log.d("test", "music getNowPlayingInput=" + nowPlaying.getDeviceKind());
        switch (nowPlaying.getDeviceKind()) {
            case Zaxel:
            case Movie:
            case Music:
                // IPT Service should retrieve the Playing Media and send the event
                break;

            default:
                mMusicLibraryActivity.addNowPlayingFragment(nowPlaying);
                break;
        }
    }

    /**
     *
     */
    private void displayScreenShareButton() {
        this.mEventBus.post(new DisplayScreenShareButtonHandler().getRequest());
    }

    public void onEvent(ScreenShareButtonVisibilityChangedEvent screenShareButtonVisibilityChangedEvent) {
        isScreenShareActive = !screenShareButtonVisibilityChangedEvent.isVisible();
        mMusicLibraryActivity.setShareButton(screenShareButtonVisibilityChangedEvent.isVisible());

        if (isScreenShareActive)
            displayOnScreen(listViewDisplayStartIndex);
    }

    /**
     * @param displayScreenShareButton
     */
    public void onEvent(DisplayScreenShareButtonHandler displayScreenShareButton) {
        boolean shareButton = displayScreenShareButton.isShareButton();
        isScreenShareActive = !shareButton;
        mMusicLibraryActivity.setShareButton(shareButton);

        if (isScreenShareActive)
            displayOnScreen(listViewDisplayStartIndex);
    }

    private long timeMillisLastMusicTrackAdded;

    public void onEvent(MusicTrackAddedEvent MusicTrackAddedEvent) {

        if ((System.currentTimeMillis() - timeMillisLastMusicTrackAdded) > 2000) {
            timeMillisLastMusicTrackAdded = System.currentTimeMillis();

            // for each track of the album, this event will be triggered
            // use time to avoid excessive refreshing of the gridview due to new
            // track added
            Log.d(TAG, "syncMusicTrackLock acquired...");

            mMusicLibraryActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // refresh screen appropriately depending on which type of
                    // content
                    // the user is on
                    mGridAdapter.setmProgressMediaLoading(0);

                    setGenreId(0);
                    setFavorite(0);
                    initGetParameters();
                    getMusicAlbums();
                    getAlphaListing();
//               mMusicLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                    displayOnScreen(0);
                    // mGridAdapter.notifyDataSetChanged();

                }
            });
        }
    }

    /**
     * Getters and Setters
     */
    public ArrayList<Media> getmMedias() {
        return mMedias;
    }

    /**
     * @param mMedias
     */
    public void setmMedias(ArrayList<Media> mMedias) {
        this.mMedias = mMedias;
    }

    /**
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * @return
     */
    public boolean ismIsLoading() {
        return mIsLoading;
    }

    /**
     * @param mIsLoading
     */
    public void setmIsLoading(boolean mIsLoading) {
        this.mIsLoading = mIsLoading;
    }

    /**
     * @return
     */
    public MenuEvent getmCurrentEvent() {
        return mCurrentEvent;
    }

    /**
     * @param mCurrentEvent
     */
    public void setmCurrentEvent(MenuEvent mCurrentEvent) {
        this.mCurrentEvent = mCurrentEvent;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
        autoCompleteFragment.setGenreId(genreId);
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
        autoCompleteFragment.setFavorite(favorite);
    }

    public void screenShare() {
        this.mEventBus.post(new ScreenShareHandler().getRequest());

    }

    private int displayedAlbumBrowse_startIndex = -1;
    private int displayedArtistsBrowse_startIndex = -1;

    private String displayed_searchString = "";
    private int displayed_genreId;
    private int displayed_favorite;

    private void resetDisplayedStartIndex() {
        displayedAlbumBrowse_startIndex = -1;
        displayedArtistsBrowse_startIndex = -1;
    }

    public void displayOnScreen(int index) {
        if (isScreenShareActive) {
            switch (mCurrentEvent) {
                case ALBUMS:
                case GENRES_MUSIC:
                case FAVOURITES_MUSIC:
                    if (index == displayedAlbumBrowse_startIndex &&
                            displayed_searchString.equals(searchString) &&
                            displayed_genreId == genreId &&
                            displayed_favorite == favorite) {
                        // already displayed on theatre screen
                        return;
                    }

                    DisplayMusicAlbumBrowseOnScreenHandler displayMusicAlbumBrowseOnScreenHandler = new DisplayMusicAlbumBrowseOnScreenHandler();
                    displayMusicAlbumBrowseOnScreenHandler.setStartIndex(index);
                    displayMusicAlbumBrowseOnScreenHandler.setGenreId(genreId);
                    displayMusicAlbumBrowseOnScreenHandler.setSearchString(searchString);
                    displayMusicAlbumBrowseOnScreenHandler.setFavorite(favorite);
                    this.mEventBus.post(displayMusicAlbumBrowseOnScreenHandler.getRequest());

                    resetDisplayedStartIndex();
                    displayedAlbumBrowse_startIndex = index;
                    displayed_searchString = searchString;
                    displayed_genreId = genreId;
                    displayed_favorite = favorite;
                    break;

                case ARTISTS:
                    if (index == displayedArtistsBrowse_startIndex &&
                            displayed_searchString.equals(searchString)) {
                        // already displayed on theatre screen
                        return;
                    }

                    DisplayMusicArtistBrowseOnScreenHandler displayMusicArtistBrowseOnScreenHandler = new DisplayMusicArtistBrowseOnScreenHandler();
                    displayMusicArtistBrowseOnScreenHandler.setStartIndex(index);
                    displayMusicArtistBrowseOnScreenHandler.setSearchString(searchString);
                    this.mEventBus.post(displayMusicArtistBrowseOnScreenHandler.getRequest());

                    resetDisplayedStartIndex();
                    displayedArtistsBrowse_startIndex = index;
                    displayed_searchString = searchString;
                    break;
            }
        }
    }
}
