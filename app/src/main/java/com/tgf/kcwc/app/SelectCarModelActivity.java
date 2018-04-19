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
import com.tgf.kcwc.driving.please.CompilePleasePlayActivity;
import com.tgf.kcwc.driving.please.SponsorPleasePlayActivity;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.see.OwnerContrastGoListActivity;
import com.tgf.kcwc.see.OwnerSubActivity;
import com.tgf.kcwc.see.sale.release.ReleaseSaleActivity;
import com.tgf.kcwc.seecar.CommitCarOrderActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/19 15:52
 * E-mail：fishloveqin@gmail.com
 * 选择车型（所选品牌下的所有车型列表）
 */

public class SelectCarModelActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        CarDataView<List<CarBean>> {


    private ListView mList;
    private WrapAdapter<CarBean> mAdapter;
    private CarDataPresenter mPresenter;
    private String mModuleType;

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

                case Constants.ModuleTypes.RELEASE_SALE:

                    Intent intent = new Intent();
                    intent.setClass(this, ReleaseSaleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constants.IntentParams.DATA, carBean);
                    startActivity(intent);
                    finish();
                    break;

                case Constants.ModuleTypes.CONTRAST_PK:

                    doContrast(carBean);
                    break;
                case Constants.ModuleTypes.OWNER_SEE:

                    doSelectModel(carBean);
                    break;
                case Constants.ModuleTypes.SPONSORDRIVING_SUCCEED:
                    doPlaysSelectModel(carBean);
                    break;
                case Constants.ModuleTypes.COMPILEDRIVING_SUCCEED:
                    doPlaycSelectModel(carBean);
                    break;
                case Constants.ModuleTypes.TAB_HOME_SEE:
                    List<Activity> activities2 = KPlayCarApp.getActivityStack();
                    for (Activity a : activities2) {
                        if (a instanceof SelectFactorySeriesActivity || a instanceof SelectBrandActivity) {
                            a.finish();
                        }
                    }
                    RxBus.$().post("commit_order", carBean);
                    finish();

                    break;

                case Constants.ModuleTypes.PRE_REG_TICKET:


                    List<Activity> activities = KPlayCarApp.getActivityStack();
                    for (Activity a : activities) {
                        if (a instanceof SelectFactorySeriesActivity || a instanceof SelectBrandActivity) {
                            a.finish();
                        }
                    }


                    KPlayCarApp.putValue(Constants.KeyParams.PRE_REG_SELECT_MODEL_VALUE, carBean.factoryName + "" + carBean.seriesName + " " + carBean.name);
                    finish();
                    break;
            }
        }


    }


    private void doSelectModel(CarBean carBean) {
        Intent intent;
        intent = new Intent(this, CommitCarOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(Constants.IntentParams.DATA, carBean);
        startActivity(intent);
    }

    private void doPlaysSelectModel(CarBean carBean) {
        Intent intent;
        intent = new Intent(this, SponsorPleasePlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        KPlayCarApp.putValue(Constants.CarSeriesBack.NAME, carBean.factoryName + carBean.seriesName + carBean.name);
        KPlayCarApp.putValue(Constants.CarSeriesBack.ID, carBean.id);

        intent.putExtra(Constants.IntentParams.DATA, carBean);
        startActivity(intent);
    }

    private void doPlaycSelectModel(CarBean carBean) {
        Intent intent;
        intent = new Intent(this, CompilePleasePlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        KPlayCarApp.putValue(Constants.CarSeriesBack.NAME, carBean.factoryName + carBean.seriesName + carBean.name);
        KPlayCarApp.putValue(Constants.CarSeriesBack.ID, carBean.id);
        intent.putExtra(Constants.IntentParams.DATA, carBean);
        startActivity(intent);
    }

    private void doContrast(CarBean carBean) {
        List<CarBean> carBeans = KPlayCarApp.getValue(Constants.KeyParams.PK_DATAS);
        if (carBeans == null) {
            carBeans = new ArrayList<CarBean>();
            KPlayCarApp.putValue(Constants.KeyParams.PK_DATAS, carBeans);
        }

        boolean isExist = false;
        //遍历一次，根据Id去重
        for (CarBean m : carBeans) {

            if (m.id == carBean.id) {
                isExist = true;

                break;
            }
        }
        if (!isExist) {
            carBeans.add(carBean);
        } else {
            CommonUtils.showToast(mContext, "亲，该车型已增加到PK列表啦");
        }

        Boolean b = KPlayCarApp.getValue(Constants.KeyParams.IS_CONTRAST);
        if (b != null) {
            if (!b) {
                Intent intent = new Intent(this, OwnerSubActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                carBean.isSelected = true;
                Intent intent = new Intent(this, OwnerContrastGoListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

        Intent intent = getIntent();
        String seriesName = intent.getStringExtra(Constants.IntentParams.SERIES_NAME);
        String factoryName = intent.getStringExtra(Constants.IntentParams.FACTORY_NAME);
        if (Constants.ModuleTypes.RELEASE_SALE.equals(mModuleType)
                || Constants.ModuleTypes.OWNER_SEE.equals(mModuleType)
                || Constants.ModuleTypes.TAB_HOME_SEE.equals(mModuleType)) {

            CarBean carBean = new CarBean();
            carBean.name = "不限车型";
            carBean.id = 0;
            carBean.seriesName = seriesName;
            carBean.factoryName = factoryName;
            carBean.seriesId = mId;
            datas.add(0, carBean);

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
