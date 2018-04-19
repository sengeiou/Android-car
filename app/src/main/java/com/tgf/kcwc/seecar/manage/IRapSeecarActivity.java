package com.tgf.kcwc.seecar.manage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.mvp.model.IRapOrderModel;
import com.tgf.kcwc.mvp.model.RapOrderPostModel;
import com.tgf.kcwc.mvp.presenter.IRapSeecarPresenter;
import com.tgf.kcwc.mvp.view.IrapOrderDetailView;
import com.tgf.kcwc.ticket.widget.SendObjDialog;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NoticeDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Auther: Scott
 * Date: 2017/3/7 0007
 * E-mail:hekescott@qq.com
 */

public class IRapSeecarActivity extends BaseActivity implements IrapOrderDetailView {

    private NoticeDialog noticeDialog;
    private TextView iraporderCreatetime;
    private TextView iraporderName;
    private SimpleDraweeView iraporderCover;
    private TextView iraporderMotomdel;
    private TextView iraporderMotooutTv;
    private TextView iraporderMotodesc;
    private IRapOrderModel mIrapOrderDetail;
    private SendObjDialog sendObjDialog;
    private int mOrderId = 104;
    private String token;
    private IRapSeecarPresenter iRapOrderPresenter;
    private Intent fromIntent;


    @Override
    protected void setUpViews() {

        fromIntent = getIntent();
        iRapOrderPresenter = new IRapSeecarPresenter();
        iRapOrderPresenter.attachView(this);
        mOrderId = fromIntent.getIntExtra(Constants.IntentParams.ID, 0);
        token = IOUtils.getToken(getContext());
        iRapOrderPresenter.getIrapOderDetail(token, mOrderId);
        findViewById(R.id.iraporder_raptv).setOnClickListener(this);
        initDialog();
        iraporderCreatetime = (TextView) findViewById(R.id.iraporder_createtime);
        iraporderName = (TextView) findViewById(R.id.iraporder_name);
        iraporderCover = (SimpleDraweeView) findViewById(R.id.iraporder_cover);
        iraporderMotomdel = (TextView) findViewById(R.id.iraporder_motomdel);
        iraporderMotooutTv = (TextView) findViewById(R.id.iraporder_motoout);
        iraporderMotodesc = (TextView) findViewById(R.id.iraporder_motodesc);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("我要报价");
    }

    private void initDialog() {
        sendObjDialog = new SendObjDialog(this);
        sendObjDialog.setTitle("提车底价");
        sendObjDialog.setMessage("关闭报价视为放弃本单");
        sendObjDialog.setEditHint("提交报价后获得用户联系方式");
        sendObjDialog.setYesOnclickListener("提交", new SendObjDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                int price = sendObjDialog.getNums();
                iRapOrderPresenter.postIRapOrderPrice(token, mOrderId, price);
            }
        });
        sendObjDialog.setNoOnclickListener("关闭", new SendObjDialog.onNoOnclickListener() {

            @Override
            public void onNoClick() {
                sendObjDialog.dismiss();
            }
        });
    }

    @Override
    public void showRapDetail(IRapOrderModel iRapOrderModel) {
        mIrapOrderDetail = iRapOrderModel;
        iraporderCreatetime.setText("下单时间： " + DateFormatUtil.formatTime5(iRapOrderModel.create_time));
        iraporderName.setText(iRapOrderModel.contact + " 想看");
        iraporderMotomdel.setText(iRapOrderModel.brand_name+"  "+iRapOrderModel.car_name);
        iraporderMotodesc.setText(iRapOrderModel.description);
        iraporderMotooutTv.setText(iRapOrderModel.getOutlooking());
        iraporderCover.setImageURI(Uri.parse(URLUtil.builderImgUrl(iRapOrderModel.cover,270, 203)));
    }


    @Override
    public void showPostSuccess(RapOrderPostModel postData) {
        sendObjDialog.dismiss();
//        List<EMMessage> messageList = new ArrayList<EMMessage>();
//        EMMessage  message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//        EMTextMessageBody body = new EMTextMessageBody("您好，我是销售顾问"+IOUtils.getAccount(getContext()).userInfo.nickName+"，很高兴为您提供服务，您可以简单描述一下计划购买的车型，或特殊需求，我将尽快回复您");
        String bodyMsg =  "您好，我是销售顾问"+IOUtils.getAccount(getContext()).userInfo.nickName+"，很高兴为您提供服务，您可以简单描述一下计划购买的车型，或特殊需求，我将尽快回复您";
//        message.addBody(body);
//        message.setFrom(SharedPreferenceUtil.getEaseaUserName(getContext()));
//        message.setTo(postData.im_user_id);
//        message.setMsgTime(System.currentTimeMillis());
//        message.setMsgId("350461429712685056");
//        message.setChatType(EMMessage.ChatType.Chat);
//        message.setStatus(EMMessage.Status.SUCCESS);
//        messageList.add(message);
//        EMClient.getInstance().chatManager().importMessages(messageList);
        Intent toOderSeeAct = new Intent(getContext(), OrderSeeCarActivity.class);
        toOderSeeAct.putExtra(Constants.IntentParams.ID, postData.offer_id);
        toOderSeeAct.putExtra(Constants.IntentParams.ID2, mOrderId);
        toOderSeeAct.putExtra(Constants.IntentParams.DATA, postData.im_user_id);
        toOderSeeAct.putExtra(Constants.IntentParams.DATA3,bodyMsg);
//        ArrayList<EMMessage> unReadMessageList = (ArrayList<EMMessage>) unReadMap.get("buycar_" + preSeecarItem.order_id + "_");
//        toOderSeeAct.putParcelableArrayListExtra(Constants.IntentParams.DATA2, unReadMessageList);
        startActivity(toOderSeeAct);
        finish();
    }

    @Override
    public void showPostFailed(String statusMessage) {
        sendObjDialog.dismiss();
        CommonUtils.showToast(this, statusMessage);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iraporder);
        noticeDialog = new NoticeDialog(this);
        noticeDialog.setYesOnclickListener("知道了", new NoticeDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                noticeDialog.dismiss();
            }
        });
        TextView rapTv2 = (TextView) findViewById(R.id.iraporder_raptv2);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_animrap);
        rapTv2.startAnimation(animation);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iraporder_raptv:
                iRapOrderPresenter.postIRapOrderPrice(token, mOrderId, 0);
//                sendObjDialog.show();
//                noticeDialog.show();
                break;
            default:
                break;
        }
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
