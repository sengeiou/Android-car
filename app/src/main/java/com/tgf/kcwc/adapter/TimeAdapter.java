package com.tgf.kcwc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.ExhibitionIntervalModel;

import java.util.ArrayList;

/**
 * author:10264
 * date:2017/8/10
 * describe:
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ExhibitionIntervalModel.TimeSlots.Slots> listItems;
    private int timeSlotsId;
    private String timesStr = "";
    OnItemsClickListener onItemsClickListener;
    public interface OnItemsClickListener{
        void onItemClick(int position,String timesStr,int timeSlotsId);
    }

    public void setOnItemsClickListener(OnItemsClickListener onItemsClickListener) {
        this.onItemsClickListener = onItemsClickListener;
    }

    public TimeAdapter(Context context, ArrayList<ExhibitionIntervalModel.TimeSlots.Slots> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ExhibitionIntervalModel.TimeSlots.Slots slots = listItems.get(position);
        //1表示已满
        if (slots.isFull == 1){
            timesStr = "";
            timeSlotsId = 0;
            //锁定=红背景+白字
            holder.checkBox.setTextColor(context.getResources().getColor(R.color.white));
            holder.checkBox.setBackgroundResource(R.drawable.shape_red_bg);
        }else {
            if (slots.isSelect){//选中
                //选中=绿背景+白字
                holder.checkBox.setTextColor(context.getResources().getColor(R.color.white));
                holder.checkBox.setBackgroundResource(R.drawable.shape_green_bg);
            }else {
                //可选=灰背景+灰字
                holder.checkBox.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.checkBox.setBackgroundResource(R.drawable.shape_gray_bg);
            }
        }
        holder.checkBox.setText(slots.timeFormat);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemsClickListener != null){
                    onItemsClickListener.onItemClick(position,timesStr,timeSlotsId);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (TextView) itemView.findViewById(R.id.item_time);
        }
    }
}
