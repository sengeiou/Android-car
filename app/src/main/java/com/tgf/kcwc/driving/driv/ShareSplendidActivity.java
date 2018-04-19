package com.tgf.kcwc.driving.driv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ShareSplendBean;
import com.tgf.kcwc.mvp.presenter.ShareSplendPresenter;
import com.tgf.kcwc.mvp.view.ShareView;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GridSpacingItemDecoration;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/4.
 */

public class ShareSplendidActivity extends BaseActivity implements ShareView {

    private ShareSplendPresenter mShareSplendPresenter;
    private TextView mTitle;
    private TextView mTime;
    private TextView mNumber;
    private RecyclerView mRecyclerView;
    private CommonAdapter<ShareSplendBean.DataList> mCommonAdapter;
    private List<ShareSplendBean.DataList> dataLists = new ArrayList<>();
    private int ID;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.share);
        function.setTextResource("编辑", R.color.white, 18);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharesplendid);
        ID = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        mShareSplendPresenter = new ShareSplendPresenter();
        mShareSplendPresenter.attachView(this);
        mRecyclerView = findViewById(R.id.rvCity);
        mTitle = findViewById(R.id.title);
        mTime = findViewById(R.id.time);
        mNumber = findViewById(R.id.number);
        GridLayoutManager mgr = new GridLayoutManager(mContext, 2);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.sidebar_text_size), true));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mgr);
        mShareSplendPresenter.getShareList(IOUtils.getToken(mContext), ID + "");
        mCommonAdapter = new CommonAdapter<ShareSplendBean.DataList>(mContext, R.layout.activity_share_item, dataLists) {
            @Override
            public void convert(ViewHolder holder, final ShareSplendBean.DataList dataList) {
                SimpleDraweeView mSimpleDraweeView = holder.getView(R.id.imageicon);
                SimpleDraweeView mGenderImg = holder.getView(R.id.genderImg);
                ImageView mEssence = holder.getView(R.id.essence);
                TextView mLike = holder.getView(R.id.like);
                TextView mInformation = holder.getView(R.id.information);
                TextView mName = holder.getView(R.id.name);
                TextView mDynamic = holder.getView(R.id.driving_list_view_dynamic);
                LinearLayout itemselect = holder.getView(R.id.itemselect);
                ImageView mModel = holder.getView(R.id.comment_model_tv);
                ImageView mConveneDa = holder.getView(R.id.drivdetails_convene_da);

                SimpleDraweeView simpleDraweeView = holder.getView(R.id.motodetail_avatar_iv);
                SimpleDraweeView mConveneBrandLogo = holder.getView(R.id.drivdetails_convene_brandLogo);
                mSimpleDraweeView
                        .setImageURI(URLUtil.builderImgUrl(dataList.cover, 144, 144));
                if (dataList.isDigest == 1) {
                    mEssence.setVisibility(View.VISIBLE);
                } else {
                    mEssence.setVisibility(View.GONE);
                }
                mLike.setText(dataList.diggCount + "");
                mInformation.setText(dataList.replyCount + "");
                mDynamic.setText(dataList.title);
                ShareSplendBean.CreateBy createBy = dataList.createBy;
                simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(createBy.logo, 144, 144)));
                if (createBy.sex == 1) {
                    mGenderImg.setImageResource(R.drawable.icon_men);
                } else {
                    mGenderImg.setImageResource(R.drawable.icon_women);
                }
                if (createBy.isDoyen == 1) {
                    mConveneDa.setVisibility(View.VISIBLE);
                } else {
                    mConveneDa.setVisibility(View.GONE);
                }

                if (createBy.isModel == 1) {
                    mModel.setVisibility(View.VISIBLE);
                } else {
                    mModel.setVisibility(View.GONE);
                }

                if (createBy.isMaster == 1) {
                    mConveneBrandLogo.setVisibility(View.VISIBLE);
                    mConveneBrandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(createBy.avatar, 144, 144)));
                } else {
                    mConveneBrandLogo.setVisibility(View.GONE);
                }
                mName.setText(createBy.username);
                itemselect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, dataList.id + "");
                        CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                    }
                });

            }
        };
        mRecyclerView.setAdapter(mCommonAdapter);
    }

    @Override
    public void dataListSucceed(ShareSplendBean shareSplendBean) {
        mTitle.setText(shareSplendBean.data.thread.title);
        mTime.setText(shareSplendBean.data.thread.beginTime + " - " + shareSplendBean.data.thread.endTime);
        String src = "报名人数" + shareSplendBean.data.thread.num;
        setTextColors(mNumber, src, 3, src.length(), R.color.text_color14);
        dataLists.addAll(shareSplendBean.data.list);
        mCommonAdapter.notifyDataSetChanged();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShareSplendPresenter != null) {
            mShareSplendPresenter.detachView();
        }
    }
}
