package com.imax.ipt.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.widget.viewpager.ViewPagerIndicator;
import com.imax.ipt.ui.widget.viewpager.ViewPagerSubMenuFragment;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yanli on 2015/11/19.
 */
public class MovieSortFragment extends Fragment implements View.OnClickListener {

    public static String TAG = "MovieSortFragment";
    private View mViewMovieSortLayout;

    private ImageButton mBtnMasterMenu;
    private IPTTextView mIPTTextViewTitle;
    private IPTTextView mIPTTextViewGenre;
    private IPTTextView mIPTTextViewActor;
    private ImageButton mBtnMasterMenuActive;
    private ImageView mImgMenuBackgroud;
    private ImageButton mBtnPower;
    private EventBus mEventBus;

    private List<Fragment> mTabContents = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<String> mDatas = Arrays.asList("TITLE", "GENRES","ACTOR", "DIRECTOR", "YEAR", "FAVOURITE" );

//    String[] titles = {"title", "actor", "year", "genres", "favorite", "movie"};

    private ViewPagerIndicator mIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mEventBus = IPT.getInstance().getEventBus();

        mViewMovieSortLayout = inflater.inflate(R.layout.fragment_movie_sort, null);
//        mIPTTextViewTitle = (IPTTextView) mViewMovieSortLayout.findViewById(R.id.txtTitleNav);
//        mIPTTextViewGenre = (IPTTextView) mViewMovieSortLayout.findViewById(R.id.txtGenreNav);
//        mIPTTextViewActor = (IPTTextView) mViewMovieSortLayout.findViewById(R.id.txtActorNav);
//        mIPTTextViewTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick:" + v.getId());
//                mIPTTextViewTitle.setTextColor(getResources().getColor(R.color.gold));
//                mIPTTextViewTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//                mIPTTextViewTitle.getPaint().setAntiAlias(true);
//            }
//        });

//        mIPTTextViewGenre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIPTTextViewGenre.setTextColor(getResources().getColor(R.color.gold));
//                mIPTTextViewGenre.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//                mIPTTextViewGenre.getPaint().setAntiAlias(true);
//            }
//        });

//        mIPTTextViewActor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIPTTextViewActor.setTextColor(getResources().getColor(R.color.gold));
//                mIPTTextViewActor.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//            }
//        });

//        initUI(container.getContext(),mViewMovieSortLayout);

        initView(mViewMovieSortLayout);
        initDatas(container.getContext());
        //设置Tab上的标题
//        mIndicator.setTabItemTitles(mDatas);
        mViewPager.setAdapter(mAdapter);
        //设置关联的ViewPager
        mIndicator.setViewPager(mViewPager,0);


        return mViewMovieSortLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
//            case R.id.txtTitleNav:
//                mIPTTextViewTitle.setTextColor(getResources().getColor(R.color.gold));
//                break;
//            case R.id.txtGenreNav:
//                mIPTTextViewGenre.setTextColor(getResources().getColor(R.color.gold));
//            break;

            default:

        }
    }


    private void initDatas(Context context)
    {

        for (String data : mDatas)
        {
            ViewPagerSubMenuFragment fragment = ViewPagerSubMenuFragment.newInstance(data);
            mTabContents.add(fragment);
        }

        mAdapter = new FragmentPagerAdapter(getFragmentManager())
        {
            @Override
            public int getCount()
            {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return mTabContents.get(position);
            }
        };
    }

    private void initView(View layout)
    {
        mViewPager = (ViewPager) layout.findViewById(R.id.id_vp);
        mIndicator = (ViewPagerIndicator) layout.findViewById(R.id.id_indicator);
    }
}
