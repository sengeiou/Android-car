package com.tgf.kcwc.app.plussign;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.SponsorDrivingActivity;
import com.tgf.kcwc.driving.please.SponsorPleasePlayActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.posting.character.ReleaseCharacterActivity;
import com.tgf.kcwc.see.sale.release.ReleaseSaleActivity;
import com.tgf.kcwc.tripbook.MakeTripbookActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.TextUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mj
 * on 2016/10/28.
 */
public class PopupMenuView {

    private Context context;

    private static final String TAG = "PopupMenuUtil";

    private ViewGroup points;//小圆点指示器
    private ImageView[] ivPoints;//小圆点图片集合
    private ViewPager viewPager;
    private int totalPage;//总的页数
    private int mPageSize = 8;//每页显示的最大数量
    private List<ProductListBean> listDatas;//总的数据源
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int currentPage;//当前页

    public static PopupMenuView getInstance() {
        return MenuUtilHolder.INSTANCE;
    }

    private static class MenuUtilHolder {
        public static PopupMenuView INSTANCE = new PopupMenuView();
    }

    private View rootVew;
    private PopupWindow popupWindow;

    private RelativeLayout rlClick;
    private ImageView ivBtn;

    /**
     * 动画执行的 属性值数组
     */
    float animatorProperty[] = null;
    /**
     * 第一排图 距离屏幕底部的距离
     */
    int top = 0;
    /**
     * 第二排图 距离屏幕底部的距离
     */
    int bottom = 0;

    /**
     * 创建 popupWindow 内容
     *
     * @param context context
     */
    private void _createView(final Context context) {
        this.context = context;
        rootVew = LayoutInflater.from(context).inflate(R.layout.popup_menu_view, null);
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
            animatorProperty = new float[]{bottom, 60, -30, -20 - 10, 0};
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
    private void initLayout(final Context context) {
        //初始化控件
        iniViews();
        //模拟数据源
        setDatas();

        LayoutInflater inflater = LayoutInflater.from(context);
        //总的页数，取整（这里有三种类型：Math.ceil(3.5)=4:向上取整，只要有小数都+1  Math.floor(3.5)=3：向下取整  Math.round(3.5)=4:四舍五入）
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview_layout, viewPager, false);
            gridView.setAdapter(new MyGridViewAdapter(context, listDatas, i, mPageSize, new MyGridViewAdapter.MyOnClickListener() {
                @Override
                public void onClick(ProductListBean productListBean) {
                    MyClick(productListBean.id);
                }
            }));
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));
        //小圆点指示器
        ivPoints = new ImageView[totalPage];
        for (int i = 0; i < ivPoints.length; i++) {
            ImageView imageView = new ImageView(context);
            //设置图片的宽高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.page__selected_indicator);
            } else {
                imageView.setBackgroundResource(R.drawable.page__normal_indicator);
            }
            ivPoints[i] = imageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 20;//设置点点点view的左边距
            layoutParams.rightMargin = 20;//设置点点点view的右边距
            points.addView(imageView, layoutParams);
        }
        //设置ViewPager滑动监听
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

    }

    private void iniViews() {
        viewPager = (ViewPager) rootVew.findViewById(R.id.viewPager);
        //初始化小圆点指示器
        points = (ViewGroup) rootVew.findViewById(R.id.points);

        rlClick = (RelativeLayout) rootVew.findViewById(R.id.pop_rl_click);
        ivBtn = (ImageView) rootVew.findViewById(R.id.pop_iv_img);
        rlClick.setOnClickListener(new MViewClick(0, context));

    }


    private void setDatas() {
        Account account = IOUtils.getAccount(context);
        listDatas = new ArrayList<>();
        listDatas.add(new ProductListBean("文字", R.drawable.btn_word, 7));
        listDatas.add(new ProductListBean("贴子", R.drawable.publish_icon, 1));
        listDatas.add(new ProductListBean("开车去", R.drawable.driver_go_icon, 2));
        listDatas.add(new ProductListBean("路书", R.drawable.travel_rec_icon, 3));
        listDatas.add(new ProductListBean("车主自售", R.drawable.owner_sale_icon, 4));
        if (account != null) {
            if (account.userInfo.playAuth == 1) {
                listDatas.add(new ProductListBean("请你玩", R.drawable.btn_post_qnw, 5));
            }
        }
        // listDatas.add(new ProductListBean("评测", R.drawable.cmt_result_icon, 6));
    }

    /**
     * 点击事件
     */
    public void MyClick(int index) {
        switch (index) {
            case 0:
                //加号按钮点击之后的执行
                _rlClickAction();
                break;
            case 1:
                if (IOUtils.isLogin(context)) {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    String key = KPlayCarApp.getValue(Constants.KeyParams.KEY_DATA);
                    if (!TextUtil.isEmpty(key)) {

                        args.put(Constants.IntentParams.FROM_ID, SharedPreferenceUtil.getExhibitId(context));
                        args.put(Constants.IntentParams.FROM_TYPE, Constants.IntentParams.EVENT_VALUE);
                    }
                    CommonUtils.startNewActivity(context, args, PostingActivity.class);

                }
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
                    Account account = IOUtils.getAccount(context);
                    CommonUtils.startNewActivity(context, SponsorPleasePlayActivity.class);
                }
                _rlClickAction();
                break;
            case 6:
                CommonUtils.showToast(context, "评测");
                break;
            case 7:
                if (IOUtils.isLogin(context)) {
                    CommonUtils.startNewActivity(context, ReleaseCharacterActivity.class);
                }
                _rlClickAction();
                break;
        }
    }

    /**
     * 点击事件
     */
    private class MViewClick implements View.OnClickListener {

        public int index;
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

        _startAnimation(viewPager, 500, animatorProperty);

/*        _startAnimation(llMenuItem1, 500, animatorProperty);
        _startAnimation(llMenuItem2, 430, animatorProperty);
        _startAnimation(llMenuItem3, 430, animatorProperty);
        _startAnimation(llMenuItem4, 500, animatorProperty);
        _startAnimation(llMenuItem5, 500, animatorProperty);
        _startAnimation(llMenuItem6, 430, animatorProperty);*/
    }

    /**
     * 关闭 popupWindow执行的动画
     */
    public void _rlClickAction() {
        if (ivBtn != null && rlClick != null) {

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();
            _closeAnimation(viewPager, 300, bottom);
/*            _closeAnimation(llMenuItem1, 300, top);
            _closeAnimation(llMenuItem2, 200, top);
            _closeAnimation(llMenuItem3, 200, top);
            _closeAnimation(llMenuItem4, 300, top);
            _closeAnimation(llMenuItem5, 300, bottom);
            _closeAnimation(llMenuItem6, 200, bottom);*/

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


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //改变小圆圈指示器的切换效果
            setImageBackground(position);
            currentPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 改变点点点的切换效果
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < ivPoints.length; i++) {
            if (i == selectItems) {
                ivPoints[i].setBackgroundResource(R.drawable.page__selected_indicator);
            } else {
                ivPoints[i].setBackgroundResource(R.drawable.page__normal_indicator);
            }
        }
    }

}
