package com.tgf.kcwc.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;

/**
 * Auther: Scott
 * Date: 2017/1/9 0009
 * E-mail:hekescott@qq.com
 */

public class FilterPopwinUtil {
    public static PopupWindow getFilterPopupWindow(Context context, View contentView) {
        PopupWindow popMenu = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
        popMenu.setOutsideTouchable(true);
        popMenu.setBackgroundDrawable(new BitmapDrawable());
        popMenu.setFocusable(true);
        popMenu.setAnimationStyle(R.style.popwin_anim_style);
        return popMenu;
    }

    public static void commonFilterTile(LinearLayout layout, String title) {
        TextView tv = (TextView) layout.findViewById(R.id.filterTitle);
        tv.setText(title);
    }
    public static void commonFilterTileSize(LinearLayout layout, float title) {
        TextView tv = (TextView) layout.findViewById(R.id.filterTitle);
        tv.setTextSize(title);
    }

    public static void commonFilterTitleColor(Resources mRes, LinearLayout layout, int titleColor) {
        TextView tv = (TextView) layout.findViewById(R.id.filterTitle);
        tv.setTextColor(mRes.getColor(titleColor));
    }

    public static void commonFilterImage(LinearLayout layout, int drawbleid) {
        ImageView img = (ImageView) layout.findViewById(R.id.filterImg);
        img.setImageResource(drawbleid);
    }

    public static void fixBug(PopupWindow popupWindow, View anchorView, Activity context) {

        if (android.os.Build.VERSION.SDK_INT == 24) {//在android 7.0中,当popupWindow的高度  过大时，调用showAsDropDown方法popupWindow可能会出现在view的上方或占满全屏，这是android 7.0的bug，用这种方式可以正常显示，7.1已经修复这个bug
            int[] a = new int[2];
            anchorView.getLocationInWindow(a);
            popupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0,
                a[1] + anchorView.getHeight());
        } else {
            popupWindow.showAsDropDown(anchorView);

        }

    }
}
