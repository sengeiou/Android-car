package com.tgf.kcwc.businessconcerns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.globalchat.GlobalChatActivity;
import com.tgf.kcwc.mvp.model.BaseInfoModel;
import com.tgf.kcwc.mvp.model.BusinessRecordModel;
import com.tgf.kcwc.mvp.presenter.BaseInfoPresenter;
import com.tgf.kcwc.mvp.presenter.BusinessRecordPresenter;
import com.tgf.kcwc.mvp.view.BaseInfoView;
import com.tgf.kcwc.mvp.view.BusinessRecordView;
import com.tgf.kcwc.tripbook.FindFrag;
import com.tgf.kcwc.tripbook.RecomentFrag;
import com.tgf.kcwc.tripbook.SameCityFrag;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @anthor noti
 * @time 2017/8/1 10:38
 * 商务关注-客户详情
 */

public class CustomerDetailActivity extends BaseActivity implements BaseInfoView, BusinessRecordView {
    private TextView rightTv;
    private SimpleDraweeView headerImg;
    private TextView nameTv;

    private LinearLayout detailLl;
    private ImageView denderIv;
    private TextView ageTv;

    private ImageView modelTv;
    private ImageView masterTv;
    private ImageView phoneTv;
    private ImageView msgTv;

    private PagerSlidingTabStrip mTab;
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private BaseInfoModel list;

    private BusinessRecordPresenter mRecordPresenter;
    private BaseInfoPresenter mInfoPresenter;

    private int friendId;//好友id
    private String phoneStr;//电话号码
    private String nickName;

    @Override
    protected void setUpViews() {
        friendId = getIntent().getIntExtra(Constants.IntentParams.DATA, -1);
        rightTv = (TextView) findViewById(R.id.title_right_tv);
        headerImg = (SimpleDraweeView) findViewById(R.id.detail_header_img);
        nameTv = (TextView) findViewById(R.id.detail_nickname);
        denderIv = (ImageView) findViewById(R.id.detail_gender);
        detailLl = (LinearLayout) findViewById(R.id.detail_ll);
        ageTv = (TextView) findViewById(R.id.detail_age);
        modelTv = (ImageView) findViewById(R.id.detail_model);
        masterTv = (ImageView) findViewById(R.id.detail_master);
        phoneTv = (ImageView) findViewById(R.id.detail_phone);
        msgTv = (ImageView) findViewById(R.id.detail_msg);
        rightTv.setOnClickListener(this);
        phoneTv.setOnClickListener(this);
        msgTv.setOnClickListener(this);
        mTab = (PagerSlidingTabStrip) findViewById(R.id.detail_tabs);
        mPager = (ViewPager) findViewById(R.id.detail_pager);
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        fragments.add(new BaseInformationFrag(friendId));
        fragments.add(new BusinessRecordFrag(friendId));
        fragments.add(new ActionAnalysisFrag(friendId));
        fragments.add(new ActionRecordFrag(friendId));
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments,
                getResources().getStringArray(R.array.customer_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTab.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();

        mRecordPresenter = new BusinessRecordPresenter();
        mRecordPresenter.attachView(this);
        mRecordPresenter.getBusinessRecord(IOUtils.getToken(getContext()),friendId);

        mInfoPresenter = new BaseInfoPresenter();
        mInfoPresenter.attachView(this);
        mInfoPresenter.getBaseInfo(IOUtils.getToken(getContext()),friendId);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.title_right_tv:
                // TODO: 2017/8/3 编辑用户资料
                if (null != list){
                    Intent intent = new Intent(mContext,EditCustomerActivity.class);
                    intent.putExtra(Constants.IntentParams.DATA2, friendId);
                    intent.putExtra(Constants.IntentParams.DATA, list);
                    startActivity(intent);
                }
                break;
            case R.id.detail_phone:
                if (StringUtils.checkNull(phoneStr)) {
                    SystemInvoker.launchDailPage(mContext, phoneStr);
                }
                break;
            case R.id.detail_msg://私信界面
                if (StringUtils.checkNull(nickName)) {
                    Intent intent = new Intent(mContext, GlobalChatActivity.class);
                    intent.putExtra(Constant.EXTRA_USER_ID, friendId);
//                    intent.putExtra(Constant.EXTRA_USER_ID, "im_development_1415_kcwc");
                    intent.putExtra(Constant.EXTRA_USER_NICKNAME, nickName);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        mTab.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        // mTabs.setDividerColor(getResources().getColor(R.color.color_80cbc4));
        // 设置分割线的上下的间距,传入的是dp
        mTab.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        mTab.setUnderlineHeight(0);
        //设置Tab底部线的颜色
        mTab.setUnderlineColor(getResources().getColor(R.color.split_line_color));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTab.setIndicatorHeight(3);
        // 设置Tab Indicator的颜色
        mTab.setIndicatorColorResource(R.color.customer_detail_bottom);

        // 设置Tab标题文字的大小,传入的是dp
        mTab.setTextSize(16);
        // 设置选中Tab文字的颜色
        mTab.setSelectedTextColorResource(R.color.customer_detail_bottom);
        //设置正常Tab文字的颜色
        mTab.setTextColorResource(R.color.text_color15);
    }

    @Override
    public void showBaseInfo(BaseInfoModel list) {
        this.list = list;
    }

    @Override
    public void showAuth(ArrayList<BaseInfoModel.AuthItem> authItem) {

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
    public void showSeeCar(BusinessRecordModel.SeeItem seeItem) {

    }

    @Override
    public void showTicket(BusinessRecordModel.TicketItem ticketItem) {

    }

    @Override
    public void showCoupon(BusinessRecordModel.CouponItem couponItem) {

    }

    @Override
    public void showActivity(BusinessRecordModel.ActivityItem activityItem) {

    }

    @Override
    public void showInfo(BusinessRecordModel.BasicInfoItem basicInfoItem) {
        String avatarUrl = URLUtil.builderImgUrl(basicInfoItem.avatar, 144, 144);
        headerImg.setImageURI(avatarUrl);
        nickName = basicInfoItem.nickname;
        nameTv.setText(nickName);
        if (basicInfoItem.sex == 1) {//男
            denderIv.setImageResource(R.drawable.icon_male);
            detailLl.setBackgroundResource(R.drawable.shape_detail_age_bg);
        } else {//女
            denderIv.setImageResource(R.drawable.icon_women);
            detailLl.setBackgroundResource(R.drawable.shape_detail_age_bg);
        }
        //模特
        if (basicInfoItem.isModel == 1) {
            modelTv.setVisibility(View.VISIBLE);
        } else {
            modelTv.setVisibility(View.GONE);
        }
        //达人
        if (basicInfoItem.isDoyen == 1) {
            masterTv.setVisibility(View.VISIBLE);
        } else {
            masterTv.setVisibility(View.GONE);
        }
        ageTv.setText(basicInfoItem.age+"");
        phoneStr = basicInfoItem.tel;
//        msgTv
    }

    public static int getAge(Date birthDate) {
        if (birthDate == null)
            throw new
                    RuntimeException("出生日期不能为null");
        int age = 0;
        Date now = new Date();
        SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
        SimpleDateFormat format_M = new SimpleDateFormat("MM");
        String birth_year = format_y.format(birthDate);
        String this_year = format_y.format(now);
        String birth_month = format_M.format(birthDate);
        String this_month = format_M.format(now);
        // 初步，估算
        age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);
        // 如果未到出生月份，则age - 1
        if (this_month.compareTo(birth_month) < 0)
            age -= 1;
        if (age < 0)
            age = 0;
        return age;
    }
}
