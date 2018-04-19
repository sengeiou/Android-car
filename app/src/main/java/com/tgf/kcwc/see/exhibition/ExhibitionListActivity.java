package com.tgf.kcwc.see.exhibition;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Exhibition;
import com.tgf.kcwc.mvp.presenter.ExhibitionListPresenter;
import com.tgf.kcwc.mvp.view.ExhibitionListView;
import com.tgf.kcwc.ticket.PurchaseTicketActivity;
import com.tgf.kcwc.ticket.TicketActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2016/12/22 0022
 * E-mail:hekescott@qq.com
 */

public class ExhibitionListActivity extends BaseActivity implements ExhibitionListView {
    private ListView                exhibitionlistLv;
    private List<Exhibition>        mExhibitionlist = new ArrayList<>();
    private WrapAdapter<Exhibition> mExhibitionListAdapter;
    private ExhibitionListPresenter mExhibitionListPresenter;
    private Intent fromIntent;
    private KPlayCarApp kPlayCarApp;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainAct();
            }
        });
        text.setText("展会列表");
//        function.setImageResource(R.drawable.filter_catalog);
//        function.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CommonUtils.showToast(mContext, "跳转城市筛选");
//            }
//        });

    }

    private void gotoMainAct() {
//        SharedPreferenceUtil.setIsBack(getContext(),true);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gotoMainAct();
    }

    @Override
    protected void setUpViews() {
        kPlayCarApp = (KPlayCarApp) getApplication();
        fromIntent= getIntent();
        exhibitionlistLv = (ListView) findViewById(R.id.exhibitionlist_lv);
        mExhibitionListPresenter = new ExhibitionListPresenter();
        mExhibitionListPresenter.attachView(this);
        mExhibitionListPresenter.getExhibitionList(kPlayCarApp.adcode,"1","999", IOUtils.getToken(this),kPlayCarApp.adcode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitionlist);

        exhibitionlistLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferenceUtil.putExhibitId(mContext,mExhibitionlist.get(position).id);
                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 1);
//                Intent intentExhibit = new Intent();
//                intentExhibit.putExtra(Constants.IntentParams.INDEX, 1);
//                intentExhibit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intentExhibit.setClass(mContext, MainActivity.class);
//                startActivity(intentExhibit);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
//                SharedPreferenceUtil.putExhibitId(mContext,mExhibitionlist.get(position).id);
//                Intent toMainact = new Intent(getContext(), MainActivity.class);
//                toMainact.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                toMainact.putExtra(Constants.IntentParams.INDEX,2);
//                kPlayCarApp.g
//                startActivity();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public void showExhibitionList(List<Exhibition> exhibitionList) {
            if(exhibitionList!=null&&exhibitionList.size()!=0){
                mExhibitionlist =   exhibitionList ;
                if(mExhibitionListAdapter == null){

                    initAdapter();
                }else{
                    mExhibitionListAdapter.notifyDataSetChanged();
                }
            }
    }

    private void initAdapter() {
            mExhibitionListAdapter = new WrapAdapter<Exhibition>(mContext, mExhibitionlist,
                    R.layout.listview_item_exhibitionlist) {
                @Override
                public void convert(ViewHolder helper, final Exhibition item) {
                    SimpleDraweeView cover = helper.getView(R.id.exhibitionlist_cover_iv);

                    cover.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.coverImg,540,270)));

                    Button buyBtn = helper.getView(R.id.exhibitionlist_buy_btn);
                    Button overBtn = helper.getView(R.id.exhibitionlist_over_btn);
                    helper.setText(R.id.exhibitionlist_title_tv,item.name);
                    helper.setText(R.id.exhibitionlist_date_tv,item.time);
                    helper.setText(R.id.exhibitionlist_address_tv,item.address);
                    Button getBtn = helper.getView(R.id.exhibitionlist_getticket_btn);
                    if(item.isTicketl == 1){
                        getBtn.setVisibility(View.VISIBLE);
                    }else {
                        getBtn.setVisibility(View.GONE);
                    }
                    if (item.isTicketg == 1) {
                        buyBtn.setVisibility(View.VISIBLE);
                    } else {
                        buyBtn.setVisibility(View.GONE);
                    }
                    if(item.isClose == 1){
                        overBtn.setVisibility(View.VISIBLE);
                    }else {
                        overBtn.setVisibility(View.GONE);
                    }
                    getBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (IOUtils.isLogin(mContext)) {
                                Intent toTicketIntent = new Intent(mContext,TicketActivity.class);
                                toTicketIntent.putExtra(Constants.IntentParams.INDEX,0);
                                startActivity(toTicketIntent);
                            }
                        }
                    });
                    buyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (IOUtils.isLogin(mContext)) {
                                HashMap args = new HashMap<String, Serializable>();
                                args.put(Constants.IntentParams.ID, item.id);
                                CommonUtils.startNewActivity(mContext, args, PurchaseTicketActivity.class);
                            }
                        }
                    });
                }
            };
            exhibitionlistLv.setAdapter(mExhibitionListAdapter);
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
