package com.tgf.kcwc.view;

import java.util.List;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.util.BitmapUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
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

public class DropUpColourPopupWindow extends PopupWindow {

    private List<DataItem>        mDataList;
    private Context               mContext;
    private View                  catContentView;
    private Resources             mRes;
    private ListView              catPopListView;
    private WrapAdapter<DataItem> mCatgroyAdapter;
    private int                   selectPos;

    public DropUpColourPopupWindow(Context context, List<DataItem> dataList) {
        super(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mContext = context;
        mDataList = dataList;
        mRes = mContext.getResources();
        IsSelc = false;
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

    public boolean IsSelc = false;

    public boolean getIsSelec() {
        return IsSelc;
    }

    private DataItem mSelectDataItem;

    private void initAdapter() {
        mCatgroyAdapter = new WrapAdapter<DataItem>(mContext, mDataList,
            R.layout.colour_listview_popwin) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                int position = helper.getPosition();
                TextView tv = helper.getView(R.id.listview_popwind_tv);
                ImageView colours = helper.getView(R.id.colours);
                final int unSelectedColor = mRes.getColor(R.color.style_bg4);
                if (position == 0) {
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    tv.setTextColor(mContext.getResources().getColor(R.color.color_login_devide));
                    tv.setGravity(Gravity.CENTER);
                    colours.setVisibility(View.GONE);
                } else {
                    tv.setTextColor(mContext.getResources().getColor(R.color.style_bg7));
                    tv.setGravity(Gravity.RIGHT | Gravity.CENTER);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    if (!item.name.equals("全部")) {
                        String colors[] = item.colours.split(",");
                        colours.setVisibility(View.VISIBLE);
                        colours.setImageBitmap(
                                BitmapUtils.getRectColors(colors, 110, 50, unSelectedColor, 1));
                    }else {
                        colours.setVisibility(View.GONE);
                    }
                }
                tv.setText(item.name);
                /*                ImageView imageView = helper.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }*/
            }
        };
        catPopListView.setAdapter(mCatgroyAdapter);
        catPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    IsSelc = true;
                    DataItem selectItem = mDataList.get(position);
                    selectItem.isSelected = true;
                    selectPos = position;
                    mSelectDataItem = selectItem;
                    singleChecked(mDataList, selectItem);
                    mCatgroyAdapter.notifyDataSetChanged();
                    DropUpColourPopupWindow.this.dismiss();
                }
            }
        });
    }

    private void initView() {
        catContentView = View.inflate(mContext, R.layout.phoneselectpopupwind, null);
        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        catPopListView = (ListView) catContentView.findViewById(R.id.popwin_supplier_list_lv);
        catContentView.findViewById(R.id.popwin_supplier_list_bottom)
            .setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    DropUpColourPopupWindow.this.dismiss();
                }
            });
        catContentView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DropUpColourPopupWindow.this.dismiss();
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
    /**
     * 显示在界面的底部
     */
    public void show(Activity activity) {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
