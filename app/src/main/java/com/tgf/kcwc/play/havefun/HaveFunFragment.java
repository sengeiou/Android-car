package com.tgf.kcwc.play.havefun;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.fragments.TabPlayHomeFragment;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */

public class HaveFunFragment extends BaseFragment {
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter = null;
    private int mSenseId = -1;
    private int number = 0;

    public TabPlayHomeFragment tabPlayHomeFragment;

    public HaveFunFragment(TabPlayHomeFragment tabPlayHomeFragm) {
        this.tabPlayHomeFragment = tabPlayHomeFragm;
    }

    @Override
    protected void updateData() {
        findView(R.id.title_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabPlayHomeFragment.switchoverPlayTab();
            }
        });
        TextView view = findView(R.id.title_bar_text);
        view.setText(R.string.havefun);
        FunctionView function = findView(R.id.title_function_btn);
        function.setImageResource(R.drawable.btn_search_white);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_havefun;
    }

    @Override
    protected void initView() {
        mTabs = findView(R.id.tabs);
        mPager = findView(R.id.pager);
        mTabs.setBackgroundColor(mRes.getColor(R.color.white));
        mPager.setBackgroundColor(mRes.getColor(R.color.white));
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        NewestListFragment newestListFragment = new NewestListFragment();
        newestListFragment.setSenseId(mSenseId);
        fragments.add(newestListFragment);
        HotListFragment hotListFragment = new HotListFragment();
        hotListFragment.setSenseId(mSenseId);
        fragments.add(hotListFragment);
        mPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), fragments, getResources().getStringArray(R.array.play_havefun_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();
    }


/*    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.havefun);
        function.setImageResource(R.drawable.btn_search_white);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
            }
        });
    }*/


    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        mTabs.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        // mTabs.setDividerColor(getResources().getColor(R.color.color_80cbc4));
        // 设置分割线的上下的间距,传入的是dp
        mTabs.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        mTabs.setUnderlineHeight(0);
        //设置Tab底部线的颜色split_line_color
        mTabs.setUnderlineColor(getResources().getColor(R.color.split_line_color));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTabs.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        mTabs.setIndicatorColorResource(R.color.tab_text_s_color);

        // 设置Tab标题文字的大小,传入的是dp
        mTabs.setTextSize(14);
        // 设置选中Tab文字的颜色
        mTabs.setSelectedTextColorResource(R.color.tab_text_s_color);
        //设置正常Tab文字的颜色
        mTabs.setTextColorResource(R.color.text_color);
    }

}
