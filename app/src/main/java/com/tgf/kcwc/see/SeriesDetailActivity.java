package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.SeriesDetailModel;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.seecar.CommitCarOrderActivity;
import com.tgf.kcwc.share.BaseUIListener;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/3/30 10:50
 * E-mail：fishloveqin@gmail.com
 * <p>
 * 车系详情
 */

public class SeriesDetailActivity extends BaseActivity implements CarDataView<SeriesDetailModel> {

    protected ImageButton mTitleBarBack;
    protected ImageButton mTitleShareBtn;
    protected ImageButton mTitleFavoriteBtn;
    protected ImageButton mTitlePkBtn;
    protected RelativeLayout mTitleBar;
    protected TextView mPicNums;
    protected TextView mCarName;
    protected TextView mPrice;
    protected TextView mCarConfigTv;
    protected RelativeLayout mCarDescLayout;
    protected TextView mBtnCarConfig;
    protected TextView mBtnCoupon;
    protected TextView mBtnOwnSale;
    protected TextView mBtnRep;
    protected TextView mNowSaleBtn;
    protected ImageView mBtmLine1;
    protected RelativeLayout mNowSaleLayout;
    protected TextView mPreSaleTitle;
    protected ImageView mBtmLine2;
    protected RelativeLayout mPreSaleLayout;
    protected RelativeLayout mCarSaleLayout;
    protected ListView mNowSaleList;
    protected ListView mPreSaleList;
    protected Button mBuyBtn;
    protected TextView stopSaleTitle;
    protected ImageView btmLine3;
    protected RelativeLayout stopSale;
    protected RelativeLayout shotcutMenu;
    protected ListView stopSaleList;
    private TextView mPKNumsTv;
    private CarDataPresenter mPresenter;
    private int mSeriesId;
    private WrapAdapter<CarBean> mNowCarsAdapter;
    private WrapAdapter<CarBean> mPreCarsAdapter;
    private WrapAdapter<CarBean> mStopCarsAdapter;
    private List<CarBean> mNowCars = new ArrayList<CarBean>();

    private List<CarBean> mPreCars = new ArrayList<CarBean>();
    private List<CarBean> mStopCars = new ArrayList<CarBean>();
    private SimpleDraweeView mCoverImg;
    private String mBrandName;

    private UserDataPresenter mAddFavoritePresenter;
    private UserDataPresenter mCancelFavoritePresenter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    private ArrayList<CarBean> mPKCars = null;//车型对比集合

    private int mPKNums;

    private void showPkNums() {
        mPKNums = mPKCars.size();
        if (mPKNums == 0) {
            mPKNumsTv.setVisibility(View.GONE);
        } else {
            mPKNumsTv.setVisibility(View.VISIBLE);
        }
        mPKNumsTv.setText(mPKNums + "");

    }

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
//            mTitleFavoriteBtn.setBackground(mRes.getDrawable(R.drawable.favorite_icon_n));
            mTitleFavoriteBtn.setImageResource(R.drawable.favorite_icon_n);
            CommonUtils.showToast(mContext, "已取消收藏");

        }
    };

    @Override
    protected void setUpViews() {
        mBaseUIListener = new BaseUIListener(mContext);
        initView();
        mPresenter = new CarDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getSeriesDetail(mSeriesId + "", IOUtils.getToken(mContext));
        mAddFavoritePresenter = new UserDataPresenter();
        mAddFavoritePresenter.attachView(mAddFavoriteView);
        mCancelFavoritePresenter = new UserDataPresenter();
        mCancelFavoritePresenter.attachView(mCancelFavoriteView);
        mNowCarsAdapter = builderAdapter(mNowCars);
        mPreCarsAdapter = builderAdapter(mPreCars);
        mNowSaleList.setAdapter(mNowCarsAdapter);
        mPreSaleList.setAdapter(mPreCarsAdapter);
        mStopCarsAdapter = builderAdapter(mStopCars);
        stopSaleList.setAdapter(mStopCarsAdapter);
    }

    private WrapAdapter<CarBean> builderAdapter(List<CarBean> datas) {


        final WrapAdapter<CarBean> beanWrapAdapter = new WrapAdapter<CarBean>(mContext,
                R.layout.car_sale_list_item, datas) {

            protected TextView goStoreBtn;
            protected TextView btnCarConfig;
            protected TextView carDetailBtn;

            @Override
            public void convert(final ViewHolder helper, final CarBean item) {

                helper.getView(R.id.baseInfoLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id);
                        CommonUtils.startNewActivity(mContext, args, CarDetailActivity.class);
                    }
                });
                carDetailBtn = helper.getView(R.id.carDetailBtn);
                btnCarConfig = helper.getView(R.id.btnCarConfig);
                goStoreBtn = helper.getView(R.id.goStoreBtn);
                carDetailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id);
                        CommonUtils.startNewActivity(mContext, args, CarDetailActivity.class);
                    }
                });

                if (item.isAdded) {
                    btnCarConfig.setText("已对比");
                    btnCarConfig.setSelected(true);
                    btnCarConfig.setTextColor(mRes.getColor(R.color.text_color16));

                } else {
                    btnCarConfig.setTextColor(mRes.getColor(R.color.text_color12));
                    btnCarConfig.setSelected(false);
                    btnCarConfig.setText("添加对比");

                }

                btnCarConfig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int size = mPKCars.size();
                        if (size >= Constants.PK_MAX) {

                            CommonUtils.showToast(mContext, "亲，最多只能添加" + Constants.PK_MAX + "辆车进行PK");
                            return;
                        } else {


                            boolean isExist = false;
                            for (CarBean carItem : mPKCars) {

                                if (carItem.id == item.id) {
                                    isExist = true;
                                    break;
                                }

                            }


                            if (!isExist) {

                                mPKCars.add(item);
                                mPKNums++;
                                showPkNums();
                                item.isAdded = true;
                                CommonUtils.showToast(mContext, "已成功添加对比");
                                notifyDataSetChanged();
                            } else {

                                removePK(item);
                                mPKCars.remove(item);
                                item.isAdded = false;
                                mPKNums--;
                                showPkNums();
                                CommonUtils.showToast(mContext, "已移出对比");
                                notifyDataSetChanged();
                            }
                        }


                    }
                });

                if (item.isOrganizationCar == 1) {
                    goStoreBtn.setText("店内看");
                } else {
                    goStoreBtn.setText("我要看");
                }
                goStoreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isOrganizationCar == 1) {
                            KPlayCarApp.putValue(Constants.IntentParams.INDEX, 2);
                            KPlayCarApp.putValue(Constants.IntentParams.CAR_ID, item.id + "");
//                            Intent intent = new Intent();
//                            intent.putExtra(Constants.IntentParams.INDEX, 1);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.setClass(mContext, MainActivity.class);
//                            startActivity(intent);
                            CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                        } else {
                            CarBean carBeanCommite = new CarBean();
                            carBeanCommite.factoryName = mBrandName;
                            carBeanCommite.seriesName = mName;
                            carBeanCommite.id = item.id;
                            carBeanCommite.name = item.name;
                            Intent toCommitIntent = new Intent(getContext(), CommitCarOrderActivity.class);
                            toCommitIntent.putExtra(Constants.IntentParams.DATA, carBeanCommite);
                            startActivity(toCommitIntent);


//                            HashMap<String, Serializable> args = new HashMap<String, Serializable>();
//
//                            args = new HashMap<String, Serializable>();
//                            args.put(Constants.IntentParams.ID, item.id);
//                            args.put(Constants.IntentParams.INTENT_TYPE, 0);
//                            args.put(Constants.IntentParams.DATA, mBrandName + " " + mName);
//                            CommonUtils.startNewActivity(mContext, args,
//                                    CommitCarOrderActivity.class);
                        }

                    }
                });
                helper.setText(R.id.carName, item.name);
                TextView priceTv = helper.getView(R.id.price);
                String price = String.format(getString(R.string.price_desc), item.guidePrice + "");
                CommonUtils.customDisplayText4(mRes, priceTv, price, R.color.text_selected);

            }
        };

        return beanWrapAdapter;
    }

    private void removePK(CarBean item) {

        CarBean tmp = null;
        int index = -1;
        int size = mPKCars.size();
        for (int i = 0; i < size; i++) {
            CarBean cb = mPKCars.get(i);
            if (cb.id == item.id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            mPKCars.remove(index);
        }

    }

    private void filterExistPKData(List<CarBean> datas) {

        for (CarBean c : mPKCars) {


            for (CarBean cb : datas) {
                Logger.d("c.id:" + c.id + ",cb.id:" + cb.id);
                if (c.id == cb.id) {
                    cb.isAdded = true;
                }
            }

        }
    }


    UserDataView<Object> mAddFavoriteView = new UserDataView<Object>() {
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
//            mTitleFavoriteBtn.setSelected(true);
            mTitleFavoriteBtn.setImageResource(R.drawable.btn_collection_s);
            CommonUtils.showToast(mContext, "收藏成功");
        }
    };
    private String mName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mSeriesId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mName = intent.getStringExtra(Constants.IntentParams.NAME);
        isTitleBar = false;

        mPKCars = KPlayCarApp.getValue(Constants.KeyParams.PK_DATAS);

        if (mPKCars == null) {

            mPKCars = new ArrayList<CarBean>();
            KPlayCarApp.putValue(Constants.KeyParams.PK_DATAS, mPKCars);
        }
        super.setContentView(R.layout.activity_series_detail);
        openShareWindow = new OpenShareWindow(this);
    }

    private OpenShareWindow openShareWindow;//分享弹框

    @Override
    protected void onResume() {
        super.onResume();


        showPkNums();


    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }


    private ObservableScrollView.ScrollViewListener mScrollListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                    int oldy) {

            int topHeight = BitmapUtils.dp2px(mContext, 132);
/*            if (y >= topHeight) {
                findViewById(R.id.titleBar).setBackgroundResource(R.color.style_bg1);
            } else {
                findViewById(R.id.titleBar).setBackgroundResource(R.drawable.shape_titlebar_bg);

            }*/
            if (y <= 0) {   //设置标题的背景颜色
                // textView.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
                findViewById(R.id.titleBar).setBackgroundColor(Color.argb((int) 0, 54, 169, 92));
            } else if (y > 0 && y <= topHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                float scale = (float) y / topHeight;
                float alpha = (255 * scale);
                //textView.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                // textView.setBackgroundColor(Color.argb((int) alpha, 144, 151, 166));
                findViewById(R.id.titleBar).setBackgroundColor(Color.argb((int) alpha, 54, 169, 92));
            } else {    //滑动到banner下面设置普通颜色
                // textView.setBackgroundColor(Color.argb((int) 255, 144, 151, 166));
                findViewById(R.id.titleBar).setBackgroundColor(Color.argb((int) 255, 54, 169, 92));
            }
            Logger.d("x:" + x + ",y:" + y + ",oldX:" + oldx + "oldy:" + oldy);
        }
    };

    @Override
    public Context getContext() {
        return mContext;
    }

    private ObservableScrollView mScrollView;

    private void initView() {
        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        mScrollView.setScrollViewListener(mScrollListener);
        mTitleBarBack = (ImageButton) findViewById(R.id.title_bar_back);
        backEvent(mTitleBarBack);
        mTitleShareBtn = (ImageButton) findViewById(R.id.title_share_btn);
        mTitleShareBtn.setOnClickListener(this);
        mTitleFavoriteBtn = (ImageButton) findViewById(R.id.title_favorite_btn);
        mTitleFavoriteBtn.setOnClickListener(this);
        mTitlePkBtn = (ImageButton) findViewById(R.id.title_pk_btn);
        mTitlePkBtn.setOnClickListener(this);
        mPKNumsTv = (TextView) findViewById(R.id.pkNums);
        mTitleBar = (RelativeLayout) findViewById(R.id.titleBar);
        mPicNums = (TextView) findViewById(R.id.tv_picnum);
        mCarName = (TextView) findViewById(R.id.carName);
        mPrice = (TextView) findViewById(R.id.price);
        mCarConfigTv = (TextView) findViewById(R.id.carConfig);
        mCarDescLayout = (RelativeLayout) findViewById(R.id.carDesc);
        mBtnCarConfig = (TextView) findViewById(R.id.btnCarConfig);
        mBtnCoupon = (TextView) findViewById(R.id.btnCoupon);
        mBtnOwnSale = (TextView) findViewById(R.id.btnOwnSale);
        mBtnRep = (TextView) findViewById(R.id.btnRep);
        mNowSaleBtn = (TextView) findViewById(R.id.nowSaleBtn);
        mBtmLine1 = (ImageView) findViewById(R.id.btmLine1);
        mNowSaleLayout = (RelativeLayout) findViewById(R.id.nowSale);
        mPreSaleTitle = (TextView) findViewById(R.id.preSaleTitle);
        mBtmLine2 = (ImageView) findViewById(R.id.btmLine2);
        mPreSaleLayout = (RelativeLayout) findViewById(R.id.preSale);
        mCarSaleLayout = (RelativeLayout) findViewById(R.id.carSaleLayout);
        mNowSaleList = (ListView) findViewById(R.id.nowSaleList);
        mPreSaleList = (ListView) findViewById(R.id.preSaleList);
        mBuyBtn = (Button) findViewById(R.id.commitBtn);
        mCoverImg = (SimpleDraweeView) findViewById(R.id.bigHeaderImg);
        mNowSaleLayout.setOnClickListener(this);
        mBuyBtn.setOnClickListener(this);
        mPreSaleLayout.setOnClickListener(this);
        mBtnCarConfig.setOnClickListener(this);
        mBtnRep.setOnClickListener(this);
        mBtnCoupon.setOnClickListener(this);
        mBtnOwnSale.setOnClickListener(this);
        mCoverImg.setOnClickListener(this);
        stopSaleTitle = (TextView) findViewById(R.id.stopSaleTitle);
        btmLine3 = (ImageView) findViewById(R.id.btmLine3);
        stopSale = (RelativeLayout) findViewById(R.id.stopSale);
        stopSale.setOnClickListener(this);
        shotcutMenu = (RelativeLayout) findViewById(R.id.shotcutMenu);
        stopSaleList = (ListView) findViewById(R.id.stopSaleList);
    }

    private int isFav;
    private CarBean mCarBean;

    private void initHeaderData(CarBean carBean) {
        mCarBean = carBean;
        isFav = carBean.isFav;
        if (isFav == 1) {
//            mTitleFavoriteBtn.setBackground(mRes.getDrawable(R.drawable.btn_collection_s));
            mTitleFavoriteBtn.setImageResource(R.drawable.btn_collection_s);
        } else {

            mTitleFavoriteBtn.setSelected(false);
        }
        mBrandName = carBean.brandName;

        mCoverImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(carBean.cover, 540, 270)));
        mCarName.setText(carBean.name + "");
        String price = String.format(getString(R.string.price_desc), carBean.price + "");
        CommonUtils.customDisplayText4(mRes, mPrice, price, R.color.text_selected);
        // mPrice.setText(price);
        mCarConfigTv.setText(carBean.carLevel + "");

        mPicNums.setText(carBean.imgNums + "张");
    }

    private SeriesDetailModel mModel;

    @Override
    public void showData(SeriesDetailModel seriesDetailModel) {

        mModel = seriesDetailModel;
        initHeaderData(seriesDetailModel.headerInfo);

        List<CarBean> lists = seriesDetailModel.cars;
        mPreCars.clear();
        mNowCars.clear();
        for (CarBean carBean : lists) {
            if (carBean.salesStatus == 1) {
                mNowCars.add(carBean);
            } else if (carBean.salesStatus == 2) {
                mPreCars.add(carBean);
            } else if (carBean.salesStatus == 3) {

                mStopCars.add(carBean);
            }
        }

        refreshUI();
    }

    private WbShareHandler mWbHandler;

    private String getCarsIds() {
        if (mModel != null && mModel.cars.size() != 0) {

            List<CarBean> datas = mModel.cars;
            StringBuffer stringBuffer = new StringBuffer();
            for (CarBean carBean : datas) {
                int status = carBean.salesStatus;
                if (status == 1 || status == 2) {

                    stringBuffer.append(carBean.id).append(",");
                }
            }

            String ids = stringBuffer.toString();
            int length = ids.length();
            if (length > 0) {
                ids = ids.substring(0, length - 1);
            }
            return ids;
        }

        return "";
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.nowSale:

                mNowSaleBtn.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                mBtmLine1.setVisibility(View.VISIBLE);

                mPreSaleTitle.setTextColor(mRes.getColor(R.color.text_color));
                mBtmLine2.setVisibility(View.INVISIBLE);
                stopSaleTitle.setTextColor(mRes.getColor(R.color.text_color));
                btmLine3.setVisibility(View.INVISIBLE);
                loadCarSalesData(1);
                break;

            case R.id.preSale:

                mPreSaleTitle.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                mBtmLine2.setVisibility(View.VISIBLE);

                mNowSaleBtn.setTextColor(mRes.getColor(R.color.text_color));
                mBtmLine1.setVisibility(View.INVISIBLE);
                stopSaleTitle.setTextColor(mRes.getColor(R.color.text_color));
                btmLine3.setVisibility(View.INVISIBLE);
                loadCarSalesData(2);
                break;

            case R.id.stopSale:


                stopSaleTitle.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                btmLine3.setVisibility(View.VISIBLE);

                mNowSaleBtn.setTextColor(mRes.getColor(R.color.text_color));
                mBtmLine1.setVisibility(View.INVISIBLE);
                mPreSaleTitle.setTextColor(mRes.getColor(R.color.text_color));
                mBtmLine2.setVisibility(View.INVISIBLE);
                loadCarSalesData(3);
                break;
            case R.id.btnCarConfig:
                String ids = getCarsIds();
                if (TextUtils.isEmpty(ids)) {
                    CommonUtils.showToast(mContext, "当前车系暂无可对比车型！");
                    return;
                }
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.IDS, ids);
                CommonUtils.startNewActivity(mContext, args, OwnerContrastGoListActivity.class);
                break;
            case R.id.btnCoupon:
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, Constants.IntentParams.SALE_DISCOUNTS);

                if (mModel != null) {
                    KPlayCarApp.putValue(Constants.IntentParams.SERIES_ID, mModel.headerInfo.brandId + "");
                    KPlayCarApp.putValue(Constants.IntentParams.SERIES_NAME, mModel.headerInfo.brandName);
                    KPlayCarApp.putValue(Constants.IntentParams.DISCOUNTS_TYPE, Constants.IntentParams.DISCOUNTS_TIME_LIMIT);
                }

                CommonUtils.gotoMainPage(mContext, Constants.Navigation.SEE_INDEX);
                break;
            case R.id.btnOwnSale:

                KPlayCarApp.putValue(Constants.IntentParams.INDEX, Constants.IntentParams.SALE_INDEX);
                KPlayCarApp.putValue(Constants.IntentParams.SERIES_ID, mSeriesId + "");

                CommonUtils.gotoMainPage(mContext, Constants.Navigation.SEE_INDEX);

                break;
            case R.id.btnRep:
                break;

            case R.id.title_pk_btn:

                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.DATA, mPKCars);
                intent.setClass(mContext, OwnerSubActivity.class);
                startActivity(intent);

                break;
            case R.id.title_favorite_btn:

                /* *
                
                
                attach	附加内容	string	@mock=
                img_path	图片路径	string	@mock=asdfasdf~~~~
                model	所属模块	string	@mock=car
                source_id	来源ID	number	@mock=8
                title	标题	string	@mock=5555555555555~~~~
                type
                 */

                if (mModel == null) {
                    CommonUtils.showToast(mContext, "数据正在加载.....");
                    return;
                }

                HashMap<String, String> params = new HashMap<String, String>();
                // args.put("attach", "");
                params.put("img_path", mModel.headerInfo.cover);
                params.put("model", "series");
                params.put("source_id", mSeriesId + "");
                params.put("title", mName);
                params.put("type", "car");
                params.put("token", IOUtils.getToken(mContext));
                if (isFav == 1) {
                    mCancelFavoritePresenter.cancelFavoriteData(params);
                } else {
                    mAddFavoritePresenter.addFavoriteData(params);
                }

                break;
            case R.id.title_share_btn:

                if (mModel == null) {
                    CommonUtils.showToast(mContext, "数据加载中,暂时无法分享!");
                    return;
                }
                final String title = mModel.headerInfo.name;
                final String description = mName + "厂商指导价:" + mModel.headerInfo.price;
                final String cover = URLUtil.builderImgUrl(mModel.headerInfo.cover, 360, 360);

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
                                mWbHandler = OpenShareUtil.shareSina(SeriesDetailActivity.this,
                                        title, description);
                                break;
                            case 3:
                                ArrayList<String> urls = new ArrayList<String>();
                                urls.add(cover);
                                OpenShareUtil.shareToQzone(mGlobalContext.getTencent(),
                                        SeriesDetailActivity.this, mBaseUIListener, urls, title,
                                        description);
                                break;
                            case 4:
                                OpenShareUtil.shareToQQ(mGlobalContext.getTencent(),
                                        SeriesDetailActivity.this, mBaseUIListener, title, cover,
                                        description);
                                break;

                        }
                    }
                });
                openShareWindow.show(this);

                break;
            case R.id.bigHeaderImg:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mSeriesId);
                args.put(Constants.IntentParams.NAME, mName);
                CommonUtils.startNewActivity(mContext, args, SeriesGalleryActivity.class);
                break;
            case R.id.commitBtn:
//                args = new HashMap<String, Serializable>();
//                args.put(Constants.IntentParams.ID, mSeriesId);
//                args.put(Constants.IntentParams.INTENT_TYPE, 1);
//                args.put(Constants.IntentParams.DATA, mBrandName + " " + mName);
//                CommonUtils.startNewActivity(mContext, args, CommitCarOrderActivity.class);
                CarBean carBeanCommite = new CarBean();
                carBeanCommite.seriesId = mSeriesId;
                carBeanCommite.factoryName = mBrandName;
                carBeanCommite.seriesName = mName;
                Intent toCommitIntent = new Intent(getContext(), CommitCarOrderActivity.class);
                toCommitIntent.putExtra(Constants.IntentParams.DATA, carBeanCommite);
                startActivity(toCommitIntent);
                break;
        }

    }

    private void loadCarSalesData(int type) {

        if (type == 1) { //在售
            mNowSaleList.setVisibility(View.VISIBLE);
            mPreSaleList.setVisibility(View.GONE);
            stopSaleList.setVisibility(View.GONE);
            ViewUtil.setListViewHeightBasedOnChildren(mNowSaleList);
            mNowCarsAdapter.notifyDataSetChanged();
        } else if (type == 2) {
            mNowSaleList.setVisibility(View.GONE);
            mPreSaleList.setVisibility(View.VISIBLE);
            stopSaleList.setVisibility(View.GONE);
            ViewUtil.setListViewHeightBasedOnChildren(mPreSaleList);
            mPreCarsAdapter.notifyDataSetChanged();
        } else if (type == 3) {
            mNowSaleList.setVisibility(View.GONE);
            mPreSaleList.setVisibility(View.GONE);
            stopSaleList.setVisibility(View.VISIBLE);
            ViewUtil.setListViewHeightBasedOnChildren(stopSaleList);
            mStopCarsAdapter.notifyDataSetChanged();

        }
    }

    private void refreshUI() {

        filterExistPKData(mPreCars);
        filterExistPKData(mNowCars);
        filterExistPKData(mStopCars);
        ViewUtil.setListViewHeightBasedOnChildren(mNowSaleList);
        mNowCarsAdapter.notifyDataSetChanged();
        ViewUtil.setListViewHeightBasedOnChildren(mPreSaleList);
        mPreCarsAdapter.notifyDataSetChanged();

        ViewUtil.setListViewHeightBasedOnChildren(stopSaleList);
        mStopCarsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //加上这一句才能回调
        Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUIListener);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mWbHandler != null) {
            mWbHandler.doResultIntent(intent, new SinaWBCallback(mContext));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (openShareWindow != null) {
            openShareWindow.dismiss();
        }
    }
}
