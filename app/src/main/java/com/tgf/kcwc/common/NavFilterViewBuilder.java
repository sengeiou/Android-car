package com.tgf.kcwc.common;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.Hall;
import com.tgf.kcwc.mvp.presenter.ExhibitionDataPresenter;
import com.tgf.kcwc.mvp.view.ExhibitionDataView;
import com.tgf.kcwc.see.WrapBrandLists;

import java.util.ArrayList;
import java.util.List;


/**
 * Author:Jenny
 * Date:2017/8/21
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 顶部导航栏构建器
 */

public class NavFilterViewBuilder {


    private Context mContext;

    private View mViewRoot;

    private int mFilterNums = 2;//筛选分类数量,默认为2

    private int mArrayId;
    private List<DataItem> mItems = null;
    private WrapAdapter<DataItem> mAdapter = null;
    private GridView mFilterGridView;
    private RelativeLayout mFilterContent;
    private LayoutInflater layoutInflater;
    private View ExbLayout;
    private WrapAdapter<DataItem> mFilterAreaAdapter = null;
    private List<DataItem> mAreas = new ArrayList<DataItem>();
    private Resources mRes;
    private ListView mAreaList;
    private View mAreaView;
    private View mBrandView;
    private int mSenseId;
    private ExhibitionDataPresenter mExDataPresenter;
    private WrapBrandLists mWrapBrandsLists;
    private NavFilterCallback mFilterCallback;
    private String isNeedAll;

    public NavFilterViewBuilder(Context context, View viewRoot, int arrayId, int eventId, NavFilterCallback callback, String isNeedAll) {

        this.mContext = context;

        this.mViewRoot = viewRoot;
        this.mArrayId = arrayId;
        this.mSenseId = eventId;
        this.mFilterCallback = callback;
        this.isNeedAll = isNeedAll;
        initView();
    }


    public NavFilterViewBuilder(Context context, View viewRoot, int filterNums, int arrayId, int eventId, NavFilterCallback callback, String isNeedAll) {

        this.mContext = context;

        this.mViewRoot = viewRoot;
        this.mFilterNums = filterNums;
        this.mArrayId = arrayId;
        this.mSenseId = eventId;
        this.mFilterCallback = callback;
        this.isNeedAll = isNeedAll;
        initView();
    }


    private void initView() {
        layoutInflater = LayoutInflater.from(mContext);
        mRes = mContext.getResources();
        mFilterGridView = (GridView) mViewRoot.findViewById(R.id.filterGridView);
        mFilterGridView.setNumColumns(mFilterNums);
        mFilterContent = (RelativeLayout) mViewRoot.findViewById(R.id.filterContent);
        bindGridAdapter();
        mAreaView = layoutInflater.inflate(R.layout.filter_sort_list, null, false);
        mAreaList = (ListView) mAreaView.findViewById(R.id.list);
        bindAreaAdapter();
        mBrandView = layoutInflater.inflate(R.layout.filter_vehicle_list, null, false);
        mWrapBrandsLists = new WrapBrandLists();
        mWrapBrandsLists.setUpViews(mContext, mBrandView);
        mWrapBrandsLists.loadEventBrandData(mSenseId + "", isNeedAll);
        mWrapBrandsLists.setSingle(true);
        mWrapBrandsLists.setCallback(mCallback);
        mFilterContent.removeAllViews();
        mFilterContent.addView(mAreaView);
        mFilterContent.addView(mBrandView);
        mExDataPresenter = new ExhibitionDataPresenter();
        mExDataPresenter.attachView(mExhDataView);
        mExDataPresenter.getHallLists(mSenseId + "");
    }

    private WrapBrandLists.Callback mCallback = new WrapBrandLists.Callback() {
        @Override
        public void refresh(ViewGroup parent, View view, Brand item, int position) {
            mFilterCallback.filterBrand(item);
            mItems.get(1).isSelected=false;
            showFilterView(1,false);
        }
    };

    private void bindGridAdapter() {
        mItems = new ArrayList<DataItem>();
        String[] arrays = mContext.getResources().getStringArray(mArrayId);

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
            public void convert(WrapAdapter.ViewHolder helper, DataItem item) {

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
        mFilterGridView.setOnItemClickListener(mFilterItemClickListener);
    }


    private void bindAreaAdapter() {

        mFilterAreaAdapter = new WrapAdapter<DataItem>(mContext, R.layout.common_list_item, mAreas) {
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
        mAreaList.setAdapter(mFilterAreaAdapter);

        mAreaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataItem item = (DataItem) parent.getAdapter().getItem(position);
                item.isSelected = true;
                mFilterCallback.filterHall(item);
                singleChecked(mAreas, item);
                mFilterAreaAdapter.notifyDataSetChanged();
                mItems.get(0).isSelected=false;
                showFilterView(0,false);
            }
        });
    }

    private AdapterView.OnItemClickListener mFilterItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DataItem dataItem = (DataItem) parent.getAdapter().getItem(position);

            dataItem.isSelected = !dataItem.isSelected;
            singleChecked(mItems, dataItem);
            showFilterView(position, dataItem.isSelected);


        }
    };

    protected void singleChecked(List<DataItem> items, DataItem item) {
        for (DataItem d : items) {
            if (d.id != item.id) {
                d.isSelected = false;
            }
        }


    }

    private void showFilterView(int index,boolean isSelected) {

        if(isSelected){
            mFilterContent.setVisibility(View.VISIBLE);
            int count = mFilterContent.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = mFilterContent.getChildAt(i);
                if (i == index) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }

        }else {
            mFilterContent.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();

    }

    private ExhibitionDataView<List<Hall>> mExhDataView = new ExhibitionDataView<List<Hall>>() {
        @Override
        public void showData(List<Hall> halls) {
            initFilterAreaData(halls);
            mFilterAreaAdapter.notifyDataSetChanged();
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    private void initFilterAreaData(List<Hall> halls) {

        int length = halls.size();
        for (int i = 0; i < length; i++) {

            DataItem item = new DataItem();
            Hall h = halls.get(i);
            item.name = h.name;
            item.id = h.id;
            mAreas.add(item);
        }
    }


    public static interface NavFilterCallback {


        public void filterBrand(Brand brand);

        public void filterHall(DataItem item);
    }


}
