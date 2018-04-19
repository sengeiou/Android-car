package com.tgf.kcwc.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.FilterItem;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.ThreeLevelView;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/20 20:27
 * E-mail：fishloveqin@gmail.com
 */

public class GoodsCategoryActivity extends BaseActivity {
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    private ThreeLevelView goodsThreeLevelView;

    private FilterItem     mRootItem, mSecondItem;

    @Override
    protected void setUpViews() {

        findViewById(R.id.closedBtn).setOnClickListener(this);
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.rootView);
        View v = LayoutInflater.from(mContext).inflate(R.layout.three_level_view_region, rootLayout,
            true);
        v.setBackgroundColor(mRes.getColor(R.color.white));
        //品类
        goodsThreeLevelView = new ThreeLevelView(this, v, ThreeLevelView.FilterType.GOODS, "");
        goodsThreeLevelView.setOnSelectRootListener(new ThreeLevelView.OnSelectListener() {
            @Override
            public void onSelect(View v, FilterItem item, int position) {

                mSecondItem = null;
            }
        });

        goodsThreeLevelView.setOnSelectListener(new ThreeLevelView.OnSelectListener() {
            @Override
            public void onSelect(View v, FilterItem item, int position) {

                Object obj = goodsThreeLevelView.getSecondLevelListView().getTag();
                if (obj != null) {
                    List<FilterItem> items = (List<FilterItem>) obj;
                    singleChecked(items, item);
                }
                mSecondItem = item;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_category);
    }

    @Override
    public void onClick(View view) {

        mRootItem = goodsThreeLevelView.getRootItem();
        Intent intent = getIntent();

        if (mRootItem != null) {
            intent.putExtra(Constants.IntentParams.DATA, mRootItem);
        }

        if (mSecondItem != null) {
            intent.putExtra(Constants.IntentParams.DATA2, mSecondItem);

        }

        setResult(RESULT_OK, intent);
        finish();
    }
}
