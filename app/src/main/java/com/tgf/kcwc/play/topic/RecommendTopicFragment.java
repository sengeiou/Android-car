package com.tgf.kcwc.play.topic;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.driv.ListviewHint;
import com.tgf.kcwc.mvp.model.TopicBean;
import com.tgf.kcwc.mvp.presenter.TopicListPresenter;
import com.tgf.kcwc.mvp.view.TopicListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/5/13.
 */

public class RecommendTopicFragment extends BaseFragment implements TopicListView {
    private TopicListPresenter mTopicListPresenter;
    private ListView mListView;
    private WrapAdapter<TopicBean.DataList> mAdapter;
    public List<TopicBean.DataList> dataList = new ArrayList<>();

    public String type = "recommend";
    private int page = 1;
    private boolean isRefresh = true;

    private ListviewHint mListviewHint;                         //尾部

    @Override
    protected void updateData() {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTopicListPresenter!=null){
            mTopicListPresenter.detachView();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.recommendtopicfragment;
    }

    @Override
    protected void initView() {

        mListView = findView(R.id.list);
        mTopicListPresenter = new TopicListPresenter();
        mTopicListPresenter.attachView(this);
        initRefreshLayout(mBGDelegate);
        mListviewHint = new ListviewHint(mContext);
        isRefresh = true;
        page = 1;
        mListView.removeFooterView(mListviewHint);
        mAdapter = new WrapAdapter<TopicBean.DataList>(mContext, R.layout.topic_item, dataList) {
            @Override
            public void convert(ViewHolder helper, TopicBean.DataList item) {
                SimpleDraweeView cover = helper.getView(R.id.cover);
                TextView title = helper.getView(R.id.title);
                TextView intro = helper.getView(R.id.intro);
                TextView threadNum = helper.getView(R.id.thread_num);
                TextView fansNum = helper.getView(R.id.fans_num);

                cover.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));//item图片
                title.setText(item.title);
                intro.setText(item.intro);
                threadNum.setText(item.threadNum + "人发帖");
                fansNum.setText(item.fansNum + "人关注");
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put("id", dataList.get(position).id + "");
                CommonUtils.startNewActivity(mContext, args, TopicDetailsActivity.class);
            }
        });
        mTopicListPresenter.getTopicList(IOUtils.getToken(mContext), type, page);
    }

    @Override
    public void dataListSucceed(TopicBean topicBean) {
        stopRefreshAll();
        if (page == 1) {
            dataList.clear();
            if (topicBean.data.list == null || topicBean.data.list.size() == 0) {
                isRefresh = false;
            }
        } else {
            if (topicBean.data.list == null || topicBean.data.list.size() == 0) {
                isRefresh = false;
                //CommonUtils.showToast(mContext, "暂时没有更多数据");
                mListView.addFooterView(mListviewHint);
            }
        }
        dataList.addAll(topicBean.data.list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
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
            mListView.removeFooterView(mListviewHint);
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
        mTopicListPresenter.getTopicList(IOUtils.getToken(mContext), type, page);
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        isRefresh = true;
        mListView.removeFooterView(mListviewHint);
        loadMore();
    }

}
