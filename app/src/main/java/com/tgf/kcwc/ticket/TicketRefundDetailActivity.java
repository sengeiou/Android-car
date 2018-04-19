package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.tgf.kcwc.mvp.model.TicketRefundDetailModel;
import com.tgf.kcwc.mvp.presenter.TicketDataPresenter;
import com.tgf.kcwc.mvp.view.TicketDataView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/8/25
 * E-mail:fishloveqin@gmail.com
 * 退款详情
 */

public class TicketRefundDetailActivity extends BaseActivity implements TicketDataView<TicketRefundDetailModel> {

    protected SimpleDraweeView img;
    protected TextView senseName;
    protected ListView ticketList;
    protected ListView refundDescList;
    protected RelativeLayout refundDescLayout;
    protected ListView refundProgresslv;
    protected LinearLayout refundStateLayout;
    protected RelativeLayout rootView;
    protected ScrollView scrollView;
    private TicketDataPresenter mPresenter;
    private int mId = -1;
    private List<DataItem> mTicketDescDatas = null;
    private WrapAdapter<DataItem> mTicketDescAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        super.setContentView(R.layout.activity_ticket_refund_detail);

    }

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new TicketDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getTicketRefundDetail(mId + "", IOUtils.getToken(mContext));
    }


    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("退款详情");
    }


    @Override
    public void showData(TicketRefundDetailModel model) {

        showHeaderInfos(model);
        showTicketInfos(model);
        showTicketDesc(model);
        showRefundStateInfos(model);
    }

    private void showTicketDesc(final TicketRefundDetailModel model) {

        mTicketDescDatas = new ArrayList<DataItem>();

        String[] arrays = mRes.getStringArray(R.array.ticket_refund_desc);
        for (String str : arrays) {
            DataItem item = new DataItem();
            item.title = str;
            mTicketDescDatas.add(item);
        }


        mTicketDescDatas.get(0).content = "￥" + model.details.refundPrice;
        String accountText = "";
        if (model.details.refundType == 1) {
            accountText = "原路退回";
        } else if (model.details.refundType == 2) {
            accountText = "看车玩车零钱包";
        } else {
            accountText = "其他";
        }

        if (model.details.status == 2) {
            mTicketDescDatas.get(0).title = "成功退款:";
        }
        mTicketDescDatas.get(1).content = accountText;
        mTicketDescDatas.get(2).content = model.details.refundTime;

        WrapAdapter<DataItem> adapter = new WrapAdapter<DataItem>(mContext, R.layout.ticket_refund_desc_item, mTicketDescDatas) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                TextView contentTv = helper.getView(R.id.content);
                ImageView imgView = helper.getView(R.id.refundStateImg);
                if (helper.getPosition() == 0) {
                    if (model.details.status == 2) {
                        imgView.setVisibility(View.VISIBLE);
                    }
                } else {
                    imgView.setVisibility(View.GONE);
                }
                titleTv.setText(item.title);
                contentTv.setText(item.content);
            }
        };
        refundDescList.setAdapter(adapter);
        ViewUtil.setListViewHeightBasedOnChildren(refundDescList);
    }

    private void showRefundStateInfos(TicketRefundDetailModel model) {

        List<TicketRefundDetailModel.FollowBean> follows = model.follows;
        refundProgresslv.setAdapter(new WrapAdapter<TicketRefundDetailModel.FollowBean>(getContext(), follows, R.layout.time_line_list_item) {
            @Override
            public void convert(ViewHolder helper, TicketRefundDetailModel.FollowBean item) {
                helper.setText(R.id.refond_progressTimeTv, item.time);

                TextView topLineTv = helper.getView(R.id.refund_tvTopLine);
                TextView contentTv = helper.getView(R.id.refond_progressActionTv);
                contentTv.setText(item.content);
                if (helper.getPosition() == 0) {
                    helper.setImageResource(R.id.refond_tvDot, R.drawable.icon_green);
                    contentTv.setTextColor(mRes.getColor(R.color.text_color3));
                    topLineTv.setVisibility(View.INVISIBLE);
                } else {
                    helper.setImageResource(R.id.refond_tvDot, R.drawable.icon_gray);
                    contentTv.setTextColor(mRes.getColor(R.color.text_color15));
                    topLineTv.setVisibility(View.VISIBLE);
                }
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(refundProgresslv);
    }

    private void showTicketInfos(TicketRefundDetailModel model) {

        List<DataItem> items = new ArrayList<DataItem>();

        DataItem data = new DataItem();
        data.name = model.details.ticketName;
        data.tCode = model.details.serialSN;
        data.refundPrice = "(退款金额: ￥" + model.details.refundPrice + ")";
        String refundState = "";
        if (model.details.status == 1) {
            refundState = "退款中";
            data.textColorValue = mRes.getColor(R.color.text_color16);
        } else {
            refundState = "已退款";
            data.textColorValue = mRes.getColor(R.color.text_selected);
        }
        data.refundState = refundState;

        items.add(data);
        WrapAdapter<DataItem> adapter = new WrapAdapter<DataItem>(mContext, R.layout.ticket_refund_detail_list_item, items) {

            protected TextView refundState;
            protected TextView priceTv;
            protected TextView ticketId;
            protected TextView ticketName;


            @Override
            public void convert(ViewHolder helper, DataItem item) {
                ticketName = helper.getView(R.id.ticketName);
                ticketId = helper.getView(R.id.ticketId);
                priceTv = helper.getView(R.id.priceTv);
                refundState = helper.getView(R.id.refundState);
                ticketName.setText(item.name);
                ticketId.setText(item.tCode);
                priceTv.setText(item.refundPrice);
                refundState.setText(item.refundState);
                refundState.setTextColor(item.textColorValue);
            }
        };

        ticketList.setAdapter(adapter);
        ViewUtil.setListViewHeightBasedOnChildren(ticketList);
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

    private void showHeaderInfos(TicketRefundDetailModel model) {


        img.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.details.cover, 270, 203)));
        senseName.setText(model.details.eventName);

    }

    private void initView() {
        img = (SimpleDraweeView) findViewById(R.id.img);
        senseName = (TextView) findViewById(R.id.senseName);
        ticketList = (ListView) findViewById(R.id.ticketList);
        refundDescList = (ListView) findViewById(R.id.refundDescList);
        refundDescLayout = (RelativeLayout) findViewById(R.id.refundDescLayout);
        refundProgresslv = (ListView) findViewById(R.id.refund_progresslv);
        refundStateLayout = (LinearLayout) findViewById(R.id.refundStateLayout);
        rootView = (RelativeLayout) findViewById(R.id.rootView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }
}
