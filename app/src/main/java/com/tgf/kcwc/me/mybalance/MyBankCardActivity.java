package com.tgf.kcwc.me.mybalance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.mybalance.setpaypwd.SettingPayPwdActivity;
import com.tgf.kcwc.mvp.model.BankCardModel;
import com.tgf.kcwc.mvp.presenter.MyWalletPresenter;
import com.tgf.kcwc.mvp.view.MyWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 */

public class MyBankCardActivity extends BaseActivity implements MyWalletView<List<BankCardModel>> {
    protected TextView msgTv;
    protected RelativeLayout emptyLayout;
    protected ListView list;
    protected Button addBtn;

    protected MyWalletPresenter mPresenter;

    private int isPwd;

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new MyWalletPresenter();
        mPresenter.attachView(this);

    }


    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getBankCards(IOUtils.getToken(mContext));
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("我的银行卡");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isPwd = intent.getIntExtra(Constants.IntentParams.DATA, -1);
        super.setContentView(R.layout.activity_my_bank_card);

    }

    private void initView() {
        msgTv = (TextView) findViewById(R.id.msgTv);
        emptyLayout = (RelativeLayout) findViewById(R.id.emptyLayout);
        list = (ListView) findViewById(R.id.list);
        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.addBtn:
                if (isPwd == 1) {
                    CommonUtils.startNewActivity(mContext, AddBankCardStepFirstActivity.class);
                } else {
                    CommonUtils.startNewActivity(mContext, SettingPayPwdActivity.class);
                }

                break;
        }
    }

    @Override
    public void showData(List<BankCardModel> bankCardModels) {

        int size = bankCardModels.size();

        if (size == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);

        } else {
            emptyLayout.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);

            WrapAdapter<BankCardModel> adapter = new WrapAdapter<BankCardModel>(mContext, R.layout.my_bank_card_list_item, bankCardModels) {


                protected TextView cardNumTv;
                protected TextView cardTypeTv;
                protected TextView cardNameTv;
                protected SimpleDraweeView icon;

                @Override
                public void convert(ViewHolder helper, BankCardModel item) {


                    icon = helper.getView(R.id.icon);
                    cardNameTv = helper.getView(R.id.cardNameTv);
                    cardTypeTv = helper.getView(R.id.cardTypeTv);
                    cardNumTv = helper.getView(R.id.cardNumTv);

                    cardNameTv.setText(item.bankInfo.name);
                    cardNumTv.setText(item.cardCode);
                    cardTypeTv.setText(item.bankInfo.type);
                }
            };

            list.setAdapter(adapter);
        }

    }
}
