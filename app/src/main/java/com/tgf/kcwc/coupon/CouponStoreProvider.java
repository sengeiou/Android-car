package com.tgf.kcwc.coupon;

import com.hedgehog.ratingbar.RatingBar;
import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.StoreDetailData;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Auther: Scott
 * Date: 2016/12/20 0020
 * E-mail:hekescott@qq.com
 */

public class CouponStoreProvider extends ItemViewProvider<StoreDetailData, CouponStoreProvider.MotoParamTitleViewHolder> {

    static class MotoParamTitleViewHolder extends RecyclerView.ViewHolder {
        final RatingBar ratingBar;
        final TextView title;
        public MotoParamTitleViewHolder(View itemView) {
            super(itemView);
            this.title =(TextView)itemView.findViewById(R.id.name);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }

    @NonNull
    @Override
    protected CouponStoreProvider.MotoParamTitleViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        View root = inflater.inflate(R.layout.recyleitem_near_store, parent, false);
        return new CouponStoreProvider.MotoParamTitleViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull CouponStoreProvider.MotoParamTitleViewHolder holder, @NonNull StoreDetailData motoParamTitleItem) {
//        holder.type.setText(motoParamTitleItem.name);
    }
}
