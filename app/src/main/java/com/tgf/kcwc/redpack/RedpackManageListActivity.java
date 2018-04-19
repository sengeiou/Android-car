package com.tgf.kcwc.redpack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.RedpackManageListModel;
import com.tgf.kcwc.mvp.view.RedpackManageListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.DropUpPhonePopupWindow;
import com.tgf.kcwc.view.DropUpSelectPopupWindow;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/10/26 0026
 * E-mail:hekescott@qq.com
 */

public class RedpackManageListActivity extends BaseActivity implements RedpackManageListView {

    private ListView managelistLv;
    private ArrayList<RedpackManageListModel> list = new ArrayList<>();
    private DropUpSelectPopupWindow mDropUpPopupWindow;
    private List<DataItem> mDataList = new ArrayList<>(); //type数据
    private DataItem mTypeDataItem;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        View backBtn = findViewById(R.id.titlebar_back);
        backEvent(backBtn);
        findViewById(R.id.title_addtv).setOnClickListener(this);
        gainTypeDataList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpackmanagelist);
        managelistLv = (ListView) findViewById(R.id.redpack_managelistlv);
        managelistLv.setAdapter(new WrapAdapter<RedpackManageListModel>(getContext(), R.layout.listitem_redpack_manage, list) {
            @Override
            public void convert(ViewHolder helper, RedpackManageListModel item) {

            }

        });
    }

    public void gainTypeDataList() {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.name = "现金红包";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "门票红包";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.name = "代金券红包";
        mDataList.add(dataItem);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.title_addtv:
                mDropUpPopupWindow = new DropUpSelectPopupWindow(mContext, mDataList);
                // mDropUpPopupWindow.showAsDropDownBelwBtnView(mTitleLayout);
                mDropUpPopupWindow.show(RedpackManageListActivity.this);
                mDropUpPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPopupWindow.getIsSelec()) {
                            mTypeDataItem = selectDataItem;
                            if (selectDataItem.id == 1) {
                                Intent toRedCashAct = new Intent(getContext(), RedpackCashActivity.class);
                                startActivity(toRedCashAct);
                            } else if (selectDataItem.id == 2) {
                                Intent toRedTicketAct = new Intent(getContext(), RedpackTicketActivity.class);
                                startActivity(toRedTicketAct);

                            } else if (selectDataItem.id == 3) {
                                Intent toRedCouponAct = new Intent(getContext(), RedpackCoupontActivity.class);
                                startActivity(toRedCouponAct);
                            }
//                            mType.setText(mTypeDataItem.name);
//                            setTypeLay(mTypeDataItem.id);
                        }
                    }
                });
                break;
            default:
                break;
        }
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
}
