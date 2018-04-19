package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.mvp.model.TicketmExhibitModel;
import com.tgf.kcwc.mvp.model.TicketmListModel;
import com.tgf.kcwc.mvp.presenter.TicketManagerListPresenter;
import com.tgf.kcwc.mvp.view.TicketManagerListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropUpSelecExhibitPopupWindow;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 * 票务管理列表
 */

public class TickerManagerListActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, TicketManagerListView {
    private RadioGroup mTicketmFilterRG;
    private RadioButton mTicketmFilterWorkerBtn;
    private RadioButton mTicketmFilterWorksuBtn;
    private RadioButton mTicketmFilterCustomerBtn;
    private TicketManagerListPresenter mManagerListPresenter;
    private String token;

    private TextView mTicketmanageListTickeKindtv;
    private SimpleDraweeView mTicketmExhibitcoveriv;
    private TextView mTicketmExhibitTitleTv;
    private TextView mTicketmExhibitExpiredIv;
    private TextView mTicketmTotalTicketTv;
    private TextView mTicketmLeftTicketTv;
    private DropUpSelecExhibitPopupWindow mDropUpSelecExhibitPopupWindow;
    private String eventId;
    private NestFullListView handlistView;
    private TextView isExpireTv;

    @Override
    protected void setUpViews() {
        mTicketmFilterRG = (RadioGroup) findViewById(R.id.ticketm_filterRG);
        mTicketmFilterWorkerBtn = (RadioButton) findViewById(R.id.ticketm_filter_workerBtn);
        mTicketmFilterWorksuBtn = (RadioButton) findViewById(R.id.ticketm_filter_worksuBtn);
        mTicketmFilterCustomerBtn = (RadioButton) findViewById(R.id.ticketm_filter_customerBtn);
        mTicketmFilterRG.setOnCheckedChangeListener(this);
        token = IOUtils.getToken(getContext());
        mManagerListPresenter = new TicketManagerListPresenter();
        mManagerListPresenter.attachView(this);
        mManagerListPresenter.getTicketmExhibitList(token);
        mManagerListPresenter.getTicketmList(token, null);
        mTicketmanageListTickeKindtv = (TextView) findViewById(R.id.ticketmanage_list_tickeKindtv);
        mTicketmExhibitcoveriv = (SimpleDraweeView) findViewById(R.id.ticketm_exhibitcoveriv);
        mTicketmExhibitTitleTv = (TextView) findViewById(R.id.ticketm_exhibitTitleTv);
        mTicketmExhibitExpiredIv = (TextView) findViewById(R.id.ticketm_exhibitExpiredIv);
        mTicketmTotalTicketTv = (TextView) findViewById(R.id.ticketm_totalTicketTv);
        mTicketmLeftTicketTv = (TextView) findViewById(R.id.ticketm_leftTicketTv);
        isExpireTv = (TextView) findViewById(R.id.ticketm_exhibitExpiredIv);
        findViewById(R.id.ticketm_list_seemoreIv).setOnClickListener(this);
        findViewById(R.id.ticketm_list_tongjiIv).setOnClickListener(this);
        findViewById(R.id.ticketmlist_gotoTongjiTV).setOnClickListener(this);
        handlistView = (NestFullListView) findViewById(R.id.ticketm_handlistlv);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("票务管理");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_ticketlist);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.ticketm_filter_workerBtn:
                mTicketmFilterWorkerBtn.setChecked(true);
                break;
            case R.id.ticketm_filter_worksuBtn:
                mTicketmFilterWorksuBtn.setChecked(true);
                break;
            case R.id.ticketm_filter_customerBtn:
                mTicketmFilterCustomerBtn.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void showExhibitList(ArrayList<TicketmExhibitModel> ticketmExhibitModelList) {
        mDropUpSelecExhibitPopupWindow = new DropUpSelecExhibitPopupWindow(getContext(), ticketmExhibitModelList);
    }

    @Override
    public void showHandTicketList(final ArrayList<TicketmListModel.HandleTicket> handleList) {
        handlistView.setAdapter(new NestFullListViewAdapter<TicketmListModel.HandleTicket>(R.layout.listitem_ticketm_kind, handleList) {
            @Override
            public void onBind(int pos, TicketmListModel.HandleTicket handleTicket, NestFullViewHolder holder) {
                TextView handleTicketTv = holder.getView(R.id.handleticket_nametv);
                GradientDrawable drawable = (GradientDrawable) handleTicketTv.getBackground();
                handleTicketTv.setText(handleTicket.name);
                drawable.setColor(Color.parseColor(handleTicket.color));
                String color2 = handleTicket.color.replace("#", "#7F");
               TextView ticketmPrice = holder.getView(R.id.ticketm_price);
                ticketmPrice.setText(SpannableUtil.getSpanMoneyString(getContext(),"￥"+handleTicket.price,12));
                ticketmPrice.setTextColor(Color.parseColor(handleTicket.color));
                LinearLayout remarkLayout = holder.getView(R.id.handleticket_remarkLayout);
                remarkLayout.setBackgroundColor(Color.parseColor(color2));
                holder.setText(R.id.remarks, handleTicket.remarks);
                NestFullListView handleOrgLv = holder.getView(R.id.handleorg_lv);
                handleOrgLv.setAdapter(new NestFullListViewAdapter<TicketmListModel.HandleTicketOrg>(R.layout.listitem_ticketm_sendp, handleTicket.ticketList) {
                    @Override
                    public void onBind(int pos, final TicketmListModel.HandleTicketOrg handleTicketOrg, NestFullViewHolder holder) {
                        holder.setText(R.id.handleorg_nametv, handleTicketOrg.orgName);
                        holder.getView(R.id.handleorg_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toIntent = new Intent(getContext(),TicketOrgManageDetailActivity.class);
                                toIntent.putExtra(Constants.IntentParams.ID,handleTicketOrg.id);
                                startActivity(toIntent);
                            }
                        });
                    }
                });

            }
        });
    }


    @Override
    public void showHead(TicketmExhibitModel eventInfo, TicketmListModel.Num nums) {
        mTicketmExhibitcoveriv.setImageURI(URLUtil.builderImgUrl(eventInfo.cover, 540, 304));
        mTicketmExhibitTitleTv.setText(eventInfo.name);
        if(eventInfo.isExpire==1){
            isExpireTv.setVisibility(View.VISIBLE);
        }else {
            isExpireTv.setVisibility(View.INVISIBLE);
        }
        if (nums.all == -1) {
            mTicketmTotalTicketTv.setText(Html.fromHtml("<font color=\"#999999\">总量 </font>不限"));
        } else {
            mTicketmTotalTicketTv.setText(Html.fromHtml("<font color=\"#999999\">总量 </font>" + nums.all));
        }
        if (nums.have == -1) {
            mTicketmTotalTicketTv.setText(Html.fromHtml("<font color=\"#999999\">剩余 </font>不限"));
        } else {
            mTicketmLeftTicketTv.setText(Html.fromHtml("<font color=\"#999999\">剩余 </font>" + nums.have));
        }

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ticketm_list_seemoreIv:
                if (mDropUpSelecExhibitPopupWindow != null) {
                    mDropUpSelecExhibitPopupWindow.show(TickerManagerListActivity.this);
                    mDropUpSelecExhibitPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            TicketmExhibitModel mTicketModel = mDropUpSelecExhibitPopupWindow.getSelectDataItem();
                            if (mTicketModel != null) {
                                if (eventId == null || (!eventId.equals(mTicketModel.id + ""))) {
                                    eventId = mTicketModel.id + "";
                                    mManagerListPresenter.getTicketmList(token, eventId);
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.ticketm_list_tongjiIv:
            case R.id.ticketmlist_gotoTongjiTV:
                Intent toTongjiAct =new Intent(getContext(), TicketmOrgTongjActivity.class);
                toTongjiAct.putExtra(Constants.IntentParams.ID,eventId);
                startActivity(toTongjiAct);
                break;
            default:
                break;
        }
    }
}
