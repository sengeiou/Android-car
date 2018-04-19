package com.tgf.kcwc.see.evaluate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.PopmanEslistModel;
import com.tgf.kcwc.mvp.presenter.PopmanEsListPresenter;
import com.tgf.kcwc.mvp.view.PopmanEsListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DiscountsPriceViewBuilder;
import com.tgf.kcwc.view.DropDownBrandSpinner;
import com.tgf.kcwc.view.DropDownPriceView;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2016/12/21 0021
 * E-mail:hekescott@qq.com
 */

public class PopmanESActivity extends BaseActivity implements PopmanEsListView {

    private LinearLayout mBrandLayout;
    private LinearLayout mPriceLayout;
    private LinearLayout mSortLayout;
    private ListView popmanesList;
    private ArrayList<DataItem> datas = new ArrayList<DataItem>();
    private ArrayList<DataItem> mCCLists = new ArrayList<DataItem>();
    private List<PopmanEslistModel.PopmanEsItem> mPopmanEsList = new ArrayList<>();
    private PopmanEsListPresenter popmanEsListPresenter;
    private WrapAdapter<PopmanEslistModel.PopmanEsItem> wholemotoAdapter;
    private DropDownBrandSpinner mDropDownBrandSpinner;
    private DiscountsPriceViewBuilder mDropDownPriceView;
    private DropDownSingleSpinner mDropDownSortSpinner;
    private int priceId;
    private int cityid = 22;
    private DataItem priceDataItem = new DataItem();
    private Integer brandId, seatNum;
    private String mPriceMax, mPriceMin;
    private ArrayList<DataItem> mSortItems;
    private View mFilterLayout;
    private TextView emptyTv;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        text.setText("达人评测");
        back.setOnClickListener(this);
        brandId = getIntent().getIntExtra(Constants.IntentParams.TAG_BRAND_ID,0);
        popmanEsListPresenter = new PopmanEsListPresenter();
        popmanEsListPresenter.attachView(this);
        popmanEsListPresenter.getPopmanesList(brandId + "", mPriceMax + "", mPriceMin + "", seatNum, mPageIndex, mPageSize);
        initFilterListsData();
    }

//    private void initSeatAdapter() {
//        mFilterCCListAdapter = new WrapAdapter<DataItem>(mContext, mCCLists,
//            R.layout.common_list_item) {
//            @Override
//            public void convert(ViewHolder holder, DataItem item) {
//                holder.setText(R.id.title, item.name);
//                ImageView imageView = holder.getView(R.id.select_status_img);
//                if (item.isSelected) {
//                    imageView.setVisibility(View.VISIBLE);
//                } else {
//                    imageView.setVisibility(View.GONE);
//                }
//            }
//        };
//
//    }

    private void initFilterListsData() {
        String seatArrays[] = mRes.getStringArray(R.array.seat_values);
        int size = seatArrays.length;
        for (int i = 0; i < size; i++) {
            DataItem dataItem = new DataItem();
            dataItem.name = seatArrays[i];
            dataItem.id = (i + 1);
            dataItem.type = R.array.seat_values;
            mCCLists.add(dataItem);
        }
    }

    private boolean isLoadMore = false;
    private BGARefreshLayout.BGARefreshLayoutDelegate bgDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {
        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 1;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isLoadMore = true;
            mPageIndex++;
            loadMore();
            return false;
        }
    };

    private void loadMore() {
        popmanEsListPresenter.getPopmanesList(brandId + "", mPriceMax + "", mPriceMin + "", seatNum, mPageIndex, mPageSize);
    }

    @Override
    protected void setUpViews() {
        initRefreshLayout(bgDelegate);

        emptyTv = (TextView) findViewById(R.id.emptyTv);
        mBrandLayout = (LinearLayout) findViewById(R.id.popmanes_brand);
        mPriceLayout = (LinearLayout) findViewById(R.id.popmanes_place);
        mSortLayout = (LinearLayout) findViewById(R.id.popmanes_type);
        popmanesList = (ListView) findViewById(R.id.popmanes_list);
        mFilterLayout = findViewById(R.id.filterLayout);
        String[] titles = mRes.getStringArray(R.array.filter_type);
        commonFilterViews(mBrandLayout, titles[1]);
        commonFilterViews(mPriceLayout, titles[2]);
        commonFilterViews(mSortLayout, titles[3]);
        mBrandLayout.setOnClickListener(this);
        mPriceLayout.setOnClickListener(this);
        mSortLayout.setOnClickListener(this);

        wholemotoAdapter = new WrapAdapter<PopmanEslistModel.PopmanEsItem>(this, mPopmanEsList,
                R.layout.listview_item_popmanes) {

            @Override
            public void convert(ViewHolder helper, PopmanEslistModel.PopmanEsItem item) {
                helper.setText(R.id.popmanes_title, item.title);
                helper.setText(R.id.listviewitem_visitors, item.view_count + "");
                helper.setText(R.id.listviewitem_focuson, item.like_count + "");
                helper.setText(R.id.listviewitem_comment, item.reply_count + "");
                String dateAgo = DateFormatUtil.getBeforeDay(item.create_time);
                helper.setText(R.id.listviewitem_date, dateAgo);
                String coverUrl = URLUtil.builderImgUrl(item.cover, 360, 360);
                helper.setSimpleDraweeViewURL(R.id.popmen_coveriv, coverUrl);
            }
        };
        popmanesList.setAdapter(wholemotoAdapter);
        popmanesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toEsDetail = new Intent(getContext(), PopmanESDetailActitivity.class);
                toEsDetail.putExtra(Constants.IntentParams.ID, mPopmanEsList.get(position).id);
                startActivity(toEsDetail);
            }
        });
        initFilters();
    }

    private DiscountsPriceViewBuilder.FilterPriceCallback priceCallback = new DiscountsPriceViewBuilder.FilterPriceCallback() {
        @Override
        public void result(String customMin, String customMax) {
            mDropDownBrandSpinner.dismiss();
            mPriceMax = customMax;
            mPriceMin = customMin;
            FilterPopwinUtil.commonFilterTile(mPriceLayout, customMin + "-" + customMax + "万");
            gain();
        }

        @Override
        public void result(DataItem item) {
            mDropDownPriceView.dismiss();
            mPriceMax = item.priceMaxStr;
            mPriceMin = item.priceMinStr;
            FilterPopwinUtil.commonFilterTile(mPriceLayout, item.title);
            //loadFilterByPriceReuslt(item.title, "", "", item.key);
            gain();

        }

        private void gain() {
            mPageIndex = 1;
            popmanEsListPresenter.getPopmanesList(brandId + "", mPriceMax + "", mPriceMin + "", seatNum, mPageIndex, mPageSize);
        }
    };

    private void initFilters() {
        mDropDownBrandSpinner = new DropDownBrandSpinner(getContext(), 1, "1", 0);
        mDropDownPriceView = new DiscountsPriceViewBuilder(getContext(), priceCallback);
        mDropDownPriceView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout, R.color.text_color15);
                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.fitler_drop_down_n);
            }
        });
        mDropDownBrandSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout,
//                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownBrandSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mBrandLayout, dataItem.name);
                    if (brandId == null || dataItem.id != brandId) {
                        brandId = dataItem.id;
                        mPageIndex = 1;
                        //todo 品牌
                        popmanEsListPresenter.getPopmanesList(brandId + "", mPriceMax + "", mPriceMin + "", seatNum, mPageIndex, mPageSize);
                    }
                }
            }
        });

//        mDropDownPriceView.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout,
//                        R.color.text_content_color);
//                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.fitler_drop_down_n);
//                DataItem dataItem = mDropDownPriceView.getSelectDataItem();
//                if (dataItem != null) {
//                    if (dataItem.id != priceId) {
//                        priceId = dataItem.id;
//                        priceDataItem = dataItem;
//                        mPriceMax = dataItem.priceMax;
//                        mPriceMin = dataItem.priceMin;
//                    }
//                    FilterPopwinUtil.commonFilterTile(mPriceLayout, "价格");
//                }
//            }
//        });
        mSortItems = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.seat_values);
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
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mSortLayout,
//                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mSortLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownSortSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mSortLayout, "座位");
                    if (seatNum == null || dataItem.id != seatNum) {
                        if (dataItem.id == 1){
                            seatNum = 0;
                        }else {
                            seatNum = dataItem.id;
                        }
                        FilterPopwinUtil.commonFilterTile(mSortLayout, dataItem.name);
                        mPageIndex = 1;
                        popmanEsListPresenter.getPopmanesList(brandId + "", mPriceMax + "", mPriceMin + "", seatNum, mPageIndex, mPageSize);
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popmanes);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.popmanes_type:
            case R.id.popmanes_place:
            case R.id.popmanes_brand:
                initSubView(view);
                break;
            default:
                break;
        }
    }

    private void commonFilterViews(LinearLayout layout, String title) {
        TextView tv = (TextView) layout.findViewById(R.id.filterTitle);
        tv.setText(title);
        ImageView img = (ImageView) layout.findViewById(R.id.filterImg);
        img.setImageResource(R.drawable.nav_filter_selector);
    }

    public void initSubView(View view) {
        switch (view.getId()) {
            case R.id.popmanes_type:
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mSortLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mSortLayout, R.drawable.filter_drop_down_r);
                mDropDownSortSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.popmanes_place:
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.filter_drop_down_r);
                mDropDownPriceView.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.popmanes_brand:
                FilterPopwinUtil.commonFilterTileSize(mBrandLayout, 12);
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.filter_drop_down_r);
                mDropDownBrandSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            default:
                break;
        }

    }


    @Override
    public void showPopmanEsList(List<PopmanEslistModel.PopmanEsItem> popmanEsList) {
        emptyTv.setVisibility(View.GONE);
        if (mPageIndex > 1) {
            if (popmanEsList.size() == 0 || null == popmanEsList) {
                Log.e("TAG", "showPopmanEsList: 1" );
                //无更多数据
                CommonUtils.showToast(getContext(),"暂无更多数据");
                isLoadMore = false;
            }else {
                Log.e("TAG", "showPopmanEsList:2 " );
                mPopmanEsList.addAll(popmanEsList);
                isLoadMore = false;
            }
        } else if (mPageIndex == 1){
            if (popmanEsList.size() == 0 || null == popmanEsList) {
                Log.e("TAG", "showPopmanEsList: 3" );
                mPopmanEsList.clear();
                //显示空布局
                emptyTv.setVisibility(View.VISIBLE);
            }else {
                Log.e("TAG", "showPopmanEsList:4 " );
                mPopmanEsList.clear();
                mPopmanEsList = popmanEsList;
            }
        }
        wholemotoAdapter.notifyDataSetChanged(mPopmanEsList);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingDialog();
        } else {
            stopRefreshAll();
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
