package com.tgf.kcwc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.SelectExhibitionPosModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/31
 * @describle 现场调配——展位适配器
 */
public class ExhibitionPosAdapter extends RecyclerView.Adapter<ExhibitionPosAdapter.PosVh> {
    private Context context;
    private ArrayList<SelectExhibitionPosModel.Data> listItems;
    private int selectPosition;
    ExhibitionPosAdapter.OnItemsClickListener onItemsClickListener;
    String name = "",serviceChargeStr,depositStr;//车位名
    private int parkTimesId;

    public interface OnItemsClickListener {
        void onItemClick(int position);
    }

    public void setOnItemsClickListener(ExhibitionPosAdapter.OnItemsClickListener onItemsClickListener) {
        this.onItemsClickListener = onItemsClickListener;
    }

    public ExhibitionPosAdapter(Context context, ArrayList<SelectExhibitionPosModel.Data> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public PosVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_item, parent, false);
        return new ExhibitionPosAdapter.PosVh(view);
    }

    @Override
    public void onBindViewHolder(PosVh holder, final int position) {
//        状态，0未开放，1可用，2已锁定，3保留车位
        SelectExhibitionPosModel.Data park = listItems.get(position);
        // status 1:空闲 2：锁定 3：预备
        switch (park.status) {
            case 1:
                if (park.isSelect()) {
                    //选中 绿色+白色
                    holder.checkBox.setBackgroundResource(R.drawable.icon_selected);
                    holder.checkBox.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    //未选中 灰色+灰色
                    holder.checkBox.setBackgroundResource(R.drawable.icon_select);
                    holder.checkBox.setTextColor(context.getResources().getColor(R.color.text_color));
                }
                break;
            case 2:
                //锁定 红色+白色
                holder.checkBox.setBackgroundResource(R.drawable.icon_no_select);
                holder.checkBox.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case 3:
                if (park.isSelect()) {
                    //选中 绿色+白色
                    holder.checkBox.setBackgroundResource(R.drawable.icon_selected);
                    holder.checkBox.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    //未选中 灰色+灰色
                    holder.checkBox.setBackgroundResource(R.drawable.icon_select);
                    holder.checkBox.setTextColor(context.getResources().getColor(R.color.text_color));
                }
                break;
        }
        //设置展位
        holder.checkBox.setText(park.parkName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemsClickListener != null) {
                    onItemsClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }

    public class PosVh extends RecyclerView.ViewHolder {
        TextView checkBox;

        public PosVh(View itemView) {
            super(itemView);
            checkBox = (TextView) itemView.findViewById(R.id.item_time);
        }
    }
}
