package com.tgf.kcwc.transmit;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autonavi.rtbt.IFrameForRTBT;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.TransmWinningResultBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;
import com.tgf.kcwc.mvp.model.TransmitWinningRaffleBean;
import com.tgf.kcwc.mvp.presenter.TransmitWinningRafflePresenter;
import com.tgf.kcwc.mvp.view.TransmitWinningRaffleView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.LuckPanLayout;
import com.tgf.kcwc.view.RotatePan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/28.
 */

public class TransmitWinningDialActivity extends BaseActivity
        implements LuckPanLayout.AnimationEndListener, TransmitWinningRaffleView {
    private TransmitWinningRafflePresenter mTransmitWinningDetailsPresenter;
    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private ImageView goBtn;
    private TextView raffle;
    private TextView number; //次数
    private RelativeLayout mTitleBar;
    private List<DataItem> mDataList = new ArrayList<>();//type数据
    List<TransmitWinningRaffleBean.DataList> mPrizeList = new ArrayList<>();
    private String ID;
    TransmitWinningRaffleBean.DataList mPrizeData;

    public boolean isOne = true;
    TransmitWinningRaffleBean mTransmitWinningRaffleBean;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        back.setImageResource(R.drawable.nav_back_selector2);
        backEvent(back);
        text.setText("转发有奖");
        text.setTextSize(16);
        text.setTextColor(getResources().getColor(R.color.text_color14));
        function.setTextResource("抽奖记录",R.color.text_color15,14);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, ID);
                CommonUtils.startNewActivity(mContext, args, RaffleRecordActivity.class);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitwinningdial);
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        rotatePan = (RotatePan) findViewById(R.id.rotatePan);
        luckPanLayout.setAnimationEndListener(this);
        goBtn = (ImageView) findViewById(R.id.go);
        raffle = (TextView) findViewById(R.id.raffle);
        number = (TextView) findViewById(R.id.number);
        goBtn.setOnClickListener(this);
        raffle.setOnClickListener(this);
        mTitleBar = (RelativeLayout) findViewById(R.id.titleBar);
        mTitleBar.setBackgroundColor(getResources().getColor(R.color.app_layout_bg_color));
        mTransmitWinningDetailsPresenter = new TransmitWinningRafflePresenter();
        mTransmitWinningDetailsPresenter.attachView(this);
        mTransmitWinningDetailsPresenter.getPrizeList(IOUtils.getToken(mContext), ID);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.raffle:
                if (mTransmitWinningRaffleBean.data.nums.canNums <= 0) {
                    CommonUtils.showToast(mContext, "您暂时没有抽奖次数");
                } else {
                    mTransmitWinningDetailsPresenter.getForward(IOUtils.getToken(mContext), ID, "2");
                }
                break;
            case R.id.go:
             /*   int width = goBtn.getWidth();
                int height = goBtn.getHeight();
                RotateAnimation rotateAnimation= new RotateAnimation(0, 720, width/2,width/2);
                rotateAnimation.setDuration(1000 * 3);
                rotateAnimation.setFillAfter(true);
                goBtn.startAnimation(rotateAnimation);*/
                // mTransmitWinningDetailsPresenter.getForward(IOUtils.getToken(mContext), ID, "2");
                break;
        }
    }


    /**
     * 谢谢参与
     */
    public void setRequireDefeated() {
        View diaEdtext = View.inflate(mContext, R.layout.transmitwinning_popwindows, null);
        TextView know = (TextView) diaEdtext.findViewById(R.id.know);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(diaEdtext).setPositiveButton("", null).setNegativeButton("", null).show();

        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mTransmitWinningDetailsPresenter.getPrizeList(IOUtils.getToken(mContext), ID);
                alertDialog.dismiss();
            }
        });
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.height = (int) (WindowManager.LayoutParams.WRAP_CONTENT); //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.5
        alertDialog.getWindow().setAttributes(p); //设置生效

    }

    /**
     * 获得奖品
     */
    public void setRequireSucceed() {
        View diaEdtext = View.inflate(mContext, R.layout.transmitwinning_popwindows_succeed, null);
        TextView know = (TextView) diaEdtext.findViewById(R.id.know);
        TextView title = (TextView) diaEdtext.findViewById(R.id.title);
        title.setText(mPrizeData.subName);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(diaEdtext).setPositiveButton("", null).setNegativeButton("", null).show();

        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mTransmitWinningDetailsPresenter.getPrizeList(IOUtils.getToken(mContext), ID);
                alertDialog.dismiss();
            }
        });
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.height = (int) (WindowManager.LayoutParams.WRAP_CONTENT); //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.5
        alertDialog.getWindow().setAttributes(p); //设置生效

    }

    public void rotation(int num) {
        luckPanLayout.rotate(num, 100);
        raffle.setEnabled(false);
    }

    @Override
    public void endAnimation(int position) {
        raffle.setEnabled(true);
        if (mPrizeData.id > 0) {
            setRequireSucceed();
        } else {
            setRequireDefeated();
        }
        mTransmitWinningDetailsPresenter.getPrizeList(IOUtils.getToken(mContext), ID);
        //Toast.makeText(this, "Position = " + position + "," + mPrizeList.get(position).name, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void dataListSucceed(TransmitWinningRaffleBean transmitWinningRaffleBean) {
        mTransmitWinningRaffleBean = transmitWinningRaffleBean;
        if (isOne) {
            mPrizeList.clear();
            mDataList.clear();
            List<TransmitWinningRaffleBean.DataList> prizeList = transmitWinningRaffleBean.data.list;
            mPrizeList.addAll(prizeList);
            for (int i = 0; i < mPrizeList.size(); i++) {
                DataItem dataItem = new DataItem();
                dataItem.id = mPrizeList.get(i).id;
                dataItem.name = mPrizeList.get(i).name;
                mDataList.add(dataItem);
            }
            rotatePan.setData(mDataList);
            isOne = false;
        }
        number.setText(transmitWinningRaffleBean.data.nums.canNums + "");
    }

    @Override
    public void dataForwardSucceed(TransmWinningResultBean BaseBean) {
        if (mPrizeList.size() > 0) {
            for (int i = 0; i < mPrizeList.size(); i++) {
                if (mPrizeList.get(i).id == BaseBean.data.prizeId) {
                    mPrizeData = mPrizeList.get(i);
                    rotation(i);
                    return;
                }
            }
            if (mPrizeData == null) {
                CommonUtils.showToast(mContext, "系统异常请稍后再试");
            }
        } else {
            CommonUtils.showToast(mContext, "系统异常请稍后再试");
            mTransmitWinningDetailsPresenter.getPrizeList(IOUtils.getToken(mContext), ID);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void dataListDefeated(String msg) {
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

}
