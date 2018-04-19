package com.tgf.kcwc.play.havefun;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.driv.ListviewHint;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.mvp.model.HaveFunBean;
import com.tgf.kcwc.mvp.model.PlayListBean;
import com.tgf.kcwc.mvp.model.TopicBean;
import com.tgf.kcwc.mvp.presenter.HaveFunListPresenter;
import com.tgf.kcwc.mvp.presenter.TopicListPresenter;
import com.tgf.kcwc.mvp.view.HaveFunListView;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/5/17.
 */

public class NewestListFragment extends BaseFragment implements HaveFunListView {

    private HaveFunListPresenter mHaveFunListPresenter;
    private ListView mListView;
    private WrapAdapter<HaveFunBean.DataList> mAdapter;
    public List<HaveFunBean.DataList> dataList = new ArrayList<>();
    public String type = "new";
    private int page = 1;
    private boolean isRefresh = true;
    private ListviewHint mListviewHint;                         //尾部

    @Override
    protected void updateData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHaveFunListPresenter != null) {
            mHaveFunListPresenter.detachView();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.newesthavefunfragment;
    }

    @Override
    protected void initView() {
        mListView = findView(R.id.list);
        mHaveFunListPresenter = new HaveFunListPresenter();
        mHaveFunListPresenter.attachView(this);
        initRefreshLayout(mBGDelegate);

        mAdapter = new WrapAdapter<HaveFunBean.DataList>(mContext, R.layout.havefunlist_item,
                dataList) {
            TextView friend;
            TextView topic;
            TextView group;

            @Override
            public void convert(ViewHolder helper, final HaveFunBean.DataList item) {
                int position = helper.getPosition();

                SimpleDraweeView thumbnail = helper.getView(R.id.thumbnail);
                SimpleDraweeView bigimage = helper.getView(R.id.bigimage);
                TextView mTitle = helper.getView(R.id.driving_list_view_dynamic);
                TextView mLook = helper.getView(R.id.look);
                TextView mLike = helper.getView(R.id.like);
                TextView mInformation = helper.getView(R.id.information);
                TextView time = helper.getView(R.id.time);
                RelativeLayout biglayout = helper.getView(R.id.biglayout);
                RelativeLayout thumbnaillayout = helper.getView(R.id.thumbnaillayout);
                ImageView bigjinh = helper.getView(R.id.bigjinh);
                ImageView thumbnailjinh = helper.getView(R.id.thumbnailjinh);
                LinearLayout itemselect = helper.getView(R.id.itemselect);

                friend = helper.getView(R.id.friend);
                topic = helper.getView(R.id.topic);
                group = helper.getView(R.id.group);

                mTitle.setText(item.title);
                mLook.setText(item.viewCount + "");
                mLike.setText(item.diggCount + "");
                mInformation.setText(item.replyCount + "");
                time.setText(DateFormatUtil.timeLogic(item.createTime));

                if (position % 3 == 0 && position != 0) {

                    thumbnaillayout.setVisibility(View.GONE);
                    biglayout.setVisibility(View.VISIBLE);
                    bigimage.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));//item图片
                    if (item.isDigest == 1) {
                        bigjinh.setVisibility(View.VISIBLE);
                    } else {
                        bigjinh.setVisibility(View.GONE);
                    }
                } else {
                    thumbnaillayout.setVisibility(View.VISIBLE);
                    biglayout.setVisibility(View.GONE);

                    thumbnail.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 144, 144)));//item图片
                    if (item.isDigest == 1) {
                        thumbnailjinh.setVisibility(View.VISIBLE);
                    } else {
                        thumbnailjinh.setVisibility(View.GONE);
                    }
                }
                itemselect.setOnClickListener(new View.OnClickListener() {
                    Map<String, Serializable> args = null;

                    @Override
                    public void onClick(View v) {
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
                            args.put(Constants.IntentParams.ID, item.id + "");
                            CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
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

        };
        mListView.setAdapter(mAdapter);
        mHaveFunListPresenter.GetDiges(IOUtils.getToken(mContext), type, page);
    }

    @Override
    public void dataListSucceed(HaveFunBean haveFunBean) {
        stopRefreshAll();
        mListView.removeFooterView(mListviewHint);
        if (page == 1) {
            dataList.clear();
        } else {
            if (haveFunBean.data.list == null || haveFunBean.data.list.size() == 0) {
                mListviewHint = new ListviewHint(mContext);
                mListView.addFooterView(mListviewHint);
            }
        }
        if (haveFunBean.data.list == null || haveFunBean.data.list.size() == 0) {
            isRefresh = false;
        }
        dataList.addAll(haveFunBean.data.list);
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
        mHaveFunListPresenter.GetDiges(IOUtils.getToken(mContext), type, page);
    }
}
