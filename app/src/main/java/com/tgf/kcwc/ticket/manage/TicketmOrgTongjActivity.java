package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.OrgGailanModel;
import com.tgf.kcwc.mvp.model.TicketmExhibitModel;
import com.tgf.kcwc.mvp.presenter.TicketmGailanPresenter;
import com.tgf.kcwc.mvp.view.OrgGailanView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/16 0016
 * E-mail:hekescott@qq.com
 */

public class TicketmOrgTongjActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, OrgGailanView {

    private RadioGroup mTicketmOrgtongjifilterRG;
    private RadioButton mTicketmOrgtongjifilterGailanBtn;
    private RadioButton mTicketmOrgtongjifilterDaifaBtn;
    private RadioButton mTicketmOrgtongjifilterFenfaBtn;
    private OrgGailanFrag orgailanfrag;
    private OrgFenfaFrag orgFenfafrag;
    private OrgDaifaFrag orgDaifafrag;
    private FragmentManager fm;
    public String eventId;
    private TicketmGailanPresenter ticketmHeadPresenter;
    private SimpleDraweeView exhibitCoverIv;
    private TextView exhibitTitleTv;

    @Override
    protected void setUpViews() {
        mTicketmOrgtongjifilterRG = (RadioGroup) findViewById(R.id.ticketm_orgtongjifilterRG);
        mTicketmOrgtongjifilterGailanBtn = (RadioButton) findViewById(R.id.ticketm_orgtongjifilter_gailanBtn);
        mTicketmOrgtongjifilterDaifaBtn = (RadioButton) findViewById(R.id.ticketm_orgtongjifilter_daifaBtn);
        mTicketmOrgtongjifilterFenfaBtn = (RadioButton) findViewById(R.id.ticketm_orgtongjifilter_fenfaBtn);
        mTicketmOrgtongjifilterRG.setOnCheckedChangeListener(this);
        mTicketmOrgtongjifilterGailanBtn.performClick();
        eventId = getIntent().getStringExtra(Constants.IntentParams.ID);
        fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        if(orgailanfrag==null){
            orgailanfrag = new OrgGailanFrag();
        }
        trans.replace(R.id.tongji_tufl,orgailanfrag);
        trans.commit();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("统计概览");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketm_orgtongji);
        exhibitCoverIv = (SimpleDraweeView) findViewById(R.id.ticketm_exhibitcoveriv);
        exhibitTitleTv = (TextView) findViewById(R.id.ticketm_exhibitTitleTv);
        ticketmHeadPresenter = new TicketmGailanPresenter();
        ticketmHeadPresenter.attachView(this);
        ticketmHeadPresenter.getTicketmGailan(IOUtils.getToken(TicketmOrgTongjActivity.this), eventId);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentTransaction trans = fm.beginTransaction();
             switch (checkedId){
                         case R.id.ticketm_orgtongjifilter_gailanBtn :
                             if(orgailanfrag==null){
                                 orgailanfrag = new OrgGailanFrag();
                             }
                             trans.replace(R.id.tongji_tufl,orgailanfrag);
                             mTicketmOrgtongjifilterGailanBtn.setChecked(true);
                             break;
                         case R.id.ticketm_orgtongjifilter_fenfaBtn :
                             if(orgFenfafrag==null){
                                 orgFenfafrag = new OrgFenfaFrag();
                             }
                             trans.replace(R.id.tongji_tufl,orgFenfafrag);
                             mTicketmOrgtongjifilterFenfaBtn.setChecked(true);
                             break;
                         case R.id.ticketm_orgtongjifilter_daifaBtn:
                             if(orgDaifafrag==null){
                                 orgDaifafrag = new OrgDaifaFrag();
                             }
                             trans.replace(R.id.tongji_tufl,orgDaifafrag);
                             mTicketmOrgtongjifilterDaifaBtn.setChecked(true);
                             break;
                         default:
                             break;
                     }
        trans.commit();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showOrgGailanTongji(ArrayList<OrgGailanModel.HandTongji> handTongjilist) {

    }

    @Override
    public void showHead(TicketmExhibitModel ticketmExhibitModel) {
        exhibitCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(ticketmExhibitModel.cover,540,304)));
        exhibitTitleTv.setText(ticketmExhibitModel.name);
    }

    @Override
    public Context getContext() {
        return TicketmOrgTongjActivity.this;
    }

    @Override
    protected void onDestroy() {
        ticketmHeadPresenter.detachView();
        super.onDestroy();

    }
}
