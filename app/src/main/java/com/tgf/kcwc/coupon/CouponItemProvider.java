package com.tgf.kcwc.coupon;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.Coupon;

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

public class CouponItemProvider extends ItemViewProvider<Coupon, CouponItemProvider.MotoParamTitleViewHolder> {

    static class MotoParamTitleViewHolder extends RecyclerView.ViewHolder {
        final TextView nowPrice;
        public MotoParamTitleViewHolder(View itemView) {
            super(itemView);
            this.nowPrice =(TextView)itemView.findViewById(R.id.recyleitem_near_nowprice);
        }
    }

    @NonNull
    @Override
    protected CouponItemProvider.MotoParamTitleViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        View root = inflater.inflate(R.layout.recyleitem_near_couponitem, parent, false);
        return new CouponItemProvider.MotoParamTitleViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull CouponItemProvider.MotoParamTitleViewHolder holder, @NonNull Coupon coupon) {
//        holder.type.setText(motoParamTitleItem.name);
    }
}
