package com.tgf.kcwc.tripbook;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.fragments.TabPlayHomeFragment;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/11 0011
 * E-mail:hekescott@qq.com
 */

public class TripBookeListFragment extends BaseFragment {

    private MyPagerAdapter mPagerAdapter;
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;


    public TabPlayHomeFragment tabPlayHomeFragment;
    public int sektype = 1;
    private MainActivity activity;
    private List<BaseFragment> fragments;

    public TripBookeListFragment(TabPlayHomeFragment tabPlayHomeFragm, int type){
        this.tabPlayHomeFragment=tabPlayHomeFragm;
        this.sektype = type;
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tripbooklist;
    }

    @Override
    protected void initView() {
        findView(R.id.play_backiv).setOnClickListener(this);
        findView(R.id.play_searchiv).setOnClickListener(this);
        mTabs = findView(R.id.tabs);
        mPager = findView(R.id.pager);

        fragments = new ArrayList<BaseFragment>();
        fragments.add(new RecomentFrag());
        fragments.add(new FindFrag());
        fragments.add(new SameCityFrag());
        mPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), fragments,
                getResources().getStringArray(R.array.play_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();
    }

    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        mTabs.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        // mTabs.setDividerColor(getResources().getColor(R.color.color_80cbc4));
        // 设置分割线的上下的间距,传入的是dp
        mTabs.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        mTabs.setUnderlineHeight(0);
        //设置Tab底部线的颜色
        mTabs.setUnderlineColor(getResources().getColor(R.color.split_line_color));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTabs.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        mTabs.setIndicatorColorResource(R.color.tab_indicator_s_line);

        // 设置Tab标题文字的大小,传入的是dp
        mTabs.setTextSize(16);
        // 设置选中Tab文字的颜色
        mTabs.setSelectedTextColorResource(R.color.text_color11);
        //设置正常Tab文字的颜色
        mTabs.setTextColorResource(R.color.text_color11);

    }

    @Override
    public void onClick(View v) {
        if (activity == null) {
            activity = (MainActivity) getActivity();
        }
        int id = v.getId();
        switch (id) {
            case R.id.play_backiv:
                if (sektype == 1) {
                    activity.switchToTab(Constants.Navigation.HOME_TAB);
                } else {
                    tabPlayHomeFragment.switchoverPlayTab();
                }
                break;
            case R.id.play_searchiv:
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
                break;

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(fragments!=null){
            fragments.clear();
        }
    }
}
