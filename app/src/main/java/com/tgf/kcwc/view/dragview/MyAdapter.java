package com.tgf.kcwc.view.dragview;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.util.URLUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13 0013.
 * E-Mail：543441727@qq.com
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<DataItem> datas;
    private Context mContext;
    private LayoutInflater mLiLayoutInflater;

    public MyAdapter(List<DataItem> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
        this.mLiLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLiLayoutInflater.inflate(R.layout.item_linear, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
        if (datas.size() < 9 && position == datas.size()) {
            holder.img.setImageResource(R.drawable.btn_tianjia);
            holder.delete.setVisibility(View.GONE);
        } else {
            holder.img.setImageURI(Uri.parse(URLUtil.builderImgUrl(datas.get(position).resp.data.path, 360, 360)));
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteClickListener != null) {
                        mDeleteClickListener.delete(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (datas.size() >= 9) {
            return datas == null ? 0 : 9;
        } else {
            return datas == null ? 0 : datas.size() + 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;
        ImageView delete;
        LinearLayout ll_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (SimpleDraweeView) itemView.findViewById(R.id.cover);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
        }
    }

    DeleteClickListener mDeleteClickListener;

    public void setDeleteClickListener(DeleteClickListener mDeleteClickListener) {
        this.mDeleteClickListener = mDeleteClickListener;
    }

    public interface DeleteClickListener {
        void delete(int num);
    }
}
