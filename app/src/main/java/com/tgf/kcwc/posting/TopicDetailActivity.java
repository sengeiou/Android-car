package com.tgf.kcwc.posting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.CommentEditActivity;
import com.tgf.kcwc.app.ReplyEditActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.comment.CommentMoreActivity;
import com.tgf.kcwc.comment.ReplyMoreActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.me.message.MessageActivity;
import com.tgf.kcwc.me.setting.FanKuiActivity;
import com.tgf.kcwc.mvp.model.Comment;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.model.LikeListModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.model.TopicDetailModel;
import com.tgf.kcwc.mvp.presenter.AttentionDataPresenter;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.FavorPresenter;
import com.tgf.kcwc.mvp.presenter.TopicDataPresenter;
import com.tgf.kcwc.mvp.presenter.TopicOperatorPresenter;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.FavoriteView;
import com.tgf.kcwc.mvp.view.TopicDataView;
import com.tgf.kcwc.mvp.view.TopicOperatorView;
import com.tgf.kcwc.play.topic.TopicDetailsActivity;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.see.PhotoBrowserActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.MultiImageView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.link.Link;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;
import com.tgf.kcwc.view.richeditor.MixedTextImageLayout;
import com.tgf.kcwc.view.richeditor.SEditorData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/23 15:57
 * E-mail：fishloveqin@gmail.com
 * 帖子详情
 */

public class TopicDetailActivity extends BaseActivity implements TopicDataView<TopicDetailModel> {

    protected NestFullListView mCommentList;
    protected TextView mTopicTitle;
    protected SimpleDraweeView mHeaderImg;
    protected SimpleDraweeView mGenderImg;
    protected TextView mNameTv;
    protected ImageView mCommentModel;
    protected ImageView mCommentPopman;
    protected SimpleDraweeView mBrandLogo;
    protected TextView mTimeRecord;
    protected TextView mCmtNums;
    protected TextView mUnReadNums;
    protected SimpleDraweeView mGroupIcon;
    protected SimpleDraweeView mGroupLevelIcon;
    protected TextView mGroupTitle;
    protected TextView mGroupDesc;
    protected RelativeLayout mGroupLayout;
    protected TextView mCmtTitle;
    protected TextView mCmtContent;
    protected ImageView mBtmLine1;
    protected RelativeLayout mCommentBtnLayout;
    protected TextView mLikeTitle;
    protected TextView mLikeContent;
    protected ImageView mBtmLine2;
    protected RelativeLayout mLikeBtnLayout;
    //评论
    private NestFullListViewAdapter mCommentsadapter;
    //回复列表
    private NestFullListViewAdapter mReplyadapter;
    private RelativeLayout mCommentLayout;
    private ImageView mFavoriteImg, mShareImg;
    private ImageView mDigestImg;
    private TopicDataPresenter mPresenter, mDeleteTopicPresenter;

    private TopicOperatorPresenter mOperatorPresenter;
    private TextView mLocTv, mDistanceTv;

    private FlowLayout mTagLayout;

    private ListView mHonorList;

    private ListView mLikeList;
    private CommentListPresenter mCommentPresenter;
    private CommentListPresenter mLikePresenter;
    private CommentListPresenter mExecLikePresenter;     //点赞
    private AttentionDataPresenter mAttentionPresenter;
    private AttentionDataPresenter mCancelPresenter;
    private LinearLayout mRepLayout;
    private MixedTextImageLayout mRichLayout;

    private RelativeLayout mHeaderLayout;
    private ArrayList<MorePopupwindowBean> dataList = new ArrayList<>();
    private FavorPresenter mFavorPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getStringExtra(Constants.IntentParams.ID);

        super.setContentView(R.layout.activity_topic_detail_layout);
        openShareWindow = new OpenShareWindow(this);
    }

    private OpenShareWindow openShareWindow;
    private String mId;

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {

        backEvent(back);
        text.setText("帖子详情");

        function.setImageResource(R.drawable.btn_hide_more_selector);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (morePopupWindow != null) {
                    morePopupWindow.showPopupWindow(function);
                }
            }
        });
    }


    @Override
    protected void setUpViews() {
        mPresenter = new TopicDataPresenter();
        mPresenter.attachView(this);
        mFavorPresenter = new FavorPresenter();
        mFavorPresenter.attachView(mFavoriteView);
        openShareWindow = new OpenShareWindow(this);
        mDeleteTopicPresenter = new TopicDataPresenter();
        mDeleteTopicPresenter.attachView(mDeleteTopicDataView);


        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(mCommentView);

        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mLikeView);
        mExecLikePresenter = new CommentListPresenter();

        mExecLikePresenter.attachView(mExecLikeView);
        mAttentionPresenter = new AttentionDataPresenter();
        mAttentionPresenter.attachView(mAttentionView);
        mCancelPresenter = new AttentionDataPresenter();
        mCancelPresenter.attachView(mCancelAttentionView);

        mOperatorPresenter = new TopicOperatorPresenter();
        mOperatorPresenter.attachView(mTopicOperatorView);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getTopicDetails(mId, mGlobalContext.getLongitude(),
                mGlobalContext.getLattitude(), IOUtils.getToken(mContext));

        if (type == 1 || type == 2||type==3) {
//            mCommentPresenter.loadCommentList(Constants.TopicParams.MODULE1, mId + "",
//                    Constants.TopicParams.VEHICLE_TYPE);
            type = 0;
        } else {
            mCommentPresenter.loadCommentList(Constants.TopicParams.MODULE1, mId + "",
                    Constants.TopicParams.VEHICLE_TYPE);
            mLikePresenter.loadLikeList(mId + "", IOUtils.getToken(mContext));
        }


    }


    private TopicOperatorView<DataItem> mTopicOperatorView = new TopicOperatorView<DataItem>() {
        @Override
        public void showData(DataItem item) {

            if (Constants.TopicParams.CAN_BE_REPORT == item.count) {

                Map<String, Serializable> params = new HashMap<String, Serializable>();

                params.put(Constants.IntentParams.ID, mId);
                params.put(Constants.IntentParams.TITLE, "@" + mModel.user.nick + ": " + mModel.title);
                CommonUtils.startNewActivity(mContext, params, TopicReportActivity.class);
            } else {
                CommonUtils.showToast(mContext, "您已举报过该帖!");
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

    private void initView() {
        mRichLayout = (MixedTextImageLayout) findViewById(R.id.richContentLayout);
        mCommentList = (NestFullListView) findViewById(R.id.commentList);
        mCommentLayout = (RelativeLayout) findViewById(R.id.commentLayout);
        mFavoriteImg = (ImageView) findViewById(R.id.favoriteImg);
        mShareImg = (ImageView) findViewById(R.id.shareImg);
        mCommentLayout.setOnClickListener(this);
        mFavoriteImg.setOnClickListener(this);
        mShareImg.setOnClickListener(this);
        mRepLayout = (LinearLayout) findViewById(R.id.repayLayout);
        mRepLayout.setOnClickListener(this);
        mHeaderLayout = (RelativeLayout) findViewById(R.id.headerImgLayout);
        mHeaderLayout.setOnClickListener(this);
        mTopicTitle = (TextView) findViewById(R.id.topicTitle);
        mHeaderImg = (SimpleDraweeView) findViewById(R.id.img);
        mGenderImg = (SimpleDraweeView) findViewById(R.id.genderImg);
        mNameTv = (TextView) findViewById(R.id.nametv);
        mCommentModel = (ImageView) findViewById(R.id.comment_model_tv);
        mCommentPopman = (ImageView) findViewById(R.id.comment_popman_tv);
        mBrandLogo = (SimpleDraweeView) findViewById(R.id.brandLogo);
        mTimeRecord = (TextView) findViewById(R.id.timeRecord);
        mCmtNums = (TextView) findViewById(R.id.content);
        mUnReadNums = (TextView) findViewById(R.id.comment_numbers);
        findViewById(R.id.replayImg).setOnClickListener(this);
        mUnReadNums.setOnClickListener(this);
        mDigestImg = (ImageView) findViewById(R.id.digestImg);
        mLocTv = (TextView) findViewById(R.id.locTv);
        mDistanceTv = (TextView) findViewById(R.id.distanceTv);
        mTagLayout = (FlowLayout) findViewById(R.id.tagLists);
        mHonorList = (ListView) findViewById(R.id.honorList);
        mGroupIcon = (SimpleDraweeView) findViewById(R.id.groupIcon);
        mGroupLevelIcon = (SimpleDraweeView) findViewById(R.id.groupLevelIcon);
        mGroupTitle = (TextView) findViewById(R.id.groupTitle);
        mGroupDesc = (TextView) findViewById(R.id.groupDesc);
        mGroupLayout = (RelativeLayout) findViewById(R.id.groupLayout);
        mCmtTitle = (TextView) findViewById(R.id.cmtTitle);
        mCmtContent = (TextView) findViewById(R.id.cmtContent);
        mBtmLine1 = (ImageView) findViewById(R.id.btmLine1);
        mCommentBtnLayout = (RelativeLayout) findViewById(R.id.commentBtnLayout);
        mCommentBtnLayout.setOnClickListener(this);
        mLikeTitle = (TextView) findViewById(R.id.likeTitle);
        mLikeContent = (TextView) findViewById(R.id.likeContent);
        mBtmLine2 = (ImageView) findViewById(R.id.btmLine2);
        mLikeBtnLayout = (RelativeLayout) findViewById(R.id.likeBtnLayout);
        mLikeBtnLayout.setOnClickListener(this);
        mLikeList = (ListView) findViewById(R.id.likeList);

    }

    private int isFav = 0;
    private FavoriteView mFavoriteView = new FavoriteView() {
        @Override
        public void addFavoriteSuccess(Object data) {
            isFav = 1;
            mPresenter.getTopicDetails(mId, mGlobalContext.getLongitude(),
                    mGlobalContext.getLattitude(), IOUtils.getToken(mContext));
            CommonUtils.showToast(getContext(), "收藏成功");
        }

        @Override
        public void cancelFavorite(Object data) {
            isFav = 0;
            mPresenter.getTopicDetails(mId, mGlobalContext.getLongitude(),
                    mGlobalContext.getLattitude(), IOUtils.getToken(mContext));
            CommonUtils.showToast(getContext(), "取消收藏");
        }

        @Override
        public void setLoadingIndicator(boolean active) {

            showLoadingIndicator(active);
        }

        @Override
        public void showLoadingTasksError() {

            dismissLoadingDialog();
        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    private TopicDataView mDeleteTopicDataView = new TopicDataView() {
        @Override
        public void showData(Object o) {

            CommonUtils.showToast(mContext, "删除成功!");
            finish();
        }

        @Override
        public void setLoadingIndicator(boolean active) {

            showLoadingIndicator(active);
        }

        @Override
        public void showLoadingTasksError() {

            dismissLoadingDialog();
        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    private void favorite() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("model", Constants.FavoriteTypes.WORDS);
        params.put("source_id", mModel.id + "");
        params.put("title", mModel.title);
        params.put("type", "car");
        params.put("token", IOUtils.getToken(mContext));
        if (isFav == 1) {
            mFavorPresenter.cancelFavoriteData(params);
        } else {
            mFavorPresenter.addFavoriteData(params);
        }
    }

    private void deleteTopic() {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("thread_id", mId);
        params.put("token", IOUtils.getToken(mContext));
        mDeleteTopicPresenter.deleteTopic(params);
    }


    private void showRichContent(TopicDetailModel topicDetailModel) {

        mRichLayout.removeAllViews();//清空所有已添加的View

        if (topicDetailModel.describe == null) {
            return;
        }
        List<SEditorData> dataList = topicDetailModel.describe.mEditorDatas;
        for (SEditorData d : dataList) {

            String inputStr = d.getInputStr();
            String path = d.getImageUrl();
            if (!TextUtils.isEmpty(inputStr)) {
                mRichLayout.appendTextView(inputStr);
            }

            if (!TextUtils.isEmpty(path)) {

                mRichLayout.appendImageView(this, URLUtil.builderImgUrl750(path));
            }

        }

    }

    //点赞
    private CommentListView<Object> mExecLikeView = new CommentListView<Object>() {
        @Override
        public void showDatas(Object obj) {
            //
            //                                                                        CommonUtils.showToast(
            //                                                                            mContext, "谢谢您的支持");
            //                                                                        mFavoriteImg
            //                                                                            .setEnabled(false);
            //                                                                        mFavoriteImg
            //                                                                            .setImageResource(
            //                                                                                R.drawable.big_love_icon);
            mPresenter.getTopicDetails(
                    mId,
                    mGlobalContext
                            .getLongitude(),
                    mGlobalContext
                            .getLattitude(),
                    IOUtils.getToken(
                            mContext));

            mCommentPresenter
                    .loadCommentList(
                            Constants.TopicParams.MODULE1,
                            mId + "",
                            Constants.TopicParams.VEHICLE_TYPE);

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

    private CommentListView<LikeListModel> mLikeView = new CommentListView<LikeListModel>() {
        @Override
        public void showDatas(LikeListModel likeListModel) {

            showLikeLists(
                    likeListModel);
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

    //关注

    AttentionView<Object> mAttentionView = new AttentionView<Object>() {
        @Override
        public void showAddAttention(Object object) {

            CommonUtils.showToast(
                    mContext, "您已添加关注");
            mLikePresenter.loadLikeList(
                    mId + "",
                    IOUtils.getToken(
                            mContext));
        }

        @Override
        public void showCancelAttention(Object object) {

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

    //取消关注

    AttentionView<Object> mCancelAttentionView = new AttentionView<Object>() {
        @Override
        public void showAddAttention(Object object) {

        }

        @Override
        public void showCancelAttention(Object object) {
            CommonUtils.showToast(
                    mContext, "您已取消关注");
            mLikePresenter.loadLikeList(
                    mId + "",
                    IOUtils.getToken(
                            mContext));
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

        mCommentList.setVisibility(View.GONE);
        mLikeList.setVisibility(View.VISIBLE);
        mLikeContent.setText("(" + model.pagination.count + ")");
        WrapAdapter<LikeListModel.LikeBean> adapter = new WrapAdapter<LikeListModel.LikeBean>(
                mContext, R.layout.user_like_list_item, model.list) {

            protected TextView descTv;
            protected SimpleDraweeView brandLogo;
            protected ImageView commentPopmanTv;
            protected ImageView commentModelTv;
            protected TextView nametv;
            protected RelativeLayout headerImgLayout;
            protected SimpleDraweeView genderImg;
            protected SimpleDraweeView img;
            Button mAttBtn;

            @Override
            public void convert(ViewHolder helper, final LikeListModel.LikeBean item) {

                img = helper.getView(R.id.img);
                genderImg = helper.getView(R.id.genderImg);
                headerImgLayout = helper.getView(R.id.headerImgLayout);
                nametv = helper.getView(R.id.nametv);
                commentModelTv = helper.getView(R.id.comment_model_tv);
                commentPopmanTv = helper.getView(R.id.comment_popman_tv);
                brandLogo = helper.getView(R.id.brandLogo);
                mAttBtn = helper.getView(R.id.attentionBtn);
                descTv = helper.getView(R.id.desc);
                nametv.setText(item.user.nick);
                //达人
                if (item.user.isExpert == 1) {
                    commentPopmanTv.setVisibility(View.VISIBLE);
                } else {
                    commentPopmanTv.setVisibility(View.GONE);
                }
                //模特
                if (item.user.is_model == 1) {
                    commentModelTv.setVisibility(View.VISIBLE);
                } else {
                    commentModelTv.setVisibility(View.GONE);
                }
                descTv.setText(item.user.desc);

                //性别
                if (item.user.gender == 1) {
                    genderImg.setImageResource(R.drawable.icon_men);
                } else {
                    genderImg.setImageResource(R.drawable.icon_women);
                }

                System.out.println(item.user.is_master == 1);
                if (item.user.is_master == 1) {
                    brandLogo.setVisibility(View.VISIBLE);
                    brandLogo.setImageURI(
                            Uri.parse(URLUtil.builderImgUrl(item.user.masterLogo + "", 144, 144)));
                } else {
                    brandLogo.setVisibility(View.GONE);
                }

                //用户头像
                img.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.user.avatar, 144, 144)));

                final int isFlw = item.user.isFlw;
                ImageView attentionStatusImg = helper.getView(R.id.attentionStatusImg);

                //isFlw ,0未关注 1已关注 2，互相关注
                if (isFlw == 0) {

                    attentionStatusImg.setImageResource(R.drawable.attention_status_1);
                } else if (isFlw == 1) {
                    attentionStatusImg.setImageResource(R.drawable.btn_attention);
                } else if (isFlw == 2) {
                    attentionStatusImg.setImageResource(R.drawable.attention_status_2);
                }

                attentionStatusImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isFlw == 1 || isFlw == 2) {
                            mCancelPresenter.cancelAttention(item.user.userId + "",
                                    IOUtils.getToken(mContext));
                        } else {
                            mAttentionPresenter.execAttention(item.user.userId + "",
                                    IOUtils.getToken(mContext));
                        }

                    }
                });
            }
        };
        mLikeList.setAdapter(adapter);
        mLikeList.setOnItemClickListener(onItemClickListener);
        ViewUtil.setListViewHeightBasedOnChildren2(mLikeList);
    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LikeListModel.LikeBean likeBean = (LikeListModel.LikeBean) parent.getAdapter().getItem(position);

            jumpToUserHome(likeBean.user.userId, 2);

        }
    };

    private void jumpToUserHome(int userId, int type) {
        this.type = type;
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentParams.ID, userId);
        intent.setClass(mContext, UserPageActivity.class);
        startActivity(intent);
    }

    private int userId;
    private WbShareHandler mWbHandler;

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.replayImg:
            case R.id.comment_numbers:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.TITLE, Constants.TopicParams.MODULE1);
                args.put(Constants.IntentParams.ID, Integer.parseInt(mId));
                CommonUtils.startNewActivity(mContext, args, CommentMoreActivity.class);
                break;

            case R.id.repayLayout:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mId + "");
                CommonUtils.startNewActivity(mContext, args, CommentEditorActivity.class);
                break;
            case R.id.favoriteImg:
                mExecLikePresenter.executePraise(mId + "", Constants.TopicParams.MODULE1,
                        IOUtils.getToken(mContext));
                break;
            case R.id.shareImg:

                if (mModel == null) {
                    CommonUtils.showToast(mContext, "数据正在加载中...");
                    return;
                }
                final String title = mModel.title;
                final StringBuilder descriptionBuilder = new StringBuilder();
                final ArrayList<String> urls = new ArrayList<String>();
                for (SEditorData data : mModel.describe.mEditorDatas) {
                    String url = data.getImageUrl();
                    if (!TextUtils.isEmpty(url)) {
                        urls.add(URLUtil.builderImgUrl(url, 360, 360));
                    }
                    String txt = data.getInputStr();
                    if (!TextUtils.isEmpty(txt)) {
                        descriptionBuilder.append(txt);
                    }
                }
                final String cover = Constants.ImageUrls.IMG_DEF_URL;

                openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        String description = descriptionBuilder.toString();
                        int size = description.length();
                        if (size == 0) {
                            description = mModel.address;
                        }
                        if (size >= 50) {
                            description = description.substring(0, 50);
                        }

                        switch (position) {

                            case 0:
                                OpenShareUtil.sendWXMoments(mContext, mGlobalContext.getMsgApi(),
                                        title, description);
                                break;
                            case 1:
                                OpenShareUtil.sendWX(mContext, mGlobalContext.getMsgApi(), title,
                                        description);
                                break;
                            case 2:
                                mWbHandler = OpenShareUtil.shareSina(TopicDetailActivity.this,
                                        title, description);
                                break;
                            case 3:
                                OpenShareUtil.shareToQzone(mGlobalContext.getTencent(),
                                        TopicDetailActivity.this, mBaseUIListener, urls, title,
                                        description);
                                break;
                            case 4:
                                String url = Constants.ImageUrls.IMG_DEF_URL;
                                if (urls.size() > 0) {
                                    url = urls.get(0);
                                }
                                OpenShareUtil.shareToQQ(mGlobalContext.getTencent(),
                                        TopicDetailActivity.this, mBaseUIListener, title, url,
                                        description);
                                break;

                        }
                    }
                });
                openShareWindow.show(this);

                break;

            case R.id.commentBtnLayout:

                mCmtTitle.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                mBtmLine1.setVisibility(View.VISIBLE);

                mLikeTitle.setTextColor(mRes.getColor(R.color.text_more));
                mBtmLine2.setVisibility(View.INVISIBLE);
                mCommentPresenter.loadCommentList(Constants.TopicParams.MODULE1, mId + "",
                        Constants.TopicParams.VEHICLE_TYPE);
                break;

            case R.id.likeBtnLayout:
                mLikeTitle.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                mBtmLine2.setVisibility(View.VISIBLE);

                mCmtTitle.setTextColor(mRes.getColor(R.color.text_more));
                mBtmLine1.setVisibility(View.INVISIBLE);
                mLikePresenter.loadLikeList(mId + "", IOUtils.getToken(mContext));
                break;
            case R.id.headerImgLayout:

                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.ID, userId);
                intent.setClass(mContext, UserPageActivity.class);
                startActivity(intent);

                break;

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mWbHandler != null) {
            mWbHandler.doResultIntent(intent, new SinaWBCallback(mContext));
        }
    }

    private void initTitleData(TopicDetailModel topicDetailModel) {

        userId = topicDetailModel.user.userId;
        mTopicTitle.setText(topicDetailModel.title);

        //精华
        if (topicDetailModel.isDigest == 1) {
            mDigestImg.setVisibility(View.VISIBLE);
        } else {
            mDigestImg.setVisibility(View.GONE);
        }
        mNameTv.setText(topicDetailModel.user.nick);
        //达人
        if (topicDetailModel.user.isExpert == 1) {
            mCommentPopman.setVisibility(View.VISIBLE);
        } else {
            mCommentPopman.setVisibility(View.GONE);
        }
        //模特
        if (topicDetailModel.user.is_model == 1) {
            mCommentModel.setVisibility(View.VISIBLE);
        } else {
            mCommentModel.setVisibility(View.GONE);
        }
        Date d = new Date();
        d.setTime(DateFormatUtil.getTime(topicDetailModel.createTime));
        mTimeRecord.setText(topicDetailModel.createTime);

        //性别
        if (topicDetailModel.user.sex == 1) {
            mGenderImg.setImageResource(R.drawable.icon_men);
        } else {
            mGenderImg.setImageResource(R.drawable.icon_women);
        }

        if (topicDetailModel.user.is_master == 1) {
            mBrandLogo.setVisibility(View.VISIBLE);
            mBrandLogo.setImageURI(
                    Uri.parse(URLUtil.builderImgUrl(topicDetailModel.user.masterLogo, 144, 144)));
        } else {
            mBrandLogo.setVisibility(View.GONE);
        }

        //用户头像
        mHeaderImg
                .setImageURI(Uri.parse(URLUtil.builderImgUrl(topicDetailModel.user.avatar, 144, 144)));

    }

    private void initLocData(TopicDetailModel topicDetailModel) {

        mDistanceTv.setText(topicDetailModel.distance + "km");
        mLocTv.setText(topicDetailModel.address);
    }

    private void initHonorData(TopicDetailModel topicDetailModel) {

        WrapAdapter<TopicDetailModel.Honour> adapter = new WrapAdapter<TopicDetailModel.Honour>(
                mContext, R.layout.honor_list_item, topicDetailModel.honours) {
            @Override
            public void convert(ViewHolder helper, TopicDetailModel.Honour item) {
                SimpleDraweeView simpleDraweeView = helper.getView(R.id.honorIcon);
                simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.icon, 144, 144)));
                TextView titleTv = helper.getView(R.id.title);
                titleTv.setText(item.text);

                if (!TextUtils.isEmpty(item.tag)) {
                    ViewUtil.link(item.substr, titleTv, new Link.OnClickListener() {
                        @Override
                        public void onClick(Object o, String clickedText) {

                        }
                    }, mRes.getColor(R.color.text_color6), true);
                }

                helper.setText(R.id.xpValue, item.integral);
            }
        };
        mHonorList.setAdapter(adapter);
        ViewUtil.setListViewHeightBasedOnChildren2(mHonorList);
    }

    private void initGroupData(TopicDetailModel.Group group) {

        if (group.id == 0) {
            mGroupLayout.setVisibility(View.GONE);
            //findViewById(R.id.split4).setVisibility(View.GONE);
        }
        mGroupIcon.setImageURI(Uri.parse(URLUtil.builderImgUrl(group.cover, 144, 144)));
        mGroupTitle.setText(group.name);
        mGroupDesc.setText(group.desc);
    }

    private TopicDetailModel mModel;
    private MorePopupWindow morePopupWindow;

    private void initHideMoreMenuData() {

        if (dataList.size() == 0) {
            String[] navValues = null;
            if ((mModel.user.userId + "").equals(IOUtils.getUserId(getContext()))) {
                navValues = mRes.getStringArray(R.array.global_navme_values);
            } else {
                navValues = mRes.getStringArray(R.array.global_nav_values);
            }
            Map<String, Integer> titleActionIconMap = DataUtil.getTitleActionIcon();
            int length = navValues.length;
            for (int i = 0; i < length; i++) {
                MorePopupwindowBean morePopupwindowBean = null;
                morePopupwindowBean = new MorePopupwindowBean();
                morePopupwindowBean.title = navValues[i];
                morePopupwindowBean.icon = titleActionIconMap.get(navValues[i]);
                morePopupwindowBean.id = i;
                dataList.add(morePopupwindowBean);
            }

            morePopupWindow = new MorePopupWindow(TopicDetailActivity.this,
                    dataList, mMoreOnClickListener);


        }


    }

    private String cover = "/car/share/default.png";

    private void share() {

        openShareWindow.show(this);
        final String title = "看车玩车";
        final String description = mModel.title;

        for (SEditorData data : mModel.describe.mEditorDatas) {
            if (!TextUtil.isEmpty(data.getImageUrl())) {

                cover = data.getImageUrl();
                break;
            }
        }
        cover = URLUtil.builderImgUrl(cover, 540, 270);

        openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        OpenShareUtil.sendWXMoments(mContext, mGlobalContext.getMsgApi(), title, description);
                        break;
                    case 1:
                        OpenShareUtil.sendWX(mContext, mGlobalContext.getMsgApi(), title, description);
                        break;

                    case 2:
                        mWbHandler = OpenShareUtil.shareSina(TopicDetailActivity.this, title,
                                description);
                        break;
                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(mGlobalContext.getTencent(), TopicDetailActivity.this,
                                mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(mGlobalContext.getTencent(), TopicDetailActivity.this,
                                mBaseUIListener, title, cover, description);
                        break;

                }
                openShareWindow.dismiss();
            }
        });
    }

    private int type = 0; //1 表示评论、2表示点赞、3表示跳转到图片浏览器界面

    private MorePopupWindow.MoreOnClickListener mMoreOnClickListener = new MorePopupWindow.MoreOnClickListener() {
        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean item) {
            switch (dataList.get(position).title) {
                case "首页":
                    CommonUtils.gotoMainPage(mContext, Constants.Navigation.HOME_INDEX);
                    break;
                case "消息":
                    CommonUtils.startNewActivity(mContext, MessageActivity.class);
                    break;
                case "分享":
                    share();
                    break;
                case "扫一扫":
                    if (IOUtils.isLogin(mContext)) {
                        startActivity(new Intent(mContext, ScannerCodeActivity.class));
                    }
                    break;
                case "反馈":
                    CommonUtils.startNewActivity(mContext, FanKuiActivity.class);
                    break;
                case "编辑":

                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.EDIT_TOPIC, true);
                    args.put(Constants.IntentParams.THREAD_ID, mId);
                    args.put(Constants.IntentParams.LAT, mGlobalContext.getLattitude());
                    args.put(Constants.IntentParams.LNG, mGlobalContext.getLongitude());
                    CommonUtils.startNewActivity(mContext, args, PostingActivity.class);
                    // CommonUtils.showToast(mContext, "暂时开放，敬请期待");
                    break;
                case "删除":
                    deleteTopic();
                    break;
                case "举报":
                    //CommonUtils.showToast(mContext, "暂时开放，敬请期待");

                    operatorReport();
                    break;
                case "收藏":
                    favorite();
                    break;

                case "取消收藏":
                    favorite();
                    break;
            }
        }
    };

    private void operatorReport() {


        Map<String, String> params = new HashMap<String, String>();

        params.put("resource_id", mId);
        params.put("resource_type", "thread");
        params.put("vehicle_type", "car");
        params.put("token", IOUtils.getToken(mContext));
        mOperatorPresenter.isExistReport(params);

    }


    @Override
    public void showData(TopicDetailModel topicDetailModel) {

        isFav = topicDetailModel.isCollect;
        mModel = topicDetailModel;
        initHideMoreMenuData();
        int isCollect = topicDetailModel.isCollect;
        isFav = isCollect;

        if ((mModel.user.userId + "").equals(IOUtils.getUserId(getContext()))) {
            if (isCollect == 0) {
                dataList.get(5).title = "收藏";
            } else {
                dataList.get(5).title = "取消收藏";
            }

        } else {
            if (isCollect == 0) {
                dataList.get(3).title = "收藏";
            } else {
                dataList.get(3).title = "取消收藏";
            }
        }

        morePopupWindow.getmCommonAdapter().notifyDataSetChanged();
        mLikeContent.setText("(" + topicDetailModel.diggCount + ")");
        mCmtContent.setText("(" + topicDetailModel.replyCount + ")");
        if (topicDetailModel.replyCount == 0) {
            mUnReadNums.setVisibility(View.GONE);
        } else {
            mUnReadNums.setVisibility(View.VISIBLE);
        }
        mUnReadNums.setText(topicDetailModel.replyCount + "");
        int isPraise = topicDetailModel.isPraise;
        if (isPraise == 1) {

            mFavoriteImg.setImageResource(R.drawable.btn_heart2);
            // mFavoriteImg.setEnabled(false);
        } else {
            mFavoriteImg.setImageResource(R.drawable.btn_heart1);
        }
        initTitleData(topicDetailModel);
        initLocData(topicDetailModel);
        addTagItems(topicDetailModel.topic);
        initHonorData(topicDetailModel);
        initGroupData(topicDetailModel.group);
        //加载图文数据
        showRichContent(topicDetailModel);
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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mCommentPresenter != null) {
            mCommentPresenter.detachView();
        }

        if (mLikePresenter != null) {
            mLikePresenter.detachView();
        }
        if (mAttentionPresenter != null) {
            mAttentionPresenter.detachView();
        }
        if (mCancelPresenter != null) {
            mCancelPresenter.detachView();
        }
        if (openShareWindow != null) {
            openShareWindow.dismiss();
        }
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

    private void showCommentLists(CommentModel model, int type) {

        mLikeList.setVisibility(View.GONE);
        mCommentList.setVisibility(View.VISIBLE);
        mCmtContent.setText("(" + model.count + ")");
        //评论列表
        mCommentsadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_comment,
                model.comments) {

            @Override
            public void onBind(final int pos, final Comment comment, NestFullViewHolder holder) {
                holder.setText(R.id.nametv, comment.senderInfo.nick);
                holder.setText(R.id.comment_time, comment.time);
                holder.setText(R.id.commnt_good, comment.fabNum + "");
                holder.setText(R.id.reply_comments_tv, comment.repliesCount + "");
                holder.setEmojiText(R.id.contentTv, comment.content);
                ImageView modelImg = holder.getView(R.id.comment_model_tv);
                ImageView popmanImg = holder.getView(R.id.comment_popman_tv);

                //达人
                if (comment.senderInfo.isExpert == 1) {
                    popmanImg.setVisibility(View.VISIBLE);
                } else {
                    popmanImg.setVisibility(View.GONE);
                }
                //模特
                if (comment.senderInfo.is_model == 1) {
                    modelImg.setVisibility(View.VISIBLE);
                } else {
                    modelImg.setVisibility(View.GONE);
                }
                SimpleDraweeView simpleDraweeView = holder.getView(R.id.motodetail_avatar_iv);
                simpleDraweeView
                        .setImageURI(URLUtil.builderImgUrl(comment.senderInfo.avatar, 144, 144));
                MultiImageView multiImageView = holder.getView(R.id.multiImagView);
                multiImageView.setList(comment.imgs);
                holder.getView(R.id.reply_comments_tv)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CommonUtils.showToast(mContext, "写评论" + pos);
                                CommonUtils.startNewActivity(mContext, CommentEditActivity.class);
                            }
                        });
                holder.getConvertView()
                        .setBackgroundColor(mRes.getColor(R.color.transparent90_white));
                NestFullListView replyListview = holder.getView(R.id.listview_item_reply_lv);
                mReplyadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_reply,
                        comment.replies) {
                    @Override
                    public void onBind(final int pos2, Comment c, NestFullViewHolder holder) {
                        TextView replyTv = holder.getView(R.id.replytv);
                        String msg = String.format(mRes.getString(R.string.reply_msg),
                                comment.senderInfo.nickName, comment.receiverInfo.nickName,
                                comment.content);
                        if (pos2 == 2) {
                            replyTv.setText("查看更多回复");
                        } else {

                            replyTv.setText(Html.fromHtml(msg));

                        }
                    }
                };
                replyListview.setAdapter(mReplyadapter);
                replyListview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(NestFullListView parent, View view, int position) {
                        if (position >= 2) {

                            CommonUtils.showToast(mContext, "查看更多回复");
                        } else {
                            // CommonUtils.showToast(mContext, "回复评论" + pos + "postwo  " + position);
                            CommonUtils.startNewActivity(mContext, ReplyEditActivity.class);

                        }

                    }
                });
            }
        };
        mCommentList.setAdapter(mCommentsadapter);
    }

    private void showCommentLists(final CommentModel model) {

        mLikeList.setVisibility(View.GONE);
        mCommentList.setVisibility(View.VISIBLE);
        //评论列表
        mCommentsadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_comment,
                model.comments) {

            @Override
            public void onBind(final int pos, final Comment comment, NestFullViewHolder holder) {

                holder.getView(R.id.headerImgLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        jumpToUserHome(comment.senderInfo.id, 1);
                    }
                });
                holder.setText(R.id.nametv, comment.senderInfo.nickName);
                holder.setText(R.id.comment_time, comment.time);
                holder.setText(R.id.commnt_good, comment.fabNum + "");
                holder.setText(R.id.reply_comments_tv, comment.repliesCount + "");
                holder.getView(R.id.commnt_good).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mExecLikePresenter.executePraise(comment.id + "", "comment",
                                IOUtils.getToken(mContext));

                    }
                });
                holder.setText(R.id.contentTv, comment.content);
                ImageView modelImg = holder.getView(R.id.comment_model_tv);
                final ImageView popmanImg = holder.getView(R.id.comment_popman_tv);

                SimpleDraweeView brandLogo = holder.getView(R.id.brandLogo);
                brandLogo.setImageURI(
                        Uri.parse(URLUtil.builderImgUrl(comment.senderInfo.brandLogo, 144, 144)));

                SimpleDraweeView genderImg = holder.getView(R.id.genderImg);

                if (comment.senderInfo.sex != 1) {
                    genderImg.setImageResource(R.drawable.icon_women);
                } else {
                    genderImg.setImageResource(R.drawable.icon_men);
                }

                //达人
                if (comment.senderInfo.isExpert == 1) {
                    popmanImg.setVisibility(View.VISIBLE);
                } else {
                    popmanImg.setVisibility(View.GONE);
                }
                //模特
                if (comment.senderInfo.is_model == 1) {
                    modelImg.setVisibility(View.VISIBLE);
                } else {
                    modelImg.setVisibility(View.GONE);
                }
                SimpleDraweeView simpleDraweeView = holder.getView(R.id.motodetail_avatar_iv);
                simpleDraweeView
                        .setImageURI(URLUtil.builderImgUrl(comment.senderInfo.avatar, 144, 144));
                MultiImageView multiImageView = holder.getView(R.id.multiImagView);

                List<String> urls = comment.imgs;
                List<String> newUrls = new ArrayList<String>();

                final ArrayList<Image> imgs = new ArrayList<Image>();
                for (String url : urls) {
                    newUrls.add(URLUtil.builderImgUrl(url, 540, 270));
                    Image image = new Image();
                    image.link = url;
                    imgs.add(image);

                }
                multiImageView.setList(newUrls);
                multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        type=3;
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("list", imgs);
                        intent.putExtra(Constants.IntentParams.DATA, position);
                        intent.setClass(mContext, PhotoBrowserActivity.class);
                        startActivity(intent);

                    }
                });
                holder.getView(R.id.reply_comments_tv)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //  CommonUtils.showToast(mContext, "写评论" + pos);
                                Map<String, Serializable> args = new HashMap<String, Serializable>();

                                args.put(Constants.IntentParams.ID, mId + "");
                                args.put(Constants.IntentParams.ID2, comment.id + "");
                                CommonUtils.startNewActivity(mContext, args,
                                        CommentEditorActivity.class);
                            }
                        });
                holder.getConvertView()
                        .setBackgroundColor(mRes.getColor(R.color.transparent90_white));
                NestFullListView replyListview = holder.getView(R.id.listview_item_reply_lv);

                List<Comment> cmts = comment.replies;
                int size = cmts.size();
                if (comment.repliesCount > size) {
                    Comment cmt = new Comment();
                    cmt.content = "更多回复";
                    cmts.add(cmt);
                }

                mReplyadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_reply,
                        cmts) {
                    @Override
                    public void onBind(final int pos2, Comment c, NestFullViewHolder holder) {

                        TextView replyTv = holder.getView(R.id.replytv);
                        String msg = "";
                        if (pos2 == 2) {
                            replyTv.setText(c.content);
                        } else {
                            msg = String.format(mRes.getString(R.string.reply_msg),
                                    c.senderInfo.nickName, c.receiverInfo.nickName, c.content);
                            replyTv.setText(Html.fromHtml(msg));
                        }

                    }
                };
                replyListview.setAdapter(mReplyadapter);
                replyListview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(NestFullListView parent, View view, int position) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        if (position >= 2) {

                            args.put(Constants.IntentParams.ID, comment.id);
                            CommonUtils.startNewActivity(mContext, args, ReplyMoreActivity.class);

                        } else {
                            args = new HashMap<String, Serializable>();

                            args.put(Constants.IntentParams.ID, mId + "");
                            args.put(Constants.IntentParams.ID2, comment.id + "");
                            CommonUtils.startNewActivity(mContext, args,
                                    CommentEditorActivity.class);
                        }

                    }
                });
            }
        };
        mCommentList.setAdapter(mCommentsadapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //加上这一句才能回调
        Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUIListener);

    }
}
