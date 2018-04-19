package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.OrderModel;
import com.tgf.kcwc.mvp.model.Ticket;
import com.tgf.kcwc.mvp.presenter.OrderDetailPresenter;
import com.tgf.kcwc.mvp.view.OrderDetailView;
import com.tgf.kcwc.util.ArithmeticUtil;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/8/17
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 门票订单详情
 */

public class BuyTicketOrderDetailActivity extends BaseActivity implements OrderDetailView {


    protected SimpleDraweeView img;
    protected TextView senseName;
    protected ListView ticketInfoList;
    protected ListView orderInfoList;
    protected ListView orderInfoList2;
    protected ListView orderInfoList3;
    private TextView mTagTv1, mTagTv2;
    private OrderDetailPresenter mPresenter;

    private String mOrderId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mOrderId = intent.getStringExtra(Constants.IntentParams.ID);
        setContentView(R.layout.activity_buy_ticket_order_detail);

    }

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new OrderDetailPresenter();
        mPresenter.attachView(this);

    }

    private OrderModel mModel;

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadOrderDetails(mOrderId + "", IOUtils.getToken(mContext));
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("订单详情");
    }

    @Override
    public void showOrderDetails(OrderModel model) {

        int refundSet = model.details.refundSet;
        if (refundSet == 0) {
            mTagTv1.setVisibility(View.GONE);
            mTagTv2.setVisibility(View.GONE);
        } else if (refundSet == 1) {
            mTagTv1.setVisibility(View.VISIBLE);
            mTagTv2.setVisibility(View.GONE);
        } else if (refundSet == 2) {
            mTagTv1.setVisibility(View.GONE);
            mTagTv2.setVisibility(View.VISIBLE);
        }
        mModel = model;
        showHeaderInfo(model);
        showTicketInfo(model);
        showOrderInfo(model);
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
        orderInfoList = (ListView) findViewById(R.id.orderInfoList);
        orderInfoList2 = (ListView) findViewById(R.id.orderInfoList2);
        orderInfoList3 = (ListView) findViewById(R.id.orderInfoList3);
        mTagTv1 = (TextView) findViewById(R.id.tag1);
        mTagTv2 = (TextView) findViewById(R.id.tag2);
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
            CommonUtils.gotoMainPage(mContext, Constants.Navigation.SEE_INDEX);
        }


    }

    private void showHeaderInfo(OrderModel model) {

        img.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.details.cover, 270, 203)));
        senseName.setText(model.details.eventName);
    }

    private void showTicketInfo(final OrderModel model) {

        final WrapAdapter<Ticket> ticketWrapAdapter = new WrapAdapter<Ticket>(mContext, R.layout.order_detail_ticket_list_item, model.tickets) {

            protected ListView ticketList;
            protected Button refundBtn;
            protected TextView descTv;
            protected TextView price;
            protected TextView moneyTag;
            protected TextView unitTv;
            protected TextView ticketName;


            @Override
            public void convert(ViewHolder helper, final Ticket item) {
                ticketName = helper.getView(R.id.ticketName);
                unitTv = helper.getView(R.id.unitTv);
                moneyTag = helper.getView(R.id.moneyTag);
                price = helper.getView(R.id.price);
                descTv = helper.getView(R.id.descTv);
                refundBtn = helper.getView(R.id.refundBtn);

                refundBtn.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    protected void onSingleClick(View view) {


                        Intent intent = new Intent();
                        intent.setClass(mContext, ApplyRefundActivity.class);
                        intent.putExtra(Constants.IntentParams.DATA, model);
                        startActivity(intent);
                    }
                });
                ticketList = helper.getView(R.id.list);
                ticketName.setText(item.name);
                price.setText(item.price);
                descTv.setText(item.remarks + "");
                if (item.isCanRefund == 1) {
                    refundBtn.setVisibility(View.VISIBLE);
                } else {
                    refundBtn.setVisibility(View.GONE);
                }

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

                        ticketStatus(status, statusTv, item.checkoutTime, item.applyRefundTime, item.giveTime, moreIconView);

                    }
                });
                ticketList.setOnItemClickListener(ticketItemClickListener);

                ViewUtil.setListViewHeightBasedOnChildren(ticketList);
            }
        };

        ticketInfoList.setAdapter(ticketWrapAdapter);
        ViewUtil.setListViewHeightBasedOnChildren(ticketInfoList);

    }

    private AdapterView.OnItemClickListener ticketItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Ticket.Code code = (Ticket.Code) parent.getAdapter().getItem(position);

            int status = code.status;
            switch (status) {
                case Constants.TicketStatus.USED:
                case Constants.TicketStatus.REFUNDED:
                case Constants.TicketStatus.REFUNDING:
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, code.id);
                    CommonUtils.startNewActivity(mContext, args, TicketRefundDetailActivity.class);
                    break;
            }

        }
    };

    private void ticketStatus(int status, TextView textView, String time, String applyRefundTime, String giveTime, TextView moreView) {

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
                moreView.setText("已退款\n" + applyRefundTime);
                break;

            case Constants.TicketStatus.FORWARDED:
                textView.setVisibility(View.VISIBLE);
                moreView.setVisibility(View.GONE);
                textView.setText("已转送\n" + giveTime);
                break;
        }
    }

    private void showOrderInfo(OrderModel model) {

        loadOrderInfo1(model);
        loadOrderInfo2(model);
        loadOrderInfo3(model);
        loadOrderInfo4(model);
    }

    private void loadOrderInfo1(OrderModel model) {

        List<DataItem> dataItems = new ArrayList<DataItem>();

        String[] arrays = mRes.getStringArray(R.array.order_info_list1);

        for (String str : arrays) {
            DataItem item = new DataItem();
            item.title = str;
            dataItems.add(item);
        }

        dataItems.get(0).content = model.details.orderSN;
        dataItems.get(1).content = model.user.mobile + "(" + model.user.nick + ")";
        dataItems.get(2).content = model.details.createTime;
        dataItems.get(3).content = model.details.amount + "";

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

    private void loadOrderInfo2(OrderModel model) {

        List<DataItem> dataItems = new ArrayList<DataItem>();

        DataItem dataItem = new DataItem();
        dataItem.ticketType = "票种";
        dataItem.unitPrice = "单价";
        dataItem.amount = "数量";
        dataItem.total = "总价";
        dataItem.textColorValue = mRes.getColor(R.color.text_color17);
        dataItems.add(dataItem);
        List<Ticket> tickets = model.tickets;

        for (Ticket t : tickets) {

            DataItem item = new DataItem();
            item.ticketType = t.name;
            item.unitPrice = "￥" + t.price;
            item.amount = t.nums + "";
            item.total = "￥" + ArithmeticUtil.mul2(t.nums, Double.parseDouble(t.price), 2) + "";
            item.textColorValue = mRes.getColor(R.color.text_color3);
            dataItems.add(item);
        }
        final int size = dataItems.size();
        WrapAdapter<DataItem> adapter = new WrapAdapter<DataItem>(mContext, dataItems, R.layout.custom_table_item) {


            protected TextView total;
            protected TextView amount;
            protected TextView unitPrice;
            protected TextView ticketType;


            @Override
            public void convert(ViewHolder helper, DataItem item) {

                ticketType = helper.getView(R.id.ticketType);
                unitPrice = helper.getView(R.id.unitPrice);
                amount = helper.getView(R.id.amount);
                total = helper.getView(R.id.total);
                ticketType.setText(item.ticketType);
                unitPrice.setText(item.unitPrice);
                amount.setText(item.amount);
                total.setText(item.total);
                ticketType.setTextColor(item.textColorValue);
                unitPrice.setTextColor(item.textColorValue);
                amount.setTextColor(item.textColorValue);
                total.setTextColor(item.textColorValue);
                View split = helper.getView(R.id.bottomSplit);
                if (size - 1 == helper.getPosition()) {
                    split.setVisibility(View.VISIBLE);
                } else {
                    split.setVisibility(View.GONE);
                }
            }
        };

        orderInfoList2.setAdapter(adapter);

        ViewUtil.setListViewHeightBasedOnChildren(orderInfoList2);
    }

    private void loadOrderInfo3(OrderModel model) {


        List<DataItem> dataItems = new ArrayList<DataItem>();

        String[] arrays = mRes.getStringArray(R.array.order_info_list3);
        for (String str : arrays) {

            DataItem dataItem = new DataItem();
            dataItem.title = str;
            dataItems.add(dataItem);
        }
        dataItems.get(0).content = "-￥ " + model.details.amountDiscount + "";
        //dataItems.get(1).content = model.details.userCouponCode;

        dataItems.get(1).content = model.details.userCouponCode;
        WrapAdapter<DataItem> adapter = new WrapAdapter<DataItem>(mContext, R.layout.order_list_item_1, dataItems) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                TextView contentTv = helper.getView(R.id.content);
                titleTv.setText(item.title);
                contentTv.setText(item.content);
            }
        };
        orderInfoList3.setAdapter(adapter);
    }


    private void loadOrderInfo4(OrderModel model) {

        TextView tv = (TextView) findViewById(R.id.actuallyPaidTv);
        tv.setText("订单实付金额: ￥ " + model.details.amount);
    }

}
