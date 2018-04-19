package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.HeadEvent;
import com.tgf.kcwc.mvp.model.TicketOrgManageDetailModel;
import com.tgf.kcwc.mvp.presenter.TicketExhibitPresenter;
import com.tgf.kcwc.mvp.presenter.TicketOrgManageDetailPresenter;
import com.tgf.kcwc.mvp.view.TicketExhibitView;
import com.tgf.kcwc.mvp.view.TicketOrgManageDetailView;
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

public class TicketOrgQueryActivity extends BaseActivity implements TicketExhibitView, TicketOrgManageDetailView {
    private final String KEY_INTENT_TICKETID = "ticketid";
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;
    private MyPagerAdapter mPagerAdapter;
    private String[] tabTitle = {"分发对象", "分发记录"};
    private TicketExhibitPresenter mTicketExhibitPresenter;
    private SimpleDraweeView simpleDraweeView;
    private TextView eventName;
    public int mTicketId;
    private TextView tickeLeftNumTV;
    private SimpleDraweeView eventCoverIv;
    private TextView eventInfo;
    private TextView ticketType;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("门票分发查询");
    }
    private TicketOrgManageDetailPresenter ticketOrgManageDetailPresenter;
    @Override
    protected void setUpViews() {
        eventCoverIv = (SimpleDraweeView) findViewById(R.id.sendsee_event_iv);
        eventInfo = (TextView) findViewById(R.id.sendsee_ticketinfo);
        ticketType = (TextView) findViewById(R.id.manageorgdetail_tickettype_tv);
        mTicketId = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        ticketOrgManageDetailPresenter = new TicketOrgManageDetailPresenter();
        ticketOrgManageDetailPresenter.attachView(this);
        ticketOrgManageDetailPresenter.getTicketOrgManageDetail(IOUtils.getToken(getContext()),mTicketId,1);
        mTicketExhibitPresenter = new TicketExhibitPresenter();
        mTicketExhibitPresenter.attachView(this);
        mTicketExhibitPresenter.getTicketExhibitInfo(IOUtils.getToken(getContext()), mTicketId);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.ticketmanage_event_iv);
        eventName = (TextView) findViewById(R.id.ticketquery_ticketinfo);
        tickeLeftNumTV = (TextView) findViewById(R.id.ticketquery_ticketleft);
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        fragments.add(new SendOrgObjFragment());
        fragments.add(new SendOrgRecordFragment());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketorgquery);
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
        mTabs.setTextColor(getResources().getColor(R.color.text_color3));

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
    public void showTicketExhibitInfo(HeadEvent event) {
        simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(event.cover, 270, 203)));
        eventName.setText(event.name + "  " + event.ticketName);
        tickeLeftNumTV.setText("剩余："+event.getLeftNum());
    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTicketExhibitPresenter.detachView();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showManageDetail(TicketOrgManageDetailModel.Details details) {


        eventCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(details.event_cover,540,304)));
        eventInfo.setText(details.event_name);
        ticketType.setText(details.ticket_name);
    }
}
