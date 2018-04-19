package com.tgf.kcwc.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.squareup.leakcanary.RefWatcher;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.callback.FragmentDataCallback;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.share.BaseUIListener;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author：Jenny
 * Date:2016/8/22 16:46
 * E-mail：fishloveqin@gmail.com
 * 基类抽象Fragment,数据懒加载方式
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private boolean isVisible = false;//当前Fragment是否可见
    protected boolean isInitView = false;//是否与View建立起映射关系
    protected boolean isFirstLoad = true; //是否是第一次加载数据
    protected Context mContext;
    protected Resources mRes;
    protected View convertView;
    protected SparseArray<View> mViews;
    public DataItem mDataItem;

    public String checkCode;
    public String pwd;

    abstract protected void updateData();//更新数据

    protected BaseUIListener mBaseUIListener;

    protected RelativeLayout mEmptyLayout;

    protected void initEmptyView() {

        mEmptyLayout = (RelativeLayout) findView(R.id.emptyLayout);
    }

    public boolean isLoading;
    public boolean isAttention;//是否关注

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mBaseUIListener = new BaseUIListener(mContext);
        convertView = inflater.inflate(getLayoutId(), container, false);
        mViews = new SparseArray<>();
        initView();
        isInitView = true;
        lazyLoadData();
        return convertView;
    }

    /**
     * 分享回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param type        分享的平台类型
     */
    public void onShare(int requestCode, int resultCode, Intent data, int type) {

    }

    /**
     * 选择地址回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onAddress(int requestCode, int resultCode, Intent data) {

    }

    public void updateInfoByCity(String... args) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mRes = mContext.getResources();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();

        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    protected void lazyLoadData() {
        if (isFirstLoad) {
        } else {
        }
        if (!isFirstLoad || !isVisible || !isInitView) {
            return;
        }

        initData();
        isFirstLoad = false;
    }

    /**
     * 加载页面布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected abstract void initView();

    /**
     * 加载要显示的数据
     */
    protected void initData() {
    }

    /**
     * fragment中可以通过这个方法直接找到需要的view，而不需要进行类型强转
     *
     * @param viewId
     * @param <E>
     * @return
     */
    protected <E extends View> E findView(int viewId) {
        if (convertView != null) {
            E view = (E) mViews.get(viewId);
            if (view == null) {
                view = (E) convertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    protected void singleChecked(List<DataItem> items, DataItem item) {
        for (DataItem d : items) {
            if (d.id != item.id) {
                d.isSelected = false;
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

    public void setSenseId(int senseId) {
        this.mSenseId = senseId;
    }

    private int currentIndex;
    protected int mSenseId;

    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper()
                    .setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("数据加载中...");
        }
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    private SweetAlertDialog mLoadingDialog;

    protected void showLoadingIndicator(boolean active) {
        if (active) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    public void setCallback(FragmentDataCallback callback) {
        this.mCallback = callback;
    }

    protected FragmentDataCallback mCallback;

    protected BGARefreshLayout mRefreshLayout = null;
    private BGARefreshViewHolder refreshViewHolder = null;

    /**
     * 初始化上拉加载、下拉刷新控件
     *
     * @param bgDelegate
     */
    protected void initRefreshLayout(BGARefreshLayout.BGARefreshLayoutDelegate bgDelegate) {
        mRefreshLayout = findView(R.id.rl_modulename_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(bgDelegate);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        refreshViewHolder = new BGANormalRefreshViewHolder(mContext, true);
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

    public void registerEvent(Object obj) {

    }

    public void openSoftKeyword(final EditText editView) {

        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editView.requestFocus();
                editView.setClickable(true);
                imm.showSoftInput(editView, InputMethodManager.SHOW_FORCED);

            }
        }, 100);

    }

    public void backEvent() {

    }
    @Override public void onDestroy() {
        super.onDestroy();
        if(Constants.IS_TEST)
       KPlayCarApp.getRefWatcher(getActivity()).watch(this);
    }
}
