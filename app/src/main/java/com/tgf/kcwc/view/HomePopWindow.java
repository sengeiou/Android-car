package com.tgf.kcwc.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.util.DensityUtils;

/**
 * Auther: Scott
 * Date: 2017/9/15 0015
 * E-mail:hekescott@qq.com
 */

public class HomePopWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
//    private Animation anim_backgroundView;
    private View conentView;
    public TextView editeBtn;
    public View editeLayout;
    public TextView deleteBtn;
    public TextView shareBtn;
    private int[] location = new int[2];
    private int marginHeight;
    private int viewWidth ;
    private int marginLeft ;
    public HomePopWindow(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
//        this.anim_backgroundView = AnimationUtils.loadAnimation(context, R.anim.alpha_show_anim);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.popwindow_home_dome, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        marginHeight =DensityUtils.dip2px(context, 70);
        viewWidth = DensityUtils.dip2px(context, 100);
        marginLeft =DensityUtils.dip2px(context, 74);
        this.setWidth(viewWidth);

//        this.setHeight(marginHeight);
        // 设置SelectPicPopupWindow弹出窗体的宽
//        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
        this.editeBtn = (TextView) conentView.findViewById(R.id.home_editebtn);
        this.editeLayout =  conentView.findViewById(R.id.home_editeLayout);
        this.shareBtn = (TextView) conentView.findViewById(R.id.home_shareBtn);
        this.deleteBtn = (TextView) conentView.findViewById(R.id.home_deleteBtn);
        this.editeBtn.setOnClickListener(this);
        this.shareBtn.setOnClickListener(this);
        this.deleteBtn.setOnClickListener(this);
    }
    /**
     * 没有半透明背景 显示popupWindow
     *
     * @param
     */
    public void showPopupWindow(View v) {
        v.getLocationOnScreen(location); //获取控件的位置坐标
        //获取自身的长宽高
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        if (location[1] > DensityUtils.getPhoneheight(context) - marginHeight -DensityUtils.dip2px(context, 40)) { //MainApplication.SCREEN_H 为屏幕的高度，方法可以自己写
//            this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
//            arrow_up.setVisibility(View.GONE);
//            arrow_down.setVisibility(View.VISIBLE);
            conentView.setBackgroundResource(R.drawable.home_domeup);
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]-marginLeft), location[1] - conentView.getMeasuredHeight()-DensityUtils.dip2px(context, 15));
        } else {
//            this.setAnimationStyle(R.style.operation_popwindow_anim_style_down);
//            arrow_up.setVisibility(View.VISIBLE);
//            arrow_down.setVisibility(View.GONE);
            conentView.setBackgroundResource(R.drawable.home_domedown);
            this.showAsDropDown(v, -marginLeft, 0);
        }
    }
    @Override
    public void onClick(View v) {
             switch (v.getId()){
                         case R.id.home_editebtn:
                             this.mItemClickListener.onItemClick(0);
                             break;
                         case R.id.home_shareBtn:
                             this.mItemClickListener.onItemClick(1);
                             break;
                         case R.id.home_deleteBtn:
                             this.mItemClickListener.onItemClick(2);
                             break;
                         default:
                             break;
                     }
        this.dismiss();
    }
    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public static interface OnItemClickListener {
        public void onItemClick(int position);
    }
    private OnItemClickListener mItemClickListener;
    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
