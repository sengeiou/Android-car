package com.tgf.kcwc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.view.EmojiTextView;

import java.util.List;

/**
 * /**
 * Author：Jenny
 * Date:2016/12/8 17:25
 * E-mail：fishloveqin@gmail.com
 * 对BaseAdapter进行包装，主要是实现View缓存
 */

public abstract class WrapAdapter<T> extends BaseAdapter {
    protected LayoutInflater    mInflater;
    protected Context           mContext;

    public List<T> getmDatas() {
        return mDatas;
    }

    protected List<T>           mDatas;
    protected final int         mItemLayoutId;
    private static final String TAG = WrapAdapter.class.getSimpleName();

    public WrapAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    public WrapAdapter(Context context, int itemLayoutId, List<T> mDatas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position));

        return viewHolder.getConvertView();

    }

    public void notifyDataSetChanged(List<T> mDatas) {
        this.mDatas = mDatas;
        super.notifyDataSetChanged();
    }

    public abstract void convert(ViewHolder helper, T item);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }

    public static class ViewHolder {
        private final SparseArray<View> mViews;
        private int                     mPosition;
        private View                    mConvertView;
        private String                  tag;
        private int                     layoutId;

        private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
            this.mPosition = position;
            this.mViews = new SparseArray<View>();
            this.layoutId = layoutId;
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            // setTag
            mConvertView.setTag(this);
        }

        /**
         * 拿到一个ViewHolder对象
         *
         * @param context
         * @param convertView
         * @param parent
         * @param layoutId
         * @param position
         * @return
         */
        public static ViewHolder get(Context context, View convertView, ViewGroup parent,
                                     int layoutId, int position) {
            if (convertView == null || layoutId != ((ViewHolder) convertView.getTag()).layoutId) {
                return new ViewHolder(context, parent, layoutId, position);
            }
            ViewHolder holder=(ViewHolder) convertView.getTag();
            holder.mPosition=position;
            return  holder;
        }

        public View getConvertView() {
            return mConvertView;
        }

        /**
         * 通过控件的Id获取对于的控件，如果没有则加入views
         *
         * @param viewId
         * @return
         */
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 为TextView设置字符串
         *
         * @param viewId
         * @param text
         * @return
         */
        public ViewHolder setText(int viewId, String text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }
        /**
         * 为TextView设置字符串
         *
         * @param viewId
         * @param text
         * @return
         */
        public ViewHolder setEmojiText(int viewId, String text) {
            EmojiTextView view = getView(viewId);
            view.setEmojiToTextView(text);
            return this;
        }

        /**
         * 为TextView设置字符串
         *
         * @param viewId
         * @param text
         * @return
         */
        public ViewHolder setText(int viewId, Spanned text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @param drawableId
         * @return
         */
        public ViewHolder setImageResource(int viewId, int drawableId) {
            ImageView view = getView(viewId);
            view.setImageResource(drawableId);

            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @param bm
         * @return
         */
        public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bm);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @param url
         * @return
         */
        public ViewHolder setImageByUrl(int viewId, String url) {
            try {
                ((SimpleDraweeView) getView(viewId)).setImageURI(Uri.parse(url));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * 为SimpleDraweeView设置URL图片
         *
         * @param viewId
         * @param url
         * @return
         */
        public ViewHolder setSimpleDraweeViewURL(int viewId, String url) {
            SimpleDraweeView view = getView(viewId);
            view.setImageURI(Uri.parse(url));
            return this;
        }

        public int getPosition() {
            return mPosition;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }
}
