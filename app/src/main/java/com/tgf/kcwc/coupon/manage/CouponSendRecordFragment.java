package com.tgf.kcwc.coupon.manage;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.CouponSendRecordModel;
import com.tgf.kcwc.mvp.presenter.CouponSendRecordPresenter;
import com.tgf.kcwc.mvp.view.CouponSendRecordView;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;


/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class CouponSendRecordFragment extends BaseFragment implements CouponSendRecordView {

    private static final String TAG = "SendRecordFragment";
    private ListView recordListView;
    private CouponQueryActivity activity;
//    private SendRecordPresenter sendRecordPresenter;
    private CouponSendRecordPresenter couponSendRecordPresenter;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ticketsendrecord;
    }

    @Override
    protected void initView() {
        recordListView = findView(R.id.sendrecord_lv);
        mEmptyLayout=  findView(R.id.empty_layout);
        couponSendRecordPresenter = new CouponSendRecordPresenter();
        couponSendRecordPresenter.attachView(this);
        activity = (CouponQueryActivity) getActivity();
        couponSendRecordPresenter.getCouponSendRecord(IOUtils.getToken(getContext()),activity.mTicketId);

    }




    @Override
    public void onDestroyView() {
        couponSendRecordPresenter.detachView();

        super.onDestroyView();
    }

    @Override
    public void showCouponSendRecord(ArrayList<CouponSendRecordModel> couponSendRecordModels) {
        if(couponSendRecordModels==null||couponSendRecordModels.size()==0){
            mEmptyLayout.setVisibility(View.VISIBLE);
            recordListView.setVisibility(View.GONE);
        }else {
            recordListView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }


        recordListView.setAdapter(new WrapAdapter<CouponSendRecordModel>(getContext(),couponSendRecordModels, R.layout.listitem_ticket_sendrecord) {
            @Override
            public void convert(ViewHolder helper, CouponSendRecordModel item) {
                helper.setText(R.id.sendrecore_date, DateFormatUtil.formatTime3(item.time));
                NestFullListView sendUserLv = helper.getView(R.id.sendrecord_userlv);
                TextView limitTv = helper.getView(R.id.senduser_limit);
                helper.setText(R.id.record_item,"代金券分发");
                if(item.get_time_limit.equals("不限")){
                    limitTv.setText("不现时效");
                }else{
                    limitTv.setText(item.get_time_limit+"小时失效");
                }
                sendUserLv.setAdapter(new NestFullListViewAdapter<CouponSendRecordModel.SendRecord>(R.layout.listitem_sendrecord_user,item.list) {

                    @Override
                    public void onBind(int pos, CouponSendRecordModel.SendRecord item, NestFullViewHolder helper) {
                        CouponSendRecordModel.User user= item.user;
                        StringBuilder sb = new StringBuilder(user.nickname).append(" (").append(user.tel).append(")");
                        helper.setText(R.id.senduser_name,sb.toString());
                        helper.setText(R.id.senduser_num,"x "+item.num);
                    }
                });
            }
        });
    }


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
