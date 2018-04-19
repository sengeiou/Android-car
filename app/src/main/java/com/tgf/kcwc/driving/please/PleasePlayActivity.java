package com.tgf.kcwc.driving.please;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
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
import com.tgf.kcwc.driving.driv.HeaderBannerView;
import com.tgf.kcwc.driving.driv.ListviewHint;
import com.tgf.kcwc.driving.driv.SignInActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.PleasePlayModel;
import com.tgf.kcwc.mvp.presenter.PleaseDataPrenter;
import com.tgf.kcwc.mvp.view.PleaseDataView;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PleaseDownCatSpinner;
import com.tgf.kcwc.view.PleaseDownCitySpinner;
import com.tgf.kcwc.view.SmoothListView.SmoothListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/20.
 */

public class PleasePlayActivity extends BaseActivity
        implements PleaseDataView, SmoothListView.ISmoothListViewListener {

    public static final int MTYPENUM = 0;
    public static final int MDESTINATIONNUM = 1;
    public static final int MGOTIMENUM = 2;
    public static final int MTIMEENUM = 3;

    private SmoothListView mSmoothListview;
    private PleaseDataPrenter mPleaseDataPrenter;
    private HeaderBannerView mHeaderBannerView;                     //头部轮播
    private HeaderFilterView mHeaderFilterView;                     //头部假选项
    private ListviewHint mListviewHint;                         //尾部
    private LinearLayout mType;                                 //选择品牌
    private LinearLayout mRank;                                 //选择区域
    private LinearLayout mGoTime;                               //选择时间

    private WrapAdapter<PleasePlayModel.DataList> mAdapter;
    public List<PleasePlayModel.DataList> dataList = new ArrayList<>();

    private int filterViewPosition = 2;                // 筛选视图的位置
    private float titleLayoutHeigh = 48;               // 广告视图的高度
    private View itemHeaderBannerView;                  // 从ListView获取的广告子View
    private boolean isStickyTop = false;            // 是否吸附在顶部
    private boolean isSmooth = false;            // 是否点击
    private boolean isScrollIdle = true;             // ListView是否在滑动
    private float filterViewTopMargin;                   // 筛选视图距离顶部的距离
    private RelativeLayout titleLayout;                           //标题栏
    private LinearLayout pleaseSelect;                          //真选择框

    private DropDownSingleSpinner mTypeDropDownSingleSpinner;            //选择类型的下拉框
    private DropDownSingleSpinner mGoTimeDropDownSingleSpinner;          //选择时间的下拉框
    private PleaseDownCatSpinner mPleaseDownCatSpinner;                 //选择品牌
    private PleaseDownCitySpinner mPleaseDownCitySpinner;                 //选择城市
    private List<DataItem> mDataList = new ArrayList<>();//type数据
    private List<DataItem> mTimeDataList = new ArrayList<>();//time数据
    private int filterPosition = 0;                // 点击FilterView的位置：品牌(0)、类型(1)、区域(2)、时间(3)
    private int page = 1;
    private String begin_time = "";               //时间
    private String brand_id = "";               //品牌id
    private String type = "";               //类型
    private String city = "";               //区域

    public void gainTypeDataList() {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.title = "test_drive";
        dataItem.name = "试乘试驾";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.title = "self_drive";
        dataItem.name = "车主自驾";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.title = "other";
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
    public void onDestroy() {
        super.onDestroy();
        if (mPleaseDataPrenter != null) {
            mPleaseDataPrenter.detachView();
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.please);
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
        pleaseSelect = (LinearLayout) findViewById(R.id.please_select);
        mType = (LinearLayout) findViewById(R.id.listact_categroy);
        mRank = (LinearLayout) findViewById(R.id.listact_rank);
        mGoTime = (LinearLayout) findViewById(R.id.listact_time);

        mType.setOnClickListener(this);
        mRank.setOnClickListener(this);
        mGoTime.setOnClickListener(this);

        mSmoothListview.setRefreshEnable(false);
        mSmoothListview.setLoadMoreEnable(false);
        mSmoothListview.setSmoothListViewListener(this);

        FilterPopwinUtil.commonFilterTile(mType, "品牌");
        FilterPopwinUtil.commonFilterTile(mRank, "区域");
        FilterPopwinUtil.commonFilterTile(mGoTime, "时间");

        mPleaseDataPrenter = new PleaseDataPrenter();
        mPleaseDataPrenter.attachView(this);
        mPleaseDataPrenter.getListData(builderParams());
        mPleaseDataPrenter.getBannerData(IOUtils.getToken(mContext));
        mAdapter = new WrapAdapter<PleasePlayModel.DataList>(mContext, dataList,
                R.layout.driving_listview_item) {
            @Override
            public void convert(ViewHolder helper, final PleasePlayModel.DataList item) {
                LinearLayout mLinearLayout = helper.getView(R.id.driverlayout);
                SimpleDraweeView mSimpleDraweeView = helper
                        .getView(R.id.driving_list_view_imageview);
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
                            .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));//item图片
                    DynamicText.setText(item.title);
                    LookText.setText(item.viewCount + "");//查看数量
                    LikeText.setText(item.diggCount + ""); //点赞数量
                    CommentText.setText(item.replyCount + "");//评论数量
                  //  DestinationText.setText(item.rendCity + item.rendezvous);//目的地
                   // TimeText.setText(item.beginTime);//报名截止时间

                    //人数
                    if (item.limitMax == 0) {
                        NumberText.setTextColor(getResources().getColor(R.color.tab_text_n_color));
                        NumberText.setText(item.surplus + "/不限");
                    } else {
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
                    } else if (activityStatus.equals("已结束")) {
                        mStateImageView.setImageResource(R.drawable.btn_finish);
                    } else if (activityStatus.equals("活动中")) {
                        mStateImageView.setImageResource(R.drawable.btn_hdz);
                    } else if (activityStatus.equals("报名截止")) {
                        mStateImageView.setImageResource(R.drawable.btn_applystop);
                    } else {
                        mStateImageView.setVisibility(View.GONE);
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
                            CommonUtils.startNewActivity(mContext, args, PleasePlayDetailsActivity.class);
                        }
                    });
                }
            }
        };

        mSmoothListview.setAdapter(mAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pleaseplay);
    }

    @Override
    public void onClick(View view) {
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
            case R.id.listact_time:
                seleDownSingleSpinner(MTIMEENUM);
                break;
        }
        super.onClick(view);
    }

    @Override
    public void dataListSucceed(PleasePlayModel pleasePlayModel) {
        mSmoothListview.stopLoadMore();
        mSmoothListview.removeFooterView(mListviewHint);
        if (pleasePlayModel.data.list.size() == 0 || pleasePlayModel.data.list == null) {
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
            mSmoothListview.setLoadMoreEnable(true);
        }
        if (page == 1) {
            dataList.clear();
            dataList.addAll(pleasePlayModel.data.list);
            if (dataList.size() < 3) {
                mSmoothListview.setLoadMoreEnable(false);
                mListviewHint = new ListviewHint(mContext);
                mSmoothListview.addFooterView(mListviewHint);
                for (int i = 0; i < 3 - dataList.size(); i++) {
                    PleasePlayModel.DataList data = new PleasePlayModel.DataList();
                    data.cover = "-1";
                    dataList.add(data);
                }
            }
        } else {
            dataList.addAll(pleasePlayModel.data.list);
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MTYPENUM:
                    filterPosition = MTYPENUM;
                    break;
                case MDESTINATIONNUM:
                    filterPosition = MDESTINATIONNUM;
                    break;
                case MGOTIMENUM:
                    filterPosition = MGOTIMENUM;
                    break;
                case MTIMEENUM:
                    filterPosition = MTIMEENUM;
                    break;
            }
            isSmooth = true;
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
                float nameHeigh = DensityUtils.px2dp(mContext, pleaseSelect.getHeight());
                // 获取筛选View、距离顶部的高度
                if (itemHeaderBannerView == null) {
                    itemHeaderBannerView = mSmoothListview
                            .getChildAt(filterViewPosition - firstVisibleItem);
                }
                if (itemHeaderBannerView != null) {
                    filterViewTopMargin = DensityUtils.px2dp(mContext,
                            itemHeaderBannerView.getTop());
                }

                // 处理筛选是否吸附在顶部
                if (filterViewTopMargin + nameHeigh <= titleLayoutHeigh
                        || firstVisibleItem > filterViewPosition) {
                    isStickyTop = true; // 吸附在顶部
                    pleaseSelect.setVisibility(View.VISIBLE);
                } else {
                    isStickyTop = false; // 没有吸附在顶部
                    pleaseSelect.setVisibility(View.GONE);
                }

                if (isSmooth && isStickyTop) {
                    isSmooth = false;
                    seleDownSingleSpinner(filterPosition);
                }

            }
        });
    }

    public void seleDownSingleSpinner(int number) {
        if (number == MTYPENUM) {
            mPleaseDownCatSpinner = new PleaseDownCatSpinner(this);
            FilterPopwinUtil.commonFilterTitleColor(mRes, mType, R.color.text_color10);
            FilterPopwinUtil.commonFilterImage(mType, R.drawable.filter_drop_down_r);
            mPleaseDownCatSpinner.showAsDropDownBelwBtnView(pleaseSelect);
            mPleaseDownCatSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mType, R.color.text_color15);
                    FilterPopwinUtil.commonFilterImage(mType, R.drawable.fitler_drop_down_n);
                    DataItem selectDataItem = mPleaseDownCatSpinner.getSelectDataItem();
                    if (selectDataItem != null && mPleaseDownCatSpinner.getIsSelec()) {
                        mHeaderFilterView.setmType(selectDataItem.name);
                        FilterPopwinUtil.commonFilterTile(mType, selectDataItem.name);
                        brand_id = selectDataItem.id + "";
                        mPleaseDataPrenter.getListData(builderParams());
                    }
                }
            });

        } else if (number == MDESTINATIONNUM) {
        } else if (number == MGOTIMENUM) {
            mPleaseDownCitySpinner = new PleaseDownCitySpinner(this);
            FilterPopwinUtil.commonFilterTitleColor(mRes, mRank, R.color.text_color10);
            FilterPopwinUtil.commonFilterImage(mRank, R.drawable.filter_drop_down_green);
            mPleaseDownCitySpinner.showAsDropDownBelwBtnView(pleaseSelect);
            mPleaseDownCitySpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mRank, R.color.text_color15);
                    FilterPopwinUtil.commonFilterImage(mRank, R.drawable.fitler_drop_down_n);
                    DataItem selectDataItem = mPleaseDownCitySpinner.getSelectDataItem();
                    if (selectDataItem != null && mPleaseDownCitySpinner.getIsSelec()) {
                        FilterPopwinUtil.commonFilterTile(mRank, selectDataItem.name);
                        mHeaderFilterView.setmRank(selectDataItem.name);
                        city = selectDataItem.name;
                        page = 1;
                        begin_time = selectDataItem.id + "";
                        mPleaseDataPrenter.getListData(builderParams());
                    }
                }
            });

        } else {
            mGoTimeDropDownSingleSpinner = new DropDownSingleSpinner(this, mTimeDataList);
            FilterPopwinUtil.commonFilterTitleColor(mRes, mGoTime, R.color.text_color10);
            FilterPopwinUtil.commonFilterImage(mGoTime, R.drawable.filter_drop_down_green);
            mGoTimeDropDownSingleSpinner.showAsDropDownBelwBtnView(pleaseSelect);
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
                            begin_time = selectDataItem.id + "";
                        } else {
                            begin_time = "";
                        }
                        mPleaseDataPrenter.getListData(builderParams());
                    }
                }
            });

        }

    }

    @Override
    public void dataBannerDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
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
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        page++;
        mPleaseDataPrenter.getListData(builderParams());
    }

    private Map<String, String> builderParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("page", page + "");
        params.put("begin_time", begin_time);//出发时间
        params.put("brand_id", brand_id);//品牌
        params.put("city", city);//区域
        params.put("type", type);//区域

/*        ArrayList<DataItem> typekData = drivingMoreBean.typekData;
        StringBuffer stringBuffer = new StringBuffer();
        for (DataItem t : typekData) {
            if (t.isSelected){
                stringBuffer.append(t.id).append(",");
            }
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); //调用 字符串的deleteCharAt() 方法,删除最后一个多余的逗号
        }
        params.put("scene_type", stringBuffer.toString());//类型
        boolean isSelected;
        isSelected = drivingMoreBean.onlyLookData.get(0).isSelected;
        if (isSelected) {
            params.put("is_digest", "1"); //只看精华帖子
        } else {
            params.put("is_digest", "0"); //不只看精华帖子
        }
        isSelected = drivingMoreBean.onlyLookData.get(2).isSelected;
        if (isSelected) {
            params.put("common_city", "1"); //同城出发
        } else {
            params.put("common_city", "0"); //不同城出发
        }
        isSelected = drivingMoreBean.onlyLookData.get(1).isSelected;
        if (isSelected) {
            params.put("common_city_start", "1"); //同城发布
        } else {
            params.put("common_city_start", "0"); //不同城发布
        }
        ArrayList<DataItem> journeyData = drivingMoreBean.journeyData;
        for (DataItem data : journeyData) {
            if (data.isSelected) {
                params.put("date", data.id + ""); //行程
                break;
            }
        }
        if (drivingMoreBean.distance==0||drivingMoreBean.distance==100){
            params.put("distance", ""); //发帖距离
        }else {
            params.put("distance", (int)drivingMoreBean.distance/2 + ""); //发帖距离
        }
        ArrayList<DataItem> stateData = drivingMoreBean.stateData;
        stringBuffer = new StringBuffer();
        for (DataItem t : stateData) {
            if (t.isSelected){
                stringBuffer.append(t.id).append(",");
            }
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); //调用 字符串的deleteCharAt() 方法,删除最后一个多余的逗号
        }
        params.put("activity_status", stringBuffer.toString()); //活动状态*/
        /*params.put("city_id", mApp.cityId + ""); //当前城市id
        params.put("longitude", mApp.longitude); //
        params.put("latitude", mApp.latitude); //*/

        return params;
    }

}
