package com.tgf.kcwc.finddiscount;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.DiscountLimitNewModel;
import com.tgf.kcwc.mvp.model.LimitDiscountEveModel;
import com.tgf.kcwc.mvp.model.SelectExbitionModel;
import com.tgf.kcwc.mvp.presenter.SelectExhibitionPresenter;
import com.tgf.kcwc.mvp.view.SelectExhibitionView;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/9/11.
 */

public class SelectExhibitionActivity extends BaseActivity implements SelectExhibitionView {


    ListView mListView;
    private boolean isRefresh = true;
    private int page = 1;

    private WrapAdapter<SelectExbitionModel> mExhibitionAdapter;
    List<SelectExbitionModel> mExhibitionData = new ArrayList<>();
    SelectExhibitionPresenter mSelectExhibitionPresenter;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("选择展会");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectexhibition);
        mSelectExhibitionPresenter = new SelectExhibitionPresenter();
        mSelectExhibitionPresenter.attachView(this);
        mListView = (ListView) findViewById(R.id.listview);

        mExhibitionAdapter = new WrapAdapter<SelectExbitionModel>(mContext,
                R.layout.selectexhibition_item, mExhibitionData) {
            @Override
            public void convert(ViewHolder helper, SelectExbitionModel item) {
                SimpleDraweeView mICon = helper.getView(R.id.icon);
                TextView mName = helper.getView(R.id.name);
                TextView mTime = helper.getView(R.id.time);
                TextView mSite = helper.getView(R.id.site);
                mName.setText(item.name);
                mTime.setText(item.startTime + " - " + item.endTime);
                mSite.setText(item.address);
                mICon.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
            }
        };
        mListView.setAdapter(mExhibitionAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RxBus.$().post(Constants.IntentParams.SELECT_EXHIBITION, mExhibitionData.get(position));
                finish();
            }
        });

        mSelectExhibitionPresenter.getEventDataLists();
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

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showEveLimitList(List<SelectExbitionModel> data) {
        mExhibitionData.clear();
        mExhibitionData.addAll(data);
        mExhibitionAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLimitListError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
