package com.tgf.kcwc.tripbook;

import java.util.ArrayList;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.TripAroundOrgModel;
import com.tgf.kcwc.mvp.model.TripAroundTongxModel;
import com.tgf.kcwc.mvp.presenter.TripAroundOrgPresenter;
import com.tgf.kcwc.mvp.presenter.TripAroundTongxPresenter;
import com.tgf.kcwc.mvp.view.TripAroundOrgView;
import com.tgf.kcwc.mvp.view.TripAroundTongXView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */

public class TripBookTongxFrag extends BaseFragment implements TripAroundTongXView {

    private ListView listView;
    private WrapAdapter mTripAroundOrgadapter;
    private TripAroundTongxPresenter tripAroundOrgPresenter;
    private ArrayList<TripAroundTongxModel.TongX> mTongxList;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragtripbook_aroud_search;
    }

    @Override
    protected void initData() {

        tripAroundOrgPresenter = new TripAroundTongxPresenter();
        tripAroundOrgPresenter.attachView(this);
        TripBookAroudActivity aroudActivity = (TripBookAroudActivity) getActivity();
        tripAroundOrgPresenter.getAroudTongxlist(aroudActivity.bookLindId);
    }

    @Override
    protected void initView() {
        setUserVisibleHint(true);
        listView = findView(R.id.fragtripbook_aroundlv);
        findView(R.id.trip_scrollTop).setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//Todo                Intent toUserPageAct = new Intent(mContext, UserPageActivity.class);
//                toUserPageAct.putExtra(Constants.IntentParams.ID, mTongxList.get(position).id);
//                mContext.startActivity(toUserPageAct);
            }
        });
        if(mTripAroundOrgadapter!=null){
            listView.setAdapter(mTripAroundOrgadapter);
        }

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showTongXlist(ArrayList<TripAroundTongxModel.TongX> tongxList) {

        mTongxList = tongxList;
        mTripAroundOrgadapter = new WrapAdapter<TripAroundTongxModel.TongX>(getContext(), R.layout.listitem_tripbook_tongx,tongxList) {
            @Override
            public void convert(ViewHolder helper, TripAroundTongxModel.TongX item) {

                helper.setSimpleDraweeViewURL(R.id.tripbookaround_useriv,
                        URLUtil.builderImgUrl(item.avatar, 144, 144));

                helper.setText(R.id.tripbookaround_name,item.nickname);
                helper.setText(R.id.tripbookaround_usertime,item.addTime+" 途径此地");
                ImageView sexIv=  helper.getView(R.id.tripbookdetail_sexiv);
//                if(item.){
//
//                }

            }
        };
        listView.setAdapter(mTripAroundOrgadapter);
        if(tongxList==null||tongxList.size()==0){
            CommonUtils.showToast(getContext(),"没有数据了");
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
}
