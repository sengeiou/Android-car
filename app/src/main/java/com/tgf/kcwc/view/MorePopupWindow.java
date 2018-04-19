package com.tgf.kcwc.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */

public class MorePopupWindow extends PopupWindow {

    private View                             conentView;
    private List<MorePopupwindowBean>        dataList;
    private Activity                         activity;
    private ListView                         listview;

    public WrapAdapter<MorePopupwindowBean> getmCommonAdapter() {
        return mCommonAdapter;
    }

    private WrapAdapter<MorePopupwindowBean> mCommonAdapter;
    private MoreOnClickListener              mMoreOnClickListener;

    public MorePopupWindow(final Activity context, List<MorePopupwindowBean> data,MoreOnClickListener mMoreOnClickListe) {
        dataList = data;
        activity = context;
        mMoreOnClickListener = mMoreOnClickListe;
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = layoutInflater.inflate(R.layout.view_popup_window, null, false);
        listview = (ListView) conentView.findViewById(R.id.listview);
        mCommonAdapter = new WrapAdapter<MorePopupwindowBean>(context,
                R.layout.view_popup_window_item, dataList) {
            @Override
            public void convert(ViewHolder helper, final MorePopupwindowBean item) {
                final int position = helper.getPosition();
                TextView name = helper.getView(R.id.btn_change_parent);
                LinearLayout select = helper.getView(R.id.select);
                ImageView icon = helper.getView(R.id.icon);
                TextView view = helper.getView(R.id.view);
                name.setText(item.title);
                if ((position + 1) == dataList.size()) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                if (item.icon != 0) {
                    icon.setVisibility(View.VISIBLE);
                    icon.setImageResource(item.icon);
                } else {
                    icon.setVisibility(View.GONE);
                }
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMoreOnClickListener.moreOnClickListener(position, item);
                        dismiss();
                    }
                });
            }
        };
        listview.setAdapter(mCommonAdapter);

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 3 + 30);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimationPreview);
    }
        public void notifyDataSetChanged(List<MorePopupwindowBean> data){
            dataList = data;
            mCommonAdapter.notifyDataSetChanged();
    }
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            int xPos = -getWidth() / 2;
            this.showAsDropDown(parent, xPos, 0);
        } else {
            this.dismiss();
        }
    }

    public interface MoreOnClickListener {
        void moreOnClickListener(int position, MorePopupwindowBean bean);
    }
}
