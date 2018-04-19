package com.tgf.kcwc.comment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.Comment;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/4/19 0019
 * E-mail:hekescott@qq.com
 */

public class ReplyMoreActivity extends BaseActivity implements CommentListView {
    private String resouceId;
    private ListView commentMoreLv;
    private List<Comment> commentlist;
    private CommentListPresenter mCommentListPresenter;
    private Intent fromIntent;
    private String commentId;
    private String mModules = "thread";
    private CommentListPresenter mLikePresenter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("回复");
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        findViewById(R.id.repayLayout).setOnClickListener(this);
        commentMoreLv = (ListView) findViewById(R.id.comentmore_contentlv);
        mCommentListPresenter = new CommentListPresenter();
        mCommentListPresenter.attachView(this);
        fromIntent = getIntent();
        commentId = fromIntent.getStringExtra(Constants.IntentParams.ID2);
        resouceId = fromIntent.getStringExtra(Constants.IntentParams.ID);
        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mExecLikeView);
        String type = fromIntent.getStringExtra(Constants.IntentParams.INTENT_TYPE);
        if (!TextUtil.isEmpty(type)) {
            mModules = type;
        }
//        commentId=854
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCommentListPresenter.loadCommentDetail(commentId + "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentmore);
    }

    private void initAdapter() {
        commentMoreLv.setAdapter(
                new WrapAdapter<Comment>(this, R.layout.listview_item_commentmore, commentlist) {
                    @Override
                    public void convert(ViewHolder helper, final Comment item) {
//                        View rootll = helper.getView(R.id.commentmore_rootll);
//                        if (helper.getPosition() == 0) {
//                            rootll.setBackgroundColor(mRes.getColor(R.color.text_def));
//                        } else {
//                            rootll.setBackgroundColor(mRes.getColor(R.color.white));
//                        }
                        helper.setText(R.id.comment_time, item.time);
                        TextView goodTv = helper.getView(R.id.commnt_good);
                        if (item.isFab == 1) {
                            Drawable drawable = getResources().getDrawable(R.drawable.btn_heart2);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            goodTv.setCompoundDrawables(drawable, null, null, null);
                        } else {
                            Drawable drawable = getResources().getDrawable(R.drawable.icon_like);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            goodTv.setCompoundDrawables(drawable, null, null, null);

                        }

                        TextView replyTv = helper.getView(R.id.reply_comments_tv);
                        replyTv.setVisibility(View.GONE);
                        goodTv.setText(item.fabNum + "");
                        if (TextUtil.isEmpty(item.replyNickname)) {
                            helper.setText(R.id.contentTv, item.content);
                        } else {
                            String replyStr = String.format(mRes.getString(R.string.replymore_msg),
                                    item.replyNickname, item.content);

                            helper.setText(R.id.contentTv, Html.fromHtml(replyStr));

                        }
                        Account.UserInfo sendInfo = item.senderInfo;
                        helper.setText(R.id.nametv, sendInfo.nickName);
                        String brandLogoUrl = URLUtil.builderImgUrl(sendInfo.brandLogo, 144, 144);
                        helper.setSimpleDraweeViewURL(R.id.brandLogo, brandLogoUrl);
                        String avatarUrl = URLUtil.builderImgUrl(sendInfo.avatar, 144, 144);
                        helper.setSimpleDraweeViewURL(R.id.motodetail_avatar_iv, avatarUrl);
                        goodTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLikePresenter.executePraise(item.id + "", "comment", v,
                                        IOUtils.getToken(mContext));
                            }
                        });
                        //性别和属性
                        if (sendInfo.sex == 1) {
                            helper.setImageBitmap(R.id.genderImg,
                                    BitmapFactory.decodeResource(mRes, R.drawable.icon_men));
                        } else {
                            helper.setImageBitmap(R.id.genderImg,
                                    BitmapFactory.decodeResource(mRes, R.drawable.icon_women));
                        }
                        ImageView isModelIv = helper.getView(R.id.comment_model_tv);
                        if (sendInfo.is_model == 0) {
                            isModelIv.setVisibility(View.INVISIBLE);
                        } else {
                            isModelIv.setVisibility(View.VISIBLE);
                        }
                        ImageView isDarenIv = helper.getView(R.id.comment_popman_tv);
                        if (sendInfo.isDaren == 0) {
                            isDarenIv.setVisibility(View.INVISIBLE);
                        } else {
                            isDarenIv.setVisibility(View.VISIBLE);
                        }
                    }

                });
        //                params.put("pid", pid);//id2
//                params.put("receiver_uid", receiverUId);//id3
//                params.put("text", content);
//                params.put("module", mModule);
//                params.put("vehicle_type", type);
//                params.put("reply_id", mId);//ID
        commentMoreLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                if (IOUtils.isLogin(getContext())) {
                    Intent toIntent = new Intent(mContext, ReplyMoreEditorActivity.class);
                    toIntent.putExtra(Constants.IntentParams.ID, commentlist.get(position).id + "");
                    toIntent.putExtra(Constants.IntentParams.ID2, commentId + "");
                    toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModules);
                    toIntent.putExtra(Constants.IntentParams.ID3, commentlist.get(position).senderInfo.id + "");
                    startActivity(toIntent);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.repayLayout:
                if (IOUtils.isLogin(getContext())) {
//                    Map<String, Serializable> args = new HashMap<String, Serializable>();
//                    args.put(Constants.IntentParams.ID, resouceId + "");
//                    args.put(Constants.IntentParams.INTENT_TYPE, mModules);
                    Intent toIntent = new Intent(mContext, CommentEditorActivity.class);
                    toIntent.putExtra(Constants.IntentParams.ID, resouceId + "");
                    toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModules);
                    toIntent.putExtra(Constants.IntentParams.ID2, commentId + "");
                    startActivity(toIntent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showDatas(Object o) {
        CommentModel commentModel = (CommentModel) o;
        commentlist = commentModel.comments;
        initAdapter();
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
        return this;
    }

    //点赞
    private CommentListView<View> mExecLikeView = new CommentListView<View>() {
        @Override
        public void showDatas(View v) {
            mCommentListPresenter.loadCommentDetail(commentId + "");
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
}
