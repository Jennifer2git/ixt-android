package com.imax.ipt.ui.widget.viewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.ui.widget.gridview.GridAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanli on 2015/12/2.
 */
public class MoviePagerAdapter extends PagerAdapter {
    private List<View> mTabContents = new ArrayList<View>();
    private GridView mGridView;
    private GridAdapter mGridAdapter;

    public MoviePagerAdapter( List<?> list) {
        this.mTabContents = (List<View>) list;
    }

    public View getItem(int i) {
        return mTabContents.get(i);

    }

    @Override
    public int getCount() {
        return mTabContents.size();
    }

//    @Override
//    public long getItemId(int position) {
//        return super.getItemId(position);
//    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(mTabContents.get(position), 0);
        TextView txt = (TextView)container.findViewById(R.id.txtHello);

        if(position == 0){
//            txt.setText("hello view pager " +position);
        }
        txt.setText("hello view pager " +position);

//        this.mGridAdapter = new GridAdapter(mMediaLibraryActivity, R.layout.item_movie_library, mMedias, R.drawable.catalogue_noposter_bg, 160, 240, 0, 2);

        mGridView = (GridView) container.findViewById(R.id.grid_movie_item);
//        mGridView.setAdapter(mGridAdapter);
//        mGridView.setOnItemClickListener(this);
//        mGridView.setOnScrollListener(this);
        mGridView.setSelector(container.getResources().getDrawable(android.R.color.transparent));

        return mTabContents.get(position);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(mTabContents.get(position));
//        super.destroyItem(container, position, object);
        this.mGridAdapter.cleanup();
    }


}
