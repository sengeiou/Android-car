package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.TicketDetailModel;
import com.tgf.kcwc.mvp.presenter.FreeTicketDetailPresenter;
import com.tgf.kcwc.mvp.view.FreeTicketDetailView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.ControlNumberView;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SpecRectView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tgf.kcwc.util.CommonUtils.startNewActivity;

/**
 * Author：Jenny
 * Date:2016/12/29 17:08
 * E-mail：fishloveqin@gmail.com
 * 门票领取
 */

public class ObtainTicketActivity extends BaseActivity implements FreeTicketDetailView {

    protected SimpleDraweeView mImg;
    protected TextView mSenseName;
    protected SpecRectView mSpecRectView;
    protected TextView mTicketType;
    protected TextView mExpire;
    protected TextView mDesc1;
    protected TextView mDesc2;
    protected ControlNumberView mCNView;
    protected TextView moneyTag;
    protected TextView mPrice;
    protected TextView mDesc3;
    protected Button mAcquireBtn;
    private RelativeLayout mTicketDescLayout;
    private FreeTicketDetailPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_obtain_ticket);
        mId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        mPresenter = new FreeTicketDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.loadFreeTicketDetail(mId + "", IOUtils.getToken(mContext));

    }

    private int mId;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(R.string.obtain_ticket);
    }

    @Override
    protected void setUpViews() {
        initView();

        mCNView.setmOnNumberChangedListener(new ControlNumberView.OnNumberChangedListener() {
            @Override
            public void onNumberChangedListener(ControlNumberView controlNumberView, float number) {

                mNums = number;
            }
        });
        mNums = mCNView.getNumber();
    }

    private float mNums;
    private int mMax;
    private TicketDetailModel mModel;

    @Override
    public void showFreeTicketDetail(TicketDetailModel model) {

        if (model.detail.hasContent == 1) {
            mTicketDescLayout.setVisibility(View.VISIBLE);
        } else {
            mTicketDescLayout.setVisibility(View.GONE);
            mAcquireBtn.setEnabled(true);
            mAcquireBtn.setBackgroundColor(mRes.getColor(R.color.btn_select_color));
        }
        mModel = model;
        mImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.detail.cover, 144, 144)));
        mSenseName.setText(model.detail.senseName);
        mSpecRectView.setDrawTicketTypeColor(model.detail.color);
        mMax = model.detail.nums;
        mIds = model.detail.ids;
        int canGetNums = model.detail.canGetNums;
        if (canGetNums == 0) {
            mDesc3.setVisibility(View.GONE);
        } else {
            mDesc3.setVisibility(View.VISIBLE);
        }
        mDesc1.setText(model.detail.remarks + "");
        String tickDesc = String.format(mRes.getString(R.string.ticket_limit_desc),
                model.detail.canGetNums);
        mDesc3.setText(tickDesc);
        mExpire.setText(DateFormatUtil.dispActiveTime2(model.detail.beginTime
                , model.detail.endTime));
        mCNView.setMax(mMax);
        mPrice.setText("" + model.detail.price);
        mPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mTicketType.setText(model.detail.ticketName);
    }

    @Override
    public void receiveTicket(boolean isSuccess) {

        if (isSuccess) {
            CommonUtils.showToast(mContext, "领取成功");
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(mContext, TicketActivity.class);
            startActivity(intent);
        } else {
            CommonUtils.showToast(mContext, "领取失败");
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private void initView() {
        mTicketDescLayout = (RelativeLayout) findViewById(R.id.ticketDescLayout);
        mImg = (SimpleDraweeView) findViewById(R.id.img);
        mSenseName = (TextView) findViewById(R.id.title);
        mSpecRectView = (SpecRectView) findViewById(R.id.specRectView);
        mTicketType = (TextView) findViewById(R.id.ticketType);
        mExpire = (TextView) findViewById(R.id.expire);
        mDesc1 = (TextView) findViewById(R.id.desc1);
        mDesc2 = (TextView) findViewById(R.id.desc2);
        mCNView = (ControlNumberView) findViewById(R.id.cnView);
        mCNView.getBtnAddition().setImageResource(R.drawable.btn_add_selector2);
        mCNView.getBtnSubtraction().setImageResource(R.drawable.btn_subtract_selector2);
        mCNView.getEditTextNumber().setTextColor(mRes.getColor(R.color.text_color15));
        moneyTag = (TextView) findViewById(R.id.moneyTag);
        mPrice = (TextView) findViewById(R.id.price);
        mDesc3 = (TextView) findViewById(R.id.desc3);
        mAcquireBtn = (Button) findViewById(R.id.acquireBtn);
        mAcquireBtn.setOnClickListener(this);
        findViewById(R.id.aboutEx).setOnClickListener(this);
        findViewById(R.id.aboutTicket).setOnClickListener(this);
        findViewById(R.id.accept).setOnClickListener(this);

    }

    List<Integer> mIds = null;

    @Override
    public void onClick(View view) {
        super.onClick(view);

        int id = view.getId();
        switch (id) {

            case R.id.aboutTicket:
                if (mModel == null) {

                    return;
                }
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mModel.detail.eventId);
                String name = "";
                if (mModel != null) {
                    name = mModel.detail.senseName;
                }
                args.put(Constants.IntentParams.DATA, name);
                startNewActivity(mContext, args, AboutTicketActivity.class);
                break;
            case R.id.aboutEx:
                startNewActivity(mContext, AboutExhibitionActivity.class);
                break;

            case R.id.acquireBtn:

                if (mNums > mMax) {
                    CommonUtils.showToast(mContext, "你的限领最大是" + mMax + "张");
                }
                if (mNums == 0) {
                    CommonUtils.showToast(mContext, "请至少选择一张");
                    return;
                }

                if (mIds != null) {

                    int size = (int) mNums;
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < size; i++) {
                        stringBuilder.append(mIds.get(i)).append(",");
                    }
                    String ids = stringBuilder.toString();
                    if (ids.length() > 0) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    mPresenter.receiveTickets(ids, IOUtils.getToken(mContext));
                }

                break;

            case R.id.accept:

                boolean flag = false;
                Object tag = view.getTag();
                if (tag != null) {
                    flag = Boolean.parseBoolean(tag.toString());
                }
                if (flag) {
                    view.setSelected(false);
                    mAcquireBtn.setEnabled(false);
                    mAcquireBtn.setBackgroundColor(mRes.getColor(R.color.text_color2));
                    view.setTag(false);
                } else {
                    view.setSelected(true);
                    mAcquireBtn.setEnabled(true);
                    mAcquireBtn.setBackgroundColor(mRes.getColor(R.color.btn_select_color));
                    view.setTag(true);
                }

                break;
        }
    }

}
