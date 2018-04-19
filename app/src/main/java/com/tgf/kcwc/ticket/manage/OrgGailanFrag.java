package com.tgf.kcwc.ticket.manage;

import android.graphics.Color;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.OrgGailanModel;
import com.tgf.kcwc.mvp.model.TicketmExhibitModel;
import com.tgf.kcwc.mvp.presenter.TicketmGailanPresenter;
import com.tgf.kcwc.mvp.view.OrgGailanView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/16 0016
 * E-mail:hekescott@qq.com
 */

public class OrgGailanFrag extends BaseFragment implements OrgGailanView {

    private TicketmGailanPresenter ticketmGailanPresenter;
    private TicketmOrgTongjActivity tickerManagerActivity;
    private NestFullListView tongjiAllLv;
    private NestFullListView tongjiFenFaLv;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_orgailan;
    }

    @Override
    protected void initView() {
        tickerManagerActivity = (TicketmOrgTongjActivity) getActivity();
        ticketmGailanPresenter = new TicketmGailanPresenter();
        ticketmGailanPresenter.attachView(this);
        ticketmGailanPresenter.getTicketmGailan(IOUtils.getToken(getContext()), tickerManagerActivity.eventId);
        tongjiAllLv = findView(R.id.orggailan_tongjialllv);
        tongjiFenFaLv = findView(R.id.orggailan_tongjiFenfalv);
    }

    @Override
    public void showOrgGailanTongji(ArrayList<OrgGailanModel.HandTongji> handTongjilist) {
        tongjiAllLv.setAdapter(new NestFullListViewAdapter<OrgGailanModel.HandTongji>(R.layout.listitem_gailan_tongjfour, handTongjilist) {
            @Override
            public void onBind(int pos, OrgGailanModel.HandTongji handTongji, NestFullViewHolder holder) {
                TextView tv0 = holder.getView(R.id.chart4_column0);
                tv0.setBackgroundColor(Color.parseColor(handTongji.color));
                holder.setText(R.id.chart4_column1, handTongji.getNums());
                holder.setText(R.id.chart4_column2, handTongji.htNumsSaler + "");
                holder.setText(R.id.chart4_column3, handTongji.htNumsOrg + "");
                holder.setText(R.id.chart4_column4, handTongji.getHaveNums() + "");
            }
        });
        tongjiFenFaLv.setAdapter(new NestFullListViewAdapter<OrgGailanModel.HandTongji>(R.layout.listitem_gailan_tongjt, handTongjilist) {
            @Override
            public void onBind(int pos, OrgGailanModel.HandTongji handTongji, NestFullViewHolder holder) {
                TextView tv0 = holder.getView(R.id.chart3_column0);
                tv0.setBackgroundColor(Color.parseColor(handTongji.color));
                holder.setText(R.id.chart3_column1, handTongji.htUserNumsAll + "\n(" + handTongji.htUserNumsAllPerson + ")");
                holder.setText(R.id.chart3_column2, handTongji.receiveNumsAll + "\n(" + handTongji.receiveNumsAllPerson + ")");
                holder.setText(R.id.chart3_column3, handTongji.useNums + "\n(" + handTongji.useNumsPerson + ")");
            }
        });

    }

    @Override
    public void showHead(TicketmExhibitModel ticketmExhibitModel) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }
}
