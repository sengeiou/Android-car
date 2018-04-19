package com.tgf.kcwc.coupon;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Auther: Scott
 * Date: 2016/12/20 0020
 * E-mail:hekescott@qq.com
 */

public class CouponTitleItemProvider extends ItemViewProvider<Coupon, CouponTitleItemProvider.MotoParamTitleViewHolder> {
    private Context mContext;

    public CouponTitleItemProvider(Context context) {
        mContext = context;
    }

    static class MotoParamTitleViewHolder extends RecyclerView.ViewHolder {
        final TextView oldPrice;
        final TextView title;
        final TextView desc;
        final TextView nowPrice;
        final TextView distance;
        final TextView saleNum;
        final SimpleDraweeView cover;
        final LinearLayout rootlr;

        public MotoParamTitleViewHolder(View itemView) {
            super(itemView);
            this.nowPrice = (TextView) itemView.findViewById(R.id.recyleitem_near_nowprice);
            this.title = (TextView) itemView.findViewById(R.id.listitem_recoment_coupontitle);
            this.desc = (TextView) itemView.findViewById(R.id.couponlist_desc);
            this.oldPrice = (TextView) itemView.findViewById(R.id.listviewitem_recomment_oldprice);
            this.saleNum = (TextView) itemView.findViewById(R.id.listviewitem_recomment_salenum);
            this.distance = (TextView) itemView.findViewById(R.id.couponlist_distancetv);
            this.cover = (SimpleDraweeView) itemView.findViewById(R.id.couponlist_cover);
            this.rootlr = (LinearLayout) itemView.findViewById(R.id.select);
        }
    }

    @NonNull
    @Override
    protected CouponTitleItemProvider.MotoParamTitleViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        View root = inflater.inflate(R.layout.listview_item_recomment_voucherlist, parent, false);
        return new CouponTitleItemProvider.MotoParamTitleViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CouponTitleItemProvider.MotoParamTitleViewHolder holder, @NonNull final Coupon coupon) {
//        holder.type.setText(motoParamTitleItem.name);

        holder.desc.setText(coupon.desc);
        boolean isFree = Double.parseDouble(coupon.price) == 0;
        if (isFree) {
            holder.nowPrice.setText("免费");
            holder.nowPrice.setTextColor(mContext.getResources().getColor(R.color.text_color10));
        } else {
            holder.nowPrice.setText("￥" + coupon.price);
            holder.nowPrice.setTextColor(mContext.getResources().getColor(R.color.voucher_yellow));
        }
        holder.oldPrice.setText(SpannableUtil.getDelLineString("￥" + coupon.denomination));
        if (coupon.is_finished == 1) {
            holder.saleNum.setText("抢光了");
        } else {
            if (isFree) {
                holder.saleNum.setText("已领:" + coupon.total);
            } else {
                holder.saleNum.setText("已售:" + coupon.total);
            }
        }
        holder.distance.setText(coupon.getDistance());
        holder.title.setText(coupon.title);
        holder.cover.setImageURI(Uri.parse(URLUtil.builderImgUrl(coupon.cover, 270, 203)));
        holder.rootlr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toIntent = new Intent(mContext, CouponDetailActivity.class);
                toIntent.putExtra(Constants.IntentParams.ID, coupon.id);
                mContext.startActivity(toIntent);
            }
        });
    }
}
