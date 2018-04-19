package com.tgf.kcwc.view;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.integral.IntegralOrderListActivity;
import com.tgf.kcwc.me.integral.ModificationAddressActivity;
import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseBean;
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

public class PurchaseGoodDetailsPopupWindow extends BottomPushPopupWindow<IntegralPurchaseBean.Data> {

    private Context activity;
    private ImageView back;//返回
    private TextView units;//单位
    private TextView unit;//单位
    private TextView num;//数量
    private TextView price;//价格
    private TextView affirm;//确定


    private IntegralPurchaseBean.Data details;
    private GoodOnClickListener goodOnClickListener;

    private boolean IsExchangeType = false;

    public PurchaseGoodDetailsPopupWindow(Context context, IntegralPurchaseBean.Data data, GoodOnClickListener goodOnClickListener) {
        super(context, data);
        activity = context;
        this.goodOnClickListener=goodOnClickListener;
    }

    @Override
    protected View generateCustomView(IntegralPurchaseBean.Data data) {
        details = data;
        View conentView = View.inflate(context, R.layout.integralconversion_good_purchasedetails, null);
        back = (ImageView) conentView.findViewById(R.id.back);
        units = (TextView) conentView.findViewById(R.id.units);
        num = (TextView) conentView.findViewById(R.id.num);
        price = (TextView) conentView.findViewById(R.id.price);
        unit = (TextView) conentView.findViewById(R.id.unit);
        affirm = (TextView) conentView.findViewById(R.id.affirm);

        if (details.pointType == 1) {
            units.setText("个人积分-金币");
            unit.setText("金币");
        } else if (details.pointType == 2) {
            units.setText("商务积分-银元");
            unit.setText("银元");
        } else {
            units.setText("钻石");
            unit.setText("钻石");
        }
        num.setText(details.points + "");
        price.setText("￥" + details.price);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        affirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodOnClickListener.goodOnClickListener(details);
                dismiss();
            }
        });
        return conentView;
    }


    public interface GoodOnClickListener {
        void goodOnClickListener(IntegralPurchaseBean.Data details);
    }

    public void back() {
        dismiss();
    }
}
