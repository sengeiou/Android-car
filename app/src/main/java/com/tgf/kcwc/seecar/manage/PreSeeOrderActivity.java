package com.tgf.kcwc.seecar.manage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Pagination;
import com.tgf.kcwc.mvp.model.PreSeecarModel;
import com.tgf.kcwc.mvp.presenter.PreSeeCarPresenter;
import com.tgf.kcwc.mvp.view.PreSeecarView;
import com.tgf.kcwc.seecar.WaittingPriceActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/3/7 0007
 * E-mail:hekescott@qq.com
 */

public class PreSeeOrderActivity extends BaseActivity implements PreSeecarView {

    private ListView preSeeMoto;
    List<String> myfilterList;
    private ListView filtListViewerLv;
    private TextView filterSelectTv;
    private PreSeeCarPresenter preSeeCarPresenter;
    private TextView countTv;
    private WrapAdapter mPreSeeAdapter;
    private String token;
    private View emptyView;
    private EMChatManager emChatManager;
    private EMmessageSeecarReciever myWaittingPricePushReciever;
 private int orderFilterStatus =0;
    private boolean isRefresh;
    private View parentFooter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("预约看车订单");
    }

    @Override
    protected void setUpViews() {
        mPageSize=20;
        initRefreshLayout(mBGDelegate);
        preSeeCarPresenter = new PreSeeCarPresenter();
        preSeeCarPresenter.attachView(this);
        token = IOUtils.getToken(getContext());

        preSeeMoto = (ListView) findViewById(R.id.preseeorder_lv);
        countTv = (TextView) findViewById(R.id.preseeorder_count);
        emptyView = findViewById(R.id.msgTv2);
        filtListViewerLv = (ListView) findViewById(R.id.preseeoder_filterlv);
        filtListViewerLv.setVisibility(View.GONE);
        filterSelectTv = (TextView) findViewById(R.id.preseeoder_filterselect);
        findViewById(R.id.prebuyorder_start).setOnClickListener(this);
        filterSelectTv.setOnClickListener(this);
        String[] filterItem = mRes.getStringArray(R.array.preseeorder_filter);
        myfilterList = Arrays.asList(filterItem);
        filtListViewerLv.setAdapter(new WrapAdapter<String>(this, R.layout.mylist, myfilterList) {
            @Override
            public void convert(ViewHolder helper, String item) {
                TextView filterItem = helper.getView(R.id.mytext);
                filterItem.setTextColor(mRes.getColor(R.color.text_color3));
                filterItem.setText(item);
            }

        });
        emChatManager = EMClient.getInstance().chatManager();
        filtListViewerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderFilterStatus =position;
                filterSelectTv.setText(myfilterList.get(position));
                filtListViewerLv.setVisibility(View.GONE);
                isRefresh =true;
                preSeeCarPresenter.getPreSeecarList(token, orderFilterStatus, 1, mPageSize);
            }
        });

        preSeeMoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < mPreBuyItemList.size()) {
                    PreSeecarModel.PreSeecarItem preSeecarItem = mPreBuyItemList.get(position);
                  if(preSeecarItem.status==4){
                      return;
                  }
                    Intent toOderSeeAct = new Intent(getContext(), OrderSeeCarActivity.class);
                    toOderSeeAct.putExtra(Constants.IntentParams.ID, preSeecarItem.id);
                    toOderSeeAct.putExtra(Constants.IntentParams.ID2, preSeecarItem.order_id);
                    toOderSeeAct.putExtra(Constants.IntentParams.DATA, preSeecarItem.user_name);
                    ArrayList<EMMessage> unReadMessageList = (ArrayList<EMMessage>) unReadMap.get("buycar_" + preSeecarItem.order_id + "_");
                    toOderSeeAct.putParcelableArrayListExtra(Constants.IntentParams.DATA2, unReadMessageList);
                    startActivity(toOderSeeAct);
                }
            }
        });

        parentFooter = View.inflate(getContext(), R.layout.bottom_hint_layout,null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preseeorder);
        myWaittingPricePushReciever = new EMmessageSeecarReciever();
        IntentFilter intentFilter = new IntentFilter("com.tgf.kcwc.seecar");
        registerReceiver(myWaittingPricePushReciever, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRefresh = true;
        preSeeCarPresenter.getPreSeecarList(token, orderFilterStatus, 1, mPageSize);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.preseeoder_filterselect:
                if (filtListViewerLv.getVisibility() == View.VISIBLE) {
                    filtListViewerLv.setVisibility(View.GONE);
                } else {
                    filtListViewerLv.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.prebuyorder_start:
                CommonUtils.startNewActivity(this, RapCarOrderActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        if (myWaittingPricePushReciever != null) {
            unregisterReceiver(myWaittingPricePushReciever);
            myWaittingPricePushReciever = null;
        }
        super.onDestroy();
    }

    ArrayList<PreSeecarModel.PreSeecarItem> mPreBuyItemList = new ArrayList<>();
boolean isAddFooter =false;
    @Override
    public void showPreBuylist(ArrayList<PreSeecarModel.PreSeecarItem> preBuyItemList) {
        if(isRefresh){
            mPreBuyItemList.clear();
            if(isAddFooter){
                preSeeMoto.removeFooterView(parentFooter);
                isAddFooter =false;
            }
        }
        mPreBuyItemList.addAll(preBuyItemList);
        if (mPreSeeAdapter == null) {
            iniAdatper();
            preSeeMoto.setAdapter(mPreSeeAdapter);
        } else {
            mPreSeeAdapter.notifyDataSetChanged();
        }
        if (mPreBuyItemList == null || mPreBuyItemList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            if(preBuyItemList==null||preBuyItemList.size()==0){
                if(!isAddFooter){
                    preSeeMoto.addFooterView(parentFooter);
                    isAddFooter =true;
                }
            }

        }

        getUnReadMesage();

    }

    @Override
    public void showListCount(Pagination pagination) {
        countTv.setText("共" + pagination.count + "条记录");
    }

    private void iniAdatper() {
        mPreSeeAdapter = new WrapAdapter<PreSeecarModel.PreSeecarItem>(this, mPreBuyItemList,
                R.layout.listitem_preseeorder_kehu) {
            @Override
            public void convert(ViewHolder helper, PreSeecarModel.PreSeecarItem item) {
                helper.setText(R.id.prebuyorder_datetv, item.create_time);
                helper.setText(R.id.prebuyorder_name, item.contact);
                helper.setText(R.id.prebuyorder_carmodel, item.car_name);
                TextView msgNumTv = helper.getView(R.id.avatar_msgnubtv);
//                EMConversation conversation = emChatManager.getConversation(item.user_name, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
                List<EMMessage> unReadList = unReadMap.get("buycar_" + item.order_id + "_");
                if (unReadList != null && unReadList.size() != 0) {
                    item.msg_num = unReadList.size();
                }
                if (item.msg_num == 0) {
                    msgNumTv.setVisibility(View.GONE);
                } else {
                    msgNumTv.setText(item.msg_num + "");
                }
             TextView lookingTv =   helper.getView(R.id.prebuyorder_looking);
                StringBuilder lookingStr = new StringBuilder("");
                if(TextUtil.isEmpty(item.outColorName)&&TextUtil.isEmpty(item.inColorName)) {
                    lookingTv.setVisibility(View.GONE);
                }else {
                    lookingTv.setVisibility(View.VISIBLE);
                }
                if(!TextUtil.isEmpty(item.outColorName)){
                    lookingStr.append("外观: <font color=\"#323539\">" + item.outColorName + "</font>"+"\t\t");
                }
                if(!TextUtil.isEmpty(item.inColorName)){
                    lookingStr.append(" 内饰: <font color=\"#323539\">" +item.inColorName + "</font>");
                }
                lookingTv.setText(Html.fromHtml(lookingStr.toString()));
//                if(TextUtil.isEmpty(item.outColorName)){
//                    lookingTv.setVisibility(View.GONE);
//                }else {
//                    String htmlStr = "外观: <font color=\"#323539\">" + item.outColorName + "</font>"
//                                    +" 内饰: <font color=\"#323539\">" +item.inColorName + "</font>";
//                    lookingTv.setText(Html.fromHtml(htmlStr));
//                }
                lookingTv.setText(Html.fromHtml(lookingStr.toString()));
                TextView filterSelct = helper.getView(R.id.preseeoder_filterselect);
                String avatarUrl = URLUtil.builderImgUrl(item.avatar, 360, 360);
                helper.setSimpleDraweeViewURL(R.id.avatariv, avatarUrl);
                setStatus(item, filterSelct);
                if(item.status==4){
                    helper.getView(R.id.preseeoder_more).setVisibility(View.GONE);
                }else {
                    helper.getView(R.id.preseeoder_more).setVisibility(View.VISIBLE);
                }
            }

            private void setStatus(PreSeecarModel.PreSeecarItem item, TextView filterSelct) {
                switch (item.status) {
                    case 1:
                        filterSelct.setText("服务中");
                        filterSelct.setTextColor(mRes.getColor(R.color.text_color1));
                        break;
                    case 2:
                        filterSelct.setText("已完成");
                        filterSelct.setTextColor(mRes.getColor(R.color.text_color3));
                        break;
                    case 3:
                        filterSelct.setText("已评价");
                        filterSelct.setTextColor(mRes.getColor(R.color.text_color2));
                        break;
                    case 4:
                        filterSelct.setText("对方已关闭");
                        filterSelct.setTextColor(mRes.getColor(R.color.text_color17));
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if(active){
            showLoadingIndicator(active);
        }else {
            stopRefreshAll();
            showLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return PreSeeOrderActivity.this;
    }

    Map<String, List<EMMessage>> unReadMap = new HashMap<>();

    public void getUnReadMesage() {
        unReadMap.clear();
        for (PreSeecarModel.PreSeecarItem preseeItem : mPreBuyItemList) {
            String orderId = null;
            EMConversation conversation = emChatManager.getConversation(preseeItem.user_name, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
            List<EMMessage> listMessga = conversation.getAllMessages();
            List<EMMessage> unReadList = new ArrayList<>();
            for (EMMessage item : listMessga) {
                if (item.isUnread()) {
                    try {
                        JSONObject jsoBj = item.getJSONObjectAttribute("data");
                        orderId = jsoBj.optString("order_id");
                        if (("buycar_" + preseeItem.order_id + "_").equals(orderId)) {
                            unReadList.add(item);
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }

                }
            }
            if (unReadList.size() != 0) {
                unReadMap.put("buycar_" + preseeItem.order_id + "_", unReadList);
            }
        }

//        for(preBuyItemList)


    }

    private class EMmessageSeecarReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            isRefresh =true;
            preSeeCarPresenter.getPreSeecarList(token,orderFilterStatus,1,mPageSize);
//            String buyMotoId = intent.getStringExtra(Constants.IntentParams.ID);
//            Logger.d("object_id" + buyMotoId);
//            waittingPricePresenter.getNewPrice(token, Integer.parseInt(buyMotoId));
        }
    }
    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isRefresh = false;
            mPageIndex++;
            loadMore();
            return false;
        }
    };

    public void loadMore() {
        preSeeCarPresenter.getPreSeecarList(token,orderFilterStatus,mPageIndex,mPageSize);
    }

}
