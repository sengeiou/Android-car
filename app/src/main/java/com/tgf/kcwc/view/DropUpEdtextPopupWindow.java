package com.tgf.kcwc.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.tgf.kcwc.R;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;

/**
 * Auther: Scott
 * Date: 2017/1/11 0011
 * E-mail:hekescott@qq.com
 */

public class DropUpEdtextPopupWindow extends BottomPushPopupWindow<Void> {

    private View catContentView;
    private Resources mRes;
    SeleListener mSeleListener;
    private EditText mEdtext;
    String name;
    public interface SeleListener {
        void getString(String s);

        void getcancel();
    }

    public DropUpEdtextPopupWindow(Context context,String name, SeleListener seleListener) {
        super(context, null);
        this.mSeleListener = seleListener;
        this.name = name;
        mEdtext.setText(name);
    }


    @Override
    protected View generateCustomView(Void aVoid) {
        mRes = context.getResources();
        catContentView = View.inflate(context, R.layout.edtextselectpopupwind, null);
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        mEdtext = (EditText) catContentView.findViewById(R.id.edtext);

        catContentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DropUpEdtextPopupWindow.this.dismiss();
                    }
                });
        catContentView.findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        mSeleListener.getcancel();
                        DropUpEdtextPopupWindow.this.dismiss();
                    }
                });
        catContentView.findViewById(R.id.confirm)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        String name = mEdtext.getText().toString();
                        if (TextUtil.isEmpty(name)) {
                            CommonUtils.showToast(context, "备注不能为空");
                        } else {
                            mSeleListener.getString(name);
                            DropUpEdtextPopupWindow.this.dismiss();
                        }

                    }
                });
        return catContentView;
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
