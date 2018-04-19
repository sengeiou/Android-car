package com.tgf.kcwc.me;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.mvp.model.UserDynamicModel;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/5/17
 * E-mail:fishloveqin@gmail.com
 */

public class DynamicFragment extends BaseFragment implements UserDataView<UserDynamicModel> {

    protected ListView        list;
    protected RelativeLayout  emptyLayout;
    protected View            rootView;
    protected ImageView       attImage;
    protected TextView        attentionTv;
    protected ImageView       secretImage;
    protected TextView        secretMsgTv;
    protected TextView        msgTv2;
    protected ImageView       inputTxtImg;
    protected TextView        releasePostedBtn;
    private UserDataPresenter mPresenter;

    @Override
    protected void updateData() {

        int userId = getActivity().getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        mPresenter.getDynamicInfo(userId + "", IOUtils.getToken(mContext));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_dynamic;
    }

    @Override
    protected void initView() {
        list = findView(R.id.list);
        emptyLayout = findView(R.id.emptyLayout);
        msgTv2 = findView(R.id.msgTv2);
        emptyLayout = findView(R.id.emptyLayout);
        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);
        list.setOnItemClickListener(mOnItemClickListener);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            UserDynamicModel.TopicBean item = (UserDynamicModel.TopicBean) parent.getAdapter()
                .getItem(position);
            Map<String, Serializable> args = null;
            String model = item.model;
            if (TextUtils.isEmpty(model)) {
                model = "words";
            }
            if (model.equals("goods")) { //车主自售
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, item.id);
                CommonUtils.startNewActivity(mContext, args, SaleDetailActivity.class);
            } else if (model.equals("play")) { //请你玩
                args = new HashMap<String, Serializable>();
                args.put("id", item.id + "");
                CommonUtils.startNewActivity(mContext, args, PleasePlayDetailsActivity.class);
            } else if (model.equals("cycle")) { //开车去
                args = new HashMap<String, Serializable>();
                args.put("id", item.id + "");
                CommonUtils.startNewActivity(mContext, args, DrivingDetailsActivity.class);
            } else if (model.equals("words")) {//普通帖子
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, item.id + "");
                CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
            } else if (model.equals("evaluate")) { //达人评测
                args.put(Constants.IntentParams.ID, item.id + "");
                CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
            } else if (model.equals("roadbook")) {//路书
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, item.id);
                CommonUtils.startNewActivity(mContext, args, TripbookDetailActivity.class);
            } else {
                CommonUtils.showToast(mContext, "正在开发中");
            }

        }
    };

    @Override
    public void showDatas(UserDynamicModel userDynamicModel) {

        List<UserDynamicModel.TopicBean> datas = userDynamicModel.list;
        int size = datas.size();

        if (size == 0) {
            list.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }

        WrapAdapter<UserDynamicModel.TopicBean> adapter = new WrapAdapter<UserDynamicModel.TopicBean>(
            mContext, R.layout.dynamic_list_item, datas) {

            protected TextView         title;
            protected TextView         commentsTv;
            protected TextView         incentiveTv;
            protected TextView         skimTv;
            protected SimpleDraweeView cover;
            protected TextView         createTime;

            @Override
            public void convert(ViewHolder helper, UserDynamicModel.TopicBean item) {
                createTime = helper.getView(R.id.createTime);
                cover = helper.getView(R.id.cover);
                skimTv = helper.getView(R.id.skimTv);
                incentiveTv = helper.getView(R.id.incentiveTv);
                commentsTv = helper.getView(R.id.commentsTv);
                title = helper.getView(R.id.title);

                cover.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 414, 310)));

                Date date = new Date();
                date.setTime(DateFormatUtil.getTime(item.time));
                createTime.setText(DateFormatUtil.timeLogic(date));
                title.setText(item.title + "");
                commentsTv.setText(item.replyCount + "");
                skimTv.setText(item.viewCount + "");
                incentiveTv.setText(item.likeCount + "");
            }
        };
        list.setAdapter(adapter);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}