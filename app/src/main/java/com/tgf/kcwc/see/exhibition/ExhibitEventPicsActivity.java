package com.tgf.kcwc.see.exhibition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ExhibitionEventPicsModel;
import com.tgf.kcwc.mvp.model.ImgItem;
import com.tgf.kcwc.mvp.presenter.ExhibitEventPicsPresenter;
import com.tgf.kcwc.mvp.view.ExhibitEventView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.see.BeatutyPhotoGalleryActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/1/19 0019
 * E-mail:hekescott@qq.com
 */

public class ExhibitEventPicsActivity extends BaseActivity implements ExhibitEventView {
    private RecyclerView rv;
    private ExhibitEventPicsPresenter exhibitEventPicsPresenter;
    private CommonAdapter envetContentAdapter;
    private int mEventId = 7;
    private ExhibitionEventPicsModel mExhibitionEventPicsModel1;
    private boolean isRefresh;
    private View emptyView;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("活动图库");
    }

    @Override
    protected void setUpViews() {
        initRefreshLayout(bgDelegate);
        Intent fromIntent = getIntent();
        mPageSize = 20;
        mEventId =  fromIntent.getIntExtra(Constants.IntentParams.ID,1);
        rv = (RecyclerView) findViewById(R.id.eventpics_rv);
        exhibitEventPicsPresenter = new ExhibitEventPicsPresenter();
        exhibitEventPicsPresenter.attachView(this);

        emptyView = findViewById(R.id.msgTv2);
        findViewById(R.id.releaseLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.FROM_ID, mEventId);
                args.put(Constants.IntentParams.FROM_TYPE, Constants.IntentParams.ACTIVITY_VALUE);
                CommonUtils.startNewActivity(mContext, args, PostingActivity.class);

            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibiteventpics);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(envetContentAdapter!=null){
            mPageIndex=0;
            isRefresh = true;
            beginRefreshing();
        }else {
            exhibitEventPicsPresenter.getExhibitPics(mEventId, mPageSize, mPageIndex);

        }

    }

    @Override
    public void showHead(ExhibitionEventPicsModel exhibitionEventPicsModel) {


        if (envetContentAdapter == null) {
            if (exhibitionEventPicsModel != null) {
                mExhibitionEventPicsModel1 = exhibitionEventPicsModel;
                envetContentAdapter = new CommonAdapter<ImgItem>(mContext, R.layout.gridview_item_motopics, mExhibitionEventPicsModel1.imgPage.imglist) {
                    @Override
                    public void convert(ViewHolder holder, ImgItem imge) {
                        String imgUrl = URLUtil.builderImgUrl(imge.linkUrl,360,360);
                        holder.setSimpleDraweeViewUrl(R.id.griditem_album_iv, imgUrl);
                    }
                };
                HeaderRecyclerAndFooterWrapperAdapter headandFotterAdapter = new MyheaderAdapter(envetContentAdapter);
                headandFotterAdapter.setHeaderView(R.layout.recyleitem_exhibitevent_title, mExhibitionEventPicsModel1);
                rv.setLayoutManager(new GridLayoutManager(mContext, 3));
                rv.setAdapter(headandFotterAdapter);
                if(mExhibitionEventPicsModel1.imgPage==null||mExhibitionEventPicsModel1.imgPage.imglist==null||mExhibitionEventPicsModel1.imgPage.imglist.size()==0){
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            envetContentAdapter.setOnItemClickListener(new OnItemClickListener<ImgItem>() {

                @Override
                public void onItemClick(ViewGroup parent, View view, ImgItem imgItem, int position) {
                    Intent intent = new Intent(mContext, BeatutyPhotoGalleryActivity.class);
                    intent.putExtra(Constants.IntentParams.INDEX,position);
                    intent.putParcelableArrayListExtra(Constants.IntentParams.DATA, mExhibitionEventPicsModel1.imgPage.imglist);
                    mContext.startActivity(intent);

                }

                @Override
                public boolean onItemLongClick(ViewGroup parent, View view, ImgItem imgItem, int position) {
                    return false;
                }
            });
        } else {
            if (isRefresh) {
                isRefresh = false;
                mExhibitionEventPicsModel1 = exhibitionEventPicsModel;
                envetContentAdapter.setDatas(mExhibitionEventPicsModel1.imgPage.imglist);
                if(mExhibitionEventPicsModel1.imgPage==null||mExhibitionEventPicsModel1.imgPage.imglist==null||mExhibitionEventPicsModel1.imgPage.imglist.size()==0){
                    emptyView.setVisibility(View.VISIBLE);
                }else {
                    emptyView.setVisibility(View.GONE);
                }
            } else {
                envetContentAdapter.addDatas(exhibitionEventPicsModel.imgPage.imglist);
            }
        }

    }


    @Override
    public void setLoadingIndicator(boolean active) {
            if(active){
                showLoadingDialog();
            }else {
                stopRefreshAll();
                dismissLoadingDialog();
            }
    }

    @Override
    public void showLoadingTasksError() {

    }

    private BGARefreshLayout.BGARefreshLayoutDelegate bgDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 0;
            isRefresh = true;
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
        exhibitEventPicsPresenter.getExhibitPics(mEventId, mPageSize, mPageIndex);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private class MyheaderAdapter extends HeaderRecyclerAndFooterWrapperAdapter {

        public MyheaderAdapter(RecyclerView.Adapter mInnerAdapter) {
            super(mInnerAdapter);
        }

        @Override
        protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
            ExhibitionEventPicsModel pic = (ExhibitionEventPicsModel) o;
            holder.setText(R.id.exhibitevent_tile, pic.title);
            holder.setText(R.id.exhibitevent_time, pic.startTime + " ~ " + pic.endTime);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exhibitEventPicsPresenter.detachView();
    }
}
