package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CarGalleryModel;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.presenter.CarGalleryPresenter;
import com.tgf.kcwc.mvp.view.CarGalleryView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GridViewWithHeaderAndFooter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/27 20:09
 * E-mail：fishloveqin@gmail.com
 */

public class NewCarGalleryActivity extends BaseActivity implements CarGalleryView {

    private GridViewWithHeaderAndFooter mGrid;
    private WrapAdapter<Image> mAdapter;

    private CarGalleryPresenter mPresenter;
    private SimpleDraweeView mLogo;
    private TextView mName, mArea;
    private TextView mModelName;
    private ImageView mMoreImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        setContentView(R.layout.activity_new_car_gallery);

    }

    private int carId;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("新车图库");
    }

    @Override
    protected void setUpViews() {

        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadCarGallery(carId + "", "1", "10");
    }

    @Override
    public void onClick(View view) {

        if (mModel != null) {
            int rel = mModel.relation;
            if (rel == 2) {
                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.ID, mModel.seriesId);
                intent.putExtra(Constants.IntentParams.NAME, mModel.seriesName);
                //intent.putParcelableArrayListExtra(Constants.IntentParams.DATA, item.matchingCars);
                //intent.putExtra(Constants.IntentParams.DATA2, item.matchNums);
                intent.setClass(mContext, SeriesDetailActivity.class);
                startActivity(intent);
            }
            if (rel == 3) {

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mModel.id);
                CommonUtils.startNewActivity(mContext, args, CarDetailActivity.class);
            }
        }
    }

    private void initView() {
        mGrid = (GridViewWithHeaderAndFooter) findViewById(R.id.grid);
        mLogo = (SimpleDraweeView) findViewById(R.id.img);
        mName = (TextView) findViewById(R.id.name);
        mArea = (TextView) findViewById(R.id.area);
        mModelName = (TextView) findViewById(R.id.modelName);
        mMoreImg = (ImageView) findViewById(R.id.moreImage);
        findViewById(R.id.itemHeader).setOnClickListener(this);
        findViewById(R.id.releaseLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.FROM_ID, carId);
                args.put(Constants.IntentParams.FROM_TYPE, Constants.IntentParams.NEW_CAR_VALUE);
                CommonUtils.startNewActivity(mContext, args, PostingActivity.class);

            }
        });
        mPresenter = new CarGalleryPresenter();
        mPresenter.attachView(this);


    }

    private CarGalleryModel mModel;
    private boolean isFirst = true;

    @Override
    public void showGallery(CarGalleryModel model) {
        mModel = model;
        mLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.logo, 144, 144)));
        mName.setText(model.brandName);
        mArea.setText(model.hallName + "/" + model.boothName);
        mModelName.setText(model.seriesName + "" + model.productName);

        int rel = model.relation;
        if (rel == 2 || rel == 3) {
            mMoreImg.setVisibility(View.VISIBLE);
        } else {
            mMoreImg.setVisibility(View.GONE);
        }
        final List<Image> images = new ArrayList<Image>();
        images.addAll(model.model.images);

        mAdapter = new WrapAdapter<Image>(mContext, images, R.layout.new_car_grid_item_2) {
            @Override
            public void convert(ViewHolder helper, Image item) {

                ImageView imageView = helper.getView(R.id.img);
                imageView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.link, 360, 360)));
            }
        };
        if (isFirst) {
            isFirst = false;

            View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_hint_layout, null,
                    false);
            mGrid.addFooterView(view);
        }
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("list", (ArrayList) images);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

    }
}
