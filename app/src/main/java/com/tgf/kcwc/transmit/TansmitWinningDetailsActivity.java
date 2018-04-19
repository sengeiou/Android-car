package com.tgf.kcwc.transmit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.autonavi.rtbt.IFrameForRTBT;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.TransmitDrawSucceedBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;
import com.tgf.kcwc.mvp.presenter.TransmitWinningDetailsPresenter;
import com.tgf.kcwc.mvp.view.TransmitWinningDetailsView;
import com.tgf.kcwc.share.BaseUIListener;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/2.
 */

public class TansmitWinningDetailsActivity extends BaseActivity implements TransmitWinningDetailsView, IUiListener {

    /**
     * 请求打电话的权限码
     */
    public static final int REQUEST_CODE_ASK_CALL_PHONE = 100;

    private TransmitWinningDetailsPresenter mTransmitWinningDetailsPresenter;

    private SimpleDraweeView covericon;                                       //封面图
    private TextView mTitle;                                          //界面
    private TextView mBrief;                                          //简要
    private TextView mRemaintime;                                     //剩余时间
    private TextView mParticipation;                                  //参与人数
    private TextView mChance;                                         //参与机会
    private LinearLayout mTransmitChance;                                 //转发机会布局

    private MyListView mPrizelistview;                                  //活动奖品
    private WrapAdapter<TransmitWinningDetailsBean.PrizeList> mPrizelistviewAdapter;                           //活动奖品adapter
    private List<TransmitWinningDetailsBean.PrizeList> prizeLists = new ArrayList<>();

    private MyListView mLotteryListview;                                //活动中奖纪录
    private WrapAdapter<TransmitWinningDetailsBean.RecordList> mLotteryListviewAdapter;                         //活动中奖纪录adapter

    private MyListView mCrunchieslistview;                              //活动榜单
    private WrapAdapter<TransmitWinningDetailsBean.RecordList> mCrunchieslistviewAdapter;                       //活动榜单adapter

    private LinearLayout mExaminelayout;                                  //查看更多布局
    private ImageView mExamineImage;                                   //查看更多图片

    private RecyclerView mparticipationview;                              //参与人列表
    private CommonAdapter<TransmitWinningDetailsBean.RecordList> mHorizontalAdapter;                              //参与人数daapter
    private List<TransmitWinningDetailsBean.RecordList> recordLists = new ArrayList<>(); //参与人数  活动榜单  活动中奖纪录 数据源
    private List<TransmitWinningDetailsBean.RecordList> recordnum = new ArrayList<>(); // 活动榜单 收起数据源数据源

    // private MixedTextImageLayout            mRichLayout;                     //活动详情
    private WebView mWebView;                                        //活动详情

    private WebView mRulewebView;                                    //活动规则

    private TextView awayTicketName;                                  //赠票名字
    private TextView awayTicketData;                                  //赠票使用日期
    private TextView awayTicketTime;                                  //赠票使用具体时间
    private TextView awayTicketMoney;                                 //赠票价格

    private TextView voucherName;                                     //代金券名字
    private TextView voucherAddress;                                  //商家地址
    private TextView voucherTime;                                     //代金券使用具体时间
    private TextView voucherMoney;                                    //代金券价格

    private SimpleDraweeView mSponsorIcon;                                    //发起方头像
    private TextView mSponsorName;                                    //发起方名字
    private TextView mAddress;                                        //发起方地址
    private ImageView mPhone;                                          //发起方电话

    private TextView mBonus;                                          //奖金总额
    private TextView mQuota;                                          //奖金限额

    private TextView mTransmitType;                                   //转发类型
    private TextView mTransmit;                                       //票证转发

    private TextView mTransmitTickets;                                //奖池内立即转发
    private LinearLayout mTransmitJackpot;                                //其他布局
    private LinearLayout mExamineAnnouncement;                            //其他布局查看榜单
    private TextView mExamineAnnouncementname;                            //其他布局 名字
    private LinearLayout mPromptlyTransmit;                               //其他布局立即转发
    private TextView mPromptlyTransmitName;

    private LinearLayout mBonuslayout;                                    //奖金总额布局
    private LinearLayout mTransmitlayout;                                 //转发的布局
    private LinearLayout mRulelayout;                                     //规则布局
    private LinearLayout mPrizelayout;                                    //奖品布局
    private LinearLayout mLotterylayout;                                  //中奖纪录布局
    private LinearLayout mDetailslayout;                                  //活动详情布局
    private LinearLayout mParticipationlayout;                            //参与人布局
    private LinearLayout mAnnouncementlayout;                             //活动榜单布局
    private LinearLayout awayTicketLayout;                                //赠票布局
    private LinearLayout voucherLayout;                                   //代金券布局

    private ImageView mGoTop;

    private boolean isSelect = false;             //false 是显示3个
    private String ID = "";

    private TransmitWinningDetailsBean transmitWinningDetailsBean;
    private OpenShareWindow openShareWindow;
    private KPlayCarApp kPlayCarApp;
    private WbShareHandler mWbHandler = null;

    private boolean IsShare = false;
    private ObservableScrollView mScrollView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTransmitWinningDetailsPresenter != null) {
            mTransmitWinningDetailsPresenter.detachView();
        }
    }

    @Override
    protected void setUpViews() {
        kPlayCarApp = (KPlayCarApp) getApplication();
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        mTransmitWinningDetailsPresenter = new TransmitWinningDetailsPresenter();
        mTransmitWinningDetailsPresenter.attachView(this);
        mTransmitlayout = (LinearLayout) findViewById(R.id.transmitlayout);
        mRulelayout = (LinearLayout) findViewById(R.id.rulelayout);
        mRulelayout = (LinearLayout) findViewById(R.id.rulelayout);
        mPrizelayout = (LinearLayout) findViewById(R.id.prizelayout);
        mLotterylayout = (LinearLayout) findViewById(R.id.lotterylayout);
        mDetailslayout = (LinearLayout) findViewById(R.id.detailslayout);
        mParticipationlayout = (LinearLayout) findViewById(R.id.participationlayout);
        mAnnouncementlayout = (LinearLayout) findViewById(R.id.announcementlayout);

        mTransmitTickets = (TextView) findViewById(R.id.transmit_tickets);
        mTransmitJackpot = (LinearLayout) findViewById(R.id.transmit_jackpot);
        mExamineAnnouncement = (LinearLayout) findViewById(R.id.examine_announcement);
        mPromptlyTransmit = (LinearLayout) findViewById(R.id.promptly_transmit);
        mPromptlyTransmitName = (TextView) findViewById(R.id.promptly_transmit_name);

        covericon = (SimpleDraweeView) findViewById(R.id.covericon);
        mTitle = (TextView) findViewById(R.id.title);
        mBrief = (TextView) findViewById(R.id.brief);
        mRemaintime = (TextView) findViewById(R.id.remaintime);
        mParticipation = (TextView) findViewById(R.id.participation);
        mChance = (TextView) findViewById(R.id.chance);
        mRulewebView = (WebView) findViewById(R.id.rulewebview);
        mPrizelistview = (MyListView) findViewById(R.id.prizelistview);
        mparticipationview = (RecyclerView) findViewById(
                R.id.participation_recyclerview_horizontal);
        mSponsorIcon = (SimpleDraweeView) findViewById(R.id.sponsor_icon);
        mSponsorName = (TextView) findViewById(R.id.sponsor_name);
        mAddress = (TextView) findViewById(R.id.address);
        mPhone = (ImageView) findViewById(R.id.phone);
        mWebView = (WebView) findViewById(R.id.web_view);
        awayTicketLayout = (LinearLayout) findViewById(R.id.away_ticket_layout);
        awayTicketName = (TextView) findViewById(R.id.away_ticket_name);
        awayTicketData = (TextView) findViewById(R.id.away_ticket_date);
        awayTicketTime = (TextView) findViewById(R.id.away_ticket_time);
        awayTicketMoney = (TextView) findViewById(R.id.away_ticket_money);

        voucherLayout = (LinearLayout) findViewById(R.id.voucherlayout);
        voucherName = (TextView) findViewById(R.id.voucher_name);
        voucherAddress = (TextView) findViewById(R.id.voucher_address);
        voucherTime = (TextView) findViewById(R.id.voucher_time);
        voucherMoney = (TextView) findViewById(R.id.voucher_money);

        mCrunchieslistview = (MyListView) findViewById(R.id.crunchieslistview);
        mExaminelayout = (LinearLayout) findViewById(R.id.examinelayout);
        mExamineImage = (ImageView) findViewById(R.id.dexamine_rivingtest);
        mBonuslayout = (LinearLayout) findViewById(R.id.bonuslayout);
        mBonus = (TextView) findViewById(R.id.bonus);
        mQuota = (TextView) findViewById(R.id.quota);
        mLotteryListview = (MyListView) findViewById(R.id.lotterylistview);

        mTransmitType = (TextView) findViewById(R.id.transmit_type);
        mTransmit = (TextView) findViewById(R.id.transmit);
        mTransmitChance = (LinearLayout) findViewById(R.id.transmitchance);
        mGoTop = (ImageView) findViewById(R.id.gotop);
        mExamineAnnouncementname = (TextView) findViewById(R.id.examine_announcementname);
        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        //mScrollView.setScrollViewListener(mScrollListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mparticipationview.setLayoutManager(linearLayoutManager);

        mparticipationview.setFocusable(false);
        mCrunchieslistview.setFocusable(false);
        mPrizelistview.setFocusable(false);
        mLotteryListview.setFocusable(false);
        mExaminelayout.setOnClickListener(this);
        mExamineAnnouncement.setOnClickListener(this);
        mPromptlyTransmit.setOnClickListener(this);
        mTransmit.setOnClickListener(this);
        mTransmitTickets.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        mGoTop.setOnClickListener(this);
        setAdapter();
        mTransmitWinningDetailsPresenter.getDetails(IOUtils.getToken(mContext), ID);
    }

    public void setAdapter() {
        //奖品adapter
        mPrizelistviewAdapter = new WrapAdapter<TransmitWinningDetailsBean.PrizeList>(mContext,
                R.layout.tansmitwinning_prize_item, prizeLists) {
            @Override
            public void convert(ViewHolder helper, TransmitWinningDetailsBean.PrizeList item) {
                int position = helper.getPosition();

                LinearLayout awayTicketLayout = helper.getView(R.id.away_ticket_layout);//赠票布局
                TextView awayTicketName = helper.getView(R.id.away_ticket_name);//赠票名字
                TextView awayTicketData = helper.getView(R.id.away_ticket_date);//赠票使用日期
                TextView awayTicketTime = helper.getView(R.id.away_ticket_time);//赠票使用具体时间
                TextView awayTicketMoney = helper.getView(R.id.away_ticket_money);//赠票价格

                LinearLayout voucherLayout = helper.getView(R.id.voucherlayout);//代金券布局
                TextView voucherName = helper.getView(R.id.voucher_name);//代金券名字
                TextView voucherAddress = helper.getView(R.id.voucher_address);//商家地址
                TextView voucherTime = helper.getView(R.id.voucher_time);//代金券使用具体时间
                TextView voucherMoney = helper.getView(R.id.voucher_money);//代金券价格

                LinearLayout generallayout = helper.getView(R.id.generallayout); //普通奖品布局
                SimpleDraweeView bigimage = helper.getView(R.id.bigimage); //普通奖品图片
                TextView grade = helper.getView(R.id.grade);//普通奖品 等级
                TextView name = helper.getView(R.id.name);//普通奖品 名字
                TextView number = helper.getView(R.id.number);//普通奖品 数量
                int type = item.type;
                switch (type) {
                    case 1:
                        awayTicketLayout.setVisibility(View.GONE);
                        voucherLayout.setVisibility(View.GONE);
                        generallayout.setVisibility(View.VISIBLE);
                        bigimage.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.img, 144, 144)));
                        grade.setText("【" + item.name + "】");
                        name.setText(item.subName);
                        number.setText("数量:" + item.nums);
                        break;
                    case 2:
                        if (item.prizeType == 1) {
                            awayTicketLayout.setVisibility(View.VISIBLE);
                            voucherLayout.setVisibility(View.GONE);
                            generallayout.setVisibility(View.GONE);
                            awayTicketName.setText(item.eventName);
                            awayTicketData.setText(item.subName);
                            awayTicketTime.setText(item.remarks);
                            awayTicketMoney.setText(item.price + "");
                            awayTicketMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                        } else {
                            awayTicketLayout.setVisibility(View.GONE);
                            voucherLayout.setVisibility(View.VISIBLE);
                            generallayout.setVisibility(View.GONE);
                            voucherName.setText(item.subName);
                            voucherTime.setText("有效期至  " + item.useTimeEnd);
                            voucherMoney.setText(item.price + "");
                            voucherMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                            voucherAddress.setText("");
                        }
                        break;
                    case 3:
                        awayTicketLayout.setVisibility(View.GONE);
                        voucherLayout.setVisibility(View.GONE);
                        generallayout.setVisibility(View.GONE);
                        break;
                }

            }
        };
        mPrizelistview.setAdapter(mPrizelistviewAdapter);

        //中奖纪录adapter
        mLotteryListviewAdapter = new WrapAdapter<TransmitWinningDetailsBean.RecordList>(mContext,
                R.layout.tansmitwinning_lottery_item, recordLists) {
            @Override
            public void convert(ViewHolder helper, TransmitWinningDetailsBean.RecordList item) {
                int position = helper.getPosition();
                SimpleDraweeView simpleDraweeView = helper.getView(R.id.head);
                TextView name = helper.getView(R.id.name);
                TextView describe = helper.getView(R.id.describe);
                simpleDraweeView
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.avatar, 144, 144)));
                name.setText(item.name);
                describe.setText(item.prizeNameSub);
            }
        };
        mLotteryListview.setAdapter(mLotteryListviewAdapter);

        //报名人数 adapter
        mHorizontalAdapter = new CommonAdapter<TransmitWinningDetailsBean.RecordList>(mContext,
                R.layout.drivingdetail_horizon_item, recordLists) {
            @Override
            public void convert(ViewHolder holder,
                                final TransmitWinningDetailsBean.RecordList applyList) {
                SimpleDraweeView simpleDraweeView = holder
                        .getView(R.id.drivdetail_recycler_view_imageview);
                simpleDraweeView
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(applyList.avatar, 144, 144)));
                simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, applyList.uid);
                        CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);
                    }
                });
            }
        };
        mparticipationview.setAdapter(mHorizontalAdapter);


        //榜单
        mCrunchieslistviewAdapter = new WrapAdapter<TransmitWinningDetailsBean.RecordList>(mContext,
                R.layout.tansmitwinning_notice_item, recordnum) {
            @Override
            public void convert(ViewHolder helper, TransmitWinningDetailsBean.RecordList item) {
                int position = helper.getPosition();
                TextView numtext = helper.getView(R.id.numtext);
                ImageView numimag = helper.getView(R.id.numimag);
                SimpleDraweeView head = helper.getView(R.id.head);
                TextView name = helper.getView(R.id.name);
                TextView integral = helper.getView(R.id.integral);
                int nums = position + 1;
                numtext.setVisibility(View.GONE);
                numimag.setVisibility(View.GONE);
                if (nums == 1) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_gold);
                } else if (nums == 2) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_yin);
                } else if (nums == 3) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_tong);
                } else {
                    numtext.setVisibility(View.VISIBLE);
                    numimag.setVisibility(View.GONE);
                    numtext.setText(nums + "");
                }
                head.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.avatar, 144, 144)));
                integral.setText("贡献值 " + item.score);
                name.setText(item.name);
            }
        };
        mCrunchieslistview.setAdapter(mCrunchieslistviewAdapter);
    }

    public void setNumData(List<TransmitWinningDetailsBean.RecordList> recordLists) {
        //榜单
        mCrunchieslistviewAdapter = new WrapAdapter<TransmitWinningDetailsBean.RecordList>(mContext,
                R.layout.tansmitwinning_notice_item, recordLists) {
            @Override
            public void convert(ViewHolder helper, TransmitWinningDetailsBean.RecordList item) {
                int position = helper.getPosition();
                TextView numtext = helper.getView(R.id.numtext);
                ImageView numimag = helper.getView(R.id.numimag);
                SimpleDraweeView head = helper.getView(R.id.head);
                TextView name = helper.getView(R.id.name);
                TextView integral = helper.getView(R.id.integral);
                int nums = position + 1;
                numtext.setVisibility(View.GONE);
                numimag.setVisibility(View.GONE);
                if (nums == 1) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_gold);
                } else if (nums == 2) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_yin);
                } else if (nums == 3) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_tong);
                } else {
                    numtext.setVisibility(View.VISIBLE);
                    numimag.setVisibility(View.GONE);
                    numtext.setText(nums + "");
                }
                head.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.avatar, 144, 144)));
                integral.setText("贡献值 " + item.score);
                name.setText(item.name);
            }
        };
        mCrunchieslistview.setAdapter(mCrunchieslistviewAdapter);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.examinelayout:
                //判断getCount()数据的数量，如果等于3点击后就设置getCount()为全部数量，设置修改标识，刷新。 否则，相反。
                if (!isSelect) {
                    setNumData(recordLists);
                    mExamineImage
                            .setImageDrawable(getResources().getDrawable(R.drawable.btn_pullup));
                } else {
                    setNumData(recordnum);
                    mExamineImage
                            .setImageDrawable(getResources().getDrawable(R.drawable.btn_pulldown));
                }
                isSelect = !isSelect;
                break;
            case R.id.gotop:
                mScrollView.smoothScrollTo(0, 0);
                break;
            case R.id.examine_announcement: //查看榜单
                if (transmitWinningDetailsBean.data.details.type == 5 || transmitWinningDetailsBean.data.details.type == 7) {
                    if (IOUtils.isLogin(mContext)) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, ID);
                        CommonUtils.startNewActivity(mContext, args, RaffleRecordActivity.class);
                    }
                } else {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, transmitWinningDetailsBean.data.details.id + "");
                    CommonUtils.startNewActivity(mContext, args, TansmitWinningCrunchiesActivity.class);
                }
                break;
            case R.id.phone: //电话
                requestPermission(transmitWinningDetailsBean.data.create.tel);
                break;
            case R.id.promptly_transmit: //下面转发
            case R.id.transmit_tickets: //单个转发
            case R.id.transmit: //上面立即转发
                if (IOUtils.isLogin(mContext)) {
                    share();
                }
                break;
        }
    }

    public void share() {
        openShareWindow = new OpenShareWindow(mContext);
        openShareWindow.show(TansmitWinningDetailsActivity.this);
        TransmitWinningDetailsBean.Share share = transmitWinningDetailsBean.data.share;
        final String title = share.title;
        final String description = share.desc;
        final String cover = URLUtil.builderImgUrl(share.img, 540, 270);
        final String url = share.url;

        openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                switch (position) {
                    case 0:
                        OpenShareUtil.sendWXMoments2(mContext, kPlayCarApp.getMsgApi(),
                                title, description, url);
                        break;
                    case 1:
                        OpenShareUtil.sendWX2(mContext, kPlayCarApp.getMsgApi(), title,
                                description, url);
                        break;
                    case 2:
                        mWbHandler = OpenShareUtil.shareSina(TansmitWinningDetailsActivity.this,
                                title, description, url);
                        mWbHandler.registerApp();
                        break;
                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone2(kPlayCarApp.getTencent(),
                                TansmitWinningDetailsActivity.this, TansmitWinningDetailsActivity.this, urls, title,
                                description, url);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ2(kPlayCarApp.getTencent(),
                                TansmitWinningDetailsActivity.this, TansmitWinningDetailsActivity.this, title, cover,
                                description, url);
                        break;
                }
                IsShare = true;
                openShareWindow.dismiss();

            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        setTitleBarDrawable(R.drawable.shape_titlebar_bg);
        backEvent(back);
        text.setText("");
        // text.setTextColor(mRes.getColor(R.color.text_color15));
        // function.setImageResource(R.drawable.global_nav_n);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Basecolors = R.color.transparent;
        //isTitleBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transwinningdetails);
    }

    @Override
    public void dataListSucceed(TransmitWinningDetailsBean transmitWinningHomeListBean) {
        transmitWinningDetailsBean = transmitWinningHomeListBean;
        TransmitWinningDetailsBean.Details details = transmitWinningDetailsBean.data.details; //详情
        TransmitWinningDetailsBean.Create create = transmitWinningDetailsBean.data.create; //发起人
        TransmitWinningDetailsBean.ForwardInfo forwardInfo = transmitWinningDetailsBean.data.forwardInfo;//转发信息

        covericon.setImageURI(Uri.parse(URLUtil.builderImgUrl(details.cover, 540, 270)));//item图片
        mTitle.setText(details.name);
        mBrief.setText(details.desc);
        String times = DateFormatUtil.formatTime((long) details.second * 1000);
        mRemaintime.setText("剩余：" + times);
        if (details.haveNums > 0) {
            mTransmitChance.setVisibility(View.VISIBLE);
            mChance.setText(details.haveNums + "");
        } else {
            mTransmitChance.setVisibility(View.GONE);
        }
        mParticipation.setText(details.times + "人参与");
        mRulewebView.loadData(WebviewUtil.getHtmlData(details.rules), "text/html; charset=utf-8",
                "utf-8");
        mWebView.loadData(WebviewUtil.getHtmlData(details.intro), "text/html; charset=utf-8",
                "utf-8");

        mSponsorIcon.setImageURI(Uri.parse(URLUtil.builderImgUrl(create.logo, 144, 144)));
        mSponsorName.setText(create.name);
        mAddress.setText(create.address);

        mBonuslayout.setVisibility(View.GONE); //奖金总额布局
        mTransmitlayout.setVisibility(View.GONE);//转发的布局
        awayTicketLayout.setVisibility(View.GONE); //赠票布局
        voucherLayout.setVisibility(View.GONE); //代金券布局
        mLotterylayout.setVisibility(View.GONE); //中奖纪录布局
        mParticipationlayout.setVisibility(View.GONE); //参与人布局
        mAnnouncementlayout.setVisibility(View.GONE); //活动榜单布局
        mTransmitTickets.setVisibility(View.GONE); //奖池内立即转发
        mTransmitJackpot.setVisibility(View.GONE); //其他布局
        int type = details.type;
        //活动类型1领票2领券3按贡献值排名4奖金池5轮盘抽奖6摇一摇抽奖
        switch (type) {
            case 1://1领票
                mParticipationlayout.setVisibility(View.VISIBLE); //参与人布局
                mTransmitTickets.setVisibility(View.VISIBLE);//奖池内立即转发
                break;
            case 2://2领券
                mParticipationlayout.setVisibility(View.VISIBLE); //参与人布局
                mTransmitTickets.setVisibility(View.VISIBLE);//奖池内立即转发
                break;
            case 3://3按贡献值排名
                mTransmitlayout.setVisibility(View.VISIBLE);//转发的布局
                mTransmitJackpot.setVisibility(View.VISIBLE); //其他布局
                mExamineAnnouncementname.setText("查看榜单");
                mAnnouncementlayout.setVisibility(View.VISIBLE); //活动榜单布局
                break;
            case 4://4奖金池
                mTransmitlayout.setVisibility(View.VISIBLE);//转发的布局
                mTransmitJackpot.setVisibility(View.VISIBLE); //其他布局
                mBonuslayout.setVisibility(View.VISIBLE); //奖金总额布局
                mAnnouncementlayout.setVisibility(View.VISIBLE); //活动榜单布局
                mExamineAnnouncementname.setText("查看榜单");
                mPrizelayout.setVisibility(View.GONE);
                mTransmitChance.setVisibility(View.GONE);
                break;
            case 5://5轮盘抽奖
            case 7://7轮盘抽奖
                mRulelayout.setVisibility(View.VISIBLE);//规则布局
                mTransmitlayout.setVisibility(View.VISIBLE);//转发的布局
                mTransmitJackpot.setVisibility(View.VISIBLE); //其他布局
                mExamineAnnouncementname.setText("中奖记录");
                mLotterylayout.setVisibility(View.VISIBLE); //中奖记录布局
                break;
            case 6://6摇一摇抽奖
                break;
        }

        if (type != 2 || type != 1) {
            if (details.haveNums != 0 || details.limits == 0) {
                mTransmitTickets.setText("立即转发");
                mTransmitTickets.setFocusable(true);
                mTransmitTickets.setTextColor(getResources().getColor(R.color.white));
                mTransmitTickets.setBackgroundColor(getResources().getColor(R.color.text_color16));
            } else {
                if (details.limitsType == 1) {
                    mTransmitTickets.setText("今日已参与");
                } else {
                    mTransmitTickets.setText("已参与");
                }
                mTransmitTickets.setFocusable(false);
                mTransmitTickets.setOnClickListener(null);
                mTransmitTickets.setTextColor(getResources().getColor(R.color.color_login_devide));
                mTransmitTickets
                        .setBackgroundColor(getResources().getColor(R.color.split_line_color));
            }
            if (transmitWinningDetailsBean.data.forwardInfo.type == 2) {
                awayTicketLayout.setVisibility(View.VISIBLE); //赠票布局
                voucherLayout.setVisibility(View.GONE); //代金券布局
                awayTicketName.setText(forwardInfo.eventName);
                awayTicketData.setText(forwardInfo.name);
                awayTicketTime.setText(forwardInfo.useTimeStart + " - " + forwardInfo.useTimeEnd);
                awayTicketMoney.setText(forwardInfo.price + "");
                awayTicketMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                mTransmitType.setText("把赠票转发给好友");
            } else if (transmitWinningDetailsBean.data.forwardInfo.type == 3) {
                awayTicketLayout.setVisibility(View.GONE); //赠票布局
                voucherLayout.setVisibility(View.VISIBLE); //代金券布局
                voucherName.setText(forwardInfo.name);
                voucherTime.setText("有效期至  " + forwardInfo.useTimeEnd);
                voucherMoney.setText(forwardInfo.price + "");
                voucherMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                voucherAddress.setText("");
                mTransmitType.setText("把代金券转发给好友");
            } else {
                mTransmitlayout.setVisibility(View.GONE);//转发的布局
                awayTicketLayout.setVisibility(View.GONE); //赠票布局
                voucherLayout.setVisibility(View.GONE); //代金券布局
            }
        } else {
            if (details.haveNums != 0 || details.limits == 0) {
                mPromptlyTransmitName.setText("立即转发");
                mPromptlyTransmitName.setFocusable(true);
                mPromptlyTransmitName.setOnClickListener(this);
            } else {
                if (details.limitsType == 1) {
                    mPromptlyTransmitName.setText("今日已参与");
                } else {
                    mPromptlyTransmitName.setText("已参与");
                }
                mPromptlyTransmitName.setFocusable(false);
                mPromptlyTransmitName.setOnClickListener(null);
            }
        }
        if (type != 4) {
            prizeLists.clear();
            prizeLists.addAll(transmitWinningDetailsBean.data.prizeList);
            mPrizelistviewAdapter.notifyDataSetChanged();
        }
        recordLists.clear();
        recordnum.clear();
        recordLists.addAll(transmitWinningDetailsBean.data.recordList);
        if (type == 1 || type == 2) {
            mHorizontalAdapter.notifyDataSetChanged();
        } else if (type == 3 || type == 4) {
            if (recordLists.size() >= 3) {
                for (int i = 0; i < 3; i++) {
                    recordnum.add(recordLists.get(i));
                }
                mExaminelayout.setVisibility(View.VISIBLE);
            } else {
                recordnum.addAll(recordLists);
                mExaminelayout.setVisibility(View.GONE);
            }
            mCrunchieslistviewAdapter.notifyDataSetChanged();
        } else {
            mLotteryListviewAdapter.notifyDataSetChanged();
        }

        if (null == recordLists || recordLists.size() <= 0) {
            mParticipationlayout.setVisibility(View.GONE);
        }
        if (type == 4) {
            mBonus.setText(details.totalPrice + "");
            if (details.limitsType == 1) {
                mQuota.setText("每日限额￥" + details.limits);
            } else {
                mQuota.setText("单人限额￥" + details.limits);
            }
        }

    }

    @Override
    public void dataForwardSucceed(TransmitDrawSucceedBean BaseBean) {
        CommonUtils.showToast(mContext, "分享成功");
        mTransmitWinningDetailsPresenter.getDetails(IOUtils.getToken(mContext), ID);
        if (transmitWinningDetailsBean.data.details.type == 5 || transmitWinningDetailsBean.data.details.type == 7) {
            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, ID);
            CommonUtils.startNewActivity(mContext, args, TransmitWinningDialActivity.class);
        } else {
            if (BaseBean.data.prizeId > 0) {
                setRequireSucceed(BaseBean.data.prizeName);
            } else {
                setRequireDefeated();
            }
        }
    }


    /**
     * 谢谢参与
     */
    public void setRequireDefeated() {
        View diaEdtext = View.inflate(mContext, R.layout.transmitwinning_popwindows, null);
        TextView know = (TextView) diaEdtext.findViewById(R.id.know);
        TextView name = (TextView) diaEdtext.findViewById(R.id.name);
        name.setText("你来晚了，奖品已发放完");
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(diaEdtext).setPositiveButton("", null).setNegativeButton("", null).show();

        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mTransmitWinningDetailsPresenter.getPrizeList(IOUtils.getToken(mContext), ID);
                alertDialog.dismiss();
            }
        });
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.height = (int) (WindowManager.LayoutParams.WRAP_CONTENT); //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.5
        alertDialog.getWindow().setAttributes(p); //设置生效

    }

    /**
     * 获得奖品
     */
    public void setRequireSucceed(String name) {
        View diaEdtext = View.inflate(mContext, R.layout.transmitwinning_popwindows_succeed, null);
        TextView know = (TextView) diaEdtext.findViewById(R.id.know);
        TextView title = (TextView) diaEdtext.findViewById(R.id.title);
        title.setText(name);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(diaEdtext).setPositiveButton("", null).setNegativeButton("", null).show();

        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mTransmitWinningDetailsPresenter.getPrizeList(IOUtils.getToken(mContext), ID);
                alertDialog.dismiss();
            }
        });
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.height = (int) (WindowManager.LayoutParams.WRAP_CONTENT); //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.5
        alertDialog.getWindow().setAttributes(p); //设置生效

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTransmitWinningDetailsPresenter.getDetails(IOUtils.getToken(mContext), ID);
        if (IsShare) {
            String value = KPlayCarApp.getValue(Constants.IntentParams.INTENT_TYPE);
            if (value != null) {
                if (value.equals(Constants.Share.SHARE_SUCCEED)) { //微信分享成功
                    shareSucceed();
                    IsShare = false;
                }
            }
        }
    }


    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
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

    /**
     * 申请权限
     */
    private void requestPermission(String tel) {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                callPhone(tel);
            }
        } else {
            callPhone(tel);
        }
    }

    /**
     * 拨号方法
     */
    private void callPhone(String tel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + tel));
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        onShare(Constants.InteractionCode.REQUEST_CODE, RESULT_OK, intent,
                Constants.InteractionCode.WEB_BO_TYPE);
    }


    public void onShare(int requestCode, int resultCode, Intent data, int type) {

        switch (type) {
            case Constants.InteractionCode.QQ_OR_ZONE_TYPE:
                if (Constants.InteractionCode.QQ_SHARE_REQUEST_CODE == requestCode) {
                    Tencent.onActivityResultData(requestCode, resultCode, data, TansmitWinningDetailsActivity.this);
                } else if (Constants.InteractionCode.QQ_ZONE_SHARE_REQUEST_CODE == requestCode) {
                    Tencent.onActivityResultData(requestCode, resultCode, data, TansmitWinningDetailsActivity.this);
                }
                break;

            case Constants.InteractionCode.WEB_BO_TYPE:
                if (mWbHandler != null) {
                    mWbHandler.doResultIntent(data, new WbShareCallback() {
                        @Override
                        public void onWbShareSuccess() {
                            shareSucceed();
                        }

                        @Override
                        public void onWbShareCancel() {
                        }

                        @Override
                        public void onWbShareFail() {
                            CommonUtils.showToast(mContext, "分享失败");
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.InteractionCode.QQ_SHARE_REQUEST_CODE) {
            onShare(requestCode, resultCode, data, Constants.InteractionCode.QQ_OR_ZONE_TYPE);
        } else if (requestCode == Constants.InteractionCode.QQ_ZONE_SHARE_REQUEST_CODE) {
            onShare(requestCode, resultCode, data, Constants.InteractionCode.QQ_OR_ZONE_TYPE);
        }
    }

    /**
     * QQ  QQ空间回调
     *
     * @param o
     */
    @Override
    public void onComplete(Object o) {
        shareSucceed();
    }

    @Override
    public void onError(UiError uiError) {
        CommonUtils.showToast(mContext, "分享失败:" + uiError.errorMessage);
    }

    @Override
    public void onCancel() {
    }

    public void shareSucceed() {
        mTransmitWinningDetailsPresenter.getForward(IOUtils.getToken(mContext), ID, "1");
    }

    private ObservableScrollView.ScrollViewListener mScrollListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                    int oldy) {

            int topHeight = BitmapUtils.dp2px(mContext, 132);
            if (y <= 0) {   //设置标题的背景颜色
                // textView.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
                setTitleBarDrawable(R.drawable.shape_titlebar_bg);
                //findViewById(R.id.titleBar).setBackgroundColor(Color.argb((int) 0, 54, 169, 92));
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

}
