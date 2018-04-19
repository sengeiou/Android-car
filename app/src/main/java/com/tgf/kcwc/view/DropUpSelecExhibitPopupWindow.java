package com.tgf.kcwc.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.TicketmExhibitModel;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.URLUtil;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/11 0011
 * E-mail:hekescott@qq.com
 */

public class DropUpSelecExhibitPopupWindow extends BottomPushPopupWindow<List<TicketmExhibitModel>> {

    private List<TicketmExhibitModel> mDataList;
    private View catContentView;
    private Resources mRes;
    private ListView catPopListView;
    private WrapAdapter<TicketmExhibitModel> mCatgroyAdapter;
    private int selectPos;

    public DropUpSelecExhibitPopupWindow(Context context, List<TicketmExhibitModel> dataList) {
        super(context, dataList);
        //super(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected View generateCustomView(List<TicketmExhibitModel> dataItems) {
        mDataList = dataItems;
        mRes = context.getResources();
        IsSelc = false;
        catContentView = View.inflate(context, R.layout.exhibitselectpopupwind, null);
        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        catPopListView = (ListView) catContentView.findViewById(R.id.popwind_exhibitlistlv);
        catContentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DropUpSelecExhibitPopupWindow.this.dismiss();
                    }
                });
        catContentView.findViewById(R.id.ticketm_exhibit_closeIV)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DropUpSelecExhibitPopupWindow.this.dismiss();
                    }
                });
        initAdapter();
        return catContentView;
    }

    public void setDeafultSelectPos(int pos) {
        selectPos = pos;
        TicketmExhibitModel selectItem = mDataList.get(selectPos);
        mCatgroyAdapter.notifyDataSetChanged();
    }

    public int getSelectPos() {
        return selectPos;
    }

    public TicketmExhibitModel getSelectDataItem() {
        return mSelectDataItem;
    }

    public boolean IsSelc = false;

    public boolean getIsSelec() {
        return IsSelc;
    }

    private TicketmExhibitModel mSelectDataItem;

    private void initAdapter() {
        mCatgroyAdapter = new WrapAdapter<TicketmExhibitModel>(context, mDataList, R.layout.listitem_popselect_exhibit) {
            @Override
            public void convert(ViewHolder helper, TicketmExhibitModel item) {
                        helper.setSimpleDraweeViewURL(R.id.listitem_coveriv, URLUtil.builderImgUrl(item.cover,540,304));
                        helper.setText(R.id.tickem_exhibitnametv,item.name);
                        helper.setText(R.id.tickem_exhibitAdtv,item.address);
                        helper.setText(R.id.tickem_exhibitTimetv, DateFormatUtil.dispActiveTime2(item.startTime,item.endTime));
            }
        };
        catPopListView.setAdapter(mCatgroyAdapter);
        catPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    IsSelc = true;
                    TicketmExhibitModel selectItem = mDataList.get(position);
                    selectPos = position;
                    mSelectDataItem = selectItem;
                    DropUpSelecExhibitPopupWindow.this.dismiss();
            }
        });
    }

//    private void initView() {
//        catContentView = View.inflate(context, R.layout.phoneselectpopupwind, null);
//        this.setContentView(catContentView);
//        this.setOutsideTouchable(true);
//        this.setBackgroundDrawable(new BitmapDrawable());
//        this.setFocusable(true);
//        this.setAnimationStyle(R.style.popwin_anim_style);
//        catPopListView = (ListView) catContentView.findViewById(R.id.popwin_supplier_list_lv);

//        catContentView.findViewById(R.id.cancel)
//                .setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View arg0) {
//                        DropUpSelecExhibitPopupWindow.this.dismiss();
//                    }
//                });
//    }


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
