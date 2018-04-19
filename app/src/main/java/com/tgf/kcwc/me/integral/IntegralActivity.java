package com.tgf.kcwc.me.integral;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.integral.orgintegral.OrgIntegralDetailsActivity;
import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.presenter.IntegralPresenter;
import com.tgf.kcwc.mvp.view.IntegralView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.link.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/7/4.
 * 积分首页
 */

public class IntegralActivity extends BaseActivity implements IntegralView {

    private IntegralPresenter mIntegralPresenter;
    private ListView mListView;
    private WrapAdapter<IntegralListBean.DataList> mAdapter;
    private List<IntegralListBean.DataList> datalist = new ArrayList<>();
    private SimpleDraweeView mSimpleDraweeView;
    //积分历史
    private LinearLayout mIntegrallayout;
    private TextView mIntegrallayoutname;
    private TextView mIntegrallayoutdown;
    //经验历史
    private LinearLayout mExperiencelayout;
    private TextView mExperiencelayoutname;
    private TextView mExperiencelayoutdown;

    private TextView mConversionBtn; //兑换
    private TextView mGrade; //等级
    private TextView mIntegralText; //积分
    private TextView mExperienceText; //经验
    private TextView mNetxExp; //下一级经验
    private ProgressBar mProgressBar; //经验条

    public int skiptype = -1; //1是个人 2是商务
    private String type = "1"; //	type  1是积分 4是经验 2是商务积分  5是商务经验
    int page = 1;
    boolean isRefresh = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
    }

    @Override
    protected void setUpViews() {
        mIntegralPresenter = new IntegralPresenter();
        mIntegralPresenter.attachView(this);
        skiptype = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        initRefreshLayout(mBGDelegate);
        mListView = (ListView) findViewById(R.id.list);
        mGrade = (TextView) findViewById(R.id.grade);
        mIntegralText = (TextView) findViewById(R.id.integraltext);
        mExperienceText = (TextView) findViewById(R.id.experiencetext);
        mProgressBar = (ProgressBar) findViewById(R.id.my_progress);

        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.icon);
        mIntegrallayout = (LinearLayout) findViewById(R.id.integrallayout);
        mIntegrallayoutname = (TextView) findViewById(R.id.integrallayoutname);
        mIntegrallayoutdown = (TextView) findViewById(R.id.integrallayoutdown);
        mExperiencelayout = (LinearLayout) findViewById(R.id.experiencelayout);
        mExperiencelayoutname = (TextView) findViewById(R.id.experiencelayoutname);
        mExperiencelayoutdown = (TextView) findViewById(R.id.experiencelayoutdown);
        mConversionBtn = (TextView) findViewById(R.id.conversion);
        mNetxExp = (TextView) findViewById(R.id.netxexp);
        setmAdapter();

        mIntegrallayout.setOnClickListener(this);
        mExperiencelayout.setOnClickListener(this);
        mConversionBtn.setOnClickListener(this);
        if (skiptype != -1) {
            if (skiptype == 1) {
                type = "1";
            } else {
                type = "2";
            }
        } else {
            CommonUtils.showToast(mContext, "系统异常");
            finish();
        }
        // mIntegralPresenter.getUserinfo(IOUtils.getToken(mContext), skiptype + "");
        loadMore();
    }

    public void loadMore() {
        mIntegralPresenter.getRecordpList(IOUtils.getToken(mContext), type, page);
    }


    public void setmAdapter() {
        mAdapter = new WrapAdapter<IntegralListBean.DataList>(mContext, R.layout.integral_listview_item, datalist) {
            @Override
            public void convert(ViewHolder helper, IntegralListBean.DataList item) {
                TextView mTitle = helper.getView(R.id.title);
                TextView mTime = helper.getView(R.id.time);
                TextView mBonuspoint = helper.getView(R.id.bonuspoint);
                mTitle.setText(item.name);
                mTime.setText(item.createTime);
                if (type.equals("1")){
                    mBonuspoint.setText(item.points + "分");
                }else if (type.equals("2")){
                    mBonuspoint.setText(item.points + "分");
                }else if (type.equals("4")){
                    mBonuspoint.setText(item.exps + "经验");
                }else if (type.equals("5")){
                    mBonuspoint.setText(item.exps + "经验");
                }
                if (item.disType.equals("add")) {
                    mBonuspoint.setTextColor(getResources().getColor(R.color.text_color36));
                } else {
                    mBonuspoint.setTextColor(getResources().getColor(R.color.tab_text_s_color));
                }
            }
        };
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Serializable> args = new HashMap<>();
                args.put(Constants.IntentParams.ID, datalist.get(position).id + "");
                if (type.equals("1")||type.equals("2")){
                    args.put(Constants.IntentParams.ID2, "1");
                }else {
                    args.put(Constants.IntentParams.ID2, "2");
                }
                CommonUtils.startNewActivity(mContext, args, OrgIntegralDetailsActivity.class);

            }
        });

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.integralinquire);
        function.setTextResource(R.string.regulation, R.color.white, 15);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.integrallayout:
                changeButton(true);
                break;
            case R.id.experiencelayout:
                changeButton(false);
                break;
            case R.id.conversion:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, skiptype);
                CommonUtils.startNewActivity(this, args, IntegralConversionActivity.class);
                break;
        }
    }

    public void changeButton(Boolean isBoolean) {

        if (isBoolean) {
            mIntegrallayoutname.setTextColor(getResources().getColor(R.color.text_selected));
            mIntegrallayoutdown.setBackgroundColor(getResources().getColor(R.color.text_selected));
            mExperiencelayoutname.setTextColor(getResources().getColor(R.color.color_login_devide));
            mExperiencelayoutdown.setBackgroundColor(getResources().getColor(R.color.white));
            if (skiptype == 1) {
                type = "1";
            } else {
                type = "2";
            }
            page = 1;
            loadMore();
        } else {
            mIntegrallayoutname.setTextColor(getResources().getColor(R.color.color_login_devide));
            mIntegrallayoutdown.setBackgroundColor(getResources().getColor(R.color.white));
            mExperiencelayoutname.setTextColor(getResources().getColor(R.color.text_selected));
            mExperiencelayoutdown.setBackgroundColor(getResources().getColor(R.color.text_selected));
            if (skiptype == 1) {
                type = "4";
            } else {
                type = "5";
            }
            page = 1;
            loadMore();
        }

    }

    @Override
    public void userDataSucceed(IntegralUserinfoBean integralUserinfoBean) {
    }

    @Override
    public void DatalistSucceed(IntegralListBean integralListBean) {
        stopRefreshAll();
        if (page == 1) {
            datalist.clear();
            IntegralListBean.Info info = integralListBean.data.info;
            mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(info.avatar, 360, 360)));
            if (skiptype == 1) { //个人
                mGrade.setText("LV" + info.level);
                mExperienceText.setText(info.currentExp + "/");
                mNetxExp.setText(info.nextLevel + "");
                mIntegralText.setText(info.currentPoints + "（金币）");
                mProgressBar.setMax(info.nextLevel);
                mProgressBar.setSecondaryProgress(info.currentExp);
            } else {
                mGrade.setText(info.businessLevel + "");
                mExperienceText.setText(info.currentBusinessExp + "/");
                mNetxExp.setText(info.nextBusinessLevel + "");
                mIntegralText.setText(info.currentBusinessPoints + "（银元）");
                mProgressBar.setMax(info.nextLevel);
                mProgressBar.setSecondaryProgress(info.currentBusinessExp);
            }


        }
        if (integralListBean.data.list != null && integralListBean.data.list.size() != 0) {
            datalist.addAll(integralListBean.data.list);
        } else {
            isRefresh = false;
        }
        if (page == 1) {
            setmAdapter();
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }


    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                // 如果网络可用，则加载网络数据
            //
            //                loadMore();
            //            } else {
            //                // 网络不可用，结束下拉刷新
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //            }
            // mPageIndex = 1;
            page = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                loadMore();
            //                return true;
            //            } else {
            //                // 网络不可用，返回false，不显示正在加载更多
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //                return false;
            //            }
            //loadMore();
            if (isRefresh) {
                page++;
                loadMore();
            }
            return false;
        }
    };

}
