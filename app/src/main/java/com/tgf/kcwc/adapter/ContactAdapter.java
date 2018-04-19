package com.tgf.kcwc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.ContactUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/9 0009
 * E-mail:hekescott@qq.com
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    protected Context           mContext;
    protected List<ContactUser> mDatas;
    protected LayoutInflater    mInflater;
    protected ViewGroup parentView;
    public ContactAdapter(Context mContext, List<ContactUser> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        mInflater.inflate(R.layout.check_ticketcontact_item, null);
        parentView = parent;
        return new ViewHolder(mInflater.inflate(R.layout.check_ticketcontact_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ContactUser contactUser = mDatas.get(position);
        Logger.d("contact 8");
        if(contactUser!=null){
            Logger.d("contact 9");
        holder.contactTv.setText(contactUser.name);
            Logger.d("contact 9 contactUser.name"+contactUser.name);
        if (contactUser.isSelected) {
            holder.isSelectedIv.setVisibility(View.VISIBLE);
        } else {
            holder.isSelectedIv.setVisibility(View.INVISIBLE);
        }
        }
        holder.contactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.get(position).isSelected = !contactUser.isSelected;
                monItemClickListener.onItemClick(parentView,v,contactUser,position);
                notifyDataSetChanged();
            }
        });
        Logger.d("contact 10");
    }

    private OnItemClickListener<ContactUser> monItemClickListener;

    public ContactAdapter setOnItemClicklisenter(OnItemClickListener<ContactUser> onItemClicklisenter) {
        this.monItemClickListener = onItemClicklisenter;
        return this;
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void setDatas(ArrayList<ContactUser> datas) {
        this.mDatas = datas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactTv;
        View     contactItem;
        View     isSelectedIv;

        public ViewHolder(View itemView) {
            super(itemView);
            contactTv = (TextView) itemView.findViewById(R.id.contact_name);
            contactItem = itemView.findViewById(R.id.contact_item);
            isSelectedIv = itemView.findViewById(R.id.select_status_img);
        }
    }
}
