package com.tgf.kcwc.see;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.util.ViewUtil;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Author：Jenny
 * Date:2016/12/26 20:50
 * E-mail：fishloveqin@gmail.com
 */
public class BrandModelsContentItemProvider extends ItemViewProvider<CarBean, ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                            @NonNull ViewGroup parent) {
        View convertView = null;
        ViewHolder viewHolder = ViewHolder.get(parent.getContext(), convertView, parent,
            R.layout.new_car_content_item, getPosition());
        this.parent = parent;
        return viewHolder;
    }

    private ViewGroup parent;

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull CarBean m) {

        holder.setText(R.id.name, m.carName);
        holder.getView(R.id.price).setVisibility(View.GONE);

        GridView gridView = holder.getView(R.id.grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(parent, view,
                        parent.getAdapter().getItem(position), position);
                }

            }
        });
        gridView.setAdapter(new WrapAdapter<CarBean.Img>(holder.getConvertView().getContext(),
            m.imgList, R.layout.new_car_grid_item) {
            @Override
            public void convert(ViewHolder helper, CarBean.Img item) {

                SimpleDraweeView draweeView = helper.getView(R.id.img);
                draweeView.setImageURI(Uri.parse(item.link));

            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(gridView,3);
    }

    public interface CountdownListener {

        public void setTime(TextView view, ViewHolder viewHolder, int position);
    }

    private CountdownListener mSetTimeListener;

    public void setCountdownListener(CountdownListener listener) {
        this.mSetTimeListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private OnItemClickListener mOnItemClickListener;
}