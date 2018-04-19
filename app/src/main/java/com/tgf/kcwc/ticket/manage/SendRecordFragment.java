package com.tgf.kcwc.ticket.manage;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.TicketSendRecordModel;
import com.tgf.kcwc.mvp.presenter.SendRecordPresenter;
import com.tgf.kcwc.mvp.view.SendRecordView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.InnerListView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class SendRecordFragment extends BaseFragment implements SendRecordView {

    private static final String TAG = "SendRecordFragment";
    private ListView recordListView;
    private TicketQueryActivity activity;
    private SendRecordPresenter sendRecordPresenter;
    private View emptyLayout;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ticketsendrecord;
    }

    @Override
    protected void initView() {
        recordListView = findView(R.id.sendrecord_lv);
        emptyLayout = findView(R.id.empty_layout);
        sendRecordPresenter = new SendRecordPresenter();
        sendRecordPresenter.attachView(this);
        activity = (TicketQueryActivity) getActivity();
        sendRecordPresenter.getSendTicketRecord(IOUtils.getToken(getContext()), activity.mTicketId);

    }

    @Override
    public void showSendRecorList(ArrayList<TicketSendRecordModel.RecordItem> senRecordList) {
        if (senRecordList == null || senRecordList.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            recordListView.setVisibility(View.GONE);
        } else {
            recordListView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
        recordListView.setAdapter(new WrapAdapter<TicketSendRecordModel.RecordItem>(getContext(), senRecordList, R.layout.listitem_ticket_sendrecord) {
            @Override
            public void convert(ViewHolder helper, TicketSendRecordModel.RecordItem item) {
                helper.setText(R.id.sendrecore_date, item.sendTime);
                NestFullListView sendUserLv = helper.getView(R.id.sendrecord_userlv);
                TextView limitTv = helper.getView(R.id.senduser_limit);
                if ("0".equals(item.timeLimit)) {
                    limitTv.setText("不现时效");
                } else {
                    limitTv.setText(item.timeLimit + "小时失效");
                }
                sendUserLv.setAdapter(new NestFullListViewAdapter<TicketSendRecordModel.Person>(R.layout.listitem_sendrecord_user, item.userList) {

                    @Override
                    public void onBind(int pos, TicketSendRecordModel.Person item, NestFullViewHolder helper) {
                        StringBuilder sb = new StringBuilder(item.userName).append(" (").append(item.mobile).append(")");
                        helper.setText(R.id.senduser_name, sb.toString());
                        helper.setText(R.id.senduser_num, "x " + item.nums);
                    }
                });
            }
        });


    }


    @Override
    public void onDestroyView() {
        sendRecordPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
