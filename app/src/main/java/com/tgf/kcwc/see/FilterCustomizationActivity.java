package com.tgf.kcwc.see;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.BrandListsActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.presenter.SetCustomizationPresenter;
import com.tgf.kcwc.mvp.view.SetCustomizationView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.rangeseekbar.RangeBar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Author：Jenny
 * Date:2016/12/13 15:46
 * E-mail：fishloveqin@gmail.com
 */

public class FilterCustomizationActivity extends BaseActivity implements SetCustomizationView {

    private WrapAdapter<DataItem>           mCCAdapter;
    private WrapAdapter<DataItem>           mDrivingAdapter;
    private WrapAdapter<DataItem>           mPowerAdapter;
    private List<DataItem>                  mCCLists                    = null;
    private List<DataItem>                  mDrivingLists               = null;
    private List<DataItem>                  mPowerLists                 = null;
    private RangeBar                        mRangeBar;
    private ScrollView                      mScrollView;
    private FlowLayout                      mBrandView;
    private List<Brand>                     brandLists                  = null;
    private String[]                        mCustomTypes                = null;
    private String[]                        mDriverTypes                = null;
    private Button                          mOriBtn, mElectricBtn, mHybridBtn;
    private Button                          mAddBrandBtn;
    private Button                          mConfirmBtn;
    private static final int                REQUEST_CODE                = 1;
    private GridView                        mCCGridView, mDriverView, mPowerView;

    private SetCustomizationPresenter       mPresenter;
    private int                             mPriceMin;
    private int                             mPriceMax;
    private AdapterView.OnItemClickListener mCCItemClickListener        = new AdapterView.OnItemClickListener() {
                                                                            @Override
                                                                            public void onItemClick(AdapterView<?> parent,
                                                                                                    View view,
                                                                                                    int position,
                                                                                                    long id) {

                                                                                DataItem item = (DataItem) parent
                                                                                    .getAdapter()
                                                                                    .getItem(
                                                                                        position);
                                                                                item.isSelected = true;
                                                                                singleChecked(
                                                                                    mCCLists, item);
                                                                                mCCAdapter
                                                                                    .notifyDataSetChanged();
                                                                            }
                                                                        };

    private AdapterView.OnItemClickListener mDrivingKMItemClickListener = new AdapterView.OnItemClickListener() {
                                                                            @Override
                                                                            public void onItemClick(AdapterView<?> parent,
                                                                                                    View view,
                                                                                                    int position,
                                                                                                    long id) {

                                                                                DataItem item = (DataItem) parent
                                                                                    .getAdapter()
                                                                                    .getItem(
                                                                                        position);
                                                                                item.isSelected = true;
                                                                                singleChecked(
                                                                                    mDrivingLists,
                                                                                    item);
                                                                                mDrivingAdapter
                                                                                    .notifyDataSetChanged();
                                                                            }
                                                                        };

    private AdapterView.OnItemClickListener mPowerItemClickListener     = new AdapterView.OnItemClickListener() {
                                                                            @Override
                                                                            public void onItemClick(AdapterView<?> parent,
                                                                                                    View view,
                                                                                                    int position,
                                                                                                    long id) {

                                                                                DataItem item = (DataItem) parent
                                                                                    .getAdapter()
                                                                                    .getItem(
                                                                                        position);
                                                                                item.isSelected = true;
                                                                                singleChecked(
                                                                                    mPowerLists,
                                                                                    item);
                                                                                mPowerAdapter
                                                                                    .notifyDataSetChanged();
                                                                            }
                                                                        };

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        mConfirmBtn = (Button) findViewById(R.id.confirmBtn);
        mConfirmBtn.setOnClickListener(this);
        if (flag) {
            text.setText(R.string.custom_title);
            mConfirmBtn.setText("确认定制");

        } else {
            text.setText(R.string.more_filter);
            mConfirmBtn.setText("确定");
        }

        text.setTextColor(mRes.getColor(R.color.text_nav_def_color));
        function.setTextResource(R.string.reset, R.color.tab_text_s_color, 16);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reset();

            }
        });
    }

    private void reset() {

        //重置价格
        if (mRangeBar != null) {
            mRangeBar.setTickCount(49);
        }
        type = 1;
        //重置动力
        if (mOriBtn != null && mElectricBtn != null && mHybridBtn != null) {
            mOriBtn.setBackgroundResource(R.drawable.button_bg);
            mElectricBtn.setBackgroundResource(R.drawable.button_bg);
            mHybridBtn.setBackgroundResource(R.drawable.button_bg);
        }

        //重置品牌
        mBrandView.removeAllViews();
        mDatas.clear();

        //重置排量

        resetItemStatus(mCCLists);

        mCCAdapter.notifyDataSetChanged();

        //重置续航里程

        resetItemStatus(mDrivingLists);
        mDrivingAdapter.notifyDataSetChanged();

        //重置电机功率
        resetItemStatus(mPowerLists);
        mPowerAdapter.notifyDataSetChanged();
    }

    private void resetItemStatus(List<DataItem> items) {

        for (DataItem item : items) {
            item.isSelected = false;
        }
    }

    @Override
    protected void setUpViews() {
        mPresenter = new SetCustomizationPresenter();
        mPresenter.attachView(this);
        mCustomTypes = mRes.getStringArray(R.array.custom_type);
        mDriverTypes = mRes.getStringArray(R.array.driver_type);
        initCommonViews();
        mOriBtn = (Button) findViewById(R.id.oilBtn);
        mElectricBtn = (Button) findViewById(R.id.electricBtn);
        mHybridBtn = (Button) findViewById(R.id.hybridBtn);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mBrandView = (FlowLayout) findViewById(R.id.brandLists);
        mAddBrandBtn = (Button) findViewById(R.id.addBrandBtn);
        mAddBrandBtn.setOnClickListener(this);
        mBrandView.setVerticalSpacing(BitmapUtils.dp2px(mContext, 10));
        mBrandView.setHorizontalSpacing(BitmapUtils.dp2px(mContext, 10));

        mRangeBar = (RangeBar) findViewById(R.id.rangebar);
        mRangeBar.setTickCount(30);
        mRangeBar.setBarColor(R.color.app_layout_bg_color);
        //设置索引的显示值
        mRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex,
                                              int rightThumbIndex) {

                mPriceMin = leftThumbIndex;
                mPriceMax = rightThumbIndex;
            }
        });
        mOriBtn.setText(mDriverTypes[0]);
        mElectricBtn.setText(mDriverTypes[1]);
        mHybridBtn.setText(mDriverTypes[2]);
        mOriBtn.setOnClickListener(this);
        mElectricBtn.setOnClickListener(this);
        mHybridBtn.setOnClickListener(this);
        mCCGridView = (GridView) findViewById(R.id.ccGridView);
        mDriverView = (GridView) findViewById(R.id.drivingGridView);
        mPowerView = (GridView) findViewById(R.id.powerGridView);
        mCCLists = new ArrayList<DataItem>();
        mDrivingLists = new ArrayList<DataItem>();
        mPowerLists = new ArrayList<DataItem>();

        initItemDatas();
        mCCAdapter = new WrapAdapter<DataItem>(mContext, mCCLists, R.layout.grid_item) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                showBtn(helper, item);
            }
        };

        mDrivingAdapter = new WrapAdapter<DataItem>(mContext, mDrivingLists, R.layout.grid_item) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                showBtn(helper, item);
            }
        };

        mPowerAdapter = new WrapAdapter<DataItem>(mContext, mPowerLists, R.layout.grid_item) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                showBtn(helper, item);
            }
        };
        mCCGridView.setAdapter(mCCAdapter);
        mDriverView.setAdapter(mDrivingAdapter);
        mPowerView.setAdapter(mPowerAdapter);
        ViewUtil.setListViewHeightBasedOnChildren(mCCGridView,3);
        ViewUtil.setListViewHeightBasedOnChildren(mDriverView,3);
        ViewUtil.setListViewHeightBasedOnChildren(mPowerView,3);
        mCCGridView.setOnItemClickListener(mCCItemClickListener);
        mDriverView.setOnItemClickListener(mDrivingKMItemClickListener);
        mPowerView.setOnItemClickListener(mPowerItemClickListener);
    }

    private void addBrandItems(List<Brand> datas) {

        mBrandView.removeAllViews();
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            Brand b = datas.get(i);
            View v = LayoutInflater.from(mContext).inflate(R.layout.text_tag_item, mBrandView,
                false);
            TextView tv = (TextView) v.findViewById(R.id.name);
            tv.setText(b.brandName + "");
            tv.setTag(b.brandId);
            ImageView imgView = (ImageView) v.findViewById(R.id.img);
            imgView.setTag(i);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mBrandView.removeViewAt(Integer.parseInt(v.getTag().toString()));
                    mBrandView.invalidate();
                }
            });
            mBrandView.addView(v);
        }
    }

    private void showBtn(WrapAdapter.ViewHolder helper, DataItem item) {
        Button btn = helper.getView(R.id.name);
        btn.setText(item.name);
        if (item.isSelected) {
            btn.setBackgroundColor(getResources().getColor(R.color.btn_select_color));
            btn.setTextColor(mRes.getColor(R.color.white));
        } else {
            btn.setBackgroundResource(R.drawable.button_bg);
            btn.setTextColor(mRes.getColor(R.color.text_color));
        }
    }

    private void initItemDatas() {
        String[] ccArrays = mRes.getStringArray(R.array.seat_values);
        String[] driverKMArrays = mRes.getStringArray(R.array.driver_km_values);
        String[] powerArrays = mRes.getStringArray(R.array.power_values);
        int size = ccArrays.length;

        for (int i = 0; i < size; i++) {
            DataItem item = new DataItem();
            item.name = ccArrays[i];
            item.id = i + 1;
            mCCLists.add(item);
        }
        size = driverKMArrays.length;
        for (int i = 0; i < size; i++) {
            DataItem item = new DataItem();
            item.name = driverKMArrays[i];
            item.id = i + 1;
            mDrivingLists.add(item);
        }
        size = powerArrays.length;
        for (int i = 0; i < size; i++) {
            DataItem item = new DataItem();
            item.name = powerArrays[i];
            item.id = i + 1;
            mPowerLists.add(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = getIntent().getBooleanExtra(Constants.IntentParams.DATA, false);
        setContentView(R.layout.activity_fitler_custom_layout);

    }

    private boolean flag = false;

    private void initCommonViews() {

        LinearLayout driverLayout = (LinearLayout) findViewById(R.id.driverLayout);
        TextView driverText = (TextView) driverLayout.findViewById(R.id.title);
        driverText.setText(mCustomTypes[0]);

        LinearLayout priceLayout = (LinearLayout) findViewById(R.id.priceLayout);
        TextView priceText = (TextView) priceLayout.findViewById(R.id.title);
        priceText.setText(mCustomTypes[1]);

        LinearLayout brandLayout = (LinearLayout) findViewById(R.id.brandLayout);
        TextView brandText = (TextView) brandLayout.findViewById(R.id.title);
        brandText.setText(mCustomTypes[2]);

        LinearLayout modelLayout = (LinearLayout) findViewById(R.id.modelLayout);
        TextView modelText = (TextView) modelLayout.findViewById(R.id.title);
        modelText.setText(mCustomTypes[3]);

        LinearLayout ccLayout = (LinearLayout) findViewById(R.id.ccLayout);
        TextView ccText = (TextView) ccLayout.findViewById(R.id.title);
        ccText.setText(mCustomTypes[4]);

        LinearLayout drivingKMLayout = (LinearLayout) findViewById(R.id.drivingKMLayout);
        TextView drivingKMText = (TextView) drivingKMLayout.findViewById(R.id.title);
        drivingKMText.setText(mCustomTypes[5]);

        LinearLayout powerLayout = (LinearLayout) findViewById(R.id.powerLayout);
        TextView powerText = (TextView) powerLayout.findViewById(R.id.title);
        powerText.setText(mCustomTypes[6]);

    }

    private int type = 1;

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.oilBtn:
                mOriBtn.setBackgroundColor(getResources().getColor(R.color.btn_select_color));

                mElectricBtn.setBackgroundResource(R.drawable.button_bg);
                mHybridBtn.setBackgroundResource(R.drawable.button_bg);
                mOriBtn.setTextColor(mRes.getColor(R.color.white));
                mElectricBtn.setTextColor(mRes.getColor(R.color.text_color));
                mHybridBtn.setTextColor(mRes.getColor(R.color.text_color));
                type = 1;
                break;
            case R.id.electricBtn:
                mElectricBtn.setBackgroundColor(getResources().getColor(R.color.btn_select_color));
                mOriBtn.setBackgroundResource(R.drawable.button_bg);
                mHybridBtn.setBackgroundResource(R.drawable.button_bg);
                type = 2;
                mOriBtn.setTextColor(mRes.getColor(R.color.text_color));
                mElectricBtn.setTextColor(mRes.getColor(R.color.white));
                mHybridBtn.setTextColor(mRes.getColor(R.color.text_color));
                break;
            case R.id.hybridBtn:
                mHybridBtn.setBackgroundColor(getResources().getColor(R.color.btn_select_color));
                mOriBtn.setBackgroundResource(R.drawable.button_bg);
                mElectricBtn.setBackgroundResource(R.drawable.button_bg);
                type = 3;
                mOriBtn.setTextColor(mRes.getColor(R.color.text_color));
                mElectricBtn.setTextColor(mRes.getColor(R.color.text_color));
                mHybridBtn.setTextColor(mRes.getColor(R.color.white));
                break;
            case R.id.addBrandBtn:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.INTENT_TYPE,
                    Constants.IntentParams.MUlTI_BRAND_TYPE);
                CommonUtils.startResultNewActivity(this, args, BrandListsActivity.class,
                    REQUEST_CODE);
                break;
            case R.id.confirmBtn:

                Map<String, String> params = new HashMap<String, String>();

                params.put("brand_ids", getIds());
                params.put("country_ids", "");
                params.put("power", getItemValue(mPowerLists));
                params.put("token", IOUtils.getToken(mContext));
                params.put("battery", getItemValue(mDrivingLists));
                params.put("seat_num", "");
                params.put("vehicle_type", "");
                params.put("cc_max", getItemValue(mCCLists));
                params.put("power_forms", "" + type);
                params.put("car_level_ids", "");
                params.put("price_min", mPriceMin + "");
                params.put("price_max", mPriceMax + "");
                mPresenter.setCustomizationData(params);
                break;
        }

    }

    private String getIds() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Brand brand : mDatas) {
            stringBuilder.append(brand.brandId).append(",");
        }
        String ids = stringBuilder.toString();
        if (ids.length() > 0) {
            ids = ids.substring(0, ids.length() - 1);
        }

        return ids;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (REQUEST_CODE == requestCode) {
            if (this.RESULT_OK == resultCode) {

                ArrayList<Brand> datas = data.getParcelableArrayListExtra("data");
                ArrayList<Brand> selectDatas = new ArrayList<Brand>();
                for (Brand brand : datas) {
                    if (brand.isSelected) {
                        selectDatas.add(brand);
                    }
                }
                ArrayList<Brand> originalDatas = new ArrayList<Brand>();

                if (mDatas.size() == 0) {
                    mDatas.addAll(selectDatas);
                } else {
                    for (Brand brand : mDatas) {
                        originalDatas.add(brand);
                    }
                    for (Brand brand : originalDatas) {
                        for (Brand brand2 : selectDatas) {

                            if (brand.brandId == brand2.brandId) {
                                mDatas.remove(brand);
                            }
                        }
                    }
                    mDatas.addAll(selectDatas);
                }

                addBrandItems(mDatas);
            }
        }

    }

    private List<Brand> mDatas = new ArrayList<Brand>();

    @Override
    public void setSuccess() {

        CommonUtils.showToast(mContext, "添加成功！");
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
