package com.tgf.kcwc.signin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.sale.MyExhibitionInfoActivity;
import com.tgf.kcwc.mvp.model.ExhibitionPosQrModel;
import com.tgf.kcwc.mvp.model.MyExhibitionInfoModel;
import com.tgf.kcwc.mvp.presenter.ExhibitionPosQrPresenter;
import com.tgf.kcwc.mvp.presenter.MyExhibitionInfoPresenter;
import com.tgf.kcwc.mvp.view.ExhibitionPosQrView;
import com.tgf.kcwc.mvp.view.MyExhibitionInfoView;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * @anthor noti
 * @time 2017/9/14
 * @describle 展位签到
 */
public class ExhibitionPosSignActivity extends BaseActivity implements MyExhibitionInfoView{
    //展馆展位
    private TextView exhibitionHallPos;
    //时间
    private TextView exhibitionTime;
    private ImageView imgSdv;
    //品牌
    private TextView exhibitionBrand;
    //车牌
    private TextView carNum;
    //进入展位签到
    private LinearLayout signInLl;
    private TextView qrCodeTime;
    private ImageView qrCodeImg;

    private ImageView qrCodeImgLeave;
    private TextView qrCodeTimeLeave;
    //离开展位打卡
    private LinearLayout signOutLl;
    //展位指引
    private Button exhibitionGuideBtn;
    //投诉
    private Button depositBtn;
    //申请id;
    public int applyId;
    public int eventId;
//    //扫描后的数据
//    public int parkCode;
//    //签到或者打卡
//    public int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        parkCode = getIntent().getIntExtra(Constants.IntentParams.ID2, -1);
//        type = getIntent().getIntExtra(Constants.IntentParams.ID3, -1);
        setContentView(R.layout.activity_exhibition_pos_sign);
    }

    @Override
    protected void setUpViews() {
        applyId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);

        exhibitionHallPos = (TextView) findViewById(R.id.exhibitionHallPos);
        exhibitionTime = (TextView) findViewById(R.id.exhibitionTime);
        imgSdv = (ImageView) findViewById(R.id.imgSdv);
        exhibitionBrand = (TextView) findViewById(R.id.exhibitionBrand);
        carNum = (TextView) findViewById(R.id.carNum);

        signInLl = (LinearLayout) findViewById(R.id.signInLl);
        qrCodeTime = findViewById(R.id.qrCodeTime);
        qrCodeImg = findViewById(R.id.qrCodeImg);

        qrCodeImgLeave = findViewById(R.id.qrCodeImgLeave);
        qrCodeTimeLeave = findViewById(R.id.qrCodeTimeLeave);
        signOutLl = (LinearLayout) findViewById(R.id.signOutLl);

        exhibitionGuideBtn = (Button) findViewById(R.id.exhibitionGuideBtn);
        depositBtn = (Button) findViewById(R.id.depositBtn);

        signInLl.setOnClickListener(this);
        signOutLl.setOnClickListener(this);
        exhibitionGuideBtn.setOnClickListener(this);
        depositBtn.setOnClickListener(this);

        MyExhibitionInfoPresenter infoPresenter = new MyExhibitionInfoPresenter();
        infoPresenter.attachView(this);
        infoPresenter.getInfo(IOUtils.getToken(this), applyId);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("展位签到");
//        function.setImageResource(R.drawable.icon_common_right);
//        function.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            protected void onSingleClick(View view) {
//                // TODO: 2017/9/14 首页，消息，反馈
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.signInLl://进入展位签到
                Intent intent = new Intent(this, ScannerCodeActivity.class);
//                intent.putExtra(Constants.IntentParams.FROM_TYPE, 1);
//                intent.putExtra(Constants.IntentParams.ID, applyId);
//                intent.putExtra(Constants.IntentParams.ID2, eventId);
                startActivity(intent);
                break;
            case R.id.signOutLl://离开展位打卡
                Intent leaveIntent = new Intent(this, ScannerCodeActivity.class);
                leaveIntent.putExtra(Constants.IntentParams.FROM_TYPE, 2);
                leaveIntent.putExtra(Constants.IntentParams.ID, applyId);
                leaveIntent.putExtra(Constants.IntentParams.ID2, eventId);
                startActivity(leaveIntent);
                break;
            case R.id.exhibitionGuideBtn://展位指引
                Intent intent1 = new Intent(this, MyExhibitionInfoActivity.class);
                intent1.putExtra(Constants.IntentParams.ID, applyId);
                startActivity(intent1);
                break;
            case R.id.depositBtn://投诉
                CommonUtils.startNewActivity(this, ExhibitionDepositActivity.class);
                break;
        }
    }

    @Override
    public void showInfo(MyExhibitionInfoModel model) {
        if (null == model){
            return;
        }
        eventId = model.eventId;
        exhibitionHallPos.setText(model.hallName + "/" + model.parkName);
        exhibitionTime.setText(model.parkTimeSlot);
        imgSdv.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.avatar, 144, 144)));
        exhibitionBrand.setText((model.brandName == null?"":model.brandName) + " " + (model.carSeriesName == null ? "" :model.carSeriesName) + " " + (model.carName == null ? "" :model.carName));
        carNum.setText("车牌号：" + model.plateNumber);
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
}
