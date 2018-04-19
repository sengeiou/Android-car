package com.tgf.kcwc.see.model;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.BeautyAlbumAdapter;
import com.tgf.kcwc.adapter.DividerGridItemDecoration;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.BeautyDetailModel;
import com.tgf.kcwc.mvp.presenter.BeautyDetailPresenter;
import com.tgf.kcwc.mvp.view.BeautyDetailView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2016/12/28 0028
 * E-mail:hekescott@qq.com
 */

public class ModelAlbumActivity extends BaseActivity implements BeautyDetailView{

    private RecyclerView recyleView;
    private GridLayoutManager gridLayoutManager;
    private BeautyAlbumAdapter adapter;
    private BeautyDetailPresenter beautyDetailPresenter;
    private  TextView titleTv;
    private int modelId =1;
    private String token;
    private int isBinding;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("展会模特-模特姓名");
        titleTv =text;
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        initRefreshLayout(mBGDelegate);
        mPageSize = 20;
        token = IOUtils.getToken(mContext);
        modelId = getIntent().getIntExtra(Constants.IntentParams.ID,1);
        isBinding = getIntent().getIntExtra(Constants.IntentParams.DATA,0);
        recyleView = (RecyclerView) findViewById(R.id.beauty_pics_rv);
        beautyDetailPresenter = new BeautyDetailPresenter();
        beautyDetailPresenter.attachView(this);


        findViewById(R.id.releaseLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.FROM_ID, modelId);
                args.put(Constants.IntentParams.FROM_TYPE, Constants.IntentParams.MODEL_VALUE);
                CommonUtils.startNewActivity(mContext, args, PostingActivity.class);

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beautyalbum);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mPageIndex=1;
        beautyDetailPresenter.getBeautyDetail(modelId, token,mPageSize,mPageIndex);
    }

    @Override
    public void showHead(BeautyDetailModel beautyDetailModel) {
        if(adapter==null){
            titleTv.setText("展会模特-"+beautyDetailModel.name);
            gridLayoutManager = new GridLayoutManager(ModelAlbumActivity.this, 3);
            recyleView.setLayoutManager(gridLayoutManager);//这里用线性宫格显示 类似于grid view
            adapter=new BeautyAlbumAdapter(this);
            adapter.setBeautyDetailModel(beautyDetailModel);
            recyleView.setAdapter(adapter);
            recyleView.addItemDecoration(new DividerGridItemDecoration(this));
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
                int span =gridLayoutManager.getSpanCount();
                @Override
                public int getSpanSize(int position) {
                    Logger.d("adapter.getItemViewType(position)"+adapter.getItemViewType(position));

                    if(adapter.getItemViewType(position) == 0||adapter.getItemViewType(position) == 2){
                        span = 3;
                    }else {
                        span = 1;
                    }
                    return span;
                }
            });

        }else if(mPageIndex<=1) {
            adapter.setBeautyDetailModel(beautyDetailModel);
            adapter.notifyDataSetChanged();
        }else if(mPageIndex>1){
            adapter.appendImgItemList(beautyDetailModel.imgStore.imgList);
            adapter.notifyDataSetChanged();
        }

    }
    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex =0;
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
        beautyDetailPresenter.getBeautyDetail(modelId, token,mPageSize,mPageIndex);
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
    protected void onDestroy() {
        super.onDestroy();
        beautyDetailPresenter.detachView();
    }
}
