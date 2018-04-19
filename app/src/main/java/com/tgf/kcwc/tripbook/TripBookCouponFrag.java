package com.tgf.kcwc.tripbook;

import java.util.ArrayList;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.CouponDetailActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.TripBookDetailModel;
import com.tgf.kcwc.mvp.presenter.TripAroundCoupnPresenter;
import com.tgf.kcwc.mvp.view.TripAroundCouponView;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;

import android.content.Intent;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */

public class TripBookCouponFrag extends BaseFragment implements TripAroundCouponView {

    private ListView listView;
    private WrapAdapter mTripAroundCouponadapter;
    private ArrayList<TripBookDetailModel.Coupon> mCouponList = new ArrayList<>();
    private int page = 1;
    private int pageSize = 20;
    private boolean isRefresh;
    private TripAroundCoupnPresenter tripAroundCouponPresenter;
    private TripBookAroudActivity aroudActivity;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragtripbook_aroud_search;
    }

    @Override
    protected void initData() {
        tripAroundCouponPresenter = new TripAroundCoupnPresenter();
        tripAroundCouponPresenter.attachView(this);
        aroudActivity = (TripBookAroudActivity) getActivity();
        tripAroundCouponPresenter.getAroudCouponlist(aroudActivity.bookLindId, page, pageSize);
    }

    @Override
    protected void initView() {
        initRefreshLayout(mBGDelegate);
        setUserVisibleHint(true);
        listView = findView(R.id.fragtripbook_aroundlv);
        findView(R.id.trip_scrollTop).setOnClickListener(this);


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

    @Override
    public void showCouponlist(ArrayList<TripBookDetailModel.Coupon> couponList) {
        if (isRefresh) {
            mCouponList.clear();
            isRefresh = false;
        }
        mCouponList.addAll(couponList);
        if (mTripAroundCouponadapter == null) {
            mTripAroundCouponadapter = new WrapAdapter<TripBookDetailModel.Coupon>(getContext(),
                    R.layout.listview_item_recomment_voucherlist, mCouponList) {
                @Override
                public void convert(ViewHolder holder, TripBookDetailModel.Coupon voucher) {
                    TextView titleTv = holder.getView(R.id.listitem_recoment_coupontitle);
                    titleTv.setText(voucher.couponTitle);
                    holder.setSimpleDraweeViewURL(R.id.couponlist_cover,
                            URLUtil.builderImgUrl(voucher.couponCover, 270, 203));
                     Double voucherPrice = Double.parseDouble(voucher.price);
                  TextView nowPriceTv =  holder.getView(R.id.recyleitem_near_nowprice);
                    String tmpStr;
                    if(voucherPrice!=0.0){
                        nowPriceTv.setText("￥ " + voucher.price);
                        tmpStr = "已售";
                    }else{
                        nowPriceTv.setText("免费");
                        nowPriceTv.setTextColor(mRes.getColor(R.color.text_color10));
                        tmpStr = "已领";
                    }

                    SpannableString demoPrice = SpannableUtil
                            .getDelLineString("￥ " + voucher.denomination);
                    holder.setText(R.id.listviewitem_recomment_oldprice, demoPrice);
                    holder.setText(R.id.couponlist_desc, voucher.orgName);
                    holder.setText(R.id.listviewitem_recomment_salenum, tmpStr + voucher.couponused);
                }
            };
            listView.setAdapter(mTripAroundCouponadapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TripBookDetailModel.Coupon coupon = mCouponList.get(position);
                        Intent toCoupontAct = new Intent(getContext(), CouponDetailActivity.class);
                        toCoupontAct.putExtra(Constants.IntentParams.ID, coupon.couponId);
                        startActivity(toCoupontAct);
                    }
                });
        } else {
            mTripAroundCouponadapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            page = 0;
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
        tripAroundCouponPresenter.getAroudCouponlist(aroudActivity.bookLindId, page, pageSize);
    }
}
