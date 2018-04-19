package com.tgf.kcwc.see.exhibition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.comment.CommentFrag;
import com.tgf.kcwc.comment.CommentMoreActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.me.message.MessageActivity;
import com.tgf.kcwc.me.setting.FanKuiActivity;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.ExhibitEvent;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.ExhibitEventDetailPresenter;
import com.tgf.kcwc.mvp.presenter.FavorPresenter;
import com.tgf.kcwc.mvp.presenter.TopicDataPresenter;
import com.tgf.kcwc.mvp.presenter.TopicOperatorPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.ExhibitEventDetailView;
import com.tgf.kcwc.mvp.view.FavoriteView;
import com.tgf.kcwc.mvp.view.TopicDataView;
import com.tgf.kcwc.mvp.view.TopicOperatorView;
import com.tgf.kcwc.posting.TopicReportActivity;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/1/22 0022
 * E-mail:hekescott@qq.com
 */

public class ExhibitionEventDetailActivity extends BaseActivity
                                           implements ExhibitEventDetailView, CommentListView {
    private static final int            REQUEST_CODE_REPLY   = 1;
    private static final int            REQUEST_CODE_COMMENT = 102;
    private NestFullListView            motodetailCommentLv;
    private NestFullListViewAdapter     replyadapter;
    private NestFullListViewAdapter     commentsadapter;
    private ArrayList                   loopHead             = new ArrayList();
    private ListView                    guestLv;
    private ListView                    qonzeLv;
    private ExhibitEventDetailPresenter exhibitEventDetailPresenter;
    private TextView                    mEvetdetailTitle;
    private TextView                    mEvetdetailTime;
    private TextView                    mEvetdetailNum;
    private TextView                    mEvetdetailDesc;
    private WebView                     webView;
    private TextView                    detailPicNum;
    private SimpleDraweeView            coverIv;
    private CommentListPresenter        mCommentPresenter;
    private String                      mModule              = "event_activity";
    private FragmentManager             commentFm;
    private CommentFrag                 commentFrag;
    private FragmentTransaction         commentTran;
    private int                         mId;
    private TextView                    commentNumTv;
    private OpenShareWindow             openShareWindow;
    private KPlayCarApp                 kPlayCarApp;
    private CommentListPresenter        mExecLikePresenter;
    private RelativeLayout              qzoneRl;
    private RelativeLayout              guestRl;
    private ObservableScrollView mScrollView;
    private MorePopupWindow morePopupWindow = null;
    private FavorPresenter mFavorPresenter;
    private TopicOperatorPresenter mOperatorPresenter;

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
       final View moreBtn  =  findViewById(R.id.exhibitevent_moreiv);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (morePopupWindow != null) {
                    morePopupWindow.showPopupWindow(moreBtn);
                }

            }
        });
    }

    @Override
    protected void setUpViews() {
        commentFm = getSupportFragmentManager();
        kPlayCarApp = (KPlayCarApp) getApplication();
        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        findViewById(R.id.repayLayout).setOnClickListener(this);
        findViewById(R.id.replayImg).setOnClickListener(this);
        findViewById(R.id.favoriteImg).setOnClickListener(this);
        findViewById(R.id.shareImg).setOnClickListener(this);
        qzoneRl = (RelativeLayout) findViewById(R.id.qzone_rl);
        guestRl = (RelativeLayout) findViewById(R.id.guest_rl);
        //        mExecLikePresenter = new CommentListPresenter();

        //        mExecLikePresenter.attachView(mExecLikeView);
        mScrollView = (ObservableScrollView)  findViewById(R.id.event_detailscroll);
        mScrollView.setScrollViewListener(mScrollListener);
        mId = getIntent().getIntExtra(Constants.IntentParams.ID, 11);
        coverIv = (SimpleDraweeView) findViewById(R.id.eventdetal_cover);
        mEvetdetailTitle = (TextView) findViewById(R.id.evetdetail_title);
        mEvetdetailTime = (TextView) findViewById(R.id.evetdetail_time);
        mEvetdetailNum = (TextView) findViewById(R.id.evetdetail_num);
        mEvetdetailDesc = (TextView) findViewById(R.id.evetdetail_desc);
        webView = (WebView) findViewById(R.id.topicWebView);
        detailPicNum = (TextView) findViewById(R.id.motodetail_tv_picnum);
        detailPicNum.setOnClickListener(this);
        guestLv = (ListView) findViewById(R.id.eventdetail_guestlist);
        qonzeLv = (ListView) findViewById(R.id.eventdetail_qzone_list);
        exhibitEventDetailPresenter = new ExhibitEventDetailPresenter();
        exhibitEventDetailPresenter.attachView(this);
        exhibitEventDetailPresenter.getExhibitionEventDetail(mId);
        mCommentPresenter.loadCommentList(mModule, mId + "", "car");

        mFavorPresenter = new FavorPresenter();
        mFavorPresenter.attachView(mFavoriteView);

        mOperatorPresenter = new TopicOperatorPresenter();
        mOperatorPresenter.attachView(mTopicOperatorView);

        commentNumTv = (TextView) findViewById(R.id.comment_numbers);
        initData();
        initHideMoreMenuData();
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

        openShareWindow = new OpenShareWindow(ExhibitionEventDetailActivity.this);
        openShareWindow.show(ExhibitionEventDetailActivity.this);
        final String title = "看车玩车";
        final String description = mExhibitEvent.title;
        final String cover = URLUtil.builderImgUrl(mExhibitEvent.cover, 540, 270);

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
                        OpenShareUtil.shareSina(ExhibitionEventDetailActivity.this, title,
                                description);
                        break;
                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(kPlayCarApp.getTencent(), ExhibitionEventDetailActivity.this,
                                mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(kPlayCarApp.getTencent(), ExhibitionEventDetailActivity.this,
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
        params.put("source_id", mExhibitEvent.id + "");
        params.put("title", mExhibitEvent.title);
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
                params.put(Constants.IntentParams.TITLE, "@" + mExhibitEvent.hosts + ": " + mExhibitEvent.title);
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibiteventdetail);
    }

    private void initData() {
        loopHead.clear();
    }
    private ObservableScrollView.ScrollViewListener mScrollListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                    int oldy) {
            int topHeight = BitmapUtils.dp2px(mContext, 132);
            if (y <= 0) {   //设置标题的背景颜色
                findViewById(R.id.title_layout).setBackgroundColor(Color.argb((int) 0, 54, 169, 92));
            } else if (y > 0 && y <= topHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                float scale = (float) y / topHeight;
                float alpha = (255 * scale);
                findViewById(R.id.title_layout).setBackgroundColor(Color.argb((int) alpha, 54, 169, 92));
            } else {    //滑动到banner下面设置普通颜色
                findViewById(R.id.title_layout).setBackgroundColor(Color.argb((int) 255, 54, 169, 92));
            }
        }
    };
    ExhibitEvent mExhibitEvent;
    @Override
    public void showHead(ExhibitEvent exhibitEvent) {
        mExhibitEvent = exhibitEvent;
        mEvetdetailTitle.setText(exhibitEvent.title);
        mEvetdetailTime.setText(exhibitEvent.startTime + " - " + exhibitEvent.endTime);
        mEvetdetailNum.setText(exhibitEvent.hallName+" "+exhibitEvent.boothName);
        String cover = URLUtil.builderImgUrl(exhibitEvent.cover, 414, 310);
        coverIv.setImageURI(Uri.parse(cover));
        mEvetdetailDesc.setText(
            exhibitEvent.hosts.replace(",", "\n") + "\n" + exhibitEvent.organizers + "\n" + exhibitEvent.sponsors);
        if (exhibitEvent.guestlist != null) {

            showGuestList(exhibitEvent.guestlist);
        }
        if (exhibitEvent.qzoneList != null) {

            showQzoneList(exhibitEvent.qzoneList);
        }
        detailPicNum.setText(" " + exhibitEvent.imgCount + "张");
        URLUtil.loadWebData(webView, WebviewUtil.getHtmlData(exhibitEvent.html));
    }

    @Override
    public void showGuestList(ArrayList<ExhibitEvent.Guest> guestArrayList) {
        if (guestArrayList == null || guestArrayList.size() == 0) {
            guestRl.setVisibility(View.GONE);
        }
        guestLv.setAdapter(new WrapAdapter<ExhibitEvent.Guest>(mContext, guestArrayList,
            R.layout.listitem_eventdetail_guest) {
            @Override
            public void convert(ViewHolder helper, ExhibitEvent.Guest item) {
                String avatar = URLUtil.builderImgUrl(item.avatar, 144, 144);
                helper.setSimpleDraweeViewURL(R.id.eventdetail_guestavatar, avatar);
                helper.setText(R.id.eventdetail_guestname, item.name);
                helper.setText(R.id.eventdetail_guesttitle, item.title);
            }
        });

        ViewUtil.setListViewHeightBasedOnChildren(guestLv);
    }

    @Override
    public void showQzoneList(ArrayList<ExhibitEvent.QzoneItem> qzoneArrayList) {
        if (qzoneArrayList == null || qzoneArrayList.size() == 0) {
            qzoneRl.setVisibility(View.GONE);
        }
        qonzeLv.setAdapter(new WrapAdapter<ExhibitEvent.QzoneItem>(mContext, qzoneArrayList,
            R.layout.listitem_eventdetail_qzone) {
            @Override
            public void convert(ViewHolder helper, ExhibitEvent.QzoneItem item) {
                String cover = URLUtil.builderImgUrl(item.cover, 270, 203);
                helper.setSimpleDraweeViewURL(R.id.eventdetail_qozavatar, cover);
                helper.setText(R.id.eventdtail_qoztitle, item.title);
                helper.setText(R.id.eventdtail_qoztime, item.createtime);
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(qonzeLv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.motodetail_tv_picnum:
                Intent toIntent = new Intent(getContext(), ExhibitEventPicsActivity.class);
                toIntent.putExtra(Constants.IntentParams.ID, mId);
                startActivity(toIntent);
                break;
            case R.id.repayLayout:
                Intent toCommentIntent = new Intent(mContext, CommentEditorActivity.class);
                toCommentIntent.putExtra(Constants.IntentParams.ID, mId + "");
                toCommentIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModule);
                startActivityForResult(toCommentIntent, REQUEST_CODE_COMMENT);
                break;
            case R.id.replayImg:
                Intent toMoreComment = new Intent(mContext, CommentMoreActivity.class);
                toMoreComment.putExtra(Constants.IntentParams.ID, mId);
                toMoreComment.putExtra(Constants.IntentParams.TITLE, mModule);
                startActivityForResult(toMoreComment, REQUEST_CODE_COMMENT);
                break;
            case R.id.favoriteImg:
                CommonUtils.showToast(getContext(), "点赞");
                //
                //                mExecLikePresenter.executePraise(mId + "", mModule,
                //                        IOUtils.getToken(mContext));
                break;
            case R.id.shareImg: {
                openShareWindow = new OpenShareWindow(getContext());
                openShareWindow.show(ExhibitionEventDetailActivity.this);
                final String title = "看车玩车";
                final String description = "欢迎关注看车玩车";
                final String cover = Constants.ImageUrls.IMG_DEF_URL;

                openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        switch (position) {

                            case 0:
                                OpenShareUtil.sendWXMoments(mContext, kPlayCarApp.getMsgApi(),
                                    title, description);
                                break;
                            case 1:
                                OpenShareUtil.sendWX(mContext, kPlayCarApp.getMsgApi(), title,
                                    description);
                                break;

                            case 2:
                                WbShareHandler mWbHandler = OpenShareUtil.shareSina(
                                    ExhibitionEventDetailActivity.this, title, description);
                                break;
                            case 3:
                                ArrayList<String> urls = new ArrayList<String>();
                                urls.add(cover);
                                OpenShareUtil.shareToQzone(kPlayCarApp.getTencent(),
                                    ExhibitionEventDetailActivity.this, mBaseUIListener, urls,
                                    title, description);
                                break;
                            case 4:
                                OpenShareUtil.shareToQQ(kPlayCarApp.getTencent(),
                                    ExhibitionEventDetailActivity.this, mBaseUIListener, title,
                                    cover, description);
                                break;
                        }
                    }
                });
            }
                break;

            default:
                break;
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
        return this;
    }

    @Override
    public void showDatas(Object obj) {
        CommentModel commentModel = (CommentModel) obj;
        commentNumTv.setText(commentModel.count + "");
        commentFrag = new CommentFrag(commentModel.comments, mId, mModule);
        commentTran = commentFm.beginTransaction();
        commentTran.replace(R.id.comment_fragfl, commentFrag);
        commentTran.commit();
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
