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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.model.Motorshow;
import com.tgf.kcwc.mvp.presenter.MotorshowPresenter;
import com.tgf.kcwc.mvp.view.MotorshowView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GridViewWithHeaderAndFooter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/28 20:09
 * E-mail：fishloveqin@gmail.com
 * <p>
 * 品牌展车图库
 */

public class BrandModelsGalleryActivity extends BaseActivity implements MotorshowView<Motorshow> {

    private GridViewWithHeaderAndFooter mGrid;
    private WrapAdapter<Image> mAdapter;
    private SimpleDraweeView mLogo;
    private TextView mName, mArea;
    private TextView mModelName;

    private int mId;

    private int mSeriesId;
    private int mProductId;
    private int isSeries;

    private RelativeLayout mHeaderLayout;

    private String factoryName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mSeriesId = intent.getIntExtra(Constants.IntentParams.ID2, -1);
        mProductId = intent.getIntExtra(Constants.IntentParams.ID3, -1);
        isSeries = intent.getIntExtra(Constants.IntentParams.DATA, -1);
        factoryName = intent.getStringExtra(Constants.IntentParams.DATA2);
        setContentView(R.layout.activity_brand_models_gallery);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("展车图库");
        text.setTextColor(mRes.getColor(R.color.white));
        //        function.setImageResource(R.drawable.global_nav_n);
        //        function.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //            }
        //        });
    }

    private MotorshowPresenter mPresenter;

    @Override
    protected void setUpViews() {

        mGrid = (GridViewWithHeaderAndFooter) findViewById(R.id.grid);
        mLogo = (SimpleDraweeView) findViewById(R.id.img);
        mName = (TextView) findViewById(R.id.name);
        mArea = (TextView) findViewById(R.id.area);
        mHeaderLayout = (RelativeLayout) findViewById(R.id.itemHeader);
        mHeaderLayout.setOnClickListener(this);
        mModelName = (TextView) findViewById(R.id.modelName);
        mPresenter = new MotorshowPresenter();
        mPresenter.attachView(this);
        mPresenter.loadGallery("" + mId, "", "", IOUtils.getToken(mContext));

        findViewById(R.id.releaseLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.FROM_ID, mId);
                args.put(Constants.IntentParams.FROM_TYPE, Constants.IntentParams.SHOW_CAR_VALUE);
                CommonUtils.startNewActivity(mContext, args, PostingActivity.class);

            }
        });

    }

    private boolean isFirst;

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.itemHeader:
                Map<String, Serializable> args = new HashMap<String, Serializable>();

                if (isSeries == 1) {
                    args.put(Constants.IntentParams.ID, mSeriesId);
                    CommonUtils.startNewActivity(mContext, args, SeriesDetailActivity.class);
                } else {
                    args.put(Constants.IntentParams.ID, mProductId);
                    CommonUtils.startNewActivity(mContext, args, CarDetailActivity.class);
                }
                break;
        }
    }

    @Override
    public void showData(final Motorshow data) {
        mLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(data.brandLogo, 360, 360)));
        mName.setText(factoryName + "");
        mArea.setText(data.hallName + "/" + data.boothName);
        mModelName.setText(data.productName + "");
        mAdapter = new WrapAdapter<Image>(mContext, data.images, R.layout.gallery_grid_item) {
            @Override
            public void convert(ViewHolder helper, Image item) {
                ImageView imageView = helper.getView(R.id.img);
                imageView.setImageURI(Uri.parse(URLUtil.builderImgUrl750(item.link)));
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
                intent.putParcelableArrayListExtra("list", (ArrayList) data.images);
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

        mPresenter.detachView();
    }
}
