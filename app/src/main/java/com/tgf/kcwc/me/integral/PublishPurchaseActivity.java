package com.tgf.kcwc.me.integral;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.SponsorDrivingActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.CommentService;
import com.tgf.kcwc.mvp.model.IntegralPurchaseRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.mvp.model.IntegralRecordListBean;
import com.tgf.kcwc.mvp.presenter.IntegralPublishPresenter;
import com.tgf.kcwc.mvp.presenter.IntegralRecordPresenter;
import com.tgf.kcwc.mvp.view.IntegralPublishView;
import com.tgf.kcwc.mvp.view.IntegralRecordView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.DropUpPhonePopupWindow;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/10/25.
 * 发布交易信息
 */

public class PublishPurchaseActivity extends BaseActivity implements IntegralPublishView {

    IntegralPublishPresenter mIntegralPublishPresenter;

    private LinearLayout mTypeLayout; //类型layout
    private LinearLayout mTimeLayout;  //时间layout
    private TextView mTypeName;
    private TextView mTimeName;

    private TextView mBalance; //余额
    private TextView mConfirm; //发布
    private EditText mNum; //数量
    private EditText mPrice; //售价
    private CheckBox mCheckBoxLicense; //是否同意协议

    private DropUpPhonePopupWindow mDropUpPopupWindow;  //type数据
    private DropUpPhonePopupWindow mDropTimeUpPopupWindow;//time数据

    private List<DataItem> mDataList = new ArrayList<>();      //type数据
    private List<DataItem> mTimeList = new ArrayList<>();      //time数据
    private DataItem mTypeDataItem;                                  //选择的类型
    private DataItem mTimeDataItem;                                  //选择的时间

    int gold = 0;  //金币余额
    int mSilverDollar = 0; //银元余额

    public void gainTypeDataList() {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = -1;
        dataItem.name = "选择类型";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.name = "金币（个人）";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "银元（商务）";
        mDataList.add(dataItem);

        mTypeDataItem = mDataList.get(1);
    }

    public void gainTimeDataList() {
        mTimeList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = -1;
        dataItem.name = "选择时间";
        mTimeList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 7;
        dataItem.name = "7天";
        mTimeList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 15;
        dataItem.name = "15天";
        mTimeList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 30;
        dataItem.name = "一个月";
        mTimeList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 90;
        dataItem.name = "3个月";
        mTimeList.add(dataItem);

        mTimeDataItem = mTimeList.get(1);
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("发布积分出售信息");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_publishpurchase);
        gainTypeDataList();
        gainTimeDataList();
        gold = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        mSilverDollar = getIntent().getIntExtra(Constants.IntentParams.ID2, 0);
        mIntegralPublishPresenter = new IntegralPublishPresenter();
        mIntegralPublishPresenter.attachView(this);
        mTypeLayout = (LinearLayout) findViewById(R.id.type);
        mTimeLayout = (LinearLayout) findViewById(R.id.timelayout);
        mTypeName = (TextView) findViewById(R.id.typename);
        mBalance = (TextView) findViewById(R.id.balance);
        mNum = (EditText) findViewById(R.id.num);
        mPrice = (EditText) findViewById(R.id.price);
        mTimeName = (TextView) findViewById(R.id.time);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mCheckBoxLicense = (CheckBox) findViewById(R.id.checkBox_license);
        mTypeLayout.setOnClickListener(this);
        mTimeLayout.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mBalance.setText("余额（" + gold + ")");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.type:
                mDropUpPopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                mDropUpPopupWindow.show(this);
                mDropUpPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPopupWindow.getIsSelec()) {
                            mTypeDataItem = selectDataItem;
                            mTypeName.setText(mTypeDataItem.name);
                            if (mTypeDataItem.id == 1) {
                                mBalance.setText("余额（" + gold + ")");
                            } else {
                                mBalance.setText("余额（" + mSilverDollar + ")");
                            }
                            //setTypeLay(mTypeDataItem.id);
                        }
                    }
                });
                break;
            case R.id.timelayout:
                mDropTimeUpPopupWindow = new DropUpPhonePopupWindow(mContext, mTimeList);
                mDropTimeUpPopupWindow.show(this);
                mDropTimeUpPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropTimeUpPopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropTimeUpPopupWindow.getIsSelec()) {
                            mTimeDataItem = selectDataItem;
                            mTimeName.setText(mTimeDataItem.name);
                            //setTypeLay(mTypeDataItem.id);
                        }
                    }
                });
                break;
            case R.id.confirm:
                String trim = mNum.getText().toString().trim();
                int num = Integer.parseInt(trim);
                if (mTypeDataItem.id == 1) {
                    if (gold < num) {
                        CommonUtils.showToast(mContext, "超出余额");
                        return;
                    }
                } else {
                    if (mSilverDollar < num) {
                        CommonUtils.showToast(mContext, "超出余额");
                        return;
                    }
                }

                if (TextUtil.isEmpty(mPrice.getText().toString().trim())) {
                    CommonUtils.showToast(mContext, "请输入出售价格");
                    return;
                }
                if (TextUtil.isEmpty(mNum.getText().toString().trim())) {
                    CommonUtils.showToast(mContext, "请输入出售数量");
                    return;
                }
                if (!mCheckBoxLicense.isChecked()) {
                    CommonUtils.showToast(mContext, "请同意积分出售信息协议");
                    return;
                }
                mIntegralPublishPresenter.getPointsReceiveList(builderParams());
                break;
        }
    }


    private Map<String, String> builderParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("price", mPrice.getText().toString().trim());
        params.put("point_type", mTypeDataItem.id + "");
        params.put("points", mNum.getText().toString().trim());
        params.put("unsale", mTimeDataItem.id + "");
        params.put("vehicle_type", "car");
        return params;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public void DataSucceed(BaseArryBean baseBean) {
        CommonUtils.showToast(mContext, "发布成功");
        finish();
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
