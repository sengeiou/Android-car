package com.tgf.kcwc.coupon;

import android.content.Intent;
import android.os.Build;
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
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.ticket.FreeTicketFragment;
import com.tgf.kcwc.ticket.MyTicketFragment;
import com.tgf.kcwc.util.SysUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/3 0003
 * E-mail:hekescott@qq.com
 */

public class MyCouponActivity extends BaseActivity {
    private PagerSlidingTabStrip mTabs;
    private ViewPager            mPager;
    private PagerAdapter mPagerAdapter = null;

    private Intent fromIntent;
    private String title[] = new String[]{"赠送","我的"};
    private VoucherMainActivity voucherMainActivity;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("我的卡券");
        backEvent(back);

    }

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        voucherMainActivity = (VoucherMainActivity) getParent();
        int index = fromIntent.getIntExtra(Constants.IntentParams.INDEX,1);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mTabs.setBackgroundColor(mRes.getColor(R.color.white));
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setBackgroundColor(mRes.getColor(R.color.white));
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        MyFreeCounponFrag freeTicketFrament = new MyFreeCounponFrag();
        fragments.add(freeTicketFrament);
        MyBuyCounponFrag myTicketFrament = new MyBuyCounponFrag();
        fragments.add(myTicketFrament);
        //fragments.add(new NonPaymentFragment());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments,title);
        //fragments.add(new NonPaymentFragment());
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchermy);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.my_root_lv).setPadding(0, SysUtils.getStatusHeight(this), 0, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }











}
