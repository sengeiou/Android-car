package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.mvp.model.TripBookThreadModel;
import com.tgf.kcwc.mvp.presenter.TripBookThreadPresenter;
import com.tgf.kcwc.mvp.view.TripbookThreadView;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.see.evaluate.PopmanESDetailActitivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

public class TripBookThreadActivity extends BaseActivity implements TripbookThreadView {

    private ListView threadLv;
    private TripBookThreadPresenter tripBookThreadPresenter;
    private boolean isRefresh;
    private int mBookId = 85;
    private List<TripBookThreadModel.Thread> mThreadList = new ArrayList<>();
    private WrapAdapter<TripBookThreadModel.Thread> threadWrapAdapter;
    private String title;

    @Override
    protected void setUpViews() {
        initRefreshLayout(bgDelegate);
        mPageSize = 20;
        mBookId = getIntent().getIntExtra(Constants.IntentParams.ID, 0);

        threadLv = (ListView) findViewById(R.id.tripbook_couponlv);
        threadLv.addFooterView(View.inflate(this, R.layout.no_more_data_layout, null));
        tripBookThreadPresenter = new TripBookThreadPresenter();
        tripBookThreadPresenter.attachView(this);
        tripBookThreadPresenter.getTripbookThreadList(mBookId, mPageIndex, mPageSize);
        threadLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < mThreadList.size()) {
//                    Map<String, Serializable> args = new HashMap<String, Serializable>();
//                    args.put(Constants.IntentParams.ID, mThreadList.get(position).id + "");
                    select(mThreadList.get(position));
//                    CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                }
            }

            public void select(TripBookThreadModel.Thread item) {
                Map<String, Serializable> args = null;
                if (item.model.equals("goods")) { //车主自售
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, item.id);
                    CommonUtils.startNewActivity(mContext, args, SaleDetailActivity.class);
                } else if (item.model.equals("play")) { //请你玩
                    args = new HashMap<String, Serializable>();
                    args.put("id", item.id + "");
                    CommonUtils.startNewActivity(mContext, args, PleasePlayDetailsActivity.class);
                } else if (item.model.equals("cycle")) { //开车去
                    args = new HashMap<String, Serializable>();
                    args.put("id", item.id + "");
                    CommonUtils.startNewActivity(mContext, args, DrivingDetailsActivity.class);
                } else if (item.model.equals("words")) {//普通帖子
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, item.id + "");
                    CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                } else if (item.model.equals("evaluate")) { //达人评测
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, item.id);
                    CommonUtils.startNewActivity(mContext, args, PopmanESDetailActitivity.class);
                } else if (item.model.equals("roadbook")) {//路书
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, item.id);
                    CommonUtils.startNewActivity(mContext, args, TripbookDetailActivity.class);
                } else {
                    CommonUtils.showToast(mContext, "正在开发中");
                }
            }
        });

    }

    private BGARefreshLayout.BGARefreshLayoutDelegate bgDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {


        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            isRefresh = true;
            mPageIndex = 0;
//            if(mExhibitEventList !=null){
//                mExhibitEventList.clear();
//            }
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            loadMore();
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {
        mPageIndex++;
        tripBookThreadPresenter.getTripbookThreadList(mBookId, mPageIndex, mPageSize);
    }

    private void initAdapter() {
        threadWrapAdapter = new WrapAdapter<TripBookThreadModel.Thread>(TripBookThreadActivity.this, R.layout.listitem_tripbook_thread, mThreadList) {

            @Override
            public void convert(ViewHolder helper, TripBookThreadModel.Thread item) {

                SimpleDraweeView thumbnail = helper.getView(R.id.thumbnail);
                helper.setText(R.id.driving_list_view_dynamic, item.title);
                helper.setText(R.id.look, item.viewCount + "");
                ImageView jiaIv = helper.getView(R.id.thumbnailjinh);
                if (item.isDigest == 1) {
                    jiaIv.setVisibility(View.VISIBLE);
                } else {
                    jiaIv.setVisibility(View.INVISIBLE);
                }
                helper.setText(R.id.like, item.likeCount + "");
                helper.setText(R.id.information, item.replyCount + "");
                thumbnail.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 360, 360)));
                helper.setText(R.id.time, DateFormatUtil.timeLogic(item.createTime));
            }
        };
        threadLv.setAdapter(threadWrapAdapter);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        View backBtn = findViewById(R.id.tripthread_back);
        TextView titleTextView = (TextView) findViewById(R.id.tripthread__title);
        backEvent(backBtn);
        title = getIntent().getStringExtra(Constants.IntentParams.TITLE);
        titleTextView.setText(title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripbookthread);
    }

    @Override
    public void showThreadList(List<TripBookThreadModel.Thread> threadList) {

        if (isRefresh) {
            mThreadList.clear();
            isRefresh =false;
        }
        mThreadList.addAll(threadList);
        if (threadWrapAdapter == null) {
            initAdapter();
        } else {
            threadWrapAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingDialog();
        } else {
            stopRefreshAll();
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
