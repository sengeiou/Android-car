package com.tgf.kcwc.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/29 09:51
 * E-mail：fishloveqin@gmail.com
 * 票务
 */

public class TicketActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabs;
    private ViewPager            mPager;
    private PagerAdapter         mPagerAdapter = null;

    private int                  mSenseId      = -1;
    private Intent fromIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSenseId = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        setContentView(R.layout.activity_ticket);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(R.string.tickets);
    }

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
       int index = fromIntent.getIntExtra(Constants.IntentParams.INDEX,1);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mTabs.setBackgroundColor(mRes.getColor(R.color.white));
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setBackgroundColor(mRes.getColor(R.color.white));
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        FreeTicketFragment freeTicketFrament = new FreeTicketFragment();
        freeTicketFrament.setSenseId(mSenseId);
        fragments.add(freeTicketFrament);
        MyTicketFragment myTicketFrament = new MyTicketFragment();
        myTicketFrament.setSenseId(mSenseId);
        fragments.add(myTicketFrament);
        //fragments.add(new NonPaymentFragment());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments,
            getResources().getStringArray(R.array.ticket_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(index);
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
