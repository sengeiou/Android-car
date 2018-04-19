package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.amap.LocationPreviewActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.CouponDetailActivity;
import com.tgf.kcwc.coupon.CouponOrderActivity;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.finddiscount.LimitDiscountNewDetailsActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.ColorBean;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.StoreShowCarDetailModel;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.presenter.DealerDataPresenter;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.DealerDataView;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.see.dealer.SalespersonListActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GridViewWithHeaderAndFooter;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;
import com.tgf.kcwc.view.countdown.CountdownView;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/4/13 10:08
 * E-mail：fishloveqin@gmail.com
 * 店内看车系、车型详情
 */

public class StoreShowCarDetailActivity extends BaseActivity
        implements DealerDataView<StoreShowCarDetailModel>,
        OnBannerClickListener {

    protected Banner banners;
    protected TextView name;
    protected TextView price;
    protected TextView couponPrice;
    protected ImageView counterIcon;
    protected CountdownView setTimeText;
    protected TextView zcImg;
    protected TextView xcImg;
    protected TextView onSaleImg;
    protected LinearLayout dealerBaseInfoLayout;
    protected TextView cfgTitleTv;
    protected TextView cfgDescTv;
    protected TextView appearanceTv;
    protected TextView interiorTv;
    protected RelativeLayout cfgLayout;
    protected TextView orgTitle;
    protected ImageView moreIcon;
    protected TextView orgAddress;
    protected TextView distanceTv;
    protected TextView callBtn2;
    protected TextView navBtn2;
    protected LinearLayout addressLayout;
    protected TextView lmtCpnTv;
    protected ImageView counterIcon2;
    protected CountdownView setTimeText2;
    protected RelativeLayout lmtLayout;
    protected TextView limitCpnDesc;
    protected RelativeLayout limitCouponLayout;
    protected ListView giftList;
    protected LinearLayout giftByShoppingLayout;
    protected ListView couponList;
    protected LinearLayout couponLayout;
    protected GridViewWithHeaderAndFooter salespersonGrid;
    protected RelativeLayout saleEliteLayout;
    protected ListView shopActivityList;
    protected RelativeLayout shopActivityLayout;
    protected GridViewWithHeaderAndFooter storeShowGridView;
    protected RelativeLayout storeShowCarLayout;
    protected RelativeLayout secHeaderLayout;
    protected GridViewWithHeaderAndFooter storeLiveGridView;
    protected RelativeLayout storeLiveCarLayout;
    protected TextView callBtn;
    protected TextView navBtn;
    private int mId = -1;
    private int isSeries = -1;
    private int mOrgId = -1;
    private DealerDataPresenter mPresenter;
    private int mCarId = -1;
    private RelativeLayout salesHeaderLayout, shopActivityHeaderLayout,
            storeShowCarHeaderLayout, storeLiveCarHeaderLayout;
    private String tel = "";
    private String lat = "";
    private String lng = "";

    private RelativeLayout mOrgFunctionLayout;
    private WbShareHandler mWbHandler;
    private RelativeLayout mLmtTimeLayout;
    private String mTitle = "";

    private TextView mTitleTv;

    private ObservableScrollView mScrollView;
    private RelativeLayout mTitleBar2;

    private UserDataPresenter mAddFavoritePresenter;
    private UserDataPresenter mCancelFavoritePresenter;

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {

        //findViewById(R.id.titleBar).setBackgroundResource(R.drawable.btn_share_selector);
        backEvent(back);
        mTitleTv = text;

        text.setText(mTitle);
        function.setImageResource(R.drawable.btn_more);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePopWindowUI();
                showPopupWindow(function);
            }
        });
    }

    List<MorePopupwindowBean> dataList = new ArrayList<>();


    private void share() {
        if (mStoreShowCarDetailModel == null) {
            CommonUtils.showToast(mContext, "数据加载中,暂时无法分享!");
            return;
        }
        final String title = mStoreShowCarDetailModel.factoryName
                + mStoreShowCarDetailModel.seriesName;
        final String description = "厂商指导价:" + mStoreShowCarDetailModel.referencePrice + "-"
                + mStoreShowCarDetailModel.referencePriceMax + "万";
        final String cover = URLUtil.builderImgUrl(mStoreShowCarDetailModel.appearanceImg,
                360, 360);

        openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                switch (position) {

                    case 0:
                        OpenShareUtil.sendWXMoments(mContext, mGlobalContext.getMsgApi(),
                                title, description);
                        break;

                    case 1:
                        OpenShareUtil.sendWX(mContext, mGlobalContext.getMsgApi(), title,
                                description);
                        break;
                    case 2:
                        mWbHandler = OpenShareUtil
                                .shareSina(StoreShowCarDetailActivity.this, title, description);
                        break;

                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(mGlobalContext.getTencent(),
                                StoreShowCarDetailActivity.this, mBaseUIListener, urls, title,
                                description);
                        break;

                    case 4:
                        OpenShareUtil.shareToQQ(mGlobalContext.getTencent(),
                                StoreShowCarDetailActivity.this, mBaseUIListener, title, cover,
                                description);
                        break;
                }
            }
        });
        openShareWindow.show(StoreShowCarDetailActivity.this);

    }

    private MorePopupWindow morePopupWindow = null;

    /**
     * 创建弹出菜单
     */
    private void builderPopMenu() {


        morePopupWindow = new MorePopupWindow(this,
                dataList, new MorePopupWindow.MoreOnClickListener() {
            @Override
            public void moreOnClickListener(int position, MorePopupwindowBean item) {
                switch (position) {
                    case 0:
                        if (IOUtils.isLogin(mContext)) {
                            setFavorite();
                        }

                        break;
                    case 1:

                        share();
                        break;


                }
            }
        });
    }

    private void showPopupWindow(FunctionView function) {
        morePopupWindow.showPopupWindow(function);
    }

    private void setFavorite() {

        if (mStoreShowCarDetailModel == null) {
            CommonUtils.showToast(mContext, "数据正在加载.....");
            return;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        // args.put("attach", "");
        String model = "";
        String sourceId = "";
        if (mId != -1) {
            model = Constants.FavoriteTypes.STORE_CAR;
            sourceId = mStoreShowCarDetailModel.id + "";
        } else {
            model = Constants.FavoriteTypes.CAR;
            sourceId = mStoreShowCarDetailModel.carId + "";
        }
        String title = mStoreShowCarDetailModel.factoryName
                + mStoreShowCarDetailModel.seriesName;
        params.put("img_path", mStoreShowCarDetailModel.appearanceImg);
        params.put("model", model);
        params.put("source_id", sourceId);
        params.put("title", title);
        params.put("type", "car");
        params.put("token", IOUtils.getToken(mContext));
        if (isFav == 1) {
            mCancelFavoritePresenter.cancelFavoriteData(params);
        } else {
            mAddFavoritePresenter.addFavoriteData(params);
        }

    }


    @Override
    protected void setUpViews() {
        initView();
        initHeaderTitle();
        mPresenter = new DealerDataPresenter();
        mPresenter.attachView(this);
        // mId = 41;


        builderPopMenu();
        if (mId != -1) {
            mPresenter.getStoreCarDetail(mId + "", IOUtils.getToken(mContext));
        } else {
            mPresenter.getStoreCarDetail(mCarId + "", mOrgId + "", IOUtils.getToken(mContext));
        }

        mAddFavoritePresenter = new UserDataPresenter();
        mAddFavoritePresenter.attachView(mAddFavoriteView);
        mCancelFavoritePresenter = new UserDataPresenter();
        mCancelFavoritePresenter.attachView(mCancelFavoriteView);


    }

    private UserDataView<Object> mAddFavoriteView = new UserDataView<Object>() {
        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public void showDatas(Object o) {

            isFav = 1;

            CommonUtils.showToast(mContext, "收藏成功");
        }
    };

    private int isFav = -1;
    private UserDataView<Object> mCancelFavoriteView = new UserDataView<Object>() {
        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public void showDatas(Object o) {

            isFav = 0;
            CommonUtils.showToast(mContext, "已取消收藏");

        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mWbHandler != null) {
            mWbHandler.doResultIntent(intent, new SinaWBCallback(mContext));
        }
    }

    private void initHeaderTitle() {

        initCommonTitleView(couponLayout, R.drawable.coupon_icon, "代金券");
        initCommonTitleView(giftByShoppingLayout, R.drawable.send_gift_icon, "购车有礼");

        initCommonTitleView2(salesHeaderLayout, "销售精英", true);
        initCommonTitleView2(shopActivityHeaderLayout, "活动", true);
        initCommonTitleView2(storeLiveCarHeaderLayout, "本店现车", true);
        initCommonTitleView2(storeShowCarHeaderLayout, "本店展车", true);

    }

    private void initCommonTitleView2(View parent, String title, boolean isMore) {

        TextView titleTv = (TextView) parent.findViewById(R.id.title);
        titleTv.setText(title);
        ImageView moreIcon = (ImageView) parent.findViewById(R.id.moreIcon);
        if (isMore) {
            moreIcon.setVisibility(View.VISIBLE);
            parent.setOnClickListener(this);
        } else {
            moreIcon.setVisibility(View.GONE);
        }

    }

    private void initCommonTitleView(View parent, int drawableId, String title) {

        ImageView imgView = (ImageView) parent.findViewById(R.id.img_icon);
        TextView titleTv = (TextView) parent.findViewById(R.id.title);
        imgView.setImageResource(drawableId);
        titleTv.setText(title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        isSeries = intent.getIntExtra(Constants.IntentParams.DATA, -1);
        mOrgId = intent.getIntExtra(Constants.IntentParams.DATA2, -1);
        mCarId = intent.getIntExtra(Constants.IntentParams.CAR_ID, -1);
        mTitle = intent.getStringExtra(Constants.IntentParams.DATA3);
        openShareWindow = new OpenShareWindow(this);

        String[] navValues = mRes.getStringArray(R.array.global_nav_values3);

        int length = navValues.length;
        for (String value : navValues) {

            MorePopupwindowBean morePopupwindowBean = null;
            morePopupwindowBean = new MorePopupwindowBean();
            morePopupwindowBean.title = value;
            dataList.add(morePopupwindowBean);
        }
        super.setContentView(R.layout.activity_store_show_car_detail);

    }

    private OpenShareWindow openShareWindow;

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private ObservableScrollView.ScrollViewListener mScrollListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                    int oldy) {

            int topHeight = BitmapUtils.dp2px(mContext, 132);
            if (y >= topHeight) {
                findViewById(R.id.titleBar).setBackgroundResource(R.color.style_bg1);
            } else {
                findViewById(R.id.titleBar).setBackgroundResource(R.drawable.shape_titlebar_bg);

            }
            Logger.d("x:" + x + ",y:" + y + ",oldX:" + oldx + "oldy:" + oldy);
        }
    };

    private void initView() {
        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        mScrollView.setScrollViewListener(mScrollListener);
        mTitleBar2 = (RelativeLayout) findViewById(R.id.titleBar);

        mTitleBar2.setBackgroundResource(R.drawable.shape_titlebar_bg);
        banners = (Banner) findViewById(R.id.banners);
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        couponPrice = (TextView) findViewById(R.id.couponPrice);
        counterIcon = (ImageView) findViewById(R.id.counterIcon);
        setTimeText = (CountdownView) findViewById(R.id.setTimeText);
        zcImg = (TextView) findViewById(R.id.zcImg);
        xcImg = (TextView) findViewById(R.id.xcImg);
        onSaleImg = (TextView) findViewById(R.id.onSaleImg);
        dealerBaseInfoLayout = (LinearLayout) findViewById(R.id.dealerBaseInfoLayout);
        cfgTitleTv = (TextView) findViewById(R.id.cfgTitleTv);
        cfgDescTv = (TextView) findViewById(R.id.cfgDescTv);
        appearanceTv = (TextView) findViewById(R.id.appearanceTv);
        interiorTv = (TextView) findViewById(R.id.interiorTv);
        cfgLayout = (RelativeLayout) findViewById(R.id.cfgLayout);
        cfgLayout.setOnClickListener(this);
        orgTitle = (TextView) findViewById(R.id.orgTitle);
        moreIcon = (ImageView) findViewById(R.id.moreIcon);
        orgAddress = (TextView) findViewById(R.id.orgAddress);
        distanceTv = (TextView) findViewById(R.id.distanceTv);
        callBtn2 = (TextView) findViewById(R.id.callBtn2);
        navBtn2 = (TextView) findViewById(R.id.navBtn2);
        addressLayout = (LinearLayout) findViewById(R.id.addressLayout);
        lmtCpnTv = (TextView) findViewById(R.id.lmtCpnTv);
        counterIcon2 = (ImageView) findViewById(R.id.counterIcon2);
        setTimeText2 = (CountdownView) findViewById(R.id.setTimeText2);
        lmtLayout = (RelativeLayout) findViewById(R.id.lmtLayout);
        limitCpnDesc = (TextView) findViewById(R.id.limitCpnDesc);
        limitCouponLayout = (RelativeLayout) findViewById(R.id.limitCouponLayout);
        limitCouponLayout.setOnClickListener(this);
        mLmtTimeLayout = (RelativeLayout) findViewById(R.id.limitTimeLayout);
        giftList = (ListView) findViewById(R.id.giftList);
        giftByShoppingLayout = (LinearLayout) findViewById(R.id.giftByShoppingLayout);
        couponList = (ListView) findViewById(R.id.couponList);
        couponLayout = (LinearLayout) findViewById(R.id.couponLayout);
        secHeaderLayout = (RelativeLayout) findViewById(R.id.secHeaderLayout);
        salespersonGrid = (GridViewWithHeaderAndFooter) findViewById(R.id.salespersonGrid);
        saleEliteLayout = (RelativeLayout) findViewById(R.id.saleEliteLayout);
        secHeaderLayout = (RelativeLayout) findViewById(R.id.secHeaderLayout);
        shopActivityList = (ListView) findViewById(R.id.shopActivityList);
        shopActivityLayout = (RelativeLayout) findViewById(R.id.shopActivityLayout);
        storeShowGridView = (GridViewWithHeaderAndFooter) findViewById(R.id.storeShowGridView);
        storeShowCarLayout = (RelativeLayout) findViewById(R.id.storeShowCarLayout);
        secHeaderLayout = (RelativeLayout) findViewById(R.id.secHeaderLayout);
        storeLiveGridView = (GridViewWithHeaderAndFooter) findViewById(R.id.storeLiveGridView);
        storeLiveCarLayout = (RelativeLayout) findViewById(R.id.storeLiveCarLayout);
        shopActivityHeaderLayout = (RelativeLayout) findViewById(R.id.shopActivityHeaderLayout);
        salesHeaderLayout = (RelativeLayout) findViewById(R.id.salesHeaderLayout);
        salesHeaderLayout.setOnClickListener(this);
        storeLiveCarHeaderLayout = (RelativeLayout) findViewById(R.id.storeLiveCarHeaderLayout);
        storeLiveCarHeaderLayout.setOnClickListener(this);
        storeShowCarHeaderLayout = (RelativeLayout) findViewById(R.id.storeShowCarHeaderLayout);
        storeShowCarHeaderLayout.setOnClickListener(this);
        callBtn = (TextView) findViewById(R.id.callBtn);
        navBtn = (TextView) findViewById(R.id.navBtn);
        callBtn.setOnClickListener(this);
        navBtn.setOnClickListener(this);
        callBtn2.setOnClickListener(this);
        navBtn2.setOnClickListener(this);
        mOrgFunctionLayout = (RelativeLayout) findViewById(R.id.orgFunctionLayout);
        mOrgFunctionLayout.setOnClickListener(this);
    }

    private void showCarBaseInfo(StoreShowCarDetailModel storeShowCarDetailModel) {
        //  name.setText(storeShowCarDetailModel.factoryName + "" + storeShowCarDetailModel.seriesName);
        isSeries = storeShowCarDetailModel.isSeries;
        if (isSeries == 1) {
            name.setText(storeShowCarDetailModel.factoryName + "" + storeShowCarDetailModel.seriesName);
            price.setText("指导价:" + storeShowCarDetailModel.referencePrice + "-"
                    + storeShowCarDetailModel.referencePriceMax + "万");
        } else {
            name.setText(storeShowCarDetailModel.factoryName + "" + storeShowCarDetailModel.seriesName + "" + storeShowCarDetailModel.carName);
            price.setText("参考价:" + storeShowCarDetailModel.referencePrice + "万");
        }

    }

    private StoreShowCarDetailModel mStoreShowCarDetailModel;

    private void updatePopWindowUI() {

        if (isFav == 1) {
            dataList.get(0).title = "已收藏";
        } else {
            dataList.get(0).title = "收藏";
        }

        morePopupWindow.getmCommonAdapter().notifyDataSetChanged();
        morePopupWindow.update();
    }

    @Override
    public void showData(StoreShowCarDetailModel storeShowCarDetailModel) {

        mStoreShowCarDetailModel = storeShowCarDetailModel;
        isFav = storeShowCarDetailModel.isFav;
        updatePopWindowUI();
        mTitle = mStoreShowCarDetailModel.factoryName + mStoreShowCarDetailModel.seriesName + "-"
                + mStoreShowCarDetailModel.org.name;
        mScrollView.scrollTo(0, 0);
        mTitleTv.setText(mTitle); //显示Title
        tel = storeShowCarDetailModel.org.tel;
        lng = storeShowCarDetailModel.org.longitude;
        lat = storeShowCarDetailModel.org.latitude;
        showBanners(storeShowCarDetailModel.appearanceImg, storeShowCarDetailModel.interiorImg);
        showCarBaseInfo(storeShowCarDetailModel);
        showLimitInfo(storeShowCarDetailModel);
        showSaleCarState(storeShowCarDetailModel);
        showCarCfg(storeShowCarDetailModel);
        showAddressInfo(storeShowCarDetailModel);
        showLimitTimeCouponContent(storeShowCarDetailModel);
        showSalespersonLists(storeShowCarDetailModel.saler);
        showGiftByCar(storeShowCarDetailModel.gift);
        showCoupons(storeShowCarDetailModel.coupon);
        showTopics(storeShowCarDetailModel.orgActivity);
        showStoreLiveCars(storeShowCarDetailModel);
        showExhibitionCars(storeShowCarDetailModel);
    }

    private void showExhibitionCars(StoreShowCarDetailModel model) {

        showCountText(storeShowCarHeaderLayout, model.showCount);
        bindGridAdapter(model.showList, storeShowGridView);
    }

    private void showStoreLiveCars(StoreShowCarDetailModel model) {
        showCountText(storeLiveCarHeaderLayout, model.existCount);
        bindGridAdapter(model.existList, storeLiveGridView);
    }

    /**
     * 显示现车、展车数量
     */
    private void showCountText(View parent, int total) {

        TextView textTv = (TextView) parent.findViewById(R.id.content2);
        textTv.setVisibility(View.VISIBLE);
        textTv.setTextColor(mRes.getColor(R.color.text_color15));
        textTv.setText("共" + total + "款");
    }

    private void bindGridAdapter(List<CarBean> existList, GridView grid) {

        WrapAdapter<CarBean> adapter = new WrapAdapter<CarBean>(mContext,
                R.layout.common_img_text_list_item, existList) {
            @Override
            public void convert(ViewHolder helper, CarBean item) {

                SimpleDraweeView img = helper.getView(R.id.img);
                img.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.appearanceImg, 270, 203)));
                TextView titleTv = helper.getView(R.id.title);
                titleTv.setText(item.carName);
            }
        };
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CarBean carBean = (CarBean) parent.getAdapter().getItem(position);
                mId = carBean.id;
                mPresenter.getStoreCarDetail(mId + "", IOUtils.getToken(mContext));
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(grid, 3);
    }

    /**
     * 活动列表
     *
     * @param topics
     */
    private void showTopics(List<Topic> topics) {

        int size = topics.size();
        if (size == 0) {
            shopActivityLayout.setVisibility(View.GONE);
            shopActivityHeaderLayout.setVisibility(View.GONE);
        } else {
            shopActivityHeaderLayout.setVisibility(View.VISIBLE);
            shopActivityLayout.setVisibility(View.VISIBLE);
        }
        shopActivityList
                .setAdapter(new WrapAdapter<Topic>(mContext, R.layout.common_text_list_item2, topics) {
                    @Override
                    public void convert(ViewHolder helper, Topic item) {

                        helper.setText(R.id.title, item.title);
                        helper.setText(R.id.createTime, item.createTime);
                        TextView tv = helper.getView(R.id.endFlag);
                        int status = item.status;
                        if (status == 3) {
                            tv.setVisibility(View.VISIBLE);
                        } else {
                            tv.setVisibility(View.GONE);
                        }
                    }
                });
        shopActivityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Topic topic = (Topic) parent.getAdapter().getItem(position);

                String model = topic.model;
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, topic.id + "");
                if ("cycle".equals(model)) {
                    CommonUtils.startNewActivity(mContext, args, DrivingDetailsActivity.class);
                } else if ("play".equals(model)) {
                    CommonUtils.startNewActivity(mContext, args, PleasePlayDetailsActivity.class);
                }

            }
        });
        ViewUtil.setListViewHeightBasedOnChildren1(shopActivityList);
    }

    /**
     * 代金券列表
     *
     * @param coupons
     */
    private void showCoupons(List<Coupon> coupons) {

        int size = coupons.size();

        if (size == 0) {
            couponLayout.setVisibility(View.GONE);
        } else {
            couponLayout.setVisibility(View.VISIBLE);
        }
        final WrapAdapter<Coupon> adapter = new WrapAdapter<Coupon>(mContext, coupons,
                R.layout.coupon_list_item) {
            @Override
            public void convert(ViewHolder helper, final Coupon item) {

                helper.setImageByUrl(R.id.img, URLUtil.builderImgUrl(item.cover, 270, 203));
                TextView denominationTv = helper.getView(R.id.price);
                TextView originalPriceText = helper.getView(R.id.originalPrice);
                TextView moneyTagTv = helper.getView(R.id.moneyTag);
                Button button = helper.getView(R.id.acquireBtn);
                TextView countTv = helper.getView(R.id.acquiredCount);
                button.setVisibility(View.GONE);
                countTv.setText("已领" + item.count);
                countTv.setVisibility(View.VISIBLE);
                //设置点击事件跳转到代金券领取页面
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id);
                        CommonUtils.startNewActivity(mContext, args, CouponOrderActivity.class);
                    }
                });
                if (item.price.equals("0")) {
                    button.setText("领");
                    moneyTagTv.setText("");
                    denominationTv.setText("免费");
                    denominationTv.setTextColor(mRes.getColor(R.color.text_color16));
                    button.setBackgroundResource(R.drawable.button_bg_2);
                } else {
                    button.setText("抢");
                    denominationTv.setText(item.price);
                    // denominationTv.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                    moneyTagTv.setText("￥");
                    button.setBackgroundResource(R.drawable.button_bg_15);
                }
                originalPriceText.setText("￥" + item.price);
                originalPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                helper.setText(R.id.title, item.title);
                TextView expireTv = helper.getView(R.id.expire);
                expireTv.setVisibility(View.GONE);
//                String exp = item.endTime;
//                if (exp.length() > 0) {
//                    exp = exp.replaceAll("-", ".");
//                    exp = exp.substring(0, 11);
//                }
//
//                helper.setText(R.id.expire, "有效期至" + exp);
            }
        };
        couponList.setAdapter(adapter);
        couponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Coupon c = (Coupon) parent.getAdapter().getItem(position);

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, c.id);

                CommonUtils.startNewActivity(mContext, args, CouponDetailActivity.class);

            }
        });
        ViewUtil.setListViewHeightBasedOnChildren2(couponList);
    }

    /**
     * 购车有礼
     *
     * @param gifts
     */
    private void showGiftByCar(List<StoreShowCarDetailModel.GiftBean> gifts) {

        int size = gifts.size();
        if (size == 0) {
            giftByShoppingLayout.setVisibility(View.GONE);
        } else {
            giftByShoppingLayout.setVisibility(View.VISIBLE);
        }
        giftList.setAdapter(new WrapAdapter<StoreShowCarDetailModel.GiftBean>(mContext,
                R.layout.common_text_list_item, gifts) {
            @Override
            public void convert(ViewHolder helper, StoreShowCarDetailModel.GiftBean item) {

                helper.setText(R.id.title, item.title);
                helper.setText(R.id.desc, item.desc);

            }
        });

        ViewUtil.setListViewHeightBasedOnChildren2(giftList);
    }

    /**
     * 销售精英
     *
     * @param users
     */
    private void showSalespersonLists(List<Account.UserInfo> users) {

        WrapAdapter<Account.UserInfo> adapter = new WrapAdapter<Account.UserInfo>(mContext, users,
                R.layout.salespersion_grid_item) {
            @Override
            public void convert(WrapAdapter.ViewHolder helper, Account.UserInfo item) {

                helper.setImageByUrl(R.id.img, URLUtil.builderImgUrl(item.avatar, 144, 144));

                String nick = item.nickName;
                if (nick == null) {
                    nick = item.nick;
                }
                helper.setText(R.id.name, nick);
                helper.setText(R.id.star, item.star);

            }
        };
        salespersonGrid.setAdapter(adapter);
        salespersonGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Account.UserInfo item = (Account.UserInfo) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.ID, item.userId);
                intent.setClass(mContext, UserPageActivity.class);
                startActivity(intent);
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(salespersonGrid, 6);
    }

    /**
     * 限时优惠
     *
     * @param storeShowCarDetailModel
     */
    private void showLimitTimeCouponContent(StoreShowCarDetailModel storeShowCarDetailModel) {

        StoreShowCarDetailModel.LimitTimeBean lmt = storeShowCarDetailModel.limitTime;
        int isLimitTime = storeShowCarDetailModel.isLimitTime;
        if (isLimitTime == 1) {
            limitCouponLayout.setVisibility(View.VISIBLE);
            long countdown = lmt.countdown;
            setTimeText2.start(lmt.countdown * 1000);//将秒转换为毫秒
            limitCpnDesc.setText(Html.fromHtml(lmt.title+"\n"+lmt.content));
            lmtCpnTv.setText("限时优惠");
        } else {
            limitCouponLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 地址基本信息
     *
     * @param storeShowCarDetailModel
     */
    private void showAddressInfo(StoreShowCarDetailModel storeShowCarDetailModel) {

        mOrgName = storeShowCarDetailModel.org.name;
        orgTitle.setText(mOrgName);
        mAddress = storeShowCarDetailModel.org.address;
        orgAddress.setText("地址:  " + mAddress);

    }

    private String mOrgName = "", mAddress = "";
    private ArrayList<Image> bannerImages = new ArrayList<Image>();

    private List<String> urls = new ArrayList<String>();

    /**
     * 头部Banner轮播
     *
     * @param appearImg
     * @param interiorImg
     */
    private void showBanners(String appearImg, String interiorImg) {

        urls.clear();

        appearImg = URLUtil.builderImgUrl(appearImg, 540, 270);
        interiorImg = URLUtil.builderImgUrl(interiorImg, 540, 270);
        urls.add(appearImg);
        urls.add(interiorImg);

        bannerImages.clear();
        Image image = new Image();
        image.name = "";
        image.link = appearImg;
        bannerImages.add(image);

        image = new Image();
        image.name = "";
        image.link = interiorImg;
        bannerImages.add(image);
        banners.setImages(urls).setImageLoader(new FrescoImageLoader())
                .setOnBannerClickListener(this).start();
    }

    /**
     * 限时优惠基本信息
     *
     * @param storeShowCarDetailModel
     */
    private void showLimitInfo(StoreShowCarDetailModel storeShowCarDetailModel) {

        int rateType = storeShowCarDetailModel.rateType;

        if (rateType == 0) {
            mLmtTimeLayout.setVisibility(View.GONE);
            return;
        }

        String value = "";

        //1、综合优惠（元） 3、现金折扣（折）4、现金优惠（元）
        if (rateType == 1) {
            value = "综合优惠   " + storeShowCarDetailModel.rate + "元";
        } else if (rateType == 3) {
            value = "现金折扣   " + storeShowCarDetailModel.rate + "折";
        } else if (rateType == 4) {
            value = "现金优惠   " + storeShowCarDetailModel.rate + "元";
        }
        couponPrice.setText("" + value);
        long endTime = DateFormatUtil.getTime(storeShowCarDetailModel.endTime);
        long nowTime = DateFormatUtil.getTime(storeShowCarDetailModel.currentTime);
        setTimeText.start(endTime - nowTime);
    }

    /**
     * 店内车状态 展车、现车、在售
     *
     * @param storeShowCarDetailModel
     */
    private void showSaleCarState(StoreShowCarDetailModel storeShowCarDetailModel) {

        if (storeShowCarDetailModel.typeExist == 1) {
            xcImg.setBackgroundResource(R.drawable.button_bg_53);
        } else {
            xcImg.setBackgroundResource(R.drawable.button_bg_17);
        }

        if (storeShowCarDetailModel.typeShow == 1) {
            zcImg.setBackgroundResource(R.drawable.button_bg_3);
        } else {
            zcImg.setBackgroundResource(R.drawable.button_bg_17);
        }

        if (storeShowCarDetailModel.onSale == 1) {
            onSaleImg.setBackgroundResource(R.drawable.button_bg_3);
        } else {
            onSaleImg.setBackgroundResource(R.drawable.button_bg_17);
        }
    }

    /**
     * 车型配置
     *
     * @param storeShowCarDetailModel
     */
    private void showCarCfg(StoreShowCarDetailModel storeShowCarDetailModel) {

        String appearName = String.format(getString(R.string.color_tag1),
                storeShowCarDetailModel.appearanceColorName);
        String interiorName = String.format(getString(R.string.color_tag2),
                storeShowCarDetailModel.interiorColorName);

        if ("- -".equals(storeShowCarDetailModel.appearanceColorName.trim())) {
            appearanceTv.setVisibility(View.GONE);
        } else {
            appearanceTv.setVisibility(View.VISIBLE);
        }
        if ("- -".equals(storeShowCarDetailModel.interiorColorName.trim())) {
            interiorTv.setVisibility(View.GONE);
        } else {
            interiorTv.setVisibility(View.VISIBLE);
        }
        CommonUtils.customDisplayText(mRes, appearanceTv, appearName, R.color.text_color12);
        CommonUtils.customDisplayText(mRes, interiorTv, interiorName, R.color.text_color12);
        if (TextUtil.isEmpty(storeShowCarDetailModel.diffConf.trim())) {
            cfgDescTv.setVisibility(View.GONE);
        } else {
            cfgDescTv.setVisibility(View.VISIBLE);
        }
        cfgDescTv.setText(storeShowCarDetailModel.diffConf);
    }

    @Override
    public void OnBannerClick(int position) {

        if (bannerImages.size() > 0) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("list", bannerImages);
            intent.putExtra(Constants.IntentParams.DATA, position - 1);
            intent.setClass(mContext, PhotoBrowserActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.salesHeaderLayout:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mOrgId + "");
                CommonUtils.startNewActivity(mContext, args, SalespersonListActivity.class);
                break;

            case R.id.callBtn:
            case R.id.callBtn2:
                SystemInvoker.launchDailPage(mContext, tel + "");
                break;
            case R.id.navBtn:
            case R.id.navBtn2:
                //
                //                MapNavPopWindow mapNavPopWindow = new MapNavPopWindow(mContext);
                //                mapNavPopWindow.setOnClickListener(this);
                //                mapNavPopWindow.show(this);

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.LAT, lat);
                args.put(Constants.IntentParams.LNG, lng);
                args.put(Constants.IntentParams.DATA, mOrgName);
                args.put(Constants.IntentParams.DATA2, mAddress);
                CommonUtils.startNewActivity(mContext, args, LocationPreviewActivity.class);

                break;

            case R.id.aMap:

                try {
                    URLUtil.launcherInnerNavAMap(this, lat, lng);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.baiduMap:
                try {
                    URLUtil.launcherInnerNavBaidu(this, lat, lng);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cancel:

                break;

            case R.id.storeLiveCarHeaderLayout:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.INDEX, 2);
                args.put(Constants.IntentParams.ID, mOrgId + "");
                args.put(Constants.IntentParams.TITLE, mOrgName);
                CommonUtils.startNewActivity(mContext, args, DealerHomeActivity.class);
                break;

            case R.id.storeShowCarHeaderLayout:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mOrgId + "");
                args.put(Constants.IntentParams.TITLE, mOrgName);
                args.put(Constants.IntentParams.INDEX, 1);
                CommonUtils.startNewActivity(mContext, args, DealerHomeActivity.class);
                break;

            case R.id.cfgLayout:

                args = new HashMap<String, Serializable>();

                args.put(Constants.IntentParams.ID, mId);
                CommonUtils.startResultNewActivity(this, args, CarColorConfigActivity.class,
                        Constants.IntentParams.REQUEST_CODE);
                break;

            case R.id.orgFunctionLayout:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mOrgId + "");
                args.put(Constants.IntentParams.TITLE, mOrgName);
                CommonUtils.startNewActivity(mContext, args, DealerHomeActivity.class);
                break;
            case R.id.shopActivityHeaderLayout:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mOrgId + "");
                args.put(Constants.IntentParams.TITLE, mOrgName);
                CommonUtils.startNewActivity(mContext, args, DealerHomeActivity.class);
                break;

            case R.id.limitCouponLayout:
                if (mStoreShowCarDetailModel != null) {
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, mStoreShowCarDetailModel.limitTime.id + "");

                    CommonUtils.startNewActivity(mContext,args, LimitDiscountNewDetailsActivity.class);
                }


                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.IntentParams.REQUEST_CODE && resultCode == RESULT_OK) {

            ColorBean bean = data.getParcelableExtra(Constants.IntentParams.DATA);

            mId = bean.id;
            mPresenter.getStoreCarDetail(mId + "", IOUtils.getToken(mContext));

        }

    }
}
