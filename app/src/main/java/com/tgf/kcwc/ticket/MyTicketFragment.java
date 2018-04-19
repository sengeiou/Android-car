package com.tgf.kcwc.ticket;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.MyTicketListModel;
import com.tgf.kcwc.mvp.model.Ticket;
import com.tgf.kcwc.mvp.model.TicketItem;
import com.tgf.kcwc.mvp.presenter.MyTicketListPresenter;
import com.tgf.kcwc.mvp.view.MyTicketListView;
import com.tgf.kcwc.see.exhibition.ExhibitionListActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DBCacheUtil;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.QRCodeUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.SpecRectView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/29 10:06
 * E-mail：fishloveqin@gmail.com
 * 我的
 */

public class MyTicketFragment extends BaseFragment implements MyTicketListView {

    protected View rootView;
    protected Button mBuyTicketBtn1;
    protected TextView mRecordBtn1;
    protected RelativeLayout mBottomLayout;
    protected RelativeLayout mRootLayout;
    protected Button mBuyTicketBtn2;
    protected TextView mRecordBtn2;
    protected RelativeLayout mEmptyLayout;
    private List<Ticket> tickets = new ArrayList<Ticket>();
    private ListView mList;

    private MyTicketListPresenter mPresenter;

    @Override
    protected void updateData() {
        mPresenter.loadMyTickets("", "", "", "", "0", IOUtils.getToken(mContext));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_ticket;
    }

    @Override
    protected void initView() {
        mList = findView(R.id.list);

        mPresenter = new MyTicketListPresenter();
        mPresenter.attachView(this);
        mBuyTicketBtn1 = findView(R.id.buyTicketBtn1);
        mRecordBtn1 = findView(R.id.recordBtn1);
        mBottomLayout = findView(R.id.bottomLayout);
        mRootLayout = findView(R.id.rootLayout);
        mBuyTicketBtn2 = findView(R.id.buyTicketBtn2);
        mRecordBtn2 = findView(R.id.recordBtn2);
        mEmptyLayout = findView(R.id.emptyLayout);
        mRecordBtn1.setOnClickListener(this);
        mRecordBtn2.setOnClickListener(this);
        mBuyTicketBtn1.setOnClickListener(this);
        mBuyTicketBtn2.setOnClickListener(this);
    }

    private void showQR(final String qrText, final String gifUrl, final MyTicketListModel.MyTicket item, final int position,String qrText2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_qr_dialog, null);
        builder.setView(v);
        TextView textView = (TextView) v.findViewById(R.id.ticket);
        textView.setText(qrText2);
        TextView forwardTv = (TextView) v.findViewById(R.id.forwardTv);
        RelativeLayout forwardLayout = (RelativeLayout) v.findViewById(R.id.forwardLayout);
        final AlertDialog alertDialog = builder.create();
        if (item.type == 1) {
            forwardLayout.setVisibility(View.VISIBLE);
        } else {
            forwardLayout.setVisibility(View.GONE);
        }
        forwardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.DATA, item);
                intent.putExtra(Constants.IntentParams.DATA2, position);
                intent.setClass(mContext, TicketForwardActivity.class);
                startActivity(intent);

                alertDialog.dismiss();
            }
        });

        ImageView img = (ImageView) v.findViewById(R.id.img);
        final ImageView qrImg = (ImageView) v.findViewById(R.id.qrImg);

        showqrBaseInfo(v, item);
        if (DBCacheUtil.getBitmapFromMemCache(qrText) == null) {

            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    qrImg.setImageBitmap(DBCacheUtil.getBitmapFromMemCache(qrText));
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBCacheUtil.addBitmapToMemoryCache(qrText,
                            QRCodeUtils.createQRCode(qrText, BitmapUtils.dp2px(mContext, 400)));
                    handler.sendEmptyMessage(0);
                }
            }).start();

        }
        qrImg.setImageBitmap(DBCacheUtil.getBitmapFromMemCache(qrText));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                //再次刷新数据
                mPresenter.loadMyTickets("", "", "", "", "0", IOUtils.getToken(mContext));
            }
        });

        alertDialog.show();
        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 350),
                BitmapUtils.dp2px(mContext, 450));
    }

    @Override
    public void showMyTickets(final MyTicketListModel model) {
        int size = model.tickets.size();
        if (size == 0) {
            mRootLayout.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mRootLayout.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }

        mList.setAdapter(new WrapAdapter<MyTicketListModel.MyTicket>(mContext, model.tickets,
                R.layout.my_ticket_list_item) {
            @Override
            public void convert(ViewHolder helper, final MyTicketListModel.MyTicket item) {

                String typeStr = "";
                final int type = item.type;
                if (type == 1) {
                    typeStr = "门票";
                } else if (type == 2) {
                    typeStr = "赠送";
                } else {
                    typeStr = "申领";
                }
                View orderTitleLayout = helper.getView(R.id.orderTitleLayout);
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

                final MyTicketListModel.MyTicket myTicket = item;
                TextView orderId = helper.getView(R.id.orderId);
                if (item.orderInfo != null && !TextUtil.isEmpty(item.orderInfo.orderSN)) {
                    orderId.setText(item.orderInfo.orderSN);
                    orderTitleLayout.setVisibility(View.VISIBLE);
                } else {
                    orderTitleLayout.setVisibility(View.GONE);
                }

                final SpecRectView specRectView = helper.getView(R.id.specRectView);
                String color = item.ticketInfo.color;

                if (!TextUtils.isEmpty(color)) {
                    specRectView.setBGColor(color);
                }

                helper.setText(R.id.title, item.senseInfo.name + "");


                helper.setText(R.id.type2, typeStr);
                helper.setText(R.id.type, item.ticketInfo.name + "");
                helper.setText(R.id.expire,
                        item.ticketInfo.remarks+"");
                TextView priceStr = helper.getView(R.id.price);

                priceStr.setText(item.ticketInfo.price + "");

                if (item.type == 1) {
                    priceStr.getPaint().setFlags(Paint.SUBPIXEL_TEXT_FLAG);
                } else {
                    priceStr.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                ListView list = helper.getView(R.id.list);
                final int size = item.ticketItems.size();

                list.setAdapter(
                        new WrapAdapter<TicketItem>(mContext, item.ticketItems, R.layout.ticket_item) {
                            @Override
                            public void convert(final ViewHolder helper, final TicketItem item) {

                                int status = item.status;
                                switch (status) {

                                }

                                View splitView = helper.getView(R.id.split);

                                if (helper.getPosition() == size - 1) {
                                    splitView.setVisibility(View.GONE);
                                } else {
                                    splitView.setVisibility(View.VISIBLE);
                                }
                                helper.setText(R.id.ticketId, "No." + item.serialSN);

                                ImageView smallQR = helper.getView(R.id.smallQR);
                                smallQR.setVisibility(View.VISIBLE);

                                smallQR
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                showQR(item.ticketSN + "", model.gif, myTicket, helper.getPosition(),item.serialSN);
                                            }
                                        });

                            }
                        });
                int height = ViewUtil.setListViewHeightBasedOnChildren2(list);
                SpecRectView view1 = helper.getView(R.id.specRectView1);
                view1.setHeight(height);


            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(mList);

    }


    private void showqrBaseInfo(View rootView, MyTicketListModel.MyTicket item) {


        View baseInfoView = rootView.findViewById(R.id.ticketBaseInfo);
        baseInfoView.setBackgroundColor(Color.parseColor(item.ticketInfo.color));
        TextView textTitle = (TextView) rootView.findViewById(R.id.title);
        textTitle.setText(item.senseInfo.name + "");

        String typeStr = "";
        int type = item.type;
        if (type == 1) {
            typeStr = "门票";
        } else if (type == 2) {
            typeStr = "赠送";
        } else {
            typeStr = "申领";
        }
        TextView typeTv2 = (TextView) rootView.findViewById(R.id.type2);
        typeTv2.setText(typeStr);
        TextView typeTv = (TextView) rootView.findViewById(R.id.type);
        typeTv.setText(item.ticketInfo.name + "");

        TextView expireTv = (TextView) rootView.findViewById(R.id.expire);
        expireTv.setText(
                DateFormatUtil.dispActiveTime2(item.ticketInfo.beginTime, item.ticketInfo.endTime));

        TextView priceStr = (TextView) rootView.findViewById(R.id.price);

        priceStr.setText(item.ticketInfo.price + "");

        if (item.type == 1) {
            priceStr.getPaint().setFlags(Paint.SUBPIXEL_TEXT_FLAG);
        } else {
            priceStr.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        TextView remarkTv = (TextView) rootView.findViewById(R.id.remarkTv);
        remarkTv.setText(item.ticketInfo.remarks);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        if (active) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
        CommonUtils.showToast(mContext, "网络异常，稍候再试！");
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.recordBtn1:
            case R.id.recordBtn2:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mSenseId);
                CommonUtils.startNewActivity(mContext, args, UseRecordActivity.class);
                break;
            case R.id.buyTicketBtn1:
            case R.id.buyTicketBtn2:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mSenseId);
                CommonUtils.startNewActivity(mContext, args, ExhibitionListActivity.class);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

}
