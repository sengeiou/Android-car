package com.tgf.kcwc.see.exhibition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.ExhibitionPicsModel;
import com.tgf.kcwc.mvp.model.Hall;
import com.tgf.kcwc.mvp.model.ImgItem;
import com.tgf.kcwc.mvp.presenter.ExhibitAlbumPresenter;
import com.tgf.kcwc.mvp.presenter.ExhibitionDataPresenter;
import com.tgf.kcwc.mvp.view.ExhibitionAlbumView;
import com.tgf.kcwc.mvp.view.ExhibitionDataView;
import com.tgf.kcwc.see.BeatutyPhotoGalleryActivity;
import com.tgf.kcwc.see.WrapBrandLists;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownBrandSpinner;
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

public class ExhibitionAlbumActivity extends BaseActivity implements ExhibitionAlbumView, ExhibitionDataView {

    private LinearLayout mBrandLayout;
    private LinearLayout mPlaceLayout;
    private LinearLayout mTypeLayout;
    private DropDownBrandSpinner mDropDownBrandSpinner;
    private DropDownSingleSpinner mDropDownHallSpinner;
    private DropDownSingleSpinner mDropDownImageSpinner;
    private GridView picsGridView;
    private ArrayList<DataItem> datas = new ArrayList<DataItem>();
    //    private ViewStub            popmanesTypeViewStub;
//    private ViewStub            popmanesPriceViewStub;
//    private ViewStub            popmanesBrandViewStub;
    private ArrayList<DataItem> mTypeLists = new ArrayList<DataItem>();
    private ArrayList<DataItem> mExhibitionPlaceLists = new ArrayList<DataItem>();
    private WrapAdapter mFilterTpyeListAdapter;
    private WrapAdapter mFilterPlaceListAdapter;
    private DataItem mPlaceItem = new DataItem();
    private DataItem mTypeItem = new DataItem();
    private DataItem mBrandItem = new DataItem();
    private View subBrandView;
    private View subTypeView;
    private View subPriceView;
    private RelativeLayout mSetBrandsLayout;
    private WrapBrandLists mWrapBrandsLists;
    private ListView rangebar;
    private int mExhibitId = 6;
    private ExhibitAlbumPresenter exhibitAlbumPresenter;
    private ExhibitionDataPresenter mExDataPresenter;
    private ArrayList<ImgItem> mExhibitImgesList;
    private WrapAdapter<ImgItem> wholemotoAdapter;
    private int mBrandId, mTypeId, mHallid;
    private boolean isLoadMore;
    private LinearLayout filterLayout;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        text.setText("展会图库");
        backEvent(back);
        initFilterListsData();
    }


    private void initFilterListsData() {
        mDropDownBrandSpinner = new DropDownBrandSpinner(getContext(), 3, "1", mExhibitId);
        mDropDownBrandSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout,
                        R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownBrandSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mBrandLayout, dataItem.name);
                    if (dataItem.id != mBrandId) {
                        mPageIndex = 1;
                        mBrandId = dataItem.id;
                        exhibitAlbumPresenter.getExhibitionPics(mExhibitId, mBrandId, mHallid, mTypeId, mPageSize, mPageIndex);

                    }
                }
            }
        });


        //图片类型
        String imgeTypeArray[] = mRes.getStringArray(R.array.image_types);
        int size = imgeTypeArray.length;
        for (int i = 0; i < size; i++) {
            DataItem dataItem = new DataItem();
            dataItem.name = imgeTypeArray[i];
            if (i >= 3) {
                dataItem.id = i + 2;
            } else {
                dataItem.id = i;
            }

            dataItem.type = R.array.image_types;
            mTypeLists.add(dataItem);
        }
        mDropDownImageSpinner = new DropDownSingleSpinner(getContext(), mTypeLists);
        mDropDownImageSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mTypeLayout,
                        R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mTypeLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownImageSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mTypeLayout, dataItem.name);
                    if (dataItem.id != mTypeId) {
                        mPageIndex = 1;
                        mTypeId = dataItem.id;
                        exhibitAlbumPresenter.getExhibitionPics(mExhibitId, mBrandId, mHallid, mTypeId, mPageSize, mPageIndex);
                    }
                }
            }
        });
    }

    @Override
    protected void setUpViews() {
        mPageSize = 20;
        initRefreshLayout(mBGDelegate);
        mExDataPresenter = new ExhibitionDataPresenter();
        mExDataPresenter.attachView(this);
        mExhibitId = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        mExDataPresenter.getHallLists(mExhibitId + "");
        filterLayout = (LinearLayout) findViewById(R.id.filterLayout);
        mBrandLayout = (LinearLayout) findViewById(R.id.popmanes_brand);
        mPlaceLayout = (LinearLayout) findViewById(R.id.popmanes_place);
        mTypeLayout = (LinearLayout) findViewById(R.id.popmanes_type);
        picsGridView = (GridView) findViewById(R.id.pics_list);
        picsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
                Intent intent = new Intent(mContext, BeatutyPhotoGalleryActivity.class);
                intent.putExtra(Constants.IntentParams.INDEX, position);
                intent.putParcelableArrayListExtra(Constants.IntentParams.DATA, mExhibitImgesList);
                mContext.startActivity(intent);
            }
        });
        commonFilterViews(mBrandLayout, "品牌");
        commonFilterViews(mPlaceLayout, "展馆");
        commonFilterViews(mTypeLayout, "类型");
        mBrandLayout.setOnClickListener(this);
        mPlaceLayout.setOnClickListener(this);
        mTypeLayout.setOnClickListener(this);
        exhibitAlbumPresenter = new ExhibitAlbumPresenter();
        exhibitAlbumPresenter.attachView(this);
        exhibitAlbumPresenter.getExhibitionPics(mExhibitId, null, null, null, mPageSize, mPageIndex);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitionalbum);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.popmanes_type:
                if (mDropDownImageSpinner != null) {
                    mDropDownImageSpinner.showAsDropDownBelwBtnView(filterLayout);
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mTypeLayout,
                            R.color.text_color10);
                    FilterPopwinUtil.commonFilterImage(mTypeLayout, R.drawable.filter_drop_down_r);
                }
                break;
            case R.id.popmanes_place:
                if (mDropDownHallSpinner != null) {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mPlaceLayout,
                            R.color.text_color10);
                    FilterPopwinUtil.commonFilterImage(mPlaceLayout, R.drawable.filter_drop_down_r);
                    mDropDownHallSpinner.showAsDropDownBelwBtnView(filterLayout);
                }
                break;
            case R.id.popmanes_brand:
                if (mDropDownBrandSpinner != null) {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout,
                            R.color.text_color10);
                    FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.filter_drop_down_r);
                    mDropDownBrandSpinner.showAsDropDownBelwBtnView(filterLayout);
                }

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

    @Override
    public void showExhbitionPics(ArrayList<ImgItem> exhibitImgesList) {
        if (isLoadMore) {
            if (exhibitImgesList != null && exhibitImgesList.size() != 0) {
                mExhibitImgesList.addAll(exhibitImgesList);
            }
            isLoadMore = false;
        } else {
            mExhibitImgesList = exhibitImgesList;
        }
        if (wholemotoAdapter == null) {
            wholemotoAdapter = new WrapAdapter<ImgItem>(this, mExhibitImgesList,
                    R.layout.gridview_item_motopics) {
                @Override
                public void convert(ViewHolder helper, ImgItem item) {
                    String url = URLUtil.builderImgUrl(item.linkUrl, 360, 360);
                    helper.setSimpleDraweeViewURL(R.id.griditem_album_iv, url);
                }
            };
            picsGridView.setAdapter(wholemotoAdapter);
        } else {
            wholemotoAdapter.notifyDataSetChanged(mExhibitImgesList);
        }
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 0;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isLoadMore = true;
            loadMore();
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {
        mPageIndex++;
        exhibitAlbumPresenter.getExhibitionPics(mExhibitId, mBrandId, mHallid, mTypeId, mPageSize, mPageIndex);
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

    @Override
    public void showData(Object o) {
        List<Hall> halls = (List<Hall>) o;
        int length = halls.size();
        for (int i = 0; i < length; i++) {
            DataItem item = new DataItem();
            Hall h = halls.get(i);
            item.name = h.name;
            item.id = h.id;
            mExhibitionPlaceLists.add(item);
        }
        if (mExhibitionPlaceLists != null && mExhibitionPlaceLists.size() != 0) {
            mDropDownHallSpinner = new DropDownSingleSpinner(getContext(), mExhibitionPlaceLists);
            mDropDownHallSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mPlaceLayout,
                            R.color.text_color3);
                    FilterPopwinUtil.commonFilterImage(mPlaceLayout, R.drawable.fitler_drop_down_n);
                    DataItem dataItem = mDropDownHallSpinner.getSelectDataItem();
                    if (dataItem != null) {
                        FilterPopwinUtil.commonFilterTile(mPlaceLayout, dataItem.name);
                        if (dataItem.id != mHallid) {
                            mPageIndex = 1;
                            mHallid = dataItem.id;
                            exhibitAlbumPresenter.getExhibitionPics(mExhibitId, mBrandId, mHallid, mTypeId, mPageSize, mPageIndex);
                        }
                    }
                }
            });
        }

    }
}
