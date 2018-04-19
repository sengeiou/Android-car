package com.tgf.kcwc.play.topic;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.TopicDetailsBean;
import com.tgf.kcwc.mvp.model.TopicDetailsListBean;
import com.tgf.kcwc.mvp.presenter.TopicDetailsPresenter;
import com.tgf.kcwc.mvp.view.TopicDetailsView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.MyGridView;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/5/13.
 */

public class TopicDetailsActivity extends BaseActivity implements TopicDetailsView {

    private TopicDetailsPresenter mTopicDetailsPresenter;
    private WrapAdapter<TopicDetailsListBean.DataList> mCommonAdapter;
    private SimpleDraweeView mCover;                       //封面
    private TextView mTitle;                       //标题
    private TextView mAttentiontext;               //关注
    private TextView mMessage;                     //发帖
    private TextView mIntroduction;                //导语
    private ImageView mAttentionbutt;               //关注按钮
    private LinearLayout mHave;                        //有主持人的Layout
    private SimpleDraweeView mHead;                        //有主持人的头像
    private TextView mNametv;                      //有主持人的名字
    private LinearLayout mNone;                        //没有有主持人的Layout
    private List<TopicDetailsListBean.DataList> dataLists = new ArrayList<>();
    private List<String> stringLists = new ArrayList<>();
    private MyGridView mRecyclerView;
    private String ID;
    private int page = 1;
    private boolean isRefresh = true;
    private TopicDetailsBean mTopicDetailsBean;
    private KPlayCarApp mApp;
    private RelativeLayout releaseLayout;

    private OpenShareWindow openShareWindow;              //分享弹框
    private WbShareHandler mWbHandler;
    private ObservableScrollView mScrollView;
    private TextView hint;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        setTitleBarDrawable(R.drawable.shape_titlebar_bg);
        backEvent(back);
        text.setText("话题详情");
        // text.setTextColor(mRes.getColor(R.color.text_color15));
        function.setImageResource(R.drawable.global_nav_n);
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topicdetails);
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        initRefreshLayout(mBGDelegate);
        mApp = (KPlayCarApp) getApplication();
        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        mScrollView.setScrollViewListener(mScrollListener);
        mRecyclerView = (MyGridView) findViewById(R.id.rvCity);
        mCover = (SimpleDraweeView) findViewById(R.id.cover);
        mTitle = (TextView) findViewById(R.id.title);
        mAttentiontext = (TextView) findViewById(R.id.attentiontext);
        mMessage = (TextView) findViewById(R.id.message);
        mIntroduction = (TextView) findViewById(R.id.introduction);
        mAttentionbutt = (ImageView) findViewById(R.id.attentionbutt);
        mHave = (LinearLayout) findViewById(R.id.have);
        mNone = (LinearLayout) findViewById(R.id.none);
        mHead = (SimpleDraweeView) findViewById(R.id.head);
        mNametv = (TextView) findViewById(R.id.nametv);
        releaseLayout = (RelativeLayout) findViewById(R.id.releaseLayout);
        hint = (TextView) findViewById(R.id.hint);
        setFocusb();
        mTopicDetailsPresenter = new TopicDetailsPresenter();
        mTopicDetailsPresenter.attachView(this);
        mCommonAdapter = new WrapAdapter<TopicDetailsListBean.DataList>(mContext,
                R.layout.activity_share_item, dataLists) {
            @Override
            public void convert(ViewHolder holder, TopicDetailsListBean.DataList dataList) {
                SimpleDraweeView mSimpleDraweeView = holder.getView(R.id.imageicon);
                SimpleDraweeView mGenderImg = holder.getView(R.id.genderImg);
                ImageView mEssence = holder.getView(R.id.essence);
                TextView mLike = holder.getView(R.id.like);
                TextView mInformation = holder.getView(R.id.information);
                TextView mName = holder.getView(R.id.name);
                TextView mDynamic = holder.getView(R.id.driving_list_view_dynamic);
                LinearLayout itemselect = holder.getView(R.id.itemselect);
                ImageView mModel = holder.getView(R.id.comment_model_tv);
                ImageView mConveneDa = holder.getView(R.id.drivdetails_convene_da);

                SimpleDraweeView simpleDraweeView = holder.getView(R.id.motodetail_avatar_iv);
                SimpleDraweeView mConveneBrandLogo = holder
                        .getView(R.id.drivdetails_convene_brandLogo);

                mSimpleDraweeView
                        .setImageURI(URLUtil.builderImgUrl(dataList.thread.cover, 360, 360));
                if (dataList.thread.isDigest == 1) {
                    mEssence.setVisibility(View.VISIBLE);
                } else {
                    mEssence.setVisibility(View.GONE);
                }
                mLike.setText(dataList.thread.diggCount + "");
                mInformation.setText(dataList.thread.replyCount + "");
                mDynamic.setText(dataList.thread.title);

                TopicDetailsListBean.CreateUser createBy = dataList.createUser;
                simpleDraweeView
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(createBy.avatar, 144, 144)));
                if (createBy.sex == 1) {
                    mGenderImg.setImageResource(R.drawable.icon_men);
                } else {
                    mGenderImg.setImageResource(R.drawable.icon_women);
                }
                if (createBy.isDoyen == 1) {
                    mConveneDa.setVisibility(View.VISIBLE);
                } else {
                    mConveneDa.setVisibility(View.GONE);
                }

                if (createBy.isModel == 1) {
                    mModel.setVisibility(View.VISIBLE);
                } else {
                    mModel.setVisibility(View.GONE);
                }

                if (createBy.isMaster == 1) {
                    mConveneBrandLogo.setVisibility(View.VISIBLE);
                    // mConveneBrandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(createBy.avatar, 144, 144)));
                } else {
                    mConveneBrandLogo.setVisibility(View.GONE);
                }
                mName.setText(createBy.nickname);

                final TopicDetailsListBean.Thread item = dataList.thread;
                itemselect.setOnClickListener(new View.OnClickListener() {
                    Map<String, Serializable> args = null;

                    @Override
                    public void onClick(View v) {
                        if (item.model.equals("goods")) { //车主自售
                            args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.id);
                            CommonUtils.startNewActivity(mContext, args, SaleDetailActivity.class);
                        } else if (item.model.equals("play")) { //请你玩
                            args = new HashMap<String, Serializable>();
                            args.put("id", item.id + "");
                            CommonUtils.startNewActivity(mContext, args,
                                    PleasePlayDetailsActivity.class);
                        } else if (item.model.equals("cycle")) { //开车去
                            args = new HashMap<String, Serializable>();
                            args.put("id", item.id + "");
                            CommonUtils.startNewActivity(mContext, args,
                                    DrivingDetailsActivity.class);
                        } else if (item.model.equals("words")) {//普通帖子
                            args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.id + "");
                            CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                        } else if (item.model.equals("evaluate")) { //达人评测
                            args.put(Constants.IntentParams.ID, item.id + "");
                            CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                        } else if (item.model.equals("roadbook")) {//路书
                            args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.id);
                            CommonUtils.startNewActivity(mContext, args,
                                    TripbookDetailActivity.class);
                        } else {
                            CommonUtils.showToast(mContext, "正在开发中");
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mCommonAdapter);

        mTopicDetailsPresenter.getTopicDetail(IOUtils.getToken(mContext), ID);
        mTopicDetailsPresenter.GetTopicListData(IOUtils.getToken(mContext), ID, page);
        setLoadingIndicator(true);
        releaseLayout.setOnClickListener(this);
        mAttentionbutt.setOnClickListener(this);
        findViewById(R.id.title_function_btn).setOnClickListener(this);
    }

    private ObservableScrollView.ScrollViewListener mScrollListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                    int oldy) {

            int topHeight = BitmapUtils.dp2px(mContext, 132);
            if (y <= 0) {   //设置标题的背景颜色
                // textView.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
                setTitleBarDrawable(R.drawable.shape_titlebar_bg);
                //findViewById(R.id.titleBar).setBackgroundColor(Color.argb((int) 0, 54, 169, 92));
            } else if (y > 0 && y <= topHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                float scale = (float) y / topHeight;
                float alpha = (255 * scale);
                //textView.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                // textView.setBackgroundColor(Color.argb((int) alpha, 144, 151, 166));
                findViewById(R.id.titleBar).setBackgroundColor(Color.argb((int) alpha, 54, 169, 92));
            } else {    //滑动到banner下面设置普通颜色
                // textView.setBackgroundColor(Color.argb((int) 255, 144, 151, 166));
                findViewById(R.id.titleBar).setBackgroundColor(Color.argb((int) 255, 54, 169, 92));
            }
            Logger.d("x:" + x + ",y:" + y + ",oldX:" + oldx + "oldy:" + oldy);
        }
    };

    public void setFocusb() {
        mCover.setFocusable(true);
        mCover.setFocusableInTouchMode(true);
        mTitle.setFocusable(true);
        mTitle.setFocusableInTouchMode(true);
        mIntroduction.setFocusable(true);
        mIntroduction.setFocusableInTouchMode(true);
        mHave.setFocusable(true);
        mHave.setFocusableInTouchMode(true);
        mNone.setFocusable(true);
        mNone.setFocusableInTouchMode(true);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.title_function_btn:
                List<MorePopupwindowBean> dataList = new ArrayList<>();
                MorePopupwindowBean morePopupwindowBean = null;
                if (IOUtils.isLogin(mContext)) {
                    if (mTopicDetailsBean.data.compereUser.id != 0) {
                        if ((mTopicDetailsBean.data.compereUser.id + "")
                                .equals(IOUtils.getUserId(mContext))) {
                            morePopupwindowBean = new MorePopupwindowBean();
                            morePopupwindowBean.title = "话题管理";
                            morePopupwindowBean.id = 0;
                            dataList.add(morePopupwindowBean);
                        }
                    }
                }
                morePopupwindowBean = new MorePopupwindowBean();
                morePopupwindowBean.title = "分享";
                morePopupwindowBean.id = 1;
                dataList.add(morePopupwindowBean);
                MorePopupWindow morePopupWindow = new MorePopupWindow(this, dataList,
                        new MorePopupWindow.MoreOnClickListener() {
                            @Override
                            public void moreOnClickListener(int position, MorePopupwindowBean item) {
                                switch (item.id) {
                                    case 0:
                                        if (IOUtils.isLogin(mContext)) {
                                            if (mTopicDetailsBean.data.compereUser.id != 0) {
                                                if ((mTopicDetailsBean.data.compereUser.id + "")
                                                        .equals(IOUtils.getUserId(mContext))) {
                                                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                                                    args.put("id", ID);
                                                    CommonUtils.startNewActivity(mContext, args,
                                                            CompileTopocDetailsActivity.class);
                                                } else {
                                                    CommonUtils.showToast(mContext, "您不是该话题主持人");
                                                }
                                            } else {
                                                CommonUtils.showToast(mContext, "该话题主持人没有主持人");
                                            }
                                        } else {
                                            CommonUtils.showToast(mContext, "请先登录");
                                        }
                                        break;
                                    case 1:
                                        Share();
                                        break;
                                }
                            }
                        });
                morePopupWindow.showPopupWindow(view);
                //showPopupWindow(view);
                break;
            case R.id.releaseLayout:
                if (IOUtils.isLogin(mContext)) {
                    if (mTopicDetailsBean != null) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.FROM_ID, mTopicDetailsBean.data.id);
                        args.put(Constants.IntentParams.FROM_TYPE,
                                Constants.IntentParams.TOPIC_VALUE);
                        CommonUtils.startNewActivity(mContext, args, PostingActivity.class);
                    }
                }

                break;
            case R.id.attentionbutt:
                if (IOUtils.isLogin(mContext)) {
                    if (mTopicDetailsBean.data.isAttention == 0) {
                        mTopicDetailsPresenter.GetTopicAttention(IOUtils.getToken(mContext), ID);
                    } else {
                        mTopicDetailsPresenter.GetTopicAttentionDelete(IOUtils.getToken(mContext),
                                ID);
                    }
                }
                break;
        }
    }

    public void Share() {
        openShareWindow = new OpenShareWindow(TopicDetailsActivity.this);
        openShareWindow.show(TopicDetailsActivity.this);
        final String title = "看车玩车";
        final String description = mTopicDetailsBean.data.title;
        final String cover = URLUtil.builderImgUrl(mTopicDetailsBean.data.cover, 540, 270);

        openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        OpenShareUtil.sendWXMoments(mContext, mApp.getMsgApi(), title, description);
                        break;
                    case 1:
                        OpenShareUtil.sendWX(mContext, mApp.getMsgApi(), title, description);
                        break;

                    case 2:
                        mWbHandler = OpenShareUtil.shareSina(TopicDetailsActivity.this, title,
                                description);
                        break;
                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(mApp.getTencent(), TopicDetailsActivity.this,
                                mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(mApp.getTencent(), TopicDetailsActivity.this,
                                mBaseUIListener, title, cover, description);
                        break;

                }
                openShareWindow.dismiss();
            }
        });
    }

    @Override
    public void dataSucceed(TopicDetailsBean topicDetailsBean) {
        setLoadingIndicator(false);
        mTopicDetailsBean = topicDetailsBean;
        if (topicDetailsBean.data.compereUser.id > 0) {
            mHave.setVisibility(View.VISIBLE);
            mNone.setVisibility(View.GONE);
            mHead.setImageURI(Uri
                    .parse(URLUtil.builderImgUrl(topicDetailsBean.data.compereUser.avatar, 540, 270)));//item图片
            mNametv.setText(topicDetailsBean.data.compereUser.nickname);
            mHave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, mTopicDetailsBean.data.compereUser.id);
                    CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);
                }
            });
        } else {
            mHave.setVisibility(View.GONE);
            mNone.setVisibility(View.VISIBLE);
            mNone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (IOUtils.isLogin(mContext)) {
                        Account account = IOUtils.getAccount(mContext);
                        if (account.userInfo.isVip == 1) {
                            mTopicDetailsPresenter.GetApplyHost(IOUtils.getToken(mContext), ID);
                        } else {
                            CommonUtils.showToast(mContext, "您不是种子用户，不能申请主持人");
                        }
                    }
                }
            });
        }
        mCover.setImageURI(Uri.parse(URLUtil.builderImgUrl(topicDetailsBean.data.cover, 540, 270)));//item图片
        mTitle.setText(topicDetailsBean.data.title);
        mAttentiontext.setText("关注  " + topicDetailsBean.data.fansNum);
        mMessage.setText("发帖  " + topicDetailsBean.data.threadNum);
        setTextColors(mIntroduction, "[ 导语 ]" + topicDetailsBean.data.intro, 0, 6,
                R.color.text_def);
        if (topicDetailsBean.data.isAttention == 1) {
            mAttentionbutt.setImageResource(R.drawable.btn_yiguanzhu);
        } else {
            mAttentionbutt.setImageResource(R.drawable.btn_kuangguanzhu);
        }

    }

    @Override
    public void dataListSucceed(TopicDetailsListBean topicDetailsListBean) {
        stopRefreshAll();
        setLoadingIndicator(false);
        if (page == 1) {
            dataLists.clear();
            hint.setVisibility(View.GONE);
        } else {
            if (topicDetailsListBean.data.list == null || topicDetailsListBean.data.list.size() == 0) {
                hint.setVisibility(View.VISIBLE);
                isRefresh = false;
            }
        }

        dataLists.addAll(topicDetailsListBean.data.list);
        mCommonAdapter.notifyDataSetChanged();
    }

    @Override
    public void Attention(BaseArryBean BaseBean) {
        CommonUtils.showToast(mContext, "关注成功");
        mTopicDetailsPresenter.getTopicDetail(IOUtils.getToken(mContext), ID);
    }

    @Override
    public void AttentionDelete(BaseArryBean BaseBean) {
        CommonUtils.showToast(mContext, "取消关注成功");
        mTopicDetailsPresenter.getTopicDetail(IOUtils.getToken(mContext), ID);
    }

    @Override
    public void ApplyHostSucceed(BaseArryBean BaseBean) {
        CommonUtils.showToast(mContext, "申请成为主持人成功");
    }

    @Override
    public void dataListDefeated(String msg) {
        stopRefreshAll();
        setLoadingIndicator(false);
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
        mTopicDetailsPresenter.GetTopicListData(IOUtils.getToken(mContext), ID, page);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        page = 1;
        mTopicDetailsPresenter.getTopicDetail(IOUtils.getToken(mContext), ID);
        mTopicDetailsPresenter.GetTopicListData(IOUtils.getToken(mContext), ID, page);
    }

    /*    PopupWindow popupWindow;
    private void showPopupWindow(View parent) {
        TextView btnChangeParent;
        TextView btnContactUs;
        TextView btnPublishUs;
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_popup_window, null);
    
            btnContactUs = (TextView) view.findViewById(R.id.btn_contact_us);
            btnContactUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    openShareWindow = new OpenShareWindow(TopicDetailsActivity.this);
                    openShareWindow.show(TopicDetailsActivity.this);
                    final String title = "看车玩车";
                    final String description = mTopicDetailsBean.data.title;
                    final String cover = URLUtil.builderImgUrl(mTopicDetailsBean.data.cover, 540, 270);

                    openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            switch (position) {

                                case 0:
                                    System.out.println("aaaa");
                                    OpenShareUtil.sendWX(mContext, mApp.getMsgApi(), title,
                                            description);
                                    break;
                                case 1:
                                    OpenShareUtil.sendWXMoments(mContext, mApp.getMsgApi(), title,
                                            description);
                                    break;
                                case 2:
                                    OpenShareUtil.shareToQQ(mApp.getTencent(), TopicDetailsActivity.this,
                                            mBaseUIListener, title, cover, description);
                                    break;
                                case 3:
                                    ArrayList<String> urls = new ArrayList<String>();
                                    urls.add(cover);
                                    OpenShareUtil.shareToQzone(mApp.getTencent(), TopicDetailsActivity.this,
                                            mBaseUIListener, urls, title, description);
                                    break;
                                case 4:
                                    mWbHandler = OpenShareUtil.shareSina(TopicDetailsActivity.this, title, description);
                                    break;
    
                            }
                            openShareWindow.dismiss();
                        }
                    });
                }
            });
    
            btnChangeParent = (TextView) view.findViewById(R.id.btn_change_parent);
            btnChangeParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (IOUtils.isLogin(mContext)) {
                        if (mTopicDetailsBean.data.compereUser.id != 0) {
                            if ((mTopicDetailsBean.data.compereUser.id + "").equals(IOUtils.getUserId(mContext))) {
                                Map<String, Serializable> args = new HashMap<String, Serializable>();
                                args.put("id", ID);
                                CommonUtils.startNewActivity(mContext, args, CompileTopocDetailsActivity.class);
                            } else {
                                CommonUtils.showToast(mContext, "您不是该话题主持人");
                            }
                        } else {
                            CommonUtils.showToast(mContext, "该话题主持人没有主持人");
                        }
                    } else {
                        CommonUtils.showToast(mContext, "请先登录");
                    }
                }
            });
    
            btnPublishUs = (TextView) view.findViewById(R.id.btn_publish_us);
            btnPublishUs.setVisibility(View.GONE);
            btnPublishUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, mTopicDetailsBean.data.id + "");
                    CommonUtils.startNewActivity(mContext, args, PostingActivity.class);
                }
            });
    
            if (AccountManager.getInstance().getParentList() == null ||
                    AccountManage.getInstance().getParentList().size() <= 1) {
                // btnChangeParent.setVisibility(View.GONE);
                btnChangeParent.setTextColor(getResources().getColor(R.color.voucher_gray));
                btnChangeParent.setClickable(false);
            } else {
                //  btnChangeParent.setVisibility(View.VISIBLE);
                btnChangeParent.setTextColor(getResources().getColor(R.color.text_color14));
                btnChangeParent.setClickable(true);
            }
            btnChangeParent.setTextColor(getResources().getColor(R.color.text_color14));
            btnChangeParent.setClickable(true);
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
    
            //popupWindow = new PopupWindow(view, outMetrics.widthPixels / 8, -2);
            popupWindow = new PopupWindow(view, -2, -2);
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        int xPos = -popupWindow.getWidth() / 2;
    
        popupWindow.showAsDropDown(parent, xPos, 0);
    
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTopicDetailsPresenter != null) {
            mTopicDetailsPresenter.detachView();
        }
    }

}
