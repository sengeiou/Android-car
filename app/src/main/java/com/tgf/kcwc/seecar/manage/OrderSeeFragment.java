package com.tgf.kcwc.seecar.manage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EmmessageTypeUtil;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.iseechat.ChatOrderFellowCommentRow;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Account;
import com.hyphenate.easeui.BuycarEmConversationMannage;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.NotitleContentOneBtnDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Auther: Scott
 * Date: 2017/3/7 0007
 * E-mail:hekescott@qq.com
 */

public class OrderSeeFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {
    private int MESSAGE_TYPE_CLOSEORDER = 1;
    private int MESSAGE_TYPE_COMMENT = 2;
    private int MESSAGE_TYPE_TIPS = 3;
    private final String KEY_CHAT_ORDERCLOSE = "close_order";
    private final String KEY_CHAT_ORDERCOMMENT = "comment";
    private final String KEY_CHAT_ORDERTYPE = "type";
    private OrderSeeCarActivity orderSeeMotoActivity;
    private GestureDetector myGesture;
    NotitleContentOneBtnDialog notitleContentDialog;
    private String orderId;
    private Bundle fromBundle;
    private String autoBodyMsg;

    @Override
    protected void setUpView() {
        hideTitleBar();
        setChatFragmentHelper(this);
        super.setUpView();
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myGesture.onTouchEvent(event);
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BuycarEmConversationMannage.getInstace().clearMessages();
        orderSeeMotoActivity = (OrderSeeCarActivity) getActivity();
        myGesture = new GestureDetector(getActivity(), new MyGestureListener());
        notitleContentDialog = new NotitleContentOneBtnDialog(getActivity());
        fromBundle = getArguments();
        orderId = fromBundle.getString(Constant.EXTRA_USER_ORDER);
        autoBodyMsg = fromBundle.getString(Constants.IntentParams.DATA);
        EaseConstant.BUYCAR_ORDERID = "buycar_" + orderId + "_";
        View createView = super.onCreateView(inflater, container, savedInstanceState);
//        orderSeeMotoActivity.backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = "如果您不想被Ta打扰，可“免TA打扰”关闭对话";
//                EMMessage message2 = EMMessage.createTxtSendMessage(content, toChatUsername);
////                    sendMessage(message2);
//                sendCustomeMsg(message2, content, "tip");
//            }
//        });
        return createView;

    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        Account account = IOUtils.getAccount(getActivity());
        String userPic = URLUtil.builderImgUrl(account.userInfo.avatar, 360, 360);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", EaseConstant.BUYCAR_ORDERID);
//            jsonObject.put("type", "order");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.setAttribute("model", "buycar");
        message.setAttribute("data", jsonObject);
        if (!TextUtils.isEmpty(userPic)) {
            message.setAttribute("userPic", userPic);
        }
        //String userName = AppSPUtils.getValueFromPrefrences("name", "");
        String nickName = account.userInfo.nickName;
        if (!TextUtils.isEmpty(nickName)) {
            message.setAttribute("userName", nickName);
        }
        Logger.d("easechat msg order_id"+ EaseConstant.BUYCAR_ORDERID);
        Logger.d("easechat msg body"+ message.getBody());
        Logger.d(message);
    }

    boolean isSendAuto;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String content = "如果您不想被Ta打扰，可“免TA打扰”关闭对话";
                    EMMessage message2 = EMMessage.createTxtSendMessage(content, toChatUsername);
//                    sendMessage(message2);
                    sendCustomeMsg(message2, content, "tip");
                    break;
                default:
                    break;
            }

            return false;
        }
    });

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtil.isEmpty(autoBodyMsg)) {
            if (!isSendAuto) {
                EMMessage message = EMMessage.createTxtSendMessage(autoBodyMsg, toChatUsername);
                sendCustomeMsg(message, autoBodyMsg, "");
                mHandler.sendEmptyMessageDelayed(1, 1000);
                isSendAuto = true;
            }
//            EMMessage message2 = EMMessage.createTxtSendMessage("如果您不想被Ta打扰，可“免TA打扰”关闭对话", toChatUsername);
//            sendMessage(message2);
//            String content = autoBodyMsg;
//            sendCustomeMsg(message, content);
//            sendCustomeMsg(message2, content);


        }
//        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
//        EMMessage message = EMMessage.createTxtSendMessage("ingodkng", toChatUsername);
////如果是群聊，设置chattype，默认是单聊
////        if (chatType == CHATTYPE_GROUP)
////            message.setChatType(ChatType.GroupChat);
////发送消息
//        EMClient.getInstance().chatManager().sendMessage(message);
    }

    private void sendCustomeMsg(EMMessage message, String content, String type) {
        try {
            message.setAttribute("model", "buycar");
            JSONObject dataJson = new JSONObject();
            dataJson.put("type", type);
            dataJson.put("order_id", EaseConstant.BUYCAR_ORDERID);
            dataJson.put("content", content);
            message.setAttribute("data", dataJson);
            sendCustomeMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {

//        return null;
        return new MyCustomChatRowProvider();
    }

    private class MyCustomChatRowProvider implements EaseCustomChatRowProvider {

        @Override
        public int getCustomChatRowTypeCount() {

            return 2;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                int emmessageTypeTip =  EmmessageTypeUtil.isEmmessageTypeTip(message);

                if (emmessageTypeTip==2) {
                    return MESSAGE_TYPE_CLOSEORDER;
                } else if (emmessageTypeTip==3) {
                    return MESSAGE_TYPE_COMMENT;
                } else if (emmessageTypeTip==4) {
                    notitleContentDialog.setContentText("订单已结束");
                    notitleContentDialog.setOnLoginOutClickListener(new NotitleContentOneBtnDialog.IOnclickLisenter() {
                        @Override
                        public void OkClick() {
                            getActivity().finish();
                        }
                    });
                    notitleContentDialog.show();
                }

            }

            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {


            if (message.getType() == EMMessage.Type.TXT) {

                int emmessageTypeTip =  EmmessageTypeUtil.isEmmessageTypeTip(message);
                Logger.d("easechat getCustomChatRow0");
                if (emmessageTypeTip ==2) {
                    return new ChatOrderCloseRow(getActivity(), message, position, adapter);
                } else if (emmessageTypeTip==3) {
                    return new ChatOrderFellowCommentRow(getActivity(), message, position, adapter);
                }
            }
            Logger.d("easechat getCustomChatRow");
            return null;
        }
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            orderSeeMotoActivity.chatmenulayout.setVisibility(View.VISIBLE);
            inputMenu.onBackPressed();
            hideKeyboard();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            orderSeeMotoActivity.customerll.setVisibility(View.GONE);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            orderSeeMotoActivity.customerll.setVisibility(View.GONE);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

}
