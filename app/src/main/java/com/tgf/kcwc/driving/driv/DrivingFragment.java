package com.tgf.kcwc.driving.driv;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hyphenate.util.DensityUtil;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.app.SelectAddressActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.entity.RichEditorEntity;
import com.tgf.kcwc.fragments.TabPlayHomeFragment;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.DrivingMoreBean;
import com.tgf.kcwc.mvp.model.DrvingListModel;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.presenter.DrivingDataPrenter;
import com.tgf.kcwc.mvp.view.DrivDataView;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.LocationUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownMoreSpinner;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.PleaseDownCitySpinner;
import com.tgf.kcwc.view.SmoothListView.SmoothListView;
import com.tgf.kcwc.view.VacancyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tgf.kcwc.R.id.more;


/**
 * Created by Administrator on 2017/4/17.
 */

public class DrivingFragment extends BaseFragment
        implements DrivDataView, SmoothListView.ISmoothListViewListener {

    public static final int MTYPENUM = 0;
    public static final int MDESTINATIONNUM = 1;
    public static final int MGOTIMENUM = 2;
    public static final int MORE = 3;

    private SmoothListView mSmoothListview;
    private DrivingDataPrenter drivingDataPrenter;
    private int page = 1;
    private boolean isRefresh = true;
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
    private LinearLayout drivingSelect;                         //真选择框
    private LinearLayout mType;                                 //选择类型
    private LinearLayout mDestination;                          //选择目的地
    private LinearLayout mGoTime;                               //选择时间
    private TextView mMore;                               //选择更多
    private DropDownSingleSpinner mGoTimeDropDownSingleSpinner;          //选择时间的下拉框
    private PleaseDownCitySpinner mPleaseDownDepartCitySpinner;                 //选择出发城市
    private PleaseDownCitySpinner mPleaseDownCitySpinner;                 //选择目的城市
    private DropDownMoreSpinner mDropDownMoreSpinner;                 //更多
    private List<DataItem> mDataList = new ArrayList<>();//type数据
    private List<DataItem> mTimeDataList = new ArrayList<>();//time数据
    private int filterPosition = 0;                // 点击FilterView的位置：类型(0)、目的地(1)、时间(2)
    private String destinationString = ""; //目的地城市
    private String DepartString = ""; //出发地城市
    private String goTimeString = ""; //时间
    public List<DrvingListModel.DataList> dataList = new ArrayList<>();
    public TabPlayHomeFragment tabPlayHomeFragment;
    public int type = 1;
    private MainActivity activity;
    DrivingMoreBean drivingMoreBean; //更多数据
    KPlayCarApp mApp;
    String startlongitude = ""; //开始的经度
    String startlatitude = ""; //开始的纬度
    String endlongitude = ""; //结束的经度
    String endlatitude = ""; //结束的纬度

    public DrivingFragment(TabPlayHomeFragment tabPlayHomeFragm, int type) {
        this.tabPlayHomeFragment = tabPlayHomeFragm;
        this.type = type;
    }

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

    public void gainMoreDataList() {
        drivingMoreBean = new DrivingMoreBean();
        drivingMoreBean.isDistance = true;
        List<DataItem> mDataList;
        DataItem dataItem = null;
        mDataList = new ArrayList<>();
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.isSelected = false;
        dataItem.name = "只看精华";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.isSelected = false;
        dataItem.name = "同城发布";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.isSelected = false;
        dataItem.name = "同城出发";
        mDataList.add(dataItem);
        drivingMoreBean.onlyLookData.addAll(mDataList);

        mDataList = new ArrayList<>();
        dataItem = new DataItem();
        dataItem.id = 0;
        dataItem.isSelected = false;
        dataItem.name = "报名中";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.isSelected = false;
        dataItem.name = "进行中";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.isSelected = false;
        dataItem.name = "已结束";
        mDataList.add(dataItem);
        drivingMoreBean.stateData.addAll(mDataList);

        mDataList = new ArrayList<>();
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.isSelected = false;
        dataItem.name = "1日";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.isSelected = false;
        dataItem.name = "2日";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.isSelected = false;
        dataItem.name = "3日";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 4;
        dataItem.isSelected = false;
        dataItem.name = "3日以上";
        mDataList.add(dataItem);
        drivingMoreBean.journeyData.addAll(mDataList);


        mDataList = new ArrayList<>();
        dataItem = new DataItem();
        dataItem.id = 6;
        dataItem.isSelected = false;
        dataItem.name = "自驾游";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 5;
        dataItem.isSelected = false;
        dataItem.name = "召集令";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 7;
        dataItem.isSelected = false;
        dataItem.name = "其他";
        mDataList.add(dataItem);
        drivingMoreBean.typekData.addAll(mDataList);
        drivingMoreBean.distance = 0;
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
        dataItem.name = "今天";
        mTimeDataList.add(dataItem);

        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "明天";
        mTimeDataList.add(dataItem);

        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.name = "三日内";
        mTimeDataList.add(dataItem);


        dataItem = new DataItem();
        dataItem.id = 4;
        dataItem.name = "一周内";
        mTimeDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 5;
        dataItem.name = "一月内";
        mTimeDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 6;
        dataItem.name = "三月内";
        mTimeDataList.add(dataItem);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_driving;
    }


    @Override
    protected void initView() {
        gainTypeDataList();
        gainTimeDataList();
        gainMoreDataList();
        mApp = (KPlayCarApp) getActivity().getApplication();
        mSmoothListview = findView(R.id.listview);
        drivingSelect = findView(R.id.driving_select);
        mType = findView(R.id.listact_categroy);
        mDestination = findView(R.id.listact_kilometer);
        mGoTime = findView(R.id.listact_rank);
        mMore = findView(R.id.more);

        mType.setOnClickListener(this);
        mDestination.setOnClickListener(this);
        mGoTime.setOnClickListener(this);
        mMore.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mType, "出发地");
        FilterPopwinUtil.commonFilterTile(mDestination, "目的地");
        FilterPopwinUtil.commonFilterTile(mGoTime, "出发时间");

        mSmoothListview.setRefreshEnable(true);
        mSmoothListview.setLoadMoreEnable(true);
        mSmoothListview.setSmoothListViewListener(this);
        drivingDataPrenter = new DrivingDataPrenter();
        drivingDataPrenter.attachView(this);
        drivingDataPrenter.getListData(builderParams());
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
                int position = helper.getPosition();

                VacancyListView mHintLayout = helper.getView(R.id.hintlayout);
                LinearLayout mLinearLayout = helper.getView(R.id.driverlayout);
                SimpleDraweeView mSimpleDraweeView = helper.getView(R.id.driving_list_view_imageview);
                SimpleDraweeView icon = helper.getView(R.id.icon);
                TextView name = helper.getView(R.id.name);
                TextView distance = helper.getView(R.id.distance);
                LinearLayout driverLayout = helper.getView(R.id.driverlayout);
                ImageView mStateImageView = helper.getView(R.id.driving_list_view_state);
                ImageView mEliteImageView = helper.getView(R.id.driving_list_view_elite);
                TextView DynamicText = helper.getView(R.id.driving_list_view_dynamic);
                TextView LookText = helper.getView(R.id.driving_list_view_look);
                TextView LikeText = helper.getView(R.id.driving_list_view_like);
                TextView CommentText = helper.getView(R.id.driving_list_view_comment);
                TextView NumberText = helper.getView(R.id.driving_list_view_number);
                driverLayout.setVisibility(View.VISIBLE);
                mHintLayout.setVisibility(View.GONE);
                if (item.cover.equals("-1")) {
                    if (position == 0) {
                        mHintLayout.setVisibility(View.VISIBLE);
                        driverLayout.setVisibility(View.GONE);
                        mHintLayout.setmHintText("当前条件暂时没有数据");
                    } else {
                        mHintLayout.setVisibility(View.GONE);
                        driverLayout.setVisibility(View.INVISIBLE);
                    }
                } else {
                    name.setText(item.createUser.username);
                    distance.setText(item.distance + "km");
                    icon.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.createUser.avatar, 360, 360)));//item图片
                    mSimpleDraweeView
                            .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));//item图片
                    DynamicText.setText(item.title); //标题
                    LookText.setText(item.viewCount + "");//查看数量
                    LikeText.setText(item.diggCount + ""); //点赞数量
                    CommentText.setText(item.replyCount + "");//评论数量

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
    protected void updateData() {
        if (activity == null) {
            activity = (MainActivity) getActivity();
        }
    }

    private Map<String, String> builderParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("page", page + "");

        params.put("start_longitude ", startlongitude);//出发地
        params.put("start_latitude ", startlatitude);//出发地
        params.put("end_longitude", endlongitude);//目的地
        params.put("end_latitude", endlatitude);//目的地

        params.put("begin_time", goTimeString);//出发时间
        ArrayList<DataItem> typekData = drivingMoreBean.typekData;
        StringBuffer stringBuffer = new StringBuffer();
        for (DataItem t : typekData) {
            if (t.isSelected) {
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
        if (drivingMoreBean.distance == 0 || drivingMoreBean.distance == 100) {
            params.put("distance", ""); //发帖距离
        } else {
            params.put("distance", (int) drivingMoreBean.distance / 2 + ""); //发帖距离
        }
        ArrayList<DataItem> stateData = drivingMoreBean.stateData;
        stringBuffer = new StringBuffer();
        for (DataItem t : stateData) {
            if (t.isSelected) {
                stringBuffer.append(t.id).append(",");
            }
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); //调用 字符串的deleteCharAt() 方法,删除最后一个多余的逗号
        }
        params.put("activity_status", stringBuffer.toString()); //活动状态
        params.put("city_id", mApp.cityId + ""); //当前城市id
        params.put("longitude", mApp.longitude); //
        params.put("latitude", mApp.latitude); //

        return params;
    }

    @Override
    public void dataListSucceed(DrvingListModel drvingListModel) {
        mSmoothListview.stopLoadMore();
        mSmoothListview.stopRefresh();
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
            mSmoothListview.smoothScrollToPositionFromTop(3, DensityUtil.dip2px(mContext, titleLayoutHeigh - 3));
            super.handleMessage(msg);
        }
    };

    @Override
    public void dataBannerSucceed(BannerModel bannerModel) {
        mHeaderBannerView = new HeaderBannerView(getActivity());
        mHeaderBannerView.fillView(bannerModel.data, mSmoothListview);
        mHeaderFilterView = new HeaderFilterView(getActivity(), mHandler);
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
                //titleLayoutHeigh = DensityUtils.px2dp(mContext, titleLayout.getHeight()); //标题栏高度
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
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            seleDownSingleSpinner(filterPosition);
                        }
                    }, 100);//3秒后执行Runnable中的run方法

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
        page = 1;
        drivingDataPrenter.getListData(builderParams());
    }

    @Override
    public void onLoadMore() {
        page++;
        drivingDataPrenter.getListData(builderParams());
    }

    public void seleDownSingleSpinner(int number) {
        if (number == MTYPENUM) {  //出发城市
          /*  mPleaseDownDepartCitySpinner = new PleaseDownCitySpinner(getActivity());
            FilterPopwinUtil.commonFilterTitleColor(mRes, mType, R.color.text_color10);
            FilterPopwinUtil.commonFilterImage(mType, R.drawable.filter_drop_down_green);
            mPleaseDownDepartCitySpinner.showAsDropDownBelwBtnView(drivingSelect);

            mPleaseDownDepartCitySpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mType, R.color.text_color15);
                    FilterPopwinUtil.commonFilterImage(mType, R.drawable.fitler_drop_down_n);
                    DataItem selectDataItem = mPleaseDownDepartCitySpinner.getSelectDataItem();
                    if (selectDataItem != null && mPleaseDownDepartCitySpinner.getIsSelec()) {
                        FilterPopwinUtil.commonFilterTile(mType, selectDataItem.name);
                        mHeaderFilterView.setmType(selectDataItem.name);
                        page = 1;
                        DepartString = selectDataItem.name;
                        drivingDataPrenter.getListData(builderParams());
                    }
                }
            });*/
            CommonUtils.startResultNewActivity(getActivity(), null, SelectAddressActivity.class,
                    Constants.InteractionCode.MAIN_PLAY_DRIVING_START);
        } else if (number == MDESTINATIONNUM) {   //目的城市
/*
            mPleaseDownCitySpinner = new PleaseDownCitySpinner(getActivity());
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
                        drivingDataPrenter.getListData(builderParams());
                    }
                }
            });
*/

            CommonUtils.startResultNewActivity(getActivity(), null, SelectAddressActivity.class,
                    Constants.InteractionCode.MAIN_PLAY_DRIVING_END);
        } else if (number == MORE) {
            mDropDownMoreSpinner = new DropDownMoreSpinner(getActivity(), drivingMoreBean);
            mDropDownMoreSpinner.showAsDropDownBelwBtnView(drivingSelect);
            mMore.setTextColor(mRes.getColor(R.color.text_color10));
            mDropDownMoreSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Logger.d(drivingMoreBean.onlyLookData);
                    mMore.setTextColor(mRes.getColor(R.color.text_color3));
                    DrivingMoreBean selectDataItem = mDropDownMoreSpinner.getSelectDataItem();
                    if (selectDataItem != null && mDropDownMoreSpinner.getIsSelec()) {
                        page = 1;
                        drivingMoreBean = selectDataItem;
                        drivingDataPrenter.getListData(builderParams());
                    }
                }
            });
        } else {
            mGoTimeDropDownSingleSpinner = new DropDownSingleSpinner(getActivity(), mTimeDataList);
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
                        drivingDataPrenter.getListData(builderParams());
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
            case more:
                seleDownSingleSpinner(MORE);
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

    @Override
    public void onAddress(int requestCode, int resultCode, Intent data) {
        super.onAddress(requestCode, resultCode, data);
        if (Constants.InteractionCode.MAIN_PLAY_DRIVING_START == requestCode && resultCode == getActivity().RESULT_OK) {
            Tip startTip = data.getParcelableExtra(Constants.IntentParams.DATA);
            String startCity = data.getStringExtra(Constants.IntentParams.DATA2);
            startlongitude = startTip.getPoint().getLongitude() + "";
            startlatitude = startTip.getPoint().getLatitude() + "";
            drivingDataPrenter.getListData(builderParams());
        }

        if (Constants.InteractionCode.MAIN_PLAY_DRIVING_END == requestCode && resultCode == getActivity().RESULT_OK) {
            Tip endTip = data.getParcelableExtra(Constants.IntentParams.DATA);
            String endCity = data.getStringExtra(Constants.IntentParams.DATA2);
            double locs[] = {endTip.getPoint().getLatitude(), endTip.getPoint().getLongitude()};
            endlongitude = endTip.getPoint().getLongitude() + "";
            endlatitude = endTip.getPoint().getLatitude() + "";
            drivingDataPrenter.getListData(builderParams());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
