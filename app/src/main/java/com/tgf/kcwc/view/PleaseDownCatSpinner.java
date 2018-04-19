package com.tgf.kcwc.view;

import java.util.ArrayList;
import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.MeituanHeaderBean;
import com.tgf.kcwc.mvp.presenter.BrandListPresenter;
import com.tgf.kcwc.mvp.view.BrandDataView;
import com.tgf.kcwc.mvp.view.DividerItemDecoration;
import com.tgf.kcwc.see.WrapBrandLists;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.URLUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/1/11 0011
 * E-mail:hekescott@qq.com
 */

public class PleaseDownCatSpinner extends PopupWindow {

    private Context mContext;
    private View catContentView;
    private Resources mRes;
    private int selectPos;
    private View mSetBrandsLayout;
    private MyBrandSelectView myBrandSelectView;


    private SuspensionDecoration mDecoration;

    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas = new ArrayList<>();


    public PleaseDownCatSpinner(Context context) {
        super(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContext = context;
        mRes = mContext.getResources();
        IsSelc = false;
        initView();
    }


    public int getSelectPos() {
        return selectPos;
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
        catContentView = View.inflate(mContext, R.layout.popwin_brand_list, null);

        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        catContentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        PleaseDownCatSpinner.this.dismiss();
                    }
                });
        mSetBrandsLayout = catContentView.findViewById(R.id.cat_setBrandLayout);
        myBrandSelectView = (MyBrandSelectView) catContentView.findViewById(R.id.MyBrandSelectView);
        myBrandSelectView.setOnCitySelect(new MyBrandSelectView.OnCitySelect() {
            @Override
            public void citySelect(Brand brand) {
                IsSelc = true;
                selectDataItem = new DataItem();
                selectDataItem.id = brand.brandId;
                selectDataItem.name = brand.brandName;
                selectDataItem.isSelected = true;
                PleaseDownCatSpinner.this.dismiss();
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
        super.showAsDropDown(btnView, 0, 0);

//        super.showAsDropDown(btnView, 0, 2);
    }
}
