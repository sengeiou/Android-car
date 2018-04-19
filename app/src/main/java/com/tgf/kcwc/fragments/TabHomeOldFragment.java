package com.tgf.kcwc.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.BannerMark;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.VoucherMainActivity;
import com.tgf.kcwc.driving.HeaderBannerWebActivity;
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.HomeModel;
import com.tgf.kcwc.mvp.presenter.HomePagePresenter;
import com.tgf.kcwc.mvp.view.HomePageView;
import com.tgf.kcwc.play.havefun.HaveFunActivity;
import com.tgf.kcwc.play.topic.TopicActivity;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.seecar.CommitCarOrderActivity;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.transmit.TransmitWinningHomeActivity;
import com.tgf.kcwc.tripbook.TripBookeListActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * scott
 * Date:16/12/9 11:16
 * E-mail:fishloveqin@gmail.com
 */
public class TabHomeOldFragment extends BaseFragment implements HomePageView, OnBannerClickListener {
    private final int REQUEST_CODE_SERCITY = 100;
    private static final String tag = "TabHomePage";
    private GridView menuGridView;
    private List<HomeMenuTool> menuList;
    private WrapAdapter menuAdapter;
    private MainActivity activity;
    private HomePagePresenter homePagePresenter;
    private List<BannerModel.Data> mBannerList;
    private Banner bannerView;
    private TextView mFilterTitle;
    private KPlayCarApp kPlayCarApp;
    private OpenShareWindow openShareWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_oldhome;
    }

    @Override
    protected void initData() {
        menuList = HomeMenuTool.getMenuList();
        iniMenu();
        homePagePresenter = new HomePagePresenter();
        homePagePresenter.attachView(this);
        homePagePresenter.getHomePageView(BannerMark.CAR_INDEX);
    }

    private void iniMenu() {
        menuAdapter = new WrapAdapter<HomeMenuTool>(getContext(), menuList,
                R.layout.griditem_home_menu) {
            @Override
            public void convert(ViewHolder helper, final HomeMenuTool item) {
                SimpleDraweeView menuIcon = helper.getView(R.id.icon);
                GenericDraweeHierarchy hierarchy = menuIcon.getHierarchy();
                hierarchy.setPlaceholderImage(item.getIconId());
                helper.setText(R.id.tv_title, item.getName());
                helper.getView(R.id.homemenu_item_root)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectToAct(item.getId());
                            }
                        });
            }
        };
        menuGridView.setAdapter(menuAdapter);
    }

    private void selectToAct(int id) {
        switch (id) {
            case 1: //展会看
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 1);
                activity.switchToTab(Constants.Navigation.SEE_TAB);
                break;
            case 2://店内看
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 2);
                activity.switchToTab(Constants.Navigation.SEE_TAB);
                break;
            case 3://看定制
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 0);
                activity.switchToTab(Constants.Navigation.SEE_TAB);
                break;
            case 4://找优惠
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 3);
                activity.switchToTab(Constants.Navigation.SEE_TAB);
                break;
            case 5://车主自售
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 4);
                activity.switchToTab(Constants.Navigation.SEE_TAB);
                break;
            case 6://车友
                activity.switchToTab(Constants.Navigation.PLAY_TAB_TAB);
                break;
            case 7://开车去
                // CommonUtils.startNewActivity(mContext, DrivingActivity.class);
                activity.skipPlay(Constants.PlayTabSkip.DRIVINGFRAGMENT);
                break;
            case 8://请你玩
                //CommonUtils.startNewActivity(mContext, PleasePlayActivity.class);
                activity.skipPlay(Constants.PlayTabSkip.PLEASEPLAYFRAGMENT);
                break;
            case 9://路书
//                activity.skipPlay(Constants.PlayTabSkip.TRIPBOOKELIST);
                CommonUtils.startNewActivity(mContext, TripBookeListActivity.class);
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
        }
    }

    @Override
    protected void initView() {
        menuGridView = findView(R.id.home_menugv);
        findView(R.id.home_iseeiv).setOnClickListener(this);
        findView(R.id.filterLayout).setOnClickListener(this);
        findView(R.id.fraghome_scanneriv).setOnClickListener(this);
        findView(R.id.seek).setOnClickListener(this);
        findView(R.id.home_shareiv).setOnClickListener(this);
        setUserVisibleHint(true);
        if (menuAdapter != null) {
            menuGridView.setAdapter(menuAdapter);

        }
        if (activity == null) {
            activity = (MainActivity) getActivity();
            kPlayCarApp = (KPlayCarApp) activity.getApplication();
        }

        bannerView = findView(R.id.homepage_banner);
        mFilterTitle = findView(R.id.filterTitle);
        mFilterTitle.setText(kPlayCarApp.locCityName);
        initBannerAdpater();
    }

    private WbShareHandler mWbHandler = null;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.home_iseeiv:
                if (IOUtils.isLogin(mContext))
                    mContext.startActivity(new Intent(mContext, CommitCarOrderActivity.class));
                break;
            case R.id.filterLayout:

                Intent intentCity = new Intent();
                intentCity.setClass(mContext, SelectCityActivity.class);
                getActivity().startActivityForResult(intentCity,
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
                openShareWindow = new OpenShareWindow(getActivity());
                openShareWindow.show(getActivity());
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
                                mWbHandler = OpenShareUtil.shareSina(getActivity(), title,
                                        description);
                                break;
                            case 3:
                                ArrayList<String> urls = new ArrayList<String>();
                                urls.add(cover);
                                OpenShareUtil.shareToQzone(kPlayCarApp.getTencent(), getActivity(),
                                        mBaseUIListener, urls, title, description);
                                break;
                            case 4:
                                OpenShareUtil.shareToQQ(kPlayCarApp.getTencent(), getActivity(),
                                        mBaseUIListener, title, cover, description);
                                break;

                        }
                    }
                });
                break;

            default:
                break;
        }
    }

    @Override
    public void showBannerView(List<BannerModel.Data> bannerList) {
        mBannerList = bannerList;
        initBannerAdpater();
    }

    @Override
    public void showHomeList(ArrayList<HomeModel.HomeModelItem> homeList) {

    }

    @Override
    public void showCommitOrderSuccess(int orderId) {

    }

    @Override
    public void showCommitOrderFailed(String statusMessage) {

    }

    @Override
    public void showFollowAddFailed(String msg, int position) {

    }


    @Override
    public void showFollowAddSuccess(int position) {

    }

    @Override
    public void showCancelSuccess(int position) {

    }

    @Override
    public void showCancelFailed(String msg) {

    }

    @Override
    public void showPraiseFailed(String statusMessage) {

    }

    @Override
    public void showPraiseSuccess(int position) {

    }

    @Override
    public void showShare(int position) {

    }

    @Override
    public void showDeletSucess(int position) {

    }

    private void initBannerAdpater() {
        if (mBannerList != null && mBannerList.size() != 0) {
            ArrayList<String> imgUrl = new ArrayList<>();
            for (int i = 0; i < mBannerList.size(); i++) {
                imgUrl.add(URLUtil.builderImgUrl(mBannerList.get(i).image, 540, 270));
            }

            bannerView.setImages(imgUrl).setImageLoader(new FrescoImageLoader())
                    .setOnBannerClickListener(this).start();
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void OnBannerClick(int position) {
        if (mBannerList.get(position - 1).type == 2) {
            if (!TextUtils.isEmpty(mBannerList.get(position - 1).url)) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mBannerList.get(position - 1).url);
                args.put(Constants.IntentParams.ID2, mBannerList.get(position - 1).title);
                CommonUtils.startNewActivity(activity, args, HeaderBannerWebActivity.class);
            }
        } else {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            String cityName = data.getStringExtra(Constants.IntentParams.DATA);
            kPlayCarApp.locCityName = cityName;
            mFilterTitle.setText(data.getStringExtra(Constants.IntentParams.DATA));
        }

    }

    public void onShare(int requestCode, int resultCode, Intent data, int type) {

        switch (type) {

            case Constants.InteractionCode.QQ_OR_ZONE_TYPE:

                if (Constants.InteractionCode.QQ_SHARE_REQUEST_CODE == requestCode) {
                    Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUIListener);
                } else if (Constants.InteractionCode.QQ_ZONE_SHARE_REQUEST_CODE == requestCode) {
                    Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUIListener);
                }
                break;

            case Constants.InteractionCode.WEB_BO_TYPE:
                if (mWbHandler != null) {
                    mWbHandler.doResultIntent(data, new SinaWBCallback(mContext));
                }

                break;
        }

    }
}
