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
 * @anthor noti
 * @time 2017/8/29
 * @describle
 */
public class BoothAdapter extends RecyclerView.Adapter<BoothAdapter.PavilionVh> {
    public ArrayList<ExhibitionIntervalModel.Booths> list;
    private Context context;
    OnItemsClickListener onItemsClickListener;

    public interface OnItemsClickListener{
        void onItemClick(int position);
    }

    public void setOnItemsClickListener(OnItemsClickListener onItemsClickListener) {
        this.onItemsClickListener = onItemsClickListener;
    }
    public BoothAdapter(Context context, ArrayList<ExhibitionIntervalModel.Booths> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public PavilionVh onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.date_item,parent,false);
        PavilionVh pavilionVh = new PavilionVh(view);
        return pavilionVh;
    }

    @Override
    public void onBindViewHolder(PavilionVh holder, final int position) {
        ExhibitionIntervalModel.Booths booths = list.get(position);
        if (booths.isSelect) {//选中
            holder.line.setBackgroundColor(context.getResources().getColor(R.color.style_bg1));
            holder.checkBox.setTextColor(context.getResources().getColor(R.color.style_bg1));
        } else {
            holder.line.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.checkBox.setTextColor(context.getResources().getColor(R.color.text_color));
        }
        holder.checkBox.setText(booths.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemsClickListener != null){
                    onItemsClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 :list.size();
    }

    public class PavilionVh extends RecyclerView.ViewHolder{
        TextView checkBox;
        View line;
        public PavilionVh(View itemView) {
            super(itemView);
            checkBox = (TextView) itemView.findViewById(R.id.item_date);
            line = itemView.findViewById(R.id.item_line);
        }
    }
}
