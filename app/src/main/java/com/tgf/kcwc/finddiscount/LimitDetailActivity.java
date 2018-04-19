package com.tgf.kcwc.finddiscount;

import java.util.ArrayList;
import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.DiscountLimitModel;
import com.tgf.kcwc.mvp.model.GiftPackDetailModel;
import com.tgf.kcwc.mvp.model.LimitDiscountDetailModel;
import com.tgf.kcwc.mvp.model.OrgModel;
import com.tgf.kcwc.mvp.presenter.GiftPackDetailPresent;
import com.tgf.kcwc.mvp.presenter.LimitDetailPresent;
import com.tgf.kcwc.mvp.view.GiftPackDetailView;
import com.tgf.kcwc.mvp.view.LimitDiscountDetailView;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/4/26 0026
 * E-mail:hekescott@qq.com
 */

public class LimitDetailActivity extends BaseActivity implements LimitDiscountDetailView {

    private TextView                               mGiftTitleTv;
    private int                                    mGiftId = 4;
    private LimitDetailPresent                     giftPackDetailPresent;
    private SimpleDraweeView                       headCoverIv;
    private TextView                               mGiftDescTv;
    private NestFullListView                       orgLv;
    private NestFullListView                       carLv;
    private NestFullListViewAdapter                mGiftPackStoreAdapter;
    private List<LimitDiscountDetailModel.GiftCar> mGiftCars;
    private View                                   carMoreRl;
    private NestFullListViewAdapter                giftPackCarAdapter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("优惠详情");
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        mGiftTitleTv = (TextView) findViewById(R.id.giftpack_title);
        mGiftDescTv = (TextView) findViewById(R.id.gifttpack_desc);
        orgLv = (NestFullListView) findViewById(R.id.giftpack_orglv);
        carLv = (NestFullListView) findViewById(R.id.giftpack_fitgoodslv);
        headCoverIv = (SimpleDraweeView) findViewById(R.id.giftpack_cover);
        Intent fromIntent = getIntent();
        mGiftId = fromIntent.getIntExtra(Constants.IntentParams.ID, 4);
        giftPackDetailPresent = new LimitDetailPresent();
        giftPackDetailPresent.attachView(this);
        giftPackDetailPresent.getDiscountLimitDetail(mGiftId);
        carMoreRl = findViewById(R.id.giftpack_carmore);
        carMoreRl.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limitdiscountdetail);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    public void showOrgList(List<OrgModel> giftOrgs) {
        mGiftPackStoreAdapter = new NestFullListViewAdapter<OrgModel>(
            R.layout.listitem_giftpack_org, giftOrgs) {
            @Override
            public void onBind(int pos, final OrgModel limitOrg, NestFullViewHolder holder) {
                String url = URLUtil.builderImgUrl(limitOrg.logo, 360, 360);
                holder.setSimpleDraweeViewURL(R.id.giftpack_orgcover, url);
                holder.setText(R.id.giftpack_orgtitle, limitOrg.full_name);
                holder.setText(R.id.giftpack_orgaddress, limitOrg.address);
                holder.getView(R.id.giftpack_call)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SystemInvoker.launchDailPage(getContext(), limitOrg.tel);
                            }
                        });
            }
        };
        orgLv.setAdapter(mGiftPackStoreAdapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        giftPackDetailPresent.detachView();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.giftpack_carmore:
                carLv.notifyDataSetChange(mGiftCars);
                carMoreRl.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void showHead(final LimitDiscountDetailModel giftPackDetailModel) {
        mGiftTitleTv.setText(giftPackDetailModel.title);
        String couverUrl = URLUtil.builderImgUrl(giftPackDetailModel.cover, 540, 270);
        headCoverIv.setImageURI(Uri.parse(couverUrl));
        mGiftDescTv.setText(giftPackDetailModel.desc);
    }

    @Override
    public void showGoodsList(List<LimitDiscountDetailModel.GiftCar> giftCars) {
        mGiftCars = giftCars;
        List<LimitDiscountDetailModel.GiftCar> tempList = new ArrayList<>();
        if (giftCars != null) {
            if (giftCars.size() > 2) {
                tempList = giftCars.subList(0, 2);
                carMoreRl.setVisibility(View.VISIBLE);
            } else {
                tempList = giftCars;
                carMoreRl.setVisibility(View.GONE);
            }
        }
        //                if(!TextUtils.isEmpty(giftCar.pic)){
        //                }
        giftPackCarAdapter = new NestFullListViewAdapter<LimitDiscountDetailModel.GiftCar>(
            R.layout.listitem_giftpack_car, tempList) {
            @Override
            public void onBind(int pos, LimitDiscountDetailModel.GiftCar giftCar,
                               NestFullViewHolder holder) {
                holder.setText(R.id.giftpack_cartitle,
                    giftCar.factory + giftCar.carSeriesName + giftCar.carName);
                //                if(!TextUtils.isEmpty(giftCar.pic)){
                String url = URLUtil.builderImgUrl(giftCar.pic, 270, 203);
                holder.setSimpleDraweeViewURL(R.id.giftpack_carcover, url);
                //                }

            }
        };

        carLv.setAdapter(giftPackCarAdapter);
    }
}
