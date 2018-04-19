package com.tgf.kcwc.fragments;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.HeaderBannerWebActivity;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.driv.ListviewHint;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.PlayListBean;
import com.tgf.kcwc.mvp.model.PlayTopicBean;
import com.tgf.kcwc.mvp.presenter.PlayActivityPresenter;
import com.tgf.kcwc.mvp.view.PlayDataView;
import com.tgf.kcwc.play.topic.TopicActivity;
import com.tgf.kcwc.play.topic.TopicDetailsActivity;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.see.evaluate.PopmanESDetailActitivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.MultiFullImageView;
import com.tgf.kcwc.view.MyListView;
import com.tgf.kcwc.view.VacancyListView;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Author:Jenny
 * Date:16/4/23 22:16
 * E-mail:fishloveqin@gmail.com
 */
public class TabNewPlayFragment extends BaseFragment implements PlayDataView {
    private TabPlayHomeFragment tabPlayHomeFragment;
    private PlayActivityPresenter mPlayActivityPresenter;
    private MainActivity activity;
    private int page = 1;
    private boolean isRefresh = true;
    private MyListView mListView;
    private ListviewHint mListviewHint;                //尾部
    private WrapAdapter<PlayListBean.DataList> mAdapter;
    private List<PlayListBean.DataList> DataList = new ArrayList<>();
    private RecyclerView mRecyclerView;//话题
    private CommonAdapter<PlayTopicBean> mTopic;
    private List<PlayTopicBean> mTopicList = new ArrayList<>();
    private Banner drivingBanner;//轮播图

    private LinearLayout topiclayout;
    private ScrollView scrollView;
    private BGARefreshLayout mBGARefreshLayout;

    public TabNewPlayFragment(TabPlayHomeFragment tabPlayHomeFragment) {
        this.tabPlayHomeFragment = tabPlayHomeFragment;
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.play_newfragment;
    }

    @Override
    protected void initView() {
        mPlayActivityPresenter = new PlayActivityPresenter();
        mPlayActivityPresenter.attachView(this);
        initRefreshLayout(mBGDelegate);

        mListView = findView(R.id.listview);
        drivingBanner = findView(R.id.driving_banner);
        mRecyclerView = findView(R.id.recyclerview);
        topiclayout = findView(R.id.topiclayout);
        scrollView = findView(R.id.scrolview);
        mBGARefreshLayout = findView(R.id.rl_modulename_refresh);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        setDataList();
        topiclayout.setOnClickListener(this);
        mPlayActivityPresenter.getBannerData(IOUtils.getToken(mContext));
        //isFirst++;+
        mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext), "", "", page);

        mPlayActivityPresenter.getShowTopic();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.topiclayout://话题
                CommonUtils.startNewActivity(mContext, TopicActivity.class);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setDataList() {

        mTopic = new CommonAdapter<PlayTopicBean>(mContext, R.layout.play_topicitem, mTopicList) {
            @Override
            public void convert(ViewHolder helper, final PlayTopicBean item) {
                RelativeLayout selectlayout = helper.getView(R.id.selectlayout);
                SimpleDraweeView mSimpleDraweeView = helper.getView(R.id.bigimage);
                TextView title = helper.getView(R.id.title);
                TextView participation = helper.getView(R.id.participation);
                RoundingParams roundingParams = new RoundingParams();
                roundingParams.setCornersRadii(10, 10, 10, 10);
                GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(mContext.getResources());
                GenericDraweeHierarchy hierarchy = builder.build();
                hierarchy.setRoundingParams(roundingParams);
                mSimpleDraweeView.setHierarchy(hierarchy);
                mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));//item图片
                title.setText(item.title);
                participation.setText(item.fansNum + "人参与");
                selectlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put("id", item.id + "");
                        CommonUtils.startNewActivity(mContext, args, TopicDetailsActivity.class);
                    }
                });

            }
        };
        mRecyclerView.setAdapter(mTopic);

        mAdapter = new WrapAdapter<PlayListBean.DataList>(mContext, R.layout.update_item, DataList) {
            TextView friend;
            TextView topic;
            TextView group;
            VacancyListView mHintLayout;

            @Override
            public void convert(ViewHolder helper, final PlayListBean.DataList item) {
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
                LinearLayout attention = helper.getView(R.id.attention);
                LinearLayout itemselect = helper.getView(R.id.itemselect);
                MultiFullImageView multiImagView = helper.getView(R.id.multiImagView);
                mHintLayout = helper.getView(R.id.hintlayout);

                friend = helper.getView(R.id.friend);
                topic = helper.getView(R.id.topic);
                group = helper.getView(R.id.group);
                attention.setVisibility(View.GONE);
                mHintLayout.setVisibility(View.GONE);

                mTitle.setText(item.title);
                mLook.setText(item.viewCount + "");
                mLike.setText(item.diggCount + "");
                mInformation.setText(item.replyCount + "");
                if(!TextUtils.isEmpty(item.createTime)){
                    time.setText(DateFormatUtil.timeLogi(item.createTime));
                }else {
                    time.setText("");
                }

                if (item.viewType == 1) {
                    thumbnaillayout.setVisibility(View.GONE);
                    biglayout.setVisibility(View.VISIBLE);
                    multiImagView.setVisibility(View.GONE);
                    bigimage.setImageURI(
                            Uri.parse(URLUtil.builderImgUrl(item.cover.get(0), 540, 270)));//item图片
                    if (item.isDigest == 1) {
                        bigjinh.setVisibility(View.VISIBLE);
                    } else {
                        bigjinh.setVisibility(View.GONE);
                    }
                } else if (item.viewType == 2) {
                    thumbnaillayout.setVisibility(View.VISIBLE);
                    biglayout.setVisibility(View.GONE);
                    multiImagView.setVisibility(View.GONE);
                    thumbnail.setImageURI(
                            Uri.parse(URLUtil.builderImgUrl(item.cover.get(0), 270, 203)));//item图片
                    if (item.isDigest == 1) {
                        thumbnailjinh.setVisibility(View.VISIBLE);
                    } else {
                        thumbnailjinh.setVisibility(View.GONE);
                    }
                } else if (item.viewType == 3) {
                    multiImagView.setVisibility(View.VISIBLE);
                    thumbnaillayout.setVisibility(View.GONE);
                    biglayout.setVisibility(View.GONE);
                    multiImagView.setList(item.getImglist());

                    multiImagView
                            .setOnItemClickListener(new MultiFullImageView.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    select(item);
                                }
                            });

                }
                itemselect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select(item);
                    }
                });

            }

            public void select(final PlayListBean.DataList item) {
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
        };
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void dataBannerSucceed(BannerModel bannerModel) {
        final List<BannerModel.Data> datas = bannerModel.data;
        List<String> imgUrl = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            imgUrl.add(URLUtil.builderImgUrl(datas.get(i).image, 540, 270));
        }
        drivingBanner.setImages(imgUrl).setImageLoader(new FrescoImageLoader()).setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                if (datas.get(position - 1).type == 2) {
                    if (!TextUtils.isEmpty(datas.get(position - 1).url)) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, datas.get(position - 1).url);
                        args.put(Constants.IntentParams.ID2, datas.get(position - 1).title);
                        CommonUtils.startNewActivity(mContext, args,
                                HeaderBannerWebActivity.class);
                    }
                } else {

                }
            }
        }).start();
    }

    @Override
    public void dataBannerDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
        if (page == 1) {
            if (DataList == null || DataList.size() == 0) {
                PlayListBean.DataList dataList = new PlayListBean.DataList();
                dataList.id = -1;
                DataList.add(dataList);
                setDataList();
            }
        }
    }

    @Override
    public void dataListfeated(PlayListBean playListBean) {
        stopRefreshAll();
        if (page == 1) {
            DataList.clear();
            DataList.addAll(playListBean.data.list);

            if (DataList == null || DataList.size() == 0) {
                isRefresh = false;
            }
            setDataList();
        } else {
            DataList.addAll(playListBean.data.list);
            mAdapter.notifyDataSetChanged();
        }
        if (playListBean.data.list != null) {
            if (playListBean.data.list.size() != 0) {
                isRefresh = true;
            } else {
                isRefresh = false;
            }
        } else {
            isRefresh = false;
        }

    }

    @Override
    public void dataTopicSucceed(List<PlayTopicBean> playListBean) {
        mTopicList.addAll(playListBean);
        mTopic.notifyDataSetChanged();
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
        mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext), "", "", page);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayActivityPresenter != null) {
            mPlayActivityPresenter.detachView();
        }
    }
}
