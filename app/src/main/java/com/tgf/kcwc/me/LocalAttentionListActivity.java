package com.tgf.kcwc.me;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/5/18
 * E-mail:fishloveqin@gmail.com
 * 维护本地更多、未关注列表
 */

public class LocalAttentionListActivity extends BaseActivity
        implements UserDataView<AttentionDataModel> {

    protected ListView list;
    protected Button nextBtn;
    private UserDataPresenter mPresenter;

    private AttentionDataPresenter mAttentionPresenter;
    private AttentionDataPresenter mCancelPresenter;
    private int userId;

    private WrapAdapter<AttentionDataModel.UserInfo> mAdapter;

    private String mTitle = "";

    private List<AttentionDataModel.UserInfo> mDatas = new ArrayList<AttentionDataModel.UserInfo>();

    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        userId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mTitle = intent.getStringExtra(Constants.IntentParams.DATA);
        type = intent.getIntExtra(Constants.IntentParams.INTENT_TYPE, -1);
        super.setContentView(R.layout.activity_local_attention_list);

    }

    private static final int IS_MORE_TYPE = 1; //有关注，更多

    private static final int IS_NO_ATT_TYPE = 2; //没有关注

    @Override
    protected void setUpViews() {

        initView();
        mPresenter = new UserDataPresenter();

        if (type == IS_MORE_TYPE) {

            mPresenter.attachView(userDataView);
            mPresenter.getMoreAttentionList(userId + "", IOUtils.getToken(mContext));
        } else if (type == IS_NO_ATT_TYPE) {
            mPresenter.attachView(this);
            mPresenter.getAttentionList(userId + "", IOUtils.getToken(mContext));
        }

        mAttentionPresenter = new AttentionDataPresenter();
        mAttentionPresenter.attachView(mAttentionView);
        mCancelPresenter = new AttentionDataPresenter();
        mCancelPresenter.attachView(mCancelAttentionView);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);

        // function.setImageResource(R.drawable.global_nav_n);

        //text.setText(mTitle);
    }

    private UserDataView<List<AttentionDataModel.UserInfo>> userDataView = new UserDataView<List<AttentionDataModel.UserInfo>>() {
        @Override
        public void showDatas(List<AttentionDataModel.UserInfo> list) {

            mDatas.clear();
            mDatas.addAll(list);
            mAdapter.notifyDataSetChanged();
        }

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
    };

    @Override
    public void showDatas(AttentionDataModel model) {

        mDatas.clear();
        mDatas.addAll(model.recommendList);
        mAdapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            AttentionDataModel.UserInfo userInfo = (AttentionDataModel.UserInfo) parent.getAdapter()
                    .getItem(position);

            Map<String, Serializable> args = new HashMap<String, Serializable>();
            int userId = 0;
            if (type == 2) {
                userId = userInfo.id;
            } else if (type == 1) {
                userId = userInfo.userId;
            }
            args.put(Constants.IntentParams.ID, userId);
            CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);
            finish();
        }
    };

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

                if (TextUtil.isEmpty(logo)) {

                    brandLogo.setVisibility(View.GONE);
                } else {
                    brandLogo.setVisibility(View.VISIBLE);
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
                if (item.isSelected) {
                    attentionStatusImg.setImageResource(R.drawable.btn_attention);

                } else {
                    attentionStatusImg.setImageResource(R.drawable.attention_status_1);

                }

                attentionStatusImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (item.isSelected) {

                            mCancelPresenter.cancelAttention(item.id + "",
                                    IOUtils.getToken(mContext));

                        } else {
                            mAttentionPresenter.execAttention(item.id + "",
                                    IOUtils.getToken(mContext));
                        }
                        item.isSelected = !item.isSelected;
                        if (hasSeleected()) {
                            nextBtn.setEnabled(true);
                            nextBtn.setBackgroundResource(R.color.style_bg6);
                        } else {
                            nextBtn.setEnabled(false);

                            nextBtn.setBackgroundResource(R.color.text_nav_def_color);
                        }
                        mAdapter.notifyDataSetChanged();

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

            //                                                       CommonUtils.showToast(mContext, "您已添加关注");
            //                                                       mPresenter.getAttentionList(userId,
            //                                                           IOUtils.getToken(mContext));
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
            //                                                       CommonUtils.showToast(mContext, "您已取消关注");
            //                                                       mPresenter.getAttentionList(userId,
            //                                                           IOUtils.getToken(mContext));
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
        list = (ListView) findViewById(R.id.list);
        nextBtn = (Button) findViewById(R.id.nextStepBtn);
        nextBtn.setOnClickListener(LocalAttentionListActivity.this);
        mAdapter = buildAdapter(mDatas);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(mItemClickListener);
    }

    private boolean hasSeleected() {

        for (AttentionDataModel.UserInfo u : mDatas) {

            if (u.isSelected) {

                return true;
            }
        }
        return false;
    }

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
            case R.id.nextStepBtn:

                if (type == IS_MORE_TYPE) {
                    finish();
                } else if (type == IS_NO_ATT_TYPE) {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, userId);
                    CommonUtils.startNewActivity(mContext, args, AttentionListActivity.class);
                    finish();
                }

                break;
        }

    }
}
