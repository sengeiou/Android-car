package com.tgf.kcwc.see.exhibition;

import java.util.ArrayList;
import java.util.List;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.ExhibitEvent;
import com.tgf.kcwc.mvp.presenter.ExhibitEventListPresenter;
import com.tgf.kcwc.mvp.view.EventListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2016/12/28 0028
 * E-mail:hekescott@qq.com
 */

public class ExhibitionEventActivity extends BaseActivity implements EventListView, RadioGroup.OnCheckedChangeListener {

    private ListView exhibitionLv;
    private WrapAdapter<ExhibitEvent> exhibitionEventWrapAdapter;
    private int exhibitId;
    private ExhibitEventListPresenter exhibitEventListPresenter;
    //1时间 2地点
    private int type = 1;
    private boolean isRefresh = false;
    private RadioGroup eventRadioRg;
    private List<ExhibitEvent> mExhibitEventList = new ArrayList<>();


    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
    }

    @Override
    protected void setUpViews() {
        initRefreshLayout(bgDelegate);
        mPageSize = 20;
        mPageIndex = 1;
        exhibitId = getIntent().getIntExtra(Constants.IntentParams.ID, 7);
        exhibitionLv = (ListView) findViewById(R.id.exhibitionevent_list);
        findViewById(R.id.title_bar_back).setOnClickListener(this);
        exhibitEventListPresenter = new ExhibitEventListPresenter();
        exhibitEventListPresenter.attachView(this);
        exhibitEventListPresenter.getEventList(exhibitId, type, mPageSize, mPageIndex);
        eventRadioRg = (RadioGroup) findViewById(R.id.exhibition_eventrg);
        eventRadioRg.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitionevent);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void showEventList(List<ExhibitEvent> exhibitEventList) {
        Logger.d(exhibitEventList.get(0).endTime);
        if (exhibitionEventWrapAdapter == null) {
            mExhibitEventList = exhibitEventList;
            exhibitionEventWrapAdapter = new WrapAdapter<ExhibitEvent>(mContext, mExhibitEventList,
                    R.layout.listview_item_exhibitionevent) {
                @Override
                public void convert(ViewHolder helper, ExhibitEvent item) {
                    String url = URLUtil.builderImgUrl(item.cover, 540, 270);
                    helper.setSimpleDraweeViewURL(R.id.event_cover, url);
                    helper.setText(R.id.eventlist_hallname, item.hallName);
                    helper.setText(R.id.eventlist_title, item.title);
                    helper.setText(R.id.eventlist_time, item.startTime + " - " + item.endTime);
                    if (item.isEnd == 1) {
                        helper.getView(R.id.eventlist_isennd).setVisibility(View.VISIBLE);
                    } else {
                        helper.getView(R.id.eventlist_isennd).setVisibility(View.INVISIBLE);
                    }
                    if (item.guestlist != null && item.guestlist.size() != 0) {
                        StringBuilder tmpStr = new StringBuilder("主讲/嘉宾: ");
                        for (int i = 0; i < item.guestlist.size(); i++) {
                            if (i == item.guestlist.size() - 1) {
                                tmpStr.append(item.guestlist.get(i).name);
                            } else {
                                tmpStr.append(item.guestlist.get(i).name + "、");
                            }
                        }
                        helper.setText(R.id.enventlist_guest, tmpStr.toString());
                    } else {
                        TextView guestTv = helper.getView(R.id.enventlist_guest);
                        guestTv.setVisibility(View.GONE);
                    }
                }
            };
            exhibitionLv.setAdapter(exhibitionEventWrapAdapter);
            exhibitionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, ExhibitionEventDetailActivity.class);
                    intent.putExtra(Constants.IntentParams.ID, mExhibitEventList.get(position).id);
                    startActivity(intent);
                }
            });
        } else if (isRefresh) {
            mExhibitEventList = exhibitEventList;
            exhibitionEventWrapAdapter.notifyDataSetChanged(mExhibitEventList);
            isRefresh = false;
        } else {
            mExhibitEventList.addAll(exhibitEventList);
            exhibitionEventWrapAdapter.notifyDataSetChanged(mExhibitEventList);
        }
    }

    @Override
    public void showNomore(String msg) {
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
    protected void onDestroy() {
        exhibitEventListPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private BGARefreshLayout.BGARefreshLayoutDelegate bgDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {
        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            isRefresh = true;
            mPageIndex = 0;
//            if(mExhibitEventList !=null){
//                mExhibitEventList.clear();
//            }
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
        exhibitEventListPresenter.getEventList(exhibitId, type, mPageSize, mPageIndex);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.title_leftbtn:
                isRefresh = true;
                type = 1;
                break;
            case R.id.title_rightbtn:
                isRefresh = true;
                type = 2;
                break;
            default:
                break;
        }
        exhibitEventListPresenter.getEventList(exhibitId, type, mPageSize, mPageIndex);
    }
}
