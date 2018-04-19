package com.tgf.kcwc.me;

import android.net.Uri;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.UserRideDataModel;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.NestedListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/17
 * E-mail:fishloveqin@gmail.com
 */

public class UserRideFragment extends BaseFragment implements UserDataView<UserRideDataModel> {

    protected View rootView;
    protected GridView gridView;
    protected View split;
    protected TextView bestDistanceTv;
    protected RelativeLayout bestMileLayout;
    protected TextView bestTimeTv;
    protected RelativeLayout bestTimeLayout;
    protected NestedListView list;
    private UserDataPresenter mPresenter;
    private List<DataItem> gridItems;
    private WrapAdapter<DataItem> gridAdapter;

    private WrapAdapter<UserRideDataModel.RideDataBean> listAdapter;
    private List<UserRideDataModel.RideDataBean> mDatas;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_ride;
    }

    @Override
    protected void initView() {
        int userId = getActivity().getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getRideDataById(userId + "", "");
        gridView = findView(R.id.gridView);
        split = findView(R.id.split);
        bestDistanceTv = findView(R.id.bestDistanceTv);
        bestMileLayout = findView(R.id.bestMileLayout);
        bestTimeTv = findView(R.id.bestTimeTv);
        bestTimeLayout = findView(R.id.bestTimeLayout);
        list = findView(R.id.list);
        mDatas = new ArrayList<UserRideDataModel.RideDataBean>();
        initGridViewData();
        loadData();
    }

    @Override
    public void showDatas(UserRideDataModel model) {


        //里程、时长、次数
        UserRideDataModel.RideBaseBean rideBean = model.data;
        gridItems.get(0).content = NumFormatUtil.getFmtTwoNum((rideBean.mileage / 1000)) + "";
        gridItems.get(1).content = rideBean.duration + "";
        gridItems.get(2).content = rideBean.time + "";
        bestDistanceTv.setText(model.data.max + "");
        bestTimeTv.setText(model.data.max_duration);
        gridAdapter.notifyDataSetChanged();

        mDatas.clear();
        mDatas.addAll(model.list);

        listAdapter.notifyDataSetChanged();


    }

    private void initGridViewData() {

        gridItems = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.ride_infos);

        for (String s : arrays) {

            DataItem dataItem = new DataItem();
            dataItem.title = s;
            dataItem.content = "122";
            gridItems.add(dataItem);
        }

        gridAdapter = new WrapAdapter<DataItem>(mContext, R.layout.track_item2, gridItems) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                titleTv.setText(item.title);
                titleTv.setTextColor(mRes.getColor(R.color.text_color2));
                titleTv.setTextSize(12);
                TextView contentTv = helper.getView(R.id.content);
                contentTv.setText(item.content + "");
                contentTv.setTextSize(21);
            }
        };

        gridView.setAdapter(gridAdapter);

    }

    private void loadData() {


        listAdapter = new WrapAdapter<UserRideDataModel.RideDataBean>(mContext, mDatas, R.layout.ride_common_list_item) {

            protected TextView totalMileTv;
            protected TextView journeyTv;
            protected SimpleDraweeView bigHeaderImg;


            @Override
            public void convert(ViewHolder helper, UserRideDataModel.RideDataBean item) {

                bigHeaderImg = helper.getView(R.id.bigHeaderImg);
                journeyTv = helper.getView(R.id.journeyTv);
                totalMileTv = helper.getView(R.id.totalMileTv);

                bigHeaderImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));

                String time = DateFormatUtil.dispActiveTime2(item.start, item.end);
                journeyTv.setText(item.startAdds + "到" + item.endAdds + "(" + time + ")");

                totalMileTv.setText(item.mileage + "km");
            }
        };
        list.setAdapter(listAdapter);

    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }
}