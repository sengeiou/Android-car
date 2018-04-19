package com.tgf.kcwc.see.exhibition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ExhibitPlace;
import com.tgf.kcwc.mvp.presenter.ExhibitPlacelistPresenter;
import com.tgf.kcwc.mvp.view.ExhibitPlacelistView;
import com.tgf.kcwc.see.BigPhotoPageActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by susu on 17/1/2.
 */

public class ExhibitPalaceListActivity extends BaseActivity implements ExhibitPlacelistView {

    private GridView                  palceListGv;
    private List<ExhibitPlace>        placelist=new ArrayList<>();
    private NestFullListView          placeImagesLv;
    private WrapAdapter<ExhibitPlace> placelistAdapter;
    private ExhibitPlacelistPresenter exhibitPlacelistPresenter;
    private List<ExhibitPlace> mExhibitPlaceList;
    private ImageView          headIv;
    private int exhibitId;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("展位图总览");
    }

    @Override
    protected void setUpViews() {
        headIv = (ImageView) findViewById(R.id.exhibitplacelist_headiv);
        palceListGv = (GridView) findViewById(R.id.palcelist_menu_gv);
        placeImagesLv = (NestFullListView) findViewById(R.id.palcelist_image_lv);
//        initdata();
        Intent fromintent = getIntent();
        exhibitId = fromintent.getIntExtra(Constants.IntentParams.ID,6);
        exhibitPlacelistPresenter = new ExhibitPlacelistPresenter();
        exhibitPlacelistPresenter.attachView(this);
        exhibitPlacelistPresenter.getExhibitPlacelist(exhibitId, IOUtils.getToken(mContext));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_palcelist);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showHeadView(String totalimage) {
      final String url =  URLUtil.builderImgUrl750(totalimage);
        Glide.with(ExhibitPalaceListActivity.this)
                .load(url)
                .into(headIv);
        headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BigPhotoPageActivity.class);
                intent.putExtra(Constants.KEY_IMG,
                        URLUtil.builderImgUrl(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public void showGridView(List<ExhibitPlace> exhibitPlaceList) {
        this.mExhibitPlaceList = exhibitPlaceList;
        placelistAdapter = new WrapAdapter<ExhibitPlace>(mContext, R.layout.grid_item,
            exhibitPlaceList) {
            @Override
            public void convert(ViewHolder helper, ExhibitPlace item) {
                Button btn = helper.getView(R.id.name);
                btn.setText(item.name);
            }
        };

        palceListGv.setAdapter(placelistAdapter);
        palceListGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap arg = new HashMap();
                arg.put(Constants.IntentParams.ID,exhibitId);
                arg.put(Constants.IntentParams.ID2,mExhibitPlaceList.get(position).id);
//                CommonUtils.startNewActivity(mContext,arg, com.tgf.kcwc.see.exhibition.plus.ExhibitPlaceDetailActivity.class);
                CommonUtils.startNewActivity(mContext,arg, com.tgf.kcwc.see.exhibition.ExhibitPlaceDetailActivity.class);
            }
        });

        showListView(exhibitPlaceList);
    }

    @Override
    public void showListView(List<ExhibitPlace> exhibitPlaceList) {
        placeImagesLv.setAdapter(new NestFullListViewAdapter<ExhibitPlace>(
            R.layout.listview_item_exhibitpalce, exhibitPlaceList) {

            @Override
            public void onBind(final int pos, ExhibitPlace exhibitPlace, NestFullViewHolder holder) {
                TextView tv = holder.getView(R.id.listitem_exhibitpalce_tile);
                tv.setText(exhibitPlace.name);
                ImageView simpleDraweeView = holder.getView(R.id.palce_img);
               String path = URLUtil.builderImgUrl750(exhibitPlace.imageurl);
                Glide.with(ExhibitPalaceListActivity.this)
                        .load(path)
                        .into(simpleDraweeView);
                simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BigPhotoPageActivity.class);
                        intent.putExtra(Constants.KEY_IMG,
                                URLUtil.builderImgUrl(mExhibitPlaceList.get(pos).imageurl));
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.exhibit_more_rv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap arg = new HashMap();
                        arg.put(Constants.IntentParams.ID,exhibitId);
                        arg.put(Constants.IntentParams.ID2,mExhibitPlaceList.get(pos).id);
                        CommonUtils.startNewActivity(mContext,arg, ExhibitPlaceDetailActivity.class);
//                        CommonUtils.startNewActivity(mContext, arg, com.tgf.kcwc.see.exhibition.plus.ExhibitPlaceDetailActivity.class);
                    }
                });
            }
        });

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
