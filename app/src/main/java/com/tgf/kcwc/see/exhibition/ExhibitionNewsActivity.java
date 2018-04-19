package com.tgf.kcwc.see.exhibition;

import java.util.List;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ExhibitionNews;
import com.tgf.kcwc.mvp.presenter.ExhibitionNewListPresenter;
import com.tgf.kcwc.mvp.view.NewsListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2016/12/28 0028
 * E-mail:hekescott@qq.com
 */

public class ExhibitionNewsActivity extends BaseActivity implements NewsListView {

    private ListView                    exhibitionLv;
    //    private  List<ExhibitionNews> newsList = new ArrayList<>();
    private ExhibitionNewListPresenter  exhibitionNewListPresenter;
    private int                         eventid = 1;
    private WrapAdapter<ExhibitionNews> exhibitionNewsWrapAdapter;
    private List<ExhibitionNews>        mExhibitionNewsList;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("展会快讯");
//        function.setImageResource(R.drawable.img_selector);
        backEvent(back);
//        function.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CommonUtils.showToast(getContext(), "分类列表");
//            }
//        });
        //        text.setTextColor(mRes.getColor(R.color.text_nav_def_color));

    }

    @Override
    protected void setUpViews() {
        mPageSize = 20;
        initRefreshLayout(mBGDelegate);
        exhibitionLv = (ListView) findViewById(R.id.exhibitionnews_list);
        exhibitionNewListPresenter = new ExhibitionNewListPresenter();
        exhibitionNewListPresenter.attachView(this);
        eventid = getIntent().getIntExtra(Constants.IntentParams.ID,1);
        exhibitionNewListPresenter.getNewsList(eventid, mPageSize, mPageIndex);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitionnews);
        initData();

    }

    private void initData() {
        //        for (int i=0;i<5;i++){
        //            newsList.add(new ExhibitionNews());
        //        }

    }

    @Override
    public void showEventList(List<ExhibitionNews> exhibitionNewsList) {
        stopRefreshAll();
        if (exhibitionNewsWrapAdapter == null) {
            mExhibitionNewsList = exhibitionNewsList;
            exhibitionNewsWrapAdapter = new WrapAdapter<ExhibitionNews>(mContext,
                exhibitionNewsList, R.layout.listview_item_exhibitionnews) {
                @Override
                public void convert(ViewHolder helper, ExhibitionNews item) {
                    helper.setSimpleDraweeViewURL(R.id.newslist_cover,
                        URLUtil.builderImgUrl(item.coverUrl, 414, 310));
                    helper.setText(R.id.newslist_time, item.date);
                    helper.setText(R.id.newslist_title, item.title);
                }
            };
            exhibitionLv.setAdapter(exhibitionNewsWrapAdapter);
            exhibitionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toIntent = new Intent(mContext, ExhibitNewsDetailActivity.class);
                    toIntent.putExtra(Constants.IntentParams.ID,mExhibitionNewsList.get(position).id);
                    startActivity(toIntent);
                }
            });
        } else {
            mExhibitionNewsList.addAll(exhibitionNewsList);
            exhibitionNewsWrapAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showNomore(String msg) {
        stopRefreshAll();
        CommonUtils.showToast(mContext, msg);
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
            mPageIndex = 1;
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
        exhibitionNewListPresenter.getNewsList(eventid, mPageSize, mPageIndex);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
    }

    @Override
    public void showLoadingTasksError() {
        stopRefreshAll();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exhibitionNewListPresenter.detachView();
    }

}
