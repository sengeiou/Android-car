package com.tgf.kcwc.finddiscount;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.DiscountLimitModel;
import com.tgf.kcwc.mvp.presenter.DiscountLimitPresenter;
import com.tgf.kcwc.mvp.view.LimitDiscountView;
import com.tgf.kcwc.see.StoreShowCarDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownBrandSpinner;
import com.tgf.kcwc.view.DropDownPriceView;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.countdown.CountdownView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/4/24 0024
 * E-mail:hekescott@qq.com
 */

public class LimitDiscountFragment extends BaseFragment implements LimitDiscountView {

    private LinearLayout mFilterLayout;
    private LinearLayout mBrandLayout;
    private LinearLayout mPriceLayout;
    private LinearLayout mSortLayout;
    private DropDownBrandSpinner mDropDownBrandSpinner;
    private DropDownPriceView mDropDownPriceView;
    private DropDownSingleSpinner mDropDownSortSpinner;
    private ArrayList<DataItem> mSortItems;
    private ListView mLimitLv;
    private DiscountLimitPresenter mDiscountLimitPresenter;
    private List<DiscountLimitModel.LimitDiscountItem> mlimitModellist = new ArrayList<>();
    private WrapAdapter limitAdapter;
    private DataItem priceDataItem = new DataItem();
    private int priceId;
    private int cityid = 290;
    private String carId = "";
    private String sereriesId = "";
    private Integer endPrice, brandId, orderId, special, startPrice,eventId;
    private int page = 1;
    private int pageSize = 10;
    private KPlayCarApp mKPlayCarApp;
    private TextView mEmptyBoxTv;

    @Override
    protected void updateData() {
       Integer tmpId = KPlayCarApp.getValue(Constants.IntentParams.EXHIBIT_ID);
        if (mKPlayCarApp.cityId != cityid||eventId!=tmpId) {
            isRefresh = true;
//            if(tmpId!=0)
            eventId = tmpId;
            cityid = mKPlayCarApp.cityId;
            mDiscountLimitPresenter.getLimitDiscountList(eventId,cityid, null, null, null, null, null, sereriesId, carId, page, pageSize);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.frag_limitdiscount;
    }

    @Override
    protected void initView() {
        initFilters();
        mKPlayCarApp = (KPlayCarApp) getActivity().getApplication();
        String temCarId = KPlayCarApp.getValue(Constants.IntentParams.CAR_ID);
        String temSerid = KPlayCarApp.getValue(Constants.IntentParams.SERIES_ID);
        if (!TextUtils.isEmpty(temSerid)) {
            sereriesId = temSerid;
        }
        if (!TextUtils.isEmpty(temCarId)) {
            carId = String.valueOf(temCarId);
        }
        if (mDiscountLimitPresenter == null) {
            Integer tmpId = KPlayCarApp.getValue(Constants.IntentParams.EXHIBIT_ID);
            eventId = tmpId;
//            if(tmpId==null)
//             = SharedPreferenceUtil.getExhibitId(mContext);
            isRefresh = true;
            cityid = mKPlayCarApp.cityId;
            mDiscountLimitPresenter = new DiscountLimitPresenter();
            mDiscountLimitPresenter.attachView(this);
            mDiscountLimitPresenter.getLimitDiscountList(eventId,cityid, null, null, null, null, null, sereriesId, carId, page, pageSize);
        }


        initRefreshLayout(mBGDelegate);
        mLimitLv = findView(R.id.discount_limit_lv);
        mEmptyBoxTv = findView(R.id.limitdiscount_emptytv);
        mLimitLv.addFooterView(View.inflate(getContext(), R.layout.bottom_hint_layout, null));
        if (limitAdapter != null) {
            mLimitLv.setAdapter(limitAdapter);
        }
        CheckBox checkBox = findView(R.id.limit_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    special = 1;
                } else {
                    special = 0;
                }
                isRefresh = true;
                mDiscountLimitPresenter.getLimitDiscountList(eventId,cityid, endPrice, brandId,
                        orderId, special, startPrice, sereriesId, carId, page, pageSize);
            }
        });
        initAdapter();
    }

    private boolean isRefresh;
    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {


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
        mDiscountLimitPresenter.getLimitDiscountList(eventId,cityid, endPrice, brandId,
                orderId, special, startPrice, sereriesId, carId, page, pageSize);
    }

    private void initFilters() {
        mFilterLayout = findView(R.id.limit_filterlayout);
        mBrandLayout = findView(R.id.limit_brand);
        mPriceLayout = findView(R.id.limit_price);
        mSortLayout = findView(R.id.limit_sort);
        mBrandLayout.setOnClickListener(this);
        mPriceLayout.setOnClickListener(this);
        mSortLayout.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mBrandLayout, "品牌");
        FilterPopwinUtil.commonFilterTile(mPriceLayout, "价格");
        FilterPopwinUtil.commonFilterTile(mSortLayout, "排序");
        mDropDownBrandSpinner = new DropDownBrandSpinner(getContext(),1,"1",0);
        mDropDownPriceView = new DropDownPriceView(getContext());
        mDropDownBrandSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout,
                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownBrandSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mBrandLayout, dataItem.name);
                    if (brandId == null || dataItem.id != brandId) {
                        brandId = dataItem.id;
                        mlimitModellist.clear();
                        page = 1;
                        mDiscountLimitPresenter.getLimitDiscountList(eventId,cityid, endPrice, brandId, orderId, special, startPrice, sereriesId, carId, page, pageSize);
                    }
                }
            }
        });
        mDropDownPriceView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout,
                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownPriceView.getSelectDataItem();
                if (dataItem != null) {
                    if (dataItem.id != priceId) {
                        priceId = dataItem.id;
                        priceDataItem = dataItem;
                        endPrice = dataItem.priceMax;
                        startPrice = dataItem.priceMin;
                        mlimitModellist.clear();
                        page = 1;
                        mDiscountLimitPresenter.getLimitDiscountList(eventId,cityid, endPrice, brandId, orderId, special, startPrice, sereriesId, carId, page, pageSize);
                    }
                    FilterPopwinUtil.commonFilterTile(mPriceLayout, "价格");
                }
            }
        });
        mSortItems = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.filter_discount_limit);
        int id = 1;
        for (String s : arrays) {
            DataItem dataItem = new DataItem();
            dataItem.name = s;
            dataItem.id = id;
            mSortItems.add(dataItem);
            id++;
        }
        mDropDownSortSpinner = new DropDownSingleSpinner(getContext(), mSortItems);
        mDropDownSortSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mSortLayout,
                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mSortLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownSortSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mSortLayout, "排序");
                    if (orderId == null || dataItem.id != orderId) {
                        orderId = dataItem.id;
                        mlimitModellist.clear();
                        page = 1;
                        mDiscountLimitPresenter.getLimitDiscountList(eventId,cityid, endPrice, brandId, orderId, special, startPrice, sereriesId, carId, page, pageSize);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.limit_price:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.filter_drop_down_r);
                mDropDownPriceView.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.limit_brand:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.filter_drop_down_r);
                mDropDownBrandSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.limit_sort:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mSortLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mSortLayout, R.drawable.filter_drop_down_r);
                mDropDownSortSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            default:
                break;
        }
    }

    @Override
    public void showLimitList(List<DiscountLimitModel.LimitDiscountItem> limitModellist) {
        if (isRefresh) {
            mlimitModellist.clear();
            isRefresh = false;
        }
        if (limitModellist != null && limitModellist.size() != 0) {
            mlimitModellist.addAll(limitModellist);
        }
        if (limitAdapter == null) {
            initAdapter();
        } else {
            limitAdapter.notifyDataSetChanged(mlimitModellist);
        }
        if(mlimitModellist==null||mlimitModellist.size()==0){
            mLimitLv.setVisibility(View.GONE);
            mEmptyBoxTv.setVisibility(View.VISIBLE);
        }else {
            mLimitLv.setVisibility(View.VISIBLE);
            mEmptyBoxTv.setVisibility(View.GONE);
        }

    }

    @Override
    public void showLimitListError() {
        if (mlimitModellist != null) {
            mlimitModellist.clear();
            if (limitAdapter != null)
                limitAdapter.notifyDataSetChanged(mlimitModellist);
        }
    }

    private void initAdapter() {
        limitAdapter = new WrapAdapter<DiscountLimitModel.LimitDiscountItem>(getContext(),
                R.layout.listitem_limitdiscount, mlimitModellist) {
            @Override
            public void convert(ViewHolder helper, final DiscountLimitModel.LimitDiscountItem item) {
                String coverUrl = URLUtil.builderImgUrl(item.carSeriesImg, 270, 203);
                helper.setSimpleDraweeViewURL(R.id.limit_cover, coverUrl);
                TextView teHuiTV = helper.getView(R.id.limit_istehui);
                if (item.special == 1) {
                    teHuiTV.setVisibility(View.VISIBLE);
                } else {
                    teHuiTV.setVisibility(View.GONE);
                }

                helper.setText(R.id.limit_title, item.title);
                helper.setText(R.id.limit_guideprice,
                        SpannableUtil.getDelLineString("¥" + item.guidePrice + "万"));
                helper.setText(R.id.limit_rate_guideprice, "¥" + item.rateGuidePrice + "万");
                helper.setText(R.id.limit_rate, item.rateText);
                TextView chountStatusTv = helper.getView(R.id.limit_settimetextitletv);
                CountdownView countdownView = helper.getView(R.id.limit_settimetext);
                if (DateFormatUtil.getTime(item.startTime) <= System.currentTimeMillis()) {
                    countdownView.start(
                            DateFormatUtil.getTime(item.endTime) - System.currentTimeMillis());
                    chountStatusTv.setVisibility(View.GONE);
                    countdownView.setVisibility(View.VISIBLE);
                } else {
                    countdownView.setVisibility(View.GONE);
                    chountStatusTv.setVisibility(View.VISIBLE);
                }
                View statusMoreIv = helper.getView(R.id.limit_orgmore);
                statusMoreIv.setVisibility(View.GONE);
                if (item.orgList != null && item.orgList.size() != 0) {
                    helper.setText(R.id.limit_org, item.orgList.get(0).name);
                    if (item.orgList.size() > 1) {
                        statusMoreIv.setVisibility(View.VISIBLE);
                    }
                }
                helper.getView(R.id.limit_gotodetailll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.CAR_ID, item.carId);
                        args.put(Constants.IntentParams.DATA2, item.orgList.get(0).id);
                        args.put(Constants.IntentParams.DATA3,
                                item.title);
                        CommonUtils.startNewActivity(mContext, args,
                                StoreShowCarDetailActivity.class);
//                        Intent toIntent = new Intent(getContext(), CarDetailActivity.class);
//                        toIntent.putParcelableArrayListExtra(Constants.IntentParams.DATA, item.orgList);
//                        toIntent.putExtra(Constants.IntentParams.ID, item.carId);
//                        startActivity(toIntent);

                    }
                });
                if (item.orgList.size() > 1) {
                    helper.getView(R.id.limitdiscount_morerl).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent toIntent = new Intent(getContext(), LimitMoreOrgActivity.class);
                            toIntent.putParcelableArrayListExtra(Constants.IntentParams.DATA, item.orgList);
                            toIntent.putExtra(Constants.IntentParams.ID, item.carId);
                            startActivity(toIntent);
                        }
                    });
                }
            }
        };
        mLimitLv.setAdapter(limitAdapter);
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

    @Override
    public void onDestroyView() {
        KPlayCarApp.removeValue(Constants.IntentParams.EXHIBIT_ID);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        KPlayCarApp.removeValue(Constants.IntentParams.SERIES_ID);
        super.onDestroy();
    }
}
