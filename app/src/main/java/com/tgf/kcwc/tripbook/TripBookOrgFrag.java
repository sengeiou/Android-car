package com.tgf.kcwc.tripbook;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.OrgModel;
import com.tgf.kcwc.mvp.model.TripAroundOrgModel;
import com.tgf.kcwc.mvp.presenter.TripAroundOrgPresenter;
import com.tgf.kcwc.mvp.view.TripAroundOrgView;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.see.dealer.SalespersonListActivity;
import com.tgf.kcwc.util.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */

public class TripBookOrgFrag extends BaseFragment implements TripAroundOrgView {

    private ListView listView;
    private WrapAdapter mTripAroundOrgadapter;
    private ArrayList<TripAroundOrgModel.Org> mOrgList = new ArrayList<>();
    private TripAroundOrgPresenter tripAroundOrgPresenter;
    private int page =1;
    private int pageSize =20;
    private TripBookAroudActivity aroudActivity;
    private boolean isRefresh;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragtripbook_aroud_search;
    }

    @Override
    protected void initData() {
        tripAroundOrgPresenter = new TripAroundOrgPresenter();
        tripAroundOrgPresenter.attachView(this);
        aroudActivity = (TripBookAroudActivity) getActivity();
        tripAroundOrgPresenter.getAroudOrglist(aroudActivity.bookLindId,page,pageSize);
    }

    @Override
    protected void initView() {
        initRefreshLayout(mBGDelegate);
        setUserVisibleHint(true);
        listView = findView(R.id.fragtripbook_aroundlv);
        findView(R.id.trip_scrollTop).setOnClickListener(this);
        if (mTripAroundOrgadapter != null) {
            listView.setAdapter(mTripAroundOrgadapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < mOrgList.size()) {
                    TripAroundOrgModel.Org model = mOrgList.get(position);
                    String orgId = model.id + "";
                    String orgTitle = model.name;
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put( Constants.IntentParams.ID, orgId);
                    args.put( Constants.IntentParams.TITLE, orgTitle);
                    args.put(Constants.IntentParams.INDEX,0);
                    CommonUtils .startNewActivity( mContext,args, DealerHomeActivity.class);
                }
            }
        });

    }

    @Override
    public void showOrglist(ArrayList<TripAroundOrgModel.Org> orgList) {
        if(isRefresh){
            mOrgList.clear();
            isRefresh= false;
        }
        mOrgList.addAll(orgList);
        if(mTripAroundOrgadapter==null){
        mTripAroundOrgadapter = new WrapAdapter<TripAroundOrgModel.Org>(getContext(), R.layout.listitem_tripbook_search, mOrgList) {
            @Override
            public void convert(ViewHolder helper, TripAroundOrgModel.Org item) {
                helper.setText(R.id.tripbook_nametv, item.name);
                helper.setText(R.id.tripbook_scoretv, item.serviceScore + "分");
                helper.setText(R.id.triparound_addresstv, item.address);
                if(item.distance!=0){
                    helper.setText(R.id.triparound_distancetv, item.distance + "km");
                }
            }
        };
        listView.setAdapter(mTripAroundOrgadapter);
        }else {
            mTripAroundOrgadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
             switch (v.getId()){
                         case R.id.trip_scrollTop:
                             getActivity().finish();
                             break;
                         default:
                             break;
                     }
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {


        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            page=0;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            loadMore();
            return false;
        }
    };
    public void loadMore() {
        page++;
        tripAroundOrgPresenter.getAroudOrglist(aroudActivity.bookLindId,page,pageSize);
    }
    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingIndicator(true);
        } else {
            stopRefreshAll();
            showLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }
}
