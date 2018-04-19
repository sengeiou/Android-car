package com.tgf.kcwc.me.mybalance;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.BalanceStatementModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.MyWalletPresenter;
import com.tgf.kcwc.mvp.view.MyWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Author:Jenny
 * Date:2017/10/19
 * E-mail:fishloveqin@gmail.com
 * 零钱明细
 */

public class BalanceStatementActivity extends BaseActivity implements MyWalletView<BalanceStatementModel> {

    protected ImageButton titleBarBack;
    protected TextView titleBarText;
    protected TextView selecttext;
    protected LinearLayout selectlayout;
    protected ListView list;
    MyWalletPresenter mWalletPresenter;
    private WrapAdapter<BalanceStatementModel.StatementItem> mAdapter = null;
    private List<BalanceStatementModel.StatementItem> mItems = new ArrayList<BalanceStatementModel.StatementItem>();
    List<MorePopupwindowBean> dataList = new ArrayList<>();

    @Override
    protected void setUpViews() {
        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar = false;
        super.setContentView(R.layout.activity_balance_statement);

    }

    private void initView() {
        initRefreshLayout(mBGDelegate);
        titleBarBack = (ImageButton) findViewById(R.id.title_bar_back);
        backEvent(titleBarBack);
        titleBarText = (TextView) findViewById(R.id.title_bar_text);
        titleBarText.setText("收支明细");
        selecttext = (TextView) findViewById(R.id.selecttext);
        selectlayout = (LinearLayout) findViewById(R.id.selectlayout);
        list = (ListView) findViewById(R.id.list);
        initListData();
        initMorePopupWindow();
        selectlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                morePopupWindow.showPopupWindow(v);


            }
        });

        mWalletPresenter = new MyWalletPresenter();
        mWalletPresenter.attachView(this);
        mPage = 1;
        mWalletPresenter.getBalanceStatements(builderReqParams());


    }

    private MorePopupWindow morePopupWindow = null;

    private void initMorePopupWindow() {
        String arrays[] = mRes.getStringArray(R.array.pay_types);
        int index = 0;
        for (String str : arrays) {

            MorePopupwindowBean bean = new MorePopupwindowBean();
            bean.title = str;
            bean.id = index;
            dataList.add(bean);
            index++;
        }
        morePopupWindow = new MorePopupWindow(this, dataList, mListener);

    }

    private MorePopupWindow.MoreOnClickListener mListener = new MorePopupWindow.MoreOnClickListener() {
        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean bean) {

            mType = bean.id + "";
            selecttext.setText(bean.title);
            mItems.clear();
            mPage = 1;
            mWalletPresenter.getBalanceStatements(builderReqParams());
        }
    };

    private Map<String, String> builderReqParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("type", mType);
        params.put("page", mPage + "");
        params.put("pageSize", pageSize + "");
        return params;
    }

    private String mType = "";

    private void initListData() {

        mAdapter = new WrapAdapter<BalanceStatementModel.StatementItem>(mContext, R.layout.balance_statement_item, mItems) {


            protected TextView moneyTv;
            protected TextView timeTv;
            protected TextView typeTv;


            @Override
            public void convert(ViewHolder helper, BalanceStatementModel.StatementItem item) {


                typeTv = helper.getView(R.id.typeTv);
                timeTv = helper.getView(R.id.timeTv);
                moneyTv = helper.getView(R.id.moneyTv);
                typeTv.setText(item.actType);
                timeTv.setText(item.createTime);

                String money = item.money;
                int flag = item.flag;
                int color = mRes.getColor(R.color.text_color18);
                if (flag == Constants.BalanceTypes.IN) {
                    money = "+" + money;
                    color = mRes.getColor(R.color.text_color10);
                } else if (flag == Constants.BalanceTypes.OUT) {
                    money = "-" + money;
                    color = mRes.getColor(R.color.text_color18);
                }
                moneyTv.setText(money);
                moneyTv.setTextColor(color);
            }
        };
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(mItemListener);
    }

    private AdapterView.OnItemClickListener mItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            BalanceStatementModel.StatementItem item = (BalanceStatementModel.StatementItem) parent.getAdapter().getItem(position);

            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, item.id + "");
            CommonUtils.startNewActivity(mContext, args, BalanceDetailActivity.class);
        }
    };

    @Override
    public void showData(BalanceStatementModel model) {

        mItems.addAll(model.list);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
        if (!active) {
            stopRefreshAll();
        }
    }

    @Override
    public void showLoadingTasksError() {
        dismissLoadingDialog();
        stopRefreshAll();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mWalletPresenter != null) {
            mWalletPresenter.detachView();
        }
    }


    private BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            loadMore();
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {


        mPage++;
        mWalletPresenter.getBalanceStatements(builderReqParams());
    }

    private int mPage;
    private int pageSize = 10;
}
