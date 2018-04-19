package com.tgf.kcwc.me.sale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.certificate.CarCertDetailActivity;
import com.tgf.kcwc.certificate.CertListActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.MyExhibitionInfoModel;
import com.tgf.kcwc.mvp.presenter.MyExhibitionInfoPresenter;
import com.tgf.kcwc.mvp.view.MyExhibitionInfoView;
import com.tgf.kcwc.signin.ExhibitionDepositActivity;
import com.tgf.kcwc.signin.ExhibitionPosSignActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @anthor noti
 * @time 2017/9/15
 * @describle 我的参展信息
 */
public class MyExhibitionInfoActivity extends BaseActivity implements MyExhibitionInfoView {
    //展会图
    private SimpleDraweeView imgSdv;
    //参展信息 展会审核状态  展会名 展馆 展会时间段 车牌 状态跟踪
    private TextView exhibitionCheckStatusTv;
    private TextView exhibitionNameTv;
    private TextView exhibitionHallTv;
    private TextView exhibitionIntervalTv;
    private TextView carNumTv;
    private TextView statusTrackTv;
    //参展车辆 品牌 车型 购买年份 行驶里程
    private TextView exhibitionBrandTv;
    private TextView carModelTv;
    private TextView buyYearTv;
    private TextView driverCourseTv;
    private TextView priceTv;
    //展会指南
    private LinearLayout guideLl;
    private TextView ticketTv;
    private TextView signTv;
    private TextView complainTv;
    //申请id;
    public int applyId;
    public String timeStr;
    public String hallStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_exhibition_info);
    }

    @Override
    protected void setUpViews() {
        applyId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);

        imgSdv = (SimpleDraweeView) findViewById(R.id.imgSdv);
        exhibitionCheckStatusTv = (TextView) findViewById(R.id.exhibitionCheckStatusTv);
        exhibitionNameTv = (TextView) findViewById(R.id.exhibitionNameTv);
        exhibitionHallTv = (TextView) findViewById(R.id.exhibitionHallTv);
        exhibitionIntervalTv = (TextView) findViewById(R.id.exhibitionIntervalTv);
        carNumTv = (TextView) findViewById(R.id.carNumTv);
        statusTrackTv = (TextView) findViewById(R.id.statusTrackTv);

        exhibitionBrandTv = (TextView) findViewById(R.id.exhibitionBrandTv);
        carModelTv = (TextView) findViewById(R.id.carModelTv);
        buyYearTv = (TextView) findViewById(R.id.buyYearTv);
        driverCourseTv = (TextView) findViewById(R.id.driverCourseTv);
        priceTv = (TextView) findViewById(R.id.priceTv);

        guideLl = (LinearLayout) findViewById(R.id.guideLl);

        ticketTv = findViewById(R.id.ticketTv);
        signTv = findViewById(R.id.signTv);
        complainTv = findViewById(R.id.complainTv);

        ticketTv.setOnClickListener(this);
        signTv.setOnClickListener(this);
        complainTv.setOnClickListener(this);
        //获取我的展会信息
        MyExhibitionInfoPresenter infoPresenter = new MyExhibitionInfoPresenter();
        infoPresenter.attachView(this);
        infoPresenter.getInfo(IOUtils.getToken(this), applyId);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("我的参展信息");
    }

    @Override
    public void showInfo(MyExhibitionInfoModel model) {
        //展会参展信息
        imgSdv.setImageURI(URLUtil.builderImgUrl(model.avatar));
        exhibitionCheckStatusTv.setText("(" + model.statusName + ")");
        exhibitionNameTv.setText(model.eventName);
        hallStr = model.hallName + "/" + model.parkName;
        exhibitionHallTv.setText(hallStr);
        timeStr = model.eventTime.date + " " + model.eventTime.start + "-" + model.eventTime.end;
        exhibitionIntervalTv.setText(timeStr);
        carNumTv.setText(model.plateNumber);
        //状态跟踪
        statusTrackTv.setText(model.eventStatus.statusName);
        //参展车辆
        exhibitionBrandTv.setText(model.brandName == null ? "" : model.brandName);
        carModelTv.setText((model.carSeriesName == null ? "" : model.carSeriesName) + (model.carName == null ? "" : model.carName));
        buyYearTv.setText(model.buyYear + "");
        driverCourseTv.setText(model.roadHaul);
        priceTv.setText("￥" + model.price);
        //展会指南
        ArrayList<MyExhibitionInfoModel.HelpList> helpList = model.helpList;
        if (helpList.size() != 0 || helpList != null) {
            for (int i = 0; i < helpList.size(); i++) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.exhibition_info_guide_item, guideLl, false);
                TextView guideTitleTv = (TextView) view.findViewById(R.id.guideTitleTv);
                WebView guideWv = (WebView) view.findViewById(R.id.guideWv);

                guideTitleTv.setText(helpList.get(i).title);
                guideWv.getSettings().setDefaultTextEncodingName("UTF-8");
                guideWv.loadDataWithBaseURL("", WebviewUtil.getHtmlData(helpList.get(i).content), "text/html", "UTF-8", "");
                guideLl.addView(view);
            }
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
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ticketTv://电子车证
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, Integer.parseInt(IOUtils.getUserId(mContext)));
                CommonUtils.startNewActivity(mContext, args, CertListActivity.class);
                break;
            case R.id.signTv://展位签到
                Intent intent1 = new Intent(getContext(), ExhibitionPosSignActivity.class);
                //申请id
                intent1.putExtra(Constants.IntentParams.ID, applyId);
                startActivity(intent1);
                break;
            case R.id.complainTv://现场投诉
                Intent intent2 = new Intent(getContext(), ExhibitionDepositActivity.class);
                //申请id
                intent2.putExtra(Constants.IntentParams.ID, applyId);
                //展馆
                intent2.putExtra(Constants.IntentParams.TITLE, hallStr);
                //时间
                intent2.putExtra(Constants.IntentParams.NAME, timeStr);
                startActivity(intent2);
                break;
        }
    }
}
