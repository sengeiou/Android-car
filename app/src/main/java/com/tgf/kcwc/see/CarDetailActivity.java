package com.tgf.kcwc.see;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.comment.CommentFrag;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.CarOwnerSalerModel;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.DaRenEvaluateModel;
import com.tgf.kcwc.mvp.model.OrgModel;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.MotoDetailPresenter;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.MotordetailView;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.see.dealer.SalespersonListActivity;
import com.tgf.kcwc.see.evaluate.PopmanESActivity;
import com.tgf.kcwc.see.evaluate.PopmanESDetailActitivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.seecar.CommitCarOrderActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MapNavPopWindow;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2016/12/12 0012
 * E-mail:hekescott@qq.com
 */

public class CarDetailActivity extends BaseActivity implements MotordetailView, CommentListView,
        OnBannerClickListener, RadioGroup.OnCheckedChangeListener {
    private static final int REQUEST_CODE_REPLY = 1;
    private static final int REQUEST_CODE_COMMENT = 102;
    Banner banner;
    //    List colorViewsList;
    //    private ArrayList loopHead = new ArrayList();
    private NestFullListView motodetailCommentLv;
    private NestFullListView motodetailORGLv;
    private MotoDetailPresenter motoDetailPresenter;
    private NestFullListViewAdapter orgsadapter;
    //评论
    private NestFullListViewAdapter commentsadapter;
    //回复列表
    private NestFullListViewAdapter replyadapter;
    private WrapAdapter usedCarSellerAdapter;
    private WrapAdapter popmanEstimateAdapter;
    private GridView motodetailUsedcarGridview;
    private ListView motodetailPopmanlv;
    private ObservableScrollView mScrollview;
    private TextView picnumTv;
    private CommentModel commentModel;
    private TextView commentnumTv;
    private CommentListPresenter mCommentPresenter;
    //获取车辆Id
    private int mCarId = 4;
    private TextView cardescTv;
    private TextView carPrice;
    private KPlayCarApp kPlayCarApp;
    private String lat, lon;
    private RadioGroup radioGroup;
    private RadioButton rbtnAll;
    private int isFav;
    private UserDataPresenter mCancelFavoritePresenter;
    private UserDataPresenter mAddFavoritePresenter;
    private ImageButton mTitleFavoriteBtn;
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
            mTitleFavoriteBtn.setImageResource(R.drawable.favorite_icon_n);
            CommonUtils.showToast(mContext,
                    "已取消收藏");
        }
    };
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
            mTitleFavoriteBtn.setImageResource(R.drawable.btn_collection_s);

            CommonUtils.showToast(mContext,
                    "收藏成功");
        }
    };
    private OpenShareWindow openShareWindow;
    private TextView mPKNumsTv;
    private RelativeLayout ownSaleTitlRl;
    private final String mModule = "car_detail";
    private CommentFrag commentFrag;
    private FragmentManager commentFm;
    private FragmentTransaction commentTran;
    private MapNavPopWindow mapNavPopWindow;
    private SimpleDraweeView genderIv;
    private GenericDraweeHierarchy hierarchy;
    private SimpleDraweeView avatarIv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTitleBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motodetail);
        backEvent(findViewById(R.id.title_bar_back));
        mPKNumsTv = (TextView) findViewById(R.id.motodetial_pknumstv);
        mCancelFavoritePresenter = new UserDataPresenter();
        mCancelFavoritePresenter.attachView(mCancelFavoriteView);
        mAddFavoritePresenter = new UserDataPresenter();
        mAddFavoritePresenter.attachView(mAddFavoriteView);
        motodetailPopmanlv.setOnItemClickListener(mPopmansItemClickListener);
        ViewUtil.setListViewHeightBasedOnChildren(motodetailPopmanlv);
        mapNavPopWindow = new MapNavPopWindow(mContext);
        mapNavPopWindow.setOnClickListener(this);
    }

    private ArrayList<CarBean> mPKCars = null;//车型对比集合

    private int mPKNums;

    private void showPkNums() {
        if (mPKNums == 0) {
            mPKNumsTv.setVisibility(View.INVISIBLE);
        }
        mPKNumsTv.setText(mPKNums + "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPKCars = KPlayCarApp.getValue(Constants.KeyParams.PK_DATAS);
        if (mPKCars == null) {

            mPKCars = new ArrayList<CarBean>();
            KPlayCarApp.putValue(Constants.KeyParams.PK_DATAS, mPKCars);
        } else {
            mPKNums = mPKCars.size();
            showPkNums();
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        mCarId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        rbtnAll = (RadioButton) findViewById(R.id.rbtn_all);
        rbtnAll.performClick();
        mContext = this;
        kPlayCarApp = (KPlayCarApp) getApplication();
        lat = kPlayCarApp.getLattitude();
        lon = kPlayCarApp.getLongitude();
        mTitleFavoriteBtn = (ImageButton) findViewById(R.id.title_favorite_btn);
        cardescTv = (TextView) findViewById(R.id.cardetail_desc);
        carPrice = (TextView) findViewById(R.id.cardetail_price);
        banner = (Banner) findViewById(R.id.motodetail_looppng_viewpager);
        mScrollview =  findViewById(R.id.motodetail_scrollview);
        mScrollview.setScrollViewListener(mScrollListener);
        motodetailORGLv = (NestFullListView) findViewById(R.id.motodetail_org_lv);
        motodetailUsedcarGridview = (GridView) findViewById(R.id.motodetail_usedcar_gridview);
        ownSaleTitlRl = (RelativeLayout) findViewById(R.id.motodetail_ownsale_rl);
        ownSaleTitlRl.setOnClickListener(this);
        picnumTv = (TextView) findViewById(R.id.motodetail_tv_picnum);
        picnumTv.setOnClickListener(this);
        commentnumTv = (TextView) findViewById(R.id.motodetail_commentnum);
        findViewById(R.id.motodetail_popmanes_title).setOnClickListener(this);
        findViewById(R.id.motodetail_pk_tv).setOnClickListener(this);
        findViewById(R.id.motodetail_compare_saleoff).setOnClickListener(this);
        findViewById(R.id.title_pk_btn).setOnClickListener(this);
        findViewById(R.id.title_favorite_btn).setOnClickListener(this);
        findViewById(R.id.title_share_btn).setOnClickListener(this);
        findViewById(R.id.motodetail_info_tv).setOnClickListener(this);
        findViewById(R.id.motodetail_buy_tv).setOnClickListener(this);
        findViewById(R.id.cardetail_repayLayout).setOnClickListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.cardetail_filterRg);
        radioGroup.setOnCheckedChangeListener(this);
        motodetailPopmanlv = (ListView) findViewById(R.id.motodetail_popmanlv);

        genderIv = (SimpleDraweeView) findViewById(R.id.genderImg);
        hierarchy = genderIv.getHierarchy();
        avatarIv = (SimpleDraweeView) findViewById(R.id.motodetail_avatar_iv);

        motodetailORGLv.setFocusable(false);
        motoDetailPresenter = new MotoDetailPresenter();
        motoDetailPresenter.attachView(this);
        motoDetailPresenter.getMotoDeatail(mCarId + "", IOUtils.getToken(this));
        commentFm = getSupportFragmentManager();

        motoDetailPresenter.getDaRenEvaluateList(mCarId);
        motoDetailPresenter.getCarOwnerList(mCarId);

        motoDetailPresenter.getORGList(mCarId, lon, lat);
        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        mCommentPresenter.loadCommentList(mModule, mCarId + "", "car");
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
    //经销商点击事件Listener
    private NestFullListView.OnItemClickListener mOrgsItemClickListener = new NestFullListView.OnItemClickListener() {
        @Override
        public void onItemClick(NestFullListView parent,
                                View view,
                                int position) {

            OrgModel model = showorgList
                    .get(
                            position);
            String orgId = model.id
                    + "";
            String orgTitle = model.full_name;
            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(
                    Constants.IntentParams.ID,
                    orgId);
            args.put(
                    Constants.IntentParams.TITLE,
                    orgTitle);
            args.put(
                    Constants.IntentParams.INDEX,
                    0);
            CommonUtils
                    .startNewActivity(
                            mContext,
                            args,
                            DealerHomeActivity.class);
        }
    };
    //达人评测点击事件Listener
    private AdapterView.OnItemClickListener mPopmansItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {
            String popesId = "";
            String orgTitle = "";
            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(
                    Constants.IntentParams.ID,
                    popesId);
            args.put(
                    Constants.IntentParams.TITLE,
                    orgTitle);
            CommonUtils
                    .startNewActivity(
                            mContext,
                            args,
                            PopmanESActivity.class);
        }
    };
    private WbShareHandler mWbHandler;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.motodetail_popmanes_title:
                Intent toPopmanIntent = new Intent(mContext, PopmanESActivity.class);
                toPopmanIntent.putExtra(Constants.IntentParams.TAG_BRAND_ID,mMotoDetail.brandId);
                startActivity(toPopmanIntent);
                break;
            case R.id.motodetail_pk_tv:
            case R.id.title_pk_btn:
                Intent intent = new Intent();
                CarBean carBean = new CarBean(mCarId, mMotoDetail.carName);
                carBean.name = mMotoDetail.carName;


                boolean isExist = false;
                for (CarBean c : mPKCars) {

                    if (c.id == carBean.carId) {

                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    mPKCars.add(carBean);
                }
                intent.putExtra(Constants.IntentParams.DATA, mPKCars);
                intent.setClass(mContext, OwnerSubActivity.class);
                startActivity(intent);

                //                Intent intent = new Intent();
                //                intent.putExtra(Constants.IntentParams.DATA, mPKCars);
                //                intent.setClass(mContext, OwnerSubActivity.class);
                //                startActivity(intent);
                //                Map<String, Serializable> args1 = new HashMap<String, Serializable>();
                //                args1.put(Constants.IntentParams.IDS, mCarId + "");
                //                args1.put(Constants.IntentParams.IS_OPEN_ADD, true);
                //                CommonUtils.startNewActivity(mContext, args1, OwnerContrastGoListActivity.class);

                break;
            case R.id.title_favorite_btn:
                if (mMotoDetail == null) {
                    CommonUtils.showToast(mContext, "数据正在加载.....");
                    return;
                }
                HashMap<String, String> params = new HashMap<String, String>();
                // args.put("attach", "");
                Gson gson = new Gson();
                params.put("img_path", gson.toJson(mMotoDetail.coverImgList));
                params.put("model", Constants.FavoriteTypes.CAR);
                params.put("source_id", mCarId + "");
                params.put("title", cardescTv.getText() + "");
                params.put("type", "car");
                params.put("token", IOUtils.getToken(mContext));
                if (isFav == 1) {
                    mCancelFavoritePresenter.cancelFavoriteData(params);
                } else {
                    mAddFavoritePresenter.addFavoriteData(params);
                }
                break;
            case R.id.motodetail_ownsale_rl:
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 3);
                KPlayCarApp.putValue(Constants.IntentParams.SERIES_ID, mMotoDetail.carSeriesId + "");
                Intent toMainAct = new Intent(getContext(), MainActivity.class);
                toMainAct.putExtra(Constants.IntentParams.INDEX, 1);
                toMainAct.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toMainAct);
                finish();
                break;
            case R.id.motodetail_compare_saleoff:
                Intent toDiscountContrastActivity = new Intent(getContext(), DiscountContrastActivity.class);
                toDiscountContrastActivity.putExtra(Constants.IntentParams.ID, mCarId);
                startActivity(toDiscountContrastActivity);
                break;
            case R.id.title_share_btn:
                if (openShareWindow == null)
                    openShareWindow = new OpenShareWindow(this);
                openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        switch (position) {

                            case 0:
                                OpenShareUtil.sendWXMoments(mContext, mGlobalContext.getMsgApi(),
                                        "微信朋友圈", "微信朋友圈xxx");
                                break;
                            case 1:
                                OpenShareUtil.sendWX(mContext, mGlobalContext.getMsgApi(), "微信好友",
                                        "微信好友xxx");
                                break;

                            case 2:
                                mWbHandler = OpenShareUtil.shareSina(CarDetailActivity.this, "微博",
                                        "躲雨");
                                break;
                            case 3:
                                ArrayList<String> urls = new ArrayList<String>();
                                urls.add(
                                        "http://down1.laifudao.com/images/tupian/201372922418.jpg");
                                OpenShareUtil.shareToQzone(mGlobalContext.getTencent(),
                                        CarDetailActivity.this, mBaseUIListener, urls, "看车玩车", "看车玩车");
                                break;

                            case 4:
                                OpenShareUtil.shareToQQ(mGlobalContext.getTencent(),
                                        CarDetailActivity.this, mBaseUIListener,
                                        "http://m.51kcwc.com/#/home/index", "看车玩车",
                                        "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg",
                                        "您身边的超级汽车生活社区！看资讯、拿优惠、分享精彩、结交朋友!", "看车玩车");
                                break;

                        }
                    }
                });
                openShareWindow.show(this);
                break;
            case R.id.motodetail_info_tv:
                Intent toParamIntent = new Intent(mContext, MotoParamActivity.class);
                toParamIntent.putExtra(Constants.IntentParams.ID, mCarId);
                startActivity(toParamIntent);
                break;
            case R.id.motodetail_buy_tv:
                if (mMotoDetail != null) {
                    Intent toIntent = new Intent(this, CommitCarOrderActivity.class);
                    CarBean carBeanTo = new CarBean();
                    carBeanTo.id = mCarId;
                    carBeanTo.factoryName = mMotoDetail.brandName;
                    carBeanTo.seriesName = mMotoDetail.seriesName;
                    carBeanTo.name = mMotoDetail.carName;
                    toIntent.putExtra(Constants.IntentParams.DATA, carBeanTo);
                    startActivity(toIntent);
                }
                break;
            case R.id.motodetail_tv_picnum:
                Intent toIntent = new Intent(getContext(), CarModelGalleryActivity.class);
                toIntent.putExtra(Constants.IntentParams.ID, mCarId);
                toIntent.putExtra(Constants.IntentParams.NAME, cardescTv.getText());
                startActivity(toIntent);
                break;
            case R.id.cardetail_repayLayout:
                Intent toEditIntent = new Intent(mContext, CommentEditorActivity.class);
                toEditIntent.putExtra(Constants.IntentParams.ID, mCarId + "");
                toEditIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModule);
                startActivityForResult(toEditIntent, REQUEST_CODE_COMMENT);
                break;
            case R.id.exhibitionlist_address_tv:
                mapNavPopWindow.show(getActivity());
                break;
            case R.id.cancel:
                mapNavPopWindow.dismiss();
                break;
            case R.id.aMap:
                try {
                    LatLng startGLat = new LatLng(Double.parseDouble(kPlayCarApp.latitude), Double.parseDouble(kPlayCarApp.longitude));
                    LatLng endGLat = new LatLng(Double.parseDouble(currentNaviOrg.latitude), Double.parseDouble(currentNaviOrg.longitude));
                    kPlayCarApp.getLattitude();
                    URLUtil.launcherInnerRouteAMap(getActivity(), currentNaviOrg.address, startGLat,
                            endGLat);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.baiduMap:
                try {
                    LatLng startBLat = new LatLng(Double.parseDouble(kPlayCarApp.latitude), Double.parseDouble(kPlayCarApp.longitude));
                    LatLng endBLat = new LatLng(Double.parseDouble(currentNaviOrg.latitude), Double.parseDouble(currentNaviOrg.longitude));
                    URLUtil.launcherInnerRouteBaidu(getActivity(), currentNaviOrg.address, startBLat,
                            endBLat);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void failure(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    private List<String> coverImglist;
    private CarBean mMotoDetail;

    @Override
    public void showMotoDeatail(CarBean motoDetail) {
        this.mMotoDetail = motoDetail;
        this.isFav = motoDetail.isFav;

        if (isFav == 1) {
            mTitleFavoriteBtn.setImageResource(R.drawable.btn_collection_s);
        } else {
            mTitleFavoriteBtn.setSelected(false);
        }
        cardescTv.setText(mMotoDetail.seriesName + " " + mMotoDetail.carName);
        carPrice.setText(
                Html.fromHtml("厂商指导价 <font color='#36a95c'>" + mMotoDetail.referencePrice + "</font>"));
        coverImglist = mMotoDetail.coverImgList;
        picnumTv.setText(mMotoDetail.picCount + "张");
        if (coverImglist != null && coverImglist.size() != 0)
            showBanner(coverImglist);
    }

    @Override
    public void showDarenEvaluate(final ArrayList<DaRenEvaluateModel> daRenEvaluateModelList) {

        //达人评测
        popmanEstimateAdapter = new WrapAdapter<DaRenEvaluateModel>(mContext,
                daRenEvaluateModelList, R.layout.listview_item_popman_estimate) {
            @Override
            public void convert(ViewHolder helper, DaRenEvaluateModel item) {
                helper.setText(R.id.daren_title, item.title);
                helper.setText(R.id.daren_nickname, item.create_user.nickname);
                helper.setText(R.id.listviewitem_comment, item.reply_count + "");
                helper.setText(R.id.listviewitem_focuson, item.digg_count + "");
                helper.setText(R.id.listviewitem_visitors, item.view_count + "");
                String coverUrl = URLUtil.builderImgUrl(item.cover, 270, 203);
                helper.setSimpleDraweeViewURL(R.id.daren_coveriv, coverUrl);
                String avatarURL = URLUtil.builderImgUrl(item.create_user.avatar, 144, 144);
                helper.setSimpleDraweeViewURL(R.id.daren_avatar, avatarURL);
            }
        };
        motodetailPopmanlv.setAdapter(popmanEstimateAdapter);
        motodetailPopmanlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toEsDetail = new Intent(getContext(), PopmanESDetailActitivity.class);
                toEsDetail.putExtra(Constants.IntentParams.ID,
                        daRenEvaluateModelList.get(position).id);
                startActivity(toEsDetail);
            }
        });

    }

    @Override
    public void showCarOwnerList(final ArrayList<CarOwnerSalerModel> carOwnerSalerList) {
        if (carOwnerSalerList == null || carOwnerSalerList.size() == 0) {
            ownSaleTitlRl.setVisibility(View.GONE);
            motodetailUsedcarGridview.setVisibility(View.GONE);
            return;
        }

        usedCarSellerAdapter = new WrapAdapter<CarOwnerSalerModel>(mContext, carOwnerSalerList,
                R.layout.gridview_item_usedcar) {
            @Override
            public void convert(ViewHolder helper, CarOwnerSalerModel item) {
                if (item.price.equals("0.0") || item.price.equals("0") || item.price.equals("0.00")){
                    helper.setText(R.id.ownersale_price, "面议");
                }else {
                    helper.setText(R.id.ownersale_price, "￥" + item.price);
                }
                String coverUrl = URLUtil.builderImgUrl(item.cover, 360, 360);
                helper.setSimpleDraweeViewURL(R.id.usedcar_coverIv, coverUrl);
                int kilo = (int) (Double.parseDouble(item.road_haul) / 10000);
                helper.setText(R.id.owersale_desc, kilo + "万公里 | " + item.buy_year + "年");
            }
        };
        motodetailUsedcarGridview.setAdapter(usedCarSellerAdapter);
        motodetailUsedcarGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toOwnSaleAct = new Intent(getContext(), SaleDetailActivity.class);
                toOwnSaleAct.putExtra(Constants.IntentParams.ID,
                        carOwnerSalerList.get(position).id);
                startActivity(toOwnSaleAct);
            }
        });
    }

    public void showBanner(List<String> bannerList) {
        if (bannerList != null && bannerList.size() != 0) {
            ArrayList<String> imgUrls = new ArrayList<>();
            for (int i = 0; i < bannerList.size(); i++) {
                imgUrls.add(URLUtil.builderImgUrl(bannerList.get(i), 540, 270));
            }
            banner.setImages(imgUrls).setImageLoader(new FrescoImageLoader())
                    .setOnBannerClickListener(this).start();
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        if (REQUEST_CODE_REPLY == requestCode) {
    //            if (this.RESULT_OK == resultCode) {
    //                String replys = data.getStringExtra("data");
    //                CommonUtils.showToast(mContext, replys);
    //            }
    //        }
    //    }

    //评论
    @Override
    public void showDatas(Object o) {
        commentModel = (CommentModel) o;
        commentnumTv.setText("车主说 (" + commentModel.count + ")");
        if (IOUtils.getAccount(this).userInfo.gender == 1) {
            hierarchy.setPlaceholderImage(R.drawable.icon_men);
        } else {
            hierarchy.setPlaceholderImage(R.drawable.icon_women);
        }
        if (StringUtils.checkNull(IOUtils.getAccount(this).userInfo.avatar)) {
            avatarIv.setImageURI(URLUtil.builderImgUrl(IOUtils.getAccount(this).userInfo.avatar, 144, 144));
        }

        avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.checkNull(IOUtils.getUserId(getContext()))) {
                    Intent toUserIntent = new Intent(getContext(), UserPageActivity.class);
                    toUserIntent.putExtra(Constants.IntentParams.ID, Integer.parseInt(IOUtils.getUserId(getContext())));
                    startActivity(toUserIntent);
                }
            }
        });
        commentFrag = new CommentFrag(commentModel.comments, mCarId, mModule);
        commentTran = commentFm.beginTransaction();
        commentTran.replace(R.id.comment_fragfl, commentFrag);
        commentTran.commit();
    }

    private ArrayList<OrgModel> showorgList;
    private ArrayList<OrgModel> allorgList = new ArrayList<>();
    private OrgModel currentNaviOrg;

    //经销商列表
    @Override
    public void showORGlist(ArrayList<OrgModel> organizationList) {
        allorgList = (ArrayList<OrgModel>) organizationList.clone();
        showorgList = organizationList;
        //经销商列表
        orgsadapter = new NestFullListViewAdapter<OrgModel>(R.layout.listview_item_motorg,
                showorgList) {
            @Override
            public void onBind(final int pos, final OrgModel orgModel, NestFullViewHolder holder) {
                GridView sakerGrird = holder.getView(R.id.motorg_gr);
                View salesLayout = holder.getView(R.id.cardetail_salesrl);
                holder.setText(R.id.org_title, orgModel.full_name);
                View couponView = holder.getView(R.id.motorg_coupon);
                View perView = holder.getView(R.id.motorg_per);
                View packView = holder.getView(R.id.motorg_pack);
                if (orgModel.is_coupon == 1) {
                    couponView.setVisibility(View.VISIBLE);
                } else {
                    couponView.setVisibility(View.GONE);
                }
                if (orgModel.is_package == 1) {
                    packView.setVisibility(View.VISIBLE);
                } else {
                    packView.setVisibility(View.GONE);
                }
                if (orgModel.is_discount == 1) {
                    perView.setVisibility(View.VISIBLE);
                } else {
                    perView.setVisibility(View.GONE);
                }

                holder.setText(R.id.orgs_addresstv, "地址： " + orgModel.address);
                holder.setText(R.id.orgs_distance, orgModel.distance + "km");
                if (orgModel.price != 0) {
                    holder.setText(R.id.motoshoper_price, "↓下降 " + orgModel.price + "万");
                }
                holder.getView(R.id.motoshoper_item_telbtn)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SystemInvoker.launchDailPage(mContext, orgModel.tel);
                            }
                        });
                salesLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toSalesIntent = new Intent(getContext(),
                                SalespersonListActivity.class);
                        toSalesIntent.putExtra(Constants.IntentParams.ID, orgModel.id + "");
                        startActivity(toSalesIntent);
                    }
                });
                holder.getView(R.id.item_gotonavi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentNaviOrg = orgModel;
                        mapNavPopWindow.show(getActivity());
                    }
                });
                if (orgModel.salelist == null || orgModel.salelist.size() == 0) {
                    salesLayout.setVisibility(View.GONE);

                } else {
                    salesLayout.setVisibility(View.VISIBLE);
                    sakerGrird.setAdapter(new WrapAdapter<OrgModel.SalerList>(mContext,
                            R.layout.griditem_saler, orgModel.salelist) {
                        @Override
                        public void convert(ViewHolder helper, final OrgModel.SalerList item) {
                            helper.setText(R.id.salerstar, item.star);
                            helper.setText(R.id.salernametv, item.nickname);
                            String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                            SimpleDraweeView avatarIv = helper.getView(R.id.saler_avatariv);
                            avatarIv.setImageURI(Uri.parse(avatarUrl));
                            avatarIv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent toUserPage = new Intent(getContext(),
                                            UserPageActivity.class);
                                    toUserPage.putExtra(Constants.IntentParams.ID, item.id);
                                    startActivity(toUserPage);
                                }
                            });
                        }
                    });
                }


            }
        };
        motodetailORGLv.setOnItemClickListener(mOrgsItemClickListener);
        motodetailORGLv.setAdapter(orgsadapter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void OnBannerClick(int position) {
        Intent toIntent = new Intent(getContext(), CarModelGalleryActivity.class);
        toIntent.putExtra(Constants.IntentParams.ID, mCarId);
        toIntent.putExtra(Constants.IntentParams.NAME, cardescTv.getText());
        startActivity(toIntent);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_all:
                showorgList = (ArrayList<OrgModel>) allorgList.clone();
                break;
            case R.id.rbtn_coupon:
                showorgList.clear();
                for (OrgModel org : allorgList) {
                    if (org.is_coupon == 1) {
                        showorgList.add(org);
                    }
                }
                break;
            case R.id.rbtn_limit:
                showorgList.clear();
                for (OrgModel org : allorgList) {
                    if (org.is_discount == 1) {
                        showorgList.add(org);
                    }
                }
                break;
            case R.id.rbtn_pack:
                showorgList.clear();
                for (OrgModel org : allorgList) {
                    if (org.is_package == 1) {
                        showorgList.add(org);
                    }
                }
                break;
            default:
                break;
        }
        motodetailORGLv.notifyDataSetChange(showorgList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_CODE_COMMENT) {
                    mCommentPresenter.loadCommentList(mModule, mCarId + "", "car");
                } else {
                    if (data.getBooleanExtra(Constants.IntentParams.DATA, false)) {
                        if (commentFrag != null) {
                            commentFrag.onActivityResult(requestCode, resultCode, data);
                        }
                    }
                }
            }
        }
    }

    public Activity getActivity() {
        return CarDetailActivity.this;
    }
}
