package com.tgf.kcwc.me.integral;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.fragments.TabNewPlayFragment;
import com.tgf.kcwc.fragments.TabPlayHomeFragment;
import com.tgf.kcwc.fragments.TripBookeListNewFragment;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.IntegralGoodListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.presenter.IntegralConversionPresenter;
import com.tgf.kcwc.mvp.view.IntegralConversionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GoodDetailsPopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/7/4.
 * 商品兑换及积分交易
 */

public class IntegralConversionActivity extends BaseActivity {

    BaseFragment IntegralCommodityFragment, IntegralDiamondFragment, knowFragment;
    public int skiptype = -1; //1是个人 2是商务

    private ImageButton back;
    private TextView record;

    private TextView mIntegralConversion; //积分兑换
    private TextView mIntegralDeal; //积分交易
    private int type=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTitleBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integralconversion);
        skiptype = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
    }

    @Override
    protected void setUpViews() {

        mIntegralConversion = (TextView) findViewById(R.id.integralconversion);
        mIntegralDeal = (TextView) findViewById(R.id.integraldeal);
        back = (ImageButton) findViewById(R.id.title_bar_back);
        record = (TextView) findViewById(R.id.record);

        mIntegralConversion.setOnClickListener(this);
        mIntegralDeal.setOnClickListener(this);
        back.setOnClickListener(this);
        record.setOnClickListener(this);
        CommodityTab();
    }


    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.record:
                if (type==1){
                    CommonUtils.startNewActivity(mContext,ConversionRecordActivity.class);
                }else {
                    CommonUtils.startNewActivity(mContext,ConversionPurchaseRecordActivity.class);
                }

                break;
            case R.id.integralconversion:
                mIntegralConversion.setTextColor(getResources().getColor(R.color.tab_text_s_color));
                mIntegralConversion.setBackgroundResource(R.drawable.button_integralcut_leftwhite_bg);
                mIntegralDeal.setTextColor(getResources().getColor(R.color.white));
                mIntegralDeal.setBackground(null);
                switchoverCommodity();
                type=1;
                break;
            case R.id.integraldeal:
                mIntegralDeal.setTextColor(getResources().getColor(R.color.tab_text_s_color));
                mIntegralDeal.setBackgroundResource(R.drawable.button_integralcut_rightwhite_bg);
                mIntegralConversion.setTextColor(getResources().getColor(R.color.white));
                mIntegralConversion.setBackground(null);
                switchoverDiamond();
                type=2;
                break;
        }
    }

    /**
     * 钻石
     */
    public void switchoverDiamond() {
        IntegralDiamondFragment = new IntegralDiamondFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), IntegralDiamondFragment);
        knowFragment = IntegralDiamondFragment;
        //CommonUtils.startNewActivity(mContext, TripBookeListActivity.class);
    }

    /**
     * 商品
     */
    public void switchoverCommodity() {
        IntegralCommodityFragment = new IntegralCommodityFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), IntegralCommodityFragment);
        knowFragment = IntegralCommodityFragment;
    }

    /**
     * 商品
     */
    private void CommodityTab() {
        if (IntegralCommodityFragment == null) {
            IntegralCommodityFragment = new IntegralCommodityFragment();
        }
        if (!IntegralCommodityFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction().add(R.id.content_layout, IntegralCommodityFragment).commit();
            // 记录当前Fragment
            knowFragment = IntegralCommodityFragment;
        }
    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction, BaseFragment fragment) {
        if (knowFragment == fragment) {
            return;
        }

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(knowFragment).add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(knowFragment).show(fragment).commit();
        }

        knowFragment = fragment;
    }

}
