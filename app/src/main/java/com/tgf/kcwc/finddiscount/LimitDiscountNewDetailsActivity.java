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
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.LimitDiscountNewDetailsLimitModel;
import com.tgf.kcwc.mvp.model.LimitDiscountNewDetailsModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.LimitDiscountDetailsPresenter;
import com.tgf.kcwc.mvp.view.LimitDiscountDetailsView;
import com.tgf.kcwc.see.StoreShowCarDetailActivity;
import com.tgf.kcwc.seecar.CommitCarOrderActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.ConvertUtil;
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

public class LimitDiscountNewDetailsActivity extends BaseActivity implements LimitDiscountDetailsView {


    LimitDiscountDetailsPresenter mLimitDiscountDetailsPresenter;
    private TextView mTitleText;                    //标题文本
    private TextView mtitle;                    //标题文本
    private TextView regulation;                    //规则
    private SimpleDraweeView mICon;
    private SimpleDraweeView mBrandIcon;
    CountdownView limitSettimetext;
    private TextView countericon;                    //状态
    TextView limitSettimetextitletv;//未开始
    WebView mWebView;//介绍
    LinearLayout mWebviewLayout;//介绍布局
    TextView maxRateValue;//最高价格优惠
    LinearLayout mExamineAnnouncement;//电话布局
    LinearLayout mOromptlyTransmit;//询价
    TextView mSellNum;//参展商个数

    private WrapAdapter<LimitDiscountNewDetailsLimitModel.BenefitAttr> gridviewAdapter;
    private List<LimitDiscountNewDetailsLimitModel.BenefitAttr> mBenefitAttrData = new ArrayList<>(); //评价
    private MyGridView mDescribe;

    private MyListView mDiscountsListview;
    private WrapAdapter<LimitDiscountNewDetailsLimitModel.Product> mDiscountsAdapter;
    private List<LimitDiscountNewDetailsLimitModel.Product> mDiscountsData = new ArrayList<>();

    private List<LimitDiscountNewDetailsLimitModel.Org> mShowSellData = new ArrayList<>(); //显示厂商
    private List<LimitDiscountNewDetailsLimitModel.Org> mSellData = new ArrayList<>(); //部分厂商
    private List<LimitDiscountNewDetailsLimitModel.Org> mAllSellData = new ArrayList<>(); //全部厂商
    private MyListView mSellListview;
    private WrapAdapter<LimitDiscountNewDetailsLimitModel.Org> mSellAdapter;

    private String ID = "";

    private String mTel = "";
    KPlayCarApp mKPlayCarApp;
    LimitDiscountNewDetailsLimitModel mLimitDiscountNewDetailsLimitModel = null;

    LinearLayout mUnwindLayout; //下拉
    ImageView mUnwindimag; //下拉
    TextView mUnwindtext; //下拉文字
    private boolean isPull = false;

    private String[] navValues = null;
    List<MorePopupwindowBean> dataList = new ArrayList<>();
    private OpenShareWindow openShareWindow;
    private WbShareHandler mWbHandler;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
        text.setText("限时优惠详情");
        function.setImageResource(R.drawable.global_nav_n);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MorePopupWindow morePopupWindow = new MorePopupWindow(LimitDiscountNewDetailsActivity.this,
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
        setContentView(R.layout.activity_limitdiscountdetails);
        navValues = mRes.getStringArray(R.array.global_nav_values5);
        Map<String, Integer> titleActionIconMap = DataUtil.getTitleActionIcon3();
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
        mDiscountsListview = (MyListView) findViewById(R.id.discountslistview);
        mSellListview = (MyListView) findViewById(R.id.selllistview);
        mTitleText = (TextView) findViewById(R.id.title_bar_text);
        mICon = (SimpleDraweeView) findViewById(R.id.icon);
        mtitle = (TextView) findViewById(R.id.title);
        mBrandIcon = (SimpleDraweeView) findViewById(R.id.brandicon);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebviewLayout = (LinearLayout) findViewById(R.id.webviewlayout);
        limitSettimetext = (CountdownView) findViewById(R.id.limit_settimetext);
        countericon = (TextView) findViewById(R.id.countericon);
        regulation = (TextView) findViewById(R.id.regulation);
        maxRateValue = (TextView) findViewById(R.id.max_rate_value);
        mExamineAnnouncement = (LinearLayout) findViewById(R.id.examine_announcement);
        limitSettimetextitletv = (TextView) findViewById(R.id.limit_settimetextitletv);
        mOromptlyTransmit = (LinearLayout) findViewById(R.id.promptly_transmit);
        mSellNum = (TextView) findViewById(R.id.sellnum);
        mUnwindLayout = (LinearLayout) findViewById(R.id.unwindlayout);
        mUnwindimag = (ImageView) findViewById(R.id.unwindimag);
        mUnwindtext = (TextView) findViewById(R.id.unwindtext);
        mLimitDiscountDetailsPresenter = new LimitDiscountDetailsPresenter();
        mLimitDiscountDetailsPresenter.attachView(this);
        setAdapter();
        mLimitDiscountDetailsPresenter.getStoreDetail(ID, ConvertUtil.convertToDouble(mKPlayCarApp.longitude, 0.00), ConvertUtil.convertToDouble(mKPlayCarApp.latitude, 0.00));
        mExamineAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        mOromptlyTransmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarBean carBeanCommite = new CarBean();
                carBeanCommite.seriesId = mLimitDiscountNewDetailsLimitModel.seriesId;
                carBeanCommite.factoryName = mLimitDiscountNewDetailsLimitModel.factoryName;
                carBeanCommite.seriesName = mLimitDiscountNewDetailsLimitModel.seriesName;
                Intent toCommitIntent = new Intent(getContext(), CommitCarOrderActivity.class);
                toCommitIntent.putExtra(Constants.IntentParams.DATA, carBeanCommite);
                startActivity(toCommitIntent);
            }
        });

        mUnwindLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.unwindlayout:
                if (isPull) {
                    mShowSellData.clear();
                    mShowSellData.addAll(mAllSellData);
                    mSellAdapter.notifyDataSetChanged();
                    mUnwindimag.setImageResource(R.drawable.btn_up);
                    mUnwindtext.setText("收起更多适用经销商");
                    isPull = false;
                } else {
                    mShowSellData.clear();
                    mShowSellData.addAll(mSellData);
                    mSellAdapter.notifyDataSetChanged();
                    mUnwindimag.setImageResource(R.drawable.btn_xiala);
                    mUnwindtext.setText("更多适用经销商");
                    isPull = true;
                }

                break;
        }
    }

    public void setAdapter() {

        gridviewAdapter = new WrapAdapter<LimitDiscountNewDetailsLimitModel.BenefitAttr>(mContext, R.layout.listitem_newlimitdiscount_item, mBenefitAttrData) {
            @Override
            public void convert(ViewHolder helper, LimitDiscountNewDetailsLimitModel.BenefitAttr item) {
                TextView name = helper.getView(R.id.name);
                name.setText(item.value);
            }
        };
        mDescribe.setAdapter(gridviewAdapter);

        mDiscountsAdapter = new WrapAdapter<LimitDiscountNewDetailsLimitModel.Product>(mContext, R.layout.limitdiscountdetails_discountsitem, mDiscountsData) {
            @Override
            public void convert(ViewHolder helper, final LimitDiscountNewDetailsLimitModel.Product item) {
                TextView name = helper.getView(R.id.name);
                TextView price = helper.getView(R.id.price);
                TextView guideprice = helper.getView(R.id.guideprice);
                TextView type = helper.getView(R.id.type);
                TextView enquiry = helper.getView(R.id.enquiry);
                ImageView limitOrgmore = helper.getView(R.id.limit_orgmore);
                name.setText(item.seriesName + item.carName);
                price.setText(item.price + "万元");
                guideprice.setText("(指导价" + item.guidePrice + "万元)");
                guideprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                switch (item.rateType) {
                    case 1:
                        type.setText("最高优惠" + item.rateValue + "元 ");
                        type.setTextColor(getResources().getColor(R.color.text_color36));
                        break;
                    case 2:
                        type.setText(item.rateValue);
                        type.setTextColor(getResources().getColor(R.color.text_color2));
                        break;
                    case 3:
                        type.setText(item.rateValue + "折");
                        type.setTextColor(getResources().getColor(R.color.limit_yellow));
                        break;
                    case 4:
                        type.setText("↓降" + item.rateValue + "元");
                        type.setTextColor(getResources().getColor(R.color.text_color10));
                        break;
                }

                enquiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toIntent = new Intent(mContext, CommitCarOrderActivity.class);
                        CarBean carBeanTo = new CarBean();
                        carBeanTo.id = item.carId;
                        carBeanTo.factoryName = mLimitDiscountNewDetailsLimitModel.factoryName;
                        carBeanTo.seriesName = item.seriesName;
                        carBeanTo.name = item.carName;
                        toIntent.putExtra(Constants.IntentParams.DATA, carBeanTo);
                        startActivity(toIntent);
                    }
                });

                limitOrgmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.DATA, item.isSeries); //是否是车系
                        args.put(Constants.IntentParams.CAR_ID, item.carId); //车型ID
                        // args.put(Constants.IntentParams.DATA2, mLimitDiscountNewDetailsLimitModel.factoryId);//机构ID
                        args.put(Constants.IntentParams.DATA2, mLimitDiscountNewDetailsLimitModel.org.id);//机构ID
                        args.put(Constants.IntentParams.DATA3, mLimitDiscountNewDetailsLimitModel.factoryName + mLimitDiscountNewDetailsLimitModel.seriesName + item.carName); //车型名字
                        CommonUtils.startNewActivity(mContext, args, StoreShowCarDetailActivity.class);
                    }
                });
            }
        };
        mDiscountsListview.setAdapter(mDiscountsAdapter);


        mSellAdapter = new WrapAdapter<LimitDiscountNewDetailsLimitModel.Org>(mContext, R.layout.limitdiscountdetails_sellitem, mShowSellData) {
            @Override
            public void convert(ViewHolder helper, final LimitDiscountNewDetailsLimitModel.Org item) {
                TextView mSponsorName = helper.getView(R.id.sponsor_name);
                TextView address = helper.getView(R.id.address);
                ImageView phone = helper.getView(R.id.phone);
                TextView distance = helper.getView(R.id.distance);
                mSponsorName.setText(item.fullName);
                address.setText(item.address);
                distance.setVisibility(View.VISIBLE);
                distance.setText(item.distance + "km");
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
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public void showEveLimitList(LimitDiscountNewDetailsModel data) {

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
    public void showStoreLimitList(LimitDiscountNewDetailsLimitModel data) {
        mLimitDiscountNewDetailsLimitModel = data;

        mTitleText.setText(data.factoryName + " --限时优惠");
        mtitle.setText(data.title);
        mICon.setImageURI(Uri.parse(URLUtil.builderImgUrl(data.cover, 540, 270)));
        mBrandIcon.setImageURI(Uri.parse(URLUtil.builderImgUrl(data.factoryLogo, 144, 144)));

        int benefitStatus = data.benefitStatus;
        if (benefitStatus == 1) {
            limitSettimetextitletv.setVisibility(View.GONE);
            limitSettimetext.setVisibility(View.VISIBLE);
            countericon.setText("距活动开始：");
            long time = DateFormatUtil.getTime(data.beginTime) - DateFormatUtil.getTime(data.currentTime);
            limitSettimetext.start(time);
            modificationTime(limitSettimetext, time);
        } else if (benefitStatus == 2) {
            limitSettimetextitletv.setVisibility(View.GONE);
            limitSettimetext.setVisibility(View.VISIBLE);
            countericon.setText("剩余：");
            long time = DateFormatUtil.getTime(data.endTime) - DateFormatUtil.getTime(data.currentTime);
            limitSettimetext.start(time);
            modificationTime(limitSettimetext, time);
        } else {
            limitSettimetextitletv.setVisibility(View.VISIBLE);
            limitSettimetext.setVisibility(View.GONE);
            limitSettimetextitletv.setText("已结束");
            countericon.setText("");
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

        int rateType = data.rateType;

        switch (rateType) {
            case 1:
                maxRateValue.setText("( 最高综合优惠" + data.maxRateValue + "元 )");
                maxRateValue.setTextColor(getResources().getColor(R.color.text_color36));
                break;
            case 2:
                maxRateValue.setText("( " + data.maxRateValue + " )");
                maxRateValue.setTextColor(getResources().getColor(R.color.text_color2));
                break;
            case 3:
                maxRateValue.setText("( 最高现金折扣" + data.maxRateValue + "折 )");
                maxRateValue.setTextColor(getResources().getColor(R.color.limit_yellow));
                break;
            case 4:
                maxRateValue.setText("( 最高现金优惠" + data.maxRateValue + "元 )");
                maxRateValue.setTextColor(getResources().getColor(R.color.text_color10));
                break;
        }
        regulation.setText(data.statement);

        mDiscountsData.clear();
        mDiscountsData.addAll(data.product);
        mDiscountsAdapter.notifyDataSetChanged();

        mAllSellData.clear();
        mSellData.clear();
        mShowSellData.clear();

        mAllSellData.addAll(data.orgList);
        if (mAllSellData.size() > 5) {
            for (int i = 0; i < 5; i++) {
                mSellData.add(mAllSellData.get(i));
            }
            mUnwindLayout.setVisibility(View.VISIBLE);
        } else {
            mSellData.addAll(data.orgList);
            mUnwindLayout.setVisibility(View.GONE);
        }
        mShowSellData.addAll(mSellData);
        mSellAdapter.notifyDataSetChanged();
/*        if (data.isMore == 1) {
            mSellNum.setVisibility(View.GONE);
        } else {
        }*/
        mSellNum.setText("活动参与经销商名录（" + data.orgCount + "）");
        if (data.org != null) {
            mTel = data.org.tel;
        }

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

        if (mLimitDiscountNewDetailsLimitModel == null) {
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
                        mWbHandler = OpenShareUtil.shareSina(LimitDiscountNewDetailsActivity.this, title,
                                description);
                        break;

                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(mGlobalContext.getTencent(),
                                LimitDiscountNewDetailsActivity.this, mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(mGlobalContext.getTencent(),
                                LimitDiscountNewDetailsActivity.this, mBaseUIListener, title, cover, description);
                        break;

                }
            }
        });
        openShareWindow.show(LimitDiscountNewDetailsActivity.this);
    }

}
