package com.tgf.kcwc.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.HomeApater;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.app.SelectBrandActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.BannerMark;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.CouponDetailActivity;
import com.tgf.kcwc.coupon.VoucherMainActivity;
import com.tgf.kcwc.driving.HeaderBannerWebActivity;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.finddiscount.LimitDiscountNewDetailsActivity;
import com.tgf.kcwc.finddiscount.LimitDiscountNewDetailsOddsActivity;
import com.tgf.kcwc.home.HomeListActivity;
import com.tgf.kcwc.home.HomeMoreActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.message.MessageActivity;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.HomeModel;
import com.tgf.kcwc.mvp.model.IntentDataModel;
import com.tgf.kcwc.mvp.presenter.HomePagePresenter;
import com.tgf.kcwc.mvp.view.HomePageView;
import com.tgf.kcwc.play.havefun.HaveFunActivity;
import com.tgf.kcwc.play.topic.TopicActivity;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.posting.character.DynamicDetailsActivity;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.see.NewCarLaunchActivity;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.see.evaluate.PopmanESDetailActitivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.seecar.CommitCarOrderActivity;
import com.tgf.kcwc.seecar.PlacePoint;
import com.tgf.kcwc.seecar.WaittingPriceActivity;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.transmit.TransmitWinningHomeActivity;
import com.tgf.kcwc.tripbook.TripBookeListActivity;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.SmoothListView.SmoothListView;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;

/**
 * scott
 * Date:16/12/9 11:16
 * E-mail:fishloveqin@gmail.com
 */
public class TabHomeFragment extends BaseFragment implements HomePageView, OnBannerClickListener, SmoothListView.ISmoothListViewListener, RadioGroup.OnCheckedChangeListener {
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
    private SmoothListView homeListview;
    View headView;
    private RadioGroup menuRG;
    private RadioButton fellowBtn, hotBtn, cityBtn;
    private RadioGroup headFilterRG;
    private RadioButton headFellowBtn, headHotBtn, headCityBtn;
    private boolean icChecked = false;
    private View headFilterView;
    private boolean isScrollIdle;
    private float headMargin;
    private String mType = "follow";
    private String mTypeHot = "hot";
    private String mTypeFollow = "follow";
    private String mTypeCity = "city";
    private int mPageIndex = 1;
    private int mPageSize = 10;
    private ArrayList<HomeModel.HomeModelItem> mHomeList = new ArrayList<>();
    private HomeApater mHomeAdapter;
    private boolean isRefresh;
    private String token;
    //    private TextView selectCarTv;
    private CarBean selectCarBean;
    private View commitBtn;
    //    private EditText phoneNumEd;
    private ImageView scrollTopBtn;
    private RelativeLayout homeFilterRootLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void updateData() {
        token = IOUtils.getToken(mContext);
        if (mHomeAdapter != null) {
            mHomeAdapter.token = token;
        }
    }

    private final String TAG = "TabHomeFragment";

    @Override
    protected int getLayoutId() {


        return R.layout.fragment_home;

    }

    @Override
    protected void initData() {
    }

    private void initMenu() {
        menuList = HomeMenuTool.getMenuList();
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
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, Constants.IntentParams.SALE_DISCOUNTS);
                activity.switchToTab(Constants.Navigation.SEE_TAB);
                break;
            case 5://车主自售
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, Constants.IntentParams.SALE_INDEX);
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
                activity.skipPlay(Constants.PlayTabSkip.TRIPBOOKELIST);
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
            case 99:
                CommonUtils.startNewActivity(mContext, HomeMoreActivity.class);
                break;
        }
    }

    public boolean isGoHomelist = true;

    @Override
    protected void initView() {
        if (activity == null) {
            activity = (MainActivity) getActivity();
            kPlayCarApp = (KPlayCarApp) activity.getApplication();
        }
        RxBus.$().register(TAG).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                IntentDataModel typeData = (IntentDataModel) o;
                if (typeData.fromName.equals("HomeListActivity")) {
                    isGoHomelist = false;
                    homeListview.smoothScrollToPosition(0);
                    mType = typeData.type;
                    setSelectType();
                }
            }
        });
        token = IOUtils.getToken(getActivity());
        homePagePresenter = new HomePagePresenter();
        homePagePresenter.attachView(this);
        homePagePresenter.getHomePageView(BannerMark.CAR_INDEX);
        homePagePresenter.getHomeList(token, mType, kPlayCarApp.cityId, kPlayCarApp.getLongitude(), kPlayCarApp.getLattitude(), mPageIndex, mPageSize);
        findView(R.id.filterLayout).setOnClickListener(this);
        findView(R.id.fraghome_scanneriv).setOnClickListener(this);
        findView(R.id.seek).setOnClickListener(this);
        findView(R.id.home_shareiv).setOnClickListener(this);
        mFilterTitle = findView(R.id.filterTitle);
        mFilterTitle.setText(kPlayCarApp.locCityName);
        homeListview = findView(R.id.home_listview);
        homeListview.setRefreshEnable(true);
        homeListview.setLoadMoreEnable(false);
        homeListview.setSmoothListViewListener(this);
        homeListview.setLoadMoreEnable(true);
        initHeadView();
        menuRG = findView(R.id.home_filterRG);
        homeFilterRootLayout = findView(R.id.home_filterRootLayout);
        scrollTopBtn = findView(R.id.home_scrollTopIv);
        scrollTopBtn.setOnClickListener(this);
        menuRG.setOnCheckedChangeListener(this);
        fellowBtn = findView(R.id.home_filter_fellowBtn);
        hotBtn = findView(R.id.home_filter_hotBtn);
        cityBtn = findView(R.id.home_filter_cityBtn);
        fellowBtn.performClick();
        initMenu();
    }

    //    private final String RXBUS_COMMITORDER = "commit_order";
    private String brandName;

    private void initHeadView() {
        headView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_banner, homeListview, false);
        headFilterView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_filter, homeListview, false);
        homeListview.addHeaderView(headView);
        homeListview.addHeaderView(headFilterView);
        mHomeAdapter = new HomeApater(activity, mHomeList, homePagePresenter, TabHomeFragment.this);
        homeListview.setAdapter(mHomeAdapter);
        menuGridView = (GridView) headView.findViewById(R.id.home_menugv);
        bannerView = (Banner) headView.findViewById(R.id.homepage_banner);
        if (menuAdapter != null) {
            menuGridView.setAdapter(menuAdapter);
        }
        initBannerAdpater();
//        selectCarTv = (TextView) headView.findViewById(R.id.home_selectedCartv);
        commitBtn = headView.findViewById(R.id.home_commitBtn);
//        phoneNumEd = (EditText) headView.findViewById(R.id.home_homeEd);

//        CarBean selectCarBean = fromIntetn.getParcelableExtra(Constants.IntentParams.DATA);


//         = ;

//        RxBus.$().register(RXBUS_COMMITORDER).subscribe(new Action1<Object>() {
//            @Override
//            public void call(Object obj) {
//
//                if (obj instanceof CarBean) {
//                    selectCarBean = (CarBean) obj;
//                    if (selectCarBean != null) {
//                        //id 为零没有车型id，则为车系
//                        StringBuilder tmpStr = new StringBuilder();
//                        if (!TextUtil.isEmpty(selectCarBean.factoryName)) {
//                            tmpStr.append(selectCarBean.factoryName);
//                        }
//                        if (!TextUtil.isEmpty(selectCarBean.seriesName)) {
//                            tmpStr.append(" " + selectCarBean.seriesName);
//                        }
//                        if (!TextUtil.isEmpty(selectCarBean.name)) {
//                            if (!selectCarBean.name.trim().equals("不限车型"))
//                                tmpStr.append(" " + selectCarBean.name);
//                        }
//                        brandName = tmpStr.toString();
//                    }
//                    if (TextUtils.isEmpty(brandName)) {
//                        brandName = "请选择车型";
//                    }
//                    selectCarTv.setText(brandName);
//                    if(!brandName.equals("请选择车型")){
//                        selectCarTv.setTextColor(mRes.getColor(R.color.text_color3));
//                    }
//                }
//
//            }


//            @Override
//            public void call(CarBean selectCarBean) {

//            }
//        });
//        selectCarTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map<String, Serializable> args = new HashMap<String, Serializable>();
//
//                args.put(Constants.IntentParams.MODULE_TYPE,
//                        Constants.ModuleTypes.TAB_HOME_SEE);
//                CommonUtils.startNewActivity(mContext, args, SelectBrandActivity.class);
//            }
//        });
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (IOUtils.isLogin(getContext())) {
                    CommonUtils.startNewActivity(mContext, CommitCarOrderActivity.class);
//                    homePagePresenter.commitMotoOrder(IOUtils.getToken(mContext), selectCarBean.id, selectCarBean.seriesId, IOUtils.getAccount(getContext()).userInfo.nickName, kPlayCarApp.latitude, kPlayCarApp.longitude, ed);
                }
            }
        });
        headFilterRG = (RadioGroup) headFilterView.findViewById(R.id.home_headfilterRG);
        headFellowBtn = (RadioButton) headFilterView.findViewById(R.id.home_headfilter_fellowBtn);
        headHotBtn = (RadioButton) headFilterView.findViewById(R.id.home_headfilter_hotBtn);
        headCityBtn = (RadioButton) headFilterView.findViewById(R.id.home_headfilter_cityBtn);
        headFilterRG.setOnCheckedChangeListener(this);
        homeListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrollIdle = (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (isScrollIdle && headMargin < 0)
//                    return;
                if (headFilterView != null) {
                    headMargin = DensityUtils.px2dp(mContext, headFilterView.getTop() + 40);
                }
                if (headMargin < 0 || firstVisibleItem >= 3) {
                    homeListview.smoothScrollToPosition(0);
//                    homeFilterRootLayout.setVisibility(View.VISIBLE);
                    if (isGoHomelist) {
                        startToHomelistAct();
                    }
                } else {
                    homeFilterRootLayout.setVisibility(View.GONE);
                    isGoHomelist = true;
                }
            }
        });
//        homeListview.setOnScrollListener(new SmoothListView.OnSmoothScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//
//
//            }
//
//            @Override
//            public void onSmoothScrolling(View view) {
//
//            }
//        });
        homeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((position - 3) >= 0 && (position - 3) < mHomeList.size()) {

                    gotoItem(mHomeList.get(position - 3));
                }
            }
        });
        clearCache();
    }

    private void startToHomelistAct() {
        Intent toIntent = new Intent(mContext, HomeListActivity.class);
        toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mType);
        startActivity(toIntent);
        getActivity().overridePendingTransition(0, 0);
//        getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.still);
    }

    private void clearCache() {
        SharedPreferenceUtil.putBrandlist(getContext(), "");
        SharedPreferenceUtil.putExhibitId(getContext(), 0);
    }

    //【event】最近有车展
//【new_car】新车首发
//【coupon】附近代金券
//【mood】说说
//【play】请你玩
//【roadbook】路书
//【show】光影秀
//【words】普通帖子
//【evaluate】达人评测
//【event_goods】展会现场车主自售
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

    private WbShareHandler mWbHandler = null;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.home_iseeiv:
//                if (IOUtils.isLogin(mContext))
//                    mContext.startActivity(new Intent(mContext, CommitCarOrderActivity.class));
//                break;
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
            case R.id.home_scrollTopIv:
                homeListview.smoothScrollToPosition(0, 0);

                break;
            case R.id.seek: //搜索
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
                break;
            case R.id.home_shareiv: //分享
//                showShare(-1);
                if (IOUtils.isLogin(getContext()))
                    CommonUtils.startNewActivity(mContext, MessageActivity.class);
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
        if (isRefresh) {
            mHomeList.clear();
            isRefresh = false;
            homeListview.setLoadMoreEnable(true);
        }
        mHomeList.addAll(homeList);
        mHomeAdapter.notifyDataSetChanged();
        if (homeList == null || homeList.size() == 0) {
            homeListview.setLoadMoreEnable(false);
        }
    }

    private final String INTENT_KEY_ORDER = "order";

    @Override
    public void showCommitOrderSuccess(int orderId) {
        PlacePoint myPlacePoint = new PlacePoint();
        CommonUtils.showToast(mContext, "我看提交订单成功");
        Intent toCommitOrder = new Intent(getContext(), WaittingPriceActivity.class);
        myPlacePoint.myLalng = new LatLng(Double.parseDouble(kPlayCarApp.getLattitude()),
                Double.parseDouble(kPlayCarApp.getLongitude()));
        toCommitOrder.putExtra(INTENT_KEY_ORDER, myPlacePoint);
        toCommitOrder.putExtra(Constants.IntentParams.ID, orderId);
        toCommitOrder.putExtra(Constants.IntentParams.TITLE, brandName);
        startActivity(toCommitOrder);
    }

    @Override
    public void showCommitOrderFailed(String statusMessage) {
        CommonUtils.showToast(mContext, statusMessage);
    }

    @Override
    public void showFollowAddFailed(String msg, int position) {
        if (msg.equals("不能重复关注")) {
            mHomeList.get(position).createUser.isFollow = 1;
            mHomeAdapter.notifyDataSetChanged();
            CommonUtils.showToast(mContext, "该用户已经关注");
        } else if (msg.equals("不存在关注关系")) {
            mHomeList.get(position).createUser.isFollow = 0;
            mHomeAdapter.notifyDataSetChanged();
            CommonUtils.showToast(mContext, "该用户已取消关注");
        } else {
            CommonUtils.showToast(mContext, msg);
        }
    }

    @Override
    public void showFollowAddSuccess(int position) {
        mHomeList.get(position).createUser.isFollow = 1;
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCancelSuccess(int position) {
        mHomeList.get(position).createUser.isFollow = 0;
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


    public void showShare(int position) {
        if (openShareWindow == null) {
            openShareWindow = new OpenShareWindow(getActivity());
        }
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
    }

    @Override
    public void showDeletSucess(int position) {
        mHomeList.remove(position);
        mHomeAdapter.notifyDataSetChanged();
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
        showLoadingIndicator(active);
        if (!active) {
            homeListview.stopLoadMore();
            homeListview.stopRefresh();
        }
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
               // CommonUtils.startNewActivity(activity, args, HeaderBannerWebActivity.class);
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

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        isRefresh = true;
        homePagePresenter.getHomeList(token, mType, kPlayCarApp.cityId, kPlayCarApp.getLongitude(), kPlayCarApp.getLattitude(), mPageIndex, mPageSize);
    }

    @Override
    public void onLoadMore() {
//        mPageIndex++;
//        homePagePresenter.getHomeList(token, mType, kPlayCarApp.cityId, kPlayCarApp.getLongitude(), kPlayCarApp.getLattitude(), mPageIndex, mPageSize);
    }

    @Override
    public synchronized void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (icChecked == true) {
            return;
        }
        icChecked = true;
        switch (checkedId) {
            case R.id.home_filter_fellowBtn:
            case R.id.home_headfilter_fellowBtn:
                fellowBtn.setChecked(true);
                headFellowBtn.setChecked(true);
                mType = mTypeFollow;
                onRefresh();
                break;
            case R.id.home_filter_hotBtn:
            case R.id.home_headfilter_hotBtn:
                hotBtn.setChecked(true);
                headHotBtn.setChecked(true);
                mType = mTypeHot;
                onRefresh();
                break;
            case R.id.home_filter_cityBtn:
            case R.id.home_headfilter_cityBtn:
                cityBtn.setChecked(true);
                headCityBtn.setChecked(true);
                mType = mTypeCity;
                onRefresh();
                break;
            default:
                break;
        }
        startToHomelistAct();
        icChecked = false;

    }

    private void setSelectType() {
        switch (mType) {
            case "follow":
                fellowBtn.setChecked(true);
                headFellowBtn.setChecked(true);
                onRefresh();
                break;
            case "hot":
                hotBtn.setChecked(true);
                headHotBtn.setChecked(true);
                onRefresh();
                break;
            case "city":
                cityBtn.setChecked(true);
                headCityBtn.setChecked(true);
                onRefresh();
                break;
            default:
                break;
        }

    }


    @Override
    public void onDestroyView() {
//        RxBus.$().unregister(RXBUS_COMMITORDER);
        RxBus.$().unregister(TAG);
        super.onDestroyView();
    }
}
