package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.ServiceCity;
import com.tgf.kcwc.mvp.presenter.ServiceCityPresenter;
import com.tgf.kcwc.mvp.view.ServiceCityView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.LoadView;
import com.tgf.kcwc.view.SingleSelectListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */

public class ServiceCityActivity extends BaseActivity implements ServiceCityView {

    private ListView             serviceCityLv;
    private ServiceCityPresenter mServiceCityPresenter;

    private ArrayList<DataItem>  dataItems = new ArrayList<>();
    private Intent fromIntent;
    private LoadView mLoadView;
    private KPlayCarApp kPlayCarApp;
    private TextView serverCity;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        kPlayCarApp = (KPlayCarApp) getApplication();
        serviceCityLv = (ListView) findViewById(R.id.servicecity_list);
        mServiceCityPresenter = new ServiceCityPresenter();
        mServiceCityPresenter.attachView(this);
        mServiceCityPresenter.getServiceCity(kPlayCarApp.getLattitude(), kPlayCarApp.getLongitude());
        mLoadView = (LoadView) findViewById(R.id.loadView);
        fromIntent = getIntent();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicecity);
        serverCity = (TextView) findViewById(R.id.servicecity_my);
//        serverCity.setText(kPlayCarApp.serverCity.alias);
    }
    @Override
    public void showOpenlist(List<ServiceCity.City> serviceList) {
        mLoadView.setVisibility(View.INVISIBLE);
        if (serviceList != null && serviceList.size() != 0) {
            for (int i = 0; i < serviceList.size(); i++) {
                DataItem dataItem = new DataItem();
                ServiceCity.City mServiceCity = serviceList.get(i);
                dataItem.id = mServiceCity.id;
                dataItem.name = mServiceCity.name;
                dataItems.add(dataItem);
            }

            SingleSelectListView singleSelectListView = new SingleSelectListView(
                ServiceCityActivity.this, dataItems, serviceCityLv);
            singleSelectListView.setSelcetedLisenter(new SingleSelectListView.SelcetedLisenter() {
                @Override
                public void onSelected(int selectPos) {
                    CommonUtils.showToast(mContext, "selectPos" + selectPos);

                    if(fromIntent !=null){
//                        kPlayCarApp.serverCity.id = dataItems.get(selectPos).id;
//                        kPlayCarApp.serverCity.name = dataItems.get(selectPos).name;
                        fromIntent.putExtra("pos",selectPos);
                        fromIntent.putExtra("id",dataItems.get(selectPos).id);
                        fromIntent.putExtra("name",dataItems.get(selectPos).name);
                        setResult(RESULT_OK,fromIntent);
                    }
                    finish();
                }
            });
        }
    }

    @Override
    public void showSelcetCity(ServiceCity.City select) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mLoadView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingTasksError() {
        mLoadView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServiceCityPresenter.unsubscribe();
    }
}
