package com.tgf.kcwc.see.dealer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Comment;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MapNavPopWindow;
import com.tgf.kcwc.view.MultiImageView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/26 13:56
 * E-mail：fishloveqin@gmail.com
 * 评价列表
 */

public class EvaluationListActivity extends BaseActivity implements CommentListView<CommentModel> {

    private NestFullListView        mEvaluateList;
    private NestFullListViewAdapter mCommentsadapter;
    //回复列表
    private NestFullListViewAdapter mReplyadapter;
    private TextView                mCallBtn, mNavBtn;
    private CommentListPresenter    mCommentPresenter;
    private String                  mOrgId = "";
    private String                  mTel   = "";
    private String                  mLng   = "";
    private String                  mLat   = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mOrgId = intent.getStringExtra(Constants.IntentParams.ID);
        mTel = intent.getStringExtra(Constants.IntentParams.TEL);
        mLng = intent.getStringExtra(Constants.IntentParams.LNG);
        mLat = intent.getStringExtra(Constants.IntentParams.LAT);
        setContentView(R.layout.activity_evaluation_list);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.evaluation);
        text.setTextColor(mRes.getColor(R.color.white));
    }

    @Override
    protected void setUpViews() {

        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        mCommentPresenter.loadEvaluateList("shop_detail", mOrgId, "car");
        mEvaluateList = (NestFullListView) findViewById(R.id.evaluateList);
        mCallBtn = (TextView) findViewById(R.id.callBtn);
        mNavBtn = (TextView) findViewById(R.id.navBtn);
        mCallBtn.setOnClickListener(this);
        mNavBtn.setOnClickListener(this);
    }

    private void showEvaluateLists(final CommentModel model) {

        List<Comment> datas = new ArrayList<Comment>();
        datas.addAll(model.comments);
        mCommentsadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_evaluate,
            datas) {

            @Override
            public void onBind(final int pos, Comment comment, NestFullViewHolder holder) {
                SimpleDraweeView simpleDraweeView = holder.getView(R.id.img);
                simpleDraweeView
                    .setImageURI(URLUtil.builderImgUrl(comment.senderInfo.avatar, 144, 144));
                RatingBar ratingBar = holder.getView(R.id.ratingBar);
                setRatingScore(ratingBar, 5, 4.5f, R.drawable.icon_star_n,
                    R.drawable.icon_half_a_star_1_n, R.drawable.icon_star_s,
                    BitmapUtils.dp2px(mContext, 15), BitmapUtils.dp2px(mContext, 15), 0);
                holder.setText(R.id.nametv, comment.senderInfo.nickName + "");
                holder.setText(R.id.timeRecord, comment.time + "");
                holder.setEmojiText(R.id.contentTv, comment.content);
                TextView modelText = holder.getView(R.id.comment_model_tv);
                TextView popmanText = holder.getView(R.id.comment_popman_tv);
                SimpleDraweeView genderImg = holder.getView(R.id.genderImg);
                if (comment.senderInfo.sex == 1) {
                    genderImg.setImageResource(R.drawable.icon_men);
                } else {
                    genderImg.setImageResource(R.drawable.icon_women);
                }
                if (comment.senderInfo.is_model == 1) {
                    modelText.setVisibility(View.VISIBLE);
                } else {
                    modelText.setVisibility(View.GONE);
                }
                if (comment.senderInfo.isDaren == 1) {
                    popmanText.setVisibility(View.VISIBLE);
                } else {
                    popmanText.setVisibility(View.GONE);
                }
                MultiImageView multiImageView = holder.getView(R.id.multiImagView);
                multiImageView.setList(comment.imgs);
                NestFullListView replyListview = holder.getView(R.id.listview_item_reply_lv);
                mReplyadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_reply,
                    comment.replies) {
                    @Override
                    public void onBind(final int pos2, Comment cmt, NestFullViewHolder holder) {
                        TextView replyTv = holder.getView(R.id.replytv);
                        String msg = String.format(mRes.getString(R.string.reply_msg),
                            cmt.senderInfo.nickName, cmt.receiverInfo.nickName, cmt.content);
                        if (pos2 == 2) {
                            replyTv.setText("查看更多回复");
                        } else {

                            replyTv.setText(Html.fromHtml(msg));

                        }
                    }
                };
                replyListview.setAdapter(mReplyadapter);
                //                replyListview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                //                    @Override
                //                    public void onItemClick(NestFullListView parent, View view, int position) {
                //                        if (position >= 2) {
                //
                //                            CommonUtils.showToast(mContext, "查看更多回复");
                //                        } else {
                //                            CommonUtils.showToast(mContext, "回复评论" + pos + "postwo  " + position);
                //                            CommonUtils.startNewActivity(mContext, ReplyEditActivity.class);
                //
                //                        }
                //                    }
                //                });
            }
        };

        mEvaluateList.setAdapter(mCommentsadapter);
    }

    private void setRatingScore(RatingBar ratingBar, int count, float star, int drawableEmptyId,
                                int drawableHalfId, int drawableFillId, int starWidth,
                                int starHeight, int starPadding) {

        ratingBar.setStarEmptyDrawable(getResources().getDrawable(drawableEmptyId));
        ratingBar.setStarHalfDrawable(getResources().getDrawable(drawableHalfId));
        ratingBar.setStarFillDrawable(getResources().getDrawable(drawableFillId));
        ratingBar.setStar(star);
        ratingBar.setStarCount(count);
        ratingBar.halfStar(true);
        ratingBar.setmClickable(true);
        ratingBar.setStarImageWidth(starWidth);
        ratingBar.setStarImageHeight(starHeight);
        ratingBar.setImagePadding(starPadding);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.callBtn:
                SystemInvoker.launchDailPage(mContext, mTel);
                break;
            case R.id.navBtn:
                MapNavPopWindow mapNavPopWindow = new MapNavPopWindow(mContext);
                mapNavPopWindow.setOnClickListener(this);
                mapNavPopWindow.show(this);
                break;

            case R.id.aMap:

                try {
                    URLUtil.launcherInnerNavAMap(this, mLat, mLng);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.baiduMap:
                try {
                    URLUtil.launcherInnerNavBaidu(this, mLat, mLng);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void showDatas(CommentModel model) {

        showEvaluateLists(model);
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
}
