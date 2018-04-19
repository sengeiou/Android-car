package com.tgf.kcwc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.BeautyDetailModel;
import com.tgf.kcwc.mvp.model.ImgItem;
import com.tgf.kcwc.see.BeatutyPhotoGalleryActivity;
import com.tgf.kcwc.see.BigPhotoPageActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2016/12/29 0029
 * E-mail:hekescott@qq.com
 */

public class BeautyAlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //item类型
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mHeaderCount = 1;//头部View个数
    private int mBottomCount = 1;//底部View个数

    public BeautyAlbumAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    private ArrayList<ImgItem> imgItemList = new ArrayList<>();
    private BeautyDetailModel beautyDetailModel;

    @Override
    public int getItemViewType(int position) {
        //        int dataItemCount = getContentItemCount();
        if (position == 0) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if (position != 0 && position <= imgItemList.size()) {
            //底部View
            return ITEM_TYPE_CONTENT;
        } else {
            //内容View
            return ITEM_TYPE_BOTTOM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == ITEM_TYPE_HEADER) {
            View headView = mLayoutInflater.inflate(R.layout.layout_head_beautyalbum, parent,
                    false);
            viewHolder = new HeaderViewHolder(headView);
        } else if (viewType == ITEM_TYPE_CONTENT) {
            View contentView = mLayoutInflater.inflate(R.layout.gridview_item_motopics, parent,
                    false);
            viewHolder = new ContentViewHolder(contentView);

        }else if(viewType == ITEM_TYPE_BOTTOM){
            View boottomView = mLayoutInflater.inflate(R.layout.bottom_hint_layout, parent,
                    false);
            viewHolder = new ContentViewHolder(boottomView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            if (beautyDetailModel != null) {
                headerViewHolder.beautyname.setText(beautyDetailModel.name);
                headerViewHolder.beautyPlace.setText(beautyDetailModel.boothName+"/"+beautyDetailModel.hallName);
                String avartarUrl = URLUtil.builderImgUrl(beautyDetailModel.cover, 144, 144);
                headerViewHolder.avatar.setImageURI(Uri.parse(avartarUrl));
                if(beautyDetailModel.height==0){
                    headerViewHolder.heightTv.setText("身高:  不详" );
                }else{
                    headerViewHolder.heightTv.setText("身高:  " + beautyDetailModel.height+"cm");
                }
                if(beautyDetailModel.bust==0||beautyDetailModel.waist==0||beautyDetailModel.hipline==0){
                    headerViewHolder.modelsizeTv
                            .setText("三围:  不详" );
                }else {
                    headerViewHolder.modelsizeTv
                            .setText("三围:  " + beautyDetailModel.bust + "/" + beautyDetailModel.waist + "/"
                                    + beautyDetailModel.hipline);
                }
                headerViewHolder.modelDescTv.setText("奖项:  " + beautyDetailModel.prize);

                String brandUrl = URLUtil.builderImgUrl(beautyDetailModel.brandLogo, 144, 144);
                headerViewHolder.beautyBrand.setImageURI(Uri.parse(brandUrl));
                if (beautyDetailModel.id != 0) {
                    View.OnClickListener goHomeonClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent toUserPageAct = new Intent(mContext, UserPageActivity.class);
                            toUserPageAct.putExtra(Constants.IntentParams.ID, beautyDetailModel.id);
                            mContext.startActivity(toUserPageAct);
                        }
                    };
                    headerViewHolder.avatar.setOnClickListener(goHomeonClickListener);
                    headerViewHolder.goHome.setOnClickListener(goHomeonClickListener);
                } else {
                    headerViewHolder.goHome.setText("主页待认领");
                    headerViewHolder.goHome.setTextColor(Color.GRAY);
                }
            }

        } else if (holder instanceof ContentViewHolder) {
            final ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            if (imgItemList.size() != 0 && position > 0&&(position - 1)<imgItemList.size()) {
                final String url = imgItemList.get(position - 1).linkUrl;
                String linkPath = URLUtil.builderImgUrl(url, 360, 360);
                contentViewHolder.imageView.setImageURI(Uri.parse(linkPath));
                contentViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BeatutyPhotoGalleryActivity.class);
                        intent.putExtra(Constants.IntentParams.INDEX,position - 1);
                        intent.putParcelableArrayListExtra(Constants.IntentParams.DATA, imgItemList);
                        mContext.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return imgItemList.size() + 2;
    }

    //内容 InnerViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView imageView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.griditem_album_iv);
        }
    }

    //头部 InnerViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView beautyname;
        private TextView goHome;
        private TextView heightTv;
        private TextView modelsizeTv;
        private TextView modelDescTv;
        private SimpleDraweeView avatar;
        private SimpleDraweeView beautyBrand;
        private final TextView beautyPlace;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            beautyname = (TextView) itemView.findViewById(R.id.beautyalbum_modelname_tv);

            goHome = (TextView) itemView.findViewById(R.id.beautyalbum_gohomepage_tv);
            modelsizeTv = (TextView) itemView.findViewById(R.id.beautyalbum_modelsize_tv);
            heightTv = (TextView) itemView.findViewById(R.id.beautyalbum_modelheight_tv);
            modelDescTv = (TextView) itemView.findViewById(R.id.beautyalbum_modeldesc_tv);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.model_avatar);
            beautyBrand = (SimpleDraweeView) itemView.findViewById(R.id.beautyalbum_brand);
            beautyPlace = (TextView) itemView.findViewById(R.id.beautyalbum_place);
        }
    }

    public void setBeautyDetailModel(BeautyDetailModel beautyDetailModel) {
        this.beautyDetailModel = beautyDetailModel;
        setImgItemList(beautyDetailModel.imgStore.imgList);
    }

    public static class BootViewHolder extends RecyclerView.ViewHolder{

        public BootViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void setImgItemList(ArrayList<ImgItem> imgItemList) {
        this.imgItemList = imgItemList;
    }

    public void appendImgItemList(List<ImgItem> imgItemList) {
        this.imgItemList.addAll(imgItemList);
    }
    //    //底部 InnerViewHolder
    //    public static class BottomViewHolder extends RecyclerView.InnerViewHolder {
    //        public BottomViewHolder(View itemView) {
    //            super(itemView);
    //        }
    //    }
}
