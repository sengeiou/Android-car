package com.tgf.kcwc.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */

public class GoodDetailsPopupWindow extends BottomPushPopupWindow<IntegralConversionGoodDetailsBean> {

    private Context activity;
    private CicleAddAndSubView mCicleAddAndSubView;
    private int points; //金币积分
    private int dollar; //银元积分
    private SimpleDraweeView mSimpleDraweeView;//图片
    private ImageView back;//返回
    private TextView name;//名字
    private TextView repertory; //库存
    private TextView unitprice;//单价
    private TextView total; //总计
    private TextView balance; //余额
    private TextView notbalance; //余额不足
    private TextView convert; //兑换
    private TextView describe; //描述
    private TextView bazaar; //市场价格
    private TextView unit; //单位
    private TextView unit2; //单位

    private LinearLayout mRecipientsLayout; //收件人
    private EditText mRecipientsEdittext; //收件人

    private LinearLayout mPhonelayout; //收件人电话
    private EditText mPhoneEdittext; //收件人电话

    private LinearLayout mAddressLaout; //收件人地址
    private EditText mAddressEdittext; //收件人地址

    private int number = 1;
    private IntegralConversionGoodDetailsBean.Details details;
    private GoodOnClickListener goodOnClickListener;

    private boolean IsExchangeType = false;

    public GoodDetailsPopupWindow(Context context, IntegralConversionGoodDetailsBean data, int points, int dollar, GoodOnClickListener goodOnClickListener) {
        super(context, data);
        this.points = points;
        this.dollar = dollar;
        activity = context;

        int exchangeType = details.exchangeType;
        if (exchangeType == 1) {
            IsExchangeType = true;
            unit.setText("金币");
            unit2.setText("金币");
        } else {
            IsExchangeType = false;
            unit.setText("银元");
            unit2.setText("银元");
        }
        balance.setText("现有金币：" + points + "     银元：" + dollar);
        calculate(details);
        this.goodOnClickListener = goodOnClickListener;
    }


    @Override
    protected View generateCustomView(IntegralConversionGoodDetailsBean data) {
        details = data.data.details;
        View conentView = View.inflate(context, R.layout.integralconversion_good_details, null);
        mCicleAddAndSubView = (CicleAddAndSubView) conentView.findViewById(R.id.addsubtract);
        mSimpleDraweeView = (SimpleDraweeView) conentView.findViewById(R.id.icon);
        back = (ImageView) conentView.findViewById(R.id.back);
        name = (TextView) conentView.findViewById(R.id.name);
        repertory = (TextView) conentView.findViewById(R.id.repertory);
        unitprice = (TextView) conentView.findViewById(R.id.unitprice);
        total = (TextView) conentView.findViewById(R.id.total);
        balance = (TextView) conentView.findViewById(R.id.balance);
        notbalance = (TextView) conentView.findViewById(R.id.notbalance);
        convert = (TextView) conentView.findViewById(R.id.convert);
        describe = (TextView) conentView.findViewById(R.id.describe);
        bazaar = (TextView) conentView.findViewById(R.id.bazaar);
        unit = (TextView) conentView.findViewById(R.id.unit1);
        unit2 = (TextView) conentView.findViewById(R.id.unit2);
        mRecipientsLayout = (LinearLayout) conentView.findViewById(R.id.recipientslayout);
        mRecipientsEdittext = (EditText) conentView.findViewById(R.id.recipientsedittext);
        mPhonelayout = (LinearLayout) conentView.findViewById(R.id.phonelayout);
        mPhoneEdittext = (EditText) conentView.findViewById(R.id.phoneedittext);
        mAddressLaout = (LinearLayout) conentView.findViewById(R.id.addresslaout);
        mAddressEdittext = (EditText) conentView.findViewById(R.id.addressedittext);

        mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(details.cover, 270, 203)));
        name.setText(details.name);
        repertory.setText("库存：" + details.stock);
        bazaar.setText("市场价格：￥：" + details.marketValue);
        unitprice.setText(details.needPoints + "");
        if (!TextUtil.isEmpty(details.describe)) {
            describe.setText(Html.fromHtml(details.describe, null, null));
        }
        if (details.plamOrShopping == 2) {
            mRecipientsLayout.setVisibility(View.VISIBLE);
            mPhonelayout.setVisibility(View.VISIBLE);
            mAddressLaout.setVisibility(View.VISIBLE);
        } else {
            mRecipientsLayout.setVisibility(View.GONE);
            mPhonelayout.setVisibility(View.GONE);
            mAddressLaout.setVisibility(View.GONE);
        }

        mCicleAddAndSubView.setAutoChangeNumber(true);//设置是否自增长
        number = 1;
        mCicleAddAndSubView.setNum(number);//设置默认值
        mCicleAddAndSubView.setOnNumChangeListener(new CicleAddAndSubView.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, int stype, int num) {
                if (num <= details.stock) {
                    number = num;
                    calculate(details);
                } else {
                    CommonUtils.showToast(context, "超出库存！");
                    mCicleAddAndSubView.setNum(number);
                }
            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (details.plamOrShopping == 2) {
                    if (TextUtil.isEmpty(mRecipientsEdittext.getText().toString().trim())) {
                        CommonUtils.showToast(context, "请输入收件人");
                        return;
                    }
                    if (TextUtil.isEmpty(mPhoneEdittext.getText().toString().trim())) {
                        CommonUtils.showToast(context, "请输入收件人电话");
                        return;
                    }
                    if (TextUtil.isEmpty(mAddressEdittext.getText().toString().trim())) {
                        CommonUtils.showToast(context, "请输入收件人地址");
                        return;
                    }
                    details.tel = mPhoneEdittext.getText().toString().trim();
                    details.uername = mRecipientsEdittext.getText().toString().trim();
                    details.address = mAddressEdittext.getText().toString().trim();
                } else {
                    details.tel = "";
                    details.uername = "";
                    details.address = "";
                }
                goodOnClickListener.goodOnClickListener(details, number);

            }
        });
        return conentView;
    }

    public void calculate(IntegralConversionGoodDetailsBean.Details details) {

        if (IsExchangeType) {
            if (details.needPoints * number > points) {
                notbalance.setVisibility(View.VISIBLE);
                convert.setBackgroundColor(context.getResources().getColor(R.color.text_content_color));
                convert.setEnabled(false);
            } else {
                notbalance.setVisibility(View.GONE);
                convert.setBackgroundColor(context.getResources().getColor(R.color.tab_text_s_color));
                convert.setEnabled(true);
            }
        } else {
            if (details.needPoints * number > dollar) {
                notbalance.setVisibility(View.VISIBLE);
                convert.setBackgroundColor(context.getResources().getColor(R.color.text_content_color));
                convert.setEnabled(false);
            } else {
                notbalance.setVisibility(View.GONE);
                convert.setBackgroundColor(context.getResources().getColor(R.color.tab_text_s_color));
                convert.setEnabled(true);
            }
        }


        if (number == 0) {
            notbalance.setVisibility(View.GONE);
            convert.setBackgroundColor(context.getResources().getColor(R.color.text_content_color));
            convert.setEnabled(false);
        }
        total.setText(details.needPoints * number + "");
    }

    public interface GoodOnClickListener {
        void goodOnClickListener(IntegralConversionGoodDetailsBean.Details details, int number);
    }

    public void back() {
        dismiss();
    }
}
