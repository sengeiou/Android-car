package com.tgf.kcwc.certificate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CertDetail;
import com.tgf.kcwc.mvp.presenter.CertDataPresenter;
import com.tgf.kcwc.mvp.view.CertDataView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author：Jenny
 * Date:2017/1/3 20:44
 * E-mail：fishloveqin@gmail.com
 * 证件挂失
 */

public class CertLossActivity extends BaseActivity {

    protected TextView mSenseName;
    protected TextView mCertTypeTv1;
    protected TextView mCertTypeTv2;
    protected SimpleDraweeView mImg;
    protected TextView mName;
    protected TextView mCompany;
    protected TextView mDesc1;
    protected TextView mDesc2;
    protected Button mLossBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mDetail = intent.getParcelableExtra(Constants.IntentParams.DATA);
        mCId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        super.setContentView(R.layout.activity_cert_loss);

    }

    private CertDataPresenter mPresenter;

    private CertDetail mDetail;
    private int mCId;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        setTitleBarBg(R.color.white);
        back.setImageResource(R.drawable.nav_back_selector2);
        backEvent(back);
        text.setText("证件挂失");
        text.setTextColor(mRes.getColor(R.color.text_color12));
    }

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new CertDataPresenter();
        mPresenter.attachView(mApplyCertLossView);

    }

    private CertDataView<Object> mApplyCertLossView = new CertDataView<Object>() {
        @Override
        public void showData(Object msg) {

            showLessDialog();
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
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private void initView() {
        mSenseName = (TextView) findViewById(R.id.senseName);
        mCertTypeTv1 = (TextView) findViewById(R.id.certTypeTv1);
        mCertTypeTv2 = (TextView) findViewById(R.id.certTypeTv2);
        mImg = (SimpleDraweeView) findViewById(R.id.img);
        mName = (TextView) findViewById(R.id.name);
        mCompany = (TextView) findViewById(R.id.company);
        mDesc1 = (TextView) findViewById(R.id.desc1);
        mDesc2 = (TextView) findViewById(R.id.desc2);
        mLossBtn = (Button) findViewById(R.id.lossBtn);
        mSenseName.setText(mDetail.eventName);
        mCertTypeTv1.setText(mDetail.certName);
        mImg.setImageURI(URLUtil.builderImgUrl(mDetail.profile, 144, 144));
        // mName.setText(String.format(getString(R.string.certName), mDetail.name));
        //mCompany.setText(String.format(getString(R.string.certCompany), mDetail.company));
        CommonUtils.customDisplayText(mRes, mName,
                String.format(getString(R.string.certName), mDetail.name), R.color.text_color12);
        //mCompany.setText(String.format(getString(R.string.certCompany), detail.company));
        CommonUtils.customDisplayText(mRes, mCompany,
                String.format(getString(R.string.certCompany), mDetail.company), R.color.text_color12);
        mDesc1.setText(String.format(getString(R.string.loss_desc), mDetail.serialSN));
        mLossBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        mPresenter.applyCertLoss(mCId + "", IOUtils.getToken(mContext));
    }


    private void showLessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_less_info_dialog, null, false);
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
        TextView contentTv = (TextView) v.findViewById(R.id.content);
        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 360),
                BitmapUtils.dp2px(mContext, 300));
        alertDialog.show();
    }
}
