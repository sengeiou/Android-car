package com.tgf.kcwc.see;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.common.PriceViewBuilder;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.FilterItem;
import com.tgf.kcwc.mvp.model.SaleListModel;
import com.tgf.kcwc.mvp.presenter.OwnerSalePresenter;
import com.tgf.kcwc.mvp.view.OwnerSaleDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.ThreeLevelView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Author:Jenny
 * Date:16/12/9 11:16
 * E-mail:fishloveqin@gmail.com
 */
public class SaleFragment extends BaseFragment implements OwnerSaleDataView<SaleListModel> {
    private List<DataItem> mItems = null;
    private List<DataItem> mSortItems = null;
    private WrapAdapter<DataItem> mAdapter = null;
    private WrapAdapter<DataItem> mSortAdapter = null;
    private WrapAdapter<SaleListModel.SaleItemBean> mSalesAdapter;
    private View mAreaLayout, mPriceLayout, mVehicleLayout,
            mSortLayout;

    private GridView mFilterGridView;

    private ListView mListView;
    private RelativeLayout mFilterContent;

    private ListView mSortList;

    private OwnerSalePresenter mPresenter;

    private List<SaleListModel.SaleItemBean> mSalesItems = new ArrayList<SaleListModel.SaleItemBean>();
    private String mPriceKey = "";
    private String mPriceMin = "", mPriceMax = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void updateData() {
        reset();
        if (mPresenter != null) {
            mPresenter.loadOwnerSalesList(builderReqParams());
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sale;
    }

    private String mAreaId = "";
    private String mBrandId = "";
    private String mSort = "";
    private String mSeriesId = "";
    private ThreeLevelView areaThreeLevelView, goodsThreeLevelView;
    private WrapBrandLists mWrapBrandsLists;
    private PriceViewBuilder priceViewBuilder;
    private View footerView;

    @Override
    protected void initView() {
        initRefreshLayout(mBGDelegate);
        footerView = LayoutInflater.from(mContext).inflate(R.layout.bottom_hint_layout, null,
                false);
        mPresenter = new OwnerSalePresenter();
        mPresenter.attachView(this);
        KPlayCarApp app = ((KPlayCarApp) getActivity().getApplication());
        String cityName = app.locCityName;
        mAreaId = app.cityId + "";
        mBrandId = "";
        mSort = "";
        mSeriesId = KPlayCarApp.getValue(Constants.IntentParams.SERIES_ID);
        if (TextUtils.isEmpty(mSeriesId)) {
            mSeriesId = "";
        }
        // mPresenter.loadOwnerSalesList(builderReqParams());
        mFilterGridView = findView(R.id.filterGridView);
        mListView = findView(R.id.list);
        mFilterContent = findView(R.id.filterContent);
        initFilterData();
        mFilterGridView.setOnItemClickListener(mGridItemClickListener);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mAreaLayout = layoutInflater.inflate(R.layout.filter_area_list, null, false);
        RelativeLayout layout = (RelativeLayout) mAreaLayout;
        View v = layoutInflater.inflate(R.layout.three_level_view_region, layout, true);
        mPriceLayout = layoutInflater.inflate(R.layout.common_price_layout, null, false);

        priceViewBuilder = new PriceViewBuilder(mContext, (RelativeLayout) mPriceLayout, priceCallback);
        mVehicleLayout = layoutInflater.inflate(R.layout.filter_vehicle_list, null, false);
        LinearLayout layout2 = (LinearLayout) mVehicleLayout;
        initEmptyView();
        // View v2 = layoutInflater.inflate(R.layout.three_level_view_region, layout2, true);

        BuilderBrands builderBrands = new BuilderBrands(mContext, mVehicleLayout);
        builderBrands.setSingle(true);
        builderBrands.setDisplayChecked(false);
        builderBrands.setCallback(mCallback);
        builderBrands.loadData(1, "1", 0);


        mSortLayout = layoutInflater.inflate(R.layout.filter_sort_list, null, false);
        mSortList = (ListView) mSortLayout.findViewById(R.id.list);
        mSortList.setOnItemClickListener(mSortItemClickListener);
        mFilterContent.removeAllViews();
        mFilterContent.addView(mAreaLayout);
        mFilterContent.addView(mVehicleLayout);
        mFilterContent.addView(mPriceLayout);
        mFilterContent.addView(mSortLayout);

        initSortData();
        initSalesItemsData();


        //区域
        areaThreeLevelView = new ThreeLevelView(getActivity(), v, ThreeLevelView.FilterType.AREA,
                cityName);
        areaThreeLevelView.setOnSelectListener(new ThreeLevelView.OnSelectListener() {
            @Override
            public void onSelect(View v, FilterItem item, int position) {

                mAreaId = item.id + "";
                mAreaLayout.setVisibility(View.GONE);
                resetFilterUI(AREA_INDEX);
            }
        });


        //        //品类
        //        goodsThreeLevelView = new ThreeLevelView(getActivity(), v2, ThreeLevelView.FilterType.GOODS,
        //            "");
        //        goodsThreeLevelView.setOnSelectRootListener(new ThreeLevelView.OnSelectListener() {
        //            @Override
        //            public void onSelect(View v, FilterItem item, int position) {
        //
        //                mBrandId = item.id + "";
        //                mVehicleLayout.setVisibility(View.GONE);
        //                resetFilterUI(CATEGORY_INDEX);
        //            }
        //        });
        //
        //        goodsThreeLevelView.setOnSelectListener(new ThreeLevelView.OnSelectListener() {
        //            @Override
        //            public void onSelect(View v, FilterItem item, int position) {
        //
        //                mBrandId = item.id + "";
        //                mVehicleLayout.setVisibility(View.GONE);
        //                resetFilterUI(CATEGORY_INDEX);
        //            }
        //        });
    }


    @Override
    public void updateInfoByCity(String... args) {

//        String cityId = args[0];
//        if (areaThreeLevelView != null) {
//            areaThreeLevelView.setTag(cityId);
//        }
//        mAreaId = cityId;
//        System.out.println("updateInfoByCity:"+mPresenter);
//        reset();

    }

    private BuilderBrands.Callback mCallback = new BuilderBrands.Callback() {
        @Override
        public void refresh(ViewGroup parent, View view, Brand item, int position) {
            int id = item.brandId;
            mBrandId = "" + id;
            mVehicleLayout.setVisibility(View.GONE);
            resetFilterUI(CATEGORY_INDEX);
        }
    };

    private PriceViewBuilder.FilterPriceCallback priceCallback = new PriceViewBuilder.FilterPriceCallback() {
        @Override
        public void result(String customMin, String customMax) {

            loadFilterByPriceResult("", customMin, customMax, "");


        }

        @Override
        public void result(DataItem item) {

            loadFilterByPriceResult(item.title, "", "", item.key);
        }
    };

    /**
     * 有分页，如果有条件筛选，将之前分页状态重置
     */
    private void reset() {
        mPage = 0;
        mSalesItems.clear();
        if (isExist) {
            mListView.removeFooterView(footerView);
            isExist = false;
        }

    }

    private void loadFilterByPriceResult(String title, String min, String max, String key) {

        mPriceLayout.setVisibility(View.GONE);
        mPriceMin = min;
        mPriceMax = max;
        mPriceKey = key;

        Logger.d("mPriceMin:" + mPriceMin + ",mPriceMax:" + mPriceMax + ",key:" + key);
        if (!TextUtil.isEmpty(title)) {

            if ("不限".equals(title)) {
                mPriceMin = "0";
                mPriceMax = "0";
            } else if ("3万以下".equals(title)) {
                mPriceMin = "0";
                mPriceMax = "3";
            } else if ("100万以上".equals(title)) {
                mPriceMin = "100";
                mPriceMax = "0";
            } else {

                int index = title.indexOf("-");
                mPriceMin = title.substring(0, index);
                mPriceMax = title.substring(index + 1, title.length() - 1);
            }
        }
        resetFilterUI(PRICE_INDEX);

    }


    private Map<String, String> builderReqParams() {

        KPlayCarApp app = ((KPlayCarApp) getActivity().getApplication());
        Map<String, String> params = new HashMap<String, String>();
        params.put("area_id", app.cityId + "");
        params.put("brand_id", mBrandId);
        params.put("car_series_id", mSeriesId);
        params.put("max_price", mPriceMax + "");
        params.put("min_price", mPriceMin + "");
        params.put("sort", mSort);
        params.put("page", mPage + "");
        params.put("pageSize", pageSize + "");
        params.put("longitude", app.getLongitude());
        params.put("latitude", app.getLattitude());
        return params;
    }

    /**
     * 根据过滤项的position,重置对应的布局状态
     *
     * @param position
     */
    private void resetFilterUI(int position) {
        mItems.get(position).isSelected = false;
        mAdapter.notifyDataSetChanged();
        reset();
        refreshData();
    }

    //根据筛选条件刷新数据
    private void refreshData() {
        //mPresenter.loadOwnerSalesList(mAreaId, mBrandId, mSeriesId, mMaxPrice, mMinPrice, mSort);
        mPresenter.loadOwnerSalesList(builderReqParams());
    }

    private static final int AREA_INDEX = 0;
    private static final int CATEGORY_INDEX = 1;
    private static final int PRICE_INDEX = 2;
    private static final int SORT_INDEX = 3;
    private AdapterView.OnItemClickListener mSortItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

            DataItem dataItem = (DataItem) parent
                    .getAdapter()
                    .getItem(position);
            mSort = "" + position;
            dataItem.isSelected = true;
            singleChecked(mSortItems,
                    dataItem);
            mSortAdapter
                    .notifyDataSetChanged();
            mSortLayout
                    .setVisibility(
                            View.GONE);

            resetFilterUI(
                    SORT_INDEX);
        }
    };

    private AdapterView.OnItemClickListener mGridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

            DataItem dataItem = (DataItem) parent
                    .getAdapter()
                    .getItem(position);
            if (dataItem.isSelected) {
                dataItem.isSelected = false;
                mFilterContent
                        .setVisibility(
                                View.GONE);
            } else {
                dataItem.isSelected = true;
                mFilterContent
                        .setVisibility(
                                View.VISIBLE);
            }

            switch (position) {
                case AREA_INDEX:
                    mAreaLayout
                            .setVisibility(
                                    View.VISIBLE);
                    mPriceLayout
                            .setVisibility(
                                    View.GONE);
                    mVehicleLayout
                            .setVisibility(
                                    View.GONE);
                    mSortLayout
                            .setVisibility(
                                    View.GONE);
                    break;
                case CATEGORY_INDEX:
                    mVehicleLayout
                            .setVisibility(
                                    View.VISIBLE);
                    mPriceLayout
                            .setVisibility(
                                    View.GONE);
                    mAreaLayout
                            .setVisibility(
                                    View.GONE);
                    mSortLayout
                            .setVisibility(
                                    View.GONE);
                    break;
                case PRICE_INDEX:
                    mPriceLayout
                            .setVisibility(
                                    View.VISIBLE);
                    mAreaLayout
                            .setVisibility(
                                    View.GONE);
                    mVehicleLayout
                            .setVisibility(
                                    View.GONE);
                    mSortLayout
                            .setVisibility(
                                    View.GONE);
                    break;
                case SORT_INDEX:
                    mSortLayout
                            .setVisibility(
                                    View.VISIBLE);
                    mPriceLayout
                            .setVisibility(
                                    View.GONE);
                    mAreaLayout
                            .setVisibility(
                                    View.GONE);
                    mVehicleLayout
                            .setVisibility(
                                    View.GONE);
                    break;
            }
            singleChecked(mItems,
                    dataItem);
            mAdapter
                    .notifyDataSetChanged();
        }
    };

    private void initFilterData() {
        mItems = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.filter_lists);

        int id = 0;
        for (String s : arrays) {
            DataItem dataItem = new DataItem();
            dataItem.name = s;
            dataItem.id = id;
            mItems.add(dataItem);
            id++;
        }
        mAdapter = new WrapAdapter<DataItem>(mContext, R.layout.common_filter_layout2, mItems) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView tv = helper.getView(R.id.filterTitle);
                ImageView imageView = helper.getView(R.id.filterImg);
                if (item.isSelected) {
                    imageView.setImageResource(R.drawable.filter_drop_down_r);
                    tv.setSelected(true);
                } else {
                    imageView.setImageResource(R.drawable.fitler_drop_down_n);
                    tv.setSelected(false);
                }
                tv.setText(item.name);

            }
        };
        mFilterGridView.setAdapter(mAdapter);
    }

    private void initSortData() {
        mSortItems = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.filter_sort);

        int id = 0;
        for (String s : arrays) {
            DataItem dataItem = new DataItem();
            dataItem.name = s;
            dataItem.id = id;
            mSortItems.add(dataItem);
            id++;
        }
        mSortAdapter = new WrapAdapter<DataItem>(mContext, R.layout.common_list_item, mSortItems) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView tv = helper.getView(R.id.title);
                ImageView imageView = helper.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                    tv.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                } else {
                    imageView.setVisibility(View.GONE);
                    tv.setTextColor(mRes.getColor(R.color.text_color12));
                }
                tv.setText(item.name);

            }
        };
        mSortList.setAdapter(mSortAdapter);
    }

    private void initSalesItemsData() {

        mSalesAdapter = new WrapAdapter<SaleListModel.SaleItemBean>(mContext,
                R.layout.owner_sale_list_item, mSalesItems) {

            @Override
            public void convert(ViewHolder helper, SaleListModel.SaleItemBean item) {

                TextView title1 = helper.getView(R.id.title1);
                title1.setText(item.title);
                SimpleDraweeView img = helper.getView(R.id.img);
                img.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
                TextView content1 = helper.getView(R.id.content1);

                TextView moneyTag = helper.getView(R.id.moneyTag);

                String price = CommonUtils.getMoneyType(item.price + "");
                if (item.price == 0) {
                    price = "面议";
                    moneyTag.setText("");
                } else {
                    moneyTag.setText("￥");
                }

                content1.setText(price);
                TextView content2 = helper.getView(R.id.content2);
                content2.setText(item.buyYear + "  |  " + item.roadHaul + "万公里");
                TextView content3 = helper.getView(R.id.content3);
                Date date = new Date();
                date.setTime(DateFormatUtil.getTime(item.createTime));
                content3.setText(DateFormatUtil.timeLogic(date));
                TextView area = helper.getView(R.id.area);
                if (item.hasEvent == 1) {
                    area.setText(item.eventName);
                } else {
                    area.setText(item.area);
                }

            }
        };

        mListView.setAdapter(mSalesAdapter);
        mListView.setOnItemClickListener(mItemListener);
    }

    private AdapterView.OnItemClickListener mItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            SaleListModel.SaleItemBean bean = (SaleListModel.SaleItemBean) parent.getAdapter()
                    .getItem(position);
            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, bean.id);
            CommonUtils.startNewActivity(mContext, args, com.tgf.kcwc.see.sale.SaleDetailActivity.class);
//            CommonUtils.startNewActivity(mContext, args, com.tgf.kcwc.see.sale.release.SaleDetailActivity.class);
        }
    };

    private int mCount;

    @Override
    public void showData(SaleListModel saleListModel) {
        mCount = saleListModel.pagination.count;
        mSalesItems.addAll(saleListModel.list);
        int size = mSalesItems.size();
        if (size == 0) {
            mListView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);

        } else {
            mListView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            mSalesAdapter.notifyDataSetChanged();
        }


    }

    private boolean isExist;

    @Override
    public void setLoadingIndicator(boolean active) {

        if (isLoading) {
            showLoadingIndicator(active);
        }
        if (!active) {
            stopRefreshAll();
        }

    }

    @Override
    public void showLoadingTasksError() {
        stopRefreshAll();
        dismissLoadingDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        KPlayCarApp.removeValue(Constants.IntentParams.SERIES_ID);
        if (mPresenter != null) {
            mPresenter.detachView();

        }
        if (areaThreeLevelView != null) {
            areaThreeLevelView.stop();
        }
        if (goodsThreeLevelView != null) {
            goodsThreeLevelView.stop();
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


    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            loadMore();
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {

        if (mSalesItems.size() >= mCount) {
            if (!isExist) {
                mListView.addFooterView(footerView);
                isExist = true;
            }

            CommonUtils.showToast(mContext, "暂无最新数据！");
            return;
        }

        mPage++;
        mPresenter.loadOwnerSalesList(builderReqParams());
    }

    private int mPage;
    private int pageSize = 10;

}
