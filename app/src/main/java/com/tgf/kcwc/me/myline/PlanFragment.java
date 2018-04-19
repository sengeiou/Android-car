package com.tgf.kcwc.me.myline;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.RideDataModel;
import com.tgf.kcwc.mvp.presenter.RideDataPresenter;
import com.tgf.kcwc.mvp.view.RideDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/5/3
 * E-mail:fishloveqin@gmail.com
 * 规划
 */

public class PlanFragment extends BaseFragment implements RideDataView<RideDataModel> {

    protected ListView list;
    private RideDataPresenter mPresenter;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_common_layout;
    }

    @Override
    protected void initView() {

        list = findView(R.id.list);
        initEmptyView();
        mPresenter = new RideDataPresenter();
        mPresenter.attachView(this);

    }

    @Override
    public void showDatas(RideDataModel rideDataModel) {

        bindData(rideDataModel);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    private int page = 1;
    private int pageSize = 10;

    @Override
    public void onResume() {
        super.onResume();
        String token = IOUtils.getToken(mContext);
        mPresenter.loadPlanDatas(page + "", pageSize + "", token);
    }

    private boolean isFirst = true;

    private void bindData(final RideDataModel rideDataModel) {
        final int size = rideDataModel.list.size();

        if (size == 0) {

            list.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            TextView msgTv = (TextView) mEmptyLayout.findViewById(R.id.msgTv);
            msgTv.setText("亲，您暂无路线规划哦!");
            return;
        } else {
            list.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }
        WrapAdapter<RideDataModel.RideData> adapter = new WrapAdapter<RideDataModel.RideData>(
                mContext, rideDataModel.list, R.layout.my_line_common_list_item) {

            protected View split;
            protected RelativeLayout contentLayout2;
            protected TextView text4;
            protected TextView text3;
            protected RelativeLayout contentLayout1;
            protected TextView text2;
            protected TextView text1;
            protected TextView title;
            protected SimpleDraweeView avatar;
            protected SimpleDraweeView bigHeaderImg;
            protected TextView mRecTime;
            private TextView mSpeedTv;

            @Override
            public void convert(WrapAdapter.ViewHolder helper, RideDataModel.RideData item) {

                bigHeaderImg = helper.getView(R.id.bigHeaderImg);
                bigHeaderImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
                avatar = helper.getView(R.id.avatar);
                title = helper.getView(R.id.title);
                title.setVisibility(View.GONE);
                title.setText(item.title + "");
                text1 = helper.getView(R.id.text1);
                text1.setText(item.startAdds + "");
                text2 = helper.getView(R.id.text2);
                text2.setText(item.mileage + "km");
                mRecTime = helper.getView(R.id.recTime);
                //mRecTime.setText(item.startTime + "-" + item.endTime);
                contentLayout1 = helper.getView(R.id.contentLayout1);
                text3 = helper.getView(R.id.text3);
                text3.setText(item.endAdds + "");
                text4 = helper.getView(R.id.text4);
                text4.setText(item.viaCount + "个途经点");
                contentLayout2 = helper.getView(R.id.contentLayout2);
                split = helper.getView(R.id.split);
                mSpeedTv = helper.getView(R.id.speedMaxTv);
                //mSpeedTv.setText(item.speedMax + "km/h");
                if (helper.getPosition() == (size - 1)) {

                    split.setVisibility(View.GONE);
                } else {
                    split.setVisibility(View.VISIBLE);
                }
                TextView rideTv = helper.getView(R.id.rideline_statustv);
                if (item.bookStatus == 2) {
                    rideTv.setVisibility(View.VISIBLE);
                    if (item.status == 1) {
                        //                        表示路线已结束，路书保存草稿，显示为编辑中
                        rideTv.setBackgroundResource(R.drawable.shape_tripeding_bg);
                        rideTv.setText("编辑中");
                    } else if (item.status == 2) {
                        // 表示路线未结束，路书保存草稿，即描述过部分节点，显示为进行中；
                        rideTv.setBackgroundResource(R.drawable.shape_tripriding_bg);
                        rideTv.setText("进行中");
                    }
                } else {
                    rideTv.setVisibility(View.GONE);
                }
            }
        };
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RideDataModel.RideData data = (RideDataModel.RideData) parent.getAdapter()
                        .getItem(position);

                if (data == null) {
                    return;
                }
                MyLineMainActivity activity = (MyLineMainActivity) getActivity();
                String type = activity.getType();
                if (!type.equals("")) {
                    KPlayCarApp.putValue(Constants.PathBack.SPONSORIS, true);
                    KPlayCarApp.putValue(Constants.PathBack.SPONSOR, data.id + "");
                    KPlayCarApp.putValue(Constants.PathBack.ROADBOOKTITLE, data.title + "");
                    KPlayCarApp.putValue(Constants.PathBack.ROADBOOKTYPE, "2");
                    getActivity().finish();
                } else {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();

                    args.put(Constants.IntentParams.ID, data.id);
                    CommonUtils.startNewActivity(mContext, args, RideLinePreviewActivity.class);
                }

            }
        });

        if (size != 0 && isFirst) {

            View v = LayoutInflater.from(mContext).inflate(R.layout.no_more_data_layout, list,
                    false);
            list.addFooterView(v);
            isFirst = false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
