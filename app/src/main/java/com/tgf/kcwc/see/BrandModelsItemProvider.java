package com.tgf.kcwc.see;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.mvp.model.Motorshow;
import com.tgf.kcwc.mvp.model.MotorshowModel;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Author：Jenny
 * Date:2016/12/26 20:50
 * E-mail：fishloveqin@gmail.com
 */
public class BrandModelsItemProvider extends
                                     ItemViewProvider<MotorshowModel.BrandBean, ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                            @NonNull ViewGroup parent) {
        View convertView = null;
        mContext = parent.getContext();
        mParent = parent;
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
            R.layout.motorshow_list_item, getPosition());
        return viewHolder;
    }

    private Context   mContext;
    private ViewGroup mParent;

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder,
                                    @NonNull MotorshowModel.BrandBean item) {

        holder.setText(R.id.brandName, item.brandName);
        SimpleDraweeView simpleDraweeView = holder.getView(R.id.img);
        simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl750(item.brandLogo)));

        ListView list = holder.getView(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(parent, view,
                        parent.getAdapter().getItem(position), position);
                }
            }
        });
        list.setAdapter(
            new WrapAdapter<Motorshow>(mContext, R.layout.motorshow_content_item, item.motorshows) {
                @Override
                public void convert(ViewHolder helper, final Motorshow item) {

                    helper.setText(R.id.carName, item.seriesName+item.productName);
                    GridView gridView = helper.getView(R.id.grid);
                    List<String> imgList = new ArrayList();
                    imgList.add(item.imgList.appearanceImg);
                    imgList.add(item.imgList.interiorImg);
                    gridView.setAdapter(
                        new WrapAdapter<String>(mContext, imgList, R.layout.new_car_grid_item) {
                            @Override
                            public void convert(ViewHolder helper, String item) {

                                SimpleDraweeView draweeView = helper.getView(R.id.img);
                                draweeView
                                    .setImageURI(Uri.parse(URLUtil.builderImgUrl750(item)));

                            }
                        });
                    ViewUtil.setListViewHeightBasedOnChildren(gridView, 3);
                    gridView.setClickable(false);
                    gridView.setPressed(false);
                    gridView.setEnabled(false);

                }
            });
        ViewUtil.setListViewHeightBasedOnChildren2(list);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private OnItemClickListener mOnItemClickListener;
}