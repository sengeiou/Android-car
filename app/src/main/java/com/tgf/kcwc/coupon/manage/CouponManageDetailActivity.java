package com.tgf.kcwc.coupon.manage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponManageDetailModel;
import com.tgf.kcwc.mvp.presenter.CouponManageDetailPresenter;
import com.tgf.kcwc.mvp.view.CouponManageDetailView;
import com.tgf.kcwc.ticket.ContactActivity;
import com.tgf.kcwc.ticket.MyFriendActivity;
import com.tgf.kcwc.ticket.widget.TicketSendWindow;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 */

public class CouponManageDetailActivity extends BaseActivity implements CouponManageDetailView {
    private static final String TAG = "CouponManageDetailActivity";
    private final String KEY_INTENT_COUPONID = "couponid";
    private final String KEY_INTENT_DISTRIBUTEID = "distributeid";
    private final String KEY_INTENT_MOBILE = "mobile";
    private final String KEY_INTENT_TYPE = "type";
    private final int VAULE_INTENT_COUPON = 2;
    private TextView managedetailEventnameTv;
    private TextView managedetailTickettypeTv;
    private TextView managedetailTicketpriceTv;
    private TextView managedetailTicketdateTv;
    private TextView managedetailNumsTv;
    private TextView managedetailHtnumsTv;
    private TextView managedetailLeftnumTv;
    private TextView managedetailRecnumsTv;
    private TextView managedetailRecpnumsTv;
    private TextView managedetailUsenumsTv;
    private TextView managedetailUsepnumsTv;
    private SimpleDraweeView imageview;
    private TicketSendWindow ticketSendWindow;
    private int mTicketId;
    private TextView managedetailCouponUsewayTv;
    private TextView managedetailCouponUselimitTv;
    private TextView goodsCheckNumsTv;
    private TextView goodsCheckUsersTv;
    private LinearLayout ticketmLayout;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        View vackBt = findViewById(R.id.managedetail_back);
        backEvent(vackBt);
        TextView titleMangeTv = (TextView) findViewById(R.id.managedetail_text);
        titleMangeTv.setText("代金券分发管理");
        findViewById(R.id.managedetail_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CouponQueryActivity.class);
                intent.putExtra(KEY_INTENT_COUPONID, mTicketId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setUpViews() {
        CouponManageDetailPresenter couponManageDetailPresenter = new CouponManageDetailPresenter();
        couponManageDetailPresenter.attachView(this);
        TextView titleTv = (TextView) findViewById(R.id.title_bar_text);
        titleTv.setText("代金券分发管理");
        TextView managedetailSendTv = (TextView) findViewById(R.id.managedetail_sendTv);
        managedetailSendTv.setText("分发代金券");
        TextView managedetailSaomaFtv = (TextView) findViewById(R.id.managedetail_saomaFtv);
        managedetailSaomaFtv.setText("扫码发券");
        imageview = (SimpleDraweeView) findViewById(R.id.ticketmangedetail_headpng_iv);
        managedetailEventnameTv = (TextView) findViewById(R.id.managedetail_eventname_tv);
        managedetailTickettypeTv = (TextView) findViewById(R.id.managedetail_tickettype_tv);
        managedetailTicketpriceTv = (TextView) findViewById(R.id.managedetail_ticketprice_tv);
        managedetailCouponUsewayTv = (TextView) findViewById(R.id.managedetail_useway_tv);
        managedetailCouponUselimitTv = (TextView) findViewById(R.id.managedetail_uselimit_tv);
        managedetailTicketdateTv = (TextView) findViewById(R.id.managedetail_ticketdate_tv);
        managedetailNumsTv = (TextView) findViewById(R.id.managedetail_nums_tv);
        managedetailHtnumsTv = (TextView) findViewById(R.id.managedetail_htnums_tv);
        managedetailLeftnumTv = (TextView) findViewById(R.id.managedetail_leftnum_tv);
        managedetailRecnumsTv = (TextView) findViewById(R.id.managedetail_recnums_tv);
        managedetailRecpnumsTv = (TextView) findViewById(R.id.managedetail_recpnums_tv);
        managedetailUsenumsTv = (TextView) findViewById(R.id.managedetail_usenums_tv);
        managedetailUsepnumsTv = (TextView) findViewById(R.id.managedetail_usepnums_tv);
        goodsCheckNumsTv = (TextView) findViewById(R.id.managedetail_cousenums_tv);
        goodsCheckUsersTv = (TextView) findViewById(R.id.managedetail_cousepnums_tv);
        ticketmLayout = findViewById(R.id.tickem_detail_layout);
        findViewById(R.id.managedetail_query).setOnClickListener(this);
        findViewById(R.id.managedetail_send).setOnClickListener(this);
        findViewById(R.id.title_bar_back).setOnClickListener(this);
//        TicketManageDetailPresenter tickeManagePresenter = new TicketManageDetailPresenter();
//        tickeManagePresenter.attachView(this);
        Intent fromIntent = getIntent();
        mTicketId = fromIntent.getIntExtra(KEY_INTENT_COUPONID, 1);
        SharedPreferenceUtil.putTFId(getContext(), mTicketId);
        SharedPreferenceUtil.putDistributeId(getContext(), fromIntent.getIntExtra(KEY_INTENT_DISTRIBUTEID, 1));
        couponManageDetailPresenter.getCouponManageDetail(IOUtils.getToken(getContext()), mTicketId);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_managedetail);

    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.managedetail_query:
                Intent intent = new Intent(getContext(), CouponSendCodeActivity.class);
                intent.putExtra(Constants.IntentParams.ID, mTicketId);
                startActivity(intent);
                break;
            case R.id.managedetail_send:
                if (ticketSendWindow == null) {
                    ticketSendWindow = new TicketSendWindow(this);
                    ticketSendWindow.setOnClickListener(mListener);
                }
                ticketSendWindow.show(this);

                break;
            case R.id.title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    private TicketSendWindow.OnClickListener mListener = new TicketSendWindow.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toIntent = new Intent();
            toIntent.putExtra(KEY_INTENT_TYPE, VAULE_INTENT_COUPON);
            switch (v.getId()) {
                case R.id.ticket_contacts:
                    toIntent.setClass(getContext(), ContactActivity.class);
                    startActivityForResult(toIntent, Constants.IntentParams.REQUEST_CODE);
                    break;
                case R.id.ticket_user:
                    toIntent.setClass(getContext(), MyFriendActivity.class);
                    startActivityForResult(toIntent, Constants.IntentParams.REQUEST_CODE_2);
                    break;
                case R.id.ticket_input:
                    toIntent = new Intent(getContext(), CouponSendSeeActivity.class);
                    toIntent.putExtra(KEY_INTENT_MOBILE, 1);
                    startActivity(toIntent);
                    break;
                default:
                    break;
            }
        }
    };
    private Coupon mCoupon;

    @Override
    public void showManageViewHead(Coupon coupon) {
        mCoupon = coupon;
        managedetailEventnameTv.setText(mCoupon.title);
        managedetailTickettypeTv.setText("票面价值  " + mCoupon.denomination);
        managedetailTicketpriceTv.setText("实际售价  ￥" + mCoupon.price + "元");
        managedetailTicketdateTv.setText("代金券有效期: " + DateFormatUtil.formatTime4(mCoupon.beginTime) + " - " + DateFormatUtil.formatTime4(mCoupon.endTime));
        imageview.setImageURI(Uri.parse(URLUtil.builderImgUrl(mCoupon.cover, 540, 270)));
        if (mCoupon.checkType == 1) {
            managedetailCouponUsewayTv.setText("核销方式  线上抵扣");
        } else {
            findViewById(R.id.managedetail_copoun_fellow).setVisibility(View.GONE);
            findViewById(R.id.managedetail_goods_lv).setVisibility(View.GONE);
            managedetailCouponUsewayTv.setText("核销方式  扫码核销");

        }
        if (mCoupon.isExpiration == 1) {
            ticketmLayout.setVisibility(View.GONE);
        } else {
            ticketmLayout.setVisibility(View.VISIBLE);
        }
        managedetailCouponUselimitTv.setText("使用限制  " + coupon.useLimitType);
    }


    @Override
    public void showStatistics(CouponManageDetailModel.CouponStatistics statistics) {
        managedetailNumsTv.setText(statistics.count + "");
        managedetailHtnumsTv.setText(statistics.send + "");
        managedetailLeftnumTv.setText(statistics.inventory + "");
        managedetailRecnumsTv.setText(statistics.getNum + "");
        managedetailRecpnumsTv.setText(statistics.userGetNum + "");
        managedetailUsenumsTv.setText(statistics.checkNum + "");
        managedetailUsepnumsTv.setText(statistics.checkUserNum + "");
        goodsCheckNumsTv.setText(statistics.goodsCheckNum + "");
        goodsCheckUsersTv.setText(statistics.goodsCheckUserNum + "");
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
    private static final String KEY_INTENT_LISTCONTACT = "select_contact";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.IntentParams.REQUEST_CODE:
                case Constants.IntentParams.REQUEST_CODE_2:
                    Intent toCouponSendSee = new Intent(getContext(),CouponSendSeeActivity.class);
                    toCouponSendSee.putExtra(KEY_INTENT_LISTCONTACT,data.getSerializableExtra(KEY_INTENT_LISTCONTACT));
                    startActivity(toCouponSendSee);
                    break;
                default:
                    break;
            }
        }

    }
}
