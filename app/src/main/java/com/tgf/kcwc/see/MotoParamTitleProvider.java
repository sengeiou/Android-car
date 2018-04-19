package com.tgf.kcwc.see;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.MotoParamTitleItem;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Auther: Scott
 * Date: 2016/12/20 0020
 * E-mail:hekescott@qq.com
 */

public class MotoParamTitleProvider extends ItemViewProvider<MotoParamTitleItem, MotoParamTitleProvider.MotoParamTitleViewHolder> {

    static class MotoParamTitleViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        final TextView title;
        public MotoParamTitleViewHolder(View itemView) {
            super(itemView);
            this.title =(TextView)itemView.findViewById(R.id.motoparam_title_tv);
        }
    }

    @NonNull
    @Override
    protected MotoParamTitleProvider.MotoParamTitleViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        View root = inflater.inflate(R.layout.recyleviewitem_motoparam_title, parent, false);
        return new MotoParamTitleProvider.MotoParamTitleViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull MotoParamTitleProvider.MotoParamTitleViewHolder holder, @NonNull MotoParamTitleItem motoParamTitleItem) {
        holder.title.setText(motoParamTitleItem.title);
    }
}
