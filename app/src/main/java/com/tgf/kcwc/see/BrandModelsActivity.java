package com.tgf.kcwc.see;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.headerfooter.songhang.library.SmartRecyclerAdapter;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.common.NavFilterViewBuilder;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.Hall;
import com.tgf.kcwc.mvp.model.Motorshow;
import com.tgf.kcwc.mvp.model.MotorshowModel;
import com.tgf.kcwc.mvp.presenter.ExhibitionDataPresenter;
import com.tgf.kcwc.mvp.presenter.MotorshowPresenter;
import com.tgf.kcwc.mvp.view.ExhibitionDataView;
import com.tgf.kcwc.mvp.view.MotorshowView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Author：Jenny
 * Date:2016/12/28 10:23
 * E-mail：fishloveqin@gmail.com
 * 品牌展车
 */

public class BrandModelsActivity extends BaseActivity implements MotorshowView<MotorshowModel> {

    //protected ImageView             mOnlySpecImg;
    //protected ImageView             mFilterBrand;
    // protected ListView              mFilterList;
    private List<DataItem> datas = new ArrayList<DataItem>();
    private WrapAdapter<DataItem> mFilterAreaAapater = null;
    //private ImageView mSplit, mToggleImg;

    protected RecyclerView mRecyclerView;
    private Items mItems;
    private MultiTypeAdapter mAdapter;

    private MotorshowPresenter mPresenter;
    private int mSenseId;
    private int isSpec;                                        //是否看定制1为看，0为不看
    private ExhibitionDataPresenter mExDataPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSenseId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        int tmpBrandId;
        tmpBrandId = getIntent().getIntExtra(Constants.IntentParams.ID2, -1);
        if (tmpBrandId != -1) {
            mSingleBrandId = tmpBrandId + "";
        }
        super.setContentView(R.layout.activity_brand_models);

    }


    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(R.string.ex_brand_models);

        text.setTextColor(mRes.getColor(R.color.white));
        //        function.setImageResource(R.drawable.btn_global_selector);
        //        function.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //            }
        //        });

    }

    @Override
    protected void setUpViews() {
        initView();
    }

    private void initFilterAreaData(List<Hall> halls) {

        int length = halls.size();
        for (int i = 0; i < length; i++) {

            DataItem item = new DataItem();
            Hall h = halls.get(i);
            item.name = h.name;
            item.id = h.id;
            datas.add(item);
        }
    }

    private ExhibitionDataView<List<Hall>> mExhDataView = new ExhibitionDataView<List<Hall>>() {
        @Override
        public void showData(List<Hall> halls) {
            initFilterAreaData(halls);
            mFilterAreaAapater.notifyDataSetChanged();
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

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        initEmptyView();
        mFilterAreaAapater = new WrapAdapter<DataItem>(mContext, R.layout.filter_area_item, datas) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView tv = helper.getView(R.id.name);
                if (item.isSelected) {
                    tv.setBackgroundColor(mRes.getColor(R.color.btn_select_color));
                } else {
                    tv.setBackgroundColor(mRes.getColor(R.color.transparent30));
                }
                helper.setText(R.id.name, item.name);
            }
        };
        mExDataPresenter = new ExhibitionDataPresenter();
        mExDataPresenter.attachView(mExhDataView);
        mExDataPresenter.getHallLists(mSenseId + "");
        mPresenter = new MotorshowPresenter();
        mPresenter.attachView(this);
        mPresenter.loadBrandModels(builderReqParams());
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);

        View rootView = findViewById(R.id.navMenuLayout);
        new NavFilterViewBuilder(this, rootView, R.array.nav_filter_values, mSenseId, navFilterCallback, "1");
        BrandModelsItemProvider provider = new BrandModelsItemProvider();
        provider.setOnItemClickListener(mOnItemListener);
        mAdapter.register(MotorshowModel.BrandBean.class, provider);
        // mRecyclerView.setAdapter(mAdapter);

        SmartRecyclerAdapter smartRecyclerAdapter = new SmartRecyclerAdapter(mAdapter);
        View footerView = LayoutInflater.from(mContext).inflate(R.layout.bottom_hint_layout, null,
                false);
        footerView.findViewById(R.id.split).setVisibility(View.VISIBLE);
        smartRecyclerAdapter.setFooterView(footerView);
        mRecyclerView.setAdapter(smartRecyclerAdapter);
    }


    private NavFilterViewBuilder.NavFilterCallback navFilterCallback = new NavFilterViewBuilder.NavFilterCallback() {
        @Override
        public void filterBrand(Brand brand) {

            mSingleBrandId = brand.brandId + "";
            mPageIndex = 1;
            resetData();
            mPresenter.loadBrandModels(builderReqParams());
        }

        @Override
        public void filterHall(DataItem item) {
            mPageIndex = 1;
            mHallId = item.id + "";
            resetData();
            mPresenter.loadBrandModels(builderReqParams());


        }
    };

    private void resetData() {
        mItems.clear();
        mAdapter.notifyDataSetChanged();
    }

    private OnItemClickListener<Motorshow> mOnItemListener = new OnItemClickListener<Motorshow>() {
        @Override
        public void onItemClick(ViewGroup parent,
                                View view,
                                Motorshow item,
                                int position) {

            Map<String, Serializable> args = new HashMap<String, Serializable>();

            args.put(
                    Constants.IntentParams.ID,
                    item.id);
            args.put(
                    Constants.IntentParams.ID2,
                    item.seriesId);
            args.put(
                    Constants.IntentParams.ID3,
                    item.productId);
            args.put(
                    Constants.IntentParams.DATA,
                    item.isSeries);
            args.put(Constants.IntentParams.DATA2, item.brandName);
            CommonUtils
                    .startNewActivity(
                            mContext, args,
                            BrandModelsGalleryActivity.class);

        }

        @Override
        public boolean onItemLongClick(ViewGroup parent,
                                       View view,
                                       Motorshow item,
                                       int position) {
            return false;
        }
    };


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
    public void showData(MotorshowModel motorshowModel) {

        mItems.clear();


        mItems.addAll(motorshowModel.brands);
        int size = mItems.size();
        if (size == 0) {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }

    }


    private Map<String, String> builderReqParams() {

        Map<String, String> params = new HashMap<String, String>();

        //page=1&pageSize=5&total=10&event_id=76&hall_id=126&single_brand_id=165

        params.put("event_id", mSenseId + "");
        params.put("page", mPageIndex + "");
        params.put("pageSize", mPageSize + "");
        params.put("hall_id", mHallId);
        params.put("token", IOUtils.getToken(mContext));
        params.put("single_brand_id", mSingleBrandId);
        return params;
    }

    private String mHallId = "";
    private String mSingleBrandId = "";
}
