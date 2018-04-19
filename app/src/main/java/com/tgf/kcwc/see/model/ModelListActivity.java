package com.tgf.kcwc.see.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.common.NavFilterViewBuilder;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Beauty;
import com.tgf.kcwc.mvp.model.BeautyListModel;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.Hall;
import com.tgf.kcwc.mvp.presenter.AttentionDataPresenter;
import com.tgf.kcwc.mvp.presenter.BeautyListPresenter;
import com.tgf.kcwc.mvp.presenter.ExhibitionDataPresenter;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.mvp.view.BeautyListView;
import com.tgf.kcwc.mvp.view.ExhibitionDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2016/12/28 0028
 * E-mail:hekescott@qq.com
 * 模特列表
 */

public class ModelListActivity extends BaseActivity implements BeautyListView, AttentionView {

    private WrapAdapter<DataItem> mFilterAreaAapater = null;
    private List<DataItem> datas = new ArrayList<DataItem>();
    private ListView recyclerView;
    private WrapAdapter beautyListAdapter;
    private int exhibitId = 7;
    private ExhibitionDataPresenter mExDataPresenter;
    private String token;
    private BeautyListPresenter beautyListPresenter;
    private int mHallId = -1;
    private int mfactoryId = 0;
    private List<BeautyListModel.BeautyBrand> mBeautylist = new ArrayList<>();
    private AttentionDataPresenter mAttentionPresenter;


    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("展会模特");
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        initRefreshLayout(mBGDelegate);
        mPageSize = 5;
        recyclerView = (ListView) findViewById(R.id.beautylist_beuty_rv);
        recyclerView.addFooterView(View.inflate(getContext(), R.layout.bottom_hint_layout, null));
        mExDataPresenter = new ExhibitionDataPresenter();
        mExDataPresenter.attachView(mExhDataView);
        mExDataPresenter.getHallLists(exhibitId + "");
        View rootView = findViewById(R.id.navMenuLayout);
        new NavFilterViewBuilder(this, rootView, R.array.nav_filter_values, exhibitId, navFilterCallback,"1");
        beautyListPresenter = new BeautyListPresenter();
        beautyListPresenter.attachView(this);
        token = IOUtils.getToken(mContext);
        beautyListPresenter.getBeautylist(exhibitId, mHallId, mfactoryId, mPageSize, mPageIndex);
        //        initFilterAreaData();
        mAttentionPresenter = new AttentionDataPresenter();
        mAttentionPresenter.attachView(this);
    }

    private void resetData() {
        mBeautylist.clear();
        beautyListAdapter.notifyDataSetChanged();
    }

    private NavFilterViewBuilder.NavFilterCallback navFilterCallback = new NavFilterViewBuilder.NavFilterCallback() {
        @Override
        public void filterBrand(Brand brand) {

            mfactoryId = brand.factoryId;
            mPageIndex = 1;
            resetData();
            beautyListPresenter.getBeautylist(exhibitId, mHallId, mfactoryId, mPageSize, mPageIndex);
        }

        @Override
        public void filterHall(DataItem item) {
            mPageIndex = 1;
            mHallId = item.id;
            resetData();
            beautyListPresenter.getBeautylist(exhibitId, mHallId, mfactoryId, mPageSize, mPageIndex);


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exhibitId = getIntent().getIntExtra(Constants.IntentParams.ID, 7);
        setContentView(R.layout.activity_beautylist);
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
    }

    private AdapterView.OnItemClickListener mFilterItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DataItem dataItem = (DataItem) parent.getAdapter().getItem(position);
            dataItem.isSelected = true;
            mHallId = dataItem.id;
            singleChecked(datas, dataItem);
            mFilterAreaAapater.notifyDataSetChanged();
            mPageIndex = 1;
            if (mBeautylist != null) {
                mBeautylist.clear();
            }

            beautyListPresenter.getBeautylist(exhibitId, mHallId, mfactoryId, mPageSize, mPageIndex);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Constants.InteractionCode.REQUEST_CODE == requestCode) {

            if (RESULT_OK == resultCode) {

                Brand b = data.getParcelableExtra(Constants.IntentParams.DATA);
                if (b != null) {

                    if (mfactoryId != b.factoryId) {
                        mfactoryId = b.factoryId;
                        mPageIndex = 1;
                        if (mBeautylist != null) {
                            mBeautylist.clear();
                        }
                        beautyListPresenter.getBeautylist(exhibitId, mHallId, mfactoryId, mPageSize, mPageIndex);
                    }
                }

            }
        }

    }

    @Override
    protected void onDestroy() {
        if (mExDataPresenter != null)
            mExDataPresenter.detachView();

        super.onDestroy();
    }

    @Override
    public void showBeautylistView(ArrayList<BeautyListModel.BeautyBrand> beautylist) {
        if (beautylist.size() == 0) {
            CommonUtils.showToast(mContext, "亲，暂无数据了");
        }
        if (beautylist == null) {
            return;
        }
        if (beautyListAdapter == null) {
            mBeautylist.addAll(beautylist);

//            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
//            recyclerView.setLayoutManager(gridLayoutManager);
            beautyListAdapter = new WrapAdapter<BeautyListModel.BeautyBrand>(getContext(), R.layout.recyleview_item_brandtitle, mBeautylist) {
                @Override
                public void convert(ViewHolder helper, BeautyListModel.BeautyBrand branditem) {
                    String url = URLUtil.builderImgUrl(branditem.logo, 144, 144);
                    helper.setSimpleDraweeViewURL(R.id.beautylist_brand, url);
                    helper.setText(R.id.beautylist_brandname, branditem.name);
                    MyGridView modelInfoGV = helper.getView(R.id.modelinfo_gridview);
                    modelInfoGV.setAdapter(new WrapAdapter<Beauty>(getContext(), R.layout.recyleview_item_beauty, branditem.beautyList) {
                        @Override
                        public void convert(ViewHolder helper, final Beauty item) {
                            SimpleDraweeView cover = helper.getView(R.id.beauty_avatar_iv);
                            TextView beauty_fellownumTV = helper.getView(R.id.beauty_fellownum);
                            View beauty_fellowaddView = helper.getView(R.id.beauty_fellowadd);
                            SimpleDraweeView avatar = helper.getView(R.id.avatarbadge_avatar);
                            ImageView genderIv = helper.getView(R.id.avatarbadge_gender);

                            String url = URLUtil.builderImgUrl(item.cover, 270, 203);
                            String avartaUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                            avatar.setImageURI(Uri.parse(avartaUrl));
                            cover.setImageURI(Uri.parse(url));
                            cover.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), ModelAlbumActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID, item.modelId);
                                    intent.putExtra(Constants.IntentParams.DATA, item.isBinding);
                                    startActivity(intent);
                                }
                            });
                            helper.setText(R.id.beautylist_modelname, item.name);
                            if (item.sex == 1) {
                                genderIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_men));
                            } else {
                                genderIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_women));
                            }
                            helper.getView(R.id.modellist_isModeliv).setVisibility(View.VISIBLE);
                            beauty_fellowaddView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (IOUtils.isLogin(getContext())) {
                                        item.isFollow++;
                                        item.isSee = false;
                                        mAttentionPresenter.execAttention(item.isBinding + "",
                                                IOUtils.getToken(getContext()));
                                    }
                                }
                            });
                            if (item.isBinding == 0) {
                                beauty_fellownumTV.setVisibility(View.GONE);
                                beauty_fellowaddView.setVisibility(View.GONE);
                            } else {
//                                if (item.isSee) {
//                                    beauty_fellowaddView.setVisibility(View.VISIBLE);
//                                    beauty_fellownumTV.setVisibility(View.GONE);
//                                } else {
                                beauty_fellowaddView.setVisibility(View.GONE);
                                beauty_fellownumTV.setVisibility(View.VISIBLE);
//                                }
                                beauty_fellownumTV.setText("粉丝 " + item.fansNum);
                            }

                        }
                    });

                }
            };
//            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    if (beautyListAdapter
//                            .getItemViewType(position) == beautyListAdapter.VIEW_TYPE_BRAND) {
//                        return 2;
//                    } else {
//                        return 1;
//                    }
//
//                }
//            });
            recyclerView.setAdapter(beautyListAdapter);
        } else {
            mBeautylist.addAll(beautylist);
            beautyListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void showExhibitNameView(String exhibitName) {
    }

    @Override
    public void showBeautylistNomore(String msg) {
        CommonUtils.showToast(mContext, msg);
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

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 0;
            if (mBeautylist != null) {
                mBeautylist.clear();
            }
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

            loadMore();
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {
        mPageIndex++;
        beautyListPresenter.getBeautylist(exhibitId, mHallId, mfactoryId, mPageSize, mPageIndex);
    }

    @Override
    public void showAddAttention(Object o) {
        beautyListAdapter.notifyDataSetChanged();
        CommonUtils.showToast(getContext(), "加关注成功");
    }

    @Override
    public void showCancelAttention(Object o) {

    }
}
