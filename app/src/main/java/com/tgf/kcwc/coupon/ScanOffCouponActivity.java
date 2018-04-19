package com.tgf.kcwc.coupon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ScanOffListModel;
import com.tgf.kcwc.mvp.presenter.ScanOffCouponPresenter;
import com.tgf.kcwc.mvp.view.ScanOffCouponView;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NotitleContentDialog;

import java.util.ArrayList;

/**
 * 扫码核销
 * Auther: Scott
 * Date: 2017/5/23 0023
 * E-mail:hekescott@qq.com
 */

public class ScanOffCouponActivity extends BaseActivity implements ScanOffCouponView {

    private ListView scanoffLv;
    private ScanOffCouponPresenter scanOffCouponPresenter;
    private ArrayList<ScanOffListModel.ScanOffItem> mlist;
    private TextView onlineTv;
    private int mIsOnlineStatus = -1;
    private NotitleContentDialog mOnLineDialog;
    private NotitleContentDialog mOffLineDialog;
    private TextView offlineTv;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        View mback = findViewById(R.id.couponscanoff_back);
        TextView titleTv = (TextView) findViewById(R.id.couponscanoff_title);
        titleTv.setText("扫码核销");
        backEvent(mback);
        onlineTv = (TextView) findViewById(R.id.couponscanoff_online);
        offlineTv = (TextView) findViewById(R.id.couponscanoff_offline);
        onlineTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsOnlineStatus == -1) {
                    scanOffCouponPresenter.getCouponOnline(IOUtils.getToken(getContext()));
                    return;
                }
                showOnLineDialog();
            }
        });
        offlineTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOffLineDialog();
            }
        });
    }

//    private void showDialog() {
//        if (mIsOnlineStatus == 0) {
//            showOnLineDialog();
//        } else {
//            showOffLineDialog();
//        }
//    }

    private void showOffLineDialog() {
        if(mIsOnlineStatus==0){
            CommonUtils.showToast(getContext(),"当前已是离线状态");
            return;
        }

        if (mOffLineDialog == null) {
            mOffLineDialog = new NotitleContentDialog(getContext());
            mOffLineDialog.setYesText("知道了");
            mOffLineDialog.mContentTv.setTextSize(14);
            mOffLineDialog.mContentTv.setTextColor(mRes.getColor(R.color.text_color3));
            mOffLineDialog.setContentText("设置为“离线”后，您的个人信息将不在对应代金券详情等页面显示。");
            mOffLineDialog.setOnLoginOutClickListener(new NotitleContentDialog.IOnclickLisenter() {
                @Override
                public void OkClick() {
                    scanOffCouponPresenter.setCouponOnline(IOUtils.getToken(getContext()));
                    mOffLineDialog.dismiss();
                }

                @Override
                public void CancleClick() {
                    mOffLineDialog.dismiss();
                }
            });
        }
        mOffLineDialog.show();
    }

    private void showOnLineDialog() {
        if(mIsOnlineStatus==1){
            CommonUtils.showToast(getContext(),"当前已是在线状态");
            return;
        }

        if (mOnLineDialog == null) {
            mOnLineDialog = new NotitleContentDialog(getContext());
            mOnLineDialog.setYesText("知道了");
            mOnLineDialog.mContentTv.setTextSize(14);
            mOnLineDialog.mContentTv.setTextColor(mRes.getColor(R.color.text_color3));
            mOnLineDialog.setContentText("设置为“上线”后，您的在线状态将在对应代金券详情等页面显示，表示您可提供相应服务并进行现场核销，便于客户直接联系到您。");
            mOnLineDialog.setOnLoginOutClickListener(new NotitleContentDialog.IOnclickLisenter() {
                @Override
                public void OkClick() {
                    scanOffCouponPresenter.setCouponOnline(IOUtils.getToken(getContext()));
                    mOnLineDialog.dismiss();
                }

                @Override
                public void CancleClick() {
                    mOnLineDialog.dismiss();
                }
            });
        }
        mOnLineDialog.show();
    }

    @Override
    protected void setUpViews() {
        scanoffLv = (ListView) findViewById(R.id.scanoff_listlv);
        findViewById(R.id.scanner_iv).setOnClickListener(this);
        scanOffCouponPresenter = new ScanOffCouponPresenter();
        scanOffCouponPresenter.attachView(this);
        scanOffCouponPresenter.getScanOffList(IOUtils.getToken(getContext()));
        scanOffCouponPresenter.getCouponOnline(IOUtils.getToken(getContext()));
        scanoffLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


              if(position>0&&position<mlist.size()){
                  ScanOffListModel.ScanOffItem scanOffItem = mlist.get(position);
                  if(scanOffItem.checkNum==0){
                      return;
                  }
                  int coupid = scanOffItem.couponId;
                  Intent toIntent = new Intent(getContext(), ScanOffCouponDetailActivity.class);
                  toIntent.putExtra(Constants.IntentParams.ID, coupid + "");
                  toIntent.putExtra(Constants.IntentParams.TITLE, scanOffItem.title);
                  toIntent.putExtra(Constants.IntentParams.DATA, scanOffItem.cover);
                  startActivity(toIntent);
              }


            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponscanoff);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanner_iv:
                startActivity(new Intent(getContext(), ScannerCodeActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void showScanOffList(ArrayList<ScanOffListModel.ScanOffItem> list) {
        mlist = list;
        scanoffLv.setAdapter(new WrapAdapter<ScanOffListModel.ScanOffItem>(getContext(), mlist,
                R.layout.listitem_scanoff_coupon) {
            @Override
            public void convert(ViewHolder helper, ScanOffListModel.ScanOffItem item) {
                helper.setText(R.id.listitem_scanoff_coupontitle, item.title);
                helper.setText(R.id.scanoff_desc, "有效时间:" + DateFormatUtil.dispActiveTime2( item.beginTime,item.endTime));

              Double priceDouble  =   Double.parseDouble(item.price);
              TextView nowPriceTv =  helper.getView(R.id.scanoff_nowprice);
              if(priceDouble==0){
                  nowPriceTv.setTextColor(mRes.getColor(R.color.text_color10));
                  nowPriceTv.setText("免费");
              }else {
                  nowPriceTv.setTextColor(mRes.getColor(R.color.text_color23));
                  nowPriceTv.setText("￥" + item.price);
              }
                SpannableString denomPrice = SpannableUtil.getDelLineString("￥" + item.denomination);
                helper.setText(R.id.scanoff_oldprice, denomPrice);
                helper.setText(R.id.scanoff_orgname, item.orgName);
                helper.setText(R.id.scanoff_numtv, "核销总量: " + item.checkNum);
                if(item.checkNum==0){
                    helper.getView(R.id.couponmore_recordiv).setVisibility(View.INVISIBLE);
                }else {
                    helper.getView(R.id.couponmore_recordiv).setVisibility(View.VISIBLE);
                }
//                SpannableString checkPrice = SpannableUtil.getColorString(mRes.getColor(R.color.text_color5), "￥ " + item.checkPrice);
                helper.setText(R.id.scanoff_moneytv, Html.fromHtml("累计金额 <font color='#ff9a22' size='12'> ￥"+item.checkPrice+"</font>"));
                String couponUrl = URLUtil.builderImgUrl(item.cover, 270, 203);
                helper.setSimpleDraweeViewURL(R.id.scanoff_cover, couponUrl);
            }
        });

    }

    @Override
    public void showsIsOnline(boolean isOnline) {
        if (isOnline) {
            onlineTv.setTextColor(mRes.getColor(R.color.white));
            offlineTv.setTextColor(mRes.getColor(R.color.text_color17));
            mIsOnlineStatus = 1;
        } else {
            mIsOnlineStatus = 0;
            offlineTv.setTextColor(mRes.getColor(R.color.white));
            onlineTv.setTextColor(mRes.getColor(R.color.text_color17));
        }
    }

    @Override
    public void showSalerSetSuccess() {
        if (mIsOnlineStatus == 1) {
            mIsOnlineStatus = 0;
        } else if (mIsOnlineStatus == 0) {
            mIsOnlineStatus = 1;
        }
        if (mIsOnlineStatus == 1) {
            onlineTv.setTextColor(mRes.getColor(R.color.white));
            offlineTv.setTextColor(mRes.getColor(R.color.text_color17));
        } else if (mIsOnlineStatus == 0) {
            offlineTv.setTextColor(mRes.getColor(R.color.white));
            onlineTv.setTextColor(mRes.getColor(R.color.text_color17));
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
        return this;
    }

    @Override
    protected void onDestroy() {
        scanOffCouponPresenter.detachView();
        super.onDestroy();

    }
}
