package com.tgf.kcwc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.app.WebViewActivity;
import com.tgf.kcwc.certificate.CheckinActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.CompileDrivingActivity;
import com.tgf.kcwc.driving.please.CompilePleasePlayActivity;
import com.tgf.kcwc.finddiscount.LimitDiscountNewDetailsActivity;
import com.tgf.kcwc.finddiscount.LimitDiscountNewDetailsOddsActivity;
import com.tgf.kcwc.fragments.TabHomeFragment;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.HomeModel;
import com.tgf.kcwc.mvp.presenter.HomePagePresenter;
import com.tgf.kcwc.mvp.view.HomePageView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.see.BigPhotoPageActivity;
import com.tgf.kcwc.see.BrandModelsActivity;
import com.tgf.kcwc.see.NewCarLaunchActivity;
import com.tgf.kcwc.see.StoreShowCarDetailActivity;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.see.exhibition.ExhibitPalaceListActivity;
import com.tgf.kcwc.see.exhibition.ExhibitionEventActivity;
import com.tgf.kcwc.see.exhibition.ExhibitionNewsActivity;
import com.tgf.kcwc.see.model.ModelListActivity;
import com.tgf.kcwc.ticket.PreRegistrationActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.MyBitmapTransformation;
import com.tgf.kcwc.util.ScreenUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.HomePopWindow;
import com.tgf.kcwc.view.home.CustomImageView;
import com.tgf.kcwc.view.home.NineGridlayout;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/9/7 0007
 * E-mail:hekescott@qq.com
 */

public class HomeApater extends BaseAdapter {
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
    //    【event】最近有车展
//【org_show_car】店内展车
//【new_car】新车首发
//【brand_benefit】品牌钜惠，特价大促
//【sale】限量特价车，先到先得
//【limit_time】限时特惠，怕你错过
//【coupon】附近代金券
//【mood】说说
//【cycle】开车去
//【play】请你玩
//【roadbook】路书
//【show】光影秀
//【goods】车主自售
//【words】普通帖子
//【evaluate】达人评测
//【event_goods】展会现场车主自售即二手车
    private static final Map<String, Integer> modelMap = new HashMap();

    static {
        modelMap.put(KEY_HOME_SAY, 0);
        modelMap.put(WORDS, 0);
        modelMap.put(EVALUATE, 0);
        modelMap.put(CYCLE, 1);
        modelMap.put(PLAY, 1);
        modelMap.put(SHOW, 2);
        modelMap.put(GOODS, 3);
        modelMap.put(EVENT_GOODS, 3);
        modelMap.put(LIMIT_TIME, 4);
        modelMap.put(SALE, 5);
        modelMap.put(BRAND_BENEFIT, 6);
        modelMap.put(ROADBOOK, 7);
        modelMap.put(EVENT, 8);
        modelMap.put(ORG_SHOW_CAR, 9);
        modelMap.put(COUPON, 10);
        modelMap.put(NEW_CAR, 11);

    }

    private MainActivity mContext;
    private KPlayCarApp kPlayCarApp;
    private ArrayList<HomeModel.HomeModelItem> mHomeList;
    private final LayoutInflater mInflater;
    private HomePagePresenter mHomePagePresenter;
    private HomePageView tabhomeFrag;
    public String token;

    public HomeApater(MainActivity context, ArrayList<HomeModel.HomeModelItem> homeList, HomePagePresenter homePagePresenter, HomePageView tabhomeFrag) {
        this.mContext =  context;
        this.mHomeList = homeList;
        this.kPlayCarApp = (KPlayCarApp) mContext.getApplication();
        mInflater = LayoutInflater.from(context);
        this.mHomePagePresenter = homePagePresenter;
        token = IOUtils.getToken(context);
        this.tabhomeFrag = tabhomeFrag;
    }


    @Override
    public int getCount() {
        return mHomeList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (modelMap.get(mHomeList.get(position).model) != null) {
            return modelMap.get(mHomeList.get(position).model);
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 12;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == modelMap.get(KEY_HOME_SAY)) {//说说
            return handleSayView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(PLAY)) {//活动
            return handleHuoDongView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(SHOW)) {
            return handleVideoView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(GOODS)) {
            return handleSalerView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(LIMIT_TIME)) {
            return handleLimitTimeView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(SALE)) {
            return handleLimitNumView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(BRAND_BENEFIT)) {
            return handleLimitBrandView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(ROADBOOK)) {
            return handleRoadbookView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(EVENT)) {
            return handleExhibitView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(ORG_SHOW_CAR)) {
            return handleStoreView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(COUPON)) {
            return handleCouponView(position, convertView, parent);
        } else if (getItemViewType(position) == modelMap.get(NEW_CAR)) {
            return handleNewcarView(position, convertView, parent);
        } else {
            return handleHuoDongView(position, convertView, parent);
        }
//        return convertView;
    }

    private View handleNewcarView(int position, View convertView, ViewGroup parent) {
        HomeNewcarViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_newcar, parent, false);
            holder = new HomeNewcarViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeNewcarViewHolder) convertView.getTag();
        }
                  /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        HomeModel.ModelData modelData = item.modelData;
        holder.mHomeCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
        holder.mHomenewCarCoveriv.setImageURI(Uri.parse(URLUtil.builderImgUrl(modelData.productLogo, 360, 360)));
        holder.mHomenewCarNameTv.setText(modelData.brandName);
        holder.mHomenewCarAddressTv.setText(modelData.eventName + "/" + modelData.name);
        holder.mHomenewCarTimeTv.setText(modelData.releaseTime);
        holder.mHomenewCarTitleTv.setText(modelData.productName);
        holder.mHomenewCarStarTv.setText("到场明星:" + modelData.star);
//         TODO: 2017/9/19 0019
//        holder.mHomenewCarBugTicketTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(IOUtils.isLogin(mContext)){
//                    Map<String, Serializable> args = new HashMap<String, Serializable>();
//                    args.put(Constants.IntentParams.ID, item.modelId);
//                    args.put(Constants.IntentParams.DATA, item.type);
//                    args.put(Constants.IntentParams.DATA2, item.cover);
//                    CommonUtils.startNewActivity(mContext, args, PreRegistrationActivity.class);
//                }
//            }
//        });

        return convertView;
    }

    private View handleCouponView(int position, View convertView, ViewGroup parent) {
        HomeCouponViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_coupon, parent, false);
            holder = new HomeCouponViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeCouponViewHolder) convertView.getTag();
        }
                     /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        HomeModel.ModelData modelData = item.modelData;
        ArrayList<HomeModel.Coupon> originCouponList = modelData.couponsList;
        HomeModel.Coupon firstItem = originCouponList.get(0);
        holder.mHomeTitleTv.setText(firstItem.title);
        holder.mHomeCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(firstItem.cover, 540, 270)));
        holder.mHomecouponPriceTv.setText("价值" + firstItem.denomination);
        if (firstItem.price == 0) {
            holder.mHomecouponGotoGetBtn.setText("免费领取");
        } else {
            holder.mHomecouponGotoGetBtn.setText("购买");
        }
        int listSize = originCouponList.size();
        if (listSize > 1) {
            List<HomeModel.Coupon> newCouponList = originCouponList.subList(1, listSize - 1);
            holder.mHomeCouponlist.setAdapter(new NestFullListViewAdapter<HomeModel.Coupon>(R.layout.layout_homecoupon_item, newCouponList) {

                @Override
                public void onBind(int pos, HomeModel.Coupon coupon, NestFullViewHolder holder) {
                    holder.setSimpleDraweeViewURL(R.id.coupon_coveriv, URLUtil.builderImgUrl(coupon.cover, 360, 360));
                    holder.setText(R.id.coupon_titleTv, coupon.title);
                    if (coupon.price == 0) {
                        holder.setText(R.id.coupon_pricetv, "免费");
                    } else {
                        holder.setText(R.id.coupon_pricetv, "￥" + coupon.price);
                    }
                    TextView denoTv = holder.getView(R.id.coupon_denotv);
                    denoTv.setText(SpannableUtil.getDelLineString("￥" + coupon.denomination));
                }
            });
        }

        return convertView;
    }

    private View handleStoreView(int position, View convertView, ViewGroup parent) {
        final HomeStoreViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_store, parent, false);
            holder = new HomeStoreViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeStoreViewHolder) convertView.getTag();
        }
                     /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        final HomeModel.ModelData modelData = item.modelData;
        final HomeModel.Org org = modelData.org;
        //展会
        holder.mHomeTitleTv.setText(modelData.factoryName + modelData.seriesName + modelData.carName);
        holder.mHomestorePiconeiv.setImageURI(Uri.parse(URLUtil.builderImgUrl(modelData.appearanceImg, 270, 203)));
        holder.mHomestorePicTwoiv.setImageURI(Uri.parse(URLUtil.builderImgUrl(modelData.interiorImg, 270, 203)));
        holder.mHomestoreOutcolor.setText(Html.fromHtml("<font color=\"#999999\">外观</font> " + modelData.appearanceColorName));
        holder.mHomestoreOutcolor.setText(Html.fromHtml("<font color=\"#999999\">内饰</font>" + modelData.interiorColorName));
        holder.mHomeStoreCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(org.logo, 270, 203)));
        holder.mHomeStoreNameTv.setText(org.name);
        holder.mHomeStorLocTv.setText(org.address);
        holder.mHomeStoreCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(org.logo, 360, 360)));
        if (!TextUtil.isEmpty(modelData.appearanceImg)) {
            holder.mHomestorePiconeiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BigPhotoPageActivity.class);
                    intent.putExtra(Constants.KEY_IMG, modelData.appearanceImg);
                    mContext.startActivity(intent);
                }
            });
        }
        holder.titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 2);
                mContext.switchToTab(Constants.Navigation.SEE_TAB);
            }
        });
        holder.mHomeTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.CAR_ID, modelData.carId);
                args.put(Constants.IntentParams.DATA2, org.id);
                args.put(Constants.IntentParams.DATA3, item.title);
                CommonUtils.startNewActivity(mContext, args,
                        StoreShowCarDetailActivity.class);
            }
        });
        holder.homeOrglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.CAR_ID, modelData.carId);
                args.put(Constants.IntentParams.DATA2, org.id);
                args.put(Constants.IntentParams.DATA3, item.title);
                CommonUtils.startNewActivity(mContext, args,
                        StoreShowCarDetailActivity.class);

            }
        });
        holder.orgCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemInvoker.launchDailPage(mContext, org.tel);
            }
        });
        if (!TextUtil.isEmpty(modelData.interiorImg)) {
            holder.mHomestorePicTwoiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BigPhotoPageActivity.class);
                    intent.putExtra(Constants.KEY_IMG, modelData.interiorImg);
                    mContext.startActivity(intent);
                }
            });
        }


        return convertView;
    }

    private View handleExhibitView(int position, View convertView, ViewGroup parent) {
        HomeExhibitViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_exhibit, parent, false);
            holder = new HomeExhibitViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeExhibitViewHolder) convertView.getTag();
        }
             /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        final HomeModel.ModelData modelData = item.modelData;
        //展会
        holder.mHomeTitleTv.setText(item.title);
        holder.mHomeCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
        holder.mHomeCreatePlaceTv.setText(item.localAddress);
        holder.mHomeExhibitTimeTv.setText(DateFormatUtil.dispActiveTime2(modelData.startTime, modelData.endTime));
        holder.mHomeExhibitMenuTv.setAdapter(new WrapAdapter<HomeModel.Navigation>(mContext, modelData.navigationList, R.layout.griditem_home_event) {
            @Override
            public void convert(ViewHolder helper, HomeModel.Navigation item) {
                helper.setText(R.id.name, item.name);
            }
        });
        holder.mHomeExhibitMenuTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoMenu(modelData.navigationList.get(position).id, item.modelId);
            }
        });
        holder.titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtil.putExhibitId(mContext, item.modelId);
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 1);
                mContext.switchToTab(Constants.Navigation.SEE_TAB);
            }
        });
        final boolean isTicket = "ticket".equals(modelData.eventBtnType);
        if (isTicket) {
            holder.mHomeExhibitBuyTv.setText("抢门票");
        } else {
            holder.mHomeExhibitBuyTv.setText("证件注册");
        }
        holder.mHomeExhibitBuyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IOUtils.isLogin(mContext)) {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    if (isTicket) {
                        args.put(Constants.IntentParams.ID, item.modelId);
                        args.put(Constants.IntentParams.DATA, item.title);
                        args.put(Constants.IntentParams.DATA2, item.cover);
                        CommonUtils.startNewActivity(mContext, args, PreRegistrationActivity.class);
                    } else {
                        args.put(Constants.IntentParams.DATA, item.title);
                        args.put(Constants.IntentParams.DATA2, item.cover);
                        args.put(Constants.IntentParams.ID, item.modelId + "");
                        args.put(Constants.IntentParams.ID2, "");
                        CommonUtils.startNewActivity(mContext, args, CheckinActivity.class);
                    }
                }
            }
        });
        return convertView;
    }

    private View handleRoadbookView(int position, View convertView, ViewGroup parent) {
        HomeRoadBookViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_tripbook, parent, false);
            holder = new HomeRoadBookViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeRoadBookViewHolder) convertView.getTag();
        }
        /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        HomeModel.ModelData modelData = item.modelData;
        HomeModel.CreateUser createUser = item.createUser;
        holder.mHomeAvatarIv.setImageURI(URLUtil.builderImgUrl(createUser.avatar, 144, 144));
        holder.mNametv.setText(createUser.nickname);
        holder.homeCreateTimeTv.setText(item.createTime);
        if (createUser.sex == 1) {
            holder.mGenderImg.setImageResource(R.drawable.icon_men);
        } else {
            holder.mGenderImg.setImageResource(R.drawable.icon_women);
        }
        if (createUser.isDoyen == 1) {
            holder.mCommentPopmanTv.setVisibility(View.VISIBLE);
        } else {
            holder.mCommentPopmanTv.setVisibility(View.GONE);
        }
        if (createUser.isModel == 1) {
            holder.mCommentModelTv.setVisibility(View.VISIBLE);
        } else {
            holder.mCommentModelTv.setVisibility(View.GONE);
        }
        holder.mHomeCreatePlaceTv.setText(item.localAddress);
        holder.mHomeCreateDistanceTv.setText(item.distance + "km");
        holder.mBrandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(createUser.masterBrand, 360, 360)));
        //浏览，点赞，评论数
        holder.mHomeVisitors.setText(item.getViewCount() + "");
        holder.mHomeFocuson.setText(item.getDiggCount() + "");
        holder.mHomeComment.setText(item.getReplyCount() + "");
        if (item.isPraise == 1) {
            holder.mHomeIsPraiseIv.setImageResource(R.drawable.icon_likes);
        } else {
            holder.mHomeIsPraiseIv.setImageResource(R.drawable.icon_like);
        }
        holder.mHomeCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
        //路书部分
        holder.mTripbookSpeedmaxtv.setText(modelData.getSpeedMax());
        holder.mTripbookSpeedavgtv.setText(modelData.getSpeedAverage());
        holder.mTripbookAltitudetv.setText(modelData.altitudeAverage + "");
        holder.mTripbookMileage.setText(modelData.mileage);
        holder.mTripbookActualtimetv.setText(modelData.actualTime);
        String quotient = modelData.getQuotient();

        if ("0.0".equals(quotient)) {
            holder.mTripBookLayout.setVisibility(View.GONE);
        } else {
            holder.mTripBookLayout.setVisibility(View.VISIBLE);
            holder.mTripbookNumtv.setText(quotient);
        }
        return convertView;
    }

    private View handleLimitBrandView(int position, View convertView, ViewGroup parent) {

        HomeLimitbrandViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_branddiscount, parent, false);
            holder = new HomeLimitbrandViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeLimitbrandViewHolder) convertView.getTag();
        }
              /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        final HomeModel.ModelData modelData = item.modelData;
        ArrayList<String> benefitAttr = modelData.benefitAttr;
        holder.mHomeCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
        if (benefitAttr != null && benefitAttr.size() != 0) {
            holder.mHomeMygridview.setAdapter(new WrapAdapter<String>(mContext, R.layout.listitem_newlimitdiscount_item, benefitAttr) {
                @Override
                public void convert(ViewHolder helper, String item) {
                    helper.setText(R.id.name, item);
                }
            });
        }
        holder.mBrandName.setText(modelData.factoryName);
        holder.mBrandicon.setImageURI(Uri.parse(URLUtil.builderImgUrl(modelData.factoryLogo, 360, 360)));
        if (modelData.applyNum != 0) {
            holder.mLimitbrandBaomingTv.setVisibility(View.VISIBLE);
            holder.mLimitbrandBaomingTv.setText(modelData.applyNum + "人报名");
        } else {
            holder.mLimitbrandBaomingTv.setVisibility(View.GONE);
        }
        int leftTime = DateFormatUtil.getLeftDay(modelData.limtEndTime);
        if (leftTime > 0) {
            holder.mBranddiscountData.setText("报名仅剩" + leftTime + "天");
        } else if (leftTime == 0) {
            holder.mBranddiscountData.setText("报名仅剩今天");
        } else {
            holder.mBranddiscountData.setText("报名已结束");
        }
        //点击事件
        holder.mLatestExhibitionRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 3);
                mContext.switchToTab(Constants.Navigation.SEE_TAB);
            }
        });
        holder.mHomeCoverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, item.id + "");
                CommonUtils.startNewActivity(mContext, args, LimitDiscountNewDetailsOddsActivity.class);
            }
        });
        final boolean isTicket = "ticket".equals(modelData.eventBtnType);
        if (isTicket) {
            holder.mGotoTicketTv.setText("抢门票");
        } else {
            holder.mGotoTicketTv.setText("证件注册");
        }
        holder.mLimitTickeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IOUtils.isLogin(mContext)) {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    if (isTicket) {
                        args.put(Constants.IntentParams.ID, modelData.eventId);
                        args.put(Constants.IntentParams.DATA, modelData.eventName);
                        args.put(Constants.IntentParams.DATA2, modelData.eventCover);
                        CommonUtils.startNewActivity(mContext, args, PreRegistrationActivity.class);
                    } else {
                        args.put(Constants.IntentParams.DATA, modelData.eventName);
                        args.put(Constants.IntentParams.DATA2, modelData.eventCover);
                        args.put(Constants.IntentParams.ID, modelData.eventId + "");
                        args.put(Constants.IntentParams.ID2, "");
                        CommonUtils.startNewActivity(mContext, args, CheckinActivity.class);
                    }
                }
            }
        });
        holder.mBranddiscountMaxprice.setText(modelData.title);
        return convertView;
    }


    private View handleLimitNumView(int position, View convertView, ViewGroup parent) {
        HomeLimitnumViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_numdiscount, parent, false);
            holder = new HomeLimitnumViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeLimitnumViewHolder) convertView.getTag();
        }
                 /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        final HomeModel.ModelData modelData = item.modelData;
        ArrayList<String> benefitAttr = modelData.benefitAttr;
        HomeModel.Org org = modelData.org;
        holder.mHomeCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
        if (benefitAttr != null && benefitAttr.size() != 0) {
            holder.mHomeMygridview.setAdapter(new WrapAdapter<String>(mContext, R.layout.listitem_newlimitdiscount_item, benefitAttr) {
                @Override
                public void convert(ViewHolder helper, String item) {
                    helper.setText(R.id.name, item);
                }
            });
        }
        holder.mHomeTitleTv.setText(modelData.title);
        if(modelData.rateType==3){
            holder.mBranddiscountMaxprice.setText(Html.fromHtml( modelData.rateValue + "折  \t<font color=\"#333333\">\t 限量" + modelData.saleNum + "台</font>"));

        }else {
            holder.mBranddiscountMaxprice.setText(Html.fromHtml("降￥" + modelData.rateValue + "  \t<font color=\"#333333\">\t 限量" + modelData.saleNum + "台</font>"));
        }
        if (modelData.applyNum == 0) {
            holder.mHomelimtBaomingTV.setVisibility(View.VISIBLE);
            holder.mHomelimtBaomingTV.setText(modelData.applyNum + "人报名");
        } else {
            holder.mHomelimtBaomingTV.setVisibility(View.GONE);

        }
        int leftTime = DateFormatUtil.getLeftDay(modelData.limtEndTime);
        if (leftTime > 0) {
            holder.mBranddiscountData.setText("报名仅剩" + leftTime + "天");
        } else if (leftTime == 0) {
            holder.mBranddiscountData.setText("报名仅剩今天");
        } else {
            holder.mBranddiscountData.setText("报名已结束");
        }
        final boolean isTicket = "ticket".equals(modelData.eventBtnType);
        if (isTicket) {
            holder.mNumdiscountTV.setText("抢门票");
        } else {
            holder.mNumdiscountTV.setText("证件注册");
        }
        holder.mNumdiscountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IOUtils.isLogin(mContext)) {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    if (isTicket) {
                        args.put(Constants.IntentParams.ID, modelData.eventId);
                        args.put(Constants.IntentParams.DATA, modelData.eventName);
                        args.put(Constants.IntentParams.DATA2, modelData.eventCover);
                        CommonUtils.startNewActivity(mContext, args, PreRegistrationActivity.class);
                    } else {
                        args.put(Constants.IntentParams.DATA, modelData.eventName);
                        args.put(Constants.IntentParams.DATA2, modelData.eventCover);
                        args.put(Constants.IntentParams.ID, modelData.eventId + "");
                        args.put(Constants.IntentParams.ID2, "");
                        CommonUtils.startNewActivity(mContext, args, CheckinActivity.class);
                    }
                }
            }
        });
        holder.mLatestExhibitionRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 3);
                mContext.switchToTab(Constants.Navigation.SEE_TAB);
            }
        });
        holder.mHomeCoverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, item.id + "");
                CommonUtils.startNewActivity(mContext, args, LimitDiscountNewDetailsOddsActivity.class);
            }
        });
        return convertView;
    }

    private View handleLimitTimeView(int position, View convertView, ViewGroup parent) {
        HomeLimittimeViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_limitdiscount, parent, false);
            holder = new HomeLimittimeViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeLimittimeViewHolder) convertView.getTag();
        }
               /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        HomeModel.ModelData modelData = item.modelData;
        ArrayList<String> benefitAttr = modelData.benefitAttr;
        final HomeModel.Org org = modelData.org;
        holder.mHomeCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
        if (benefitAttr != null && benefitAttr.size() != 0) {
            holder.mHomeMygridview.setAdapter(new WrapAdapter<String>(mContext, R.layout.listitem_newlimitdiscount_item, benefitAttr) {
                @Override
                public void convert(ViewHolder helper, String item) {
                    helper.setText(R.id.name, item);
                }
            });
        }
        holder.mHomeTitleTv.setText(item.title);
        long leftTime = DateFormatUtil.getTime(modelData.limtEndTime) - System.currentTimeMillis();
        if (leftTime > 0) {
            holder.limitTimeLayout.setVisibility(View.VISIBLE);
            holder.mLimittimeSettimetext.start(DateFormatUtil.getTime(modelData.limtEndTime) - System.currentTimeMillis());
        } else {
            holder.limitTimeLayout.setVisibility(View.GONE);
        }
        holder.mHomeStoreAddressTv.setText(org.address);
        holder.mHomeStoreNameTv.setText(org.name);
        holder.mHomeStoreCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(org.logo, 360, 360)));
        holder.mHomeTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 3);
                mContext.switchToTab(Constants.Navigation.SEE_TAB);
            }
        });
        holder.mHomeCoverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, item.id + "");
                CommonUtils.startNewActivity(mContext, args, LimitDiscountNewDetailsActivity.class);
            }
        });
        holder.mHomeStoreTelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemInvoker.launchDailPage(mContext,org.tel);
            }
        });
        return convertView;
    }

    private View handleSalerView(final int position, View convertView, ViewGroup parent) {
        HomeSalecarViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_salecar, parent, false);
            holder = new HomeSalecarViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeSalecarViewHolder) convertView.getTag();
        }
                /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        HomeModel.ModelData modelData = item.modelData;
        HomeModel.CreateUser createUser = item.createUser;
        int type = 0; //0是车主自售 1展会现场车主自售
        if (item.model.equals(EVENT_GOODS)) {
            type = 1;
        }
        holder.mHomeAvatarIv.setImageURI(URLUtil.builderImgUrl(createUser.avatar, 144, 144));
        holder.mHomeUserNameTv.setText(createUser.nickname);
        holder.mHomeCreateTimeTv.setText(item.createTime);
        if (createUser.sex == 1) {
            holder.mHomeGenderImg.setImageResource(R.drawable.icon_men);
        } else {
            holder.mHomeGenderImg.setImageResource(R.drawable.icon_women);
        }
        if (createUser.isDoyen == 1) {
            holder.mHomeIsDarenIv.setVisibility(View.VISIBLE);
        } else {
            holder.mHomeIsDarenIv.setVisibility(View.GONE);
        }
        if (createUser.isModel == 1) {
            holder.mHomeIsModelIv.setVisibility(View.VISIBLE);
        } else {
            holder.mHomeIsModelIv.setVisibility(View.GONE);
        }
        if (TextUtil.isEmpty(item.localAddress)) {
            holder.mHomeCreatePlaceTv.setVisibility(View.INVISIBLE);
        } else {
            holder.mHomeCreatePlaceTv.setVisibility(View.VISIBLE);
            holder.mHomeCreatePlaceTv.setText(item.localAddress);
        }

        holder.mHomeBrandLogoIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(createUser.masterBrand, 360, 360)));
        //浏览，点赞，评论数
        holder.mHomeVisitorsNumTv.setText(item.getViewCount() + "");
        holder.mHomePraiseNumIv.setText(item.getDiggCount() + "");
        holder.mHomeCommentNumTv.setText(item.getReplyCount() + "");
        if (item.isPraise == 1) {
            holder.mHomeIsPraiseIv.setImageResource(R.drawable.icon_likes);
        } else {
            holder.mHomeIsPraiseIv.setImageResource(R.drawable.icon_like);
        }
        //中间不同部分
        holder.mHomeIsPraiseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IOUtils.isLogin(mContext)){
                    if(item.model.equals(EVENT_GOODS)){
                        mHomePagePresenter.executePraise(token, EVENT_GOODS, item.id + "", position);
                    }else {
                        mHomePagePresenter.executePraise(token, "thread", item.id + "", position);
                    }
                }
            }
        });
        holder.mHomeCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
        //不同部分
        String price = CommonUtils.getMoneyType(modelData.price + "");
        if (modelData.price==0) {
            price = "面议";
        } else {
            price ="￥"+price;
        }
        holder.mHomeSalerPriceTv.setText(price);
        if (type == 0) {
            holder.mHomeSalerExhibitLayout.setVisibility(View.GONE);
            holder.mHomeSalerGoTicketBtn.setVisibility(View.GONE);
            holder.mHomeTitleTv.setText("他发布了二手车信息，您可能感兴趣!");
        } else {
            holder.mHomeTitleTv.setText("他发布了二手车现场出售信息，您可能感兴趣!");
            holder.mHomeSalerGoTicketBtn.setVisibility(View.VISIBLE);
            holder.mHomeSalerExhibitLayout.setVisibility(View.VISIBLE);
        }
        holder.mHomeSalerTitleTv.setText(item.title);

        return convertView;
    }

    private View handleVideoView(int position, View convertView, ViewGroup parent) {
        HomeVideoViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_video, parent, false);
            holder = new HomeVideoViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeVideoViewHolder) convertView.getTag();
        }

        /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        HomeModel.ModelData modelData = item.modelData;
        HomeModel.CreateUser createUser = item.createUser;

        holder.mHomeAvatarIv.setImageURI(URLUtil.builderImgUrl(createUser.avatar, 144, 144));
        holder.mHomeUserNameTv.setText(createUser.nickname);
        holder.mHomeCreateTimeTv.setText(item.createTime);
        if (createUser.sex == 1) {
            holder.mHomeGenderImg.setImageResource(R.drawable.icon_men);
        } else {
            holder.mHomeGenderImg.setImageResource(R.drawable.icon_women);
        }
        if (createUser.isDoyen == 1) {
            holder.mHomeIsDarenIv.setVisibility(View.VISIBLE);
        } else {
            holder.mHomeIsDarenIv.setVisibility(View.GONE);
        }
        if (createUser.isModel == 1) {
            holder.mHomeIsModelIv.setVisibility(View.VISIBLE);
        } else {
            holder.mHomeIsModelIv.setVisibility(View.GONE);
        }
        if (createUser.isFollow == -1) {
            holder.mHomeDoMeIv.setVisibility(View.VISIBLE);
//            holder.mAddFellowIv.setVisibility(View.GONE);
        } else {
            holder.mHomeDoMeIv.setVisibility(View.GONE);
        }
        holder.mHomeCreatePlaceTv.setText(item.localAddress);
        holder.mHomeCreateDistanceTv.setText(item.distance + "km");
        holder.mBrandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(createUser.masterBrand, 360, 360)));
        //浏览，点赞，评论数
        holder.mHomeVisitors.setText(item.getViewCount() + "");
        holder.mHomePraiseNumTv.setText(item.getDiggCount() + "");
        holder.mHomeCommentNumTv.setText(item.getReplyCount() + "");
        if (item.isPraise == 1) {
            holder.mHomeIsPraiseIv.setImageResource(R.drawable.icon_likes);
        } else {
            holder.mHomeIsPraiseIv.setImageResource(R.drawable.icon_like);
        }
        holder.mHomeCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
        return convertView;
    }

    //开车去，请你玩活动
    private View handleHuoDongView(int position, View convertView, ViewGroup parent) {
        final HomeHuoDongViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_huodong, parent, false);
            holder = new HomeHuoDongViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeHuoDongViewHolder) convertView.getTag();
        }
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        HomeModel.CreateUser createUser = item.createUser;
        HomeModel.ModelData modelData = item.modelData;
        HomeModel.Org org = modelData.org;
        final boolean isDriving = item.model.equals(CYCLE);

        holder.avatarIv.setImageURI(URLUtil.builderImgUrl(createUser.avatar, 144, 144));
        holder.mUserNameTv.setText(createUser.nickname);
        holder.mCreateTimeTv.setText(item.createTime);
        if (createUser.sex == 1) {
            holder.genderImg.setImageResource(R.drawable.icon_men);
        } else {
            holder.genderImg.setImageResource(R.drawable.icon_women);
        }
        if (createUser.isDoyen == 1) {
            holder.mHotmanIv.setVisibility(View.VISIBLE);
        } else {
            holder.mHotmanIv.setVisibility(View.GONE);
        }
        if (createUser.isModel == 1) {
            holder.mModelIv.setVisibility(View.VISIBLE);
        } else {
            holder.mModelIv.setVisibility(View.GONE);
        }

        if (createUser.isFollow == -1) {
            holder.mDomoreIv.setVisibility(View.VISIBLE);
            holder.homePopWindow.setmItemClickListener(new HomePopWindow.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    CommonUtils.showToast(mContext, "position" + position);
                    if (isDriving) {
                        if (position == 0) {//编辑开车去
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.id + "");
                            CommonUtils.startNewActivity(mContext, args, CompileDrivingActivity.class);
                        }

                    } else {
                        if (position == 0) {//编辑请你玩
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.id + "");
                            CommonUtils.startNewActivity(mContext, args, CompilePleasePlayActivity.class);
                        }
                    }

                }
            });
            holder.mDomoreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.homePopWindow.showPopupWindow(holder.mDomoreIv);
                }
            });
        } else {
            holder.mDomoreIv.setVisibility(View.GONE);
        }
        if (item.isPraise == 1) {
            holder.mIsFellow.setImageResource(R.drawable.icon_likes);
        } else {
            holder.mIsFellow.setImageResource(R.drawable.icon_like);
        }
        holder.mBrandIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.createUser.masterBrand, 360, 360)));
        holder.mTitleTv.setText(mHomeList.get(position).title);
        holder.mCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 540, 270)));
        holder.mVisitors.setText(item.getViewCount() + "");
        holder.mFocuson.setText(item.getDiggCount() + "");
        holder.mComment.setText(item.getReplyCount() + "");
        holder.mJoinNumTv.setText("已报名:" + modelData.applyNum + "人/上限:" + modelData.limitMax + "人");
        if (TextUtil.isEmpty(item.localAddress)) {
            holder.mCreatePlaceLayout.setVisibility(View.GONE);
        } else {
            holder.mCreatePlaceLayout.setVisibility(View.VISIBLE);
            holder.mCreatePlaceTv.setText(item.localAddress);
            holder.mDistanceTv.setText(item.distance + "km");
        }

        holder.mTimeTv.setText(modelData.beginTime + "至" + modelData.endTime);
        if (isDriving) {
            holder.mTypeTv.setText("发布了开车去活动，一起来加入吧！");
            holder.mDirectionTv.setVisibility(View.VISIBLE);
            holder.mDirectionTv.setText("目的地:" + modelData.destination);
            holder.mOrgLayout.setVisibility(View.GONE);
        } else {
            holder.mDirectionTv.setVisibility(View.GONE);
            holder.mTypeTv.setText("发布了请你玩活动，一起来加入吧！");
            holder.mOrgLayout.setVisibility(View.VISIBLE);
            if (org != null)
                holder.mOrgCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(org.logo)));
            holder.mOrgNameTv.setText(Html.fromHtml("<font color=\"#4e81ba\">" + org.name + "</font> 诚邀"));
        }
                //该活动状态
         String activityStatus = modelData.activityStatus;
        if (activityStatus.equals("报名中")) {
            holder.mHomeHuodonStatusIv.setImageResource(R.drawable.btn_apply);
        } else if (activityStatus.equals("活动结束")) {
            holder.mHomeHuodonStatusIv.setImageResource(R.drawable.btn_finish);
        } else if (activityStatus.equals("活动中")) {
            holder.mHomeHuodonStatusIv.setImageResource(R.drawable.btn_hdz);
        } else if (activityStatus.equals("报名截止")) {
            holder.mHomeHuodonStatusIv.setImageResource(R.drawable.btn_applystop);
        }
        return convertView;
    }

    //说说，话题，达人评测
    private View handleSayView(final int position, View convertView, final ViewGroup parent) {
        final HomeSayViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_home_say, parent, false);
            holder = new HomeSayViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HomeSayViewHolder) convertView.getTag();
        }
        int type = 0;//默认0 ，1为话题，2达人评测

        /*头部底部相同部分*/
        final HomeModel.HomeModelItem item = mHomeList.get(position);
        final HomeModel.ModelData modelData = item.modelData;
        final HomeModel.CreateUser createUser = item.createUser;
        HomeModel.Topic topic = modelData.topic;
        //话题
        if (topic != null && topic.id != 0) {
            type = 1;
        } else if (item.model.equals(EVALUATE)) {
            type = 2;
        } else {
            type = 0;
        }
        holder.mAvatarIv.setImageURI(URLUtil.builderImgUrl(createUser.avatar, 144, 144));
        holder.mUsernameTv.setText(createUser.nickname);
        holder.mCreateTimeTv.setText(item.createTime);
        if (createUser.sex == 1) {
            holder.mGenderImg.setImageResource(R.drawable.icon_men);
        } else {
            holder.mGenderImg.setImageResource(R.drawable.icon_women);
        }
        if (createUser.isDoyen == 1) {
            holder.mPopmanTv.setVisibility(View.VISIBLE);
        } else {
            holder.mPopmanTv.setVisibility(View.GONE);
        }
        if (createUser.isModel == 1) {
            holder.mModelTv.setVisibility(View.VISIBLE);
        } else {
            holder.mModelTv.setVisibility(View.GONE);
        }
        if (createUser.isFollow == -1) {
            holder.mMeDoIv.setVisibility(View.VISIBLE);
            holder.mAddFellowIv.setVisibility(View.GONE);
            holder.homePopWindow.setmItemClickListener(new HomePopWindow.OnItemClickListener() {
                @Override
                public void onItemClick(int position2) {
                    if (position2 == 1) {
                        tabhomeFrag.showShare(position);
                    }else if(position2==2){
                        mHomePagePresenter.deleteThread(token,item.id+"",position);
                    }else if(position2==0){
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.EDIT_TOPIC, true);
                        args.put(Constants.IntentParams.THREAD_ID, item.id+"");
                        args.put(Constants.IntentParams.LAT, kPlayCarApp.getLattitude());
                        args.put(Constants.IntentParams.LNG, kPlayCarApp.getLongitude());
                        CommonUtils.startNewActivity(mContext, args, PostingActivity.class);


//                        Map<String, Serializable> args = new HashMap<String, Serializable>();
//                        args.put(Constants.IntentParams.FROM_ID,item.id);
//                        CommonUtils.startNewActivity(mContext, args, PostingActivity.class);
                    }
                }
            });
//            if(type==0){
//                holder.homePopWindow.editeLayout.setVisibility(View.GONE);
//            }
            holder.mMeDoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.model.equals(EVALUATE)) {
                        holder.homePopWindow.editeLayout.setVisibility(View.GONE);
                    }
                    holder.homePopWindow.showPopupWindow(holder.mMeDoIv);
                }
            });
        } else {
            holder.mMeDoIv.setVisibility(View.GONE);
            holder.mAddFellowIv.setVisibility(View.VISIBLE);
            if (createUser.isFollow == 0) {
                holder.mAddFellowIv.setImageResource(R.drawable.btn_guanzbu);
            } else {
                holder.mAddFellowIv.setImageResource(R.drawable.btn_yiguanzhu);
            }
            holder.mAddFellowIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (createUser.isFollow == 0) {
                        mHomePagePresenter.getFollowAdd(token, createUser.id + "", position);
                    } else {
                        mHomePagePresenter.getFollowCancel(token, createUser.id + "", position);
                    }

                }
            });
        }
        if (TextUtil.isEmpty(item.localAddress)) {
            holder.mCreatePlaceLayout.setVisibility(View.GONE);
        } else {
            holder.mCreatePlaceLayout.setVisibility(View.VISIBLE);
            holder.mCreatePlaceTv.setText(item.localAddress);
            holder.mCreateDistanceTv.setText(item.distance + "km");
        }

        holder.mBrandLogoIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(createUser.masterBrand, 360, 360)));
        //浏览，点赞，评论数
        holder.mVisitors.setText(item.getViewCount() + "");
        holder.mPraiseNumTv.setText(item.getDiggCount() + "");
        holder.mComment.setText(item.getReplyCount() + "");
        if (item.isPraise == 1) {
            holder.mIsPraiseIv.setImageResource(R.drawable.icon_likes);
        } else {
            holder.mIsPraiseIv.setImageResource(R.drawable.icon_like);
        }
        //中间不同部分
        holder.mIsPraiseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IOUtils.isLogin(mContext))
                mHomePagePresenter.executePraise(token, "thread", item.id + "", position);
            }
        });

        //话题
        if (type == 1) {
            holder.mTopicLayout.setVisibility(View.VISIBLE);
            holder.mTopicCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(topic.cover, 360, 360)));
            holder.mTopicFellowNumTv.setText(topic.threadNum + "帖子" + topic.fansNum + "粉丝");
            holder.mTopincTagTv.setText("#" + topic.title + "#");

        } else {
            holder.mTopicLayout.setVisibility(View.GONE);

        }
        String tmpTtiltStr="";
        if(!TextUtil.isEmpty(item.title)){
            StringBuilder titleSb = new StringBuilder("【").append(item.title).append("】").append(modelData.content);
            tmpTtiltStr = titleSb.toString();
        }else {
            tmpTtiltStr = modelData.content;

        }
        if(tmpTtiltStr.toString().length()>140){
            tmpTtiltStr =  tmpTtiltStr.substring(0,140);
            holder.mTitletv.setText(Html.fromHtml(tmpTtiltStr + "...<font color=\"#4e81ba\"> 详情>></font>"));
        }else {
            holder.mTitletv.setText(tmpTtiltStr);
        }
//        if (type == 1 || type == 2) {
//            holder.mTitletv.setText(Html.fromHtml(item.type + "<font color=\"#4e81ba\"> 详情>></font>"));
//            holder.mTitletv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CommonUtils.showToast(mContext, item.type);
//                }
//            });
//        } else {
//            //标题
//            holder.mTitletv.setText(modelData.content);
//        }


        //图片处理
        List<String> imgUrls = modelData.imageList;
        if ((imgUrls == null) || imgUrls.size() == 0) {
            holder.nineGrid.setVisibility(View.GONE);
            holder.oneIv.setVisibility(View.GONE);
        } else if (imgUrls.size() == 1) {
            holder.oneIv.setVisibility(View.VISIBLE);
            holder.nineGrid.setVisibility(View.GONE);
            handlerOneImage(holder.oneIv, URLUtil.builderImgUrl(imgUrls.get(0)));
            holder.oneIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toInent = new Intent(mContext, BigPhotoPageActivity.class);
                    toInent.putStringArrayListExtra(Constants.KEY_IMGS, modelData.imageList);
                    toInent.putExtra(Constants.IntentParams.INDEX, 0);
                    mContext.startActivity(toInent);
                }
            });
        } else {
            holder.nineGrid.setVisibility(View.VISIBLE);
            holder.oneIv.setVisibility(View.GONE);
            holder.nineGrid.setImagesData(modelData.imageList);
            holder.nineGrid.setOnItemClickListerer(new NineGridlayout.OnItemClickListerer() {
                @Override
                public void onClick(int position2) {
                    Intent toInent = new Intent(mContext, BigPhotoPageActivity.class);
                    toInent.putStringArrayListExtra(Constants.KEY_IMGS, modelData.imageList);
                    toInent.putExtra(Constants.IntentParams.INDEX, position2);
                    mContext.startActivity(toInent);
                }
            });
        }
        return convertView;
    }

    private void handlerOneImage(final CustomImageView ivOne, final String imageUrl) {

//        viewHolder.ivOne.setImageUrl(image.getUrl());
//        Glide.with(context).load(image.getUrl()).into(viewHolder.ivOne);
        Glide.with(mContext)
                .load(imageUrl)
                .asBitmap()
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        cacluateSize(resource.getWidth(), resource.getHeight());
                        ViewGroup.LayoutParams layoutparams = ivOne.getLayoutParams();
                        layoutparams.height = (int) (finalHeight + 0.5D);
                        layoutparams.width = (int) (finalWidth + 0.5D);
                        ivOne.setLayoutParams(layoutparams);
                        ivOne.setClickable(true);
                        ivOne.setScaleType(ImageView.ScaleType.MATRIX);
                        Glide.with(mContext)
                                .load(imageUrl)
                                .transform(new MyBitmapTransformation(mContext))
                                .override(layoutparams.width, layoutparams.height)
                                .into(ivOne);
                    }

                    private double finalWidth;
                    private double finalHeight;

                    private void cacluateSize(int imageWidth, int imageHeight) {
                        ScreenUtil screentools = ScreenUtil.getInstance(mContext);
                        int maxSize = DensityUtils.dip2px(mContext, 230);
                        int minSize = DensityUtils.dip2px(mContext, 174);
                        if (imageWidth > imageHeight) {
                            finalHeight = imageHeight * maxSize / imageWidth;
                            finalWidth = maxSize;
                            if (finalWidth / finalHeight >= 3.5) {
                                finalWidth = screentools.getScreenWidth() - DensityUtils.dip2px(mContext, 30);
                                finalHeight = finalWidth * 0.32;
                            } else if (finalHeight < minSize) {
                                finalHeight = minSize;
                            }

                        } else {
                            finalWidth = imageWidth * maxSize / imageHeight;
                            finalHeight = maxSize;
                            if (finalWidth < minSize) {
                                finalWidth = minSize;
                            }
                        }


                    }

                });


    }

    //展会menu
    private void gotoMenu(int id, int exhibitId) {
        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.ID, exhibitId);
        if (id == 1) {
            Integer tmeExhibitId = exhibitId;
            KPlayCarApp.putValue(Constants.IntentParams.EXHIBIT_ID, tmeExhibitId);
            KPlayCarApp.putValue(Constants.IntentParams.INDEX, 3);
            mContext.switchToTab(Constants.Navigation.SEE_TAB);
        } else if (id == 2) {
            CommonUtils.startNewActivity(mContext, args, ExhibitionEventActivity.class);
        } else if (id == 3) {
            CommonUtils.startNewActivity(mContext, args, ExhibitionNewsActivity.class);
        } else if (id == 4) {
            CommonUtils.startNewActivity(mContext, args, ExhibitPalaceListActivity.class);
        } else if (id == 5) {
            Intent toWebIntent = new Intent(mContext, WebViewActivity.class);
            toWebIntent.putExtra(Constants.IntentParams.TITLE, "观展攻略");
//            toWebIntent.putExtra(Constants.IntentParams.DATA,
//                    "http://car.i.cacf.cn/#/app/event/taste/" + exhibitId);
            toWebIntent.putExtra(Constants.IntentParams.DATA,
                    Constants.H5.WAP_LINK + "/#/app/event/taste/" + exhibitId);
            mContext.startActivity(toWebIntent);
        } else if (id == 6) {
            CommonUtils.startNewActivity(mContext, args, NewCarLaunchActivity.class);
        } else if (id == 7) {
            CommonUtils.startNewActivity(mContext, args, ModelListActivity.class);
            //            CommonUtils.startNewActivity(mContext, com.tgf.kcwc.see.model.ModelListActivity.class);
        } else if (id == 10) {
            CommonUtils.startNewActivity(mContext, args, BrandModelsActivity.class);
        } else if (id == 11) {
            Integer tmeExhibitId = exhibitId;
            KPlayCarApp.putValue(Constants.IntentParams.EXHIBIT_ID, tmeExhibitId);
            KPlayCarApp.putValue(Constants.IntentParams.INDEX, 4);
            mContext.switchToTab(Constants.Navigation.SEE_TAB);
        }
    }
}
