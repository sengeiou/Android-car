package com.tgf.kcwc.finddiscount;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.LimitDiscountNewDetailsLimitModel;
import com.tgf.kcwc.mvp.model.LimitDiscountNewDetailsModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.LimitDiscountDetailsPresenter;
import com.tgf.kcwc.mvp.view.LimitDiscountDetailsView;
import com.tgf.kcwc.see.StoreShowCarDetailActivity;
import com.tgf.kcwc.see.exhibition.ExhibitPlaceDetailActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.ticket.PreRegistrationActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.MyGridView;
import com.tgf.kcwc.view.MyListView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.countdown.CountdownView;
import com.tgf.kcwc.view.countdown.DynamicConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/11.
 */

public class LimitDiscountNewDetailsOddsActivity extends BaseActivity implements LimitDiscountDetailsView {


    LimitDiscountDetailsPresenter mLimitDiscountDetailsPresenter;
    private TextView mTitleText;                    //标题文本
    private TextView mtitle;                    //标题文本
    private SimpleDraweeView mICon;
    private SimpleDraweeView mBrandIcon;
    CountdownView limitSettimetext;
    private TextView countericon;                    //状态
    TextView limitSettimetextitletv;//未开始
    WebView mWebView;//介绍
    LinearLayout mWebviewLayout;//介绍布局
    LinearLayout mLimitTimeLayout;//特价车布局

    TextView mOfferName; //特价名字
    TextView mOfferPrice;//特价价格
    TextView mOfferGuideprice; //特价指导价
    TextView mOfferType;//特价类型
    TextView mOfferNum; //特价数量

    LinearLayout mBrandBenefitLayout;//品牌布局
    LinearLayout mLimitGotodetailll;//title布局
    TextView mlimitTitle;//title
    TextView mSellNum;//参展商个数


    TextView mExhibitionname; //展会名字
    TextView mExhibitiontime; //展会时间
    TextView mExhibitionsite; //展会地点
    SimpleDraweeView mBrandCIcon; //品牌icon
    TextView mBrandname; //品牌名字
    TextView mExhibitionhall; //展会展馆
    ImageView mAddress; //展会定位
    ImageView mSpecialOfferimage; //特价车
    LinearLayout mUnwindLayout; //下拉
    ImageView mUnwindimag; //下拉
    TextView mUnwindtext; //下拉文字

    TextView mYapplied; //已报名多少人
    private WebView regulation;                    //规则
    private LinearLayout mExhibitionlayout;//展会调转

    private ImageView offerNameTo;
    private TextView applyText;//报名
    private TextView configuration;//配置信息

    private boolean isPull = false;


    private WrapAdapter<LimitDiscountNewDetailsModel.BenefitAttr> gridviewAdapter;
    private List<LimitDiscountNewDetailsModel.BenefitAttr> mBenefitAttrData = new ArrayList<>(); //评价
    private MyGridView mDescribe;

    private MyGridView mSeriesGridview;
    private WrapAdapter<LimitDiscountNewDetailsModel.Product> mSeriesAdapter;
    private List<LimitDiscountNewDetailsModel.Product> mSeriesData = new ArrayList<>(); //车型

    private List<LimitDiscountNewDetailsModel.Exhibitor> mSellDataPortion = new ArrayList<>(); //部分厂商
    private List<LimitDiscountNewDetailsModel.Exhibitor> mSellDataAll = new ArrayList<>(); //全部厂商
    private List<LimitDiscountNewDetailsModel.Exhibitor> mShowSellData = new ArrayList<>(); //显示厂商
    private MyListView mSellListview;
    private WrapAdapter<LimitDiscountNewDetailsModel.Exhibitor> mSellAdapter;

    private String ID = "";
    private String mTel = "";

    private LimitDiscountNewDetailsModel.Product seletcProdut = null;
    LimitDiscountNewDetailsModel mLimitDiscountNewDetailsModel;

    private String[] navValues = null;
    List<MorePopupwindowBean> dataList = new ArrayList<>();
    private OpenShareWindow openShareWindow;
    private WbShareHandler mWbHandler;
    KPlayCarApp mKPlayCarApp;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
        text.setText("展会优惠详情");
        function.setImageResource(R.drawable.global_nav_n);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MorePopupWindow morePopupWindow = new MorePopupWindow(LimitDiscountNewDetailsOddsActivity.this,
                        dataList, new MorePopupWindow.MoreOnClickListener() {
                    @Override
                    public void moreOnClickListener(int position, MorePopupwindowBean item) {
                        switch (position) {
                            case 0:
                                finish();
                                break;
                            case 1:
                                break;
                            case 2:
                                share();
                            case 3:
                                break;
                            case 4:

                                break;
                        }
                    }
                });
                morePopupWindow.showPopupWindow(function);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limitdiscountdetailsodds);
        navValues = mRes.getStringArray(R.array.global_nav_values5);
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
        openShareWindow = new OpenShareWindow(this);
        mKPlayCarApp = (KPlayCarApp) getApplication();

        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        mDescribe = (MyGridView) findViewById(R.id.mygridview);
        mSellListview = (MyListView) findViewById(R.id.selllistview);
        mTitleText = (TextView) findViewById(R.id.title_bar_text);
        mICon = (SimpleDraweeView) findViewById(R.id.icon);
        mtitle = (TextView) findViewById(R.id.title);
        mBrandIcon = (SimpleDraweeView) findViewById(R.id.brandicon);
        mWebView = (WebView) findViewById(R.id.webview);
        limitSettimetext = (CountdownView) findViewById(R.id.limit_settimetext);
        countericon = (TextView) findViewById(R.id.countericon);
        limitSettimetextitletv = (TextView) findViewById(R.id.limit_settimetextitletv);
        mSeriesGridview = (MyGridView) findViewById(R.id.seriesgridview);
        mWebviewLayout = (LinearLayout) findViewById(R.id.webviewlayout);
        mLimitTimeLayout = (LinearLayout) findViewById(R.id.limit_timelayout);
        mBrandBenefitLayout = (LinearLayout) findViewById(R.id.brand_benefitlayout);
        mLimitGotodetailll = (LinearLayout) findViewById(R.id.limit_gotodetailll);
        mOfferName = (TextView) findViewById(R.id.offername);
        mOfferPrice = (TextView) findViewById(R.id.offerprice);
        mOfferGuideprice = (TextView) findViewById(R.id.offerguideprice);
        mOfferType = (TextView) findViewById(R.id.offertype);
        mOfferNum = (TextView) findViewById(R.id.offernum);
        mlimitTitle = (TextView) findViewById(R.id.limit_title);
        mSellNum = (TextView) findViewById(R.id.sellnum);
        mYapplied = (TextView) findViewById(R.id.yapplied);
        regulation = (WebView) findViewById(R.id.regulationwebview);
        mExhibitionlayout = (LinearLayout) findViewById(R.id.exhibitionlayout);
        mSpecialOfferimage = (ImageView) findViewById(R.id.specialofferimage);
        configuration = (TextView) findViewById(R.id.configuration);

        mExhibitionname = (TextView) findViewById(R.id.exhibitionname);
        mExhibitiontime = (TextView) findViewById(R.id.exhibitiontime);
        mExhibitionsite = (TextView) findViewById(R.id.exhibitionsite);
        mBrandCIcon = (SimpleDraweeView) findViewById(R.id.brand_icon);
        mBrandname = (TextView) findViewById(R.id.brandname);
        mExhibitionhall = (TextView) findViewById(R.id.exhibitionhall);
        mAddress = (ImageView) findViewById(R.id.address);
        mUnwindLayout = (LinearLayout) findViewById(R.id.unwindlayout);
        mUnwindimag = (ImageView) findViewById(R.id.unwindimag);
        mUnwindtext = (TextView) findViewById(R.id.unwindtext);
        offerNameTo = (ImageView) findViewById(R.id.offername_go);
        applyText = (TextView) findViewById(R.id.applytext);

        mLimitDiscountDetailsPresenter = new LimitDiscountDetailsPresenter();
        mLimitDiscountDetailsPresenter.attachView(this);

        setAdapter();
        applyText.setOnClickListener(this);
        mExhibitionlayout.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mUnwindLayout.setOnClickListener(this);
        offerNameTo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Map<String, Serializable> args = null;
        switch (view.getId()) {
            case R.id.applytext:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mLimitDiscountNewDetailsModel.event.id);
                args.put(Constants.IntentParams.DATA, mLimitDiscountNewDetailsModel.event.name);
                args.put(Constants.IntentParams.DATA2, mLimitDiscountNewDetailsModel.event.cover);
                CommonUtils.startNewActivity(mContext, args, PreRegistrationActivity.class);
                break;
            case R.id.exhibitionlayout:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.INDEX, 1);
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 1);
                KPlayCarApp.putValue(Constants.IntentParams.EVENT_ID, mLimitDiscountNewDetailsModel.event.id + "");
                CommonUtils.startNewActivity(mContext, args, MainActivity.class);
                finish();
                break;
            case R.id.address:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mLimitDiscountNewDetailsModel.event.id);
                args.put(Constants.IntentParams.ID2, mLimitDiscountNewDetailsModel.event.hallId);
                CommonUtils.startNewActivity(mContext, args, ExhibitPlaceDetailActivity.class);
//                CommonUtils.startNewActivity(mContext, args, com.tgf.kcwc.see.exhibition.plus.ExhibitPlaceDetailActivity.class);
                break;
            case R.id.unwindlayout:
                if (isPull) {
                    mShowSellData.clear();
                    mShowSellData.addAll(mSellDataAll);
                    mSellAdapter.notifyDataSetChanged();
                    mUnwindimag.setImageResource(R.drawable.btn_up);
                    mUnwindtext.setText("收起更多适用经销商");
                    isPull = false;
                } else {
                    mShowSellData.clear();
                    mShowSellData.addAll(mSellDataPortion);
                    mSellAdapter.notifyDataSetChanged();
                    mUnwindimag.setImageResource(R.drawable.btn_xiala);
                    mUnwindtext.setText("更多适用经销商");
                    isPull = true;
                }

                break;
            case R.id.offername_go:
                LimitDiscountNewDetailsModel.Sale sale = mLimitDiscountNewDetailsModel.sale;
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.DATA, sale.isSeries); //是否是车系
                args.put(Constants.IntentParams.CAR_ID, sale.productId); //车型ID
                args.put(Constants.IntentParams.DATA3, mLimitDiscountNewDetailsModel.factoryName + mLimitDiscountNewDetailsModel.seriesName + sale.carName); //车型名字
                //args.put(Constants.IntentParams.DATA2, mLimitDiscountNewDetailsModel.factoryId);//机构ID
                if (mLimitDiscountNewDetailsModel.exhibitor.size() > 0) {
                    args.put(Constants.IntentParams.DATA2, mLimitDiscountNewDetailsModel.exhibitor.get(0).id);//机构ID
                    CommonUtils.startNewActivity(mContext, args, StoreShowCarDetailActivity.class);
                } else {
                    CommonUtils.showToast(mContext, "暂无经销商");
                }
                break;
        }
    }

    public void setAdapter() {

        gridviewAdapter = new WrapAdapter<LimitDiscountNewDetailsModel.BenefitAttr>(mContext, R.layout.listitem_newlimitdiscount_item, mBenefitAttrData) {
            @Override
            public void convert(ViewHolder helper, LimitDiscountNewDetailsModel.BenefitAttr item) {
                TextView name = helper.getView(R.id.name);
                name.setText(item.value);
            }
        };
        mDescribe.setAdapter(gridviewAdapter);

        mSeriesAdapter = new WrapAdapter<LimitDiscountNewDetailsModel.Product>(mContext, R.layout.limitdetails_series_item, mSeriesData) {
            @Override
            public void convert(ViewHolder helper, final LimitDiscountNewDetailsModel.Product item) {
                TextView name = helper.getView(R.id.name);
                LinearLayout layout = helper.getView(R.id.layout);
                if (item.isCheck == 1) {
                    layout.setBackground(getResources().getDrawable(R.drawable.button_bg_limitderiesgreen));
                    name.setTextColor(getResources().getColor(R.color.tab_text_s_color));
                    mlimitTitle.setText(item.title);
                    seletcProdut = item;
                } else {
                    layout.setBackground(getResources().getDrawable(R.drawable.button_bg_limitderiesgray));
                    name.setTextColor(getResources().getColor(R.color.text_color));
                }
                name.setText(item.name);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isCheck == 1) {

                        } else {
                            for (LimitDiscountNewDetailsModel.Product data : mSeriesData) {
                                if (data.id == item.id) {
                                    data.isCheck = 1;
                                } else {
                                    data.isCheck = 0;
                                }
                            }
                            mSeriesAdapter.notifyDataSetChanged();

                        }

                    }
                });
            }
        };
        mSeriesGridview.setAdapter(mSeriesAdapter);


        mSellAdapter = new WrapAdapter<LimitDiscountNewDetailsModel.Exhibitor>(mContext, R.layout.limitdiscountdetails_sellitem, mShowSellData) {
            @Override
            public void convert(ViewHolder helper, final LimitDiscountNewDetailsModel.Exhibitor item) {
                TextView mSponsorName = helper.getView(R.id.sponsor_name);
                TextView address = helper.getView(R.id.address);
                ImageView phone = helper.getView(R.id.phone);
                TextView distance = helper.getView(R.id.distance);
                distance.setVisibility(View.GONE);
                mSponsorName.setText(item.fullName);
                address.setText(item.address);
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTel = item.tel;
                        requestPermission();
                    }
                });

            }
        };
        mSellListview.setAdapter(mSellAdapter);

    }

    public void loadMore() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLimitDiscountDetailsPresenter.getEventDetail(ID);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    public void modificationTime(CountdownView limitSettimetext, long time) {
        DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();

        if (time <= 24 * 60 * 60 * 1000) {
            dynamicConfigBuilder.setTimeTextSize(12)
                    .setTimeTextColor(0xFFffe53a)
                    .setTimeTextBold(false)
                    .setSuffixTextColor(0xFFffe53a)
                    .setSuffixTextSize(12)
                    .setSuffixTextBold(false)
                    .setSuffixDay("天").setSuffixHour("时").setSuffixMinute("分").setSuffixSecond("秒").setSuffixMillisecond("毫秒")
                    .setSuffixGravity(DynamicConfig.SuffixGravity.TOP)
                    .setShowDay(true).setShowHour(true).setShowMinute(true).setShowSecond(false).setShowMillisecond(false);
        } else {
            dynamicConfigBuilder.setTimeTextSize(12)
                    .setTimeTextColor(0xFF1e2124)
                    .setTimeTextBold(false)
                    .setSuffixTextColor(0xFF666666)
                    .setSuffixTextSize(12)
                    .setSuffixTextBold(false)
                    .setSuffixDay("天").setSuffixHour("时").setSuffixMinute("分").setSuffixSecond("秒").setSuffixMillisecond("毫秒")
                    .setSuffixGravity(DynamicConfig.SuffixGravity.TOP)
                    .setShowDay(true).setShowHour(true).setShowMinute(true).setShowSecond(false).setShowMillisecond(false);
        }
        limitSettimetext.dynamicShow(dynamicConfigBuilder.build());
    }

    @Override
    public void showEveLimitList(LimitDiscountNewDetailsModel data) {
        mLimitDiscountNewDetailsModel = data;
        mtitle.setText(data.factoryName);
        mICon.setImageURI(Uri.parse(URLUtil.builderImgUrl(data.cover, 540, 270)));
        mBrandIcon.setImageURI(Uri.parse(URLUtil.builderImgUrl(data.factoryLogo, 144, 144)));

        int benefitStatus = data.applyStatus;
        if (benefitStatus == 1) {
            limitSettimetextitletv.setVisibility(View.GONE);
            limitSettimetext.setVisibility(View.VISIBLE);
            countericon.setText("距活动开始：");
            long time = DateFormatUtil.getTime(data.applyBeginTime) - DateFormatUtil.getTime(data.currentTime);
            limitSettimetext.start(time);
            modificationTime(limitSettimetext, time);
        } else if (benefitStatus == 2) {
            limitSettimetextitletv.setVisibility(View.GONE);
            limitSettimetext.setVisibility(View.VISIBLE);
            countericon.setText("距报名结束仅剩：");
            long time = DateFormatUtil.getTime(data.applyEndTime) - DateFormatUtil.getTime(data.currentTime);
            limitSettimetext.start(time);
            modificationTime(limitSettimetext, time);
            applyText.setText("立即报名");
            applyText.setBackground(getResources().getDrawable(R.drawable.button_bg_limitdiscount));
            applyText.setFocusable(true);
            applyText.setOnClickListener(this);
        } else {
            limitSettimetextitletv.setVisibility(View.VISIBLE);
            limitSettimetext.setVisibility(View.GONE);
            limitSettimetextitletv.setText("报名结束");
            countericon.setText("");
            applyText.setText("报名结束");
            applyText.setBackground(getResources().getDrawable(R.drawable.button_bg_nolimitdiscount));
            applyText.setFocusable(false);
            applyText.setOnClickListener(null);
        }
        mBenefitAttrData.clear();
        mBenefitAttrData.addAll(data.benefitAttr);
        gridviewAdapter.notifyDataSetChanged();

        if (TextUtil.isEmpty(data.content)) {
            mWebviewLayout.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
        } else {
            mWebviewLayout.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadData(WebviewUtil.getHtmlData(data.content), "text/html; charset=utf-8",
                    "utf-8");
        }

        String type = data.type;
        if (type.equals("sale")) {
            mTitleText.setText(data.factoryName + "特价车" + " --展期优惠");
            mLimitGotodetailll.setVisibility(View.GONE);
            mBrandBenefitLayout.setVisibility(View.GONE);
            mLimitTimeLayout.setVisibility(View.VISIBLE);
            mOfferName.setText(data.sale.title);
            mOfferPrice.setText("￥" + data.sale.price + "万元");
            if (TextUtil.isEmpty(data.sale.config)) {
                configuration.setVisibility(View.GONE);
            } else {
                configuration.setVisibility(View.VISIBLE);
                configuration.setText("配置：" + data.sale.config);
            }

            mOfferGuideprice.setText("(指导价" + data.sale.guidePrice + "万元)");
            mOfferGuideprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            setTextColors(mOfferNum, "限售 " + data.sale.saleNum + " 台", 2, 4 + data.sale.saleNum.length(), R.color.text_color20);
            int rateType = data.sale.rateType;
            switch (rateType) {
                case 1:
                    mOfferType.setText("最高优惠" + data.sale.rateValue + "元");
                    mOfferType.setTextColor(getResources().getColor(R.color.text_color36));
                    break;
                case 2:
                    mOfferType.setText(data.sale.rateValue);
                    mOfferType.setTextColor(getResources().getColor(R.color.text_color2));
                    break;
                case 3:
                    mOfferType.setText(data.sale.rateValue + "折");
                    mOfferType.setTextColor(getResources().getColor(R.color.limit_yellow));
                    break;
                case 4:
                    mOfferType.setText("↓降" + data.sale.rateValue + "元");
                    mOfferType.setTextColor(getResources().getColor(R.color.text_color10));
                    break;
            }
            mlimitTitle.setText(data.title);
            mSpecialOfferimage.setVisibility(View.VISIBLE);
        } else {
            mTitleText.setText(data.factoryName + " --展期优惠");
            mLimitGotodetailll.setVisibility(View.VISIBLE);
            mBrandBenefitLayout.setVisibility(View.VISIBLE);
            mLimitTimeLayout.setVisibility(View.GONE);
            mSeriesData.clear();
            mSeriesData.addAll(data.product);
            mSeriesAdapter.notifyDataSetChanged();
            mSpecialOfferimage.setVisibility(View.GONE);
            if (mSeriesData == null || mSeriesData.size() == 0) {
                mlimitTitle.setText(data.title);
                mBrandBenefitLayout.setVisibility(View.GONE);
            } else {
                mlimitTitle.setText(mSeriesData.get(0).title);
                mBrandBenefitLayout.setVisibility(View.VISIBLE);
            }
        }

        mExhibitionname.setText(data.event.name);
        mExhibitiontime.setText(data.event.startTime + " - " + data.event.endTime);
        mExhibitionsite.setText(data.event.address);
        mBrandCIcon.setImageURI(Uri.parse(URLUtil.builderImgUrl(data.event.factoryLogo, 144, 144)));
        mBrandname.setText(data.event.factoryName);
        mExhibitionhall.setText("(" + data.event.hallName + ")");

        mSellDataPortion.clear();
        mSellDataAll.clear();
        mShowSellData.clear();
        mSellDataAll.addAll(data.exhibitor);
        if (data.exhibitor.size() > 5) {
            for (int i = 0; i < 5; i++) {
                mSellDataPortion.add(data.exhibitor.get(i));
            }
            mUnwindLayout.setVisibility(View.VISIBLE);
        } else {
            mSellDataPortion.addAll(data.exhibitor);
            mUnwindLayout.setVisibility(View.GONE);
        }
        mShowSellData.addAll(mSellDataPortion);
        mSellAdapter.notifyDataSetChanged();
        mSellNum.setText("活动参与经销商名录（" + mSellDataAll.size() + "）");

        setTextColors(mYapplied, "已报名 " + data.number + " 人", 3, 5 + data.number.length(), R.color.text_color20);
        regulation.loadData(WebviewUtil.getHtmlData(data.statement), "text/html; charset=utf-8",
                "utf-8");
    }


    @Override
    public void showStoreLimitList(LimitDiscountNewDetailsLimitModel data) {

    }

    @Override
    public void showLimitListError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                callPhone();
            }
        } else {
            callPhone();
        }
    }

    /**
     * 请求打电话的权限码
     */
    public static final int REQUEST_CODE_ASK_CALL_PHONE = 100;

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    // Permission Denied
                    Toast.makeText(mContext, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 拨号方法
     */
    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        if (!TextUtil.isEmpty(mTel)) {
            intent.setData(Uri.parse("tel:" + mTel));
            startActivity(intent);
        } else {
            CommonUtils.showToast(mContext, "电话号码为空");
        }
    }

    private void share() {

        if (mLimitDiscountNewDetailsModel == null) {
            CommonUtils.showToast(mContext, "数据正在加载中...");
            return;
        }
        final String title = "";
        final String description = "";
        final String cover = Constants.ImageUrls.IMG_DEF_URL;

        openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        OpenShareUtil.sendWXMoments(mContext, mGlobalContext.getMsgApi(), title,
                                description);
                        break;
                    case 1:
                        OpenShareUtil.sendWX(mContext, mGlobalContext.getMsgApi(), title,
                                description);
                        break;

                    case 2:
                        mWbHandler = OpenShareUtil.shareSina(LimitDiscountNewDetailsOddsActivity.this, title,
                                description);
                        break;

                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(mGlobalContext.getTencent(),
                                LimitDiscountNewDetailsOddsActivity.this, mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(mGlobalContext.getTencent(),
                                LimitDiscountNewDetailsOddsActivity.this, mBaseUIListener, title, cover, description);
                        break;

                }
            }
        });
        openShareWindow.show(LimitDiscountNewDetailsOddsActivity.this);
    }

}
