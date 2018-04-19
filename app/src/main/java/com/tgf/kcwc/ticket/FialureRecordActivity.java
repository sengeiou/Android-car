package com.tgf.kcwc.ticket;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.FreeTicketListModel;
import com.tgf.kcwc.mvp.model.Ticket;
import com.tgf.kcwc.mvp.presenter.FreeTicketListPresenter;
import com.tgf.kcwc.mvp.view.FreeTicketListView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SpecRectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/29 19:53
 * E-mail：fishloveqin@gmail.com
 * <p>
 * 失效记录
 */

public class FialureRecordActivity extends BaseActivity implements FreeTicketListView {
    private FreeTicketListPresenter mPresenter;
    private int mSenseId;
    private View footView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSenseId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        setContentView(R.layout.activity_ticket_failure_record);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("领取失效记录");
    }

    @Override
    protected void setUpViews() {
        footView = LayoutInflater.from(mContext).inflate(R.layout.no_more_data_layout, mList, false);
        mList = (ListView) findViewById(R.id.list);
        initEmptyView();

        //        initListData();
        //
        //        mList.setAdapter(
        //            new WrapAdapter<Ticket>(mContext, tickets, R.layout.failure_ticket_list_item) {
        //                @Override
        //                public void convert(InnerViewHolder helper, final Ticket item) {
        //
        //                    helper.setText(R.id.title, item.name);
        //                    helper.setText(R.id.type, "观众日");
        //                    helper.setText(R.id.expire, item.beginTime + "-" + item.endTime);
        //                    helper.setText(R.id.price, item.price + "");
        //                    ((TextView) helper.getView(R.id.price)).getPaint()
        //                        .setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //                    helper.setText(R.id.ticketStatus, "已领取");
        //                    TextView countText = helper.getView(R.id.countText);
        //                    countText.setVisibility(View.VISIBLE);
        //                    countText.setText("03:11:12");
        //                }
        //            });
        mPresenter = new FreeTicketListPresenter();
        mPresenter.attachView(this);
        mPresenter.loadFreeTickets("", "", "2", IOUtils.getToken(mContext));
    }

    private List<Ticket> tickets = new ArrayList<Ticket>();
    private ListView mList;

    @Override
    public void showFreeTickets(FreeTicketListModel model) {

        int size = model.list.size();

        if (size == 0) {

            mList.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            return;
        } else {
            mList.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }

        mList.setAdapter(new WrapAdapter<FreeTicketListModel.FreeTicketItem>(mContext, model.list,
                R.layout.failure_ticket_list_item) {
            @Override
            public void convert(ViewHolder helper, final FreeTicketListModel.FreeTicketItem item) {

                helper.setText(R.id.title, item.sense.name + "");
                helper.setText(R.id.type, item.ticket.name + "");
                helper.setText(R.id.expire, item.ticket.remarks);
                helper.setText(R.id.price, item.ticket.price + "");
                helper.setText(R.id.num, item.nums + "");
                SpecRectView specRectView = helper.getView(R.id.specRectView);
                specRectView.setBGColor(mRes.getColor(R.color.text_content_color));
                //helper.getView(R.id.specRectView1).setBackgroundColor(mRes.getColor(R.color.white));
                ((TextView) helper.getView(R.id.price)).getPaint()
                        .setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                int status = item.status;
                String statusText = "";
                switch (status) {
                    case 2:
                        statusText = "领取失效";
                        break;
                    case 3:
                        statusText = "票券过期";
                        break;
                    default:
                        statusText = "已领取";
                        break;
                }
                helper.setText(R.id.ticketStatus, statusText);
                helper.setText(R.id.desc, item.workerInfo.nickname);

            }
        });
        mList.addFooterView(footView);
        footView.setBackgroundColor(mRes.getColor(R.color.app_layout_bg_color));

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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
