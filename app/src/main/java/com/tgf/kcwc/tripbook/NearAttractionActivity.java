package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.NearAttractionModel;
import com.tgf.kcwc.mvp.presenter.NearAttrationPresenter;
import com.tgf.kcwc.mvp.view.NearAttrationView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/10 0010
 * E-mail:hekescott@qq.com
 */

public class NearAttractionActivity extends BaseActivity implements NearAttrationView {

    private ListView nearAttralv;
    private String token;
    private NearAttrationPresenter nearAttrationPresenter;
    private ArrayList<NearAttractionModel.NearAttractionItem> mAtrationlist;
    private TextView emptyBoxTv;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        int type = getIntent().getIntExtra(Constants.IntentParams.INTENT_TYPE, 1);
        emptyBoxTv = (TextView) findViewById(R.id.nearatt_emptyboxTv);

        token = IOUtils.getToken(getContext());
        nearAttrationPresenter = new NearAttrationPresenter();
        nearAttrationPresenter.attachView(this);

        nearAttrationPresenter.getNearAttraionlist(token, type, (String) KPlayCarApp.getValue(Constants.IntentParams.LAT),
                (String) KPlayCarApp.getValue(Constants.IntentParams.LNG), 1, 9999);
        setType(type);
    }

    private void setType(int type) {
        TextView titeBarTv = (TextView) findViewById(R.id.title_bar_text);
        if (type == 1) {
            emptyBoxTv.setText(String.format(mRes.getString(R.string.nearattr_sellect_empty),"风景"));
        } else if (type == 2) {
            emptyBoxTv.setText(String.format(mRes.getString(R.string.nearattr_sellect_empty),"餐厅"));
            titeBarTv.setText("附近餐厅");
        } else if (type == 3) {
            emptyBoxTv.setText(String.format(mRes.getString(R.string.nearattr_sellect_empty),"酒店"));
            titeBarTv.setText("附近酒店");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearattraction);
        findViewById(R.id.title_bar_back).setOnClickListener(this);
        nearAttralv = (ListView) findViewById(R.id.nearattration_list);
        nearAttralv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = mAtrationlist.get(position).name;
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constants.IntentParams.TITLE, title);
                resultIntent.putExtra(Constants.IntentParams.ID, mAtrationlist.get(position).id);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void showNearAttrationlist(ArrayList<NearAttractionModel.NearAttractionItem> list) {
        mAtrationlist = list;
        if(mAtrationlist==null||mAtrationlist.size()==0){
            nearAttralv.setVisibility(View.GONE);
            emptyBoxTv.setVisibility(View.VISIBLE);
            return;
        }
        nearAttralv.setAdapter(new WrapAdapter<NearAttractionModel.NearAttractionItem>(getContext(),
                list, R.layout.listitem_tripbook_nearattr) {
            @Override
            public void convert(ViewHolder helper, NearAttractionModel.NearAttractionItem item) {
                helper.setText(R.id.nearatt_nametv, item.name);
                helper.setText(R.id.nearatt_scoretv, item.serviceScore + "分");
                helper.setText(R.id.nearatt_addresstv, item.address);
                if (StringUtils.checkNull(item.distance + "")) {
                    if (item.distance > 1) {
                        //保留小数点后面2位 km
                        double kmTwoStr = NumFormatUtil.getFmtTwoNum(item.distance);
                        helper.setText(R.id.nearatt_distancetv, kmTwoStr + "km");
                    } else {
                        //保留整数 m
                        double mDistanceStr = NumFormatUtil.getFmtNum(item.distance * 1000);
                        helper.setText(R.id.nearatt_distancetv, (int) mDistanceStr + "m");
                    }
                }
            }
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
