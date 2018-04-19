package com.tgf.kcwc.coupon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;
import com.tgf.kcwc.mvp.model.CouponRefoundModel;
import com.tgf.kcwc.mvp.presenter.CouponRefounPresenter;
import com.tgf.kcwc.mvp.view.CouponRefounView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NotitleContentDialog;
import com.tgf.kcwc.view.NotitleContentOneBtnDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/8/10 0010
 * E-mail:hekescott@qq.com
 */

public class CouponRefounActivity extends BaseActivity implements CouponRefounView {
    private Intent fromIntent;
    private int mOrderId;
    private SimpleDraweeView couponCoverIv;
    private TextView couponTitle;
    private TextView couponDesc;
    private TextView couponNowprice;
    private TextView couponOldprice;
    private CouponRefounPresenter couponRefounPresenter;
    private ListView codesLv;
    private WrapAdapter<CouponRefoundModel.RefundCode> refondCodeAdapter;
    private ArrayList<CouponRefoundModel.RefundCode> mCanRefundCodes;
    private ListView waysLv;
    private WrapAdapter<RefondWays> waysWrapAdapter;
    private ListView whysLv;
    private WrapAdapter<DataItem> whyAdapter;
    private EditText backNoteEd;
    private TextView textLengTv;
    private String token;
    private int wayType = -1;
    private NotitleContentDialog mCancelOrderDialog;
    private String mWhySStr;
    private String mCodesStr;
    private TextView refondNumTv;
    private CouponOrderDetailModel.OrderDetailCoupon mCoupon;
    private NotitleContentOneBtnDialog mRefondSuccesslDialog;

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        mOrderId = fromIntent.getIntExtra(Constants.IntentParams.ID, 0);
        couponRefounPresenter = new CouponRefounPresenter();
        couponRefounPresenter.attachView(this);
        token = IOUtils.getToken(getContext());
        couponRefounPresenter.getRefoundInfo(mOrderId, token);
        couponCoverIv = (SimpleDraweeView) findViewById(R.id.couponlist_cover);
        couponTitle = (TextView) findViewById(R.id.listitem_recoment_coupontitle);
        couponDesc = (TextView) findViewById(R.id.couponlist_desc);
        couponNowprice = (TextView) findViewById(R.id.recyleitem_near_nowprice);
        couponOldprice = (TextView) findViewById(R.id.listviewitem_recomment_oldprice);
        codesLv = (ListView) findViewById(R.id.couponRefound_codeLV);
        waysLv = (ListView) findViewById(R.id.couponRefound_wayLV);
        whysLv = (ListView) findViewById(R.id.couponRefound_whyLV);
        refondNumTv = (TextView) findViewById(R.id.refound_numtv);
        backNoteEd = (EditText) findViewById(R.id.refond_backNoteTv);
        textLengTv = (TextView) findViewById(R.id.bockNote_textSizeTv);
        findViewById(R.id.refond_submittv).setOnClickListener(this);
        backNoteEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textLengTv.setText(s.length() + "/300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("申请退款");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_refound);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refond_submittv:
                StringBuilder codes = null;
                for (CouponRefoundModel.RefundCode refundCode : mCanRefundCodes) {
                    if (refundCode.isSelected) {
                        if (codes == null) {
                            codes = new StringBuilder("" + refundCode.id);
                        } else {
                            codes.append("," + refundCode.id);
                        }
                    }
                }
                if (codes == null) {
                    CommonUtils.showToast(getContext(), "请选择代金券");
                    return;
                }
                for (RefondWays refundWay : refondWaysList) {
                    if (refundWay.isSelected) {
                        wayType = refundWay.id;
                    }
                }
                if (wayType == -1) {
                    CommonUtils.showToast(getContext(), "请选择退款方式");
                    return;
                }
                StringBuilder whys = null;
                for (DataItem refonWhy : refondWhysList) {
                    if (refonWhy.isSelected) {
                        if (whys == null) {
                            whys = new StringBuilder(refonWhy.name);
                        } else {
                            whys.append("," + refonWhy.name);
                        }
                    }
                }
                String tmpStr = backNoteEd.getText().toString();
                if (whys == null && (TextUtil.isEmpty(tmpStr))) {
                    CommonUtils.showToast(getContext(), "请选择退款原因");
                    return;
                }
                mCodesStr = codes.toString();
                if (whys != null)
                    mWhySStr = whys.toString();
                showCancelDialog();
                break;
            default:
                break;
        }
    }

    private void showCancelDialog() {
        if (mCancelOrderDialog == null) {
            mCancelOrderDialog = new NotitleContentDialog(getContext());
            mCancelOrderDialog.setContentText(" 申请退款后,该代金券不可使用!" +
                    "您确定要继续申请退款吗？");
            mCancelOrderDialog.setCancelText("我再想想");
            mCancelOrderDialog.setYesText("确定");
            mCancelOrderDialog.setOnLoginOutClickListener(new NotitleContentDialog.IOnclickLisenter() {
                @Override
                public void OkClick() {
                    couponRefounPresenter.postRefond(token, mCodesStr, wayType, mWhySStr, backNoteEd.getText() + "");
                    mCancelOrderDialog.dismiss();
                }

                @Override
                public void CancleClick() {
                    mCancelOrderDialog.dismiss();
                }
            });
        }
        mCancelOrderDialog.show();
    }
    private void showRefondSuccesslDialog() {
        if (mRefondSuccesslDialog == null) {
            mRefondSuccesslDialog = new NotitleContentOneBtnDialog(getContext());
            String content ="您的退款申请已提交，您可以在\n"+TextUtil.getColorText("#4e81ba","“我的卡券”")
                    +"中点击" +TextUtil.getColorText("#4e81ba","“退款进度”\n")
                    +"查看退款进度";
            mRefondSuccesslDialog.setHtmlContentText(content);
            mRefondSuccesslDialog.setYesText("知道了");
            mRefondSuccesslDialog.setOnLoginOutClickListener(new NotitleContentOneBtnDialog.IOnclickLisenter(){
                @Override
                public void OkClick() {
                    finish();
                }
            });
        }
        mRefondSuccesslDialog.show();
    }
    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showReundCodes(ArrayList<CouponRefoundModel.RefundCode> canRefundCodes) {
        String string = String.format(mRes.getString(R.string.refoud_numtitle), canRefundCodes.size() + "");
        refondNumTv.setText(Html.fromHtml(string));
        mCanRefundCodes = canRefundCodes;
        if (refondCodeAdapter == null) {

            refondCodeAdapter = new WrapAdapter<CouponRefoundModel.RefundCode>(getContext(), R.layout.listitem_copon_refond, canRefundCodes) {
                @Override
                public void convert(ViewHolder helper, CouponRefoundModel.RefundCode item) {
                    helper.setText(R.id.refond_numTv, "NO." + item.code);
                    if (item.isSelected) {
                        helper.setImageResource(R.id.refond_statusIv, R.drawable.refound_s);
                    } else {
                        helper.setImageResource(R.id.refond_statusIv, R.drawable.refound_n);
                    }
                }
            };
            codesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCanRefundCodes.get(position).isSelected = !mCanRefundCodes.get(position).isSelected;
                    refondCodeAdapter.notifyDataSetChanged();
                    Double priceCount = 0.0;
                    for (CouponRefoundModel.RefundCode refundCode : mCanRefundCodes) {
                        if (refundCode.isSelected) {
                            priceCount += Double.parseDouble(refundCode.price);
                        }
                    }
//                    if(priceCount!=0){
//
//                    }
                    TextView refondPrice = (TextView) findViewById(R.id.refond_priceTv);
                    refondPrice.setText("退款合计：￥" + priceCount);
                }
            });
            codesLv.setAdapter(refondCodeAdapter);
            ViewUtil.setListViewHeightBasedOnChildren(codesLv);
            showRefondWay();
            showRefondWhy();
        }
    }


    List<RefondWays> refondWaysList = new ArrayList<>();

    private void showRefondWay() {
        initWayData();
        if (waysWrapAdapter == null) {
            waysWrapAdapter = new WrapAdapter<RefondWays>(getContext(), refondWaysList, R.layout.listitem_copon_refondway) {
                @Override
                public void convert(ViewHolder helper, RefondWays item) {
                    helper.setText(R.id.refond_wayTv, item.title);
                    helper.setText(R.id.refond_wayDescTv, item.desc);
                    if (item.isSelected) {
                        helper.setImageResource(R.id.refond_statusIv, R.drawable.refound_s);
                    } else {
                        helper.setImageResource(R.id.refond_statusIv, R.drawable.refound_n);
                    }
                }
            };


            waysLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int selectId = refondWaysList.get(position).id;
                    for (RefondWays refondWay : refondWaysList) {
                        if (refondWay.id == selectId) {
                            refondWay.isSelected = !refondWay.isSelected;
                        } else {
                            refondWay.isSelected = false;
                        }
                    }
                    waysWrapAdapter.notifyDataSetChanged();
                }
            });
            waysLv.setAdapter(waysWrapAdapter);
            ViewUtil.setListViewHeightBasedOnChildren(waysLv);
        }
    }

    private void initWayData() {
        refondWaysList.clear();
        refondWaysList.add(new RefondWays("原路退回", "(1-7个工作日内退款到原支付方)", 1));
        refondWaysList.add(new RefondWays("我的零钱", "(退回到看车玩车零钱包)", 2));
    }

    List<DataItem> refondWhysList = new ArrayList<>();

    private void showRefondWhy() {
        initWhyData();
        if (whyAdapter == null) {
            whyAdapter = new WrapAdapter<DataItem>(getContext(), refondWhysList, R.layout.listitem_copon_refond) {
                @Override
                public void convert(ViewHolder helper, DataItem item) {
                    helper.setText(R.id.refond_numTv, item.name);
                    if (item.isSelected) {
                        helper.setImageResource(R.id.refond_statusIv, R.drawable.refound_s);
                    } else {
                        helper.setImageResource(R.id.refond_statusIv, R.drawable.refound_n);
                    }
                }
            };
            whysLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    refondWhysList.get(position).isSelected = !refondWhysList.get(position).isSelected;
                    whyAdapter.notifyDataSetChanged();
                }
            });
            whysLv.setAdapter(whyAdapter);
            ViewUtil.setListViewHeightBasedOnChildren(whysLv);
        }

    }

    private void initWhyData() {
        String whys[] = mRes.getStringArray(R.array.refond_why);
        for (int i = 0; i < whys.length; i++) {
            refondWhysList.add(new DataItem(i, whys[i]));
        }
    }

    @Override
    public void showHead(CouponOrderDetailModel.OrderDetailCoupon coupon) {
        mCoupon = coupon;
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
    public void showPostSuccess() {
//        CommonUtils.showToast(getContext(), "申请提交成功");
//        finish();
        showRefondSuccesslDialog();
//        Intent toRefondDetail = new Intent(getContext(), CouponRefundDetailActivity.class);
//        toRefondDetail.putExtra(Constants.IntentParams.ID, mCoupon.id);
//        startActivity(toRefondDetail);
    }

    @Override
    public void showPostFailed(String statusMessage) {
        CommonUtils.showToast(getContext(), statusMessage);
    }


    @Override
    public Context getContext() {
        return CouponRefounActivity.this;
    }

    private class RefondWays {
        public String title;
        public String desc;
        public int id;
        public boolean isSelected;

        public RefondWays(String title, String desc, int id) {
            this.title = title;
            this.desc = desc;
            this.id = id;
        }
    }
}
