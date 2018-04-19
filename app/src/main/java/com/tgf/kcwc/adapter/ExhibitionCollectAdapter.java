package com.tgf.kcwc.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.SelectExhibitionModel;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;

/**
 * author:10264
 * date:2017/8/10
 * describe:
 */

public class ExhibitionCollectAdapter extends RecyclerView.Adapter<ExhibitionCollectAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SelectExhibitionModel.List> listItems;
    OnItemsClickListener onItemsClickListener;
    private int eventId,exhibitionPosNums;
    private String exhibitionNames,exhibitionCovers;
    boolean isFirst = true;

    public interface OnItemsClickListener {
        void onItemClick(int position, int eventIds,int exhibitionPosNums,String exhibitionNames,String exhibitionCovers);
    }

    public void setOnItemsClickListener(OnItemsClickListener onItemsClickListener) {
        this.onItemsClickListener = onItemsClickListener;
    }

    public ExhibitionCollectAdapter(Context context, ArrayList<SelectExhibitionModel.List> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exhibition_collect_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SelectExhibitionModel.List list = listItems.get(position);

        holder.itemExhibitionImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(list.cover, 144, 144)));
        holder.itemExhibitionName.setText(list.name);
        holder.itemExhibitionAddress.setText(list.address);
        holder.itemInterval.setText(DateFormatUtil.formatTimePlus2(list.startTime) + "-" + DateFormatUtil.formatTimePlus2(list.endTime));
        holder.itemRetainExhibitionPos.setText("剩余展位：" + list.remaining);
        if (isFirst){
            listItems.get(0).setSelect(true);
            isFirst = false;
        }
        if (listItems.get(position).isSelect){
            holder.itemLayerRl.setAlpha((float) 0.3);
        }else {
            holder.itemLayerRl.setAlpha(1);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemsClickListener != null) {
                    if (listItems.get(position).isSelect){
//                        for (int i = 0; i < listItems.size(); i++) {
//                            listItems.get(i).setSelect(false);
//                        }
                        eventId = 0;
                        exhibitionCovers = "";
                        exhibitionNames = "";
                        exhibitionPosNums = -1;
                        listItems.get(position).setSelect(false);
                    }else {
                        for (int i = 0; i < listItems.size(); i++) {
                            listItems.get(i).setSelect(false);
                        }
                        listItems.get(position).setSelect(true);
//                        holder.itemRb.setChecked(true);
                        eventId = list.id;
                        exhibitionNames = list.name;
                        exhibitionCovers = list.cover;
                        exhibitionPosNums = list.remaining;
                    }
                    notifyDataSetChanged();
                    onItemsClickListener.onItemClick(position, eventId,exhibitionPosNums,exhibitionNames,exhibitionCovers);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView itemExhibitionImg;
        TextView itemInterval;
        TextView itemExhibitionName;
        TextView itemExhibitionAddress;
        TextView itemRetainExhibitionPos;
        //我要参展
        TextView itemJoinExhibition;
        //蒙层
        RelativeLayout itemLayerRl;
//        RadioButton itemRb;

        public ViewHolder(View itemView) {
            super(itemView);
            itemExhibitionImg = (SimpleDraweeView) itemView.findViewById(R.id.item_exhibition_img);
            itemInterval = (TextView) itemView.findViewById(R.id.item_interval);
            itemExhibitionName = (TextView) itemView.findViewById(R.id.item_exhibition_name);
            itemExhibitionAddress = (TextView) itemView.findViewById(R.id.item_exhibition_address);
            itemRetainExhibitionPos = (TextView) itemView.findViewById(R.id.item_retain_exhibition_pos);
            itemJoinExhibition = (TextView) itemView.findViewById(R.id.item_join_exhibition);
            itemLayerRl = (RelativeLayout) itemView.findViewById(R.id.item_layer_rl);
//            itemRb = (RadioButton) itemView.findViewById(R.id.item_rb);
        }
    }
}
