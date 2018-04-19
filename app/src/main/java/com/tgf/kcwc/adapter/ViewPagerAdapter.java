package com.tgf.kcwc.adapter;

import java.util.List;

import com.tgf.kcwc.base.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:Jenny
 * Date:16/5/15 16:49
 * E-mail:fishloveqin@gmail.com
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> viewList;
    public ViewPagerAdapter(List<View> viewList)
    {
        this.viewList = viewList;
    }

    @Override



    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
