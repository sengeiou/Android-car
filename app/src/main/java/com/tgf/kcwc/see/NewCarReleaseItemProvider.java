package com.tgf.kcwc.see;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.model.NewCarBean;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.countdown.CountdownView;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Author：Jenny
 * Date:2016/12/26 20:50
 * E-mail：fishloveqin@gmail.com
 */
public class NewCarReleaseItemProvider extends ItemViewProvider<NewCarBean, ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                            @NonNull ViewGroup parent) {
        View convertView = null;
        ViewHolder viewHolder = ViewHolder.get(parent.getContext(), convertView, parent,
                R.layout.new_car_release_item, getPosition());
        return viewHolder;
    }


    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final NewCarBean moto) {


        View view = holder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    mListener.onClickListener(moto, holder.getPosition());
                }
            }
        });

        holder.setText(R.id.name, moto.brandName);
        SimpleDraweeView draweeView = holder.getView(R.id.img);
        draweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(moto.brandLogo, 144, 144)));

        StringBuilder sb = new StringBuilder();
        holder.setText(R.id.releaseTime, "发布时间: " + DateFormatUtil.formatTime1(moto.releaseTime));
        TextView starText = holder.getView(R.id.star);
        if (TextUtils.isEmpty(moto.star)) {
            starText.setVisibility(View.GONE);
        } else {
            starText.setVisibility(View.VISIBLE);
        }
        sb.append("到场明星:   ").append(moto.star);
        CommonUtils.customDisplayText(holder.getConvertView().getResources(), starText,
                sb.toString(), R.color.text_color13);

        holder.setText(R.id.carName, moto.seriesName + moto.carName + "");
        holder.setText(R.id.price, "指导价: " + moto.guidePrice + "万");

        RelativeLayout gridLayout = holder.getView(R.id.gridLayout);
        RelativeLayout specLayout = holder.getView(R.id.specLayout);
        CountdownView setTimeText = holder.getView(R.id.setTimeText);
        RelativeLayout countLayout = holder.getView(R.id.countLayout);
        TextView launchingText = holder.getView(R.id.launchingText);
        long deltaTime = DateFormatUtil.getTime(moto.releaseTime)
                - DateFormatUtil.getTime(moto.currentTime);

        boolean flag = false; //deltaTime > 0 ? true : false;
        int imgSize = moto.imgs.size();
        int type = 1;
        if (imgSize == 4) {
            flag = true;
        }

        boolean isExp = deltaTime > 0 ? true : false;
        //        if (isExp) {
        //            gridLayout.setVisibility(View.GONE);
        //            specLayout.setVisibility(View.VISIBLE);
        //            if (flag) {
        //                countLayout.setVisibility(View.VISIBLE);
        //                launchingText.setVisibility(View.GONE);
        //                setTimeText.start(deltaTime);
        //            } else {
        //                countLayout.setVisibility(View.GONE);
        //                launchingText.setVisibility(View.VISIBLE);
        //            }
        //            return;
        //        } else {
        //            gridLayout.setVisibility(View.VISIBLE);
        //            specLayout.setVisibility(View.GONE);
        //        }

        if (isExp) {
            specLayout.setVisibility(View.VISIBLE);
            countLayout.setVisibility(View.VISIBLE);
            launchingText.setVisibility(View.GONE);
            setTimeText.start(deltaTime);
            gridLayout.setVisibility(View.GONE);
            return;
        } else {
            countLayout.setVisibility(View.GONE);

            if (flag) {
                gridLayout.setVisibility(View.VISIBLE);
                launchingText.setVisibility(View.GONE);
                specLayout.setVisibility(View.GONE);
            } else {
                specLayout.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.GONE);
                launchingText.setVisibility(View.VISIBLE);
            }
        }

        GridView gridView = holder.getView(R.id.grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(parent, view, moto, position);
                }

            }
        });
        gridView.setAdapter(new WrapAdapter<Image>(holder.getConvertView().getContext(), moto.imgs,
                R.layout.new_car_grid_item) {
            @Override
            public void convert(ViewHolder helper, Image item) {

                SimpleDraweeView draweeView = helper.getView(R.id.img);
                draweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.link, 270, 203)));

            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(gridView, 3);
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


    public interface OnClickListener<T> {
        void onClickListener(T t, int position);
    }

    private OnClickListener mListener = null;

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
    }
}