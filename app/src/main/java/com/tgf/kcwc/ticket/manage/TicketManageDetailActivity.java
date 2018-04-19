package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.content.Intent;
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
import com.tgf.kcwc.mvp.presenter.TicketManageDetailPresenter;
import com.tgf.kcwc.mvp.view.TicketManageDetailView;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.ticket.ContactActivity;
import com.tgf.kcwc.ticket.MyFriendActivity;
import com.tgf.kcwc.util.CommonUtils;
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

public class TicketManageDetailActivity extends BaseActivity implements TicketManageDetailView {
    private static final String TAG = "TicketManageDetailActivity";
    private final String KEY_INTENT_TYPE = "type";
    private final int VAULE_INTENT_TICKET = 1;
    private final String KEY_INTENT_TICKETID = "ticketid";
    private final String KEY_INTENT_MOBILE = "mobile";
    private TextView managedetailEventnameTv;
    private TextView managedetailTickettypeTv;
    private TextView managedetailTicketpriceTv;
    private TextView managedetailTicketdateTv;
    private TextView managedetailNumsTv;
    private TextView managedetailHtnumsTv;
    private TextView managedetailLeftnumTv;
    private TextView managedetailRecnumsTv;
    private TextView managedetailRecpnumsTv;
    private TextView managedetailUsenumsTv;
    private TextView managedetailUsepnumsTv;
    private SimpleDraweeView imageview;
    private TicketSendWindow ticketSendWindow;
    private int mTicketId;
    private View tinkmDetailLayout;
    private TextView managedetailSendTv;
    private TextView managedetailSaomaFtv;
    private TicketManageDetailPresenter tickeManagePresenter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {


    }

    @Override
    protected void setUpViews() {
        findViewById(R.id.managedetail_coupon_lv).setVisibility(View.GONE);
        findViewById(R.id.managedetail_copoun_fellow).setVisibility(View.GONE);
        findViewById(R.id.managedetail_goods_lv).setVisibility(View.GONE);
        imageview = (SimpleDraweeView) findViewById(R.id.ticketmangedetail_headpng_iv);
        managedetailEventnameTv = (TextView) findViewById(R.id.managedetail_eventname_tv);
        TextView managedetailText = (TextView) findViewById(R.id.managedetail_text);
        managedetailText.setText("票务管理");
        managedetailTickettypeTv = (TextView) findViewById(R.id.managedetail_tickettype_tv);
        managedetailSendTv = (TextView) findViewById(R.id.managedetail_sendTv);
        managedetailSendTv.setText("分发门票");
        managedetailSaomaFtv = (TextView) findViewById(R.id.managedetail_saomaFtv);
        managedetailSaomaFtv.setText("扫码发票");
        managedetailTicketpriceTv = (TextView) findViewById(R.id.managedetail_ticketprice_tv);
        managedetailTicketdateTv = (TextView) findViewById(R.id.managedetail_ticketdate_tv);
        managedetailNumsTv = (TextView) findViewById(R.id.managedetail_nums_tv);
        managedetailHtnumsTv = (TextView) findViewById(R.id.managedetail_htnums_tv);
        managedetailLeftnumTv = (TextView) findViewById(R.id.managedetail_leftnum_tv);
        managedetailRecnumsTv = (TextView) findViewById(R.id.managedetail_recnums_tv);
        managedetailRecpnumsTv = (TextView) findViewById(R.id.managedetail_recpnums_tv);
        managedetailUsenumsTv = (TextView) findViewById(R.id.managedetail_usenums_tv);
        managedetailUsepnumsTv = (TextView) findViewById(R.id.managedetail_usepnums_tv);
        tinkmDetailLayout = findViewById(R.id.tickem_detail_layout);
        findViewById(R.id.managedetail_query).setOnClickListener(this);
        findViewById(R.id.managedetail_send).setOnClickListener(this);
        findViewById(R.id.managedetail_back).setOnClickListener(this);
        findViewById(R.id.managedetail_btn).setOnClickListener(this);
        tickeManagePresenter = new TicketManageDetailPresenter();
        tickeManagePresenter.attachView(this);
        mTicketId = getIntent().getIntExtra(KEY_INTENT_TICKETID, 1);
        SharedPreferenceUtil.putTFId(getContext(), mTicketId);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tickeManagePresenter.getTicketManageDetail(IOUtils.getToken(getContext()), mTicketId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_managedetail);

    }

    @Override
    public void showTicketDetail(TicketManageDetailModel.Detail ticketManageDetailModel) {
        managedetailEventnameTv.setText(ticketManageDetailModel.eventName);
        managedetailTickettypeTv.setText("门票类型  " + ticketManageDetailModel.ticketName);


        managedetailTicketpriceTv.setText("票面价值  ￥" + ticketManageDetailModel.price + "元");
//        managedetailTicketdateTv.setText("门票有效期: "+DateFormatUtil.dispActiveTime2(ticketManageDetailModel.useTimeStart,ticketManageDetailModel.useTimeEnd));
        managedetailTicketdateTv.setText("门票有效期: " + ticketManageDetailModel.remarks);
        managedetailNumsTv.setText(ticketManageDetailModel.nums + "");
        managedetailHtnumsTv.setText(ticketManageDetailModel.htUserNums + "");
        managedetailLeftnumTv.setText((ticketManageDetailModel.nums - ticketManageDetailModel.htUserNums) + "");
        managedetailRecnumsTv.setText(ticketManageDetailModel.receiveNums + "");
        managedetailRecpnumsTv.setText(ticketManageDetailModel.receiveNumsPerson + "");
        managedetailUsenumsTv.setText(ticketManageDetailModel.useNums + "");
        managedetailUsepnumsTv.setText(ticketManageDetailModel.useNumsPerson + "");
        imageview.setImageURI(Uri.parse(URLUtil.builderImgUrl(ticketManageDetailModel.eventCover, 540, 270)));
        if (ticketManageDetailModel.status == 1) {

        } else {
            tinkmDetailLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void failedMessage(String localizedMessage) {

    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.managedetail_query:
                Intent toTicketCodeAct = new Intent(mContext, TicketSendCodeActivity.class);
                toTicketCodeAct.putExtra(Constants.IntentParams.ID, mTicketId);

                startActivity(toTicketCodeAct);

                break;
            case R.id.managedetail_send:
                if (ticketSendWindow == null) {
                    ticketSendWindow = new TicketSendWindow(this);
                    ticketSendWindow.setOnClickListener(mListener);
                }
                ticketSendWindow.show(this);
                break;
            case R.id.managedetail_back:
                finish();
                break;
            case R.id.managedetail_btn:
                Intent intent = new Intent(getContext(), TicketQueryActivity.class);
                intent.putExtra(KEY_INTENT_TICKETID, mTicketId);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private TicketSendWindow.OnClickListener mListener = new TicketSendWindow.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toIntent = new Intent();
            toIntent.putExtra(KEY_INTENT_TYPE, VAULE_INTENT_TICKET);
            toIntent = new Intent(getContext(), TicketSendSeeActivity.class);
            switch (v.getId()) {
                case R.id.ticket_contacts:
                    toIntent.setClass(getContext(), ContactActivity.class);
                    startActivityForResult(toIntent, Constants.IntentParams.REQUEST_CODE);
                    break;
                case R.id.ticket_user:
                    toIntent.setClass(getContext(), MyFriendActivity.class);
                    startActivityForResult(toIntent, Constants.IntentParams.REQUEST_CODE_2);
                    break;
                case R.id.ticket_input:
                    toIntent = new Intent(getContext(), TicketSendSeeActivity.class);
                    toIntent.putExtra(KEY_INTENT_MOBILE, 1);
                    startActivity(toIntent);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    private static final String KEY_INTENT_LISTCONTACT = "select_contact";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.IntentParams.REQUEST_CODE:
                case Constants.IntentParams.REQUEST_CODE_2:
                    Intent toCouponSendSee = new Intent(getContext(), TicketSendSeeActivity.class);
                    toCouponSendSee.putExtra(KEY_INTENT_LISTCONTACT, data.getSerializableExtra(KEY_INTENT_LISTCONTACT));
                    startActivity(toCouponSendSee);
                    break;
                default:
                    break;
            }
        }

    }
}
