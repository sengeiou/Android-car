package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.mvp.model.RideDataModel;
import com.tgf.kcwc.mvp.presenter.SelectRoadLinePresenter;
import com.tgf.kcwc.mvp.view.SelectRoadlLineView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/2 0002
 * E-mail:hekescott@qq.com
 */

public class SelectRoadLineActivity extends BaseActivity implements SelectRoadlLineView {

    private ListView selecLineLv;
    private SelectRoadLinePresenter selectRoadLinePresenter;
    private String token;
    private int formType;
    private List<RideDataModel.RideData> mRideDataList;
    private View rootNobook;
    private TextView noticeTitle;
    private TextView noticeMsg;
    private int mRideId;
    private TextView cancelBtn;
    private TextView yesBtn;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("选择驾途路线");
    }

    @Override
    protected void setUpViews() {
        token = IOUtils.getToken(this);
        selecLineLv = (ListView) findViewById(R.id.selectline_lv);
        formType = getIntent().getIntExtra(Constants.IntentParams.INTENT_TYPE, 0);
        cancelBtn = (TextView) findViewById(R.id.roadline_cancel);
        cancelBtn.setOnClickListener(this);
        yesBtn = (TextView) findViewById(R.id.roadline_yes);
        yesBtn.setOnClickListener(this);
        noticeTitle = (TextView) findViewById(R.id.notice_titleTv);
        noticeMsg = (TextView) findViewById(R.id.notice_msgTv);
        rootNobook = findViewById(R.id.eidtbook_nobookroot);
        selecLineLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mRideDataList!=null&&position<mRideDataList.size()){
                    Intent toEditBook = new Intent(getContext(), EditTripbookActivity.class);
                    toEditBook.putExtra(Constants.IntentParams.ID, mRideDataList.get(position).id);
                    if (formType == 100) {
                        startActivity(toEditBook);
                    } else {
                        setResult(RESULT_OK, toEditBook);
                    }
                }
            }
        });
        View v = LayoutInflater.from(mContext).inflate(R.layout.no_more_data_layout,
                selecLineLv, false);
        selecLineLv.addFooterView(v);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectroadline);
        selectRoadLinePresenter = new SelectRoadLinePresenter();
        selectRoadLinePresenter.attachView(this);
findViewById(R.id.selectline_startnewtv).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectRoadLinePresenter.getRoadLinelist(token);
    }

    @Override
    public void showRoadLines(List<RideDataModel.RideData> rideDataList) {

        mRideDataList = rideDataList;

        if (rideDataList == null || rideDataList.size() != 0) {
            final int size = rideDataList.size();
            selecLineLv.setAdapter(new WrapAdapter<RideDataModel.RideData>(getContext(),
                    R.layout.my_line_common_list_item, rideDataList) {
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
                public void convert(ViewHolder helper, RideDataModel.RideData item) {

                    bigHeaderImg = helper.getView(R.id.bigHeaderImg);
                    bigHeaderImg
                            .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
                    avatar = helper.getView(R.id.avatar);
                    title = helper.getView(R.id.title);
                    text1 = helper.getView(R.id.text1);
                    text1.setText(item.startAdds + "");
                    text2 = helper.getView(R.id.text2);
                    text2.setText(item.mileage + "km");
                    mRecTime = helper.getView(R.id.recTime);
                    mRecTime.setText(item.startTime + "-" + item.endTime);
                    contentLayout1 = helper.getView(R.id.contentLayout1);
                    text3 = helper.getView(R.id.text3);
                    text3.setText(item.endAdds + "");
                    text4 = helper.getView(R.id.text4);
                    text4.setText(item.viaCount + "个途经点");
                    contentLayout2 = helper.getView(R.id.contentLayout2);
                    split = helper.getView(R.id.split);
                    mSpeedTv = helper.getView(R.id.speedMaxTv);
                    mSpeedTv.setText(item.speedMax + "km/h");
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
                    }
//                    else if(item.bookStatus==3){
//                        rideTv.setBackgroundResource(R.drawable.shape_tripriding_bg);
//                        rideTv.setText("待发布");
//                    }
                    else {
                        rideTv.setVisibility(View.GONE);
                    }
                }
            });


        } else {
            rootNobook.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showContinue(RideDataModel rideDataModel) {
//        mRideId = rideId;
//        rootNobook.setVisibility(View.VISIBLE);
//        noticeTitle.setText("检测到您有尚未结束的骑行路线");
//        noticeMsg.setText("继续骑行，还是开始新的路线？");
//        yesBtn.setText("新路线");
//        cancelBtn.setText("继续骑行");
    }

    @Override
    public void showUnStop(RideDataModel rideDataModel) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.roadline_cancel:
//                if(mRideId==0){
//                          finish();
//                }else{
//                    CommonUtils.showToast(getContext(),"继续骑行");
//                }
                break;
            case R.id.selectline_startnewtv:
                Intent   toDringIntent = new Intent(getContext(), DrivingHomeActivity.class);
                toDringIntent.putExtra(Constants.IntentParams.DATA,
                        Constants.IntentParams.RIDE_BOOK_MODULE);
                toDringIntent.putExtra(Constants.IntentParams.ID, -1);
                toDringIntent.putExtra(Constants.IntentParams.DATA2,
                        Constants.IntentParams.ALL_NEWLINE);
                startActivity(toDringIntent);
                break;
            case R.id.roadline_yes:
//                if(mRideId==0) {
//                    CommonUtils.startNewActivity(getContext(), DrivingHomeActivity.class);
//                }else{
//                    CommonUtils.showToast(getContext(),"新路线");
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
