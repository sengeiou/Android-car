package com.tgf.kcwc.coupon.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.CouponManageListModel;
import com.tgf.kcwc.mvp.presenter.CouponManageListPresenter;
import com.tgf.kcwc.mvp.view.CouponManageListView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 * 代金券管理列表
 */

public class CouponManagerActivity extends BaseActivity implements CouponManageListView {

    private static final String                      TAG = "TickerManagerActivity";
    private CouponManageListPresenter ticketManageListPresenter;
    private ListView                                 mlistView;
    private List<CouponManageListModel.CouponManageItem> manageList;
    private final String KEY_INTENT_COUPONID = "couponid";
    private final String KEY_INTENT_DISTRIBUTEID = "distributeid";
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("代金券管理");
    }

    @Override
    protected void setUpViews() {
        ticketManageListPresenter = new CouponManageListPresenter();
        ticketManageListPresenter.attachView(this);
        ticketManageListPresenter.getCouponManageList(IOUtils.getToken(getContext()));
        mlistView = (ListView) findViewById(R.id.ticketmanage_list_lv);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_managelist);
    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ticketManageListPresenter.detachView();
    }

    @Override
    public void showCouponManageList(ArrayList<CouponManageListModel.CouponManageItem> couponManageItems) {
        manageList =couponManageItems;

        mlistView.setAdapter(new WrapAdapter<CouponManageListModel.CouponManageItem>(this, manageList, R.layout.listitem_ticket_manage) {
            @Override
            public void convert(ViewHolder helper,  CouponManageListModel.CouponManageItem item) {
                helper.setText(R.id.ticketmanage_nums_tv, "总量   " + item.count + "  剩余   " + item.inventory);
                helper.setText(R.id.ticketmange_ticketinfo,item.coupon.title);
                View isExpireView = helper.getView(R.id.ticketmanagelist_iv);
                helper.setImageByUrl(R.id.ticketmanage_event_iv, URLUtil.builderImgUrl(item.coupon.cover,270, 203));
                if (item.isExpiration == 1) {
                    isExpireView.setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.ticketmanagelist_iv).setVisibility(View.INVISIBLE);
                }
            }
        });
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),CouponManageDetailActivity.class);
                CouponManageListModel.CouponManageItem couponManageItem = manageList.get(position);
                intent.putExtra(KEY_INTENT_COUPONID,couponManageItem.coupon.id);
                intent.putExtra(KEY_INTENT_DISTRIBUTEID,couponManageItem.id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
