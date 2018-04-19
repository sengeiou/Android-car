package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tgf.kcwc.R;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Brand;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/11 0011
 * E-mail:hekescott@qq.com
 */

public class PleaseDownCitySpinner extends PopupWindow {

    private Context mContext;
    private View catContentView;
    private Resources mRes;
    private int selectPos;
    MyCitySelectView myCitySelectView;

    public PleaseDownCitySpinner(Context context) {
        super(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContext = context;
        mRes = mContext.getResources();
        IsSelc = false;
        initView();
        initAdapter();
    }

    public void setDeafultSelectPos(int pos) {
        selectPos = pos;
    }

    public int getSelectPos() {
        return selectPos;
    }

    private void initAdapter() {

    }

    public boolean IsSelc = false;

    public boolean getIsSelec() {
        return IsSelc;
    }

    private boolean isBrand;
    private DataItem selectDataItem;

    public boolean getIsBrand() {
        return isBrand;
    }

    public DataItem getSelectDataItem() {
        return selectDataItem;
    }

    private void initView() {
        catContentView = View.inflate(mContext, R.layout.popwin_citygory_list, null);
        myCitySelectView = (MyCitySelectView) catContentView.findViewById(R.id.MyCitySelectView);
        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        catContentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        PleaseDownCitySpinner.this.dismiss();
                    }
                });
        myCitySelectView.setOnCitySelect(new MyCitySelectView.OnCitySelect() {
            @Override
            public void citySelect(String name, Brand brand) {
                mBrandItem.name = name;
                mBrandItem.isSelected = true;
                selectDataItem = mBrandItem;
                IsSelc = true;
                PleaseDownCitySpinner.this.dismiss();
            }
        });
    }

    private DataItem mBrandItem = new DataItem();

    private void singleChecked(List<DataItem> items, DataItem item) {
        for (DataItem d : items) {
            if (d.id != item.id) {
                d.isSelected = false;
            }
        }
    }

    public void showAsDropDownBelwBtnView(View btnView) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            btnView.getGlobalVisibleRect(rect);
            int h = btnView.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(btnView, 0, 0);

//        super.showAsDropDown(btnView, 0, 2);
    }
}
