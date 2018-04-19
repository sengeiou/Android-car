package com.tgf.kcwc.coupon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;
import com.tgf.kcwc.mvp.model.RefondDetailModel;
import com.tgf.kcwc.mvp.presenter.CouponRefondDetailPresenter;
import com.tgf.kcwc.mvp.view.CouponRefondDetailView;
import com.tgf.kcwc.util.ArrayUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/8/11 0011
 * E-mail:hekescott@qq.com
 */

public class CouponRefundDetailActivity extends BaseActivity implements CouponRefondDetailView {
    private Intent fromIntent;
    private int codeId;
    private SimpleDraweeView couponCoverIv;
    private TextView couponTitle;
    private TextView couponDesc;
    private TextView couponNowprice;
    private TextView couponOldprice;
    private TextView backStatus;
    private String token;
    private CouponRefondDetailPresenter couponRefondDetailPresenter;
//    private TextView refondSnTv;
//    private TextView reFondStatusTv;
    private ListView refondProgressLv;
    private NestFullListView refondCodesLv;

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        codeId = fromIntent.getIntExtra(Constants.IntentParams.ID, 0);
        token = IOUtils.getToken(getContext());
        couponRefondDetailPresenter = new CouponRefondDetailPresenter();
        couponRefondDetailPresenter.attachView(this);
        couponRefondDetailPresenter.getCouponRefondDetail(token, codeId);
        couponCoverIv = (SimpleDraweeView) findViewById(R.id.couponlist_cover);
        couponTitle = (TextView) findViewById(R.id.listitem_recoment_coupontitle);
        couponDesc = (TextView) findViewById(R.id.couponlist_desc);
        couponNowprice = (TextView) findViewById(R.id.recyleitem_near_nowprice);
        couponOldprice = (TextView) findViewById(R.id.listviewitem_recomment_oldprice);
        backStatus = (TextView) findViewById(R.id.refond_backstatusTv);
        refondProgressLv = (ListView) findViewById(R.id.refond_progresslv);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
            backEvent(back);
            text.setText("退款详情");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_refounddetail);
    }
    private TextView mCouponMoneytv;
    private TextView mCouponRefondusertv;
    private TextView mCouponRefondTimetv;
    @Override
    public void showHead(CouponOrderDetailModel.OrderDetailCoupon coupon) {
        couponCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(coupon.cover, 270, 203)));
        couponTitle.setText(coupon.title);
        couponDesc.setText(coupon.desc);
        couponNowprice.setText("");
        double nowPrice = Double.parseDouble(coupon.price);
        if (nowPrice == 0) {
            couponNowprice.setText("免费");
            couponNowprice.setTextColor(mRes.getColor(R.color.voucher_green));
        } else {
            couponNowprice.setText("￥ " + nowPrice);
            couponNowprice.setTextColor(mRes.getColor(R.color.voucher_yellow));
        }
        SpannableString demoPrice = SpannableUtil.getDelLineString("￥ " + coupon.denomination);
        couponOldprice.setText(demoPrice);
    }

    @Override
    public void showRefondInfo(final RefondDetailModel refondDetailModel) {
        refondCodesLv = (NestFullListView) findViewById(R.id.refound_detailSnLv);
        mCouponMoneytv = (TextView) findViewById(R.id.coupon_Moneytv);
        mCouponRefondusertv = (TextView) findViewById(R.id.coupon_refondusertv);
        mCouponRefondTimetv = (TextView) findViewById(R.id.coupon_refondTimetv);
        mCouponMoneytv.setText(refondDetailModel.refund_money);
        mCouponRefondusertv.setText(refondDetailModel.refund_type);
        mCouponRefondTimetv.setText(refondDetailModel.refund_success_time);

        final String[] codeArys = refondDetailModel.codes.split(",");
        List<String> codeList = new ArrayList<>();
        Collections.addAll(codeList, codeArys);
        refondCodesLv.setAdapter(new NestFullListViewAdapter<String>(R.layout.listitem_refond_detail,codeList) {
            @Override
            public void onBind(int pos, String s, NestFullViewHolder holder) {
                holder.setText(R.id.refound_sntv,s);
                TextView statusTv = holder.getView(R.id.refond_backstatusTv);
                if (refondDetailModel.check_status == 1) {
                    statusTv.setText("已退款");
                    statusTv.setTextColor(mRes.getColor(R.color.text_color10));
                } else {
                    holder.setText(R.id.refond_backstatusTv,"退款中");
                }
            }
        });
    }

    @Override
    public void showRefondProgress(ArrayList<RefondDetailModel.RefondProgress> progressList) {
        refondProgressLv.setAdapter(new WrapAdapter<RefondDetailModel.RefondProgress>(getContext(),progressList,R.layout.listitem_refond_progress) {
            @Override
            public void convert(ViewHolder helper, RefondDetailModel.RefondProgress item) {
                helper.setText(R.id.refond_progressTimeTv,item.time);
                helper.setText(R.id.refond_progressActionTv,item.action);
                if(helper.getPosition()==0){
                    helper.setImageResource(R.id.refond_tvDot,R.drawable.icon_green);
                }else {
                    helper.setImageResource(R.id.refond_tvDot,R.drawable.icon_gray);
                }
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(refondProgressLv);
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
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        couponRefondDetailPresenter.detachView();
    }
}
