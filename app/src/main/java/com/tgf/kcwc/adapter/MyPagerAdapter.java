package com.tgf.kcwc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.tgf.kcwc.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:16/5/15 16:49
 * E-mail:fishloveqin@gmail.com
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> mFragments;

    public MyPagerAdapter(FragmentManager fm, List<BaseFragment> list, String[] titles) {
        super(fm);
        this.mFragments = list;
        this.titles = titles;
    }

    public MyPagerAdapter(FragmentManager fm, List<BaseFragment> list, ArrayList<String> titles) {
        super(fm);
        this.mFragments = list;
        String temp[] = new String[titles.size()];
        int index = 0;
        for (String s : titles) {
            temp[index] = s;
            index++;
        }
        this.titles = temp;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    private String[] titles = null;

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

}
