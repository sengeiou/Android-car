package com.tgf.kcwc.see;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.SeriesByBrandModel;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jenny on 2017/8/7.
 */

public class BaseBrandsFragment extends BaseFragment {

    private SeriesByBrandModel mModel;
    private List<SeriesByBrandModel.SeriesData> mDatas = new ArrayList<SeriesByBrandModel.SeriesData>();

    public BaseBrandsFragment(SeriesByBrandModel model, int type) {
        this.mModel = model;
        this.type = type;
    }
    public BaseBrandsFragment(){}
    private int type = -1;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_series_bybrand;
    }

    @Override
    protected void initView() {
        mLogo = findView(R.id.brandLogoImg);
        mTextView = findView(R.id.brandName);

        mTextView.setText(mModel.brand.name);
        mLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(mModel.brand.logo, 144, 144)));
        mList = findView(R.id.list);
        mDatas.clear();
        switch (type) {

            case Constants.SeriesStatus.ON:
                mDatas.addAll(mModel.listIn);
                break;

            case Constants.SeriesStatus.PRE:
                mDatas.addAll(mModel.listPre);
                break;

            case Constants.SeriesStatus.STOP:
                mDatas.addAll(mModel.listStop);
                break;
        }
        WrapAdapter<SeriesByBrandModel.SeriesData> adapter = new WrapAdapter<SeriesByBrandModel.SeriesData>(mContext, mDatas, R.layout.series_first_list_item) {
            @Override
            public void convert(ViewHolder holder, SeriesByBrandModel.SeriesData item) {


                TextView factoryNameTv = holder.getView(R.id.factoryName);
                factoryNameTv.setText(item.factory.name);


                ListView list = holder.getView(R.id.list);
                list.setAdapter(new WrapAdapter<SeriesByBrandModel.SeriesData.SeriesBean>(mContext, item.series, R.layout.series_list_item) {
                    @Override
                    public void convert(ViewHolder helper, SeriesByBrandModel.SeriesData.SeriesBean item) {

                        SimpleDraweeView img = helper.getView(R.id.img);
                        img.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.logo, 270, 203)));

                        TextView price = helper.getView(R.id.price);
                        price.setText(item.price + "");

                        TextView seriesNameTv = helper.getView(R.id.seriesName);
                        seriesNameTv.setText(item.name);

                    }
                });

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        SeriesByBrandModel.SeriesData.SeriesBean bean = (SeriesByBrandModel.SeriesData.SeriesBean) parent.getAdapter().getItem(position);
                        intent.putExtra(Constants.IntentParams.ID, bean.id);
                        intent.putExtra(Constants.IntentParams.NAME, bean.name);
                        intent.setClass(mContext, SeriesDetailActivity.class);
                        startActivity(intent);

                    }
                });
                ViewUtil.setListViewHeightBasedOnChildren2(list);
            }
        };
        mList.setAdapter(adapter);
        ViewUtil.setListViewHeightBasedOnChildren2(mList);
    }

    private SimpleDraweeView mLogo;
    private TextView mTextView;

    private ListView mList;

}
