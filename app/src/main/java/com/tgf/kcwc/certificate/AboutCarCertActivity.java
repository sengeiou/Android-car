package com.tgf.kcwc.certificate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.sale.MyExhibitionInfoActivity;
import com.tgf.kcwc.signin.ExhibitionPosSignActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/1/3 20:44
 * E-mail：fishloveqin@gmail.com
 * 证件使用须知
 */

public class AboutCarCertActivity extends BaseActivity {
    //展会名
    private TextView exhibitionName;
    //证件类型
    private TextView certType;
    //证件条形码
    private ImageView qrImg;
    //证件编码
    private TextView certSn;
    //提示
    private TextView hintTv;
    private TextView exhibitionGuideTv;
    private TextView signInTv;
    private TextView recordTv;
    private String mId;
    private int mCId;
    //申请
    private int applyId;
//    private WebView mWebView;
//    private CertDataPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getStringExtra(Constants.IntentParams.ID);
        setContentView(R.layout.activity_about_car_cert);
    }

    @Override
    protected void setUpViews() {
        Intent intent = getIntent();
        mCId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        exhibitionName = (TextView) findViewById(R.id.senseName);
        certType = (TextView) findViewById(R.id.certTypeTv1);
        qrImg = (ImageView) findViewById(R.id.qrImg);
        certSn = (TextView) findViewById(R.id.certType);
        hintTv = (TextView) findViewById(R.id.hint_tv);

        exhibitionGuideTv = (TextView) findViewById(R.id.exhibitionGuideTv);
        signInTv = (TextView) findViewById(R.id.signInTv);
        recordTv = (TextView) findViewById(R.id.recordTv);

        exhibitionGuideTv.setOnClickListener(this);
        signInTv.setOnClickListener(this);
        recordTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //展会指引
            case R.id.exhibitionGuideTv:
                Intent intent1 = new Intent(this, MyExhibitionInfoActivity.class);
                intent1.putExtra(Constants.IntentParams.ID,applyId);
                startActivity(intent1);
                break;
            //展位签到
            case R.id.signInTv:
                Intent intent = new Intent(this, ExhibitionPosSignActivity.class);
                intent.putExtra(Constants.IntentParams.ID,applyId);
                startActivity(intent);
                break;
            //证件记录
            case R.id.recordTv:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mCId);
                CommonUtils.startNewActivity(mContext, args, AccessRecordsActivity.class);
                break;
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("车辆通行证(电子)");
        function.setImageResource(R.drawable.cover_default);
        function.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {

            }
        });
    }

//    @Override
//    protected void setUpViews() {
//        mWebView = (WebView) findViewById(R.id.webView);
//        mPresenter = new CertDataPresenter();
//        mPresenter.attachView(this);
//        mPresenter.loadCertDesc(mId + "");
//    }
//
//    @Override
//    public void showData(CertResultModel certResultModel) {
//
//        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
//        mWebView.loadDataWithBaseURL("", certResultModel.detail.rules, "text/html", "UTF-8", "");
//    }
//
//    @Override
//    public void setLoadingIndicator(boolean active) {
//
//        showLoadingIndicator(active);
//    }
//
//    @Override
//    public void showLoadingTasksError() {
//        dismissLoadingDialog();
//    }
//
//    @Override
//    public Context getContext() {
//        return mContext;
//    }
}
