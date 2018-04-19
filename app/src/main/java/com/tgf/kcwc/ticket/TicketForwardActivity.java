package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.MyTicketListModel;
import com.tgf.kcwc.mvp.presenter.TicketDataPresenter;
import com.tgf.kcwc.mvp.view.TicketDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SpecRectView;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/9/6
 * E-mail:fishloveqin@gmail.com
 * 票的转送
 */

public class TicketForwardActivity extends BaseActivity implements TicketDataView {


    private SpecRectView specRectView;
    private TextView title;
    private TextView type;
    private TextView type2;
    private TextView expire;
    private TextView moneyTag;
    private TextView price;
    private TextView limitCunt;
    private RelativeLayout topLayout;
    private TextView ticketId;
    private SpecRectView specRectView1;
    private RelativeLayout ticketBaseInfo;
    private EditText phoneNumET;
    private Button confirmShareBtn;
    private MyTicketListModel.MyTicket item;
    private int position;

    private TicketDataPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        item = intent.getParcelableExtra(Constants.IntentParams.DATA);
        position = intent.getIntExtra(Constants.IntentParams.DATA2, -1);
        super.setContentView(R.layout.activity_ticket_forward);

    }

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new TicketDataPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("转送给朋友");
    }


    private void initView() {
        specRectView = (SpecRectView) findViewById(R.id.specRectView);
        title = (TextView) findViewById(R.id.title);
        type = (TextView) findViewById(R.id.type);
        type2 = (TextView) findViewById(R.id.type2);
        expire = (TextView) findViewById(R.id.expire);
        moneyTag = (TextView) findViewById(R.id.moneyTag);
        price = (TextView) findViewById(R.id.price);
        limitCunt = (TextView) findViewById(R.id.limitCunt);
        topLayout = (RelativeLayout) findViewById(R.id.topLayout);
        ticketId = (TextView) findViewById(R.id.ticketId);
        specRectView1 = (SpecRectView) findViewById(R.id.specRectView1);
        ticketBaseInfo = (RelativeLayout) findViewById(R.id.ticketBaseInfo);
        phoneNumET = (EditText) findViewById(R.id.phoneNumET);
        confirmShareBtn = (Button) findViewById(R.id.confirmShareBtn);
        confirmShareBtn.setOnClickListener(confirmClickListener);
        showTicketBaseInfo();
    }

    private OnSingleClickListener confirmClickListener = new OnSingleClickListener() {
        @Override
        protected void onSingleClick(View view) {

            String phoneNumber = phoneNumET.getText().toString();

            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile", phoneNumber);
            params.put("id", item.id + "");
            params.put("token", IOUtils.getToken(mContext));
            mPresenter.setTicketForward(params);
        }
    };

    private void showTicketBaseInfo() {


        String color = item.ticketInfo.color;

        if (!TextUtils.isEmpty(color)) {
            specRectView.setBGColor(color);
        }
        title.setText(item.senseInfo.name + "");

        String typeStr = "";
        if (item.type == 1) {
            typeStr = "门票";
        } else {
            typeStr = "赠票";
        }
        type2.setText(typeStr);
        type.setText(item.ticketInfo.name + "");

        expire.setText(
                item.ticketInfo.remarks);


        price.setText(item.ticketInfo.price + "");

        if (item.type == 1) {
            price.getPaint().setFlags(Paint.SUBPIXEL_TEXT_FLAG);
        } else {
            price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        ticketId.setText("No. " + item.ticketItems.get(position).serialSN);
        //remarkTv.setText(item.ticketInfo.remarks);
    }

    @Override
    public void showData(Object o) {

        CommonUtils.showToast(mContext,"转送成功");
        finish();
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
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
