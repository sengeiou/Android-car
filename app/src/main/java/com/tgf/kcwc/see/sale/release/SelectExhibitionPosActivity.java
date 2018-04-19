package com.tgf.kcwc.see.sale.release;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.BoothAdapter;
import com.tgf.kcwc.adapter.DateAdapter;
import com.tgf.kcwc.adapter.ExhibitionPosAdapter;
import com.tgf.kcwc.adapter.TimeAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ChangeExhibitionModel;
import com.tgf.kcwc.mvp.model.ExhibitionApplyModel;
import com.tgf.kcwc.mvp.model.ExhibitionIntervalModel;
import com.tgf.kcwc.mvp.model.ExhibitionIntervalPresenter;
import com.tgf.kcwc.mvp.model.ExhibitionIntervalView;
import com.tgf.kcwc.mvp.model.GetApplyModel;
import com.tgf.kcwc.mvp.model.SelectExhibitionPosModel;
import com.tgf.kcwc.mvp.presenter.ChangeExhibitionPresenter;
import com.tgf.kcwc.mvp.presenter.ExhibitionApplyPresenter;
import com.tgf.kcwc.mvp.presenter.GetApplyPresenter;
import com.tgf.kcwc.mvp.presenter.SelectExhibitionPosPresenter;
import com.tgf.kcwc.mvp.view.ChangeExhibitionView;
import com.tgf.kcwc.mvp.view.ExhibitionApplyView;
import com.tgf.kcwc.mvp.view.GetApplyView;
import com.tgf.kcwc.mvp.view.SelectExhibitionPosView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataFormatUtil;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.BannerConfig;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle 二手车主参展申请——选择展位时段
 */
public class SelectExhibitionPosActivity extends BaseActivity implements ExhibitionIntervalView, SelectExhibitionPosView, ExhibitionApplyView, GetApplyView,ChangeExhibitionView {
    //状态 提交审核材料+展位申请+等待审核
    ImageView commitIv, applyIv, waitIv;
    //展会名
    TextView exhibitionNameTv;
    //展区名
    RecyclerView boothRv;
    BoothAdapter boothAdapter;
    //    DateAdapter.OnItemsClickListener dateListener;
    ArrayList<ExhibitionIntervalModel.Booths> boothList = new ArrayList<>();
    //展会轮播图
    Banner banner;
    //日期列表
    RecyclerView dateRv;
    DateAdapter dateAdapter;
    DateAdapter.OnItemsClickListener dateListener;
    ArrayList<ExhibitionIntervalModel.TimeSlots> dateList = new ArrayList<>();
    //时间列表
    RecyclerView timeRv;
    TimeAdapter timeAdapter;
    TimeAdapter.OnItemsClickListener timeListener;
    ArrayList<ExhibitionIntervalModel.TimeSlots.Slots> timeList = new ArrayList<>();
    //展位列表
    RecyclerView exhibitionPosRv;
    ExhibitionPosAdapter exhibitionPosAdapter;
    ExhibitionPosAdapter.OnItemsClickListener exhibitionPosListener;
    ArrayList<SelectExhibitionPosModel.Data> exhibitionPosList = new ArrayList<>();
    //展位：日期+时间+展位
    TextView exhibitionPosTv;
    //服务费
    TextView serviceChargeTv;
    //押金
    TextView depositTv;
    //参展申请
    TextView applyHintTv;
    //下一步
    Button nextStep;

    SelectExhibitionPosPresenter mPresenter;
    private ExhibitionIntervalPresenter presenter;
    private ExhibitionApplyPresenter exhibitionApplyPresenter;
    //订单编号
//    private String orderSn;
    //申请ID
    private int applyId;
    //帖子id
    private int threadId;
    //展会id
    private int eventId;
    //时间id
    private int timeSlotId;
    //展位时间ID
    private int parkTimeId;
    private String parkName = "";
    private String serviceChargeStr = "";
    private String depositStr = "";

    //展区ID
    private int boothId;
    private String dateStr;
    private String timeStr, endTime = "", startTime = "";
    //小时数
    private double remainHour;
    //申请人昵称
    private String applyName;
    private String nickStr, idCardTruePath, idCardFalsePath, drivingLicensePath, outPath, inPath;
    //车牌号
    private String carNumStr;
    //状态
    private int status;
    //展位id
    private int parkId;
    //变更展位
    private String changeExhibition = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exhibition_pos);
    }

    @Override
    protected void setUpViews() {
        applyId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        changeExhibition = getIntent().getStringExtra(Constants.IntentParams.NAME);

        commitIv = (ImageView) findViewById(R.id.commit_iv);
        applyIv = (ImageView) findViewById(R.id.apply_iv);
        waitIv = (ImageView) findViewById(R.id.wait_iv);

        exhibitionNameTv = (TextView) findViewById(R.id.exhibition_name_tv);
        banner = (Banner) findViewById(R.id.banner);
        boothRv = (RecyclerView) findViewById(R.id.booth_rv);
        banner.isAutoPlay(false);

        dateRv = (RecyclerView) findViewById(R.id.date_rv);
        timeRv = (RecyclerView) findViewById(R.id.time_rv);
        exhibitionPosRv = (RecyclerView) findViewById(R.id.exhibition_pos_rv);

        exhibitionPosTv = (TextView) findViewById(R.id.exhibition_pos_tv);
        serviceChargeTv = (TextView) findViewById(R.id.service_charge_tv);
        depositTv = (TextView) findViewById(R.id.deposit_tv);

        applyHintTv = (TextView) findViewById(R.id.apply_hint_tv);
        nextStep = (Button) findViewById(R.id.next_step);

        exhibitionApplyPresenter = new ExhibitionApplyPresenter();
        exhibitionApplyPresenter.attachView(this);

        applyHintTv.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                // TODO: 2017/9/13 参展申请
                Intent intent = new Intent(getContext(), ApplyHintActivity.class);
                intent.putExtra(Constants.IntentParams.ID, eventId);
                intent.putExtra(Constants.IntentParams.INDEX, 2);
                startActivity(intent);
            }
        });
        nextStep.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                // TODO: 2017/9/13 订单支付
                Log.e("TAG", "applyId: " + applyId + "parkTimeId:" + parkTimeId + "threadId:" + threadId);
                Log.e("TAG", "eventId: " + eventId);
                if (parkTimeId > 0)
                    exhibitionApplyPresenter.commitApply(IOUtils.getToken(getContext()), applyId, nickStr, status, inPath, outPath, drivingLicensePath, eventId, idCardFalsePath, idCardTruePath, parkTimeId, carNumStr, threadId, parkId);
                else
                    CommonUtils.showToast(getContext(),"请选择展位");
            }
        });
        initBoothAdapter();
        initDateAdapter();
        initTimeAdapter();
        initExhibitionPosAdapter();

        GetApplyPresenter getApplyPresenter = new GetApplyPresenter();
        getApplyPresenter.attachView(this);
        getApplyPresenter.getApply(IOUtils.getToken(getContext()), applyId);
        //展位时段
        presenter = new ExhibitionIntervalPresenter();
        presenter.attachView(this);
        //展位
        mPresenter = new SelectExhibitionPosPresenter();
        mPresenter.attachView(this);
        //判断是否是变更展位
        if (changeExhibition.equals("变更展位")){
            nextStep.setText("完成");
            ChangeExhibitionPresenter changeExhibitionPresenter = new ChangeExhibitionPresenter();
            changeExhibitionPresenter.attachView(this);
            changeExhibitionPresenter.changeExhibition(IOUtils.getToken(getContext()),applyId);
        }
    }

    private void initBoothAdapter() {
        boothRv.setHasFixedSize(true);
        boothRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        boothAdapter = new BoothAdapter(this, boothList);
        boothRv.setAdapter(boothAdapter);
    }

    private void initExhibitionPosAdapter() {
        exhibitionPosRv.setHasFixedSize(true);
        exhibitionPosRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        exhibitionPosAdapter = new ExhibitionPosAdapter(this, exhibitionPosList);
        exhibitionPosListener = new ExhibitionPosAdapter.OnItemsClickListener() {
            @Override
            public void onItemClick(int position) {
                if (exhibitionPosList.get(position).status == 2) {
                    CommonUtils.showToast(getContext(), "该展位已锁定");
                } else {
                    if (exhibitionPosList.get(position).isSelect()) {//选中
                        for (int i = 0; i < exhibitionPosList.size(); i++) {
                            exhibitionPosList.get(i).setSelect(false);
                        }
                        exhibitionPosList.get(position).setSelect(false);
                        parkName = "";
                        parkTimeId = -1;
                        SelectExhibitionPosActivity.this.serviceChargeStr = "";
                        SelectExhibitionPosActivity.this.depositStr = "";
                    } else {
                        for (int i = 0; i < exhibitionPosList.size(); i++) {
                            exhibitionPosList.get(i).setSelect(false);
                        }
                        exhibitionPosList.get(position).setSelect(true);
                        parkName = exhibitionPosList.get(position).parkName;
                        parkTimeId = exhibitionPosList.get(position).parkTimeId;
                        SelectExhibitionPosActivity.this.serviceChargeStr = exhibitionPosList.get(position).serviceCharge;
                        SelectExhibitionPosActivity.this.depositStr = exhibitionPosList.get(position).deposit;
                    }
                }
                exhibitionPosAdapter.notifyDataSetChanged();
                remainHour = remainHour(startTime, endTime);
                Log.e("TAG", "parkName: " + parkName + "parkTimeId:" + parkTimeId + "remainHour:" + remainHour);
                exhibitionPosTv.setText("展位：" + dateStr + " " + timeStr + " " + parkName);
                if (StringUtils.checkNull(SelectExhibitionPosActivity.this.serviceChargeStr)) {
                    serviceChargeTv.setText("服务费：￥" + (Double.parseDouble(SelectExhibitionPosActivity.this.serviceChargeStr) * remainHour) + "元");
                } else {
                    serviceChargeTv.setText("服务费：￥元");
                }
                depositTv.setText("押金：￥" + SelectExhibitionPosActivity.this.depositStr + "元");
            }
        };
        exhibitionPosRv.setAdapter(exhibitionPosAdapter);
        exhibitionPosAdapter.setOnItemsClickListener(exhibitionPosListener);
    }

    private void initTimeAdapter() {
        timeRv.setHasFixedSize(true);
        timeRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        timeAdapter = new TimeAdapter(this, timeList);
        timeListener = new TimeAdapter.OnItemsClickListener() {
            @Override
            public void onItemClick(int position, String timesStr, int timeSlotsId) {
                //1表示已满
                if (timeList.get(position).isFull == 1) {
                    CommonUtils.showToast(getContext(), "展位已满");
                    timeSlotId = 0;
                    timeStr = "";
                    endTime = "";
                    startTime = "";
                } else {
                    for (int i = 0; i < timeList.size(); i++) {
                        timeList.get(i).setSelect(false);
                    }
                    timeList.get(position).setSelect(true);
                    timeAdapter.notifyDataSetChanged();

                    timeSlotId = timeList.get(position).id;
                    timeStr = timeList.get(position).timeFormat;
                    endTime = timeList.get(position).endTime;
                    startTime = timeList.get(position).startTime;
                }
                Log.e("TAG", "boothId:" + boothId + "timeSlotId: " + timeSlotId + "timeStr: " + timeStr);
                exhibitionPosTv.setText("展位：" + dateStr + " " + timeStr);
                mPresenter.getSelectExhibitionPos(IOUtils.getToken(getContext()), boothId, timeSlotId);
            }
        };
        timeRv.setAdapter(timeAdapter);
        timeAdapter.setOnItemsClickListener(timeListener);
    }

    private void initDateAdapter() {
        dateRv.setHasFixedSize(true);
        dateRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dateAdapter = new DateAdapter(this, dateList);
        dateListener = new DateAdapter.OnItemsClickListener() {
            @Override
            public void onItemClick(int position) {
                for (int i = 0; i < dateList.size(); i++) {
                    dateList.get(i).setSelect(false);
                }
                dateList.get(position).setSelect(true);
                dateAdapter.notifyDataSetChanged();
//
//                timeSlotId = timeList.get(position).id;
                dateStr = dateList.get(position).timeFormat;

                timeList.clear();
                timeList.addAll(dateList.get(position).slotses);
                for (int i = 0; i < timeList.size(); i++) {
                    if (timeList.get(i).isFull == 0) {
                        timeList.get(i).setSelect(true);
                        timeSlotId = timeList.get(i).id;
                        timeAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                Log.e("TAG", "dateStr:" + dateStr + "boothId:" + boothId + "timeSlotId: " + timeSlotId);
                exhibitionPosTv.setText("展位：" + dateStr + " " + timeStr);
                mPresenter.getSelectExhibitionPos(IOUtils.getToken(getContext()), boothId, timeSlotId);
            }
        };
        dateRv.setAdapter(dateAdapter);
        dateAdapter.setOnItemsClickListener(dateListener);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("二手车主参展申请");
//        function.setImageResource(R.drawable.cover_default);
//        function.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            protected void onSingleClick(View view) {
//                // TODO: 2017/9/13 参展申请须知
//                CommonUtils.startNewActivity(SelectExhibitionPosActivity.this, ApplyHintActivity.class);
//            }
//        });
    }

    public void setBannerView(final ArrayList<ExhibitionIntervalModel.Booths> boothses) {
        List<String> imgUrl = new ArrayList<>();
        for (int i = 0; i < boothses.size(); i++) {
            imgUrl.add(URLUtil.builderImgUrl(boothses.get(i).cover, 540, 270));
        }
        banner.setImages(imgUrl).setImageLoader(new FrescoImageLoader())
                .setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void OnBannerClick(int position) {
                    }
                }).start();
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("TAG", "onPageSelected: " + position);
                if (boothses.size() > 0 && position > 0) {
                    boothId = boothses.get(position - 1).id;
                    for (int i = 0; i < boothList.size(); i++) {
                        boothList.get(i).setSelect(false);
                    }
                    boothList.get(position - 1).setSelect(true);
                    boothAdapter.notifyDataSetChanged();
                    mPresenter.getSelectExhibitionPos(IOUtils.getToken(getContext()), boothId, timeSlotId);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void showExhibitionInterval(ExhibitionIntervalModel model) {
        exhibitionNameTv.setText(model.name);
        ArrayList<ExhibitionIntervalModel.TimeSlots> date = model.timeSlotses;
        ArrayList<ExhibitionIntervalModel.TimeSlots.Slots> time = model.timeSlotses.get(0).slotses;
        ArrayList<ExhibitionIntervalModel.Booths> boothses = model.boothses;
        //展位图
        boothList.clear();
        boothList.addAll(boothses);
        if (boothList.size() != 0 && null != boothList) {
            if (boothId == 0) {
                boothId = boothList.get(0).id;
                boothList.get(0).setSelect(true);
            }
            setBannerView(boothList);
        }
        boothAdapter.notifyDataSetChanged();
        //日期
        dateList.clear();
        dateList.addAll(date);
        if (date.size() != 0 && null != date) {
            for (int i = 0; i < dateList.size(); i++) {
                dateList.get(i).setTimeFormat("周" + dateList.get(i).timeFormat);
            }
            if (!StringUtils.checkNull(dateStr)) {
                dateStr = dateList.get(0).timeFormat;
                dateList.get(0).setSelect(true);
            }
        }
        dateAdapter.notifyDataSetChanged();
        //时间
        timeList.clear();
        timeList.addAll(time);
        if (time.size() != 0 && null != time) {
            for (int i = 0; i < timeList.size(); i++) {
                //默认可点击的第一个
                if (timeList.get(i).isFull != 1) {
                    timeSlotId = timeList.get(i).id;
                    timeStr = timeList.get(i).timeFormat;
                    startTime = timeList.get(i).startTime;
                    endTime = timeList.get(i).endTime;
                    timeList.get(i).setSelect(true);
                    break;
                }
            }
        }
        timeAdapter.notifyDataSetChanged();
        mPresenter.getSelectExhibitionPos(IOUtils.getToken(getContext()), boothId, timeSlotId);
    }

    private double remainHour(String startTime, String endTime) {
        double remainTime = (DateFormatUtil.getTime(endTime) - DateFormatUtil.getTime(startTime)) / 1000 / 60;
        double remainLeft = remainTime / 60;//小时数
        double remainRight = remainTime % 60;//分钟数
        if (remainRight == 0.0) {
            return remainLeft;
        } else if (remainRight > 0.0 && remainRight <= 30.0) {
            return remainLeft + 0.5;
        } else if (remainRight > 30.0 && remainRight < 60.0) {
            return remainLeft + 1;
        }
        return 0;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showExhibitionPos(SelectExhibitionPosModel model) {
        exhibitionPosList.clear();
        exhibitionPosList.addAll(model.datas);
        if (model.datas != null && model.datas.size() != 0) {

            remainHour = remainHour(startTime, endTime);
            Log.e("TAG", "parkName: " + parkName + "parkTimeId:" + parkTimeId + "remainHour:" + remainHour);
            exhibitionPosTv.setText("展位：" + dateStr + " " + timeStr + " " + parkName);
//            if (StringUtils.checkNull(SelectExhibitionPosActivity.this.serviceChargeStr)) {
//                serviceChargeTv.setText("服务费：￥" + (Double.parseDouble(SelectExhibitionPosActivity.this.serviceChargeStr) * remainHour) + "元");
//            } else {
//                serviceChargeTv.setText("服务费：￥元");
//            }
//            depositTv.setText("押金：￥" + depositStr + "元");
        }
        exhibitionPosAdapter.notifyDataSetChanged();
    }

    @Override
    public void commitApplySuccess(ExhibitionApplyModel model) {
//        IOUtils.writeObject(getContext(),,Constants.KEY_EXHIBITION_INTERNAL_FILE);
        //跳转订单支付界面
        Intent intent = new Intent(SelectExhibitionPosActivity.this, OrderPayActivity.class);
        intent.putExtra(Constants.IntentParams.ID, model.data.orderSn);
        startActivity(intent);
    }

    @Override
    public void commitApplyFail(ExhibitionApplyModel model) {
        CommonUtils.showToast(this, model.statusMessage);
    }

    @Override
    public void showGetApply(GetApplyModel model) {
        if (model != null) {
            applyName = model.applyName;
            parkTimeId = model.parkTimeId;
            eventId = model.eventId;
            applyId = model.applyId;
            threadId = model.threadId;
            parkId = model.parkId;
            status = model.status;
            //昵称
            nickStr = model.applyName;
            //车牌号
            carNumStr = model.plateNumber;
            idCardTruePath = model.idcardFront;
            idCardFalsePath = model.idcardBack;
            drivingLicensePath = model.drivingLicense;
            outPath = model.carImageOut;
            inPath = model.carImageIn;
            presenter.getExhibitionInterval(IOUtils.getToken(this), eventId);
        }
    }

    @Override
    public void showExhibitionPos(ChangeExhibitionModel model) {
        if (null == model.data){
            return;
        }
        depositStr = model.data.deposit;
        serviceChargeStr = model.data.serviceCharge;
        parkTimeId = model.data.id;
//        remainHour = remainHour(startTime, endTime);
//        Log.e("TAG", "parkName: " + parkName + "parkTimeId:" + parkTimeId + "remainHour:" + remainHour);
        exhibitionPosTv.setText("展位：" + dateStr + " " + timeStr + " " + parkName);
        if (StringUtils.checkNull(SelectExhibitionPosActivity.this.serviceChargeStr)) {
            serviceChargeTv.setText("服务费：￥" + (Double.parseDouble(SelectExhibitionPosActivity.this.serviceChargeStr) * remainHour) + "元");
        } else {
            serviceChargeTv.setText("服务费：￥元");
        }
        depositTv.setText("押金：￥" + SelectExhibitionPosActivity.this.depositStr + "元");
    }
}
