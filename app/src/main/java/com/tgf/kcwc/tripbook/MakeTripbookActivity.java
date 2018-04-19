package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.mvp.model.RideDataModel;
import com.tgf.kcwc.mvp.presenter.SelectRoadLinePresenter;
import com.tgf.kcwc.mvp.view.SelectRoadlLineView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NetUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NotitleContentDialog;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/2 0002
 * E-mail:hekescott@qq.com
 */

public class MakeTripbookActivity extends BaseActivity implements SelectRoadlLineView {
    private SelectRoadLinePresenter selectRoadLinePresenter;
    private String                  token;
    private View                    rootNobook;
    private TextView                noticeTitle;
    private TextView                noticeMsg;
    private int                     mRideId;
    private TextView                cancelBtn;
    private TextView                yesBtn;
    private RideDataModel           mRideDataModel;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("制作路书");
    }

    @Override
    protected void setUpViews() {
        token = IOUtils.getToken(this);
        cancelBtn = (TextView) findViewById(R.id.roadline_cancel);
        cancelBtn.setOnClickListener(this);
        yesBtn = (TextView) findViewById(R.id.roadline_yes);
        yesBtn.setOnClickListener(this);
        noticeTitle = (TextView) findViewById(R.id.notice_titleTv);
        noticeMsg = (TextView) findViewById(R.id.notice_msgTv);
        rootNobook = findViewById(R.id.eidtbook_nobookroot);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maketripbook);
        selectRoadLinePresenter = new SelectRoadLinePresenter();
        selectRoadLinePresenter.attachView(this);

        if (!NetUtil.isNetworkAvailable(this)) {
            CommonUtils.showToast(this, "网络异常，请稍后再试");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        rootNobook.setVisibility(View.GONE);
        selectRoadLinePresenter.getRoadLinelist(token);
    }

    @Override
    public void showRoadLines(List<RideDataModel.RideData> rideDataList) {
        rootNobook.setVisibility(View.VISIBLE);
        if (rideDataList == null || rideDataList.size() != 0) {
            mRideId = -1;
            rootNobook.setVisibility(View.VISIBLE);
            noticeTitle.setText("路书需含驾途路线");
            noticeMsg.setText("从已有驾途路线中选择，还是开始新的驾途？");
            yesBtn.setText("开始驾途");
            cancelBtn.setText("驾途路线");
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent   toDringIntent = new Intent(getContext(), DrivingHomeActivity.class);
                    toDringIntent.putExtra(Constants.IntentParams.DATA,
                            Constants.IntentParams.RIDE_BOOK_MODULE);
                    toDringIntent.putExtra(Constants.IntentParams.ID, -1);
                    toDringIntent.putExtra(Constants.IntentParams.DATA2,
                            Constants.IntentParams.ALL_NEWLINE);
                    startActivity(toDringIntent);
                    finish();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toSelecRoadLine = new Intent(MakeTripbookActivity.this,
                            SelectRoadLineActivity.class);
                    toSelecRoadLine.putExtra(Constants.IntentParams.INTENT_TYPE, 100);
                    startActivity(toSelecRoadLine);
                }
            });

        } else {
            rootNobook.setVisibility(View.VISIBLE);
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent   toDringIntent = new Intent(getContext(), DrivingHomeActivity.class);
                    toDringIntent.putExtra(Constants.IntentParams.DATA,
                            Constants.IntentParams.RIDE_BOOK_MODULE);
                    toDringIntent.putExtra(Constants.IntentParams.ID, -1);
                    toDringIntent.putExtra(Constants.IntentParams.DATA2,
                            Constants.IntentParams.ALL_NEWLINE);
                    startActivity(toDringIntent);
                    finish();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }

    }

    @Override
    public void showContinue(RideDataModel rideDataModel) {
        rootNobook.setVisibility(View.VISIBLE);
        mRideId = rideDataModel.ride_id;
        mRideDataModel = rideDataModel;
        noticeTitle.setText("检测到您当前有未结束的驾途路线");
        noticeMsg.setText("选择继续制作，还是开始新的路线？");
        yesBtn.setText("新路线");
        cancelBtn.setText("继续制作");
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent   toDringIntent = new Intent(getContext(), DrivingHomeActivity.class);
                toDringIntent.putExtra(Constants.IntentParams.DATA,
                        Constants.IntentParams.RIDE_BOOK_MODULE);
                toDringIntent.putExtra(Constants.IntentParams.ID, mRideId);
                toDringIntent.putExtra(Constants.IntentParams.DATA2,
                    Constants.IntentParams.CONTINUE_NEWLINE);
                startActivity(toDringIntent);
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRideDataModel.nodeList != null && mRideDataModel.nodeList.size() != 0) {
                    Intent toEditBook = new Intent(getContext(), EditTripbookActivity.class);
                    toEditBook.putExtra(Constants.IntentParams.ID, mRideId);
                    startActivity(toEditBook);
                    finish();
                } else {
                    CommonUtils.showToast(getContext(), "没有继续的点");
                }
            }
        });

    }

    @Override
    public void showUnStop(final RideDataModel unStoprideDataModel) {
        final NotitleContentDialog unStopDialog = new NotitleContentDialog(getContext());
        unStopDialog.setContentText(" 您有未结束的行驶轨迹，进入驾途继续,还是结束运行？");
        unStopDialog.setCancelText("结束");
        unStopDialog.setYesText("继续");
        unStopDialog.setOnLoginOutClickListener(new NotitleContentDialog.IOnclickLisenter() {
            @Override
            public void OkClick() {
                jumpToRidePage(false,unStoprideDataModel);
                unStopDialog.dismiss();
                finish();
            }

            @Override
            public void CancleClick() {
                jumpToRidePage(true,unStoprideDataModel);
                unStopDialog.dismiss();
                finish();
            }
        });
        unStopDialog.show();

    }

    private void jumpToRidePage(boolean isEnd, RideDataModel unStoprideDataModel) {

        Intent toDringIntent = new Intent(getContext(), DrivingHomeActivity.class);
        toDringIntent.putExtra(Constants.IntentParams.BG_RIDE_TYPE, 1);
        toDringIntent.putExtra(Constants.IntentParams.ID, unStoprideDataModel.ride_id);
        toDringIntent.putExtra(Constants.IntentParams.DATA,
            Constants.IntentParams.RIDE_BOOK_MODULE);
        toDringIntent.putParcelableArrayListExtra(Constants.IntentParams.DATA3,
            unStoprideDataModel.nodeList);
        toDringIntent.putExtra(Constants.IntentParams.IS_END, isEnd);
        startActivity(toDringIntent);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public Context getContext() {
        return this;
    }

}
