package com.tgf.kcwc.see;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.MotoParamInfoItem;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Auther: Scott
 * Date: 2016/12/20 0020
 * E-mail:hekescott@qq.com
 */

public class MotoParamItemProvider extends ItemViewProvider<MotoParamInfoItem, MotoParamItemProvider.MotoParamItemViewHolder> {

    static class MotoParamItemViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        final TextView name;
        @NonNull
        final TextView value;
        public MotoParamItemViewHolder(View itemView) {
            super(itemView);
            this.name =(TextView)itemView.findViewById(R.id.motoparam_name_tv);
            this.value =(TextView)itemView.findViewById(R.id.motoparam_value_tv);
        }
    }

    @NonNull
    @Override
    protected MotoParamItemViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        View root = inflater.inflate(R.layout.recyleviewitem_motoparam_info, parent, false);
        return new MotoParamItemViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull MotoParamItemViewHolder holder, @NonNull MotoParamInfoItem motoParamTitleItem) {
            holder.name.setText(motoParamTitleItem.key);
            holder.value.setText(motoParamTitleItem.value);
    }

}
