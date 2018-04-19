package com.tgf.kcwc.coupon.manage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.CouponEventModel;
import com.tgf.kcwc.mvp.presenter.CouponExhibitPresenter;
import com.tgf.kcwc.mvp.view.CouponExhibitView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class CouponQueryActivity extends BaseActivity implements CouponExhibitView {
    private final String KEY_INTENT_COUPONID = "couponid";
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;
    private MyPagerAdapter mPagerAdapter;
    private String[] tabTitle ={"分发对象","分发记录"};
//    private TicketExhibitPresenter mTicketExhibitPresenter;
    private SimpleDraweeView simpleDraweeView;
    private TextView eventName;
    public int mTicketId;
    private CouponExhibitPresenter couponExhibitPresenter;
    private TextView ticketLeftTv;


    @Override
    protected void setUpViews() {
        mTicketId = getIntent().getIntExtra(KEY_INTENT_COUPONID, 1);
        couponExhibitPresenter = new CouponExhibitPresenter();
        couponExhibitPresenter.attachView(this);
        couponExhibitPresenter.getCouponExhibt(IOUtils.getToken(getContext()),mTicketId);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.ticketmanage_event_iv);
        eventName = (TextView) findViewById(R.id.ticketquery_ticketinfo);
        ticketLeftTv = (TextView) findViewById(R.id.ticketquery_ticketleft);
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        fragments.add(new CouponSendObjFragment());
        fragments.add(new CouponSendRecordFragment());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("代金券分发查询");
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketquery);
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
        mTabs.setUnderlineHeight(1);
        //设置Tab底部线的颜色
        mTabs.setUnderlineColor(getResources().getColor(R.color.split_line_color));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTabs.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        mTabs.setIndicatorColor(getResources().getColor(R.color.tab_text_s_color));

        // 设置Tab标题文字的大小,传入的是dp
        mTabs.setTextSize(16);
        // 设置选中Tab文字的颜色
        mTabs.setSelectedTextColor(getResources().getColor(R.color.tab_text_s_color));
        //设置正常Tab文字的颜色
        mTabs.setTextColorResource(R.color.text_color3);

        //  设置点击Tab时的背景色
//         mTabs.setTabBackground(R.color.white);

        //是否支持动画渐变(颜色渐变和文字大小渐变)
        // mTabs.setFadeEnabled(true);
        // 设置最大缩放,是正常状态的0.3倍
        //mTabs.setZoomMax(0.3F);
        //设置Tab文字的左右间距,传入的是dp
        // mTabs.setTabPaddingLeftRight(24);
    }




    @Override
    public Context getContext() {
        return this;
    }



    @Override
    protected void onDestroy() {
        couponExhibitPresenter.detachView();
        super.onDestroy();

    }

    @Override
    public void showCouponExhibit(CouponEventModel couponEventModel) {
        simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(couponEventModel.cover,270, 203)));
        eventName.setText(couponEventModel.title);
        ticketLeftTv.setText("剩余 "+couponEventModel.inventory);
    }


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
