package com.tgf.kcwc.app;

import android.content.Context;
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
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/19 15:52
 * E-mail：fishloveqin@gmail.com
 * 选择车系
 */

public class SelectSeriesActivity extends BaseActivity implements AdapterView.OnItemClickListener,
                                  CarDataView<List<CarBean>> {

    private ListView             mList;
    private WrapAdapter<CarBean> mAdapter;
    private CarDataPresenter     mPresenter;
    private String mBrandName;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.select_series);
        text.setTextColor(mRes.getColor(R.color.white));

    }

    @Override
    protected void setUpViews() {

        mList = (ListView) findViewById(R.id.list);

        mPresenter = new CarDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getSeriesByBrand(mId + "", IOUtils.getToken(mContext));
        mList.setOnItemClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        mBrandName = getIntent().getStringExtra(Constants.IntentParams.DATA)+" ";
        setContentView(R.layout.activity_model_list);
    }

    private int mId;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CarBean carBean = (CarBean) parent.getAdapter().getItem(position);

        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.ID, carBean.id);
        args.put(Constants.IntentParams.DATA,mBrandName);
        args.put(Constants.IntentParams.DATA2,carBean.name);
        args.put(Constants.IntentParams.ID2,mId);
        CommonUtils.startNewActivity(this, args, SelectModelActivity.class);
        // KPlayCarApp.putValue(Constants.KeyParams.CAR_MODEL_KEY, moto);
//        if(mIndex!=-1){
//            finish();
//        }
       // finish();
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
