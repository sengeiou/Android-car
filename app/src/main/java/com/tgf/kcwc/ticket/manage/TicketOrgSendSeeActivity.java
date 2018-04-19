package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.CheckSendSeeModel;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.model.HeadEvent;
import com.tgf.kcwc.mvp.model.TicketOrgManageDetailModel;
import com.tgf.kcwc.mvp.presenter.SendSeePresenter;
import com.tgf.kcwc.mvp.presenter.TicketOrgManageDetailPresenter;
import com.tgf.kcwc.mvp.view.TicketOrgManageDetailView;
import com.tgf.kcwc.mvp.view.TicketSendSeeView;
import com.tgf.kcwc.ticket.SelectWorkerActivity;
import com.tgf.kcwc.ticket.widget.SendSeeDialog;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.Validator;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/10 0010
 * E-mail:hekescott@qq.com
 */

public class TicketOrgSendSeeActivity extends BaseActivity
        implements TicketSendSeeView, TicketOrgManageDetailView {
    private static final String TAG = "TicketSendSeeActivity";
    private final String KEY_INTENT_TYPE = "type";
    private final int VAULE_INTENT_TICKET = 1;
    private ArrayList<ContactUser> contactUsers = new ArrayList<ContactUser>();
    private int timeLimit = -1;
    private final String KEY_INTENT_MOBILE = "mobile";
    private SendSeeDialog sendSeeDialog;
    private int mTicketId;
    private ArrayList<DataItem> limitDataList;
    private TicketOrgManageDetailPresenter ticketOrgManageDetailPresenter;

    /**
     * 创建侧滑菜单项
     */
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            deleteItem.setBackground(new ColorDrawable(mRes.getColor(R.color.text_color36)));
            deleteItem.setWidth(BitmapUtils.dp2px(mContext, 40));
            deleteItem.setTitle("删除");
            deleteItem.setTitleSize(14);
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
    private SendSeePresenter mSendSeePresneter;
    private TextView limitNumTv;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("分发预览");

    }

    @Override
    protected void setUpViews() {

        String doc = "\n" +
                "1、此操作为机构向员工分发赠票；\n" +
                "2、向员工分发赠票后，员工未向客户发放部分赠票可被收回，员工已向客户发放部分赠票不可被收回；\n" +
                "3、若需要收回赠票，可在分发列表进行”-“操作。";
        TextView textViewDoc = (TextView) findViewById(R.id.ticket_doctv);
        textViewDoc.setText(doc);
        ticketOrgManageDetailPresenter = new TicketOrgManageDetailPresenter();
        ticketOrgManageDetailPresenter.attachView(this);
        mTicketId =SharedPreferenceUtil.getTFId(getContext());
        ticketOrgManageDetailPresenter.getTicketOrgManageDetail(IOUtils.getToken(getContext()),mTicketId,1);
        mSendSeePresneter = new SendSeePresenter();
        mSendSeePresneter.attachView(this);
        fromIntent = getIntent();
        sendSeeDialog = new SendSeeDialog(getContext());
        initData();
        limitNumTv = (TextView) findViewById(R.id.sendsee_limiNumtv);
        findViewById(R.id.sendsee_addbtn).setOnClickListener(this);
        findViewById(R.id.sendsee_submit).setOnClickListener(this);
        //设置侧滑菜项
        mTicketListView = (SwipeMenuListView) findViewById(R.id.swipe_ticketlv);
        mTicketListView.setMenuCreator(creator);
        mTicketListView.setOnMenuItemClickListener(onMenuItemClickListener);

    }



    private void initData() {
        mSendSeePresneter.getTicketExhibitInfo(IOUtils.getToken(getContext()), mTicketId);
        int mobile = fromIntent.getIntExtra(KEY_INTENT_MOBILE, 0);
        sendSeeDialog.setYesOnclickListener("确认", new SendSeeDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (Validator.isMobile(sendSeeDialog.getNums())) {
                    ContactUser contactUser = new ContactUser();
                    contactUser.isSelected = true;
                    contactUser.mobile = sendSeeDialog.getNums();
                    for (ContactUser forcontactUser1 : contactUsers) {
                        if (forcontactUser1.mobile.equals(contactUser.mobile)) {
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
        setContentView(R.layout.activity_orgsendsee);

        Serializable selctUser = fromIntent.getSerializableExtra(Constants.IntentParams.DATA);
        if (selctUser != null) {
            contactUsers = (ArrayList<ContactUser>) selctUser;
        }
        checkContact();

        if (mUserAdapter == null) {
            mUserAdapter = new WrapAdapter<ContactUser>(TicketOrgSendSeeActivity.this, contactUsers,
                    R.layout.listitem_sendsee_user) {
                @Override
                public void convert(final ViewHolder helper, final ContactUser item) {
                    if(TextUtil.isEmpty(item.name)){
                        helper.setText(R.id.sendsee_desctv, "(" + item.mobile + ")");
                    }else {
                        helper.setText(R.id.sendsee_desctv, item.name + "(" + item.mobile + ")");
                    }

                    TextView numText = helper.getView(R.id.sendsee_nums);
                    ImageView addIv = helper.getView(R.id.sendsee_add);
                    ImageView reduceIv = helper.getView(R.id.sendsee_reduce);

                    if (item.maxNum == 0) {
                        numText.setText("已超");
                        numText.setTextColor(mRes.getColor(R.color.red));
                        reduceIv.setVisibility(View.INVISIBLE);
                        addIv.setVisibility(View.INVISIBLE);
                    } else {
                        numText.setText(item.num + "");
                        numText.setTextColor(mRes.getColor(R.color.black_font1));
                        reduceIv.setVisibility(View.VISIBLE);
                        addIv.setVisibility(View.VISIBLE);
                        reduceIv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int num = contactUsers.get(helper.getPosition()).num;
                                if (num < 2) {
                                    num = 1;
                                    CommonUtils.showToast(getContext(), "已经不能在减了");
                                } else {
                                    num = num - 1;
                                }
                                contactUsers.get(helper.getPosition()).num = num;
                                mUserAdapter.notifyDataSetChanged(contactUsers);
                            }
                        });
                        addIv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int num = contactUsers.get(helper.getPosition()).num;
                                if (num >= item.maxNum) {
                                    CommonUtils.showToast(getContext(), "已超过限领量了");
                                } else {
                                    num = num + 1;
                                }
                                contactUsers.get(helper.getPosition()).num = num;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendsee_addbtn:
                Intent    toIntent = new Intent(TicketOrgSendSeeActivity.this, SelectWorkerActivity.class);
                toIntent.putExtra(Constants.IntentParams.DATA, (Serializable) contactUsers);
                toIntent.putExtra(KEY_INTENT_TYPE, VAULE_INTENT_TICKET);
                startActivity(toIntent);
                break;
            case R.id.sendsee_submit:
//                if (timeLimit == -1) {
//                    CommonUtils.showToast(getContext(), "请选择失效时间");
//                    return;
//                }
                if (contactUsers != null && contactUsers.size() != 0) {
                    StringBuilder nameSb = new StringBuilder("");
                    StringBuilder mobileSb = new StringBuilder("");
                    StringBuilder userTypeSb = new StringBuilder("");
                    StringBuilder numsSb = new StringBuilder("");
                    StringBuilder uidSb = new StringBuilder("");
                    for (int i = 0; i < contactUsers.size(); i++) {
                        ContactUser contactUser = contactUsers.get(i);
                        if (i == 0) {
                            uidSb.append(contactUser.userId);
                            nameSb.append(contactUser.name);
                            mobileSb.append(contactUser.mobile);
                            userTypeSb.append(contactUser.type);
                            numsSb.append(contactUser.num);
                        } else {
                            uidSb.append(",").append(contactUser.userId);
                            nameSb.append(",").append(contactUser.name);
                            mobileSb.append(",").append(contactUser.mobile);
                            userTypeSb.append(",").append(contactUser.type);
                            numsSb.append(",").append(contactUser.num);
                        }
                    }
//                    String token,  String tfId,String nums, String uid
//                    mSendSeePresneter.sendTicket(IOUtils.getToken(getContext()), mTicketId, mobileSb.toString(), nameSb.toString(), numsSb.toString(), timeLimit, userTypeSb.toString());
                    mSendSeePresneter.sendTicket(IOUtils.getToken(getContext()), mTicketId+"", numsSb.toString(),uidSb.toString());
                } else {
                    CommonUtils.showToast(getContext(), "请添加发送人");
                }


                break;
            default:
                break;
        }
    }

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

            mSendSeePresneter.checkeSendSeeUser(IOUtils.getToken(getContext()), mTicketId, mobileSb.toString());
        }
    }

    @Override
    public void showSendSeehead(HeadEvent event) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.sendsee_event_iv);
        simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(event.cover, 270, 203)));
        TextView eventName = (TextView) findViewById(R.id.sendsee_ticketinfo);
        TextView ticketLeftTv = (TextView) findViewById(R.id.sendsee_ticketleft);
        eventName.setText(event.name + "  " + event.ticketName);
        ticketLeftTv.setText("剩余：" + event.getLeftNum());
        limitNumTv.setText("每人限领" + event.limitNums + "张");
        if (event.limitNums == 0) {
            limitNumTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSendTicketSuccess() {
        CommonUtils.showToast(getContext(), "操作成功");
        finish();
    }

    @Override
    public void showCheckTicket(ArrayList<CheckSendSeeModel> checkSendSeeModelArrayList) {
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
    public void failedMessage(String statusMessage) {
            CommonUtils.showToast(getContext(),statusMessage);
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
        return TicketOrgSendSeeActivity.this;
    }
    private SimpleDraweeView mSendseeEventcoverIv;
    private TextView mSendseeTicketinfo;
    private TextView mManageorgdetailTickettypeTv;
    private TextView mOrgsendseeSourcetv;
    private TextView mSendseeLeftTicketTv;
    @Override
    public void showManageDetail(TicketOrgManageDetailModel.Details details) {


        mSendseeEventcoverIv = (SimpleDraweeView) findViewById(R.id.sendsee_eventcoverIv);
        mSendseeTicketinfo = (TextView) findViewById(R.id.sendsee_ticketinfo);
        mManageorgdetailTickettypeTv = (TextView) findViewById(R.id.manageorgdetail_tickettype_tv);
        mOrgsendseeSourcetv = (TextView) findViewById(R.id.orgsendsee_sourcetv);
        mSendseeLeftTicketTv = (TextView) findViewById(R.id.sendsee_leftTicketTv);

        mSendseeEventcoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(details.event_cover,540,304)));
        mManageorgdetailTickettypeTv.setText(details.ticket_name);
        mSendseeTicketinfo.setText(details.event_name);
        mOrgsendseeSourcetv.setText("来源：" + details.source);
        mSendseeLeftTicketTv.setText(Html.fromHtml("剩余: <font color=\"#f46280\">" + details.getHaveNums() + "</font>"));
    }

    @Override
    protected void onDestroy() {
        ticketOrgManageDetailPresenter.detachView();
        super.onDestroy();
    }
}
