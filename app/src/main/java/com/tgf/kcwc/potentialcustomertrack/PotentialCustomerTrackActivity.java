package com.tgf.kcwc.potentialcustomertrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.businessconcerns.CustomerDetailActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.CustomerTrackModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.PotentialCustomerTrackPresenter;
import com.tgf.kcwc.mvp.view.PotentialCustomerTrackView;
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
 * 潜客跟踪
 */

public class PotentialCustomerTrackActivity extends BaseActivity implements PotentialCustomerTrackView {
    private TextView recordTv;
    private ListView listView;
    private PotentialCustomerTrackPresenter mPresenter;
    private WrapAdapter mAdapter;
    private AdapterView.OnItemClickListener mItemListener;
    private ArrayList<CustomerTrackModel.CustomerTrackItem> mList = new ArrayList<>();
    //popwindow集合数据
    List<MorePopupwindowBean> popList = new ArrayList<>();
    private int page = 1;
    private boolean isRefresh;

    @Override
    protected void setUpViews() {
        recordTv = (TextView) findViewById(R.id.record_tv);
        listView = (ListView) findViewById(R.id.list_view);
        initRefreshLayout(mBGDelegate);
        initPop();
        mPresenter = new PotentialCustomerTrackPresenter();
        mPresenter.attachView(this);
        mPresenter.getTrackList(IOUtils.getToken(this), page);
        if (null != mAdapter) {
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(mItemListener);
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
        text.setText("潜客跟踪");
        function.setImageResource(R.drawable.icon_common_right);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MorePopupWindow morePopupWindow = new MorePopupWindow(PotentialCustomerTrackActivity.this,
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
        setContentView(R.layout.activity_potential_customer_track);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingIndicator(true);
        } else {
            stopRefreshAll();
            showLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showTrackList(ArrayList<CustomerTrackModel.CustomerTrackItem> list) {
        if (isRefresh) {
            mList.clear();
        }
        mList.addAll(list);
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        } else {
            initAdapter();
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(mItemListener);
        }
        recordTv.setText(Html.fromHtml("已为您推送<font color=\"#36A95C\">" + mList.size() + "</font>条客户记录，<font color=\"#E7624A\">" + mList.size() + "</font>条待解密"));
    }

    public void initAdapter() {
        mItemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转抢单解密
                // TODO: 2017/8/1 判断是否被抢
                if (mList.get(i).isRob == 0) {
                    Intent intent = new Intent(PotentialCustomerTrackActivity.this, RobOrderDecryptActivity.class);
                    intent.putExtra("user_id", mList.get(i).userItem.id + "");
                    startActivity(intent);
                } else {
                    // TODO: 2017/8/2 弹出提示
//                    Dialog dialog = new DecryptDialog(PotentialCustomerTrackActivity.this);
//                    dialog.show();
                    int friendId = mList.get(i).friendId;//好友id
                    Intent intent = new Intent(mContext,CustomerDetailActivity.class);
                    intent.putExtra(Constants.IntentParams.DATA,friendId);
                    startActivity(intent);
                }
            }
        };
        mAdapter = new WrapAdapter<CustomerTrackModel.CustomerTrackItem>(this, mList, R.layout.custom_track_item) {

            @Override
            public void convert(ViewHolder helper, final CustomerTrackModel.CustomerTrackItem item) {
                SimpleDraweeView avatarSdv = helper.getView(R.id.track_header_img);
                String avatarUrl = URLUtil.builderImgUrl(item.userItem.avatar, 144, 144);
                avatarSdv.setImageURI(avatarUrl);
                avatarSdv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转个人主页
                        int userId = item.userItem.id;
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, userId);
                        CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);
                    }
                });
                TextView nickname = helper.getView(R.id.track_nickname);
                nickname.setText(item.userItem.nickname);
                nickname.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    protected void onSingleClick(View view) {
                        // TODO: 2017/8/7
//                        CommonUtils.showToast(PotentialCustomerTrackActivity.this,"nickname");
                    }
                });
                if (StringUtils.checkNull(item.actionItem.action)) {
                    helper.setText(R.id.track_type, item.actionItem.action);
                }
                if (StringUtils.checkNull(item.actionItem.object)) {
                    helper.setText(R.id.track_action, item.actionItem.object);
                }
                String str = DateFormatUtil.timeLogic(item.createTime);
                helper.setText(R.id.track_time, str);
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
        mPresenter.getTrackList(IOUtils.getToken(this), page);
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
}
