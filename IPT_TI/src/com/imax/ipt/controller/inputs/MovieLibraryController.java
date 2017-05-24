package com.imax.ipt.controller.inputs;

import android.util.Log;
import android.view.View;
import com.imax.ipt.R;
import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.eventbus.handler.input.GetNowPlayingInputHandler;
import com.imax.ipt.controller.eventbus.handler.media.DisplayScreenShareButtonHandler;
import com.imax.ipt.controller.eventbus.handler.media.RegisterMediaLoadProgressHandler;
import com.imax.ipt.controller.eventbus.handler.media.ScreenShareHandler;
import com.imax.ipt.controller.eventbus.handler.movie.*;
import com.imax.ipt.controller.eventbus.handler.music.GetPlayingMusicTrackHandler;
import com.imax.ipt.controller.eventbus.handler.push.*;
import com.imax.ipt.controller.eventbus.handler.ui.AutoCompleteEvent;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.controller.eventbus.handler.ui.PickerEvent;
import com.imax.ipt.model.Genre;
import com.imax.ipt.model.Input;
import com.imax.ipt.model.Media;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.fragments.AutoCompleteFragment;
import com.imax.ipt.ui.util.DisplayUtil;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;
import com.imax.ipt.ui.widget.gridview.GridAdapter;
import com.imax.ipt.ui.widget.viewpager.ViewPagerSubMenuFragment;

import java.util.ArrayList;
import java.util.List;

public class MovieLibraryController extends BaseController {
    public static final String TAG = "MovieLibraryController";

    private ViewPagerSubMenuFragment mMovieItemFragment;
    private MovieLibraryActivity mMediaLibraryActivity;
    private AutoCompleteFragment autoCompleteFragment;
    private GridAdapter mGridAdapter;
    private MenuEvent mCurrentEvent = MenuEvent.TITLES;
    private int initialCount = 100;
    private int initialIndex = 0;
    private int count = initialCount;
    private int mSize;

    private int startIndex = initialIndex;
    private String searchString = "";
//   private String mfirstName;
//   private String mLastName;

    // begin add by jennifer 20150824
    private final int subMenuWidth = 250;//
    // end and by jennifer 20150824
    private int genreId = 0;
    private Integer[] years = {};
    private Integer favorite = 0;
    private int mTotalRecordsAvailable = 0;
    private int orderBy = 0;
    private ArrayList<Media> mMedias = new ArrayList<Media>(mTotalRecordsAvailable);

    private String mActorId = Handler.DEFAULT_GUID;
    private String mDirectorId = Handler.DEFAULT_GUID;

    private boolean mLazyLoad = false;
    private boolean mIsLoading = false;
    private boolean isScreenShareActive = true;

    private boolean mMediaLoading = false;
    private boolean isActivityVisible = false;

    private int listViewDisplayStartIndex;
    private int listViewDisplayCount;

    /**
     * @param mediaLibraryActivity
     */
    public MovieLibraryController(MovieLibraryActivity mediaLibraryActivity, AutoCompleteFragment autoCompleteFragment, ViewPagerSubMenuFragment mMovieItemFragment) {
        this.mMovieItemFragment = mMovieItemFragment;
        this.mMediaLibraryActivity = mediaLibraryActivity;
        this.autoCompleteFragment = autoCompleteFragment;
        this.mEventBus.registerSticky(this);
        // this is movie content gridview
//        this.mGridAdapter = new GridAdapter(mMediaLibraryActivity, R.layout.item_movie_library, mMedias, R.drawable.ipt_gui_asset_new_movie_icon, 115, 175, 5, 9);
        this.mGridAdapter = new GridAdapter(mMediaLibraryActivity, R.layout.item_movie_library, mMedias,
                R.drawable.catalogue_noposter_bg, 160, 240, 0, 2);
//        this.mGridAdapter = new GridAdapter(mMovieItemFragment.getActivity(), R.layout.item_movie_library, mMedias, R.drawable.catalogue_noposter_bg, 115, 175, 5, 9);
    }

    /**
     *
     */
    public void init() {
        this.getMovieAlphaListing();
        this.getMovieByTitles();
        this.getGenres();
        this.mMediaLibraryActivity.getGridView().setAdapter(mGridAdapter);

//        mMovieItemFragment.getmGridView().setAdapter(mGridAdapter);

    }

    public void onDestroy() {
        this.mEventBus.unregister(this);
        this.mGridAdapter.cleanup();
        this.mMediaLibraryActivity.finish();
    }

    public void onActivityResume() {
        resetDisplayedStartIndex();
//        displayScreenShareButton();

        isActivityVisible = true;

        autoCompleteFragment.setmMenuEvent(mCurrentEvent);
    }

    public void onActivityPause() {
        isActivityVisible = false;
    }

    /**
     * SCREEN LOGIC This logic should be isolated in another class
     *
     * @param
     */
    private void initGetParameters() {
//      this.mfirstName = "";
//      this.mLastName = "";
        this.mActorId = Handler.DEFAULT_GUID;
        this.mDirectorId = Handler.DEFAULT_GUID;
        this.searchString = "";
        this.startIndex = listViewDisplayStartIndex;// initialIndex;
        this.count = initialCount;
        // this.genreId = 0;
        this.years = new Integer[0];
        this.favorite = 0;
        this.orderBy = 0;
        this.mGridAdapter.clear();
        this.mGridAdapter.notifyDataSetInvalidated();
        this.mLazyLoad = false;
    }

    public int getDummyCount() {
        return mGridAdapter.getEmptyElements();
    }

    public void getMore() {
        switch (mCurrentEvent) {
            case TITLES:
            case GENRES_MOVIE:
                getMovieLite();
                break;
            case ACTORS:
                getActors();
                break;
            case DIRECTORS:
                getDirectors();
                break;
            case FAVOURITES_MOVIE:
                getFavourites();
                break;
            case YEARS:
                getMovieByYears();
                break;
            default:
                Log.d(TAG, "mCurrentEvent -->" + mCurrentEvent);
                break;
        }
    }

    /**
     * @param index
     * @param count
     * @return
     */
    private boolean isElementsLoaded(int index, int count) {
        // int size = (index + count) + mGridAdapter.getEmptyElements();
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
        // int size = (index + count) - mGridAdapter.getEmptyElements();
        // for (int i = index; i < size; i++)
        // {
        // Media media = mMedias.get(i);
        // if (media != null && media.getId().equals(GridAdapter.POINTER_ID))
        // {
        // return false;
        // }
        // }
        return true;
    }

    /**
     * @param index
     * @param count
     */
    public void checkIsLoadingMedia(int index, int count) {
        boolean mLoading = false;
        if (getLoadingMediaIndex(index, count) >= 0) mLoading = true;

        if (mLoading) {
            if (!mMediaLoading) {
                this.mEventBus.post(new RegisterMediaLoadProgressHandler(true).getRequest());
                mMediaLoading = true;
            }
        } else {
            if (mMediaLoading) {
                this.mEventBus.post(new RegisterMediaLoadProgressHandler(false).getRequest());
                mMediaLoading = false;
            }
        }
        // To display loading animation
        mGridAdapter.setmViewShowMediaLoadingProgress(mLoading);
    }

    private int getLoadingMediaIndex(int index, int count) {
        int size = (index + count) - mGridAdapter.getEmptyElements();
        for (int i = index; i < size; i++) {
            Media media;
            try {
                media = mMedias.get(i);
            } catch (Exception e) {
                Log.w(TAG, e.toString());
                return -1;
            }
            if (media != null && !media.getId().equals(GridAdapter.POINTER_ID) && media.isLoading()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param index
     * @param count
     */
    public void loadView(int index, int count) {
        listViewDisplayStartIndex = index;
        listViewDisplayCount = count;

        // startIndex is # of EmptyElements behind, if jumping is made, need to
        // preload elements behind the Title Menu
        index = index - mGridAdapter.getEmptyElements();
        if (index < 0) index = 0;

        if (!isElementsLoaded(index, count) && !mIsLoading) {
            // why do we need to clear the search string here?
            // searchString = "";

            // // startIndex is # of EmptyElements behind, if jumping is made, need
            // to preload elements behind the Title Menu
            // index = index - mGridAdapter.getEmptyElements();
            // if (index < 0)
            // index = 0;

            startIndex = index;
            setmIsLoading(true);
            getMore();
        }
    }

    /***
     * UI EVENTS
     */
    public void onEvent(final PickerEvent pickerEvent) {
        // this.searchString = pickerEvent.getSelection();

        if (isActivityVisible) {
            this.mMediaLibraryActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (mCurrentEvent) {
                        case TITLES:
                        case GENRES_MOVIE:
                            getMovieAlphaListingIndex(pickerEvent.getSelection(), 0);
                            break;
                        case ACTORS:
                            getMovieActorAlphaListingIndex(pickerEvent.getSelection());
                            break;
                        case DIRECTORS:
                            getMovieDirectorAlphaListingIndex(pickerEvent.getSelection());
                            break;
                        case FAVOURITES_MOVIE:
                            getMovieAlphaListingIndex(pickerEvent.getSelection(), 1);

                        case YEARS:
                            Integer year = Integer.parseInt(pickerEvent.getSelection());
                            // initGetParameters();
                            // years = new Integer[1];
                            // years[0] = year;
                            // getMovieLite();
                            getMovieYearIndex(year);
                            break;

                        default:
//                            Log.d(TAG, "Movie Library Controller on Picker Event Default Case");
                            break;
                    }

                }
            });
        }
    }

    /**
     * @param menuMovieLibraryEvent
     */
    public void onEvent(final MediaMenuLibraryEvent menuMovieLibraryEvent) {
        // list should starts at the beginning of the gridview if a new category
        // is selected
        listViewDisplayStartIndex = 0;

        this.mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MenuEvent tempMenuEvent = menuMovieLibraryEvent.getEvent();
                switch (tempMenuEvent) {
                    case TITLES:
                        mCurrentEvent = tempMenuEvent;

                        // genreId = 0;
                        setGenreId(0);
                        setFavorite(0);

                        getMovieByTitles();
                        displayOnScreen(0);
                        break;
                    case GENRES_MOVIE:
                        mCurrentEvent = tempMenuEvent;
                        setFavorite(0);

                        initGetParameters();
                        getGenreById(menuMovieLibraryEvent.getMenu().getId());
                        getMovieAlphaListing();
//               mMediaLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                        mGridAdapter.cleanup();
                        displayOnScreen(0);
                        break;
                    case ACTORS:
                        mCurrentEvent = tempMenuEvent;
                        // genreId = 0;
                        setGenreId(0);

                        initGetParameters();
                        getActors();
                        getMovieActorAlphaListing();
//               mMediaLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                        mGridAdapter.cleanup();
                        displayOnScreen(0);
                        break;
                    case DIRECTORS:
                        mCurrentEvent = tempMenuEvent;
                        // genreId = 0;
                        setGenreId(0);

                        initGetParameters();
                        getDirectors();
                        getMovieDirectorAlphaListing();
//               mMediaLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                        mGridAdapter.cleanup();
                        displayOnScreen(0);
                        break;
                    case FAVOURITES_MOVIE:
                        mCurrentEvent = tempMenuEvent;
                        // genreId = 0;
                        setGenreId(0);
                        setFavorite(1);

                        initGetParameters();
                        getFavourites();
                        getMovieAlphaListing();
//               mMediaLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                        mGridAdapter.cleanup();
                        displayOnScreen(0);
                        break;
                    case YEARS:
                        mCurrentEvent = tempMenuEvent;
                        // genreId = 0;
                        setGenreId(0);
                        setFavorite(0);

                        initGetParameters();
                        getMovieByYears();
                        getMovieYearDecades();
//               mMediaLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                        mGridAdapter.cleanup();
                        displayOnScreen(0);
                        break;
                    default:
                        Log.d(TAG, "This the Menu by default " + menuMovieLibraryEvent.toString());
                        break;
                }
            }
        });
    }

    /***
     * ################### SERVER INTERACTION ##########################
     */

    public void screenShare() {
        this.mEventBus.post(new ScreenShareHandler().getRequest());
    }

    // do not send displayOnScreen if all parameters are the same as the one that had been sent
    private int displayedMovieBrowse_startIndex = -1;
    private int displayedActorBrowse_startIndex = -1;
    private int displayedDirectorBrowse_startIndex = -1;
    private int displayedYearBrowse_startIndex = -1;

    private String displayed_searchString = "";
    private int displayed_genreId;
    private int displayed_favorite;

    private void resetDisplayedStartIndex() {
        displayedMovieBrowse_startIndex = -1;
        displayedActorBrowse_startIndex = -1;
        displayedDirectorBrowse_startIndex = -1;
        displayedYearBrowse_startIndex = -1;
    }

    public void displayOnScreen(int index) {
        Log.d("test", "isScreenActive=" + isScreenShareActive + ",current=" + mCurrentEvent);
        if (isScreenShareActive) {
            switch (mCurrentEvent) {
                case TITLES:
                case FAVOURITES_MOVIE:
                case GENRES_MOVIE: {
                    if (index == displayedMovieBrowse_startIndex &&
                            displayed_searchString.equals(searchString) &&
                            displayed_genreId == genreId &&
                            displayed_favorite == favorite) {
                        Log.d("test", "ret");
                        // already displayed on theatre screen
                        return;
                    }

                    DisplayMovieBrowseOnScreenHandler displayMovieBrowseOnScreenHandler = new DisplayMovieBrowseOnScreenHandler();
                    displayMovieBrowseOnScreenHandler.setStartIndex(index);
                    displayMovieBrowseOnScreenHandler.setSearchString(searchString);
                    displayMovieBrowseOnScreenHandler.setGenreId(genreId);
                    displayMovieBrowseOnScreenHandler.setFavorite(favorite);
                    this.mEventBus.post(displayMovieBrowseOnScreenHandler.getRequest());
                    Log.d("test", "pro");
                    resetDisplayedStartIndex();
                    displayedMovieBrowse_startIndex = index;
                    displayed_searchString = searchString;
                    displayed_genreId = genreId;
                    displayed_favorite = favorite;
                    break;
                }

                case ACTORS:
                    if (index == displayedActorBrowse_startIndex &&
                            displayed_searchString.equals(searchString)) {
                        // already displayed on theatre screen
                        return;
                    }

                    DisplayActorBrowseOnScreenHandler displayActorBrowseOnScreenHandler = new DisplayActorBrowseOnScreenHandler(Handler.DEFAULT_GUID);
                    displayActorBrowseOnScreenHandler.setStartIndex(index);
                    displayActorBrowseOnScreenHandler.setSearchString(searchString);
                    this.mEventBus.post(displayActorBrowseOnScreenHandler.getRequest());

                    resetDisplayedStartIndex();
                    displayedActorBrowse_startIndex = index;
                    displayed_searchString = searchString;
                    break;

                case DIRECTORS:
                    if (index == displayedDirectorBrowse_startIndex &&
                            displayed_searchString.equals(searchString)) {
                        // already displayed on theatre screen
                        return;
                    }

                    DisplayDirectorBrowseOnScreenHandler displayDirectorBrowseOnScreenHandler = new DisplayDirectorBrowseOnScreenHandler(Handler.DEFAULT_GUID);
                    displayDirectorBrowseOnScreenHandler.setStartIndex(index);
                    displayDirectorBrowseOnScreenHandler.setSearchString(searchString);
                    this.mEventBus.post(displayDirectorBrowseOnScreenHandler.getRequest());

                    resetDisplayedStartIndex();
                    displayedDirectorBrowse_startIndex = index;
                    displayed_searchString = searchString;
                    break;

                case YEARS: {
                    if (index == displayedYearBrowse_startIndex &&
                            displayed_searchString.equals(searchString) &&
                            displayed_genreId == genreId &&
                            displayed_favorite == favorite) {
                        // already displayed on theatre screen
                        return;
                    }

                    DisplayMovieBrowseOnScreenHandler displayMovieBrowseOnScreenHandler = new DisplayMovieBrowseOnScreenHandler();
                    displayMovieBrowseOnScreenHandler.setStartIndex(index);
                    displayMovieBrowseOnScreenHandler.setSearchString(searchString);
                    displayMovieBrowseOnScreenHandler.setGenreId(genreId);
                    displayMovieBrowseOnScreenHandler.setFavorite(favorite);
                    displayMovieBrowseOnScreenHandler.setOrderByOptions(1);
                    this.mEventBus.post(displayMovieBrowseOnScreenHandler.getRequest());

                    resetDisplayedStartIndex();
                    displayedYearBrowse_startIndex = index;
                    displayed_searchString = searchString;
                    displayed_genreId = genreId;
                    displayed_favorite = favorite;
                    break;
                }
            }
        }
    }

    // /**
    // *
    // */
    // public void displayMediaDetailsOnScreen(String uId)
    // {
    // if (mShareButton)
    // {
    // this.mEventBus.post(new
    // DisplayMovieDetailsOnScreenHandler(uId).getRequest());
    // }
    // }

    /**
     * Request for displayScreenShareButton
     */
    private void displayScreenShareButton() {
        this.mEventBus.post(new DisplayScreenShareButtonHandler().getRequest());
    }

    /**
     * Response DisplayScreenShareButton
     *
     * @param displayScreenShareButton
     */
    public void onEvent(DisplayScreenShareButtonHandler displayScreenShareButton) {
        boolean mShareButton = displayScreenShareButton.isShareButton();
        isScreenShareActive = !mShareButton;
        mMediaLibraryActivity.setShareButton(mShareButton);

        if (isScreenShareActive)
            displayOnScreen(listViewDisplayStartIndex);
    }

    public void onEvent(ScreenShareButtonVisibilityChangedEvent screenShareButtonVisibilityChangedEvent) {
        isScreenShareActive = !screenShareButtonVisibilityChangedEvent.isVisible();
        mMediaLibraryActivity.setShareButton(screenShareButtonVisibilityChangedEvent.isVisible());

        if (isScreenShareActive)
            displayOnScreen(listViewDisplayStartIndex);
    }

    /***
     * Request
     */
    private void search(String search) {
        this.initGetParameters();
        this.searchString = search;
        this.getMovieLite();

        this.getMovieAlphaListing();
    }

    /**
     * @param genreId
     */
    private void getGenreById(int genreId) {
        // this.genreId = genreId;
        setGenreId(genreId);
        this.getMovieLite();
    }

    /**
     *
     */
    private void getMovieAlphaListing() {
        this.mEventBus.post(new GetMovieAlphaListingHandler(searchString, genreId, favorite).getRequest());
    }

    /**
     * Event Response to Build Picker Options Menu . Years or Letters .
     *
     * @param
     */
    public void onEvent(GetMovieAlphaListingHandler alphaListingResponseEvent) {
        final String[] options = alphaListingResponseEvent.getOptions();
        mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMediaLibraryActivity.setOptionForPickerFragment(options, -100, 40); // first time and title favorite  genre sub item event
            }
        });
    }

    /**
     *
     */
    private void getMovieActorAlphaListing() {
        this.mEventBus.post(new GetMovieActorAlphaListingHandler(searchString).getRequest());
    }

    /**
     * @param alphaListingResponseEvent
     */
    public void onEvent(GetMovieActorAlphaListingHandler alphaListingResponseEvent) {
        final String[] options = alphaListingResponseEvent.getOptions();
        mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMediaLibraryActivity.setOptionForPickerFragment(options, -100, 30); // actor event
            }
        });
    }

    private void getMovieDirectorAlphaListing() {
        this.mEventBus.post(new GetMovieDirectorAlphaListingsHandler(searchString).getRequest());
    }

    public void onEvent(GetMovieDirectorAlphaListingsHandler alphaListingResponseEvent) {
        final String[] options = alphaListingResponseEvent.getOptions();
        mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMediaLibraryActivity.setOptionForPickerFragment(options, -100, 10);// director event
            }
        });
    }

    /**
     */
    private void getMovieYearDecades() {
        GetMovieYearDecadesHandler getMovieYearDecadesHandler = new GetMovieYearDecadesHandler();
        getMovieYearDecadesHandler.setSearchString(searchString);
        this.mEventBus.post(getMovieYearDecadesHandler.getRequest());
    }

    /**
     * GetMovieAlphaListingIndex Request
     *
     * @param
     */
    private void getMovieAlphaListingIndex(String alpha, int favorite) {
        GetMovieAlphaListingIndexHandler getMovieAlphaListingIndexHandler = new GetMovieAlphaListingIndexHandler(alpha, searchString, genreId);
        getMovieAlphaListingIndexHandler.setFavorite(favorite);
        this.mEventBus.post(getMovieAlphaListingIndexHandler.getRequest());
    }

    /**
     * GetMovieAlphaListingIndex Response
     *
     * @param
     */
    public void onEvent(final GetMovieAlphaListingIndexHandler getMovieAlphaListingIndexHandler) {
        int index = getMovieAlphaListingIndexHandler.getIndex();
        // alpha index may not be the start of the column, using that index may
        // cause layout issue that is not synchronize b/w the theatre screen and
        // the table
        Log.d("test", "index=" + index);
        while (index % 3 != 0) {
            index--;
        }
        this.displayOnScreen(index);
        mMediaLibraryActivity.onScroll(index);
    }

    /**
     * GetMovieAlphaListingIndex Request
     *
     * @param alpha
     */
    private void getMovieActorAlphaListingIndex(String alpha) {
        GetMovieActorAlphaListingIndexHandler getMovieAlphaListingIndexHandler = new GetMovieActorAlphaListingIndexHandler(alpha);
        getMovieAlphaListingIndexHandler.setSearchString(searchString);
        this.mEventBus.post(getMovieAlphaListingIndexHandler.getRequest());
    }

    /**
     * @param
     */
    private void getMovieDirectorAlphaListingIndex(String alpha) {
        GetMovieDirectorsAlphaListingIndexHandler getMovieDirectorsAlphaListingIndexHandler = new GetMovieDirectorsAlphaListingIndexHandler(alpha);
        getMovieDirectorsAlphaListingIndexHandler.setSearchString(searchString);
        this.mEventBus.post(getMovieDirectorsAlphaListingIndexHandler.getRequest());
    }

    /* *
     */
    public void onEvent(final GetMovieDirectorsAlphaListingIndexHandler getMovieDirectorsAlphaListingIndexHandler) {
        int index = getMovieDirectorsAlphaListingIndexHandler.getIndex();
        while (index % 3 != 0) {
            index--;
        }
        this.displayOnScreen(index);
        mMediaLibraryActivity.onScroll(index);
    }

    /**
     * @param getMovieAlphaListingIndexHandler
     */
    public void onEvent(final GetMovieActorAlphaListingIndexHandler getMovieAlphaListingIndexHandler) {
        int index = getMovieAlphaListingIndexHandler.getIndex();
        while (index % 3 != 0) {
            index--;
        }
        this.displayOnScreen(index);
        mMediaLibraryActivity.onScroll(index);
    }

    public void getMovieYearIndex(int selectedYear) {
        // GetMovieYearIndexHandler
        GetMovieYearIndexHandler getMovieYearIndexHandler = new GetMovieYearIndexHandler(selectedYear);
        getMovieYearIndexHandler.setSearchString(searchString);
        this.mEventBus.post(getMovieYearIndexHandler.getRequest());
    }

    public void onEvent(final GetMovieYearIndexHandler getMovieYearIndex) {
        int index = getMovieYearIndex.getIndex();
        while (index % 3 != 0) {
            index--;
        }
        this.displayOnScreen(index);
        // mMediaLibraryActivity.onScroll(3);
        mMediaLibraryActivity.onScroll(index);
    }

    /**
     *
     */
    private void getGenres() {
        GetMovieGenresHandler genresRequestEvent = new GetMovieGenresHandler();
        this.mEventBus.post(genresRequestEvent.getRequest());

    }

    /**
     * GetMovieTitle
     */
    private void getMovieByYears() {
        this.orderBy = 1;
        this.getMovieLite();
    }

    /**
     * GetMovieTitle
     */
    private void getMovieByTitles() {
        initGetParameters();
        this.getMovieLite();
        getMovieAlphaListing();
//      mMediaLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
        mGridAdapter.cleanup();
    }

    /**
     *
     */
    private void getMovieLite() {
        GetMovieLiteHandler getMovieLiteEvent = new GetMovieLiteHandler();
        getMovieLiteEvent.setStartIndex(startIndex);
        getMovieLiteEvent.setCount(count);
        getMovieLiteEvent.setSearchString(searchString);
        getMovieLiteEvent.setGenreId(genreId);
        getMovieLiteEvent.setDirectorId(mDirectorId);
        getMovieLiteEvent.setActorId(mActorId);
        getMovieLiteEvent.setYears(years);
        getMovieLiteEvent.setFavorite(favorite);
        getMovieLiteEvent.setOrderByOptions(orderBy);
        this.mEventBus.post(getMovieLiteEvent.getRequest());
    }

    /**
     * Event Response to get movies listing
     *
     * @param getMovieLiteEvent
     */
    public void onEvent(final GetMovieLiteHandler getMovieLiteEvent) {
        if (!mLazyLoad) {
            this.mGridAdapter.setTotalCount(getMovieLiteEvent.getTotalRecordsAvailable());
            mLazyLoad = true;
        }
        final List<? extends Media> movieLites = getMovieLiteEvent.getMovieLites(); //
        this.mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridAdapter.addElements(movieLites, getMovieLiteEvent.getStartIndex());
                mGridAdapter.notifyDataSetChanged();
                setmIsLoading(false);
            }
        });
    }


    public void onEvent(MovieDeletedEvent movieDeletedEvent) {
        mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // refresh screen appropriately depending on which type of content
                // the user is on
                mGridAdapter.setmProgressMediaLoading(0);
                getMovieByTitles();
                // mGridAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Get Actors Request
     */
    private void getActors() {
        GetMovieActorsHandler movieActors = new GetMovieActorsHandler();
        movieActors.setStartIndex(startIndex);
        movieActors.setCount(count);
        movieActors.setSearchString(searchString);
        // movieActors.setSearchStringFirstName(mfirstName);
        // movieActors.setSearchStringLastName(mLastName);
        this.mEventBus.post(movieActors.getRequest());
    }

    /**
     * Event Response to Build Picker Options Menu . Years or Letters .
     *
     * @param
     */
    public void onEvent(final GetMovieActorsHandler getMovieActorsResponseEvent) {
        if (!mLazyLoad) {
            this.mGridAdapter.setTotalCount(getMovieActorsResponseEvent.getTotalRecordsAvailable());
            mLazyLoad = true;
        }
        final List<? extends Media> mMediaActors = getMovieActorsResponseEvent.getActors();
        this.mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridAdapter.addElements(mMediaActors, getMovieActorsResponseEvent.getStartIndex());
                mGridAdapter.notifyDataSetChanged();
                setmIsLoading(false);
            }
        });
    }

    /**
     * Request getDirectors
     */
    private void getDirectors() {
        GetMovieDirectorsHandler getMovieDirectorsRequestEvent = new GetMovieDirectorsHandler();
        getMovieDirectorsRequestEvent.setStartIndex(startIndex);
        getMovieDirectorsRequestEvent.setCount(count);
        getMovieDirectorsRequestEvent.setSearchString(searchString);
        // getMovieDirectorsRequestEvent.setSearchStringFirstName(mfirstName);
        // getMovieDirectorsRequestEvent.setSearchStringLastName(mLastName);
        this.mEventBus.post(getMovieDirectorsRequestEvent.getRequest());
    }

    /**
     * @param getMovieDirectorsResponseEvent
     */
    public void onEvent(final GetMovieDirectorsHandler getMovieDirectorsResponseEvent) {
        int totalCount = getMovieDirectorsResponseEvent.getTotalRecordsAvailable();
        if (!mLazyLoad) {
            mGridAdapter.setTotalCount(totalCount);
            mLazyLoad = true;
        }
        final List<? extends Media> mMediaActors = getMovieDirectorsResponseEvent.getPersons();
        this.mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridAdapter.addElements(mMediaActors, getMovieDirectorsResponseEvent.getStartIndex());
                mGridAdapter.notifyDataSetChanged();
                setmIsLoading(false);
            }
        });
    }

    /**
     *
     */
    private void getFavourites() {
        this.favorite = 1;
        getMovieLite();
    }

    /**
     * Event Response to Build Picker Options Menu . Years 2011,,, .
     *
     * @param
     */
    public void onEvent(GetMovieYearDecadesHandler getMovieYearDecadesResponseEvent) {
        final String[] options = getMovieYearDecadesResponseEvent.getOptions();
        mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMediaLibraryActivity.setOptionForPickerFragment(options, -50, 20);// year event
            }
        });
    }

    /**
     * Event Response to Build Menu From Sever.
     *
     * @param genresResponseEvent
     */
    public void onEvent(GetMovieGenresHandler genresResponseEvent) {
        mMediaLibraryActivity.removeOptionForPickerFragment();
        if (genresResponseEvent.isAutoComplete()) {
            return;
        }

        final Genre[] genres = genresResponseEvent.getGenres();
        this.mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<MenuLibraryElement> listGenres = null;
                if (genres != null) {
                    listGenres = new ArrayList<MenuLibraryElement>();
                    for (int i = 0; i < genres.length; i++) {
                        Genre genre = genres[i];
                        listGenres.add(new MenuLibraryElement(genre.getId(), genre.getName(), null, MenuEvent.GENRES_MOVIE));
                    }
                }
                List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();
                MenuLibraryElement menuTitle = new MenuLibraryElement(-1, mMediaLibraryActivity.getResources().getString(R.string.titles), null, MenuEvent.TITLES);
                MenuLibraryElement menuGenres = new MenuLibraryElement(-2, mMediaLibraryActivity.getResources().getString(R.string.genres), listGenres, MenuEvent.SHOW_GENRES);
                MenuLibraryElement menuActors = new MenuLibraryElement(-3, mMediaLibraryActivity.getResources().getString(R.string.actors), null, MenuEvent.ACTORS);
                MenuLibraryElement menuDirectors = new MenuLibraryElement(-4, mMediaLibraryActivity.getResources().getString(R.string.directors), null, MenuEvent.DIRECTORS);
                MenuLibraryElement menuYears = new MenuLibraryElement(-5, mMediaLibraryActivity.getResources().getString(R.string.years), null, MenuEvent.YEARS);
                MenuLibraryElement menuFavourites = new MenuLibraryElement(-6, mMediaLibraryActivity.getResources().getString(R.string.favourites), null, MenuEvent.FAVOURITES_MOVIE);
//            MenuLibraryElement menuFavourites = null;
//            final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mMediaLibraryActivity);
//            boolean mode = sp.getBoolean("default_favourite", true);
//            if (mode) {
//            	menuFavourites = new MenuLibraryElement(-6, mMediaLibraryActivity.getResources().getString(R.string.lable_movie_favourite), null, MenuEvent.FAVOURITES_MOVIE);
//            }else {
//            	menuFavourites = new MenuLibraryElement(-6, mMediaLibraryActivity.getResources().getString(R.string.lable_movie_my), null, MenuEvent.FAVOURITES_MOVIE);
//            }
                mMenuOptions.add(menuTitle);
                mMenuOptions.add(menuGenres);
                mMenuOptions.add(menuActors);
                mMenuOptions.add(menuDirectors);
                mMenuOptions.add(menuYears);
                mMenuOptions.add(menuFavourites);
                int px = DisplayUtil.dip2px(mMediaLibraryActivity.getApplicationContext(), subMenuWidth);
                mMediaLibraryActivity.addMenuLibraryFragment(mMediaLibraryActivity.getResources()
                        .getString(R.string.movies), mMenuOptions, px);// show movie sort


            }
        });
    }


    /**
     * @param movieAddedEvent
     */
    public void onEvent(MovieAddedEvent movieAddedEvent) {
        mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // refresh screen appropriately depending on which type of content
                // the user is on
                mGridAdapter.setmProgressMediaLoading(0);

                getMovieByTitles();
                // mGridAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * UIInteraction
     */

    public void onEvent(AutoCompleteEvent autoCompleteEvent) {
        switch (autoCompleteEvent.getmMenuEvent()) {
            case ACTORS:
                initGetParameters();
                // Person person = (Person) autoCompleteEvent.getmMedia();
                // mfirstName = person.getFirstName();
                // mLastName = person.getLastName();
                // searchString = person.fullName();
                searchString = autoCompleteEvent.getSearchString();
                getActors();
                getMovieActorAlphaListing();
                mGridAdapter.cleanup();
                displayOnScreen(0);
                break;
            case DIRECTORS:
                initGetParameters();
                // person = (Person) autoCompleteEvent.getmMedia();
                // mfirstName = person.getFirstName();
                // mLastName = person.getLastName();
                // searchString = person.fullName();
                searchString = autoCompleteEvent.getSearchString();
                getDirectors();
                getMovieDirectorAlphaListing();
                mGridAdapter.cleanup();
                displayOnScreen(0);
                break;
            case FAVOURITES_MOVIE:
                initGetParameters();
                searchString = autoCompleteEvent.getSearchString();
                getFavourites();
                getMovieAlphaListing();
                // mMediaLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                mGridAdapter.cleanup();
                displayOnScreen(0);
                break;
            case GENRES_MOVIE:
            case TITLES:
                initGetParameters();
                searchString = autoCompleteEvent.getSearchString();
                this.getMovieLite();
                getMovieAlphaListing();
                // mMediaLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                mGridAdapter.cleanup();
                displayOnScreen(0);
                break;

            case YEARS:
                initGetParameters();
                searchString = autoCompleteEvent.getSearchString();
                getMovieByYears();
                getMovieYearDecades();
                // mMediaLibraryActivity.setAutoCompleteEvent(mCurrentEvent);
                mGridAdapter.cleanup();
                displayOnScreen(0);

                break;
            default:
                Log.d(TAG, "Default Event");
                break;
        }
    }

    /**
     * @param nowPlayingEvent
     */
    public void onEvent(PlayingMovieChangedEvent nowPlayingEvent) {
        mMediaLibraryActivity.addNowPlayingFragment(nowPlayingEvent.getMedia());
    }

    public void onEvent(GetPlayingMovieHandler getPlayingMovieHandler) {
        mMediaLibraryActivity.addNowPlayingFragment(getPlayingMovieHandler.getMovieLite());
    }

    /**
     * @param nowPlayingEvent
     */
    public void onEvent(PlayingMusicTrackChangedEvent nowPlayingEvent) {
        mMediaLibraryActivity.addNowPlayingFragment(nowPlayingEvent.getmMusicAlbum());
    }

    public void onEvent(GetPlayingMusicTrackHandler getPlayingMusicTrackHandler) {
        mMediaLibraryActivity.addNowPlayingFragment(getPlayingMusicTrackHandler.getmMusicAlbum());
    }

    public void onEvent(NowPlayingChangedEvent nowPlayingChangedEvent) {
        mMediaLibraryActivity.addNowPlayingFragment(nowPlayingChangedEvent.getInput());
    }

    public void getNowPlayingStatus() {
        this.mEventBus.post(new GetNowPlayingInputHandler().getRequest());
    }

    public void onEvent(GetNowPlayingInputHandler getNowPlayingInputHandler) {
        Input nowPlaying = getNowPlayingInputHandler.getNowPlayingInput();
        if (nowPlaying == null) {
//            mMediaLibraryActivity.removeMediaFragment();
            return;
        }
        switch (nowPlaying.getDeviceKind()) {
            case Zaxel:
                mEventBus.post(new GetPlayingMovieHandler().getRequest());
                break;
            case Movie:
                mEventBus.post(new GetPlayingMovieHandler().getRequest());
                break;
            case Music:
                // IPT Service should retrieve the Playing Media and send the event
                mEventBus.post(new GetPlayingMusicTrackHandler().getRequest());
                break;

            default:
                mMediaLibraryActivity.addNowPlayingFragment(nowPlaying);
                break;
        }
    }

    /**
     * @param
     */
    public void onEvent(MediaLoadProgressChangedEvent mediaLoadProgressChangedEvent) {
        mGridAdapter.setmProgressMediaLoading(mediaLoadProgressChangedEvent.getmPorcentage());
        mMediaLibraryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int index = getLoadingMediaIndex(listViewDisplayStartIndex, listViewDisplayCount);
                if (index >= 0) {
                    View view = mMediaLibraryActivity.getGridView().getChildAt(index - listViewDisplayStartIndex);
                    mGridAdapter.getView(index, view, mMediaLibraryActivity.getGridView());
                }

                // mGridAdapter.invalidateLoadingViewHolder();
                // mGridAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * Getters and Setters
     */
    public ArrayList<Media> getMedias() {
        return mMedias;
    }

    // /**
    // *
    // * @param mMedias
    // */
    // public void setMedias(ArrayList<Media> mMedias)
    // {
    // this.mMedias = mMedias;
    // }

    /**
     * @return
     */
    public int getCount() {
        return count;
    }

    public int getmSize() {
        return mSize;
    }

    public void setmSize(int mSize) {
        this.mSize = mSize;
    }

    public boolean ismIsLoading() {
        return mIsLoading;
    }

    public void setmIsLoading(boolean mIsLoading) {
        this.mIsLoading = mIsLoading;
    }

    // public boolean ismShareButton()
    // {
    // return mShareButton;
    // }
    //
    // public void setmShareButton(boolean mShareButton)
    // {
    // this.mShareButton = mShareButton;
    // }

    public MenuEvent getmCurrentEvent() {
        return mCurrentEvent;
    }

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
}
