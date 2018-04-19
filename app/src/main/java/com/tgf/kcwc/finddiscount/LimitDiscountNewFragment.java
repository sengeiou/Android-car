package com.tgf.kcwc.finddiscount;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.DiscountLimitNewModel;
import com.tgf.kcwc.mvp.model.LimitDiscountEveModel;
import com.tgf.kcwc.mvp.model.SelectExbitionModel;
import com.tgf.kcwc.mvp.presenter.DiscountLimitNewPresenter;
import com.tgf.kcwc.mvp.view.LimitNewDiscountView;
import com.tgf.kcwc.ticket.PreRegistrationActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DiscountsPriceViewBuilder;
import com.tgf.kcwc.view.DropDownBrandSpinner;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.MyGridView;
import com.tgf.kcwc.view.MyListView;
import com.tgf.kcwc.view.countdown.CountdownView;
import com.tgf.kcwc.view.countdown.DynamicConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import rx.functions.Action1;

/**
 * Auther: Scott
 * Date: 2017/4/24 0024
 * E-mail:hekescott@qq.com
 */

public class LimitDiscountNewFragment extends BaseFragment implements LimitNewDiscountView {

    private DiscountLimitNewPresenter mDiscountLimitNewPresenter;

    private static final int SELECT_EXHIBITION = 1001;                   //选择展会
    private static final int SELECT_CITY = 1002;                   //选择展会
    private LinearLayout mFilterLayout;
    private LinearLayout mlimitTypeLayout; //选择类型
    private LinearLayout mBrandLayout; //选择品牌
    private LinearLayout mPriceLayout; //价格
    private LinearLayout mlimitSeat; //座位
    private LinearLayout mTimeLimit; //限时展会选项布局
    private LinearLayout mExhibitionLayout; //选择展会布局
    private TextView mFilterTitle; //展会名字

    private LinearLayout mBrandlayout; //展期特惠品牌布局
    private LinearLayout mUnwindLayout; //展开收起布局
    private TextView mUnwindText; //展开收起文字

    private ImageView mUnwindImag; //展开收起文字

    private boolean isRefresh = true;
    private int page = 1;
    LimitDiscountEveModel mLimitDiscountEveModel = null;
    private MyListView mLimitLv;
    private List<DiscountLimitNewModel.LimitDiscountItem> mlimitModellist = new ArrayList<>();
    private WrapAdapter<DiscountLimitNewModel.LimitDiscountItem> limitAdapter;
    private WrapAdapter<LimitDiscountEveModel.DataList> specialAdapter;
    private List<LimitDiscountEveModel.DataList> specialModellist = new ArrayList<>();

    private MyGridView myGridView;
    private WrapAdapter<LimitDiscountEveModel.FactoryListS> brandAdapter;
    private List<LimitDiscountEveModel.FactoryListS> brandAlllist = new ArrayList<>();
    private List<LimitDiscountEveModel.FactoryListS> brandShowlist = new ArrayList<>();
    private List<LimitDiscountEveModel.FactoryListS> brandPortionlist = new ArrayList<>();

    private KPlayCarApp mKPlayCarApp;
    private TextView mEmptyBoxTv; //没有数据的时候


    private DropDownSingleSpinner mTypeDropDownSingleSpinner;          //选择类型的下拉框
    private List<DataItem> mTypeDataList = new ArrayList<>();//类型数据
    DataItem mTypedata = null;  //类型

    private DropDownBrandSpinner mDropDownBrandSpinner; //品牌下拉框
    private List<DataItem> mSeatDataList = new ArrayList<>();//座位数据
    private int brandId; //品牌ID
    private String brandName; //品牌名字


    private DiscountsPriceViewBuilder priceViewBuilder;//价格下拉框
    private String priceMaxStr = "";
    private String priceMinStr = "";

    private DropDownSingleSpinner mSeatDropDownSingleSpinner;          //选择座位的下拉框
    DataItem mSeatdata = null;  //座位
    private boolean IsUnwind = false; //是否展开
    private boolean IsUnwinds = true;//是否刷新
    private String eventId = "";

    public LimitDiscountEveModel.Event event = null;

    private String exhibitionbrandId = ""; //展会品牌ID
    private BGARefreshLayout rlModulenameRefresh;

    /**
     * 类型数据
     */
    public void gainTypeDataList() {
        mTypeDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.name = "限时优惠";
        mTypeDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "展期优惠";
        mTypeDataList.add(dataItem);

        mTypedata = mTypeDataList.get(0);
    }

    /**
     * 座位数据
     */
    public void gainSeatDataList() {
        mSeatDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = 0;
        dataItem.key = "";
        dataItem.name = "不限";
        mSeatDataList.add(dataItem);

        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.key = "1";
        dataItem.name = "2座";
        mSeatDataList.add(dataItem);

        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.key = "2";
        dataItem.name = "3座";
        mSeatDataList.add(dataItem);

        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.key = "3";
        dataItem.name = "4座";
        mSeatDataList.add(dataItem);

        dataItem = new DataItem();
        dataItem.id = 4;
        dataItem.key = "4";
        dataItem.name = "5座";
        mSeatDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 5;
        dataItem.key = "5";
        dataItem.name = "6座";
        mSeatDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 6;
        dataItem.key = "6";
        dataItem.name = "7座";
        mSeatDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 7;
        dataItem.key = "7";
        dataItem.name = "7座以上";
        mSeatDataList.add(dataItem);

        mSeatdata = mSeatDataList.get(0);
    }

    @Override
    public void updateInfoByCity(String... args) {
        super.updateInfoByCity(args);
        if (mTypedata.id == 1) {
            gain();
        }
    }

    @Override
    protected void updateData() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.frag_newlimitdiscount;
    }

    @Override
    protected void initView() {

        gainTypeDataList();
        gainSeatDataList();
        initFilters();
        mKPlayCarApp = (KPlayCarApp) getActivity().getApplication();
        String discountsType = KPlayCarApp.getValue(Constants.IntentParams.DISCOUNTS_TYPE);  //1 为限时优惠 2为展期优惠

        if (discountsType.equals(Constants.IntentParams.DISCOUNTS_TIME_LIMIT)) {
            String temSerid = KPlayCarApp.getValue(Constants.IntentParams.SERIES_ID);
            String temSeridName = KPlayCarApp.getValue(Constants.IntentParams.SERIES_NAME);
            if (!TextUtils.isEmpty(temSerid)) {
                brandId = Integer.parseInt(temSerid);
                brandName = temSeridName;
                mTypedata = mTypeDataList.get(0);
                KPlayCarApp.removeValue(Constants.IntentParams.SERIES_ID);
                KPlayCarApp.removeValue(Constants.IntentParams.SERIES_NAME);
            }
        } else if (discountsType.equals(Constants.IntentParams.DISCOUNTS_EXHIBITION)) {
            String tmpId = KPlayCarApp.getValue(Constants.IntentParams.EXHIBIT_ID)+"";
            if (tmpId != null) {
                eventId = tmpId + "";
                mTypedata = mTypeDataList.get(1);
                FilterPopwinUtil.commonFilterTile(mlimitTypeLayout, mTypedata.name);
                KPlayCarApp.removeValue(Constants.IntentParams.EXHIBIT_ID);
            }
        }

        mDiscountLimitNewPresenter = new DiscountLimitNewPresenter();
        mDiscountLimitNewPresenter.attachView(this);
        gain();

        initRefreshLayout(mBGDelegate);
        mLimitLv = findView(R.id.discount_limit_lv);
        myGridView = findView(R.id.mygridview);
        mEmptyBoxTv = findView(R.id.limitdiscount_emptytv);
        rlModulenameRefresh = findView(R.id.rl_modulename_refresh);
        mLimitLv.addFooterView(View.inflate(getContext(), R.layout.bottom_hint_layout, null));
        initAdapter(mlimitModellist);


        RxBus.$().register(Constants.IntentParams.SELECT_EXHIBITION)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        SelectExbitionModel mCurrentPartId = (SelectExbitionModel) o;
                        event = new LimitDiscountEveModel.Event();
                        event.eventId = mCurrentPartId.id;
                        event.eventName = mCurrentPartId.name + "";
                        event.cover = mCurrentPartId.cover + "";
                        eventId = mCurrentPartId.id + "";
                        mFilterTitle.setText(mCurrentPartId.name);
                        exhibitionbrandId = "";
                        IsUnwinds = true;
                        gain();
                    }
                });
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
            if (isRefresh == true) {
                page++;
                loadMore();
            }
            return false;
        }
    };

    public void loadMore() {
        if (mTypedata.id == 1) {
            specialAdapter = null;
            mTimeLimit.setVisibility(View.VISIBLE);
            mExhibitionLayout.setVisibility(View.GONE);
            mDiscountLimitNewPresenter.getDataLists(mSeatdata.key, brandId + "", priceMaxStr, priceMinStr, page, mKPlayCarApp.cityId + "");
        } else {
            limitAdapter = null;
            mTimeLimit.setVisibility(View.GONE);
            mExhibitionLayout.setVisibility(View.VISIBLE);
            mDiscountLimitNewPresenter.getEventDataLists(eventId, page, exhibitionbrandId);
        }
    }

    private void initFilters() {
        mFilterLayout = findView(R.id.limit_filterlayout);
        mBrandLayout = findView(R.id.limit_brand);
        mPriceLayout = findView(R.id.limit_price);
        mlimitSeat = findView(R.id.limit_seat);
        mTimeLimit = findView(R.id.timelimit);
        mlimitTypeLayout = findView(R.id.limit_type);
        mBrandlayout = findView(R.id.brandlayout);
        mUnwindLayout = findView(R.id.unwindlayout);
        mUnwindText = findView(R.id.unwindtext);
        mUnwindImag = findView(R.id.unwindimag);
        mExhibitionLayout = findView(R.id.exhibitionlayout);
        mFilterTitle = findView(R.id.fTitle);

        mBrandLayout.setOnClickListener(this);
        mPriceLayout.setOnClickListener(this);
        mlimitSeat.setOnClickListener(this);
        mlimitTypeLayout.setOnClickListener(this);
        mExhibitionLayout.setOnClickListener(this);
        mUnwindLayout.setOnClickListener(this);

        if (TextUtil.isEmpty(brandName)) {
            FilterPopwinUtil.commonFilterTile(mBrandLayout, "品牌");
        } else {
            FilterPopwinUtil.commonFilterTile(mBrandLayout, brandName);
        }
        FilterPopwinUtil.commonFilterTile(mPriceLayout, "价格");
        FilterPopwinUtil.commonFilterTile(mlimitSeat, "座位");
        FilterPopwinUtil.commonFilterTile(mlimitTypeLayout, mTypedata.name);
        mFilterTitle.setText("请选择展会");

        mDropDownBrandSpinner = new DropDownBrandSpinner(getContext(), 1, "1", 0);
        mDropDownBrandSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout, R.color.text_color15);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownBrandSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mBrandLayout, dataItem.name);
                    if (dataItem.id != brandId) {
                        brandId = dataItem.id;
                        gain();
                    }
                }
            }
        });

        mTypeDropDownSingleSpinner = new DropDownSingleSpinner(getActivity(), mTypeDataList);
        mTypeDropDownSingleSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mlimitTypeLayout, R.color.text_color15);
                FilterPopwinUtil.commonFilterImage(mlimitTypeLayout, R.drawable.fitler_drop_down_n);
                DataItem selectDataItem = mTypeDropDownSingleSpinner.getSelectDataItem();
                if (selectDataItem != null && mTypeDropDownSingleSpinner.getIsSelec()) {
                    FilterPopwinUtil.commonFilterTile(mlimitTypeLayout, selectDataItem.name);
                    if (selectDataItem.id != mTypedata.id) {
                        mTypedata = selectDataItem;
                        IsUnwinds = true;
                        IsUnwind = false;
                        mUnwindText.setText("所有优惠品牌");
                        mUnwindImag.setImageResource(R.drawable.btn_xiala);
                        gain();
                    }
                }
            }
        });
        mSeatDropDownSingleSpinner = new DropDownSingleSpinner(getActivity(), mSeatDataList);
        mSeatDropDownSingleSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mlimitSeat, R.color.text_color15);
                FilterPopwinUtil.commonFilterImage(mlimitSeat, R.drawable.fitler_drop_down_n);
                DataItem selectDataItem = mSeatDropDownSingleSpinner.getSelectDataItem();
                if (selectDataItem != null && mSeatDropDownSingleSpinner.getIsSelec()) {
                    FilterPopwinUtil.commonFilterTile(mlimitSeat, selectDataItem.name);
                    if (selectDataItem.id != mSeatdata.id) {
                        mSeatdata = selectDataItem;
                        gain();
                    }
                }
            }
        });

        priceViewBuilder = new DiscountsPriceViewBuilder(mContext, priceCallback);
        priceViewBuilder.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout, R.color.text_color15);
                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.fitler_drop_down_n);
            }
        });
    }

    private DiscountsPriceViewBuilder.FilterPriceCallback priceCallback = new DiscountsPriceViewBuilder.FilterPriceCallback() {
        @Override
        public void result(String customMin, String customMax) {
            priceViewBuilder.dismiss();
            priceMaxStr = customMax;
            priceMinStr = customMin;
            FilterPopwinUtil.commonFilterTile(mPriceLayout, customMin + "-" + customMax + "万");
            gain();
            // loadFilterByPriceReuslt(customMin + "-" + customMax + "万", customMin, customMax, "");


        }

        @Override
        public void result(DataItem item) {
            priceViewBuilder.dismiss();
            priceMaxStr = item.priceMaxStr;
            priceMinStr = item.priceMinStr;
            FilterPopwinUtil.commonFilterTile(mPriceLayout, item.title);
            //loadFilterByPriceReuslt(item.title, "", "", item.key);
            gain();

        }
    };

    public void gain() {
        page = 1;
        isRefresh = true;
        loadMore();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.limit_brand:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout, R.color.text_color10);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.filter_drop_down_green);
                mDropDownBrandSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.limit_price:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout, R.color.text_color10);
                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.filter_drop_down_green);
                priceViewBuilder.clear();
                priceViewBuilder.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.limit_seat:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mlimitSeat, R.color.text_color10);
                FilterPopwinUtil.commonFilterImage(mlimitSeat, R.drawable.filter_drop_down_green);
                mSeatDropDownSingleSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.limit_type:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mlimitTypeLayout, R.color.text_color10);
                FilterPopwinUtil.commonFilterImage(mlimitTypeLayout, R.drawable.filter_drop_down_green);
                mTypeDropDownSingleSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.exhibitionlayout:
                Intent intent = new Intent(getActivity(), SelectExhibitionActivity.class);
                startActivityForResult(intent, SELECT_EXHIBITION);
                break;
            case R.id.unwindlayout:
                if (!IsUnwind) {
                    brandShowlist.clear();
                    brandShowlist.addAll(brandAlllist);
                    brandAdapter.notifyDataSetChanged();
                    IsUnwind = true;
                    mUnwindText.setText("收起优惠品牌");
                    mUnwindImag.setImageResource(R.drawable.btn_up);
                } else {
                    brandShowlist.clear();
                    brandShowlist.addAll(brandPortionlist);
                    brandAdapter.notifyDataSetChanged();
                    IsUnwind = false;
                    mUnwindText.setText("所有优惠品牌");
                    mUnwindImag.setImageResource(R.drawable.btn_xiala);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void showLimitList(List<DiscountLimitNewModel.LimitDiscountItem> limitModellist) {
        stopRefreshAll();
        if (page == 1) {
            mlimitModellist.clear();
        }
        if (limitModellist == null || limitModellist.size() == 0) {
            isRefresh = false;
        } else {
            mlimitModellist.addAll(limitModellist);
            if (limitAdapter == null) {
                initAdapter(mlimitModellist);
            } else {
                limitAdapter.notifyDataSetChanged(mlimitModellist);
            }
        }
        if (mlimitModellist == null || mlimitModellist.size() == 0) {
            isRefresh = false;
            rlModulenameRefresh.setVisibility(View.GONE);
            mEmptyBoxTv.setVisibility(View.VISIBLE);
        } else {
            rlModulenameRefresh.setVisibility(View.VISIBLE);
            mEmptyBoxTv.setVisibility(View.GONE);
        }

    }

    @Override
    public void showEveLimitList(LimitDiscountEveModel data) {
        List<LimitDiscountEveModel.DataList> list = data.list;
        mLimitDiscountEveModel = data;
        event = mLimitDiscountEveModel.event;
        stopRefreshAll();
        if (page == 1) {
            specialModellist.clear();
            if (IsUnwinds) {
                brandAlllist.clear();
                brandShowlist.clear();
                brandPortionlist.clear();
                brandAlllist.addAll(data.factoryList.list);
                if (brandAlllist.size() > 12) {
                    mUnwindLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < 12; i++) {
                        brandPortionlist.add(brandAlllist.get(i));
                    }
                } else {
                    mUnwindLayout.setVisibility(View.GONE);
                    brandPortionlist.addAll(brandAlllist);
                }
                brandShowlist.addAll(brandPortionlist);
                setBrandAdapter(brandShowlist);
            }
            IsUnwinds = false;
            mFilterTitle.setText(data.event.eventName);
            eventId = data.event.eventId + "";
        }
        if (list == null || list.size() == 0) {
            isRefresh = false;
        } else {
            specialModellist.addAll(list);
            if (specialAdapter == null) {
                initAdapters(specialModellist);
            } else {
                specialAdapter.notifyDataSetChanged();
            }
        }
        if (specialModellist == null || specialModellist.size() == 0) {
            isRefresh = false;
            mLimitLv.setVisibility(View.GONE);
            mEmptyBoxTv.setVisibility(View.VISIBLE);
        } else {
            mLimitLv.setVisibility(View.VISIBLE);
            mEmptyBoxTv.setVisibility(View.GONE);
        }

    }

    @Override
    public void showLimitListError() {
        CommonUtils.showToast(getActivity(), "系统异常");
    }

    /**
     * 找优惠
     */
    private void initAdapter(List<DiscountLimitNewModel.LimitDiscountItem> mlimitModellist) {
        mBrandlayout.setVisibility(View.GONE);
        limitAdapter = new WrapAdapter<DiscountLimitNewModel.LimitDiscountItem>(getContext(),
                R.layout.listitem_newlimitdiscount, mlimitModellist) {
            @Override
            public void convert(ViewHolder helper, final DiscountLimitNewModel.LimitDiscountItem item) {
                LinearLayout clicklayout = helper.getView(R.id.clicklayout);
                SimpleDraweeView mICon = helper.getView(R.id.icon);
                SimpleDraweeView mBrandicon = helper.getView(R.id.brandicon);
                MyGridView mDescribe = helper.getView(R.id.mygridview);
                TextView brandName = helper.getView(R.id.brand_name);
                TextView limitTitle = helper.getView(R.id.limit_title);
                TextView countericon = helper.getView(R.id.countericon);
                TextView limitOrg = helper.getView(R.id.limit_org);
                TextView limitSettimetextitletv = helper.getView(R.id.limit_settimetextitletv);
                CountdownView limitSettimetext = helper.getView(R.id.limit_settimetext);
                mICon.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
                mBrandicon.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.factoryLogo, 144, 144)));
                brandName.setText(item.factoryName);
                limitTitle.setText(item.title);
                if (item.org != null && item.org.size() != 0) {
                    if (item.org.size() == 1) {
                        limitOrg.setText(item.org.get(0).name);
                    } else {
                        limitOrg.setText(item.org.get(0).name + "等" + item.org.size() + "家经销商");
                    }
                } else {
                    limitOrg.setText(" ");
                }
                int benefitStatus = item.benefitStatus;
                if (benefitStatus == 1) {
                    limitSettimetextitletv.setVisibility(View.GONE);
                    limitSettimetext.setVisibility(View.VISIBLE);
                    countericon.setText("距活动开始：");
                    long time = DateFormatUtil.getTime(item.beginTime) - DateFormatUtil.getTime(item.currentTime);
                    limitSettimetext.start(time);
                    modification(limitSettimetext, time);
                } else if (benefitStatus == 2) {
                    limitSettimetextitletv.setVisibility(View.GONE);
                    limitSettimetext.setVisibility(View.VISIBLE);
                    countericon.setText("剩余：");
                    long time = DateFormatUtil.getTime(item.endTime) - DateFormatUtil.getTime(item.currentTime);
                    limitSettimetext.start(time);
                    modification(limitSettimetext, time);
                } else {
                    limitSettimetextitletv.setVisibility(View.VISIBLE);
                    limitSettimetext.setVisibility(View.GONE);
                    limitSettimetextitletv.setText("已结束");
                    countericon.setText("");
                }
                WrapAdapter<DiscountLimitNewModel.BenefitAttr> gridviewAdapter;
                gridviewAdapter = new WrapAdapter<DiscountLimitNewModel.BenefitAttr>(mContext, R.layout.listitem_newlimitdiscount_item, item.benefitAttr) {
                    @Override
                    public void convert(ViewHolder helper, DiscountLimitNewModel.BenefitAttr item) {
                        TextView name = helper.getView(R.id.name);
                        name.setText(item.value);
                    }
                };
                mDescribe.setAdapter(gridviewAdapter);

                clicklayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id + "");
                        CommonUtils.startNewActivity(mContext, args, LimitDiscountNewDetailsActivity.class);
                    }
                });
            }
        };
        mLimitLv.setAdapter(limitAdapter);
    }

    public void modification(CountdownView limitSettimetext, long time) {
        DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();

        if (time <= 24 * 60 * 60 * 1000) {
            dynamicConfigBuilder.setTimeTextSize(12)
                    .setTimeTextColor(0xFFffe53a)
                    .setTimeTextBold(false)
                    .setSuffixTextColor(0xFFffe53a)
                    .setSuffixTextSize(12)
                    .setSuffixTextBold(false)
                    .setSuffixDay("天").setSuffixHour("时").setSuffixMinute("分").setSuffixSecond("秒").setSuffixMillisecond("毫秒")
                    .setSuffixGravity(DynamicConfig.SuffixGravity.TOP)
                    .setShowDay(true).setShowHour(true).setShowMinute(true).setShowSecond(false).setShowMillisecond(false);
        } else {
            dynamicConfigBuilder.setTimeTextSize(12)
                    .setTimeTextColor(0xFF1e2124)
                    .setTimeTextBold(false)
                    .setSuffixTextColor(0xFF666666)
                    .setSuffixTextSize(12)
                    .setSuffixTextBold(false)
                    .setSuffixDay("天").setSuffixHour("时").setSuffixMinute("分").setSuffixSecond("秒").setSuffixMillisecond("毫秒")
                    .setSuffixGravity(DynamicConfig.SuffixGravity.TOP)
                    .setShowDay(true).setShowHour(true).setShowMinute(true).setShowSecond(false).setShowMillisecond(false);
        }
        limitSettimetext.dynamicShow(dynamicConfigBuilder.build());
    }

    /**
     * 展期特惠
     */
    private void initAdapters(List<LimitDiscountEveModel.DataList> specialModellist) {
        mBrandlayout.setVisibility(View.VISIBLE);

        specialAdapter = new WrapAdapter<LimitDiscountEveModel.DataList>(getContext(),
                R.layout.listitem_newlimitdisspecial, specialModellist) {
            @Override
            public void convert(ViewHolder helper, final LimitDiscountEveModel.DataList item) {
                LinearLayout clicklayout = helper.getView(R.id.clicklayout);
                SimpleDraweeView mICon = helper.getView(R.id.icon);
                SimpleDraweeView mBrandicon = helper.getView(R.id.brandicon);
                MyGridView mDescribe = helper.getView(R.id.mygridview);

                TextView brandName = helper.getView(R.id.brand_name);
                TextView limitTitle = helper.getView(R.id.limit_title);
                TextView specialoffertitle = helper.getView(R.id.specialoffertitle);
                TextView countericon = helper.getView(R.id.countericon);
                TextView appnumber = helper.getView(R.id.appnumber);
                TextView onlytext = helper.getView(R.id.onlytext);
                TextView transmit = helper.getView(R.id.transmit);
                TextView decline = helper.getView(R.id.decline);
                ImageView specialofferimage = helper.getView(R.id.specialofferimage);

                LinearLayout limitGotodetailll = helper.getView(R.id.limit_gotodetailll); //优惠
                LinearLayout specialoffer = helper.getView(R.id.specialoffer); //特价

                TextView limitSettimetextitletv = helper.getView(R.id.limit_settimetextitletv);
                CountdownView limitSettimetext = helper.getView(R.id.limit_settimetext);
                mICon.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
                mBrandicon.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.logo, 144, 144)));
                brandName.setText(item.factoryName);

                if (item.rateType == 0) {
                    decline.setVisibility(View.GONE);
                } else {
                    decline.setVisibility(View.VISIBLE);

                    switch (item.rateType) {
                        case 1:
                            decline.setText("最高优惠" + item.rateValue + "元");
                            decline.setTextColor(getResources().getColor(R.color.text_color2));
                            break;
                        case 2:
                            decline.setText(item.rateValue);
                            decline.setTextColor(getResources().getColor(R.color.text_color2));
                            break;
                        case 3:
                            decline.setText("  " + item.rateValue + "折");
                            decline.setTextColor(getResources().getColor(R.color.limit_yellow));
                            break;
                        case 4:
                            decline.setText("↓降" + item.rateValue + "元");
                            decline.setTextColor(getResources().getColor(R.color.text_color10));
                            break;
                    }

                }

                int benefitStatus = item.applyStatus;
                if (benefitStatus == 1) {
                    limitSettimetextitletv.setVisibility(View.GONE);
                    appnumber.setVisibility(View.GONE);
                    transmit.setVisibility(View.GONE);
                    limitSettimetext.setVisibility(View.VISIBLE);
                    countericon.setText("距活动开始：");
                    long time = DateFormatUtil.getTime(item.applyBeginTime) - DateFormatUtil.getTime(item.currentTime);
                    limitSettimetext.start(time);
                    modification(limitSettimetext, time);
                } else if (benefitStatus == 2) {
                    limitSettimetextitletv.setVisibility(View.GONE);
                    limitSettimetext.setVisibility(View.VISIBLE);
                    appnumber.setVisibility(View.VISIBLE);
                    transmit.setVisibility(View.VISIBLE);
                    countericon.setText("距报名结束仅剩：");
                    long time = DateFormatUtil.getTime(item.applyEndTime) - DateFormatUtil.getTime(item.currentTime);
                    limitSettimetext.start(time);
                    modification(limitSettimetext, time);
                    appnumber.setText("已报名" + item.number + "人");
                    transmit.setText("立即报名");
                    transmit.setBackground(getActivity().getResources().getDrawable(R.drawable.button_bg_limitdispink));
                    transmit.setFocusable(true);
                    transmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, event.eventId);
                            args.put(Constants.IntentParams.DATA, event.eventName);
                            args.put(Constants.IntentParams.DATA2, event.cover);
                            CommonUtils.startNewActivity(mContext, args, PreRegistrationActivity.class);
                        }
                    });
                } else {
                    limitSettimetextitletv.setVisibility(View.VISIBLE);
                    appnumber.setVisibility(View.VISIBLE);
                    transmit.setVisibility(View.VISIBLE);
                    limitSettimetext.setVisibility(View.GONE);
                    limitSettimetextitletv.setText("报名结束");
                    countericon.setText("");
                    transmit.setText("报名结束");
                    appnumber.setText("已报名" + item.number + "人");
                    transmit.setFocusable(false);
                    transmit.setBackground(getActivity().getResources().getDrawable(R.drawable.button_bg_limitdisgray));
                }
                String type = item.type;
                if (type.equals("sale")) {
                    limitGotodetailll.setVisibility(View.GONE);
                    specialoffer.setVisibility(View.VISIBLE);
                    specialofferimage.setVisibility(View.VISIBLE);
                    specialoffertitle.setText(item.title);
                    onlytext.setVisibility(View.VISIBLE);
                    onlytext.setText("仅限" + item.saleNum + "台");
                    appnumber.setVisibility(View.GONE);
                } else {
                    limitGotodetailll.setVisibility(View.VISIBLE);
                    limitTitle.setText(item.title);
                    specialoffer.setVisibility(View.GONE);
                    specialofferimage.setVisibility(View.GONE);
                    onlytext.setVisibility(View.GONE);
                    Log.i("TT", item.title);
                    appnumber.setVisibility(View.VISIBLE);
                }

                WrapAdapter<LimitDiscountEveModel.BenefitAttr> gridviewAdapter;
                gridviewAdapter = new WrapAdapter<LimitDiscountEveModel.BenefitAttr>(mContext, R.layout.listitem_newlimitdiscount_item, item.benefitAttr) {
                    @Override
                    public void convert(ViewHolder helper, LimitDiscountEveModel.BenefitAttr item) {
                        TextView name = helper.getView(R.id.name);
                        name.setText(item.value);
                    }
                };
                mDescribe.setAdapter(gridviewAdapter);

                clicklayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.benefitId + "");
                        CommonUtils.startNewActivity(mContext, args, LimitDiscountNewDetailsOddsActivity.class);
                    }
                });
            }
        };
        mLimitLv.setAdapter(specialAdapter);
    }

    public void setBrandAdapter(List<LimitDiscountEveModel.FactoryListS> brandPor) {
        brandAdapter = new WrapAdapter<LimitDiscountEveModel.FactoryListS>(getContext(),
                R.layout.listitem_newlimitdisspecial_brand, brandPor) {
            @Override
            public void convert(ViewHolder helper, final LimitDiscountEveModel.FactoryListS item) {
                SimpleDraweeView mICon = helper.getView(R.id.icon);
                LinearLayout bglayout = helper.getView(R.id.bglayout);
                if (item.factoryId == 0) {
                    mICon.setImageResource(R.drawable.btn_suoyouyouh);
                } else {
                    mICon.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.logo, 144, 144)));
                }
                if (!TextUtil.isEmpty(exhibitionbrandId)) {
                    if (exhibitionbrandId.equals(item.factoryId + "")) {
                        bglayout.setBackgroundResource(R.drawable.button_bg_frame_select);
                    } else {
                        bglayout.setBackgroundResource(R.drawable.button_bg_frame);
                    }
                } else {
                    bglayout.setBackgroundResource(R.drawable.button_bg_frame);
                }
                mICon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exhibitionbrandId.equals(item.factoryId + "")) {
                            exhibitionbrandId = "";
                        } else {
                            exhibitionbrandId = item.factoryId + "";
                        }
                        IsUnwinds = false;
                        brandAdapter.notifyDataSetChanged();
                        gain();
                    }
                });
            }
        };
        myGridView.setAdapter(brandAdapter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        if (isLoading) {
            if (active) {
                showLoadingIndicator(true);
            } else {
                stopRefreshAll();
                showLoadingIndicator(false);
            }
        }

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (KPlayCarApp.getValue(Constants.IntentParams.INDEX) == null) {
            return;
        }
        int index = KPlayCarApp.getValue(Constants.IntentParams.INDEX);
        if (Constants.IntentParams.SALE_DISCOUNTS == index) {
            KPlayCarApp.removeValue(Constants.IntentParams.INDEX);
            KPlayCarApp.removeValue(Constants.IntentParams.SERIES_ID);
        }

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser) {

            isLoading = true;
        } else {
            isLoading = false;
        }

    }

}
