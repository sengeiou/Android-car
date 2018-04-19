package com.tgf.kcwc.comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.ReplyEditActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.Comment;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.see.BigPhotoPageActivity;
import com.tgf.kcwc.util.ArrayUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MultiImageView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/4/19 0019
 * E-mail:hekescott@qq.com
 */

public class CommentMoreActivity extends BaseActivity implements CommentListView {
    private ListView                commentMoreLv;
    private List<Comment>           commentlist          = new ArrayList<>();
    private CommentListPresenter    mCommentListPresenter;
    private NestFullListViewAdapter replyadapter;
    private static final int        REQUEST_CODE_REPLY   = 1;
    private static final int        REQUEST_CODE_COMMENT = 2;
    private int                     mResId               = 7;
    private WrapAdapter<Comment>    myCommentAdapter;
    private String mModules;
    private CommentListPresenter mLikePresenter;
    //点赞
    private CommentListView<View> mExecLikeView = new CommentListView<View>() {
        @Override
        public void showDatas(View v) {
            myCommentAdapter.notifyDataSetChanged();
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
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("评论");
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        mPageSize = 10;
        initRefreshLayout(mBGDelegate);
        findViewById(R.id.repayLayout).setOnClickListener(this);
        commentMoreLv = (ListView) findViewById(R.id.comentmore_contentlv);
        Intent fromIntent =  getIntent();
        mResId = fromIntent.getIntExtra(Constants.IntentParams.ID,4);
        mModules = fromIntent.getStringExtra(Constants.IntentParams.TITLE);
        mCommentListPresenter = new CommentListPresenter();
        mCommentListPresenter.attachView(this);
        mCommentListPresenter.loadCommentList(mModules, mResId + "", "car");
        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mExecLikeView);
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 0;
            commentlist.clear();
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            loadMore();
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {
        mPageIndex++;
        mCommentListPresenter.loadCommentList(mModules, mResId + "", "car", mPageIndex,
            mPageSize);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentmore);

        commentlist = new ArrayList<>();
    }

    private void initAdapter() {

        myCommentAdapter = new WrapAdapter<Comment>(this, R.layout.listview_item_comment,
            commentlist) {
            @Override
            public void convert(final ViewHolder holder, final Comment comment) {
                final Account.UserInfo sendinfo = comment.senderInfo;
               SimpleDraweeView avatarIv  =  holder.getView(R.id.motodetail_avatar_iv);
                avatarIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(sendinfo.avatar, 144, 144)));
                TextView goodTv =   holder.getView(R.id.commnt_good);
                goodTv.setText(comment.fabNum+"");
                holder.setText(R.id.nametv, sendinfo.nickName);
                holder.setText(R.id.comment_time, comment.time);
                holder.setEmojiText(R.id.contentTv, comment.content);
                ImageView modelImg = holder.getView(R.id.comment_model_tv);
                ImageView popmanImg = holder.getView(R.id.comment_popman_tv);
                SimpleDraweeView genderIv = holder.getView(R.id.genderImg);
                GenericDraweeHierarchy hierarchy = genderIv.getHierarchy();
                avatarIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toUserIntent = new Intent(getContext(),UserPageActivity.class);
                        toUserIntent.putExtra(Constants.IntentParams.ID,sendinfo.id);
                        startActivity(toUserIntent);
                    }
                });
                if (comment.senderInfo.sex == 1) {
                    hierarchy.setPlaceholderImage(R.drawable.icon_men);
                } else {
                    hierarchy.setPlaceholderImage(R.drawable.icon_women);
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
                goodTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(comment.isFab==0){
                            comment.fabNum++;
                            comment.isFab=1;
                        }else{
                            comment.fabNum--;
                            comment.isFab=0;
                        }
                        mLikePresenter.executePraise(comment.id + "", "comment", v,
                                IOUtils.getToken(mContext));
                    }
                });
                MultiImageView multiImageView = holder.getView(R.id.multiImagView);
                if (comment.imgs != null) {
                    List<String> listImgs = new ArrayList<>();
                    for (String img : comment.imgs) {
                        listImgs.add(URLUtil.builderImgUrl(img, 360, 360));
                    }
                    multiImageView.setList(listImgs);
                    multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ArrayList<String> imageUrlList = ArrayUtils.getImageUrlList(comment.imgs);
                            Intent toInent = new Intent(getContext(),BigPhotoPageActivity.class);
                            toInent.putStringArrayListExtra(Constants.KEY_IMGS,imageUrlList);
                            toInent.putExtra(Constants.IntentParams.INDEX,position);
                            startActivity(toInent);
                        }
                    });
                }
                TextView replyNum = holder.getView(R.id.reply_comments_tv);
                replyNum.setText(comment.repliesCount + "");
                replyNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toIntent = new Intent(mContext, CommentEditorActivity.class);
                        toIntent.putExtra(Constants.IntentParams.ID, mResId + "");
                        toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModules);
                        toIntent.putExtra(Constants.IntentParams.ID2,comment.id+"");
                        startActivityForResult(toIntent,RESULT_FIRST_USER);
                    }
                });
                NestFullListView replyListview = holder.getView(R.id.listview_item_reply_lv);
                replyListView(comment, replyListview);

            }

            private void replyListView(final Comment comment, NestFullListView replyListview) {
                if(comment.repliesCount>=3){
                    comment.replies.add(new Comment());
                }
                List<Comment> replyList = null;
                if(comment.replies!=null&&comment.replies.size()>=3){
                    replyList= comment.replies.subList(0,3);
                }else{
                    replyList = comment.replies;
                }
                replyadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_reply,
                        replyList) {
                    @Override
                    public void onBind(int pos2, Comment cmt, NestFullViewHolder holder) {
                        TextView replyTv = holder.getView(R.id.replytv);

                        if (pos2 == 2) {
                            replyTv.setText("查看更多回复");
                        } else {
                            if(cmt.senderInfo!=null){
                                String msg = String.format(mRes.getString(R.string.reply_msg),
                                        cmt.senderInfo.nickName, cmt.receiverInfo.nickName,
                                        cmt.content);

                                replyTv.setText(Html.fromHtml(msg));
                            }
                        }
                    }
                };
                replyListview.setAdapter(replyadapter);
                replyListview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(NestFullListView parent, View view, int position) {
                        if (position >= 2) {
                            Intent toReplyMore = new Intent(getContext(),ReplyMoreActivity.class);
                            toReplyMore.putExtra(Constants.IntentParams.ID, mResId+"");
                            toReplyMore.putExtra(Constants.IntentParams.ID2, comment.id+"");
                            toReplyMore.putExtra(Constants.IntentParams.INTENT_TYPE, mModules);
                            startActivity(toReplyMore);
                        }
                    }
                });
            }
        };
        commentMoreLv.setAdapter(myCommentAdapter);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.repayLayout:
                if(IOUtils.isLogin(getContext())){
//                    Map<String, Serializable> args = new HashMap<String, Serializable>();
//                    args.put(Constants.IntentParams.ID, mResId + "");
//                    CommonUtils.startNewActivity(mContext, args, CommentEditorActivity.class);

                    Intent toIntent = new Intent(mContext, CommentEditorActivity.class);
                    toIntent.putExtra(Constants.IntentParams.ID, mResId + "");
                    toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModules);
                    startActivityForResult(toIntent, REQUEST_CODE_COMMENT);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void showDatas(Object o) {
        CommentModel commentModel = (CommentModel) o;
        if (commentModel.comments == null || commentModel.comments.size() == 0) {
            return;
        }
        commentlist.addAll(commentModel.comments);
        if (myCommentAdapter == null) {
            initAdapter();
        } else {
            myCommentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingDialog();
        } else {
            stopRefreshAll();
            dismissLoadingDialog();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(data!=null){
                mPageIndex = 0;
                commentlist.clear();
                loadMore();
            }
        }
    }
}
