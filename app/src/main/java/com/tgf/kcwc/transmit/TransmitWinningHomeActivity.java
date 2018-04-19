package com.tgf.kcwc.transmit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.ListviewHint;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeListBean;
import com.tgf.kcwc.mvp.presenter.TransmitWinningHomePresenter;
import com.tgf.kcwc.mvp.view.TransmitWinningHomeView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/7/28.
 */

public class TransmitWinningHomeActivity extends BaseActivity implements TransmitWinningHomeView {

    private TransmitWinningHomePresenter mTransmitWinningHomePresenter;
    private TextView mAll;
    private TextView mParticipation;
    private ImageView mTitleBarBack;
    private FunctionView mTitleFunctionBtn;

    private ListView mListView;
    private WrapAdapter<TransmitWinningHomeListBean.DataList> mAdapter;
    public List<TransmitWinningHomeListBean.DataList> dataList = new ArrayList<>();

    private ListviewHint mListviewHint ;             //尾部

    private int page = 1;
    private boolean isRefresh = true;
    private String type = "1";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTransmitWinningHomePresenter!=null){
            mTransmitWinningHomePresenter.detachView();
        }
    }

    @Override
    protected void setUpViews() {
        mTransmitWinningHomePresenter = new TransmitWinningHomePresenter();
        mTransmitWinningHomePresenter.attachView(this);
        initRefreshLayout(mBGDelegate);
        mAll = (TextView) findViewById(R.id.all);
        mParticipation = (TextView) findViewById(R.id.participation);
        mTitleBarBack = (ImageView) findViewById(R.id.title_bar_back);
        mTitleFunctionBtn = (FunctionView) findViewById(R.id.title_function_btn);
        mListView = (ListView) findViewById(R.id.list);
        mAll.setOnClickListener(this);
        mParticipation.setOnClickListener(this);
        mTitleBarBack.setOnClickListener(this);
        mListviewHint = new ListviewHint(mContext);

        mAdapter = new WrapAdapter<TransmitWinningHomeListBean.DataList>(mContext,
                R.layout.transmitwinning_homelist_item, dataList) {

            @Override
            public void convert(ViewHolder helper,
                                final TransmitWinningHomeListBean.DataList item) {
                int position = helper.getPosition();
                SimpleDraweeView simpleDraweeView = helper.getView(R.id.bigimage);
                String times = DateFormatUtil.formatTime((long) item.second * 1000);
                TextView name = helper.getView(R.id.name);
                TextView number = helper.getView(R.id.number);
                TextView time = helper.getView(R.id.times);
                number.setText(item.times+"人参加");
                time.setText("剩余："+times);
                name.setText(item.name);
                simpleDraweeView
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));//item图片
            }

        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID,dataList.get(position).id+"");
                CommonUtils.startNewActivity(TransmitWinningHomeActivity.this, args, TansmitWinningDetailsActivity.class);
            }
        });
        loadMore();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTitleBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitwinning);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.all:
                seleteButton(true);
                break;
            case R.id.participation:
                if (IOUtils.isLogin(mContext)){
                    seleteButton(false);
                }
                break;
            case R.id.title_bar_back:
                finish();
                break;
        }
    }

    public void seleteButton(boolean isSelete) {
        if (isSelete) {
            mAll.setBackgroundResource(R.drawable.transmithome_layout_left_green);
            mAll.setTextColor(getResources().getColor(R.color.white));
            mParticipation.setBackgroundResource(R.drawable.transmithome_layout_righ_white);
            mParticipation.setTextColor(getResources().getColor(R.color.tab_text_s_color));
            type = "1";
        } else {
            mAll.setBackgroundResource(R.drawable.transmithome_layout_left_white);
            mAll.setTextColor(getResources().getColor(R.color.tab_text_s_color));
            mParticipation.setBackgroundResource(R.drawable.transmithome_layout_righ_green);
            mParticipation.setTextColor(getResources().getColor(R.color.white));
            type = "2";
        }
        page = 1;
        isRefresh = true;
        loadMore();
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

    public void loadMore() {
        if ("1".equals(type)){
            mTransmitWinningHomePresenter.gainAppLsis(IOUtils.getToken(mContext), page);
        }else {
            mTransmitWinningHomePresenter.getUserLists(IOUtils.getToken(mContext), page);
        }
    }

    @Override
    public void dataListSucceed(TransmitWinningHomeListBean transmitWinningHomeListBean) {
        stopRefreshAll();
        mListView.removeFooterView(mListviewHint);
        List<TransmitWinningHomeListBean.DataList> list = transmitWinningHomeListBean.data.list;
        if (page == 1) {
            dataList.clear();
            if (list.size() == 0 || list == null) {
                isRefresh = false;
            } else {
                mListView.removeFooterView(mListviewHint);
            }
        } else {
            if (list.size() == 0 || list == null) {
                isRefresh = false;
                mListView.addFooterView(mListviewHint);
            } else {
                mListView.removeFooterView(mListviewHint);
                isRefresh = true;
            }
        }
        dataList.addAll(list);
        mAdapter.notifyDataSetChanged();
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
}
