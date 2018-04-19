package com.tgf.kcwc.ticket;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.MyTicketListModel;
import com.tgf.kcwc.mvp.model.TicketItem;
import com.tgf.kcwc.mvp.presenter.MyTicketListPresenter;
import com.tgf.kcwc.mvp.view.MyTicketListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SpecRectView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/29 19:53
 * E-mail：fishloveqin@gmail.com
 * <p>
 * 使用记录
 */

public class UseRecordActivity extends BaseActivity implements MyTicketListView {

    private View footView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSenseId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        setContentView(R.layout.activity_ticket_use_record);
    }

    private int mSenseId;
    private MyTicketListPresenter mPresenter;

    private ScrollView rootView;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("使用/退款记录");
    }

    @Override
    protected void setUpViews() {

        mList = (ListView) findViewById(R.id.list);
        rootView = (ScrollView) findViewById(R.id.rootView);
        footView = LayoutInflater.from(mContext).inflate(R.layout.no_more_data_layout, mList, false);
        initEmptyView();
        mPresenter = new MyTicketListPresenter();
        mPresenter.attachView(this);

        mPresenter.loadMyTickets("", "", "", "", "1", IOUtils.getToken(mContext));

    }

    private ListView mList;

    @Override
    public void showMyTickets(MyTicketListModel model) {

        int size = model.tickets.size();
        if (size == 0) {

            rootView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            return;
        } else {
            rootView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }

        mList.setAdapter(new WrapAdapter<MyTicketListModel.MyTicket>(mContext, model.tickets,
                R.layout.my_ticket_list_item) {
            @Override
            public void convert(ViewHolder helper, final MyTicketListModel.MyTicket item) {


                View orderTitleLayout = helper.getView(R.id.orderTitleLayout);
                final int type = item.type;
                orderTitleLayout.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    protected void onSingleClick(View view) {

                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.orderInfo.orderId);

                        Class classz = BuyTicketOrderDetailActivity.class;

                        if (type == 1) {
                            classz = BuyTicketOrderDetailActivity.class;
                        } else {

                            classz = FreeTicketOrderDetailActivity.class;
                        }

                        CommonUtils.startNewActivity(mContext, args, classz);
                    }
                });

                TextView orderId = helper.getView(R.id.orderId);
                if (item.orderInfo != null && !TextUtil.isEmpty(item.orderInfo.orderSN)) {
                    orderId.setText(item.orderInfo.orderSN);
                    orderTitleLayout.setVisibility(View.VISIBLE);
                } else {
                    orderTitleLayout.setVisibility(View.GONE);
                }

                SpecRectView specRectView = helper.getView(R.id.specRectView);
                specRectView.setBGColor(mRes.getColor(R.color.text_content_color));
                helper.setText(R.id.title, item.senseInfo.name + "");
                helper.setText(R.id.type, item.ticketInfo.name + "");
                helper.setText(R.id.expire,
                        item.ticketInfo.remarks);

                TextView limitCountTv = helper.getView(R.id.limitCunt);
                //limitCountTv.setVisibility(View.GONE);
                TextView priceTv = helper.getView(R.id.price);
                priceTv.setText(item.ticketInfo.price + "");

                if (item.type == 1) {
                    priceTv.getPaint().setFlags(Paint.SUBPIXEL_TEXT_FLAG);
                } else {
                    priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                ListView list = helper.getView(R.id.list);
                final int size = item.ticketItems.size();
                list.setAdapter(
                        new WrapAdapter<TicketItem>(mContext, item.ticketItems, R.layout.ticket_item) {
                            @Override
                            public void convert(ViewHolder helper, final TicketItem item) {
                                View splitView = helper.getView(R.id.split);

                                if (helper.getPosition() == size - 1) {
                                    splitView.setVisibility(View.GONE);
                                } else {
                                    splitView.setVisibility(View.VISIBLE);
                                }

                                TextView tvSn = helper.getView(R.id.ticketId);
                                tvSn.setText("No. " + item.serialSN);
                                int status = item.status;
                                if (Constants.TicketStatus.EXPIRED == status || Constants.TicketStatus.REFUNDED == status) {
                                    tvSn.setPaintFlags(Paint.SUBPIXEL_TEXT_FLAG);
                                    tvSn.setTextColor(mRes.getColor(R.color.text_color17));
                                } else {
                                    tvSn.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                    tvSn.setTextColor(mRes.getColor(R.color.text_color3));
                                }

                                ImageView smallQR = helper.getView(R.id.smallQR);
                                smallQR.setVisibility(View.GONE);
                                TextView statusTv = helper.getView(R.id.statusTv);
                                statusTv.setVisibility(View.VISIBLE);


                                //statusTv.setText(item.checkoutTime + "核销");
                                ticketStatus(status, statusTv, item.checkoutTime, item.applyRefundTime, item.giveTime);
                            }
                        });
                int height = ViewUtil.setListViewHeightBasedOnChildren2(list);
                SpecRectView view1 = helper.getView(R.id.specRectView1);
                view1.setHeight(height);
            }
        });
        mList.addFooterView(footView);
        footView.setBackgroundColor(mRes.getColor(R.color.app_layout_bg_color));
        ViewUtil.setListViewHeightBasedOnChildren(mList);


    }

    private void ticketStatus(int status, TextView textView, String checkouttime, String applyRefundTime, String giveTime) {

        switch (status) {
            case Constants.TicketStatus.UN_USED:
                break;
            case Constants.TicketStatus.USED:
                textView.setText("已核销" + "\n" + checkouttime);
                break;
            case Constants.TicketStatus.EXP:
            case Constants.TicketStatus.EXPIRED:
                textView.setText("已过期");
                break;
            case Constants.TicketStatus.CANCEL:
                break;
            case Constants.TicketStatus.REFUNDING:
                textView.setText("退款中");
                break;
            case Constants.TicketStatus.REFUNDED:
                textView.setText("已退款\n" + applyRefundTime);
                break;
            case Constants.TicketStatus.FORWARDED:
                textView.setText("已转送\n" + giveTime);

            case Constants.TicketStatus.DISABLED:

                textView.setText("已禁用");
                break;
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        showLoadingIndicator(false);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
