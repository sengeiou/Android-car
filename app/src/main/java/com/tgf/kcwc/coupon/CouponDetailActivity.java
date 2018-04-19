package com.tgf.kcwc.coupon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentFrag;
import com.tgf.kcwc.comment.EvaluateFrag;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.CouponInfo;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.CouponDetailModel;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.CouponDetailPresenter;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.CouponDetailView;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.countdown.CountdownView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/9 0009
 * E-mail:hekescott@qq.com
 */

public class CouponDetailActivity extends BaseActivity
        implements CouponDetailView, CommentListView {
    private WebView couponInfowebView;
    private View viewCommentTitle;
    private NestFullListView mEvaluateList;
    private NestFullListViewAdapter mCommentsadapter;
    //回复列表
    private NestFullListViewAdapter mReplyadapter;
    private ArrayList mLoopHead = new ArrayList();
    private TextView titleTv;
    private TextView headNowTv;
    private TextView headOldTv;
    private CountdownView countdownView;
    private CouponDetailPresenter couponDetailPresenter;
    private TextView saleNum;
    private ListView ruleLv;
    private List<CouponDetailModel.Dealers> mDealers;
    private SimpleDraweeView headCoverIv;
    private KPlayCarApp mCarApp;
    private TextView mGoButTv;
    private TextView toBuyNowPriceTv;
    private TextView toBuyOldPriceTv;
    private int mId = -1;
    private CommentListPresenter mCommentPresenter;
    private EvaluateFrag evaluateFrag;
    private FragmentManager commentFm;
    private FragmentTransaction commentTran;
    private TextView mCmtContent;
    private OpenShareWindow openShareWindow;
    private CouponDetailModel mCouponDetailModel;

    private UserDataPresenter mAddFavoritePresenter;
    private UserDataPresenter mCancelFavoritePresenter;
    private int isFav = 0;
    private UserDataView<Object> mAddFavoriteView = new UserDataView<Object>() {
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
        public void showDatas(Object o) {

            isFav = 1;
            mFavoriteBtn
                    .setSelected(true);
            CommonUtils.showToast(
                    mContext, "收藏成功");
            mFavoriteBtn.setImageResource(R.drawable.btn_collection_s);
        }
    };

    private UserDataView<Object> mCancelFavoriteView = new UserDataView<Object>() {
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
        public void showDatas(Object o) {

            isFav = 0;
            mFavoriteBtn
                    .setSelected(false);
            mFavoriteBtn.setImageResource(R.drawable.favorite_icon_n);
            CommonUtils.showToast(
                    mContext, "已取消收藏");

        }
    };
    private ImageButton mFavoriteBtn;
    private RecyclerView mOnlineRV;
    private View mOlineLayout;
    private RatingBar ratingBar;
    private ObservableScrollView mScrollView;
    private Intent fromIntent;
    private int limitData;
    private int distributeId;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    private String mModule = "order_coupon";

    @Override
    protected void setUpViews() {
        mCarApp = (KPlayCarApp) getApplication();
        mAddFavoritePresenter = new UserDataPresenter();
        mAddFavoritePresenter.attachView(mAddFavoriteView);
        mCancelFavoritePresenter = new UserDataPresenter();
        mCancelFavoritePresenter.attachView(mCancelFavoriteView);
        couponInfowebView = (WebView) findViewById(R.id.coupondetail_info_webview);
        mFavoriteBtn = (ImageButton) findViewById(R.id.title_favorite_btn);
        findViewById(R.id.coupondetail_back_iv).setOnClickListener(this);
        mFavoriteBtn.setOnClickListener(this);
        findViewById(R.id.title_share_btn).setOnClickListener(this);
        mGoButTv = (TextView) findViewById(R.id.coupondetail_free_tv);
        mGoButTv.setOnClickListener(this);
        commentFm = getSupportFragmentManager();
        mEvaluateList = (NestFullListView) findViewById(R.id.evaluateList);
        mOnlineRV = (RecyclerView) findViewById(R.id.coupononline_recylerview);
        mOlineLayout = findViewById(R.id.coupondail_online_rl);
        mOlineLayout.setOnClickListener(this);
        ruleLv = (ListView) findViewById(R.id.coupondetail_rulelv);
        titleTv = (TextView) findViewById(R.id.coupondetail_title);
        headOldTv = (TextView) findViewById(R.id.coupondetail_headoldprice_tv);
        headNowTv = (TextView) findViewById(R.id.coupondetail_headnowprice_tv);
        saleNum = (TextView) findViewById(R.id.coupondetail_head_salenum);
        headCoverIv = (SimpleDraweeView) findViewById(R.id.coupondetail_headpng_iv);
        toBuyNowPriceTv = (TextView) findViewById(R.id.coupondetail_nowprice_tv);
        toBuyOldPriceTv = (TextView) findViewById(R.id.coupondetail_oldprice_tv);
        findViewById(R.id.gotoorg_detailrl).setOnClickListener(this);
        mCmtContent = (TextView) findViewById(R.id.cmtContent);
        findViewById(R.id.coupondetail_mygift).setOnClickListener(this);
        findViewById(R.id.coupondetail_mykarl).setOnClickListener(this);
        findViewById(R.id.coupondail_fitstore_rl).setOnClickListener(this);
        fromIntent = getIntent();
        mId = fromIntent.getIntExtra(Constants.IntentParams.ID, -1);
        limitData = fromIntent.getIntExtra(Constants.IntentParams.DATA,-1);
        distributeId = fromIntent.getIntExtra(Constants.IntentParams.ID2,-1);
        couponDetailPresenter = new CouponDetailPresenter();
        couponDetailPresenter.attachView(this);
        couponDetailPresenter.getCouponDetail(mId, mCarApp.getLattitude(), mCarApp.getLongitude(), IOUtils.getToken(mContext));
        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        mCommentPresenter.loadEvaluateList(mModule, mId + "", "car");
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        mScrollView.setScrollViewListener(mScrollListener);
    }
    private ObservableScrollView.ScrollViewListener mScrollListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                    int oldy) {

            int topHeight = BitmapUtils.dp2px(mContext, 132);
            if (y <= 0) {   //设置标题的背景颜色
                findViewById(R.id.coupondetail_titlebar).setBackgroundResource(R.drawable.shape_titlebar_bg);
            } else if (y > 0 && y <= topHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                float scale = (float) y / topHeight;
                float alpha = (255 * scale);
                findViewById(R.id.coupondetail_titlebar).setBackgroundColor(Color.argb((int) alpha, 54, 169, 92));
            } else {    //滑动到banner下面设置普通颜色
                findViewById(R.id.coupondetail_titlebar).setBackgroundColor(Color.argb((int) 255, 54, 169, 92));
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupondetail);

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.coupondail_fitstore_rl:
                Intent toIntent = new Intent(mContext, CouponStoreActivity.class);
                toIntent.putExtra(Constants.IntentParams.DATA, (Serializable) mDealers);
                startActivity(toIntent);
                break;
            case R.id.coupondetail_mygift:
                CommonUtils.showToast(mContext, "参与有奖转发");
                break;
            case R.id.coupondetail_mykarl:
                if (IOUtils.isLogin(getContext())) {
                    Intent toCouponAct = new Intent(getContext(), VoucherMainActivity.class);
                    toCouponAct.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    toCouponAct.putExtra(Constants.IntentParams.INDEX, 3);
                    startActivity(toCouponAct);
                }
                break;
            case R.id.coupondail_online_rl:
                //// TODO: 2017/9/25 0025 去销售人员列表

                break;
            case R.id.title_share_btn:
                openShareWindow = new OpenShareWindow(CouponDetailActivity.this);
                openShareWindow.show(CouponDetailActivity.this);
                final String title = "看车玩车";
                final String description = "欢迎关注看车玩车";
                final String cover = Constants.ImageUrls.IMG_DEF_URL;

                openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        switch (position) {

                            case 0:
                                OpenShareUtil.sendWXMoments(mContext, mCarApp.getMsgApi(), title,
                                        description);
                                break;
                            case 1:
                                OpenShareUtil.sendWX(mContext, mCarApp.getMsgApi(), title,
                                        description);
                                break;

                            case 2:
                                WbShareHandler mWbHandler = OpenShareUtil
                                        .shareSina(CouponDetailActivity.this, title, description);
                                break;
                            case 3:
                                ArrayList<String> urls = new ArrayList<String>();
                                urls.add(cover);
                                OpenShareUtil.shareToQzone(mCarApp.getTencent(),
                                        CouponDetailActivity.this, mBaseUIListener, urls, title,
                                        description);
                                break;
                            case 4:
                                OpenShareUtil.shareToQQ(mCarApp.getTencent(),
                                        CouponDetailActivity.this, mBaseUIListener, title, cover,
                                        description);
                                break;

                        }
                    }
                });
                break;
            case R.id.title_favorite_btn:

                if (mCouponDetailModel == null) {
                    CommonUtils.showToast(mContext, "数据正在加载.....");
                    return;
                }

                HashMap<String, String> params = new HashMap<String, String>();
                // args.put("attach", "");
                params.put("img_path", mCouponDetailModel.cover);
                params.put("model", Constants.FavoriteTypes.COUPON);
                params.put("source_id", mCouponDetailModel.id + "");
                params.put("title", mCouponDetailModel.title);
                params.put("type", "car");
                params.put("token", IOUtils.getToken(mContext));
                if (isFav == 1) {
                    mCancelFavoritePresenter.cancelFavoriteData(params);
                } else {
                    mAddFavoritePresenter.addFavoriteData(params);
                }

                //CommonUtils.showToast(mContext, "收藏");
                break;
            case R.id.gotoorg_detailrl:
                Intent toOrgdetail = new Intent(getContext(), DealerHomeActivity.class);
                toOrgdetail.putExtra(Constants.IntentParams.ID, mDealers.get(0).id + "");
                toOrgdetail.putExtra(Constants.IntentParams.TITLE, mDealers.get(0).fullName);
                toOrgdetail.putExtra(Constants.IntentParams.INDEX, 0);
                startActivity(toOrgdetail);
                break;
            case R.id.coupondetail_back_iv:
                finish();
                break;
            case R.id.coupondetail_free_tv:
                if (mCouponDetailModel.statistics.isFinished == 1) {
                    CommonUtils.showToast(getContext(), "已抢光");
                    return;
                }
                Intent toBuyIntent = new Intent(mContext, CouponOrderActivity.class);
                if(limitData!=-1){
                    mCouponInfo.limit =limitData;
                }
                toBuyIntent.putExtra(Constants.IntentParams.DATA, mCouponInfo);
                toBuyIntent.putExtra(Constants.IntentParams.ID2, distributeId+"");
                startActivity(toBuyIntent);
                break;
            default:
                break;
        }
    }

    private CouponInfo mCouponInfo = new CouponInfo();

    @Override
    public void showHead(CouponDetailModel couponDetailModel) {
        mCouponDetailModel = couponDetailModel;
        isFav = couponDetailModel.statistics.isCollect;
        if (isFav == 1) {
            mFavoriteBtn.setImageResource(R.drawable.btn_collection_s);
            mFavoriteBtn.setSelected(true);
        } else {
            mFavoriteBtn.setImageResource(R.drawable.favorite_icon_n);
            mFavoriteBtn.setSelected(false);
        }
        initCouponInfo(couponDetailModel);
        titleTv.setText(couponDetailModel.title);
        toBuyNowPriceTv.setText(SpannableUtil.getSpanMoneyString(getContext(), "￥" + couponDetailModel.price, 12));
        headNowTv.setText(SpannableUtil.getSpanMoneyString(getContext(), "￥" + couponDetailModel.price, 12));

        String showGetLeft = "";
        if (couponDetailModel.price == 0) {
            mGoButTv.setText("免费领取");
            toBuyNowPriceTv.setText("免费");
            headNowTv.setText("免费");
            headNowTv.setTextColor(mRes.getColor(R.color.tab_text_s_color));
            toBuyNowPriceTv.setTextColor(mRes.getColor(R.color.tab_text_s_color));
            showGetLeft = "已领: " + TextUtil.getColorText("#36a95c",couponDetailModel.statistics.send);
        } else {
            mGoButTv.setText("立即购买");
//            saleNum.setText("已售" + couponDetailModel.statistics.send);
            showGetLeft = "已售" + TextUtil.getColorText("#36a95c",couponDetailModel.statistics.send);

        }
        showGetLeft=showGetLeft +"\t\t  剩余: "+ TextUtil.getColorText("#da667e",couponDetailModel.statistics.surplus);
        saleNum.setText(Html.fromHtml(showGetLeft));
        if (couponDetailModel.statistics.isFinished == 1) {
            mGoButTv.setText("抢光了");
            toBuyNowPriceTv.setText("抢光了");
            mGoButTv.setBackgroundColor(mRes.getColor(R.color.text_color17));
        }
        SpannableString oldPrice = SpannableUtil
                .getDelLineString("￥" + couponDetailModel.denomination);
        headOldTv.setText(oldPrice);
        toBuyOldPriceTv.setText(oldPrice);
        countdownView = (CountdownView) findViewById(R.id.coupondetail_setTimeText);
        long endTime = DateFormatUtil.getTime(couponDetailModel.endTime);
        long nowTime = DateFormatUtil.getTime(couponDetailModel.currentTime);
        long leftTime = endTime - nowTime;
        if (leftTime > 0) {
            countdownView.start(leftTime);
        } else {
            countdownView.setVisibility(View.GONE);
        }
        TextView overdueTv = (TextView) findViewById(R.id.overdueTv);
        if (couponDetailModel.refundType == 1) {
            //随时退 1
            overdueTv.setText("随时退");
            overdueTv.setVisibility(View.VISIBLE);
        } else if (couponDetailModel.refundType == 0) {
            //过期退 0
            overdueTv.setText("过期退");
            overdueTv.setVisibility(View.VISIBLE);
        }
        if (couponDetailModel.price == 0) {
            overdueTv.setVisibility(View.INVISIBLE);
        }


        if (couponDetailModel.statistics.isCollect == 1) {
            mFavoriteBtn.setSelected(true);
        }
        String headPngUrl = URLUtil.builderImgUrl(couponDetailModel.cover, 540, 304);
        headCoverIv.setImageURI(Uri.parse(headPngUrl));
        showDealers(couponDetailModel.dealers);
        showDesc(couponDetailModel.detail);
        showRule(couponDetailModel.rules);
        showOnlineList(couponDetailModel.onlineList);
    }

    private void showOnlineList(ArrayList<CouponDetailModel.OnlineItem> onlineList) {
        if (onlineList == null || onlineList.size() == 0) {
            mOlineLayout.setVisibility(View.GONE);
            mOnlineRV.setVisibility(View.GONE);
            return;
        }

        mOnlineRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        CommonAdapter onLineAdapter = new CommonAdapter<CouponDetailModel.OnlineItem>(getContext(), R.layout.griditem_online, onlineList) {

            @Override
            public void convert(ViewHolder holder, CouponDetailModel.OnlineItem onlineItem) {
                holder.setText(R.id.salerstar, onlineItem.star);
                holder.setSimpleDraweeViewUrl(R.id.saler_avatariv, URLUtil.builderImgUrl(onlineItem.avatar, 144, 144));
                holder.setText(R.id.salernametv, onlineItem.nickname);
            }
        };
        onLineAdapter.setOnItemClickListener(new OnItemClickListener<CouponDetailModel.OnlineItem>() {

            @Override
            public void onItemClick(ViewGroup parent, View view, CouponDetailModel.OnlineItem onlineItem, int position) {
                Intent toUserPageAct = new Intent(mContext, UserPageActivity.class);
                toUserPageAct.putExtra(Constants.IntentParams.ID, onlineItem.id);
                startActivity(toUserPageAct);

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, CouponDetailModel.OnlineItem onlineItem, int position) {
                return false;
            }
        });
        mOnlineRV.setAdapter(onLineAdapter);
//        onLineAdapter.setOnItemClickListener(new OnItemClickListener<CouponDetailModel.OnlineItem>() {
//
//            @Override
//            public void onItemClick(ViewGroup parent, View view, CouponDetailModel.OnlineItem onlineItem, int position) {
//
//            }
//
//            @Override
//            public boolean onItemLongClick(ViewGroup parent, View view, CouponDetailModel.OnlineItem onlineItem, int position) {
//                return false;
//            }
//        });

    }

    private void initCouponInfo(final CouponDetailModel couponDetailModel) {
        mCouponInfo.title = couponDetailModel.title;
        mCouponInfo.id = couponDetailModel.id;

        mCouponInfo.limit = couponDetailModel.getLimit;
        mCouponInfo.price = couponDetailModel.price;
        mCouponInfo.denomination = couponDetailModel.denomination;
        mCouponInfo.cover = couponDetailModel.cover;
        mCouponInfo.dealerName = couponDetailModel.dealers.get(0).name;
        ratingBar.setStar(couponDetailModel.dealers.get(0).service_score);
        findViewById(R.id.coupondetail_telBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemInvoker.launchDailPage(getContext(), couponDetailModel.dealers.get(0).tel);
            }
        });
    }

    ArrayList<MyRule> myRulelist = new ArrayList<>();

    @Override
    public void showRule(List<CouponDetailModel.Rrule> rules) {

        for (int i = 0; i < rules.size(); i++) {
            CouponDetailModel.Rrule rule = rules.get(i);
            MyRule myRule = new MyRule();
            myRule.isTitle = true;
            myRule.content = rule.title;
            myRulelist.add(myRule);
            for (int j = 0; j < rule.contentList.size(); j++) {
                MyRule myRuleContent = new MyRule();
                myRuleContent.isTitle = false;
                myRuleContent.content = rule.contentList.get(j);
                myRulelist.add(myRuleContent);
            }
        }

        ruleLv.setAdapter(
                new WrapAdapter<MyRule>(mContext, R.layout.listitem_coupondetail_rule, myRulelist) {
                    @Override
                    public void convert(ViewHolder helper, MyRule item) {

                        TextView tv = helper.getView(R.id.coupondetail_ruletv);
                        if (item.isTitle) {
                            tv.setText(item.content);
                            tv.setTextColor(mRes.getColor(R.color.voucher_gray));
                        } else {
                            tv.setText("  ・" + item.content);
                            tv.setTextColor(mRes.getColor(R.color.voucher_black));
                        }
                    }
                });
        ViewUtil.setListViewHeightBasedOnChildren(ruleLv);
    }

    @Override
    public void showDatas(Object obj) {
        CommentModel commentModel = (CommentModel) obj;
        mCmtContent.setText("(" + commentModel.count + ")");
        evaluateFrag = new EvaluateFrag(commentModel.comments, mId, mModule);
        commentTran = commentFm.beginTransaction();
        commentTran.replace(R.id.comment_fragfl, evaluateFrag);
        commentTran.commit();
    }

    private class MyRule {
        public boolean isTitle;
        public String content;
    }

    @Override
    public void showDealers(List<CouponDetailModel.Dealers> dealrs) {
        mDealers = dealrs;
        TextView fitStoreNum = (TextView) findViewById(R.id.coupondetail_fitstore_num);
        if (dealrs != null && dealrs.size() != 0) {
            fitStoreNum.setText("(" + dealrs.size() + ")");
            TextView storeName = (TextView) findViewById(R.id.coupondetail_storename);
            storeName.setText(dealrs.get(0).name);
            TextView address = (TextView) findViewById(R.id.coupondeatail_area_tv);
            address.setText(dealrs.get(0).address);
            TextView distance = (TextView) findViewById(R.id.coupondeatail_distance_tv);
            distance.setText(dealrs.get(0).getDistance() );
            ratingBar.setStar(dealrs.get(0).service_score);
            NestFullListView useNoticeLv = (NestFullListView) findViewById(
                    R.id.coupondetail_usenotice_lv);
            useNoticeLv.setAdapter(new NestFullListViewAdapter<NoticeData>(
                    R.layout.listitem_coupondetail_notice, noticeDatas) {

                @Override
                public void onBind(int pos, NoticeData noticeData, NestFullViewHolder holder) {

                }
            });

        } else {
            fitStoreNum.setText(0 + "");
        }
    }

    private ArrayList<NoticeData> noticeDatas = new ArrayList<>();

    private class NoticeData {
        boolean isTitle;
        boolean conent;
    }

    @Override
    public void showDesc(String detail) {
        URLUtil.loadWebData(couponInfowebView, WebviewUtil.getHtmlData(detail));
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