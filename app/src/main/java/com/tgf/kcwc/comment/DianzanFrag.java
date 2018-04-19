package com.tgf.kcwc.comment;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.LikeListModel;
import com.tgf.kcwc.mvp.presenter.AttentionDataPresenter;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/13 0013
 * E-mail:hekescott@qq.com
 */

public class DianzanFrag extends BaseFragment implements AttentionView {
    private List<LikeListModel.LikeBean> mlikeBeanList;
    private AttentionDataPresenter       mCancelPresenter;
    private int                          mClickPos = -3;
    //取消关注

    private NestFullListView             dianzanLv;

    public DianzanFrag(List<LikeListModel.LikeBean> likeBeanList) {
        this.mlikeBeanList = likeBeanList;
    }

    @Override
    protected void updateData() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_dianzan;
    }

    @Override
    protected void initView() {
        mCancelPresenter = new AttentionDataPresenter();
        mCancelPresenter.attachView(this);
        dianzanLv = findView(R.id.dianzan_lv);
        dianzanLv.setAdapter(new NestFullListViewAdapter<LikeListModel.LikeBean>(
            R.layout.user_like_list_item, mlikeBeanList) {
            protected TextView         descTv;
            protected SimpleDraweeView brandLogo;
            protected ImageView        commentPopmanTv;
            protected ImageView        commentModelTv;
            protected TextView         nametv;
            protected RelativeLayout   headerImgLayout;
            protected SimpleDraweeView genderImg;
            protected SimpleDraweeView img;
            Button                     mAttBtn;

            @Override
            public void onBind(final int pos, final LikeListModel.LikeBean item,
                               NestFullViewHolder helper) {
                img = helper.getView(R.id.img);
                genderImg = helper.getView(R.id.genderImg);
                headerImgLayout = helper.getView(R.id.headerImgLayout);
                nametv = helper.getView(R.id.nametv);
                commentModelTv = helper.getView(R.id.comment_model_tv);
                commentPopmanTv = helper.getView(R.id.comment_popman_tv);
                brandLogo = helper.getView(R.id.brandLogo);
                mAttBtn = helper.getView(R.id.attentionBtn);
                descTv = helper.getView(R.id.desc);
                nametv.setText(item.user.nick);
                //达人
                if (item.user.isExpert == 1) {
                    commentPopmanTv.setVisibility(View.VISIBLE);
                } else {
                    commentPopmanTv.setVisibility(View.GONE);
                }
                //模特
                if (item.user.is_model == 1) {
                    commentModelTv.setVisibility(View.VISIBLE);
                } else {
                    commentModelTv.setVisibility(View.GONE);
                }
                descTv.setText(item.user.desc);

                //性别
                if (item.user.gender == 1) {
                    genderImg.setImageResource(R.drawable.icon_men);
                } else {
                    genderImg.setImageResource(R.drawable.icon_women);
                }

                if (item.user.is_master == 1) {
                    brandLogo.setVisibility(View.VISIBLE);
                    brandLogo.setImageURI(
                        Uri.parse(URLUtil.builderImgUrl(item.user.masterLogo + "", 144, 144)));
                } else {
                    brandLogo.setVisibility(View.GONE);
                }

                //用户头像
                img.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.user.avatar, 144, 144)));

                final int isFlw = item.user.isFlw;
                if (isFlw == 1) {

                    mAttBtn.setText("已关注");
                    mAttBtn.setBackgroundResource(R.drawable.button_bg_normal);
                } else {
                    mAttBtn.setText("关注");
                    mAttBtn.setBackgroundResource(R.drawable.button_bg_3);
                }
                mAttBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickPos = pos;
                        if (isFlw == 1) {

                            mCancelPresenter.cancelAttention(item.user.userId + "",
                                IOUtils.getToken(mContext));
                        } else {
                            mCancelPresenter.execAttention(item.user.userId + "",
                                IOUtils.getToken(mContext));
                        }

                    }
                });

            }
        });
    }

    @Override
    public void showAddAttention(Object o) {
        CommonUtils.showToast(mContext, "您已加关注");
        if (mClickPos != -3) {
            mlikeBeanList.get(mClickPos).user.isFlw = 1;
        }
        dianzanLv.notifyDataSetChange(mlikeBeanList);
    }

    @Override
    public void showCancelAttention(Object obj) {
        CommonUtils.showToast(mContext, "您已取消关注");
        if (mClickPos != -3){
            mlikeBeanList.get(mClickPos).user.isFlw = 0;
        }
        dianzanLv.notifyDataSetChange(mlikeBeanList);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }
    public void notifylikeDataSetChange(List<LikeListModel.LikeBean> likeBeanList){
        this.mlikeBeanList.clear();
        this.mlikeBeanList.addAll(likeBeanList);
        dianzanLv.notifyDataSetChange(mlikeBeanList);
    }

}
