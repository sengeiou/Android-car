package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.TicketManageDetailModel;
import com.tgf.kcwc.mvp.model.TicketOrgManageDetailModel;
import com.tgf.kcwc.mvp.presenter.TicketManageDetailPresenter;
import com.tgf.kcwc.mvp.presenter.TicketOrgManageDetailPresenter;
import com.tgf.kcwc.mvp.view.TicketManageDetailView;
import com.tgf.kcwc.mvp.view.TicketOrgManageDetailView;
import com.tgf.kcwc.ticket.ContactActivity;
import com.tgf.kcwc.ticket.MyFriendActivity;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 */

public class TicketOrgManageDetailActivity extends BaseActivity implements TicketOrgManageDetailView {


    private TicketOrgManageDetailPresenter ticketOrgManageDetailPresenter;
    private String token;
    private int id;
    private SimpleDraweeView mTicketmOrgdetailHeadpngIv;
    private TextView mManageorgdetailEventnameTv;
    private TextView mManageorgdetailPrice;
    private TextView mManageorgdetailTickettypeTv;
    private TextView mManageorgdetailTicketdateTv;
    private TextView mManageorgdetailFromTv;
    private TextView mManageorgdetailNumsTv;
    private TextView mManageorgdetailHtsalernumsTv;
    private TextView mManageorgdetailHtorgnumsTv;
    private TextView mManageorgdetailLeftnumTv;
    private TextView mManageorgdetailUsernumsTv;
    private TextView mManageorgdetailUserhavenumsTv;
    private TextView mManageorgdetailRecnumsTv;
    private TextView mManageorgdetailRecpnumsTv;
    private TextView mManageorgdetailUsenumsTv;
    private TextView mManageorgdetailUsepnumsTv;

    @Override
    protected void setUpViews() {
        token = IOUtils.getToken(getContext());
        id = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        ticketOrgManageDetailPresenter = new TicketOrgManageDetailPresenter();
        ticketOrgManageDetailPresenter.attachView(this);
        ticketOrgManageDetailPresenter.getTicketOrgManageDetail(token, id, 1);
        mTicketmOrgdetailHeadpngIv = (SimpleDraweeView) findViewById(R.id.ticketmOrgdetail_headpng_iv);
        mManageorgdetailEventnameTv = (TextView) findViewById(R.id.manageorgdetail_eventname_tv);
        mManageorgdetailPrice = (TextView) findViewById(R.id.manageorgdetail_price);
        mManageorgdetailTickettypeTv = (TextView) findViewById(R.id.manageorgdetail_tickettype_tv);
        mManageorgdetailTicketdateTv = (TextView) findViewById(R.id.manageorgdetail_ticketdate_tv);
        mManageorgdetailFromTv = (TextView) findViewById(R.id.manageorgdetail_from_tv);
        mManageorgdetailNumsTv = (TextView) findViewById(R.id.manageorgdetail_nums_tv);
        mManageorgdetailHtsalernumsTv = (TextView) findViewById(R.id.manageorgdetail_htsalernums_tv);
        mManageorgdetailHtorgnumsTv = (TextView) findViewById(R.id.manageorgdetail_htorgnums_tv);
        mManageorgdetailLeftnumTv = (TextView) findViewById(R.id.manageorgdetail_leftnum_tv);
        mManageorgdetailUsernumsTv = (TextView) findViewById(R.id.manageorgdetail_usernums_tv);
        mManageorgdetailUserhavenumsTv = (TextView) findViewById(R.id.manageorgdetail_userhavenums_tv);
        mManageorgdetailRecnumsTv = (TextView) findViewById(R.id.manageorgdetail_recnums_tv);
        mManageorgdetailRecpnumsTv = (TextView) findViewById(R.id.manageorgdetail_recpnums_tv);
        mManageorgdetailUsenumsTv = (TextView) findViewById(R.id.manageorgdetail_usenums_tv);
        mManageorgdetailUsepnumsTv = (TextView) findViewById(R.id.manageorgdetail_usepnums_tv);
        findViewById(R.id.manageorgdetail_send).setOnClickListener(this);
        findViewById(R.id.manageorgdetail_sendQueryTv).setOnClickListener(this);

    }


    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        findViewById(R.id.manageorgdetail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_orgmanagedetail);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showManageDetail(TicketOrgManageDetailModel.Details details) {
        //        mManageorgdetailTicketdateTv.setText(DateFormatUtil);//todo
        mTicketmOrgdetailHeadpngIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(details.event_cover, 540, 270)));
        mManageorgdetailEventnameTv.setText(details.event_name);

//        Logger.d("heke"+  String.format("%d", 123.00));
        mManageorgdetailPrice.setText("￥" + details.getPrice() + "元");
        mManageorgdetailTickettypeTv.setText(details.ticket_name);
        mManageorgdetailFromTv.setText("来源：" + details.source);
        mManageorgdetailNumsTv.setText(details.getNums());
        mManageorgdetailHtsalernumsTv.setText(details.ht_nums_saler + "");
        mManageorgdetailHtorgnumsTv.setText(details.ht_nums_org + "");
        mManageorgdetailLeftnumTv.setText(details.getHaveNums());
        mManageorgdetailUsernumsTv.setText(details.ht_user_nums_all + "");
        mManageorgdetailUserhavenumsTv.setText(details.getUserHaveNums());
        mManageorgdetailRecnumsTv.setText(details.receive_nums_all + "");
        mManageorgdetailRecpnumsTv.setText(details.receive_nums_all_person + "");
        mManageorgdetailUsenumsTv.setText(details.use_nums + "");
        mManageorgdetailUsepnumsTv.setText(details.use_nums_person + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.manageorgdetail_send:
                Intent toIntent = new Intent(getContext(), TicketOrgSendSeeActivity.class);
                toIntent.putExtra(Constants.IntentParams.ID,id);
                SharedPreferenceUtil.putTFId(getContext(),id);
                startActivity(toIntent);
                break;
            case R.id.manageorgdetail_sendQueryTv:
                Intent toOrgIntent = new Intent(getContext(), TicketOrgQueryActivity.class);
                toOrgIntent.putExtra(Constants.IntentParams.ID,id);
                startActivity(toOrgIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ticketOrgManageDetailPresenter.detachView();
        super.onDestroy();
    }
}
