package com.tgf.kcwc.coupon;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BasePageModel;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.presenter.CouponPresenter;
import com.tgf.kcwc.mvp.view.CouponView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.DBCacheUtil;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.QRCodeUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.SpecRectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/9/22 0022
 * E-mail:hekescott@qq.com
 */

public class MyBuyCounponFrag extends BaseFragment implements CouponView<List<Coupon>> {
    private ListView mList;
    private CouponPresenter mPresenter;
    private String token;
    private int filterId = 0;
    private TextView couponCount;
    private WrapAdapter couponAdapter;
    private View titleFilterLayout;
    private LinearLayout mExpireLayout;
    private ArrayList<DataItem> expilreDataitems = new ArrayList<>();
    private DropDownSingleSpinner dropDownExpireSpinner;
    private List<MyCouponModel.MyCouponOrder> mCoupons = new ArrayList<>();
    private TextView emptyBoxView;

    @Override
    protected void updateData() {
        token = IOUtils.getToken(mContext);
        mPresenter.loadCoupons(token, filterId + "", 1, 999);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_vouchermybuy;
    }

    @Override
    protected void initView() {
        mContext = getContext();
        couponCount =  findView(R.id.coupon_count);
        mList =  findView(R.id.list);
        mPresenter = new CouponPresenter();
        mPresenter.attachView(this);
        emptyBoxView = findView(R.id.emptyboxTv);
        mList.addFooterView(View.inflate(mContext,R.layout.bottom_hint_layout,null));
        initFilter();
    }
    private void initFilter() {
        titleFilterLayout = findView(R.id.voucher_mytitle);
        mExpireLayout =  findView(R.id.vouchermyy_expirely);
        mExpireLayout.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mExpireLayout, "有效");
        String arrayFilter[] = mRes.getStringArray(R.array.filter_expire);
        for (int i = 0; i < arrayFilter.length; i++) {
            DataItem dataItem = new DataItem(i, arrayFilter[i]);
            expilreDataitems.add(dataItem);
        }
        dropDownExpireSpinner = new DropDownSingleSpinner(getContext(), expilreDataitems);
        dropDownExpireSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mExpireLayout,
                        R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mExpireLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = dropDownExpireSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mExpireLayout, dataItem.name);
                    if (dataItem.id != filterId) {
                        filterId = dataItem.id;
                        mPresenter.loadCoupons(token, filterId + "", 1, 999);
                    }
                }
            }
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }


    @Override
    public void showDatas(List<Coupon> coupons) {

    }

    @Override
    public void shwoFailed(String statusMessage) {

    }

    @Override
    public void shoMyCouponCount(BasePageModel.Pagination pagination) {
        couponCount.setText("共" + pagination.count + "条");
    }
    private void showQR(String qrText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_couponqr_dialog, null);
        builder.setView(v);
        TextView textView = (TextView) v.findViewById(R.id.ticket);
        textView.setText(qrText);
        final AlertDialog alertDialog = builder.create();
        ImageView img = (ImageView) v.findViewById(R.id.img);
        ImageView qrImg = (ImageView) v.findViewById(R.id.qrImg);
        if (DBCacheUtil.getBitmapFromMemCache(qrText) == null) {
            String scanStr = Constants.H5.WAP_LINK + "/#/scan/coupon?code=" + qrText;
            DBCacheUtil.addBitmapToMemoryCache(qrText, QRCodeUtils.createImage(scanStr));
        }
        qrImg.setImageBitmap(DBCacheUtil.getBitmapFromMemCache(qrText));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadCoupons(token, filterId + "", 1, 999);
                alertDialog.dismiss();

            }
        });

        alertDialog.show();
        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 300),
                BitmapUtils.dp2px(mContext, 400));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vouchermyy_expirely:
                dropDownExpireSpinner.showAsDropDownBelwBtnView(titleFilterLayout);
                break;
            default:
                break;
        }

    }
    @Override
    public void showMyCouponList(ArrayList<MyCouponModel.MyCouponOrder> mMyCouponList) {
        mCoupons.clear();
        if (mMyCouponList != null) {
            mCoupons.addAll(mMyCouponList);
        }
        if(mCoupons.size()==0){
            emptyBoxView.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
        }else {
            emptyBoxView.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
        }

        if (couponAdapter == null) {
            couponAdapter = new WrapAdapter<MyCouponModel.MyCouponOrder>(mContext, mCoupons,
                    R.layout.listitem_coupon_my) {
                @Override
                public void convert(ViewHolder helper, final MyCouponModel.MyCouponOrder item) {


                    SpecRectView specRectView = helper.getView(R.id.specRectView);
//                                    String color = item.ticketInfo.color;
//                                    if(!TextUtils.isEmpty(color)){
                    if (filterId == 0) {
                        specRectView.setBGColor("#f2c510");
                    } else {
                        specRectView.setBGColor("#c0c0c0");
                    }

                    helper.setText(R.id.title, item.coupon.title);
                    helper.setText(R.id.price, item.coupon.denomination);
                    helper.setText(R.id.address, item.issueOrg.name);
                    helper.setText(R.id.expire, "有效期至: " + item.coupon.endTime);
                    helper.setText(R.id.mycoupon_onlinetv, item.onlienNum + "人在线服务");
                    helper.setText(R.id.mycoupon_ordersntv, "订单编号: " + item.orderSn);
                    helper.getView(R.id.mycoupon_orderrv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO 跳订单详情
                            Intent toCouponOrderDetail = new Intent(getContext(), CouponOrderDetailActivity.class);
                            toCouponOrderDetail.putExtra(Constants.IntentParams.ID, item.id);
                            startActivity(toCouponOrderDetail);
                        }
                    });
                    ListView list = helper.getView(R.id.innerlist);
                    list.setAdapter(
                            new WrapAdapter<MyCouponModel.CouponCode>(mContext, item.codes, R.layout.ticket_item) {
                                @Override
                                public void convert(ViewHolder helper, final MyCouponModel.CouponCode item) {
                                    String ticketSn = "票号 " + item.code;
                                    if (filterId == 1) {
                                        helper.setText(R.id.ticketId, SpannableUtil.getDelLineString(4,ticketSn));
                                    } else {
                                        helper.setText(R.id.ticketId, "票号 " + item.code);
                                    }
//                                    2.退款中 3.已退款 4.已核销 5.已过期 6.已禁用
                                    TextView statusTv = helper.getView(R.id.statusTv);
                                    View imageQR =  helper.getView(R.id.smallQR);
                                    statusTv.setVisibility(View.VISIBLE);
                                    statusTv.setTextColor(mRes.getColor(R.color.text_color17));
                                    imageQR.setVisibility(View.GONE);
                                    if (item.status == 2) {
                                        statusTv.setTextColor(mRes.getColor(R.color.text_color16));
                                        statusTv.setText("退款中");
                                    } else if (item.status == 3) {
                                        statusTv.setText("已退款\n" + item.statusTime);
                                    } else if (item.status == 4) {
                                        statusTv.setText("已核销\n" + item.statusTime);
                                    } else if (item.status == 5) {
                                        statusTv.setText("已过期");
                                    } else if (item.status == 6) {
                                        statusTv.setText("已禁用");
                                    }else {
                                        statusTv.setVisibility(View.GONE);
                                        imageQR.setVisibility(View.VISIBLE);
                                    }

                                    imageQR .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showQR(item.code + "");
                                        }
                                    });
                                }
                            });
                    ViewUtil.setListViewHeightBasedOnChildren2(list);
                }
            };
        } else {
            couponAdapter.notifyDataSetChanged();
        }

        mList.setAdapter(couponAdapter);
    }
}
