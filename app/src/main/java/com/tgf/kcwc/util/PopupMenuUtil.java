package com.tgf.kcwc.util;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tgf.kcwc.R;
import com.tgf.kcwc.driving.driv.SponsorDrivingActivity;
import com.tgf.kcwc.driving.please.SponsorPleasePlayActivity;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.see.sale.release.ReleaseSaleActivity;
import com.tgf.kcwc.tripbook.MakeTripbookActivity;

/**
 * Created by mj
 * on 2016/10/28.
 */
public class PopupMenuUtil {

    private static final String TAG = "PopupMenuUtil";

    public static PopupMenuUtil getInstance() {
        return MenuUtilHolder.INSTANCE;
    }

    private static class MenuUtilHolder {
        public static PopupMenuUtil INSTANCE = new PopupMenuUtil();
    }

    private View           rootVew;
    private PopupWindow    popupWindow;

    private RelativeLayout rlClick;
    private ImageView      ivBtn;
    private LinearLayout   llMenuItem1, llMenuItem2, llMenuItem3, llMenuItem4, llMenuItem5,
            llMenuItem6;

    /**
     * 动画执行的 属性值数组
     */
    float                  animatorProperty[] = null;
    /**
     * 第一排图 距离屏幕底部的距离
     */
    int                    top                = 0;
    /**
     * 第二排图 距离屏幕底部的距离
     */
    int                    bottom             = 0;

    /**
     * 创建 popupWindow 内容
     *
     * @param context context
     */
    private void _createView(final Context context) {
        rootVew = LayoutInflater.from(context).inflate(R.layout.popup_menu, null);
        popupWindow = new PopupWindow(rootVew, LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
        //设置为失去焦点 方便监听返回键的监听
        popupWindow.setFocusable(false);

        // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
        //popupWindow.setClippingEnabled(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);

        if (animatorProperty == null) {
            top = dip2px(context, 310);
            bottom = dip2px(context, 210);
            animatorProperty = new float[] { bottom, 60, -30, -20 - 10, 0 };
        }

        initLayout(context);
    }

    /**
     * dp转化为px
     *
     * @param context  context
     * @param dipValue dp value
     * @return 转换之后的px值
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 初始化 view
     */
    private void initLayout(Context context) {
        rlClick = (RelativeLayout) rootVew.findViewById(R.id.pop_rl_click);
        ivBtn = (ImageView) rootVew.findViewById(R.id.pop_iv_img);
        llMenuItem1 = (LinearLayout) rootVew.findViewById(R.id.menuItem1);
        llMenuItem2 = (LinearLayout) rootVew.findViewById(R.id.menuItem2);
        llMenuItem3 = (LinearLayout) rootVew.findViewById(R.id.menuItem3);
        llMenuItem4 = (LinearLayout) rootVew.findViewById(R.id.menuItem4);
        llMenuItem5 = (LinearLayout) rootVew.findViewById(R.id.menuItem5);
        llMenuItem6 = (LinearLayout) rootVew.findViewById(R.id.menuItem6);

        rlClick.setOnClickListener(new MViewClick(0, context));

        llMenuItem1.setOnClickListener(new MViewClick(1, context));
        llMenuItem2.setOnClickListener(new MViewClick(2, context));
        llMenuItem3.setOnClickListener(new MViewClick(3, context));
        llMenuItem4.setOnClickListener(new MViewClick(4, context));
        llMenuItem5.setOnClickListener(new MViewClick(5, context));
        llMenuItem6.setOnClickListener(new MViewClick(6, context));

    }

    /**
     * 点击事件
     */
    private class MViewClick implements View.OnClickListener {

        public int     index;
        public Context context;

        public MViewClick(int index, Context context) {
            this.index = index;
            this.context = context;
        }

        @Override
        public void onClick(View v) {


                switch (index) {

                    case 0:
                        //加号按钮点击之后的执行
                        _rlClickAction();
                        break;
                    case 1:
                        if (IOUtils.isLogin(context))
                        CommonUtils.startNewActivity(context, PostingActivity.class);
                        _rlClickAction();
                        break;
                    case 2:
                        if (IOUtils.isLogin(context)) {
                            CommonUtils.startNewActivity(context, SponsorDrivingActivity.class);
                        }
                        _rlClickAction();
                        break;
                    case 3:
                        if (IOUtils.isLogin(context)) {
                            Intent toSelecRoadLine = new Intent(context,
                                    MakeTripbookActivity.class);
//                            toSelecRoadLine.putExtra(Constants.IntentParams.INTENT_TYPE, 100);
                            context.startActivity(toSelecRoadLine);
                        }
                        _rlClickAction();
                        break;

                    case 4:
                        if (IOUtils.isLogin(context))
                        CommonUtils.startNewActivity(context, ReleaseSaleActivity.class);
                        _rlClickAction();
                        break;
                    case 5:
                        if (IOUtils.isLogin(context)) {
                            CommonUtils.startNewActivity(context, SponsorPleasePlayActivity.class);
                        }
                        _rlClickAction();
                        break;
                    case 6:
                        CommonUtils.showToast(context, "评测");
                        break;
                }
            }

    }

    Toast toast = null;

    /**
     * 防止toast 多次被创建
     *
     * @param context context
     * @param str     str
     */
    private void showToast(Context context, String str) {
        if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
        }
        toast.show();
    }

    /**
     * 刚打开popupWindow 执行的动画
     */
    private void _openPopupWindowAction() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 0f, 135f);
        objectAnimator.setDuration(200);
        objectAnimator.start();

        _startAnimation(llMenuItem1, 500, animatorProperty);
        _startAnimation(llMenuItem2, 430, animatorProperty);
        _startAnimation(llMenuItem3, 430, animatorProperty);
        _startAnimation(llMenuItem4, 500, animatorProperty);
        _startAnimation(llMenuItem5, 500, animatorProperty);
        _startAnimation(llMenuItem6, 430, animatorProperty);
    }

    /**
     * 关闭 popupWindow执行的动画
     */
    public void _rlClickAction() {
        if (ivBtn != null && rlClick != null) {

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();

            _closeAnimation(llMenuItem1, 300, top);
            _closeAnimation(llMenuItem2, 200, top);
            _closeAnimation(llMenuItem3, 200, top);
            _closeAnimation(llMenuItem4, 300, top);
            _closeAnimation(llMenuItem5, 300, bottom);
            _closeAnimation(llMenuItem6, 200, bottom);

            rlClick.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _close();
                }
            }, 300);

        }
    }

    /**
     * 弹起 popupWindow
     *
     * @param context context
     * @param parent  parent
     */
    public void _show(Context context, View parent) {
        _createView(context);
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
            _openPopupWindowAction();
        }
    }

    /**
     * 关闭popupWindow
     */

    public void _close() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * @return popupWindow 是否显示了
     */
    public boolean _isShowing() {
        if (popupWindow == null) {
            return false;
        } else {
            return popupWindow.isShowing();
        }
    }

    /**
     * 关闭 popupWindow 时的动画
     *
     * @param view     mView
     * @param duration 动画执行时长
     * @param next     平移量
     */
    private void _closeAnimation(View view, int duration, int next) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, next);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 启动动画
     *
     * @param view     view
     * @param duration 执行时长
     * @param distance 执行的轨迹数组
     */
    private void _startAnimation(View view, int duration, float[] distance) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim.setDuration(duration);
        anim.start();
    }

}
