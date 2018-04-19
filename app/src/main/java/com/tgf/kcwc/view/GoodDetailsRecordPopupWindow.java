package com.tgf.kcwc.view;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.integral.ModificationAddressActivity;
import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/26.
 */

public class GoodDetailsRecordPopupWindow extends BottomPushPopupWindow<IntegralRecordBean.Data> {

    private Context activity;
    private SimpleDraweeView mSimpleDraweeView;//图片
    private ImageView back;//返回
    private TextView name;//名字

    private TextView mUnitPrice; //单价
    private TextView mNum;//数量
    private TextView mTotalPrices; //总价
    private TextView mTime; //时间
    private TextView mCondition; //领取状态

    String units = "";

    protected LinearLayout mReceiveyardLayout;//兑换码布局
    protected TextView mReceiveyard;//兑换码
    protected TextView mReceiveAddress;//兑换地址

    protected LinearLayout mAddAddress;//添加收件人
    protected LinearLayout mAddressLayout;//收件人信息
    protected LinearLayout mRecipientsLayout;//收件人布局
    protected TextView mRecipients;//收件人
    protected TextView mConsigneeAddress;//收件人地址
    protected LinearLayout mModification;//修改收件人
    protected LinearLayout mPickupMethod;//领取方式布局

    private IntegralRecordBean.Data details;
    private GoodOnClickListener goodOnClickListener;

    private boolean IsExchangeType = false;

    public GoodDetailsRecordPopupWindow(Context context, IntegralRecordBean.Data data) {
        super(context, data);
        activity = context;
    }

    @Override
    protected View generateCustomView(IntegralRecordBean.Data data) {
        details = data;
        View conentView = View.inflate(context, R.layout.integralconversion_good_recorddetails, null);
        mSimpleDraweeView = (SimpleDraweeView) conentView.findViewById(R.id.icon);
        back = (ImageView) conentView.findViewById(R.id.back);
        name = (TextView) conentView.findViewById(R.id.name);

        mReceiveyardLayout = (LinearLayout) conentView.findViewById(R.id.receiveyardlayout);
        mReceiveyard = (TextView) conentView.findViewById(R.id.receiveyard);
        mReceiveAddress = (TextView) conentView.findViewById(R.id.receiveaddress);

        mAddAddress = (LinearLayout) conentView.findViewById(R.id.add_address);
        mAddressLayout = (LinearLayout) conentView.findViewById(R.id.addresslayout);
        mRecipientsLayout = (LinearLayout) conentView.findViewById(R.id.recipientslayout);
        mRecipients = (TextView) conentView.findViewById(R.id.recipients);
        mConsigneeAddress = (TextView) conentView.findViewById(R.id.consigneeaddress);
        mModification = (LinearLayout) conentView.findViewById(R.id.modification);
        mPickupMethod = (LinearLayout) conentView.findViewById(R.id.pickupmethod);

        mUnitPrice = (TextView) conentView.findViewById(R.id.unitprice);
        mNum = (TextView) conentView.findViewById(R.id.num);
        mTotalPrices = (TextView) conentView.findViewById(R.id.totalprices);
        mTime = (TextView) conentView.findViewById(R.id.time);
        mCondition = (TextView) conentView.findViewById(R.id.condition);

        mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(details.cover, 270, 203)));
        name.setText(details.name);
        mNum.setText("×" + details.num);
        if (details.receiveType == 1) {
            units = "金币";
        } else {
            units = "银元";
        }
        mUnitPrice.setText(details.usePoints + units);
        mTotalPrices.setText(details.usePoints * details.num + units);
        mTime.setText(details.receiveTime);

        if (details.receiveStatus == 1) {
            mCondition.setText("未领取");
            mCondition.setTextColor(context.getResources().getColor(R.color.text_selected));
        } else {
            mCondition.setTextColor(context.getResources().getColor(R.color.sevicecity_tilefontopen));
            mCondition.setText("已领取");
        }


        int productType = details.productType;
        if (productType == 1) {
            if (!TextUtil.isEmpty(details.code)) {
                mPickupMethod.setVisibility(View.VISIBLE);
                mReceiveyardLayout.setVisibility(View.VISIBLE);
                mRecipientsLayout.setVisibility(View.GONE);
            } else {
                mPickupMethod.setVisibility(View.GONE);
            }
        } else if (productType == 2) {
            mPickupMethod.setVisibility(View.VISIBLE);
            mReceiveyardLayout.setVisibility(View.GONE);
            mRecipientsLayout.setVisibility(View.VISIBLE);
        } else {
            mPickupMethod.setVisibility(View.GONE);
        }

        mReceiveyard.setText(details.code);
        mReceiveAddress.setText(details.address);
        mRecipients.setText(details.nickname + "(" + details.tel + ")");
        mConsigneeAddress.setText(details.address);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Serializable> args = new HashMap<>();
                args.put(Constants.IntentParams.ID, details.id + "");
                CommonUtils.startNewActivity(context, args, ModificationAddressActivity.class);
            }
        });
        mAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Serializable> args = new HashMap<>();
                args.put(Constants.IntentParams.ID, details.id + "");
                CommonUtils.startNewActivity(context, args, ModificationAddressActivity.class);
            }
        });
        return conentView;
    }


    public interface GoodOnClickListener {
        void goodOnClickListener(IntegralConversionGoodDetailsBean.Details details, int number);
    }

    public void back() {
        dismiss();
    }
}
