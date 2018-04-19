package com.tgf.kcwc.coupon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.message.MessageActivity;
import com.tgf.kcwc.me.setting.FanKuiActivity;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.ScanOffDetailModel;
import com.tgf.kcwc.mvp.presenter.ScanOffCouponDetailPresenter;
import com.tgf.kcwc.mvp.view.ScanOffCouponDetailView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/5/24 0024
 * E-mail:hekescott@qq.com
 */

public class ScanOffCouponDetailActivity extends BaseActivity implements ScanOffCouponDetailView {

    private SimpleDraweeView coverIv;
    private TextView titleTv;
    private ListView scanoffLv;
    private Intent fromIntetn;
    private String couponId;
    private ScanOffCouponDetailPresenter scanOffCouponDetailPresenter;
    private MorePopupWindow.MoreOnClickListener moreOnClickListener = new MorePopupWindow.MoreOnClickListener() {
        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean item) {

            String title = item.title;
            switch (title) {
                case "首页":
//                    Intent intent = new Intent();
//                    intent.putExtra(Constants.IntentParams.INDEX, 0);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.setClass(mContext, MainActivity.class);
//                    startActivity(intent);
                    CommonUtils.gotoMainPage(mContext,Constants.Navigation.HOME_INDEX);
                    break;
                case "消息":
                    CommonUtils.startNewActivity(mContext, MessageActivity.class);
                    break;
                case "反馈":
                    CommonUtils.startNewActivity(mContext, FanKuiActivity.class);
                    break;
            }
        }
    };
    private MorePopupWindow morePopupWindow = null;
    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
        text.setText("核销明细");
        function.setImageResource(R.drawable.global_nav_n);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (morePopupWindow != null) {
                    morePopupWindow.showPopupWindow(function);
                }

            }
        });
    }

    @Override
    protected void setUpViews() {
        fromIntetn = getIntent();
        couponId = fromIntetn.getStringExtra(Constants.IntentParams.ID);
        coverIv = (SimpleDraweeView) findViewById(R.id.scanoffdetail_cover);
        titleTv = (TextView) findViewById(R.id.scanoffdetail_title);
        scanoffLv = (ListView) findViewById(R.id.scanoffdetail_lv);
        titleTv.setText(fromIntetn.getStringExtra(Constants.IntentParams.TITLE));
        String coverUrl = URLUtil.builderImgUrl(fromIntetn.getStringExtra(Constants.IntentParams.DATA), 270, 203);
        coverIv.setImageURI(Uri.parse(coverUrl));
        scanOffCouponDetailPresenter = new ScanOffCouponDetailPresenter();
        scanOffCouponDetailPresenter.attachView(this);
        scanOffCouponDetailPresenter.getScanOffDetail(IOUtils.getToken(getContext()), couponId);
        initHideMoreMenuData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanoffcoupon_detail);
    }
  private   List<MorePopupwindowBean> dataList = new ArrayList<>();
    private void initHideMoreMenuData() {
        if (dataList.size() == 0) {
            String[] navValues = new String[]{"首页","消息","反馈"};
            Map<String, Integer> titleActionIconMap = DataUtil.getTitleActionIcon();
            int length = navValues.length;
            for (int i = 0; i < length; i++) {
                MorePopupwindowBean morePopupwindowBean = null;
                morePopupwindowBean = new MorePopupwindowBean();
                morePopupwindowBean.title = navValues[i];
                morePopupwindowBean.icon = titleActionIconMap.get(navValues[i]);
                morePopupwindowBean.id = i;
                dataList.add(morePopupwindowBean);
            }

            morePopupWindow = new MorePopupWindow(this, dataList, moreOnClickListener);
        }
    }
    @Override
    public void showScanOffList(ArrayList<ScanOffDetailModel.ScanOffDetailItem> scanOffDetailList) {

        scanoffLv.setAdapter(new WrapAdapter<ScanOffDetailModel.ScanOffDetailItem>(getContext(), scanOffDetailList, R.layout.listitem_scanoffcoupon_detail) {
            @Override
            public void convert(ViewHolder helper, ScanOffDetailModel.ScanOffDetailItem item) {

                String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                helper.setSimpleDraweeViewURL(R.id.avatar_iv, avatarUrl);
                ImageView genderIv = helper.getView(R.id.avatar_gender);
                helper.setText(R.id.scanoffdetail_nickname, item.nickname);
                helper.setText(R.id.scanoff_checktime, item.checkTime);
                if (item.sex == 1) {
                    genderIv.setImageResource(R.drawable.icon_men);
                } else {
                    genderIv.setImageResource(R.drawable.icon_women);
                }
            }
        });

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
    protected void onDestroy() {
        super.onDestroy();
        scanOffCouponDetailPresenter.detachView();
    }
}
