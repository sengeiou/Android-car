package com.tgf.kcwc.redpack;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.view.WrapView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.view.DropUpSelectPopupWindow;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.selecttime.TimeSelector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Auther: Scott
 * Date: 2017/10/26 0026
 * E-mail:hekescott@qq.com
 */

public class RedpackCashActivity extends BaseActivity implements WrapView {
    private ArrayList<DataItem> redpackWaylist = new ArrayList();
    private ArrayList<DataItem> redpackShowWaylist = new ArrayList();
    private DropUpSelectPopupWindow mRedpackCreateWayPopupWindow;
    private DropUpSelectPopupWindow mRedpackShowWayPopupWindow;
    private View suijiLayout;
    private View dingeLayout;
    private View kouLingLayout;
    private boolean isOnline;
    private ImageView onlineIv;
    private TextView mSendWayTv;
    private TextView sendWayNoticeTv;
    private DataItem erWeidataItem;
    private String beginTime;
    private String endTime;
    private TextView starttimetv;
    private TextView endtimetv;
    private EditText redpackNameED;

    private  int sendType;
    private EditText redpackPossibiltyED;

    @Override
    protected void setUpViews() {
        suijiLayout = findViewById(R.id.redpack_suijilayout);
        dingeLayout = findViewById(R.id.redpack_dingelayout);
        kouLingLayout = findViewById(R.id.redpack_koulinglayout);
        mSendWayTv = findViewById(R.id.repback_sendway);
        onlineIv = findViewById(R.id.redpack_onlineiv);
        sendWayNoticeTv = findViewById(R.id.redpack_sendWayNoticeTv);
        starttimetv = findViewById(R.id.redpack_starttimetv);
        endtimetv = findViewById(R.id.redpack_endtimetv);
        redpackNameED = findViewById(R.id.redpack_nameEd);
        redpackPossibiltyED = findViewById(R.id.repback_possibiltyEd);

        onlineIv.setOnClickListener(this);
        starttimetv.setOnClickListener(this);
        endtimetv.setOnClickListener(this);

        findViewById(R.id.redpack_showwaysbtn).setOnClickListener(this);
        findViewById(R.id.redpack_createwaysbtn).setOnClickListener(this);
        findViewById(R.id.redpack_createbtn).setOnClickListener(this);



        gainWayDataList();
        gainShowWayDataList();
        mRedpackCreateWayPopupWindow = new DropUpSelectPopupWindow(mContext, redpackWaylist);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("创建现金红包");
        backEvent(back);
    }

    public void gainWayDataList() {
        redpackWaylist.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.name = "随机";
        redpackWaylist.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "定额";
        redpackWaylist.add(dataItem);
    }

    public void gainShowWayDataList() {
        redpackShowWaylist.clear();
        erWeidataItem = new DataItem();
        erWeidataItem.id = 1;
        erWeidataItem.name = "二维码";
        redpackShowWaylist.add(erWeidataItem);
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "自动推送";
        redpackShowWaylist.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.name = "口令红包";
        redpackShowWaylist.add(dataItem);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpackcash);
    }
//    token:Y7OHZJwfzui6sV6YZ1poZxqiz38arY6x
//    end_time:2017-11-13 11:16
//    start_time:2017-11-13 11:16
//    total_money:12
//    welcome_word:你哈
//    name:123
//    send_type:1
//    act_type:2
//    area_sign:123
//    area_sign_question:你好呀的
//    prize_probability:1
//    diameter:
//    prize_price_min:123
//    prize_price_max:123
//    prize_prize_id:
//    prize_ht_id:
//    prize_nums:
    private void buildParams() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(getContext()));
        params.put("end_time",endTime);
        params.put("start_time",beginTime);
        params.put("total_money",beginTime);

        params.put("welcome_word",beginTime);
        params.put("name",beginTime);
        params.put("send_type",beginTime);
        params.put("act_type",beginTime);
        params.put("area_sign",beginTime);
        params.put("area_sign_question",beginTime);
        params.put("prize_probability",redpackPossibiltyED.getText().toString());
        params.put("diameter",beginTime);
        params.put("prize_price_min",beginTime);
        params.put("prize_price_max",beginTime);
        params.put("prize_prize_id",beginTime);
        params.put("prize_ht_id",beginTime);
        params.put("prize_nums",beginTime);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        TimeSelector TimePicKDialog = null;
        String dataString = "";
        switch (view.getId()) {
            case R.id.redpack_showwaysbtn:
                mRedpackShowWayPopupWindow = new DropUpSelectPopupWindow(mContext, redpackShowWaylist);
                mRedpackShowWayPopupWindow.show(RedpackCashActivity.this);
                mRedpackShowWayPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectItem = mRedpackShowWayPopupWindow.getSelectDataItem();
                        if (selectItem == null) {
                            return;
                        }
                        if (selectItem.id == 1) {
                            CommonUtils.showToast(getContext(), "二维码");
                            mSendWayTv.setText("二维码");
                            kouLingLayout.setVisibility(View.GONE);
                            sendWayNoticeTv.setText("审核通过后生成二维码");
                        } else if (selectItem.id == 2) {
                            CommonUtils.showToast(getContext(), "自动推送");
                            kouLingLayout.setVisibility(View.GONE);
                            mSendWayTv.setText("自动推送(滑块验证)");
                            sendWayNoticeTv.setText("当用户在可见范围内，可查看或获取红包推送信息；仅需滑块验证，即可领取红包");
                        } else if (selectItem.id == 3) {
                            CommonUtils.showToast(getContext(), "口令红包");
                            mSendWayTv.setText("口令红包");
                            kouLingLayout.setVisibility(View.VISIBLE);
                            sendWayNoticeTv.setText("用以提醒用户口令是什么，非必填项");
                        }
                    }
                });
                break;
            case R.id.redpack_createwaysbtn:
                mRedpackCreateWayPopupWindow.show(RedpackCashActivity.this);
                mRedpackCreateWayPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectItem = mRedpackCreateWayPopupWindow.getSelectDataItem();
                        if (selectItem == null) {
                            return;
                        }
                        if (selectItem.id == 1) {
                            CommonUtils.showToast(getContext(), "随机");
                            dingeLayout.setVisibility(View.GONE);
                            suijiLayout.setVisibility(View.VISIBLE);

                        } else if (selectItem.id == 2) {
                            CommonUtils.showToast(getContext(), "定额");
                            dingeLayout.setVisibility(View.VISIBLE);
                            suijiLayout.setVisibility(View.GONE);
                        }
                    }
                });
                break;
            case R.id.redpack_createbtn:
                CommonUtils.showToast(getContext(),"创建红包");

                break;
            case R.id.redpack_onlineiv:
                isOnline =!isOnline;
                if(isOnline){
                    CommonUtils.showToast(getContext(),"创建线上红包");
                    onlineIv.setImageResource(R.drawable.icon_xianshang);
                    if(redpackShowWaylist.size()==3){
                        redpackShowWaylist.remove(erWeidataItem);
                    }
                }else {
                    CommonUtils.showToast(getContext(),"创建线下红包");
                    onlineIv.setImageResource(R.drawable.icon_xianxia);
                    if(redpackShowWaylist.size()==2){
                        redpackShowWaylist.add(0,erWeidataItem);
                    }
                }

                break;
            case R.id.redpack_starttimetv:
                if (StringUtils.checkNull(beginTime)) {
                    dataString = beginTime;
                } else {
                    dataString = gainTime();
                }
                TimePicKDialog = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        beginTime = time;
                        starttimetv.setText(time);
                        starttimetv.setTextColor(getResources().getColor(R.color.text_color13));
                    }
                }, dataString, "2199-12-31 23:59");

                TimePicKDialog.setScrollUnit(TimeSelector.SCROLLTYPE.YEAR,
                        TimeSelector.SCROLLTYPE.MONTH, TimeSelector.SCROLLTYPE.DAY,
                        TimeSelector.SCROLLTYPE.HOUR, TimeSelector.SCROLLTYPE.MINUTE);
                TimePicKDialog.show();
                break;
            case R.id.redpack_endtimetv:
                if (StringUtils.checkNull(endTime)) {
                    dataString = endTime;
                } else {
                    dataString = gainTime();
                }
                TimePicKDialog = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        endTime = time;
                        endtimetv.setText(time);
                        endtimetv.setTextColor(getResources().getColor(R.color.text_color13));
                    }
                }, dataString, "2199-12-31 23:59");

                TimePicKDialog.setScrollUnit(TimeSelector.SCROLLTYPE.YEAR,
                        TimeSelector.SCROLLTYPE.MONTH, TimeSelector.SCROLLTYPE.DAY,
                        TimeSelector.SCROLLTYPE.HOUR, TimeSelector.SCROLLTYPE.MINUTE);
                TimePicKDialog.show();
                break;
            default:
                break;
        }
    }

    public String gainTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
