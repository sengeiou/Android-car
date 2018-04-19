package com.tgf.kcwc.businessconcerns;

import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.BusinessRecordModel;
import com.tgf.kcwc.mvp.presenter.BusinessRecordPresenter;
import com.tgf.kcwc.mvp.view.BusinessRecordView;
import com.tgf.kcwc.util.IOUtils;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/8/3 8:52
 * 商务记录
 */

public class BusinessRecordFrag extends BaseFragment implements BusinessRecordView{
    //看车
    private LinearLayout seeCarAll;
    private TextView seeCarRecord;
    private TextView seeDay;
    private TextView mySeeDay;
    private TextView seeMonth;
    private TextView mySeeMonth;
    private TextView seeYear;
    private TextView mySeeYear;
    private TextView seeNearest;
    //票证
    private LinearLayout ticketCertAll;
    private TextView ticketCertRecord;
    private TextView recordTicket;
    private TextView recordTicketTotal;
    private TextView recordTicketCheck;
    private TextView recordCert;
    private TextView recordCertTotal;
    private TextView recordCertCheck;
    private TextView recordJoin;
    private TextView recordNearestGet;
    private TextView recordNearestEnter;
    //代金卷
    private LinearLayout couponAll;
    private TextView couponRecord;
    private TextView couponGet;
    private TextView couponGetCheck;
    private TextView couponBuy;
    private TextView couponBuyCheck;
    private TextView couponNearest;
    private TextView couponNearestWriteOff;
    //活动
    private LinearLayout activityAll;
    private TextView activityRecord;
    private TextView activityCycle;
    private TextView activityCycleTotal;
    private TextView activityPlay;
    private TextView activityPlayTotal;
    private TextView activityNearestJoin;

    BusinessRecordPresenter mPresenter;
    private int page = 1;
    //是否刷新
    private boolean isRefresh;
    //好友id
    private int friendId;
    //类型
    private int type;

    public BusinessRecordFrag(int friendId) {
        super();
        this.friendId = friendId;
    }

    @Override
    protected void updateData() {
        mPresenter = new BusinessRecordPresenter();
        mPresenter.attachView(this);
        mPresenter.getBusinessRecord(IOUtils.getToken(getContext()),friendId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_business_record;
    }

    @Override
    protected void initView() {
        setUserVisibleHint(true);

        seeCarAll = findView(R.id.see_car_record_all);
        seeCarRecord = findView(R.id.record_see_car);
        seeDay = findView(R.id.record_see_day);
        mySeeDay = findView(R.id.record_my_see_day);
        seeMonth = findView(R.id.record_see_month);
        mySeeMonth = findView(R.id.record_my_see_month);
        seeYear = findView(R.id.record_see_year);
        mySeeYear = findView(R.id.record_my_see_year);
        seeNearest = findView(R.id.record_see_nearest);

        ticketCertAll = findView(R.id.ticket_record_all);
        ticketCertRecord = findView(R.id.record_ticket_cert);
        recordTicket = findView(R.id.record_ticket);
        recordTicketTotal = findView(R.id.record_ticket_total);
        recordTicketCheck = findView(R.id.record_ticket_check);
        recordCert = findView(R.id.record_cert);
        recordCertTotal = findView(R.id.record_cert_total);
        recordCertCheck = findView(R.id.record_cert_check);
        recordJoin = findView(R.id.record_join);
        recordNearestGet = findView(R.id.record_nearest_get);
        recordNearestEnter = findView(R.id.record_nearest_enter);

        couponAll = findView(R.id.coupon_record_all);
        couponRecord = findView(R.id.record_coupon);
        couponGet = findView(R.id.record_coupon_get);
        couponGetCheck = findView(R.id.record_coupon_check);
        couponBuy = findView(R.id.record_coupon_buy);
        couponBuyCheck = findView(R.id.record_coupon_buy_check);
        couponNearest = findView(R.id.record_coupon_nearest);
        couponNearestWriteOff = findView(R.id.record_coupon_nearest_write_off);

        activityAll = findView(R.id.record_activity_all);
        activityRecord = findView(R.id.record_activity);
        activityCycle = findView(R.id.record_activity_cycle);
        activityCycleTotal = findView(R.id.record_activity_cycle_total);
        activityPlay = findView(R.id.record_activity_play);
        activityPlayTotal = findView(R.id.record_activity_play_total);
        activityNearestJoin = findView(R.id.record_activity_nearest_join);

        seeCarRecord.setOnClickListener(this);
        ticketCertRecord.setOnClickListener(this);
        couponRecord.setOnClickListener(this);
        activityRecord.setOnClickListener(this);

        initRefreshLayout(mBGDelegate);

    }

    @Override
    protected void initData() {
//        super.initData();
        mPresenter = new BusinessRecordPresenter();
        mPresenter.attachView(this);
        mPresenter.getBusinessRecord(IOUtils.getToken(getContext()),friendId);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent(mContext,BusinessRecordDetailActivity.class);
        switch (v.getId()) {
            case R.id.record_see_car:
                intent.putExtra(Constants.IntentParams.DATA,friendId);
                intent.putExtra(Constants.IntentParams.DATA2,1);
                break;
            case R.id.record_ticket_cert:
                intent.putExtra(Constants.IntentParams.DATA,friendId);
                intent.putExtra(Constants.IntentParams.DATA2,2);
                break;
            case R.id.record_coupon:
                intent.putExtra(Constants.IntentParams.DATA,friendId);
                intent.putExtra(Constants.IntentParams.DATA2,3);
                break;
            case R.id.record_activity:
                intent.putExtra(Constants.IntentParams.DATA,friendId);
                intent.putExtra(Constants.IntentParams.DATA2,4);
                break;
        }
        if (null != intent){
            startActivity(intent);
        }
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            page = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isRefresh = false;
            page++;
            loadMore();
            return false;
        }
    };

    public void loadMore() {
        mPresenter.getBusinessRecord(IOUtils.getToken(getContext()),friendId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }

    @Override
    public void showSeeCar(BusinessRecordModel.SeeItem seeItem) {
        if (null != seeItem){
            seeCarAll.setVisibility(View.VISIBLE);
            mySeeDay.setText(seeItem.myTotalMonth+"");
            seeDay.setText(seeItem.totalMonth+"");
            mySeeMonth.setText(seeItem.myTotalHalfYear+"");
            seeMonth.setText(seeItem.totalHalfYear+"");
            mySeeYear.setText(seeItem.myTotalYear+"");
            seeYear.setText(seeItem.totalYear+"");
            seeNearest.setText(seeItem.nearestItem.seriesName+seeItem.nearestItem.carName);
        }else {
            seeCarAll.setVisibility(View.GONE);
        }
    }
    @Override
    public void showTicket(BusinessRecordModel.TicketItem ticketItem) {
        if (ticketItem != null) {
            ticketCertAll.setVisibility(View.VISIBLE);
            recordTicket.setText(ticketItem.tickets.receiveTotal+"");
            recordTicketTotal.setText(ticketItem.tickets.total+"");
            recordTicketCheck.setText("(核销"+ticketItem.tickets.check+")");
            recordCert.setText(ticketItem.certItem.receiveTotal+"");
            recordCertTotal.setText(ticketItem.certItem.total+"");
            recordCertCheck.setText("(核销"+ticketItem.certItem.enter+")");
            recordJoin.setText(Html.fromHtml("<font color=\"#333333\">"+ticketItem.eventCount+"</font>场"));
            recordNearestGet.setText(ticketItem.nearestItem.name+ticketItem.nearestItem.type);
//        recordNearestEnter.setText(ticketItem.nearestEventItem+"");
            recordNearestEnter.setText("");
        }else {
            ticketCertAll.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCoupon(BusinessRecordModel.CouponItem couponItem) {
        if (couponItem != null){
            couponAll.setVisibility(View.VISIBLE);
            couponGet.setText(couponItem.distributeItem.total+"");
            couponGetCheck.setText("(核销"+couponItem.distributeItem.check+"张)");
            couponBuy.setText(couponItem.buyItem.total+"");
            couponBuyCheck.setText("(核销"+couponItem.buyItem.check+"张)");
            couponNearest.setText(couponItem.nearestItem.title);
            couponNearestWriteOff.setText(couponItem.nearestCheckItem.title);
        }else {
            couponAll.setVisibility(View.GONE);
        }
    }
    @Override
    public void showActivity(BusinessRecordModel.ActivityItem activityItem) {
        if (activityItem != null){
            activityAll.setVisibility(View.VISIBLE);
            activityCycle.setText(activityItem.cycleItem.myTotal+"");
            activityCycleTotal.setText(activityItem.cycleItem.total+"");
            activityPlay.setText(activityItem.playItem.total+"");
            activityPlayTotal.setText(activityItem.playItem.total+"");
            activityNearestJoin.setText(activityItem.nearestItem.title);
        }else {
            activityAll.setVisibility(View.GONE);
        }
    }

    @Override
    public void showInfo(BusinessRecordModel.BasicInfoItem basicInfoItem) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active){
            showLoadingIndicator(true);
        }else {
            stopRefreshAll();
            showLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }
}
