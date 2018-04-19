package com.tgf.kcwc.see;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.OnClickListener;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Author：Jenny
 * Date:2016/12/12 17:50
 * E-mail：fishloveqin@gmail.com
 */
public class SeriesItemViewProvider extends ItemViewProvider<CarBean, ViewHolder> {

    protected View             rootView;
    protected RelativeLayout   topLayout;
    protected SimpleDraweeView img;
    protected TextView         seriesName;
    protected TextView         price;
    protected RelativeLayout   contentLayout;
    protected TextView         desc;
    protected RelativeLayout   bottomLayout;
    private ViewGroup          mParent;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                            @NonNull ViewGroup parent) {
        View convertView = null;
        this.mParent = parent;
        ViewHolder viewHolder = ViewHolder.get(parent.getContext(), convertView, parent,
            R.layout.car_series_list_item, getPosition());
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final @NonNull CarBean item) {

        holder.setText(R.id.brandName, item.factoryName);
        ImageView imageView = holder.getView(R.id.brandLogoImg);
        imageView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.factoryLogo, 270, 203)));
        topLayout = holder.getView(R.id.topLayout);
        img = holder.getView(R.id.img);
        img.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
        seriesName = holder.getView(R.id.seriesName);
        seriesName.setText(item.seriesName);
        price = holder.getView(R.id.price);
        price.setText(item.price + "");
        contentLayout = holder.getView(R.id.contentLayout);
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(mParent, v, item, getPosition());
                }
            }
        });
        desc = holder.getView(R.id.desc);
        desc.setText("共" + item.matchNums + "款符合条件");
        bottomLayout = holder.getView(R.id.bottomLayout);
        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mClickListener != null) {
                    mClickListener.onClick(v, item);
                }
            }
        });

    }

    private OnClickListener mClickListener;

    public void setOnClickListener(OnClickListener listener) {
        this.mClickListener = listener;
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

}