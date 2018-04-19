package com.tgf.kcwc.see.exhibition.plus;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Beauty;
import com.tgf.kcwc.mvp.presenter.AttentionDataPresenter;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.see.model.ModelAlbumActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public class SeeModelFragment extends BaseFragment implements AttentionView {
    private GridView gridView;
    private WrapAdapter<Beauty> beautyAdapter;
    private ArrayList<Beauty> modelList = new ArrayList<>();

    private AttentionDataPresenter mAttentionPresenter;

    public SeeModelFragment() {
    }

    public SeeModelFragment(ArrayList<Beauty> modelList) {
        super();
        this.modelList.addAll(modelList);
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_see_model;
    }

    @Override
    protected void initView() {
        gridView = findView(R.id.grid_view);
        mAttentionPresenter = new AttentionDataPresenter();
        mAttentionPresenter.attachView(this);
        beautyAdapter = new WrapAdapter<Beauty>(mContext, modelList,
                R.layout.gridviewitem_exhibitplacedetail_beauty) {
            @Override
            public void convert(ViewHolder helper, final Beauty item) {
                helper.setText(R.id.beauty_name, item.name);
                SimpleDraweeView avatarCoverIv = helper.getView(R.id.recyleitme_beauty_cover);
                String coverUrl = URLUtil.builderImgUrl(item.cover, 360, 360);
                avatarCoverIv.setImageURI(Uri.parse(coverUrl));
                avatarCoverIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ModelAlbumActivity.class);
                        intent.putExtra(Constants.IntentParams.ID, item.modelId);
                        getContext().startActivity(intent);
                    }
                });
                String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);

                helper.setSimpleDraweeViewURL(R.id.avatarbadge_avatar, avatarUrl);
                //性别
                final ImageView genderIv = helper.getView(R.id.avatarbadge_gender);
                if (item.sex == 1) {
                    genderIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_men));
                } else {
                    genderIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_women));
                }
                final ImageView modelIv = helper.getView(R.id.modelIv);
                //模特logo
                if (item.isNew == 1){
                    modelIv.setBackgroundResource(R.drawable.icon_model);
                }
                //品牌logo
                ImageView brandIv = helper.getView(R.id.brandIv);
                brandIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.brandLogo,144,144)));
                //关注、粉丝数量
                final TextView addattentionTv = helper.getView(R.id.add_attentiontv);
                final TextView fansTv = helper.getView(R.id.fansTv);
                addattentionTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.isFollow++;
                        if (IOUtils.isLogin(getContext())) {
                            mAttentionPresenter.execAttention(item.modelId + "",
                                    IOUtils.getToken(getContext()));
                        }
                        fansTv.setText("粉丝：" + item.isFollow);
//                        fansTv.setTextColor(getContext().getResources().getColor(R.color.text_color3));
                    }
                });

            }
        };
        gridView.setAdapter(beautyAdapter);
//        ViewUtil.setListViewHeightBasedOnChildren(beautysGv, 3);
    }

    @Override
    public void showAddAttention(Object o) {
        CommonUtils.showToast(getContext(), "加关注成功");
    }

    @Override
    public void showCancelAttention(Object o) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }
}
