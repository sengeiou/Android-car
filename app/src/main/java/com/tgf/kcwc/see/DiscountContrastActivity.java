package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.DiscountCompareAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.DiscountContrastModel;
import com.tgf.kcwc.mvp.presenter.DiscountContrastPresenter;
import com.tgf.kcwc.mvp.view.DiscountContrastView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.ThreadPoolProxy;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.VSScrollView;
import com.tgf.kcwc.view.pinnedheader.PinnedHeaderListView;

import java.util.ArrayList;
import java.util.List;

import static com.tgf.kcwc.R.id.iv_item_header_close;

/**
 * Author：Jenny
 * Date:2016/12/20 13:41
 * E-mail：fishloveqin@gmail.com
 * 优惠对比页
 */

public class DiscountContrastActivity extends BaseActivity implements DiscountContrastView {
    protected LinearLayout llContrastModelHeaderContent;
    protected VSScrollView headerContentScroll;
    protected LinearLayout llHeaderPart;
    protected TextView ownersubMainNavDivider;
    protected PinnedHeaderListView contrasrConfigListview;
    protected ImageView seeImg2;
    protected TextView toggleBtn2;
    protected LinearLayout llContrastModelHeaderContent2;
    protected VSScrollView headerContentScroll2;
    protected LinearLayout llHeaderPart2;
    private RelativeLayout mTitleBar;
    private int mCompareCount;
    private int mMaxCount = 9;
    int mOffSexX = 0;
    private DiscountContrastPresenter mPresenter;
    private boolean isOpenAdd = false;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.discount_pk_text);
        text.setTextColor(mRes.getColor(R.color.white));
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new DiscountContrastPresenter();
        mPresenter.attachView(this);


    }

    private void synScrollItems(int l, int t) {
        int size = mAdapter.mVSViews.size();

        for (int i = 0; i < size; i++) {
            mAdapter.mVSViews.get(i).smoothScrollTo(l, t);
        }
    }

    private DiscountCompareAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_discount_contrast);

    }


    private boolean isPKAll = false;

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        int id = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mPresenter.loadContrastList("" + id);

    }

    private String mIds;

    private void addHeaderItemView(int index, DiscountContrastModel.OrgBean item) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.owner_contrast_model_item_header2,
                llContrastModelHeaderContent, false);
        ImageView deleteImg = (ImageView) v.findViewById(iv_item_header_close);
        TextView nameText = (TextView) v.findViewById(R.id.tv_item_header_name);
        nameText.setText(item.name);
        nameText.setTextColor(mRes.getColor(R.color.text_color12));
        item.index = index;
        nameText.setTag(item);
        nameText.setOnClickListener(mOnClickListener);
        deleteImg.setVisibility(View.VISIBLE);
        deleteImg.setOnClickListener(this.mOnClickListener);
        deleteImg.setBackgroundDrawable(
                this.mContext.getResources().getDrawable(R.drawable.iv_header_close));
        deleteImg.setTag(R.id.tv_item_header_name, index);

        llContrastModelHeaderContent.addView(v, index);
    }

    private void addHeaderItemView2(int index, String item) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.owner_contrast_model_item_header3,
                llContrastModelHeaderContent2, false);
        TextView nameText = (TextView) v.findViewById(R.id.tv_item_header_name);
        nameText.setText(item);
        nameText.setTextColor(mRes.getColor(R.color.text_color20));
        nameText.setTag(item);
        nameText.setOnClickListener(mOnClickListener);
        llContrastModelHeaderContent2.addView(v, index);
    }

    private List<DiscountContrastModel.OrgBean> mItems = new ArrayList<DiscountContrastModel.OrgBean>();
    private List<String> mItems2 = new ArrayList<String>();
    private int mCurrentLength = 0;

    /**
     * 增加或者删除了数据项需要更新整个界面，重绘View
     */
    private void updateUI() {
        buildHeaderItem();
        mAdapter.notifyDataSetChanged();

    }

    private int id = 0;


    private void buildHeaderItem() {

        llContrastModelHeaderContent.removeAllViews();
        int size = mItems.size();
        mCompareCount = 0;
        for (int i = 0; i < size; i++) {

            DiscountContrastModel.OrgBean item = mItems.get(i);
            addHeaderItemView(i, item);
            mCompareCount++;
        }

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Object tag = v.getTag(R.id.tv_item_header_name);
            if (tag != null) {

                int index = Integer.parseInt(tag.toString());
                removeItemByIndex(index);
            }
        }
    };

    /**
     * 根据id 根据index下标本地删除
     *
     * @param index
     */
    private void removeItemByIndex(final int index) {


        showLoadingDialog("正在移出对比...");

        ThreadPoolProxy.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mItems != null && mItems.size() > 0) {

                    mItems.remove(index);
                }

                if (mItems2 != null && mItems2.size() > 0) {
                    mItems2.remove(index);
                }
                for (DiscountContrastModel.ParamBean bean : mParams) {


                    for (DiscountContrastModel.ValueBean valueBean : bean.value) {

                        List<String> values = valueBean.value;
                        if (values.size() > 0) {
                            values.remove(index);
                        }
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        });


    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            dismissLoadingDialog2();
            buildHeaderItem();//刷新数据源
            showDiscountPriceData();
            mAdapter.notifyDataSetChanged();
        }
    };
    private List<DiscountContrastModel.ParamBean> mParams = new ArrayList<DiscountContrastModel.ParamBean>();

    private boolean isFirst = true;


    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showContrastView(DiscountContrastModel model) {


        mItems.clear();
        mParams.clear();
        mItems2.clear();
        List<DiscountContrastModel.ParamBean> paramContrasts = model.list;

        /**
         *  将优惠提出做为单独一行进行显示
         *
         DiscountContrastModel.ParamBean priceBean = new DiscountContrastModel.ParamBean();priceBean.name = "最低优惠价";
        DiscountContrastModel.ValueBean valueBean = new DiscountContrastModel.ValueBean();
        valueBean.name = "最低优惠价";
        valueBean.value = model.priceDiscount.value;
       List<DiscountContrastModel.ValueBean> priceValues = new ArrayList<DiscountContrastModel.ValueBean>();
        priceValues.add(valueBean);
        priceBean.value = priceValues;
        mParams.add(priceBean); **/
        mParams.addAll(paramContrasts);
        mItems.addAll(model.orgs);
        mItems2.addAll(model.priceDiscount.value);
        buildHeaderItem();
        showDiscountPriceData();
        mAdapter = new DiscountCompareAdapter(mParams);
        mAdapter.mScrollCallback = new DiscountCompareAdapter.ScrollCallback() {
            @Override
            public void callback(int l, int t, int oldl, int oldt) {
                synScrollItems(l, t);
                headerContentScroll.smoothScrollTo(l, t);
                headerContentScroll2.smoothScrollTo(l, t);
            }
        };
        contrasrConfigListview.setAdapter(mAdapter);
        if (isFirst) {
            isFirst = false;
            View view = LayoutInflater.from(mContext).inflate(R.layout.contrast_footer_item, null,
                    false);
            contrasrConfigListview.addFooterView(view);
        }
    }

    private void showDiscountPriceData() {

        llContrastModelHeaderContent2.removeAllViews();
        int size = mItems2.size();
        for (int i = 0; i < size; i++) {

            String item = mItems2.get(i);
            addHeaderItemView2(i, item);
        }
    }

    private void initView() {
        mTitleBar = (RelativeLayout) findViewById(R.id.titleBar);
        llContrastModelHeaderContent = (LinearLayout) findViewById(
                R.id.ll_contrast_model_header_content);
        headerContentScroll = (VSScrollView) findViewById(R.id.header_content_scroll);
        headerContentScroll.mTouchView = headerContentScroll;
        headerContentScroll.setScrollChangedListener(new VSScrollView.ScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {

                synScrollItems(l, t);
            }
        });
        llHeaderPart = (LinearLayout) findViewById(R.id.ll_header_part);
        ownersubMainNavDivider = (TextView) findViewById(R.id.ownersub_main_nav_divider);
        contrasrConfigListview = (PinnedHeaderListView) findViewById(R.id.contrasr_config_listview);
        mOffSexX = BitmapUtils.dp2px(mContext, 100);
        llContrastModelHeaderContent = (LinearLayout) findViewById(R.id.ll_contrast_model_header_content);
        headerContentScroll = (VSScrollView) findViewById(R.id.header_content_scroll);
        llHeaderPart = (LinearLayout) findViewById(R.id.ll_header_part);
        seeImg2 = (ImageView) findViewById(R.id.seeImg2);
        toggleBtn2 = (TextView) findViewById(R.id.toggleBtn2);
        llContrastModelHeaderContent2 = (LinearLayout) findViewById(R.id.ll_contrast_model_header_content2);
        headerContentScroll2 = (VSScrollView) findViewById(R.id.header_content_scroll2);
        llHeaderPart2 = (LinearLayout) findViewById(R.id.ll_header_part2);
        ownersubMainNavDivider = (TextView) findViewById(R.id.ownersub_main_nav_divider);
        contrasrConfigListview = (PinnedHeaderListView) findViewById(R.id.contrasr_config_listview);
        headerContentScroll2.mTouchView = headerContentScroll;

        headerContentScroll2.setScrollChangedListener(new VSScrollView.ScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {

                synScrollItems(l, t);
            }
        });
    }
}
