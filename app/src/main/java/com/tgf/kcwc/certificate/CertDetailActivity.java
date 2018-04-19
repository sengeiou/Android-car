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

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CertDetail;
import com.tgf.kcwc.mvp.model.CertDetailModel;
import com.tgf.kcwc.mvp.model.CertResultModel;
import com.tgf.kcwc.mvp.presenter.CertDataPresenter;
import com.tgf.kcwc.mvp.view.CertDataView;
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

public class CertDetailActivity extends BaseActivity implements CertDataView<CertDetailModel> {

    protected TextView mCurrentRecord;
    protected TextView mNotes;
    protected TextView mRemark;
    protected TextView mLockBtn;
    protected TextView mPrintBtn;
    protected TextView mRecordBtn;
    protected TextView mSenseName;
    protected TextView mCertTypeTv1;
    protected TextView mCertTypeTv2;
    protected SimpleDraweeView mImg;
    protected ImageView mQRCodeImg;
    protected TextView mSNTv;
    protected TextView mName;
    protected TextView mCompany;
    protected TextView mDesc1;
    protected TextView mDesc2;
    protected Button reapplyBtn;
    protected RelativeLayout mErrorStatusLayout;
    protected RelativeLayout mNormalLayout;
    private RelativeLayout mBottomLayout;
    private CertDataPresenter mPresenter;
    private RelativeLayout mRootView;
    private int mCId;
    private String mCSN;

    private CertDataPresenter mPrintPresenter;
    private CertDataPresenter mApplyCertLossPresenter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        setTitleBarBg(R.color.white);
        back.setImageResource(R.drawable.nav_back_selector2);
        backEvent(back);
        text.setText("证件详情");
        text.setTextColor(mRes.getColor(R.color.text_color12));
        function.setImageResource(R.drawable.btn_back_cert_list);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
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
        super.setContentView(R.layout.activity_cert_detail);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mPrintPresenter != null) {
            mPrintPresenter.detachView();
        }
        if (mApplyCertLossPresenter != null) {
            mApplyCertLossPresenter.detachView();
        }

    }

    private void initView() {
        mPresenter = new CertDataPresenter();
        mPresenter.attachView(this);
        mPrintPresenter = new CertDataPresenter();
        mPrintPresenter.attachView(mPrintDataView);

        mPresenter.loadCertDetailDatas("1", "" + mCId, "1", "" + mCSN, IOUtils.getToken(mContext));
        mCurrentRecord = (TextView) findViewById(R.id.currentRecord);
        mNotes = (TextView) findViewById(R.id.notes);
        mNotes.setOnClickListener(this);
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
        mSNTv = (TextView) findViewById(R.id.snTv);
        mName = (TextView) findViewById(R.id.name);
        mCompany = (TextView) findViewById(R.id.company);
        mDesc1 = (TextView) findViewById(R.id.desc1);
        mDesc2 = (TextView) findViewById(R.id.desc2);
        reapplyBtn = (Button) findViewById(R.id.reapplyBtn);
        mErrorStatusLayout = (RelativeLayout) findViewById(R.id.errorStatusLayout);
        mNormalLayout = (RelativeLayout) findViewById(R.id.normalLayout);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        mRootView = (RelativeLayout) findViewById(R.id.rootView);
    }

    private CertDetail mDetail;


    @Override
    public void showData(CertDetailModel certDetailModel) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(BitmapUtils.dp2px(mContext, 5));
        drawable.setStroke(1, Color.parseColor(certDetailModel.detail.color));
        drawable.setColor(Color.parseColor(certDetailModel.detail.color));
        mRootView.setBackground(drawable);
        mDetail = certDetailModel.detail;
        mCertTypeTv1.setText(certDetailModel.detail.certName);
        int checkStatus = certDetailModel.detail.checkStatus;

        if (certDetailModel.detail.isFirst == 1) {
            showCertDescDialog(certDetailModel);
        }
        if (checkStatus == 0) {
            mNormalLayout.setVisibility(View.GONE);
            mErrorStatusLayout.setVisibility(View.VISIBLE);
            reapplyBtn.setVisibility(View.GONE);
            mDesc2.setVisibility(View.GONE);
            mCurrentRecord.setVisibility(View.GONE);
            mNotes.setVisibility(View.GONE);
            mRemark.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);
            mDesc1.setText(getString(R.string.cert_auditing));
            setHeaderTextColor(mRes.getColor(R.color.text_color17));
        } else if (checkStatus == 1) {
            mNormalLayout.setVisibility(View.VISIBLE);
            mErrorStatusLayout.setVisibility(View.GONE);
            mSNTv.setText(certDetailModel.detail.serialSN);
            int status = certDetailModel.detail.status;

            if (status == 2) { //电子证件已挂失

                mQRCodeImg.setVisibility(View.GONE);
                mSNTv.setText("证件已经挂失，您可至打印点重新打印纸质证件");
                mSNTv.setTextSize(16);
                TextView printView = (TextView) findViewById(R.id.printSN);
                printView.setVisibility(View.VISIBLE);
                printView.setText("打印码: " + certDetailModel.detail.printSN);
            }
            if (status == 1 && certDetailModel.detail.isON == 1) {

                SimpleDraweeView view = findViewById(R.id.pendulumImg);
                view.setVisibility(View.VISIBLE);
                BitmapUtils.loadGif(view,"asset:///anim_pendulum.gif");
            }
            int print = certDetailModel.detail.isPrint;

            if (print == 1) { //已打印，电子证件失效
                mQRCodeImg.setVisibility(View.GONE);
                mSNTv.setText("已打印纸质证件\n电子证件失效");
            }
        } else if (checkStatus == 2) {
            mNormalLayout.setVisibility(View.GONE);
            mErrorStatusLayout.setVisibility(View.VISIBLE);
            mCurrentRecord.setVisibility(View.GONE);
            mDesc1.setText(getString(R.string.cert_no_pass));
            String reason = "失败原因:  " + certDetailModel.detail.checkFailReason + "";
            //mDesc2.setText();
            CommonUtils.customDisplayText(mDesc2, reason, mRes.getColor(R.color.text_color22), 0, 4);

            setHeaderTextColor(mRes.getColor(R.color.text_color17));
            mDesc2.setTextColor(mRes.getColor(R.color.text_color12));
            mNotes.setVisibility(View.GONE);
            mRemark.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);
            reapplyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID2, "");
                    args.put(Constants.IntentParams.ID, "" + mDetail.eventId);
                    args.put(Constants.IntentParams.DATA, mDetail.eventName);
                    args.put(Constants.IntentParams.DATA2, mDetail.eventCover);
                    CommonUtils.startNewActivity(mContext, args, CheckinActivity.class);
                }
            });
        } else {

            setHeaderTextColor(mRes.getColor(R.color.text_color12));
        }
        CertDetail detail = certDetailModel.detail;

        mSenseName.setText(detail.eventName);

        if (detail.isCanLose == 1) {
            mLockBtn.setVisibility(View.VISIBLE);
        } else {
            mLockBtn.setVisibility(View.INVISIBLE);
        }

        if (detail.isCanPrint == 1) {
            mPrintBtn.setVisibility(View.VISIBLE);
        } else {
            mPrintBtn.setVisibility(View.INVISIBLE);
        }

        mImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(detail.profile, 144, 144)));

        mCompany.setText(detail.company);
        //mName.setText(String.format(getString(R.string.certName), detail.name));
        CommonUtils.customDisplayText(mRes, mName,
                String.format(getString(R.string.certName), detail.name), R.color.text_color12);
        //mCompany.setText(String.format(getString(R.string.certCompany), detail.company));
        CommonUtils.customDisplayText(mRes, mCompany,
                String.format(getString(R.string.certCompany), detail.company), R.color.text_color12);
        mQRCodeImg.setImageBitmap(
                QRCodeUtils.encodeAsBitmap(detail.certSN, BarcodeFormat.CODE_128, 800, 200));

        if (detail.timesType == 1) {

            if (detail.times != 0) {
                mCurrentRecord.setText(detail.timesCheck + "/" + detail.times);
                mRemark.setText(String.format(getString(R.string.cert_expire_propmt),
                        detail.certStartTime + "-" + detail.certEndTime, "每日限" + detail.times));
            } else {
                mRemark.setText(String.format(getString(R.string.cert_expire_propmt),
                        detail.certStartTime + "-" + detail.certEndTime, "每日不限"));
                mCurrentRecord.setText(detail.timesCheck + "/不限");
            }

        } else {

            if (detail.times != 0) {
                mRemark.setText(String.format(getString(R.string.cert_expire_propmt),
                        detail.certStartTime + "-" + detail.certEndTime, "一共限" + detail.times));
                mCurrentRecord.setText(detail.timesCheck + "/" + detail.times);
            } else {
                mRemark.setText(String.format(getString(R.string.cert_expire_propmt),
                        detail.certStartTime + "-" + detail.certEndTime, "不限次"));
                mCurrentRecord.setText(detail.timesCheck + "/不限");
            }
        }

    }

    private void setHeaderTextColor(int color) {

        mSenseName.setTextColor(color);
        mCertTypeTv1.setTextColor(color);
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
            case R.id.notes:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                if (mDetail != null) {
                    args.put(Constants.IntentParams.ID, mDetail.cid + "");
                    args.put(Constants.IntentParams.DATA, mDetail.eventName);
                }

                CommonUtils.startNewActivity(mContext, args, AboutCertActivity.class);
                break;
            case R.id.lockBtn:

                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.DATA, mDetail);
                intent.putExtra(Constants.IntentParams.ID, mCId);
                intent.setClass(mContext, CertLossActivity.class);
                startActivity(intent);

                break;
            case R.id.printBtn:
                mPrintPresenter.loadCertPrintDatas(mCId + "", IOUtils.getToken(mContext));
                break;
            case R.id.recordBtn:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mCId);
                CommonUtils.startNewActivity(mContext, args, AccessRecordsActivity.class);
                break;
        }

    }

    private CertDataView<CertResultModel> mPrintDataView = new CertDataView<CertResultModel>() {
        @Override
        public void showData(CertResultModel certPrintModel) {

            showPrintInfoDialog(certPrintModel);
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    private void showPrintInfoDialog(CertResultModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_print_info_dialog, null, false);
        builder.setView(v);

        final AlertDialog alertDialog = builder.create();
        ImageView img = (ImageView) v.findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        Button btn = (Button) v.findViewById(R.id.confirm_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        TextView codeTv = (TextView) v.findViewById(R.id.code);
        TextView contentTv = (TextView) v.findViewById(R.id.content);
        codeTv.setText(model.detail.code);
        contentTv.setText(model.detail.remarks);

        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 360),
                BitmapUtils.dp2px(mContext, 300));
        alertDialog.show();
    }


    private void showCertDescDialog(CertDetailModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_cert_desc_dialog, null, false);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        TextView btn = v.findViewById(R.id.confirm_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        TextView contentTv = (TextView) v.findViewById(R.id.content);
        contentTv.setText("您已领取" + model.detail.eventName + model.detail.certName + "。" + "\n使用前请详细查阅须知。");
        alertDialog.show();
//        //改变对话框的宽度和高度
//        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 330),
//                BitmapUtils.dp2px(mContext, 220));

    }

}
