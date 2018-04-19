package com.tgf.kcwc.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.util.DensityUtil;
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
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.AttentionListActivity;
import com.tgf.kcwc.me.HobbyTagMgrActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.PlayListBean;
import com.tgf.kcwc.mvp.model.PlayTopicBean;
import com.tgf.kcwc.mvp.model.PlayTypeBean;
import com.tgf.kcwc.mvp.presenter.PlayActivityPresenter;
import com.tgf.kcwc.mvp.view.PlayDataView;
import com.tgf.kcwc.play.topic.FoundTopicActivity;
import com.tgf.kcwc.play.topic.TopicActivity;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.see.evaluate.PopmanESDetailActitivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.MultiFullImageView;
import com.tgf.kcwc.view.SmoothListView.SmoothListView;
import com.tgf.kcwc.view.VacancyListView;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;
import com.tgf.kcwc.view.link.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:16/4/23 22:16
 * E-mail:fishloveqin@gmail.com
 */
public class TabPlayFragment extends BaseFragment
        implements PlayDataView, SmoothListView.ISmoothListViewListener {
    private TabPlayHomeFragment tabPlayHomeFragment;
    private PlayActivityPresenter mPlayActivityPresenter;
    private LinearLayout titleLayout;                                    //标题栏
    private TextView mFilterTitle;
    private ArrayList<CarBean> mDatas = new ArrayList<CarBean>();
    private KPlayCarApp kPlayCarApp;
    private final int request_code_sercity = 100;
    private int isattention = 0;                       //是否点击关注
    private int isattentiontext = 0;                       //关注按钮记录
    private SmoothListView mSmoothListview;
    private WrapAdapter<PlayListBean.DataList> mAdapter;
    private List<PlayListBean.DataList> DataList = new ArrayList<>();
    private int page = 1;
    private String check = "";                      //group群组 topic话题 friend好友
    private RecyclerView mRecyclerView;

    private RecyclerView mRecyclerViewTop;

    private CommonAdapter<PlayTypeBean> mType;                                          //顶部
    private CommonAdapter<PlayTypeBean> mTypeTop;                                       //头部
    private List<PlayTypeBean> mTypeList = new ArrayList<>();

    private View TypeView;                                       //头部type
    private View AttentionView;                                  //关注特有

    private View itemHeaderFilterView; // 从ListView获取的筛选子View
    private int filterViewPosition = 3; // 筛选视图的位置

    private boolean isScrollIdle = true;                    // ListView是否在滑动
    private float filterViewTopMargin;                            // 筛选视图距离顶部的距离
    private float titleLayoutHeigh = 48;                      // 广告视图的高度
    PlayTypeBean playTypeBean = null;
    PlayListBean mPlayListBean = null;
    private TextView mSeek;
    // private int isFirst = 1;
    private MainActivity activity;
    TabPlayHomeFragment parentFragment;

    public void setTypeDate() {
        mTypeList.clear();
        setTypeBean("每日更新", "index", 0, true);
        setTypeBean("热图", "hotPic", 1, false);
        setTypeBean("红人", "hotMan", 2, false);
        setTypeBean("关注", "attention", 3, false);
        setTypeBean("兴趣", "interest", 4, false);
        //setTypeBean("爱车", "loveCar", 5, false);

    }

    public TabPlayFragment(TabPlayHomeFragment tabPlayHomeFragment) {
        this.tabPlayHomeFragment = tabPlayHomeFragment;
    }

    public void setTypeBean(String name, String type, int num, boolean isClick) {
        PlayTypeBean playTypeBean = null;
        playTypeBean = new PlayTypeBean();
        playTypeBean.name = name;
        playTypeBean.type = type;
        playTypeBean.isClick = isClick;
        playTypeBean.number = num;
        mTypeList.add(playTypeBean);
    }

    @Override
    protected void updateData() {
        if (kPlayCarApp.locCityName != null) {
            mFilterTitle.setText(kPlayCarApp.locCityName);
        }
        if (activity == null) {
            activity = (MainActivity) getActivity();
            kPlayCarApp = (KPlayCarApp) activity.getApplication();
        }
        // parentFragment = (TabPlayHomeFragment)
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragm : fragments) {
            Log.i("TT", fragm.getTag());
        }
        if (parentFragment == null) {
            parentFragment = (TabPlayHomeFragment) getParentFragment();
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.play_fragment;
    }

    @Override
    protected void initView() {
        setTypeDate();
        playTypeBean = mTypeList.get(0);
        kPlayCarApp = (KPlayCarApp) getContext().getApplicationContext();
        mPlayActivityPresenter = new PlayActivityPresenter();
        mPlayActivityPresenter.attachView(this);
        mFilterTitle = findView(R.id.filterTitle);
        mRecyclerView = findView(R.id.recyclerview);
        mRecyclerViewTop = findView(R.id.recyclerviewtop);
        mSmoothListview = findView(R.id.listview);
        titleLayout = findView(R.id.title_layout);
        mSeek = findView(R.id.seek);
        findView(R.id.filterLayout).setOnClickListener(this);
        mSeek.setOnClickListener(this);
        mSmoothListview.setRefreshEnable(true);
        mSmoothListview.setLoadMoreEnable(false);
        mSmoothListview.setSmoothListViewListener(this);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mSmoothListview.setOnScrollListener(new SmoothListView.OnSmoothScrollListener() {
            @Override
            public void onSmoothScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrollIdle = (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (isScrollIdle && filterViewTopMargin < 0)
                    return;

                titleLayoutHeigh = DensityUtils.px2dp(mContext, titleLayout.getHeight()); //标题栏高度

                // 获取筛选View、距离顶部的高度
                if (itemHeaderFilterView == null) {
                    itemHeaderFilterView = mSmoothListview.getChildAt(filterViewPosition - firstVisibleItem);
                }
                if (itemHeaderFilterView != null) {
                    filterViewTopMargin = DensityUtils.px2dp(mContext, itemHeaderFilterView.getTop())+titleLayoutHeigh;
                }

                // 处理筛选是否吸附在顶部
                if (filterViewTopMargin <= titleLayoutHeigh || firstVisibleItem > filterViewPosition) {
                    //  isStickyTop = true; // 吸附在顶部
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    // isStickyTop = false; // 没有吸附在顶部
                    mRecyclerView.setVisibility(View.GONE);
                }

/*                if (TypeView != null) {
                    filterViewTopMargin = DensityUtils.px2dp(mContext, TypeView.getTop());
                }


                // 处理筛选是否吸附在顶部
                if (filterViewTopMargin <= 0) {
                    //  isStickyTop = true; // 吸附在顶部
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    // isStickyTop = false; // 没有吸附在顶部
                    mRecyclerView.setVisibility(View.GONE);
                }*/
                mSmoothListview.setVisibility(View.VISIBLE);
            }
        });

        mType = new CommonAdapter<PlayTypeBean>(mContext, R.layout.typeselect_item, mTypeList) {
            @Override
            public void convert(ViewHolder helper, PlayTypeBean item) {
                final int position = helper.getPosition();
                LinearLayout selectlayout = helper.getView(R.id.selectlayout);
                TextView name = helper.getView(R.id.name);
                TextView select = helper.getView(R.id.select);
                name.setText(item.name);

                if (item.name.length() == 4) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            DensityUtils.dp2px(mContext, 80), DensityUtils.dp2px(mContext, 50));
                    layoutParams.leftMargin = DensityUtils.dp2px(mContext, 8);
                    selectlayout.setLayoutParams(layoutParams);
                } else if (item.name.length() == 2) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            DensityUtils.dp2px(mContext, 60), DensityUtils.dp2px(mContext, 50));
                    layoutParams.leftMargin = DensityUtils.dp2px(mContext, 8);
                    selectlayout.setLayoutParams(layoutParams);
                }
                if (item.isClick) {
                    select.setVisibility(View.VISIBLE);
                } else {
                    select.setVisibility(View.GONE);
                }

                selectlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTypeList.get(position).isClick) {

                        } else {
                            if (mTypeList.get(position).name.equals("关注")) {
                                if (IOUtils.isLogin(mContext)) {
                                    check = "friend";
                                    isattention = 1;
                                    isattentiontext = 0;
                                    mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                            mTypeList.get(position).type, check, page);
                                } else {
                                    return;
                                }

                            } else {
                                if (mTypeList.get(position).name.equals("兴趣")
                                        || mTypeList.get(position).name.equals("爱车")) {
                                    if (IOUtils.isLogin(mContext)) {
                                        check = "";
                                        isattention = 0;
                                        isattentiontext = 0;
                                        mPlayActivityPresenter.getDataLists(
                                                IOUtils.getToken(mContext),
                                                mTypeList.get(position).type, check, page);
                                    } else {
                                        return;
                                    }
                                } else {
                                    check = "";
                                    isattention = 0;
                                    isattentiontext = 0;
                                    mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                            mTypeList.get(position).type, check, page);
                                }
                            }
                        }
                        for (PlayTypeBean bean : mTypeList) {
                            bean.isClick = false;
                        }
                        mTypeList.get(position).isClick = true;
                        playTypeBean = mTypeList.get(position);
                        mType.notifyDataSetChanged();
                        mTypeTop.notifyDataSetChanged();
                        page = 1;

                    }
                });

            }
        };
        mRecyclerView.setAdapter(mType);
        setDataList();
        isattention = 0;
        isattentiontext = 0;
        mPlayActivityPresenter.getBannerData(IOUtils.getToken(mContext));
        //isFirst++;
        mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext), playTypeBean.type, check,
                page);

    }

    @Override
    public void onClick(View v) {
        parentFragment = (TabPlayHomeFragment) TabPlayFragment.this.getParentFragment();
        int id = v.getId();
        switch (id) {
            case R.id.filterLayout:
                Intent intentCity = new Intent();
                intentCity.setClass(mContext, SelectCityActivity.class);
                getActivity().startActivityForResult(intentCity, request_code_sercity);
                break;
            case R.id.driving:
                KPlayCarApp.putValue(Constants.IntentParams.PLAYINDEX, Constants.PlayTabSkip.DRIVINGFRAGMENT);
                tabPlayHomeFragment.switchoverDriving(2);
                //CommonUtils.startNewActivity(mContext, DrivingActivity.class);
                break;
            case R.id.please:
                KPlayCarApp.putValue(Constants.IntentParams.PLAYINDEX, Constants.PlayTabSkip.PLEASEPLAYFRAGMENT);
                tabPlayHomeFragment.switchoverPlease(2);
                // CommonUtils.startNewActivity(mContext, PleasePlayActivity.class);
                break;
            case R.id.travelplan:
                tabPlayHomeFragment.switchoverTripBookeList(2);
                //CommonUtils.startNewActivity(mContext, TripBookeListActivity.class);
                break;
            case R.id.topic://话题
                CommonUtils.startNewActivity(mContext, TopicActivity.class);
                break;
            case R.id.playbright: //玩得爽
                //CommonUtils.startNewActivity(mContext, HaveFunActivity.class);
                tabPlayHomeFragment.switchoverHaveFun();
                break;
            case R.id.seek: //搜索
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case request_code_sercity:
                    String cityName = data.getStringExtra(Constants.IntentParams.DATA);
                    kPlayCarApp.locCityName = cityName;
                    mFilterTitle.setText(data.getStringExtra(Constants.IntentParams.DATA));
                    break;
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        switch (playTypeBean.number) {
            case 3:
                if (mPlayListBean != null) {
                    if (mPlayListBean.data.list == null || mPlayListBean.data.list.size() == 0) {
                        if (check.equals("friend")) {
                            page = 1;
                            check = "friend";
                            mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                    playTypeBean.type, check, page);
                        } else if (check.equals("topic")) {
                            page = 1;
                            check = "topic";
                            mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                    playTypeBean.type, check, page);
                        } else if (check.equals("group")) {
                            page = 1;
                            check = "group";
                            mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                    playTypeBean.type, check, page);
                        }
                    }
                }

                break;
            case 4:
            case 5:
                if (mPlayListBean != null) {
                    if (mPlayListBean.data.list == null || mPlayListBean.data.list.size() == 0) {
                        mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                playTypeBean.type, check, page);
                    }
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        KPlayCarApp.removeValue(Constants.IntentParams.INDEX);
        Logger.d("KPlayCarApp  index tabplay");

    }

    public void setDataList() {
        mAdapter = new WrapAdapter<PlayListBean.DataList>(mContext, R.layout.update_item,
                DataList) {
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
                sethint();
                if (item.id == -1) {
                    if (position == 0 && isattention == 1) {
                        attention.setVisibility(View.VISIBLE);
                        itemselect.setVisibility(View.GONE);
                        mHintLayout.setVisibility(View.VISIBLE);
                        setType(isattentiontext);
                        attention.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        //group群组 topic话题 friend好友
                        friend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setType(0);
                                page = 1;
                                check = "friend";
                                mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                        playTypeBean.type, check, page);
                            }
                        });

                        topic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setType(1);
                                page = 1;
                                check = "topic";
                                mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                        playTypeBean.type, check, page);
                            }
                        });

                        group.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setType(2);
                                page = 1;
                                check = "group";
                                mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                        playTypeBean.type, check, page);
                            }
                        });

                    } else {
                        attention.setVisibility(View.GONE);
                    }
                } else if (item.id == -2) {
                    attention.setVisibility(View.GONE);
                    mHintLayout.setVisibility(View.VISIBLE);
                    itemselect.setVisibility(View.GONE);

                } else if (item.id == -3) {
                    attention.setVisibility(View.GONE);
                    mHintLayout.setVisibility(View.GONE);
                    itemselect.setVisibility(View.INVISIBLE);
                } else {
                    if (position == 0 && isattention == 1) {
                        attention.setVisibility(View.VISIBLE);
                        setType(isattentiontext);
                        attention.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        //group群组 topic话题 friend好友
                        friend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setType(0);
                                page = 1;
                                check = "friend";
                                mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                        playTypeBean.type, check, page);
                            }
                        });

                        topic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setType(1);
                                page = 1;
                                check = "topic";
                                mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                        playTypeBean.type, check, page);
                            }
                        });

                        group.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setType(2);
                                page = 1;
                                check = "group";
                                mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                        playTypeBean.type, check, page);
                            }
                        });

                    } else {
                        attention.setVisibility(View.GONE);
                    }

                    mTitle.setText(item.title);
                    mLook.setText(item.viewCount + "");
                    mLike.setText(item.diggCount + "");
                    mInformation.setText(item.replyCount + "");
                    time.setText(DateFormatUtil.timeLogi(item.createTime));

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

            public void sethint() {
                switch (playTypeBean.number) {
                    case 0:
                        mHintLayout.setmHintText("每日更新暂时没有数据");
                        break;
                    case 1:
                        mHintLayout.setmHintText("热图暂时没有数据");
                        break;
                    case 2:
                        mHintLayout.setmHintText("红人暂时没有数据");
                        break;
                    case 3:
                        if (mPlayListBean != null) {
                            if (check.equals("friend")) {
                                if (mPlayListBean.data.isRelevance == 0) {
                                    mHintLayout.setmHintText("木有关注好友");
                                    mHintLayout.setOperation("请关注好友后在  试试  ", "  试试  ",
                                            R.color.text_color2, new Link.OnClickListener() {
                                                @Override
                                                public void onClick(Object o, String clickedText) {
                                                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                                                    args.put(Constants.IntentParams.ID,
                                                            IOUtils.getAccount(mContext).userInfo.id);
                                                    CommonUtils.startNewActivity(mContext, args,
                                                            AttentionListActivity.class);
                                                }
                                            });
                                } else {
                                    mHintLayout.setmHintText("好友们都太懒了，什么都没有发！");
                                    mHintLayout.setOperation("发一条给好友瞧瞧，立即发帖  ", "立即发帖  ",
                                            R.color.text_color6, new Link.OnClickListener() {
                                                @Override
                                                public void onClick(Object o, String clickedText) {
                                                    CommonUtils.startNewActivity(mContext,
                                                            PostingActivity.class);
                                                }
                                            });
                                }
                            } else if (check.equals("topic")) {
                                if (mPlayListBean.data.isRelevance == 0) {
                                    mHintLayout.setmHintText("木有关注话题");
                                    mHintLayout.setOperation("请先关注话题  ", "关注话题  ",
                                            R.color.text_color6, new Link.OnClickListener() {
                                                @Override
                                                public void onClick(Object o, String clickedText) {
                                                    CommonUtils.startNewActivity(mContext,
                                                            TopicActivity.class);
                                                }
                                            });
                                } else {
                                    mHintLayout.setmHintText("木有话题相关的帖子");
                                    mHintLayout.setOperation("发一条给好友瞧瞧，立即发帖  ", "立即发帖  ",
                                            R.color.text_color6, new Link.OnClickListener() {
                                                @Override
                                                public void onClick(Object o, String clickedText) {
                                                    Account account = IOUtils.getAccount(mContext);
                                                    int isVip = account.userInfo.isVip;
                                                    if (isVip == 1) {
                                                        CommonUtils.startNewActivity(mContext,
                                                                FoundTopicActivity.class);
                                                    } else {
                                                        CommonUtils.showToast(mContext,
                                                                "您还不是种子用户，不能发帖");
                                                    }
                                                }
                                            });
                                }

                            }

                        }

                        break;
                    case 4:
                        if (mPlayListBean != null) {
                            if (mPlayListBean.data.isRelevance == 0) {
                                mHintLayout.setmHintText("木有设置兴趣标签");
                                mHintLayout.setOperation("请先设置兴趣标签", "设置兴趣标签", R.color.text_color6,
                                        new Link.OnClickListener() {
                                            @Override
                                            public void onClick(Object o, String clickedText) {
                                                CommonUtils.startNewActivity(mContext,
                                                        HobbyTagMgrActivity.class);
                                            }
                                        });
                            } else {
                                mHintLayout.setmHintText("木有兴趣相关的帖子");
                                mHintLayout.setOperation("那就让我来一发吧，立即发帖", "立即发帖",
                                        R.color.text_color6, new Link.OnClickListener() {
                                            @Override
                                            public void onClick(Object o, String clickedText) {
                                                CommonUtils.startNewActivity(mContext,
                                                        PostingActivity.class);
                                            }
                                        });
                            }
                        }
                        break;
                    case 5:
                        if (mPlayListBean != null) {
                            if (mPlayListBean.data.isRelevance == 0) {
                                mHintLayout.setmHintText("亲，你还没有设置个人定制哦！");
                                mHintLayout.setOperation("立即设置", "立即设置", R.color.text_color6,
                                        new Link.OnClickListener() {
                                            @Override
                                            public void onClick(Object o, String clickedText) {
                                                CommonUtils.showToast(mContext, "立即设置");
                                            }
                                        });
                            } else {
                                mHintLayout.setmHintText("暂无与您定制相关的内容！");
                                mHintLayout.setOperation("去发帖", "去发帖", R.color.text_color6,
                                        new Link.OnClickListener() {
                                            @Override
                                            public void onClick(Object o, String clickedText) {
                                                CommonUtils.startNewActivity(mContext,
                                                        PostingActivity.class);
                                            }
                                        });
                            }
                        }
                        break;
                }
            }

            public void setType(int num) {
                switch (num) {
                    case 0:
                        friend.setBackground(mRes.getDrawable(R.drawable.play_attention_bg));
                        topic.setBackground(mRes.getDrawable(R.drawable.play_attention_text_bg));
                        group.setBackground(mRes.getDrawable(R.drawable.play_attention_text_bg));
                        friend.setTextColor(mRes.getColor(R.color.white));
                        topic.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                        group.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                        isattentiontext = 0;
                        break;
                    case 1:
                        friend.setBackground(mRes.getDrawable(R.drawable.play_attention_text_bg));
                        topic.setBackground(mRes.getDrawable(R.drawable.play_attention_bg));
                        group.setBackground(mRes.getDrawable(R.drawable.play_attention_text_bg));
                        friend.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                        topic.setTextColor(mRes.getColor(R.color.white));
                        group.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                        isattentiontext = 1;
                        break;
                    case 2:
                        friend.setBackground(mRes.getDrawable(R.drawable.play_attention_text_bg));
                        topic.setBackground(mRes.getDrawable(R.drawable.play_attention_text_bg));
                        group.setBackground(mRes.getDrawable(R.drawable.play_attention_bg));
                        friend.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                        topic.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                        group.setTextColor(mRes.getColor(R.color.white));
                        isattentiontext = 2;
                        break;
                }
            }
        };
        mSmoothListview.setAdapter(mAdapter);

    }

    @Override
    public void dataBannerSucceed(BannerModel bannerModel) {
        List<BannerModel.Data> data = bannerModel.data;
        // mSmoothListview.removeAllViews();
        setBannerView(data);
        setSkipView();
        setTypeView();
    }

    @Override
    public void dataBannerDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
        mSmoothListview.stopLoadMore();
        mSmoothListview.stopRefresh();
        if (isattention == 1 || page == 1) {
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
        mRecyclerView.setVisibility(View.GONE);
        mSmoothListview.stopLoadMore();
        mSmoothListview.stopRefresh();
        mPlayListBean = playListBean;
        if (page == 1) {
            DataList.clear();
            DataList.addAll(playListBean.data.list);

            if (DataList == null || DataList.size() == 0) {
                if (isattention == 1) {
                    PlayListBean.DataList dataList = new PlayListBean.DataList();
                    dataList.id = -1;
                    DataList.add(dataList);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    PlayListBean.DataList dataList = new PlayListBean.DataList();
                    dataList.id = -2;
                    DataList.add(dataList);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
            if (DataList.size() < 3) {
                for (int i = DataList.size(); i < 3; i++) {
                    PlayListBean.DataList dataList = new PlayListBean.DataList();
                    dataList.id = -3;
                    DataList.add(dataList);
                }
            }
            setDataList();
        } else {
            DataList.addAll(playListBean.data.list);
            mAdapter.notifyDataSetChanged();
        }
        if (playListBean.data.list != null) {
            if (playListBean.data.list.size() != 0) {
                mSmoothListview.setLoadMoreEnable(true);
            } else {
                mSmoothListview.setLoadMoreEnable(false);
            }
        } else {
            mSmoothListview.setLoadMoreEnable(false);
        }

    }

    @Override
    public void dataTopicSucceed(List<PlayTopicBean> playListBean) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    public void setBannerView(final List<BannerModel.Data> datas) {
        List<String> imgUrl = new ArrayList<>();
        View BannerView = LayoutInflater.from(mContext).inflate(R.layout.activity_drivi_banner,
                mSmoothListview, false);
        Banner drivingBanner = (Banner) BannerView.findViewById(R.id.driving_banner);

        for (int i = 0; i < datas.size(); i++) {
            imgUrl.add(URLUtil.builderImgUrl(datas.get(i).image, 540, 270));

        }
        drivingBanner.setImages(imgUrl).setImageLoader(new FrescoImageLoader())
                .setOnBannerClickListener(new OnBannerClickListener() {
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
        mSmoothListview.addHeaderView(BannerView);
    }

    public void setSkipView() {
        View SkipView = LayoutInflater.from(mContext).inflate(R.layout.play_skip, mSmoothListview,
                false);
        LinearLayout driving = (LinearLayout) SkipView.findViewById(R.id.driving);
        LinearLayout please = (LinearLayout) SkipView.findViewById(R.id.please);
        LinearLayout travelplan = (LinearLayout) SkipView.findViewById(R.id.travelplan);
        LinearLayout topic = (LinearLayout) SkipView.findViewById(R.id.topic);
        LinearLayout playbright = (LinearLayout) SkipView.findViewById(R.id.playbright);
        driving.setOnClickListener(this);
        please.setOnClickListener(this);
        travelplan.setOnClickListener(this);
        topic.setOnClickListener(this);
        playbright.setOnClickListener(this);
        mSmoothListview.addHeaderView(SkipView);
    }

    public void setTypeView() {
        TypeView = LayoutInflater.from(mContext).inflate(R.layout.play_type, mSmoothListview,
                false);
        mRecyclerViewTop = (RecyclerView) TypeView.findViewById(R.id.recyclerviewtop);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewTop.setLayoutManager(linearLayoutManager);

        mTypeTop = new CommonAdapter<PlayTypeBean>(mContext, R.layout.typeselect_item, mTypeList) {
            @Override
            public void convert(ViewHolder helper, PlayTypeBean item) {
                final int position = helper.getPosition();
                LinearLayout selectlayout = helper.getView(R.id.selectlayout);
                TextView name = helper.getView(R.id.name);
                TextView select = helper.getView(R.id.select);
                name.setText(item.name);

                if (item.isClick) {
                    select.setVisibility(View.VISIBLE);
                } else {
                    select.setVisibility(View.GONE);
                }

                selectlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTypeList.get(position).isClick) {

                        } else {

                            for (PlayTypeBean bean : mTypeList) {
                                bean.isClick = false;
                            }
                            mTypeList.get(position).isClick = true;
                            playTypeBean = mTypeList.get(position);
                            mType.notifyDataSetChanged();
                            mTypeTop.notifyDataSetChanged();
                            page = 1;
                            if (mTypeList.get(position).name.equals("关注")) {
                                if (IOUtils.isLogin(mContext)) {
                                    check = "friend";
                                    isattention = 1;
                                    isattentiontext = 0;
                                    mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                            mTypeList.get(position).type, check, page);
                                } else {
                                    DataList.clear();
                                    mAdapter.notifyDataSetChanged();
                                    return;
                                }

                            } else {
                                if (mTypeList.get(position).name.equals("兴趣")
                                        || mTypeList.get(position).name.equals("爱车")) {
                                    if (IOUtils.isLogin(mContext)) {
                                        check = "";
                                        isattention = 0;
                                        isattentiontext = 0;
                                        mPlayActivityPresenter.getDataLists(
                                                IOUtils.getToken(mContext),
                                                mTypeList.get(position).type, check, page);
                                    } else {
                                        DataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                        return;
                                    }
                                } else {
                                    check = "";
                                    isattention = 0;
                                    isattentiontext = 0;
                                    mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext),
                                            mTypeList.get(position).type, check, page);
                                }
                            }
                        }

                    }
                });

            }
        };
        mRecyclerViewTop.setAdapter(mType);
        mSmoothListview.addHeaderView(TypeView);
    }

    @Override
    public void onRefresh() {
        page = 1;
        if (isattention == 1) {

        } else {
            check = "";
            isattentiontext = 0;
        }
        mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext), playTypeBean.type, check,
                page);
    }

    @Override
    public void onLoadMore() {
        page++;
        mPlayActivityPresenter.getDataLists(IOUtils.getToken(mContext), playTypeBean.type, check,
                page);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayActivityPresenter != null) {
            mPlayActivityPresenter.detachView();
        }
    }
}
