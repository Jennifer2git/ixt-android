package com.imax.ipt.ui.widget.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.controller.inputs.MovieLibraryController;
import com.imax.ipt.ui.widget.gridview.GridAdapter;
import com.imax.ipt.ui.widget.horizontallistview.HorizontalListView;
import com.imax.ipt.ui.widget.horizontallistview.HorizontalListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerSubMenuFragment extends Fragment
{
	public static final String BUNDLE_TITLE = "title";
	private static final String TAG = ViewPagerSubMenuFragment.class.getSimpleName();
	private String mTitle = "TITLE";

	private HorizontalListView hListView;
	private HorizontalListViewAdapter hListViewAdapter;

	private android.support.v4.view.ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;

//	private TwoWayGridWidget mGridView;
	public  GridView mGridView;
	private GridAdapter mGridAdapter;

//	private ViewPagerAdapter adapter;
	private View view1, view2, view3;
	private int oldPosition = 0;// 记录上一次点的位置
	private int currentItem; // 当前页面
	private List<View> viewList = new ArrayList<View>();// 把需要滑动的页卡添加到这个list中

	private MovieLibraryController mMediaLibraryModel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Bundle arguments = getArguments();
		if (arguments != null)
		{
			mTitle = arguments.getString(BUNDLE_TITLE);
		}

		View secondNavBarLayout = inflater.inflate(R.layout.catalogue_second_nav_bar, null);
		mGridView = (GridView) secondNavBarLayout.findViewById(R.id.grid_movie_item);
		TextView test = (TextView) secondNavBarLayout.findViewById(R.id.test);
		test.setText(mTitle + "");

//		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Log.d(TAG,"cll gridview onItemClick");
//			}
//		});
//		mGridAdapter = new GridAdapter(this, R.layout.item_movie_library, mMedias, R.drawable.catalogue_noposter_bg, 115, 175, 5, 9);

//		mGridView.setSelector(getResources().getDrawable(android.R.color.transparent));
//		mGridView.setAdapter(mGridAdapter);

//		mMediaLibraryModel.init();

//		View secondNavBarLayout = inflater.inflate(R.layout.activity_input_movie_library, null);
//		TwoWayGridWidget mGridView = (TwoWayGridWidget) secondNavBarLayout.findViewById(R.id.gridview);
//		mGridView.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
//				{
//					Log.d(TAG,"gridview onItemClick");
//
//					if (position >= mMediaLibraryModel.getDummyCount() &&
//							!mMediaLibraryModel.getMedias().get(position).isLoading()) {
//						// open movie dialog if the movie is not being load
////						showDialogMediaDescription(position);
//					}
//					if (position >= mMediaLibraryModel.getDummyCount() && mMediaLibraryModel.getMedias().get(position).isLoading()) {
//						Media media = mMediaLibraryModel.getMedias().get(position);
//						if (media != null) {
//							int inputId = 1;
//							MovieRemoteFullActivity.fire(container.getContext(), media.getId(), media.getTitle(), inputId);
//
//						}
//					}
//				}
//			}
//		});
//		mGridView.setOnScrollListener(new TwoWayAbsListView.OnScrollListener() {
//			@Override
//			public void onScrollStateChanged(TwoWayAbsListView view, int scrollState) {
//				Log.d(TAG,"gridview onScrollStateChanged");
//			}
//
//			@Override
//			public void onScroll(TwoWayAbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				Log.d(TAG,"gridview onScroll");
//				mMediaLibraryModel.loadView(firstVisibleItem, visibleItemCount);
//				mMediaLibraryModel.checkIsLoadingMedia(firstVisibleItem, visibleItemCount);
//			}
//		});
//		mGridView.setSelector(getResources().getDrawable(android.R.color.transparent));



//
//		String[] subMenu = {"A","B","C","D","E","A","B","C","D","E","A","B","C","D","E"};
//		String[] subMenu2 = {"2015","2014","2013","2012","2011","2010"};
//
//		if(mTitle.equals(getResources().getString(R.string.titles_nav))){
//			Log.d(TAG,"onCreateView:" + mTitle);
////			initUI(container.geta(), secondNavBarLayout, subMenu);
////			initViewPager(container);
//		}else if (mTitle.equals(getResources().getString(R.string.genres_nav))){
//			Log.d(TAG,"onCreateView:" + mTitle);
////			initUI(container.getContext(),secondNavBarLayout, subMenu2);
////			initViewPager(container);
//		}else if (mTitle.equals(getResources().getString(R.string.actors_nav))){
//			Log.d(TAG,"onCreateView:" + mTitle);
////			initUI(container.getContext(),secondNavBarLayout, subMenu2);
////			initViewPager(container);
//		}else if (mTitle.equals(getResources().getString(R.string.directors_nav))){
////			initUI(container.getContext(),secondNavBarLayout, subMenu2);
//		}else if (mTitle.equals(getResources().getString(R.string.years_nav))){
////			initUI(container.getContext(),secondNavBarLayout, subMenu2);
//		}else if (mTitle.equals(getResources().getString(R.string.favorites_nav))){
////			initUI(container.getContext(),secondNavBarLayout, subMenu2);
//		}

		return secondNavBarLayout;
	}

	public static ViewPagerSubMenuFragment newInstance(String title)
	{
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, title);
		ViewPagerSubMenuFragment fragment = new ViewPagerSubMenuFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	public GridView getmGridView(){
		return this.mGridView;
	}

	public void setmGridView(GridView mGridView) {
		this.mGridView = mGridView;
	}

	public void initUI(Context context,View layout, String[] subMenu){

		hListView = (HorizontalListView) layout.findViewById(R.id.list_movie_sub_sort);
//		previewImg = (ImageView)findViewById(R.id.image_preview);
		String[] titles = {"title", "actor", "year", "genres", "favorite", "movie"};
		titles = subMenu;
//		hListViewAdapter = new HorizontalListViewAdapter(getApplicationContext(),titles,ids);
//		hListViewAdapter = new HorizontalListViewAdapter(context,subMenu);
		hListView.setAdapter(hListViewAdapter);
		//		hListView.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		//			@Override
		//			public void onItemSelected(AdapterView<?> parent, View view,
		//					int position, long id) {
		//				// TODO Auto-generated method stub
		//				if(olderSelected != null){
		//					olderSelected.setSelected(false); //上一个选中的View恢复原背景
		//				}
		//				olderSelected = view;
		//				view.setSelected(true);
		//			}
		//
		//			@Override
		//			public void onNothingSelected(AdapterView<?> parent) {
		//				// TODO Auto-generated method stub
		//
		//			}
		//		});
		hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
//				if(olderSelectView == null){
//					olderSelectView = view;
//				}else{
//					olderSelectView.setSelected(false);
//					olderSelectView = null;
//				}
//				olderSelectView = view;
//				view.setSelected(true);
				hListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						//TODO ：do refresh the screen
						Log.d(TAG, "onItemSelected;" + "the item position :" + position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
				hListViewAdapter.setSelectIndex(position);
				hListViewAdapter.notifyDataSetChanged();

			}
		});


	}


	private void initViewPager(View layout){
//		mViewPager = (ViewPager) layout.findViewById(R.id.viewpager_movie_content);
//		TextView txt = (TextView)layout.findViewById(R.id.txt_test);
		mAdapter = new FragmentPagerAdapter(getFragmentManager())
		{
			@Override
			public int getCount()
			{
				return 0;//mTabContents.size();
			}

			@Override
			public Fragment getItem(int position)
			{
				return null;//mTabContents.get(position);
			}
		};
//		mViewPager.setAdapter(mAdapter);
//		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//			@Override
//			public void onPageScrolled(int i, float v, int i1) {
//				Log.d(TAG,"onPageScrolled");
//			}
//
//			@Override
//			public void onPageSelected(int i) {
//				Log.d(TAG,"onPageSelected");
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int i) {
//				Log.d(TAG,"onPageScrollStateChanged");
//			}
//		});
	}

}
