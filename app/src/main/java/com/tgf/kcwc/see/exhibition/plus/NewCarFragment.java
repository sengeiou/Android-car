package com.tgf.kcwc.see.exhibition.plus;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.model.NewCarBean;
import com.tgf.kcwc.see.NewCarGalleryActivity;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public class NewCarFragment extends BaseFragment {
    private ListView listView;
    private WrapAdapter<NewCarBean> mAdapter;
    private ArrayList<NewCarBean> newCarList = new ArrayList<>();

    public NewCarFragment() {
    }

    public NewCarFragment(ArrayList<NewCarBean> newCarList) {
        this.newCarList.addAll(newCarList);
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_car;
    }

    @Override
    protected void initView() {
        listView = findView(R.id.list_view);
        mAdapter = new WrapAdapter<NewCarBean>(getContext(), newCarList, R.layout.new_car_exhibitplace_item) {
            @Override
            public void convert(final ViewHolder holder, NewCarBean moto) {
                holder.setText(R.id.name, moto.brandName);
                SimpleDraweeView draweeView = holder.getView(R.id.img);
                draweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(moto.brandLogo, 144, 144)));
                holder.setText(R.id.star, moto.star);
                holder.setText(R.id.releaseTime, "发布时间: " + DateFormatUtil.formatTime1(moto.releaseTime));
                holder.setText(R.id.carname, moto.productName + "");
                holder.setText(R.id.carprice, "￥" + moto.brandPrice+"万");
                GridView gridView = holder.getView(R.id.grid);
                gridView.setAdapter(new WrapAdapter<Image>(holder.getConvertView().getContext(), moto.imgs,
                        R.layout.new_car_grid_item) {
                    @Override
                    public void convert(ViewHolder helper, Image item) {

                        SimpleDraweeView draweeView = helper.getView(R.id.img);
                        draweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.link, 270, 203)));

                    }
                });
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent toNecarAct = new Intent(mContext, NewCarGalleryActivity.class);
                        toNecarAct.putExtra(Constants.IntentParams.ID, newCarList.get(holder.getPosition()).id);
                        startActivity(toNecarAct);
                    }
                });
                ViewUtil.setListViewHeightBasedOnChildren(gridView, 3);
            }
        };
        listView.setAdapter(mAdapter);
//        ViewUtil.setListViewHeightBasedOnChildren(listView);

    }
}
