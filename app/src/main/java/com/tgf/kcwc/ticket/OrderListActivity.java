package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.BaseCoupon;
import com.tgf.kcwc.mvp.model.CouponPayModel;
import com.tgf.kcwc.mvp.model.OrderModel;
import com.tgf.kcwc.mvp.model.OrderPayParam;
import com.tgf.kcwc.mvp.model.Ticket;
import com.tgf.kcwc.mvp.presenter.OrderDetailPresenter;
import com.tgf.kcwc.mvp.presenter.OrderPayPresenter;
import com.tgf.kcwc.mvp.view.OrderDetailView;
import com.tgf.kcwc.mvp.view.OrderPayView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.GsonUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.PayUtil;
import com.tgf.kcwc.util.ThreadPoolProxy;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/1/5 19:55
 * E-mail：fishloveqin@gmail.com
 * 确认订单
 */

public class OrderListActivity extends BaseActivity
        implements OrderDetailView, OrderPayView<OrderPayParam> {

    protected TextView orderNum;
    protected RelativeLayout headerLayout;
    protected ListView list;
    protected TextView orderTotalPriceTv;
    protected RelativeLayout orderTotalLayout;
    protected RelativeLayout orderWayTitleLayout;
    protected ListView orderWayList;
    protected Button submitOrder;
    private KPlayCarApp mApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderId = getIntent().getStringExtra(Constants.IntentParams.ID);
        super.setContentView(R.layout.activity_order_list);
        mApp = (KPlayCarApp) getApplication();
        mApp.setOrderId(mOrderId);
        mWXAPI = mApp.getMsgApi();

    }

    private IWXAPI mWXAPI;
    private OrderDetailPresenter mPresenter;
    private OrderPayPresenter mPayPreseneter, mAlipayPresenter;
    private String mOrderId = "";

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(R.string.confirm_order);
    }

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new OrderDetailPresenter();
        mList = (ListView) findViewById(R.id.list);
        mPresenter.attachView(this);
        mPresenter.loadOrderDetails(mOrderId + "", IOUtils.getToken(mContext));
        mPayPreseneter = new OrderPayPresenter();
        mPayPreseneter.attachView(this);
        mAlipayPresenter = new OrderPayPresenter();
        mAlipayPresenter.attachView(orderPayView);
        showOrderWay();
    }

    private List<DataItem> payByWays = new ArrayList<DataItem>();
    private WrapAdapter<DataItem> mOrderByWayAdapter = null;

    private void showOrderWay() {

        DataItem dataItem = new DataItem();
        dataItem.title = "微信支付";
        dataItem.icon = R.drawable.icon_weixinzhifu;
        dataItem.id = 1;
        dataItem.isSelected = true;
        payByWays.add(dataItem);
        dataItem = new DataItem();
        dataItem.title = "支付宝支付";
        dataItem.id = 2;
        dataItem.icon = R.drawable.icon_zhifubao;
        payByWays.add(dataItem);
        final int size = payByWays.size();
        mOrderByWayAdapter = new WrapAdapter<DataItem>(mContext, payByWays, R.layout.order_pay_byway_list_item) {

            protected View split;
            protected ImageView selectImg;
            protected TextView titleTv;
            protected ImageView payByWayIcon;


            @Override
            public void convert(ViewHolder helper, DataItem item) {

                payByWayIcon = helper.getView(R.id.payByWayIcon);
                titleTv = helper.getView(R.id.titleTv);
                payByWayIcon.setImageResource(item.icon);
                titleTv.setText(item.title);
                selectImg = helper.getView(R.id.selectImg);
                split = helper.getView(R.id.split);
                if (item.isSelected) {
                    selectImg.setVisibility(View.VISIBLE);
                } else {
                    selectImg.setVisibility(View.GONE);
                }
                if (helper.getPosition() == size - 1) {
                    split.setVisibility(View.GONE);
                } else {
                    split.setVisibility(View.VISIBLE);
                }
            }
        };
        orderWayList.setAdapter(mOrderByWayAdapter);
        orderWayList.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DataItem item = (DataItem) parent.getAdapter().getItem(position);
            item.isSelected = true;
            singleChecked(payByWays, item);
            mOrderByWayAdapter.notifyDataSetChanged();
        }
    };
    private String orderInfo = "";
    private OrderPayView orderPayView = new OrderPayView() {
        @Override
        public void showData(Object o) {

            orderInfo = o.toString();

            execAlipay(orderInfo);
        }

        @Override
        public void showPayCouponResult(CouponPayModel data) {

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
    };


    private ListView mList;

    private double mTotal;
    public List<BaseCoupon> mCoupons;
    private OrderModel mModel;

    @Override
    public void showOrderDetails(final OrderModel model) {
        mModel = model;
        mCoupons = model.coupons;


        //订单编号
        orderNum.setText(model.details.orderSN);

        //订单总价
        orderTotalPriceTv.setText(model.details.amount);
        String orderPrice = "订单总价: ￥" + model.details.amount + "元";
        CommonUtils.customDisplayText(mRes, orderTotalPriceTv, orderPrice, R.color.text_color36, orderPrice.length() - 1);
        List<DataItem> datas = new ArrayList<DataItem>();

        DataItem item = new DataItem();
        item.title = "订单名称";
        item.content = model.details.orderTitle;
        datas.add(item);
        item = new DataItem();
        item.title = model.details.eventName;
        item.content = "";
        datas.add(item);
        double priceDiscount = 0;
        for (Ticket t : model.tickets) {

            DataItem dataItem = new DataItem();
            dataItem.title = t.name + " " + "电子门票";
            dataItem.content = "￥" + t.price + "x" + t.nums;
            priceDiscount += Double.parseDouble(t.discount);
            datas.add(dataItem);
        }
        item = new DataItem();
        item.title = "应付";
        item.content = "￥" + model.details.amountOri;
        datas.add(item);
        item = new DataItem();
        item.title = "抵扣";
        item.content = "-￥" + priceDiscount + "";
        datas.add(item);
        mList
                .setAdapter(new WrapAdapter<DataItem>(mContext, R.layout.order_list_item_2, datas) {

                    protected TextView contentTv;
                    protected TextView titleTv;


                    @Override
                    public void convert(ViewHolder helper, DataItem item) {

                        titleTv = helper.getView(R.id.titleTv);
                        contentTv = helper.getView(R.id.contentTv);
                        titleTv.setText(item.title);
                        contentTv.setText(item.content);
                    }
                });
        ViewUtil.setListViewHeightBasedOnChildren(mList);

    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.submitOrder:

                int byWayId = 0;
                for (DataItem item : payByWays) {

                    if (item.isSelected) {
                        byWayId = item.id;
                        break;
                    }
                }
                switch (byWayId) {
                    case Constants.PayTypes.WX:
                        mPayPreseneter.loadPayData(mOrderId, "APP", IOUtils.getToken(mContext));
                        break;
                    case Constants.PayTypes.ALIPAY:
                        mAlipayPresenter.loadPayData(IOUtils.getToken(mContext), mOrderId);
                        break;
                    case Constants.PayTypes.KCWC:
                        break;
                }

                break;
        }

    }


    private boolean isSelectCoupon = false;

    private Map<String, String> builderUpdateOrderReqParams() {


        Map<String, String> params = new HashMap<String, String>();

        String coupon = "";
        if (isSelectCoupon) {
            if (mCoupons != null) {

                coupon = GsonUtil.objToJson(mCoupons);
            }
        } else {
            coupon = "";
        }
        params.put("coupon", coupon);
        params.put("id", mOrderId);
        params.put("trade_type", "APP");
        params.put("is_pay", "1");
        params.put("token", IOUtils.getToken(mContext));
        return params;
    }

    private void execWXPay() {
        if (mWXPayMOdel != null) {
            PayUtil.weixinPay(mWXAPI, Constants.WEIXIN_APP_ID, mWXPayMOdel.partnerId,
                    mWXPayMOdel.prepayId, Constants.PACKAGE_VALUE, mWXPayMOdel.nonceStr,
                    mWXPayMOdel.timeStamp, mWXPayMOdel.paySign);
        } else {
            CommonUtils.showToast(mContext, "订单异常，请稍候重试！");
        }
    }

    /**
     * 支付宝支付
     */
    private void execAlipay(final String orderInfo) {


        ThreadPoolProxy.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {

                PayTask alipay = new PayTask(OrderListActivity.this);
                Map map = alipay.payV2(orderInfo, true);

                Message msg = mHandler.obtainMessage();
                msg.obj = map;
                msg.what = WHAT_VALUE;
                mHandler.sendMessage(msg);

                Logger.d("支付宝:" + map);
            }
        });

    }

    private int WHAT_VALUE = 1;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            Intent intent = new Intent();
            if (WHAT_VALUE == msg.what) {

                String result = ((Map) msg.obj).get("resultStatus").toString();


                if (Constants.PayStatus.APAY_SUCCESS == Integer.parseInt(result)) {
                    intent.putExtra(Constants.IntentParams.PAY_STATUS, Constants.PayStatus.APAY_SUCCESS);
                } else {
                    intent.putExtra(Constants.IntentParams.PAY_STATUS, -1);
                }
                intent.putExtra(Constants.IntentParams.DATA, orderInfo);
                intent.setClass(mContext, TicketPayStatusActivity.class);
                startActivity(intent);
            }


        }
    };
    private OrderPayParam mWXPayMOdel;

    @Override
    public void showData(OrderPayParam weiXinPayModel) {
        mWXPayMOdel = weiXinPayModel;
        KPlayCarApp.putValue(Constants.KeyParams.WX_PAY_DATA, mWXPayMOdel);
        Logger.d(weiXinPayModel + "");
        execWXPay();
        finish();
    }

    @Override
    public void showPayCouponResult(CouponPayModel data) {

    }

    private void initView() {
        orderNum = (TextView) findViewById(R.id.orderNum);
        headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);
        list = (ListView) findViewById(R.id.list);
        orderTotalPriceTv = (TextView) findViewById(R.id.orderTotalPriceTv);
        orderTotalLayout = (RelativeLayout) findViewById(R.id.orderTotalLayout);
        orderWayTitleLayout = (RelativeLayout) findViewById(R.id.orderWayTitleLayout);
        orderWayList = (ListView) findViewById(R.id.orderWayList);
        submitOrder = (Button) findViewById(R.id.submitOrder);
        submitOrder.setOnClickListener(this);
    }
}
