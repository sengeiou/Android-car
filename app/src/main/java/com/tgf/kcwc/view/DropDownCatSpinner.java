package com.tgf.kcwc.view;

import java.util.List;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.see.WrapBrandLists;
import com.tgf.kcwc.util.CommonUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/1/11 0011
 * E-mail:hekescott@qq.com
 */

public class DropDownCatSpinner extends PopupWindow {

    private List<DataItem> mDataList;
    private Context mContext;
    private View catContentView;
    private ListView catLeftListView;
    private WrapAdapter<DataItem> mCatgroyAdapter;
    private int selectPos;
//    private WrapBrandLists mWrapBrandsLists;
//    private View mSetBrandsLayout;
    private boolean isBrand;
    private DataItem selectDataItem;

    public boolean getIsBrand() {
        return isBrand;
    }

    public DataItem getSelectDataItem() {
        return selectDataItem;
    }

    public DropDownCatSpinner(Context context, List<DataItem> dataList) {

        super(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContext = context;
        mDataList = dataList;
        initView();
        initAdapter();
    }

    public void setDeafultSelectPos(int pos) {
        selectPos = pos;
        DataItem selectItem = mDataList.get(selectPos);
        selectItem.isSelected = true;
        mCatgroyAdapter.notifyDataSetChanged();
    }
    public void setDeafultSelectId(int id) {
        for (DataItem d : mDataList) {
            if (d.id == id) {
                d.isSelected = true;
                break;
            }
        }
//        selectPos = pos;
//        DataItem selectItem = mDataList.get(selectPos);
//        selectItem.isSelected = true;
        mCatgroyAdapter.notifyDataSetChanged();
    }

    public int getSelectPos() {
        return selectPos;
    }

    private void initAdapter() {
        mCatgroyAdapter = new WrapAdapter<DataItem>(mContext, mDataList, R.layout.item_listview_popwin) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                TextView tv = helper.getView(R.id.listview_popwind_tv);
                tv.setText(item.name);
                ImageView imageView = helper.getView(R.id.select_status_img);
                if (item.isSelected && item.id != 0) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        };
        catLeftListView.setAdapter(mCatgroyAdapter);
        catLeftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataItem selectItem = mDataList.get(position);
                selectItem.isSelected = true;
                selectPos = position;
                singleChecked(mDataList, selectItem);
                mCatgroyAdapter.notifyDataSetChanged();
                if (!selectItem.name.equals("买车")) {
                    isBrand = false;
                    selectDataItem = selectItem;
                    DropDownCatSpinner.this.dismiss();
//                    mSetBrandsLayout.setVisibility(View.INVISIBLE);
                } else {
                    isBrand = true;
                    selectDataItem = selectItem;
                    DropDownCatSpinner.this.dismiss();
//                    mSetBrandsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initView() {
        catContentView = LayoutInflater.from(mContext).inflate(R.layout.popwin_filtercatgory_list, null, false);
        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        catLeftListView = (ListView) catContentView.findViewById(R.id.popwin_filtercat_listleft_lv);
//        mSetBrandsLayout = catContentView.findViewById(R.id.right_brandslayout);
//        mWrapBrandsLists = new WrapBrandLists();
//        mWrapBrandsLists.setUpViews(mContext, catContentView);
//        mWrapBrandsLists.loadData();
//        mWrapBrandsLists.setSingle(true);
//        mWrapBrandsLists.setCallback(mCallback);

    }

    private DataItem mBrandItem = new DataItem();
    private WrapBrandLists.Callback mCallback = new WrapBrandLists.Callback() {
        @Override
        public void refresh(ViewGroup parent, View view, Brand item, int position) {

            mBrandItem.id = item.brandId;
            mBrandItem.name = item.brandName;
            mBrandItem.isSelected = true;
            selectDataItem = mBrandItem;
            DropDownCatSpinner.this.dismiss();

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
        super.showAsDropDown(btnView, 0, 2);
    }
}
