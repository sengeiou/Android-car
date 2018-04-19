package com.tgf.kcwc.comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
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
import com.tgf.kcwc.view.MultiImageView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/13 0013
 * E-mail:hekescott@qq.com
 */

public class CommentFrag extends BaseFragment implements CommentListView {
    private List<Comment> commentArrayList = new ArrayList<>();
    private NestFullListViewAdapter mReplyadapter;
    private NestFullListViewAdapter mCommentsadapter;
    private int mthreadId;
    private String mModules;
    private NestFullListView commentlV;
    private final CommentListPresenter mCommentPresenter;
    private CommentListPresenter mLikePresenter;

    /**
     * @param comments 评论的数据
     * @param threadId 资源id
     * @param modules  模块
     */
    public CommentFrag(List<Comment> comments, int threadId, String modules) {
        if (comments != null) {
            commentArrayList = comments;
        }
        mthreadId = threadId;
        mModules = modules;
        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mExecLikeView);
    }

    //点赞
    private CommentListView<View> mExecLikeView = new CommentListView<View>() {
        @Override
        public void showDatas(View v) {
            mCommentPresenter.loadCommentList(mModules, mthreadId + "", "car");
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
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_comment;
    }

    @Override
    protected void initView() {
        commentlV = findView(R.id.comment_lv);
        showCommentLists();
        commentlV.setAdapter(mCommentsadapter);
    }

    private void showCommentLists() {

        mCommentsadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_comment,
                commentArrayList) {

            @Override
            public void onBind(final int pos, final Comment comment, NestFullViewHolder holder) {
                final Account.UserInfo sendinfo = comment.senderInfo;
                holder.setText(R.id.nametv, sendinfo.nickName);
                holder.setText(R.id.comment_time, comment.time);
                holder.setText(R.id.commnt_good, comment.fabNum + "");
                TextView goodTv = holder.getView(R.id.commnt_good);
                holder.setEmojiText(R.id.contentTv, comment.content);
                ImageView modelImg = holder.getView(R.id.comment_model_tv);
                ImageView popmanImg = holder.getView(R.id.comment_popman_tv);
                TextView remplyNumTv = holder.getView(R.id.reply_comments_tv);
                SimpleDraweeView genderIv = holder.getView(R.id.genderImg);
                GenericDraweeHierarchy hierarchy = genderIv.getHierarchy();
                if (comment.senderInfo.sex == 1) {
                    hierarchy.setPlaceholderImage(R.drawable.icon_men);
                } else {
                    hierarchy.setPlaceholderImage(R.drawable.icon_women);
                }
                remplyNumTv.setText(comment.repliesCount + "");
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
                if (comment.isFab == 1) {
                    Drawable drawable = getResources().getDrawable(R.drawable.btn_heart2);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    goodTv.setCompoundDrawables(drawable, null, null, null);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_like);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    goodTv.setCompoundDrawables(drawable, null, null, null);

                }
                String brandLogo = URLUtil.builderImgUrl(comment.senderInfo.brandLogo, 144, 144);
                holder.setSimpleDraweeViewURL(R.id.brandLogo, brandLogo);
                SimpleDraweeView avatarIv = holder.getView(R.id.motodetail_avatar_iv);
                avatarIv
                        .setImageURI(URLUtil.builderImgUrl(comment.senderInfo.avatar, 144, 144));
                avatarIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toUserIntent = new Intent(getContext(), UserPageActivity.class);
                        toUserIntent.putExtra(Constants.IntentParams.ID, sendinfo.id);
                        startActivity(toUserIntent);
                    }
                });
                goodTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mLikePresenter.executePraise(comment.id + "", "comment", v,
                                IOUtils.getToken(mContext));

                    }
                });
                MultiImageView multiImageView = holder.getView(R.id.multiImagView);
                multiImageView.setList(comment.getImglist());
                multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ArrayList<String> imageUrlList = ArrayUtils.getImageUrlList(comment.imgs);
                        Intent toInent = new Intent(getContext(), BigPhotoPageActivity.class);
                        toInent.putStringArrayListExtra(Constants.KEY_IMGS, imageUrlList);
                        toInent.putExtra(Constants.IntentParams.INDEX, position);
                        startActivity(toInent);
                    }
                });
                remplyNumTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toIntent = new Intent(mContext, CommentEditorActivity.class);
                        toIntent.putExtra(Constants.IntentParams.ID, mthreadId + "");
                        toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModules);
                        toIntent.putExtra(Constants.IntentParams.ID2, comment.id + "");
                        getActivity().startActivityForResult(toIntent, getActivity().RESULT_FIRST_USER);
                    }
                });

                holder.getConvertView()
                        .setBackgroundColor(mRes.getColor(R.color.transparent90_white));
                NestFullListView replyListview = holder.getView(R.id.listview_item_reply_lv);
                if (comment.repliesCount >= 3) {
                    comment.replies.add(new Comment());
                }
                List<Comment> replyList = null;
                if (comment.replies != null && comment.replies.size() >= 3) {
                    replyList = comment.replies.subList(0, 3);
                } else {
                    replyList = comment.replies;
                }
                mReplyadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_reply,
                        replyList) {
                    @Override
                    public void onBind(final int pos2, Comment c, NestFullViewHolder holder) {
                        TextView replyTv = holder.getView(R.id.replytv);


                        if (pos2 >= 2) {
                            replyTv.setText("查看更多回复");
                        } else {
                            if (c.senderInfo != null) {
                                String msg = String.format(mRes.getString(R.string.reply_msg),
                                        c.senderInfo.nickName, c.receiverInfo.nickName,
                                        c.content);
                                replyTv.setText(Html.fromHtml(msg));
                            }

                        }
                    }
                };
                replyListview.setAdapter(mReplyadapter);
                replyListview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(NestFullListView parent, View view, int position) {
                        if (position >= 2) {
                            Intent toReplyMore = new Intent(getContext(), ReplyMoreActivity.class);
                            toReplyMore.putExtra(Constants.IntentParams.ID, mthreadId);
                            toReplyMore.putExtra(Constants.IntentParams.ID2, comment.id + "");
                            toReplyMore.putExtra(Constants.IntentParams.INTENT_TYPE, mModules);
                            startActivity(toReplyMore);
                        }

                    }
                });
            }
        };
//        commentlV.addFooterView(View.inflate(getContext(),R.layout.smoothlistview_footer,null));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                mCommentPresenter.loadCommentList(mModules, mthreadId + "", "car");
            }
        }
    }

    public void notifyCommentChange(List<Comment> comments) {
        commentArrayList = comments;
        commentlV.notifyDataSetChange(commentArrayList);
    }

    @Override
    public void showDatas(Object obj) {
        CommentModel commentModel = (CommentModel) obj;
        notifyCommentChange(commentModel.comments);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
