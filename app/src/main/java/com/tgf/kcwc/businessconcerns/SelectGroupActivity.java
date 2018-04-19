package com.tgf.kcwc.businessconcerns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AddCustomerModel;
import com.tgf.kcwc.mvp.model.FriendGroupingModel;
import com.tgf.kcwc.mvp.model.FriendListModel;
import com.tgf.kcwc.mvp.presenter.BusinessAttentionPresenter;
import com.tgf.kcwc.mvp.presenter.GroupingPresenter;
import com.tgf.kcwc.mvp.view.AddCustomerView;
import com.tgf.kcwc.mvp.view.BusinessAttentionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/8/4 15:08
 * 选择分组
 */

public class SelectGroupActivity extends BaseActivity implements BusinessAttentionView,AddCustomerView{
    private ListView mListView;
    private WrapAdapter mAdapter;
    private AdapterView.OnItemClickListener mItemClickListener;
    ArrayList<FriendGroupingModel.ListItem> groupingList = new ArrayList<>();
    private BusinessAttentionPresenter mPresenter;
    private GroupingPresenter mGroupingPresenter;
    ArrayList<AddCustomerModel> mGroupingList = new ArrayList<>();
    private int page = 1;
    private boolean isRefresh;
    //多个好友id
    private String ids;
    @Override
    protected void setUpViews() {
        mListView = (ListView) findViewById(R.id.friend_group_lv);
        initRefreshLayout(mBGDelegate);
        mPresenter = new BusinessAttentionPresenter();
        mPresenter.attachView(this);
        mGroupingPresenter = new GroupingPresenter();
        mGroupingPresenter.attachView(this);
        ids = getIntent().getStringExtra(Constants.IntentParams.DATA);
        //获取好友分组列表
        mPresenter.getGroupingList(IOUtils.getToken(getContext()));
        if (null != mAdapter){
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mItemClickListener);
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        back.setImageResource(R.drawable.icon_left_x);
        backEvent(back);
        text.setText("选择分组");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter){
            mPresenter.detachView();
        }
        if (null != mGroupingPresenter){
            mPresenter.detachView();
        }
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            page = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isRefresh = false;
            page++;
            loadMore();
            return false;
        }
    };

    public void loadMore() {
//        mPresenter.getGroupingList(IOUtils.getToken(getContext()));
        stopRefreshAll();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (isRefresh){
            showLoadingIndicator(true);
        }else {
            stopRefreshAll();
            showLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showGrouping(ArrayList<FriendGroupingModel.ListItem> list) {
        if (isRefresh){
            groupingList.clear();
        }
        groupingList.addAll(list);
        if (null != mAdapter){
            mAdapter.notifyDataSetChanged();
        }else {
            initAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mItemClickListener);
        }
    }
    //不需要
    @Override
    public void showFriendList(ArrayList<FriendListModel.ListItem> list) {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public void initAdapter() {
        mItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: 2017/8/9 返回好友分组
                if (getIntent().hasExtra(Constants.IntentParams.DATA)){
                    int friendGroupId = groupingList.get(i).friendGroupId;
                    mGroupingPresenter.grouping(IOUtils.getToken(getContext()),friendGroupId,ids);
                }else {
                    // TODO: 2017/8/7 返回添加用户
                    String groupName = groupingList.get(i).name;
                    int friendGroupId = groupingList.get(i).friendGroupId;
                    Intent intent = new Intent();
                    intent.putExtra(Constants.IntentParams.DATA,groupName);
                    intent.putExtra(Constants.IntentParams.DATA2,friendGroupId);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        };
        mAdapter = new WrapAdapter<FriendGroupingModel.ListItem>(SelectGroupActivity.this, groupingList, R.layout.select_group_item) {
            @Override
            public void convert(ViewHolder helper, FriendGroupingModel.ListItem item) {
                if (StringUtils.checkNull(item.name)){
                    helper.setText(R.id.item_grouping_name,item.name);
                }
            }
        };
    }

    @Override
    public void addSuccess(ArrayList<AddCustomerModel.FriendItem> item) {
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext,"好友分组成功");
        finish();
    }

    @Override
    public void addFail(String msg) {
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext,msg);
    }
}
