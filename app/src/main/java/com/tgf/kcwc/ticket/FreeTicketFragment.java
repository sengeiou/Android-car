package com.tgf.kcwc.ticket;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.FreeTicketListModel;
import com.tgf.kcwc.mvp.presenter.FreeTicketListPresenter;
import com.tgf.kcwc.mvp.view.FreeTicketListView;
import com.tgf.kcwc.see.exhibition.ExhibitionListActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.SpecRectView;
import com.tgf.kcwc.view.countdown.CountdownView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.tgf.kcwc.R.id.priceTv;

/**
 * Author：Jenny
 * Date:2016/12/29 10:06
 * E-mail：fishloveqin@gmail.com
 * 赠票
 */

public class FreeTicketFragment extends BaseFragment implements FreeTicketListView {
    protected View rootView;
    protected Button mBuyTicketBtn1;
    protected TextView mRecordBtn1;
    protected RelativeLayout mBottomLayout;
    protected RelativeLayout mRootLayout;
    protected Button mBuyTicketBtn2;
    protected TextView mRecordBtn2;
    protected RelativeLayout mEmptyLayout;
    private ListView mList;
    private TextView mRecordBtn;
    private FreeTicketListPresenter mPresenter;

    @Override
    protected void updateData() {

        mPresenter.loadFreeTickets("", "", "0", IOUtils.getToken(mContext));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_free_ticket;
    }

    @Override
    protected void initView() {
        mPresenter = new FreeTicketListPresenter();
        mPresenter.attachView(this);

        mList = findView(R.id.list);
        mBuyTicketBtn1 = findView(R.id.buyTicketBtn1);
        mRecordBtn1 = findView(R.id.recordBtn1);
        mBottomLayout = findView(R.id.bottomLayout);
        mRootLayout = findView(R.id.rootLayout);
        mBuyTicketBtn2 = findView(R.id.buyTicketBtn2);
        mRecordBtn2 = findView(R.id.recordBtn2);
        mEmptyLayout = findView(R.id.emptyLayout);
        mBuyTicketBtn1.setOnClickListener(this);
        mBuyTicketBtn2.setOnClickListener(this);
        mRecordBtn1.setOnClickListener(this);
        mRecordBtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.recordBtn1:
            case R.id.recordBtn2:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mSenseId);
                CommonUtils.startNewActivity(mContext, args, FialureRecordActivity.class);
                break;
            case R.id.buyTicketBtn1:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mSenseId);
                CommonUtils.startNewActivity(mContext, args, ExhibitionListActivity.class);
                break;
            case R.id.buyTicketBtn2:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mSenseId);
                CommonUtils.startNewActivity(mContext, args, ExhibitionListActivity.class);
                break;
        }

    }

    @Override
    public void showFreeTickets(FreeTicketListModel model) {

        int size = model.list.size();
        if (size == 0) {
            mRootLayout.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mRootLayout.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }
        mList.setAdapter(new WrapAdapter<FreeTicketListModel.FreeTicketItem>(mContext, model.list,
                R.layout.free_ticket_list_item) {
            @Override
            public void convert(ViewHolder helper, final FreeTicketListModel.FreeTicketItem item) {

                SpecRectView specRectView = helper.getView(R.id.specRectView);
                String color = item.ticket.color;
                if (!TextUtils.isEmpty(color)) {
                    specRectView.setBGColor(color);
                }

                helper.setText(R.id.title, item.sense.name + "");
                helper.setText(R.id.type, item.ticket.name + "");
                TextView numTv = helper.getView(R.id.numTv);
                CountdownView setTimeText = helper.getView(R.id.setTimeText);
                View v = helper.getView(R.id.counterLayout);
                long deltaTime = DateFormatUtil.getTime(item.loseTime)
                        - DateFormatUtil.getTime(item.nowTime);
                TextView hadEXPTv = helper.getView(R.id.hadExpire);
                Button acquireBtn = helper.getView(R.id.acquireBtn);

                if (item.receiveTimeLimit > 0) {
                    if (deltaTime > 0) {
                        v.setVisibility(View.VISIBLE);
                        setTimeText.start(deltaTime);
                        hadEXPTv.setVisibility(View.GONE);
                        acquireBtn.setVisibility(View.VISIBLE);
                    } else {
                        v.setVisibility(View.GONE);
                        hadEXPTv.setVisibility(View.VISIBLE);
                        acquireBtn.setVisibility(View.GONE);
                    }

                } else if (item.receiveTimeLimit == 0) {
                    v.setVisibility(View.GONE);
                    hadEXPTv.setVisibility(View.GONE);
                    acquireBtn.setVisibility(View.VISIBLE);
                }
                numTv.setText("" + item.nums);

                //                helper.setText(R.id.expire, DateFormatUtil.formatTime(item.ticket.beginTime) + "-"
                //                                            + DateFormatUtil.formatTime(item.ticket.endTime));
                helper.setText(R.id.expire, item.ticket.remarks);
                TextView priceTv = helper.getView(R.id.price);
                priceTv.setText(item.ticket.price + "");
                priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                helper.setText(R.id.desc, item.workerInfo.nickname + "");
                helper.getView(R.id.acquireBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id);
                        CommonUtils.startNewActivity(mContext, args, ObtainTicketActivity.class);

                    }
                });

            }
        });

    }

    @Override
    public void setLoadingIndicator(boolean active) {

        if (active) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingTasksError() {

        CommonUtils.showToast(mContext, "网络异常，稍候再试！");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
