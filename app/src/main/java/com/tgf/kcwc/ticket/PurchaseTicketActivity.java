package com.tgf.kcwc.ticket;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.BuyTicketModel;
import com.tgf.kcwc.mvp.model.TicketCoupon;
import com.tgf.kcwc.mvp.model.TicketDetail;
import com.tgf.kcwc.mvp.model.TicketOrderModel;
import com.tgf.kcwc.mvp.presenter.BuyTicketlPresenter;
import com.tgf.kcwc.mvp.presenter.CouponDataPresenter;
import com.tgf.kcwc.mvp.presenter.TicketDataPresenter;
import com.tgf.kcwc.mvp.view.BuyTicketView;
import com.tgf.kcwc.mvp.view.CouponDataView;
import com.tgf.kcwc.mvp.view.TicketDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.ThreadPoolProxy;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.ControlNumberView;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SpecRectView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.tgf.kcwc.util.ViewUtil.setEnabled;

/**
 * Author：Jenny
 * Date:2017/1/3 19:46
 * E-mail：fishloveqin@gmail.com
 * 购票列表
 */

public class PurchaseTicketActivity extends BaseActivity implements BuyTicketView {

    protected SimpleDraweeView img;
    protected ImageView ticketDetailImage;
    protected RelativeLayout headerLayout;
    protected RelativeLayout ticketDetailContentLayout, ticketAgreementLayout;
    protected TextView senseName;
    protected RelativeLayout titleLayout;
    protected ListView ticketList;
    protected TextView ticketDescTv;
    protected TextView accept;
    protected TextView aboutTicket;
    protected TextView totalTv;
    protected TextView tag;
    protected TextView spendText;
    protected TextView deductionTv;
    protected Button submitOrder;
    protected ImageView getCouponImg;
    protected ImageView closeCouponBtn;
    protected RelativeLayout couponTitleLayout;
    protected ListView couponList;
    protected RelativeLayout couponContentLayout;
    protected WebView ticketDescContent;
    protected ImageView ticketUpImage;
    protected LinearLayout orderConfirmLayout;
    protected RelativeLayout getCouponImgLayout;
    protected RelativeLayout partOneLayout;
    protected RelativeLayout partTwoLayout;
    protected TextView msgTv;
    private ListView mList;
    private TextView mSenseNameTv;
    private TextView mSpendTv;
    private Button mSubmitBtn;
    private ScrollView mScrollView;
    private int mSenseId;

    private TextView mAboutTicketTv;

    private BuyTicketlPresenter mPresenter;
    private CouponDataPresenter mCouponPresenter;
    private CouponDataPresenter mGetCouponPresenter;

    private TicketDataPresenter mTicketDataPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSenseId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        super.setContentView(R.layout.activity_purchase_ticket);


    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(R.string.buy_ticket);
    }

    @Override
    protected void setUpViews() {
        initView();

        mPresenter = new BuyTicketlPresenter();
        mPresenter.attachView(this);
        mPresenter.loadTickets("", mSenseId + "", IOUtils.getToken(mContext));
        mCouponPresenter = new CouponDataPresenter();
        mCouponPresenter.attachView(couponDataView);
        mGetCouponPresenter = new CouponDataPresenter();
        mGetCouponPresenter.attachView(getCouponDataView);
        mTicketDataPresenter = new TicketDataPresenter();
        mTicketDataPresenter.attachView(ticketOrderModelTicketDataView);
    }

    private TicketDataView<TicketOrderModel> ticketOrderModelTicketDataView = new TicketDataView<TicketOrderModel>() {
        @Override
        public void showData(TicketOrderModel ticketOrderModel) {

            showOrderBaseInfo(ticketOrderModel);
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

    private void showOrderBaseInfo(TicketOrderModel ticketOrderModel) {

        spendText.setText(NumFormatUtil.getFmtMoneyTwoNum(ticketOrderModel.amount));
        deductionTv.setText("已抵扣 ￥" + NumFormatUtil.getFmtMoneyTwoNum(ticketOrderModel.amountDiscount));
    }


    private CouponDataView getCouponDataView = new CouponDataView() {
        @Override
        public void showData(Object o) {


            CommonUtils.showToast(mContext, "代金券领取成功!");
            mCouponPresenter.loadCoupons(mSenseId + "", IOUtils.getToken(mContext));
            ticketOrderDiscountInfo();
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
    };
    private CouponDataView<List<TicketCoupon>> couponDataView = new CouponDataView<List<TicketCoupon>>() {
        @Override
        public void showData(List<TicketCoupon> ticketCoupons) {

            if (ticketCoupons != null && ticketCoupons.size() == 0) {
                couponList.setVisibility(View.GONE);
                msgTv.setVisibility(View.VISIBLE);
            } else {
                couponList.setVisibility(View.VISIBLE);
                msgTv.setVisibility(View.GONE);
            }

            WrapAdapter<TicketCoupon> adapter = new WrapAdapter<TicketCoupon>(mContext, ticketCoupons, R.layout.ticket_coupon_list_item) {

                protected Button getCouponBtn;
                protected TextView expireTv;
                protected TextView descTv;
                protected TextView priceTv;


                @Override
                public void convert(ViewHolder helper, final TicketCoupon item) {

                    priceTv = helper.getView(R.id.priceTv);
                    descTv = helper.getView(R.id.descTv);
                    expireTv = helper.getView(R.id.expireTv);
                    getCouponBtn = helper.getView(R.id.getCouponBtn);
                    priceTv.setText("￥" + item.denomination);
                    descTv.setText(item.ticketType);
                    CommonUtils.customDisplayText(mRes, descTv, "限 " + item.ticketType, R.color.text_color9);
                    expireTv.setText("使用期限 " + DateFormatUtil.dispActiveTime2(item.beginTime, item.endTime));

                    int canGet = item.canGet;
                    if (canGet > 0) {
                        getCouponBtn.setText("领取 x" + canGet);
                        getCouponBtn.setEnabled(true);
                        getCouponBtn.setTextColor(mRes.getColor(R.color.text_color36));
                        getCouponBtn.setBackgroundResource(R.drawable.button_bg_35);
                    } else {
                        getCouponBtn.setText("已领取");
                        getCouponBtn.setTextColor(mRes.getColor(R.color.text_color28));
                        getCouponBtn.setEnabled(false);
                        getCouponBtn.setBackgroundResource(R.drawable.button_bg_36);
                    }
                    getCouponBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("coupon_id", item.couponId + "");
                            params.put("num", "1");
                            params.put("token", IOUtils.getToken(mContext));
                            mGetCouponPresenter.getCoupon(params);
                        }
                    });

                }
            };

            couponList.setAdapter(adapter);
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
    };

    private void buildOrder() {

        Iterator iterator = mTicketOrderDataItems.entrySet().iterator();
        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();

        int count = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            DataItem dataItem = (DataItem) entry.getValue();
            stringBuilder1.append(dataItem.id).append(",");
            int nums = dataItem.nums;
            if (nums == 0) {
                count++;
            }
            stringBuilder2.append(nums).append(",");
        }
        if (count == mTicketOrderDataItems.size()) {
            spendText.setText("0.00");
            deductionTv.setText("已抵扣 ￥" + "0.00");
            CommonUtils.showToast(mContext, "至少选购一张门票！");
            return;
        }
        String ids = stringBuilder1.toString();
        String nums = stringBuilder2.toString();
        if (ids.length() > 0) {
            ids = ids.substring(0, ids.length() - 1);
        }
        if (nums.length() > 0) {
            nums = nums.substring(0, nums.length() - 1);
        }
        Logger.d("ids:" + ids + ",nums:" + nums);


        mPresenter.createOrder(ids, nums, IOUtils.getToken(mContext), "APP");
    }


    /**
     * 购票订单
     */
    private void ticketOrderDiscountInfo() {

        Iterator iterator = mTicketOrderDataItems.entrySet().iterator();
        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();

        int count = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            DataItem dataItem = (DataItem) entry.getValue();
            stringBuilder1.append(dataItem.id).append(",");
            int nums = dataItem.nums;
            if (nums == 0) {
                count++;
            }
            stringBuilder2.append(nums).append(",");
        }
        if (count == mTicketOrderDataItems.size()) {
            spendText.setText("0.00");
            deductionTv.setText("已抵扣 ￥" + "0.00");
            return;
        }
        String ids = stringBuilder1.toString();
        String nums = stringBuilder2.toString();
        if (ids.length() > 0) {
            ids = ids.substring(0, ids.length() - 1);
        }
        if (nums.length() > 0) {
            nums = nums.substring(0, nums.length() - 1);
        }
        Logger.d("ids:" + ids + ",nums:" + nums);
        Map<String, String> params = new HashMap<>();
        params.put("tid", ids);
        params.put("nums", nums);
        params.put("token", IOUtils.getToken(mContext));


        if (ids.length() > 0 && !nums.equals("0")) {
            mTicketDataPresenter.getTicketOrderDiscountInfo(params);
        }


    }


    private double total = 0;

    private HashMap<Integer, DataItem> mDataItems = new HashMap<Integer, DataItem>();

    private HashMap<Integer, DataItem> mTicketOrderDataItems = new HashMap<Integer, DataItem>();
    private static final int TMAX = 9;
    private BuyTicketModel mModel;

    @Override
    public void showBuyTickets(final BuyTicketModel model) {
        mModel = model;


        showHeaderInfo(model);
        showTicketListInfo(model);

    }

    private void showTicketListInfo(BuyTicketModel model) {


        WrapAdapter<TicketDetail> adapter = new WrapAdapter<TicketDetail>(mContext, model.tickets, R.layout.purchase_ticket_item) {

            protected ControlNumberView cnView;
            protected RelativeLayout priceLayout;
            protected TextView price;
            protected TextView moneyTag;
            protected TextView ticketRemarkTv;
            protected TextView ticketDescTv;
            protected TextView ticketTypeTv;
            protected RelativeLayout leftLayout;
            protected SpecRectView specRectView;
            protected View rootView;

            @Override
            public void convert(final ViewHolder helper, final TicketDetail item) {


                specRectView = helper.getView(R.id.specRectView);
                leftLayout = helper.getView(R.id.leftLayout);
                ticketTypeTv = helper.getView(R.id.ticketTypeTv);
                ticketDescTv = helper.getView(R.id.ticketDescTv);
                ticketRemarkTv = helper.getView(R.id.ticketRemarkTv);
                moneyTag = helper.getView(R.id.moneyTag);
                price = helper.getView(R.id.price);
                moneyTag.setTextColor(Color.parseColor(item.color));
                price.setTextColor(Color.parseColor(item.color));
                priceLayout = helper.getView(R.id.priceLayout);
                cnView = helper.getView(R.id.cnView);
                int canBeByNum = item.canBeByNum;
                if (canBeByNum == 0) {
                    ticketDescTv.setText("不限张数");
                    canBeByNum = TMAX;
                } else {
                    ticketDescTv.setText("每人限购" + item.canBeByNum + "张");
                }
                cnView.setMax(canBeByNum);

                cnView.setmOnNumberChangedListener(
                        new ControlNumberView.OnNumberChangedListener() {
                            @Override
                            public void onNumberChangedListener(ControlNumberView controlNumberView,
                                                                float number) {

                                DataItem dataItem = mTicketOrderDataItems.get(helper.getPosition());
                                if (dataItem == null) {
                                    dataItem = new DataItem();
                                    mTicketOrderDataItems.put(helper.getPosition(), dataItem);
                                }
                                dataItem.id = item.id;
                                dataItem.nums = (int) number;


                                ticketOrderDiscountInfo();
                            }
                        });


                //specRectView.setDrawTicketTypeColor(item.color + "");
                specRectView.setAlpha(Double.parseDouble(item.alpha));
                specRectView.setBGColor(item.color);

                ticketRemarkTv.setText(item.remarks);
                ticketTypeTv.setText(item.ticketName);
                price.setText(item.price);
            }
        };

        ticketList.setAdapter(adapter);

        ViewUtil.setListViewHeightBasedOnChildren(ticketList);
    }

    private void showHeaderInfo(final BuyTicketModel model) {


        if (model.haveCoupon == 1) {
            getCouponImg.setVisibility(View.VISIBLE);
        } else {
            getCouponImg.setVisibility(View.GONE);
        }
        img.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.cover, 540, 270)));
        senseName.setText(model.senseName);

        String ticketDescStr = model.ticketDescContent;

        if (TextUtil.isEmpty(ticketDescStr)) {
            ticketDetailImage.setVisibility(View.GONE);
        } else {

            partTwoLayout.getBackground().setAlpha(100);
            ticketDetailContentLayout.setVisibility(View.VISIBLE);
            ticketDetailImage.setVisibility(View.VISIBLE);
            ticketDescContent.getSettings().setDefaultTextEncodingName("UTF-8");
            ticketDescContent.loadDataWithBaseURL("", WebviewUtil.getHtmlData(ticketDescStr), "text/html", "UTF-8", "");
            setEnabled(getCouponImgLayout, false);
            setEnabled(orderConfirmLayout, false);
            setEnabled(mScrollView, false);

            ThreadPoolProxy.getInstance().executeTask(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.arg1 = model.haveContent;
                    mHandler.sendMessage(msg);
                }
            });
        }
        if (model.haveContent == 1) {

            ticketAgreementLayout.setVisibility(View.VISIBLE);
            submitOrder.setSelected(false);
            submitOrder.setEnabled(false);
        } else {
            ticketAgreementLayout.setVisibility(View.GONE);
            submitOrder.setSelected(true);
            submitOrder.setEnabled(true);
        }

    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            int what = msg.what;
            if (what == 0) {


                setEnabled(getCouponImgLayout, true);
                setEnabled(orderConfirmLayout, true);
                setEnabled(mScrollView, true);
                ticketDetailContentLayout.setVisibility(View.GONE);
                partTwoLayout.getBackground().setAlpha(255);
                if (msg.arg1 == 1) {
                    submitOrder.setEnabled(false);
                } else {
                    submitOrder.setEnabled(true);
                }
            }

        }
    };

    @Override
    public void generateOrderSuccess(String orderId) {

        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.ID, orderId);
        CommonUtils.startNewActivity(mContext, args, OrderListActivity.class);
    }

    @Override
    public void generateOrderFailure(String msg) {

        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        dismissLoadingDialog();
        CommonUtils.showToast(mContext, "暂时不能提交订单");
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mCouponPresenter != null) {
            mCouponPresenter.detachView();
        }

        if (mGetCouponPresenter != null) {
            mGetCouponPresenter.detachView();
        }
    }

    private void initView() {
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        img = (SimpleDraweeView) findViewById(R.id.img);
        ticketDetailImage = (ImageView) findViewById(R.id.ticketDetailImage);
        headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);
        ticketDetailContentLayout = (RelativeLayout) findViewById(R.id.ticketDetailContentLayout);
        ticketAgreementLayout = (RelativeLayout) findViewById(R.id.ticketAgreementLayout);
        senseName = (TextView) findViewById(R.id.senseName);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        ticketList = (ListView) findViewById(R.id.ticketList);
        ticketDescTv = (TextView) findViewById(R.id.ticketDescTv);
        accept = (TextView) findViewById(R.id.accept);
        aboutTicket = (TextView) findViewById(R.id.aboutTicket);
        totalTv = (TextView) findViewById(R.id.totalTv);
        tag = (TextView) findViewById(R.id.tag);
        spendText = (TextView) findViewById(R.id.spendText);
        deductionTv = (TextView) findViewById(R.id.deductionTv);
        submitOrder = (Button) findViewById(R.id.submitOrder);
        getCouponImg = (ImageView) findViewById(R.id.getCouponImg);
        closeCouponBtn = (ImageView) findViewById(R.id.closeCouponBtn);
        couponTitleLayout = (RelativeLayout) findViewById(R.id.couponTitleLayout);
        couponList = (ListView) findViewById(R.id.couponList);
        couponContentLayout = (RelativeLayout) findViewById(R.id.couponContentLayout);
        ticketDetailImage.setOnClickListener(this);
        submitOrder.setOnClickListener(this);
        accept.setOnClickListener(this);
        aboutTicket.setOnClickListener(this);
        getCouponImg.setOnClickListener(this);
        ticketDescContent = (WebView) findViewById(R.id.ticketDescContent);
        ticketUpImage = (ImageView) findViewById(R.id.ticketUpImage);
        ticketUpImage.setOnClickListener(this);
        closeCouponBtn.setOnClickListener(this);
        titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        orderConfirmLayout = (LinearLayout) findViewById(R.id.orderConfirmLayout);
        getCouponImgLayout = (RelativeLayout) findViewById(R.id.getCouponImgLayout);
        partOneLayout = (RelativeLayout) findViewById(R.id.partOneLayout);
        partTwoLayout = (RelativeLayout) findViewById(R.id.partTwoLayout);
        msgTv = (TextView) findViewById(R.id.msgTv);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.accept:

                boolean isSelected = view.isSelected();
                if (isSelected) {
                    view.setSelected(!isSelected);
                    submitOrder.setSelected(false);
                    submitOrder.setEnabled(false);
                } else {
                    view.setSelected(!isSelected);
                    submitOrder.setSelected(true);
                    submitOrder.setEnabled(true);
                }
                break;
            case R.id.aboutTicket:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mSenseId);
                String name = "";
                if (mModel != null) {
                    name = mModel.senseName;
                }
                args.put(Constants.IntentParams.DATA, name);
                CommonUtils.startNewActivity(mContext, args, AboutTicketActivity.class);
                break;
            case R.id.ticketDetailImage:

                ticketDetailContentLayout.setVisibility(View.VISIBLE);
                ticketDetailImage.setVisibility(View.GONE);
                partTwoLayout.getBackground().setAlpha(100);
                getCouponImgLayout.setClickable(false);
                getCouponImgLayout.setFocusable(false);
                getCouponImgLayout.setFocusableInTouchMode(false);
                setEnabled(getCouponImgLayout, false);
                setEnabled(orderConfirmLayout, false);
                setEnabled(mScrollView, false);
                break;

            case R.id.ticketUpImage:
                ticketDetailContentLayout.setVisibility(View.GONE);
                ticketDetailImage.setVisibility(View.VISIBLE);
                partTwoLayout.getBackground().setAlpha(255);
                mScrollView.setEnabled(true);
                setEnabled(getCouponImgLayout, true);
                setEnabled(orderConfirmLayout, true);
                setEnabled(mScrollView, true);
                break;
            case R.id.submitOrder:

                buildOrder();
                break;
            case R.id.getCouponImg:
                couponContentLayout.setVisibility(View.VISIBLE);
                mCouponPresenter.loadCoupons(mSenseId + "", IOUtils.getToken(mContext));
                break;
            case R.id.closeCouponBtn:
                couponContentLayout.setVisibility(View.GONE);
                break;
        }

    }


}
