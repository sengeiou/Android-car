package com.tgf.kcwc.membercenter.driv;

import java.util.ArrayList;
import java.util.List;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.driving.driv.SponsorDrivingActivity;
import com.tgf.kcwc.driving.please.SponsorPleasePlayActivity;
import com.tgf.kcwc.membercenter.please.PleasePlayActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/21.
 */

public class DrivingActivity extends BaseActivity {
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter = null;
    private int mSenseId = -1;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.driving);
        function.setImageResource(R.drawable.btn_luxian);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startNewActivity(DrivingActivity.this, SponsorDrivingActivity.class);
            }
        });
    }

    @Override
    protected void setUpViews() {
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
        mTabs.setBackgroundColor(mRes.getColor(R.color.white));
        mPager.setBackgroundColor(mRes.getColor(R.color.white));
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        ApplyListFragment freeTicketFrament = new ApplyListFragment();
        freeTicketFrament.setSenseId(mSenseId);
        fragments.add(freeTicketFrament);
        FoundListFragment myFoundListFragment = new FoundListFragment();
        myFoundListFragment.setSenseId(mSenseId);
        fragments.add(myFoundListFragment);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, getResources().getStringArray(R.array.driving_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menber_driving);
        setTabsValue();
    }

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
        //设置Tab底部线的颜色
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
