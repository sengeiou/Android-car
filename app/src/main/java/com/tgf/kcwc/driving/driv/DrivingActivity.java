package com.tgf.kcwc.driving.driv;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.util.DensityUtil;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.DrvingListModel;
import com.tgf.kcwc.mvp.presenter.DrivingDataPrenter;
import com.tgf.kcwc.mvp.view.DrivDataView;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PleaseDownCitySpinner;
import com.tgf.kcwc.view.SmoothListView.SmoothListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/17.
 */

public class DrivingActivity extends BaseActivity
        implements DrivDataView, SmoothListView.ISmoothListViewListener {

    public static final int MTYPENUM = 0;
    public static final int MDESTINATIONNUM = 1;
    public static final int MGOTIMENUM = 2;
    public static final int MORE = 3;

    private SmoothListView mSmoothListview;
    private DrivingDataPrenter drivingDataPrenter;
    private int page = 1;
    private HeaderBannerView mHeaderBannerView;                     //头部轮播
    private HeaderFilterView mHeaderFilterView;                     //头部选择框
    private ListviewHint mListviewHint;                         //尾部
    private WrapAdapter<DrvingListModel.DataList> mAdapter;
    private int filterViewPosition = 2;                // 筛选视图的位置
    private float titleLayoutHeigh = 48;               // 广告视图的高度
    private View itemHeaderBannerView;                  // 从ListView获取的广告子View
    private boolean isStickyTop = false;            // 是否吸附在顶部
    private boolean isSmooth = false;            // 是否点击
    private boolean isScrollIdle = true;             // ListView是否在滑动
    private float filterViewTopMargin;                   // 筛选视图距离顶部的距离
    private RelativeLayout titleLayout;                           //标题栏
    private LinearLayout drivingSelect;                         //真选择框
    private LinearLayout mType;                                 //选择类型
    private LinearLayout mDestination;                          //选择目的地
    private LinearLayout mGoTime;                               //选择时间
    private DropDownSingleSpinner mTypeDropDownSingleSpinner;            //选择类型的下拉框
    private DropDownSingleSpinner mGoTimeDropDownSingleSpinner;          //选择时间的下拉框
    private PleaseDownCitySpinner mPleaseDownCitySpinner;                 //选择城市
    private List<DataItem> mDataList = new ArrayList<>();//type数据
    private List<DataItem> mTimeDataList = new ArrayList<>();//time数据
    private int filterPosition = 0;                // 点击FilterView的位置：类型(0)、目的地(1)、时间(2)
    private String typeString = "";
    private String destinationString = "";
    private String goTimeString = "";
    public List<DrvingListModel.DataList> dataList = new ArrayList<>();

    public void gainTypeDataList() {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = -1;
        dataItem.name = "不限";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 6;
        dataItem.name = "自驾游";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 5;
        dataItem.name = "召集令";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "聚会";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 7;
        dataItem.name = "其它";
        mDataList.add(dataItem);
    }

    public void gainTimeDataList() {
        mTimeDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = -1;
        dataItem.name = "不限";
        mTimeDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.name = "一周内";
        mTimeDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "一月内";
        mTimeDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.name = "三月内";
        mTimeDataList.add(dataItem);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.driving);
        function.setImageResource(R.drawable.btn_search_white);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
            }
        });
    }

    @Override
    protected void setUpViews() {
        gainTypeDataList();
        gainTimeDataList();
        mSmoothListview = (SmoothListView) findViewById(R.id.listview);
        titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        drivingSelect = (LinearLayout) findViewById(R.id.driving_select);
        mType = (LinearLayout) findViewById(R.id.listact_categroy);
        mDestination = (LinearLayout) findViewById(R.id.listact_kilometer);
        mGoTime = (LinearLayout) findViewById(R.id.listact_rank);

        mType.setOnClickListener(this);
        mDestination.setOnClickListener(this);
        mGoTime.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mType, "类型");
        FilterPopwinUtil.commonFilterTile(mDestination, "目的地");
        FilterPopwinUtil.commonFilterTile(mGoTime, "出发时间");

        mSmoothListview.setRefreshEnable(false);
        mSmoothListview.setLoadMoreEnable(false);
        mSmoothListview.setSmoothListViewListener(this);
        drivingDataPrenter = new DrivingDataPrenter();
        drivingDataPrenter.attachView(this);
        //drivingDataPrenter.getListData(IOUtils.getToken(mContext), page, goTimeString,destinationString, typeString);
        drivingDataPrenter.getBannerData(IOUtils.getToken(mContext));

        mSmoothListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mAdapter = new WrapAdapter<DrvingListModel.DataList>(mContext, dataList,
                R.layout.driving_listview_item) {
            @Override
            public void convert(ViewHolder helper, final DrvingListModel.DataList item) {
                LinearLayout mLinearLayout = helper.getView(R.id.driverlayout);
                SimpleDraweeView mSimpleDraweeView = helper.getView(R.id.driving_list_view_imageview);
                LinearLayout driverLayout = helper.getView(R.id.driverlayout);
                ImageView mStateImageView = helper.getView(R.id.driving_list_view_state);
                ImageView mEliteImageView = helper.getView(R.id.driving_list_view_elite);
                TextView DynamicText = helper.getView(R.id.driving_list_view_dynamic);
                TextView LookText = helper.getView(R.id.driving_list_view_look);
                TextView LikeText = helper.getView(R.id.driving_list_view_like);
                TextView CommentText = helper.getView(R.id.driving_list_view_comment);
                //TextView DestinationText = helper.getView(R.id.driving_list_view_destination);
                //TextView TimeText = helper.getView(R.id.driving_list_view_time);
                TextView NumberText = helper.getView(R.id.driving_list_view_number);
                driverLayout.setVisibility(View.VISIBLE);
                if (item.cover.equals("-1")) {
                    driverLayout.setVisibility(View.INVISIBLE);
                } else {
                    mSimpleDraweeView
                            .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));//item图片
                    DynamicText.setText(item.title); //标题
                    LookText.setText(item.viewCount + "");//查看数量
                    LikeText.setText(item.diggCount + ""); //点赞数量
                    CommentText.setText(item.replyCount + "");//评论数量
                    //DestinationText.setText(item.destCity + item.destination);//目的地
                    //TimeText.setText(item.beginTime);//报名截止时间

                    if (item.limitMax == 0) {
                        NumberText.setTextColor(getResources().getColor(R.color.tab_text_n_color));
                        NumberText.setText(item.surplus + "/不限");
                    } else {
                        //人数
                        if (item.surplus == item.limitMax) {
                            NumberText.setTextColor(getResources().getColor(R.color.red));
                            NumberText.setText("已满");
                        } else {
                            NumberText.setTextColor(getResources().getColor(R.color.tab_text_n_color));
                            if (item.limitMax == 0) {
                                NumberText.setText(item.surplus + "/不限");
                            } else {
                                NumberText.setText(item.surplus + "/" + item.limitMax);
                            }
                        }
                    }

                    //该活动状态
                    String activityStatus = item.activityStatus;
                    if (activityStatus.equals("报名中")) {
                        mStateImageView.setImageResource(R.drawable.btn_apply);
                    } else if (activityStatus.equals("活动结束")) {
                        mStateImageView.setImageResource(R.drawable.btn_finish);
                    } else if (activityStatus.equals("活动中")) {
                        mStateImageView.setImageResource(R.drawable.btn_hdz);
                    } else if (activityStatus.equals("报名截止")) {
                        mStateImageView.setImageResource(R.drawable.btn_applystop);
                    }

                    //是否是精华
                    if (item.isDigest == 1) {
                        mEliteImageView.setImageResource(R.drawable.icon_jinghua);
                        mEliteImageView.setVisibility(View.VISIBLE);
                    } else {
                        mEliteImageView.setVisibility(View.GONE);
                    }

                    mLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.id + "");
                            CommonUtils.startNewActivity(mContext, args, DrivingDetailsActivity.class);
                        }
                    });
                }

            }
        };
        mSmoothListview.setAdapter(mAdapter);
    }

    @Override
    public void dataListSucceed(DrvingListModel drvingListModel) {
        mSmoothListview.stopLoadMore();
        mSmoothListview.removeFooterView(mListviewHint);
        if (drvingListModel.data.list.size() == 0 || drvingListModel.data.list == null) {
            mSmoothListview.setLoadMoreEnable(false);
            if (page == 1) {
                mSmoothListview.smoothScrollToPositionFromTop(1,
                        DensityUtil.dip2px(mContext, titleLayoutHeigh));
            } else {
                mListviewHint = new ListviewHint(mContext);
                mSmoothListview.addFooterView(mListviewHint);
                return;
            }
        } else {
            mSmoothListview.removeFooterView(mListviewHint);
            mSmoothListview.setLoadMoreEnable(true);
        }

        if (page == 1) {
            dataList.clear();
            dataList.addAll(drvingListModel.data.list);
            if (dataList.size() < 3) {
                mSmoothListview.setLoadMoreEnable(false);
                mListviewHint = new ListviewHint(mContext);
                mSmoothListview.addFooterView(mListviewHint);
                for (int i = 0; i < 3 - dataList.size(); i++) {
                    DrvingListModel.DataList data = new DrvingListModel.DataList();
                    data.cover = "-1";
                    dataList.add(data);
                }
            }
            mAdapter.notifyDataSetChanged();
        } else {
            dataList.addAll(drvingListModel.data.list);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
        mSmoothListview.stopLoadMore();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MTYPENUM:
                    isSmooth = true;
                    filterPosition = MTYPENUM;
                    break;
                case MDESTINATIONNUM:
                    isSmooth = true;
                    filterPosition = MDESTINATIONNUM;
                    break;
                case MGOTIMENUM:
                    isSmooth = true;
                    filterPosition = MGOTIMENUM;
                    break;
                case MORE:
                    isSmooth = true;
                    filterPosition = MORE;
                    break;
            }
            mSmoothListview.smoothScrollToPositionFromTop(3,
                    DensityUtil.dip2px(mContext, titleLayoutHeigh - 3));
            super.handleMessage(msg);
        }
    };

    @Override
    public void dataBannerSucceed(BannerModel bannerModel) {
        mHeaderBannerView = new HeaderBannerView(this);
        mHeaderBannerView.fillView(bannerModel.data, mSmoothListview);
        mHeaderFilterView = new HeaderFilterView(this, mHandler);
        mHeaderFilterView.fillView(new Object(), mSmoothListview);

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
                float nameHeigh = DensityUtils.px2dp(mContext, drivingSelect.getHeight());
                // 获取筛选View、距离顶部的高度
                if (itemHeaderBannerView == null) {
                    itemHeaderBannerView = mSmoothListview
                            .getChildAt(filterViewPosition - firstVisibleItem);
                }
                if (itemHeaderBannerView != null) {
                    filterViewTopMargin = DensityUtils.px2dp(mContext, itemHeaderBannerView.getTop());
                }

                // 处理筛选是否吸附在顶部
                if (filterViewTopMargin + nameHeigh <= titleLayoutHeigh
                        || firstVisibleItem > filterViewPosition) {
                    isStickyTop = true; // 吸附在顶部
                    drivingSelect.setVisibility(View.VISIBLE);
                } else {
                    isStickyTop = false; // 没有吸附在顶部
                    drivingSelect.setVisibility(View.GONE);
                }

                if (isSmooth && isStickyTop) {
                    isSmooth = false;
                    seleDownSingleSpinner(filterPosition);
                }

            }
        });

    }

    @Override
    public void dataBannerDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        page++;
        //drivingDataPrenter.getListData(IOUtils.getToken(mContext), page, goTimeString,destinationString, typeString);
    }

    public void seleDownSingleSpinner(int number) {
        if (number == MTYPENUM) {
            mTypeDropDownSingleSpinner = new DropDownSingleSpinner(this, mDataList);
            FilterPopwinUtil.commonFilterTitleColor(mRes, mType, R.color.text_color10);
            FilterPopwinUtil.commonFilterImage(mType, R.drawable.filter_drop_down_green);
            mTypeDropDownSingleSpinner.showAsDropDownBelwBtnView(drivingSelect);

            mTypeDropDownSingleSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mType, R.color.text_color15);
                    FilterPopwinUtil.commonFilterImage(mType, R.drawable.fitler_drop_down_n);
                    DataItem selectDataItem = mTypeDropDownSingleSpinner.getSelectDataItem();
                    if (selectDataItem != null && mTypeDropDownSingleSpinner.getIsSelec()) {
                        FilterPopwinUtil.commonFilterTile(mType, selectDataItem.name);
                        mHeaderFilterView.setmType(selectDataItem.name);
                        page = 1;
                        if (selectDataItem.id != -1) {
                            typeString = selectDataItem.id + "";
                        } else {
                            typeString = "";
                        }
                        //drivingDataPrenter.getListData(IOUtils.getToken(mContext), page,goTimeString, destinationString, typeString);

                    }
                }
            });

        } else if (number == MDESTINATIONNUM) {
            mPleaseDownCitySpinner = new PleaseDownCitySpinner(this);
            FilterPopwinUtil.commonFilterTitleColor(mRes, mDestination, R.color.text_color10);
            FilterPopwinUtil.commonFilterImage(mDestination, R.drawable.filter_drop_down_green);
            mPleaseDownCitySpinner.showAsDropDownBelwBtnView(drivingSelect);
            mPleaseDownCitySpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mDestination,
                            R.color.text_color15);
                    FilterPopwinUtil.commonFilterImage(mDestination, R.drawable.fitler_drop_down_n);
                    DataItem selectDataItem = mPleaseDownCitySpinner.getSelectDataItem();
                    if (selectDataItem != null && mPleaseDownCitySpinner.getIsSelec()) {
                        FilterPopwinUtil.commonFilterTile(mDestination, selectDataItem.name);
                        mHeaderFilterView.setmDestination(selectDataItem.name);
                        page = 1;
                        destinationString = selectDataItem.name;
                       // drivingDataPrenter.getListData(IOUtils.getToken(mContext), page, goTimeString, destinationString, typeString);
                    }
                }
            });
        } else if (number == MORE) {
            CommonUtils.showToast(mContext,"更多");
        } else {
            mGoTimeDropDownSingleSpinner = new DropDownSingleSpinner(this, mTimeDataList);
            FilterPopwinUtil.commonFilterTitleColor(mRes, mGoTime, R.color.text_color10);
            FilterPopwinUtil.commonFilterImage(mGoTime, R.drawable.filter_drop_down_green);
            mGoTimeDropDownSingleSpinner.showAsDropDownBelwBtnView(drivingSelect);
            mGoTimeDropDownSingleSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mGoTime, R.color.text_color15);
                    FilterPopwinUtil.commonFilterImage(mGoTime, R.drawable.fitler_drop_down_n);
                    DataItem selectDataItem = mGoTimeDropDownSingleSpinner.getSelectDataItem();
                    if (selectDataItem != null && mGoTimeDropDownSingleSpinner.getIsSelec()) {
                        FilterPopwinUtil.commonFilterTile(mGoTime, selectDataItem.name);
                        mHeaderFilterView.setmGoTime(selectDataItem.name);
                        page = 1;
                        if (selectDataItem.id != -1) {
                            goTimeString = selectDataItem.id + "";
                        } else {
                            goTimeString = "";
                        }
                       // drivingDataPrenter.getListData(IOUtils.getToken(mContext), page, goTimeString, destinationString, typeString);

                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.listact_categroy:
                seleDownSingleSpinner(MTYPENUM);
                break;
            case R.id.listact_kilometer:
                seleDownSingleSpinner(MDESTINATIONNUM);
                break;
            case R.id.listact_rank:
                seleDownSingleSpinner(MGOTIMENUM);
                break;
        }
        mSmoothListview.smoothScrollToPositionFromTop(3,
                DensityUtil.dip2px(mContext, titleLayoutHeigh - 2));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (drivingDataPrenter != null) {
            drivingDataPrenter.detachView();
        }
    }
}
