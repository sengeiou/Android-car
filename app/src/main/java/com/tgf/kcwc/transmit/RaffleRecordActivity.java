package com.tgf.kcwc.transmit;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.autonavi.rtbt.IFrameForRTBT;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.VoucherMainActivity;
import com.tgf.kcwc.driving.driv.SponsorDrivingActivity;
import com.tgf.kcwc.mvp.model.RaffleRecordBean;
import com.tgf.kcwc.mvp.presenter.TransmitWinningRaffleRecordPresenter;
import com.tgf.kcwc.mvp.view.TransmitWinningRaffleRecordView;
import com.tgf.kcwc.ticket.TicketActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.VacancyListView;
import com.tgf.kcwc.view.link.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/10.
 */

public class RaffleRecordActivity extends BaseActivity implements TransmitWinningRaffleRecordView {

    private TransmitWinningRaffleRecordPresenter mTransmitWinningRaffleRecordPresenter;
    private ListView mListView;
    private VacancyListView mVacancyListView;
    private WrapAdapter<RaffleRecordBean.DataList> mDatadapter;
    private List<RaffleRecordBean.DataList> mDataLists = new ArrayList<>();

    private String ID = "";

    @Override
    protected void setUpViews() {
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        mTransmitWinningRaffleRecordPresenter = new TransmitWinningRaffleRecordPresenter();
        mTransmitWinningRaffleRecordPresenter.attachView(this);
        mListView = (ListView) findViewById(R.id.listview);
        mVacancyListView = (VacancyListView) findViewById(R.id.hintlayout);
        mVacancyListView.setmHintText("您暂时还没有中奖记录哟");
        mDatadapter = new WrapAdapter<RaffleRecordBean.DataList>(mContext, R.layout.activity_rafflerecor_item, mDataLists) {
            @Override
            public void convert(ViewHolder helper, final RaffleRecordBean.DataList item) {
                TextView name = helper.getView(R.id.name);
                TextView time = helper.getView(R.id.time);
                final TextView examine = helper.getView(R.id.examine);
                time.setText(item.createTime);
                if (item.forwardPrizeInfo != null) {
                    if (item.forwardPrizeInfo.type == 2) {
                        examine.setVisibility(View.VISIBLE);
                    } else {
                        examine.setVisibility(View.GONE);
                    }
                } else {
                    examine.setVisibility(View.GONE);
                }
                if (item.isPrize == 1) {
                    name.setText(item.forwardPrizeInfo.remarks);
                } else {
                    name.setText("谢谢参与");
                }
                examine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.forwardPrizeInfo.prizeType == 1) {
                            Map<String, Serializable> args = new HashMap<>();
                            CommonUtils.startNewActivity(mContext, null, TicketActivity.class);
                        } else {
                            Map<String, Serializable> args = new HashMap<>();
                            args.put(Constants.IntentParams.INDEX, 3);
                            CommonUtils.startNewActivity(mContext, args, VoucherMainActivity.class);
                        }
                    }
                });
            }
        };
        mListView.setAdapter(mDatadapter);
        mTransmitWinningRaffleRecordPresenter.getrecordDetails(IOUtils.getToken(mContext), ID);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("抽奖记录");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rafflerecor);
    }

    public View getView() {
        View diaEdtext = View.inflate(mContext, R.layout.activity_rafflerecor_hint, null);
        return diaEdtext;
    }

    @Override
    public void dataListSucceed(RaffleRecordBean raffleRecordBean) {
        mDataLists.clear();
        mDataLists.addAll(raffleRecordBean.data.list);
        mDatadapter.notifyDataSetChanged();
        if (mDataLists.size() == 0) {
            mListView.setVisibility(View.GONE);
            mVacancyListView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mVacancyListView.setVisibility(View.GONE);
            mListView.addFooterView(getView());
        }

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
