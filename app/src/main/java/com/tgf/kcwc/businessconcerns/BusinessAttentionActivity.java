package com.tgf.kcwc.businessconcerns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.BusinessAdapter;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.FriendGroupingModel;
import com.tgf.kcwc.mvp.model.FriendListModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.BusinessAttentionPresenter;
import com.tgf.kcwc.mvp.view.BusinessAttentionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/8/4 10:00
 * 商务关注首页
 */

public class BusinessAttentionActivity extends BaseActivity implements BusinessAttentionView {
    private ImageView searchIv;
    private RecyclerView groupNameRv;
    private ListView mListView;
    //空数据时显示
    private RelativeLayout emptyLl;
    private WrapAdapter mAdapter;
    private BusinessAdapter mRecyclerAdapter;
    private AdapterView.OnItemClickListener mItemListener;
    private BusinessAdapter.OnItemsClickListener mRecyclerListener;
    //popwindow集合数据
    List<MorePopupwindowBean> popList = new ArrayList<>();
    ArrayList<FriendGroupingModel.ListItem> groupingList = new ArrayList<>();
    ArrayList<FriendListModel.ListItem> friendList = new ArrayList<>();
    private BusinessAttentionPresenter mPresenter;
    private int page = 1;
    //是否刷新
    private boolean isRefresh;
    private int friendGroupId;
    //选中的位置
    public int positions;

    @Override
    protected void setUpViews() {
        searchIv = (ImageView) findViewById(R.id.attention_search);
        groupNameRv = (RecyclerView) findViewById(R.id.attention_group_name);
        mListView = (ListView) findViewById(R.id.attention_lv);
        emptyLl = (RelativeLayout) findViewById(R.id.empty_layout);
        initRefreshLayout(mBGDelegate);
        groupNameRv.setHasFixedSize(true);
        groupNameRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPresenter = new BusinessAttentionPresenter();
        mPresenter.attachView(this);
        //获取好友分组列表
        mPresenter.getGroupingList(IOUtils.getToken(this));
        //获取好友列表
        mPresenter.getFriendAllList(IOUtils.getToken(this),page);
        if (mRecyclerAdapter != null) {
            groupNameRv.setAdapter(mRecyclerAdapter);
            mRecyclerAdapter.setOnItemsClickListener(mRecyclerListener);
        }
        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mItemListener);
            mRecyclerAdapter.notifyDataSetChanged();
        }
        initPop();//获取popwindow选项
        searchIv.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                // TODO: 2017/8/7 跳转搜索
                CommonUtils.startNewActivity(mContext, FriendSeekActivity.class);
            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
        function.setImageResource(R.drawable.icon_common_right);
        function.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                MorePopupWindow morePopupWindow = new MorePopupWindow(BusinessAttentionActivity.this,
                        popList, new MorePopupWindow.MoreOnClickListener() {
                    @Override
                    public void moreOnClickListener(int position, MorePopupwindowBean item) {
                        switch (position) {
                            case 0://首页
                                finish();
                                break;
                            case 1://消息
                                break;
                            case 2://扫一扫
                                break;
                            case 3://添加用户
                                CommonUtils.startNewActivity(BusinessAttentionActivity.this, AddCustomerActivity.class);
                                break;
                            case 4://好友分组
                                CommonUtils.startNewActivity(
                                        BusinessAttentionActivity.this, FriendGroupingActivity.class);
                                break;
                            case 5://分组管理
                                CommonUtils.startNewActivity(BusinessAttentionActivity.this, ManageGroupActivity.class);
                                break;
                        }
                    }
                });
                morePopupWindow.showPopupWindow(function);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_attention);
    }

    /**
     * 初始化popwindow选项
     */
    public void initPop() {
        String[] popValues = mRes.getStringArray(R.array.right_pop_values);
        for (String value : popValues) {
            MorePopupwindowBean morePopupwindowBean = null;
            morePopupwindowBean = new MorePopupwindowBean();
            morePopupwindowBean.title = value;
            popList.add(morePopupwindowBean);
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingIndicator(true);
        } else {
            stopRefreshAll();
            showLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showGrouping(ArrayList<FriendGroupingModel.ListItem> list) {
        groupingList.addAll(list);
        if (mRecyclerAdapter != null) {
            mRecyclerAdapter.notifyDataSetChanged();
        } else {
            initRecyclerAdapter();
            groupNameRv.setAdapter(mRecyclerAdapter);
            mRecyclerAdapter.setOnItemsClickListener(mRecyclerListener);
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showFriendList(ArrayList<FriendListModel.ListItem> list) {
        mListView.setVisibility(View.VISIBLE);
        emptyLl.setVisibility(View.GONE);
        if (page == 1){
            friendList.clear();
        }
        if (list.size() != 0 && list != null) {
            friendList.addAll(list);
        }
        if (friendList.size() == 0 && friendList == null){
            mListView.setVisibility(View.GONE);
            emptyLl.setVisibility(View.VISIBLE);
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        } else {
            initAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mItemListener);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void initRecyclerAdapter() {
        mRecyclerListener = new BusinessAdapter.OnItemsClickListener() {

            @Override
            public void onItemClick(int position) {
                mListView.setVisibility(View.VISIBLE);
                emptyLl.setVisibility(View.GONE);
                positions = position;
                if (position == 0){//获取所有好友列表
                    mPresenter.getFriendAllList(IOUtils.getToken(getContext()),page);
                }else {//获取分组好友列表
                    friendGroupId = groupingList.get(position).friendGroupId;
                    mPresenter.getGroupingFriendList(IOUtils.getToken(getContext()), friendGroupId,page);
                }
            }
        };
        mRecyclerAdapter = new BusinessAdapter(mContext,groupingList);
    }

    public void initAdapter() {
        mItemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: 2017/8/7 跳转详情
                int friendId = friendList.get(i).id;//好友id
                Intent intent = new Intent(mContext,CustomerDetailActivity.class);
                intent.putExtra(Constants.IntentParams.DATA,friendId);
                startActivity(intent);

            }
        };
        mAdapter = new WrapAdapter<FriendListModel.ListItem>(this, friendList, R.layout.friend_list_item) {
            @Override
            public void convert(ViewHolder helper, final FriendListModel.ListItem item) {
                //缺性别
                String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                SimpleDraweeView avatar = helper.getView(R.id.tagHeaderImg);
                avatar.setImageURI(avatarUrl);
                helper.setText(R.id.item_nickname, item.name);
                helper.setText(R.id.item_source, item.source);
                ImageView imageView = helper.getView(R.id.item_relation);
                imageView.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    protected void onSingleClick(View view) {
                        if (item.isAllot == 0) {

                        } else if (item.isAllot == 1) {

                        }
                    }
                });
            }
        };
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
        if (positions != 0){
            mPresenter.getGroupingFriendList(IOUtils.getToken(getContext()),friendGroupId,page);
        }else {
            mPresenter.getFriendAllList(IOUtils.getToken(getContext()),page);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }
}
