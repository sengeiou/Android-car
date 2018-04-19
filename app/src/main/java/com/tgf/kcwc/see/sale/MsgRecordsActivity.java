package com.tgf.kcwc.see.sale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.ChatRecyclerAdapter;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.ChatMessageBean;
import com.tgf.kcwc.mvp.presenter.OwnerSalePresenter;
import com.tgf.kcwc.mvp.view.OwnerSaleDataView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NestLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import static com.tgf.kcwc.R.string.comment;

/**
 * Author：Jenny
 * Date:2017/2/28 17:11
 * E-mail：fishloveqin@gmail.com
 */

public class MsgRecordsActivity extends BaseActivity
                                implements OwnerSaleDataView<List<ChatMessageBean>> {
    protected RecyclerView                mList1;
    protected ListView                    mList2;
    protected DrawerLayout                mDrawerLayout;
    protected ImageView                   mContactBtn;
    protected ImageButton                 mBack;
    protected TextView                    mTitleTv;
    private OwnerSalePresenter            mContactsPresenter;
    private OwnerSalePresenter            mMsgDataPresenter;
    private int                           mId;
    private ChatRecyclerAdapter           mAdapter      = null;
    private List<ChatMessageBean>         mMsgDatas     = new ArrayList<ChatMessageBean>();
    private TextView                      mSendMsgBtn;
    private String                        mTitle;

    private RelativeLayout                mContentLayout;

    private List<Account.UserInfo>        mUsers        = new ArrayList<Account.UserInfo>();

    private WrapAdapter<Account.UserInfo> mUsersAdapter = null;

    private RelativeLayout                mSendMsgLayout;
    private View                          mLayerView;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mTitle = intent.getStringExtra(Constants.IntentParams.TITLE);
        isTitleBar = false;
        super.setContentView(R.layout.activity_contacts_msg_records);

    }

    /**
     * 动态改变底部蒙层块Vie的宽度
     * @param width
     */
    private void zoomInViewSize(int width) {
        ViewGroup.LayoutParams lp = mLayerView.getLayoutParams();
        lp.width = width;
        mLayerView.setLayoutParams(lp);
    }

    private void initView() {
        mList1 = (RecyclerView) findViewById(R.id.list1);
        NestLinearLayoutManager nestLinearLayoutManager = new NestLinearLayoutManager(mContext);
        nestLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList1.setLayoutManager(nestLinearLayoutManager);
        mContentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        mContentLayout.setPadding(BitmapUtils.dp2px(mContext, 60), 0, 0, 0);
        mSendMsgLayout = (RelativeLayout) findViewById(R.id.sendMsgLayout);
        mLayerView = findViewById(R.id.layerView);
        mSendMsgLayout.setOnClickListener(this);
        mList2 = (ListView) findViewById(R.id.list2);
        mSendMsgBtn = (TextView) findViewById(R.id.sendMsg);
        mSendMsgBtn.setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(Gravity.LEFT);
        mDrawerLayout.setScrimColor(0x00000000);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                int width = (int) (BitmapUtils.dp2px(mContext, 60) * slideOffset);
                zoomInViewSize(width);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mContentLayout.setPadding(BitmapUtils.dp2px(mContext, 60), 0, 0, 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                mContentLayout.setPadding(0, 0, 0, 0);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mContactsPresenter = new OwnerSalePresenter();
        mContactsPresenter.attachView(mContactsView);
        mContactsPresenter.getContactsList(mId + "", IOUtils.getToken(mContext));
        mMsgDataPresenter = new OwnerSalePresenter();
        mMsgDataPresenter.attachView(this);
        mContactBtn = (ImageView) findViewById(R.id.contactsBtn);
        mContactBtn.setOnClickListener(this);
        mBack = (ImageButton) findViewById(R.id.title_bar_back);
        backEvent(mBack);
        mTitleTv = (TextView) findViewById(R.id.title_bar_text);
        mTitleTv.setText(mTitle + "");
        mList2.setOnItemClickListener(mContactsItemListener);
    }

    private void singleChecked(Account.UserInfo userInfo) {
        for (Account.UserInfo user : mUsers) {
            if (user.userId != userInfo.userId) {
                user.isSelected = false;
            }
        }

    }

    /**
     * 显示用户信息
     * @param userInfo
     */
    private void showUserInfo(Account.UserInfo userInfo) {

        ImageView modelImg = (ImageView) findViewById(R.id.comment_model_tv);
        ImageView popmanImg = (ImageView) findViewById(R.id.comment_popman_tv);

        SimpleDraweeView brandLogo = (SimpleDraweeView) findViewById(R.id.brandLogo);
        SimpleDraweeView headerImg = (SimpleDraweeView) findViewById(R.id.tagHeaderImg);
        headerImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(userInfo.avatar, 144, 144)));
        brandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(userInfo.masterLogo, 144, 144)));

        SimpleDraweeView genderImg = (SimpleDraweeView) findViewById(R.id.genderImg);

        if (userInfo.sex == 0) {
            genderImg.setImageResource(R.drawable.icon_women);
        } else {
            genderImg.setImageResource(R.drawable.icon_men);
        }

        //达人
        if (userInfo.isExpert == 1) {
            popmanImg.setVisibility(View.VISIBLE);
        } else {
            popmanImg.setVisibility(View.GONE);
        }
        //模特
        if (userInfo.is_model == 1) {
            modelImg.setVisibility(View.VISIBLE);
        } else {
            modelImg.setVisibility(View.GONE);
        }
    }

    private AdapterView.OnItemClickListener   mContactsItemListener = new AdapterView.OnItemClickListener() {
                                                                        @Override
                                                                        public void onItemClick(AdapterView<?> parent,
                                                                                                View view,
                                                                                                int position,
                                                                                                long id) {

                                                                            Account.UserInfo userInfo = (Account.UserInfo) parent
                                                                                .getAdapter()
                                                                                .getItem(position);
                                                                            showUserInfo(userInfo);
                                                                            userInfo.isSelected = true;

                                                                            singleChecked(userInfo);
                                                                            mUsersAdapter
                                                                                .notifyDataSetChanged();
                                                                            mMsgDataPresenter
                                                                                .getMsgDatas(
                                                                                    mId + "",
                                                                                    userInfo.userId + "",
                                                                                    IOUtils
                                                                                        .getToken(
                                                                                            mContext));
                                                                        }
                                                                    };
    OwnerSaleDataView<List<Account.UserInfo>> mContactsView         = new OwnerSaleDataView<List<Account.UserInfo>>() {
                                                                        @Override
                                                                        public void showData(List<Account.UserInfo> userInfos) {

                                                                            mUsers.clear();
                                                                            mUsers
                                                                                .addAll(userInfos);

                                                                            mUsersAdapter = new WrapAdapter<Account.UserInfo>(
                                                                                mContext,
                                                                                R.layout.contacts_item,
                                                                                mUsers) {
                                                                                                                                                @Override
                                                                                                                                                public void convert(ViewHolder helper,
                                                                                                                                                                    Account.UserInfo item) {

                                                                                                                                                    View v = helper
                                                                                                                                                        .getConvertView();
                                                                                                                                                    if (item.isSelected) {
                                                                                                                                                        v.setBackgroundColor(
                                                                                                                                                            mRes.getColor(
                                                                                                                                                                R.color.white));
                                                                                                                                                    } else {
                                                                                                                                                        v.setBackgroundColor(
                                                                                                                                                            mRes.getColor(
                                                                                                                                                                R.color.app_layout_bg_color));
                                                                                                                                                    }
                                                                                                                                                    SimpleDraweeView simpleDraweeView = helper
                                                                                                                                                        .getView(
                                                                                                                                                            R.id.img);
                                                                                                                                                    simpleDraweeView
                                                                                                                                                        .setImageURI(
                                                                                                                                                            Uri.parse(
                                                                                                                                                                URLUtil
                                                                                                                                                                    .builderImgUrl(
                                                                                                                                                                        item.avatar,
                                                                                                                                                                        144,
                                                                                                                                                                        144)));
                                                                                                                                                    TextView txView = helper
                                                                                                                                                        .getView(
                                                                                                                                                            R.id.msgTotals);
                                                                                                                                                    int nums = item.msgTotal;
                                                                                                                                                    if (nums > 0) {
                                                                                                                                                        txView
                                                                                                                                                            .setVisibility(
                                                                                                                                                                View.VISIBLE);
                                                                                                                                                        txView
                                                                                                                                                            .setText(
                                                                                                                                                                nums + "");
                                                                                                                                                    } else {
                                                                                                                                                        txView
                                                                                                                                                            .setVisibility(
                                                                                                                                                                View.GONE);
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            };
                                                                            mList2.setAdapter(
                                                                                mUsersAdapter);

                                                                            if (mUsers.size() > 0) {
                                                                                mMsgDataPresenter
                                                                                    .getMsgDatas(
                                                                                        mId + "",
                                                                                        mUsers.get(
                                                                                            0).userId + "",
                                                                                        IOUtils
                                                                                            .getToken(
                                                                                                mContext));
                                                                                showUserInfo(
                                                                                    mUsers.get(0));
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

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.contactsBtn:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;

            case R.id.sendMsg:
            case R.id.sendMsgLayout:

                CommonUtils.showToast(mContext, "发私信!");
                //发私信....
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContactsPresenter != null) {
            mContactsPresenter.detachView();
        }
    }

    @Override
    public void showData(List<ChatMessageBean> messageEntities) {

        mMsgDatas.clear();
        mMsgDatas.addAll(messageEntities);
        mAdapter = new ChatRecyclerAdapter(mContext, mMsgDatas);
        mList1.setAdapter(mAdapter);
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
}
