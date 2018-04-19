package com.tgf.kcwc.see.evaluate;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.comment.CommentFrag;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.PopmanEsDetailModel;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.PopmanEsDetailPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.PopmanEsDetailView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/6/3 0003
 * E-mail:hekescott@qq.com
 */

public class PopmanESDetailActitivity extends BaseActivity implements CommentListView, PopmanEsDetailView {
    private final int REQUEST_CODE_COMMENT = 102;
    private CommentListPresenter mCommentPresenter;
    private String mModule = "thread";
    private int mId;
    private TextView mCmtContent;
    private CommentFrag commentFrag;
    private FragmentManager commentFm;
    private FragmentTransaction commentTran;
    private WebView webView;
    private PopmanEsDetailPresenter popmanEsDetailPresenter;

    @Override
    protected void setUpViews() {
        mId = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        mCmtContent = (TextView) findViewById(R.id.cmtContent);
        webView = (WebView) findViewById(R.id.esdetail_contenwv);
        WebSettings webSettings = webView.getSettings();
        popmanEsDetailPresenter = new PopmanEsDetailPresenter();
        popmanEsDetailPresenter.attachView(this);
        popmanEsDetailPresenter.getPopmanEsDetail(mId+"");
        findViewById(R.id.inputComment).setOnClickListener(this);
        webSettings.setJavaScriptEnabled(true);//设置能够解析Javascript
        webSettings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        commentFm = getSupportFragmentManager();
        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        mCommentPresenter.loadCommentList(mModule, mId + "", "car");
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("文章详情");
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esdetail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_CODE_COMMENT) {
                    mCommentPresenter.loadCommentList(mModule, mId + "", "car");
                } else {
                    if (data.getBooleanExtra(Constants.IntentParams.DATA, false)) {
                        if (commentFrag != null) {
                            commentFrag.onActivityResult(requestCode, resultCode, data);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void showDatas(Object obj) {
        CommentModel commentModel = (CommentModel) obj;
        mCmtContent.setText("(" + commentModel.count + ")");
        commentFrag = new CommentFrag(commentModel.comments, mId, mModule);
        commentTran = commentFm.beginTransaction();
        commentTran.replace(R.id.comment_fragfl, commentFrag);
        commentTran.commit();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
            showLoadingIndicator(active);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.inputComment:
                if (IOUtils.isLogin(getContext())) {
                    Intent toIntent = new Intent(mContext, CommentEditorActivity.class);
                    toIntent.putExtra(Constants.IntentParams.ID, mId + "");
                    toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModule);
                    startActivityForResult(toIntent, REQUEST_CODE_COMMENT);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return PopmanESDetailActitivity.this;
    }

    @Override
    public void showPopmanEsContent(String content) {
        webView.loadData(WebviewUtil.getHtmlData(content), "text/html; charset=utf-8", "utf-8");
    }

    @Override
    public void showCreateUser(PopmanEsDetailModel.CreateUser userInfo) {
        SimpleDraweeView avatarIv = (SimpleDraweeView) findViewById(R.id.tripbookdetail_useriv);
        String avatar = URLUtil.builderImgUrl(userInfo.avatar, 144, 144);
        avatarIv.setImageURI(Uri.parse(avatar));
        ImageView genderIv = (ImageView) findViewById(R.id.tripbookdetail_sexiv);
        if (userInfo.sex == 1) {
            genderIv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.icon_men));
        }
        TextView nameTv = (TextView) findViewById(R.id.tripbookdetail_name);

        nameTv.setText(userInfo.nickname);
        View darenIV = findViewById(R.id.drivdetails_convene_da);
        View modelIV = findViewById(R.id.comment_model_tv);
        if (userInfo.is_doyen == 1) {
            darenIV.setVisibility(View.VISIBLE);
        } else {
            darenIV.setVisibility(View.GONE);
        }
        if (userInfo.is_model == 1) {
            modelIV.setVisibility(View.VISIBLE);
        } else {
            modelIV.setVisibility(View.GONE);
        }
        if (!TextUtil.isEmpty(userInfo.master_brand )) {
            SimpleDraweeView userLogoIv = (SimpleDraweeView) findViewById(
                    R.id.drivdetails_brandLogo);
            String userLogoUrl = URLUtil.builderImgUrl(userInfo.master_brand, 144, 144);
            userLogoIv.setImageURI(Uri.parse(userLogoUrl));
        }
    }

    @Override
    public void showTags(ArrayList<Topic> topiclist) {
        View tagLayout = findViewById(R.id.tagLayout);
        if (topiclist == null || topiclist.size() == 0) {
            tagLayout.setVisibility(View.GONE);
        } else {
            FlowLayout mTagLayout = (FlowLayout) findViewById(R.id.tagLists);
            mTagLayout.removeAllViews();
            int size = topiclist.size();
            for (int i = 0; i < size; i++) {
                Topic topic = topiclist.get(i);
                View v = LayoutInflater.from(mContext).inflate(R.layout.tagview, mTagLayout, false);
                v.setTag(topic.id);
                mTagLayout.addView(v);
                TextView tv = (TextView) v.findViewById(R.id.tag_btn);
                tv.setText(topic.name + "");
                tv.setTag(topic.id);
            }
        }
    }

    @Override
    public void showHead(PopmanEsDetailModel esDetailModel) {
       TextView titleTv= (TextView) findViewById(R.id.esdetail_tiltetv);
        titleTv.setText(esDetailModel.title);
        TextView timeTv = (TextView) findViewById(R.id.tripbookdetail_usertime);
        timeTv.setText(esDetailModel.create_time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popmanEsDetailPresenter.detachView();
    }
}
