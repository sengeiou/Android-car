package com.tgf.kcwc.membercenter.please;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.ApplyListActivity;
import com.tgf.kcwc.driving.driv.SignInActivity;
import com.tgf.kcwc.driving.please.CompilePleasePlayActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.driving.please.SponsorPleasePlayActivity;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.PleaseFoundListBean;
import com.tgf.kcwc.mvp.presenter.PleaseFoundListPresenter;
import com.tgf.kcwc.mvp.view.PleaseFoundListView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.VacancyListView;
import com.tgf.kcwc.view.link.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/4/21.
 */

public class PleaseFoundListFragment extends BaseFragment implements PleaseFoundListView {

    private ListView mListView;
    private TextView mHint;
    private PleaseFoundListPresenter mPleaseFoundListPresenter;
    private WrapAdapter<PleaseFoundListBean.DataList> mAdapter;
    public List<PleaseFoundListBean.DataList> dataList = new ArrayList<>();
    private VacancyListView mHintLayout;
    private BGARefreshLayout rlModulenameRefresh;
    private int page = 1;
    private boolean isRefresh = true;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.driviing_apply;
    }

    @Override
    protected void initView() {
        initRefreshLayout(mBGDelegate);
        mPleaseFoundListPresenter = new PleaseFoundListPresenter();
        mPleaseFoundListPresenter.attachView(this);
        mListView = findView(R.id.list);
        mHint = findView(R.id.hint);
        mHintLayout = findView(R.id.hintlayout);
        rlModulenameRefresh = findView(R.id.rl_modulename_refresh);
        mHintLayout.setmHintText("您暂时还没有发起的活动");
        mHintLayout.setOperation("发起活动", "发起活动", R.color.text_color6, new Link.OnClickListener() {
            @Override
            public void onClick(Object o, String clickedText) {
                CommonUtils.startNewActivity(mContext, SponsorPleasePlayActivity.class);
            }
        });
        Account account = IOUtils.getAccount(mContext);

        if (account.userInfo.playAuth != 1) {
            mHint.setVisibility(View.VISIBLE);
            mHintLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
        } else {
            mHint.setVisibility(View.GONE);
            mHintLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

        mAdapter = new WrapAdapter<PleaseFoundListBean.DataList>(mContext, dataList,
                R.layout.menmber_pleasefund_item) {
            @Override
            public void convert(ViewHolder helper, final PleaseFoundListBean.DataList item) {
                ImageView apply = helper.getView(R.id.appltlist_item_apply); //是否在报名中
                ImageView mSimpleDraweeView = helper.getView(R.id.appltlist_item_image); //图片
                TextView title = helper.getView(R.id.appltlist_item_title); //标题
                TextView type = helper.getView(R.id.appltlist_item_type); //类型
                TextView number = helper.getView(R.id.appltlist_item_number); //总人数和报名人数
                TextView startCity = helper.getView(R.id.appltlist_item_startcity); //开始城市
                TextView start = helper.getView(R.id.appltlist_item_start); //开始详细地址
                TextView overCity = helper.getView(R.id.appltlist_item_overcity); //结束地城市
                TextView over = helper.getView(R.id.appltlist_item_over); //结束地详细位置
                TextView rendCity = helper.getView(R.id.appltlist_item_rendcity); //目的地城市
                TextView rend = helper.getView(R.id.appltlist_item_rend); //目的地详细位置
                TextView time = helper.getView(R.id.appltlist_item_time); //时间周期
                LinearLayout starttravel = helper.getView(R.id.appltlist_item_starttravel); //活动中
                TextView sign = helper.getView(R.id.appltlist_item_sign); //签到
                TextView diary = helper.getView(R.id.appltlist_item_diary); //途记
                TextView drive = helper.getView(R.id.appltlist_item_drive); //驾途
                TextView group = helper.getView(R.id.appltlist_item_group); //群聊
                LinearLayout applying = helper.getView(R.id.appltlist_item_applying); //正在报名
                LinearLayout watch = helper.getView(R.id.watch); //查看消息的布局
                LinearLayout znumber = helper.getView(R.id.number); //人数布局
                TextView cancelApply = helper.getView(R.id.appltlist_item_cancelapply); //取消报名
                TextView compile = helper.getView(R.id.appltlist_item_compile); //编辑
                TextView audit = helper.getView(R.id.appltlist_item_audit); //审核
                TextView cancelGroup = helper.getView(R.id.appltlist_item_cancelgroup); //正在报名群聊
                LinearLayout end = helper.getView(R.id.appltlist_item_end); //活动结束
                TextView enddiary = helper.getView(R.id.appltlist_item_enddiary); //结束后的途记
                TextView endgroup = helper.getView(R.id.appltlist_item_endgroup); //结束后的群聊
                TextView look = helper.getView(R.id.found_list_view_look); //查看的人数
                TextView like = helper.getView(R.id.found_list_view_like); //喜欢的人数
                TextView comment = helper.getView(R.id.found_list_view_comment); //消息

                LinearLayout staLayout = helper.getView(R.id.start); //开始的地点
                LinearLayout endLayout = helper.getView(R.id.end); //结束的地点
                LinearLayout rendLayout = helper.getView(R.id.appltlist_item_rendezvous); //目的地地点
                LinearLayout mLinearLayout = helper.getView(R.id.mlinearlayout);
                znumber.setVisibility(View.VISIBLE);
                watch.setVisibility(View.GONE);
                audit.setVisibility(View.VISIBLE);

                //该活动状态
                int activityStatus = item.activityStatus;
                if (activityStatus == 0) {//报名中
                    applying.setVisibility(View.VISIBLE);
                    starttravel.setVisibility(View.GONE);
                    end.setVisibility(View.GONE);
                    znumber.setVisibility(View.VISIBLE);
                    watch.setVisibility(View.GONE);
                    apply.setImageResource(R.drawable.icon_baoming);
                    if (item.needReview != 1) {
                        audit.setVisibility(View.GONE);
                    } else {
                        audit.setVisibility(View.VISIBLE);
                    }
                } else if (activityStatus == 3) {//活动结束
                    apply.setImageResource(R.drawable.icon_jiesu);
                    applying.setVisibility(View.GONE);
                    starttravel.setVisibility(View.GONE);
                    end.setVisibility(View.VISIBLE);
                    watch.setVisibility(View.GONE);
                    znumber.setVisibility(View.GONE);
                    watch.setVisibility(View.VISIBLE);
                    like.setText(item.likeCount + "");
                    comment.setText(item.replyCount + "");
                    look.setText(item.viewCount + "");
                    cancelApply.setVisibility(View.GONE);
                } else if (activityStatus == 2) {//活动进行中
                    applying.setVisibility(View.GONE);
                    starttravel.setVisibility(View.VISIBLE);
                    drive.setVisibility(View.VISIBLE);
                    end.setVisibility(View.GONE);
                    watch.setVisibility(View.GONE);
                    apply.setImageResource(R.drawable.icon_jinxing);
                } else if (activityStatus == 4) {//活动取消
                    applying.setVisibility(View.GONE);
                    starttravel.setVisibility(View.GONE);
                    end.setVisibility(View.GONE);
                    apply.setImageResource(R.drawable.icon_yiquxiao);
                } else if (activityStatus == 1) {//报名截止
                    applying.setVisibility(View.VISIBLE);
                    starttravel.setVisibility(View.GONE);
                    audit.setVisibility(View.GONE);
                    end.setVisibility(View.GONE);
                    znumber.setVisibility(View.GONE);
                    watch.setVisibility(View.VISIBLE);
                    apply.setImageResource(R.drawable.icon_bmjz);
                    like.setText(item.likeCount + "");
                    comment.setText(item.replyCount + "");
                    look.setText(item.viewCount + "");
                    cancelApply.setVisibility(View.GONE);
                } else {
                    znumber.setVisibility(View.GONE);
                    watch.setVisibility(View.GONE);
                    applying.setVisibility(View.GONE);
                    starttravel.setVisibility(View.GONE);
                    end.setVisibility(View.GONE);
                    apply.setVisibility(View.GONE);
                }
                mSimpleDraweeView
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));//item图片
                title.setText(item.title);
                String typeString = item.type;
                if (typeString.equals("self_drive")) {
                    type.setText("类型：自驾游");
                    staLayout.setVisibility(View.VISIBLE);
                    endLayout.setVisibility(View.VISIBLE);
                    rendLayout.setVisibility(View.VISIBLE);
                    startCity.setText(item.startCity);
                    start.setText(item.start);
                    overCity.setText(item.destCity);
                    over.setText(item.destination);
                } else if (typeString.equals("test_drive")) {
                    type.setText("类型：试乘试驾");
                    staLayout.setVisibility(View.GONE);
                    endLayout.setVisibility(View.GONE);
                    rendLayout.setVisibility(View.VISIBLE);
                } else {
                    type.setText("类型：其他");
                    staLayout.setVisibility(View.GONE);
                    endLayout.setVisibility(View.GONE);
                    rendLayout.setVisibility(View.VISIBLE);
                }
                time.setText(item.beginTime + " | " + item.endTime);
                rendCity.setText(item.rendCity);
                rend.setText(item.rendezvous);
                if (item.num != 0) {
                    number.setText(item.passNum + "/" + item.num);
                } else {
                    number.setText(item.passNum + "/无限");
                }

                cancelApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showQR(item.id + "");
                    }
                });
                compile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {// 编辑详情
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id + "");
                        CommonUtils.startNewActivity(mContext, args, CompilePleasePlayActivity.class);
                    }
                });
                audit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//审核
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id + "");
                        CommonUtils.startNewActivity(mContext, args, ApplyListActivity.class);
                    }
                });
                sign.setOnClickListener(new View.OnClickListener() {//签到
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id + "");
                        args.put(Constants.IntentParams.ID2, "4");
                        CommonUtils.startNewActivity(mContext, args, SignInActivity.class);
                    }
                });
                diary.setOnClickListener(new View.OnClickListener() {//途记
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id + "");
                        CommonUtils.startNewActivity(mContext, args, PostingActivity.class);
                    }
                });
                enddiary.setOnClickListener(new View.OnClickListener() {//结束后的途记
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id + "");
                        CommonUtils.startNewActivity(mContext, args, PostingActivity.class);
                    }
                });
                drive.setOnClickListener(new View.OnClickListener() {//驾途
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.THREAD_ID, item.id);
                        args.put(Constants.IntentParams.DATA, Constants.IntentParams.PLAY_CAR_MODULE);
                        CommonUtils.startNewActivity(mContext, args, DrivingHomeActivity.class);
                    }
                });

                mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id + "");
                        if (IOUtils.isLogin(mContext)) {
                            CommonUtils.startNewActivity(mContext, args, PleasePlayDetailsActivity.class);
                        }
                    }
                });
            }
        };
        mListView.setAdapter(mAdapter);
        gainData();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void dataListSucceed(PleaseFoundListBean foundListBean) {
        showLoadingIndicator(false);
        stopRefreshAll();
        if (page == 1) {
            dataList.clear();
            if (foundListBean.data.list == null || foundListBean.data.list.size() == 0) {
                mHintLayout.setVisibility(View.VISIBLE);
                rlModulenameRefresh.setVisibility(View.GONE);
            } else {
                mHintLayout.setVisibility(View.GONE);
                rlModulenameRefresh.setVisibility(View.VISIBLE);
            }
        } else {
            if (foundListBean.data.list == null || foundListBean.data.list.size() == 0) {
                isRefresh = false;
            }
        }
        dataList.addAll(foundListBean.data.list);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void dataSucceed(BaseBean baseBean) {
        CommonUtils.showToast(mContext, "取消活动成功");
        gainData();
    }

    @Override
    public void dataListDefeated(String msg) {
        showLoadingIndicator(false);
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPleaseFoundListPresenter != null) {
            mPleaseFoundListPresenter.detachView();
        }
    }

    private void showQR(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_driving_dialog, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        ImageView img = (ImageView) v.findViewById(R.id.img);
        TextView mEnddiary = (TextView) v.findViewById(R.id.appltlist_item_enddiary); //我在想想
        TextView mendgroup = (TextView) v.findViewById(R.id.appltlist_item_endgroup); //确认取消
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mEnddiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mendgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPleaseFoundListPresenter.getActivityCancel(IOUtils.getToken(mContext), id);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 300),
                BitmapUtils.dp2px(mContext, 300));
    }

    @Override
    public void onResume() {
        super.onResume();
        gainData();
    }

    public void gainData() {
        page = 1;
        isRefresh = true;
        loadMore();
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
            // mListView.removeFooterView(mListviewHint);
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
        mPleaseFoundListPresenter.gainFoundLsis(IOUtils.getToken(mContext), page);
    }

}
