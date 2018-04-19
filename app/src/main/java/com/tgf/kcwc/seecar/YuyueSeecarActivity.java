package com.tgf.kcwc.seecar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.PreSeecarModel;
import com.tgf.kcwc.mvp.model.YuyueBuyModel;
import com.tgf.kcwc.mvp.presenter.YuYueServerPresenter;
import com.tgf.kcwc.seecar.manage.PreSeeOrderActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.BadgeView;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NotitleContentDialog;
import com.tgf.kcwc.view.YueyuBuyView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Auther: Scott
 * Date: 2017/2/24 0024
 * E-mail:hekescott@qq.com
 */

public class YuyueSeecarActivity extends BaseActivity implements YueyuBuyView {

    private SimpleDraweeView orgCover;
    private TextView offerNum;
    private TextView carModel;
    private ListView orglistListView;
    private YuYueServerPresenter completeServerPresenter;
    private ImageView outImgIv;
    private ImageView inImgIv;
    private int mOrderId;
    private String mToken;
    private EMChatManager mChatManager;
    private NotitleContentDialog noStopPriceDialog;
    private TextView closePriceBtn;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("预约看车");

    }

    @Override
    protected void setUpViews() {
        completeServerPresenter = new YuYueServerPresenter();
        completeServerPresenter.attachView(this);
        mOrderId = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        mToken = IOUtils.getToken(this);
        orgCover = (SimpleDraweeView) findViewById(R.id.completeserver_org_cover);
        offerNum = (TextView) findViewById(R.id.completeserver_tickettitle);
        carModel = (TextView) findViewById(R.id.completeserver_carmodel);
        outImgIv = (ImageView) findViewById(R.id.booksee_outimg);
        inImgIv = (ImageView) findViewById(R.id.booksee_inimg);
        View bottomListView = View.inflate(getContext(), R.layout.listfooter_yuyueseecar, null);
        orglistListView = (ListView) findViewById(R.id.completeserver_orglistlv);
        orglistListView.addFooterView(bottomListView);
        closePriceBtn = findViewById(R.id.yuyuebuy_closeprice);
        closePriceBtn.setOnClickListener(this);
        findViewById(R.id.yuyuebuy_completeserver).setOnClickListener(this);
        noStopPriceDialog = new NotitleContentDialog(getContext());
        noStopPriceDialog.setContentText("确定关闭报价吗?");
        noStopPriceDialog.setOnLoginOutClickListener(new NotitleContentDialog.IOnclickLisenter() {
            @Override
            public void OkClick() {
                completeServerPresenter.posClosePrice(mToken, mOrderId);
            }

            @Override
            public void CancleClick() {
                noStopPriceDialog.dismiss();
            }
        });
        emChatManager = EMClient.getInstance().chatManager();
        myWaittingPricePushReciever = new EMmessageSeecarReciever();
        IntentFilter intentFilter = new IntentFilter("com.tgf.kcwc.seecar");
        registerReceiver(myWaittingPricePushReciever, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        completeServerPresenter.getCompeleteServerModel(mToken, mOrderId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuyuebuy);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        super.showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        completeServerPresenter.detachView();
        if(myWaittingPricePushReciever!=null){
            unregisterReceiver(myWaittingPricePushReciever);
            myWaittingPricePushReciever =null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.yuyuebuy_completeserver:
                completeServerPresenter.posCarorderCompelete(mToken, mOrderId);
                break;
            case R.id.yuyuebuy_closeprice:
                noStopPriceDialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void showHead(YuyueBuyModel completeServerModel) {
        String url = URLUtil.builderImgUrl(completeServerModel.car_cover, 270, 203);
        orgCover.setImageURI(Uri.parse(url));
        offerNum.setTextColor(mRes.getColor(R.color.style_bg6));
        offerNum.setText(completeServerModel.offer_count + "名业务员已经报价");
        carModel.setText(completeServerModel.brand_name + " " + completeServerModel.seriesName + " "
                + completeServerModel.car_name);
        int size = BitmapUtils.dp2px(mContext, 15);
        outImgIv.setImageBitmap(BitmapUtils.getRectColors(completeServerModel.outColor.split(","),
                size, size, R.color.style_bg4, 2));
        inImgIv.setImageBitmap(BitmapUtils.getRectColors(completeServerModel.inColor.split(","),
                size, size, R.color.style_bg4, 2));
        showOrglist(completeServerModel.offer_list);
        if(completeServerModel.orderStatus==0){
            closePriceBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void showOrglist(List<YuyueBuyModel.OrgItem> orgItemList) {
        getUnReadMesage(orgItemList);
//        mChatManager = EMClient.getInstance().chatManager();
        orglistListView.setAdapter(new WrapAdapter<YuyueBuyModel.OrgItem>(getContext(),
                R.layout.listitem_buymoto_completeserver, orgItemList) {
            @Override
            public void convert(ViewHolder helper, final YuyueBuyModel.OrgItem item) {
                helper.setText(R.id.listitem_complete_nickname, item.nickname);
//                EMConversation conversation = mChatManager.getConversation(item.username);
                SimpleDraweeView avatarIv = helper.getView(R.id.listitem_complete_avatar);
                BadgeView badgeView = new BadgeView(getContext());
                badgeView.setTargetView(avatarIv);
                badgeView.setBackground(10, mRes.getColor(R.color.red));
                badgeView.setGravity(Gravity.TOP | Gravity.RIGHT);
//                if (unReadMap != null) {
//                    if(!TextUtil.isEmpty(unReadMap.get(item.username))){
//                        int count = Integer.parseInt(unReadMap.get(item.username));
//                        badgeView.setBadgeCount(count);
//                    }
//
//                }
                List<EMMessage> unReadList = unReadMap.get(item.username);
                if (unReadList != null && unReadList.size() != 0) {
                    item.msg_num = unReadList.size();
                }
                badgeView.setBadgeCount(item.msg_num);

                badgeView.setBadgeMarginRight(10);
                badgeView.setTextColor(mRes.getColor(R.color.white));
                String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                avatarIv.setImageURI(avatarUrl);
                setStatus(helper, item);
                helper.setText(R.id.listitem_complete_leveal, item.star);
                helper.setText(R.id.listitem_complete_title, item.org_name);
                helper.setText(R.id.listitem_complete_adderss, item.org_address);
//                helper.setText(R.id.listitem_complete_price, Html.fromHtml("提车底价 <font color=\"#e12c0e\">￥   " + item.offer + "</font>"));


            }

            private void setStatus(ViewHolder helper, final YuyueBuyModel.OrgItem orgItem) {
                RatingBar listitemCompleteRating = helper.getView(R.id.listitem_complete_rating);
                Float starScore = Float.parseFloat(orgItem.integral);

                listitemCompleteRating.setStar(NumFormatUtil.getFmtString(orgItem.star));
                TextView waitevalTv = helper.getView(R.id.seecar_waitevaltv);
                View msgLayout = helper.getView(R.id.seecar_msglayout);
                View hasevalMsgLay = helper.getView(R.id.seecar_hasevallayout);
                if (orgItem.status == 3) {//服务中
                    msgLayout.setVisibility(View.VISIBLE);
                    waitevalTv.setVisibility(View.INVISIBLE);
                    hasevalMsgLay.setVisibility(View.INVISIBLE);
                    helper.getView(R.id.seecar_msgiv)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public int hashCode() {
                                    return super.hashCode();
                                }

                                @Override
                                public void onClick(View v) {
                                    EMConversation conversation=  emChatManager.getConversation(orgItem.username, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
                                    if(conversation!=null){
                                        List<EMMessage> unReadMessageList=  unReadMap.get(orgItem.username);
                                        if(unReadMessageList!=null&&unReadMessageList.size()!=0){
                                            for(EMMessage emMessage:unReadMessageList){
                                                conversation.markMessageAsRead(emMessage.getMsgId());
                                            }
                                        }
                                    }
                                    Intent toIntent = new Intent(getContext(),
                                            OrderFellowActivity.class);
                                    toIntent.putExtra(Constants.IntentParams.DATA, orgItem);
                                    toIntent.putExtra(Constants.IntentParams.ID, mOrderId);
                                    startActivity(toIntent);
                                }
                            });
                    helper.getView(R.id.seecar_calliv)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SystemInvoker.launchDailPage(getContext(), orgItem.tel);
                                }
                            });
                } else {//已服务
                    if (starScore <= 0) {//未评价
                        msgLayout.setVisibility(View.INVISIBLE);
                        waitevalTv.setVisibility(View.VISIBLE);
                        hasevalMsgLay.setVisibility(View.INVISIBLE);
                        waitevalTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toIntent = new Intent(getContext(),
                                        ServerEvaluationActivity.class);
                                toIntent.putExtra(Constants.IntentParams.DATA, orgItem);
                                toIntent.putExtra(Constants.IntentParams.ID, mOrderId);
                                startActivity(toIntent);
                            }
                        });
                    } else {//已评价
                        msgLayout.setVisibility(View.INVISIBLE);
                        waitevalTv.setVisibility(View.INVISIBLE);
                        hasevalMsgLay.setVisibility(View.VISIBLE);
                        helper.setText(R.id.seecar_hasevalscoreTv, orgItem.integral + "分");
                    }

                }
            }
        });

    }
    Map<String, List<EMMessage>> unReadMap = new HashMap<>();
    private EMChatManager emChatManager;
    public void getUnReadMesage(List<YuyueBuyModel.OrgItem> mPreBuyItemList) {
        unReadMap.clear();
        for (YuyueBuyModel.OrgItem preseeItem : mPreBuyItemList) {
            String orderId = null;
            EMConversation conversation = emChatManager.getConversation(preseeItem.username, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
            List<EMMessage> listMessga = conversation.getAllMessages();
            List<EMMessage> unReadList = new ArrayList<>();
            for (EMMessage item : listMessga) {
                if (item.isUnread()) {
                    try {
                        JSONObject jsoBj = item.getJSONObjectAttribute("data");
                        orderId = jsoBj.optString("order_id");
                        if (("buycar_" + mOrderId + "_").equals(orderId)) {
                            unReadList.add(item);
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (unReadList.size() != 0) {
                unReadMap.put(preseeItem.username, unReadList);
            }
        }

//        for(preBuyItemList)


    }
    @Override
    public void showCompleteSuccess() {
        Intent toIntent = new Intent(getContext(), CompleteServerActivity.class);
        toIntent.putExtra(Constants.IntentParams.ID, mOrderId);
        startActivity(toIntent);
        finish();
    }

    @Override
    public void showCompleteFailed() {
        CommonUtils.showToast(getContext(), "完成订单失败，请重新尝试");
    }

    @Override
    public void showCloseSuccess() {
        CommonUtils.showToast(getContext(), "关闭报价成功");
        closePriceBtn.setVisibility(View.GONE);
        noStopPriceDialog.dismiss();
    }

    @Override
    public void showCloseFailed(String statusMessage) {
        CommonUtils.showToast(getContext(), statusMessage);
    }
    private EMmessageSeecarReciever myWaittingPricePushReciever;
    private class EMmessageSeecarReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            isRefresh =true;
//            preSeeCarPresenter.getPreSeecarList(token,orderFilterStatus,1,mPageSize);
            completeServerPresenter.getCompeleteServerModel(mToken, mOrderId);
        }
    }

}
