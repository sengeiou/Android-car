package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CarColor;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/4/6 09:31
 * E-mail：fishloveqin@gmail.com
 */

public class ColorCategoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    protected TextView mClosedBtn;
    protected TextView mDesc;
    protected ListView mList;
    private CarDataPresenter mColorTypePresenter;
    private int mSeriesId;
    private String mType = "car_series";
    private String mColorType = "in";

    private int mIntentType = 0;
    private List<CarColor> mDatas = new ArrayList<CarColor>();
    private WrapAdapter<CarColor> mAdapter = null;
    private boolean isCarSeries = true;
    private String mColorId = "";

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        initView();
        mColorTypePresenter = new CarDataPresenter();
        mColorTypePresenter.attachView(mCarColorView);
        String descValue = "";
        if (mIntentType == 1) {
            mColorType = "out";
            descValue = "外观";
        } else if (mIntentType == 2) {
            mColorType = "in";
            descValue = "内饰";
        }
        if (isCarSeries) {
            mType = "car_series";
        } else {
            mType = "car";
        }
        mClosedBtn.setText(descValue);
        backEvent(mClosedBtn);
        mColorTypePresenter.getCarCategoryColors(mSeriesId + "", mType, mColorType);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mSeriesId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mIntentType = intent.getIntExtra(Constants.IntentParams.INTENT_TYPE, -1);
        isCarSeries = intent.getBooleanExtra(Constants.IntentParams.DATA, true);
        mColorId = intent.getStringExtra(Constants.IntentParams.DATA2);
        isTitleBar = false;
        super.setContentView(R.layout.activity_color_category);

    }

    CarDataView<List<CarColor>> mCarColorView = new CarDataView<List<CarColor>>() {
        @Override
        public void showData(List<CarColor> carColors) {

            mDatas.clear();
            mDatas.addAll(carColors);
            mAdapter = new WrapAdapter<CarColor>(mContext, mDatas, R.layout.car_color_list_item) {

                protected ImageView selectStatusImg;
                protected TextView name;
                protected ImageView img;

                @Override
                public void convert(ViewHolder helper, CarColor item) {

                    img = helper.getView(R.id.img);
                    name = helper.getView(R.id.name);
                    selectStatusImg = helper.getView(R.id.select_status_img);
                    name.setText(item.name);

                    String value = item.value;
                    if (TextUtil.isEmpty(value)) {
                        value = "#ffffff";
                    }
                    String splitArrays[] = value.split(",");

                    Bitmap bitmap = BitmapUtils.getRectColors(splitArrays,
                            BitmapUtils.dp2px(mContext, 15), BitmapUtils.dp2px(mContext, 15),
                            R.color.style_bg4, 2);
                    //                    GradientDrawable drawable = new GradientDrawable();
                    //                    drawable.setColor(Color.parseColor(item.value));
                    //                    drawable.setStroke(1, mRes.getColor(R.color.style_bg4));
                    //                    img.setBackground(drawable);
                    img.setImageBitmap(bitmap);


                    if (!TextUtil.isEmpty(mColorId)) {
                        if (Integer.parseInt(mColorId) == item.id) {
                            selectStatusImg.setVisibility(View.VISIBLE);
                        } else {
                            selectStatusImg.setVisibility(View.GONE);
                        }
                    } else {
                        selectStatusImg.setVisibility(View.GONE);
                    }

                }
            };

            mList.setAdapter(mAdapter);
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

    private void initView() {
        mClosedBtn = (TextView) findViewById(R.id.closedBtn);
        mDesc = (TextView) findViewById(R.id.desc);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mColorTypePresenter != null) {
            mColorTypePresenter.detachView();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CarColor c = (CarColor) parent.getAdapter().getItem(position);
        // c.isSelected = !c.isSelected;
        //singleChecked(mDatas, c);

        Intent intent = new Intent();
        intent.putExtra(Constants.IntentParams.DATA, c);
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void singleChecked(List<CarColor> items, CarColor item) {
        for (CarColor carColor : items) {
            if (carColor.id != item.id) {
                carColor.isSelected = false;
            }
        }
        mAdapter.notifyDataSetChanged();

    }
}
