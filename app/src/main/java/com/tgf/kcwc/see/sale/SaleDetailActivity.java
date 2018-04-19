package com.tgf.kcwc.see.sale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.amap.LocationPreviewActivity;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.ReplyEditActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.comment.ReplyMoreActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.me.message.MessageActivity;
import com.tgf.kcwc.me.sale.MyExhibitionInfoActivity;
import com.tgf.kcwc.me.setting.FanKuiActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.Comment;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.SaleDetailModel;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.FavorPresenter;
import com.tgf.kcwc.mvp.presenter.OwnerSalePresenter;
import com.tgf.kcwc.mvp.presenter.TopicDataPresenter;
import com.tgf.kcwc.mvp.presenter.TopicOperatorPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.FavoriteView;
import com.tgf.kcwc.mvp.view.OwnerSaleDataView;
import com.tgf.kcwc.mvp.view.TopicDataView;
import com.tgf.kcwc.mvp.view.TopicOperatorView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.posting.TopicReportActivity;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.see.CarDetailActivity;
import com.tgf.kcwc.see.PhotoBrowserActivity;
import com.tgf.kcwc.see.SeriesDetailActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.LocationUtil;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.MultiImageView;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;
import com.tgf.kcwc.view.richeditor.MixedTextImageLayout;
import com.tgf.kcwc.view.richeditor.SEditorData;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/2/14 15:01
 * E-mail：fishloveqin@gmail.com
 */

public class SaleDetailActivity extends BaseActivity implements OwnerSaleDataView<SaleDetailModel> {

    protected SimpleDraweeView mTagHeaderImg;
    protected SimpleDraweeView mTagGenderImg;
    protected TextView mNickname;
    protected ImageView mTagImg1;
    protected ImageView mTagImg2;
    protected TextView mTagInfo;
    protected SimpleDraweeView mCurrentTagHeaderImg;
    protected SimpleDraweeView mCurrentGenderImg;
    protected TextView mCommentBtn;
    private TextView mTitle;
    private TextView mCreateTime;
    private TextView mPV;
    private TextView mPrice;
    private TextView mCallBtn;
    private TextView mSendMsgBtn;
    private TextView mMoreParamsBtn;
    private TextView mAddressPart1, mAddressPart2;
    private OwnerSalePresenter mPresenter;
    private TopicDataPresenter mTopicPresenter;
    private CommentListPresenter mCommentPresenter;
    private GridView mGridView, mPlayerGridView;
    //评论
    private NestFullListViewAdapter mCommentsadapter;
    //回复列表
    private NestFullListViewAdapter mReplyadapter;
    private int mId;
    //参展数据
    private RelativeLayout middleLayout, homeRl;
    private TextView exhibitionNameTv, exhibitionHallTv, exhibitionIntervalTv, exhibitionStartTv, exhibitionEnterTv, exhibitionLeaveTv, exhibitionStopTv;
    private SeekBar progressBar;

    private SimpleDraweeView mCover;
    protected NestFullListView mCommentList;
    private List<DataItem> mParamItems = new ArrayList<DataItem>();
    private WebView mTopicWebView;

    private RelativeLayout mNavLayout;

    private CommentListPresenter mLikePresenter;

    private RelativeLayout mTagLayout;
    private OpenShareWindow openShareWindow;
    private WbShareHandler mWbHandler;

    private String[] navValues = null;
    List<MorePopupwindowBean> dataList = new ArrayList<>();
    private FavorPresenter mFavorPresenter;
    private TopicOperatorPresenter mOperatorPresenter;
    private MorePopupWindow morePopupWindow = null;

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {

        setTitleBarDrawable(R.drawable.shape_titlebar_bg);
        backEvent(back);
        text.setText("车主自售");
        // text.setTextColor(mRes.getColor(R.color.text_color15));
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


    private MorePopupWindow.MoreOnClickListener moreOnClickListener = new MorePopupWindow.MoreOnClickListener() {
        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean item) {

            String title = item.title;
            switch (title) {
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

        params.put("resource_id", mId + "");
        params.put("resource_type", "thread");
        params.put("vehicle_type", "car");
        params.put("token", IOUtils.getToken(mContext));
        mOperatorPresenter.isExistReport(params);

    }

    private void favorite() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("model", Constants.FavoriteTypes.GOODS);
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

        params.put("thread_id", mId + "");
        params.put("token", IOUtils.getToken(mContext));
        mDeleteTopicPresenter.deleteTopic(params);
    }

    private TopicDataPresenter mDeleteTopicPresenter;

    private void share() {

        if (mModel == null) {
            CommonUtils.showToast(mContext, "数据正在加载中...");
            return;
        }
        final String title = mModel.title;
        final StringBuilder descriptionBuilder = new StringBuilder();
        final ArrayList<String> urls = new ArrayList<String>();
        for (SEditorData data : mModel.goods.describe.mEditorDatas) {
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                        OpenShareUtil.sendWXMoments(mContext, mGlobalContext.getMsgApi(), title,
                                description);
                        break;
                    case 1:
                        OpenShareUtil.sendWX(mContext, mGlobalContext.getMsgApi(), title,
                                description);
                        break;

                    case 2:
                        mWbHandler = OpenShareUtil.shareSina(SaleDetailActivity.this, title,
                                description);
                        break;

                    case 3:
                        OpenShareUtil.shareToQzone(mGlobalContext.getTencent(),
                                SaleDetailActivity.this, mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        String url = Constants.ImageUrls.IMG_DEF_URL;
                        if (urls.size() > 0) {
                            url = urls.get(0);
                        }
                        OpenShareUtil.shareToQQ(mGlobalContext.getTencent(),
                                SaleDetailActivity.this, mBaseUIListener, title, url, description);
                        break;

                }
            }
        });
        openShareWindow.show(SaleDetailActivity.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mWbHandler != null) {
            mWbHandler.doResultIntent(intent, new SinaWBCallback(mContext));
        }
    }

    @Override
    protected void setUpViews() {
        //初始化地图
        initView();

        mPresenter = new OwnerSalePresenter();
        mPresenter.attachView(this);
        mTopicPresenter = new TopicDataPresenter();
        mTopicPresenter.attachView(mTopicView);
        mCommentPresenter = new CommentListPresenter();
        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mExecLikeView);
        mCommentPresenter.attachView(mCommentView);
        mFavorPresenter = new FavorPresenter();
        mFavorPresenter.attachView(mFavoriteView);
        mDeleteTopicPresenter = new TopicDataPresenter();
        mDeleteTopicPresenter.attachView(mDeleteTopicDataView);

        mOperatorPresenter = new TopicOperatorPresenter();
        mOperatorPresenter.attachView(mTopicOperatorView);
        String[] arrays = mRes.getStringArray(R.array.config_params);

        for (String s : arrays) {
            DataItem d = new DataItem();
            d.title = s;
            mParamItems.add(d);
        }
    }


    private TopicOperatorView<DataItem> mTopicOperatorView = new TopicOperatorView<DataItem>() {
        @Override
        public void showData(DataItem item) {

            if (Constants.TopicParams.CAN_BE_REPORT == item.count) {

                Map<String, Serializable> params = new HashMap<String, Serializable>();

                params.put(Constants.IntentParams.ID, mId + "");
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

    private int isFav = 0;
    private FavoriteView mFavoriteView = new FavoriteView() {
        @Override
        public void addFavoriteSuccess(Object data) {
            isFav = 1;
            CommonUtils.showToast(getContext(), "收藏成功");
            mPresenter.loadOwnerSalesDetail(mId + "", IOUtils.getToken(mContext));
        }

        @Override
        public void cancelFavorite(Object data) {
            isFav = 0;
            CommonUtils.showToast(getContext(), "取消收藏");
            mPresenter.loadOwnerSalesDetail(mId + "", IOUtils.getToken(mContext));
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

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.loadOwnerSalesDetail(mId + "", IOUtils.getToken(mContext));
        mTopicPresenter.loadPlayerTopics(mId + "", "2");
        mCommentPresenter.loadCommentList("thread", mId + "", "car");
    }

    private TopicDataView<List<Topic>> mTopicView = new TopicDataView<List<Topic>>() {
        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public void showData(List<Topic> topics) {

            showPlayerLists(topics);
        }
    };

    private CommentListView<CommentModel> mCommentView = new CommentListView<CommentModel>() {
        @Override
        public void showDatas(CommentModel model) {

            showCommentLists(model);
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

    private void showCommentLists(final CommentModel model) {

        TextView contentTv = (TextView) findViewById(R.id.content);
        contentTv.setText("(" + model.count + ")");
        //评论列表
        mCommentsadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_comment,
                model.comments) {

            @Override
            public void onBind(final int pos, final Comment comment, NestFullViewHolder holder) {
                holder.setText(R.id.nametv, comment.senderInfo.nickName);
                holder.setText(R.id.comment_time, comment.time);
                holder.setText(R.id.commnt_good, comment.fabNum + "");
                holder.setText(R.id.reply_comments_tv, comment.repliesCount + "");
                holder.getView(R.id.commnt_good).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mLikePresenter.executePraise(comment.id + "", "comment", v,
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
                    mTagGenderImg.setImageResource(R.drawable.icon_women);
                } else {
                    mTagGenderImg.setImageResource(R.drawable.icon_men);
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
                            CommonUtils.customDisplayReplyText(msg, replyTv, mRes);
                        }

                    }
                };
                replyListview.setAdapter(mReplyadapter);
                replyListview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(NestFullListView parent, View view, int position) {
                        if (position >= 2) {

                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, comment.id);
                            CommonUtils.startNewActivity(mContext, args, ReplyMoreActivity.class);

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

    private void initWebViews() {
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

        mTopicWebView.loadUrl("http://www.2cto.com/kf/201508/431670.html");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        openShareWindow = new OpenShareWindow(this);
        super.setContentView(R.layout.activity_sale_detail_plus);

    }

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

    private ObservableScrollView mScrollView;
    private MixedTextImageLayout mRichLayout;
    private int mCarId;
    private String tel = "";
    private double[] locations = new double[2];
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            LocationUtil.reverseGeo(getApplicationContext(),
                    locations, mGeoListener);

        }
    };

    private void showRichContent(SaleDetailModel saleDetailModel) {

        mRichLayout.removeAllViews();//清空所有已添加的View

        if (saleDetailModel.goods.describe == null) {
            return;
        }
        List<SEditorData> dataList = saleDetailModel.goods.describe.mEditorDatas;
        for (SEditorData d : dataList) {

            String inputStr = d.getInputStr();
            String path = d.getImageUrl();
            if (!TextUtils.isEmpty(inputStr)) {
                mRichLayout.appendTextView(inputStr);
            }

            if (!TextUtils.isEmpty(path)) {

                mRichLayout.appendImageView(this, URLUtil.builderImgUrl(path, 540, 270));
            }

        }

    }

    private int mSeriesId = 0;
    private SaleDetailModel mModel;

    @Override
    public void showData(SaleDetailModel saleDetailModel) {


        mModel = saleDetailModel;
        initHideMoreMenuData();

        int isCollect = saleDetailModel.isCollect;

        if (isCollect == 0) {
            dataList.get(3).title = "收藏";
        } else {
            dataList.get(3).title = "取消收藏";
        }
        isFav = isCollect;
        morePopupWindow.getmCommonAdapter().notifyDataSetChanged();
        mAddress = saleDetailModel.address;
        mCover
                .setImageURI(Uri.parse(URLUtil.builderImgUrl(saleDetailModel.covers.get(0), 540, 270)));

        mTitle.setText(saleDetailModel.title);
        mCreateTime.setText(saleDetailModel.createTime);
        String price = CommonUtils.getMoneyType(saleDetailModel.goods.price + "");
        if (saleDetailModel.goods.price==0) {
            price = "面议";
            moneyTagTv.setText("");
        } else {
            moneyTagTv.setText("￥");
        }
        mPrice.setText(price);
        mPV.setText(saleDetailModel.viewCount + "");

        // 加载富文本信息

        showRichContent(saleDetailModel);
        // TODO: 2017/10/24 判断是否参展
        //加载地址信息
        String lat = saleDetailModel.goods.latitude;
        String lnt = saleDetailModel.goods.longitude;
        if (TextUtils.isEmpty(lat)) {
            lat = "29.001";
        }
        if (TextUtils.isEmpty(lnt)) {
            lnt = "103.222";
        }
        locations[0] = Double.parseDouble(lat);
        locations[1] = Double.parseDouble(lnt);
        //需要异步请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }).start();

        loadTagUserInfo(saleDetailModel.user);
        int index = 0;
        CarBean carBean = saleDetailModel.goods.car;
        mSeriesId = saleDetailModel.goods.series.id;
        mCarId = carBean.id;
        if (mCarId != 0) {
            mMoreParamsBtn.setText("标准车型参数>>");
            mParamItems.get(1).title = "车型名称";
        } else {
            mMoreParamsBtn.setText("车系详情>>");
            mParamItems.get(1).title = "车系";
        }
        for (DataItem d : mParamItems) {

            switch (index) {

                case 0:
                    d.content = saleDetailModel.goods.brand.name;
                    break;
                case 1:
                    if (mCarId != 0) {
                        d.content = carBean.name;
                    } else {
                        d.content = saleDetailModel.goods.series.name;
                    }

                    break;
                case 2:
                    d.content = saleDetailModel.goods.buyYear + "年";
                    break;
                case 3:

                    d.content = saleDetailModel.goods.roadHaul + "万公里";
                    break;

            }
            index++;
        }

        mMoreParamsBtn.setVisibility(View.VISIBLE);

        mMoreParamsBtn.setOnClickListener(this);
        mGridView.setAdapter(new WrapAdapter<DataItem>(mContext, mParamItems, R.layout.param_item) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView tv = helper.getView(R.id.title);
                String title = String.format(mRes.getString(R.string.config_item_value), item.title,
                        item.content);
                CommonUtils.customDisplayText3(mRes, tv, title, R.color.text_more);
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(mGridView, 2);

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

    private String mAddress = "";

    private TextView moneyTagTv;

    private void initView() {
        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        mScrollView.setScrollViewListener(mScrollListener);
        mTitle = (TextView) findViewById(R.id.title);
        mCreateTime = (TextView) findViewById(R.id.createTime);
        mPV = (TextView) findViewById(R.id.pViews);
        mPrice = (TextView) findViewById(R.id.price);
        moneyTagTv = (TextView) findViewById(R.id.moneyTag);
        mCallBtn = (TextView) findViewById(R.id.callBtn);
        mSendMsgBtn = (TextView) findViewById(R.id.sendMsgBtn);
        mMoreParamsBtn = (TextView) findViewById(R.id.moreParamBtn);
        mNavLayout = (RelativeLayout) findViewById(R.id.addressLayout);
        mNavLayout.setOnClickListener(this);
        mAddressPart1 = (TextView) findViewById(R.id.addressInfo1);
        mAddressPart2 = (TextView) findViewById(R.id.addressInfo2);
        mTopicWebView = (WebView) findViewById(R.id.topicWebView);
        mSendMsgBtn.setOnClickListener(this);
        mCallBtn.setOnClickListener(this);
        mRichLayout = (MixedTextImageLayout) findViewById(R.id.richContentLayout);
        // initWebViews();
        mTagHeaderImg = (SimpleDraweeView) findViewById(R.id.tagHeaderImg);
        mTagGenderImg = (SimpleDraweeView) findViewById(R.id.genderImg);
        mNickname = (TextView) findViewById(R.id.nickname);
        mTagImg1 = (ImageView) findViewById(R.id.tagImg1);
        mTagImg2 = (ImageView) findViewById(R.id.tagImg2);
        mTagInfo = (TextView) findViewById(R.id.tagInfo);
        mTagLayout = (RelativeLayout) findViewById(R.id.tagLayout);
        mTagLayout.setOnClickListener(this);
        mCommentList = (NestFullListView) findViewById(R.id.commentList);
        mCurrentTagHeaderImg = (SimpleDraweeView) findViewById(R.id.currentTagHeaderImg);
        mCurrentGenderImg = (SimpleDraweeView) findViewById(R.id.currentGenderImg);
        mCommentBtn = (TextView) findViewById(R.id.commentBtn);
        mCover = (SimpleDraweeView) findViewById(R.id.bigHeaderImg);
        mGridView = (GridView) findViewById(R.id.gridView);
        mPlayerGridView = (GridView) findViewById(R.id.playList);

        middleLayout = (RelativeLayout) findViewById(R.id.middleLayout);
        homeRl = (RelativeLayout) findViewById(R.id.homeRl);
        exhibitionNameTv = (TextView) findViewById(R.id.exhibitionNameTv);
        exhibitionHallTv = (TextView) findViewById(R.id.exhibitionHallTv);
        exhibitionIntervalTv = (TextView) findViewById(R.id.exhibitionIntervalTv);
        progressBar = (SeekBar) findViewById(R.id.progressBar);
        exhibitionStartTv = (TextView) findViewById(R.id.exhibitionStartTv);
        exhibitionEnterTv = (TextView) findViewById(R.id.exhibitionEnterTv);
        exhibitionLeaveTv = (TextView) findViewById(R.id.exhibitionLeaveTv);
        exhibitionStopTv = (TextView) findViewById(R.id.exhibitionStopTv);
        mPlayerGridView.setOnItemClickListener(mGoodsItemClick);
        mCommentBtn.setOnClickListener(this);
        homeRl.setOnClickListener(this);
        findViewById(R.id.commentHeaderLayout).setOnClickListener(this);
        findViewById(R.id.playerHeaderLayout).setOnClickListener(this);
        loadCurrentUserInfo();
    }

    private ObservableScrollView.ScrollViewListener mScrollListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                    int oldy) {

            int topHeight = BitmapUtils.dp2px(mContext, 132);
/*            if (y >= topHeight) {
                findViewById(R.id.titleBar).setBackgroundResource(R.color.style_bg1);
            } else {
                findViewById(R.id.titleBar).setBackgroundResource(R.drawable.shape_titlebar_bg);

            }*/
            if (y <= 0) {   //设置标题的背景颜色
                // textView.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
                findViewById(R.id.titleBar).setBackgroundColor(Color.argb((int) 0, 54, 169, 92));
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

    private void loadCurrentUserInfo() {
        Account a = IOUtils.getAccount(mContext);

        if (a != null) {
            mCurrentTagHeaderImg
                    .setImageURI(Uri.parse(URLUtil.builderImgUrl(a.userInfo.avatar, 144, 144)));
            if (a.userInfo.gender == 1) {
                mCurrentGenderImg.setImageResource(R.drawable.icon_men);
            } else {
                mCurrentGenderImg.setImageResource(R.drawable.icon_women);
            }
        }
    }

    private int mUserId = -1;

    private void loadTagUserInfo(Account.UserInfo user) {

        mUserId = user.userId;
        mTagHeaderImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(user.avatar, 100, 100)));

        if (user.sex == 1) {
            mTagGenderImg.setImageResource(R.drawable.icon_men);
        } else {
            mTagGenderImg.setImageResource(R.drawable.icon_women);
        }
        mNickname.setText(user.nick);

        if (user.isExpert == 1) {
            mTagImg1.setVisibility(View.VISIBLE);
        } else {
            mTagImg1.setVisibility(View.INVISIBLE);
        }
        if (user.is_model == 1) {
            mTagImg2.setVisibility(View.VISIBLE);
        } else {
            mTagImg2.setVisibility(View.INVISIBLE);
        }

        String tagInfo = String.format(mRes.getString(R.string.user_topic_tag), user.count + "");
        mTagInfo.setText(tagInfo);
    }

    /**
     * 车主自售列表
     *
     * @param topics
     */
    private void showPlayerLists(final List<Topic> topics) {

        final WrapAdapter<Topic> adapter = new WrapAdapter<Topic>(mContext, topics,
                R.layout.player_grid_item) {
            @Override
            public void convert(ViewHolder helper, Topic item) {

                helper.setImageByUrl(R.id.img, URLUtil.builderImgUrl(item.cover, 360, 360));
                helper.setText(R.id.title, item.title);

            }
        };
        mPlayerGridView.setAdapter(adapter);
        ViewUtil.setListViewHeightBasedOnChildren(mPlayerGridView, 3);
    }

    private AdapterView.OnItemClickListener mGoodsItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            Topic topic = (Topic) adapterView.getAdapter().getItem(position);

            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, topic.id);
            CommonUtils.startNewActivity(mContext, args, SaleDetailActivity.class);
            finish();
        }
    };

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.moreParamBtn:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                if (mCarId != 0) {

                    args.put(Constants.IntentParams.ID, mCarId);
                    CommonUtils.startNewActivity(mContext, args, CarDetailActivity.class);
                } else {
                    args.put(Constants.IntentParams.ID, mSeriesId);
                    CommonUtils.startNewActivity(mContext, args, SeriesDetailActivity.class);
                }

                break;

            case R.id.callBtn:
                if (mModel == null) {
                    return;
                }
                tel = mModel.goods.phone;
                SystemInvoker.launchDailPage(mContext, tel);
                break;
            case R.id.sendMsgBtn:

                break;
            case R.id.addressLayout:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.LAT, locations[0] + "");
                args.put(Constants.IntentParams.LNG, locations[1] + "");
                args.put(Constants.IntentParams.DATA, "");
                args.put(Constants.IntentParams.DATA2, mAddress);
                CommonUtils.startNewActivity(mContext, args, LocationPreviewActivity.class);

                break;

            case R.id.aMap:

                try {
                    URLUtil.launcherInnerNavAMap(this, locations[0] + "", locations[1] + "");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.baiduMap:
                try {
                    URLUtil.launcherInnerNavBaidu(this, locations[0] + "", locations[1] + "");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.commentHeaderLayout:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mId + "");
                CommonUtils.startNewActivity(mContext, args, CommentEditorActivity.class);
                break;

            case R.id.tagLayout:

                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.ID, mUserId);
                intent.setClass(mContext, UserPageActivity.class);
                startActivity(intent);
                break;

            case R.id.playerHeaderLayout:

                KPlayCarApp.putValue(Constants.IntentParams.INDEX, 3);
                finish();

                break;
            case R.id.homeRl://展会
                Intent intent1 = new Intent(this, MyExhibitionInfoActivity.class);
//                intent1.putExtra(Constants.IntentParams.ID,applyId);
                startActivity(intent1);
                break;
        }

    }

    GeocodeSearch.OnGeocodeSearchListener mGeoListener = new GeocodeSearch.OnGeocodeSearchListener() {

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult,
                                        int i) {
            if (regeocodeResult != null) {
                RegeocodeAddress regeocodeAddress = regeocodeResult
                        .getRegeocodeAddress();

                regeocodeAddress.getRoads();
                regeocodeAddress
                        .getStreetNumber();
                regeocodeAddress
                        .getCrossroads();
                String address1 = regeocodeAddress
                        .getProvince() + " | "
                        + regeocodeAddress
                        .getDistrict();
                String address2 = regeocodeResult
                        .getRegeocodeAddress()
                        .getTownship() + regeocodeAddress
                        .getStreetNumber()
                        .getNumber();
                mAddressPart1.setText(address1);
                mAddressPart2.setText(address2);
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult,
                                      int i) {

        }
    };

    //点赞
    private CommentListView<View> mExecLikeView = new CommentListView<View>() {
        @Override
        public void showDatas(View v) {

            //                                                                CommonUtils.showToast(mContext,
            //                                                                    "谢谢您的支持");

            mCommentPresenter.loadCommentList(
                    "thread", mId + "", "car");

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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (openShareWindow != null) {
            openShareWindow.dismiss();
        }
    }
}
