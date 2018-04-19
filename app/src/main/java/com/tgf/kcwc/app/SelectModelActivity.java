package com.tgf.kcwc.app;

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
import com.tgf.kcwc.me.exhibition.CompileStoreExhibitionActivity;
import com.tgf.kcwc.me.exhibition.SponsorStoreExhibitionActivity;
import com.tgf.kcwc.me.storebelow.CompileStoreBelowActivity;
import com.tgf.kcwc.me.storebelow.SponsorStoreBelowActivity;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/19 15:52
 * E-mail：fishloveqin@gmail.com
 * 选择车型（所选品牌下的所有车型列表）
 */

public class SelectModelActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        CarDataView<List<CarBean>> {

    private ListView mList;
    private WrapAdapter<CarBean> mAdapter;
    private CarDataPresenter mPresenter;
    private String mBrandName;
    private String mSeriesName;

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
        mFirmId = intent.getIntExtra(Constants.IntentParams.ID2, -1);
        mBrandName = intent.getStringExtra(Constants.IntentParams.DATA) + " ";
        mSeriesName = intent.getStringExtra(Constants.IntentParams.DATA2) + " ";
        setContentView(R.layout.activity_model_list);
    }

    private int mId;
    private int mFirmId;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CarBean carBean = (CarBean) parent.getAdapter().getItem(position);
        doSelectModel(carBean);
    }

    private void doSelectModel(CarBean carBean) {
        Intent intent = null;
        String sponsor = KPlayCarApp.getValue(Constants.IntentParams.SPONSORPLEASE);
        if (StringUtils.checkNull(sponsor)) {
            KPlayCarApp.putValue(Constants.CarSeriesBack.ID, carBean.id);
            KPlayCarApp.putValue(Constants.CarSeriesBack.CARID, mId);
            KPlayCarApp.putValue(Constants.CarSeriesBack.MFIRMID, mFirmId);

            if (sponsor.equals(Constants.CarSeriesBack.SPONSORSTOREBELOW_SUCCEED)) {
                intent = new Intent(this, SponsorStoreBelowActivity.class);
            } else if (sponsor.equals(Constants.CarSeriesBack.SPONSORSTOREEXHIBITION_SUCCEED)) {
                intent = new Intent(this, SponsorStoreExhibitionActivity.class);
            } else if (sponsor.equals(Constants.CarSeriesBack.COMPILESTOREBELOW_SUCCEED)) {
                intent = new Intent(this, CompileStoreBelowActivity.class);
            } else if (sponsor.equals(Constants.CarSeriesBack.COMPILESTOREEXHIBITION_SUCCEED)) {
                intent = new Intent(this, CompileStoreExhibitionActivity.class);
            } else if (sponsor.equals(Constants.CarSeriesBack.SPONSORPLEASEPLAY_SUCCEED)) {
                intent = new Intent(this, SponsorPleasePlayActivity.class);
            } else if (sponsor.equals(Constants.CarSeriesBack.COMPILEPLEASEPLAY_SUCCEED)) {
                intent = new Intent(this, CompilePleasePlayActivity.class);
            }
        }
        if (carBean.id == 0) {
            KPlayCarApp.putValue(Constants.CarSeriesBack.NAME, mSeriesName);
        } else {
            KPlayCarApp.putValue(Constants.CarSeriesBack.NAME, mSeriesName + carBean.name);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.IntentParams.ID, carBean.id);
        intent.putExtra(Constants.IntentParams.DATA, mBrandName + carBean.name);
        intent.putExtra(Constants.CarSeriesBack.NAME, carBean.name);
        intent.putExtra(Constants.CarSeriesBack.SERIESNAME, mSeriesName);
        intent.putExtra(Constants.CarSeriesBack.MANUFACTURERNAME, mBrandName);
        intent.putExtra(Constants.CarSeriesBack.ID, carBean.id);
        intent.putExtra(Constants.CarSeriesBack.CARID, mId);
        intent.putExtra(Constants.CarSeriesBack.MFIRMID, mFirmId);
        startActivity(intent);
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
