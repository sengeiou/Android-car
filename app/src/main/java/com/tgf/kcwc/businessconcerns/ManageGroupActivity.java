package com.tgf.kcwc.businessconcerns;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.tgf.kcwc.mvp.presenter.DeleteGroupPresenter;
import com.tgf.kcwc.mvp.view.BusinessAttentionView;
import com.tgf.kcwc.mvp.view.GroupMoveView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.swipelistview.SwipeLayoutManager;

import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/8/4 11:52
 * 分组管理
 */

public class ManageGroupActivity extends BaseActivity implements BusinessAttentionView,GroupMoveView{
    private ListView mListView;
    private TextView addBtn;
    private WrapAdapter mAdapter;
    private AdapterView.OnItemClickListener mItemClickListener;
    ArrayList<FriendGroupingModel.ListItem> groupingList = new ArrayList<>();
    private BusinessAttentionPresenter mPresenter;
    private DeleteGroupPresenter mDeletePresenter;
    private int page = 1;
    private boolean isRefresh;
    @Override
    protected void setUpViews() {
        mListView = (ListView) findViewById(R.id.manage_group_lv);
        addBtn = (TextView) findViewById(R.id.manage_add_tv);
        // 初始化SwipeLayout
        SwipeLayoutManager.getInstance().closeOpenInstance();
        initRefreshLayout(mBGDelegate);
        stopRefreshAll();
        mPresenter = new BusinessAttentionPresenter();
        mPresenter.attachView(this);
        mDeletePresenter = new DeleteGroupPresenter();
        mDeletePresenter.attachView(this);
        //获取分组名列表
        mPresenter.getGroupingList(IOUtils.getToken(getContext()));
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.startNewActivity(ManageGroupActivity.this,AddGroupActivity.class);
            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("分组管理");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);
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
        stopRefreshAll();
//        mPresenter.getGroupingList(IOUtils.getToken(getContext()));
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
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mItemClickListener);
        }else {
            initAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mItemClickListener);
        }

    }

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
                // TODO: 2017/8/7 分组
            }
        };
        mAdapter = new WrapAdapter<FriendGroupingModel.ListItem>(mContext, groupingList, R.layout.manager_group_item) {
            @Override
            public void convert(final ViewHolder helper, final FriendGroupingModel.ListItem item) {
                final LinearLayout deleteLl = helper.getView(R.id.item_delete);
                if (StringUtils.checkNull(item.name)){
                    helper.setText(R.id.item_grouping_name,item.name);
                }
                //删除
                deleteLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: 2017/8/10 删除
                        SwipeLayoutManager.getInstance().closeOpenInstance();
                        mDatas.remove(item);
                        mDeletePresenter.deleteGroup(IOUtils.getToken(getContext()),item.friendGroupId,helper.getPosition());
                    }
                });
            }
        };
        // 侧滑打来的时候滑动没有想到什么好的办法解决，只能这样了。
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // 如果listView跟随手机拖动，关闭已经打开的SwipeLayout
                    SwipeLayoutManager.getInstance().closeOpenInstance();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    public void moveSuccess(AddCustomerModel list,int position) {
        CommonUtils.showToast(mContext,"删除成功");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void moveFail(String msg) {
        CommonUtils.showToast(mContext,msg);
    }
}
