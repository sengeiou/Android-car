package com.tgf.kcwc.driving.driv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyCitySelectView;

/**
 * Created by Administrator on 2017/5/3.
 */

public class SelectCityActivity extends BaseActivity {

    MyCitySelectView myCitySelectView;
    private Intent fromIntent;
    private KPlayCarApp mKPlayCarApp;
    private ImageButton back;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTitleBar = false;
        fromIntent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_select_city);
        mKPlayCarApp = (KPlayCarApp) getApplication();
        back = (ImageButton) findViewById(R.id.title_bar_back);
        myCitySelectView = (MyCitySelectView) findViewById(R.id.MyCitySelectView);
        myCitySelectView.setOnCitySelect(new MyCitySelectView.OnCitySelect() {
            @Override
            public void citySelect(String name, Brand brand) {
                fromIntent.putExtra(Constants.IntentParams.DATA, name);
                fromIntent.putExtra(Constants.IntentParams.DATA2, brand);
                fromIntent.putExtra(Constants.IntentParams.DATA3, brand.id+"");
                mKPlayCarApp.cityId = brand.id;
                mKPlayCarApp.locCityName = brand.brandName;
                mKPlayCarApp.adcode =brand.adcode;
                setResult(RESULT_OK, fromIntent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
