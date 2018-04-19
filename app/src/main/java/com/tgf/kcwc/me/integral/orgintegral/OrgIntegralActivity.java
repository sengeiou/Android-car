package com.tgf.kcwc.me.integral.orgintegral;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.tgf.kcwc.me.integral.IntegralConversionActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.model.OrgIntegralListBean;
import com.tgf.kcwc.mvp.presenter.IntegralPresenter;
import com.tgf.kcwc.mvp.presenter.OrgIntegralPresenter;
import com.tgf.kcwc.mvp.view.IntegralView;
import com.tgf.kcwc.mvp.view.OrgIntegralView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/7/4.
 * 机构机构积分首页
 */

public class OrgIntegralActivity extends BaseActivity implements OrgIntegralView {

    private OrgIntegralPresenter mOrgIntegralPresenter;
    private ListView mListView;
    private WrapAdapter<OrgIntegralListBean.DataList> mAdapter;
    private List<OrgIntegralListBean.DataList> datalist = new ArrayList<>();
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
    private ProgressBar mProgressBar; //经验条

    private TextView mAdd; //增加
    private TextView mSubtract; //减少

    public int skiptype = 2; //1是个人 2是商务
    private String type = "1"; //	type  1是积分 2是经验
    int page = 1;
    boolean isRefresh = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgintegral);
    }

    @Override
    protected void setUpViews() {
        mOrgIntegralPresenter = new OrgIntegralPresenter();
        mOrgIntegralPresenter.attachView(this);
        // skiptype = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
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
        mAdd = (TextView) findViewById(R.id.add);
        mSubtract = (TextView) findViewById(R.id.subtract);
        setmAdapter();

        mIntegrallayout.setOnClickListener(this);
        mExperiencelayout.setOnClickListener(this);
        mConversionBtn.setOnClickListener(this);
        loadMore();
    }

    public void loadMore() {
        mOrgIntegralPresenter.getRecordpList(IOUtils.getToken(mContext), type, page);
    }


    public void setmAdapter() {
        mAdapter = new WrapAdapter<OrgIntegralListBean.DataList>(mContext, R.layout.integral_listview_item, datalist) {
            @Override
            public void convert(ViewHolder helper, OrgIntegralListBean.DataList item) {
                TextView mTitle = helper.getView(R.id.title);
                TextView mTime = helper.getView(R.id.time);
                TextView mBonuspoint = helper.getView(R.id.bonuspoint);
                mTitle.setText(item.name);
                mTime.setText(item.createTime);
                if (type.equals("1")) {
                    mBonuspoint.setText(item.points + "分");
                } else {
                    mBonuspoint.setText(item.exps + "分");
                }
                if (item.crease.equals("reward")) {
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
                args.put(Constants.IntentParams.ID2, type);
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
                CommonUtils.startNewActivity(this, args, OrgIntegralConcersionActivity.class);
                break;
        }
    }

    public void changeButton(Boolean isBoolean) {
        if (isBoolean) {
            mIntegrallayoutname.setTextColor(getResources().getColor(R.color.text_selected));
            mIntegrallayoutdown.setBackgroundColor(getResources().getColor(R.color.text_selected));
            mExperiencelayoutname.setTextColor(getResources().getColor(R.color.color_login_devide));
            mExperiencelayoutdown.setBackgroundColor(getResources().getColor(R.color.white));
            type = "1";
            page = 1;
            loadMore();
        } else {
            mIntegrallayoutname.setTextColor(getResources().getColor(R.color.color_login_devide));
            mIntegrallayoutdown.setBackgroundColor(getResources().getColor(R.color.white));
            mExperiencelayoutname.setTextColor(getResources().getColor(R.color.text_selected));
            mExperiencelayoutdown.setBackgroundColor(getResources().getColor(R.color.text_selected));
            type = "2";
            page = 1;
            loadMore();
        }

    }

    @Override
    public void DatalistSucceed(OrgIntegralListBean integralListBean) {
        stopRefreshAll();
        if (page == 1) {
            OrgIntegralListBean.Info info = integralListBean.data.info;
            mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(info.avatar, 360, 360)));
            mGrade.setText(info.level + "");
            mExperienceText.setText(info.exp + "/" + info.nextLevel);
            mIntegralText.setText(info.currentPoints + "");
            mProgressBar.setMax(info.nextLevel);
            mProgressBar.setSecondaryProgress(info.exp);
            mAdd.setText(integralListBean.data.info.getPoints + "");
            mSubtract.setText(integralListBean.data.info.usedPoints + "");
            datalist.clear();
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
