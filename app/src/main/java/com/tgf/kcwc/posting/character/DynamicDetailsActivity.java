package com.tgf.kcwc.posting.character;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentFrag;
import com.tgf.kcwc.comment.CommentMoreActivity;
import com.tgf.kcwc.comment.DianzanFrag;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.DynamicDetailsBean;
import com.tgf.kcwc.mvp.model.LikeBean;
import com.tgf.kcwc.mvp.model.LikeListModel;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.DynamicDataPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.DynamicDataView;
import com.tgf.kcwc.play.topic.TopicDetailsActivity;
import com.tgf.kcwc.see.BigPhotoPageActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.MyBitmapTransformation;
import com.tgf.kcwc.util.ScreenUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.home.CustomImageView;
import com.tgf.kcwc.view.home.NineGridlayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/26.
 */

public class DynamicDetailsActivity extends BaseActivity implements DynamicDataView {

    private DynamicDataPresenter mDynamicDataPresenter;     //详情
    private CommentListPresenter mCommentPresenter;                           //评论列表
    private CommentListPresenter mLikePresenter;                              //点赞列表
    private CommentListPresenter mExecLikePresenter;                          //帖子点赞

    protected SimpleDraweeView mHeaderImg;    // 发帖人的头像
    protected SimpleDraweeView mGenderImg;    // 发帖人的性别
    protected SimpleDraweeView mMotodetailAvatarIv;    // 我自己的头像
    protected SimpleDraweeView mBelowgenderImg;    // 我自己的性别
    protected TextView mNameTv;//发帖人姓名
    protected ImageView mCommentModel;  //达人
    protected ImageView mCommentPopman;  //模特
    protected SimpleDraweeView mBrandLogo; //品牌
    protected ImageView mAttentionbutt;//关注
    protected TextView mTitletv; //标题
    protected TextView mTimeRecord; //时间
    protected TextView mLocTv;//地址
    protected TextView mDistance;//距离
    private FlowLayout mTagLayout; //标签
    private NineGridlayout nineGrid; //多张图片
    private CustomImageView oneIv; //单张图片
    List<String> imgUrls = new ArrayList<>();

    private LinearLayout mRepayLayout;//评论
    private ImageView mFavoriteImg;                                //帖子点赞
    private ImageView share; //分享
    private RelativeLayout mCommentLayout;                              //评论数字布局
    protected TextView mUnReadNums;                                 //评论数量


    //评论点赞
    protected RelativeLayout mCommentBtnLayout;
    protected TextView mCmtTitle;
    protected ImageView mBtmLine1;
    protected TextView mCmtContent;

    protected RelativeLayout mLikeBtnLayout;
    protected ImageView mBtmLine2;
    protected TextView mLikeTitle;
    protected TextView mLikeContent;

    protected Fragment commentFrag; //评论点赞碎片
    protected FragmentManager commentFm;
    protected FragmentTransaction commentTran;                                 //评论
    protected DianzanFrag dianzanFrag;                                 //点赞


    private OpenShareWindow openShareWindow;                              //分享弹框
    private WbShareHandler mWbHandler;
    private KPlayCarApp mApp;

    protected int ID = -1; //ID
    protected DynamicDetailsBean mDynamicDetailsBean;

    @Override
    protected void setUpViews() {
        ID = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        mApp = (KPlayCarApp) getApplication();
        mHeaderImg = (SimpleDraweeView) findViewById(R.id.img);
        mGenderImg = (SimpleDraweeView) findViewById(R.id.genderImg);
        mMotodetailAvatarIv = (SimpleDraweeView) findViewById(R.id.motodetail_avatar_iv);
        mBelowgenderImg = (SimpleDraweeView) findViewById(R.id.belowgenderImg);
        mNameTv = (TextView) findViewById(R.id.nametv);
        mCommentModel = (ImageView) findViewById(R.id.comment_model_tv);
        mCommentPopman = (ImageView) findViewById(R.id.comment_popman_tv);
        mBrandLogo = (SimpleDraweeView) findViewById(R.id.brandLogo);
        mAttentionbutt = (ImageView) findViewById(R.id.attentionbutt);
        mLocTv = (TextView) findViewById(R.id.locTv);
        mDistance = (TextView) findViewById(R.id.distance);
        mTagLayout = (FlowLayout) findViewById(R.id.tagLists);
        mTimeRecord = (TextView) findViewById(R.id.timeRecord);
        nineGrid = (NineGridlayout) findViewById(R.id.homesay_ninegrid);
        oneIv = (CustomImageView) findViewById(R.id.homesay_oneiv);
        mTitletv = (TextView) findViewById(R.id.homesay_titletv);

        mRepayLayout = (LinearLayout) findViewById(R.id.repayLayout);
        mFavoriteImg = (ImageView) findViewById(R.id.favoriteImg);
        share = (ImageView) findViewById(R.id.shareImg);
        mCommentLayout = (RelativeLayout) findViewById(R.id.commentLayout);
        mUnReadNums = (TextView) findViewById(R.id.comment_numbers);

        mCommentBtnLayout = (RelativeLayout) findViewById(R.id.commentBtnLayout);
        mLikeBtnLayout = (RelativeLayout) findViewById(R.id.likeBtnLayout);

        mLikeTitle = (TextView) findViewById(R.id.likeTitle);
        mLikeContent = (TextView) findViewById(R.id.likeContent);
        mBtmLine1 = (ImageView) findViewById(R.id.btmLine1);
        mBtmLine2 = (ImageView) findViewById(R.id.btmLine2);
        mCmtContent = (TextView) findViewById(R.id.cmtContent);
        mCmtTitle = (TextView) findViewById(R.id.cmtTitle);

        mCommentBtnLayout.setOnClickListener(this);
        mLikeBtnLayout.setOnClickListener(this);
        mRepayLayout.setOnClickListener(this);
        mFavoriteImg.setOnClickListener(this);
        share.setOnClickListener(this);
        mCommentLayout.setOnClickListener(this);
        mAttentionbutt.setOnClickListener(this);
        mHeaderImg.setOnClickListener(this);

        commentFm = getSupportFragmentManager();

        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(mCommentView);

        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mLikeView);

        mExecLikePresenter = new CommentListPresenter();
        mExecLikePresenter.attachView(mExecLikeView);

        mDynamicDataPresenter = new DynamicDataPresenter();
        mDynamicDataPresenter.attachView(this);
        gainData();
    }

    public void gainData() {
        if (ID == -1) {
            CommonUtils.showToast(mContext, "系统异常");
        } else {
            mDynamicDataPresenter.detailMood(IOUtils.getToken(mContext), ID + "");
            mCommentPresenter.loadCommentList("thread", ID + "", "moto");
            mLikePresenter.loadLikeList(ID + "", IOUtils.getToken(mContext));
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.dynamicdetails);
        function.setImageResource(R.drawable.btn_more);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicdetails);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Map<String, Serializable> args = null;
        switch (view.getId()) {
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
                break;

            case R.id.commentLayout: //评论数量
            case R.id.repayLayout: //评论布局
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.TITLE, Constants.TopicParams.MODULE1);
                args.put(Constants.IntentParams.ID, ID);
                CommonUtils.startNewActivity(mContext, args, CommentMoreActivity.class);
                break;
            case R.id.img: //头像
                if (mDynamicDetailsBean != null) {
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, mDynamicDetailsBean.createUser.id);
                    CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);
                }
                break;

            case R.id.favoriteImg: //帖子点赞
                if (IOUtils.isLogin(mContext)) {
                    mExecLikePresenter.executePraise(ID + "", "thread", IOUtils.getToken(mContext));
                }
                break;
            case R.id.attentionbutt: //关注
                if (IOUtils.isLogin(mContext)) {
                    if (mDynamicDetailsBean != null) {
                        if (mDynamicDetailsBean.isFollow == 0) {
                            mDynamicDataPresenter.getFollowAdd(IOUtils.getToken(mContext), mDynamicDetailsBean.createUser.id + "");
                        } else {
                            mDynamicDataPresenter.getFollowCancel(IOUtils.getToken(mContext), mDynamicDetailsBean.createUser.id + "");
                        }
                    }
                }
                break;
            case R.id.shareImg: //分享
                String description = "";
                String cover = "";
                if (mDynamicDetailsBean != null) {
                    if (mDynamicDetailsBean.mood.content.length() > 10) {
                        description = mDynamicDetailsBean.mood.content.substring(0, 10);
                    } else {
                        description = mDynamicDetailsBean.mood.content;
                    }
                    if (!TextUtil.isEmpty(mDynamicDetailsBean.mood.image) && imgUrls.size() > 0) {
                        cover = URLUtil.builderImgUrl(imgUrls.get(0), 540, 270);
                    } else {
                        cover = "";
                    }
                    share(description, cover);
                }
                break;

        }
    }


    public void share(String descri, String imag) {
        openShareWindow = new OpenShareWindow(DynamicDetailsActivity.this);
        openShareWindow.show(DynamicDetailsActivity.this);
        final String title = "看车玩车";
        final String description = descri;
        final String cover = imag;

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
                        mWbHandler = OpenShareUtil.shareSina(DynamicDetailsActivity.this,
                                title, description);
                        break;
                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(mApp.getTencent(),
                                DynamicDetailsActivity.this, mBaseUIListener, urls, title,
                                description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(mApp.getTencent(),
                                DynamicDetailsActivity.this, mBaseUIListener, title, cover,
                                description);
                        break;

                }
                openShareWindow.dismiss();
            }
        });

    }

    @Override
    public void showData(DynamicDetailsBean dynamicDetailsBean) {
        mDynamicDetailsBean = dynamicDetailsBean;
        //用户头像
        mHeaderImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(dynamicDetailsBean.createUser.avatar, 144, 144)));
        mNameTv.setText(dynamicDetailsBean.createUser.nickname);
        //性别
        if (dynamicDetailsBean.createUser.sex == 1) {
            mGenderImg.setImageResource(R.drawable.icon_men);
        } else {
            mGenderImg.setImageResource(R.drawable.icon_women);
        }

        //达人
        if (dynamicDetailsBean.createUser.isDoyen == 1) {
            mCommentModel.setVisibility(View.VISIBLE);
        } else {
            mCommentModel.setVisibility(View.GONE);
        }
        //模特
        if (dynamicDetailsBean.createUser.isModel == 1) {
            mCommentPopman.setVisibility(View.VISIBLE);
        } else {
            mCommentPopman.setVisibility(View.GONE);
        }
        //品牌
        if (dynamicDetailsBean.createUser.isMaster == 1) {
            mBrandLogo.setVisibility(View.VISIBLE);
            mBrandLogo.setImageURI(
                    Uri.parse(URLUtil.builderImgUrl(dynamicDetailsBean.createUser.masterBrand, 144, 144)));
        } else {
            mBrandLogo.setVisibility(View.GONE);
        }
        mTimeRecord.setText(DateFormatUtil.timeLogi(dynamicDetailsBean.createTime));
        mLocTv.setText(dynamicDetailsBean.localAddress);
        mDistance.setText(dynamicDetailsBean.distance + "km");
        mTitletv.setText(dynamicDetailsBean.mood.content);
        //图片处理
        String image = dynamicDetailsBean.mood.image;
        imgUrls = Arrays.asList(image.split(","));
        final ArrayList<String> dataimag = new ArrayList();
        dataimag.addAll(imgUrls);
        if ((imgUrls == null) || imgUrls.size() == 0) {
            nineGrid.setVisibility(View.GONE);
            oneIv.setVisibility(View.GONE);
        } else if (imgUrls.size() == 1) {
            oneIv.setVisibility(View.VISIBLE);
            nineGrid.setVisibility(View.GONE);
            handlerOneImage(oneIv, URLUtil.builderImgUrl(imgUrls.get(0)));
            oneIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toInent = new Intent(mContext, BigPhotoPageActivity.class);
                    toInent.putStringArrayListExtra(Constants.KEY_IMGS, dataimag);
                    toInent.putExtra(Constants.IntentParams.INDEX, 0);
                    mContext.startActivity(toInent);
                }
            });
        } else {
            nineGrid.setVisibility(View.VISIBLE);
            oneIv.setVisibility(View.GONE);
            nineGrid.setImagesData(imgUrls, 100);
            nineGrid.setOnItemClickListerer(new NineGridlayout.OnItemClickListerer() {
                @Override
                public void onClick(int position2) {
                    Intent toInent = new Intent(mContext, BigPhotoPageActivity.class);
                    toInent.putStringArrayListExtra(Constants.KEY_IMGS, dataimag);
                    toInent.putExtra(Constants.IntentParams.INDEX, position2);
                    mContext.startActivity(toInent);
                }
            });
        }
        addTagItems(dynamicDetailsBean.topic);
        Account account = IOUtils.getAccount(mContext);
        mMotodetailAvatarIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(account.userInfo.avatar)));
        if (account.userInfo.sex == 1) {
            mBelowgenderImg.setImageResource(R.drawable.icon_men);
        } else {
            mBelowgenderImg.setImageResource(R.drawable.icon_women);
        }

        if (dynamicDetailsBean.isPraise == 0) {
            mFavoriteImg.setImageResource(R.drawable.btn_heart1);
        } else {
            mFavoriteImg.setImageResource(R.drawable.btn_heart2);
        }

        if (dynamicDetailsBean.isFollow == -1) {
            mAttentionbutt.setVisibility(View.GONE);
        } else {
            mAttentionbutt.setVisibility(View.VISIBLE);
            if (dynamicDetailsBean.isFollow == 1) {
                mAttentionbutt.setImageResource(R.drawable.btn_yiguanzhu);
            } else {
                mAttentionbutt.setImageResource(R.drawable.btn_kuangguanzhu);
            }
        }

    }

    @Override
    public void showFollowAddSuccess(BaseBean baseBean) {
        gainData();
    }

    @Override
    public void showCancelSuccess(BaseBean baseBean) {
        gainData();
    }

    @Override
    public void detailsDataFeated(String msg) {

    }

    /**
     * 处理图片
     *
     * @param ivOne
     * @param imageUrl
     */
    private void handlerOneImage(final CustomImageView ivOne, final String imageUrl) {

//        viewHolder.ivOne.setImageUrl(image.getUrl());
//        Glide.with(context).load(image.getUrl()).into(viewHolder.ivOne);
        Glide.with(mContext)
                .load(imageUrl)
                .asBitmap()
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        cacluateSize(resource.getWidth(), resource.getHeight());
                        ViewGroup.LayoutParams layoutparams = ivOne.getLayoutParams();
                        layoutparams.height = (int) (finalHeight + 0.5D);
                        layoutparams.width = (int) (finalWidth + 0.5D);
                        ivOne.setLayoutParams(layoutparams);
                        ivOne.setClickable(true);
                        ivOne.setScaleType(ImageView.ScaleType.MATRIX);
                        Glide.with(mContext)
                                .load(imageUrl)
                                .transform(new MyBitmapTransformation(mContext))
                                .override(layoutparams.width, layoutparams.height)
                                .into(ivOne);
                    }

                    private double finalWidth;
                    private double finalHeight;

                    private void cacluateSize(int imageWidth, int imageHeight) {
                        ScreenUtil screentools = ScreenUtil.getInstance(mContext);
                        int maxSize = DensityUtils.dip2px(mContext, 230);
                        int minSize = DensityUtils.dip2px(mContext, 174);
                        if (imageWidth > imageHeight) {
                            finalHeight = imageHeight * maxSize / imageWidth;
                            finalWidth = maxSize;
                            if (finalWidth / finalHeight >= 3.5) {
                                finalWidth = screentools.getScreenWidth() - DensityUtils.dip2px(mContext, 30);
                                finalHeight = finalWidth * 0.32;
                            } else if (finalHeight < minSize) {
                                finalHeight = minSize;
                            }

                        } else {
                            finalWidth = imageWidth * maxSize / imageHeight;
                            finalHeight = maxSize;
                            if (finalWidth < minSize) {
                                finalWidth = minSize;
                            }
                        }


                    }

                });


    }

    /**
     * 添加标签
     *
     * @param datas
     */
    private void addTagItems(List<Topic> datas) {

        mTagLayout.removeAllViews();
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            final Topic topic = datas.get(i);
            View v = LayoutInflater.from(mContext).inflate(R.layout.text_tag_item5, mTagLayout,
                    false);
            v.setTag(topic.id);
            mTagLayout.addView(v);
            TextView tv = (TextView) v.findViewById(R.id.name);
            tv.setText(topic.title + "");
            tv.setTextColor(mRes.getColor(R.color.text_color21));
            tv.setTag(topic.id);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, topic.id + "");
                    CommonUtils.startNewActivity(mContext, args, TopicDetailsActivity.class);

                }
            });
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public Context getContext() {
        return mContext;
    }


    /**
     * 评论列表
     */
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

    private void showCommentLists(CommentModel model) {
        mCmtContent.setText("(" + model.count + ")");
        int id = Integer.valueOf(ID).intValue();
        commentFrag = new CommentFrag(model.comments, id, "thread");
        commentTran = commentFm.beginTransaction();
        commentTran.replace(R.id.comment_fragfl, commentFrag);
        commentTran.commit();
    }

    /**
     * 点赞列表
     */
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

    private void showLikeLists(LikeListModel model) {

        mLikeContent.setText("(" + model.pagination.count + ")");
        if (dianzanFrag == null) {
            dianzanFrag = new DianzanFrag(model.list);
        } else {
            dianzanFrag.notifylikeDataSetChange(model.list);
        }

    }

    /**
     * 点赞
     */
    private CommentListView<LikeBean> mExecLikeView = new CommentListView<LikeBean>() {
        @Override
        public void showDatas(LikeBean likeBean) {

            if (mDynamicDetailsBean != null) {
                if (mDynamicDetailsBean.isPraise == 1) {
                    mFavoriteImg.setImageResource(R.drawable.btn_heart1);
                    mDynamicDetailsBean.isPraise = 0;
                } else {
                    CommonUtils.showToast(mContext, "谢谢您的支持");
                    mFavoriteImg.setImageResource(R.drawable.btn_heart2);
                    mDynamicDetailsBean.isPraise = 1;
                }
                mLikePresenter.loadLikeList(ID + "", IOUtils.getToken(mContext));
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
}
