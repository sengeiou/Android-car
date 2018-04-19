package com.tgf.kcwc.seecar.manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.mvp.model.OrderBuyTrackModel;
import com.tgf.kcwc.mvp.presenter.BuyTrackPresenter;
import com.tgf.kcwc.mvp.view.BuyTrackView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;


/**
 * Auther: Scott
 * Date: 2017/3/7 0007
 * E-mail:hekescott@qq.com
 */

public class OrderSeeCarActivity extends BaseActivity implements BuyTrackView {

    private int chatType;
    private String toChatUsername;
    private Bundle bundle;
    public View chatmenulayout;
    public View customerll;
    private TextView nicknameTv;
    private TextView motoDesc;
    private BuyTrackPresenter buyTrackPresenter;
    private Intent fromIntent;
    private SimpleDraweeView titleAvatarIv;
    private OrderBuyTrackModel.UserInfo userInfo;
    private SimpleDraweeView titleGenderIv;
    private ImageView isDaIv;
    private ImageView inImg;
    private ImageView outImg;
    private OrderBuyTrackModel.OrderInfo orderInfo;
    private TextView lookingTv;
    private TextView carNameTv;
    private TextView oderDesc;
    private SimpleDraweeView brandIv;
    private TextView titleNametv;
    private TextView inputNameTv;
    private ImageView isModelIv;
    public View backBtn;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EaseConstant.IS_BUYCAR_TYPE =true;
        EaseConstant.IS_BUYCAR_MANAGE = true;
        setContentView(R.layout.activity_orderseecarm);
        findViewById(R.id.orderfellow_sendmsg).setOnClickListener(this);
        findViewById(R.id.orderfellow_call).setOnClickListener(this);
        backBtn =   findViewById(R.id.title_back);
        findViewById(R.id.title_back).setOnClickListener(this);
        findViewById(R.id.orderseemoto_call).setOnClickListener(this);
        titleNametv = (TextView) findViewById(R.id.orderfellochat_nikanmetv);
        customerll = findViewById(R.id.orderseemoto_custommer);
        chatmenulayout = findViewById(R.id.orderseemoto_chatmenu);
        nicknameTv = (TextView) findViewById(R.id.listitem_complete_nickname);
        inputNameTv = (TextView) findViewById(R.id.orderseemoto_nickanmetv);
        titleAvatarIv = (SimpleDraweeView) findViewById(R.id.orderfellow_avatar);
        titleGenderIv = (SimpleDraweeView) findViewById(R.id.genderImg);
        brandIv = (SimpleDraweeView) findViewById(R.id.orderseem_brand);
        isDaIv = (ImageView) findViewById(R.id.oderbuy_isda);
        isModelIv = (ImageView) findViewById(R.id.oderbuy_ismodel);
        motoDesc = (TextView) findViewById(R.id.orderseemoto_desc);
        outImg = (ImageView) findViewById(R.id.orderseem_outimg);
        inImg = (ImageView) findViewById(R.id.orderseem_inimg);
        lookingTv = (TextView) findViewById(R.id.orderseem_colorname);
        carNameTv = (TextView) findViewById(R.id.ordersee_carNameTv);
        oderDesc = (TextView) findViewById(R.id.orderseemoto_desc);
        findViewById(R.id.ordersee_xialaiv).setOnClickListener(this);
    }

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
     int offerId =   fromIntent.getIntExtra(Constants.IntentParams.ID,0);
     int orderId =   fromIntent.getIntExtra(Constants.IntentParams.ID2,0);
        toChatUsername =    fromIntent.getStringExtra(Constants.IntentParams.DATA);
        String bodyMsg =   fromIntent.getStringExtra(Constants.IntentParams.DATA3);
        ArrayList<EMMessage> unReadMessageList = fromIntent.getParcelableArrayListExtra(Constants.IntentParams.DATA2);
        buyTrackPresenter = new BuyTrackPresenter();
        buyTrackPresenter.attachView(this);
        buyTrackPresenter.getTrackInfo(IOUtils.getToken(getContext()),offerId,orderId);

//        toChatUsername = mToChatUser.username;
        if(TextUtil.isEmpty(toChatUsername)){
            toChatUsername = "im_843_kcwc";
        }
        EMConversation conversation=  EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
        if(conversation!=null){
            if(unReadMessageList!=null&&unReadMessageList.size()!=0){
                for(EMMessage emMessage:unReadMessageList){
                    conversation.markMessageAsRead(emMessage.getMsgId());
                }
            }
        }

//        toChatUsername = "13368246592";
        chatType = EaseConstant.CHATTYPE_SINGLE;
        OrderSeeFragment chatFragment = new OrderSeeFragment();
        bundle = new Bundle();
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType);
        bundle.putString(EaseConstant.EXTRA_USER_ID, toChatUsername);
        bundle.putString(EaseConstant.EXTRA_USER_ORDER,orderId+"");
        if(!TextUtil.isEmpty(bodyMsg)){
            bundle.putString(Constants.IntentParams.DATA,bodyMsg);
        }
        chatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.orderseemoto_chatframe, chatFragment, toChatUsername).commit();

    }



    @Override
    public void onClick(View view) {
             switch (view.getId()){
                         case R.id.orderfellow_sendmsg:
                             chatmenulayout.setVisibility(View.GONE);
                             break;
                         case R.id.orderfellow_call:
                         case R.id.orderseemoto_call:
                             SystemInvoker.launchDailPage(getContext(),orderInfo.tel);
                             break;
                         case R.id.ordersee_xialaiv:
                             customerll.setVisibility(View.VISIBLE);
                             break;
                         case R.id.title_back:
                             finish();
                             break;
                         default:
                             break;
                     }
    }




    @Override
    public void showBuyTrackView(OrderBuyTrackModel orderBuyTrackModel) {
        userInfo = orderBuyTrackModel.userInfo;
        orderInfo = orderBuyTrackModel.orderInfo;
        nicknameTv.setText(userInfo.nickname);
        if(TextUtils.isEmpty(orderBuyTrackModel.offNote)){
            motoDesc.setVisibility(View.GONE);
        }else {
            motoDesc.setText(orderBuyTrackModel.offNote);
        }

        GenericDraweeHierarchy genderGDH =titleGenderIv.getHierarchy();
            if(userInfo.sex==1){
                genderGDH.setPlaceholderImage(R.drawable.icon_men);
            }else {
                genderGDH.setPlaceholderImage(R.drawable.icon_women);
            }
            if(userInfo.isDa==1){
                isDaIv.setVisibility(View.VISIBLE);
            }else{
                isDaIv.setVisibility(View.GONE);
            }
            if(userInfo.isModel==1){
                isModelIv.setVisibility(View.VISIBLE);
            }else{
                isModelIv.setVisibility(View.GONE);
            }

        titleAvatarIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(userInfo.avatar,144,144)));
        int size = BitmapUtils.dp2px(this,15);
        String appearColorValue = orderInfo.out_color_value;
        if(TextUtil.isEmpty(orderInfo.inColorValue)){
            inImg.setVisibility(View.INVISIBLE);
        }
        if(TextUtil.isEmpty(orderInfo.out_color_value)){
            outImg.setVisibility(View.INVISIBLE);
        }

        if(!appearColorValue.startsWith("#")){
            appearColorValue="#fafafb";
        }
        String splitArrays[] = appearColorValue.split(",");
        Bitmap bitmap = BitmapUtils.getRectColors(splitArrays,
                BitmapUtils.dp2px(mContext, size), BitmapUtils.dp2px(mContext, size),
                R.color.style_bg4, 1);
        outImg.setImageBitmap(bitmap);

        String interiorColorValue = orderInfo.inColorValue;
        if(!interiorColorValue.startsWith("#")){
            interiorColorValue="#f0f0f0";
        }
         splitArrays = interiorColorValue.split(",");
           bitmap = BitmapUtils.getRectColors(splitArrays, BitmapUtils.dp2px(mContext, size),
                BitmapUtils.dp2px(mContext, size), R.color.style_bg4, 1);

        inImg.setImageBitmap(bitmap);
        StringBuilder lookingStr = new StringBuilder();
        if(TextUtil.isEmpty(orderInfo.out_color_name)&&TextUtil.isEmpty(orderInfo.in_color_name)) {
            lookingTv.setVisibility(View.GONE);
        }else {
            lookingTv.setVisibility(View.VISIBLE);
        }
        if(!TextUtil.isEmpty(orderInfo.out_color_name)){
            lookingStr.append("外观: ").append(TextUtil.getColorText("#323539",orderInfo.out_color_name)+"\t ");
        }
        if(!TextUtil.isEmpty(orderInfo.in_color_name)){
            lookingStr.append("内饰: ").append(TextUtil.getColorText("#323539",orderInfo.in_color_name));
        }
        lookingTv.setText(Html.fromHtml(lookingStr.toString()));

//        if(TextUtil.isEmpty(orderInfo.out_color_name)){
//
//        }else {
//            TextUtil.getColorText("#323539",orderInfo.out_color_name);
//            TextUtil.getColorText("#323539",orderInfo.in_color_name);
////            Html.fromHtml("外观 "+  TextUtil.getColorText("#323539",orderInfo.out_color_name)+
////                            "\t   内饰 "+TextUtil.getColorText("#323539",orderInfo.in_color_name));
//            lookingTv.setText(            Html.fromHtml("外观:"+  +
//                    "\t   内饰:"+TextUtil.getColorText("#323539",orderInfo.in_color_name)));
//        }

        String carNameStr= orderInfo.factoryName+" "+orderInfo.seriesName+" "+orderInfo.car_name;
        carNameTv.setText(carNameStr);
        titleNametv.setText(carNameStr+"-"+userInfo.nickname);
        oderDesc.setText(orderInfo.description);
        inputNameTv.setText(orderInfo.contact);
        brandIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(userInfo.masterBrand,360, 360)));
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
        return OrderSeeCarActivity.this;
    }

    @Override
    protected void onDestroy() {
        EaseConstant.IS_BUYCAR_TYPE =false;
        EaseConstant.IS_BUYCAR_MANAGE = false;
        super.onDestroy();
    }
}
