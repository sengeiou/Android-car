package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.WeChatActivity;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.app.plussign.PopupMenuView;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/11 0011
 * E-mail:hekescott@qq.com
 */

public class TripBookeListActivity extends BaseActivity {

    private MyPagerAdapter mPagerAdapter;
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private List<BaseFragment> fragments;
    private View bottomLayout;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
        bottomLayout = findViewById(R.id.triplist_bottll);
        findViewById(R.id.play_backiv).setOnClickListener(this);
        findViewById(R.id.play_searchiv).setOnClickListener(this);
        findViewById(R.id.home_indicatorBg).setOnClickListener(this);
        findViewById(R.id.see_indicatorBg).setOnClickListener(this);
        findViewById(R.id.posted_indicatorBg).setOnClickListener(this);
        findViewById(R.id.me_indicatorBg).setOnClickListener(this);
        fragments = new ArrayList<BaseFragment>();
        fragments.add(new RecomentFrag());
        fragments.add(new FindFrag());
        fragments.add(new SameCityFrag());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments,
                getResources().getStringArray(R.array.play_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripbooklist);
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
        int id = v.getId();
        switch (id) {
            case R.id.play_backiv:
                finish();
//                CommonUtils.startNewActivity(getContext(), WeChatActivity.class);
                break;
            case R.id.play_searchiv:
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
                break;
            case R.id.home_indicatorBg:
//                Intent intent = new Intent();
//                intent.putExtra(Constants.IntentParams.INDEX, 0);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.setClass(mContext, MainActivity.class);
//                startActivity(intent);
                CommonUtils.gotoMainPage(mContext, Constants.Navigation.HOME_INDEX);
                break;
            case R.id.see_indicatorBg:
//                Intent intentSee = new Intent();
//                intentSee.putExtra(Constants.IntentParams.INDEX, 1);
//                intentSee.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intentSee.setClass(mContext, MainActivity.class);
//                startActivity(intentSee);
                CommonUtils.gotoMainPage(mContext, Constants.Navigation.SEE_INDEX);
                break;
            case R.id.posted_indicatorBg:
                PopupMenuView.getInstance()._show(mContext, bottomLayout);
                break;
            case R.id.me_indicatorBg:
                if (IOUtils.isLogin(getContext())) {
//                    Intent intentMe= new Intent();
//                    intentMe.putExtra(Constants.IntentParams.INDEX, 4);
//                    intentMe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intentMe.setClass(mContext, MainActivity.class);
//                    startActivity(intentMe);
                    CommonUtils.gotoMainPage(mContext, Constants.Navigation.ME_INDEX);
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public Context getContext() {
        return this;
    }
}
