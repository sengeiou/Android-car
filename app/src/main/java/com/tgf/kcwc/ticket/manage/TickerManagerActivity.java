package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.TicketManageListModel;
import com.tgf.kcwc.mvp.presenter.TicketManageListPresenter;
import com.tgf.kcwc.mvp.view.TicketManageListView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 * 票务管理列表
 */

public class TickerManagerActivity extends BaseActivity implements TicketManageListView {

    private static final String                      TAG = "TickerManagerActivity";
    private TicketManageListPresenter ticketManageListPresenter;
    private ListView                                 mlistView;
    private List<TicketManageListModel.TicketManage> manageList;
    private  final String  KEY_INTENT_TICKETID = "ticketid";

    @Override
    protected void setUpViews() {
        ticketManageListPresenter = new TicketManageListPresenter();
        ticketManageListPresenter.attachView(this);
        ticketManageListPresenter.getManageTicketList(IOUtils.getToken(getContext()));
        mlistView = (ListView) findViewById(R.id.ticketmanage_list_lv);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("票务管理");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_managelist);

    }

    @Override
    public void showTicketList(TicketManageListModel data) {
        manageList = data.mTicketManageList;

        mlistView.setAdapter(new WrapAdapter<TicketManageListModel.TicketManage>(this, manageList, R.layout.listitem_ticket_manage) {
            @Override
            public void convert(ViewHolder helper, TicketManageListModel.TicketManage item) {
                helper.setText(R.id.ticketmanage_nums_tv, "总量   " + item.nums + "  剩余   " + item.getTicketLeft());
                helper.setText(R.id.ticketmange_ticketinfo,item.eventInfo.name+"   "+item.ticketInfo.name);
                View isExpireView = helper.getView(R.id.ticketmanagelist_iv);
                helper.setImageByUrl(R.id.ticketmanage_event_iv, URLUtil.builderImgUrl(item.eventInfo.cover,270, 203));
                if (item.ticketInfo.status == 2) {
                    isExpireView.setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.ticketmanagelist_iv).setVisibility(View.INVISIBLE);
                }
            }
        });
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),TicketManageDetailActivity.class);
                intent.putExtra(KEY_INTENT_TICKETID,manageList.get(position).id);
                startActivity(intent);
            }
        });
    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ticketManageListPresenter.detachView();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }
}
