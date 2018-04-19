package com.tgf.kcwc.driving.driv;

import java.util.ArrayList;
import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.ApplyListBean;
import com.tgf.kcwc.mvp.presenter.ApplyListPresenter;
import com.tgf.kcwc.mvp.view.ApplyView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/5/2.
 */

public class ApplyListActivity extends BaseActivity implements ApplyView {
    private ApplyListPresenter mApplyListPresenter;
    private String ID;                           //传过来的id
    private WrapAdapter<ApplyListBean.DataList> mAdapter;
    private List<ApplyListBean.DataList> mDataList = new ArrayList<>();
    private ListView mListView;
    private ApplyListBean mApplyListBean;
    private boolean isRefresh = false;
    private boolean isPull = true;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.apply);
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applelist);
        ID = (String) getIntent().getSerializableExtra("id");
        initRefreshLayout(bgDelegate);
        mPageIndex = 1;
        mApplyListPresenter = new ApplyListPresenter();
        mApplyListPresenter.attachView(this);
        mApplyListPresenter.gainAppLsis(IOUtils.getToken(mContext), ID, mPageIndex);
        setLoadingIndicator(true);

        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new WrapAdapter<ApplyListBean.DataList>(mContext,
                R.layout.activity_applylist_item, mDataList) {
            @Override
            public void convert(ViewHolder helper, final ApplyListBean.DataList item) {
                final int position = helper.getPosition();
                LinearLayout linearLayout = helper.getView(R.id.unfoldlay);
                SimpleDraweeView mSimpleDraweeView = helper.getView(R.id.motodetail_avatar_iv);
                SimpleDraweeView mGenderImgView = helper.getView(R.id.genderImg);
                SimpleDraweeView mBrandLogo = helper.getView(R.id.brandLogo);
                TextView nametv = helper.getView(R.id.nametv);
                TextView mContentTv = helper.getView(R.id.contentTv);
                TextView mTime = helper.getView(R.id.time);
                TextView mPhone = helper.getView(R.id.phone);
                TextView mReason = helper.getView(R.id.reason);
                TextView mConsent = helper.getView(R.id.consent);
                TextView mNotConsent = helper.getView(R.id.notconsent);
                ImageView mCommentModel = helper.getView(R.id.comment_model_tv);
                ImageView mCommentPopman = helper.getView(R.id.comment_popman_tv);
                ImageView mState = helper.getView(R.id.state);
                LinearLayout mSelectlay = helper.getView(R.id.selectlay);
                LinearLayout mClick = helper.getView(R.id.click);
                final ImageView mCondition = helper.getView(R.id.condition);

                mSimpleDraweeView.setImageURI(URLUtil.builderImgUrl(item.avatar, 144, 144));
                int sex = item.sex;
                if (sex == 1) {
                    mGenderImgView.setImageResource(R.drawable.icon_men);
                } else {
                    mGenderImgView.setImageResource(R.drawable.icon_women);
                }
                int checkStatus = item.checkStatus;
                switch (checkStatus) {
                    case 0:
                        mState.setImageResource(R.drawable.icon_weishenhe);
                        mSelectlay.setVisibility(View.VISIBLE);
                        mConsent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (IOUtils.isLogin(mContext)) {
                                    mApplyListPresenter.getCheck(IOUtils.getToken(mContext),
                                            item.id + "", 1, position);
                                    setLoadingIndicator(true);
                                }

                            }
                        });
                        mNotConsent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (IOUtils.isLogin(mContext)) {
                                    mApplyListPresenter.getCheck(IOUtils.getToken(mContext),
                                            item.id + "", 2, position);
                                    setLoadingIndicator(true);
                                }
                            }
                        });

                        break;
                    case 1:
                        mState.setImageResource(R.drawable.icon_agree);
                        mSelectlay.setVisibility(View.GONE);
                        break;
                    case 2:
                        mState.setImageResource(R.drawable.icon_disagree);
                        mSelectlay.setVisibility(View.GONE);
                        break;
                    case 3:
                        mState.setImageResource(R.drawable.icon_disagree);
                        mSelectlay.setVisibility(View.GONE);
                        break;
                }
                nametv.setText(item.nickname);
                setTextColors(mContentTv, "报名人数：" + item.num + "人", 0, 5, R.color.text_color2);
                setTextColors(mPhone, "手机号码：" + item.tel, 0, 5, R.color.text_color2);
                setTextColors(mReason, "报名理由：" + item.reason, 0, 5, R.color.text_color2);

                mTime.setText(item.createTime);
                final String relation = item.relation;
                if (relation.equals("not_concern")) {
                    mCondition.setImageResource(R.drawable.btn_guanzhu_orang);
                } else if (relation.equals("already_concern")) {
                    mCondition.setImageResource(R.drawable.btn_weiguanzhu);
                } else if (relation.equals("mutual_concern")) {
                    mCondition.setImageResource(R.drawable.btn_huxiang);
                } else {
                    mCondition.setImageResource(R.drawable.btn_guanzhu_orang);
                    mCondition.setVisibility(View.GONE);
                }
                mCondition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (IOUtils.isLogin(mContext)) {
                            if (relation.equals("not_concern")) {
                                mApplyListPresenter.getFollow(IOUtils.getToken(mContext),
                                        item.applicantId + "", position);
                            } else if (relation.equals("already_concern")) {
                                mApplyListPresenter.getCancel(IOUtils.getToken(mContext),
                                        item.applicantId + "", position);
                            } else if (relation.equals("mutual_concern")) {
                                mApplyListPresenter.getCancel(IOUtils.getToken(mContext),
                                        item.applicantId + "", position);
                            } else {
                            }
                        }
                    }
                });
                String id = IOUtils.getUserId(mContext);
                if (id.equals(mApplyListBean.data.createBy)) {
                    mClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mDataList.get(position).isUnfold) {
                                for (int i = 0; i < mDataList.size(); i++) {
                                    mDataList.get(i).isUnfold = false;
                                }
                                mDataList.get(position).isUnfold = true;
                                mAdapter.notifyDataSetChanged();
                            } else {
                                for (int i = 0; i < mDataList.size(); i++) {
                                    mDataList.get(i).isUnfold = false;
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
                if (item.isUnfold) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.GONE);
                }

            }
        };
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingDialog();
        } else {
            stopRefreshAll();
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void dataListSucceed(ApplyListBean applyListBean) {
        // CommonUtils.showToast(mContext, "成功");
        mApplyListBean = applyListBean;
        if (isRefresh) {
            mDataList.clear();
            mDataList.addAll(applyListBean.data.list);
            mAdapter.notifyDataSetChanged();
        } else {
            mDataList.addAll(applyListBean.data.list);
            mAdapter.notifyDataSetChanged();
        }

        if (applyListBean.data.list == null || applyListBean.data.list.size() == 0) {
            isPull = false;
        } else {
            isPull = true;
        }

        setLoadingIndicator(false);
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
        setLoadingIndicator(false);
    }

    @Override
    public void dataFollowSucceed(int num, String follow_id, String msg) {
        CommonUtils.showToast(mContext, msg);
        mApplyListPresenter.getFollowRelation(IOUtils.getToken(mContext), follow_id, num);
        setLoadingIndicator(true);
    }

    @Override
    public void dataFollowRelationSucceed(int num, String relation) {
        ApplyListBean.DataList dataList = mDataList.get(num);
        dataList.relation = relation;
        mAdapter.notifyDataSetChanged();
        setLoadingIndicator(false);
    }

    @Override
    public void dataCheckSucceed(int num, int type) {
        ApplyListBean.DataList dataList = mDataList.get(num);

        if (type == 1) {
            CommonUtils.showToast(mContext, "同意成功");
            dataList.checkStatus = 1;
        } else {
            CommonUtils.showToast(mContext, "拒绝成功");
            dataList.checkStatus = 2;
        }
        setLoadingIndicator(false);
        mAdapter.notifyDataSetChanged();
    }

    private BGARefreshLayout.BGARefreshLayoutDelegate bgDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {
        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            isRefresh = true;
            isPull = true;
            mPageIndex = 0;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            if (isPull) {
                loadMore();
            }
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {
        mPageIndex++;
        mApplyListPresenter.gainAppLsis(IOUtils.getToken(mContext), ID, mPageIndex);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mApplyListPresenter != null) {
            mApplyListPresenter.detachView();
        }
    }
}
