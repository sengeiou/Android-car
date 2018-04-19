package com.tgf.kcwc.me.dealerbalance;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.entity.PieDataEntity;
import com.tgf.kcwc.mvp.model.AccountBillModel;
import com.tgf.kcwc.mvp.presenter.DealerWalletPresenter;
import com.tgf.kcwc.mvp.view.DealerWalletView;
import com.tgf.kcwc.util.DataFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PieChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.qqtheme.framework.picker.DatePicker;

/**
 * Author:Jenny
 * Date:2017/10/17
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 对账单
 */

public class CheckingAccountActivity extends BaseActivity implements DealerWalletView<AccountBillModel> {
    protected PieChart pieChart;
    protected TextView titleTv;
    protected TextView incomeTv;
    protected TextView outlayTv;
    protected ListView list;
    private ScrollView mRootView;
    private RelativeLayout emptyLayout;
    private DealerWalletPresenter mPresenter;

    @Override
    protected void setUpViews() {
        initView();


    }


    private void showPieView(AccountBillModel model) {


        pieChart = (PieChart) findViewById(R.id.pv);
        List<PieDataEntity> dataEntities = new ArrayList<>();
        PieDataEntity entity = new PieDataEntity("当月总支出", model.expenditure, mRes.getColor(R.color.text_color18));
        dataEntities.add(entity);
        entity = new PieDataEntity("当月总收入", model.income, mRes.getColor(R.color.text_color10));

        dataEntities.add(entity);

        pieChart.setDataList(dataEntities);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        text.setText("电子账单");
        backEvent(back);

        function.setImageResource(R.drawable.btn_date_selector);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadAccountDataByDate();
            }
        });
    }

    private void loadAccountDataByDate() {


        DatePicker picker = new DatePicker(this,DatePicker.YEAR_MONTH);
        picker.setRange(Calendar.getInstance().get(Calendar.YEAR) - 1, Calendar.getInstance().get(Calendar.YEAR));//年份范围

        picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
                String date = year + "-" + month;
                mPresenter.getAccountBill(IOUtils.getToken(mContext), date);
            }
        });
        picker.show();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_checking_account);

    }

    private void initView() {
        pieChart = (PieChart) findViewById(R.id.pv);

        mPresenter = new DealerWalletPresenter();
        mPresenter.attachView(this);
        mPresenter.getAccountBill(IOUtils.getToken(mContext), "");
        titleTv = (TextView) findViewById(R.id.titleTv);
        incomeTv = (TextView) findViewById(R.id.incomeTv);
        outlayTv = (TextView) findViewById(R.id.outlayTv);
        list = (ListView) findViewById(R.id.list);
        mRootView = (ScrollView) findViewById(R.id.rootView);
        mEmptyLayout = (RelativeLayout) findViewById(R.id.emptyLayout);
    }


    @Override
    public void showData(AccountBillModel model) {


        if (model.expenditureInfo.size() == 0 && model.incomeInfo.size() == 0) {
            mRootView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mRootView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            showHeaderInfo(model);

            showPieView(model);

            showAccountDetail(model);
        }


    }

    private void showAccountDetail(AccountBillModel model) {

        List<Bill> datas = new ArrayList<Bill>();

        Bill bill = new Bill();
        bill.total = "+" + model.income;
        bill.title = model.month + "月总收入(元)";
        bill.textColor = mRes.getColor(R.color.text_color10);
        bill.items = model.incomeInfo;
        for (AccountBillModel.BillItemBean billItemBean : bill.items) {
            billItemBean.content = "转入";
        }
        datas.add(bill);

        bill = new Bill();
        bill.total = "-" + model.expenditure;
        bill.title = model.month + "月总支出(元)";
        bill.items = model.expenditureInfo;
        bill.textColor = mRes.getColor(R.color.text_color18);
        for (AccountBillModel.BillItemBean billItemBean : bill.items) {
            billItemBean.content = "支出";
        }
        datas.add(bill);
        WrapAdapter<Bill> adapter = new WrapAdapter<Bill>(mContext, R.layout.account_bill_list_item, datas) {


            protected ListView list;
            protected TextView totalTv;
            protected TextView titleTv;
            protected View rootView;

            @Override
            public void convert(ViewHolder helper, Bill item) {

                titleTv = helper.getView(R.id.titleTv);
                totalTv = helper.getView(R.id.totalTv);
                titleTv.setText(item.title);
                totalTv.setText(DataFormatUtil.getFormatMoney(item.total + ""));
                totalTv.setTextColor(item.textColor);

                list = helper.getView(R.id.list);

                list.setAdapter(new WrapAdapter<AccountBillModel.BillItemBean>(mContext, item.items, R.layout.bill_item) {


                    protected TextView moneyTv;
                    protected TextView contentTv;
                    protected TextView titleTv;
                    protected ImageView iconImage;
                    protected View rootView;

                    @Override
                    public void convert(ViewHolder helper, AccountBillModel.BillItemBean item) {

                        iconImage = helper.getView(R.id.iconImage);

                        int drawableIcon = R.drawable.icon_chongz;


                        iconImage.setVisibility(View.VISIBLE);
                        titleTv = helper.getView(R.id.titleTv);
                        contentTv = helper.getView(R.id.contentTv);
                        moneyTv = helper.getView(R.id.moneyTv);

                        /*
                        * 1=>'打赏',
             2=>'充值',
             3=>'提现',
             4=>'分红',
             5=>'门票',
             6=>'代金券',
             7=>'红包'
                        *
                        * */

                        switch (item.iconType) {

                            case 1:
                                drawableIcon = R.drawable.icon_dashang;
                                break;

                            case 2:
                                drawableIcon = R.drawable.icon_chongz;
                                break;
                            case 3:
                                drawableIcon = R.drawable.icon_tixian;
                                break;

                            case 4:
                                drawableIcon = R.drawable.icon_fenhong;
                                break;

                            case 5:
                                drawableIcon = R.drawable.icon_menpiao;
                                break;
                            case 6:
                                drawableIcon = R.drawable.icon_daijq;
                                break;
                            case 7:
                                drawableIcon = R.drawable.icon_hongbaox;
                                break;
                        }
                        iconImage.setImageResource(drawableIcon);
                        contentTv.setText(item.content);
                        if ("转入".equals(item.content)) {
                            moneyTv.setText("+" + DataFormatUtil.getFormatMoney(item.totalMoney + ""));
                        } else if ("支出".equals(item.content)) {
                            moneyTv.setText("-" + DataFormatUtil.getFormatMoney(item.totalMoney + ""));
                        }
                        titleTv.setText(item.actType);
                    }
                });
                ViewUtil.setListViewHeightBasedOnChildren(list);
            }
        };
        list.setAdapter(adapter);
        ViewUtil.setListViewHeightBasedOnChildren(list);

    }

    private void showHeaderInfo(AccountBillModel model) {

        titleTv.setText(model.month + "月总账单(元):");

        String incomeMoney = model.income + "";
        String expenditureMoney = model.expenditure + "";

        incomeTv.setText(DataFormatUtil.getFormatMoney(incomeMoney));
        outlayTv.setText(DataFormatUtil.getFormatMoney(expenditureMoney));
    }

    private void showFormatMoney(TextView incomeTv, TextView outlayTv, String incomeMoney, String expenditureMoney) {


        if (incomeMoney.indexOf(".0") >= 0 || incomeMoney.indexOf(".00") >= 0) {

            incomeMoney = incomeMoney.substring(0, incomeMoney.indexOf("."));
        }

        if (expenditureMoney.indexOf(".0") >= 0 || expenditureMoney.indexOf(".00") >= 0) {

            expenditureMoney = expenditureMoney.substring(0, expenditureMoney.indexOf("."));
        }
        incomeTv.setText("+ " + incomeMoney);
        outlayTv.setText("- " + expenditureMoney);

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


    public static class Bill {

        public String title;

        public String total;

        public int textColor;
        public List<AccountBillModel.BillItemBean> items;


    }
}
