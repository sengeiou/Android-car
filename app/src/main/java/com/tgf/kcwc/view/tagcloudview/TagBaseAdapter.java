package com.tgf.kcwc.view.tagcloudview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.DrivDetailsBean;

import java.util.List;

/**
 * @author fyales
 * @since 8/26/15.
 */
public class TagBaseAdapter extends BaseAdapter {

    private Context mContext;
    private List<DrivDetailsBean.TopicList> mList;

    public TagBaseAdapter(Context context, List<DrivDetailsBean.TopicList> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DrivDetailsBean.TopicList getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tagview, null);
            holder = new ViewHolder();
            holder.tagBtn = (Button) convertView.findViewById(R.id.tag_btn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final String text = getItem(position).title;
        holder.tagBtn.setText(text);
        return convertView;
    }

    static class ViewHolder {
        Button tagBtn;
    }
}
