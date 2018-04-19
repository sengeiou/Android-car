package com.tgf.kcwc.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.FilterItem;
import com.tgf.kcwc.share.BaseUIListener;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.LoadView;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author：Jenny
 * Date:2016/12/8 15:07
 * E-mail：fishloveqin@gmail.com
 * Base页面，主要抽象子页面公共资源
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected Context mContext;
    protected Resources mRes;
    private ImageButton mBackBtn;                      //标题栏返回按钮
    private FunctionView mFunctionBtn;                  //标题栏功能按钮
    private TextView mTitleText;                    //标题文本
    protected boolean isTitleBar = true;
    private LoadView mLoadView;
    protected KPlayCarApp mGlobalContext;
    protected int Basecolors = R.color.style_bg7;

    protected int mPageIndex = 1;
    protected int mPageSize = 6;

    protected BaseUIListener mBaseUIListener;               //qq、qq空间回调

    protected String[] ageGroups = null;
    protected String[] industrys = null;
    protected String[] budgets = null;
    private KPlayCarApp mApp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        mContext = this;
        mBaseUIListener = new BaseUIListener(mContext);
        mGlobalContext = (KPlayCarApp) getApplication();
        setStatusBar(Basecolors);
        super.onCreate(savedInstanceState);
        mApp = (KPlayCarApp) getApplication();
        mApp.addActivity(this);

        mRes = mContext.getResources();
        ageGroups = mRes.getStringArray(R.array.age_group_values);
        industrys = mRes.getStringArray(R.array.industry_items);
        budgets = mRes.getStringArray(R.array.buy_car_budget);
    }

    protected void setStatusBar(int colors) {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colors);
    }

    //标题栏初始化
    private void initTitleBar() {
        mBackBtn = (ImageButton) findViewById(R.id.title_bar_back);
        mFunctionBtn = (FunctionView) findViewById(R.id.title_function_btn);
        mTitleText = (TextView) findViewById(R.id.title_bar_text);

        //事件响应
        titleBarCallback(mBackBtn, mFunctionBtn, mTitleText);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 视图控件初始化
     */
    abstract protected void setUpViews();

    /**
     * //标题栏事件回调
     *
     * @param back     返回按钮
     * @param function 交互控件View
     * @param text     标题文本
     */
    protected abstract void titleBarCallback(ImageButton back, FunctionView function,
                                             TextView text);

    /**
     * 重写用于初始化 setUpViews、initTitleBar方法
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setUpViews();
        if (isTitleBar) {
            initTitleBar();
        }
    }

    protected void setTitleBarBg(int resColor) {
        findViewById(R.id.title_layout).setBackgroundColor(mRes.getColor(resColor));
    }

    protected void setTitleBarDrawable(int drawableId) {
        findViewById(R.id.titleBar).setBackgroundResource(drawableId);
    }

    @Override
    protected void onDestroy() {
        mApp.removeActivity(this);
        super.onDestroy();
        if(Constants.IS_TEST)
        KPlayCarApp.getRefWatcher(this).watch(this);
    }

    /**
     * 重写用于初始化 setUpViews、initTitleBar方法
     *
     * @param view
     */
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setUpViews();
        if (isTitleBar) {
            initTitleBar();
        }
    }

    /**
     * 重写用于初始化 setUpViews、initTitleBar方法
     *
     * @param view
     * @param params
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setUpViews();
        if (isTitleBar) {
            initTitleBar();
        }

    }

    /**
     * 返回事件
     *
     * @param button
     */
    protected void backEvent(View button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 显示加载动画
     * 注意：当前Layout下必须有LoadView控件的声明，否则会抛空指针异常
     */
    protected void showLoadView() {
        findViewById(R.id.loadView).setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏加载动画
     * 注意：当前Layout下必须有LoadView控件的声明，否则会抛空指针异常
     */
    protected void hideLoadView() {
        findViewById(R.id.loadView).setVisibility(View.GONE);
    }

    protected void singleChecked(List<DataItem> items, DataItem item) {
        for (DataItem d : items) {
            if (d.id != item.id) {
                d.isSelected = false;
            }
        }

    }

    protected void singleChecked(List<FilterItem> items, FilterItem item) {
        for (FilterItem f : items) {
            if (f.id != item.id) {
                f.isSelected = false;
            }
        }

    }

    protected String getItemValue(List<DataItem> items) {
        String name = "";
        for (DataItem item : items) {
            if (item.isSelected) {
                name = item.name;
            }
        }
        return name;
    }

    protected int getItemIndex(List<DataItem> items) {
        int index = -1;
        for (DataItem item : items) {
            index++;
            if (item.isSelected) {
                return index;
            }
        }
        return index;
    }

    protected BGARefreshLayout mRefreshLayout = null;
    private BGARefreshViewHolder refreshViewHolder = null;

    /**
     * 初始化上拉加载、下拉刷新控件
     *
     * @param bgDelegate
     */
    protected void initRefreshLayout(BGARefreshLayout.BGARefreshLayoutDelegate bgDelegate) {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(bgDelegate);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        refreshViewHolder = new BGANormalRefreshViewHolder(this, true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在activity的onStart方法中调用，自动进入正在刷新状态获取最新数据
    protected void beginRefreshing() {
        mRefreshLayout.beginRefreshing();
    }

    // 通过代码方式控制进入加载更多状态
    protected void beginLoadingMore() {
        mRefreshLayout.beginLoadingMore();
    }

    protected void stopRefreshAll() {
        mRefreshLayout.endLoadingMore();
        mRefreshLayout.endRefreshing();
    }

    protected void showLoadingDialog(String text) {

        if (mLoadingDialog2 == null) {
            mLoadingDialog2 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog2.getProgressHelper()
                    .setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog2.setCancelable(false);
            mLoadingDialog2.setTitleText(text);
        }

        mLoadingDialog2.show();

    }

    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper()
                    .setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(true);
            mLoadingDialog.setTitleText("数据加载中...");
        }
        if(!mLoadingDialog.isShowing()){
            mLoadingDialog.show();
        }

    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null&&mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    protected void dismissLoadingDialog2() {
        if (mLoadingDialog2 != null) {
            mLoadingDialog2.dismiss();
        }
    }

    protected SweetAlertDialog mLoadingDialog, mLoadingDialog2;

    protected void showLoadingIndicator(boolean active) {
        if (active) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    /**
     * 改变字体颜色
     *
     * @param textView
     * @param src
     * @param sta
     * @param end
     * @param color
     */
    public void setTextColors(TextView textView, String src, int sta, int end, int color) {
        SpannableString spannableString = new SpannableString(src);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(mRes.getColor(color));
        spannableString.setSpan(colorSpan, sta, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    protected RelativeLayout mEmptyLayout;

    protected void initEmptyView() {

        mEmptyLayout = (RelativeLayout) findViewById(R.id.emptyLayout);
    }
}
