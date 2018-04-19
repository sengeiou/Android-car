package com.tgf.kcwc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.Brand;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2016/12/29 0029
 * E-mail:hekescott@qq.com
 */

public class ExhibitionPlaceNewCarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int   VIEW_TYPE_BRAND = 1;
    public final int   VIEW_TYPE_MODEL = 2;
    private List<Brand> brands;
    private Context     context;
    private LayoutInflater mLayoutInflater;
    public ExhibitionPlaceNewCarAdapter(Context context, List<Brand> brands) {
        this.brands = brands;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {

        for (int i = 0; i < brands.size(); i++) {
            if (position == 0) {
                return VIEW_TYPE_BRAND;
            }
            position--;
            for (int j = 0; j < brands.get(i).beautlist.size(); j++) {
                if (position == 0) {
                    return VIEW_TYPE_MODEL;
                } else {
                    position--;
                }
            }

        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == VIEW_TYPE_BRAND){
            viewHolder = new BrandViewHolder(mLayoutInflater.inflate(R.layout.recyleview_item_brandtitle,parent,false));
        }else if (viewType == VIEW_TYPE_MODEL){
            viewHolder = new BrandViewHolder(mLayoutInflater.inflate(R.layout.recyleview_item_beauty,parent,false));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ModelViewHolder){

        }else if (holder instanceof BrandViewHolder){

        }

    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {

        public BrandViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        public ModelViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < brands.size(); i++) {
            count += brands.get(i).beautlist.size();
        }
        return brands.size() + count;
    }
}
