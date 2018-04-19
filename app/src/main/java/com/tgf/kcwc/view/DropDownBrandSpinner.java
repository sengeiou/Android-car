package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.see.BuilderBrands;
import com.tgf.kcwc.see.WrapBrandLists;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/11 0011
 * E-mail:hekescott@qq.com
 */

public class DropDownBrandSpinner extends PopupWindow {

    private Context mContext;
    private View catContentView;
    private Resources mRes;
    private WrapAdapter<DataItem> mCatgroyAdapter;
    private int selectPos;
    private WrapBrandLists mWrapBrandsLists;
    private View mSetBrandsLayout;

    public DropDownBrandSpinner(Context context, int type, String isNeedAll, int exId) {
        super(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContext = context;
        mRes = mContext.getResources();
        initView(type, isNeedAll, exId);
    }

    public void setDeafultSelectPos(int pos) {
        selectPos = pos;
        mCatgroyAdapter.notifyDataSetChanged();
    }

    public int getSelectPos() {
        return selectPos;
    }


    private DataItem selectDataItem;

    public DataItem getSelectDataItem() {
        return selectDataItem;
    }

    private void initView(int brandType, String isNeedAll, int exId) {
        catContentView = View.inflate(mContext, R.layout.popwin_filterbrand_list, null);
        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);

        BuilderBrands builderBrands = new BuilderBrands(mContext, catContentView);
        builderBrands.setSingle(true);
        builderBrands.setDisplayChecked(false);
        builderBrands.setCallback(mCallback);
        builderBrands.loadData(brandType, isNeedAll, exId);
    }

    private DataItem mBrandItem = new DataItem();
    private BuilderBrands.Callback mCallback = new BuilderBrands.Callback() {
        @Override
        public void refresh(ViewGroup parent, View view, Brand item, int position) {

            mBrandItem.id = item.brandId;
            mBrandItem.name = item.brandName;
            mBrandItem.isSelected = true;
            selectDataItem = mBrandItem;
            DropDownBrandSpinner.this.dismiss();

        }
    };

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
