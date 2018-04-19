package com.tgf.kcwc.coupon.manage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponManageDetailModel;
import com.tgf.kcwc.mvp.presenter.SendCodeCouponPresenter;
import com.tgf.kcwc.mvp.view.SendCodeCouponView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DBCacheUtil;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.QRCodeUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/8/17 0017
 * E-mail:hekescott@qq.com
 */

public class CouponSendCodeActivity extends BaseActivity implements SendCodeCouponView {
    private int mCouponId;
    private SendCodeCouponPresenter sendCodeCouponPresenter;
    private TextView mCouponSendcodeTitletv;
    private TextView mCouponSendcodeDemtv;
    private TextView mCouponSendcodeDatetv;
    private ImageView mCouponSendcodeCodeiv;
    private TextView mSendcodeNums;
    private CouponManageDetailModel mCouponModel;
    private int couponNum =1;
    private TextView sendcodeLimittv;

    @Override
    protected void setUpViews() {
        mCouponId = getIntent().getIntExtra(Constants.IntentParams.ID,0);
        sendCodeCouponPresenter = new SendCodeCouponPresenter();
        sendCodeCouponPresenter.attachView(this);
        sendCodeCouponPresenter.getCouponManageDetail(IOUtils.getToken(getContext()),mCouponId);
        mCouponSendcodeTitletv = (TextView) findViewById(R.id.coupon_sendcode_titletv);
        mCouponSendcodeDemtv = (TextView) findViewById(R.id.coupon_sendcode_demtv);
        mCouponSendcodeDatetv = (TextView) findViewById(R.id.coupon_sendcode_datetv);
        sendcodeLimittv = (TextView) findViewById(R.id.sendcode_limittv);
        mCouponSendcodeCodeiv = (ImageView) findViewById(R.id.coupon_sendcode_codeiv);
        mSendcodeNums = (TextView) findViewById(R.id.sendcode_nums);
        findViewById(R.id.sendcode_reduce).setOnClickListener(this);
        findViewById(R.id.sendcode_add).setOnClickListener(this);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("扫码分发");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendcode);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.sendcode_reduce:
                setLoadingIndicator(true);
                if (couponNum < 2) {
                    couponNum = 1;
                    CommonUtils.showToast(getContext(), "已经不能在减了");
                    setLoadingIndicator(false);
                    return;
                } else {
                    couponNum = couponNum - 1;
                }
                mSendcodeNums.setText(couponNum+"");
                String scanStr = Constants.H5.WAP_LINK+"/#/scan/coupon?coupon_id="+mCouponModel.coupon.id+"&num="+couponNum+"&distribute_id="+mCouponModel.id;
                DBCacheUtil.addBitmapToMemoryCache(scanStr, QRCodeUtils.createImage(scanStr));
                mCouponSendcodeCodeiv.setImageBitmap(DBCacheUtil.getBitmapFromMemCache(scanStr));
                setLoadingIndicator(false);
                break;
            case R.id.sendcode_add:
                setLoadingIndicator(true);
                if (couponNum >= mCouponModel.coupon.getLimit) {
                    CommonUtils.showToast(getContext(), "已超过限发量了");
                    setLoadingIndicator(false);
                    return;
                } else {
                    couponNum = couponNum + 1;
                }
                mSendcodeNums.setText(couponNum+"");
                String scanStr2 = Constants.H5.WAP_LINK+"/#/scan/coupon?coupon_id="+mCouponModel.coupon.id+"&num="+couponNum+"&distribute_id="+mCouponModel.id;
                DBCacheUtil.addBitmapToMemoryCache(scanStr2, QRCodeUtils.createImage(scanStr2));
                mCouponSendcodeCodeiv.setImageBitmap(DBCacheUtil.getBitmapFromMemCache(scanStr2));
                setLoadingIndicator(false);
                break;
        }
    }

    @Override
    public void showManageViewHead(CouponManageDetailModel couponModel) {
        mCouponModel = couponModel;
         Coupon coupon = couponModel.coupon;
        mCouponSendcodeTitletv.setText(coupon.title);
        mCouponSendcodeDemtv.setText("原价 ￥"+coupon.denomination);
        sendcodeLimittv.setText("单人限领"+coupon.getLimit+"张");
        mCouponSendcodeDatetv.setText(DateFormatUtil.formatTime4(coupon.beginTime)+" - "+ DateFormatUtil.formatTime4(coupon.endTime));
        String scanStr = Constants.H5.WAP_LINK+"/#/scan/coupon?coupon_id="+coupon.id+"&num="+1+"&distribute_id="+mCouponModel.id;
        DBCacheUtil.addBitmapToMemoryCache(scanStr, QRCodeUtils.createImage(scanStr));
        mCouponSendcodeCodeiv.setImageBitmap(DBCacheUtil.getBitmapFromMemCache(scanStr));
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        sendCodeCouponPresenter.detachView();
        super.onDestroy();
    }
}
