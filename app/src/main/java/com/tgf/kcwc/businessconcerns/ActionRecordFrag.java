package com.tgf.kcwc.businessconcerns;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.mvp.model.ActionRecordModel;
import com.tgf.kcwc.mvp.presenter.ActionRecordPresenter;
import com.tgf.kcwc.mvp.view.ActionRecordView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;

import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/8/3 8:53
 * 行为记录
 */

public class ActionRecordFrag extends BaseFragment implements ActionRecordView{

    private ListView mListView;
    private WrapAdapter mAdapter;
    private ArrayList<ActionRecordModel.ListItem> mList = new ArrayList<>();

    private ActionRecordPresenter mPresenter;

    //当前页数
    private int page = 1;
    //总页数
    private int count;
    //是否刷新
    private boolean isRefresh;
    //好友id
    private int friendId;

    public ActionRecordFrag(int friendId) {
        super();
        this.friendId = friendId;
    }

    @Override
    protected void updateData() {
//        mPresenter = new ActionRecordPresenter();
//        mPresenter.attachView(this);
//        mPresenter.getActionRecord(Constants.SALE_TOKEN,friendId,page);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_playrecomend;
    }

    @Override
    protected void initView() {
        mListView = findView(R.id.playrecoment_lv);
        setUserVisibleHint(true);
        initRefreshLayout(mRefreshDelegate);

        if (null != mAdapter){
            mListView.setAdapter(mAdapter);
        }

    }

    @Override
    protected void initData() {
//        super.initData();
        mPresenter = new ActionRecordPresenter();
        mPresenter.attachView(this);
        mPresenter.getActionRecord(IOUtils.getToken(getContext()),friendId,page);
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

    public void initAdapter(){
        mAdapter = new WrapAdapter<ActionRecordModel.ListItem>(mContext,mList,R.layout.action_record_item) {
            @Override
            public void convert(ViewHolder helper, ActionRecordModel.ListItem item) {
//                LinearLayout recordItem = helper.getView(R.id.action_record_item_all);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                if (helper.getPosition() == 0){
//                    params.topMargin = 40;
//                    recordItem.setLayoutParams(params);
//                }
                TextView keywords = helper.getView(R.id.item_action_plus);
                helper.setText(R.id.item_action,item.type);
                if (StringUtils.checkNull(item.keywords)){
                    keywords.setText(item.keywords);
                }
                helper.setText(R.id.item_time,item.createTime);
            }

        };
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mRefreshDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {


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
        if (page <= count){
            mPresenter.getActionRecord(IOUtils.getToken(getContext()),friendId,page);
        }else {
            // TODO: 2017/8/18
        }
    }

    @Override
    public void showActionRecord(ActionRecordModel listItem) {
        count = listItem.pagination.count;
        if (page == 1){
            mList.clear();
        }
        mList.addAll(listItem.list);
        if (null != mAdapter){
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }else {
            initAdapter();
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
