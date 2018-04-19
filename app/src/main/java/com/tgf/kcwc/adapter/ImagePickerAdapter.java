package com.tgf.kcwc.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.tgf.kcwc.R;
import com.tgf.kcwc.util.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 
 */
public class ImagePickerAdapter extends
                                RecyclerView.Adapter<ImagePickerAdapter.SelectedPicViewHolder> {
    private int                             maxImgCount;
    private Context                         mContext;
    private List<ImageItem>                 mData,mSourceData;
    private LayoutInflater                  mInflater;
    private OnRecyclerViewItemClickListener listener;
    //private boolean                         isAdded;    //是否额外添加了最后一个图片
    private static final int                IMAGE_ITEM_ADD = -1;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setImages(List<ImageItem> data) {
        mData = new ArrayList<>(data);
        mSourceData=data;
        //        if (getItemCount() < maxImgCount) {
        //            mData.add(new ImageItem());
        //           // isAdded = true;
        //        } else {
        //            isAdded = false;
        //        }
        notifyDataSetChanged();
    }

    public List<ImageItem> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        //        if (isAdded)
        //            return new ArrayList<>(mData.subList(0, mData.size() - 1));
        //        else
        return mData;
    }

    public ImagePickerAdapter(Context mContext, List<ImageItem> data, int maxImgCount) {
        this.mContext = mContext;
        this.maxImgCount = maxImgCount;
        this.mInflater = LayoutInflater.from(mContext);
        setImages(data);
    }

    @Override
    public SelectedPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedPicViewHolder(
            mInflater.inflate(R.layout.list_item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedPicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedPicViewHolder extends RecyclerView.ViewHolder
                                       implements View.OnClickListener {

        private ImageView iv_img, deleteBtn;
        private int       clickPosition;

        public SelectedPicViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            deleteBtn = (ImageView) itemView.findViewById(R.id.deleteBtn);
        }

        public void bind(final int position) {
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mData.remove(position);
                    mSourceData.remove(position);
                    notifyDataSetChanged();
                }
            });
            //根据条目位置设置图片
            ImageItem item = mData.get(position);
            //            if (isAdded && position == getItemCount() - 1) {
            //                iv_img.setImageResource(R.drawable.icon_add);
            //                clickPosition = IMAGE_ITEM_ADD;
            //            } else {
            if (item != null && iv_img != null) {
                ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext,
                    item.path, iv_img, BitmapUtils.dp2px(mContext, 120),
                    BitmapUtils.dp2px(mContext, 120));
            }

            clickPosition = position;
            // }
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(v, clickPosition);
        }
    }
}