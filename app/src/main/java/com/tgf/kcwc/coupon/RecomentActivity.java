package com.tgf.kcwc.coupon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.VoucherMenuAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.BannerMark;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.HeaderBannerWebActivity;
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.mvp.presenter.CouponRecomentPresenter;
import com.tgf.kcwc.mvp.view.CouponRecomentView;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.SysUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyPageIndicator;
import com.tgf.kcwc.view.PageGridView;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/1/3 0003
 * E-mail:hekescott@qq.com
 */

public class RecomentActivity extends BaseActivity implements CouponRecomentView, OnBannerClickListener {
    private final int REQUEST_CODE_SERCITY = 100;
    private PageGridView pageGridView;
    private MyPageIndicator pageIndicator;
    private List<CouponCategory> menulist = new ArrayList<>();
    private List<Coupon> voucherList = new ArrayList<>();
    private VoucherMenuAdapter voucherMenuAdapter;
    private NestFullListView voucherListView;
    private CouponRecomentPresenter couponRecomentPresenter;
    private NestFullListViewAdapter nestFullListViewAdapter;
    private TextView headTileTv;
    private KPlayCarApp mKplayCarApp;
    private TextView mFilterTitle;
    private Banner recomendBanner;
    private List<BannerModel.Data> mBannerList;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        Basecolors = R.color.transparent;
        findViewById(R.id.filterLayout).setOnClickListener(this);
        pageGridView = (PageGridView) findViewById(R.id.pagegridview_menu);
        pageIndicator = (MyPageIndicator) findViewById(R.id.pageindicator);
        voucherListView = (NestFullListView) findViewById(R.id.recoment_voucherlist_lv);
        headTileTv = (TextView) findViewById(R.id.recoment_title_tv);
        couponRecomentPresenter = new CouponRecomentPresenter();
        couponRecomentPresenter.attachView(this);
        findViewById(R.id.etSearch).setOnClickListener(this);
        couponRecomentPresenter.getRecomendCategory();
        mKplayCarApp = (KPlayCarApp) getApplicationContext();
        mFilterTitle = (TextView) findViewById(R.id.filterTitle);
        mFilterTitle.setText(mKplayCarApp.locCityName);
        findViewById(R.id.voucher_back).setOnClickListener(this);
        recomendBanner = (Banner) findViewById(R.id.recomend_banner);
        couponRecomentPresenter.getRecomend(mKplayCarApp.getLattitude(), mKplayCarApp.getLongitude());
        couponRecomentPresenter.getBannerView(BannerMark.CAR_COUPON_INDEX);
        mFilterTitle.setTextColor(mRes.getColor(R.color.text_color17));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTitleBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (findViewById(R.id.recomment_root_lv)).setPadding(0, SysUtils.getStatusHeight(this), 0, 0);
        }

        pageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
            @Override
            public void onItemClick(PageGridView pageGridView, int position) {
                if (menulist.get(position).id == 0) {
                    return;
                }
                Intent tolistAct = new Intent(getContext(), CouponListActivity.class);
                tolistAct.putExtra(Constants.IntentParams.ID, menulist.get(position).id);
                tolistAct.putExtra(Constants.IntentParams.NAME, menulist.get(position).name);
                tolistAct.putExtra(Constants.IntentParams.INDEX, menulist.get(position).id);
                startActivity(tolistAct);
            }
        });

    }

    @Override
    public void showRecomendHead(Coupon headCoupon) {
        if (headCoupon != null) {
            StringBuilder sb = new StringBuilder("[").append(headCoupon.desc).append("] ").append(headCoupon.title);
            headTileTv.setText(sb.toString());
        }

    }

    @Override
    public void showRecomendList(ArrayList<Coupon> couponList) {
        voucherList = couponList;
        if (nestFullListViewAdapter == null) {
            nestFullListViewAdapter = new NestFullListViewAdapter<Coupon>(
                    R.layout.listview_item_recomment_voucherlist, voucherList) {

                @Override
                public void onBind(int pos, Coupon voucher, NestFullViewHolder holder) {
                    TextView titleTv = holder.getView(R.id.listitem_recoment_coupontitle);
                    TextView distanceTv = holder.getView(R.id.couponlist_distancetv);
                    distanceTv.setText(voucher.getDistance() );
                    titleTv.setText(voucher.title);
                    holder.setSimpleDraweeViewURL(R.id.couponlist_cover, URLUtil.builderImgUrl(voucher.cover, 270, 203));
                    double nowPrice = Double.parseDouble(voucher.price);
                    TextView nowPriceTv = holder.getView(R.id.recyleitem_near_nowprice);
                    //已领+已售
                    String type = null;
                    if (nowPrice == 0) {
                        type = "已领";
                        nowPriceTv.setText("免费");
                        nowPriceTv.setTextColor(mRes.getColor(R.color.voucher_green));
                    } else {
                        type = "已售";
                        holder.setText(R.id.recyleitem_near_nowprice, "￥ " + nowPrice);
                        nowPriceTv.setTextColor(mRes.getColor(R.color.voucher_yellow));
                    }
                    SpannableString demoPrice = SpannableUtil.getDelLineString("￥ " + voucher.denomination);
                    holder.setText(R.id.listviewitem_recomment_oldprice, demoPrice);
                    if (voucher.is_finished == 1) {
                        holder.setText(R.id.listviewitem_recomment_salenum, "抢光了");
                    } else {
                        holder.setText(R.id.listviewitem_recomment_salenum, type + voucher.sales);
                    }
                    holder.setText(R.id.couponlist_desc, voucher.descripiton);
                }
            };
            voucherListView.setAdapter(nestFullListViewAdapter);
            voucherListView.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                @Override
                public void onItemClick(NestFullListView parent, View view, int position) {
                    Intent toIntent = new Intent(mContext, CouponDetailActivity.class);
                    toIntent.putExtra(Constants.IntentParams.ID, voucherList.get(position).id);
                    startActivity(toIntent);
                }
            });
        } else {
            nestFullListViewAdapter.setDatas(couponList);
            voucherListView.updateUI();
        }
    }

    @Override
    public void showCategorylist(ArrayList<CouponCategory> couponCategoryList) {
        menulist = couponCategoryList;
        if (menulist == null || menulist.size() <= 5) {
            pageGridView.setmRows(1);
        } else {
            pageGridView.setmRows(2);
        }
        if (voucherMenuAdapter == null) {
            voucherMenuAdapter = new VoucherMenuAdapter(menulist, mContext);
            pageGridView.setAdapter(voucherMenuAdapter);
            pageGridView.setPageIndicator(pageIndicator);

        } else {
            voucherMenuAdapter.notifyDataSetChanged();
        }
        if (menulist == null || menulist.size() <= 10) {
            pageIndicator.setVisibility(View.GONE);
        }

    }

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

            recomendBanner.setImages(imgUrl).setImageLoader(new FrescoImageLoader())
                    .setOnBannerClickListener(this).start();
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voucher_back:
                finish();
                break;
            case R.id.filterLayout:
                Intent intentCity = new Intent();
                intentCity.setClass(mContext, SelectCityActivity.class);
                startActivityForResult(intentCity, REQUEST_CODE_SERCITY);
                break;
            case R.id.etSearch:
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SERCITY:
                    String cityName = data.getStringExtra(Constants.IntentParams.DATA);
                    mKplayCarApp.locCityName = cityName;
                    mFilterTitle.setText(data.getStringExtra(Constants.IntentParams.DATA));
                    mFilterTitle.setTextColor(mRes.getColor(R.color.text_color17));
                    break;
            }
        }
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
