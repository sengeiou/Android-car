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
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GridViewWithHeaderAndFooter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/4/7 09:32
 * E-mail：fishloveqin@gmail.com
 */

public class GalleryDetailActivity extends BaseActivity implements CarDataView<ArrayList<Image>> {
    protected GridViewWithHeaderAndFooter mGrid;
    private String                        mTitle   = "";
    private int                           mId      = -1;
    private String                        mPicType = "";

    private CarDataPresenter              mPresenter;
    private String mCarType ="car_series";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mTitle = intent.getStringExtra(Constants.IntentParams.TITLE);
        mId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mPicType = intent.getStringExtra(Constants.IntentParams.INTENT_TYPE);
        String fromeCarType   = intent.getStringExtra(Constants.IntentParams.INDEX);
        if(!TextUtil.isEmpty(fromeCarType)){
            mCarType =fromeCarType;
        }
        super.setContentView(R.layout.activity_gallery_detail);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        text.setText(mTitle);
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new CarDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getGalleryDetails(mId + "", mPicType + "", mCarType);
    }

    @Override
    public void showData(ArrayList<Image> images) {

        bindGalleryAdapter(mGrid, images);
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

    private void initView() {
        mGrid = (GridViewWithHeaderAndFooter) findViewById(R.id.grid);
    }
}
