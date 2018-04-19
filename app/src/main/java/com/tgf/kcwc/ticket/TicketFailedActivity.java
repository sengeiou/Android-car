package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.CheckSendSeeModel;
import com.tgf.kcwc.mvp.model.HeadEvent;
import com.tgf.kcwc.mvp.presenter.SendSeePresenter;
import com.tgf.kcwc.mvp.view.TicketSendSeeView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/27 0027
 * E-mail:hekescott@qq.com
 */

public class TicketFailedActivity extends BaseActivity implements TicketSendSeeView {

    private SendSeePresenter sendSeePresenter;
    private LinearLayout failedLayout;
    private String nums;
    private TextView failedMsgTv;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("领取失败");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketgetf);
        failedMsgTv = (TextView) findViewById(R.id.ticketget_failedmsgtv);
        sendSeePresenter = new SendSeePresenter();
        sendSeePresenter.attachView(this);
        Intent fromIntent = getIntent();
        String ticketId = fromIntent.getStringExtra(Constants.IntentParams.ID);
        nums = fromIntent.getStringExtra(Constants.IntentParams.DATA);
        String sign = fromIntent.getStringExtra(Constants.IntentParams.DATA2);
        failedLayout = (LinearLayout) findViewById(R.id.ticketget_failedlayout);
        Account account = IOUtils.getAccount(getContext());
        int ticketID = 0;
        if (!TextUtil.isEmpty(ticketId)) {
            ticketID = Integer.parseInt(ticketId);
        }
        sendSeePresenter.sendSignTicket(IOUtils.getToken(getContext()), ticketID, account.userInfo.mobile, sign, nums);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showSendSeehead(HeadEvent event) {

    }

    @Override
    public void showSendTicketSuccess() {
        CommonUtils.showToast(getContext(),"成功领取"+nums+"张赠票！");

        Intent toTicketIntent = new Intent(mContext, TicketActivity.class);
        toTicketIntent.putExtra(Constants.IntentParams.INDEX, 0);
        startActivity(toTicketIntent);
        finish();

    }

    @Override
    public void showCheckTicket(ArrayList<CheckSendSeeModel> checkSendSeeModelArrayList) {

    }

    @Override
    public void failedMessage(String statusMessage) {
        failedLayout.setVisibility(View.VISIBLE);
        failedMsgTv.setText("失败原因："+statusMessage);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendSeePresenter.detachView();
    }
}
