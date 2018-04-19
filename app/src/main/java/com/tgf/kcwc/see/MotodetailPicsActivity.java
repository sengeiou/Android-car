package com.tgf.kcwc.see;

import java.util.ArrayList;
import java.util.List;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.CarModelPicsModel;
import com.tgf.kcwc.mvp.presenter.MotodetailPicsPresenter;
import com.tgf.kcwc.mvp.view.MotoDetailPicsView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2016/12/19 0019
 * E-mail:hekescott@qq.com
 */

public class MotodetailPicsActivity extends BaseActivity implements MotoDetailPicsView {

    private GridView          wholemotGv;
    private GridView          pointsemotoGv;
    private GridView          livingmotoGv;
    private ArrayList<String> tmpList = new ArrayList<>();
    private MotodetailPicsPresenter motodetailPicsPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motodetail_pics);



    }

    @Override
    protected void setUpViews() {
        wholemotGv = (GridView) findViewById(R.id.wholemoto_gv);
        pointsemotoGv = (GridView) findViewById(R.id.pointsemoto_gv);
        livingmotoGv = (GridView) findViewById(R.id.livingmoto_gv);
        findViewById(R.id.livingmoto_more_rv).setOnClickListener(this);
        findViewById(R.id.pointsemoto_gv).setOnClickListener(this);
        findViewById(R.id.wholemoto_more_rv).setOnClickListener(this);
        for (int i = 0; i < 6; i++) {
            tmpList.add("测试数据");
        }
        motodetailPicsPresenter = new MotodetailPicsPresenter();
        motodetailPicsPresenter.attachView(this);
        motodetailPicsPresenter.getMotoDetailPics(1);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        CommonUtils.startNewActivity(this,MotoAlbumActivity.class);
    }

    @Override
    public void showWholeCar(List<CarModelPicsModel.CarModelPic> wholecarImglist) {
        WrapAdapter<CarModelPicsModel.CarModelPic> wholemotoAdapter = new WrapAdapter<CarModelPicsModel.CarModelPic>(this, wholecarImglist,
                R.layout.gridview_item_motopics) {
            @Override
            public void convert(ViewHolder helper, CarModelPicsModel.CarModelPic item) {
                helper.setSimpleDraweeViewURL(R.id.griditem_album_iv, DataUtil.AVATAR[1]);
            }
        };
        wholemotGv.setAdapter(wholemotoAdapter);
        ViewUtil.setListViewHeightBasedOnChildren(wholemotGv,3);
    }

    @Override
    public void showInterior(List<CarModelPicsModel.CarModelPic> interiorImglist) {
        WrapAdapter<String> pointsemotoAdapter = new WrapAdapter<String>(this, tmpList,
                R.layout.gridview_item_motopics) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setSimpleDraweeViewURL(R.id.griditem_album_iv, DataUtil.AVATAR[4]);
            }
        };

        pointsemotoGv.setAdapter(pointsemotoAdapter);
        ViewUtil.setListViewHeightBasedOnChildren(pointsemotoGv,3);
    }

    @Override
    public void showLiving(List<CarModelPicsModel.CarModelPic> livingImglist) {

        WrapAdapter<CarModelPicsModel.CarModelPic> livingmotoAdapter = new WrapAdapter<CarModelPicsModel.CarModelPic>(this, livingImglist,
                R.layout.gridview_item_motopics) {

            @Override
            public void convert(ViewHolder helper, CarModelPicsModel.CarModelPic item) {
                helper.setSimpleDraweeViewURL(R.id.griditem_album_iv, DataUtil.AVATAR[2]);
            }
        };

        livingmotoGv.setAdapter(livingmotoAdapter);
        ViewUtil.setListViewHeightBasedOnChildren(livingmotoGv,3);
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
        motodetailPicsPresenter.detachView();
    }
}
