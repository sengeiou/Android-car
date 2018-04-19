package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/3/30 10:50
 * E-mail：fishloveqin@gmail.com
 * 车型列表
 */

public class ModelListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    protected TextView mDesc;
    protected ListView mList;
    private ArrayList<CarBean> mCars;
    private int mSeriesId;
    private int mNums;

    private int mType;

    private CarDataPresenter mPresenter;

    private String mTitle = "";

    private String mCarId = "";

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        // setTitleBarBg(R.color.white);
        //text.setText("车型列表");
        //text.setTextColor(mRes.getColor(R.color.text_color13));
    }

    @Override
    protected void setUpViews() {
        initView();
        TextView closeBtn = (TextView) findViewById(R.id.closedBtn);
        closeBtn.setText(mTitle + "");
        backEvent(closeBtn);

        // initListData();
        if (mType == 1) {

            mCars = KPlayCarApp.getValue(Constants.IntentParams.DATA);
            mNums = KPlayCarApp.getValue(Constants.IntentParams.DATA2);
            initListData(mCars);
        } else {
            mPresenter = new CarDataPresenter();
            mPresenter.attachView(mCarDataView);
            mPresenter.getCarList(mSeriesId + "");
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mSeriesId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mTitle = intent.getStringExtra(Constants.IntentParams.TITLE);
        mCarId = intent.getStringExtra(Constants.IntentParams.DATA);
        //mCars = intent.getParcelableArrayListExtra(Constants.IntentParams.DATA);
        //mNums = intent.getIntExtra(Constants.IntentParams.DATA2, -1);
        //mCars = KPlayCarApp.getValue(Constants.IntentParams.DATA);
        //mNums = KPlayCarApp.getValue(Constants.IntentParams.DATA2);
        mType = intent.getIntExtra(Constants.IntentParams.INTENT_TYPE, -1);
        isTitleBar = false;
        super.setContentView(R.layout.activity_car_model_list);

    }

    private void initView() {
        mDesc = (TextView) findViewById(R.id.desc);
        mList = (ListView) findViewById(R.id.list);
    }

    private void initListData(List<CarBean> cars) {

        mNums = cars.size();
        mDesc.setText("共" + mNums + "款车符合条件");
        WrapAdapter<CarBean> adapter = new WrapAdapter<CarBean>(mContext, cars,
                R.layout.model_list_item) {
            @Override
            public void convert(ViewHolder helper, CarBean item) {

                View view = helper.getView(R.id.selectedImg);
                if (!TextUtil.isEmpty(mCarId)) {
                    if (Integer.parseInt(mCarId) == item.id) {
                        view.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.GONE);
                    }
                } else {
                    view.setVisibility(View.GONE);
                }
                helper.setText(R.id.carName, item.name);
                helper.setText(R.id.price, item.guidePrice + "");
                helper.setText(R.id.configTv, item.enginePower);
                ImageView xcImg = helper.getView(R.id.xcImg);
                if (item.isExist == 1) {
                    xcImg.setVisibility(View.VISIBLE);
                } else {
                    xcImg.setVisibility(View.GONE);
                }
                ImageView zcImg = helper.getView(R.id.zcImg);
                if (item.isShow == 1) {
                    zcImg.setVisibility(View.VISIBLE);
                } else {
                    zcImg.setVisibility(View.GONE);
                }
            }
        };
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CarBean carBean = (CarBean) parent.getAdapter().getItem(position);

        if (mType != 1) {

            Intent intent = new Intent();
            intent.putExtra(Constants.IntentParams.ID, carBean.id);
            intent.putExtra(Constants.IntentParams.NAME, carBean.name);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, carBean.id);
            CommonUtils.startNewActivity(mContext, args, CarDetailActivity.class);
        }

    }

    CarDataView<List<CarBean>> mCarDataView = new CarDataView<List<CarBean>>() {
        @Override
        public void showData(List<CarBean> datas) {

            initListData(datas);
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
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
