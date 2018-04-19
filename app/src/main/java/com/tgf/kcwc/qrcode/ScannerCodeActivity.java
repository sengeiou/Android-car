package com.tgf.kcwc.qrcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.certificate.CertRegActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.CouponScanSuccesAcitivity;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.me.message.MessageActivity;
import com.tgf.kcwc.me.sale.MyExhibitionInfoActivity;
import com.tgf.kcwc.me.setting.FanKuiActivity;
import com.tgf.kcwc.membercenter.SignInSucceedActivity;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.AttentionDataPresenter;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.mvp.view.ExhibitionPosQrView;
import com.tgf.kcwc.ticket.TicketFailedActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Author：Jenny
 * Date:2017/3/27 14:48
 * E-mail：fishloveqin@gmail.com
 */

public class ScannerCodeActivity extends BaseActivity implements ZBarScannerView.ResultHandler, ExhibitionPosQrView {

    private static final String TAG = ScannerCodeActivity.class.getSimpleName();
    private MorePopupWindow morePopupWindow;
    private ArrayList<MorePopupwindowBean> dataList = new ArrayList<>();

    //    public int eventId;
//    //签到或者打卡
//    public int type;
//    public int applyId;
    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {

        backEvent(back);
        text.setText("二维码/条形码");

        function.setImageResource(R.drawable.btn_hide_more_selector);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                morePopupWindow.showPopupWindow(function);
            }
        });
    }

    private AttentionDataPresenter mAttentionPresenter;

    @Override
    protected void setUpViews() {
//        eventId = getIntent().getIntExtra(Constants.IntentParams.ID2,-1);
//        applyId = getIntent().getIntExtra(Constants.IntentParams.ID,-1);
//        type = getIntent().getIntExtra(Constants.IntentParams.FROM_TYPE,-1);
//        Log.e("TAG", "eventId: "+eventId+"applyId:"+applyId+"type:"+type );

        initHideMoreMenuData();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mAttentionPresenter = new AttentionDataPresenter();
        mAttentionPresenter.attachView(mAttentionView);
        mScannerView = new ZBarScannerView(this) {

            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
    }

    //关注

    AttentionView<String> mAttentionView = new AttentionView<String>() {
        @Override
        public void showAddAttention(String string) {

            CommonUtils.showToast(mContext, " 已添加对方为好友啦！");
            finish();
        }

        @Override
        public void showCancelAttention(String string) {

        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_code);
    }


    private void initHideMoreMenuData() {

        if (dataList.size() == 0) {
            String[] navValues = null;

            navValues = mRes.getStringArray(R.array.global_nav_values8);

            Map<String, Integer> titleActionIconMap = DataUtil.getTitleActionIcon();
            int length = navValues.length;
            for (int i = 0; i < length; i++) {
                MorePopupwindowBean morePopupwindowBean = null;
                morePopupwindowBean = new MorePopupwindowBean();
                morePopupwindowBean.title = navValues[i];
                morePopupwindowBean.icon = titleActionIconMap.get(navValues[i]);
                morePopupwindowBean.id = i;
                dataList.add(morePopupwindowBean);
            }
        }

        morePopupWindow = new MorePopupWindow(this,
                dataList, mMoreOnClickListener);


    }


    private MorePopupWindow.MoreOnClickListener mMoreOnClickListener = new MorePopupWindow.MoreOnClickListener() {
        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean item) {
            switch (dataList.get(position).title) {
                case "首页":
                    CommonUtils.gotoMainPage(mContext, Constants.Navigation.HOME_INDEX);
                    break;
                case "消息":
                    CommonUtils.startNewActivity(mContext, MessageActivity.class);
                    break;

                case "反馈":
                    CommonUtils.startNewActivity(mContext, FanKuiActivity.class);
                    break;

            }

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        boolean flag = CommonUtils.checkSpPermission(this,
                new String[]{Manifest.permission.CAMERA}, 1);
        if (flag) {
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();
        }
    }

    private ZBarScannerView mScannerView;

    @Override
    public void onResume() {
        super.onResume();
        // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera(); // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        String content = rawResult.getContents();
        Log.e("TAG", content); // Prints scan results
        Log.e("TAG", rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
        mScannerView.stopCamera();
        content = content;

        if (!TextUtils.isEmpty(content)) {

//            int index = content.indexOf(Constants.QRValues.SIGN_IN_OUT);
//            //签到打卡
//            if (index >= 0) {
//                String data = content.substring(index + Constants.QRValues.SIGN_IN_OUT.length(),
//                        content.length());
//                ExhibitionPosQrPresenter exhibitionPosQrPresenter = new ExhibitionPosQrPresenter();
//                exhibitionPosQrPresenter.attachView(this);
//                exhibitionPosQrPresenter.signIn(IOUtils.getToken(getContext()), applyId, data, eventId);
//            }

            int index = content.indexOf(Constants.QRValues.CERT_REG);
            if (index >= 0) {

                String data = content.substring(index + Constants.QRValues.CERT_REG.length(),
                        content.length());
                //System.out.println("data:" + data);

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.DATA, data);
                CommonUtils.startNewActivity(mContext, args, CertRegActivity.class);
                finish();
            }

            index = content.indexOf(Constants.QRValues.ADD_FRIEND_ID);

            if (index >= 0) {

                String id = content.substring(index + Constants.QRValues.ADD_FRIEND_ID.length(),
                        content.length());
                //跳转到个人主页页面

                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.ID, Integer.parseInt(id));
                intent.setClass(mContext, UserPageActivity.class);
                startActivity(intent);
                finish();
                //mAttentionPresenter.execAttention(id, IOUtils.getToken(mContext));
            }

            index = content.indexOf(Constants.QRValues.RIDECHECKID); //签到
            if (index >= 0) {
                String newStr = content.substring(index + Constants.QRValues.RIDECHECKID.length(),
                        content.length());
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, newStr.trim());
                CommonUtils.startNewActivity(mContext, args, SignInSucceedActivity.class);
                finish();
            }
            index = content.indexOf(Constants.QRValues.TICKETM_SEND); //门票领取
            if (index >= 0) {
                String[] arrSplit;
                String strUrlParam = content.substring(index + Constants.QRValues.TICKETM_SEND.length(), content.length());
                arrSplit = strUrlParam.split("&");
                String ticketId = null;
                String num = null;
                String sign = null;
                for (String strSplit : arrSplit) {
                    if (strSplit.startsWith("id=")) {
                        ticketId = strSplit.substring("id=".length(), strSplit.length());
                    } else if (strSplit.startsWith("num=")) {
                        num = strSplit.substring("num=".length(), strSplit.length());
                    } else if (strSplit.startsWith("sign=")) {
                        sign = strSplit.substring("sign=".length(), strSplit.length());
                    }
                }

                Intent toTicketm = new Intent(ScannerCodeActivity.this, TicketFailedActivity.class);
                toTicketm.putExtra(Constants.IntentParams.ID, ticketId);
                toTicketm.putExtra(Constants.IntentParams.DATA, num);
                toTicketm.putExtra(Constants.IntentParams.DATA2, sign);
                startActivity(toTicketm);
                finish();
                return;
            }
            index = content.indexOf(Constants.QRValues.COUPONID); //代金券核销
            if (index >= 0) {
                String[] arrSplit;
                String strUrlParam = content.substring(index + Constants.QRValues.COUPONID.length(),
                        content.length());
                arrSplit = strUrlParam.split("&");
                String couponId = null;
                String num = null;
                String distributeId = null;
                for (String strSplit : arrSplit) {
                    if (strSplit.startsWith("coupon_id=")) {
                        couponId = strSplit.substring("coupon_id=".length(), strSplit.length());
                    } else if (strSplit.startsWith("num=")) {
                        num = strSplit.substring("num=".length(), strSplit.length());
                    } else if (strSplit.startsWith("distribute_id=")) {
                        distributeId = strSplit.substring("distribute_id=".length(), strSplit.length());
                    }
                }
                try {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, couponId.trim());
                    args.put(Constants.IntentParams.ID2, distributeId.trim());
                    args.put(Constants.IntentParams.DATA, num.trim());
                    CommonUtils.startNewActivity(mContext, args, CouponScanSuccesAcitivity.class);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            showQRResultInfo(content);
        }

    }

    /**
     * @param content
     */
    private void showQRResultInfo(String content) {
        finish();
        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.DATA, content);
        CommonUtils.startNewActivity(mContext, args, ScannerCodeResultActivity.class);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAttentionPresenter != null) {

            mAttentionPresenter.detachView();
        }
    }

    @Override
    public void exhibitionSuccess(String model) {
        CommonUtils.showToast(getContext(), model);
        Map<String, Serializable> args = new HashMap<String, Serializable>();
        CommonUtils.startNewActivity(mContext, args, MyExhibitionInfoActivity.class);
        finish();
    }

    @Override
    public void exhibitionFail(String model) {
        CommonUtils.showToast(getContext(), model);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return getContext();
    }

    private static class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT = "";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }
    }

}
