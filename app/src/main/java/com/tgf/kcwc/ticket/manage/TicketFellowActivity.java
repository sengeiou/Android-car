package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.SelectExhibitionModel;
import com.tgf.kcwc.mvp.model.TicketFellowModel;
import com.tgf.kcwc.mvp.presenter.TicketFellowPresenter;
import com.tgf.kcwc.mvp.view.TicketFellowView;
import com.tgf.kcwc.ticket.widget.SendObjDialog;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;


/**
 * Auther: Scott
 * Date: 2017/2/9 0009
 * E-mail:hekescott@qq.com
 */

public class TicketFellowActivity extends BaseActivity implements TicketFellowView {
    private static final String TAG = "TicketFellowActivity";
    private final String KEY_INTENT_TICKETID = "ticketid";
    private final String KEY_INTENT_MOBILE = "mobile";
    private final String KEY_INTENT_USERTYPE = "user_type";
    private ListView lv;
    private TicketFellowModel.FellowUser mFellowUser;
    private SendObjDialog mSendObjDialog;
    private int tfid;
    private int userType;
    private TicketFellowPresenter fellowPresenter;
    private List<TicketFellowModel.FellowItem> mFellowContents = new ArrayList<TicketFellowModel.FellowItem>();
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("门票跟踪");
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        Intent fromintent = getIntent();
        tfid = fromintent.getIntExtra(KEY_INTENT_TICKETID, 1);
        String mobile = fromintent.getStringExtra(KEY_INTENT_MOBILE);
        userType = fromintent.getIntExtra(KEY_INTENT_USERTYPE, 1);
        fellowPresenter = new TicketFellowPresenter();
        fellowPresenter.attachView(this);
        fellowPresenter.getTicketFellow(IOUtils.getToken(getContext()), tfid, mobile, userType);
        lv = (ListView) findViewById(R.id.ticketfellow_lv);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketfellow);
        findViewById(R.id.orderfellow_phoneCallbtn).setOnClickListener(this);
        findViewById(R.id.orderfellow_sendmsgbtn).setOnClickListener(this);
        findViewById(R.id.orderfellow_sendTickbtn).setOnClickListener(this);
        mSendObjDialog = new SendObjDialog(getContext());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
             switch (view.getId()){
                         case R.id.orderfellow_phoneCallbtn:
                             SystemInvoker.launchDailPage(getContext(),mFellowUser.mobile);
                             break;
                         case R.id.orderfellow_sendmsgbtn:
                             Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+mFellowUser.mobile));
                             startActivity(intent);
                             break;
                         case R.id.orderfellow_sendTickbtn:

                             mSendObjDialog.setTitle("补发");
                             mSendObjDialog.setMessage(mFellowUser.nickname);
                             mSendObjDialog.setNoOnclickListener("取消", new SendObjDialog.onNoOnclickListener() {
                                 @Override
                                 public void onNoClick() {
                                     mSendObjDialog.dismiss();
                                 }
                             });
                             mSendObjDialog.setYesOnclickListener("确认", new SendObjDialog.onYesOnclickListener() {
                                 @Override
                                 public void onYesClick() {
                                     fellowPresenter.sendTicket(IOUtils.getToken(getContext()),tfid,mFellowUser.mobile,mFellowUser.nickname,mSendObjDialog.getNums(),userType);
                                     mSendObjDialog.dismiss();

                                 }
                             });
                             mSendObjDialog.show();
                             break;
                         default:
                             break;
                     }
    }

    @Override
    public void showTickeFellow(TicketFellowModel ticketFellowModel) {
        mFellowUser = ticketFellowModel.user;
        if(!(ticketFellowModel.nums_lose==0)){
          View footerLose =  View.inflate(getContext(), R.layout.listitem_ticket_fellowfooter,null);
          TextView expireNumTv = (TextView) footerLose.findViewById(R.id.sendfellow_num);
            expireNumTv.setText("有"+ticketFellowModel.nums_lose+"张票领取权限失效");
            lv.addFooterView(footerLose);
        }
//        if(ticketFellowModel.nums_expire==0){
            View footerExpire =  View.inflate(getContext(), R.layout.listitem_ticket_fellowfooter,null);
            TextView expireNumTv = (TextView) footerExpire.findViewById(R.id.sendfellow_num);
            expireNumTv.setText("有"+ticketFellowModel.nums_expire+"张票过期");
            lv.addFooterView(footerExpire);
//        }
        if(ticketFellowModel.fellowContents!=null&&ticketFellowModel.fellowContents.size()!=0){
            mFellowContents= ticketFellowModel.fellowContents.subList(0,ticketFellowModel.fellowContents.size()-1);
        }
        WrapAdapter ticketCommonAdapter = new WrapAdapter<TicketFellowModel.FellowItem>(
                getContext(), mFellowContents, R.layout.listitem_ticket_fellow) {
            @Override
            public void convert(ViewHolder helper, TicketFellowModel.FellowItem item) {
                ImageView sendIv = helper.getView(R.id.fellow_sendiv);
                ImageView recieveIv = helper.getView(R.id.fellow_recieveiv);
                LinearLayout contenLv = helper.getView(R.id.ticketfellow_contentlv);
                LinearLayout codeLv = helper.getView(R.id.ticketfellow_codelv);
                TextView titleTv = helper.getView(R.id.ticketfellow_titletv);
                TextView contentTv = helper.getView(R.id.fellow_contenttv);
                helper.setText(R.id.sendfellow_date,item.date);
                if ("reg".equals(item.type)||"receive".equals(item.type)) {
                    sendIv.setVisibility(View.INVISIBLE);
                    recieveIv.setVisibility(View.VISIBLE);
                    contenLv.setBackgroundResource(R.drawable.order_fellow_left);
                    titleTv.setTextColor(mRes.getColor(R.color.black));
                    contentTv.setTextColor(mRes.getColor(R.color.black));
                } else {
                    sendIv.setVisibility(View.VISIBLE);
                    recieveIv.setVisibility(View.INVISIBLE);
                    contenLv.setBackgroundResource(R.drawable.order_fellow_right);
                    titleTv.setTextColor(mRes.getColor(R.color.white));
                    contentTv.setTextColor(mRes.getColor(R.color.white));

                }
                titleTv.setText(item.title);
                contentTv.setText(item.content);
                if(!TextUtils.isEmpty(item.ticket)){
                    String ticketCode = item.ticket.replace(",","\n")+"\n";
                    helper.setText(R.id.ticket_codetv,ticketCode);
                    codeLv.setVisibility(View.VISIBLE);
                }else {
                    codeLv.setVisibility(View.GONE);
                }

            }
        };

        lv.setAdapter(ticketCommonAdapter);

    }

    @Override
    public void showSendTicketSuccess() {
        CommonUtils.showToast(getContext(),"补发成功");
    }

    @Override
    public void faildeMessage(String statusMessage) {
        CommonUtils.showToast(getContext(),statusMessage);
    }

//    private void showSend(WrapAdapter.ViewHolder helper, TicketFellowModel.FellowItem item) {
//
//
//    }
//
//    private void showRecieve(WrapAdapter.ViewHolder helper, TicketFellowModel.FellowItem item) {
//
//    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }
}
