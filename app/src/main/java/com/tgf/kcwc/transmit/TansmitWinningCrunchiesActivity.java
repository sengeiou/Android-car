package com.tgf.kcwc.transmit;

import java.util.ArrayList;
import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.MeCrunchiesBean;
import com.tgf.kcwc.mvp.model.TransmitWinningCrunchiesBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;
import com.tgf.kcwc.mvp.presenter.TransmitWinningCrunchiesPresenter;
import com.tgf.kcwc.mvp.view.TransmitWinningCrunchiesView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/7.
 */

public class TansmitWinningCrunchiesActivity extends BaseActivity implements TransmitWinningCrunchiesView {

    private TransmitWinningCrunchiesPresenter mTransmitWinningCrunchiesPresenter;
    private ListView mListView;
    private WrapAdapter<TransmitWinningCrunchiesBean.DataList> mCrunchieslistviewAdapter;
    private List<TransmitWinningCrunchiesBean.DataList> dataList = new ArrayList<>();
    private String ID = "";

    private TextView mNumtext;
    private SimpleDraweeView mHead;
    private SimpleDraweeView mGender;
    private TextView mName;
    private TextView mIntegral;
    private TextView mTime;

    @Override
    protected void setUpViews() {
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        mListView = (ListView) findViewById(R.id.list);
        mNumtext = (TextView) findViewById(R.id.numtext);
        mHead = (SimpleDraweeView) findViewById(R.id.head);
        mGender = (SimpleDraweeView) findViewById(R.id.genderImg);
        mName = (TextView) findViewById(R.id.name);
        mIntegral = (TextView) findViewById(R.id.integral);
        mTime = (TextView) findViewById(R.id.time);
        //榜单
        mCrunchieslistviewAdapter = new WrapAdapter<TransmitWinningCrunchiesBean.DataList>(mContext,
                R.layout.activity_transmitcrunchies_item, dataList) {
            @Override
            public void convert(ViewHolder helper, TransmitWinningCrunchiesBean.DataList item) {
                int position = helper.getPosition();
                TextView numtext = helper.getView(R.id.numtext);
                ImageView numimag = helper.getView(R.id.numimag);
                SimpleDraweeView head = helper.getView(R.id.head);
                SimpleDraweeView genderImg = helper.getView(R.id.genderImg);
                TextView name = helper.getView(R.id.name);
                TextView integral = helper.getView(R.id.integral);
                TextView xuwei = helper.getView(R.id.xuwei);
                LinearLayout noxuwei = helper.getView(R.id.noxuwei);

                int nums = position + 1;
                numtext.setVisibility(View.GONE);
                numimag.setVisibility(View.GONE);
                if (nums == 1) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_gold);
                } else if (nums == 2) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_yin);
                } else if (nums == 3) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_tong);
                } else {
                    numtext.setVisibility(View.VISIBLE);
                    numimag.setVisibility(View.GONE);
                    numtext.setText(nums + "");
                }

                if (item.id == -1) {
                    genderImg.setVisibility(View.GONE);
                    noxuwei.setVisibility(View.GONE);
                    xuwei.setVisibility(View.VISIBLE);
                    head.setImageResource(R.drawable.xuweiyidai);
                } else {
                    genderImg.setVisibility(View.VISIBLE);
                    noxuwei.setVisibility(View.VISIBLE);
                    xuwei.setVisibility(View.GONE);
                    head.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.userInfo.avatar, 144, 144)));
                    integral.setText("贡献值 " + item.score);
                    name.setText(item.userInfo.nickname);
                    genderImg.setImageResource(R.drawable.icon_male);
                    if (item.userInfo.sex == 1) {
                        genderImg.setImageResource(R.drawable.icon_male);
                    } else {
                        genderImg.setImageResource(R.drawable.icon_famale);
                    }
                }
            }
        };
        mListView.setAdapter(mCrunchieslistviewAdapter);
        mTransmitWinningCrunchiesPresenter = new TransmitWinningCrunchiesPresenter();
        mTransmitWinningCrunchiesPresenter.attachView(this);
        mTransmitWinningCrunchiesPresenter.getRecordLists(IOUtils.getToken(mContext), ID);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("活动榜单");
        function.setImageResource(R.drawable.btn_more);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitcrunchies);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public void dataListSucceed(TransmitWinningCrunchiesBean transmitWinningHomeListBean) {
        dataList.clear();
        dataList.addAll(transmitWinningHomeListBean.data.list);
        int size = dataList.size();
        if (size < 10) {
            for (int i = 0; i < 10 - size; i++) {
                TransmitWinningCrunchiesBean.DataList data = new TransmitWinningCrunchiesBean.DataList();
                data.id = -1;
                dataList.add(data);
            }
        }
        mCrunchieslistviewAdapter.notifyDataSetChanged();
        TransmitWinningCrunchiesBean.Me me = transmitWinningHomeListBean.data.me;
        mHead.setImageURI(Uri.parse(URLUtil.builderImgUrl(me.avatar, 144, 144)));
        mNumtext.setText(me.index + "");
        mName.setText(me.nickname);
        mIntegral.setText("贡献值：" + me.score);
        mGender.setImageResource(R.drawable.icon_famale);
        if (me.sex == 1) {
            mGender.setImageResource(R.drawable.icon_male);
        } else {
            mGender.setImageResource(R.drawable.icon_famale);
        }
        mTime.setText("当前统计截止：" + transmitWinningHomeListBean.data.time);
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
