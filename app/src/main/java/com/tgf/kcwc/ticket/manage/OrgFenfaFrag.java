package com.tgf.kcwc.ticket.manage;

import android.graphics.Color;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.OrgFenfaModel;
import com.tgf.kcwc.mvp.presenter.OrgFenfaPresenter;
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

public class OrgFenfaFrag extends BaseFragment implements OrgFenfaView {

    private OrgFenfaPresenter orgFenfaPresenter;
    private TicketmOrgTongjActivity ticketmOrgTongjActivity;
    private NestFullListView orgfenfaTongji;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_orgfenfa;
    }

    @Override
    protected void initView() {
        ticketmOrgTongjActivity = (TicketmOrgTongjActivity) getActivity();
        orgfenfaTongji = findView(R.id.orgfenfa_tongjiFenfalv);
        orgFenfaPresenter = new OrgFenfaPresenter();
        orgFenfaPresenter.attachView(this);
        orgFenfaPresenter.getTicketmOrgFenfaStatitics(IOUtils.getToken(getContext()),ticketmOrgTongjActivity.eventId,null);

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showFenfaStatitics(ArrayList<OrgFenfaModel.UserInfo> list) {
        orgfenfaTongji.setAdapter(new NestFullListViewAdapter<OrgFenfaModel.UserInfo>(R.layout.listitem_fenfa_tongji,list) {
            @Override
            public void onBind(int pos, OrgFenfaModel.UserInfo userInfo, NestFullViewHolder holder) {
                    holder.setSimpleDraweeViewURL(R.id.user_avatariv, URLUtil.builderImgUrl(userInfo.avatar,144,144));
                if(TextUtil.isEmpty(userInfo.tel)){
                    holder.setText(R.id.user_nameTv,userInfo.realName);
                }else {
                    holder.setText(R.id.user_nameTv,userInfo.realName+"("+userInfo.tel+")");
                }
             NestFullListView  tongjiChartLV=    holder.getView(R.id.orgfenfa_tongjiChartlv);
                tongjiChartLV.setAdapter(new NestFullListViewAdapter<OrgFenfaModel.FenfaStatistics>(R.layout.listitem_gailan_tongjfive,userInfo.fenfaStatisticslist) {
                    @Override
                    public void onBind(int pos, OrgFenfaModel.FenfaStatistics fenfaStatistics, NestFullViewHolder holder) {
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
