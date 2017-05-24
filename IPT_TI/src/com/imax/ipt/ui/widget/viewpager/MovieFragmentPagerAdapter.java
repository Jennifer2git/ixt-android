package com.imax.ipt.ui.widget.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanli on 2015/12/2.
 */
public class MovieFragmentPagerAdapter extends FragmentPagerAdapter{
    private List<ViewPagerSubMenuFragment> mTabContents = new ArrayList<ViewPagerSubMenuFragment>();

    public MovieFragmentPagerAdapter(FragmentManager fm, List<?> list) {
        super(fm);
        this.mTabContents = (List<ViewPagerSubMenuFragment>) list;
    }

    @Override
    public Fragment getItem(int i) {
        return mTabContents.get(i);

    }

    @Override
    public int getCount() {
        return mTabContents.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(mTabContents.get(position).getView(), 0);
        ((ViewPager)container).addView(mTabContents.get(position).getView(), 1);
        return super.instantiateItem(container, position);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(mTabContents.get(position).getView());
        super.destroyItem(container, position, object);
    }


}
