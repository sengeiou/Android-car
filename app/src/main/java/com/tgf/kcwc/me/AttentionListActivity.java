package com.tgf.kcwc.me;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AttentionDataModel;
import com.tgf.kcwc.mvp.presenter.AttentionDataPresenter;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/5/18
 * E-mail:fishloveqin@gmail.com
 * 关注列表
 */

public class AttentionListActivity extends BaseActivity
        implements UserDataView<AttentionDataModel> {

    protected ListView list1;
    protected LinearLayout firstPartLayout;
    protected ListView list2;
    protected LinearLayout secondPartLayout;
    private UserDataPresenter mPresenter;

    private AttentionDataPresenter mAttentionPresenter;
    private AttentionDataPresenter mCancelPresenter;
    private int userId;

    private WrapAdapter<AttentionDataModel.UserInfo> mAdapter1, mAdapter2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        super.setContentView(R.layout.activity_attention_list);

    }

    @Override
    protected void setUpViews() {

        initView();
        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);

        mAttentionPresenter = new AttentionDataPresenter();
        mAttentionPresenter.attachView(mAttentionView);
        mCancelPresenter = new AttentionDataPresenter();
        mCancelPresenter.attachView(mCancelAttentionView);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);

        //function.setImageResource(R.drawable.global_nav_n);

        text.setText("关注列表");
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getAttentionList(userId + "", IOUtils.getToken(mContext));
    }

    @Override
    public void showDatas(AttentionDataModel model) {

        List<AttentionDataModel.UserInfo> recmtDatas = model.recommendList;

        List<AttentionDataModel.UserInfo> attentionDatas = model.list;

        int recmtSize = recmtDatas.size();
        int attSize = attentionDatas.size();

        //判断是否为当前登录用户
        int uId = model.id;
        if (uId == userId) {

            if (attSize == 0) {
                firstPartLayout.setVisibility(View.VISIBLE);
                secondPartLayout.setVisibility(View.GONE);

            } else {
                firstPartLayout.setVisibility(View.VISIBLE);
                secondPartLayout.setVisibility(View.VISIBLE);
            }

        } else {

            firstPartLayout.setVisibility(View.GONE);
            secondPartLayout.setVisibility(View.VISIBLE);
        }

        list1.setAdapter(buildAdapter(recmtDatas));
        list2.setAdapter(buildAdapter(attentionDatas));
    }

    private WrapAdapter<AttentionDataModel.UserInfo> buildAdapter(List<AttentionDataModel.UserInfo> datas) {

        WrapAdapter<AttentionDataModel.UserInfo> adapter = new WrapAdapter<AttentionDataModel.UserInfo>(
                mContext, R.layout.attention_list_item, datas) {
            protected RelativeLayout userInfoLayout;
            protected ImageView attentionStatusImg;
            protected TextView distanceTv;
            protected TextView signTextTv;
            protected SimpleDraweeView brandLogo;
            protected TextView nametv;
            protected RelativeLayout headerImgLayout;
            protected SimpleDraweeView genderImg;
            protected SimpleDraweeView tagHeaderImg;
            protected View rootView;

            @Override
            public void convert(ViewHolder helper, final AttentionDataModel.UserInfo item) {

                tagHeaderImg = helper.getView(R.id.tagHeaderImg);
                genderImg = helper.getView(R.id.genderImg);
                headerImgLayout = helper.getView(R.id.headerImgLayout);
                nametv = helper.getView(R.id.nametv);
                brandLogo = helper.getView(R.id.brandLogo);
                String logo = item.logo;

                if (TextUtils.isEmpty(logo)) {
                    brandLogo.setVisibility(View.GONE);
                } else {
                    brandLogo.setVisibility(View.VISIBLE);
                    brandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(logo, 144, 144)));
                }
                signTextTv = helper.getView(R.id.signTextTv);
                distanceTv = helper.getView(R.id.distanceTv);
                attentionStatusImg = helper.getView(R.id.attentionStatusImg);
                userInfoLayout = helper.getView(R.id.userInfoLayout);

                if (item.sex == 2) {
                    genderImg.setImageResource(R.drawable.icon_famale);
                } else {
                    genderImg.setImageResource(R.drawable.icon_male);
                }
                tagHeaderImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.avatar, 144, 144)));
                if (TextUtil.isEmpty(item.remark)) {
                    nametv.setText(item.nickname);
                } else {
                    nametv.setText(item.remark);
                }
                signTextTv.setText(item.signText);
                distanceTv.setText(item.registerArea);

                final String rel = item.relation;
                if (TextUtils.isEmpty(rel)) {

                    attentionStatusImg.setImageResource(R.drawable.attention_status_1);
                } else if ("already_concern".equals(rel)) {
                    attentionStatusImg.setImageResource(R.drawable.btn_attention);
                } else if ("mutual_concern".equals(rel)) {
                    attentionStatusImg.setImageResource(R.drawable.attention_status_2);
                }

                attentionStatusImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(rel)) {

                            if ("already_concern".equals(rel) || "mutual_concern".equals(rel)) {
                                mCancelPresenter.cancelAttention(item.followId + "",
                                        IOUtils.getToken(mContext));

                            } else {
                                mAttentionPresenter.execAttention(item.followId + "",
                                        IOUtils.getToken(mContext));
                            }

                        } else {
                            mAttentionPresenter.execAttention(item.id + "",
                                    IOUtils.getToken(mContext));
                        }

                    }
                });
            }
        };
        return adapter;

    }

    //关注

    AttentionView<String> mAttentionView = new AttentionView<String>() {
        @Override
        public void showAddAttention(String string) {

            CommonUtils.showToast(mContext, "您已添加关注");
            mPresenter.getAttentionList(userId + "",
                    IOUtils.getToken(mContext));
        }

        @Override
        public void showCancelAttention(String string) {

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

    //取消关注

    AttentionView<String> mCancelAttentionView = new AttentionView<String>() {
        @Override
        public void showAddAttention(String string) {

        }

        @Override
        public void showCancelAttention(String string) {
            CommonUtils.showToast(mContext, "您已取消关注");
            mPresenter.getAttentionList(userId + "",
                    IOUtils.getToken(mContext));
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
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        showLoadingIndicator(false);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private void initView() {
        list1 = (ListView) findViewById(R.id.list1);
        firstPartLayout = (LinearLayout) findViewById(R.id.firstPartLayout);
        firstPartLayout.setOnClickListener(this);
        list2 = (ListView) findViewById(R.id.list2);
        list1.setOnItemClickListener(mItemClickListener1);
        list2.setOnItemClickListener(mItemClickListener2);
        secondPartLayout = (LinearLayout) findViewById(R.id.secondPartLayout);
    }

    private AdapterView.OnItemClickListener mItemClickListener1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

            AttentionDataModel.UserInfo userInfo = (AttentionDataModel.UserInfo) parent
                    .getAdapter()
                    .getItem(position);

            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(
                    Constants.IntentParams.ID,
                    userInfo.id);
            CommonUtils
                    .startNewActivity(
                            mContext, args,
                            UserPageActivity.class);
            //finish();
        }
    };

    private AdapterView.OnItemClickListener mItemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

            AttentionDataModel.UserInfo userInfo = (AttentionDataModel.UserInfo) parent
                    .getAdapter()
                    .getItem(position);

            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(
                    Constants.IntentParams.ID,
                    userInfo.followId);
            CommonUtils
                    .startNewActivity(
                            mContext, args,
                            UserPageActivity.class);
            //finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (mAttentionPresenter != null) {
            mAttentionPresenter.detachView();
        }

        if (mCancelPresenter != null) {
            mCancelPresenter.detachView();
        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.firstPartLayout:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, userId);
                args.put(Constants.IntentParams.DATA, "你可能感兴趣的人");
                args.put(Constants.IntentParams.INTENT_TYPE, 1);
                CommonUtils.startNewActivity(mContext, args, LocalAttentionListActivity.class);

                break;
        }

    }
}
