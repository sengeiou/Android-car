package com.tgf.kcwc.potentialcustomertrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.globalchat.GlobalChatActivity;
import com.tgf.kcwc.me.message.PrivateMessagesActivity;
import com.tgf.kcwc.mvp.model.AddCustomerModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.OrderProcessModel;
import com.tgf.kcwc.mvp.presenter.RobOrderDecryptPresenter;
import com.tgf.kcwc.mvp.presenter.RobOrderPresenter;
import com.tgf.kcwc.mvp.view.AddCustomerView;
import com.tgf.kcwc.mvp.view.RobOrderDecryptView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/7/31 11:00
 * 抢单解密
 */

public class RobOrderDecryptActivity extends BaseActivity implements RobOrderDecryptView,AddCustomerView{
    private SimpleDraweeView avatarSdv;
    private TextView nicknameTv;
    private TextView saleRobTv;
    private TextView robTv;
    private ListView mListView;
    private WrapAdapter mAdapter;
    private ArrayList<OrderProcessModel.OrderProcessItem> mList = new ArrayList<>();
    //潜客解密
    private RobOrderDecryptPresenter mDecryptPresenter;
    //潜客抢单
    private RobOrderPresenter mRobPresenter;
    //popwindow集合数据
    List<MorePopupwindowBean> popList = new ArrayList<>();
    private int page = 1;
    private boolean isRefresh;
    private String userId;
    private String nickname;

    @Override
    protected void setUpViews() {
        avatarSdv = (SimpleDraweeView) findViewById(R.id.avatar_sdv);
        nicknameTv = (TextView) findViewById(R.id.nickname_tv);
        saleRobTv = (TextView) findViewById(R.id.sale_rob_tv);
        mListView = (ListView) findViewById(R.id.decrypt_lv);
        robTv = (TextView) findViewById(R.id.decrypt_rob);
        initRefreshLayout(mBGDelegate);
        initPop();
        mDecryptPresenter = new RobOrderDecryptPresenter();
        mDecryptPresenter.attachView(this);
        mRobPresenter = new RobOrderPresenter();
        mRobPresenter.attachView(this);
        mDecryptPresenter.getOrderProcessList(IOUtils.getToken(getContext()), userId, page);
        if (null != mAdapter) {
            mListView.setAdapter(mAdapter);
        }
        robTv.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
//                Log.e("TAG", "onSingleClick: "+IOUtils.getUserId(mContext));
                //抢单
                mRobPresenter.rob(IOUtils.getToken(getContext()),userId);
            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
        text.setText("抢单解密");
        function.setImageResource(R.drawable.icon_common_right);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MorePopupWindow morePopupWindow = new MorePopupWindow(RobOrderDecryptActivity.this,
                        popList, new MorePopupWindow.MoreOnClickListener() {
                    @Override
                    public void moreOnClickListener(int position, MorePopupwindowBean item) {
                        switch (position) {
                            case 0://首页
                                finish();
                                break;
                            case 1://消息
                                finish();
                                break;
                            case 2://扫一扫
                                finish();
                                break;
                        }
                    }
                });
                morePopupWindow.showPopupWindow(function);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("user_id");
        }
        super.setContentView(R.layout.activity_rob_oder_decrypt);
    }

    @Override
    public void showOrderProcess(ArrayList<OrderProcessModel.OrderProcessItem> list) {
        if (isRefresh) {
            mList.clear();
        }
        mList.addAll(list);
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        } else {
            initAdapter();
            mListView.setAdapter(mAdapter);
        }
    }

    /**
     * 初始化popwindow选项
     */
    public void initPop() {
        String[] popValues = mRes.getStringArray(R.array.global_nav_values2);
        for (String value : popValues) {
            MorePopupwindowBean morePopupwindowBean = null;
            morePopupwindowBean = new MorePopupwindowBean();
            morePopupwindowBean.title = value;
            popList.add(morePopupwindowBean);
        }
    }

    @Override
    public void showUserData(OrderProcessModel.UserItem userItems) {
        String avatarStr = userItems.avatar;
        nickname = userItems.nickname;
        int saleCount = userItems.saleCount;
        if (avatarStr != null) {
            String avatarUrl = URLUtil.builderImgUrl(avatarStr, 144, 144);
            avatarSdv.setImageURI(avatarUrl);
        }
        nicknameTv.setText(nickname);
        saleRobTv.setText(Html.fromHtml("<font color=\"#D46A30\">" + saleCount + "</font>个销售待抢"));
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingIndicator(active);
        } else {
            stopRefreshAll();
            showLoadingIndicator(active);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    public void initAdapter() {
        mAdapter = new WrapAdapter<OrderProcessModel.OrderProcessItem>(this, mList, R.layout.order_process_item) {
            @Override
            public void convert(ViewHolder helper, OrderProcessModel.OrderProcessItem item) {
//                if (helper.getPosition() == 0) {
//                    helper.setImageResource(R.id.item_circle_iv, R.drawable.shape_order_circle_s);
//                } else {
//                    helper.setImageResource(R.id.item_circle_iv, R.drawable.shape_order_circle_n);
//                }
                if (StringUtils.checkNull(item.time)) {
                    helper.setText(R.id.item_time_tv, DateFormatUtil.timeLogic(item.time));
                }
                if (StringUtils.checkNull(item.action) && StringUtils.checkNull(item.object)) {
                    helper.setText(R.id.item_dynamic_tv, Html.fromHtml(item.action + "<font color=\"#333333\">" + item.object + "</font>  "));
                }
            }
        };
    }


    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            page = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isRefresh = false;
            page++;
            loadMore();
            return false;
        }
    };

    public void loadMore() {
        mDecryptPresenter.getOrderProcessList(IOUtils.getToken(getContext()), userId, page);
    }

    @Override
    public void addSuccess(ArrayList<AddCustomerModel.FriendItem> item) {
        //跳转私信
        if (StringUtils.checkNull(nickname) && StringUtils.checkNull(userId)) {
            setLoadingIndicator(false);
            CommonUtils.showToast(mContext,"抢单成功");
            Intent intent = new Intent(mContext, GlobalChatActivity.class);
            intent.putExtra(Constant.EXTRA_USER_ID, userId);
            //intent.putExtra(Constant.EXTRA_USER_ID, "im_development_1415_kcwc");
            intent.putExtra(Constant.EXTRA_USER_NICKNAME, nickname);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void addFail(String msg) {
        //返回潜客跟踪
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext,msg);
//        finish();
    }
}
