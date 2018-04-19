package com.tgf.kcwc.coupon.manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgf.kcwc.R;
import com.tgf.kcwc.WeChatActivity;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.CheckSendSeeModel;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.model.CouponEventModel;
import com.tgf.kcwc.mvp.model.SendCouponModel;
import com.tgf.kcwc.mvp.presenter.CouponSendPresenter;
import com.tgf.kcwc.mvp.view.CouponSendSeeView;
import com.tgf.kcwc.ticket.ContactActivity;
import com.tgf.kcwc.ticket.MyFriendActivity;
import com.tgf.kcwc.ticket.manage.TicketSendSeeActivity;
import com.tgf.kcwc.ticket.widget.SendSeeDialog;
import com.tgf.kcwc.ticket.widget.TicketSendWindow;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.Validator;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tgf.kcwc.R.id.radioGroup;

/**
 * Auther: Scott
 * Date: 2017/2/10 0010
 * E-mail:hekescott@qq.com
 */

public class CouponSendSeeActivity extends BaseActivity
        implements  CouponSendSeeView {
    private static final String TAG = "TicketSendSeeActivity";
    private  final String KEY_INTENT_TYPE= "type";
    private  final int VAULE_INTENT_COUPON= 2;
    private ArrayList<ContactUser> contactUsers = new ArrayList<ContactUser>();
    private int timeLimit =-1;
    private TicketSendWindow ticketSendWindow;
    private static final String KEY_INTENT_LISTCONTACT = "select_contact";
    private final String KEY_INTENT_MOBILE = "mobile";
    private SendSeeDialog sendSeeDialog;
    private int mTicketId;
    private int distributeId;
    private TextView limitNumTv;
    /**
     * 创建侧滑菜单项
     */
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            deleteItem.setBackground(new ColorDrawable(mRes.getColor(R.color.text_color36)));
            deleteItem.setWidth(DensityUtils.dp2px(mContext, 40));
            deleteItem.setTitle("删除");
            deleteItem.setTitleSize(18);
            deleteItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(deleteItem);
        }
    };
    SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            int size = contactUsers.size();
            if (size > 0) {
                contactUsers.remove(position);
                mUserAdapter.notifyDataSetChanged();
            }
            return false;
        }
    };
    private SwipeMenuListView mTicketListView;
    private WrapAdapter mUserAdapter;
    private Intent fromIntent;
    private CouponSendPresenter mSendSeePresneter;
    private MyGridView loseTimeGv;
    private ArrayList<DataItem> limitDataList;
    private WrapAdapter limitTimeAdapter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("代金券分发预览");

    }

    @Override
    protected void setUpViews() {
        String doc = "\n" +
                "1、每张代金券有领取时限，若超过时限，则该代金券“失效”；\n" +
                "2、领取后的代金券有“使用有效期”，超过有效期未使用则为“过期”；\n" +
                "3、代金券若在线上抵扣购买门票等产品，表明用户“核销”了该代金券，但抵扣购买的商品是否核销，我们会持续跟踪。\n";
       TextView textViewDoc = (TextView) findViewById(R.id.ticket_doctv);
        textViewDoc.setText(doc);
        mSendSeePresneter = new CouponSendPresenter();
        mSendSeePresneter.attachView(this);
        fromIntent = getIntent();
        sendSeeDialog = new SendSeeDialog(getContext());
        initData();
        findViewById(R.id.sendsee_addbtn).setOnClickListener(this);
        findViewById(R.id.sendsee_submit).setOnClickListener(this);
        loseTimeGv = (MyGridView) findViewById(R.id.sendsee_lostTimeGv);
        initSendSeeLimitTime();
        //设置侧滑菜项
        mTicketListView = (SwipeMenuListView) findViewById(R.id.swipe_ticketlv);
        mTicketListView.setMenuCreator(creator);
        mTicketListView.setOnMenuItemClickListener(onMenuItemClickListener);
        limitNumTv = (TextView) findViewById(R.id.sendsee_limiNumtv);

    }

    private void initSendSeeLimitTime() {
        limitDataList = new ArrayList<>();
        limitDataList.add(new DataItem(12,"12小时"));
        limitDataList.add(new DataItem(24,"24小时"));
        limitDataList.add(new DataItem(48,"48小时"));
        limitDataList.add(new DataItem(72,"72小时"));
        limitDataList.add(new DataItem(0,"永不失效"));

        limitTimeAdapter = new WrapAdapter<DataItem>(getContext(), R.layout.sendsee_limittime_girditem,limitDataList) {
            @Override
            public void convert(final ViewHolder helper, DataItem item) {
                TextView limitTimeTv =     helper.getView(R.id.sendsee_limitTimeBtn);
                limitTimeTv.setText(item.name);
                if(item.isSelected){
                    limitTimeTv.setBackgroundResource(R.drawable.shape_sendsee_bordergreen);
                    limitTimeTv.setTextColor(mRes.getColor(R.color.white));
                }else {
                    limitTimeTv.setTextColor(mRes.getColor(R.color.text_color3));
                    limitTimeTv.setBackgroundResource(R.drawable.shape_sendsee_borderwhite);
                }
                limitTimeTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeLimit = limitDataList.get(helper.getPosition()).id;
                        limitDataList.get(helper.getPosition()).isSelected=true;
                        singleChecked(limitDataList,limitDataList.get(helper.getPosition()));
                        limitTimeAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        loseTimeGv.setAdapter(limitTimeAdapter);
//        loseTimeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }
//    private void singleChecked(List<DataItem> items, DataItem item) {
//        for (DataItem d : items) {
//            if (d.id != item.id) {
//                d.isSelected = false;
//            }
//        }
//    }
    private void initData() {
        mTicketId = SharedPreferenceUtil.getTFId(getContext());
        distributeId = SharedPreferenceUtil.getDistributeId(getContext());
        mSendSeePresneter.getTicketExhibitInfo(IOUtils.getToken(getContext()), mTicketId);
        int mobile = fromIntent.getIntExtra(KEY_INTENT_MOBILE, 0);
        sendSeeDialog.setYesOnclickListener("确认", new SendSeeDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (Validator.isMobile(sendSeeDialog.getNums())) {
                    ContactUser contactUser = new ContactUser();
                    contactUser.isSelected = true;
                    contactUser.mobile = sendSeeDialog.getNums();
                    for(ContactUser forcontactUser1:contactUsers){
                        if(forcontactUser1.mobile.equals(contactUser.mobile)){
                            CommonUtils.showToast(getContext(), "该用户已添加");
                            return;
                        }
                    }

                    contactUsers.add(contactUser);
                    checkContact();
                    mUserAdapter.notifyDataSetChanged();
                    sendSeeDialog.dismiss();
                } else {
                    CommonUtils.showToast(getContext(), "输入正确手机号");
                }
            }
        });
        sendSeeDialog.setNoOnclickListener("取消", new SendSeeDialog.onNoOnclickListener() {

            @Override
            public void onNoClick() {
                sendSeeDialog.dismiss();
            }
        });
        if (mobile == 1) {
            sendSeeDialog.show();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsee);
      TextView sendSeeMsgTv = (TextView) findViewById(R.id.sendsee_loseTimeMsgtv);
        sendSeeMsgTv.setText("（领取失效后，劵自动回收）");
        Serializable selctUser = fromIntent.getSerializableExtra(KEY_INTENT_LISTCONTACT);
        if (selctUser != null) {
            contactUsers = (ArrayList<ContactUser>) selctUser;
        }
        checkContact();

        if (mUserAdapter == null) {
            mUserAdapter = new WrapAdapter<ContactUser>(CouponSendSeeActivity.this, contactUsers,
                    R.layout.listitem_sendsee_user) {



                @Override
                public void convert(ViewHolder helper, final ContactUser item) {
                    final int position = helper.getPosition();
                    if(TextUtil.isEmpty(item.name)){
                        helper.setText(R.id.sendsee_desctv,item.mobile );
                    }else{
                        helper.setText(R.id.sendsee_desctv, item.name + "(" + item.mobile + ")");
                    }
                     TextView numText = helper.getView(R.id.sendsee_nums);
                    ImageView addIv =  helper.getView(R.id.sendsee_add);
                    ImageView reduceIv =  helper.getView(R.id.sendsee_reduce);

                    if(item.maxNum ==0){
                        numText.setText("已超");
                        numText.setTextColor(mRes.getColor(R.color.red));
                        reduceIv.setVisibility(View.INVISIBLE);
                        addIv.setVisibility(View.INVISIBLE);
                    }else {
                        numText.setText(item.num + "");
                        numText.setTextColor(mRes.getColor(R.color.text_color3));
                        reduceIv.setVisibility(View.VISIBLE);
                        addIv.setVisibility(View.VISIBLE);
                        reduceIv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int num = contactUsers.get(position).num;
                                if (num < 2) {
                                    num = 1;
                                    CommonUtils.showToast(getContext(), "已经不能在减了");
                                } else {
                                    num = num - 1;
                                }
                                contactUsers.get(position).num = num;
                                mUserAdapter.notifyDataSetChanged(contactUsers);
                            }
                        });
                        addIv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int num = contactUsers.get(position).num;
                                if (num >= item.maxNum) {
                                    CommonUtils.showToast(getContext(), "已超过限领量了");
                                } else {
                                    num = num + 1;
                                }
                                contactUsers.get(position).num = num;
                                mUserAdapter.notifyDataSetChanged(contactUsers);
                            }
                        });
                    }


                }


            };
            mTicketListView.setAdapter(mUserAdapter);
        } else {
            mUserAdapter.notifyDataSetChanged(contactUsers);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if(requestCode == Constants.IntentParams.REQUEST_CODE){
                Serializable selctUser = data.getSerializableExtra(KEY_INTENT_LISTCONTACT);
                if (selctUser != null) {
                    contactUsers = (ArrayList<ContactUser>) selctUser;
                }
                checkContact();
                mUserAdapter.notifyDataSetChanged(contactUsers);
            }else if(requestCode ==Constants.IntentParams.REQUEST_CODE_2){
                Serializable selctUser = data.getSerializableExtra(KEY_INTENT_LISTCONTACT);
                if (selctUser != null) {
                    contactUsers = (ArrayList<ContactUser>) selctUser;
                }
                checkContact();
                mUserAdapter.notifyDataSetChanged(contactUsers);
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendsee_addbtn:
                if (ticketSendWindow == null) {
                    ticketSendWindow = new TicketSendWindow(this);
                    ticketSendWindow.setOnClickListener(mListener);
                }
                ticketSendWindow.show(this);
                break;
            case R.id.sendsee_submit:
                if(timeLimit==-1){
                    CommonUtils.showToast(getContext(),"请选择失效时间");
                    return;
                }

                if (contactUsers != null && contactUsers.size() != 0) {
                    ObjectMapper mapper = new ObjectMapper();
                    ArrayList<SendCouponModel> sendCouponModels =new ArrayList<>();
                    for (int i = 0; i < contactUsers.size(); i++) {
                        ContactUser contactUser = contactUsers.get(i);
                        SendCouponModel sendCouponModel = new SendCouponModel();
                        sendCouponModel.name = contactUser.name;
                        sendCouponModel.num =contactUser.num;
                        sendCouponModel.tel = contactUser.mobile;
                        sendCouponModel.userType=contactUser.type;
                        sendCouponModels.add(sendCouponModel);
                    }
                    try {
                        String jsonlist = mapper.writeValueAsString(sendCouponModels);
                        mSendSeePresneter.sendCoupon(IOUtils.getToken(mContext), mTicketId,distributeId, timeLimit, jsonlist);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }else {
                    CommonUtils.showToast(getContext(),"请添加发送人");
                }


                break;
            default:
                break;
        }
    }

    private TicketSendWindow.OnClickListener mListener = new TicketSendWindow.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toIntent;
            switch (v.getId()) {
                case R.id.ticket_contacts:
//                    toIntent = new Intent(CouponSendSeeActivity.this, ContactActivity.class);
//                    toIntent.putExtra(KEY_INTENT_LISTCONTACT, (Serializable) contactUsers);
//                    toIntent.putExtra(KEY_INTENT_TYPE,VAULE_INTENT_COUPON);
//                    startActivity(toIntent);
                    toIntent = new Intent(CouponSendSeeActivity.this, ContactActivity.class);
                    toIntent.putExtra(KEY_INTENT_LISTCONTACT, (Serializable) contactUsers);
                    toIntent.putExtra(KEY_INTENT_TYPE, VAULE_INTENT_COUPON);
                    startActivityForResult(toIntent, Constants.IntentParams.REQUEST_CODE);
                    break;
                case R.id.ticket_user:
                    toIntent = new Intent(CouponSendSeeActivity.this, MyFriendActivity.class);
                    toIntent.putExtra(KEY_INTENT_LISTCONTACT, (Serializable) contactUsers);
                    toIntent.putExtra(KEY_INTENT_TYPE,VAULE_INTENT_COUPON);
                    startActivityForResult(toIntent, Constants.IntentParams.REQUEST_CODE_2);
                    break;
                case R.id.ticket_input:
                    sendSeeDialog.show();
                    break;
                default:
                    break;
            }
        }
    };

    private void checkContact() {
        if (contactUsers.size() != 0) {
            StringBuilder mobileSb = new StringBuilder("");
            for (int i = 0; i < contactUsers.size(); i++) {
                if (i == 0) {
                    mobileSb.append(contactUsers.get(i).mobile);
                } else {
                    mobileSb.append(",").append(contactUsers.get(i).mobile);
                }
            }
            mSendSeePresneter.checkCouponSeeUser(IOUtils.getToken(getContext()), distributeId, mobileSb.toString());
        }
    }

    @Override
    public void showSendSeehead(CouponEventModel event) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.sendsee_event_iv);
        simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(event.cover, 270, 203)));
        TextView eventName = (TextView) findViewById(R.id.sendsee_ticketinfo);
        eventName.setText(event.title);
        TextView ticketLeft = (TextView) findViewById(R.id.sendsee_ticketleft);
        ticketLeft.setText("剩余 "+event.inventory);
        if(event.limitNums!=0){
            limitNumTv.setText("每人限领"+event.limitNums+"张");
        }
    }


    @Override
    public void showCheckCoupon(ArrayList<CheckSendSeeModel> checkSendSeeModelArrayList) {
        for (CheckSendSeeModel check : checkSendSeeModelArrayList) {
            for (ContactUser contact : contactUsers) {
                if (check.mobile.equals(contact.mobile)) {
                    if (check.num == -1) {
                        contact.maxNum = Integer.MAX_VALUE;
                    } else {
                        contact.maxNum = check.num;
                    }
                }
            }
        }
        mUserAdapter.notifyDataSetChanged(contactUsers);
    }

    @Override
    public void errorMessage(String msg) {
        CommonUtils.showToast(getContext(),msg);
    }

    @Override
    public void showSendCoupponSucsses() {
        CommonUtils.showToast(getContext(), "分发成功");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSendSeePresneter.detachView();
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
}
