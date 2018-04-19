package com.tgf.kcwc.driving.driv;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.please.PleasePlayActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.QrcodeSiginBean;
import com.tgf.kcwc.mvp.presenter.SignInPresenter;
import com.tgf.kcwc.mvp.view.SignInView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/26.
 */

public class SignInActivity extends BaseActivity implements SignInView {

    private SignInPresenter mSignInPresenter;
    MyListView mylistview;
    SimpleDraweeView mSimpleDraweeView;
    DrivingRoadBookBean mDrivingRoadBookBean = null;
    List<DrivingRoadBookBean.RideCheck> dataRideLeaders = new ArrayList<>();
    String id = "";
    String type = "";
    private WrapAdapter<DrivingRoadBookBean.RideCheck> mSigninAdapter;
    TextView mEndText;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.signin);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mSignInPresenter = new SignInPresenter();
        mSignInPresenter.attachView(this);
        id = getIntent().getStringExtra(Constants.IntentParams.ID);
        type = getIntent().getStringExtra(Constants.IntentParams.ID2);
        mylistview = (MyListView) findViewById(R.id.mylistview);
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.mapimag);
        mEndText = (TextView) findViewById(R.id.endtext);
        if (mDrivingRoadBookBean != null) {
            dataRideLeaders.addAll(mDrivingRoadBookBean.data.rideCheck);
        }
        mSigninAdapter = new WrapAdapter<DrivingRoadBookBean.RideCheck>(mContext, R.layout.activity_signin_item, dataRideLeaders) {
            @Override
            public void convert(ViewHolder helper, final DrivingRoadBookBean.RideCheck item) {
                final int position = helper.getPosition();
                ImageView qrcode = helper.getView(R.id.qrcode);
                RelativeLayout currentPosLayout = helper.getView(R.id.currentPosLayout);
                ImageView endpoint = helper.getView(R.id.endpoint);
                TextView number = helper.getView(R.id.number);
                TextView sta = helper.getView(R.id.sta);
                TextView title = helper.getView(R.id.title);
                qrcode.setVisibility(View.VISIBLE);
                endpoint.setVisibility(View.VISIBLE);

                if (position == 0) {
                    currentPosLayout.setVisibility(View.GONE);
                    sta.setVisibility(View.VISIBLE);
                    sta.setText("起");
                    sta.setTextColor(getResources().getColor(R.color.text_color10));
                } else if (position == (dataRideLeaders.size() - 1)) {
                    currentPosLayout.setVisibility(View.GONE);
                    sta.setVisibility(View.VISIBLE);
                    sta.setText("终");
                    sta.setTextColor(getResources().getColor(R.color.text_color20));
                    qrcode.setVisibility(View.VISIBLE);
                } else {
                    currentPosLayout.setVisibility(View.VISIBLE);
                    sta.setVisibility(View.GONE);
                    number.setText(position + "");
                }

                if (item.isLight == 1) {
                    qrcode.setVisibility(View.VISIBLE);
                    endpoint.setVisibility(View.GONE);
                    endpoint.setImageResource(R.drawable.icon_dengpao2);
                } else {
                    qrcode.setVisibility(View.GONE);
                    endpoint.setImageResource(R.drawable.icon_dengpao);
                }

                endpoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isLight != 1) {
                            mSignInPresenter.getLight(IOUtils.getToken(mContext), item.id + "");
                        }
                    }
                });

                title.setText(item.address);
                qrcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isLight == 1) {
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.DATA, item);
                            args.put(Constants.IntentParams.ID, item.id + "");
                            args.put(Constants.IntentParams.ID3, item.address);
                            args.put(Constants.IntentParams.ID2, (position + 1));
                            if (IOUtils.isLogin(mContext)) {
                                CommonUtils.startNewActivity(mContext, args, QrcodeSignInActivity.class);
                            }
                        } else {
                            CommonUtils.showToast(mContext, "请点亮该节点，方可进行签到");
                        }

                    }
                });
            }
        };
        mylistview.setAdapter(mSigninAdapter);
        mEndText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignInPresenter.getEndAcitivity(IOUtils.getToken(mContext), id);
            }
        });

        mSignInPresenter.getDetail(IOUtils.getToken(mContext), id);
    }


    @Override
    public void DetailsSucceed(DrivingRoadBookBean drivingRoadBookBean) {
        dataRideLeaders.clear();
        mDrivingRoadBookBean = drivingRoadBookBean;
        DrivingRoadBookBean.PlanLine dataRidePlan = drivingRoadBookBean.data.planLine;
        mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(dataRidePlan.cover, 540, 270)));
        dataRideLeaders.addAll(drivingRoadBookBean.data.rideCheck);
        mSigninAdapter.notifyDataSetChanged();
    }

    @Override
    public void CreateChecksSucceed(QrcodeSiginBean qrcodeSiginBean) {

    }

    @Override
    public void EndAcitivitySucceed(BaseArryBean baseBean) {
        CommonUtils.showToast(mContext, "活动结束");
/*        Intent intent = null;
        if (type.equals("1")) {
            intent = new Intent(mContext, DrivingActivity.class);
        } else if (type.equals("2")) {
            intent = new Intent(mContext, PleasePlayActivity.class);
        } else if (type.equals("3")) {
            intent = new Intent(mContext, com.tgf.kcwc.membercenter.driv.DrivingActivity.class);
        } else if (type.equals("4")) {
            intent = new Intent(mContext, com.tgf.kcwc.membercenter.please.PleasePlayActivity.class);
        } else if (type.equals("5")) {
            intent = new Intent(mContext, PleasePlayDetailsActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
        finish();
    }

    @Override
    public void LightenSucceed(BaseBean baseBean) {
        mSignInPresenter.getDetail(IOUtils.getToken(mContext), id);
    }

    @Override
    public void detailsDataFeated(String msg) {
        CommonUtils.showToast(mContext, msg);
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
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSignInPresenter != null) {
            mSignInPresenter.detachView();
        }
    }
}
