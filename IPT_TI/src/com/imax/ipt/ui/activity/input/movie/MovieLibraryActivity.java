package com.imax.ipt.ui.activity.input.movie;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.push.MovieAddedEvent;
import com.imax.ipt.controller.eventbus.handler.push.MovieDeletedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.controller.inputs.MovieLibraryController;
import com.imax.ipt.model.Input;
import com.imax.ipt.model.Media;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.activity.menu.MenuLibraryFragment_custom;
import com.imax.ipt.ui.layout.MenuView;
import com.imax.ipt.ui.util.Blur;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;
import com.imax.ipt.ui.widget.gridview.TwoWayAbsListView;
import com.imax.ipt.ui.widget.gridview.TwoWayAbsListView.OnScrollListener;
import com.imax.ipt.ui.widget.horizontallistview.HorizontalListView;
import com.imax.ipt.ui.widget.horizontallistview.HorizontalListViewAdapter;
import com.imax.ipt.ui.widget.viewpager.MoviePagerAdapter;
import com.imax.ipt.ui.widget.viewpager.ViewPagerSubMenuFragment;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

//import com.imax.ipt.ui.widget.gridview.TwoWayAdapterView.OnItemClickListener;

/**
 * @author Rodrigo Lopez
 */
//public class MovieLibraryActivity extends BaseActivity implements TwoWayAdapterView.OnItemClickListener, OnScrollListener, OnClickListener {
public class MovieLibraryActivity extends BaseActivity implements AdapterView.OnItemClickListener, OnScrollListener, OnClickListener {
    private static final String TAG = MovieLibraryActivity.class.getSimpleName();
    private static final String ACTION_MOVIE_LIBRARY = "com.imax.ipt.ui.activity.input.movie.show_movie_library";
    private static final int DEFAULT_PAGER_TITLE = 0;
//    private TwoWayGridWidget mGridView;
    private GridView mGridView;
    public MovieLibraryController mMediaLibraryModel;
    private ImageButton mShareButton;
    private ImageButton mBtnBack;
    private ImageButton mBtnRemote;
    private EventBus mEventBus;

//    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<Object> mList = new ArrayList<Object>();

    LayoutInflater mLayoutInflater;
    LinearLayout mNumLayout;
    Button mPreSelectedBt;
//    private MovieFragmentPagerAdapter mMoviePagerAdapter;
    private MoviePagerAdapter mMoviePagerAdapter;
    private ViewPager mMovieViewPager;

    private ViewPagerSubMenuFragment mMovieItemFragment;

//    private List<String> mDatas = Arrays.asList("TITLE", "GENRES", "ACTOR", "DIRECTOR", "YEAR", "FAVORITE");
    private List<String[]> mDatas = new ArrayList<String[]>();
//    private String[] menu = {"TITLE", "GENRES", "ACTOR", "DIRECTOR", "YEAR", "FAVORITE"};

    private HorizontalListView hListViewMenu;
    private HorizontalListViewAdapter hListViewAdapterMenu;
    private HorizontalListView hListView;
    private HorizontalListViewAdapter hListViewAdapter;


    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_movie_library);
        this.init();
        this.mMediaLibraryModel.setmCurrentEvent(MenuEvent.TITLES);

    }



    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaLibraryModel.onActivityResume();
        mMediaLibraryModel.getNowPlayingStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaLibraryModel.onActivityPause();
    }

    /**
     * @param context
     */
    public static void fire(Context context) {
        Intent intent = new Intent(context, MovieLibraryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);

    }

    /**
     * @param context
     */
    public static void fireToFront(Context context) {
        Intent intent = new Intent(context, MovieLibraryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    /**
     *
     */
    protected void init() {
//        this.addMenuFragment("MY MOVIES");//main menu same with navigation
//        this.addMenuFragment(getResources().getString(R.string.menu_name_movie));
//        this.addPickerFragment(5, 10);//abcd,,,z not used again.
        this.addAutomplete(MenuEvent.TITLES);//search icon

        this.addNavgationMenuFragment();// title genere
        mBtnBack = (ImageButton)findViewById(R.id.btnBack);
        mBtnRemote = (ImageButton)findViewById(R.id.btn_remote2);
//        mBtnRemote.setVisibility(View.VISIBLE);
//        mBtnRemote.setOnClickListener(this);

        mMovieItemFragment =  ViewPagerSubMenuFragment.newInstance("title");

        this.mMediaLibraryModel = new MovieLibraryController(this, mAutoCompleteFragment,mMovieItemFragment );
        this.initGrid();
        this.mMediaLibraryModel.init();

        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);
//        this.mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_MOVIE).getRequest());
//      applyBlur();
    }

    /* movie sort : title genre,actor,,,.*/
    public void addMenuLibraryFragment(String title, List<MenuLibraryElement> mMenuOptions, int width) {
        mMenuLibraryFragment = new MenuLibraryFragment_custom();
        mMenuLibraryFragment.setmTitle(title);
//        mMenuLibraryFragment.setWidth(width);
        mMenuLibraryFragment.setmMenuOptions(mMenuOptions);
        Log.d(TAG,"the mMenuOptions is " + mMenuOptions.indexOf(0));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.menuLibraryFragment, mMenuLibraryFragment);
        transaction.commitAllowingStateLoss();
    }

//    public void addMovieSortFragment() {
//        Bundle bundle = new Bundle();
////        bundle.putInt(Picker.PADDING, padding);
////        bundle.putInt(Picker.TEXTSIZE, textSize);
//        String[] options = {"title","actor","genres","year","director"};
//        bundle.putStringArray(Picker.KEY_OPTIONS, options);
////        mPicker.setArguments(bundle);
//        mMovieSortFragment = new MovieSortFragment();
//        mMovieSortFragment.setArguments(bundle);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.menuLibraryFragment, mMovieSortFragment);
//        transaction.commitAllowingStateLoss();
//    }



    public void applyBlur() {
        if (mMenuLibraryFragment == null) {
            return;
        }
        final MenuView menuView = mMenuLibraryFragment.getMenuView();

        if (menuView == null) {
            return;
        }
//       menuView.setAlpha(1f);
        mGridView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mGridView.getViewTreeObserver().removeOnPreDrawListener(this);
                mGridView.setDrawingCacheEnabled(true);
                mGridView.buildDrawingCache();

                Bitmap bmp = mGridView.getDrawingCache();
                blur(bmp, menuView);
                return true;
            }
        });
    }

    @SuppressLint("NewApi")
    public void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 16;
        float radius = 2;
//       if (downScale.isChecked()) {
//           scaleFactor = 8;
//           radius = 2;
//       }

//       Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
//               (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Bitmap overlay = drawableToBitmap(view.getBackground(), view);
        Log.d("water", "overlay=" + overlay);
//       overlay.setHeight((int) (view.getMeasuredHeight()/scaleFactor));
//       overlay.setWidth((int) (view.getMeasuredWidth()/scaleFactor));

        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 200, paint);

//       overlay = FastBlur.doBlur(overlay, (int)radius, true);

        overlay = Blur.fastblur(getApplicationContext(), overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));

//       statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }

    public static Bitmap drawableToBitmap(Drawable drawable, View view) {
        // ȡ drawable �ĳ���
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        w = (int) (view.getMeasuredWidth() / 16);
        h = (int) (view.getMeasuredHeight() / 16);

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

//    private void initViewPager(){
////
////        ViewPagerSubMenuFragment mMovieItemFragment2 =  ViewPagerSubMenuFragment.newInstance("title2");
////        ViewPagerSubMenuFragment mMovieItemFragment3 =  ViewPagerSubMenuFragment.newInstance("title3");
////        mFragmentList.add(mMovieItemFragment);
////        mFragmentList.add(mMovieItemFragment2);
////        mFragmentList.add(mMovieItemFragment3);
////        mMoviePagerAdapter = new MovieFragmentPagerAdapter(getSupportFragmentManager(),mFragmentList);
//
////查找布局文件用LayoutInflater.inflate
//        LayoutInflater inflater =getLayoutInflater();
//        View view1 = inflater.inflate(R.layout.grid_item_movie_content, null);
//        View view2 = inflater.inflate(R.layout.grid_item_movie_content, null);
//        View view3 = inflater.inflate(R.layout.grid_item_movie_content, null);
////        View view2 = inflater.inflate(R.layout.item02, null);
////        View view3 = inflater.inflate(R.layout.item03, null);
//        mList.add(view1);
//        mList.add(view2);
//        mList.add(view3);
//
////        initGrid(view1);
////        initGrid(view2);
////        initGrid(view3);
//        mMoviePagerAdapter = new MoviePagerAdapter(mList);
//        mMovieViewPager = (ViewPager)findViewById(R.id.vp_movie_content);
//        mMovieViewPager.setAdapter(mMoviePagerAdapter);
//
//    }

    /**
     *
     */
    private void initGrid() {
//        mGridView = (TwoWayGridWidget) findViewById(R.id.gridview);
        mGridView = (GridView) findViewById(R.id.grid_movie_item);
        mGridView.setOnItemClickListener(this);
//        mGridView.setOnScrollListener(this);
        mGridView.setSelector(getResources().getDrawable(android.R.color.transparent));

    }

    /**
     * @param position
     */
    private void showDialogMediaDescription(int position) {
        Media media;
        final int p = position % 7;
        try {
            media = mMediaLibraryModel.getMedias().get(position);
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            return;
        }
        if (media != null) {
            Bundle bundle = null;
            FragmentManager fm = getSupportFragmentManager();
            if (media.getMediaType() == null) {
                return;
            }
            switch (media.getMediaType()) {
                case MOVIE:
                    Location location = Location.LIFT_T;
                    if (p>3 && position>6 ){ //right down
                        //update
                        location = Location.LIFT_B;
                    }else if(p> 3 && position < 6){
                        location = Location.RIGHT_T;
                    }else if(p< 3 && position >6){
                        location = Location.LIFT_B;
                    }

                    bundle = new Bundle();
                    bundle.putString(DialogMovieFragment.MOVIE_ID, media.getId());
                    DialogMovieFragment mDialogMediaLibraryFragment = new DialogMovieFragment();
                    mDialogMediaLibraryFragment.setArguments(bundle);
                    mDialogMediaLibraryFragment.show(fm, "fragment_movie");

                    break;
                case ACTOR:
                    bundle = new Bundle();
                    bundle.putString(DialogMovieActorsFragment.ACTOR_ID, media.getId());
                    bundle.putString(DialogMovieActorsFragment.ACTOR_NAME, media.getTitle());
                    DialogMovieActorsFragment dialogMovieActorsFragment = new DialogMovieActorsFragment();
                    dialogMovieActorsFragment.setArguments(bundle);
                    dialogMovieActorsFragment.show(fm, "fragment_actor");
                    break;
                case DIRECTOR:
                    bundle = new Bundle();
                    bundle.putString(DialogMovieDirectorsFragment.DIRECTOR_ID, media.getId());
                    bundle.putString(DialogMovieDirectorsFragment.DIRECTOR_NAME, media.getTitle());
                    DialogMovieDirectorsFragment dialogMovieDirectorsFragment = new DialogMovieDirectorsFragment();
                    dialogMovieDirectorsFragment.setArguments(bundle);
                    dialogMovieDirectorsFragment.show(fm, "fragment_directores");
                    break;
                default:
                    break;
            }
            // mMediaLibraryModel.displayMediaDetailsOnScreen(media.getId());
        }
    }

    /**
     *
     */
    @Override
//    public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position >= mMediaLibraryModel.getDummyCount() &&
                !mMediaLibraryModel.getMedias().get(position).isLoading()) {
            // open movie dialog if the movie is not being load
            Log.d(TAG,"cll on item click call show dialog");
            showDialogMediaDescription(position);
        }


        //added by watershao
        if (position >= mMediaLibraryModel.getDummyCount() && mMediaLibraryModel.getMedias().get(position).isLoading()) {
            Media media = mMediaLibraryModel.getMedias().get(position);
            if (media != null) {
//	            eventBus.post(new PlayMovieHandler(media.getId()).getRequest());

                // MediaPlayer is not sent in the list
	            Map<FactoryDeviceTypeDrawable.DeviceKind, Vector<Input>> inputsByDeviceKind = (Map<FactoryDeviceTypeDrawable.DeviceKind, Vector<Input>>)IPT.getInstance().getIPTContext().get(IPT.INPUTS_BY_DEVICE_KIND);
	            int inputId = inputsByDeviceKind.get(FactoryDeviceTypeDrawable.DeviceKind.Movie).get(0).getId();
//                int inputId = 2;
                MovieRemoteFullActivity.fire(MovieLibraryActivity.this, media.getId(),media.getTitle(), inputId);
//                MovieRemoteFullActivity.fire(MovieLibraryActivity.this, (Movie)media, inputId);

            }
        }

        //add by jennifer
        // move right to left
        if(position > 3) {

        }

    }

    /**
     * @param menuEvent
     */
    public void addAutomplete(MenuEvent menuEvent) {
        this.addAutoCompleteFragment(menuEvent);
    }

    /**
     *
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

//          IPTService.stop(getApplicationContext());
        }

        return super.onKeyDown(keyCode, event);
    }


    /**
     *
     */
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        this.mMediaLibraryModel.onDestroy();
        this.mEventBus.unregister(this);
    }

    /**
     *
     */
    @Override
    public void onScrollStateChanged(TwoWayAbsListView view, int scrollState) {
        // int position = view.getFirstVisiblePosition() +
        // mMediaLibraryModel.getDummyCount();
        int position = view.getFirstVisiblePosition();
        if (scrollState == SCROLL_STATE_IDLE) {
            mMediaLibraryModel.displayOnScreen(position);
        }
    }

    /**
     *
     */
    @Override
    public void onScroll(TwoWayAbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // int dummyDisplayedCount = 0;
        // firstVisibleItem = firstVisibleItem -
        // mMediaLibraryModel.getDummyCount();
        // if (firstVisibleItem < 0)
        // {
        // dummyDisplayedCount = mMediaLibraryModel.getDummyCount() -
        // firstVisibleItem;
        // firstVisibleItem = 0;
        // }
        // visibleItemCount = visibleItemCount + dummyDisplayedCount;

        mMediaLibraryModel.loadView(firstVisibleItem, visibleItemCount);
        mMediaLibraryModel.checkIsLoadingMedia(firstVisibleItem, visibleItemCount);
    }

    private boolean scroll = false;

    /**
     * @param index
     */
    public void onScroll(final int index) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // //mGridView.setSelection((index -
                // mMediaLibraryModel.getDummyCount()));
                mGridView.setSelection(index);
            }
        });
    }

    /**
     * @param shareButton
     */
    public void setShareButton(final boolean shareButton) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (shareButton) {
//               mShareButton.setVisibility(View.VISIBLE);
//                    mShareButton.setVisibility(View.INVISIBLE);
                } else {
//                    mShareButton.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * Getters and Setters
     */
//    public TwoWayGridWidget getGridView() {
//        return mGridView;
//    }

    public GridView getGridView(){
        return mGridView;
    }

    /**
     * @param
     */
//    public void setmGridView(TwoWayGridWidget mGridView) {
//        this.mGridView = mGridView;
//    }

//    public void setmGridView(GridView mGridView) {
//        this.mGridView = mGridView;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                break;
//            case R.id.shareButton:
//                mMediaLibraryModel.screenShare();
//                break;
            case R.id.btn_remote2:
            case R.id.btnPreviousMenu:
          /*ExecuteRemoteControlHandler need method string name. */
                this.mEventBus.post(new ExecuteRemoteControlHandler("PreviousMenu").getRequest());
                MozaxRemoteControlActivity.fire(MovieLibraryActivity.this, "guid", "", 1);
                break;
        }
    }

    public void onEvent(MovieAddedEvent movieAddedEvent) {
        mMediaLibraryModel.onEvent(movieAddedEvent);
    }

    public void onEvent(MovieDeletedEvent movieDeletedEvent) {
        mMediaLibraryModel.onEvent(movieDeletedEvent);
    };

//    public void initMenuItem(Context context, String[] menu){
//        hListViewMenu = (HorizontalListView) findViewById(R.id.list_movie_sort);
////        hListViewAdapterMenu = new HorizontalListViewAdapter(context,menu);
//        hListViewMenu.setAdapter(hListViewAdapterMenu);
//        hListViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                hListViewMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        //TODO get movie content set the view pager
//                        initSubMenuItem((String[])mDatas.get(position));
////                        mItemViewPager.setCurrentItem(position);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                        Log.d(TAG, "onNothingSelected;" + "the menu item position :" + position);
////                        initSubMenuItem((String[])mDatas.get(position));
////                        mItemViewPager.setCurrentItem(DEFAULT_PAGER_TITLE);
//                    }
//                });
//                hListViewAdapterMenu.setSelectIndex(position);
//                hListViewAdapterMenu.notifyDataSetChanged();
//
//            }
//        });
//    }

//    public void initSubMenuItem( String[] subMenu){
//
//        hListView = (HorizontalListView) findViewById(R.id.list_movie_sub_sort);
////        String[] titles = null;// {"title", "actor", "year", "genres", "favorite", "movie"};
////        hListViewAdapter = new HorizontalListViewAdapter(this,subMenu);
//        hListView.setAdapter(hListViewAdapter);
//        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                hListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        //TODO ��do refresh the screen
//                        Log.d(TAG, "onItemSelected;" + "the item position :" + position);
////                        mItemViewPager.setCurrentItem(position);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                        Log.d(TAG, "onNothingSelected;" + "the item position :" + position);
////                        mItemViewPager.setCurrentItem(DEFAULT_PAGER_TITLE);
//                    }
//                });
//                hListViewAdapter.setSelectIndex(position);
//                hListViewAdapter.notifyDataSetChanged();
//
//            }
//        });
//    }


    private enum Location{
        LIFT_T, LIFT_B, RIGHT_T, RIGHT_B, MIDDLE_T, MIDDLE_B
    }
}
