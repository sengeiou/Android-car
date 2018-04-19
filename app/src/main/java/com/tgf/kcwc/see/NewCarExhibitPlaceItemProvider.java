package com.tgf.kcwc.see;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.model.NewCarBean;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Author：Jenny
 * Date:2016/12/26 20:50
 * E-mail：fishloveqin@gmail.com
 */
public class NewCarExhibitPlaceItemProvider extends ItemViewProvider<NewCarBean, ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                            @NonNull ViewGroup parent) {
        View convertView = null;
        ViewHolder viewHolder = ViewHolder.get(parent.getContext(), convertView, parent,
            R.layout.new_car_exhibitplace_item, getPosition());
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final NewCarBean moto) {

        holder.setText(R.id.name, moto.brandName);
        SimpleDraweeView draweeView = holder.getView(R.id.img);
        draweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(moto.brandLogo, 144, 144)));
        holder.setText(R.id.releaseTime, "发布时间: " + DateFormatUtil.formatTime1(moto.releaseTime));

        holder.setText(R.id.carname, moto.productName + "");
        holder.setText(R.id.carprice, "指导价: " + moto.brandPrice);
        GridView gridView = holder.getView(R.id.grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(parent, view, moto, position);
                }

            }
        });
        gridView.setAdapter(new WrapAdapter<Image>(holder.getConvertView().getContext(), moto.imgs,
            R.layout.new_car_grid_item) {
            @Override
            public void convert(ViewHolder helper, Image item) {

                SimpleDraweeView draweeView = helper.getView(R.id.img);
                draweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.link, 270, 203)));

            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(gridView,3);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private OnItemClickListener mOnItemClickListener;

}