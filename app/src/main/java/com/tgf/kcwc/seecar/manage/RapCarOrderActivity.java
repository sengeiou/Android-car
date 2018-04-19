package com.tgf.kcwc.seecar.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Pagination;
import com.tgf.kcwc.mvp.model.RapBuymotoModel;
import com.tgf.kcwc.mvp.presenter.RapSeeCarPresenter;
import com.tgf.kcwc.mvp.view.RapOrderMotoView;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Auther: Scott
 * Date: 2017/3/7 0007
 * E-mail:hekescott@qq.com
 */

public class RapCarOrderActivity extends BaseActivity implements RapOrderMotoView {

    private ListView rapOrderL;
    private RapSeeCarPresenter rapBuyMotoPresenter;
    private TextView raporderCount;
    private TimerTask timerTask;
    private Timer timer;
    private ArrayList<RapBuymotoModel.RapbuyItem> prebuyList ;
    private String token;
    private boolean isRealTime = true;
    int isRealTimeCount =30;
    private TextView noticeMsgTv;

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
        text.setText("我要抢单");
       final TextView titleRightBtn =   findViewById(R.id.title_rightBtn);
        titleRightBtn.setText("全部");
        titleRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRealTime = !isRealTime;
                if(isRealTime){
                    isRealTimeCount=30;
                    titleRightBtn.setText("全部");
                    noticeMsgTv.setText("③ 实时刷新时仅显示最新50条待抢单需求，点“全部”可查看所有待抢单需求");
                }else {
                    titleRightBtn.setText("实时");
                    raporderCount.setText("共" + mPagination.count + "条记录");
                    noticeMsgTv.setText("③ 全部需求列表显示截至进入列表时，点“实时”回需求实时刷新列表。");
                    rapBuyMotoPresenter.getRapSeecarlist(token, 1, 9999,1);
                }
            }
        });
    }
    @Override
    protected void setUpViews() {
        rapBuyMotoPresenter = new RapSeeCarPresenter();
        rapBuyMotoPresenter.attachView(this);
        token = IOUtils.getToken(this);
        mPageSize =50;
        rapOrderL = (ListView) findViewById(R.id.raporder_lv);
        raporderCount = (TextView) findViewById(R.id.raporder_count);
        raporderCount.setText(String.format("实时抢单 (%1$d秒后刷新)",30));
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(isRealTime){
                    if(isRealTimeCount<=0){
                        rapBuyMotoPresenter.getRapSeecarlist(token, 1, mPageSize,1);
                        isRealTimeCount=30;
                    }else {
                        isRealTimeCount--;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            raporderCount.setText(String.format("实时抢单 (%1$d秒后刷新)",isRealTimeCount));
                        }
                    });
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,1000,1000);
        rapOrderL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0&&position<prebuyList.size()){
                    Intent toIntent = new Intent(getContext(), IRapSeecarActivity.class);
                    toIntent.putExtra(Constants.IntentParams.ID,prebuyList.get(position).order_id);
                    startActivity(toIntent);
                }
            }
        });

     View footerView =   View.inflate(getContext(),R.layout.listfooter_rapseecar,null);
        noticeMsgTv = footerView.findViewById(R.id.raporder_noticemsg);
        rapOrderL.addFooterView(footerView);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_raporder);

    }

    @Override
    protected void onResume() {
        super.onResume();
        rapBuyMotoPresenter.getRapSeecarlist(token, mPageIndex, mPageSize,1);
    }

    @Override
    public void showPreBuyList(ArrayList<RapBuymotoModel.RapbuyItem> prebuyList) {
        this.prebuyList =prebuyList;
        rapOrderL.setAdapter(new WrapAdapter<RapBuymotoModel.RapbuyItem>(this, prebuyList, R.layout.listitem_raporder) {
            @Override
            public void convert(final ViewHolder helper, final RapBuymotoModel.RapbuyItem item) {
                helper.setText(R.id.raporder_carnametv, item.car_name);
                 TextView offerCountTV = helper.getView(R.id.raporder_offercounttv);
                if(item.offerCount==0){
                    offerCountTV.setVisibility(View.INVISIBLE);
                }else {
                    offerCountTV.setText(item.offerCount + "人抢单");
                }
                Date date =new Date(DateFormatUtil.getTime(item.create_time));
                helper.setText(R.id.raporder_datetv,  DateFormatUtil.timeLogic(date));
                helper.setText(R.id.raporder_username,item.contact+"发出一条看车需求");
                helper.setText(R.id.raporder_address,item.one_city_name+"|"+item.two_city_name);
               TextView lookingTv=  helper.getView(R.id.raporder_lookingtv);
                StringBuilder lookingStr = new StringBuilder();
                if(TextUtil.isEmpty(item.out_color_name)&&TextUtil.isEmpty(item.in_color_name)) {
                    lookingTv.setVisibility(View.GONE);
                }else {
                    lookingTv.setVisibility(View.VISIBLE);
                }
                if(!TextUtil.isEmpty(item.out_color_name)){
                    lookingStr.append("外观: ").append(item.out_color_name+"  ");
                }
                if(!TextUtil.isEmpty(item.in_color_name)){
                    lookingStr.append("内饰: ").append(item.in_color_name);
                }
                helper.setText(R.id.raporder_lookingtv,lookingStr.toString());

//TODO                LocationUtil.getreverseGeoCode(item.lat, item.lng, new OnGetGeoCoderResultListener() {
//                    @Override
//                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
//                    }
//                    @Override
//                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
//                        reverseGeoCodeResult.getBusinessCircle();
//                        ReverseGeoCodeResult.AddressComponent addressComponent = reverseGeoCodeResult.getAddressDetail();
//                        helper.setText(R.id.raporder_username,item.contact+" ["+addressComponent.district+"]");
//                    }
//                });
            }
        });
    }
    Pagination mPagination;
    @Override
    public void showHead(Pagination pagination) {
        mPagination =pagination;
        if(!isRealTime)
        raporderCount.setText("共" + pagination.count + "条记录");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        rapBuyMotoPresenter.detachView();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
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
        return RapCarOrderActivity.this;
    }
}
