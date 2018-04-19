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

public class DropUpPopupWindow extends PopupWindow {

    private List<DataItem> mDataList;
    private Context mContext;
    private View catContentView;
    private Resources mRes;
    private ListView catPopListView;
    private WrapAdapter<DataItem> mCatgroyAdapter;
    private int selectPos;

    public DropUpPopupWindow(Context context, List<DataItem> dataList) {
        super(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContext = context;
        mDataList = dataList;
        mRes = mContext.getResources();
        IsSelc=false;
        initView();
        initAdapter();
    }

    public void setDeafultSelectPos(int pos) {
        selectPos = pos;
        DataItem selectItem = mDataList.get(selectPos);
        selectItem.isSelected = true;
        mCatgroyAdapter.notifyDataSetChanged();
    }

    public int getSelectPos() {
        return selectPos;
    }
    public DataItem getSelectDataItem() {
        return mSelectDataItem;
    }
    public boolean IsSelc=false;
    public boolean getIsSelec(){
        return IsSelc;
    }
    private DataItem mSelectDataItem;
    private void initAdapter() {
        mCatgroyAdapter = new WrapAdapter<DataItem>(mContext, mDataList, R.layout.item_listview_popwin) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                TextView tv = helper.getView(R.id.listview_popwind_tv);
                tv.setText(item.name);
                ImageView imageView = helper.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        };
        catPopListView.setAdapter(mCatgroyAdapter);
        catPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IsSelc=true;
                DataItem selectItem = mDataList.get(position);
                selectItem.isSelected = true;
                selectPos = position;
                mSelectDataItem = selectItem;
                singleChecked(mDataList, selectItem);
                mCatgroyAdapter.notifyDataSetChanged();
                DropUpPopupWindow.this.dismiss();
            }
        });
    }

    private void initView() {
        catContentView = View.inflate(mContext, R.layout.selectpopupwind, null);
        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        catPopListView = (ListView) catContentView.findViewById(R.id.popwin_supplier_list_lv);
        catContentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DropUpPopupWindow.this.dismiss();
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
