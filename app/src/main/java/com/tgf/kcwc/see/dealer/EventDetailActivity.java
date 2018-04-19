package com.tgf.kcwc.see.dealer;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.CommentEditActivity;
import com.tgf.kcwc.app.ReplyEditActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MultiImageView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;

/**
 * Author：Jenny
 * Date:2016/12/23 15:57
 * E-mail：fishloveqin@gmail.com
 * 店内活动详情
 */

public class EventDetailActivity extends BaseActivity {

    protected WebView               mTopicWebView;
    protected NestFullListView      mCommentList;
    //评论
    private NestFullListViewAdapter mCommentsadapter;
    //回复列表
    private NestFullListViewAdapter mReplyadapter;
    private ArrayList               mLoopHead = new ArrayList();
    private RelativeLayout          mCommentLayout;
    private ImageView               mFavoriteImg, mShareImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getStringExtra(Constants.IntentParams.ID);

        super.setContentView(R.layout.activity_event_detail_layout);

    }

    private void initViews() {
        mTopicWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings webSettings = mTopicWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);

        mTopicWebView.loadUrl("https://tgf.worktile.com/tasks/projects/5843acbf6cf5cd2b52ca45ce");

    }

    private String mId;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("活动详情");
    }

    @Override
    protected void setUpViews() {
        initView();
    }

    private void initView() {
        mTopicWebView = (WebView) findViewById(R.id.topicWebView);
        mCommentList = (NestFullListView) findViewById(R.id.commentList);
        mCommentLayout = (RelativeLayout) findViewById(R.id.commentLayout);
        mFavoriteImg = (ImageView) findViewById(R.id.favoriteImg);
        mShareImg = (ImageView) findViewById(R.id.shareImg);
        mCommentLayout.setOnClickListener(this);
        mFavoriteImg.setOnClickListener(this);
        mShareImg.setOnClickListener(this);
        for (int i = 0; i < 3; i++) {
            mLoopHead.add("donig" + i);
        }
        initViews();
        showCommentLists();
    }

    private void showCommentLists() {

        //评论列表
        mCommentsadapter = new NestFullListViewAdapter<String>(R.layout.listview_item_comment,
            mLoopHead) {

            @Override
            public void onBind(final int pos, String s, NestFullViewHolder holder) {
                SimpleDraweeView simpleDraweeView = holder.getView(R.id.motodetail_avatar_iv);
                simpleDraweeView.setImageURI(DataUtil.AVATAR[pos]);
                MultiImageView multiImageView = holder.getView(R.id.multiImagView);
                multiImageView.setList(DataUtil.listurl);
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
                mReplyadapter = new NestFullListViewAdapter(R.layout.listview_item_reply,
                    mLoopHead.subList(0, 3)) {
                    @Override
                    public void onBind(final int pos2, Object o, NestFullViewHolder holder) {
                        if (pos2 == 2) {
                            TextView replyTv = holder.getView(R.id.replytv);
                            replyTv.setText("查看更多回复");
                        }
                    }
                };
                replyListview.setAdapter(mReplyadapter);
                replyListview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(NestFullListView parent, View view, int position) {
                        if(position>=2){

                            CommonUtils.showToast(mContext, "查看更多回复");
                        }else {
                            CommonUtils.showToast(mContext, "回复评论" + pos + "postwo  " + position);
                            CommonUtils.startNewActivity(mContext, ReplyEditActivity.class);

                        }

                    }
                });
            }
        };
        mCommentList.setAdapter(mCommentsadapter);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.commentLayout:
                break;

            case R.id.favoriteImg:
                break;
            case R.id.shareImg:
                break;

        }

    }
}
