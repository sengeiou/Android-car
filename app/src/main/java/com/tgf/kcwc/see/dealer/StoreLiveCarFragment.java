package com.tgf.kcwc.see.dealer;

import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.StoreCarModel;
import com.tgf.kcwc.mvp.presenter.DealerDataPresenter;
import com.tgf.kcwc.mvp.view.DealerDataView;
import com.tgf.kcwc.see.StoreShowCarDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/3/15 15:53
 * E-mail：fishloveqin@gmail.com
 * 店内现车
 */

public class StoreLiveCarFragment extends BaseFragment implements DealerDataView<StoreCarModel> {

    private ListView            mList;

    private DealerDataPresenter mPresenter;

    private String              mOrgId = "";

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_live_car;
    }

    @Override
    protected void initView() {

        mOrgId = getArguments().getString(Constants.IntentParams.ID);

        mList = findView(R.id.list);
        mPresenter = new DealerDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getLiveCars(mOrgId);
    }

    @Override
    public void showData(StoreCarModel storeCarModel) {

        WrapAdapter<StoreCarModel.StoreCarBean> adapter = new WrapAdapter<StoreCarModel.StoreCarBean>(
            mContext, R.layout.common_car_list_item, storeCarModel.list) {

            protected TextView         interiorTv;
            protected TextView         appearanceTv;
            protected LinearLayout     coverLayout;
            protected SimpleDraweeView cover2;
            protected SimpleDraweeView cover1;
            protected TextView         name;
            protected ImageView        couponImg;

            @Override
            public void convert(ViewHolder helper, StoreCarModel.StoreCarBean item) {

                couponImg = helper.getView(R.id.couponImg);
                name = helper.getView(R.id.name);
                cover1 = helper.getView(R.id.cover1);
                cover2 = helper.getView(R.id.cover2);
                coverLayout = helper.getView(R.id.coverLayout);
                appearanceTv = helper.getView(R.id.appearanceTv);
                interiorTv = helper.getView(R.id.interiorTv);

                String tag1 = String.format(mRes.getString(R.string.color_tag1),
                    item.appearanceColorName);
                String tag2 = String.format(mRes.getString(R.string.color_tag2),
                    item.interiorColorName);
                if ("- -".equals(item.appearanceColorName.trim())) {
                    appearanceTv.setVisibility(View.GONE);
                } else {
                    appearanceTv.setVisibility(View.VISIBLE);
                }
                if ("- -".equals(item.interiorColorName.trim())) {
                    interiorTv.setVisibility(View.GONE);
                } else {
                    interiorTv.setVisibility(View.VISIBLE);
                }
                CommonUtils.customDisplayText(mRes, appearanceTv, tag1, R.color.text_color12);
                CommonUtils.customDisplayText(mRes, interiorTv, tag2, R.color.text_color12);
                name.setText(item.carName);

                if (item.benefit == 0) {
                    couponImg.setVisibility(View.GONE);
                } else {
                    couponImg.setVisibility(View.VISIBLE);
                }
                cover1.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.appearanceImg, 270, 203)));
                cover2.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.interiorImg, 270, 203)));
            }
        };

        mList.setAdapter(adapter);
        ViewUtil.setListViewHeightBasedOnChildren2(mList);
        mList.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            StoreCarModel.StoreCarBean bean = (StoreCarModel.StoreCarBean) parent.getAdapter().getItem(position);

            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, bean.id);
            args.put(Constants.IntentParams.DATA3, bean.carName);
            CommonUtils.startNewActivity(mContext, args,
                    StoreShowCarDetailActivity.class);
        }
    };
    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

}
