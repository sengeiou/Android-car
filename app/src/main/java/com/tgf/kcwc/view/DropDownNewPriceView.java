package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.view.rangeseekbar.RangeBar;

import java.util.Random;

/**
 * Auther: Scott
 * Date: 2017/1/11 0011
 * E-mail:hekescott@qq.com
 */

public class DropDownNewPriceView extends PopupWindow {

    private Context mContext;
    private View priceContentView;
    private GridView mGridView;
    private Resources mRes;
    private WrapAdapter<DataItem> mCatgroyAdapter;

    public DropDownNewPriceView(Context context) {
        super(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContext = context;
        mRes = mContext.getResources();
        initView();
    }


    private DataItem selectDataItem = new DataItem();

    public DataItem getSelectDataItem() {
        return selectDataItem;
    }

    private void initView() {
        priceContentView = View.inflate(mContext, R.layout.common_newprice_layout, null);
        this.setContentView(priceContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);

        mGridView= (GridView) priceContentView.findViewById(R.id.gridview);

        priceContentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {

                    }
                });
        priceContentView.findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropDownNewPriceView.this.dismiss();
            }
        });

        priceContentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DropDownNewPriceView.this.dismiss();
                    }
                });
    }


    public void showAsDropDownBelwBtnView(View btnView) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            btnView.getGlobalVisibleRect(rect);
            int h = btnView.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(btnView, 0, 2);
    }
}
