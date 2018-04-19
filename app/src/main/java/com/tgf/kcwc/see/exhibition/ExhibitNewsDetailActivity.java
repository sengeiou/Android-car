package com.tgf.kcwc.see.exhibition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.comment.CommentFrag;
import com.tgf.kcwc.comment.CommentMoreActivity;
import com.tgf.kcwc.comment.DianzanFrag;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.message.MessageActivity;
import com.tgf.kcwc.me.setting.FanKuiActivity;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.ExhibitionNews;
import com.tgf.kcwc.mvp.model.LikeListModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.ExhibitionNewsDetailPresenter;
import com.tgf.kcwc.mvp.presenter.FavorPresenter;
import com.tgf.kcwc.mvp.presenter.TopicDataPresenter;
import com.tgf.kcwc.mvp.presenter.TopicOperatorPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.ExhibitionNewsDetailView;
import com.tgf.kcwc.mvp.view.FavoriteView;
import com.tgf.kcwc.mvp.view.TopicDataView;
import com.tgf.kcwc.mvp.view.TopicOperatorView;
import com.tgf.kcwc.posting.TopicReportActivity;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.OpenShareWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/23 15:57
 * E-mail：fishloveqin@gmail.com
 * 帖子详情
 */

public class ExhibitNewsDetailActivity extends BaseActivity implements ExhibitionNewsDetailView, CommentListView {

    protected WebView mTopicWebView;
    //    private ArrayList               mLoopHead = new ArrayList();
    private RelativeLayout mCommentLayout;
    private ImageView mFavoriteImg, mShareImg;
    private ExhibitionNewsDetailPresenter exhibitionNewsDetailPresenter;
    private CommentListPresenter mCommentPresenter;
    private TextView commentNum;
    private static final int REQUEST_CODE_REPLY = 1;
    private static final int REQUEST_CODE_COMMENT = 102;
    private int mId;
    private String mModule = "event_activity";
    private CommentFrag commentFrag;
    private FragmentManager commentFm;
    private FragmentTransaction commentTran;
    private OpenShareWindow openShareWindow;
    private KPlayCarApp kPlayCarApp;
    private TextView articleTitleTv;
    private TextView articleAuthorTv;
    private TextView clickNumtv;
    private TextView articleDatetv;
    private TextView commetNumTv;
    private CommentListPresenter mExecPraisePresenter;
    private ExhibitionNews mArticleModel;
    private ImageView praiseIv;
    private CommentListPresenter mLikePresenter;
    private DianzanFrag dianzanFrag;
    private TextView mLikeContent;
    private TextView mCmtTitle;
    private TextView mLikeTitle;
    private ImageView mBtmLine1;
    private ImageView mBtmLine2;
    private TopicOperatorPresenter mOperatorPresenter;
    private FavorPresenter mFavorPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_newsdetail);
    }
    private MorePopupWindow morePopupWindow = null;
    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {

        backEvent(back);
        text.setText("文章详情");
        function.setImageResource(R.drawable.global_nav_n);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (morePopupWindow != null) {
                    morePopupWindow.showPopupWindow(function);
                }

            }
        });
    }
    List<MorePopupwindowBean> dataList = new ArrayList<>();
    private void initHideMoreMenuData() {

        if (dataList.size() == 0) {
            String[] navValues = null;
            navValues = mRes.getStringArray(R.array.global_nav_values);
//            if ((mModel.user.userId + "").equals(IOUtils.getUserId(getContext()))) {
//                navValues = mRes.getStringArray(R.array.global_navme_values);
//            } else {
//                //navValues = mRes.getStringArray(R.array.global_nav_values);
//            }
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

            morePopupWindow = new MorePopupWindow(this, dataList, moreOnClickListener);
        }
    }
    private MorePopupWindow.MoreOnClickListener moreOnClickListener = new MorePopupWindow.MoreOnClickListener() {
        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean item) {

            String title = item.title;
            switch (title) {
                case "首页":

//                    Intent intent = new Intent();
//                    intent.putExtra(Constants.IntentParams.INDEX, 0);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.setClass(mContext, MainActivity.class);
//                    startActivity(intent);
                    CommonUtils.gotoMainPage(mContext,Constants.Navigation.HOME_INDEX);
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
    private void share() {

        openShareWindow = new OpenShareWindow(ExhibitNewsDetailActivity.this);
        openShareWindow.show(ExhibitNewsDetailActivity.this);
        final String title = "看车玩车";
        final String description = mArticleModel.title;
        final String cover = URLUtil.builderImgUrl(mArticleModel.coverUrl, 540, 270);

        openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        OpenShareUtil.sendWXMoments(mContext, kPlayCarApp.getMsgApi(), title, description);
                        break;
                    case 1:
                        OpenShareUtil.sendWX(mContext, kPlayCarApp.getMsgApi(), title, description);
                        break;

                    case 2:
                        OpenShareUtil.shareSina(ExhibitNewsDetailActivity.this, title,
                                description);
                        break;
                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(kPlayCarApp.getTencent(), ExhibitNewsDetailActivity.this,
                                mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(kPlayCarApp.getTencent(), ExhibitNewsDetailActivity.this,
                                mBaseUIListener, title, cover, description);
                        break;

                }
                openShareWindow.dismiss();
            }
        });
    }
    private void operatorReport() {
        Map<String, String> params = new HashMap<String, String>();

        params.put("resource_id", mId + "");
        params.put("resource_type", "thread");
        params.put("vehicle_type", "car");
        params.put("token", IOUtils.getToken(mContext));
        mOperatorPresenter.isExistReport(params);
    }
    private void favorite() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("model", Constants.FavoriteTypes.GOODS);
        params.put("source_id", mArticleModel.id + "");
        params.put("title", mArticleModel.title);
        params.put("type", "car");
        params.put("token", IOUtils.getToken(mContext));
        if (isFav == 1) {
            mFavorPresenter.cancelFavoriteData(params);
        } else {
            mFavorPresenter.addFavoriteData(params);
        }
    }
    private int isFav = 0;
    private FavoriteView mFavoriteView = new FavoriteView() {
        @Override
        public void addFavoriteSuccess(Object data) {

            isFav = 1;
            CommonUtils.showToast(getContext(), "收藏成功");
            dataList.get(3).title="取消收藏";
            morePopupWindow.notifyDataSetChanged(dataList);
        }

        @Override
        public void cancelFavorite(Object data) {
            isFav = 0;
            dataList.get(3).title="收藏";
            morePopupWindow.notifyDataSetChanged(dataList);
//            updataFavView();
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
    private TopicOperatorView<DataItem> mTopicOperatorView = new TopicOperatorView<DataItem>() {
        @Override
        public void showData(DataItem item) {

            if (Constants.TopicParams.CAN_BE_REPORT == item.count) {

                Map<String, Serializable> params = new HashMap<String, Serializable>();

                params.put(Constants.IntentParams.ID, mId + "");
                params.put(Constants.IntentParams.TITLE, "@" + mArticleModel.author + ": " + mArticleModel.title);
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
    private void deleteTopic() {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("thread_id", mId + "");
        params.put("token", IOUtils.getToken(mContext));
        mDeleteTopicPresenter.deleteTopic(params);
    }
    private TopicDataPresenter mDeleteTopicPresenter;

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
    private void showLikeLists(LikeListModel likeListModel) {
        mLikeContent.setText("(" + likeListModel.pagination.count + ")");
        if (dianzanFrag == null) {
            dianzanFrag = new DianzanFrag(likeListModel.list);
        } else {
            dianzanFrag.notifylikeDataSetChange(likeListModel.list);
        }
    }
    @Override
    protected void setUpViews() {
        kPlayCarApp = (KPlayCarApp) getApplication();
        mId = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        commentFm = getSupportFragmentManager();
        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        mCommentPresenter.loadCommentList(mModule, mId + "", "car");
        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mLikeView);
        mLikePresenter.loadLikeList(mId + "", IOUtils.getToken(getContext()));

        mFavorPresenter = new FavorPresenter();
        mFavorPresenter.attachView(mFavoriteView);
        mDeleteTopicPresenter = new TopicDataPresenter();
        mDeleteTopicPresenter.attachView(mDeleteTopicDataView);
        mOperatorPresenter = new TopicOperatorPresenter();
        mOperatorPresenter.attachView(mTopicOperatorView);
        initView();
        exhibitionNewsDetailPresenter = new ExhibitionNewsDetailPresenter();
        exhibitionNewsDetailPresenter.attachView(this);
        exhibitionNewsDetailPresenter.getExhibitionActicle(mId);
        initHideMoreMenuData();
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        mTopicWebView = (WebView) findViewById(R.id.topicWebView);
        praiseIv = (ImageView) findViewById(R.id.favoriteImg);
        mLikeContent = (TextView) findViewById(R.id.goodcontent);
        commetNumTv = (TextView) findViewById(R.id.comment_numbers);
        WebSettings webSettings  = mTopicWebView.getSettings();
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mTopicWebView.setInitialScale(5);
//        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        mTopicWebView.measure(w, h);
//        int height =mTopicWebView.getMeasuredHeight();
//        int width =mTopicWebView.getMeasuredWidth();
        webSettings.setJavaScriptEnabled(true);
        mTopicWebView.setSaveEnabled(true);
//        mTopicWebView.addJavascriptInterface(new JavaScriptInterface(), "HTMLOUT");
//        mTopicWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                mTopicWebView.loadUrl("javascript:window.HTMLOUT.getContentWidth(document.getElementsByTagName('html')[0].scrollWidth);");
//                Logger.d("mTopicWebView2 "+mTopicWebView.getContentHeight());
//            }
//        });
        mCmtTitle = (TextView) findViewById(R.id.title);
        mLikeTitle = (TextView) findViewById(R.id.goodtitle);
        mBtmLine1 = (ImageView) findViewById(R.id.btmLine1);
        mBtmLine2 = (ImageView) findViewById(R.id.btmLine2);
        articleTitleTv = (TextView) findViewById(R.id.article_titletv);
        clickNumtv = (TextView) findViewById(R.id.click_numtv);
        articleDatetv = (TextView) findViewById(R.id.article_datetv);
        articleAuthorTv = (TextView) findViewById(R.id.article_authortv);
        commentNum = (TextView) findViewById(R.id.content);
        mCommentLayout = (RelativeLayout) findViewById(R.id.commentLayout);
        findViewById(R.id.repayLayout).setOnClickListener(this);
        mFavoriteImg = (ImageView) findViewById(R.id.favoriteImg);
        findViewById(R.id.headerLayout).setOnClickListener(this);
        View headGood = findViewById(R.id.headerGood);
        headGood.setOnClickListener(this);
        mShareImg = (ImageView) findViewById(R.id.shareImg);
        mCommentLayout.setOnClickListener(this);
        mFavoriteImg.setOnClickListener(this);
        mShareImg.setOnClickListener(this);
    }
    int   webviewContentWidth;

    class JavaScriptInterface {
        @android.webkit.JavascriptInterface
        public void getContentWidth(String value) {
            if (value != null) {
             int   webviewContentWidth = Integer.parseInt(value);
                Logger.d("Result from javascript: " + webviewContentWidth);
                Logger.d("Result from d: " + DensityUtils.getPhonewidth(getContext()));

            }
        }
    }

    @Override
    public void showDatas(Object obj) {
        CommentModel commentModel = (CommentModel) obj;
        commentNum.setText("(" + commentModel.count + ")");
        if(commentModel.count!=0){
            commetNumTv.setText(commentModel.count+"");
        }
        commentFrag = new CommentFrag(commentModel.comments, mId, mModule);
        commentTran = commentFm.beginTransaction();
        commentTran.replace(R.id.comment_fragfl, commentFrag);
        commentTran.commit();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.commentLayout:
                Intent toMoreComment = new Intent(mContext, CommentMoreActivity.class);
                toMoreComment.putExtra(Constants.IntentParams.ID, mId);
                toMoreComment.putExtra(Constants.IntentParams.TITLE, mModule);
                startActivityForResult(toMoreComment, REQUEST_CODE_COMMENT);
                break;
            case R.id.favoriteImg:
                if (IOUtils.isLogin(mContext)) {
                    if (mExecPraisePresenter == null) {
                        mExecPraisePresenter = new CommentListPresenter();
                        mExecPraisePresenter.attachView(mExecPraiseView);
                    }
                    if (mArticleModel != null)
                        mExecPraisePresenter.executePraise(mArticleModel.id + "", mModule, IOUtils.getToken(mContext));
                }
                break;
            case R.id.shareImg: {
                openShareWindow = new OpenShareWindow(getContext());
                openShareWindow.show(this);
                final String title = "看车玩车";
                final String description = "欢迎关注看车玩车";
                final String cover = Constants.ImageUrls.IMG_DEF_URL;
                openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        switch (position) {


                            case 0:
                                OpenShareUtil.sendWXMoments(mContext, kPlayCarApp.getMsgApi(), title,
                                        description);
                                break;
                            case 1:
                                OpenShareUtil.sendWX(mContext, kPlayCarApp.getMsgApi(), title,
                                        description);
                                break;
                            case 2:
                                WbShareHandler mWbHandler = OpenShareUtil.shareSina(ExhibitNewsDetailActivity.this, title, description);
                                break;

                            case 3:
                                ArrayList<String> urls = new ArrayList<String>();
                                urls.add(cover);
                                OpenShareUtil.shareToQzone(kPlayCarApp.getTencent(), ExhibitNewsDetailActivity.this,
                                        mBaseUIListener, urls, title, description);
                                break;
                            case 4:
                                OpenShareUtil.shareToQQ(kPlayCarApp.getTencent(), ExhibitNewsDetailActivity.this,
                                        mBaseUIListener, title, cover, description);
                                break;

                        }
                    }
                });
            }
            break;
            case R.id.repayLayout:
                Intent toIntent = new Intent(mContext, CommentEditorActivity.class);
                toIntent.putExtra(Constants.IntentParams.ID, mId + "");
                toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModule);
                startActivityForResult(toIntent, REQUEST_CODE_COMMENT);
                break;
            case R.id.headerLayout:
                mLikeTitle.setTextColor(mRes.getColor(R.color.text_more));
                mBtmLine2.setVisibility(View.INVISIBLE);
                mCmtTitle.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                mBtmLine1.setVisibility(View.VISIBLE);
                if (commentFrag != null) {
                    commentTran = commentFm.beginTransaction();
                    commentTran.replace(R.id.comment_fragfl, commentFrag);
                    commentTran.commit();
                }
                break;
            case R.id.headerGood:
                mCmtTitle.setTextColor(mRes.getColor(R.color.text_more));
                mBtmLine1.setVisibility(View.INVISIBLE);
                mLikeTitle.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                mBtmLine2.setVisibility(View.VISIBLE);
                if (dianzanFrag != null) {
                    commentTran = commentFm.beginTransaction();
                    commentTran.replace(R.id.comment_fragfl, dianzanFrag);
                    commentTran.commit();
                }
                break;

        }

    }
    private CommentListView<Object> mExecPraiseView = new CommentListView<Object>() {
        @Override
        public void showDatas(Object o) {
            if (mArticleModel != null) {
                if (mArticleModel.is_praise == 1) {
                    praiseIv.setImageResource(R.drawable.btn_heart1);
                    mArticleModel.is_praise = 0;
                } else {
                    praiseIv.setImageResource(R.drawable.btn_heart2);
                    mArticleModel.is_praise = 1;
                }
                mLikePresenter.loadLikeList(mArticleModel.id + "", IOUtils.getToken(mContext));
            }
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
    };
    @Override
    public void showArticle(ExhibitionNews article) {
        mArticleModel = article;
        articleTitleTv.setText(article.title);
        clickNumtv.setText(article.clicksNum + "");
        articleDatetv.setText(article.date);
        if(TextUtil.isEmpty(article.copyfrom)){
            articleAuthorTv.setText(article.author);
        }else{
            articleAuthorTv.setText(article.copyfrom + "/" + article.author);
        }

        URLUtil.loadWebData(mTopicWebView, WebviewUtil.getHtmlData(article.article));

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
    protected void onDestroy() {
        super.onDestroy();
        exhibitionNewsDetailPresenter.detachView();
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

}
