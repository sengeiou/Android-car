package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.OrderModel;
import com.tgf.kcwc.mvp.model.Ticket;
import com.tgf.kcwc.mvp.presenter.OrderDetailPresenter;
import com.tgf.kcwc.mvp.view.OrderDetailView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/8/17
 * E-mail:fishloveqin@gmail.com
 * 赠票订单详情
 */

public class FreeTicketOrderDetailActivity extends BaseActivity implements OrderDetailView {
    protected SimpleDraweeView img;
    protected TextView senseName;
    protected ListView ticketInfoList;
    protected TextView descTv;
    protected ListView orderInfoList;
    private OrderDetailPresenter mPresenter;

    private String mOrderId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mOrderId = intent.getStringExtra(Constants.IntentParams.ID);
        super.setContentView(R.layout.activity_free_ticket_order_detail);
        initView();
    }

    @Override
    protected void setUpViews() {

        mPresenter = new OrderDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.loadOrderDetails(mOrderId + "", IOUtils.getToken(mContext));
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("订单详情");
    }

    @Override
    public void showOrderDetails(OrderModel model) {

        showHeaderInfo(model);
        showTicketInfo(model);
        loadOrderInfo(model);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private void initView() {
        img = (SimpleDraweeView) findViewById(R.id.img);
        senseName = (TextView) findViewById(R.id.senseName);
        ticketInfoList = (ListView) findViewById(R.id.ticketInfoList);
        descTv = (TextView) findViewById(R.id.descTv);
        orderInfoList = (ListView) findViewById(R.id.orderInfoList);
        findViewById(R.id.headerLayout).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        jumpExhibitionDetail();
    }

    private void jumpExhibitionDetail() {

        if (mModel != null) {

            KPlayCarApp.putValue(Constants.IntentParams.INDEX, 1);
            KPlayCarApp.putValue(Constants.IntentParams.EVENT_ID, mModel.details.eventId + "");

//            Intent intent = new Intent();
//            intent.putExtra(Constants.IntentParams.INDEX, 1);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.setClass(mContext, MainActivity.class);
//            startActivity(intent);
            CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
        }


    }

    private OrderModel mModel;

    private void showHeaderInfo(OrderModel model) {
        mModel = model;
        img.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.details.cover, 270, 203)));
        senseName.setText(model.details.eventName);
    }

    private void showTicketInfo(OrderModel model) {

        WrapAdapter<Ticket> ticketWrapAdapter = new WrapAdapter<Ticket>(mContext, R.layout.order_detail_ticket_list_item, model.tickets) {

            protected ListView ticketList;
            protected Button refundBtn;
            protected TextView descTv;
            protected TextView price;
            protected TextView moneyTag;
            protected TextView unitTv;
            protected TextView ticketName;


            @Override
            public void convert(WrapAdapter.ViewHolder helper, Ticket item) {
                ticketName = helper.getView(R.id.ticketName);
                unitTv = helper.getView(R.id.unitTv);
                moneyTag = helper.getView(R.id.moneyTag);
                price = helper.getView(R.id.price);
                descTv = helper.getView(R.id.descTv);
                refundBtn = helper.getView(R.id.refundBtn);
                ticketList = helper.getView(R.id.list);
                ticketName.setText(item.name);
                price.setText(item.price);
                price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                descTv.setText(item.remarks);
                refundBtn.setVisibility(View.GONE);
                ticketList.setAdapter(new WrapAdapter<Ticket.Code>(mContext, R.layout.ticket_item2, item.listCodes) {


                    protected View split;
                    protected TextView statusTv;
                    protected TextView moreIconView;
                    protected TextView ticketId;

                    @Override
                    public void convert(ViewHolder helper, Ticket.Code item) {

                        ticketId = helper.getView(R.id.ticketId);
                        moreIconView = helper.getView(R.id.moreIconView);
                        statusTv = helper.getView(R.id.statusTv);
                        split = helper.getView(R.id.split);
                        ticketId.setText("No." + item.serialSN);
                        int status = item.status;

                        ticketStatus(status, statusTv, item.checkoutTime, moreIconView);

                    }
                });
                ViewUtil.setListViewHeightBasedOnChildren(ticketList);

            }
        };

        ticketInfoList.setAdapter(ticketWrapAdapter);
        ViewUtil.setListViewHeightBasedOnChildren(ticketInfoList);

    }

    private void ticketStatus(int status, TextView textView, String time, TextView moreView) {

        switch (status) {
            case Constants.TicketStatus.UN_USED:
                textView.setVisibility(View.VISIBLE);
                moreView.setVisibility(View.GONE);
                textView.setTextColor(mRes.getColor(R.color.text_color15));
                textView.setText("未使用");
                break;
            case Constants.TicketStatus.USED:
                textView.setVisibility(View.VISIBLE);
                moreView.setVisibility(View.GONE);
                textView.setText("已核销" + "\n" + time);
                break;
            case Constants.TicketStatus.EXP:
            case Constants.TicketStatus.EXPIRED:
                textView.setVisibility(View.VISIBLE);
                moreView.setVisibility(View.GONE);
                textView.setText("已过期");
                break;
            case Constants.TicketStatus.CANCEL:
                textView.setVisibility(View.VISIBLE);
                moreView.setVisibility(View.GONE);
                break;
            case Constants.TicketStatus.REFUNDING:
                textView.setVisibility(View.VISIBLE);
                moreView.setVisibility(View.GONE);
                textView.setTextColor(mRes.getColor(R.color.text_color16));
                textView.setText("退款中");
                break;
            case Constants.TicketStatus.REFUNDED:
                textView.setVisibility(View.GONE);
                moreView.setVisibility(View.VISIBLE);
                moreView.setText("已退款\n" + time);
                break;
        }
    }


    private void loadOrderInfo(OrderModel model) {

        List<DataItem> dataItems = new ArrayList<DataItem>();

        String[] arrays = mRes.getStringArray(R.array.order_info_list2);

        for (String str : arrays) {
            DataItem item = new DataItem();
            item.title = str;
            dataItems.add(item);
        }

        dataItems.get(0).content = model.details.orderSN;
        dataItems.get(1).content = model.user.mobile + "(" + model.user.nick + ")";
        dataItems.get(2).content = model.details.createTime;
        dataItems.get(3).content = model.handout.mobile + "" + model.handout.organizationlName + "";
        dataItems.get(4).content = model.handout.time;
        WrapAdapter<DataItem> adapter = new WrapAdapter<DataItem>(mContext, R.layout.order_list_item_1, dataItems) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                TextView contentTv = helper.getView(R.id.content);
                titleTv.setText(item.title);
                contentTv.setText(item.content);
            }
        };
        orderInfoList.setAdapter(adapter);
    }
}
