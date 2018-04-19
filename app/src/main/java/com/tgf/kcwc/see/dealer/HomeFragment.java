package com.tgf.kcwc.see.dealer;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.CouponDetailActivity;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.finddiscount.LimitDetailActivity;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.Comment;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.StoreDetailData;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.StoreHomePresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.StoreHomeView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.GridViewWithHeaderAndFooter;
import com.tgf.kcwc.view.MultiImageView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/3/15 15:53
 * E-mail：fishloveqin@gmail.com
 * 店铺主页(优惠及活动)
 */

public class HomeFragment extends BaseFragment
        implements StoreHomeView, CommentListView<CommentModel> {

    private SimpleDraweeView mBigHeaderImg;
    private TextView mImgCountText;
    private SimpleDraweeView mLogo;
    private TextView mName;
    private RatingBar mRatingBar;
    private ImageView mCall;
    private ListView mCouponList;
    private ListView mShopActivityList;
    private ListView mGiftsList;
    private ListView mLimitCouponList;
    private GridViewWithHeaderAndFooter mPlayerList;
    private GridViewWithHeaderAndFooter mSalespersonGrid;
    private String mOrgTitle = "";
    private StoreHomePresenter mPresenter;
    private CommentListPresenter mCommentPresenter;
    private List<Topic> mPlayerDatas = new ArrayList<Topic>();
    private NestFullListView mEvaluateList;
    private NestFullListViewAdapter mCommentsadapter;
    //回复列表
    private NestFullListViewAdapter mReplyadapter;
    private RelativeLayout mEvaluationLayout;
    private String mOrgId;
    private List<Coupon> mCouponDatas = new ArrayList<Coupon>();

    private LinearLayout mCouponLayout, mGiftByShoppingLayout, mLimitCouponLayout;
    private String lat;
    private String lng;
    private String tel;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_delear_home;
    }

    private RelativeLayout mActivityLayout;

    @Override
    protected void initView() {

        mCouponList = findView(R.id.couponList);
        mShopActivityList = findView(R.id.shopActivityList);
        mActivityLayout = findView(R.id.shopActivityLayout);
        mGiftsList = findView(R.id.giftList);
        mLimitCouponList = findView(R.id.limitCouponList);
        mLimitCouponList.setOnItemClickListener(mLimitItemClickListener);
        mPlayerList = findView(R.id.playList);
        mEvaluateList = findView(R.id.evaluateList);
        mSalespersonGrid = findView(R.id.salespersonGrid);
        mCouponLayout = findView(R.id.couponLayout);
        mGiftByShoppingLayout = findView(R.id.giftByShoppingLayout);
        mLimitCouponLayout = findView(R.id.limitCouponLayout);
        mShopActivityList.setOnItemClickListener(mShopActivityItemListener);

        mEvaluationLayout = findView(R.id.evaluateHeaderLayout);
        mEvaluationLayout.setOnClickListener(this);
        findView(R.id.saleEliteLayout).setOnClickListener(this);
        findView(R.id.salesHeaderLayout).setOnClickListener(this);
        findView(R.id.shopActivityHeaderLayout).setOnClickListener(this);
        showHeaderTitleViews();
        mOrgId = getArguments().getString(Constants.IntentParams.ID);
        mPresenter = new StoreHomePresenter();
        mPresenter.attachView(this);
        mPresenter.loadStoreInfo(mOrgId + "", IOUtils.getToken(mContext));
        mPresenter.loadCouponList(mOrgId + "");
        mPresenter.loadGiftList(mOrgId, "1", IOUtils.getToken(mContext));
        mPresenter.loadGiftList(mOrgId, "2", IOUtils.getToken(mContext));
        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        mCommentPresenter.loadEvaluateList("shop_detail", mOrgId, "car");
    }

    private AdapterView.OnItemClickListener mShopActivityItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            Topic topic = (Topic) parent.getAdapter().getItem(position);

            String model = topic.model;
            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, topic.id + "");
            if ("cycle".equals(model)) {
                CommonUtils.startNewActivity(mContext, args, DrivingDetailsActivity.class);
            } else if ("play".equals(model)) {
                CommonUtils.startNewActivity(mContext, args, PleasePlayDetailsActivity.class);
            }

        }
    };
    private AdapterView.OnItemClickListener mLimitItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Coupon coupon = (Coupon) parent.getAdapter().getItem(position);

            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, coupon.id);
            CommonUtils.startNewActivity(mContext, args, LimitDetailActivity.class);
        }
    };

    private void showHeaderTitleViews() {

        initCommonTitleView(mCouponLayout, R.drawable.coupon_icon, "代金券");
        initCommonTitleView(mGiftByShoppingLayout, R.drawable.send_gift_icon, "购车有礼");
        initCommonTitleView(mLimitCouponLayout, R.drawable.limit_privilege_icon, "限时优惠");
        String[] titles = mRes.getStringArray(R.array.list_header_titles);
        int length = mCommonHeaderViewIds.length;
        for (int i = 0; i < length; i++) {
            View v = findView(mCommonHeaderViewIds[i]);
            TextView titleText = (TextView) v.findViewById(R.id.title);
            titleText.setText(titles[i]);
            if (i == length - 1) {
                v.findViewById(R.id.content).setVisibility(View.VISIBLE);
                v.findViewById(R.id.secHeaderLayout).setVisibility(View.VISIBLE);
                RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
                ratingBar.setVisibility(View.VISIBLE);
                setRatingScore(ratingBar, 5, 3, R.drawable.rating_bar_star_empty,
                        R.drawable.rating_bar_star_empty, R.drawable.rating_bar_star_fill);
            }

        }

    }

    private void initCommonTitleView(View parent, int drawableId, String title) {

        ImageView imgView = (ImageView) parent.findViewById(R.id.img_icon);
        TextView titleTv = (TextView) parent.findViewById(R.id.title);
        imgView.setImageResource(drawableId);
        titleTv.setText(title);
    }

    private int[] mCommonHeaderViewIds = {R.id.shopActivityHeaderLayout, R.id.saleEliteLayout,
            R.id.evaluateHeaderLayout};

    private void setRatingScore(RatingBar ratingBar, int count, float star, int drawableEmptyId,
                                int drawableHalfId, int drawableFillId) {
        ratingBar.setStarEmptyDrawable(getResources().getDrawable(drawableEmptyId));
        ratingBar.setStarHalfDrawable(getResources().getDrawable(drawableHalfId));
        ratingBar.setStarFillDrawable(getResources().getDrawable(drawableFillId));
        ratingBar.setStarCount(count);
        ratingBar.setStarImageSize(star);
        ratingBar.setStar(star);
        ratingBar.halfStar(true);
        ratingBar.setmClickable(false);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (mCommentPresenter != null) {
            mCommentPresenter.detachView();
        }
    }

    @Override
    public void showCouponList(final List<Coupon> datas) {

        int size = datas.size();

        if (size == 0) {
            mCouponLayout.setVisibility(View.GONE);
        } else {
            mCouponLayout.setVisibility(View.VISIBLE);
        }
        mCouponDatas.clear();
        mCouponDatas.addAll(datas);
        final WrapAdapter<Coupon> adapter = new WrapAdapter<Coupon>(mContext, mCouponDatas,
                R.layout.coupon_list_item) {
            @Override
            public void convert(ViewHolder helper, final Coupon item) {

                helper.setImageByUrl(R.id.img, URLUtil.builderImgUrl(item.cover, 270, 203));
                TextView denominationTv = helper.getView(R.id.price);
                TextView originalPriceText = helper.getView(R.id.originalPrice);
                TextView moneyTagTv = helper.getView(R.id.moneyTag);
                Button button = helper.getView(R.id.acquireBtn);

                //设置点击事件跳转到代金券领取页面
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id);
                        CommonUtils.startNewActivity(mContext, args, CouponDetailActivity.class);
                    }
                });
                if (item.price.equals("0.00")) {
                    button.setText("领");
                    moneyTagTv.setText("");
                    denominationTv.setText("免费");
                    denominationTv.setTextColor(mRes.getColor(R.color.text_color16));
                    button.setBackgroundResource(R.drawable.button_bg_2);
                } else {
                    button.setText("抢");
                    denominationTv.setText(item.price);
                    // denominationTv.setTextColor(mRes.getColor(R.color.tab_text_s_color));
                    moneyTagTv.setText("￥");
                    button.setBackgroundResource(R.drawable.button_bg_15);
                }
                originalPriceText.setText("￥" + item.denomination);
                originalPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                helper.setText(R.id.title, item.title);
                String exp = item.expire;
                if (exp.length() > 0) {
                    exp = exp.replaceAll("-", ".");
                    exp = exp.substring(0, 11);
                }

                helper.setText(R.id.expire, "有效期至" + exp);
            }
        };
        mCouponList.setAdapter(adapter);
        //        String formatText = String.format(getString(R.string.coupon_other_count_text), 2);
        //        final View loadMoreView = ViewUtil.createFooterView(formatText,
        //            Constants.Types.LOAD_MORE_STYLE_1, mCouponList);
        //        loadMoreView.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //                mCouponDatas.addAll(datas);
        //                adapter.notifyDataSetChanged();
        //                mCouponList.removeFooterView(loadMoreView);
        //                ViewUtil.setListViewHeightBasedOnChildren(mCouponList);
        //            }
        //        });
        //
        //        mCouponList.addFooterView(loadMoreView);
        ViewUtil.setListViewHeightBasedOnChildren(mCouponList);

    }

    @Override
    public void showStoreInfo(StoreDetailData data) {

        tel = data.tel;
        lat = data.latitude;
        lng = data.longitude;

        showSalespersonLists(data.users);
        showTopicsLists(data.topics);
    }

    @Override
    public void showGifts(final List<Coupon> datas) {

        int size = datas.size();

        if (size == 0) {
            mGiftByShoppingLayout.setVisibility(View.GONE);
        } else {
            mGiftByShoppingLayout.setVisibility(View.VISIBLE);
        }

        WrapAdapter<Coupon> adapter = new WrapAdapter<Coupon>(mContext, R.layout.gift_list_item,
                datas) {
            @Override
            public void convert(ViewHolder helper, Coupon item) {

                SimpleDraweeView coverImg = helper.getView(R.id.cover);
                coverImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
                TextView titleTv = helper.getView(R.id.title);
                titleTv.setText(item.title);
                TextView descTv = helper.getView(R.id.desc);
                descTv.setText(item.desc);
            }
        };

        mGiftsList.setAdapter(adapter);
    }

    @Override
    public void showPrivileges(List<Coupon> datas) {

        int size = datas.size();

        if (size == 0) {
            mLimitCouponLayout.setVisibility(View.GONE);
        } else {
            mLimitCouponLayout.setVisibility(View.VISIBLE);
        }
        WrapAdapter<Coupon> adapter = new WrapAdapter<Coupon>(mContext, R.layout.gift_list_item,
                datas) {
            @Override
            public void convert(ViewHolder helper, Coupon item) {

                SimpleDraweeView coverImg = helper.getView(R.id.cover);
                coverImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
                TextView titleTv = helper.getView(R.id.title);
                titleTv.setText(item.title);
                TextView descTv = helper.getView(R.id.desc);
                descTv.setText(item.desc);
            }
        };

        mLimitCouponList.setAdapter(adapter);

    }

    private void showSalespersonLists(List<Account.UserInfo> users) {

        WrapAdapter<Account.UserInfo> adapter = new WrapAdapter<Account.UserInfo>(mContext, users,
                R.layout.salespersion_grid_item) {
            @Override
            public void convert(WrapAdapter.ViewHolder helper, Account.UserInfo item) {

                helper.setImageByUrl(R.id.img, URLUtil.builderImgUrl(item.avatar, 144, 144));

                helper.setText(R.id.name, item.nickName);
                helper.setText(R.id.star, item.star);
            }
        };
        mSalespersonGrid.setAdapter(adapter);
        mSalespersonGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Account.UserInfo userInfo = (Account.UserInfo) parent.getAdapter()
                        .getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.ID, userInfo.userId);
                intent.setClass(mContext, UserPageActivity.class);
                startActivity(intent);

            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(mSalespersonGrid, 6);
    }

    private void showTopicsLists(List<Topic> topics) {
        int size = topics.size();
        if (size == 0) {
            mActivityLayout.setVisibility(View.GONE);
        } else {
            mActivityLayout.setVisibility(View.VISIBLE);
        }

        WrapAdapter<Topic> adapter = new WrapAdapter<Topic>(mContext, topics,
                R.layout.topic_list_item) {
            @Override
            public void convert(ViewHolder helper, Topic item) {

                helper.setImageByUrl(R.id.img, URLUtil.builderImgUrl(item.itemImgUrl, 360, 360));

                helper.setText(R.id.title, item.title);
                String expire = DateFormatUtil.dispActiveTime2(item.begin, item.end);
                helper.setText(R.id.expire, expire);
            }
        };
        mShopActivityList.setAdapter(adapter);
        ViewUtil.setListViewHeightBasedOnChildren(mShopActivityList);
    }

    private void showEvaluateLists(final CommentModel model) {

        View v = findView(R.id.evaluateHeaderLayout);
        TextView contentTv = (TextView) v.findViewById(R.id.content);
        TextView contentTv2 = (TextView) v.findViewById(R.id.content2);
        contentTv2.setText(model.avgStar + "");
        contentTv.setText("(" + model.count + ")");
        float avgStar = Float.parseFloat(model.avgStar);
        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        setRatingScore(ratingBar, 5, avgStar, R.drawable.rating_bar_star_empty,
                R.drawable.icon_half_a_star_n, R.drawable.rating_bar_star_fill);

        mCommentsadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_evaluate,
                model.comments) {

            @Override
            public void onBind(final int pos, Comment comment, NestFullViewHolder holder) {
                SimpleDraweeView simpleDraweeView = holder.getView(R.id.img);
                simpleDraweeView
                        .setImageURI(URLUtil.builderImgUrl(comment.senderInfo.avatar, 144, 144));
                RatingBar ratingBar = holder.getView(R.id.ratingBar);
                setRatingScore(ratingBar, 5, 4.5f, R.drawable.icon_star_n,
                        R.drawable.icon_half_a_star_1_n, R.drawable.icon_star_s);
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

    @Override
    public void showDatas(CommentModel commentModel) {

        showEvaluateLists(commentModel);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {

            case R.id.evaluateHeaderLayout:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mOrgId);
                args.put(Constants.IntentParams.TEL, tel);
                args.put(Constants.IntentParams.LAT, lat);
                args.put(Constants.IntentParams.LNG, lng);
                CommonUtils.startNewActivity(mContext, args, EvaluationListActivity.class);
                break;

            case R.id.salesHeaderLayout:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mOrgId);
                CommonUtils.startNewActivity(mContext, args, SalespersonListActivity.class);
                break;

        }
    }
}
