package com.tgf.kcwc.ticket.manage;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.TicketSendOrgRecordModel;
import com.tgf.kcwc.mvp.model.TicketSendRecordModel;
import com.tgf.kcwc.mvp.presenter.SendOrgRecordPresenter;
import com.tgf.kcwc.mvp.presenter.SendRecordPresenter;
import com.tgf.kcwc.mvp.view.SendOrgRecordView;
import com.tgf.kcwc.mvp.view.SendRecordView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class SendOrgRecordFragment extends BaseFragment implements  SendOrgRecordView {

    private static final String TAG = "SendOrgRecordFragment";
    private ListView recordListView;
    private TicketOrgQueryActivity activity;
    private SendOrgRecordPresenter sendRecordPresenter;
    private View emptyLayout;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ticketsendorgrecord;
    }

    @Override
    protected void initView() {
        recordListView = findView(R.id.sendrecord_lv);
        emptyLayout = findView(R.id.empty_layout);
        sendRecordPresenter = new SendOrgRecordPresenter();
        sendRecordPresenter.attachView(this);
        activity = (TicketOrgQueryActivity) getActivity();
//        sendRecordPresenter.getSendTicketRecord(IOUtils.getToken(getContext()), activity.mTicketId);
        sendRecordPresenter.getSendOrgTicketRecord(IOUtils.getToken(getContext()), activity.mTicketId);

    }

//    @Override
//    public void showSendRecorList(ArrayList<TicketSendRecordModel.RecordItem> senRecordList) {
//
//
//
//    }


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

    @Override
    public void showSendOrgRecorList(ArrayList<TicketSendOrgRecordModel.OrgRecordItem> list) {
        if (list == null || list.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            recordListView.setVisibility(View.GONE);
        } else {
            recordListView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
        recordListView.setAdapter(new WrapAdapter<TicketSendOrgRecordModel.OrgRecordItem>(getContext(), list, R.layout.listitem_ticket_sendorgrecord) {
            @Override
            public void convert(ViewHolder helper, TicketSendOrgRecordModel.OrgRecordItem item) {
                helper.setText(R.id.sendrecore_date, item.time);
                NestFullListView sendUserLv = helper.getView(R.id.sendrecord_userlv);
//                TextView limitTv = helper.getView(R.id.sendrecord_userlv);
//                if ("0".equals(item.timeLimit)) {
//                    limitTv.setText("不现时效");
//                } else {
//                    limitTv.setText(item.timeLimit + "小时失效");
//                }
                sendUserLv.setAdapter(new NestFullListViewAdapter<TicketSendOrgRecordModel.OrgRecordUser>(R.layout.listitem_sendorgrecord_user, item.items) {
                    @Override
                    public void onBind(int pos, TicketSendOrgRecordModel.OrgRecordUser item, NestFullViewHolder helper) {
                        StringBuilder sb = new StringBuilder(item.real_name).append(" (").append(item.tel).append(")");
                        helper.setText(R.id.senduser_name, sb.toString());
                        helper.setSimpleDraweeViewURL(R.id.orgrecord_useravatariv, URLUtil.builderImgUrl(item.avatar,144,144));
                        helper.setText(R.id.senduser_num, "x " + item.nums);
                    }
                });
            }
        });
    }
}
