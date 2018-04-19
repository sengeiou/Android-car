package com.tgf.kcwc.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.PageGridView;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/5 0005
 * E-mail:hekescott@qq.com
 */

public class VoucherMenuAdapter extends
                                PageGridView.PagingAdapter<VoucherMenuAdapter.MenuViewholder>{
    List<CouponCategory>    mData;
    private Context mContext;
    private int width;
    private int height;

    public VoucherMenuAdapter(List<CouponCategory> data, Context context) {
        this.mContext =context;
        this.mData=data;
        width = (int) (DensityUtils.getPhonewidth(mContext)/5);
        height = width+DensityUtils.dp2px(mContext,8);
    }


    @Override
    public List getData() {
        return mData;
    }

    @Override
    public Object getEmpty() {
        CouponCategory couponMenu = new CouponCategory();
        return couponMenu;
    }

    @Override
    public MenuViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.griditem_voucher_menu, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        params.width = width;
        view.setLayoutParams(params);

        return new MenuViewholder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewholder holder, int position) {
        CouponCategory couponMenu = mData.get(position);
        if(couponMenu ==null|| TextUtils.isEmpty(couponMenu.name)){
            holder.icon.setVisibility(View.GONE);
        }else {
            String iconUrl = URLUtil.builderImgUrl(couponMenu.icon,360,360);
            holder.icon.setImageURI(Uri.parse(iconUrl));
            holder.icon.setVisibility(View.VISIBLE);
        }
        holder.tv_title.setText(couponMenu.name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MenuViewholder extends RecyclerView.ViewHolder {
        public TextView  tv_title;
        public SimpleDraweeView icon;

        public MenuViewholder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            icon = (SimpleDraweeView) itemView.findViewById(R.id.icon);
        }
    }
}
