package com.tgf.kcwc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.FriendGroupingModel;

import java.util.ArrayList;

/**
 * author:10264
 * date:2017/8/10
 * describe:
 */

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {
    private Context context;
    private ArrayList<FriendGroupingModel.ListItem> listItems;
    private int selectPosition = 0;
    OnItemsClickListener onItemsClickListener;
    public interface OnItemsClickListener{
        void onItemClick(int position);
    }

    public void setOnItemsClickListener(OnItemsClickListener onItemsClickListener) {
        this.onItemsClickListener = onItemsClickListener;
    }

    public BusinessAdapter(Context context, ArrayList<FriendGroupingModel.ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grouping_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == selectPosition) {
            holder.checkBox.setBackgroundResource(R.drawable.shape_s_bg);
            holder.checkBox.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.checkBox.setBackgroundResource(R.drawable.grouping_n_bg);
            holder.checkBox.setTextColor(context.getResources().getColor(R.color.tab_text_n_color));
        }
        holder.checkBox.setText(listItems.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemsClickListener != null){
                    onItemsClickListener.onItemClick(position);
                    setSelectPosition(position);
                }
            }
        });

    }

    private void setSelectPosition(int position){
        selectPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (TextView) itemView.findViewById(R.id.item_grouping);
        }
    }
}
