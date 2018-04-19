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
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.common.NavFilterViewBuilder;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.CarLaunchModel;
import com.tgf.kcwc.mvp.model.Hall;
import com.tgf.kcwc.mvp.model.NewCarBean;
import com.tgf.kcwc.mvp.presenter.CarLaunchPresenter;
import com.tgf.kcwc.mvp.presenter.ExhibitionDataPresenter;
import com.tgf.kcwc.mvp.view.CarLaunchView;
import com.tgf.kcwc.mvp.view.ExhibitionDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Author：Jenny
 * Date:2016/12/26 16:48
 * E-mail：fishloveqin@gmail.com
 */

public class NewCarLaunchActivity extends BaseActivity implements CarLaunchView {

    protected RecyclerView mRecyclerView;
    private Items mItems;
    private MultiTypeAdapter mAdapter;
    //protected ListView              mFilterList;
    private List<DataItem> datas = new ArrayList<DataItem>();
    private WrapAdapter<DataItem> mFilterAreaAapater = null;
    private CarLaunchPresenter mPresenter;
    //private ImageView               mSplit, mToggleImg;

    private int mSenseId;
    private ExhibitionDataPresenter mExDataPresenter;
    private String mHallId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSenseId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        setContentView(R.layout.activity_new_car_launch);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(R.string.new_car_launch);
        //function.setImageResource(R.drawable.btn_global_selector);

        //        function.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //            }
        //        });
    }

    @Override
    protected void setUpViews() {
        initRefreshLayout(mBGDelegate);
        mPresenter = new CarLaunchPresenter();
        mPresenter.attachView(this);
        mPresenter.loadNewCarsDatas(builderReqParams());
        mExDataPresenter = new ExhibitionDataPresenter();
        mExDataPresenter.attachView(mExhDataView);
        mExDataPresenter.getHallLists(mSenseId + "");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        // mFilterList = (ListView) findViewById(R.id.filterList);
        // mSplit = (ImageView) findViewById(R.id.filterSplit);
        // mToggleImg = (ImageView) findViewById(R.id.toggleImg);
        //mToggleImg.setOnClickListener(this);
        // mFilterList.setOnItemClickListener(mFilterItemClickListener);

        View rootView = findViewById(R.id.navMenuLayout);
        new NavFilterViewBuilder(this, rootView, R.array.nav_filter_values, mSenseId, navFilterCallback, "1");
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
        // mFilterList.setAdapter(mFilterAreaAapater);
        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);
        NewCarReleaseItemProvider provider = new NewCarReleaseItemProvider();
        provider.setOnClickListener(new NewCarReleaseItemProvider.OnClickListener() {
            @Override
            public void onClickListener(Object object, int position) {
                NewCarBean moto = (NewCarBean) object;
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(
                        Constants.IntentParams.ID,
                        moto.id);
                CommonUtils
                        .startNewActivity(
                                mContext,
                                args,
                                NewCarGalleryActivity.class);

            }
        });
        mAdapter.register(NewCarBean.class, provider);
        provider.setCountdownListener(mCountdownListener);
        provider.setOnItemClickListener(mOnItemListener);
        SmartRecyclerAdapter smartRecyclerAdapter = new SmartRecyclerAdapter(mAdapter);
        View footerView = LayoutInflater.from(mContext).inflate(R.layout.bottom_hint_layout, null,
                false);
        footerView.findViewById(R.id.split).setVisibility(View.VISIBLE);
        smartRecyclerAdapter.setFooterView(footerView);
        mRecyclerView.setAdapter(smartRecyclerAdapter);
    }

    private void resetData() {
        mItems.clear();
        mAdapter.notifyDataSetChanged();
    }

    private NavFilterViewBuilder.NavFilterCallback navFilterCallback = new NavFilterViewBuilder.NavFilterCallback() {
        @Override
        public void filterBrand(Brand brand) {

            mFactoryId = brand.factoryId + "";
            mPageIndex = 1;
            resetData();
            mPresenter.loadNewCarsDatas(builderReqParams());
        }

        @Override
        public void filterHall(DataItem item) {
            mPageIndex = 1;
            mHallId = item.id + "";
            resetData();
            mPresenter.loadNewCarsDatas(builderReqParams());


        }
    };
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


    private OnItemClickListener mOnItemListener = new OnItemClickListener() {
        @Override
        public void onItemClick(ViewGroup parent,
                                View view,
                                Object o,
                                int position) {
            NewCarBean moto = (NewCarBean) o;
            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(
                    Constants.IntentParams.ID,
                    moto.id);
            CommonUtils
                    .startNewActivity(
                            mContext,
                            args,
                            NewCarGalleryActivity.class);
        }

        ;

        @Override
        public boolean onItemLongClick(ViewGroup parent,
                                       View view,
                                       Object o,
                                       int position) {
            return false;
        }


    };
    private NewCarReleaseItemProvider.CountdownListener mCountdownListener = new NewCarReleaseItemProvider.CountdownListener() {
        @Override
        public void setTime(TextView view,
                            ViewHolder viewHolder,
                            int position) {

        }
    };

    @Override
    public void showCars(CarLaunchModel model) {

        List<NewCarBean> lists = model.lists;
        int size = lists.size();
        //mItems.clear();
        if (size == 0) {
            CommonUtils.showToast(mContext, "暂无最新数据！");
            return;
        }
        for (int i = 0; i < size; i++) {
            NewCarBean moto = lists.get(i);
            mItems.add(moto);
        }

        mAdapter.notifyDataSetChanged();
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
        return mContext;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        Object tag = view.getTag();
        boolean flag = false;
        if (tag != null) {
            flag = Boolean.parseBoolean(tag.toString());
        }
        switch (id) {

            case R.id.toggleImg:

                if (flag) {
                    //mFilterList.setVisibility(View.INVISIBLE);
                    view.setSelected(true);
                    view.setTag(false);
                } else {
                    // mFilterList.setVisibility(View.VISIBLE);
                    view.setSelected(false);
                    view.setTag(true);
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mExDataPresenter != null) {
            mExDataPresenter.detachView();
        }
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                // 如果网络可用，则加载网络数据
            //
            //                loadMore();
            //            } else {
            //                // 网络不可用，结束下拉刷新
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //            }
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                loadMore();
            //                return true;
            //            } else {
            //                // 网络不可用，返回false，不显示正在加载更多
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //                return false;
            //            }
            loadMore();
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {

        mPageIndex++;
        mPresenter.loadNewCarsDatas(builderReqParams());
    }


    private Map<String, String> builderReqParams() {

        Map<String, String> params = new HashMap<String, String>();


        params.put("event_id", mSenseId + "");
        params.put("page", mPageIndex + "");
        params.put("pageSize", mPageSize + "");
        params.put("hall_id", mHallId);
        params.put("token", IOUtils.getToken(mContext));
        params.put("real_factory_id", mFactoryId);
        return params;
    }

    private String mFactoryId = "";
}
