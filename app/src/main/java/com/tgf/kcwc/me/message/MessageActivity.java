package com.tgf.kcwc.me.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.GlobalConversationListFragment;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.login.LoginSevice;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.MessageListBean;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.MessagePresenter;
import com.tgf.kcwc.mvp.view.MessageListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.MyListView;
import com.tgf.kcwc.view.TipView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/18.
 */

public class MessageActivity extends BaseActivity implements MessageListView {

    private MessagePresenter mMessagePresenter;
    private MyListView mListView;

    private int page = 1;
    private boolean isRefresh = true;

    private List<MessageListBean.DataList> Datalist = new ArrayList<>(); //数据源
    private List<MessageListBean.DataList> StatisticsDatalist = new ArrayList<>(); //固定的数据
    private List<MessageListBean.DataList> PrivateDatalist = new ArrayList<>();//私有的数据
    private WrapAdapter<MessageListBean.DataList> mAdapter;

    private GlobalConversationListFragment mGlobalConversationListFragment;

    @Override
    protected void onResume() {
        super.onResume();
       // mMessagePresenter.getAppStatistics(IOUtils.getToken(mContext));
    }


    @Override
    protected void setUpViews() {
        mGlobalConversationListFragment = new GlobalConversationListFragment();
        mGlobalConversationListFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, mGlobalConversationListFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EaseConstant.IS_PRIVATE_ETTER=false;
        RxBus.$().unregister(Constants.IntentParams.MESSAGEREFRESH);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.messagecenter);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  importMessages();
            }
        });
        function.setImageResource(R.drawable.btn_more);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MorePopupwindowBean> dataList = new ArrayList<>();
                MorePopupwindowBean morePopupwindowBean = null;
                morePopupwindowBean = new MorePopupwindowBean();
                morePopupwindowBean.title = "全部标记为已读";
                dataList.add(morePopupwindowBean);
                MorePopupWindow morePopupWindow = new MorePopupWindow(MessageActivity.this, dataList,
                        new MorePopupWindow.MoreOnClickListener() {
                            @Override
                            public void moreOnClickListener(int position, MorePopupwindowBean item) {
                                mMessagePresenter.getRead(IOUtils.getToken(mContext));
                            }
                        });
                morePopupWindow.showPopupWindow(v);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EaseConstant.IS_PRIVATE_ETTER=true;
        setContentView(R.layout.activity_message);
        mListView = (MyListView) findViewById(R.id.list);
        mMessagePresenter = new MessagePresenter();
        mMessagePresenter.attachView(this);

        mAdapter = new WrapAdapter<MessageListBean.DataList>(mContext, R.layout.activity_message_item, Datalist) {
            @Override
            public void convert(ViewHolder helper, MessageListBean.DataList item) {
                SimpleDraweeView mAvatar = helper.getView(R.id.avatar);
                TipView number = helper.getView(R.id.number);
                TextView name = helper.getView(R.id.name);
                TextView title = helper.getView(R.id.title);
                TextView time = helper.getView(R.id.time);
                if (item.selectype == 1) {
                    if (item.type == 10) {
                        mAvatar.setImageURI(URLUtil.builderImgUrl(item.avatar, 144, 144));
                    } else {
                        mAvatar.setImageResource(item.imagenum);
                    }
                    if (item.number > 0) {
                        number.setVisibility(View.VISIBLE);
                        number.setText(item.number);
                    } else {
                        number.setVisibility(View.GONE);
                    }
                    name.setText(item.name);
                    title.setText(item.title);
                    time.setText(item.time);
                } else {
                    mAvatar.setImageURI(URLUtil.builderImgUrl(item.avatar, 144, 144));
                    if (item.count > 0) {
                        number.setText(item.count);
                    } else {
                        number.setVisibility(View.VISIBLE);
                    }
                    name.setText(item.uname);
                    title.setText(item.content);
                    time.setText(item.createtime);

                }

            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Datalist.get(position).selectype == 1) {
                    SystemSelec(Datalist.get(position).type, Datalist.get(position).type + "", Datalist.get(position).eventId);
                } else {
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put("type", Datalist.get(position).letteruser + "");
                    args.put("name", Datalist.get(position).uname);
                    CommonUtils.startNewActivity(mContext, args, PrivateMessagesActivity.class);
                }

            }
        });
        mMessagePresenter.getAppStatistics(IOUtils.getToken(mContext));
        //mMessagePresenter.getPrivateletterList(IOUtils.getToken(mContext), page);
    }

    /**
     * type   1提醒，提示3看车4代金券门票5评论6点赞7活动
     *
     * @param number
     */
    public void SystemSelec(int number, String type, int event_id) {
        Map<String, Serializable> args = null;
        switch (number) {
            case 1:
                args = new HashMap<String, Serializable>();
                args.put("type", type);
                args.put("name", "系统消息");
                CommonUtils.startNewActivity(mContext, args, SystemMessagesActivity.class);
                break;
            case 3:
                args = new HashMap<String, Serializable>();
                args.put("type", type);
                args.put("name", "看车");
                CommonUtils.startNewActivity(mContext, args, SystemMessagesActivity.class);
                break;
            case 4:
                args = new HashMap<String, Serializable>();
                args.put("type", type);
                args.put("name", "门票");
                CommonUtils.startNewActivity(mContext, args, SystemMessagesActivity.class);
                break;
            case 5:
                args = new HashMap<String, Serializable>();
                args.put("type", type);
                args.put("name", "评论");
                CommonUtils.startNewActivity(mContext, args, SystemMessagesActivity.class);
                break;
            case 6:
                args = new HashMap<String, Serializable>();
                args.put("type", type);
                args.put("name", "点赞");
                CommonUtils.startNewActivity(mContext, args, SystemMessagesActivity.class);
                break;
            case 7:
                args = new HashMap<String, Serializable>();
                args.put("type", type);
                args.put("name", "活动");
                CommonUtils.startNewActivity(mContext, args, SystemMessagesActivity.class);
                break;
            case 8:
                args = new HashMap<String, Serializable>();
                args.put("type", type);
                args.put("name", "票证");
                CommonUtils.startNewActivity(mContext, args, SystemMessagesActivity.class);
                break;
            case 10:
                args = new HashMap<String, Serializable>();
                args.put("type", type);
                args.put("name", "车主自售");
                args.put("event_id", event_id);
                CommonUtils.startNewActivity(mContext, args, SystemMessagesActivity.class);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void StatisticsListSucceed(MessageListBean messageListBean) {
        StatisticsDatalist.clear();
        List<MessageListBean.DataList> lists = messageListBean.data.lists;
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).selectype = 1;
            switch (lists.get(i).type) {
                case 1:
                    lists.get(i).imagenum = R.drawable.btn_laba;
                    break;
                case 3:
                    lists.get(i).imagenum = R.drawable.btn;
                    break;
                case 4:
                    lists.get(i).imagenum = R.drawable.btn_pink;
                    break;
                case 5:
                    lists.get(i).imagenum = R.drawable.btn_xinxi;
                    break;
                case 6:
                    lists.get(i).imagenum = R.drawable.btn_zan;
                    break;
                case 7:
                    lists.get(i).imagenum = R.drawable.btn_huodong;
                    break;
                case 8:
                    lists.get(i).imagenum = R.drawable.btn_piao;
                    break;
                case 10:
                    //lists.get(i).imagenum = R.drawable.btn_piao;
                    break;
            }
            StatisticsDatalist.add(lists.get(i));
        }
        setData();
    }

    @Override
    public void PrivateletterListSucceed(MessageListBean messageListBean) {
        stopRefreshAll();
        List<MessageListBean.DataList> lists = new ArrayList<>();
        lists.addAll(messageListBean.data.lists);
        if (page == 1) {
            PrivateDatalist.clear();
        }
        if (lists.size() == 0) {
            isRefresh = false;
        }
        for (MessageListBean.DataList dataList : lists) {
            dataList.selectype = 2;
            PrivateDatalist.add(dataList);
        }
        setData();
    }

    @Override
    public void ReadSucceed(BaseBean baseBean) {
        mMessagePresenter.getAppStatistics(IOUtils.getToken(mContext));
    }

    public void setData() {
        Datalist.clear();
        Datalist.addAll(StatisticsDatalist);
        Datalist.addAll(PrivateDatalist);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
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
        return mContext;
    }

    /**
     * 导入多条消息
     */
    private void importMessages() {
        String msgJson = "[{\n"
                + "    \"from\": \"lz1\",\n"
                + "    \"msg_id\": \"350475701050148824\",\n"
                + "    \"payload\": {\n"
                + "        \"ext\": {},\n"
                + "        \"bodies\": [{\n"
                + "            \"secret\": \"lRjEWmCoEee9vfnPereH2W_JZYRVXhhnyCYX7ejQ5Y2Va6SN\",\n"
                + "            \"size\": {\n"
                + "                \"width\": 288,\n"
                + "                \"height\": 384\n"
                + "            },\n"
                + "            \"filename\": \"1499166285637\",\n"
                + "            \"url\": \"https://a1.easemob.com/1188170313178478/youhaodongxi/chatfiles/9518c450-60a8-11e7-8229-e3a51fcf21a3\",\n"
                + "            \"type\": \"img\"\n"
                + "        }]\n"
                + "    },\n"
                + "    \"direction\": \"\",\n"
                + "    \"timestamp\": \"1499166285638\",\n"
                + "    \"to\": \"19369592684545\",\n"
                + "    \"chat_type\": \"GroupChat\"\n"
                + "},\n"
                + "{\n"
                + "    \"from\": \"lz2\",\n"
                + "    \"msg_id\": \"350475800664868824\",\n"
                + "    \"payload\": {\n"
                + "        \"ext\": {},\n"
                + "        \"bodies\": [{\n"
                + "            \"secret\": \"ouuV2mCoEeeZx7_YUlvSbZggsYQGkVkok97rpEyJDKPm5j3P\",\n"
                + "            \"length\": 2,\n"
                + "            \"thumb_secret\": \"oqzfKmCoEeebQ9fCtwzh9qz80EMC-5Aa4SIzlVf1yFGHox4o\",\n"
                + "            \"size\": {\n"
                + "                \"width\": 360,\n"
                + "                \"height\": 480\n"
                + "            },\n"
                + "            \"file_length\": 258008,\n"
                + "            \"filename\": \"149916630777674.mp4\",\n"
                + "            \"thumb\": \"https://a1.easemob.com/1188170313178478/youhaodongxi/chatfiles/a2acdf20-60a8-11e7-b726-ab0d6e300107\",\n"
                + "            \"type\": \"video\",\n"
                + "            \"url\": \"https://a1.easemob.com/1188170313178478/youhaodongxi/chatfiles/a2eb95d0-60a8-11e7-b6a5-073d17c5638d\"\n"
                + "        }]\n"
                + "    },\n"
                + "    \"timestamp\": \"1499166307402\",\n"
                + "    \"to\": \"19369592684545\",\n"
                + "    \"chat_type\": \"GroupChat\"\n"
                + "},\n"
                + "{\n"
                + "    \"chat_type\": \"GroupChat\",\n"
                + "    \"direction\": \"SEND\",\n"
                + "    \"from\": \"lz3\",\n"
                + "    \"msg_id\": \"350476658324539380\",\n"
                + "    \"payload\": {\n"
                + "        \"bodies\": [{\n"
                + "            \"file_length\": \"1.94KB\",\n"
                + "            \"filename\": \"yhdx-4620170704T190822.amr\",\n"
                + "            \"length\": \"2\",\n"
                + "            \"secret\": \"GfcMmmCpEeepRAuL5W3NLlab-3kBku0jcUNleNjnuNxbSoB0\",\n"
                + "            \"type\": \"audio\",\n"
                + "            \"url\": \"https://a1.easemob.com/1188170313178478/youhaodongxi/chatfiles/19f70c90-60a9-11e7-9927-df857dc80880\"\n"
                + "        }]\n"
                + "    },\n"
                + "    \"timestamp\": \"1499166505987\",\n"
                + "    \"to\": \"19369592684545\"\n"
                + "},\n"
                + "{\n"
                + "    \"chat_type\": \"GroupChat\",\n"
                + "    \"direction\": \"SEND\",\n"
                + "    \"from\": \"lz4\",\n"
                + "    \"msg_id\": \"350476682890577908\",\n"
                + "    \"payload\": {\n"
                + "        \"bodies\": [{\n"
                + "            \"filename\": \"image-1849353007.jpg\",\n"
                + "            \"secret\": \"HVLhymCpEeeBP8fLp1zGL-bWFjrve4Hclkni9XVDriCrrB2w\",\n"
                + "            \"size\": {\n"
                + "                \"height\": 1263,\n"
                + "                \"width\": 840\n"
                + "            },\n"
                + "            \"type\": \"img\",\n"
                + "            \"url\": \"https://a1.easemob.com/1188170313178478/youhaodongxi/chatfiles/1d52e1c0-60a9-11e7-94a9-071c28363fe3\"\n"
                + "        }]\n"
                + "    },\n"
                + "    \"timestamp\": \"1499166511701\",\n"
                + "    \"to\": \"19369592684545\"\n"
                + "},\n"
                + "{\n"
                + "    \"chat_type\": \"GroupChat\",\n"
                + "    \"direction\": \"SEND\",\n"
                + "    \"from\": \"lz5\",\n"
                + "    \"msg_id\": \"350461429712685056\",\n"
                + "    \"payload\": {\n"
                + "        \"bodies\": [{\n"
                + "            \"filename\": \"image269235292.jpg\",\n"
                + "            \"secret\": \"2GwAimCgEeepbS3Xzirr2IlxF_VoO4i_dByB0XMIIDx-UNqG\",\n"
                + "            \"size\": {\n"
                + "                \"height\": 360,\n"
                + "                \"width\": 640\n"
                + "            },\n"
                + "            \"type\": \"img\",\n"
                + "            \"url\": \"https://a1.easemob.com/1188170313178478/youhaodongxi/chatfiles/d86c0080-60a0-11e7-ade1-6dfb6ad635c4\"\n"
                + "        }]\n"
                + "    },\n"
                + "    \"timestamp\": \"1499162960283\",\n"
                + "    \"to\": \"19369592684545\"\n"
                + "},\n"
                + "{\n"
                + "    \"from\": \"lz6\",\n"
                + "    \"msg_id\": \"350445529139775524\",\n"
                + "    \"payload\": {\n"
                + "        \"ext\": {},\n"
                + "        \"bodies\": [{\n"
                + "            \"msg\": \"忒MSN\",\n"
                + "            \"type\": \"txt\"\n"
                + "        }]\n"
                + "    },\n"
                + "    \"timestamp\": \"1499159258091\",\n"
                + "    \"to\": \"19369592684545\",\n"
                + "    \"chat_type\": \"GroupChat\"\n"
                + "},\n"
                + "{\n"
                + "    \"from\": \"lz7\",\n"
                + "    \"msg_id\": \"350445455370360868\",\n"
                + "    \"payload\": {\n"
                + "        \"ext\": {},\n"
                + "        \"bodies\": [{\n"
                + "            \"msg\": \"测试\",\n"
                + "            \"type\": \"txt\"\n"
                + "        }]\n"
                + "    },\n"
                + "    \"timestamp\": \"1499159240911\",\n"
                + "    \"to\": \"19369592684545\",\n"
                + "    \"chat_type\": \"GroupChat\"\n"
                + "}]";
        List<EMMessage> messageList = new ArrayList<EMMessage>();
        try {
            JSONArray jsonArray = new JSONArray(msgJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long timestamp = jsonObject.optLong("timestamp");
               /* String from = jsonObject.optString("from");
                String to = jsonObject.optString("to");*/
                String from = "im_development_827_kcwc";
                String to = "im_development_110_kcwc";
/*                String from = "im_development_827_kcwc";
                String to = "im_development_618_kcwc";*/
                String msgId = jsonObject.optString("msg_id");

                JSONObject bodyObject = jsonObject.optJSONObject("payload").optJSONArray("bodies").getJSONObject(0);
                String type = bodyObject.optString("type");
                EMMessage message = null;
                if (type.equals("txt")) {
                    if (!from.equals("im_development_110_kcwc")) {
                        message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                    } else {
                        message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                    }
                    EMTextMessageBody body = new EMTextMessageBody(bodyObject.optString("msg"));
                    message.addBody(body);
                } else if (type.equals("video")) {
                    if (!from.equals("im_development_110_kcwc")) {
                        message = EMMessage.createReceiveMessage(EMMessage.Type.VIDEO);
                    } else {
                        message = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
                    }
                    EMVideoMessageBody body = new EMVideoMessageBody();
                    body.setThumbnailUrl(bodyObject.optString("thumb"));
                    body.setThumbnailSecret(bodyObject.optString("thumb_secret"));
                    body.setRemoteUrl(bodyObject.optString("url"));
                    body.setVideoFileLength(bodyObject.optLong("file_length"));
                    body.setSecret(bodyObject.optString("secret"));
                    message.addBody(body);
                } else if (type.equals("audio")) {
                    if (!from.equals("im_development_110_kcwc")) {
                        message = EMMessage.createReceiveMessage(EMMessage.Type.VOICE);
                    } else {
                        message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
                    }
                    File file = new File("");
                    EMVoiceMessageBody body = new EMVoiceMessageBody(file, bodyObject.optInt("length"));
                    body.setRemoteUrl(bodyObject.optString("url"));
                    body.setSecret(bodyObject.optString("secret"));
                    body.setFileName(bodyObject.optString("filename"));
                    message.addBody(body);
                } else if (type.equals("img")) {
                    if (!from.equals("im_development_110_kcwc")) {
                        message = EMMessage.createReceiveMessage(EMMessage.Type.IMAGE);
                    } else {
                        message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
                    }
                    File file = new File("");
                    // 这里使用反射获取 ImageBody，为了设置 size
                    Class<?> bodyClass = Class.forName("com.hyphenate.chat.EMImageMessageBody");
                    Class<?>[] parTypes = new Class<?>[1];
                    parTypes[0] = File.class;
                    Constructor<?> constructor = bodyClass.getDeclaredConstructor(parTypes);
                    Object[] pars = new Object[1];
                    pars[0] = file;
                    EMImageMessageBody body = (EMImageMessageBody) constructor.newInstance(pars);
                    Method setSize = Class.forName("com.hyphenate.chat.EMImageMessageBody")
                            .getDeclaredMethod("setSize", int.class, int.class);
                    setSize.setAccessible(true);
                    int width = bodyObject.optJSONObject("size").optInt("width");
                    int height = bodyObject.optJSONObject("size").optInt("height");
                    setSize.invoke(body, width, height);

                    body.setFileName(bodyObject.optString("filename"));
                    body.setSecret(bodyObject.optString("secret"));
                    body.setRemoteUrl(bodyObject.optString("url"));
                    body.setThumbnailUrl(bodyObject.optString("thumb"));
                    message.addBody(body);
                }
                message.setFrom(from);
                message.setTo(to);
                message.setMsgTime(timestamp);
                message.setMsgId(msgId);
                message.setChatType(EMMessage.ChatType.GroupChat);
                message.setStatus(EMMessage.Status.SUCCESS);
                messageList.add(message);
            }
            Logger.e("TT" + "他有之前有多少" + EMClient.getInstance().chatManager().getAllConversations().size());
            EMClient.getInstance().chatManager().importMessages(messageList);
            int logout = EMClient.getInstance().logout(true);
//            startService(new Intent(getContext(), LoginSevice.class));
            Logger.e("TT" + "他有之后有多少" + EMClient.getInstance().chatManager().getAllConversations().size());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载更多消息
     */
    private void loadMoreMessage() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation("19369592684545", EMConversation.EMConversationType.GroupChat, true);
        String msgId = conversation.getAllMessages().get(0).getMsgId();
        List<EMMessage> list = conversation.loadMoreMsgFromDB(msgId, 20);
        // VMLog.d("load more message result %d, all msg count %d", list.size(), conversation.getAllMsgCount());
    }

}
