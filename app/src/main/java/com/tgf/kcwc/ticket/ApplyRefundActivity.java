package com.tgf.kcwc.ticket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.OrderModel;
import com.tgf.kcwc.mvp.model.Ticket;
import com.tgf.kcwc.mvp.presenter.TicketDataPresenter;
import com.tgf.kcwc.mvp.view.TicketDataView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.link.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author:Jenny
 * Date:2017/8/23
 * E-mail:fishloveqin@gmail.com
 */

public class ApplyRefundActivity extends BaseActivity {
    protected SimpleDraweeView img;
    protected TextView senseName;
    protected RelativeLayout headerLayout;
    protected ListView ticketInfoList;
    protected RelativeLayout ticketInfoLayout;
    protected TextView titleTv1;
    protected TextView titleTv2;
    protected RelativeLayout refundTypeTitleLayout;
    protected ListView refundTypeList;
    protected RelativeLayout refundTypeLayout;
    protected TextView refundDescTitleTv1;
    protected TextView refundDescTitleTv2;
    protected RelativeLayout refundDescTitleLayout;
    protected ListView refundDescList;
    protected RelativeLayout refundDescLayout;
    protected Button buyTicketBtn2;
    protected RelativeLayout applyRefundBottomLayout;
    protected EditText refundBackNoteTv;
    protected TextView bockNoteTextSizeTv;
    protected Button applyBtn;
    private List<DataItem> mRefundDatas, mRefundWays;
    WrapAdapter<Ticket> adapter;
    private OrderModel mOrderModel;
    private WrapAdapter<DataItem> mRefundReasonsAdapter, mRefundWayAdapter;

    private TicketDataPresenter mPresenter;
    private TextView totalAccountTv;

    @Override
    protected void setUpViews() {
        mPresenter = new TicketDataPresenter();
        mPresenter.attachView(ticketDataView);
        initView();
        showHeaderInfos();
        showOrderInfos();
        showRefundReasons();
        showRefundWays();

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("申请退款");
    }


    private TicketDataView<Object> ticketDataView = new TicketDataView<Object>() {
        @Override
        public Context getContext() {
            return mContext;
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
        public void showData(Object o) {

            showRefundStyleDialog2();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mOrderModel = intent.getParcelableExtra(Constants.IntentParams.DATA);
        super.setContentView(R.layout.activity_apply_refund);

    }

    private ScrollView mScrollView;
    private boolean isShow;

    private Handler mHandler = new Handler();
    private boolean isFirst = true;

    private void initView() {
        mScrollView = (ScrollView) findViewById(R.id.scrollView);

        //final ExRelativeLayout rootLayout = (ExRelativeLayout) findViewById(R.id.rootView);
        refundBackNoteTv = (EditText) findViewById(R.id.refundBackNoteTv);


//        rootLayout.setSoftKeyboardListener(new ExRelativeLayout.OnSoftKeyboardListener() {
//            @Override
//            public void onSoftKeyboardChange() {
//                if (!isFirst) {
//                    isShow = isSoftKeyboardShow(rootLayout);
//
//                    if (isShow) {
//
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
//                            }
//                        }, 200);
//                    }
//                }
//                isFirst = false;
//            }
//        });

        totalAccountTv = (TextView) findViewById(R.id.totalAccountTv);
        img = (SimpleDraweeView) findViewById(R.id.img);
        senseName = (TextView) findViewById(R.id.senseName);
        headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);
        ticketInfoList = (ListView) findViewById(R.id.ticketInfoList);
        ticketInfoLayout = (RelativeLayout) findViewById(R.id.ticketInfoLayout);
        titleTv1 = (TextView) findViewById(R.id.titleTv1);
        titleTv2 = (TextView) findViewById(R.id.titleTv2);
        refundTypeTitleLayout = (RelativeLayout) findViewById(R.id.refundTypeTitleLayout);
        refundTypeList = (ListView) findViewById(R.id.refundTypeList);
        refundTypeLayout = (RelativeLayout) findViewById(R.id.refundTypeLayout);
        refundDescTitleTv1 = (TextView) findViewById(R.id.refundDescTitleTv1);
        refundDescTitleTv2 = (TextView) findViewById(R.id.refundDescTitleTv2);
        refundDescTitleLayout = (RelativeLayout) findViewById(R.id.refundDescTitleLayout);
        refundDescList = (ListView) findViewById(R.id.refundDescList);
        refundDescLayout = (RelativeLayout) findViewById(R.id.refundDescLayout);
        buyTicketBtn2 = (Button) findViewById(R.id.buyTicketBtn2);
        applyRefundBottomLayout = (RelativeLayout) findViewById(R.id.applyRefundBottomLayout);
        refundBackNoteTv.addTextChangedListener(mWatcher);

        bockNoteTextSizeTv = (TextView) findViewById(R.id.bockNoteTextSizeTv);
        applyBtn = (Button) findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(applyRefundListener);
    }

    private View.OnClickListener applyRefundListener = new OnSingleClickListener() {
        @Override
        protected void onSingleClick(View view) {

            validateRefundStatus();

        }
    };

    private boolean isSoftKeyboardShow(View rootView) {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int threshold = screenHeight / 3;
        int rootViewBottom = rootView.getBottom();
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        int visibleBottom = rect.bottom;
        int heightDiff = rootViewBottom - visibleBottom;
        //        System.out
        //            .println("----> rootViewBottom=" + rootViewBottom + ",visibleBottom=" + visibleBottom);
        //        System.out.println("----> heightDiff=" + heightDiff + ",threshold=" + threshold);
        return heightDiff > threshold;
    }


    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 200);
            bockNoteTextSizeTv.setText(s.length() + "/300");
        }
    };

    private void initWhyData() {
        mRefundDatas = new ArrayList<DataItem>();
        String whys[] = mRes.getStringArray(R.array.refond_why);
        for (int i = 0; i < whys.length; i++) {
            mRefundDatas.add(new DataItem(i, whys[i]));
        }
    }

    private void showRefundReasons() {
        initWhyData();
        final int size = mRefundDatas.size();
        mRefundReasonsAdapter = new WrapAdapter<DataItem>(this, mRefundDatas, R.layout.listitem_ticket_refund) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                setSplitLineVisible(helper, size);
                helper.setText(R.id.refundReasonTv, item.name);
                if (item.isSelected) {
                    helper.setImageResource(R.id.refund_statusIv, R.drawable.refound_s);
                } else {
                    helper.setImageResource(R.id.refund_statusIv, R.drawable.refound_n);
                }

            }
        };
        refundDescList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataItem item = (DataItem) parent.getAdapter().getItem(position);
                item.isSelected = !item.isSelected;
                mRefundReasonsAdapter.notifyDataSetChanged();
            }
        });
        refundDescList.setAdapter(mRefundReasonsAdapter);

        ViewUtil.setListViewHeightBasedOnChildren(refundDescList);


    }

    private void initWayData() {
        mRefundWays = new ArrayList<DataItem>();
        DataItem item = new DataItem();
        item.id = 1;
        item.title = "原路退回";
        item.content = "(1-7个工作日内退款到原支付方)";
        mRefundWays.add(item);
        item = new DataItem();
        item.id = 2;
        item.title = "我的零钱";
        item.content = "(退回到看车玩车零钱包)";
        mRefundWays.add(item);


    }

    private void showRefundWays() {
        initWayData();
        final int size = mRefundWays.size();
        mRefundWayAdapter = new WrapAdapter<DataItem>(this, mRefundWays, R.layout.listitem_ticket_refundway) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                setSplitLineVisible(helper, size);
                helper.setText(R.id.refund_wayTv, item.title);
                helper.setText(R.id.refund_wayDescTv, item.content);
                if (item.isSelected) {
                    helper.setImageResource(R.id.refund_statusIv, R.drawable.refound_s);
                } else {
                    helper.setImageResource(R.id.refund_statusIv, R.drawable.refound_n);
                }
            }
        };


        refundTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataItem item = (DataItem) parent.getAdapter().getItem(position);
                int selectId = item.id;
                item.isSelected = true;
                singleChecked(mRefundWays, item);
                mRefundWayAdapter.notifyDataSetChanged();
            }
        });
        refundTypeList.setAdapter(mRefundWayAdapter);

        ViewUtil.setListViewHeightBasedOnChildren(refundTypeList);


    }

    private void setSplitLineVisible(WrapAdapter.ViewHolder helper, int size) {
        View splitView = helper.getView(R.id.split);

        if (size == helper.getPosition() + 1) {
            splitView.setVisibility(View.GONE);
        } else {
            splitView.setVisibility(View.VISIBLE);
        }
    }


    private void showOrderInfos() {


        adapter = new WrapAdapter<Ticket>(mContext, R.layout.apply_refund_ticket_list_item, mOrderModel.tickets) {


            protected RelativeLayout orderInfoLayout;
            protected TextView partAccountTv;
            protected ListView list;
            protected RelativeLayout titleLayout;
            protected TextView price;
            protected TextView moneyTag;
            protected TextView unitTv;
            protected TextView ticketName;


            @Override
            public void convert(ViewHolder helper, Ticket item) {

                ticketName = helper.getView(R.id.ticketName);
                ticketName.setText(item.name);
                unitTv = helper.getView(R.id.unitTv);
                moneyTag = helper.getView(R.id.moneyTag);
                price = helper.getView(R.id.price);
                price.setText(item.price);
                titleLayout = helper.getView(R.id.titleLayout);
                list = helper.getView(R.id.list);
                partAccountTv = helper.getView(R.id.partAccountTv);

                double partPrice = calcRefundPrice(item.listCodes);

                //计算总退款额
                double totalPrice = 0;
                for (Ticket t : mOrderModel.tickets) {
                    totalPrice += calcRefundPrice(t.listCodes);
                }
                totalAccountTv.setText(totalPrice + "");
                partAccountTv.setText(partPrice + "");
                orderInfoLayout = helper.getView(R.id.orderInfoLayout);


                List<Ticket.Code> codes = new ArrayList<Ticket.Code>();
                for (Ticket.Code c : item.listCodes) {
                    if (c.status == Constants.TicketStatus.UN_USED) {
                        codes.add(c);
                    }
                }
                item.listCodes = codes;
                list.setAdapter(new WrapAdapter<Ticket.Code>(mContext, R.layout.ticket_refund_list_item, codes) {
                    @Override
                    public void convert(ViewHolder helper, Ticket.Code item) {
                        helper.setText(R.id.refund_numTv, "No. " + item.serialSN);
                        if (item.isSelected) {
                            helper.setImageResource(R.id.refund_statusIv, R.drawable.refound_s);
                        } else {
                            helper.setImageResource(R.id.refund_statusIv, R.drawable.refound_n);
                        }

                        TextView titleTv = helper.getView(R.id.title);
                        TextView contentTv = helper.getView(R.id.content);
                        if ("0".equals(item.priceDiscount) || "0.0".equals(item.priceDiscount)) {

                            titleTv.setVisibility(View.GONE);
                            contentTv.setVisibility(View.GONE);
                        } else {
                            titleTv.setVisibility(View.VISIBLE);
                            contentTv.setVisibility(View.VISIBLE);
                        }
                        titleTv.setText("抵扣: ");
                        contentTv.setText("-￥" + item.priceDiscount);
                    }
                });
                ViewUtil.setListViewHeightBasedOnChildren1(list);
                list.setOnItemClickListener(refundItemListener);
            }
        };
        ticketInfoList.setAdapter(adapter);

        ViewUtil.setListViewHeightBasedOnChildren1(ticketInfoList);

    }

    private AdapterView.OnItemClickListener refundItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Ticket.Code code = (Ticket.Code) parent.getAdapter().getItem(position);
            code.isSelected = !code.isSelected;

            adapter.notifyDataSetChanged();
        }
    };

    private double calcRefundPrice(List<Ticket.Code> codes) {

        double tempPrice = 0;
        for (Ticket.Code c : codes) {

            if (c.isSelected) {
                tempPrice += c.price;
            }

        }

        return tempPrice;
    }

    private void showHeaderInfos() {


        img.setImageURI(Uri.parse(URLUtil.builderImgUrl(mOrderModel.details.cover, 270, 203)));
        senseName.setText(mOrderModel.details.eventName);

        RelativeLayout headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);
        headerLayout.setFocusable(true);
        headerLayout.setFocusableInTouchMode(true);
        headerLayout.requestFocus();

    }


    private void showRefundStyleDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_apply_refund_dialog, null, false);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 350),
                BitmapUtils.dp2px(mContext, 247));
        alertDialog.show();
        TextView confirmTv = (TextView) v.findViewById(R.id.confirmTv);
        TextView cancelTv = (TextView) v.findViewById(R.id.cancelTv);
        ImageView closeImage = (ImageView) v.findViewById(R.id.img);
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();
                mPresenter.createRefund(builderReqParams(ids, reasonStrs, wayType));
                ids = "";
                wayType = -1;
                reasonStrs = "";
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
    }


    private void showRefundStyleDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_apply_refund_dialog2, null, false);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 350),
                BitmapUtils.dp2px(mContext, 247));
        alertDialog.show();
        TextView confirmTv = (TextView) v.findViewById(R.id.confirmTv);
        ImageView closeImage = (ImageView) v.findViewById(R.id.img);
        TextView msgTv = (TextView) v.findViewById(R.id.msgTv);

        ViewUtil.link("“我的门票-门票订单”", msgTv, new Link.OnClickListener() {
            @Override
            public void onClick(Object o, String clickedText) {


                finish();


            }
        }, mRes.getColor(R.color.text_color6), false);


        ViewUtil.link("“退款进度”", msgTv, new Link.OnClickListener() {
            @Override
            public void onClick(Object o, String clickedText) {

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                //  args.put(Constants.IntentParams.ID, code.id);
                CommonUtils.startNewActivity(mContext, args, TicketRefundDetailActivity.class);

            }
        }, mRes.getColor(R.color.text_color6), false);
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();
                finish();
            }
        });


        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                finish();
            }
        });
    }

    private void showHadRefundDialog() {


        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("退款")
                .setContentText("您的退款申请已提交，您可以在“我的门票-门票订单”\n中点击“退款进度”查看退款进度！")
                .setConfirmText("知道了").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finish();
            }
        })
                .show();

    }

    private String ids = "";
    private String reasonStrs = "";
    private int wayType = -1;

    /**
     * 验证退款状态，如是否选择退款方式，是否选反退款原因，是否选定要退款的门票
     */
    private void validateRefundStatus() {
        StringBuilder refundIds = new StringBuilder();


        for (Ticket t : mOrderModel.tickets) {
            for (Ticket.Code code : t.listCodes) {
                if (code.isSelected) {
                    refundIds.append(code.id).append(",");
                }
            }
        }

        ids = refundIds.toString();
        if (ids.length() > 0) {
            ids = ids.substring(0, ids.length() - 1);
        } else {
            CommonUtils.showToast(mContext, "请选择要退款的门票!");
            return;
        }


        wayType = -1;
        for (DataItem refundWay : mRefundWays) {
            if (refundWay.isSelected) {
                wayType = refundWay.id;
            }
        }
        if (wayType == -1) {
            CommonUtils.showToast(mContext, "请选择现金退还方式！");
            return;
        }
        StringBuilder reasonsBuilder = new StringBuilder();
        for (DataItem refundReason : mRefundDatas) {
            if (refundReason.isSelected) {
                reasonsBuilder.append(refundReason.name).append(",");
            }
        }
        reasonStrs = reasonsBuilder.toString();
        if (reasonStrs.length() > 0) {
            reasonStrs = reasonStrs.substring(0, reasonStrs.length() - 1);
        } else {
            CommonUtils.showToast(mContext, "请选择退款原因!");
            return;

        }

        String remark = refundBackNoteTv.getText().toString();
        if (!TextUtil.isEmpty(remark)) {
            reasonStrs = reasonStrs + "," + remark;
        }

        Logger.e("ids:" + ids + ",reasons:" + reasonStrs + ",type:" + wayType);
        showRefundStyleDialog1();
    }

    private Map<String, String> builderReqParams(String ids, String reasons, int type) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("tu_ids", ids);
        params.put("reason", reasons);
        params.put("apply_type", type + "");
        params.put("token", IOUtils.getToken(mContext));

        return params;
    }
}
