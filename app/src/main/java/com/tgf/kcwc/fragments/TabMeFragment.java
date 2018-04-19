package com.tgf.kcwc.fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.E;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.businessconcerns.BusinessAttentionActivity;
import com.tgf.kcwc.certificate.CertListActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.ScanOffCouponActivity;
import com.tgf.kcwc.coupon.VoucherMainActivity;
import com.tgf.kcwc.coupon.manage.CouponManagerActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.me.AttentionListActivity;
import com.tgf.kcwc.me.ContactsListActivity;
import com.tgf.kcwc.me.DealerMgrActivity;
import com.tgf.kcwc.me.FansListActivity;
import com.tgf.kcwc.me.LocalAttentionListActivity;
import com.tgf.kcwc.me.MyFavoriteActivity;
import com.tgf.kcwc.me.MyQCodeActivity;
import com.tgf.kcwc.me.SaleMgrListActivity;
import com.tgf.kcwc.me.UserInfoActivity;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.me.exhibition.StoreExhibitionActivity;
import com.tgf.kcwc.me.integral.IntegralActivity;
import com.tgf.kcwc.me.integral.orgintegral.OrgIntegralActivity;
import com.tgf.kcwc.me.message.MessageActivity;
import com.tgf.kcwc.me.mybalance.MyBalanceActivity;
import com.tgf.kcwc.me.myline.MyLineMainActivity;
import com.tgf.kcwc.me.setting.SetHomeActivity;
import com.tgf.kcwc.me.storebelow.StoreBelowActivity;
import com.tgf.kcwc.membercenter.driv.DrivingActivity;
import com.tgf.kcwc.membercenter.please.PleasePlayActivity;
import com.tgf.kcwc.mvp.model.UserHomeDataModel;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.ourglory.OurGloryHomeActivity;
import com.tgf.kcwc.potentialcustomertrack.PotentialCustomerTrackActivity;
import com.tgf.kcwc.redpack.RedpackManageListActivity;
import com.tgf.kcwc.see.evaluate.PopmanESActivity;
import com.tgf.kcwc.seecar.MySeecarMsgActivity;
import com.tgf.kcwc.seecar.manage.PreSeeOrderActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.ticket.TicketActivity;
import com.tgf.kcwc.ticket.apply.PreRegistrationActivity;
import com.tgf.kcwc.ticket.manage.TickerManagerActivity;
import com.tgf.kcwc.transmit.TransmitWinningHomeActivity;
import com.tgf.kcwc.tripbook.MyTripbookActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.InvitationFriendWindow;
import com.tgf.kcwc.view.LineGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:16/4/23 22:16
 * E-mail:fishloveqin@gmail.com
 */
public class TabMeFragment extends BaseFragment implements UserDataView<UserHomeDataModel> {

    private static final String tag = "TabHomePage";
    protected View rootView;
    protected SimpleDraweeView bigHeaderImg;
    protected ImageButton msgTv;
    protected TextView unreadMsgTv;
    protected ImageButton contactTv;
    protected ImageButton settingTv;
    protected RelativeLayout toolBars;
    protected SimpleDraweeView tagHeaderImg;
    protected SimpleDraweeView genderImg;
    protected RelativeLayout headerImgLayout;
    protected TextView nametv;
    protected ImageView commentModelTv;
    protected ImageView commentPopmanTv;
    protected SimpleDraweeView brandLogo;
    protected LinearLayout userTagLayout;
    protected TextView userLevel;
    protected ImageView qrcode;
    protected RelativeLayout userInfoLayout;
    protected RelativeLayout topLayout;
    protected GridView gridView;
    protected TextView balanceTv;
    protected RelativeLayout balanceLayout;
    protected TextView expTv;
    protected RelativeLayout expValueLayout;
    protected RelativeLayout organizationIntegralLayout;
    protected TextView businessTv;
    protected LineGridView showMenuGv;
    protected ImageView img1;
    protected TextView mylineTv;
    protected RelativeLayout myLineLayout;
    protected ImageView img2;
    protected TextView driveTv;
    protected RelativeLayout driveDataLayout;
    protected GridView gridView2;
    protected RelativeLayout bestMileLayout;
    protected RelativeLayout bestTimeLayout;
    protected ImageView img3;
    protected RelativeLayout topListLayout;
    protected ImageView img4;
    protected RelativeLayout shareLayout;
    protected LinearLayout contentLayout;
    private TextView mBestTimeTv;
    private TextView mBestDistanceTv;
    private UserDataPresenter mPresenter;
    private LineGridView showBusinessMenuGv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void updateData() {
        mPresenter.loadUserHomeInfo(IOUtils.getToken(mContext), "APP");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {
        mAPP = (KPlayCarApp) getActivity().getApplication();
        bigHeaderImg = findView(R.id.bigHeaderImg);
        bigHeaderImg.setOnClickListener(this);
        msgTv = findView(R.id.msgTv);
        msgTv.setOnClickListener(this);
        unreadMsgTv = findView(R.id.unreadMsgTv);
        unreadMsgTv.setOnClickListener(this);
        contactTv = findView(R.id.contactTv);
        contactTv.setOnClickListener(this);
        settingTv = findView(R.id.settingTv);
        settingTv.setOnClickListener(this);
        toolBars = findView(R.id.toolBars);
        tagHeaderImg = findView(R.id.tagHeaderImg);
        genderImg = findView(R.id.genderImg);
        headerImgLayout = findView(R.id.headerImgLayout);
        headerImgLayout.setOnClickListener(this);
        nametv = findView(R.id.nametv);
        commentModelTv = findView(R.id.comment_model_tv);
        commentPopmanTv = findView(R.id.comment_popman_tv);
        brandLogo = findView(R.id.brandLogo);
        userTagLayout = findView(R.id.userTagLayout);
        userLevel = findView(R.id.userLevel);
        qrcode = findView(R.id.qrcode);
        qrcode.setOnClickListener(this);
        userInfoLayout = findView(R.id.userInfoLayout);
        userInfoLayout.setOnClickListener(this);
        topLayout = findView(R.id.topLayout);
        gridView = findView(R.id.gridView);
        balanceTv = findView(R.id.balanceTv);
        balanceLayout = findView(R.id.balanceLayout);
        balanceLayout.setOnClickListener(this);
        expTv = findView(R.id.expTv);
        expValueLayout = findView(R.id.expValueLayout);
        expValueLayout.setOnClickListener(this);
        businessTv = findView(R.id.business);
        showMenuGv = findView(R.id.showMenuGv);
        showBusinessMenuGv = findView(R.id.showBusinessMenuGv);
        img1 = findView(R.id.img1);
        mylineTv = findView(R.id.mylineTv);
        myLineLayout = findView(R.id.myLineLayout);
        myLineLayout.setOnClickListener(this);
        img2 = findView(R.id.img2);
        driveTv = findView(R.id.driveTv);
        driveDataLayout = findView(R.id.driveDataLayout);
        driveDataLayout.setOnClickListener(this);
        gridView2 = findView(R.id.gridView2);
        bestMileLayout = findView(R.id.bestMileLayout);
        bestTimeLayout = findView(R.id.bestTimeLayout);
        img3 = findView(R.id.img3);
        topListLayout = findView(R.id.topListLayout);
        topListLayout.setOnClickListener(this);
        img4 = findView(R.id.img4);
        shareLayout = findView(R.id.shareLayout);
        shareLayout.setOnClickListener(this);
        contentLayout = findView(R.id.contentLayout);
        mBestTimeTv = findView(R.id.bestTimeTv);
        mBestDistanceTv = findView(R.id.bestDistanceTv);
        organizationIntegralLayout = findView(R.id.organization_integral);
        organizationIntegralLayout.setOnClickListener(this);
        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);
        initGridViewData();
        invitationFriendWindow = new InvitationFriendWindow(mContext);
        findView(R.id.orgBalanceTv).setOnClickListener(this);
        findView(R.id.applyRegBtn).setOnClickListener(this);
    }

    private InvitationFriendWindow invitationFriendWindow = null;
    private WrapAdapter<DataItem> mGridAdapter = null;
    private WrapAdapter<DataItem> mGridAdapter2 = null;
    private List<DataItem> gridItems = new ArrayList<DataItem>();
    private List<DataItem> gridItems2 = new ArrayList<DataItem>();

    private UserHomeDataModel mModel;
    KPlayCarApp mAPP;

    private void initGridViewData() {

        String[] arrays = mRes.getStringArray(R.array.user_tabs);

        for (String s : arrays) {

            DataItem dataItem = new DataItem();
            dataItem.title = s;
            dataItem.content = "";
            gridItems.add(dataItem);
        }

        mGridAdapter = new WrapAdapter<DataItem>(mContext, R.layout.common_text_list_item3,
                gridItems) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                helper.setText(R.id.title, item.title);
                helper.setText(R.id.content, item.content);
            }
        };

        gridView.setAdapter(mGridAdapter);

        arrays = mRes.getStringArray(R.array.ride_infos);

        for (String s : arrays) {

            DataItem dataItem = new DataItem();
            dataItem.title = s;
            dataItem.content = "122";
            gridItems2.add(dataItem);
        }

        mGridAdapter2 = new WrapAdapter<DataItem>(mContext, R.layout.track_item, gridItems2) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                titleTv.setText(item.title);
                titleTv.setTextColor(mRes.getColor(R.color.text_color15));
                titleTv.setTextSize(12);
                TextView contentTv = helper.getView(R.id.content);
                contentTv.setText(item.content + "");
                contentTv.setTextSize(21);
            }
        };

        gridView2.setAdapter(mGridAdapter2);

    }

    private void showUserBaseInfo(UserHomeDataModel model) {

        bigHeaderImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.cover, 360, 360)));
        tagHeaderImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.avatar, 144, 144)));
        nametv.setText(model.nickname + "");
        userLevel.setText("Lv." + model.level + "");

        //性别

        if (model.sex == 1) {
            genderImg.setImageResource(R.drawable.icon_male);
        } else {
            genderImg.setImageResource(R.drawable.icon_famale);
        }

        //模特
        if (model.isModel == 1) {
            commentModelTv.setVisibility(View.VISIBLE);
        } else {
            commentModelTv.setVisibility(View.GONE);
        }

        //达人
        if (model.isDoyen == 1) {
            commentPopmanTv.setVisibility(View.VISIBLE);
        } else {
            commentPopmanTv.setVisibility(View.GONE);
        }

        //车主

        if (model.isMaster == 1) {
            brandLogo.setVisibility(View.VISIBLE);
            brandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.logo, 144, 144)));
        } else {
            brandLogo.setVisibility(View.GONE);
        }

        //动态、关注、粉丝、爱车
        gridItems.get(0).content = model.dynamic + "";

        final int followNum = model.followNum;
        gridItems.get(1).content = followNum + "";

        gridItems.get(2).content = model.fansNum + "";

        // gridItems.get(3).content = model.myCar + "";
        mGridAdapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int userId = Integer.parseInt(IOUtils.getUserId(mContext));
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, userId);
                switch (position) {

                    case 0:
                        CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);

                        break;
                    case 1:

                        if (followNum == 0) {
                            args.put(Constants.IntentParams.DATA, "选择感兴趣的用户");
                            args.put(Constants.IntentParams.INTENT_TYPE, 2);
                            CommonUtils.startNewActivity(mContext, args,
                                    LocalAttentionListActivity.class);
                        } else {
                            CommonUtils.startNewActivity(mContext, args,
                                    AttentionListActivity.class);
                        }

                        break;

                    case 2:
                        CommonUtils.startNewActivity(mContext, args, FansListActivity.class);
                    case 3:
                        break;

                }

            }
        });

        //个人余额、积分

        String str1 = String.format(getString(R.string.balance_tv_value), model.money + "");
        CommonUtils.customDisplayText(mRes, balanceTv, str1, R.color.text_color10);
        String str2 = "金币\t\t" + model.pointsKcwc + "\t\t经验\t\t" + model.exp;
        expTv.setText(str2);

        //商务余额、积分

        if (model.orgId != 0) {
            organizationIntegralLayout.setVisibility(View.VISIBLE);
            String str3 = String.format(getString(R.string.balance_tv_value), model.money + "");
            CommonUtils.customDisplayText(mRes, balanceTv, str3, R.color.text_color10);
            String str4 = "银元\t\t" + model.kcwcBusinessPoints + "\t\t经验\t\t" + model.kcwcBusinessExp;
            businessTv.setText(str4);
        } else {
            organizationIntegralLayout.setVisibility(View.GONE);
        }


        //我的路线、驾图数据

        mylineTv.setText(model.line + "");
        driveTv.setText(model.rideData.total + "");

        //里程、时长、次数

        UserHomeDataModel.RideDataBean rideBean = model.rideData;

        gridItems2.get(0).content = NumFormatUtil.getFmtTwoNum((rideBean.mileage / 1000)) + "";
        gridItems2.get(1).content = rideBean.duration + "";
        gridItems2.get(2).content = rideBean.time + "";
        mGridAdapter2.notifyDataSetChanged();

        //最远距离、最长时间

        mBestDistanceTv.setText(rideBean.max / 1000.0 + "");
        mBestTimeTv.setText(rideBean.maxTime + "");
    }

    /**
     * 快捷菜单
     *
     * @param model
     */
    private void showShotMenu(UserHomeDataModel model) {

        ArrayList<UserHomeDataModel.ButtonBean> datas = new ArrayList<UserHomeDataModel.ButtonBean>();
        for (UserHomeDataModel.ButtonBean buttonBean : model.button) {
            String type = buttonBean.type;

            if (buttonBean.isBusiness != 1 && buttonBean.status == 1&&!("tops").equals(type)) {
                datas.add(buttonBean);
            }

        }
        WrapAdapter<UserHomeDataModel.ButtonBean> adapter = new WrapAdapter<UserHomeDataModel.ButtonBean>(
                mContext, datas, R.layout.gridview_item_showmenu) {

            @Override
            public void convert(ViewHolder helper, UserHomeDataModel.ButtonBean item) {


                TextView titletv = helper.getView(R.id.gridview_item_menutv);
                titletv.setText(item.name);
                setMenuIcon(item.type, helper);
            }
        };

        showMenuGv.setAdapter(adapter);
        showMenuGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserHomeDataModel.ButtonBean item = (UserHomeDataModel.ButtonBean) parent
                        .getAdapter().getItem(position);

                linkMenu(item.type);
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(showMenuGv, 4);
    }


    /**
     * menu菜单跳转
     *
     * @param type
     */
    private void linkMenu(String type) {

        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.ID, Integer.parseInt(IOUtils.getUserId(mContext)));
        switch (type) {
            case "ticket":
                CommonUtils.startNewActivity(mContext, args, TicketActivity.class);
                break;
            case "cert":
                CommonUtils.startNewActivity(mContext, args, CertListActivity.class);
                break;
            case "coupon":
                Intent toMyCouponAct = new Intent(mContext, VoucherMainActivity.class);
                toMyCouponAct.putExtra(Constants.IntentParams.INDEX, 3);
                startActivity(toMyCouponAct);
                break;
            case "custom_made":
                CommonUtils.showToast(mContext, "暂无开发");
                break;
            case "order":
                CommonUtils.startNewActivity(mContext, args, MySeecarMsgActivity.class);
                break;
            case "cycle":
                CommonUtils.startNewActivity(mContext, args, DrivingActivity.class);
                break;
            case "play":
                CommonUtils.startNewActivity(mContext, args, PleasePlayActivity.class);
                break;
            case "goods":
                CommonUtils.startNewActivity(mContext, args, SaleMgrListActivity.class);
//                CommonUtils.startNewActivity(mContext, args, com.tgf.kcwc.me.sale.SaleMgrListActivity.class);
                break;
            case "roadbook":
                CommonUtils.startNewActivity(mContext, args, MyTripbookActivity.class);
                break;
            case "group":

                CommonUtils.showToast(mContext, "暂未开放，敬请期待！");
                break;

            case "collect":

                CommonUtils.startNewActivity(mContext, args, MyFavoriteActivity.class);
                break;
            case "evaluate":
                CommonUtils.startNewActivity(mContext, PopmanESActivity.class);
                break;
            case "tops": //荣誉榜单
                CommonUtils.startNewActivity(mContext, OurGloryHomeActivity.class);
                break;
            case "topic":
                break;
            case "route":
                CommonUtils.startNewActivity(mContext, MyLineMainActivity.class);
                break;
            case "reward":
                CommonUtils.startNewActivity(mContext, TransmitWinningHomeActivity.class);
                break;


        }
    }

    /**
     * @param type   ticket cert coupon custom_made order cycle play  goods roadbook group collect evaluate
     * @param helper
     */
    private void setMenuIcon(String type, WrapAdapter.ViewHolder helper) {
        ImageView iv = helper.getView(R.id.gridview_item_menuiv);
        switch (type) {
            case "ticket":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_ticket));
                break;
            case "cert":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_cert));
                break;
            case "coupon":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_djq));
                break;
            case "custom_made":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_dz));
                break;
            case "order":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_wyk));
                break;
            case "cycle":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_driver_car));
                break;
            case "play":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_qnw));
                break;
            case "goods":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_sale));
                break;
            case "roadbook":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_driver_rec));
                break;
            case "group":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_group));
                break;

            case "collect":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_fav));
                break;
            case "evaluate":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_my_cmt));
                break;

            case "tops":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_rongyubangdan));
                break;
            case "topic":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_huatiguanli));

            case "route":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_wodeluxian));
                break;
            case "reward":
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_zhuanfayoujiang));
                break;
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        Map<String, Serializable> args = null;
        switch (id) {

            case R.id.balanceLayout:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, IOUtils.getUserId(mContext));
                CommonUtils.startNewActivity(mContext, args, MyBalanceActivity.class);
                break;
            case R.id.expLayout:
                break;
            case R.id.myLineLayout:
                CommonUtils.startNewActivity(mContext, MyLineMainActivity.class);
                break;
            case R.id.driveDataLayout:
                break;
            case R.id.msgTv:
            case R.id.unreadMsgTv:
                CommonUtils.startNewActivity(mContext, MessageActivity.class);
                //CommonUtils.startNewActivity(mContext, OurGloryHomeActivity.class);//活动榜单
                break;
            case R.id.expValueLayout:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, 1);
                CommonUtils.startNewActivity(mContext, args, IntegralActivity.class);//个人积分
                break;
            case R.id.organization_integral:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, 2);
                CommonUtils.startNewActivity(mContext, args, IntegralActivity.class);//商务积分
                //CommonUtils.startNewActivity(mContext,args, OrgIntegralActivity.class);//个人积分
                break;
            case R.id.settingTv:
                CommonUtils.startNewActivity(mContext, SetHomeActivity.class);
                break;

            case R.id.contactTv:

                if (mModel == null) {
                    CommonUtils.showToast(mContext, "数据正在加载中...");
                    return;
                }
                invitationFriendWindow.show(getActivity());
                final String title = mModel.nickname + "邀请您加入看车玩车";
                final String desc = "看车玩车——您身边的超级汽车生活社区！看资讯、拿优惠、分享精彩、结交朋友!";
                final String url = Constants.ImageUrls.IMG_DEF_URL;
                invitationFriendWindow
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                    long id) {

                                switch (position) {

                                    case 0:
                                        CommonUtils.startNewActivity(mContext,
                                                ContactsListActivity.class);
                                        break;

                                    case 1:

                                        OpenShareUtil.sendWX(mContext, mAPP.getMsgApi(), title, desc);

                                        break;
                                    case 2:

                                        OpenShareUtil.shareToQQ(mAPP.getTencent(), getActivity(),
                                                mBaseUIListener, title, url, desc);
                                        break;

                                    case 3:

                                        mWbHandler = OpenShareUtil.shareSina(getActivity(), title,
                                                desc);
                                        break;
                                }

                            }


                        });
                break;

            case R.id.topListLayout:
                break;
            case R.id.shareLayout:
                break;

            case R.id.qrcode:

                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.DATA, mModel);
                intent.setClass(mContext, MyQCodeActivity.class);
                startActivity(intent);
                break;
            case R.id.headerImgLayout:
            case R.id.userInfoLayout:
            case R.id.bigHeaderImg:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, IOUtils.getUserId(mContext));
                CommonUtils.startNewActivity(mContext, args, UserInfoActivity.class);
                break;

            case R.id.orgBalanceTv:


                CommonUtils.startNewActivity(mContext, DealerMgrActivity.class);
                break;

            case R.id.applyRegBtn:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, 212);
                args.put(Constants.IntentParams.DATA, "中国重庆国际汽车工业展！");
                args.put(Constants.IntentParams.DATA2, "");
                CommonUtils.startNewActivity(mContext, args, PreRegistrationActivity.class);
                break;
        }

    }

    private WbShareHandler mWbHandler;
    private List<UserHomeDataModel.ButtonBean> bussinessMenu = new ArrayList<>();

    @Override
    public void showDatas(UserHomeDataModel model) {
        mModel = model;
        showUserBaseInfo(model);
        showShotMenu(model);
        bussinessMenu.clear();
        for (UserHomeDataModel.ButtonBean item : model.button) {
            if (item.isBusiness == 1 && item.status == 1) {
//                if("redpack".equals(item.type)){
//                    continue;
//                }else if ("business".equals(item.type)){
//                    continue;
//                }else if ("ticket_handout".equals(item.type)){
//                    continue;
//                }else if ("coupon_handout".equals(item.type)){
//                    continue;
//                }else {
//
//                }
                bussinessMenu.add(item);
            }
        }
        showBussinessMenu(bussinessMenu);
    }


    public void showBussinessMenu(List<UserHomeDataModel.ButtonBean> bussinessMenu) {

        if (bussinessMenu == null || bussinessMenu.size() == 0) {
            findView(R.id.businessTitle).setVisibility(View.GONE);
        }

        WrapAdapter<UserHomeDataModel.ButtonBean> showBusinessMenuAdpater = new WrapAdapter<UserHomeDataModel.ButtonBean>(
                mContext, bussinessMenu, R.layout.gridview_item_showmenu) {

            @Override
            public void convert(ViewHolder helper, UserHomeDataModel.ButtonBean item) {

                View v = helper.getConvertView();


                TextView titletv = helper.getView(R.id.gridview_item_menutv);
                titletv.setText(item.name);
                setBussinessMenuIcon(item.type, helper);
            }

            private void setBussinessMenuIcon(String type, ViewHolder helper) {
                switch (type) {
                    case "event"://拍展会展车
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_exhibitcar);
                        break;
                    case "store"://拍店内展车
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_storecar);
                        break;
                    case "ticket_handout": //门票分发
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_ticketm);
                        break;
                    case "coupon_handout"://代金券分发
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_couponm);
                        break;
                    case "scan_check"://扫码核销
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_saoyisao);
                        break;
                    case "patrolOffer"://我要抢单
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_raporder);
                        break;
                    case "business"://商务关注
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_business);
                        break;
                    case "latent"://潜客跟踪
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_latent);
                        break;
                    case "forward"://转发有奖
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_latent);
                        break;
                    case "redpack"://红包管理
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_hongbao);
                        break;
                    case "card"://名片管理
                        helper.setImageResource(R.id.gridview_item_menuiv, R.drawable.icon_mingpian);
                        break;
                    default:
                        break;
                }
            }
        };
        showBusinessMenuGv.setAdapter(showBusinessMenuAdpater);
        showBusinessMenuGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserHomeDataModel.ButtonBean item = (UserHomeDataModel.ButtonBean) parent
                        .getAdapter().getItem(position);

                businessMenu(item.type);
            }

            private void businessMenu(String type) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, Integer.parseInt(IOUtils.getUserId(mContext)));
                switch (type) {
//                    case "我的评测":
//                        CommonUtils.showToast(getContext(), "暂未开放");
//                        break;
//                    case "话题管理":
//                        CommonUtils.showToast(getContext(), "暂未开放");
                    case "event"://拍展会展车
                        CommonUtils.startNewActivity(mContext, StoreExhibitionActivity.class);//展会展车
                        break;
                    case "store"://拍店内展车
                        CommonUtils.startNewActivity(mContext, StoreBelowActivity.class);//店内展车
                        break;
                    case "ticket_handout": //门票分发
                        CommonUtils.startNewActivity(mContext, TickerManagerActivity.class);//店内展车
//                        CommonUtils.startNewActivity(mContext, TickerManagerListActivity.class);//店内展车
                        break;
                    case "coupon_handout"://代金券分发
                        CommonUtils.startNewActivity(getContext(), CouponManagerActivity.class);
                        break;
                    case "scan_check"://扫码核销
                        CommonUtils.startNewActivity(getContext(), ScanOffCouponActivity.class);
                        break;
                    case "patrolOffer"://我要抢单
                        CommonUtils.startNewActivity(getContext(), PreSeeOrderActivity.class);
                        break;
                    case "business"://商务关注
                        CommonUtils.startNewActivity(getContext(), BusinessAttentionActivity.class);
                        break;
                    case "latent"://潜客跟踪
                        CommonUtils.startNewActivity(getContext(), PotentialCustomerTrackActivity.class);
                        break;
                    case "forward"://转发有奖
                        CommonUtils.startNewActivity(getContext(), TransmitWinningHomeActivity.class);
                        break;
                    case "redpack"://红包管理
                        CommonUtils.startNewActivity(getContext(), RedpackManageListActivity.class);
                        break;
                    case "card"://转发有奖
                       CommonUtils.showToast(getContext(),"功能待开发");
                        break;
                    default:
                        break;
                }
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(showBusinessMenuGv, 4);
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
    public void onShare(int requestCode, int resultCode, Intent data, int type) {

        if (type == 1) {
            //加上这一句才能回调
            Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUIListener);
        }

        if (type == 2 && mWbHandler != null) {
            mWbHandler.doResultIntent(data, new SinaWBCallback(mContext));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (invitationFriendWindow != null) {
            invitationFriendWindow.dismiss();
        }
    }
}
