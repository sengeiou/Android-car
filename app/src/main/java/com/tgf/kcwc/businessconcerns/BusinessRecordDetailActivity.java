package com.tgf.kcwc.businessconcerns;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.RecordActivityModel;
import com.tgf.kcwc.mvp.model.RecordCouponModel;
import com.tgf.kcwc.mvp.model.RecordSeeCarModel;
import com.tgf.kcwc.mvp.model.RecordTicketModel;
import com.tgf.kcwc.mvp.presenter.RecordActivityPresenter;
import com.tgf.kcwc.mvp.presenter.RecordCouponPresenter;
import com.tgf.kcwc.mvp.presenter.RecordSeeCarPresenter;
import com.tgf.kcwc.mvp.presenter.RecordTicketPresenter;
import com.tgf.kcwc.mvp.view.RecordActivityView;
import com.tgf.kcwc.mvp.view.RecordCouponView;
import com.tgf.kcwc.mvp.view.RecordSeeCarView;
import com.tgf.kcwc.mvp.view.RecordTicketView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle 商务记录详情页
 */
public class BusinessRecordDetailActivity extends BaseActivity implements RecordActivityView, RecordTicketView, RecordSeeCarView, RecordCouponView {
    private SimpleDraweeView headerImg;
    private TextView nameTv;
    private ImageView brandIv;
    private ImageView denderIv;
    private TextView ageTv;
    private ImageView modelTv;
    private ImageView masterTv;

    private ListView mListView;

    WrapAdapter mActivityAdapter;
    WrapAdapter mCouponAdapter;
    WrapAdapter mSeeCarAdapter;
    WrapAdapter mTicketAdapter;

    private ArrayList<RecordActivityModel.ListItem> mActivityList = new ArrayList<>();
    private ArrayList<RecordCouponModel.ListItem> mCouponList = new ArrayList<>();
    private ArrayList<RecordSeeCarModel.ListItem> mSeeCarList = new ArrayList<>();
    private ArrayList<RecordTicketModel.ListItem> mTicketList = new ArrayList<>();

    RecordActivityPresenter mActivityPresenter;
    RecordCouponPresenter mCouponPresenter;
    RecordSeeCarPresenter mSeeCarPresenter;
    RecordTicketPresenter mTicketPresenter;
    //好友id
    private int friendId;
    //类型
    private int type;

    private int page = 1;
    private boolean isRefresh;

    @Override
    protected void setUpViews() {
        friendId = getIntent().getIntExtra(Constants.IntentParams.DATA, -1);
        type = getIntent().getIntExtra(Constants.IntentParams.DATA2, -1);
        friendId = getIntent().getIntExtra(Constants.IntentParams.DATA, -1);
        headerImg = (SimpleDraweeView) findViewById(R.id.detail_header_img);
        nameTv = (TextView) findViewById(R.id.detail_nickname);
        denderIv = (ImageView) findViewById(R.id.detail_gender);
        brandIv = (ImageView) findViewById(R.id.detail_brand);
        ageTv = (TextView) findViewById(R.id.detail_age);
        modelTv = (ImageView) findViewById(R.id.detail_model);
        masterTv = (ImageView) findViewById(R.id.detail_master);

        mListView = (ListView) findViewById(R.id.list_view);
        initRefreshLayout(mBGDelegate);

        initPresenter();
        selectLoad();
    }

    private void initPresenter() {
        switch (type) {
            case 1:
                mSeeCarPresenter = new RecordSeeCarPresenter();
                mSeeCarPresenter.attachView(this);
                break;
            case 2:
                mTicketPresenter = new RecordTicketPresenter();
                mTicketPresenter.attachView(this);
                break;
            case 3:
                mCouponPresenter = new RecordCouponPresenter();
                mCouponPresenter.attachView(this);
                break;
            case 4:
                mActivityPresenter = new RecordActivityPresenter();
                mActivityPresenter.attachView(this);
                break;
        }
    }

    private void selectLoad() {
        switch (type) {
            case 1:
                mSeeCarPresenter.getSeeCar(IOUtils.getToken(getContext()), friendId, type, page);
                break;
            case 2:
                mTicketPresenter.getTicket(IOUtils.getToken(getContext()), friendId, type, page);
                break;
            case 3:
                mCouponPresenter.getCoupon(IOUtils.getToken(getContext()), friendId, type, page);
                break;
            case 4:
                mActivityPresenter.getActivity(IOUtils.getToken(getContext()), friendId, type, page);
                break;
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("票/证记录");
        setMiddleTitle(text);
    }

    private void setMiddleTitle(TextView text) {
        switch (type) {
            case 1:
                text.setText("看车记录");
                break;
            case 2:
                text.setText("票证记录");
                break;
            case 3:
                text.setText("代金卷记录");
                break;
            case 4:
                text.setText("参与活动记录");
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_record_detail);
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
        selectLoad();
    }

    public void initAdapter() {
        switch (type) {
            case 4:
                mActivityAdapter = new WrapAdapter<RecordActivityModel.ListItem>(mContext, mActivityList, R.layout.detail_activity_item) {
                    @Override
                    public void convert(ViewHolder helper, RecordActivityModel.ListItem item) {
                        if (helper.getPosition() == 0) {
                            helper.setImageResource(R.id.item_activity_circle, R.drawable.shape_see_car_circle_s);
                        } else {
                            helper.setImageResource(R.id.item_activity_circle, R.drawable.shape_see_car_circle_n);
                        }
                        helper.setText(R.id.item_time, item.createTime);
                        helper.setText(R.id.item_activity_title, item.title);
                        TextView textView = helper.getView(R.id.item_activity_create_by);
                        if (StringUtils.checkNull(item.createBy)) {
                            textView.setVisibility(View.VISIBLE);
                            helper.setText(R.id.item_activity_create_by, Html.fromHtml("发帖人<font color=\"#36A95C\">   " + item.createBy + "</font>"));
                        } else {
                            textView.setVisibility(View.GONE);
                        }
                        helper.setText(R.id.item_activity_start, Html.fromHtml("<font color=\"#333333\">出发地</font>   " + item.start));
                        helper.setText(R.id.item_activity_destination, Html.fromHtml("<font color=\"#333333\">目的地</font>   " + item.destination));
                        helper.setText(R.id.item_activity_begin_time, Html.fromHtml("<font color=\"#333333\">开始时间</font>  " + item.beginTime));
                        helper.setText(R.id.item_activity_num, Html.fromHtml("<font color=\"#36A95C\">" + item.num + "</font>人参加"));
                        TextView cancel = helper.getView(R.id.item_activity_cancel);
                        if (item.isCancel == 0) {//取消
                            cancel.setVisibility(View.VISIBLE);
                            cancel.setText(item.cancelTime + "  取消报名");
                        } else {
                            cancel.setVisibility(View.GONE);
                        }
                        helper.setText(R.id.item_activity_status, item.activityStatus);
                    }
                };
                break;
            case 3:
                mCouponAdapter = new WrapAdapter<RecordCouponModel.ListItem>(mContext, mCouponList, R.layout.detail_coupon_item) {
                    @Override
                    public void convert(ViewHolder helper, RecordCouponModel.ListItem item) {
                        if (helper.getPosition() == 0) {
                            helper.setImageResource(R.id.item_coupon_circle, R.drawable.shape_see_car_circle_s);
                        } else {
                            helper.setImageResource(R.id.item_coupon_circle, R.drawable.shape_see_car_circle_n);
                        }
                        helper.setText(R.id.item_time, item.createTime);
                        TextView receive = helper.getView(R.id.item_receive_get);
                        if (item.couponType.equals("buy")) {//购买
                            helper.setText(R.id.item_record, "购买代金卷");
                            receive.setVisibility(View.GONE);
                        } else if (item.couponType.equals("distribute")) {//领取
                            helper.setText(R.id.item_record, "发放代金卷");
                            receive.setVisibility(View.VISIBLE);
                            receive.setText(Html.fromHtml("<font color=\"#36A95C\">" + item.salerName + "  </font>发放"));
                        }
                        helper.setText(R.id.item_order, "订单编号：" + item.orderSn);
                        helper.setText(R.id.item_name, item.title);
                        helper.setText(R.id.item_name_num, "x" + item.couponTotal);
                        helper.setText(R.id.item_name_price, item.totalPrice);

                        ListView listView = helper.getView(R.id.item_ticket_list);
                        if (item.codeItems != null) {
                            WrapAdapter ticketAdapter = new WrapAdapter<RecordCouponModel.ListItem.CodeItem>(mContext, item.codeItems, R.layout.ticket_list_item) {

                                @Override
                                public void convert(ViewHolder helper, RecordCouponModel.ListItem.CodeItem item) {
                                    //核销编号
                                    TextView checkoutOrderTv = helper.getView(R.id.item_checkout_serial_sn);
                                    //是否核销
                                    String time = null;
                                    if (item.checkId > 0) {//核销
                                        time = item.checkTime + " 核销";
                                    } else {
                                        time = item.receiveTime + " 领取";
                                    }
                                    //编号
                                    checkoutOrderTv.setText(item.code);
                                    //时间
                                    helper.setText(R.id.item_checkout_time, time);

                                }
                            };
                            listView.setAdapter(ticketAdapter);
                        }

                    }
                };
                break;
            case 1:
                mSeeCarAdapter = new WrapAdapter<RecordSeeCarModel.ListItem>(mContext, mSeeCarList, R.layout.detail_see_car_item) {
                    @Override
                    public void convert(ViewHolder helper, RecordSeeCarModel.ListItem item) {
                        if (helper.getPosition() == 0) {
                            helper.setImageResource(R.id.item_see_car_circle, R.drawable.shape_see_car_circle_s);
                        } else {
                            helper.setImageResource(R.id.item_see_car_circle, R.drawable.shape_see_car_circle_n);
                        }
                        helper.setText(R.id.item_time, item.createTime);
                        String statusStr = null;
                        switch (item.orderOfferItem.offerStatus) {
                            case 0://未响应
                                statusStr = "未响应";
                                break;
                            case 1://已完成
                                statusStr = "已完成";
                                break;
                            case 2://已关闭
                                statusStr = "已关闭";
                                break;
                            case 3://服务中
                                statusStr = "服务中";
                                break;
                        }
                        helper.setText(R.id.item_name, item.carName);
                        helper.setText(R.id.item_status, statusStr);
                        helper.setText(R.id.item_order, "请求订单" + item.orderOfferItem.orderId);
                        helper.setText(R.id.item_color, "外观：" + item.appearanceColor + "    内饰：" + item.interiorColor);
                        helper.setText(R.id.item_describle, item.description);
                        TextView saler = helper.getView(R.id.item_saler);
                        if (StringUtils.checkNull(item.orderOfferItem.salerName)) {
                            saler.setVisibility(View.VISIBLE);
                        } else {
                            saler.setVisibility(View.GONE);
                        }
                    }
                };
                break;
            case 2:
                mTicketAdapter = new WrapAdapter<RecordTicketModel.ListItem>(mContext, mTicketList, R.layout.detail_ticket_item) {
                    @Override
                    public void convert(ViewHolder helper, final RecordTicketModel.ListItem item) {
                        if (helper.getPosition() == 0) {
                            helper.setImageResource(R.id.item_ticket_circle, R.drawable.shape_see_car_circle_s);
                        } else {
                            helper.setImageResource(R.id.item_ticket_circle, R.drawable.shape_see_car_circle_n);
                        }
                        String status = null;
                        //证的状态
                        if (item.status == 1) {//正常

                        } else if (item.status == 2) {//挂失
                            status = "挂失";
                        } else if (item.status == 3) {//过期
                            status = "过期";
                        }
                        //票的状态
                        //票证创建时间
                        helper.setText(R.id.item_time, item.createTime);
                        int eventNameBg = 0;
                        int ticketNameColor = R.color.bg_3;
                        //隐藏门票数量
                        TextView ticketNumTv = helper.getView(R.id.item_ticket_num);
                        //票证类型
                        if (item.recordType.equals("ticket")) {
                            ticketNameColor = R.color.bg_3;
                            helper.setText(R.id.item_ticket, "发放门票");
                            eventNameBg = R.drawable.shape_ticket_bg;
                            ticketNumTv.setVisibility(View.VISIBLE);
                            //票的数量
                            ticketNumTv.setText("x" + item.num);
                            ListView listView = helper.getView(R.id.item_ticket_list);
                            ArrayList<RecordTicketModel.ListItem.CodeListItem> codeItems = new ArrayList<>();
                            codeItems.addAll(item.codeListItems);
                            WrapAdapter ticketAdapter = new WrapAdapter<RecordTicketModel.ListItem.CodeListItem>(mContext, codeItems, R.layout.ticket_list_item) {

                                @Override
                                public void convert(ViewHolder helper, RecordTicketModel.ListItem.CodeListItem item) {
                                    //核销编号
                                    TextView checkoutOrderTv = helper.getView(R.id.item_checkout_serial_sn);
                                    String time = null;
                                    switch (item.status) {
                                        case 0://未使用
                                            //是否领取
                                            if (item.isReceive > 0) {//已领取
                                                time = item.receiveTime + " 领取";
                                            } else {//未领取
                                                time = "未领取";
                                            }
                                            break;
                                        case 1://已使用 == 核销
                                            if (item.isReceive > 0) {//已使用
                                                //中划线
                                                checkoutOrderTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                                time = item.checkoutTime + " 核销";
                                            }
                                            break;
                                        case 2://已失效 == 没领取
                                            //是否领取
                                            if (item.isReceive > 0) {//已领取
                                            } else {//未领取
                                                time = "领取失效";
                                            }
                                            break;
                                        case 3://已过期 == 领取没使用 + 不能使用
                                            if (item.isReceive > 0) {//已领取
                                                time = item.receiveTime + " 过期";
                                            }
                                            break;
                                        case 4://已取消
                                            break;
                                        case 5://退款中
                                            break;
                                        case 6://已退款
                                            break;
                                        case 7://退款失败
                                            break;
                                    }
                                    //编号
                                    checkoutOrderTv.setText(item.serialSn);
                                    //时间
                                    helper.setText(R.id.item_checkout_time, time);

                                }
                            };
                            listView.setAdapter(ticketAdapter);
                        } else if (item.recordType.equals("cert")) {
                            ticketNameColor = R.color.text_color6;
                            helper.setText(R.id.item_ticket, "发放证件");
                            eventNameBg = R.drawable.shape_cert_bg;
                            ticketNumTv.setVisibility(View.GONE);
                            TextView notReceiveTv = helper.getView(R.id.item_not_receive);
                            TextView printTv = helper.getView(R.id.item_receive_print);
                            TextView receiveTv = helper.getView(R.id.item_receive);
                            TextView enterTv = helper.getView(R.id.item_enter);
                            //打印
//                        printTv.setVisibility(View.VISIBLE);
//                        printTv.setText(item.printTime + "  打印");
                            //证 是否领取
                            if (item.isReceive > 0) {//已领取
                                notReceiveTv.setVisibility(View.GONE);
                                receiveTv.setVisibility(View.VISIBLE);
                                receiveTv.setText(item.receiveTime + " 领取");
                                //证 是否进场
                                if (item.isEnter > 0) {//已进场
                                    enterTv.setVisibility(View.VISIBLE);
                                    enterTv.setText(item.enterTime + " 进场");
                                } else {
                                    enterTv.setVisibility(View.GONE);
                                }
                            } else {
                                receiveTv.setVisibility(View.GONE);
                                notReceiveTv.setVisibility(View.VISIBLE);
                                notReceiveTv.setText("客户未领取，自动收回");
                            }
                        }
                        //编号
                        TextView serialSn = helper.getView(R.id.item_order);
                        if (StringUtils.checkNull(item.serialSn)) {
                            serialSn.setVisibility(View.VISIBLE);
                            serialSn.setText("订单编号：" + item.serialSn);
                        } else {
                            serialSn.setVisibility(View.GONE);
                        }
                        //发票人
                        TextView salerName = helper.getView(R.id.item_receive_get);
                        if (StringUtils.checkNull(item.salerName)) {
                            salerName.setVisibility(View.VISIBLE);
                            salerName.setText(item.salerName + "  发票");
                        } else {
                            salerName.setVisibility(View.GONE);
                        }
                        //展会名称
                        TextView textView = helper.getView(R.id.item_title);
                        if (StringUtils.checkNull(item.name)) {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(item.name);
                        } else {
                            textView.setVisibility(View.GONE);
                        }
                        //展会名称背景
                        RelativeLayout relativeLayout = helper.getView(R.id.item_ticket_ll);
                        relativeLayout.setBackgroundResource(eventNameBg);
                        //票证名称
                        TextView ticketName = helper.getView(R.id.item_ticket_name);
                        if (StringUtils.checkNull(item.type)) {
                            ticketName.setText(item.type);
                            ticketName.setTextColor(getResources().getColor(ticketNameColor));
                        }

                    }
                };
                break;
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingIndicator(true);
        } else {
            stopRefreshAll();
            showLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showTicket(ArrayList<RecordTicketModel.ListItem> list) {
        if (page == 1) {
            mTicketList.clear();
        }
        mTicketList.addAll(list);
        if (null != mTicketAdapter) {
            mListView.setAdapter(mTicketAdapter);
            mTicketAdapter.notifyDataSetChanged();
        } else {
            initAdapter();
            mListView.setAdapter(mTicketAdapter);
            mTicketAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showTicketHead(RecordTicketModel.UserItem userItem) {
        headerImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(userItem.avatar, 144, 144)));
        nameTv.setText(userItem.nickname);
        //性别
        if (userItem.sex == 1) {
            denderIv.setImageResource(R.drawable.icon_men);
        } else {
            denderIv.setImageResource(R.drawable.icon_women);
        }
        //年龄
        ageTv.setText(userItem.age + "");
//        Date date = new Date(userItem.birthday);
//        try {
//            ageTv.setText(getAge(date));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //模特
        if (userItem.isModel == 1) {
            modelTv.setVisibility(View.VISIBLE);
        } else {
            modelTv.setVisibility(View.GONE);
        }
        //达人
        if (userItem.isDoyen == 1) {
            masterTv.setVisibility(View.VISIBLE);
        } else {
            masterTv.setVisibility(View.GONE);
        }
        //车主 isMaster
        if (userItem.isMaster == 1) {
            brandIv.setVisibility(View.VISIBLE);
            brandIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(userItem.logo, 144, 144)));
        } else {
            brandIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCoupon(ArrayList<RecordCouponModel.ListItem> list) {
        if (page == 1) {
            mCouponList.clear();
        }
        mCouponList.addAll(list);
        if (null != mCouponAdapter) {
            mListView.setAdapter(mCouponAdapter);
            mCouponAdapter.notifyDataSetChanged();
        } else {
            initAdapter();
            mListView.setAdapter(mCouponAdapter);
            mCouponAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showCouponHead(RecordCouponModel.UserItem userItem) {
        headerImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(userItem.avatar, 144, 144)));
        nameTv.setText(userItem.nickname);
        //性别
        if (userItem.sex == 1) {
            denderIv.setImageResource(R.drawable.icon_men);
        } else {
            denderIv.setImageResource(R.drawable.icon_women);
        }
        //年龄
        ageTv.setText(userItem.age + "");
//        Date date = new Date(userItem.birthday);
//        try {
//            ageTv.setText(getAge(date));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //模特
        if (userItem.isModel == 1) {
            modelTv.setVisibility(View.VISIBLE);
        } else {
            modelTv.setVisibility(View.GONE);
        }
        //达人
        if (userItem.isDoyen == 1) {
            masterTv.setVisibility(View.VISIBLE);
        } else {
            masterTv.setVisibility(View.GONE);
        }
        //车主 isMaster
        if (userItem.isMaster == 1) {
            brandIv.setVisibility(View.VISIBLE);
            brandIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(userItem.logo, 144, 144)));
        } else {
            brandIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showActivity(ArrayList<RecordActivityModel.ListItem> list) {
        if (page == 1) {
            mActivityList.clear();
        }
        mActivityList.addAll(list);
        if (null != mActivityAdapter) {
            mListView.setAdapter(mActivityAdapter);
            mActivityAdapter.notifyDataSetChanged();
        } else {
            initAdapter();
            mListView.setAdapter(mActivityAdapter);
            mActivityAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showActivityHead(RecordActivityModel.UserItem userItem) {
        headerImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(userItem.avatar, 144, 144)));
        nameTv.setText(userItem.nickname);
        //性别
        if (userItem.sex == 1) {
            denderIv.setImageResource(R.drawable.icon_men);
        } else {
            denderIv.setImageResource(R.drawable.icon_women);
        }
        //年龄
        ageTv.setText(userItem.age + "");
//        Date date = new Date(userItem.birthday);
//        try {
//            ageTv.setText(getAge(date));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //模特
        if (userItem.isModel == 1) {
            modelTv.setVisibility(View.VISIBLE);
        } else {
            modelTv.setVisibility(View.GONE);
        }
        //达人
        if (userItem.isDoyen == 1) {
            masterTv.setVisibility(View.VISIBLE);
        } else {
            masterTv.setVisibility(View.GONE);
        }
        //车主 isMaster
        if (userItem.isMaster == 1) {
            brandIv.setVisibility(View.VISIBLE);
            brandIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(userItem.logo, 144, 144)));
        } else {
            brandIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSeeCar(ArrayList<RecordSeeCarModel.ListItem> list) {
        if (page == 1) {
            mSeeCarList.clear();
        }
        mSeeCarList.addAll(list);
        if (null != mSeeCarAdapter) {
            mListView.setAdapter(mSeeCarAdapter);
            mSeeCarAdapter.notifyDataSetChanged();
        } else {
            initAdapter();
            mListView.setAdapter(mSeeCarAdapter);
            mSeeCarAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showSeeCarHead(RecordSeeCarModel.UserItem userItem) {
        headerImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(userItem.avatar, 144, 144)));
        nameTv.setText(userItem.nickname);
        //性别
        if (userItem.sex == 1) {
            denderIv.setImageResource(R.drawable.icon_men);
        } else {
            denderIv.setImageResource(R.drawable.icon_women);
        }
        //年龄
        ageTv.setText(userItem.age + "");
//        Date date = new Date(userItem.birthday);
//        try {
//            ageTv.setText(getAge(date));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //模特
        if (userItem.isModel == 1) {
            modelTv.setVisibility(View.VISIBLE);
        } else {
            modelTv.setVisibility(View.GONE);
        }
        //达人
        if (userItem.isDoyen == 1) {
            masterTv.setVisibility(View.VISIBLE);
        } else {
            masterTv.setVisibility(View.GONE);
        }
        //车主 isMaster
        if (userItem.isMaster == 1) {
            brandIv.setVisibility(View.VISIBLE);
            brandIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(userItem.logo, 144, 144)));
        } else {
            brandIv.setVisibility(View.GONE);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 根据出生年月求年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        return age;
    }
}
