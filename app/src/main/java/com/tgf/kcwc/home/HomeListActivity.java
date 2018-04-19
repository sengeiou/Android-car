package com.tgf.kcwc.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.HomeListApater;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.plussign.PopupMenuView;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.CouponDetailActivity;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.finddiscount.LimitDiscountNewDetailsActivity;
import com.tgf.kcwc.finddiscount.LimitDiscountNewDetailsOddsActivity;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.HomeModel;
import com.tgf.kcwc.mvp.model.IntentDataModel;
import com.tgf.kcwc.mvp.presenter.HomePagePresenter;
import com.tgf.kcwc.mvp.view.HomePageView;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.posting.character.DynamicDetailsActivity;
import com.tgf.kcwc.see.NewCarLaunchActivity;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.see.evaluate.PopmanESDetailActitivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.SmoothListView.SmoothListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/10/31 0031
 * E-mail:hekescott@qq.com
 */

public class HomeListActivity extends BaseActivity implements SmoothListView.ISmoothListViewListener, HomePageView, RadioGroup.OnCheckedChangeListener {
    private SmoothListView homeListview;
    private KPlayCarApp kPlayCarApp;
    private boolean isRefresh;
    private HomePagePresenter homePagePresenter;
    private String token;
    private String mType = "follow";
    private String mTypeHot = "hot";
    private String mTypeFollow = "follow";
    private String mTypeCity = "city";
    private boolean icChecked = false;
    private RadioGroup menuRG;
    private RadioButton fellowBtn, hotBtn, cityBtn;
    private ArrayList<HomeModel.HomeModelItem> mHomeList = new ArrayList<>();
    private HomeListApater mHomeAdapter;
    private Intent fromIntent;
    private OpenShareWindow openShareWindow;
    private View bottomLayout;
    private View footer;
    private View parenFotterView;

    @Override
    protected void setUpViews() {
    findViewById(R.id.home_scrollTopIv).setOnClickListener(this);
        kPlayCarApp = (KPlayCarApp) getApplication();
        token = IOUtils.getToken(getContext());
        mPageIndex = 1;
        mPageSize = 10;
        menuRG = findViewById(R.id.home_filterRG);
        menuRG.setOnCheckedChangeListener(this);
        fellowBtn = findViewById(R.id.home_filter_fellowBtn);
        hotBtn = findViewById(R.id.home_filter_hotBtn);
        cityBtn = findViewById(R.id.home_filter_cityBtn);
        bottomLayout = findViewById(R.id.triplist_bottll);
        findViewById(R.id.play_indicatorBg).setOnClickListener(this);
        findViewById(R.id.see_indicatorBg).setOnClickListener(this);
        findViewById(R.id.posted_indicatorBg).setOnClickListener(this);
        findViewById(R.id.me_indicatorBg).setOnClickListener(this);
        homePagePresenter = new HomePagePresenter();
        homePagePresenter.attachView(this);
        fromIntent = getIntent();
        mType = fromIntent.getStringExtra(Constants.IntentParams.INTENT_TYPE);
//        homePagePresenter.getHomeList(token, mType, kPlayCarApp.cityId, kPlayCarApp.getLongitude(), kPlayCarApp.getLattitude(), mPageIndex, mPageSize);
        mHomeAdapter = new HomeListApater(HomeListActivity.this, mHomeList, homePagePresenter, this);
        homeListview = findViewById(R.id.home_listview);
        homeListview.setRefreshEnable(true);
        homeListview.setLoadMoreEnable(false);
        homeListview.setSmoothListViewListener(this);
        homeListview.setLoadMoreEnable(true);
        homeListview.setAdapter(mHomeAdapter);
        setSelectedType();
        homeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((position -1) >= 0 && (position-1 ) < mHomeList.size()) {

                    gotoItem(mHomeList.get(position -1));
                }
            }
        });
//        View parenFotterView  = View.inflate(getContext(),R.layout.bottom_hint_layout,null);
        parenFotterView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_hint_layout, null);
//        footer = parenFotterView.findViewById(R.id.hint_footerlv);
//
//        footer.setVisibility(View.GONE);
    }
    private static final String KEY_HOME_SAY = "mood";
    private static final String WORDS = "words";
    private static final String EVALUATE = "evaluate";
    private static final String CYCLE = "cycle";
    private static final String PLAY = "play";
    private static final String SHOW = "show";
    private static final String GOODS = "goods";
    private static final String EVENT_GOODS = "event_goods";
    private static final String LIMIT_TIME = "limit_time";
    private static final String SALE = "sale";
    private static final String BRAND_BENEFIT = "brand_benefit";
    private static final String ROADBOOK = "roadbook";
    private static final String EVENT = "event";
    private static final String ORG_SHOW_CAR = "org_show_car";
    private static final String COUPON = "coupon";
    private static final String NEW_CAR = "new_car";
    private void gotoItem(HomeModel.HomeModelItem item) {
        Map<String, Serializable> args = new HashMap<String, Serializable>();
        switch (item.model) {
            case KEY_HOME_SAY://说说
                args.put(Constants.IntentParams.ID, item.id);
                CommonUtils.startNewActivity(mContext, args, DynamicDetailsActivity.class);
                break;
            case WORDS:
                args.put(Constants.IntentParams.ID, item.id + "");
                CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                break;
            case EVALUATE:
                args.put(Constants.IntentParams.ID, item.id);
                CommonUtils.startNewActivity(mContext, args, PopmanESDetailActitivity.class);
                break;
            case CYCLE://开车去
                args.put("id", item.id + "");
                CommonUtils.startNewActivity(mContext, args, DrivingDetailsActivity.class);
                break;
            case PLAY://请你玩
                args.put("id", item.id + "");
                CommonUtils.startNewActivity(mContext, args, PleasePlayDetailsActivity.class);
                break;
//                 case SHOW ://光影秀
//                     break;
            case GOODS://车主自售
                args.put(Constants.IntentParams.ID, item.id);
                CommonUtils.startNewActivity(mContext, args, SaleDetailActivity.class);
                break;
            case EVENT_GOODS://展会现场车主自售
                args.put(Constants.IntentParams.ID, item.id);
                CommonUtils.startNewActivity(mContext, args, SaleDetailActivity.class);
                break;
            case LIMIT_TIME://【limit_time】限时特惠，怕你错过
                args.put(Constants.IntentParams.ID, item.id + "");
                CommonUtils.startNewActivity(mContext, args, LimitDiscountNewDetailsActivity.class);
//                CommonUtils.startNewActivity(mContext, args, LimitDiscountNewDetailsOddsActivity.class);
                break;
            case SALE://限量特价车，先到先得
                args.put(Constants.IntentParams.ID, item.id + "");
                CommonUtils.startNewActivity(mContext, args, LimitDiscountNewDetailsOddsActivity.class);
                break;
            case BRAND_BENEFIT://【brand_benefit】品牌钜惠，特价大促
                args.put(Constants.IntentParams.ID, item.id + "");
                CommonUtils.startNewActivity(mContext, args, LimitDiscountNewDetailsOddsActivity.class);
                break;
            case ROADBOOK:
                args.put(Constants.IntentParams.ID, item.modelId);
                CommonUtils.startNewActivity(mContext, args, TripbookDetailActivity.class);
                break;
            case EVENT://【event】最近有车展
                SharedPreferenceUtil.putExhibitId(mContext, item.modelId);
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, Constants.IntentParams.SEE_EXHIBIT);

//                Intent playIntent = new Intent();
//                playIntent.putExtra(Constants.IntentParams.INDEX, 1);
//                playIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                playIntent.setClass(mContext, MainActivity.class);
//                mContext.startActivity(playIntent);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                break;
            case ORG_SHOW_CAR://【org_show_car】店内展车
                Intent toOrgdetail = new Intent(getContext(), DealerHomeActivity.class);
                toOrgdetail.putExtra(Constants.IntentParams.ID, item.id + "");
                toOrgdetail.putExtra(Constants.IntentParams.TITLE, item.title);
                toOrgdetail.putExtra(Constants.IntentParams.INDEX, 0);
                startActivity(toOrgdetail);
                break;
            case COUPON:
                args.put(Constants.IntentParams.ID, item.id);
                CommonUtils.startNewActivity(mContext, args, CouponDetailActivity.class);
                break;
            case NEW_CAR:
                args.put(Constants.IntentParams.ID, item.id);
                CommonUtils.startNewActivity(mContext, args, NewCarLaunchActivity.class);
                break;
            default:
                break;
        }
    }

    private void setSelectedType() {
        switch (mType) {
            case "hot":
                hotBtn.setChecked(true);
                mType = mTypeHot;
                onRefresh();
                break;
            case "follow":
                fellowBtn.setChecked(true);
                mType = mTypeFollow;
                onRefresh();
                break;
            case "city":
                cityBtn.setChecked(true);
                mType = mTypeCity;
                onRefresh();
                break;
            default:
                break;
        }

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
             switch (view.getId()){
                         case R.id.home_scrollTopIv:
                             backSendData();
                             break;
                 case R.id.play_indicatorBg:
//                     Intent intent = new Intent();
//                     intent.putExtra(Constants.IntentParams.INDEX, 3);
//                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                     intent.setClass(mContext, MainActivity.class);
//                     startActivity(intent);
                     CommonUtils.gotoMainPage(mContext,Constants.Navigation.PLAY_INDEX);
                     break;
                 case R.id.see_indicatorBg:
//                     Intent intentSee = new Intent();
//                     intentSee.putExtra(Constants.IntentParams.INDEX, 1);
//                     intentSee.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                     intentSee.setClass(mContext, MainActivity.class);
//                     startActivity(intentSee);
                     CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                     break;
                 case R.id.posted_indicatorBg:
                     PopupMenuView.getInstance()._show(mContext, bottomLayout);
                     break;
                 case R.id.me_indicatorBg:
                     if (IOUtils.isLogin(getContext())) {
//                         Intent intentMe = new Intent();
//                         intentMe.putExtra(Constants.IntentParams.INDEX, 4);
//                         intentMe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                         intentMe.setClass(mContext, MainActivity.class);
//                         startActivity(intentMe);
                         CommonUtils.gotoMainPage(mContext,Constants.Navigation.ME_INDEX);
                     }
                     break;
                         default:
                             break;
                     }
    }

    @Override
    protected void onResume() {
        super.onResume();
        token = IOUtils.getToken(getContext());
        if (mHomeAdapter != null) {
            mHomeAdapter.token = token;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        setContentView(R.layout.activity_homelist);
    }

    @Override
    public synchronized void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (icChecked == true) {
            return;
        }
        icChecked = true;
        switch (checkedId) {
            case R.id.home_filter_fellowBtn:
                fellowBtn.setChecked(true);
                mType = mTypeFollow;
                onRefresh();

                break;
            case R.id.home_filter_hotBtn:
                hotBtn.setChecked(true);
                mType = mTypeHot;
                onRefresh();
                break;
            case R.id.home_filter_cityBtn:
                cityBtn.setChecked(true);
                mType = mTypeCity;
                onRefresh();
                break;
            default:
                break;
        }
        homeListview.smoothScrollToPosition(0);
        if(isAddFooter){
            homeListview.removeFooterView(parenFotterView);
            isAddFooter =false;
        }
        icChecked = false;
    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        isRefresh = true;
        homePagePresenter.getHomeList(token, mType, kPlayCarApp.cityId, kPlayCarApp.getLongitude(), kPlayCarApp.getLattitude(), mPageIndex, mPageSize);
    }

    @Override
    public void onLoadMore() {
        mPageIndex++;
        homePagePresenter.getHomeList(token, mType, kPlayCarApp.cityId, kPlayCarApp.getLongitude(), kPlayCarApp.getLattitude(), mPageIndex, mPageSize);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
//        showLoadingIndicator(active);
        if (!active) {
            homeListview.stopLoadMore();
            homeListview.stopRefresh();
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showBannerView(List<BannerModel.Data> data) {

    }
boolean isAddFooter =false;
    @Override
    public void showHomeList(ArrayList<HomeModel.HomeModelItem> homeList) {
        if (isRefresh) {
            mHomeList.clear();
            isRefresh = false;
            homeListview.setLoadMoreEnable(true);
        }
        mHomeList.addAll(homeList);
        mHomeAdapter.notifyDataSetChanged();
        if (homeList == null || homeList.size() == 0) {
            homeListview.setLoadMoreEnable(false);
            if(!isAddFooter){
                homeListview.addFooterView(parenFotterView);
                isAddFooter = true;
            }
        }
    }

    @Override
    public void showCommitOrderSuccess(int orderId) {

    }

    @Override
    public void showCommitOrderFailed(String statusMessage) {

    }

    @Override
    public void showFollowAddFailed(String msg, int position) {
        if(msg.equals("不能重复关注")){
            int craeteUserId =  mHomeList.get(position).createUser.id;
            for(int i=0;i<mHomeList.size();i++){
                if(craeteUserId == mHomeList.get(i).createUser.id){
                    mHomeList.get(i).createUser.isFollow = 1;
                }
            }
            mHomeAdapter.notifyDataSetChanged();
            CommonUtils.showToast(mContext, "该用户已经关注");
        }else   if(msg.equals("不存在关注关系")){
            int craeteUserId =  mHomeList.get(position).createUser.id;
            for(int i=0;i<mHomeList.size();i++){
                if(craeteUserId == mHomeList.get(i).createUser.id){
                    mHomeList.get(i).createUser.isFollow = 0;
                }
            }
            mHomeAdapter.notifyDataSetChanged();
            CommonUtils.showToast(mContext, "该用户已取消关注");
        }else {
            CommonUtils.showToast(mContext, msg);
        }
    }

    @Override
    public void showFollowAddSuccess(int position) {
        int craeteUserId =  mHomeList.get(position).createUser.id;
        for(int i=0;i<mHomeList.size();i++){
            if(craeteUserId == mHomeList.get(i).createUser.id){
                mHomeList.get(i).createUser.isFollow = 1;
            }
        }
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCancelSuccess(int position) {
        int craeteUserId =  mHomeList.get(position).createUser.id;
        for(int i=0;i<mHomeList.size();i++){
            if(craeteUserId == mHomeList.get(i).createUser.id){
                mHomeList.get(i).createUser.isFollow = 0;
            }
        }
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCancelFailed(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void showPraiseFailed(String statusMessage) {
        CommonUtils.showToast(mContext, statusMessage);
    }

    @Override
    public void showPraiseSuccess(int position) {
        if (mHomeList.get(position).isPraise == 1) {
            mHomeList.get(position).isPraise = 0;
            mHomeList.get(position).diggCount--;
        } else {
            mHomeList.get(position).isPraise = 1;
            mHomeList.get(position).diggCount++;
        }
        mHomeAdapter.notifyDataSetChanged();
    }
    @Override
    public void showShare(int position) {
        if (openShareWindow == null) {
            openShareWindow = new OpenShareWindow(HomeListActivity.this);
        }
        openShareWindow.show(HomeListActivity.this);
        final String title = "看车玩车";
        final String description = "欢迎关注看车玩车";
        final String cover = Constants.ImageUrls.IMG_DEF_URL;
        openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                switch (position) {
                    case 0:
                        OpenShareUtil.sendWXMoments(mContext, kPlayCarApp.getMsgApi(),
                                title, description);
                        break;
                    case 1:
                        OpenShareUtil.sendWX(mContext, kPlayCarApp.getMsgApi(), title,
                                description);
                        break;
                    case 2:
                         OpenShareUtil.shareSina(HomeListActivity.this, title,
                                description);
                        break;
                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(kPlayCarApp.getTencent(), HomeListActivity.this,
                                mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(kPlayCarApp.getTencent(), HomeListActivity.this,
                                mBaseUIListener, title, cover, description);
                        break;

                }
            }
        });
    }

    @Override
    public void showDeletSucess(int position) {
        mHomeList.remove(position);
        mHomeAdapter.notifyDataSetChanged();
    }

    private String TAG = "HomeListActivity";

    @Override
    public void onBackPressed() {
        backSendData();
        super.onBackPressed();
    }

    private void backSendData() {
        IntentDataModel intentDataModel = new IntentDataModel();
        intentDataModel.fromName = TAG;
        intentDataModel.type = mType;
        RxBus.$().post("TabHomeFragment", intentDataModel);
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homePagePresenter.detachView();
    }
}
