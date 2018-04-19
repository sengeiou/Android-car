package com.tgf.kcwc.coupon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.CouponDetailModel;
import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.presenter.CouponOrderDetailPresenter;
import com.tgf.kcwc.mvp.view.CouponOrderDetailView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/8/8 0008
 * E-mail:hekescott@qq.com
 */

public class CouponOrderDetailActivity extends BaseActivity implements CouponOrderDetailView {

    private CouponOrderDetailPresenter couponOrderDetailPresenter;
    private Intent fromIntent;
    private int mOrderId;
    private NestFullListView codesLv;
    private SimpleDraweeView couponCoverIv;
    private TextView couponTitle;
    private TextView couponDesc;
    private TextView couponNowprice;
    private TextView couponOldprice;
    private TextView reFoundTypetv;
    private TextView expireDateTv;
    private RatingBar ratingBar;
    private ImageView orderTel;
    private TextView commenttv;
    private TextView mBacktv;
    private RecyclerView orderDetailOnlineRv;
    private View onlineLayout;
    private CouponOrderDetailModel mCouponOrderDetailModel;
    private ArrayList<CouponDetailModel.Dealers> mdealers;
    private KPlayCarApp kPlayCarApp;

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        kPlayCarApp = (KPlayCarApp) getApplication();
        mOrderId = fromIntent.getIntExtra(Constants.IntentParams.ID, 0);
        couponOrderDetailPresenter = new CouponOrderDetailPresenter();
        couponOrderDetailPresenter.attachView(this);
        orderDetailOnlineRv = (RecyclerView) findViewById(R.id.coupon_orderdetail_onlinerv);
        onlineLayout = findViewById(R.id.coupon_orderdetail_onlineLayout);
        codesLv = (NestFullListView) findViewById(R.id.coupon_codeslv);
        couponCoverIv = (SimpleDraweeView) findViewById(R.id.couponlist_cover);
        couponTitle = (TextView) findViewById(R.id.listitem_recoment_coupontitle);
        couponDesc = (TextView) findViewById(R.id.couponlist_desc);
        couponNowprice = (TextView) findViewById(R.id.recyleitem_near_nowprice);
        couponOldprice = (TextView) findViewById(R.id.listviewitem_recomment_oldprice);
        reFoundTypetv = (TextView) findViewById(R.id.couponrefoude_typdtv);
        expireDateTv = (TextView) findViewById(R.id.orderdetail_expiretv);
        commenttv = (TextView) findViewById(R.id.couponOder_commenttv);
        commenttv.setOnClickListener(this);
        mBacktv = (TextView) findViewById(R.id.couponOder_backtv);
        findViewById(R.id.select).setOnClickListener(this);
        findViewById(R.id.gotoorg_detailrl).setOnClickListener(this);
        mBacktv.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        couponOrderDetailPresenter.getCouponOrderdetail(IOUtils.getToken(getContext()), mOrderId, kPlayCarApp.getLattitude(), kPlayCarApp.getLongitude());
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("订单详情");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_orderdetail);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.couponOder_backtv:
                Intent toCouponRefound = new Intent(getContext(), CouponRefounActivity.class);
                toCouponRefound.putExtra(Constants.IntentParams.ID, mOrderId);
                startActivity(toCouponRefound);
                break;
            case R.id.gotoorg_detailrl:
                Intent toOrgdetail = new Intent(getContext(), DealerHomeActivity.class);
                toOrgdetail.putExtra(Constants.IntentParams.ID, mdealers.get(0).id + "");
                toOrgdetail.putExtra(Constants.IntentParams.TITLE, mdealers.get(0).fullName);
                toOrgdetail.putExtra(Constants.IntentParams.INDEX, 0);
                startActivity(toOrgdetail);
                break;
            case R.id.couponOder_commenttv:
                Intent toCouponEvaluatect = new Intent(getContext(), CouponEvaluateActivity.class);
                toCouponEvaluatect.putExtra(Constants.IntentParams.ID, mOrderId);
                startActivity(toCouponEvaluatect);
                break;
            case R.id.select:
                if (!isExpired) {
                    Intent toCouponDetailAct = new Intent(mContext, CouponDetailActivity.class);
                    toCouponDetailAct.putExtra(Constants.IntentParams.ID, mCouponOrderDetailModel.coupon.id);
                    startActivity(toCouponDetailAct);
                }

//                             Intent toCouponComment = new Intent(getContext(),CouponRefounActivity.class);
//                             toCouponRefound.putExtra(Constants.IntentParams.ID,mOrderId);
//                            startActivity(toCouponRefound);
                break;
            default:
                break;
        }
    }

    @Override
    public Context getContext() {
        return CouponOrderDetailActivity.this;
    }

    @Override
    public void showCodeList(ArrayList<MyCouponModel.CouponCode> codes) {
        codesLv.setAdapter(new NestFullListViewAdapter<MyCouponModel.CouponCode>(R.layout.listitem_coupon_orderdetail, codes) {
            @Override
            public void onBind(int pos, final MyCouponModel.CouponCode couponCode, NestFullViewHolder holder) {
                holder.setText(R.id.couponoder_codertv, "NO." + couponCode.code);
                TextView timeTV = holder.getView(R.id.couponoder_timetv);
                RelativeLayout orderDetailRoot = holder.getView(R.id.couponoder_coderl);

                timeTV.setText("有效期" + couponCode.statusTime);
                ImageView orderMoreIv = holder.getView(R.id.coupon_orderDetailIv);
//                0.待支付 1.正常待使用 2.退款中 3.已退款 4.已核销 5.已过期 6.已禁用
                TextView orderStatus = holder.getView(R.id.orderStatusTv);
                if (couponCode.status == 0) {
                    orderStatus.setText("待支付");
                    orderStatus.setTextColor(mRes.getColor(R.color.text_color3));
                    timeTV.setVisibility(View.GONE);
                } else if (couponCode.status == 1) {
                    orderStatus.setText("未消费");
                    orderStatus.setTextColor(mRes.getColor(R.color.text_color3));
                    timeTV.setVisibility(View.GONE);
                } else if (couponCode.status == 2) {
                    orderStatus.setText("退款中");
                    orderStatus.setTextColor(mRes.getColor(R.color.text_color16));
                    timeTV.setVisibility(View.GONE);
                    orderMoreIv.setVisibility(View.VISIBLE);
                } else if (couponCode.status == 3) {
                    orderStatus.setText("已退款");
                    orderMoreIv.setVisibility(View.VISIBLE);
                } else if (couponCode.status == 4) {
                    orderStatus.setText("已核销");
                } else if (couponCode.status == 5) {
                    orderStatus.setText("已过期");
                } else if (couponCode.status == 6) {
                    orderStatus.setText("已禁用");
                }
                orderDetailRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (couponCode.status == 2 || couponCode.status == 3) {
                            Intent toRefondDetail = new Intent(getContext(), CouponRefundDetailActivity.class);
                            toRefondDetail.putExtra(Constants.IntentParams.ID, couponCode.id);
                            startActivity(toRefondDetail);
                        }
                    }
                });
            }
        });
    }

    private boolean isExpired = false;

    @Override
    public void showHeads(CouponOrderDetailModel.OrderDetailCoupon orderDetailCoupon) {
        couponCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(orderDetailCoupon.cover, 270, 203)));
        couponTitle.setText(orderDetailCoupon.title);
        couponDesc.setText(orderDetailCoupon.desc);
        isExpired = false;
        if (orderDetailCoupon.is_expire == 1) {
            isExpired = true;
            findViewById(R.id.couponOder_isExpiretv).setVisibility(View.VISIBLE);
        }
        couponNowprice.setText("");
        double nowPrice = Double.parseDouble(orderDetailCoupon.price);
        if (nowPrice == 0) {
            couponNowprice.setText("免费");
            reFoundTypetv.setVisibility(View.GONE);
            couponNowprice.setTextColor(mRes.getColor(R.color.voucher_green));
        } else {
            couponNowprice.setText("￥ " + nowPrice);
            couponNowprice.setTextColor(mRes.getColor(R.color.voucher_yellow));
        }
        SpannableString demoPrice = SpannableUtil.getDelLineString("￥ " + orderDetailCoupon.denomination);
        couponOldprice.setText(demoPrice);
        if (orderDetailCoupon.refund_type == 1) {
            reFoundTypetv.setText("随时退");
        } else if (nowPrice != 0) {
            reFoundTypetv.setText("过期退");
        }
        if(nowPrice==0){
            reFoundTypetv.setVisibility(View.INVISIBLE);
            findViewById(R.id.couponrefoude_typdLayout).setVisibility(View.GONE);
        }

        if (reFoundTypetv.getVisibility() == View.GONE) {
            findViewById(R.id.couponrefoude_typdLayout).setVisibility(View.GONE);
        }
        expireDateTv.setText("有效期: " + DateFormatUtil.dispActiveTime2(orderDetailCoupon.begin_time, orderDetailCoupon.end_time));
    }

    @Override
    public void showDealer(final ArrayList<CouponDetailModel.Dealers> dealers) {
        mdealers = dealers;
        TextView moreTv = (TextView) findViewById(R.id.orderDetail_moreTV);
        if (dealers != null && dealers.size() != 0) {
            moreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toIntent = new Intent(mContext, CouponStoreActivity.class);
                    toIntent.putExtra(Constants.IntentParams.DATA, (Serializable) dealers);
                    startActivity(toIntent);
                }
            });
            TextView storeName = (TextView) findViewById(R.id.coupondetail_storename);
            storeName.setText(dealers.get(0).name);
            TextView address = (TextView) findViewById(R.id.coupondeatail_area_tv);
            address.setText(dealers.get(0).address);
            TextView distance = (TextView) findViewById(R.id.coupondeatail_distance_tv);
            distance.setText(dealers.get(0).distance + "km");
            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            ratingBar.setStar(dealers.get(0).service_score);
            orderTel = (ImageView) findViewById(R.id.orderdetail_teliv);
            orderTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SystemInvoker.launchDailPage(getContext(), dealers.get(0).tel);
                }
            });
        } else {
            moreTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showOrderInfo(CouponOrderDetailModel couponOrderDetailModel) {
        mCouponOrderDetailModel = couponOrderDetailModel;
        TextView orderSntv = (TextView) findViewById(R.id.coupon_ordersntv);
        TextView payTeltv = (TextView) findViewById(R.id.couponoder_payteltv);
        TextView createTimetv = (TextView) findViewById(R.id.couponoder_createTimetv);
        TextView buyNumtv = (TextView) findViewById(R.id.couponoder_bugNumtv);
        TextView sumPricetv = (TextView) findViewById(R.id.couponoder_sumPricetv);
        orderSntv.setText(couponOrderDetailModel.orderSn);
        payTeltv.setText(couponOrderDetailModel.user.tel);
        createTimetv.setText(couponOrderDetailModel.createTime);
        buyNumtv.setText(couponOrderDetailModel.couponTotal + "");
        sumPricetv.setText("￥ " + couponOrderDetailModel.totalPrice);
        if (couponOrderDetailModel.canEvaluate == 1) {
            commenttv.setVisibility(View.VISIBLE);
        }
        else {
            commenttv.setVisibility(View.GONE);
//            commenttv.setVisibility(View.GONE);
        }
        if (couponOrderDetailModel.canRefund == 1) {
            mBacktv.setVisibility(View.VISIBLE);
        } else {
            mBacktv.setVisibility(View.GONE);
        }
//tODO        commenttv.setVisibility();
    }

    public void showOnlineList(ArrayList<CouponDetailModel.OnlineItem> onlineList) {
        if (onlineList == null || onlineList.size() == 0) {
            onlineLayout.setVisibility(View.GONE);
            return;
        }

        orderDetailOnlineRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        CommonAdapter onLineAdapter = new CommonAdapter<CouponDetailModel.OnlineItem>(getContext(), R.layout.griditem_online, onlineList) {

            @Override
            public void convert(ViewHolder holder, CouponDetailModel.OnlineItem onlineItem) {
                holder.setText(R.id.salerstar, onlineItem.star);
                holder.setSimpleDraweeViewUrl(R.id.saler_avatariv, URLUtil.builderImgUrl(onlineItem.avatar, 144, 144));
                holder.setText(R.id.salernametv, onlineItem.nickname);
            }
        };
        orderDetailOnlineRv.setAdapter(onLineAdapter);
        onLineAdapter.setOnItemClickListener(new OnItemClickListener<CouponDetailModel.OnlineItem>() {

            @Override
            public void onItemClick(ViewGroup parent, View view, CouponDetailModel.OnlineItem onlineItem, int position) {
                Intent toUserPageAct = new Intent(getContext(), UserPageActivity.class);
                toUserPageAct.putExtra(Constants.IntentParams.ID, onlineItem.id);
                startActivity(toUserPageAct);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, CouponDetailModel.OnlineItem onlineItem, int position) {
                return false;
            }
        });

    }

}
