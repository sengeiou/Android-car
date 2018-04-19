package com.tgf.kcwc.seecar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EmmessageTypeUtil;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.iseechat.ChatOrderCloseRow;
import com.tgf.kcwc.iseechat.ChatOrderFellowCommentRow;
import com.tgf.kcwc.iseechat.ChatTipRow;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.seecar.OrderFellowActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.seecar.manage.SeecarMEaseChatFrag;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/3/1 0001
 * E-mail:hekescott@qq.com
 */

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {
    private int MESSAGE_TYPE_TIP = 1;
    private int MESSAGE_TYPE_CLOSEORDER = 2;
    private int MESSAGE_TYPE_COMMENT = 3;
    private OrderFellowActivity orderFellowActivity;
    private int isFirst =0;
    GestureDetector myGesture ;
    private final String ChatUserPic ="userPic";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myGesture = new GestureDetector(getActivity(),new MyOnGestureListener());

    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        super.setUpView();
        orderFellowActivity = (OrderFellowActivity) getActivity();
        titleBar.setVisibility(View.GONE);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                myGesture.onTouchEvent(event);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         Bundle fromBundle = getArguments();
       String orderId = fromBundle.getString(Constant.EXTRA_USER_ORDER);
        EaseConstant.BUYCAR_ORDERID = "buycar_" + orderId+"_";
        Logger.d("easechat orderId =="+ EaseConstant.BUYCAR_ORDERID);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setInputVisble(){
        inputMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

        Account account = IOUtils.getAccount(getActivity());
        String userPic = URLUtil.builderImgUrl(account.userInfo.avatar, 360, 360);

        if (!TextUtils.isEmpty(userPic)) {
            message.setAttribute("userPic", userPic);
        }
        //String userName = AppSPUtils.getValueFromPrefrences("name", "");
        String userName = account.userInfo.nickName;
        if (!TextUtils.isEmpty(userName)) {
            message.setAttribute("userName", userName);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", EaseConstant.BUYCAR_ORDERID);
//            jsonObject.put("type", "order");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.d("easechat"+message.getBody());
        Logger.d(message);
        message.setAttribute("model", "buycar");
        message.setAttribute("data", jsonObject);
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



        return new MyEaseCustomChatRowProvider();
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        super.onMessageReceived(messages);
        Logger.d("ChatFragment",messages);
//Todo
//        String avatarUrl = messages.getStringAttribute(SharePrefConstant.ChatUserPic);
    }

    private class MyEaseCustomChatRowProvider implements EaseCustomChatRowProvider {

        @Override
        public int getCustomChatRowTypeCount() {
            return 3;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
        Logger.d("message "+message.getBody().toString());
            if(message.getType() == EMMessage.Type.TXT){
                int messageType = EmmessageTypeUtil.isEmmessageTypeTip(message);
                    if(messageType==1){
                            return MESSAGE_TYPE_TIP;
                    }else if (messageType==2){
                        orderFellowActivity.gotoComment.setVisibility(View.VISIBLE);
                        return MESSAGE_TYPE_CLOSEORDER;
                    }else  if (messageType==3){
                                    return  MESSAGE_TYPE_COMMENT;
                    }
//                return ;
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            Logger.d("getCustomChatRow"+message.getFrom());

            if(message.getType() == EMMessage.Type.TXT){
//                String tip = message.getStringAttribute("tip","");
//                if(!TextUtils.isEmpty(tip)){
                int messageType = EmmessageTypeUtil.isEmmessageTypeTip(message);
                if(messageType==1){
                    return new ChatTipRow(getActivity(),message,position,adapter);
                }else if (messageType==2){
                    return new ChatOrderCloseRow(getActivity(),message,position,adapter);
                }else  if (messageType==3){
                    return  new ChatOrderFellowCommentRow(getActivity(),message,position,adapter);
                }
            }

            return null;
        }
    }


    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            orderFellowActivity.chatOrgInfo.setVisibility(View.GONE);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
//            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(orderFellowActivity.btnSend.getWindowToken(), 0);
            orderFellowActivity.chatMenull.setVisibility(View.VISIBLE);
            inputMenu.onBackPressed();
            hideKeyboard();

            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            orderFellowActivity.chatOrgInfo.setVisibility(View.GONE);

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
