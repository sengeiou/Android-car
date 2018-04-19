package com.tgf.kcwc.certificate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.sale.MyExhibitionInfoActivity;
import com.tgf.kcwc.mvp.model.CarCertDetailModel;
import com.tgf.kcwc.mvp.model.CertDetail;
import com.tgf.kcwc.mvp.model.CertDetailModel;
import com.tgf.kcwc.mvp.model.CertResultModel;
import com.tgf.kcwc.mvp.presenter.CarCertDetailPresenter;
import com.tgf.kcwc.mvp.presenter.CertDataPresenter;
import com.tgf.kcwc.mvp.view.CarCertDetailView;
import com.tgf.kcwc.mvp.view.CertDataView;
import com.tgf.kcwc.signin.ExhibitionPosSignActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.QRCodeUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/2/7 20:56
 * E-mail：fishloveqin@gmail.com
 * 证件详情
 */

public class CarCertDetailActivity extends BaseActivity implements CarCertDetailView {
    //证件使用次数
    protected TextView mCurrentRecord;
    protected TextView mNotes;
    //有效时间
    protected TextView mRemark;
    protected TextView mLockBtn;
    protected TextView mPrintBtn;
    protected TextView mRecordBtn;
    //展会名称
    protected TextView mSenseName;
    //证件名称
    protected TextView mCertTypeTv1;
    protected TextView mCertTypeTv2;
    //车辆照片
    protected SimpleDraweeView mImg;
    //二维码
    protected ImageView mQRCodeImg;
    //展会logo
    protected ImageView imgSdv;
    //车牌号
    private TextView carNum;
    //展位
    private TextView exhibitionPos;
    //通行证编号
    protected TextView mSNTv;
    //提示
    protected TextView hintTv;
    protected TextView mName;
    protected TextView mCompany;
    protected TextView mDesc1;
    protected TextView mDesc2;
    protected RelativeLayout mNormalLayout;
    private RelativeLayout mBottomLayout;
    private int mCId;
    private String mCSN;
    private int applyId;//申请id
    CarCertDetailPresenter mPresenter;


    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("车辆通行证(电子)");
        function.setTextResource("使用须知", R.color.white, 14);
        function.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                // TODO: 2017/9/14 使用须知
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mCId + "");
                CommonUtils.startNewActivity(mContext, args, AboutCarCertActivity.class);
            }
        });
    }

    @Override
    protected void setUpViews() {
        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mCId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mCSN = intent.getStringExtra(Constants.IntentParams.DATA);
        super.setContentView(R.layout.activity_car_cert_detail);

    }

    private void initView() {
        mCurrentRecord = (TextView) findViewById(R.id.currentRecord);
        mRemark = (TextView) findViewById(R.id.remark);
        mLockBtn = (TextView) findViewById(R.id.lockBtn);
        mPrintBtn = (TextView) findViewById(R.id.printBtn);
        mRecordBtn = (TextView) findViewById(R.id.recordBtn);
        mLockBtn.setOnClickListener(this);
        mPrintBtn.setOnClickListener(this);
        mRecordBtn.setOnClickListener(this);

        mSenseName = (TextView) findViewById(R.id.senseName);
        mCertTypeTv1 = (TextView) findViewById(R.id.certTypeTv1);
        mCertTypeTv2 = (TextView) findViewById(R.id.certTypeTv2);
        mImg = (SimpleDraweeView) findViewById(R.id.img);
        mQRCodeImg = (ImageView) findViewById(R.id.qrImg);
        imgSdv = (ImageView) findViewById(R.id.img_sdv);
        mSNTv = (TextView) findViewById(R.id.snTv);
        mName = (TextView) findViewById(R.id.name);
        mCompany = (TextView) findViewById(R.id.company);
        mDesc1 = (TextView) findViewById(R.id.desc1);
        mDesc2 = (TextView) findViewById(R.id.desc2);
        mNormalLayout = (RelativeLayout) findViewById(R.id.normalLayout);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        carNum = (TextView) findViewById(R.id.car_num);
        exhibitionPos = (TextView) findViewById(R.id.exhibition_pos);
        hintTv = (TextView) findViewById(R.id.hint_tv);
        mPresenter = new CarCertDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.getCertDetail("1", "" + mCId, "1", "" + mCSN, IOUtils.getToken(mContext));
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.lockBtn://参展指引
                Intent intent1 = new Intent(this, MyExhibitionInfoActivity.class);
                intent1.putExtra(Constants.IntentParams.ID,applyId);
                startActivity(intent1);
                break;
            case R.id.printBtn://展位签到
                Intent intent = new Intent(this, ExhibitionPosSignActivity.class);
                intent.putExtra(Constants.IntentParams.ID,applyId);
                startActivity(intent);
                break;
            case R.id.recordBtn://证件记录
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mCId);
                CommonUtils.startNewActivity(mContext, args, AccessRecordsActivity.class);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showCertDetail(CarCertDetailModel model) {
        applyId = model.carCertInfo.carApplyId;
        //展会logo
        imgSdv.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.details.eventCover, 144, 144)));
        mCurrentRecord.setText(model.details.timesCheck + "/" + model.details.times);
        //展会名称
        mSenseName.setText(model.details.eventName);
        //证件名称
        mCertTypeTv1.setText(model.details.certName);
        mImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.carCertInfo.carImageOut, 144, 144)));
        //车牌号
        carNum.setText("车牌：" + model.carCertInfo.carPlateNumber);
        //有效时间
        mRemark.setText("有效时间：" + model.carCertInfo.carShowTime);
        //展位
        exhibitionPos.setText("参展展位：" + model.carCertInfo.carHall);
        //二维码
        mQRCodeImg.setImageBitmap(
                QRCodeUtils.encodeAsBitmap(model.details.certSn, BarcodeFormat.CODE_128, 800, 200));
        //通行证编号
        mSNTv.setText(model.details.certSn);
        hintTv.setText(model.carCertInfo.carOvertimeTime + "前未进场将扣押金");
    }
}
