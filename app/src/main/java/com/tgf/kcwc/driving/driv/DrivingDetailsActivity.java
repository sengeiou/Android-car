package com.tgf.kcwc.driving.driv;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.ViewPagerAdapter;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.comment.CommentFrag;
import com.tgf.kcwc.comment.CommentMoreActivity;
import com.tgf.kcwc.comment.DianzanFrag;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.DrivDetailsBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.LikeBean;
import com.tgf.kcwc.mvp.model.LikeListModel;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.DrivingDetailsPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.DrivingDetailsView;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.link.Link;
import com.tgf.kcwc.view.richeditor.MixedTextImageLayout;
import com.tgf.kcwc.view.tagcloudview.TagBaseAdapter;
import com.tgf.kcwc.view.tagcloudview.TagCloudLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/24.
 * <p>
 * if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//4.4 全透明状态栏
 * getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
 * }
 * if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
 * Window window = getWindow();
 * window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
 * window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
 * | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
 * window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
 * window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
 * }
 */

public class DrivingDetailsActivity extends BaseActivity implements DrivingDetailsView {

    private DrivingDetailsPresenter mDrivingDetailsPresenter;
    private DrivDetailsBean mDrivDetailsBean;
    private SimpleDraweeView mSimpleDraweeViewCover;                       //封面
    private TagCloudLayout mContainer;                                   //标签列表
    private TagBaseAdapter mAdapter;                                     //标签adapter
    private LinearLayout mContainerLayout;                          //标签布局
    private LinearLayout mHonorlayout;                          //荣誉标签布局
    private List<DrivDetailsBean.TopicList> ContainerList = new ArrayList<>();     //标签数据源
    private ViewPager mViewPager;                                   //精彩分享
    private ViewPagerAdapter mViewPagerAdapter;                            //精彩分享adapter
    private List<View> fragments = new ArrayList<View>(); //分享数据源
    private List<String> stringList = new ArrayList<>();
    private TextView mTextPagination;                              //分享页码
    private TextView mTitle;                                       //标题
    private ImageView mActivityStatus;                              //活动状态
    private int typenume = -1;                    //状态
    private ImageView mEssence;                                     //是否是精华
    private TextView mApplyAborttime;                              //报名中截止时间
    private TextView mStartcity;                                   //开始城市
    private TextView mStart;                                       //开始详细地址
    private TextView mOvercity;                                    //结束城市
    private TextView mOver;                                        //结束详细地址
    private TextView mTime;                                        //时间
    private TextView mBudget;                                      //人均预算
    //  private MyListView mExplainList;                          //活动说明
    private WrapAdapter<DrivDetailsBean.MEditorDatas> mExplainAdapter;                              //活动adapter
    private List<DrivDetailsBean.MEditorDatas> mEditorDatas = new ArrayList<>();
    private TextView mRequireAnnotation;                           //报名要求注
    private TextView mRequireattention;                            //注意
    private ImageView mRequireOwner;                                //报名要求是否是车主
    private ImageView mRequireAudit;                                //报名要求是否需要审核
    private SimpleDraweeView mSimpleDraweeView;                            //召集人头像
    private ImageView mConveneSex;                                  //召集人性别
    private TextView mConveneName;                                 //召集人名字
    private TextView mConveneTime;                                 //召集人发布时间
    private TextView mConveneNumber;                               //召集人发布次数
    private ImageView mConveneDa;                                   //召集人 达
    private ImageView mConveneModel;                                //召集人 模
    private SimpleDraweeView mConveneBrandLogo;                            //召集人 车
    private TextView mApplyNumber;                                 //报名中人数
    private TextView mApplynumber;                                 //报名人数
    private RecyclerView mRecyclerView;                                //报名人数列表
    private CommonAdapter<DrivDetailsBean.ApplyList> mHorizontalAdapter;                           //报名人数daapter
    private List<DrivDetailsBean.ApplyList> mApplyList = new ArrayList<>();     //报名人数数据源
    private RecyclerView mHonorrecycler;                               //荣誉列表
    private CommonAdapter<DrivDetailsBean.Honour> mHonorrecyclerAdapter;                        //荣誉记录daapter
    private List<DrivDetailsBean.Honour> mHonour = new ArrayList<>();     //荣誉记录数据源
    private LinearLayout mApply;                                       //报名Layout
    private LinearLayout mPagerlayout;                                 //viewpagerLayout
    private LinearLayout mStartLay;                                    //开始地址布局
    private LinearLayout mOverLay;                                     //结束地址布局
    private LinearLayout mApplylay;                                    //报名人数布局
    private TextView mSignup;                                      //报名
    private ImageView mFavoriteImg;                                 //帖子点赞
    private RelativeLayout mCommentLayout;                               //评论数字
    protected TextView mUnReadNums;                                  //评论数量
    private LinearLayout mRepLayout;                                   //评论
    private RelativeLayout mUserRelayout;                                //发起人
    private LinearLayout mBudgetLayout;                                //预算布局

    private WrapAdapter<DrivingRoadBookBean.RideCheck> mSigninAdapter;                               //签到adapter
    private List<DrivingRoadBookBean.RideCheck> dataRideLeadersList = new ArrayList<>();     //签到数据源
    private MyListView myListView;                                   //签到列表
    private TextView mTotalrun;                                    //总行程
    private TextView plantime;                                     //计划时间
    private TextView already;                                      //已进行的行程
    private TextView elapsedtime;                                  //已用时间
    private TextView stationnum;                                   //地几个站点
    private TextView percentage;                                   //百分比
    private ProgressBar myProgress;                                   // 进度条
    private SimpleDraweeView mapimag;                                      // 图片

    private LinearLayout maplayout;                                    // 地图layout

    private LinearLayout notbegunlayout;                               // 没开始
    private LinearLayout signinlayout;                                 // 开始
    private TextView notbegunkilometre;                            // 没开始的公里数
    private TextView notbegunkispot;                               //  途径多少个点

    private DrivingRoadBookBean mDrivingRoadBookBean = null;                  //数据源

    //点赞评论
    protected RelativeLayout mCommentBtnLayout;
    protected TextView mCmtTitle;
    protected ImageView mBtmLine1;
    protected TextView mCmtContent;

    protected RelativeLayout mLikeBtnLayout;
    protected ImageView mBtmLine2;
    protected TextView mLikeTitle;
    protected TextView mLikeContent;
    private Fragment commentFrag;
    private FragmentManager commentFm;
    private FragmentTransaction commentTran;                                  //评论
    private DianzanFrag dianzanFrag;                                  //点赞
    private LinearLayout melayout;                                     //自己发表评论
    private LinearLayout applynumber;                                  //报名layout
    private ImageView share;                                        //分享
    private OpenShareWindow openShareWindow;                              //分享弹框
    private WbShareHandler mWbHandler;
    private KPlayCarApp mApp;
    private String ID;                                           //传过来的id
    private MixedTextImageLayout mRichLayout;

    private CommentListPresenter mCommentPresenter;                            //评论列表
    private CommentListPresenter mLikePresenter;                               //点赞列表
    private CommentListPresenter mExecLikePresenter;                           //帖子点赞

    private LinearLayout mOwnerLayout;                                 //车主Layout
    private LinearLayout mChecklayout;                                 //审核Layout
    private ObservableScrollView mScrollView;

    protected SimpleDraweeView mMotodetailAvatarIv;    // 我自己的头像
    protected SimpleDraweeView mBelowgenderImg;    // 我自己的性别

    @Override
    protected void setUpViews() {
        ID = (String) getIntent().getSerializableExtra(Constants.IntentParams.ID);
        commentFm = getSupportFragmentManager();
        mApp = (KPlayCarApp) getApplication();
        mRichLayout = (MixedTextImageLayout) findViewById(R.id.richContentLayout);
        mOwnerLayout = (LinearLayout) findViewById(R.id.ownerlayout);
        mChecklayout = (LinearLayout) findViewById(R.id.checklayout);
        mContainerLayout = (LinearLayout) findViewById(R.id.containerlayout);
        mHonorlayout = (LinearLayout) findViewById(R.id.honorlayout);
        mContainer = (TagCloudLayout) findViewById(R.id.container);
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.driving_list_view_imageview);
        mSimpleDraweeViewCover = (SimpleDraweeView) findViewById(R.id.drivdetails_cover);
        mRecyclerView = (RecyclerView) findViewById(R.id.drivdetail_recyclerview_horizontal);
        mHonorrecycler = (RecyclerView) findViewById(R.id.drivdetail_honorrecycler);
        mViewPager = (ViewPager) findViewById(R.id.drivdetail_pager);
        mTextPagination = (TextView) findViewById(R.id.drivdetail_pager_pagination);

        mCommentBtnLayout = (RelativeLayout) findViewById(R.id.commentBtnLayout);
        mLikeBtnLayout = (RelativeLayout) findViewById(R.id.likeBtnLayout);

        mLikeTitle = (TextView) findViewById(R.id.likeTitle);
        mLikeContent = (TextView) findViewById(R.id.likeContent);
        mBtmLine2 = (ImageView) findViewById(R.id.btmLine2);
        mCmtContent = (TextView) findViewById(R.id.cmtContent);
        mCmtTitle = (TextView) findViewById(R.id.cmtTitle);

        mBtmLine1 = (ImageView) findViewById(R.id.btmLine1);

        mTitle = (TextView) findViewById(R.id.drivdetails_title);
        mStartcity = (TextView) findViewById(R.id.drivdetails_startcity);
        mStart = (TextView) findViewById(R.id.drivdetails_start);
        mOvercity = (TextView) findViewById(R.id.drivdetails_overcity);
        mOver = (TextView) findViewById(R.id.drivdetails_over);
        mTime = (TextView) findViewById(R.id.drivdetails_item_time);
        mBudget = (TextView) findViewById(R.id.drivdetails_item_budget);
        mRequireAnnotation = (TextView) findViewById(R.id.drivdetails_require_annotation);
        mRequireattention = (TextView) findViewById(R.id.drivdetails_require_attention);
        mRequireOwner = (ImageView) findViewById(R.id.drivdetails_require_owner);
        mRequireAudit = (ImageView) findViewById(R.id.drivdetails_require_audit);
        mConveneSex = (ImageView) findViewById(R.id.drivdetails_convene_sex);
        mConveneDa = (ImageView) findViewById(R.id.drivdetails_convene_da);
        mConveneName = (TextView) findViewById(R.id.drivdetails_convene_name);
        mConveneTime = (TextView) findViewById(R.id.drivdetails_convene_time);
        mConveneNumber = (TextView) findViewById(R.id.drivdetails_convene_number);
        mConveneBrandLogo = (SimpleDraweeView) findViewById(R.id.drivdetails_convene_brandLogo);
        mEssence = (ImageView) findViewById(R.id.drivdetails_essence);
        mApplyNumber = (TextView) findViewById(R.id.drivdetails_apply_number);
        mApplyAborttime = (TextView) findViewById(R.id.drivdetails_apply_aborttime);
        mActivityStatus = (ImageView) findViewById(R.id.drivdetails_activitystatus);
        mApply = (LinearLayout) findViewById(R.id.drivdetails_apply);
        mPagerlayout = (LinearLayout) findViewById(R.id.drivdetail_pagerlayout);
        //mExplainList = (MyListView) findViewById(R.id.drivdetails_explain);
        mConveneModel = (ImageView) findViewById(R.id.comment_model_tv);
        mApplynumber = (TextView) findViewById(R.id.applynumber);
        mStartLay = (LinearLayout) findViewById(R.id.drivdetails_startlay);
        mOverLay = (LinearLayout) findViewById(R.id.drivdetails_overlay);
        mApplylay = (LinearLayout) findViewById(R.id.applylay);
        mSignup = (TextView) findViewById(R.id.signup);
        mFavoriteImg = (ImageView) findViewById(R.id.favoriteImg);
        mCommentLayout = (RelativeLayout) findViewById(R.id.commentLayout);
        mRepLayout = (LinearLayout) findViewById(R.id.CommentrepayLayout);
        mApply = (LinearLayout) findViewById(R.id.drivdetails_apply);
        mUserRelayout = (RelativeLayout) findViewById(R.id.userrelayout);

        myListView = (MyListView) findViewById(R.id.mylistview);
        mTotalrun = (TextView) findViewById(R.id.totalrun);
        plantime = (TextView) findViewById(R.id.plantime);
        already = (TextView) findViewById(R.id.already);
        elapsedtime = (TextView) findViewById(R.id.elapsedtime);
        stationnum = (TextView) findViewById(R.id.stationnum);
        percentage = (TextView) findViewById(R.id.percentage);
        myProgress = (ProgressBar) findViewById(R.id.my_progress);
        mapimag = (SimpleDraweeView) findViewById(R.id.mapimag);
        notbegunlayout = (LinearLayout) findViewById(R.id.notbegunlayout);
        signinlayout = (LinearLayout) findViewById(R.id.signinlayout);
        notbegunkilometre = (TextView) findViewById(R.id.notbegunkilometre);
        notbegunkispot = (TextView) findViewById(R.id.notbegunkispot);
        maplayout = (LinearLayout) findViewById(R.id.maplayout);
        melayout = (LinearLayout) findViewById(R.id.melayout);
        applynumber = (LinearLayout) findViewById(R.id.applynumberlayout);
        share = (ImageView) findViewById(R.id.share);
        mUnReadNums = (TextView) findViewById(R.id.comment_numbers);
        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        mScrollView.setScrollViewListener(mScrollListener);
        mMotodetailAvatarIv = (SimpleDraweeView) findViewById(R.id.motodetail_avatar_iv);
        mBelowgenderImg = (SimpleDraweeView) findViewById(R.id.belowgenderImg);
        mBudgetLayout = (LinearLayout) findViewById(R.id.budgetlayout);

        mRepLayout.setOnClickListener(this);
        mCommentBtnLayout.setOnClickListener(this);
        mLikeBtnLayout.setOnClickListener(this);
        mSignup.setOnClickListener(this);
        mFavoriteImg.setOnClickListener(this);
        mCommentLayout.setOnClickListener(this);
        mApply.setOnClickListener(this);
        share.setOnClickListener(this);
        mUserRelayout.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager HonorrecyclerLayoutManager = new LinearLayoutManager(this);
        HonorrecyclerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHonorrecycler.setLayoutManager(HonorrecyclerLayoutManager);

        //mExplainList.setFocusable(false);
        myListView.setFocusable(false);

        mApplylay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                if (mDrivDetailsBean != null) {
                    args.put("id", mDrivDetailsBean.data.id + "");
                }
                if (IOUtils.isLogin(mContext)) {
                    CommonUtils.startNewActivity(mContext, args, ApplyListActivity.class);
                }
            }
        });
        applynumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                if (mDrivDetailsBean != null) {
                    args.put("id", mDrivDetailsBean.data.id + "");
                }
                if (IOUtils.isLogin(mContext)) {
                    CommonUtils.startNewActivity(mContext, args, ApplyListActivity.class);
                }
            }
        });
        setAdapter();

        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(mCommentView);

        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mLikeView);

        mExecLikePresenter = new CommentListPresenter();
        mExecLikePresenter.attachView(mExecLikeView);

        mDrivingDetailsPresenter = new DrivingDetailsPresenter();
        mDrivingDetailsPresenter.attachView(this);
        CommentLiske();
    }

    private ObservableScrollView.ScrollViewListener mScrollListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                    int oldy) {

            int topHeight = BitmapUtils.dp2px(mContext, 132);
            if (y <= 0) {   //设置标题的背景颜色
                // textView.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
                setTitleBarDrawable(R.drawable.shape_titlebar_bg);
                // findViewById(R.id.titleBar).setBackgroundColor(Color.argb((int) 0, 54, 169, 92));
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

    public void CommentLiske() {
        mCommentPresenter.loadCommentList("thread", ID, "moto");
        mLikePresenter.loadLikeList(ID, IOUtils.getToken(mContext));
        mDrivingDetailsPresenter.gainDetailsData(IOUtils.getToken(mContext), ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommentLiske();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivingdetails);

    }

    public void setAdapter() {

        //签到adapter
        mSigninAdapter = new WrapAdapter<DrivingRoadBookBean.RideCheck>(mContext,
                R.layout.drivingdeyail_signin_item, dataRideLeadersList) {
            @Override
            public void convert(ViewHolder helper, final DrivingRoadBookBean.RideCheck item) {
                ImageView node = helper.getView(R.id.node);
                TextView city = helper.getView(R.id.city);
                TextView cityname = helper.getView(R.id.name);
                TextView numbersignin = helper.getView(R.id.numbersignin);
                TextView start = helper.getView(R.id.start);
                TextView kilometre = helper.getView(R.id.kilometre);
                TextView time = helper.getView(R.id.time);
                RelativeLayout leftdown = helper.getView(R.id.leftdown);
                LinearLayout rightdown = helper.getView(R.id.rightdown);
                city.setText(item.cityName);
                cityname.setText(item.address);
                numbersignin.setText(item.num + "人签到");
                start.setText(item.lightTime);
                kilometre.setText(item.mileage + "km");
                time.setText(item.previousTime);

                if (item.isLight == 1) {
                    leftdown.setVisibility(View.VISIBLE);
                    rightdown.setVisibility(View.VISIBLE);
                } else {
                    leftdown.setVisibility(View.GONE);
                    rightdown.setVisibility(View.GONE);
                }

                int position = helper.getPosition();
                if ((position + 1) == dataRideLeadersList.size()) {
                    node.setImageResource(R.drawable.icon_red);
                } else {
                    node.setImageResource(R.drawable.icon_green);
                }
                numbersignin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        // args.put("mDrivingRoadBookBean",mDrivingRoadBookBean);
                        args.put(Constants.IntentParams.ID, ID + "");
                        args.put(Constants.IntentParams.ID2, "1");
                        if (IOUtils.isLogin(mContext)) {
                            CommonUtils.startNewActivity(mContext, args, SignInActivity.class);
                        }
                    }
                });
            }
        };
        myListView.setAdapter(mSigninAdapter);


        //标签adapter
        mAdapter = new TagBaseAdapter(this, ContainerList);
        mContainer.setAdapter(mAdapter);

        //活动说明
        mExplainAdapter = new WrapAdapter<DrivDetailsBean.MEditorDatas>(mContext,
                R.layout.activity_drivdetails_item, mEditorDatas) {
            @Override
            public void convert(ViewHolder helper, DrivDetailsBean.MEditorDatas item) {
                TextView content = helper.getView(R.id.drivdetails_explain_content);
                ImageView explain = helper.getView(R.id.drivingdetails_explain);

                if (item != null) {
                    if (item.inputStr == null) {
                        content.setVisibility(View.GONE);
                    } else {
                        if (item.inputStr.equals("")) {
                            content.setVisibility(View.GONE);
                        } else {
                            content.setVisibility(View.VISIBLE);
                            content.setText(item.inputStr);
                        }
                    }

                    if (item.imageUrl == null) {
                        explain.setVisibility(View.GONE);
                    } else {
                        if (item.imageUrl.equals("")) {
                            explain.setVisibility(View.GONE);
                        } else {
                            explain.setVisibility(View.VISIBLE);
                            Glide.with(mContext).load(URLUtil.builderImgUrl750(item.imageUrl))
                                    .placeholder(R.mipmap.default_image)//
                                    .error(R.mipmap.default_image)//
                                    .into(explain);
                        }
                    }
                }
            }
        };
        //mExplainList.setAdapter(mExplainAdapter);

        //报名人数 adapter
        mHorizontalAdapter = new CommonAdapter<DrivDetailsBean.ApplyList>(mContext,
                R.layout.drivingdetail_horizon_item, mApplyList) {
            @Override
            public void convert(ViewHolder holder, DrivDetailsBean.ApplyList applyList) {
                SimpleDraweeView simpleDraweeView = holder
                        .getView(R.id.drivdetail_recycler_view_imageview);
                simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(applyList.avatar)));
                simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        if (mDrivDetailsBean != null) {
                            args.put("id", mDrivDetailsBean.data.id + "");
                        }
                        if (IOUtils.isLogin(mContext)) {
                            CommonUtils.startNewActivity(mContext, args, ApplyListActivity.class);
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mHorizontalAdapter);

        //荣誉记录adapter
        mHonorrecyclerAdapter = new CommonAdapter<DrivDetailsBean.Honour>(mContext,
                R.layout.drivdetail_honor_item, mHonour) {
            @Override
            public void convert(ViewHolder holder, DrivDetailsBean.Honour honour) {
                SimpleDraweeView mSimpleDraweeView = holder.getView(R.id.icon);
                TextView title = holder.getView(R.id.title);
                TextView bonuspoint = holder.getView(R.id.bonuspoint);
                mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(honour.icon)));
                title.setText(honour.text);
                if (!TextUtils.isEmpty(honour.tag)) {
                    ViewUtil.link(honour.substr, title, new Link.OnClickListener() {
                        @Override
                        public void onClick(Object o, String clickedText) {

                        }
                    }, mRes.getColor(R.color.text_color6), true);
                }
                bonuspoint.setText(honour.integral + "分");
            }
        };
        mHonorrecycler.setAdapter(mHonorrecyclerAdapter);
    }

    @Override
    public void detailsDataSucceed(DrivDetailsBean drivDetailsBean) {
        mDrivDetailsBean = drivDetailsBean;
        mEditorDatas.clear();
        ContainerList.clear();
        mApplyList.clear();
        mHonour.clear();
        fragments.clear();
        //标题
        mTitle.setText(drivDetailsBean.data.title);

        mSimpleDraweeViewCover
                .setImageURI(Uri.parse(URLUtil.builderImgUrl(drivDetailsBean.data.cover, 540, 270)));
        //该活动状态
        String activityStatus = drivDetailsBean.data.activityStatus;
        mActivityStatus.setVisibility(View.VISIBLE);
        mSignup.setBackgroundColor(getResources().getColor(R.color.btn_select_color));
        if (activityStatus.equals("报名中")) {
            typenume = 1;
            mApply.setVisibility(View.VISIBLE);
            mApplyAborttime.setText("报名截止：" + drivDetailsBean.data.deadlineTime);
            if (drivDetailsBean.data.passNum == 0) {
                mApplyNumber
                        .setText(drivDetailsBean.data.passNum + "/不限");
            } else {
                mApplyNumber
                        .setText(drivDetailsBean.data.passNum + "/" + drivDetailsBean.data.limitMax);
            }
            mActivityStatus.setImageResource(R.drawable.btn_apply);
            if (mDrivDetailsBean.data.isApply == 0) {
                mSignup.setText("我要报名");
                mSignup.setBackgroundColor(getResources().getColor(R.color.btn_select_color));
            } else if (mDrivDetailsBean.data.isApply == 1) {
                mSignup.setText("取消报名");
                mSignup.setBackgroundColor(getResources().getColor(R.color.progres_green));
            } else if (mDrivDetailsBean.data.isApply == 2) {
                mSignup.setText("报名待审核");
                mSignup.setBackgroundColor(getResources().getColor(R.color.text_color));
            }
        } else if (activityStatus.equals("活动结束")) {
            typenume = 2;
            mApply.setVisibility(View.GONE);
            mActivityStatus.setImageResource(R.drawable.btn_finish);
            mSignup.setText("活动结束");
            mSignup.setBackgroundColor(getResources().getColor(R.color.text_color));
        } else if (activityStatus.equals("活动中")) {
            typenume = 3;
            mApply.setVisibility(View.GONE);
            mActivityStatus.setImageResource(R.drawable.btn_hdz);
            mSignup.setText("活动中");
        } else if (activityStatus.equals("报名截止")) {
            typenume = 4;
            mApply.setVisibility(View.GONE);
            mActivityStatus.setImageResource(R.drawable.btn_applystop);
            mSignup.setText("报名截止");
            mSignup.setBackgroundColor(getResources().getColor(R.color.text_color));
        } else if (activityStatus.equals("活动取消")) {
            typenume = 5;
            mApply.setVisibility(View.GONE);
            mActivityStatus.setImageResource(R.drawable.kcyiquxiao);
            mSignup.setText("活动取消");
            mSignup.setBackgroundColor(getResources().getColor(R.color.text_color));
        }
     /*   //这是途经点数据获取
        if (drivDetailsBean.data.roadbookId != 0) {
            mDrivingDetailsPresenter.getDetail(IOUtils.getToken(mContext), drivDetailsBean.data.id + "");
        } else {
            maplayout.setVisibility(View.GONE);
            signinlayout.setVisibility(View.GONE);
            myListView.setVisibility(View.GONE);
            notbegunlayout.setVisibility(View.GONE);
        }*/
        //暂时隐藏
        maplayout.setVisibility(View.GONE);
        signinlayout.setVisibility(View.GONE);
        myListView.setVisibility(View.GONE);
        notbegunlayout.setVisibility(View.GONE);

        if (drivDetailsBean.data.sceneType == 5 || drivDetailsBean.data.sceneType == 2) {
            mStartLay.setVisibility(View.GONE);
            mOverLay.setVisibility(View.VISIBLE);
            mOvercity.setText(drivDetailsBean.data.destCity);
            mOver.setText(drivDetailsBean.data.destination);
        } else {
            mStartLay.setVisibility(View.VISIBLE);
            mOverLay.setVisibility(View.VISIBLE);
            mStartcity.setText(drivDetailsBean.data.startCity);
            mStart.setText(drivDetailsBean.data.start);
            mOvercity.setText(drivDetailsBean.data.destCity);
            mOver.setText(drivDetailsBean.data.destination);
        }

        int isDigest = drivDetailsBean.data.isDigest;
        if (isDigest == 1) {
            mEssence.setVisibility(View.VISIBLE);
        } else {
            mEssence.setVisibility(View.GONE);
        }
        mTime.setText(drivDetailsBean.data.beginTime + " 至 " + drivDetailsBean.data.endTime);
        if (drivDetailsBean.data.budget.equals("0.00")) {
            mBudgetLayout.setVisibility(View.GONE);
        } else {
            mBudgetLayout.setVisibility(View.VISIBLE);
            mBudget.setText(drivDetailsBean.data.budget + "元（人均预算）");
        }
        List<DrivDetailsBean.ShareList> shareList = new ArrayList<>();
        shareList.addAll(drivDetailsBean.data.shareList);
        if (shareList.size() > 0) {
            mPagerlayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < shareList.size(); i++) {
                fragments.add(getView(shareList.get(i)));
            }
        } else {
            mPagerlayout.setVisibility(View.GONE);
        }
        //精彩分享adapter
        mTextPagination.setText("精彩分享（" + 1 + "/" + fragments.size() + "）");
        mViewPagerAdapter = new ViewPagerAdapter(fragments);
        mViewPager.setAdapter(mViewPagerAdapter);

        //设置监听，主要是设置当前页码
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTextPagination.setText(
                        "精彩分享（" + (position % fragments.size() + 1) + "/" + fragments.size() + "）");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //活动说明
        showRichContent(drivDetailsBean);
        /*        if (drivDetailsBean.data.intro != null) {
            if (drivDetailsBean.data.intro.mEditorDatas != null) {
                mEditorDatas.addAll(drivDetailsBean.data.intro.mEditorDatas);
            }+
        
        }
        mExplainAdapter.notifyDataSetChanged();*/

        int needReview = drivDetailsBean.data.needReview;
        int onlyOwner = drivDetailsBean.data.onlyOwner;

        if (needReview == 1) {
            mChecklayout.setVisibility(View.VISIBLE);
            mRequireAudit.setImageResource(R.drawable.btn_kuanggou);
        } else {
            mChecklayout.setVisibility(View.GONE);
            mRequireAudit.setImageResource(R.drawable.icon_select_box_n);
        }

        if (onlyOwner == 1) {
            mOwnerLayout.setVisibility(View.VISIBLE);
            mRequireOwner.setImageResource(R.drawable.btn_kuanggou);
        } else {
            mOwnerLayout.setVisibility(View.GONE);
            mRequireOwner.setImageResource(R.drawable.icon_select_box_n);
        }

        mRequireAnnotation.setText(drivDetailsBean.data.otherCondition);
        mRequireattention.setText("注：报名人数不足" + drivDetailsBean.data.limitMin + "人时，活动将自动取消");

        if (drivDetailsBean.data.topicList != null && drivDetailsBean.data.topicList.size() != 0) {
            ContainerList.addAll(drivDetailsBean.data.topicList);
            mAdapter.notifyDataSetChanged();
        } else {
            mContainerLayout.setVisibility(View.GONE);
        }

        mApplynumber.setText("报名人数（" + drivDetailsBean.data.passNum + "）");
        if (drivDetailsBean.data.applyList.size() == 0 || drivDetailsBean.data.applyList == null) {
            mApplylay.setVisibility(View.GONE);
        } else {
            mApplyList.clear();
            mApplylay.setVisibility(View.VISIBLE);
            mApplyList.addAll(drivDetailsBean.data.applyList);
            mHorizontalAdapter.notifyDataSetChanged();
        }

        if (drivDetailsBean.data.honour != null && drivDetailsBean.data.honour.size() != 0) {
            mHonour.addAll(drivDetailsBean.data.honour);
            mHonorrecyclerAdapter.notifyDataSetChanged();
        } else {
            mHonorlayout.setVisibility(View.GONE);
        }

        DrivDetailsBean.CreateBy createBy = drivDetailsBean.data.createBy;
        mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(createBy.avatar)));
        if (createBy.sex == 1) {
            mConveneSex.setImageResource(R.drawable.icon_men);
        } else {
            mConveneSex.setImageResource(R.drawable.icon_women);
        }
        mConveneName.setText(createBy.username);
        if (createBy.isDoyen == 1) {
            mConveneDa.setVisibility(View.VISIBLE);
        } else {
            mConveneDa.setVisibility(View.GONE);
        }

        if (createBy.isModel == 1) {
            mConveneModel.setVisibility(View.VISIBLE);
        } else {
            mConveneModel.setVisibility(View.GONE);
        }

        if (createBy.isMaster == 1) {
            mConveneBrandLogo.setVisibility(View.VISIBLE);
            mConveneBrandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(createBy.logo)));
        } else {
            mConveneBrandLogo.setVisibility(View.GONE);
        }
        mConveneNumber.setText("共发布" + createBy.createCount + "次召集");
        mConveneTime.setText(DateFormatUtil.timeLogic(drivDetailsBean.data.createTime) + "发布召集");

        if (drivDetailsBean.data.isPraise == 1) {
            mFavoriteImg.setImageResource(R.drawable.btn_heart2);
        } else {
            mFavoriteImg.setImageResource(R.drawable.btn_heart1);
        }

        if (drivDetailsBean.data.replyCount > 0) {
            mUnReadNums.setVisibility(View.VISIBLE);
            mUnReadNums.setText(drivDetailsBean.data.replyCount + "");
        } else {
            mUnReadNums.setVisibility(View.GONE);
        }
        Account accou = IOUtils.getAccount(mContext);
        if (accou != null) {
            Account.UserInfo account = IOUtils.getAccount(mContext).userInfo;
            mMotodetailAvatarIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(account.avatar)));
            if (account.gender == 1) {
                mBelowgenderImg.setImageResource(R.drawable.icon_men);
            } else {
                mBelowgenderImg.setImageResource(R.drawable.icon_women);
            }
        }

    }

    private void showRichContent(DrivDetailsBean drivDetailsBean) {

        mRichLayout.removeAllViews();//清空所有已添加的View

        if (drivDetailsBean.data.intro == null) {
            return;
        }
        List<DrivDetailsBean.MEditorDatas> dataList = drivDetailsBean.data.intro.mEditorDatas;
        for (DrivDetailsBean.MEditorDatas d : dataList) {

            String inputStr = d.inputStr;
            String path = d.imageUrl;
            if (!TextUtils.isEmpty(inputStr)) {
                mRichLayout.appendTextView(inputStr);
            }

            if (!TextUtils.isEmpty(path)) {

                mRichLayout.appendImageView(this, URLUtil.builderImgUrl750(path));
            }

        }
    }

    @Override
    public void DetailsSucceed(DrivingRoadBookBean drivingRoadBookBean) {
        mDrivingRoadBookBean = drivingRoadBookBean;
        dataRideLeadersList.clear();
        DrivingRoadBookBean.PlanLine dataRidePlan = drivingRoadBookBean.data.planLine;
        DrivingRoadBookBean.AlreadyRide allAlrealyDistance = drivingRoadBookBean.data.alreadyRide;
        mapimag.setImageURI(Uri.parse(URLUtil.builderImgUrl(dataRidePlan.cover, 540, 270)));

        switch (typenume) {
            case 1:
            case 4:
                maplayout.setVisibility(View.VISIBLE);
                signinlayout.setVisibility(View.GONE);
                myListView.setVisibility(View.GONE);
                notbegunlayout.setVisibility(View.VISIBLE);
                notbegunkilometre.setText(dataRidePlan.mileage);
                notbegunkispot.setText("途经点" + dataRidePlan.number + "个");
                break;
            case 3:
            case 2:
                signinlayout.setVisibility(View.VISIBLE);
                myListView.setVisibility(View.VISIBLE);
                notbegunlayout.setVisibility(View.GONE);
                maplayout.setVisibility(View.VISIBLE);
                mTotalrun.setText(dataRidePlan.mileage + "");
                plantime.setText("计划用时 （t） " + dataRidePlan.planTime);
                already.setText(allAlrealyDistance.mileage + "");
                elapsedtime.setText(allAlrealyDistance.time + "");
                stationnum.setText(allAlrealyDistance.orders + "");
                percentage.setText(allAlrealyDistance.percent + "%");
                myProgress.setSecondaryProgress(allAlrealyDistance.percent);
                dataRideLeadersList.addAll(drivingRoadBookBean.data.rideCheck);
                mSigninAdapter.notifyDataSetChanged();
                break;
            default:
                signinlayout.setVisibility(View.GONE);
                myListView.setVisibility(View.GONE);
                notbegunlayout.setVisibility(View.GONE);
                maplayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void ApplyCancelSucceed(BaseBean baseBean) {
        CommentLiske();
        CommonUtils.showToast(mContext, "取消报名成功");
    }

    @Override
    public void detailsDataFeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        setTitleBarDrawable(R.drawable.shape_titlebar_bg);
        backEvent(back);
        text.setText("开车去详情");
        // text.setTextColor(mRes.getColor(R.color.text_color15));
        function.setImageResource(R.drawable.global_nav_n);
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Map<String, Serializable> args = null;
        int id = view.getId();
        switch (id) {
            case R.id.commentBtnLayout:
                mCmtTitle.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                mBtmLine1.setVisibility(View.VISIBLE);
                mLikeTitle.setTextColor(mRes.getColor(R.color.text_more));
                mBtmLine2.setVisibility(View.INVISIBLE);
                // mCommentPresenter.loadCommentList("thread", ID + "", "moto");
                if (commentFrag != null) {
                    commentTran = commentFm.beginTransaction();
                    commentTran.replace(R.id.comment_fragfl, commentFrag);
                    commentTran.commit();
                }
                melayout.setVisibility(View.VISIBLE);
                break;

            case R.id.likeBtnLayout:
                mLikeTitle.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                mBtmLine2.setVisibility(View.VISIBLE);
                mCmtTitle.setTextColor(mRes.getColor(R.color.text_more));
                mBtmLine1.setVisibility(View.INVISIBLE);
                // mLikePresenter.loadLikeList(ID + "", IOUtils.getToken(mContext));
                if (dianzanFrag != null) {
                    commentTran = commentFm.beginTransaction();
                    commentTran.replace(R.id.comment_fragfl, dianzanFrag);
                    commentTran.commit();
                }
                melayout.setVisibility(View.GONE);
                break;
            case R.id.commentLayout: //评论数量
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.TITLE, Constants.TopicParams.MODULE1);
                args.put(Constants.IntentParams.ID, Integer.parseInt(ID));
                CommonUtils.startNewActivity(mContext, args, CommentMoreActivity.class);
                break;
            case R.id.userrelayout: //发起人
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mDrivDetailsBean.data.createBy.id);
                CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);
                break;

            case R.id.share: //分享
                openShareWindow = new OpenShareWindow(DrivingDetailsActivity.this);
                openShareWindow.show(DrivingDetailsActivity.this);
                final String title = "看车玩车";
                final String description = mDrivDetailsBean.data.title;
                final String cover = URLUtil.builderImgUrl(mDrivDetailsBean.data.cover, 540, 270);

                openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        switch (position) {

                            case 0:
                                OpenShareUtil.sendWXMoments(mContext, mApp.getMsgApi(), title,
                                        description);
                                break;
                            case 1:
                                OpenShareUtil.sendWX(mContext, mApp.getMsgApi(), title,
                                        description);
                                break;
                            case 2:
                                mWbHandler = OpenShareUtil.shareSina(DrivingDetailsActivity.this,
                                        title, description);
                                break;
                            case 3:
                                ArrayList<String> urls = new ArrayList<String>();
                                urls.add(cover);
                                OpenShareUtil.shareToQzone(mApp.getTencent(),
                                        DrivingDetailsActivity.this, mBaseUIListener, urls, title,
                                        description);
                                break;
                            case 4:
                                OpenShareUtil.shareToQQ(mApp.getTencent(),
                                        DrivingDetailsActivity.this, mBaseUIListener, title, cover,
                                        description);
                                break;

                        }
                        openShareWindow.dismiss();
                    }
                });

                break;

            case R.id.favoriteImg: //帖子点赞
                if (IOUtils.isLogin(mContext)) {
                    mExecLikePresenter.executePraise(ID + "", "thread", IOUtils.getToken(mContext));
                }
                break;

            case R.id.CommentrepayLayout:
                args = new HashMap<String, Serializable>();
                if (IOUtils.isLogin(mContext)) {
                    args.put(Constants.IntentParams.ID, ID + "");
                    CommonUtils.startNewActivity(mContext, args, CommentEditorActivity.class);
                }
                break;

            case R.id.signup:
                if (IOUtils.isLogin(mContext)) {
                    if (typenume == 1) {
                        Map<String, Serializable> arg = new HashMap<String, Serializable>();
                        if (mDrivDetailsBean != null) {
                            if (mDrivDetailsBean.data.isApply == 0) {
                                arg.put(Constants.IntentParams.ID, mDrivDetailsBean.data.id + "");
                                arg.put(Constants.IntentParams.ID2, "1");
                                CommonUtils.startNewActivity(mContext, arg, SignUpActivity.class);
                            } else if (mDrivDetailsBean.data.isApply == 1) {
                                mDrivingDetailsPresenter.getApplyCancel(IOUtils.getToken(mContext),
                                        ID);
                                // CommonUtils.showToast(mContext, "取消报名");
                            } else if (mDrivDetailsBean.data.isApply == 2) {
                                CommonUtils.showToast(mContext, "审核中.....");
                            }
                        }
                    } else if (typenume == 2) {
                        // CommonUtils.showToast(mContext, "活动结束");
                    } else if (typenume == 3) {
                        // CommonUtils.showToast(mContext, "活动中");
                    } else if (typenume == 4) {
                        // CommonUtils.showToast(mContext, "报名截止");
                    } else if (typenume == 5) {
                        // CommonUtils.showToast(mContext, "活动已取消");
                    } else {
                        CommonUtils.showToast(mContext, "系统异常");
                    }
                }
                break;

            case R.id.drivdetails_apply:
                Map<String, Serializable> argss = new HashMap<String, Serializable>();
                if (mDrivDetailsBean != null) {
                    argss.put("id", mDrivDetailsBean.data.id + "");
                }
                if (IOUtils.isLogin(mContext)) {
                    CommonUtils.startNewActivity(mContext, args, ApplyListActivity.class);
                }
                break;

        }
    }

    public View getView(DrivDetailsBean.ShareList shareList) {
        SimpleDraweeView mSimpleDraweeViewHead; //头像
        SimpleDraweeView mSimpleDraweeViewExplain; //封面
        TextView mTitle; //标题
        TextView mTime; //时间
        TextView mLocation; //地址
        TextView mName; //发布人名字
        ImageView mSex;//性别
        ImageView mModel; //模
        ImageView mConveneDa; //达
        SimpleDraweeView mConveneBrandLogo; //车
        View view = View.inflate(mContext, R.layout.drivdetail_share_item, null);
        mSimpleDraweeViewExplain = (SimpleDraweeView) view
                .findViewById(R.id.drivingdetails_explain);
        mSimpleDraweeViewHead = (SimpleDraweeView) view.findViewById(R.id.drdetails_share_head);
        mTitle = (TextView) view.findViewById(R.id.title);
        mTime = (TextView) view.findViewById(R.id.time);
        mLocation = (TextView) view.findViewById(R.id.location);
        mName = (TextView) view.findViewById(R.id.name);
        mModel = (ImageView) view.findViewById(R.id.comment_model_tv);
        mConveneDa = (ImageView) view.findViewById(R.id.drivdetails_convene_da);
        mConveneBrandLogo = (SimpleDraweeView) view
                .findViewById(R.id.drivdetails_convene_brandLogo);
        mSex = (ImageView) view.findViewById(R.id.sex);
        mSimpleDraweeViewHead
                .setImageURI(Uri.parse(URLUtil.builderImgUrl(shareList.avatar, 144, 144)));
        mSimpleDraweeViewExplain
                .setImageURI(Uri.parse(URLUtil.builderImgUrl(shareList.cover, 360, 360)));
        mTitle.setText(shareList.title);
        mLocation.setText(shareList.localAddress);
        mName.setText(shareList.nickname);
        if (shareList.sex == 1) {
            mSex.setImageResource(R.drawable.icon_men);
        } else {
            mSex.setImageResource(R.drawable.icon_women);
        }
        mTime.setText(DateFormatUtil.timeLogic(shareList.createTime));

        if (shareList.isDoyen == 1) {
            mConveneDa.setVisibility(View.VISIBLE);
        } else {
            mConveneDa.setVisibility(View.GONE);
        }

        if (shareList.isModel == 1) {
            mModel.setVisibility(View.VISIBLE);
        } else {
            mModel.setVisibility(View.GONE);
        }

        if (shareList.isMaster == 1) {
            mConveneBrandLogo.setVisibility(View.VISIBLE);
            mConveneBrandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(shareList.logo)));
        } else {
            mConveneBrandLogo.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IOUtils.isLogin(mContext)) {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    if (mDrivDetailsBean != null) {
                        args.put(Constants.IntentParams.ID, mDrivDetailsBean.data.id );
                    }
                    CommonUtils.startNewActivity(mContext, args, ShareSplendidActivity.class);
                }

            }
        });
        return view;
    }

    private CommentListView<CommentModel> mCommentView = new CommentListView<CommentModel>() {
        @Override
        public void showDatas(CommentModel model) {

            showCommentLists(model);
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
    };

    /**
     * 评论
     *
     * @param model
     */
    private void showCommentLists(CommentModel model) {
        mCmtContent.setText("(" + model.count + ")");
        int id = Integer.valueOf(ID).intValue();
        commentFrag = new CommentFrag(model.comments, id, "thread");
        commentTran = commentFm.beginTransaction();
        commentTran.replace(R.id.comment_fragfl, commentFrag);
        commentTran.commit();
    }

    private CommentListView<LikeListModel> mLikeView = new CommentListView<LikeListModel>() {
        @Override
        public void showDatas(LikeListModel likeListModel) {

            showLikeLists(likeListModel);
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
    };

    /**
     * 点赞
     *
     * @param model
     */
    private void showLikeLists(LikeListModel model) {

        mLikeContent.setText("(" + model.pagination.count + ")");
        if (dianzanFrag == null) {
            dianzanFrag = new DianzanFrag(model.list);
        } else {
            dianzanFrag.notifylikeDataSetChange(model.list);
        }
    }

    //帖子点赞
    private CommentListView<LikeBean> mExecLikeView = new CommentListView<LikeBean>() {
        @Override
        public void showDatas(LikeBean strings) {

            if (mDrivDetailsBean != null) {
                if (mDrivDetailsBean.data.isPraise == 1) {
                    mFavoriteImg.setImageResource(R.drawable.btn_heart1);
                    mDrivDetailsBean.data.isPraise = 0;
                } else {
                    CommonUtils.showToast(mContext, "谢谢您的支持");
                    mFavoriteImg.setImageResource(R.drawable.btn_heart2);
                    mDrivDetailsBean.data.isPraise = 1;
                }
                mLikePresenter.loadLikeList(ID, IOUtils.getToken(mContext));
            }

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
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLikePresenter != null) {
            mLikePresenter.detachView();
        }
        if (mCommentPresenter != null) {
            mCommentPresenter.detachView();
        }
        if (mExecLikePresenter != null) {
            mExecLikePresenter.detachView();
        }
        if (mDrivingDetailsPresenter != null) {
            mDrivingDetailsPresenter.detachView();
        }
    }
}
