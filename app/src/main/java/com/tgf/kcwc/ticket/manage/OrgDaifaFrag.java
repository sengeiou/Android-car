package com.tgf.kcwc.ticket.manage;

import android.graphics.Color;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.OrgDaifaModel;
import com.tgf.kcwc.mvp.model.OrgFenfaModel;
import com.tgf.kcwc.mvp.presenter.OrgDaifaPresenter;
import com.tgf.kcwc.mvp.presenter.OrgFenfaPresenter;
import com.tgf.kcwc.mvp.view.OrgDaifaView;
import com.tgf.kcwc.mvp.view.OrgFenfaView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/16 0016
 * E-mail:hekescott@qq.com
 */

public class OrgDaifaFrag extends BaseFragment implements OrgDaifaView {
    private OrgDaifaPresenter orgFenfaPresenter;
    private TicketmOrgTongjActivity ticketmOrgTongjActivity;
    private NestFullListView orgfenfaTongji;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_orgdaifa;
    }

    @Override
    protected void initView() {
        ticketmOrgTongjActivity = (TicketmOrgTongjActivity) getActivity();
        orgfenfaTongji = findView(R.id.orgdaifa_tongjiFenfalv);
        orgFenfaPresenter = new OrgDaifaPresenter();
        orgFenfaPresenter.attachView(this);
        orgFenfaPresenter.getTicketmOrgDaifaStatitics(IOUtils.getToken(getContext()), ticketmOrgTongjActivity.eventId, null);
    }


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showFenfaStatitics(ArrayList<OrgDaifaModel.UserInfo> list) {
        orgfenfaTongji.setAdapter(new NestFullListViewAdapter<OrgDaifaModel.UserInfo>(R.layout.listitem_daifa_tongji, list) {
            @Override
            public void onBind(int pos, OrgDaifaModel.UserInfo userInfo, NestFullViewHolder holder) {
                holder.setSimpleDraweeViewURL(R.id.user_avatariv, URLUtil.builderImgUrl(userInfo.avatar, 144, 144));
                holder.setText(R.id.user_nameTv, userInfo.realName);
                NestFullListView tongjiChartLV = holder.getView(R.id.orgfenfa_tongjiChartlv);
                tongjiChartLV.setAdapter(new NestFullListViewAdapter<OrgDaifaModel.FenfaStatistics>(R.layout.listitem_gailan_tongjfive, userInfo.fenfaStatisticslist) {
                    @Override
                    public void onBind(int pos, OrgDaifaModel.FenfaStatistics fenfaStatistics, NestFullViewHolder holder) {
                        TextView tv0 = holder.getView(R.id.chart5_column0);
                        tv0.setBackgroundColor(Color.parseColor(fenfaStatistics.color));
                        holder.setText(R.id.chart5_column1, fenfaStatistics.getNums());
                        holder.setText(R.id.chart5_column2, fenfaStatistics.getHaveNums());
                        holder.setText(R.id.chart5_column3, fenfaStatistics.htUserNumsAll + "");
                        holder.setText(R.id.chart5_column4, fenfaStatistics.receiveNumsAll + "");
                        holder.setText(R.id.chart5_column5, fenfaStatistics.useNums + "");
                    }
                });
            }
        });
    }
}
