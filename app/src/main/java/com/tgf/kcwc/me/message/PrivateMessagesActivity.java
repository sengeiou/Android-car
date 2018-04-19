package com.tgf.kcwc.me.message;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.driving.driv.ListviewHint;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.MessagePrivateListBean;
import com.tgf.kcwc.mvp.model.MessageSystemListBean;
import com.tgf.kcwc.mvp.presenter.MessageSystemPresenter;
import com.tgf.kcwc.mvp.view.MessageSystemListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/5/18.
 */

public class PrivateMessagesActivity extends BaseActivity implements MessageSystemListView {
    MessageSystemPresenter mMessageSystemPresenter;
    ListView mListView;
    int page = 1;
    boolean isRefresh = true;


    private WrapAdapter<MessagePrivateListBean.DataList> mAdapter;
    List<MessagePrivateListBean.DataList> Datalist = new ArrayList<>(); //数据源
    private ListviewHint mListviewHint;                         //尾部
    String Name = "";
    String letteruser = "";

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(Name);
        // function.setImageResource(R.drawable.btn_more);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Name = getIntent().getStringExtra("name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemmessages);
        initRefreshLayout(mBGDelegate);
        letteruser = getIntent().getStringExtra("type");
        mMessageSystemPresenter = new MessageSystemPresenter();
        mMessageSystemPresenter.attachView(this);
        mListView = (ListView) findViewById(R.id.list);
        mMessageSystemPresenter.getLetterlistList(IOUtils.getToken(mContext), letteruser, page);
        mAdapter = new WrapAdapter<MessagePrivateListBean.DataList>(mContext, R.layout.message_private_generalitem, Datalist) {
            @Override
            public void convert(ViewHolder helper, MessagePrivateListBean.DataList item) {
                TextView mExternalTime = helper.getView(R.id.externaltime); //外部时间
                LinearLayout oppositelayout = helper.getView(R.id.oppositelayout); //对方LinearLayout
                SimpleDraweeView mExternalHead = helper.getView(R.id.externalhead); //对方头像
                TextView mAnnouncementTitle = helper.getView(R.id.announcementtitle); //对方title
                RelativeLayout mMelayout = helper.getView(R.id.melayout); //我的LinearLayout
                SimpleDraweeView mMehead = helper.getView(R.id.mehead); //我头像
                TextView mMeTitle = helper.getView(R.id.metitle); //我的title

                mExternalTime.setText(item.createtime);

                if (!(item.userId + "").equals(IOUtils.getUserId(mContext))) {
                    oppositelayout.setVisibility(View.VISIBLE);
                    mMelayout.setVisibility(View.GONE);
                    mExternalHead.setImageURI(URLUtil.builderImgUrl(item.avatar, 144, 144));
                    mAnnouncementTitle.setText(item.content);
                } else {
                    oppositelayout.setVisibility(View.GONE);
                    mMelayout.setVisibility(View.VISIBLE);
                    mMehead.setImageURI(URLUtil.builderImgUrl(item.avatar, 144, 144));
                    mMeTitle.setText(item.content);
                }

            }

        };
        mListView.setAdapter(mAdapter);
        mListviewHint = new ListviewHint(mContext);
    }


    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                // 如果网络可用，则加载网络数据
            //
            //                loadMore();
            //            } else {
            //                // 网络不可用，结束下拉刷新
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //            }
            // mPageIndex = 1;
            page = 1;
            isRefresh = true;
            mListView.removeFooterView(mListviewHint);
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                loadMore();
            //                return true;
            //            } else {
            //                // 网络不可用，返回false，不显示正在加载更多
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //                return false;
            //            }
            //loadMore();
            if (isRefresh) {
                page++;
                loadMore();
            }
            return false;
        }
    };

    public void loadMore() {
        mMessageSystemPresenter.getLetterlistList(IOUtils.getToken(mContext), letteruser, page);
    }

    @Override
    public void StatisticsListSucceed(MessageSystemListBean messageListBean) {

    }

    @Override
    public void PrivateListSucceed(MessagePrivateListBean messageListBean) {
        stopRefreshAll();
        if (page == 1) {
            Datalist.clear();
        }
        if (messageListBean.data.lists == null || messageListBean.data.lists.size() == 0) {
            isRefresh = false;
            if (page != 1) {
                mListView.addFooterView(mListviewHint);
            }
        }
        Datalist.addAll(messageListBean.data.lists);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void DeleteSucceed(BaseArryBean baseBean) {

    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
