package com.tgf.kcwc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/19 15:52
 * E-mail：fishloveqin@gmail.com
 * 选择车型（所选品牌下的所有车型列表）
 */

public class SelectModelBySeriesActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        CarDataView<List<CarBean>> {

    private ListView mList;
    private WrapAdapter<CarBean> mAdapter;
    private CarDataPresenter mPresenter;
    private boolean isSelectModel;
    private int mIndex;
    private String mBrandName;
    private String mModuleType = "";

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.select_model);
        text.setTextColor(mRes.getColor(R.color.white));

    }

    @Override
    protected void setUpViews() {

        mList = (ListView) findViewById(R.id.list);

        mPresenter = new CarDataPresenter();
        mPresenter.attachView(this);
        System.out.println("mId:" + mId);
        mPresenter.getCarBySeries(mId + "", IOUtils.getToken(mContext));
        mList.setOnItemClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mModuleType = intent.getStringExtra(Constants.IntentParams.MODULE_TYPE);
        setContentView(R.layout.activity_model_list);
    }

    private int mId;
    private int mFirmId;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CarBean carBean = (CarBean) parent.getAdapter().getItem(position);


        if (!TextUtil.isEmpty(mModuleType)) {
            switch (mModuleType) {

                case Constants.ModuleTypes.PRE_REG_CERT:


                    List<Activity> activities = KPlayCarApp.getActivityStack();
                    for (Activity a : activities) {
                        if (a instanceof SelectSeriesByFactoryBrandActivity || a instanceof FactoryBrandActivity) {
                            a.finish();
                        }
                    }


                    KPlayCarApp.putValue(Constants.KeyParams.PRE_REG_SELECT_MODEL_VALUE, carBean.factoryName + "" + carBean.seriesName + " " + carBean.name);
                    finish();
                    break;
            }
        }


    }


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
    public void showData(List<CarBean> datas) {
        String sponsor = KPlayCarApp.getValue(Constants.IntentParams.SPONSORPLEASE);

        if (sponsor != null) {

            if (sponsor.equals(Constants.CarSeriesBack.SPONSORSTOREBELOW_SUCCEED) || sponsor.equals(Constants.CarSeriesBack.SPONSORSTOREEXHIBITION_SUCCEED) || sponsor.equals(Constants.CarSeriesBack.COMPILESTOREBELOW_SUCCEED) || sponsor.equals(Constants.CarSeriesBack.COMPILESTOREEXHIBITION_SUCCEED)) {
                CarBean carBean = new CarBean();
                carBean.name = "不限车型";
                carBean.id = 0;
                datas.add(0, carBean);
            }
        }

        mAdapter = new WrapAdapter<CarBean>(mContext, datas, R.layout.select_model_item) {
            @Override
            public void convert(ViewHolder helper, CarBean item) {

                TextView tv = helper.getView(R.id.name);
                tv.setText(item.name);

            }
        };
        mList.setAdapter(mAdapter);

    }
}
