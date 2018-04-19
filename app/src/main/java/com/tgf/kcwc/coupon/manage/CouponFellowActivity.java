package com.tgf.kcwc.coupon.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CouponTailRecyleAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.CouponFellowModel;
import com.tgf.kcwc.mvp.presenter.CouponFellowPresenter;
import com.tgf.kcwc.mvp.view.CouponFellowView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;


/**
 * Auther: Scott
 * Date: 2017/2/9 0009
 * E-mail:hekescott@qq.com
 */

public class CouponFellowActivity extends BaseActivity implements CouponFellowView {
    private static final String TAG = "TicketFellowActivity";
    private final String KEY_INTENT_COUPONID = "couponid";
    private final String KEY_INTENT_MOBILE = "mobile";
    private RecyclerView recyleView;


    @Override
    protected void setUpViews() {
        Intent fromintent = getIntent();
        int tfid = fromintent.getIntExtra(KEY_INTENT_COUPONID, 1);
//        String mobile = fromintent.getStringExtra(KEY_INTENT_MOBILE);
        String mobile = fromintent.getStringExtra(KEY_INTENT_MOBILE);
//        TicketFellowPresenter fellowPresenter = new TicketFellowPresenter();
//        fellowPresenter.attachView(this);
//        fellowPresenter.getTicketFellow(Constants.token, tfid, mobile, userType);

        CouponFellowPresenter couponFellowPresenter = new CouponFellowPresenter();
        couponFellowPresenter.attachView(this);
        couponFellowPresenter.getFellowRecord(IOUtils.getToken(getContext()),tfid,mobile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyleView = (RecyclerView) findViewById(R.id.couponfellow_rv);
        recyleView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("代金券跟踪");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponfellow);
    }

//    @Override
//    public void showTickeFellow(TicketFellowModel ticketFellowModel) {
//
//
//    }
//
//    private void showSend(CommonAdapter.ViewHolder helper, TicketFellowModel.FellowItem item) {
//
//
//    }
//
//    private void showRecieve(CommonAdapter.ViewHolder helper, TicketFellowModel.FellowItem item) {
//
//    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void showCouponFellows(CouponFellowModel couponFellowModel) {
        recyleView.setAdapter(new CouponTailRecyleAdapter(getContext(),couponFellowModel.couponFellowItems));

//        if(!(ticketFellowModel.nums_lose==0)){
//            View footerLose =  View.inflate(getContext(), R.layout.listitem_ticket_fellowfooter,null);
//            TextView expireNumTv = (TextView) footerLose.findViewById(R.id.sendfellow_num);
//            expireNumTv.setText("有"+ticketFellowModel.nums_lose+"张票领取权限失效");
//            lv.addFooterView(footerLose);
//        }
//        if(!(ticketFellowModel.nums_expire==0)){
//            View footerExpire =  View.inflate(getContext(), R.layout.listitem_ticket_fellowfooter,null);
//            TextView expireNumTv = (TextView) footerExpire.findViewById(R.id.sendfellow_num);
//            expireNumTv.setText("有"+ticketFellowModel.nums_expire+"张票过期");
//            lv.addFooterView(footerExpire);
//        }
//        CommonAdapter ticketCommonAdapter = new CommonAdapter<TicketFellowModel.FellowItem>(
//                getContext(), ticketFellowModel.fellowContents, R.layout.listitem_ticket_fellow) {
//            @Override
//            public void convert(ViewHolder helper, TicketFellowModel.FellowItem item) {
//                ImageView sendIv = helper.getView(R.id.fellow_sendiv);
//                ImageView recieveIv = helper.getView(R.id.fellow_recieveiv);
//                LinearLayout contenLv = helper.getView(R.id.ticketfellow_contentlv);
//                LinearLayout codeLv = helper.getView(R.id.ticketfellow_codelv);
//                TextView titleTv = helper.getView(R.id.ticketfellow_titletv);
//                TextView contentTv = helper.getView(R.id.fellow_contenttv);
//                if (1==item.msgType) {
//                    sendIv.setVisibility(View.VISIBLE);
//                    recieveIv.setVisibility(View.INVISIBLE);
//                    contenLv.setBackgroundColor(mRes.getColor(R.color.tab_text_s_color));
//                    titleTv.setTextColor(mRes.getColor(R.color.white));
//                    contentTv.setTextColor(mRes.getColor(R.color.white));
//                    showSend(helper, item);
//                } else {
//                    sendIv.setVisibility(View.INVISIBLE);
//                    recieveIv.setVisibility(View.VISIBLE);
//                    contenLv.setBackgroundColor(mRes.getColor(R.color.white));
//                    titleTv.setTextColor(mRes.getColor(R.color.black));
//                    contentTv.setTextColor(mRes.getColor(R.color.black));
//                    showRecieve(helper, item);
//                }
//                titleTv.setText(item.type);
//                contentTv.setText(item.content);
//                if(!TextUtils.isEmpty(item.ticket)){
//                    String ticketCode = item.ticket.replace(",","\n")+"\n";
//                    helper.setText(R.id.ticket_codetv,ticketCode);
//                    codeLv.setVisibility(View.VISIBLE);
//                }else {
//                    codeLv.setVisibility(View.GONE);
//                }
//
//            }
//        };
//
//        lv.setAdapter(ticketCommonAdapter);
    }


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
