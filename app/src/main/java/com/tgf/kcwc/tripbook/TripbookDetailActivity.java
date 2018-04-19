package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.comment.CommentFrag;
import com.tgf.kcwc.comment.CommentMoreActivity;
import com.tgf.kcwc.comment.DianzanFrag;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.AttrationEntity;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.LikeListModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.RideDataItem;
import com.tgf.kcwc.mvp.model.RideDataModel;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.model.TripBookDetailModel;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.FavorPresenter;
import com.tgf.kcwc.mvp.presenter.TirpBookDetailPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.FavoriteView;
import com.tgf.kcwc.mvp.view.TripBookDetailView;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.MyGridView;
import com.tgf.kcwc.view.NotitleContentDialog;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.VerticalProgressBar;
import com.tgf.kcwc.view.link.Link;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;
import com.tgf.kcwc.view.richeditor.SEditorData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/5/10 0010
 * E-mail:hekescott@qq.com
 */

public class TripbookDetailActivity extends BaseActivity
        implements TripBookDetailView, CommentListView, FavoriteView {
    private final int REQUEST_CODE_COMMENT = 102;
    private String token = "";
    private TirpBookDetailPresenter tripPresenter;

    private int progressColorsIds[] = {R.color.progres_green,
            R.color.progress_red,
            R.color.progress_blue,
            R.color.progress_gray,
            R.color.progress_yellow,
            R.color.progress_yellow};
    private NestFullListView nodeLv;
    private GridView mRideReportGV;
    private WrapAdapter mRideReportAdapter;
    private KPlayCarApp kPlayerApp;
    private FrameLayout commentFL;
    private FragmentManager commentFm;
    private Fragment commentFrag;
    private FragmentTransaction commentTran;
    private DianzanFrag dianzanFrag;
    private TextView mCmtTitle;
    private View mBtmLine1;
    private TextView mLikeTitle;
    private View mBtmLine2;
    private CommentListPresenter mCommentPresenter;
    private TextView mCmtContent;
    private int mId = 428;
    private int mBookId = 51;
    private CommentListPresenter mLikePresenter;
    private ArrayList<MorePopupwindowBean> dataList = new ArrayList<>();
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
    private TextView mLikeContent;
    private String mModule = "thread";
    private OpenShareWindow openShareWindow;
    private int nodeCount;
    private CommentListPresenter mExecLikePresenter;
    private TripBookDetailModel mTripBookDetailModel;
    private ImageView praiseIv;
    private WbShareHandler mWbHandler;
    private int isFav;
    //    private MorePopupWindow.MoreOnClickListener mMoreOnClickListener;
    private MorePopupWindow.MoreOnClickListener mMoreOnClickListener = new MorePopupWindow.MoreOnClickListener() {
        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean item) {
            switch (dataList.get(position).title) {
                case "首页":
                    finish();
                    break;
                case "消息":
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
                    break;
                case "编辑":
                    if (unStopDialog == null) {
                        Intent toCreateIntent = new Intent(getContext(), EditTripbookActivity.class);
                        toCreateIntent.putExtra(Constants.IntentParams.ID, mTripBookDetailModel.ride_id);
                        startActivity(toCreateIntent);
                    } else {
                        unStopDialog.show();
                    }
                    break;
                case "删除":
                    tripPresenter.deleteTripBook(mBookId, token);
                    break;
                case "举报":
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
    private TextView favTextView;

    private void favorite() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("model", "roadbook");
        params.put("source_id", mBookId + "");
        params.put("title", mTripBookDetailModel.title);
        params.put("type", "car");
        params.put("token", IOUtils.getToken(mContext));
        if (isFav == 1) {
            mFavorPresenter.cancelFavoriteData(params);
        } else {
            mFavorPresenter.addFavoriteData(params);
        }
    }

    private MorePopupWindow morePopupWindow;
    private Intent toDringIntent;
    private NotitleContentDialog unStopDialog;
    private FavorPresenter mFavorPresenter;
    private ScrollView tripdetailScrol;
    private TextView titleBarTv;
    private int threadId;
    private CommentListPresenter mExecPraisePresenter;
    private String url;
    private SimpleDraweeView avatarIv;

    @Override
    protected void setUpViews() {
        mBookId = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        token = IOUtils.getToken(getContext());
        tripPresenter = new TirpBookDetailPresenter();
        tripPresenter.attachView(this);
        mFavorPresenter = new FavorPresenter();
        mFavorPresenter.attachView(this);
        nodeLv = (NestFullListView) findViewById(R.id.tripbookdetail_nodelv);
        mRideReportGV = (GridView) findViewById(R.id.tripbookdetail_gridView);
        mCmtContent = (TextView) findViewById(R.id.cmtContent);
        mLikeContent = (TextView) findViewById(R.id.likeContent);
        favTextView = findViewById(R.id.tripbook_detail_downloadtv);
        favTextView.setOnClickListener(this);
        tripdetailScrol = (ScrollView) findViewById(R.id.tripdetail_scrollv);
        findViewById(R.id.tripbookdetail_getcoupontv).setOnClickListener(this);
        findViewById(R.id.tripdetail_commentiv).setOnClickListener(this);


        praiseIv = (ImageView) findViewById(R.id.tripdetail_careiv);
        praiseIv.setOnClickListener(this);

        findViewById(R.id.tripdetail_shareiv).setOnClickListener(this);
        findViewById(R.id.repayLayout).setOnClickListener(this);
        findViewById(R.id.mapgoiv).setOnClickListener(this);
        findViewById(R.id.recordIv).setOnClickListener(this);
        findViewById(R.id.lineDataIv).setOnClickListener(this);

        kPlayerApp = (KPlayCarApp) getApplication();
        tripPresenter.getTripbookDetail(mBookId + "", token);
        commentFL = (FrameLayout) findViewById(R.id.comment_fragfl);
        findViewById(R.id.tripbook_likeBtnLayout).setOnClickListener(this);
        findViewById(R.id.commentBtnLayout).setOnClickListener(this);
        mCmtTitle = (TextView) findViewById(R.id.cmtTitle);
        mLikeTitle = (TextView) findViewById(R.id.likeTitle);
        mBtmLine1 = findViewById(R.id.btmLine1);
        mBtmLine2 = findViewById(R.id.btmLine2);
        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        mId = mBookId;
        morePopupWindow = new MorePopupWindow(TripbookDetailActivity.this,
                dataList, mMoreOnClickListener);
        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mLikeView);
        commentFm = getSupportFragmentManager();
        toDringIntent = new Intent(getContext(), DrivingHomeActivity.class);
        toDringIntent.putExtra(Constants.IntentParams.DATA, Constants.IntentParams.RIDE_BOOK_MODULE);

    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        titleBarTv = text;
        backEvent(back);
        function.setImageResource(R.drawable.global_nav_n);

        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataList != null && dataList.size() != 0) {
                    morePopupWindow.showPopupWindow(function);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripdetail);
        initGridData();


    }

    public void share() {
        openShareWindow = new OpenShareWindow(TripbookDetailActivity.this);
        openShareWindow.show(TripbookDetailActivity.this);
        final String title = "看车玩车";
        final String description = mTripBookDetailModel.title;
        final String cover = URLUtil.builderImgUrl(mTripBookDetailModel.cover, 540, 270);

        openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        OpenShareUtil.sendWXMoments(mContext, kPlayerApp.getMsgApi(), title, description);
                        break;
                    case 1:
                        OpenShareUtil.sendWX(mContext, kPlayerApp.getMsgApi(), title, description);
                        break;

                    case 2:
                        mWbHandler = OpenShareUtil.shareSina(TripbookDetailActivity.this, title,
                                description);
                        break;
                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(kPlayerApp.getTencent(), TripbookDetailActivity.this,
                                mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(kPlayerApp.getTencent(), TripbookDetailActivity.this,
                                mBaseUIListener, title, cover, description);
                        break;

                }
                openShareWindow.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tripbook_likeBtnLayout:
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
            case R.id.commentBtnLayout:
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
            case R.id.tripbookdetail_getcoupontv:
                Intent toTripBookAct2 = new Intent(getContext(), TripBookMapActivity.class);
                toTripBookAct2.putExtra(Constants.IntentParams.ID, mBookId);
                toTripBookAct2.putExtra(Constants.IntentParams.INDEX, -1);
                startActivity(toTripBookAct2);
                break;
            case R.id.lineDataIv://线路数据
                Intent toTripBookAct3 = new Intent(getContext(), TripBookLineDataActivity.class);
                toTripBookAct3.putExtra(Constants.IntentParams.ID, mBookId);
                toTripBookAct3.putExtra(Constants.IntentParams.LAT, mTripBookDetailModel.lat);
                toTripBookAct3.putExtra(Constants.IntentParams.LNG, mTripBookDetailModel.lng);
                toTripBookAct3.putExtra(Constants.IntentParams.INDEX, -1);
//                toTripBookAct3.putExtra(Constants.IntentParams.LONGITUDE, mTripBookDetailModel.top);
//                toTripBookAct3.putExtra(Constants.IntentParams.LATITUDE, mBookId);
                startActivity(toTripBookAct3);
                break;
            case R.id.mapgoiv:
                Intent toTripBookAct = new Intent(getContext(), TripBookMapActivity.class);
                toTripBookAct.putExtra(Constants.IntentParams.ID, mBookId);
//                toTripBookAct.putExtra(Constants.IntentParams.INDEX, -1);
                startActivity(toTripBookAct);
                break;
            case R.id.recordIv://我的记录
                Intent recordIntent = new Intent(getContext(), TripBookRecordActivity.class);
                recordIntent.putExtra(Constants.IntentParams.ID, mBookId);
                recordIntent.putExtra(Constants.IntentParams.DATA, url);
                startActivity(recordIntent);
                break;
            case R.id.tripbook_detail_downloadtv:
                if (IOUtils.isLogin(getContext())) {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    params.put("model", "roadbook");
//                    params.put("source_id", mBookId + "");
//                    params.put("title", mTripBookDetailModel.title);
//                    params.put("type", "car");
//                    params.put("token", IOUtils.getToken(mContext));
//                    if (isFav == 1) {
//                        mFavorPresenter.cancelFavoriteData(params);
//                    } else {
//                        mFavorPresenter.addFavoriteData(params);
//                    }
                    favorite();
                }
                break;
            case R.id.tripdetail_shareiv:
                openShareWindow = new OpenShareWindow(getContext());
                openShareWindow.show(TripbookDetailActivity.this);
                final String title = "看车玩车";
                final String description = "欢迎关注看车玩车";
                final String cover = Constants.ImageUrls.IMG_DEF_URL;

                openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        switch (position) {

                            case 0:
                                OpenShareUtil.sendWXMoments(mContext, kPlayerApp.getMsgApi(), title,
                                        description);
                                break;
                            case 1:
                                OpenShareUtil.sendWX(mContext, kPlayerApp.getMsgApi(), title,
                                        description);
                                break;
                            case 2:
                                WbShareHandler mWbHandler = OpenShareUtil
                                        .shareSina(TripbookDetailActivity.this, title, description);
                                break;

                            case 3:
                                ArrayList<String> urls = new ArrayList<String>();
                                urls.add(cover);
                                OpenShareUtil.shareToQzone(kPlayerApp.getTencent(),
                                        TripbookDetailActivity.this, mBaseUIListener, urls, title,
                                        description);
                                break;

                            case 4:
                                OpenShareUtil.shareToQQ(kPlayerApp.getTencent(),
                                        TripbookDetailActivity.this, mBaseUIListener, title, cover,
                                        description);
                                break;
                        }
                    }
                });
                break;
            case R.id.tripdetail_careiv:
                if (IOUtils.isLogin(mContext)) {
                    if (mExecPraisePresenter == null) {
                        mExecPraisePresenter = new CommentListPresenter();
                        mExecPraisePresenter.attachView(mExecPraiseView);
                    }
                    if (mTripBookDetailModel != null)
                        mExecPraisePresenter.executePraise(mTripBookDetailModel.thread_id + "", mModule, IOUtils.getToken(mContext));
                }
                break;
            case R.id.tripdetail_commentiv:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.TITLE, mModule);
                args.put(Constants.IntentParams.ID, mTripBookDetailModel.thread_id);
                CommonUtils.startNewActivity(mContext, args, CommentMoreActivity.class);
                break;
            case R.id.repayLayout:
                Intent toIntent = new Intent(mContext, CommentEditorActivity.class);
                toIntent.putExtra(Constants.IntentParams.ID, mTripBookDetailModel.thread_id + "");
                toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModule);
                startActivityForResult(toIntent, REQUEST_CODE_COMMENT);
                break;
            default:
                break;
        }
    }

    //帖子点赞
//    private CommentListView<List<String>> mExecLikeView = new CommentListView<List<String>>() {
//        @Override
//        public void showDatas(List<String> strings) {
//
//            if (mTripBookDetailModel != null) {
//                if (mTripBookDetailModel.is_praise == 1) {
//                    praiseIv.setImageResource(R.drawable.btn_heart1);
//                    mTripBookDetailModel.is_praise = 0;
//                } else {
//                    praiseIv.setImageResource(R.drawable.btn_heart2);
//                    mTripBookDetailModel.is_praise = 1;
//                }
//                mLikePresenter.loadLikeList(mTripBookDetailModel.thread_id+"", IOUtils.getToken(mContext));
//            }
//
//        }
//
//        @Override
//        public void setLoadingIndicator(boolean active) {
//            showLoadingIndicator(active);
//        }
//
//        @Override
//        public void showLoadingTasksError() {
//
//        }
//
//        @Override
//        public Context getContext() {
//            return mContext;
//        }
//    };
    private CommentListView<Object> mExecPraiseView = new CommentListView<Object>() {
        @Override
        public void showDatas(Object o) {
            if (mTripBookDetailModel != null) {
                if (mTripBookDetailModel.is_praise == 1) {
                    praiseIv.setImageBitmap(BitmapFactory.decodeResource(mRes,R.drawable.btn_heart1));
                    mTripBookDetailModel.is_praise = 0;
                } else {
//                    praiseIv.setImageResource(R.drawable.btn_heart2);
                    praiseIv.setImageBitmap(BitmapFactory.decodeResource(mRes,R.drawable.btn_heart2));
                    mTripBookDetailModel.is_praise = 1;
                }
                mLikePresenter.loadLikeList(mTripBookDetailModel.thread_id + "", IOUtils.getToken(mContext));
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
    public void showTitle(TripBookDetailModel tripBookDetailModel) {
        mTripBookDetailModel = tripBookDetailModel;
        threadId = tripBookDetailModel.thread_id;
        isFav =tripBookDetailModel.is_collect;
        mLikePresenter.loadLikeList(tripBookDetailModel.thread_id + "", token);
        mCommentPresenter.loadCommentList(mModule, tripBookDetailModel.thread_id + "", "car");
        TextView tvTitle = (TextView) findViewById(R.id.tripdetail_titletv);
        tvTitle.setText(tripBookDetailModel.title);
        titleBarTv.setText(tripBookDetailModel.title);
        TextView tripBookDetailTv = (TextView) findViewById(R.id.tripbookdetail_content);
        tripBookDetailTv.setText(tripBookDetailModel.content);
        TextView addressTv = (TextView) findViewById(R.id.tripdetail_addresstv);
        addressTv.setText(tripBookDetailModel.address);
        TextView distanceTv = (TextView) findViewById(R.id.tripdetail_distanceTv);
        distanceTv.setText(tripBookDetailModel.getDistance(kPlayerApp));
        TextView startTv = (TextView) findViewById(R.id.rideStartTV);
        startTv.setText(tripBookDetailModel.start_adds);
        TextView endTv = (TextView) findViewById(R.id.rideEndTV);
        endTv.setText(tripBookDetailModel.end_adds);
        ImageView imageview = (ImageView) findViewById(R.id.map_startendiv);
        TextView startTimeTv = (TextView) findViewById(R.id.rideStartTimeTV);
        startTimeTv.setText(tripBookDetailModel.startTime);
        TextView endTimeTv = (TextView) findViewById(R.id.rideEndTimeTV);
        endTimeTv.setText(tripBookDetailModel.endTime);
        url = URLUtil.builderImgUrl750(tripBookDetailModel.cover);
        Glide.with(getContext()).load(url).into(imageview);
        TextView postTimeTv = (TextView) findViewById(R.id.tripbookdetail_usertime);
        postTimeTv.setText(tripBookDetailModel.postTime);
        String[] navValues = null;
        if ((tripBookDetailModel.userInfo.id + "").equals(IOUtils.getUserId(getContext()))) {
//            navValues = mRes.getStringArray(R.array.global_navme_values);
            navValues = new String[]{"分享", "扫一扫", "编辑", "删除"};
            tripPresenter.getRoadLinelist(IOUtils.getToken(getContext()));
        } else {
            if(isFav ==1){
                navValues = new String[]{"分享", "取消收藏", "扫一扫"};
            } else {
                navValues = new String[]{"分享", "收藏", "扫一扫"};
            }

//            navValues = mRes.getStringArray(R.array.global_nav_values);
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
        if (mTripBookDetailModel.is_praise == 1) {
            praiseIv.setImageBitmap(BitmapFactory.decodeResource(mRes,R.drawable.btn_heart2));
        } else {
            praiseIv.setImageBitmap(BitmapFactory.decodeResource(mRes,R.drawable.btn_heart1));
        }
        updataFavView();
    }
    public void  updataFavView(){
        if(isFav==1){
            favTextView.setText("取消收藏");
        }else {
            favTextView.setText("收藏路线");
        }
    }
    @Override
    public void showRideReport(RideReportDetailModel.RideBean rideReport) {
        TextView rideMileTv = (TextView) findViewById(R.id.rideMileTv);
        TextView rideTime = (TextView) findViewById(R.id.rideTime);
        TextView totalTime = (TextView) findViewById(R.id.totalTime);
        rideMileTv.setText(rideReport.mileage + "");
        rideTime.setText(rideReport.actualTime);
        totalTime.setText(rideReport.allTime);
        tripdetailScrol.smoothScrollTo(0, 0);
        updateGirdData(rideReport);
    }

    /**
     * 更新GridView数据
     *
     * @param rideBean
     */
    private void updateGirdData(RideReportDetailModel.RideBean rideBean) {

        updateItemData(0, (int) Double.parseDouble(rideBean.speedMax), rideBean.speedMax);
        updateItemData(1, (int) Double.parseDouble(rideBean.speedAverage), rideBean.speedAverage);
        updateItemData(2, rideBean.altitudeMax, rideBean.altitudeMax + "");
//        updateItemData(3, rideBean.bendingAngleMax, rideBean.bendingAngleMax + "");
        updateItemData(3, rideBean.hundredTime, rideBean.hundredTime + "");
        updateItemData(4, rideBean.fourHundredTime, rideBean.fourHundredTime + "");
        mRideReportAdapter.notifyDataSetChanged();
    }

    @Override
    public void showUserInfo(final TripBookDetailModel.User userInfo) {
        SimpleDraweeView avatarIv = (SimpleDraweeView) findViewById(R.id.tripbookdetail_useriv);
        String avatar = URLUtil.builderImgUrl(userInfo.avatar, 144, 144);
        avatarIv.setImageURI(Uri.parse(avatar));
        ImageView genderIv = (ImageView) findViewById(R.id.tripbookdetail_sexiv);
        if (userInfo.sex == 1) {
            genderIv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.icon_men));
        }
        TextView nameTv = (TextView) findViewById(R.id.tripbookdetail_name);
        nameTv.setText(userInfo.nickname);
        findViewById(R.id.tripbook_avatarlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUserPageAct = new Intent(getContext(), UserPageActivity.class);
                toUserPageAct.putExtra(Constants.IntentParams.ID, userInfo.id);
                startActivity(toUserPageAct);
            }
        });
        View darenIV = findViewById(R.id.drivdetails_convene_da);
        View modelIV = findViewById(R.id.comment_model_tv);
        if (userInfo.isDaren == 1) {
            darenIV.setVisibility(View.VISIBLE);
        } else {
            darenIV.setVisibility(View.GONE);
        }
        if (userInfo.isModel == 1) {
            modelIV.setVisibility(View.VISIBLE);
        } else {
            modelIV.setVisibility(View.GONE);
        }
        if (userInfo.auth != null) {
            SimpleDraweeView userLogoIv = (SimpleDraweeView) findViewById(
                    R.id.drivdetails_brandLogo);
            String userLogoUrl = URLUtil.builderImgUrl(userInfo.auth.brandLogo, 144, 144);
            userLogoIv.setImageURI(Uri.parse(userLogoUrl));
        }

    }

    @Override
    public void showRoadLineDesc(List<TripBookDetailModel.NodeDesc> lineList) {
        nodeCount = lineList.size();
        nodeLv.setAdapter(new NestFullListViewAdapter<TripBookDetailModel.NodeDesc>(
                R.layout.listitem_tripdetail_node, lineList) {
            @Override
            public void onBind(final int pos, final TripBookDetailModel.NodeDesc nodeDesc,
                               NestFullViewHolder holder) {
                holder.setText(R.id.node_addresstv, nodeDesc.adds);
                holder.setText(R.id.node_timetv, DateFormatUtil.formatTime5(nodeDesc.arriveTime));
                NestFullListView attrationLv = holder.getView(R.id.tripdetail_node_attralv);
                LinearLayout couponll = holder.getView(R.id.tripdetal_node_couponll);
                MyGridView couponGalley = holder.getView(R.id.tripdetal_node_couponlv);
                ImageView nodeIconIv = holder.getView(R.id.tripdetailnode_iconiv);
                nodeIconIv.setImageBitmap(getNodeIcon(pos));
                TextView distanceTv = holder.getView(R.id.tripdetail_ride_distancetv);
                if (pos == 0 ) {
                    holder.getView(R.id.tripdetail_ride_distancelayout).setVisibility(View.INVISIBLE);
                } else {
                    distanceTv.setText("行驶约" + nodeDesc.rideTime + "  " + nodeDesc.rideDistance + "公里");
                }
                holder.getView(R.id.tripbookdetail_tomapll)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toTripBookAct = new Intent(getContext(),
                                        TripBookMapActivity.class);
                                toTripBookAct.putExtra(Constants.IntentParams.ID, mBookId);
                                toTripBookAct.putExtra(Constants.IntentParams.INDEX, pos);
                                startActivity(toTripBookAct);
                            }
                        });
                if (nodeDesc.couponList == null || nodeDesc.couponList.size() == 0) {
                    couponll.setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.tripdetail_couponmoretv)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent toMoreCouponIntent = new Intent(getContext(),
                                            TripBookCouponActivity.class);
                                    toMoreCouponIntent.putExtra(
                                            Constants.IntentParams.ID, nodeDesc.rideNodeId);
                                    startActivity(toMoreCouponIntent);
                                }
                            });
                    couponll.setVisibility(View.VISIBLE);
                    showCouponList(couponGalley, nodeDesc.couponList);
                }
                //图文列表数据组装
                if (nodeDesc.desc != null) {
                    AttrationEntity descEntity = new AttrationEntity();
                    descEntity.type = -1;
                    descEntity.content = nodeDesc.desc;
                    if (nodeDesc.contentList == null) {
                        nodeDesc.contentList = new ArrayList<AttrationEntity>();
                    }
                    nodeDesc.contentList.add(0, descEntity);
                }
                showAtrrationList(nodeDesc.contentList, attrationLv);
            }

            private void showCouponList(MyGridView couponGalley,
                                        ArrayList<TripBookDetailModel.Coupon> couponList) {

                couponGalley.setAdapter(new WrapAdapter<TripBookDetailModel.Coupon>(getContext(),
                        couponList, R.layout.listitem_tripdetail_coupon) {
                    @Override
                    public void convert(ViewHolder helper, TripBookDetailModel.Coupon item) {
                        Double price = Double.parseDouble(item.price);
                        TextView priceTv = helper.getView(R.id.tripdetail_coupon_price);
                        LinearLayout rootlv = helper.getView(R.id.tripdetail_coupon_rootlv);
                        if (price == 0) {
                            priceTv.setText("免费领取");
                            rootlv.setBackground(mRes.getDrawable(R.drawable.coupon_green));
                        } else {
                            priceTv.setText("现价" + Math.round(price) + "元");
                            rootlv.setBackground(mRes.getDrawable(R.drawable.coupon_orange));
                        }
                        helper.setText(R.id.tripdetail_coupon_denominationtv,
                                "¥ " + item.denomination);
                    }
                });
            }

            private void showAtrrationList(ArrayList<AttrationEntity> contentList,
                                           NestFullListView attrationLv) {
                attrationLv.setAdapter(new NestFullListViewAdapter<AttrationEntity>(
                        R.layout.listitem_tripbook_editnode, contentList) {
                    @Override
                    public void onBind(int pos, AttrationEntity item, NestFullViewHolder helper) {
                        TextView nodeDesc = helper.getView(R.id.node_desctv);
                        nodeDesc.setText(item.title);
//                        if(item.org_id!=0){
//                            nodeDesc.setTextColor(mRes.getColor(R.color.text_color6));
//                            nodeDesc.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                        }
                        helper.getView(R.id.tripbook_pencil).setVisibility(View.INVISIBLE);
                        LinearLayout typeLv = helper.getView(R.id.attraction_typelv);
                        SimpleDraweeView poinIconIv = helper.getView(R.id.point_iconiv);
                        GenericDraweeHierarchy hierarchy = poinIconIv.getHierarchy();
                        if (item.type == -1) {
                            typeLv.setVisibility(View.GONE);
                        } else if (item.type == 1) {
                            typeLv.setVisibility(View.VISIBLE);
                            hierarchy.setPlaceholderImage(R.drawable.btn_attraction);
                        } else if (item.type == 2) {
                            typeLv.setVisibility(View.VISIBLE);
                            hierarchy.setPlaceholderImage(R.drawable.btn_canteen);
                        } else {
                            typeLv.setVisibility(View.VISIBLE);
                            hierarchy.setPlaceholderImage(R.drawable.btn_hotel);
                        }
                        NestFullListView contentLv = helper.getView(R.id.nodedesc_contentlv);
                        if (item.content != null) {
                            contentLv.setAdapter(new NestFullListViewAdapter<SEditorData>(
                                    R.layout.listitem_nodedesc_content, item.content) {
                                @Override
                                public void onBind(int pos, SEditorData attrationEntity,
                                                   NestFullViewHolder holder) {
                                    TextView nodeContentTv = holder.getView(R.id.node_content_tv);
                                    ImageView nodeContentIv = holder
                                            .getView(R.id.node_content_iv);
                                    if (TextUtils.isEmpty(attrationEntity.getInputStr())) {
                                        nodeContentTv.setVisibility(View.GONE);
                                        attrationEntity.setInputStr(null);
                                    } else {
                                        nodeContentTv.setVisibility(View.VISIBLE);
                                        nodeContentTv.setText(attrationEntity.getInputStr());
                                    }
                                    if (TextUtils.isEmpty(attrationEntity.getImageUrl())) {
                                        nodeContentIv.setVisibility(View.GONE);
                                        attrationEntity.setImageUrl(null);
                                    } else {
                                        nodeContentIv.setVisibility(View.VISIBLE);
                                        String url = URLUtil
                                                .builderImgUrl750(attrationEntity.getImageUrl());
//                                        nodeContentIv.setImageURI(Uri.parse(url));
                                        Glide.with(TripbookDetailActivity.this)
                                                .load(url)
                                                .into(nodeContentIv);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    @Override
    public void showRecomdInfo(TripBookDetailModel.Recomment recommendInfo) {
        if (recommendInfo == null || recommendInfo.quotient == 0) {
            findViewById(R.id.recomentinfo_layout).setVisibility(View.GONE);
        } else {
            TextView recommdInfoTV = (TextView) findViewById(R.id.tripdetail_recommedinfotv);
            recommdInfoTV.setText(recommendInfo.content);
            TextView recomentScoretv = (TextView) findViewById(R.id.recoment_scoretv);
            recomentScoretv.setText(recommendInfo.quotient + "");
            RatingBar ratingBar = (RatingBar) findViewById(R.id.tripdetail_ratingbar);
            ratingBar.setStar(recommendInfo.quotient);
        }
    }

    public Bitmap getNodeIcon(int position) {
        View markerPosView = LayoutInflater.from(mContext).inflate(R.layout.marker_pos_tripmap,
                null, false);
        TextView markerPosName = (TextView) markerPosView.findViewById(R.id.name);

        if (position == 0) {
            markerPosName.setText("起");
        } else if (position == nodeCount - 1) {
            markerPosName.setText("终");
        } else {
            markerPosName.setText(position + "");
        }
        return convertViewToBitmap(mContext, markerPosView);
    }

    //view 转bitmap
    public static Bitmap convertViewToBitmap(Context context, View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, BitmapUtils.dp2px(context, 21), BitmapUtils.dp2px(context, 29));
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    @Override
    public void showTagList(ArrayList<Topic> topiclist) {
        View tagLayout = findViewById(R.id.tagLayout);
        if (topiclist == null || topiclist.size() == 0) {
            tagLayout.setVisibility(View.GONE);
        } else {
            FlowLayout mTagLayout = (FlowLayout) findViewById(R.id.tagLists);
            mTagLayout.setVerticalSpacing(BitmapUtils.dp2px(mContext, 5));
            mTagLayout.setHorizontalSpacing(BitmapUtils.dp2px(mContext, 5));
            mTagLayout.removeAllViews();
            int size = topiclist.size();
            for (int i = 0; i < size; i++) {
                Topic topic = topiclist.get(i);
                View v = LayoutInflater.from(mContext).inflate(R.layout.tagview, mTagLayout, false);
                v.setTag(topic.id);
                mTagLayout.addView(v);
                TextView tv = (TextView) v.findViewById(R.id.tag_btn);
                tv.setText(topic.title + "");
                tv.setTag(topic.id);
            }
        }
    }

    @Override
    public void showHonorList(List<TripBookDetailModel.Honor> honorList) {
        View layoutHonor = findViewById(R.id.tripdetail_honortitlelv);
        if (honorList == null || honorList.size() == 0) {
            layoutHonor.setVisibility(View.GONE);
        }
        NestFullListView honorLv = (NestFullListView) findViewById(R.id.tripdetail_honorlv);
        honorLv.setAdapter(new NestFullListViewAdapter<TripBookDetailModel.Honor>(
                R.layout.drivdetail_honor_item, honorList) {
            @Override
            public void onBind(int pos, TripBookDetailModel.Honor honour,
                               NestFullViewHolder holder) {
                SimpleDraweeView mSimpleDraweeView = holder.getView(R.id.icon);
                TextView title = holder.getView(R.id.title);
                TextView bonuspoint = holder.getView(R.id.bonuspoint);
                mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(honour.icon)));
                title.setText(honour.text);
                if (!TextUtils.isEmpty(honour.tag)) {
                    ViewUtil.link(honour.substr, title, new Link.OnClickListener() {
                        @Override
                        public void onClick(Object o, String clickedText) {

                        }
                    }, mRes.getColor(R.color.text_color6), true);
                }
                bonuspoint.setText(honour.integral + "分");
            }
        });
    }

    @Override
    public void showUnStop(final RideDataModel unStoprideDataModel) {
        if (unStoprideDataModel.ride_id == mTripBookDetailModel.ride_id) {
            unStopDialog = new NotitleContentDialog(getContext());
            unStopDialog.setContentText(" 您有未结束的行驶轨迹，进入驾途继续，还是结束运行？");
            unStopDialog.setCancelText("结束");
            unStopDialog.setYesText("继续");
            unStopDialog.setOnLoginOutClickListener(new NotitleContentDialog.IOnclickLisenter() {
                @Override
                public void OkClick() {
                    jumpToRidePage(false, unStoprideDataModel);
                    unStopDialog.dismiss();
                }

                @Override
                public void CancleClick() {
                    jumpToRidePage(true, unStoprideDataModel);
                    unStopDialog.dismiss();
                }
            });
        }


    }

    private void jumpToRidePage(boolean isEnd, RideDataModel unStoprideDataModel) {

        Intent toDringIntent = new Intent(getContext(), DrivingHomeActivity.class);
        toDringIntent.putExtra(Constants.IntentParams.BG_RIDE_TYPE, 1);
        toDringIntent.putExtra(Constants.IntentParams.ID, unStoprideDataModel.ride_id);
        toDringIntent.putExtra(Constants.IntentParams.DATA,
                Constants.IntentParams.RIDE_BOOK_MODULE);
        toDringIntent.putParcelableArrayListExtra(Constants.IntentParams.DATA3,
                unStoprideDataModel.nodeList);
        toDringIntent.putExtra(Constants.IntentParams.IS_END, isEnd);
        startActivity(toDringIntent);
    }

    @Override
    public void showDeleteSuccess() {
        finish();
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

    private List<RideDataItem> mItems = new ArrayList<RideDataItem>();

    private void initGridData() {

        String[] arrays = mRes.getStringArray(R.array.ride_items_titles);

        int index = 0;
        for (String s : arrays) {
            RideDataItem item = new RideDataItem();
            item.title = s;
            item.progressColorId = progressColorsIds[index];
            mItems.add(item);
            switch (index) {
                case 0:
                case 1:
                    item.max = 300;
                    break;
                case 2:
                    item.max = 8000;
                    break;
                case 3:
                    item.max = 70;
                    break;
                case 4:
                    item.max = 100;
                    break;

            }
            index++;
        }

        mRideReportAdapter = new WrapAdapter<RideDataItem>(mContext, R.layout.report_ride_item,
                mItems) {

            protected TextView title;
            protected TextView content;
            protected VerticalProgressBar progressBar;

            @Override
            public void convert(ViewHolder helper, RideDataItem item) {

                progressBar = helper.getView(R.id.progressBar);
                content = helper.getView(R.id.content);
                content.setTypeface(mGlobalContext.getTypeface());
                content.setText(item.content);
                title = helper.getView(R.id.title);
                title.setText(item.title);
                progressBar.setMax(item.max);
                progressBar.setProgress(item.progress);
                setProgressDrawable(progressBar, item.progressColorId);
            }
        };

        mRideReportGV.setAdapter(mRideReportAdapter);
    }

    /**
     * 动态设置进度条状态值
     *
     * @param progressBar
     * @param colorId
     */
    private void setProgressDrawable(ProgressBar progressBar, int colorId) {

        LayerDrawable layered = (LayerDrawable) progressBar.getProgressDrawable();
        Drawable backgroudDrawable = layered.getDrawable(0);
        backgroudDrawable.setColorFilter(mRes.getColor(R.color.black), PorterDuff.Mode.SRC_IN);

        Drawable progressDrawable = layered.getDrawable(2);
        progressDrawable.setColorFilter(mRes.getColor(colorId), PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(layered);
    }

    private void updateItemData(int position, int progress, String content) {

        mItems.get(position).progress = progress;
        mItems.get(position).content = content;
    }

    @Override
    public void showDatas(Object obj) {
        CommentModel commentModel = (CommentModel) obj;
        mCmtContent.setText("(" + commentModel.count + ")");
        TextView comentNumbers = (TextView) findViewById(R.id.comment_numbers);
        if (commentModel.count == 0) {
            comentNumbers.setVisibility(View.GONE);
        } else {
            comentNumbers.setText(commentModel.count + "");
        }
        commentFrag = new CommentFrag(commentModel.comments, threadId, mModule);
        commentTran = commentFm.beginTransaction();
        commentTran.replace(R.id.comment_fragfl, commentFrag);
        commentTran.commit();
    }

    private void showLikeLists(LikeListModel likeListModel) {
        mLikeContent.setText("(" + likeListModel.pagination.count + ")");
        if (dianzanFrag == null) {
            dianzanFrag = new DianzanFrag(likeListModel.list);
        } else {
            dianzanFrag.notifylikeDataSetChange(likeListModel.list);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_CODE_COMMENT) {
                    mCommentPresenter.loadCommentList(mModule, mTripBookDetailModel.thread_id + "", "car");
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
    public void addFavoriteSuccess(Object data) {
        isFav = 1;
        CommonUtils.showToast(getContext(), "收藏成功");
        updataFavView();
        dataList.get(1).title="取消收藏";
        morePopupWindow.notifyDataSetChanged(dataList);
    }

    @Override
    public void cancelFavorite(Object data) {
        isFav = 0;
        dataList.get(1).title="收藏";
        morePopupWindow.notifyDataSetChanged(dataList);
        updataFavView();
        CommonUtils.showToast(getContext(), "取消收藏");
    }
}
