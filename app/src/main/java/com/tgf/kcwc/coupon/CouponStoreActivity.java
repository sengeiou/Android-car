package com.tgf.kcwc.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponDetailModel;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/10 0010
 * E-mail:hekescott@qq.com
 */

public class CouponStoreActivity extends BaseActivity {
    private ListView couponstoreStoresLv;
    private List<Coupon> couponList = new ArrayList<Coupon>();
    private WrapAdapter couponStoreAdapter;
    private Intent fromIntent;
    private List<CouponDetailModel.Dealers> mDealers;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("适用门店");
    }

    @Override
    protected void setUpViews() {
        couponstoreStoresLv = (ListView) findViewById(R.id.couponstore_stores_lv);
        fromIntent = getIntent();
        mDealers = (List<CouponDetailModel.Dealers>) fromIntent.getSerializableExtra(Constants.IntentParams.DATA);
        initData();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponstore);

        couponStoreAdapter = new WrapAdapter<CouponDetailModel.Dealers>(mContext, R.layout.listitem_couponstore,mDealers) {
            @Override
            public void convert(ViewHolder helper, final CouponDetailModel.Dealers item) {
                helper.setText(R.id.name,(helper.getPosition()+1)+". "+item.name);
                RatingBar ratingBar =helper.getView(R.id.ratingBar);
                ratingBar.setVisibility(View.INVISIBLE);
                helper.setText(R.id.near_area_tv,item.address);
                helper.setText(R.id.near_distance_tv,item.getDistance());
                helper.getView(R.id.img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SystemInvoker.launchDailPage(CouponStoreActivity.this,item.tel);
                    }
                });
            }
        };
        couponstoreStoresLv.setAdapter(couponStoreAdapter);
        couponstoreStoresLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toOrgdetail = new Intent(CouponStoreActivity.this, DealerHomeActivity.class);
                toOrgdetail.putExtra(Constants.IntentParams.ID, mDealers.get(position).id + "");
                toOrgdetail.putExtra(Constants.IntentParams.TITLE, mDealers.get(position).fullName);
                toOrgdetail.putExtra(Constants.IntentParams.INDEX, 0);
                startActivity(toOrgdetail);
            }
        });
    }

    private void initData() {
        for(int i=0;i<10;i++){
            Coupon coupon = new Coupon();
            coupon.name ="nig";
            couponList.add(coupon);
        }

    }
}
