package com.tgf.kcwc.me.dealerbalance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BalanceDetailModel;
import com.tgf.kcwc.mvp.presenter.DealerWalletPresenter;
import com.tgf.kcwc.mvp.view.DealerWalletView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/10/19
 * E-mail:fishloveqin@gmail.com
 * 明细详情
 */

public class BalanceDetailActivity extends BaseActivity implements DealerWalletView<BalanceDetailModel> {

    protected TextView statementTypeTv;
    protected TextView moneyTv;
    protected ListView list;
    protected TextView remarkContentTv;
    private DealerWalletPresenter mWalletPresenter;
    private String mId = "";
    private List<DataItem> items = new ArrayList<DataItem>();
    private WrapAdapter<DataItem> mAdapter = null;

    @Override
    protected void setUpViews() {

        initView();

        initListData();
    }

    private void initListData() {


        String arrays[] = mRes.getStringArray(R.array.balance_detail_values);

        for (String str : arrays) {

            DataItem item = new DataItem();
            item.title = str;
            items.add(item);
        }
        mAdapter = new WrapAdapter<DataItem>(mContext, R.layout.balance_detail_item, items) {


            protected TextView contentTv;
            protected TextView titleTv;


            @Override
            public void convert(ViewHolder helper, DataItem item) {

                titleTv = helper.getView(R.id.titleTv);
                contentTv = helper.getView(R.id.contentTv);
                titleTv.setText(item.title);
                contentTv.setText(item.content);
            }
        };
        list.setAdapter(mAdapter);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("零钱明细");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mId = intent.getStringExtra(Constants.IntentParams.ID);
        super.setContentView(R.layout.activity_balance_detail);

    }

    private void initView() {
        statementTypeTv = (TextView) findViewById(R.id.statementTypeTv);
        moneyTv = (TextView) findViewById(R.id.moneyTv);
        list = (ListView) findViewById(R.id.list);
        remarkContentTv = (TextView) findViewById(R.id.remarkContentTv);

        mWalletPresenter = new DealerWalletPresenter();
        mWalletPresenter.attachView(this);
        mWalletPresenter.getBalanceDetail(IOUtils.getToken(mContext), mId);
    }

    @Override
    public void showData(BalanceDetailModel model) {

        int flag = model.flag;
        String title = "", type = "";
        int color = mRes.getColor(R.color.text_color18);
        if (flag == Constants.BalanceTypes.IN) {
            title = "入账金额";
            type = "收入";
            color = mRes.getColor(R.color.text_color10);
        } else if (flag == Constants.BalanceTypes.OUT) {

            title = "出账金额";
            type = "支出";
            color = mRes.getColor(R.color.text_color18);
        }
        statementTypeTv.setText(title);
        moneyTv.setText(model.money);
        moneyTv.setTextColor(color);
        remarkContentTv.setText(model.actType);
        items.get(0).content = type;
        items.get(1).content = model.createTime;
        items.get(2).content = model.orderSN;
        items.get(3).content = model.balance;
        mAdapter.notifyDataSetChanged();
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
        if (mWalletPresenter != null) {
            mWalletPresenter.detachView();
        }
    }
}
