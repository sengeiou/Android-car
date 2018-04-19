package com.tgf.kcwc.view;

import java.util.List;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/1/11 0011
 * E-mail:hekescott@qq.com
 */

public class DropDownDoubleSpinner extends PopupWindow {

    private List<DataItem> mDataList;
    private List<DataItem> mDataListRight;
    private Context mContext;
    private View catContentView;
    private Resources mRes;
    private ListView kilPopListViewLeft;
    private WrapAdapter<DataItem> mKilometerLeftAdapter;
    private WrapAdapter<DataItem> mKilometerRightAdapter;
    private int selectLeftPos;
    private ListView kilPopListViewRight;
    private DataItem mSelectDateItem;
    private int selectId;

    public DropDownDoubleSpinner(Context context, List<DataItem> dataList) {
        super(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContext = context;
        mDataList = dataList;
        mDataListRight = dataList.get(selectLeftPos).subDataItem;
        mRes = mContext.getResources();
        initView();
        initAdapter();
    }

    public void setDeafultSelectPos(int pos) {
        selectLeftPos = pos;
        DataItem selectItem = mDataList.get(selectLeftPos);
        selectItem.isSelected = true;
        mKilometerLeftAdapter.notifyDataSetChanged();
    }

    public int getSelectPos() {
        return selectLeftPos;
    }
    public DataItem getSelectDataItem() {
        return mSelectDateItem;
    }

    private void initView() {
        catContentView = View.inflate(mContext, R.layout.popwin_filternear_list, null);
        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        kilPopListViewLeft = (ListView) catContentView.findViewById(R.id.popwin_supplier_listleft_lv);
        kilPopListViewRight = (ListView) catContentView.findViewById(R.id.popwin_supplier_listright_lv);
        catContentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DropDownDoubleSpinner.this.dismiss();
                    }
                });
    }
    private void initAdapter() {
        mKilometerLeftAdapter = new WrapAdapter<DataItem>(mContext, mDataList,
                R.layout.item_listview_popwin) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                TextView tv = helper.getView(R.id.listview_popwind_tv);
                tv.setText(item.name);
                LinearLayout ll = helper.getView(R.id.listitem_popwind_ll);
                if (item.isSelected) {
                    ll.setBackgroundColor(mRes.getColor(R.color.white));
                } else {
                    ll.setBackgroundColor(mRes.getColor(R.color.voucher_divide_line));
                }
            }
        };
        mKilometerRightAdapter = new WrapAdapter<DataItem>(mContext, mDataListRight,
                R.layout.item_listview_popwin) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                TextView tv = helper.getView(R.id.listview_popwind_tv);
                tv.setText(item.name);
                View view = helper.getView(R.id.select_status_img);
                if(item.isSelected){
                    view.setVisibility(View.VISIBLE);
                }else {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        };
        kilPopListViewLeft.setAdapter(mKilometerLeftAdapter);
        kilPopListViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataItem selectItem = mDataList.get(position);
                selectItem.isSelected = true;
                selectLeftPos = position;
                singleChecked(mDataList, selectItem);
                mDataListRight = mDataList.get(position).subDataItem;
                singleChecked(mDataListRight,selectId);
                mKilometerLeftAdapter.notifyDataSetChanged();
                mKilometerRightAdapter.notifyDataSetChanged(mDataListRight);
            }
        });

        kilPopListViewRight.setAdapter(mKilometerRightAdapter);
        kilPopListViewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataItem selectItem = mDataListRight.get(position);
                selectId = selectItem.id;
                mSelectDateItem = selectItem;
                selectItem.isSelected =true;
                singleChecked(mDataListRight,selectId);
                mKilometerRightAdapter.notifyDataSetChanged();
                DropDownDoubleSpinner.this.dismiss();
            }
        });
    }



    private void singleChecked(List<DataItem> items, DataItem item) {
        for (DataItem d : items) {
            if (d.id != item.id) {
                d.isSelected = false;
            }
        }
    }
    private void singleChecked(List<DataItem> items, int ite) {
        for (DataItem d : items) {
            if (d.id != selectId) {
                d.isSelected = false;
            }
        }
    }
    public void showAsDropDownBelwBtnView(View btnView) {
        if(Build.VERSION.SDK_INT >=24) {
            Rect rect = new Rect();
            btnView.getGlobalVisibleRect(rect);
            int h = btnView.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(btnView, 0, 2);
    }
}
