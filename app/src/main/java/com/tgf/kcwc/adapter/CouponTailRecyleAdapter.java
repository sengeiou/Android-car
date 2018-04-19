package com.tgf.kcwc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.CouponFellowModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/16 0016
 * E-mail:hekescott@qq.com
 */

public class CouponTailRecyleAdapter extends RecyclerView.Adapter {
    private ArrayList<CouponFellowModel.CouponFellowItem> couponFellowList;
    private Context context;
    private final LayoutInflater mLayoutInflater;
    public final int VIEW_TYPE_RECIVER = 0;
    public final int VIEW_TYPE_SENDER = 1;
    public final int VIEW_TYPE_MSG = 2;

    public CouponTailRecyleAdapter(Context context, ArrayList<CouponFellowModel.CouponFellowItem> couponFellowList) {
        this.couponFellowList = couponFellowList;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        String msgType = couponFellowList.get(position).msgType;
        if ("send".equals(msgType)){
            return VIEW_TYPE_SENDER;
        }else if ("receive".equals(msgType)){
            return VIEW_TYPE_RECIVER;
        }else if ("msg".equals(msgType)){
            return VIEW_TYPE_MSG;
        }
        return VIEW_TYPE_SENDER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;

        if (viewType == VIEW_TYPE_RECIVER) {
             holder= new RecieverViewHolder(mLayoutInflater.inflate(R.layout.recyleitem_coupon_reciever, parent, false));
        } else if (viewType == VIEW_TYPE_SENDER) {
             holder=new SenderViewHolder(mLayoutInflater.inflate(R.layout.recyleitem_coupon_fellowsender, parent, false));
        } else if (viewType == VIEW_TYPE_MSG) {
             holder=new MsgViewHolder(mLayoutInflater.inflate(R.layout.listitem_ticket_fellowfooter, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CouponFellowModel.CouponFellowItem couponfellowItem = couponFellowList.get(position);
        if (holder instanceof SenderViewHolder) {
            SenderViewHolder senderViholder = (SenderViewHolder) holder;
            senderViholder.dateTv.setText(couponfellowItem.createTime);
            senderViholder.sendTitleTv.setText(couponfellowItem.h1);
            senderViholder.sendContentTv.setText(couponfellowItem.h2);
            if(couponfellowItem.data!=null&&couponfellowItem.data.size()!=0){
                String couponCode="";
                for (String copdestr : couponfellowItem.data) {
                    couponCode +=copdestr+"\n";
                }
                senderViholder.sendfellowCouponcodetv.setText(couponCode);
            }
        } else if (holder instanceof RecieverViewHolder) {
            RecieverViewHolder recieverHolder = (RecieverViewHolder) holder;
            recieverHolder.dateTv.setText(couponfellowItem.createTime);
            recieverHolder.recieverTitleTv.setText(couponfellowItem.h1);
            recieverHolder.recieverfellow_contenttv.setText(couponfellowItem.h2);
            if(couponfellowItem.data!=null&&couponfellowItem.data.size()!=0){
                String couponCode="";
                for (String copdestr : couponfellowItem.data) {
                    couponCode +=copdestr+"\n";
                }
                recieverHolder.couponrecieve_codetv.setText(couponCode);
            }

        } else if (holder instanceof MsgViewHolder) {
            MsgViewHolder msgHolder = (MsgViewHolder) holder;
            msgHolder.sendfellow_num.setText(couponfellowItem.h2);
        }


    }

    @Override
    public int getItemCount() {
        return couponFellowList.size();
    }

    public static class MsgViewHolder extends RecyclerView.ViewHolder {
        public TextView sendfellow_num;

        public MsgViewHolder(View itemView) {
            super(itemView);
            sendfellow_num = (TextView) itemView.findViewById(R.id.sendfellow_num);
        }
    }

    public static class SenderViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarIv;
        public TextView dateTv;
        public TextView sendContentTv;
        public TextView sendTitleTv;
        public TextView sendfellowCouponcodetv;

        public SenderViewHolder(View itemView) {
            super(itemView);
            avatarIv = (ImageView) itemView.findViewById(R.id.fellow_sendiv);
            dateTv = (TextView) itemView.findViewById(R.id.sendfellow_date);
            sendContentTv = (TextView) itemView.findViewById(R.id.sendfellow_contenttv);
            sendTitleTv = (TextView) itemView.findViewById(R.id.sendfellow_titletv);
            sendfellowCouponcodetv = (TextView) itemView.findViewById(R.id.sendfeloow_couponcodetv);
        }
    }

    public static class RecieverViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarIv;
        public TextView dateTv;
        public TextView recieverfellow_contenttv;
        public TextView recieverTitleTv;
        public TextView couponrecieve_codetv;

        public RecieverViewHolder(View itemView) {
            super(itemView);
            avatarIv = (ImageView) itemView.findViewById(R.id.fellow_recieveiv);
            dateTv = (TextView) itemView.findViewById(R.id.recieverfellow_date);
            recieverTitleTv = (TextView) itemView.findViewById(R.id.recieverfellow_titletv);
            recieverfellow_contenttv = (TextView) itemView.findViewById(R.id.recieverfellow_contenttv);
            couponrecieve_codetv = (TextView) itemView.findViewById(R.id.couponrecieve_codetv);
        }
    }



}
