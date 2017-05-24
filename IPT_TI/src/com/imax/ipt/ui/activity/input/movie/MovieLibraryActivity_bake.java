//package com.imax.ipt.ui.activity.input.movie;
//
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.*;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.*;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import com.imax.ipt.IPT;
//import com.imax.ipt.R;
//import com.imax.ipt.controller.eventbus.handler.push.MovieAddedEvent;
//import com.imax.ipt.controller.eventbus.handler.push.MovieDeletedEvent;
//import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
//import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
//import com.imax.ipt.controller.inputs.MovieLibraryController;
//import com.imax.ipt.model.Media;
//import com.imax.ipt.ui.activity.BaseActivity;
//import com.imax.ipt.ui.fragments.MovieSortFragment;
//import com.imax.ipt.ui.layout.MenuView;
//import com.imax.ipt.ui.util.Blur;
//import com.imax.ipt.ui.widget.gridview.TwoWayAbsListView;
//import com.imax.ipt.ui.widget.gridview.TwoWayAbsListView.OnScrollListener;
//import com.imax.ipt.ui.widget.gridview.TwoWayAdapterView;
//import com.imax.ipt.ui.widget.gridview.TwoWayAdapterView.OnItemClickListener;
//import com.imax.ipt.ui.widget.gridview.TwoWayGridWidget;
//import com.imax.ipt.ui.widget.horizontallistview.HorizontalListView;
//import com.imax.ipt.ui.widget.horizontallistview.HorizontalListViewAdapter;
//import com.imax.ipt.ui.widget.picker.Picker;
//import com.imax.ipt.ui.widget.viewpager.MovieFragmentPagerAdapter;
//import com.imax.ipt.ui.widget.viewpager.ViewPagerSubMenuFragment;
//import com.imax.iptevent.EventBus;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Rodrigo Lopez
// */
//public class MovieLibraryActivity_bake extends BaseActivity implements OnItemClickListener, OnScrollListener, OnClickListener {
//    private static final String TAG = "MediaLibraryActivity";
//    private static final String ACTION_MOVIE_LIBRARY = "com.imax.ipt.ui.activity.input.movie.show_movie_library";
//    private static final int DEFAULT_PAGER_TITLE = 0;
//    private TwoWayGridWidget mGridView;
////    private GridView mGridView;
//    private MovieLibraryController mMediaLibraryModel;
//    private ImageButton mShareButton;
//    private EventBus mEventBus;
//
//    private List<Fragment> mTabContents = new ArrayList<Fragment>();
//    LayoutInflater mLayoutInflater;
//    LinearLayout mNumLayout;
//    Button mPreSelectedBt;
//    private MovieFragmentPagerAdapter mMoviePagerAdapter;
//    private ViewPager mMovieViewPager;
//    private ViewPagerSubMenuFragment mMovieItemFragment;
//
////    private List<String> mDatas = Arrays.asList("TITLE", "GENRES", "ACTOR", "DIRECTOR", "YEAR", "FAVORITE");
//    private List<String[]> mDatas = new ArrayList<String[]>();
//    private String[] menu = {"TITLE", "GENRES", "ACTOR", "DIRECTOR", "YEAR", "FAVORITE"};
//
//    private HorizontalListView hListViewMenu;
//    private HorizontalListViewAdapter hListViewAdapterMenu;
//    private HorizontalListView hListView;
//    private HorizontalListViewAdapter hListViewAdapter;
//
//    /**
//     *
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_input_movie_library);
//        this.init();
//        this.mMediaLibraryModel.setmCurrentEvent(MenuEvent.TITLES);
//        mMediaLibraryModel.getNowPlayingStatus();
//    }
//
//    private void initItemViewPager() {
//        mLayoutInflater = getLayoutInflater();
////        for(){
//            ViewPagerSubMenuFragment fragment = ViewPagerSubMenuFragment.newInstance("title1");
//            ViewPagerSubMenuFragment fragment2 = ViewPagerSubMenuFragment.newInstance("title2");
//            ViewPagerSubMenuFragment fragment3 = ViewPagerSubMenuFragment.newInstance("title3");
//            ViewPagerSubMenuFragment fragment4 = ViewPagerSubMenuFragment.newInstance("title4");
//            mTabContents.add(fragment);
//            mTabContents.add(fragment2);
//            mTabContents.add(fragment3);
//            mTabContents.add(fragment4);
//
////        }
//        mMovieViewPager = (ViewPager)findViewById(R.id.vp_movie_item);
//        mMoviePagerAdapter = new MovieFragmentPagerAdapter(this.getSupportFragmentManager(),mTabContents) ;
//        mMoviePagerAdapter.notifyDataSetChanged();// notify data change
//        mMovieViewPager.setAdapter(mMoviePagerAdapter);
//
//        mNumLayout = (LinearLayout) findViewById(R.id.layout_pager_dot);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.catalogue_pagination_notselected);
//        for (int i = 0; i < mTabContents.size(); i++) {
//            Button bt = new Button(this);
//            bt.setLayoutParams(new ViewGroup.LayoutParams(bitmap.getWidth(),bitmap.getHeight()));
//            bt.setBackgroundResource(R.drawable.catalogue_pagination_notselected);
//            mNumLayout.addView(bt);
//        }
//
//        mMovieViewPager.setCurrentItem(0);
//        mMovieViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//                Log.d(TAG,"onPageScrolled");
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.d(TAG,"onPageSelected :" + position);
//                if(mPreSelectedBt != null){
//                    mPreSelectedBt.setBackgroundResource(R.drawable.catalogue_pagination_notselected);
//                }
//
//                Button currentBt = (Button)mNumLayout.getChildAt(position);
//                currentBt.setBackgroundResource(R.drawable.catalogue_pagination_selected);
//                mPreSelectedBt = currentBt;
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//                Log.d(TAG,"onPageScrollStateChanged");
//            }
//        });
//
//    }
//
//
//
//    /**
//     *
//     */
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mMediaLibraryModel.onActivityResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mMediaLibraryModel.onActivityPause();
//    }
//
//    /**
//     * @param context
//     */
//    public static void fire(Context context) {
//        Log.d(TAG, "Starting MovieLibraryActivity...");
//        Intent intent = new Intent(context, MovieLibraryActivity_bake.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        context.startActivity(intent);
//
//    }
//
//    /**
//     * @param context
//     */
//    public static void fireToFront(Context context) {
//        Intent intent = new Intent(context, MovieLibraryActivity_bake.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        context.startActivity(intent);
//    }
//
//    /**
//     *
//     */
//    protected void init() {
//
////        this.addMenuFragment();//main menu same with navigation
//        this.addAutomplete(MenuEvent.TITLES);
//        this.addPickerFragment(10, 10);//abcd,,,z
//
//        //add by jennifer begin.
//        this.addNavgationMenuFragment();// nav main menu add by jennifer
////        addMovieSortFragment();
//        //add by jennifer end.
////        mMovieItemFragment =  ViewPagerSubMenuFragment.newInstance("title");
////        mMovieItemFragment.getmGridView();
//        initItemViewPager();
//        this.initGrid();
//        Log.d(TAG,"CLL IS gridview null ?" + mMovieItemFragment);
//        this.mMediaLibraryModel = new MovieLibraryController(this, mAutoCompleteFragment,mMovieItemFragment );
//
//        this.mMediaLibraryModel.init();
////        this.mShareButton = (ImageButton) findViewById(R.id.shareButton);
////        this.mShareButton.setOnClickListener(this);
////      mShareButton.setVisibility(View.VISIBLE);
//        //begain added by jennifer
//        this.mEventBus = IPT.getInstance().getEventBus();
//        this.mEventBus.register(this);
//
////        ImageButton mBtnPreviousMenu;
////        mBtnPreviousMenu = (ImageButton) findViewById(R.id.btnPreviousMenu);
////        mBtnPreviousMenu.setOnClickListener(this);
////        IPTTextView previousMenuTextView = (IPTTextView) findViewById(R.id.previous_menu);
////        previousMenuTextView.setVisibility(View.VISIBLE);
////        mBtnPreviousMenu.setVisibility(View.VISIBLE);
////        mBtnPreviousMenu.setOnClickListener(this);
//
////      applyBlur();
//
//    }
//
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
//
////    public void setMovieSortOptionForPickerFragment(String[] options, int padding, int textSize) {
////        Picker mPicker = new Picker();
////        Bundle bundle = new Bundle();
////        bundle.putInt(Picker.PADDING, padding);
////        bundle.putInt(Picker.TEXTSIZE, textSize);
////        bundle.putStringArray(Picker.KEY_OPTIONS, options);
////        mPicker.setArguments(bundle);
////        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////        transaction.replace(R.id.alphabetPickerFragment, mPicker);
////        transaction.commitAllowingStateLoss();
////    }
//
//    public void applyBlur() {
//        if (mMenuLibraryFragment == null) {
//            return;
//        }
//        final MenuView menuView = mMenuLibraryFragment.getMenuView();
//
//        if (menuView == null) {
//            return;
//        }
////       menuView.setAlpha(1f);
//        mGridView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                mGridView.getViewTreeObserver().removeOnPreDrawListener(this);
//                mGridView.setDrawingCacheEnabled(true);
//                mGridView.buildDrawingCache();
//
//                Bitmap bmp = mGridView.getDrawingCache();
//                blur(bmp, menuView);
//                return true;
//            }
//        });
//    }
//
//    @SuppressLint("NewApi")
////    private void blur(Bitmap bkg, View view) {
//    public void blur(Bitmap bkg, View view) {
//        long startMs = System.currentTimeMillis();
//        float scaleFactor = 16;
//        float radius = 2;
////       if (downScale.isChecked()) {
////           scaleFactor = 8;
////           radius = 2;
////       }
//
////       Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
////               (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
//        Bitmap overlay = drawableToBitmap(view.getBackground(), view);
//        Log.d("water", "overlay=" + overlay);
////       overlay.setHeight((int) (view.getMeasuredHeight()/scaleFactor));
////       overlay.setWidth((int) (view.getMeasuredWidth()/scaleFactor));
//
//        Canvas canvas = new Canvas(overlay);
//        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
//        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
//        Paint paint = new Paint();
//        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
//        canvas.drawBitmap(bkg, 0, 200, paint);
//
////       overlay = FastBlur.doBlur(overlay, (int)radius, true);
//
//        overlay = Blur.fastblur(getApplicationContext(), overlay, (int) radius, true);
//        view.setBackground(new BitmapDrawable(getResources(), overlay));
//
////       statusText.setText(System.currentTimeMillis() - startMs + "ms");
//    }
//
//    public static Bitmap drawableToBitmap(Drawable drawable, View view) {
//        // ȡ drawable �ĳ���
//        int w = drawable.getIntrinsicWidth();
//        int h = drawable.getIntrinsicHeight();
//
//        w = (int) (view.getMeasuredWidth() / 16);
//        h = (int) (view.getMeasuredHeight() / 16);
//
//        // ȡ drawable ����ɫ��ʽ
//        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                : Bitmap.Config.RGB_565;
//        // ������Ӧ bitmap
//        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
//        // ������Ӧ bitmap �Ļ���
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, w, h);
//        // �� drawable ���ݻ���������
//        drawable.draw(canvas);
//        return bitmap;
//    }
//
//
//    /**
//     *
//     */
//    private void initGrid() {
//        mGridView = (TwoWayGridWidget) findViewById(R.id.gridview);
//        mGridView.setOnItemClickListener(this);
//        mGridView.setOnScrollListener(this);
//        mGridView.setSelector(getResources().getDrawable(android.R.color.transparent));
////        initItemViewPager();
//
//    }
//
//    /**
//     * @param position
//     */
//    private void showDialogMediaDescription(int position) {
//        Media media;
//        try {
//            media = mMediaLibraryModel.getMedias().get(position);
//        } catch (Exception e) {
//            Log.w(TAG, e.toString());
//            return;
//        }
//        if (media != null) {
//            Bundle bundle = null;
//            FragmentManager fm = getSupportFragmentManager();
//            if (media.getMediaType() == null) {
//                return;
//            }
//            switch (media.getMediaType()) {
//                case MOVIE:
//                    bundle = new Bundle();
//                    bundle.putString(DialogMovieFragment.MOVIE_ID, media.getId());
//                    DialogMovieFragment mDialogMediaLibraryFragment = new DialogMovieFragment();
//                    mDialogMediaLibraryFragment.setArguments(bundle);
//                    mDialogMediaLibraryFragment.show(fm, "fragment_movie");
//                    break;
//                case ACTOR:
//                    bundle = new Bundle();
//                    bundle.putString(DialogMovieActorsFragment.ACTOR_ID, media.getId());
//                    bundle.putString(DialogMovieActorsFragment.ACTOR_NAME, media.getTitle());
//                    DialogMovieActorsFragment dialogMovieActorsFragment = new DialogMovieActorsFragment();
//                    dialogMovieActorsFragment.setArguments(bundle);
//                    dialogMovieActorsFragment.show(fm, "fragment_actor");
//                    break;
//                case DIRECTOR:
//                    bundle = new Bundle();
//                    bundle.putString(DialogMovieDirectorsFragment.DIRECTOR_ID, media.getId());
//                    bundle.putString(DialogMovieDirectorsFragment.DIRECTOR_NAME, media.getTitle());
//                    DialogMovieDirectorsFragment dialogMovieDirectorsFragment = new DialogMovieDirectorsFragment();
//                    dialogMovieDirectorsFragment.setArguments(bundle);
//                    dialogMovieDirectorsFragment.show(fm, "fragment_directores");
//                    break;
//                default:
//                    break;
//            }
//            // mMediaLibraryModel.displayMediaDetailsOnScreen(media.getId());
//        }
//    }
//
//    /**
//     *
//     */
//    @Override
//    public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
//
///*	   Log.d("water", "onItemClick");
//	  //added by watershao
//	   Media media = new Media();;
//		media.setId("media-"+position);
//		media.setTitle("Title-"+position);
//		media.setCoverArtPath("/cmd/a.jpg");
//		media.setMediaType(MediaType.MOVIE);
//		media.setLoading(false);
//
//
//	      if (media != null)
//	      {
//	    	  Log.d("water", "onItemClick1");
//	         Bundle bundle = null;
//	         FragmentManager fm = getSupportFragmentManager();
//	         if (media.getMediaType() == null)
//	         {
//	            return;
//
//	         }
//	       bundle = new Bundle();
//	       bundle.putString(DialogMovieFragment.MOVIE_ID, media.getId());
//	       DialogMovieFragment mDialogMediaLibraryFragment = new DialogMovieFragment();
//	       mDialogMediaLibraryFragment.setArguments(bundle);
//	       mDialogMediaLibraryFragment.show(fm, "fragment_movie");
//	       Log.d("water", "onItemClick1");
//	      }
//
//
////      if (position >= mMediaLibraryModel.getDummyCount() &&
////            !mMediaLibraryModel.getMedias().get(position).isLoading())
////      {
////         // open movie dialog if the movie is not being load
////         showDialogMediaDescription(position);
////      }
//*/
//        if (position >= mMediaLibraryModel.getDummyCount() &&
//                !mMediaLibraryModel.getMedias().get(position).isLoading()) {
//            // open movie dialog if the movie is not being load
//            showDialogMediaDescription(position);
//        }
//
//
//        //added by watershao
//        if (position >= mMediaLibraryModel.getDummyCount() && mMediaLibraryModel.getMedias().get(position).isLoading()) {
//            Media media = mMediaLibraryModel.getMedias().get(position);
//            if (media != null) {
////	            eventBus.post(new PlayMovieHandler(media.getId()).getRequest());
//
//                // MediaPlayer is not sent in the list
////	            Map<DeviceKind, Vector<Input>> inputsByDeviceKind = (Map<DeviceKind, Vector<Input>>)IPT.getInstance().getIPTContext().get(IPT.INPUTS_BY_DEVICE_KIND);
////	            int inputId = inputsByDeviceKind.get(DeviceKind.Movie).get(0).getId();
//                int inputId = 1;
//
//                MovieRemoteFullActivity.fire(MovieLibraryActivity_bake.this, media.getId(), media.getTitle(), inputId);
//
//            }
//        }
//    }
//
//    /**
//     * @param menuEvent
//     */
//    public void addAutomplete(MenuEvent menuEvent) {
//        this.addAutoCompleteFragment(menuEvent);
//    }
//
//    /**
//     *
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//
////          IPTService.stop(getApplicationContext());
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    /**
//     *
//     */
//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy");
//        super.onDestroy();
//        this.mMediaLibraryModel.onDestroy();
//        this.mEventBus.unregister(this);
//    }
//
//    /**
//     *
//     */
//    @Override
//    public void onScrollStateChanged(TwoWayAbsListView view, int scrollState) {
//        // int position = view.getFirstVisiblePosition() +
//        // mMediaLibraryModel.getDummyCount();
//        int position = view.getFirstVisiblePosition();
//        if (scrollState == SCROLL_STATE_IDLE) {
//            mMediaLibraryModel.displayOnScreen(position);
//        }
//    }
//
//    /**
//     *
//     */
//    @Override
//    public void onScroll(TwoWayAbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//         int dummyDisplayedCount = 0;
//         firstVisibleItem = firstVisibleItem -
//         mMediaLibraryModel.getDummyCount();
//         if (firstVisibleItem < 0)
//         {
//         dummyDisplayedCount = mMediaLibraryModel.getDummyCount() -
//         firstVisibleItem;
//         firstVisibleItem = 0;
//         }
//         visibleItemCount = visibleItemCount + dummyDisplayedCount;
//
//        mMediaLibraryModel.loadView(firstVisibleItem, visibleItemCount);
//        mMediaLibraryModel.checkIsLoadingMedia(firstVisibleItem, visibleItemCount);
//        Log.d(TAG, "first=" + view.getFirstVisiblePosition() + ",shown=");
//    }
//
//    private boolean scroll = false;
//
//    /**
//     * @param index
//     */
//    public void onScroll(final int index) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "smoothScrollToPosition index " + index);
//                 mGridView.setSelection((index - mMediaLibraryModel.getDummyCount()));
//                mGridView.setSelection(index);
//            }
//        });
//    }
//
//    /**
//     * @param shareButton
//     */
//    public void setShareButton(final boolean shareButton) {
//        runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                if (shareButton) {
////               mShareButton.setVisibility(View.VISIBLE);
////                    mShareButton.setVisibility(View.INVISIBLE);
//                } else {
////                    mShareButton.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//    }
//
//    /**
//     * Getters and Setters
//     */
//    public TwoWayGridWidget getGridView() {
//        return mGridView;
//    }
//
////    public GridView getGridView(){
////        return mGridView;
////    }
//
//    /**
//     * @param mGridView
//     */
//    public void setmGridView(TwoWayGridWidget mGridView) {
//        this.mGridView = mGridView;
//    }
//
////    public void setmGridView(GridView mGridView) {
////        this.mGridView = mGridView;
////    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
////            case R.id.shareButton:
////                mMediaLibraryModel.screenShare();
////                break;
//            case R.id.btnPreviousMenu:
//          /*ExecuteRemoteControlHandler need method string name. */
//                this.mEventBus.post(new ExecuteRemoteControlHandler("PreviousMenu").getRequest());
//                MozaxRemoteControlActivity.fire(MovieLibraryActivity_bake.this, "guid", "", 1);
//                break;
//        }
//    }
//
//    public void onEvent(MovieAddedEvent movieAddedEvent) {
//        mMediaLibraryModel.onEvent(movieAddedEvent);
//    }
//
//    public void onEvent(MovieDeletedEvent movieDeletedEvent) {
//        mMediaLibraryModel.onEvent(movieDeletedEvent);
//    };
//
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
//                        Log.d(TAG, "onItemSelected;" + "the menu item position :" + position);
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
//
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
//
//}
