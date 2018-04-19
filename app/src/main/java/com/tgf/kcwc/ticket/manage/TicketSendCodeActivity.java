package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponManageDetailModel;
import com.tgf.kcwc.mvp.model.TicketManageDetailModel;
import com.tgf.kcwc.mvp.presenter.SendCodeCouponPresenter;
import com.tgf.kcwc.mvp.presenter.TicketManageDetailPresenter;
import com.tgf.kcwc.mvp.view.SendCodeCouponView;
import com.tgf.kcwc.mvp.view.TicketManageDetailView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DBCacheUtil;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.QRCodeUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SpecRectView;

/**
 * Auther: Scott
 * Date: 2017/8/17 0017
 * E-mail:hekescott@qq.com
 */

public class TicketSendCodeActivity extends BaseActivity implements  TicketManageDetailView {
    private TicketManageDetailPresenter sendCodeCouponPresenter;
    private int mTicketId;
    private ImageView mCouponSendcodeCodeiv;
    private TextView mSendcodeNums;
    private TicketManageDetailModel.Detail mTicketModel;
    private int couponNum =1;
    private TextView sendcodeLimittv;
    private final String KEY_PARAM_ID ="&id=";
    private final String KEY_PARAM_NUMS ="&num=";
    private final String KEY_PARAM_SIGN ="&sign=";
    @Override
    protected void setUpViews() {
        mTicketId = getIntent().getIntExtra(Constants.IntentParams.ID,0);
        sendCodeCouponPresenter = new TicketManageDetailPresenter();
        sendCodeCouponPresenter.attachView(this);
        sendCodeCouponPresenter.getTicketManageDetail(IOUtils.getToken(getContext()), mTicketId);
        sendcodeLimittv = (TextView) findViewById(R.id.sendcode_limittv);
        mCouponSendcodeCodeiv = (ImageView) findViewById(R.id.coupon_sendcode_codeiv);
        mSendcodeNums = (TextView) findViewById(R.id.sendcode_nums);
        findViewById(R.id.sendcode_reduce).setOnClickListener(this);
        findViewById(R.id.sendcode_add).setOnClickListener(this);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("扫码发票");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketsendcode);
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
                String scanStr = Constants.QRValues.TICKETM_SEND+KEY_PARAM_ID+mTicketModel.id+KEY_PARAM_NUMS+couponNum+KEY_PARAM_SIGN+mTicketModel.sign;
                DBCacheUtil.addBitmapToMemoryCache(scanStr, QRCodeUtils.createImage(scanStr));
                mCouponSendcodeCodeiv.setImageBitmap(DBCacheUtil.getBitmapFromMemCache(scanStr));
                setLoadingIndicator(false);
                break;
            case R.id.sendcode_add:
                setLoadingIndicator(true);
                if (couponNum >= mTicketModel.canGetNums) {
                    CommonUtils.showToast(getContext(), "已超过限发量了");
                    setLoadingIndicator(false);
                    return;
                } else {
                    couponNum = couponNum + 1;
                }
                mSendcodeNums.setText(couponNum+"");
//                String scanStr2 = Constants.H5.WAP_LINK+"/#/scan/ticket?&token"+IOUtils.getToken(getContext())+"&id="+mTicketModel.id+"&nums="+couponNum+"&distribute_id="+mTicketModel.id;
                String scanStr2 = Constants.QRValues.TICKETM_SEND+KEY_PARAM_ID+mTicketModel.id+KEY_PARAM_NUMS+couponNum+KEY_PARAM_SIGN+mTicketModel.sign;
                DBCacheUtil.addBitmapToMemoryCache(scanStr2, QRCodeUtils.createImage(scanStr2));
                mCouponSendcodeCodeiv.setImageBitmap(DBCacheUtil.getBitmapFromMemCache(scanStr2));
                setLoadingIndicator(false);
                break;
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
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        sendCodeCouponPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showTicketDetail(TicketManageDetailModel.Detail ticketManageDetailModel) {
        mTicketModel = ticketManageDetailModel;

        SpecRectView specRectView = (SpecRectView) findViewById(R.id.specRectView);
        specRectView.setDrawTicketTypeColor(mTicketModel.color);
        TextView type = (TextView) findViewById(R.id.type);
        type.setText(mTicketModel.ticketName);

        TextView expire = (TextView) findViewById(R.id.expire);
        expire.setText("有效期" + DateFormatUtil.dispActiveTime2(mTicketModel.useTimeStart,
                mTicketModel.useTimeEnd));
        TextView remark = (TextView) findViewById(R.id.remark);
        remark.setText(mTicketModel.remarks);
        TextView price = (TextView) findViewById(R.id.price);
        price.setText(mTicketModel.price);
        price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        String scanStr = Constants.QRValues.TICKETM_SEND+KEY_PARAM_ID+mTicketModel.id+KEY_PARAM_NUMS+1+KEY_PARAM_SIGN+mTicketModel.sign;
        DBCacheUtil.addBitmapToMemoryCache(scanStr, QRCodeUtils.createImage(scanStr));
        mCouponSendcodeCodeiv.setImageBitmap(DBCacheUtil.getBitmapFromMemCache(scanStr));
    }

    @Override
    public void failedMessage(String localizedMessage) {

    }
}
