package com.tgf.kcwc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.FactorySeriesModel;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.mvp.view.DividerItemDecoration;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tgf.kcwc.R.id.rv;

/**
 * Author：Jenny
 * Date:2016/12/19 15:52
 * E-mail：fishloveqin@gmail.com
 * 选择厂商车系,按厂商分组
 */

public class SelectFactorySeriesActivity extends BaseActivity implements
        CarDataView<FactorySeriesModel> {
    protected RecyclerView mRv;
    protected IndexBar mIndexBar;
    protected TextView tvSideBarHint;
    private CarDataPresenter mPresenter;
    private String mModuleType;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.select_series);
        text.setTextColor(mRes.getColor(R.color.white));

    }

    @Override
    protected void setUpViews() {

        initView();
        mPresenter = new CarDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getFactorySeries(builderReqParams());
    }

    private Map<String, String> builderReqParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("brand_id", mId + "");
        params.put("token", IOUtils.getToken(mContext));
        return params;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mModuleType = intent.getStringExtra(Constants.IntentParams.MODULE_TYPE);
        setContentView(R.layout.activity_factory_series);

    }

    private int mId;


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
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

    }

    @Override
    public void showData(FactorySeriesModel model) {

        showSeriesData(model.list);
    }

    private void initView() {
        mRv = (RecyclerView) findViewById(rv);
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);
        tvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
    }

    private SuspensionDecoration mDecoration;

    private void showSeriesData(List<FactorySeriesModel.Factory> factorys) {


        List<FactorySeriesModel.Factory.Series> series = new ArrayList<FactorySeriesModel.Factory.Series>();

        for (FactorySeriesModel.Factory factory : factorys) {

            List<FactorySeriesModel.Factory.Series> fSeries = factory.series;

            for (FactorySeriesModel.Factory.Series s : fSeries) {
                s.setBaseIndexTag(factory.name);
                s.title = factory.name;
            }
            series.addAll(fSeries);

        }


        CommonAdapter<FactorySeriesModel.Factory.Series> adapter = new CommonAdapter<FactorySeriesModel.Factory.Series>(mContext, R.layout.select_factory_series_list_item, series) {
            @Override
            public void convert(ViewHolder holder, FactorySeriesModel.Factory.Series item) {
                TextView brandNameText = holder.getView(R.id.name);
                brandNameText.setText(item.name);
                SimpleDraweeView simpleDraweeView = holder.getView(R.id.img);
                simpleDraweeView.setImageURI(URLUtil.builderImgUrl(item.logo, 270, 203));
                ImageView imageView = holder.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                    brandNameText
                            .setTextColor(mContext.getResources().getColor(R.color.text_color6));
                } else {
                    imageView.setVisibility(View.GONE);
                    brandNameText
                            .setTextColor(mContext.getResources().getColor(R.color.text_color12));
                }
            }
        };
        // adapter.setOnItemClickListener(this);
        adapter.setOnItemClickListener(mItemClickListener);
        mRv.setAdapter(adapter);
        mDecoration = new SuspensionDecoration(mContext, series);
        mDecoration.setColorTitleBg(

                getContext().getResources().getColor(R.color.style_bg4));
        mDecoration.setColorTitleFont(getContext().getResources().getColor(R.color.text_color15));
        //  mDecoration.setHeaderViewCount(1);
        mRv.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(
                new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        mIndexBar.setVisibility(View.GONE);
        mIndexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true);//设置需要真实的索引

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRv.setLayoutManager(mManager);//设置RecyclerView的LayoutManager
        mIndexBar.getDataHelper().sortSourceDatas(series);
        mIndexBar.setmSourceDatas(series);
        mIndexBar.invalidate();
    }

    private OnItemClickListener<FactorySeriesModel.Factory.Series> mItemClickListener = new OnItemClickListener<FactorySeriesModel.Factory.Series>() {
        @Override
        public void onItemClick(ViewGroup parent, View view, FactorySeriesModel.Factory.Series s, int position) {


            String modelType = KPlayCarApp.getValue(Constants.KeyParams.PRE_REG_SELECT_MODEL);
            if (!TextUtil.isEmpty(modelType) && Constants.CheckInFormKey.SERIES.equals(modelType)) {

                KPlayCarApp.putValue(Constants.KeyParams.PRE_REG_SELECT_MODEL_VALUE, s.title + " " + s.name);
                // finish();

                List<Activity> activities = KPlayCarApp.getActivityStack();
                for (Activity a : activities) {
                    if (a instanceof SelectBrandActivity || a instanceof SelectFactorySeriesActivity) {
                        a.finish();
                    }
                }
                return;
            }


            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, s.id);
            args.put(Constants.IntentParams.MODULE_TYPE, mModuleType);
            args.put(Constants.IntentParams.SERIES_NAME, s.name);
            args.put(Constants.IntentParams.FACTORY_NAME, s.title);
            CommonUtils.startNewActivity(mContext, args, SelectCarModelActivity.class);

        }

        @Override
        public boolean onItemLongClick(ViewGroup parent, View view, FactorySeriesModel.Factory.Series s, int position) {
            return false;
        }
    };
}
