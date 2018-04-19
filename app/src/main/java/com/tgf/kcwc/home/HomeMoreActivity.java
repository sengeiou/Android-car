package com.tgf.kcwc.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.plussign.PopupMenuView;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.VoucherMainActivity;
import com.tgf.kcwc.driving.HeaderBannerWebActivity;
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.driving.please.PleasePlayActivity;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.fragments.HomeMenuTool;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.view.HomePageMoreView;
import com.tgf.kcwc.play.havefun.HaveFunActivity;
import com.tgf.kcwc.play.topic.TopicActivity;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.see.evaluate.PopmanESActivity;
import com.tgf.kcwc.seecar.CommitCarOrderActivity;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.transmit.TransmitWinningHomeActivity;
import com.tgf.kcwc.tripbook.TripBookeListActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.LineGridView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/9/8 0008
 * E-mail:hekescott@qq.com
 */

public class HomeMoreActivity extends BaseActivity implements HomePageMoreView, OnBannerClickListener {

    //    private HomePageMorePresenter homePagePresenter;
    private KPlayCarApp kPlayCarApp;
    //    private Banner bannerView;
    private TextView mFilterTitle;
    private OpenShareWindow openShareWindow;
    private WbShareHandler mWbHandler;
    private LineGridView mSeecarGV;
    private LineGridView mPlaycarGV;
    private LineGridView mFuliGV;
    private ArrayList<HomeMenuTool> mSeecarMenuList;
    private ArrayList<HomeMenuTool> mFuliMenuList;
    private ArrayList<HomeMenuTool> mPlaycarMenuList;
    private View bottomLayout;

    @Override
    protected void setUpViews() {
        kPlayCarApp = (KPlayCarApp) getApplication();
//        bannerView = (Banner) findViewById(R.id.homepage_banner);
        mFilterTitle = (TextView) findViewById(R.id.filterTitle);
        findViewById(R.id.filterLayout).setOnClickListener(this);
        findViewById(R.id.fraghome_scanneriv).setOnClickListener(this);
        findViewById(R.id.seek).setOnClickListener(this);
        findViewById(R.id.home_shareiv).setOnClickListener(this);
        bottomLayout = findViewById(R.id.triplist_bottll);
        findViewById(R.id.play_indicatorBg).setOnClickListener(this);
        findViewById(R.id.see_indicatorBg).setOnClickListener(this);
        findViewById(R.id.posted_indicatorBg).setOnClickListener(this);
        findViewById(R.id.me_indicatorBg).setOnClickListener(this);
        mSeecarGV = (LineGridView) findViewById(R.id.showSeecarMenuGv);
        mPlaycarGV = (LineGridView) findViewById(R.id.showPlaycarMenuGv);
        mFuliGV = (LineGridView) findViewById(R.id.showFuliMenuGv);
        mFilterTitle.setText(kPlayCarApp.locCityName);
//        homePagePresenter = new HomePageMorePresenter();
//        homePagePresenter.attachView(this);
//        homePagePresenter.getHomePageView(BannerMark.CAR_INDEX);
//        initBannerAdpater();
        initSeeCarMenu();
        initPlayCarMenu();
        initFuliMenu();
    }

    private void initSeeCarMenu() {
        mSeecarMenuList = HomeMenuTool.getSeecarMenuList();
        mSeecarGV.setAdapter(new WrapAdapter<HomeMenuTool>(getContext(), R.layout.gridview_item_showmenu, mSeecarMenuList) {
            @Override
            public void convert(ViewHolder helper, final HomeMenuTool item) {

                TextView titletv = helper.getView(R.id.gridview_item_menutv);
                titletv.setText(item.getName());
                ImageView iv = helper.getView(R.id.gridview_item_menuiv);
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, item.getIconId()));
                helper.getView(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setMenuOnclick(item.getId());
                    }
                });
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(mSeecarGV, 4);
    }

    private void setMenuOnclick(int id) {
        selectToAct(id);
    }

    private void selectToAct(int id) {
        Logger.d("selectToAct ==="+id);
        switch (id) {
            case 1: //展会看
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 1);
//                Intent intentExhibit = new Intent();
//                intentExhibit.putExtra(Constants.IntentParams.INDEX, 1);
//                intentExhibit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intentExhibit.setClass(mContext, MainActivity.class);
//                startActivity(intentExhibit);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                break;
            case 2://店内看
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 2);
//                Intent intentStore = new Intent();
//                intentStore.putExtra(Constants.IntentParams.INDEX, 1);
//                intentStore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intentStore.setClass(mContext, MainActivity.class);
//                startActivity(intentStore);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                break;
            case 3://选车
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 0);
//                Intent intentSelectCar = new Intent();
//                intentSelectCar.putExtra(Constants.IntentParams.INDEX, 1);
//                intentSelectCar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intentSelectCar.setClass(mContext, MainActivity.class);
//                startActivity(intentSelectCar);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                break;
            case 4://找优惠
                KPlayCarApp.putValue(Constants.IntentParams.INDEX,Constants.IntentParams.SALE_DISCOUNTS);
//                Intent intentDiscount = new Intent();
//                intentDiscount.putExtra(Constants.IntentParams.INDEX, 1);
//                intentDiscount.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intentDiscount.setClass(mContext, MainActivity.class);
//                startActivity(intentDiscount);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                break;
            case 5://车主自售
                Intent saleIntent = new Intent();
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, Constants.IntentParams.SALE_INDEX);
//                saleIntent.putExtra(Constants.IntentParams.INDEX, 1);
//                saleIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                saleIntent.setClass(mContext, MainActivity.class);
//                startActivity(saleIntent);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                break;
            case 6://车友
//                Intent playIntent = new Intent();
//                playIntent.putExtra(Constants.IntentParams.INDEX, 3);
//                playIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                playIntent.setClass(mContext, MainActivity.class);
//                startActivity(playIntent);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.PLAY_INDEX);
                break;
            case 7://开车去
                RxBus.$().post(Constants.IntentParams.SELECT_DRIVING, Constants.PlayTabSkip.DRIVINGFRAGMENT);
                finish();
                // CommonUtils.startNewActivity(mContext, DrivingActivity.class);
//                activity.skipPlay(Constants.PlayTabSkip.DRIVINGFRAGMENT);
                break;
            case 8://请你玩
                RxBus.$().post(Constants.IntentParams.SELECT_DRIVING, Constants.PlayTabSkip.PLEASEPLAYFRAGMENT);
                finish();
//                activity.skipPlay(Constants.PlayTabSkip.PLEASEPLAYFRAGMENT);
                break;
            case 9://路书
//                activity.skipPlay(Constants.PlayTabSkip.TRIPBOOKELIST);
                RxBus.$().post(Constants.IntentParams.SELECT_DRIVING, Constants.PlayTabSkip.TRIPBOOKELIST);
                finish();
//                CommonUtils.startNewActivity(mContext, TripBookeListActivity.class);
                break;
            case 10://玩的爽
                CommonUtils.startNewActivity(mContext, HaveFunActivity.class);
                break;
            case 11://代金券
                CommonUtils.startNewActivity(mContext, VoucherMainActivity.class);
                break;
            case 12://转发有奖
                CommonUtils.startNewActivity(mContext, TransmitWinningHomeActivity.class);
                break;
            case 13://话题
                CommonUtils.startNewActivity(mContext, TopicActivity.class);
                //                CommonUtils.showToast(getContext(), "话题");
                break;
            case 14://驾途
                CommonUtils.startNewActivity(mContext, DrivingHomeActivity.class);
                break;
            case 15:
                CommonUtils.showToast(getContext(), "新车快报");
                break;
            case 16://评测
                CommonUtils.startNewActivity(mContext, PopmanESActivity.class);
                break;
            case 17://我要看
                CommonUtils.startNewActivity(mContext, CommitCarOrderActivity.class);
                break;

        }
    }

    private void initPlayCarMenu() {
        mPlaycarMenuList = HomeMenuTool.getPlaycarMenuList();
        mPlaycarGV.setAdapter(new WrapAdapter<HomeMenuTool>(getContext(), R.layout.gridview_item_showmenu, mPlaycarMenuList) {
            @Override
            public void convert(ViewHolder helper, final HomeMenuTool item) {

                TextView titletv = helper.getView(R.id.gridview_item_menutv);
                titletv.setText(item.getName());
                ImageView iv = helper.getView(R.id.gridview_item_menuiv);
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, item.getIconId()));
                helper.getView(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setMenuOnclick(item.getId());
                    }
                });
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(mPlaycarGV, 4);
    }

    private void initFuliMenu() {
        mFuliMenuList = HomeMenuTool.getFuliList();
        mFuliGV.setAdapter(new WrapAdapter<HomeMenuTool>(getContext(), R.layout.gridview_item_showmenu, mFuliMenuList) {
            @Override
            public void convert(ViewHolder helper, final HomeMenuTool item) {

                TextView titletv = helper.getView(R.id.gridview_item_menutv);
                titletv.setText(item.getName());
                ImageView iv = helper.getView(R.id.gridview_item_menuiv);
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, item.getIconId()));
                helper.getView(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setMenuOnclick(item.getId());
                    }
                });
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(mFuliGV, 4);
    }


    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("全部应用");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homemore);
    }

    private List<BannerModel.Data> mBannerList;

    @Override
    public void showBannerView(List<BannerModel.Data> bannerList) {
        mBannerList = bannerList;
        initBannerAdpater();
    }


    private void initBannerAdpater() {
        if (mBannerList != null && mBannerList.size() != 0) {
            ArrayList<String> imgUrl = new ArrayList<>();
            for (int i = 0; i < mBannerList.size(); i++) {
                imgUrl.add(URLUtil.builderImgUrl(mBannerList.get(i).image, 540, 270));
            }
//            bannerView.setImages(imgUrl).setImageLoader(new FrescoImageLoader())
//                    .setOnBannerClickListener(this).start();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.filterLayout:
                Intent intentCity = new Intent();
                intentCity.setClass(mContext, SelectCityActivity.class);
                startActivityForResult(intentCity,
                        Constants.InteractionCode.REQUEST_CODE_HOME);
                break;
            case R.id.fraghome_scanneriv:
                if (IOUtils.isLogin(mContext)) {
                    startActivity(new Intent(mContext, ScannerCodeActivity.class));
                }
                break;
            case R.id.seek: //搜索
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
                break;
            case R.id.home_shareiv: //分享
                openShareWindow = new OpenShareWindow(HomeMoreActivity.this);
                openShareWindow.show(HomeMoreActivity.this);
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

                                mWbHandler = OpenShareUtil.shareSina(HomeMoreActivity.this, title,
                                        description);
                                break;
                            case 3:
                                ArrayList<String> urls = new ArrayList<String>();
                                urls.add(cover);
                                OpenShareUtil.shareToQzone(kPlayCarApp.getTencent(), HomeMoreActivity.this,
                                        mBaseUIListener, urls, title, description);
                                break;
                            case 4:
                                OpenShareUtil.shareToQQ(kPlayCarApp.getTencent(), HomeMoreActivity.this,
                                        mBaseUIListener, title, cover, description);
                                break;

                        }
                    }
                });
                break;

            case R.id.play_indicatorBg:
//                Intent intent = new Intent();
//                intent.putExtra(Constants.IntentParams.INDEX, 3);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.setClass(mContext, MainActivity.class);
//                startActivity(intent);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.PLAY_INDEX);
                break;
            case R.id.see_indicatorBg:
//                Intent intentSee = new Intent();
//                intentSee.putExtra(Constants.IntentParams.INDEX, 1);
//                intentSee.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intentSee.setClass(mContext, MainActivity.class);
//                startActivity(intentSee);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                break;
            case R.id.posted_indicatorBg:
                PopupMenuView.getInstance()._show(mContext, bottomLayout);
                break;
            case R.id.me_indicatorBg:
                if (IOUtils.isLogin(getContext())) {
//                    Intent intentMe = new Intent();
//                    intentMe.putExtra(Constants.IntentParams.INDEX, 4);
//                    intentMe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intentMe.setClass(mContext, MainActivity.class);
//                    startActivity(intentMe);
                    CommonUtils.gotoMainPage(mContext,Constants.Navigation.ME_INDEX);
                }
                break;
            default:
                break;
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
        return this;
    }

    @Override
    public void OnBannerClick(int position) {
        if (mBannerList.get(position - 1).type == 2) {
            if (!TextUtils.isEmpty(mBannerList.get(position - 1).url)) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mBannerList.get(position - 1).url);
                args.put(Constants.IntentParams.ID2, mBannerList.get(position - 1).title);
                CommonUtils.startNewActivity(getContext(), args, HeaderBannerWebActivity.class);
            }
        } else {

        }
    }
}
