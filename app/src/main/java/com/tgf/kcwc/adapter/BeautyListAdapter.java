package com.tgf.kcwc.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Beauty;
import com.tgf.kcwc.mvp.presenter.AttentionDataPresenter;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.see.model.ModelAlbumActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2016/12/29 0029
 * E-mail:hekescott@qq.com
 */

public class BeautyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AttentionView {
    public final int VIEW_TYPE_BRAND = 1;
    public final int VIEW_TYPE_MODEL = 2;
    private List<Beauty> brands;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private AttentionDataPresenter mAttentionPresenter;
    private boolean isSee = true;

    public BeautyListAdapter(Context context, List<Beauty> brands) {
        this.brands = brands;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        mAttentionPresenter = new AttentionDataPresenter();
        mAttentionPresenter.attachView(this);
    }

    @Override
    public int getItemViewType(int position) {
        if (brands.get(position).isTitle) {
            return VIEW_TYPE_BRAND;
        } else {
            return VIEW_TYPE_MODEL;
        }
    }

    public void notifyDataSetChanged(List<Beauty> brands) {
        this.brands = brands;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_TYPE_BRAND) {
            viewHolder = new BrandViewHolder(mLayoutInflater.inflate(R.layout.recyleview_item_brandtitle, parent, false));
        } else if (viewType == VIEW_TYPE_MODEL) {
            viewHolder = new ModelViewHolder(mLayoutInflater.inflate(R.layout.recyleview_item_beauty, parent, false));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Beauty beauty = brands.get(position);
        if (holder instanceof ModelViewHolder) {
            ModelViewHolder modelHolder = (ModelViewHolder) holder;
            String url = URLUtil.builderImgUrl(beauty.cover, 270, 203);
            String avartaUrl = URLUtil.builderImgUrl(beauty.avatar, 144, 144);
            modelHolder.avatar.setImageURI(Uri.parse(avartaUrl));
            modelHolder.cover.setImageURI(Uri.parse(url));
            modelHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ModelAlbumActivity.class);
                    intent.putExtra(Constants.IntentParams.ID, beauty.modelId);
                    context.startActivity(intent);
                }
            });
            modelHolder.beautyNameTv.setText(beauty.name);
            if (beauty.sex == 1) {
                modelHolder.genderIv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_men));
            } else {
                modelHolder.genderIv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_women));
            }
            modelHolder.beauty_fellowaddView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beauty.isFollow++;
                    beauty.isSee = false;
                    notifyDataSetChanged();
                    mAttentionPresenter.execAttention(beauty.userId + "",
                            IOUtils.getToken(context));
                }
            });
            if(beauty.sex == 0){
                modelHolder.beauty_fellownumTV.setVisibility(View.GONE);
                modelHolder.beauty_fellowaddView.setVisibility(View.GONE);
            }else{
            if (isSee) {
                modelHolder.beauty_fellowaddView.setVisibility(View.VISIBLE);
                modelHolder.beauty_fellownumTV.setVisibility(View.GONE);
            } else {
                modelHolder.beauty_fellowaddView.setVisibility(View.GONE);
                modelHolder.beauty_fellownumTV.setVisibility(View.VISIBLE);
            }
            modelHolder.beauty_fellownumTV.setText("粉丝 " + beauty.fansNum);
            }
        } else if (holder instanceof BrandViewHolder) {
            BrandViewHolder brandHolder = (BrandViewHolder) holder;
            String url = URLUtil.builderImgUrl(beauty.brandLogo, 144, 144);
            brandHolder.brandlogoIv.setImageURI(Uri.parse(url));
//            brandHolder.brandlogoIv.setImageURI(Uri.parse("http://kcwc2pic.51kcwc.com/brand/fengtian@3x.png"));
            brandHolder.brandNameTv.setText(beauty.brandName);
        }
    }


    public static class BrandViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView brandlogoIv;
        public TextView brandNameTv;

        public BrandViewHolder(View itemView) {
            super(itemView);
            brandlogoIv = (SimpleDraweeView) itemView.findViewById(R.id.beautylist_brand);
            brandNameTv = (TextView) itemView.findViewById(R.id.beautylist_brandname);
        }
    }

    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView cover;
        public SimpleDraweeView avatar;
        public ImageView genderIv;
        public TextView beauty_fellownumTV;
        public View beauty_fellowaddView;
        public TextView beautyNameTv;

        public ModelViewHolder(View itemView) {
            super(itemView);
            cover = (SimpleDraweeView) itemView.findViewById(R.id.beauty_avatar_iv);
            beautyNameTv = (TextView) itemView.findViewById(R.id.beautylist_modelname);
            beauty_fellownumTV = (TextView) itemView.findViewById(R.id.beauty_fellownum);
            beauty_fellowaddView = itemView.findViewById(R.id.beauty_fellowadd);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatarbadge_avatar);
            genderIv = (ImageView) itemView.findViewById(R.id.avatarbadge_gender);
        }
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    @Override
    public void showAddAttention(Object o) {
        CommonUtils.showToast(context, "加关注成功");
    }

    @Override
    public void showCancelAttention(Object o) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return context;
    }
}
