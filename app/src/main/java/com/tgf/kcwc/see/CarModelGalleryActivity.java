package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CarColor;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.model.ModelGalleryModel;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GridViewWithHeaderAndFooter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/4/1 14:20
 * E-mail：fishloveqin@gmail.com
 */

public class CarModelGalleryActivity extends BaseActivity implements CarDataView<ModelGalleryModel> {
    protected TextView                    mAppearanceTitle;
    protected TextView                    mAppearanceNums;
    protected RelativeLayout              mAppearanceHeaderLayout;
    protected GridViewWithHeaderAndFooter mAppearanceGrid;
    protected RelativeLayout              mAppearanceLayout;
    protected TextView                    mInteriorTitle;
    protected TextView                    mInteriorNums;
    protected RelativeLayout              mInteriorHeaderLayout;
    protected GridViewWithHeaderAndFooter mInteriorGrid;
    protected RelativeLayout              mInteriorLayout;
    protected TextView                    mLiveTitle;
    protected TextView                    mLiveNums;
    protected RelativeLayout              mLiveHeaderLayout;
    protected GridViewWithHeaderAndFooter mLiveGrid;
    protected RelativeLayout              mLiveLayout;
    private String                        mName;
    private int mTypeCarId;
    private CarDataPresenter              mPresenter;
    private LinearLayout                  mAppearanceFilterLayout;
    private LinearLayout                  mInteriorFilterLayout;
    private String                        mOutId = "";
    private String                        mInId  = "";
    private String                        mCarId = "";
    private String mGalleryType = "car";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_car_series_details_pics);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText(mName + "");
        text.setTextColor(mRes.getColor(R.color.white));
//        function.setTextResource("车型", R.color.white, 16);
//        function.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Map<String, Serializable> args = new HashMap<String, Serializable>();
//                args.put(Constants.IntentParams.INTENT_TYPE, 2);
//                args.put(Constants.IntentParams.ID, mTypeCarId);
//                CommonUtils.startResultNewActivity(CarModelGalleryActivity.this, args,
//                    ModelListActivity.class, Constants.IntentParams.REQUEST_CODE);
//            }
//        });
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
//        Intent fromIntent = getIntent();
//        mTypeCarId = fromIntent.getIntExtra(Constants.IntentParams.ID,0);
        Intent intent = getIntent();
        mTypeCarId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mName = intent.getStringExtra(Constants.IntentParams.NAME);
        initView();
        setFilterTitle(mAppearanceFilterLayout, "外观");
        setFilterTitle(mInteriorFilterLayout, "内饰");
        mPresenter = new CarDataPresenter();
        mPresenter.attachView(this);
       // mTypeCarId = 1;
        mPresenter.getCarGallery(mTypeCarId + "", mGalleryType, mOutId, mInId, mCarId);

    }



    private void initView() {
        mAppearanceTitle = (TextView) findViewById(R.id.appearanceTitle);
        mAppearanceNums = (TextView) findViewById(R.id.appearanceNums);
        mAppearanceFilterLayout = (LinearLayout) findViewById(R.id.appearanceFilterLayout);
        mInteriorFilterLayout = (LinearLayout) findViewById(R.id.interiorFilterLayout);
        mAppearanceFilterLayout.setOnClickListener(this);
        mInteriorFilterLayout.setOnClickListener(this);
        mAppearanceHeaderLayout = (RelativeLayout) findViewById(R.id.appearanceHeaderLayout);
        mAppearanceHeaderLayout.setOnClickListener(this);
        mAppearanceGrid = (GridViewWithHeaderAndFooter) findViewById(R.id.appearanceGrid);
        mAppearanceLayout = (RelativeLayout) findViewById(R.id.appearanceLayout);
        mInteriorTitle = (TextView) findViewById(R.id.interiorTitle);
        mInteriorNums = (TextView) findViewById(R.id.interiorNums);
        mInteriorHeaderLayout = (RelativeLayout) findViewById(R.id.interiorHeaderLayout);
        mInteriorGrid = (GridViewWithHeaderAndFooter) findViewById(R.id.interiorGrid);
        mInteriorLayout = (RelativeLayout) findViewById(R.id.interiorLayout);
        mLiveTitle = (TextView) findViewById(R.id.liveTitle);
        mLiveNums = (TextView) findViewById(R.id.liveNums);
        mLiveHeaderLayout = (RelativeLayout) findViewById(R.id.liveHeaderLayout);
        mLiveGrid = (GridViewWithHeaderAndFooter) findViewById(R.id.liveGrid);
        mLiveLayout = (RelativeLayout) findViewById(R.id.liveLayout);
        mInteriorHeaderLayout.setOnClickListener(this);
        mLiveHeaderLayout.setOnClickListener(this);
    }

    @Override
    public void showData(ModelGalleryModel modelGalleryModel) {

        mAppearanceNums.setText(modelGalleryModel.nums.out + "");
        mInteriorNums.setText(modelGalleryModel.nums.in + "");
        mLiveNums.setText(modelGalleryModel.nums.event + "");
        bindGalleryAdapter(mAppearanceGrid, modelGalleryModel.out);
        bindGalleryAdapter(mInteriorGrid, modelGalleryModel.in);
        bindGalleryAdapter(mLiveGrid, modelGalleryModel.event);
    }

    private void bindGalleryAdapter(GridView grid, final ArrayList<Image> images) {

        WrapAdapter<Image> adapter = new WrapAdapter<Image>(mContext, images,
            R.layout.new_car_grid_item_2) {
            @Override
            public void convert(ViewHolder helper, Image item) {

                ImageView imageView = helper.getView(R.id.img);
                imageView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.imgUrl, 360, 360)));
            }
        };
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("list", images);
                intent.putExtra(Constants.IntentParams.DATA, position);
                intent.setClass(mContext, PhotoBrowserActivity.class);
                startActivity(intent);
            }
        });
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

    private int mType = -1;

    @Override
    public void onClick(View view) {

        int id = view.getId();
        Object tag = view.getTag();
        boolean flag = false;
        if (tag != null) {
            flag = Boolean.parseBoolean(tag.toString());
        }
        switch (id) {

            case R.id.appearanceFilterLayout:

                if (flag) {
                    view.setTag(false);
                    setSelectStatus(view, false);
                    setSelectStatus(mInteriorFilterLayout, false);
                } else {
                    setSelectStatus(view, true);
                    setSelectStatus(mInteriorFilterLayout, false);
                    view.setTag(true);
                }
                mInteriorFilterLayout.setTag(false);
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                mType = 1;
                args.put(Constants.IntentParams.INTENT_TYPE, mType);
                args.put(Constants.IntentParams.ID, mTypeCarId);
                args.put(Constants.IntentParams.DATA,false);
                args.put(Constants.IntentParams.DATA2,mOutId);
                CommonUtils.startResultNewActivity(this, args, ColorCategoryActivity.class,
                    Constants.IntentParams.REQUEST_CODE_2);
                break;
            case R.id.interiorFilterLayout:

                if (flag) {
                    view.setTag(false);
                    setSelectStatus(view, false);
                    setSelectStatus(mAppearanceFilterLayout, false);
                } else {
                    setSelectStatus(view, true);
                    setSelectStatus(mAppearanceFilterLayout, false);
                    view.setTag(true);
                }
                mAppearanceFilterLayout.setTag(false);
                args = new HashMap<String, Serializable>();
                mType = 2;
                args.put(Constants.IntentParams.INTENT_TYPE, mType);
                args.put(Constants.IntentParams.ID, mTypeCarId);
                args.put(Constants.IntentParams.DATA,false);
                args.put(Constants.IntentParams.DATA2,mInId);
                CommonUtils.startResultNewActivity(this, args, ColorCategoryActivity.class,
                    Constants.IntentParams.REQUEST_CODE_2);
                break;
            case R.id.appearanceHeaderLayout:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mTypeCarId);
                args.put(Constants.IntentParams.TITLE, "外观");
                args.put(Constants.IntentParams.INTENT_TYPE, "out");
                args.put(Constants.IntentParams.INDEX, "car");
                CommonUtils.startNewActivity(mContext, args, GalleryDetailActivity.class);
                break;
            case R.id.interiorHeaderLayout:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mTypeCarId);
                args.put(Constants.IntentParams.TITLE, "内饰");
                args.put(Constants.IntentParams.INTENT_TYPE, "in");
                args.put(Constants.IntentParams.INDEX, "car");
                CommonUtils.startNewActivity(mContext, args, GalleryDetailActivity.class);
                break;
            case R.id.liveHeaderLayout:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mTypeCarId);
                args.put(Constants.IntentParams.TITLE, "现场");
                args.put(Constants.IntentParams.INTENT_TYPE, "event");
                args.put(Constants.IntentParams.INDEX, "car");
                CommonUtils.startNewActivity(mContext, args, GalleryDetailActivity.class);
                break;

        }

    }

    private void setSelectStatus(View v, boolean isSelected) {

        v.findViewById(R.id.filterImg).setSelected(isSelected);
        v.findViewById(R.id.filterTitle).setSelected(isSelected);
    }

    private void setFilterTitle(View parent, String title) {

        TextView tv = (TextView) parent.findViewById(R.id.filterTitle);
        tv.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Constants.IntentParams.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            int modelId = data.getIntExtra(Constants.IntentParams.ID, -1);

            mCarId = modelId + "";
            mPresenter.getCarGallery(mTypeCarId + "", mGalleryType, mOutId, mInId, mCarId);
        }

        if (Constants.IntentParams.REQUEST_CODE_2 == requestCode && RESULT_OK == resultCode) {

            CarColor c = data.getParcelableExtra(Constants.IntentParams.DATA);

            if (mType == 1) {
                mOutId = c.id + "";
            } else if (mType == 2) {
                mInId = c.id + "";
            }

            mPresenter.getCarGallery(mTypeCarId + "", mGalleryType, mOutId, mInId, mCarId);
        }
    }

}
