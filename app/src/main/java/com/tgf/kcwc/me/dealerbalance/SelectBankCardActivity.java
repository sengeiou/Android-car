package com.tgf.kcwc.me.dealerbalance;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.BankCardModel;
import com.tgf.kcwc.mvp.presenter.DealerWalletPresenter;
import com.tgf.kcwc.mvp.view.DealerWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/10/23
 * E-mail:fishloveqin@gmail.com
 */

public class SelectBankCardActivity extends BaseActivity implements DealerWalletView<List<BankCardModel>>, AdapterView.OnItemClickListener {
    protected RelativeLayout firstLayout;
    protected ListView list;
    protected ImageView bankCardImg;
    protected TextView bankName;
    protected RelativeLayout addBankLayout;
    protected DealerWalletPresenter mPresenter, mSetPresenter;
    private List<BankCardModel> mItems = new ArrayList<BankCardModel>();

    WrapAdapter<BankCardModel> mAdapter = null;

    @Override
    protected void setUpViews() {
        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("选择银行卡");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_select_bank_card);

    }


    private void initView() {
        firstLayout = (RelativeLayout) findViewById(R.id.firstLayout);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);
        bankCardImg = (ImageView) findViewById(R.id.bankCardImg);
        bankName = (TextView) findViewById(R.id.bankName);
        addBankLayout = (RelativeLayout) findViewById(R.id.addBankLayout);
        addBankLayout.setOnClickListener(this);
        mPresenter = new DealerWalletPresenter();
        mPresenter.attachView(this);
        mSetPresenter = new DealerWalletPresenter();
        mSetPresenter.attachView(mSetView);
        initListData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getBankCards(IOUtils.getToken(mContext));

    }

    /**
     * 设置银行卡选中View回调
     */
    private DealerWalletView mSetView = new DealerWalletView() {
        @Override
        public void showData(Object object) {

            CommonUtils.showToast(mContext, "设置成功");
            finish();
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
    };


    private void initListData() {

        mAdapter = new WrapAdapter<BankCardModel>(mContext, R.layout.select_bank_card_list_item, mItems) {


            protected ImageView selectImg;
            protected TextView cardTypeTv;
            protected TextView cardNameTv;
            protected SimpleDraweeView icon;
            protected View rootView;

            @Override
            public void convert(ViewHolder helper, BankCardModel item) {


                icon = helper.getView(R.id.icon);
                cardNameTv = helper.getView(R.id.cardNameTv);
                cardTypeTv = helper.getView(R.id.cardTypeTv);
                selectImg = helper.getView(R.id.selectImg);

                if (item.isSelected == 1) {
                    selectImg.setVisibility(View.VISIBLE);
                } else {
                    selectImg.setVisibility(View.GONE);
                }
                cardNameTv.setText(item.bankInfo.name);
                cardTypeTv.setText(item.bankInfo.type);
            }
        };

        list.setAdapter(mAdapter);
    }

    @Override
    public void showData(List<BankCardModel> bankCardModels) {

        mItems.clear();
        mItems.addAll(bankCardModels);
        if (mItems.size() == 0) {
            list.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.VISIBLE);
        }

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        BankCardModel model = (BankCardModel) parent.getAdapter().getItem(position);

        mSetPresenter.setBankCardSelected(builderReqParams(model));
    }

    private Map<String, String> builderReqParams(BankCardModel model) {

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("token", IOUtils.getToken(mContext));
        params.put("id", model.id + "");
        return params;
    }

    @Override
    public void onClick(View view) {


        CommonUtils.startNewActivity(mContext, AddBankCardStepFirstActivity.class);

    }
}
