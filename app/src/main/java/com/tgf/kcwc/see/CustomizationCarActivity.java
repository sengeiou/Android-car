package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headerfooter.songhang.library.SmartRecyclerAdapter;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.common.PriceViewBuilder;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.HotTag;
import com.tgf.kcwc.mvp.model.MotoModel;
import com.tgf.kcwc.mvp.model.SeriesModel;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.presenter.SetCustomizationPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.mvp.view.SetCustomizationView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GridSpacingItemDecoration;
import com.tgf.kcwc.view.OnClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.tgf.kcwc.R.id.brandLayout;
import static com.tgf.kcwc.R.id.confirmBtn;
import static com.tgf.kcwc.R.id.setPriceLayout;

/**
 * Author:Jenny
 * Date:16/12/9 11:16
 * E-mail:fishloveqin@gmail.com
 * 看定制
 */
public class CustomizationCarActivity extends BaseActivity implements CarDataView<SeriesModel>,
        OnItemClickListener<CarBean>, OnClickListener<CarBean> {
    protected View rootView;
    protected RecyclerView mCustomLists;
    private MultiTypeAdapter mMultiAdapter;
    private Items items;
    private CarDataPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private LinearLayout mBrandLayout;
    private LinearLayout mPriceLayout;
    private LinearLayout mLevelLayout;
    private LinearLayout mSeatLayout;
    private ImageView mMoreFilterView;
    private ArrayList<DataItem> datas = new ArrayList<DataItem>();
    private CommonAdapter<DataItem> mCustomListAdapter = null;
    private WrapAdapter<DataItem> mFilterSeatListAdapter, mFilterLevelAdapter = null;
    private RelativeLayout customModelList;
    private ListView filterSeatList, filterCarLevelList;
    private ArrayList<DataItem> mSeatLists = new ArrayList<DataItem>();


    private ArrayList<DataItem> mCarLevelLists = new ArrayList<DataItem>();
    private RelativeLayout mSetPriceLayout;

    private String mPriceMin = "", mPriceMax = "";

    private RelativeLayout mSetBrandsLayout;
    //private WrapBrandLists mWrapBrandsLists;
    private RelativeLayout mMoreFilterLayout;

    private DataItem mPriceItem = new DataItem();
    private DataItem mSeatItem = new DataItem();
    private DataItem mBrandItem = new DataItem();
    private DataItem mLevelItem = new DataItem();
    private RelativeLayout mFilterItems;
    private TextView mCustomBtn;
    private PriceViewBuilder priceViewBuilder;
    private BuilderBrands builderBrands = null;
    private SetCustomizationPresenter mSetCustomizationPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();

        mHotTag = intent.getParcelableExtra(Constants.IntentParams.DATA);


        //根据热搜类型增加条件标签
        if (mHotTag != null) {

            String type = mHotTag.type;

            switch (type) {

                case "price":

                    String priceSplit[] = mHotTag.value.split("-");
                    mPriceMin = priceSplit[0];
                    mPriceMax = priceSplit[1];

                    mPriceItem.name = mPriceMin + "-" + mPriceMax + "万元";
                    mPriceItem.type = setPriceLayout;
                    mPriceKey = "";
                    datas.add(mPriceItem);
                    break;
                case "level":
                    mLevelItem.name = mHotTag.text;
                    mLevelItem.type = R.id.level;
                    mLevelItem.key = mHotTag.value;
                    mLevel = mHotTag.value;
                    datas.add(mLevelItem);
                    break;
                case "set":
                    mSeatItem.name = mHotTag.text;
                    mSeatItem.type = R.id.filterSeatList;
                    mSeatItem.key = mHotTag.value;
                    mSeatKey = mHotTag.value;
                    datas.add(mSeatItem);
                    break;
                case "brand":

                    mBrandItem.id = Integer.parseInt(mHotTag.value);
                    mBrandItem.type = R.id.brandLayout;
                    mBrandItem.name = mHotTag.text;
                    mBrandId = mBrandItem.id + "";
                    datas.add(mBrandItem);
                    break;
            }

        }
        setContentView(R.layout.activity_customization_car);

    }

    private HotTag mHotTag = null;

    @Override
    protected void setUpViews() {

        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("条件选车");
        function.setTextResource("重置", R.color.white, 14);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                resetAll();
            }
        });
    }

    private String mBrandId = "";
    private String mSeatKey = "";
    private int mPage = 1;
    private String mPriceKey = "";
    private String mLevel = "";
    private String seatArrays[] = null, carlevelArrays[] = null;
    private TextView mMatchesTv;
    private int pageSize = 10;

    protected void updateData() {

        if (mPresenter != null) {
            mPresenter.loadDatas(builderReqParams());

        }


    }


    private void initFilterListsData() {

        seatArrays = mRes.getStringArray(R.array.seat_values);
        int size = 0;

        //先清空集合数据，防止重复增加
        mSeatLists.clear();
        size = seatArrays.length;
        for (int i = 0; i < size; i++) {
            DataItem dataItem = new DataItem();
            dataItem.name = seatArrays[i];
            dataItem.id = (i + 1);
            dataItem.key = i + "";
            dataItem.type = R.array.seat_values;
            mSeatLists.add(dataItem);
        }


        carlevelArrays = mRes.getStringArray(R.array.cars_levels);
        mCarLevelLists.clear();
        size = carlevelArrays.length;

        for (int i = 0; i < size; i++) {
            DataItem dataItem = new DataItem();
            dataItem.name = carlevelArrays[i];
            dataItem.id = (i + 1);
            dataItem.key = (i + 1) + "";
            dataItem.type = R.array.cars_levels;
            mCarLevelLists.add(dataItem);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        updateData();
    }

    protected void initView() {
        initRefreshLayout(mBGDelegate);
        initEmptyView();
        mMoreFilterLayout = (RelativeLayout) findViewById(R.id.moreFilterLayout);
        mFilterItems = (RelativeLayout) findViewById(R.id.filterItems);
        mCustomBtn = (TextView) findViewById(R.id.customBtn);
        mMatchesTv = (TextView) findViewById(R.id.matchesTv);
        mCustomBtn.setOnClickListener(this);
        mMoreFilterLayout.setOnClickListener(this);
        mPresenter = new CarDataPresenter();
        mSetCustomizationPresenter = new SetCustomizationPresenter();
        mSetCustomizationPresenter.attachView(mSetCustomizationView);
        mPresenter.attachView(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setNestedScrollingEnabled(false);
        items = new Items();
        mMultiAdapter = new MultiTypeAdapter(items);
        SeriesItemViewProvider provider = new SeriesItemViewProvider();
        mMultiAdapter.register(CarBean.class, provider);

        smartRecyclerAdapter = new SmartRecyclerAdapter(mMultiAdapter);
        View footerView = LayoutInflater.from(mContext).inflate(R.layout.bottom_hint_layout, null,
                false);
        smartRecyclerAdapter.setFooterView(footerView);
        mRecyclerView.setAdapter(smartRecyclerAdapter);
        provider.setOnItemClickListener(this);
        provider.setOnClickListener(this);
        initFilterViews();
        mCustomLists = (RecyclerView) findViewById(R.id.custom_lists);

        int spanCount = 1; // 只显示一行
        int spacing = BitmapUtils.dp2px(mContext, 0); // 50px
        boolean includeEdge = true;
        mCustomLists
                .addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount,
                StaggeredGridLayoutManager.HORIZONTAL);

        mCustomLists.setLayoutManager(layoutManager);
        showFilterItems();
        mCustomListAdapter = new CommonAdapter<DataItem>(mContext, R.layout.text_tag_item, datas) {
            @Override
            public void convert(ViewHolder holder, final DataItem item) {

                ImageView imgView = holder.getView(R.id.img);
                //添加删除事件
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int type = item.type;
                        switch (type) {
                            case R.id.brandLayout:
                                //删除品牌之前将所选状态置为false
                                List<Brand> brands = builderBrands.getDatas();
                                int size = brands.size();
                                for (int i = 0; i < size; i++) {
                                    brands.get(i).isSelected = false;
                                }
                                builderBrands.notifyDataSetChanged();
                                mBrandId = "";
                                break;
                            case R.id.filterSeatList:
                                size = mSeatLists.size();
                                for (int i = 0; i < size; i++) {
                                    mSeatLists.get(i).isSelected = false;
                                }
                                mSeatKey = "";
                                mFilterSeatListAdapter.notifyDataSetChanged();
                                break;
                            case R.id.setPriceLayout:
                                mPriceMin = "";
                                mPriceMax = "";
                                mPriceKey = "";
                                break;
                            case R.id.level:


                                size = mCarLevelLists.size();
                                for (int i = 0; i < size; i++) {
                                    mCarLevelLists.get(i).isSelected = false;
                                }
                                mLevel = "";
                                mFilterLevelAdapter.notifyDataSetChanged();

                                break;
                        }
                        int size = datas.size();
                        if (size > 0) {
                            datas.remove(item);
                            showText(R.string.custom, true);
                        }
                        mCustomListAdapter.notifyDataSetChanged();
                        showFilterItems();
                        mPage = 1;
                        mPresenter.loadDatas(builderReqParams());
                    }
                });
                holder.setText(R.id.name, item.name);
            }

        };
        mCustomLists.setAdapter(mCustomListAdapter);

        customModelList = (RelativeLayout) findViewById(R.id.customModelList);
        filterSeatList = (ListView) findViewById(R.id.filterSeatList);
        filterSeatList.setOnItemClickListener(mFilterSeatItemListener);

        filterCarLevelList = (ListView) findViewById(R.id.filterCarLevelList);
        filterCarLevelList.setOnItemClickListener(mFilterCarLevelItemListener);

        initFilterListsData();
        bindAdapter();
        filterSeatList = (ListView) findViewById(R.id.filterSeatList);

        customModelList = (RelativeLayout) findViewById(R.id.customModelList);
        mSetPriceLayout = (RelativeLayout) findViewById(setPriceLayout);
        mSetPriceLayout.setVisibility(View.GONE);
        priceViewBuilder = new PriceViewBuilder(mContext, mSetPriceLayout, priceCallback);
        mSetBrandsLayout = (RelativeLayout) findViewById(R.id.setBrandLayout);
        builderBrands = new BuilderBrands(mContext, mSetBrandsLayout);
        builderBrands.setSingle(true);
        builderBrands.setCallback(mCallback);
        builderBrands.loadData(1,"1",0);
    }


    private PriceViewBuilder.FilterPriceCallback priceCallback = new PriceViewBuilder.FilterPriceCallback() {
        @Override
        public void result(String customMin, String customMax) {

            loadFilterByPriceReuslt(customMin + "-" + customMax + "万", customMin, customMax, "");


        }

        @Override
        public void result(DataItem item) {

            loadFilterByPriceReuslt(item.title, "", "", item.key);
        }
    };

    private void loadFilterByPriceReuslt(String title, String min, String max, String key) {


        setFilterViewsVisible(mSetPriceLayout, mPriceLayout);
        mPriceItem.name = title;
        mPriceItem.type = R.id.setPriceLayout;
        if (!datas.contains(mPriceItem)) {
            datas.add(mPriceItem);
        }
        showFilterItems();
        mCustomListAdapter.notifyDataSetChanged();
        mPriceMin = min;
        mPriceMax = max;
        mPriceKey = key;

        mPresenter.loadDatas(
                builderReqParams());
    }

    private void bindAdapter() {

        mFilterSeatListAdapter = new WrapAdapter<DataItem>(mContext, mSeatLists,
                R.layout.common_list_item) {
            @Override
            public void convert(ViewHolder holder, DataItem item) {
                TextView titleText = holder.getView(R.id.title);
                titleText.setText(item.name);
                ImageView imageView = holder.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                    titleText.setTextColor(mRes.getColor(R.color.text_color6));
                } else {
                    imageView.setVisibility(View.GONE);
                    titleText.setTextColor(mRes.getColor(R.color.text_color12));
                }
            }
        };
        filterSeatList.setAdapter(mFilterSeatListAdapter);


        mFilterLevelAdapter = new WrapAdapter<DataItem>(mContext, mCarLevelLists,
                R.layout.common_list_item) {
            @Override
            public void convert(ViewHolder holder, DataItem item) {
                TextView titleText = holder.getView(R.id.title);
                titleText.setText(item.name);
                ImageView imageView = holder.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                    titleText.setTextColor(mRes.getColor(R.color.text_color6));
                } else {
                    imageView.setVisibility(View.GONE);
                    titleText.setTextColor(mRes.getColor(R.color.text_color12));
                }
            }
        };
        filterCarLevelList.setAdapter(mFilterLevelAdapter);

    }

    private void showText(int stringId, boolean enabled) {
        mCustomBtn.setText(mRes.getString(stringId));
        mCustomBtn.setEnabled(enabled);
    }

    private AdapterView.OnItemClickListener mFilterSeatItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {
            DataItem item = (DataItem) parent
                    .getAdapter()
                    .getItem(position);

            item.isSelected = true;
            singleChecked(
                    mSeatLists, item);
            mFilterSeatListAdapter
                    .notifyDataSetChanged();

            showText(
                    R.string.custom,
                    true);
            //判断是否增加了排量信息
            boolean isExist = datas
                    .contains(
                            mSeatItem);
            if (!isExist) {
                datas
                        .add(mSeatItem);
            }
            mSeatItem.name = item.name;
            mSeatItem.type = R.id.filterSeatList;
            mSeatItem.key = item.key;
            mCustomListAdapter
                    .notifyDataSetChanged();
            showFilterItems();
            hideSeatList();
            mSeatKey = item.key
                    + "";
            mPage = 1;
            mPresenter.loadDatas(
                    builderReqParams());
        }
    };


    private AdapterView.OnItemClickListener mFilterCarLevelItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {
            DataItem item = (DataItem) parent
                    .getAdapter()
                    .getItem(position);

            item.isSelected = true;
            singleChecked(
                    mCarLevelLists, item);
            mFilterLevelAdapter
                    .notifyDataSetChanged();

            showText(
                    R.string.custom,
                    true);
            //判断是否增加了排量信息
            boolean isExist = datas
                    .contains(
                            mLevelItem);
            if (!isExist) {
                datas
                        .add(mLevelItem);
            }
            mLevelItem.name = item.name;
            mLevelItem.type = R.id.level;
            mLevelItem.key = item.key;
            mCustomListAdapter
                    .notifyDataSetChanged();
            showFilterItems();
            setFilterViewsVisible(filterCarLevelList, mLevelLayout);
            mLevel = item.key
                    + "";
            mPage = 1;
            mPresenter.loadDatas(
                    builderReqParams());
        }
    };


    /**
     * 构建请求的参数
     */
    private Map<String, String> builderReqParams() {


        Map<String, String> params = new HashMap<String, String>();
        params.put("brand_ids", mBrandId);
        params.put("page", mPage + "");
        params.put("pageSize", pageSize + "");
        params.put("price_range_key", mPriceKey);
        params.put("price_range_min", mPriceMin + "");
        params.put("price_range_max", mPriceMax + "");
        params.put("seat_num_range_key", mSeatKey);
        params.put("car_levels", mLevel + "");
        params.put("token", IOUtils.getToken(mContext));
        //token=&page=1&pageSize=10&total=0&brand_ids=122&price_range_key=1&seat_num_range_key=1&car_levels=1
        return params;
    }

    private OnItemClickListener<DataItem> mCustomItemListener = new OnItemClickListener<DataItem>() {
        @Override
        public void onItemClick(ViewGroup parent,
                                View view,
                                DataItem item,
                                int position) {

            int size = datas.size();
            if (size > 0) {
                datas.remove(item);

            }
            mCustomListAdapter
                    .notifyDataSetChanged();
            showFilterItems();
        }

        @Override
        public boolean onItemLongClick(ViewGroup parent,
                                       View view,
                                       DataItem item,
                                       int position) {
            return false;
        }
    };

    private void initFilterViews() {
        mBrandLayout = (LinearLayout) findViewById(R.id.brand);
        mPriceLayout = (LinearLayout) findViewById(R.id.price);
        mSeatLayout = (LinearLayout) findViewById(R.id.seat);
        mLevelLayout = (LinearLayout) findViewById(R.id.level);
        String[] titles = mRes.getStringArray(R.array.filter_type);
        commonFilterViews(mBrandLayout, titles[1]);
        commonFilterViews(mPriceLayout, titles[2]);
        commonFilterViews(mSeatLayout, titles[3]);
        commonFilterViews(mLevelLayout, titles[4]);
        mMoreFilterView = (ImageView) findViewById(R.id.moreFilter);
        mBrandLayout.setOnClickListener(this);
        mPriceLayout.setOnClickListener(this);
        mSeatLayout.setOnClickListener(this);
        mLevelLayout.setOnClickListener(this);
        mMoreFilterView.setOnClickListener(this);
    }

    private void commonFilterViews(LinearLayout layout, String title) {
        TextView tv = (TextView) layout.findViewById(R.id.filterTitle);
        tv.setText(title);
        // tv.setTextColor(mRes.getColor(R.color.text_color15));
        ImageView img = (ImageView) layout.findViewById(R.id.filterImg);
        img.setImageResource(R.drawable.nav_filter_selector);
    }

    private MotoModel.CustomData customData;

    private int isFirst = 1;

    private SmartRecyclerAdapter smartRecyclerAdapter;

    @Override
    public void showData(SeriesModel model) {

        String args = String.format(getString(R.string.matches_values), model.seriesTotal + "",
                model.carTotal + "");
        mMatchesTv.setText(args);

        int size = model.list.size();

        if (size == 0 && items.size() == 0) {
            mMatchesTv.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            return;
        } else {
            mMatchesTv.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }
        if (mPage == 1) {
            items.clear();
        }

        mMultiAdapter.notifyDataSetChanged();

        items.addAll(model.list);

        SeriesModel.CustomMade sc = model.customMade;
        isFirst = 0;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);

        if (!active) {
            stopRefreshAll();
        }
    }

    @Override
    public void showLoadingTasksError() {

        showLoadingIndicator(false);
        stopRefreshAll();
    }

    @Override
    public void onItemClick(ViewGroup parent, View v, CarBean item, int position) {

        //缓存车型列表数据
        KPlayCarApp.putValue(Constants.IntentParams.DATA, item.matchingCars);
        KPlayCarApp.putValue(Constants.IntentParams.DATA2, item.matchNums);
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentParams.ID, item.seriesId);
        intent.putExtra(Constants.IntentParams.NAME, item.seriesName);
        intent.setClass(mContext, SeriesDetailActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, CarBean item, int position) {
        return false;
    }

    @Override
    public void onClick(View v, CarBean item) {
        //缓存车型列表数据
        KPlayCarApp.putValue(Constants.IntentParams.DATA, item.matchingCars);
        KPlayCarApp.putValue(Constants.IntentParams.DATA2, item.matchNums);
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentParams.TITLE, item.seriesName + "");
        intent.putExtra(Constants.IntentParams.ID, item.seriesId);
        intent.putExtra(Constants.IntentParams.INTENT_TYPE, 1);
        intent.setClass(mContext, ModelListActivity.class);
        startActivity(intent);
    }

    private int mTotals;


    private void resetAll() {

        datas.clear();

        showFilterItems();
        if (mCustomListAdapter != null) {
            mCustomListAdapter.notifyDataSetChanged();
        }
        mBrandId = "";
        mPage = 1;
        mPriceKey = "";
        mPriceMin = "";
        mPriceMax = "";
        mSeatKey = "";
        mLevel = "";
        if (mPresenter != null) {
            mPresenter.loadDatas(builderReqParams());
        }


    }

    private BuilderBrands.Callback mCallback = new BuilderBrands.Callback() {
        @Override
        public void refresh(ViewGroup parent, View view,
                            Brand item, int position) {

            int id = item.brandId;
            boolean isExist = datas.contains(mBrandItem);
            if (!isExist) {
                datas.add(mBrandItem);
            }
            mBrandItem.id = id;
            mBrandItem.type = R.id.brandLayout;
            mBrandItem.name = item.brandName;
            mBrandItem.isSelected = true;
            mCustomListAdapter.notifyDataSetChanged();
            showText(R.string.custom, true);
            showFilterItems();
            hideBrandList();
            mBrandId = "" + id;
            mPage = 1;
            mPresenter.loadDatas(builderReqParams());
        }
    };

    private void showFilterItems() {
        int size = datas.size();

        if (mFilterItems != null) {
            if (size > 0) {
                mFilterItems.setVisibility(View.VISIBLE);
            } else {
                mFilterItems.setVisibility(View.GONE);
            }
        }

    }

    private void hideBrandList() {
        setSelectStatus(mBrandLayout, false);
        customModelList.setVisibility(View.GONE);
        filterSeatList.setVisibility(View.GONE);
        mSetPriceLayout.setVisibility(View.GONE);
        mSetBrandsLayout.setVisibility(View.VISIBLE);
        mBrandLayout.setTag(false);
    }

    private void hideSeatList() {
        setSelectStatus(mSeatLayout, false);
        customModelList.setVisibility(View.GONE);
        filterSeatList.setVisibility(View.GONE);
        mSeatLayout.setTag(false);
    }

    private void setSelectStatus(View v, boolean isSelected) {

        v.findViewById(R.id.filterImg).setSelected(isSelected);
        v.findViewById(R.id.filterTitle).setSelected(isSelected);
    }

    /**
     * 除当前选中的view，其它全设置为gone
     *
     * @param visibleView
     * @param isSelected
     * @param views
     */
    private void setFilterViewsVisible(View visibleView, boolean isSelected, View... views) {

        setSelectStatus(visibleView, true);
        visibleView.setTag(true);

        customModelList.setVisibility(View.VISIBLE);
        if (visibleView.equals(mBrandLayout)) {

            mSetBrandsLayout.setVisibility(View.VISIBLE);
            mSetPriceLayout.setVisibility(View.GONE);
            filterSeatList.setVisibility(View.GONE);
            filterCarLevelList.setVisibility(View.GONE);
        } else if (visibleView.equals(mPriceLayout)) {

            mSetPriceLayout.setVisibility(View.VISIBLE);
            filterSeatList.setVisibility(View.GONE);
            filterCarLevelList.setVisibility(View.GONE);
            mSetBrandsLayout.setVisibility(View.GONE);
        } else if (visibleView.equals(mSeatLayout)) {
            filterSeatList.setVisibility(View.VISIBLE);
            mSetPriceLayout.setVisibility(View.GONE);
            filterCarLevelList.setVisibility(View.GONE);
            mSetBrandsLayout.setVisibility(View.GONE);
        } else if (visibleView.equals(mLevelLayout)) {
            filterCarLevelList.setVisibility(View.VISIBLE);
            mSetPriceLayout.setVisibility(View.GONE);
            filterSeatList.setVisibility(View.GONE);
            mSetBrandsLayout.setVisibility(View.GONE);
        }
        for (View v : views) {
            v.setTag(isSelected);
            setSelectStatus(v, isSelected);

        }
    }

    /**
     * 隐藏当前可见的View
     *
     * @param v
     */
    private void setFilterViewsVisible(View v, View tagView) {

        customModelList.setVisibility(View.GONE);
        v.setVisibility(View.GONE);
        tagView.setTag(false);
        setSelectStatus(tagView, false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Object tag = v.getTag();
        boolean flag = false;
        if (tag != null) {
            flag = Boolean.parseBoolean(tag.toString());
        }

        switch (id) {
            case R.id.brand:

                if (flag) {
                    setFilterViewsVisible(mSetBrandsLayout, v);
                } else {


                    setFilterViewsVisible(v, false, mLevelLayout, mSeatLayout, mPriceLayout);
                }

                break;
            case R.id.price:

                priceViewBuilder.clear();
                if (flag) {
                    setFilterViewsVisible(mSetPriceLayout, v);
                } else {


                    setFilterViewsVisible(v, false, mLevelLayout, mBrandLayout, mSeatLayout);
                }
                break;
            case R.id.seat:
                if (flag) {
                    setFilterViewsVisible(filterSeatList, v);
                } else {

                    setFilterViewsVisible(v, false, mLevelLayout, mBrandLayout, mPriceLayout);
                }
                break;
            case R.id.level:


                if (flag) {
                    setFilterViewsVisible(filterCarLevelList, v);
                } else {


                    setFilterViewsVisible(v, false, mSeatLayout, mBrandLayout, mPriceLayout);
                }


                break;
            case R.id.moreFilterLayout:
                CommonUtils.startResultNewActivity(this, null,
                        FilterCustomizationActivity.class, Constants.InteractionCode.REQUEST_CODE);
                break;

            case confirmBtn:

                boolean isExist = datas.contains(mPriceItem);
                if (!isExist) {
                    datas.add(mPriceItem);
                }
                mPriceItem.name = mPriceMin + "-" + mPriceMax + "万元";
                mPriceItem.type = setPriceLayout;
                mCustomListAdapter.notifyDataSetChanged();
                showFilterItems();
                customModelList.setVisibility(View.GONE);
                mSetPriceLayout.setVisibility(View.GONE);
                mPriceLayout.findViewById(R.id.filterImg).setSelected(false);
                mPriceLayout.setTag(flag);
                showText(R.string.custom, true);
                mPage = 1;
                mPresenter.loadDatas(builderReqParams());
                break;
            case R.id.customBtn:
                //                if (!TextUtils.isEmpty(IOUtils.getToken(getContext())))
                //                    setCustomParams();
                boolean isLogin = IOUtils.isLogin(mContext);
                if (isLogin) {
                    setCustomParams();
                }
                break;
        }

    }

    //定制View
    private SetCustomizationView mSetCustomizationView = new SetCustomizationView() {
        @Override
        public void setSuccess() {

            CommonUtils.showToast(mContext, "定制成功");
            showText(R.string.subscribed, false);
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
    };

    private void setCustomParams() {

        String brands = "";
        String seatKey = "";
        for (DataItem d : datas) {

            int type = d.type;
            if (brandLayout == type) {
                brands = d.id + "";
            }
            if (R.id.filterSeatList == type) {
                seatKey = d.key + "";
            }

        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("brand_ids", brands);
        params.put("seat_num_range_keys", seatKey);
        params.put("price_min", mPriceMin + "");
        params.put("price_max", mPriceMax + "");

        mSetCustomizationPresenter.setCustomizationData(params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        if (builderBrands != null) {
            builderBrands.detachView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

        mPage++;
        mPresenter.loadDatas(builderReqParams());
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
