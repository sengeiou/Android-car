package com.tgf.kcwc.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/14
 * @describle
 */
public class ImgAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> imgList = new ArrayList<>();

    public OnItemClickListener onItemClickListener;
    public OnDeleteClickListener onDeleteClickListener;
    public OnAddClickListener onAddClickListener;

    @Override
    public int getCount() {
        return imgList.size() == 0 ? 0 :imgList.size();
    }

    @Override
    public Object getItem(int i) {
        return imgList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ImgVh imgVh;
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.img_item, viewGroup, false);
            imgVh = new ImgVh();
            imgVh.imgSdv = view.findViewById(R.id.imgSdv);
            imgVh.itemX = view.findViewById(R.id.itemX);
            view.setTag(imgVh);
        }else {
            imgVh = (ImgVh) view.getTag();
        }

        if (i == 0) {
            //隐藏删除按钮
            imgVh.itemX.setVisibility(View.GONE);
            imgVh.imgSdv.setBackgroundResource(Integer.parseInt(imgList.get(i)));
            imgVh.imgSdv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onAddClickListener) {
                        onAddClickListener.onAddClick(i);
                    }
                }
            });
        } else {
            //显示删除按钮
            imgVh.itemX.setVisibility(View.VISIBLE);
            imgVh.imgSdv.setImageURI(Uri.parse(URLUtil.builderImgUrl(imgList.get(i))));
            imgVh.itemX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onDeleteClickListener) {
                        onDeleteClickListener.onDeleteClick(i);
                    }
                }
            });
        }
        return view;
    }

    //图片点击
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //图片单张删除
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    //图片多张选择
    public interface OnAddClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public ImgAdapter(Context context, ArrayList<String> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    public class ImgVh {
        SimpleDraweeView imgSdv;
        ImageView itemX;
    }
}
