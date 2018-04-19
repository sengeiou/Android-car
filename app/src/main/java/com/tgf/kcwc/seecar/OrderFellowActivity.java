package com.tgf.kcwc.seecar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.YuyueBuyModel;
import com.tgf.kcwc.mvp.presenter.OrderFellowPresenter;
import com.tgf.kcwc.mvp.view.OrderFellowView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DistanceUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NotitleContentDialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/28 0028
 * E-mail:hekescott@qq.com
 */

public class OrderFellowActivity extends BaseActivity implements OrderFellowView {

    private Account.UserInfo myInfo;
    private String toChatUsername;
    private int chatType;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:

                    break;
                default:
                    break;
            }
        }
    };
    public View chatOrgInfo;
    private View btnSend;
    public View chatMenull;
    public View gotoComment;
    private TextView nickNameTv;
    private ListView contactLV;
    private OrderFellowPresenter orderFellowPresenter;
    private FragmentManager fragmentManager;
    private String mToken;
    private Intent fromIntent;
    private YuyueBuyModel.OrgItem mToChatUser;
    private TextView mHeadNicknameTv;
    private TextView mOrgTitleTv;
    private TextView mOrgAddressTv;
    private RatingBar orgRating;
    private TextView orgLevelTv;
    private TextView offerMoneyTv;
    private ArrayList<YuyueBuyModel.OrgItem> mOfferList;
    private WrapAdapter mOfferAdapter;
    private SimpleDraweeView headAvatr;
    private int mOrderId;
    private NotitleContentDialog noDisturbDialog;
    private final  int REQUEST_CODE_EVAL=100;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        noDisturbDialog = new NotitleContentDialog(getContext());
        noDisturbDialog.setContentText("确定免TA打扰吗?");
        noDisturbDialog.setOnLoginOutClickListener(new NotitleContentDialog.IOnclickLisenter() {
            @Override
            public void OkClick() {
                orderFellowPresenter.postNodisturb(mToken, mOrderId, mToChatUser.id);
                noDisturbDialog.dismiss();
            }

            @Override
            public void CancleClick() {
                noDisturbDialog.dismiss();
            }
        });
        chatOrgInfo = findViewById(R.id.chat_orginfll);
        nickNameTv = (TextView) findViewById(R.id.orderfellowchat_nikanmetv);
        mOrgTitleTv = (TextView) findViewById(R.id.orderfellow_orgtitle);
        mOrgAddressTv = (TextView) findViewById(R.id.orderfellow_addresstv);
        orgRating = (RatingBar) findViewById(R.id.orderfellow_rating);
        orgLevelTv = (TextView) findViewById(R.id.orderfellow_leveal);
        offerMoneyTv = (TextView) findViewById(R.id.orderfellow_money);
        headAvatr = (SimpleDraweeView) findViewById(R.id.orderfellow_avatar);
        findViewById(R.id.orderfellow_xialaiv).setOnClickListener(this);
        findViewById(R.id.title_contactright).setOnClickListener(this);
        findViewById(R.id.title_bar_back).setOnClickListener(this);
        btnSend = findViewById(R.id.btn_send);
        chatMenull = findViewById(R.id.orderfellow_chatmenu);
        findViewById(R.id.orderfellow_nomsg).setOnClickListener(this);
        findViewById(R.id.orderfellow_complete).setOnClickListener(this);
        gotoComment = findViewById(R.id.orderfellow_gotocomment);
        contactLV = (ListView) findViewById(R.id.orderfellow_contactlv);
        mHeadNicknameTv = (TextView) findViewById(R.id.orderfellow_nickname);
        gotoComment.setOnClickListener(this);
        //        chatOrgInfo.setOnClickListener(this);
        orderFellowPresenter = new OrderFellowPresenter();
        orderFellowPresenter.attachView(this);
        btnSend.setOnClickListener(this);
        contactLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mToChatUser = mOfferList.get(position);
                contactLV.setVisibility(View.GONE);
                showHead();
            }
        });
    }

    private void showHead() {
        nickNameTv.setText(mToChatUser.nickname);
        mHeadNicknameTv.setText(mToChatUser.nickname);
        mOrgTitleTv.setText(mToChatUser.org_name);
        KPlayCarApp kPlayCarApp = (KPlayCarApp) getApplication();
        mOrgAddressTv.setText(Html.fromHtml(mToChatUser.org_address));
        TextView distanceTv = findViewById(R.id.orderfellow_distanceTv);
        distanceTv.setText(DistanceUtil.getDistance(kPlayCarApp, mToChatUser.lat, mToChatUser.lng));
        orgRating.setStar(NumFormatUtil.getFmtString(mToChatUser.star));
        orgLevelTv.setText(mToChatUser.star);
        offerMoneyTv.setText("￥" +NumFormatUtil.getFmtNum(mToChatUser.offer));
        String avatarUrl = URLUtil.builderImgUrl(mToChatUser.avatar, 144, 144);
        headAvatr.setImageURI(Uri.parse(avatarUrl));
//        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
//            @Override
//            public EaseUser getUser(String username) {
//                EaseUser user = new EaseUser(username);
//                if (username.equals(SharedPreferenceUtil.getEaseaUserName(getContext()))) {
//                    user.setAvatar(URLUtil.builderImgUrl(IOUtils.getAccount(getContext()).userInfo.avatar, 144, 144));
//                } else {
//                    String toAvatarUrl = URLUtil.builderImgUrl(mToChatUser.avatar, 144, 144);
//                    user.setAvatar(toAvatarUrl);
//                }
//                return user;
//            }
//        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar = false;
        EaseConstant.IS_BUYCAR_TYPE = true;
        setContentView(R.layout.activity_orderfellow);
        mToken = IOUtils.getToken(this);
        fromIntent = getIntent();
        mToChatUser = fromIntent.getParcelableExtra(Constants.IntentParams.DATA);
        mOrderId = fromIntent.getIntExtra(Constants.IntentParams.ID, 0);
        toChatUsername = mToChatUser.username;
        if (TextUtil.isEmpty(toChatUsername)) {
            toChatUsername = "im_843_kcwc";
        }

        chatType = EaseConstant.CHATTYPE_SINGLE;
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType);
        bundle.putString(EaseConstant.EXTRA_USER_ID, toChatUsername);
        bundle.putString(Constant.EXTRA_USER_ORDER, mOrderId + "");
        Logger.d("easechat==" + mOrderId);
        chatFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.orderfellow_chatfrag, chatFragment, toChatUsername).commit();
        showHead();
        //        myInfo = IOUtils.getAccount(this).userInfo;
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_send:
                chatMenull.setVisibility(View.GONE);
                ChatFragment chatFragment = (ChatFragment) fragmentManager
                        .findFragmentByTag(toChatUsername);
                chatFragment.setInputVisble();
                break;
            case R.id.orderfellow_nomsg:
                noDisturbDialog.show();
                break;
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.orderfellow_complete:
                CommonUtils.showToast(this, "完成服务");
                orderFellowPresenter.postOrderFellowComplete(mToken, mOrderId, mToChatUser.id);
                break;
            case R.id.orderfellow_gotocomment:
                Intent toCommentIntent = new Intent(getContext(), ServerEvaluationActivity.class);
                toCommentIntent.putExtra(Constants.IntentParams.DATA, mToChatUser);
                toCommentIntent.putExtra(Constants.IntentParams.ID, mOrderId);
                startActivity(toCommentIntent);
                CommonUtils.startNewActivity(this, ServerEvaluationActivity.class);
                break;
            case R.id.title_contactright:
                if (contactLV.getVisibility() == View.VISIBLE) {
                    contactLV.setVisibility(View.GONE);
                } else {
                    contactLV.setVisibility(View.VISIBLE);
                    //TODO 死数据
                    orderFellowPresenter.getOrderFellowContact(mToken, 46);
                }
                break;
            case R.id.orderfellow_xialaiv:
                chatOrgInfo.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        //        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EaseConstant.IS_BUYCAR_TYPE = false;
        super.onDestroy();
    }


    @Override
    public void showOrgList(ArrayList<YuyueBuyModel.OrgItem> offerList) {
        mOfferList = offerList;
        if (mOfferAdapter == null) {
            mOfferAdapter = new WrapAdapter<YuyueBuyModel.OrgItem>(getContext(), mOfferList,
                    R.layout.listitem_avatarmsg) {
                @Override
                public void convert(ViewHolder helper, YuyueBuyModel.OrgItem item) {
                    TextView msgNumTv = helper.getView(R.id.orderfellow_msgnum);
                    if (item.msg_num == 0) {
                        msgNumTv.setVisibility(View.INVISIBLE);
                    } else {
                        msgNumTv.setVisibility(View.VISIBLE);
                        msgNumTv.setText(item.msg_num + "");
                    }
                    String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                    helper.setSimpleDraweeViewURL(R.id.orderfellow_contactavatar, avatarUrl);
                }
            };
            contactLV.setAdapter(mOfferAdapter);
        } else {
            mOfferAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void showPostDataFailed(String statusMessage) {
        CommonUtils.showToast(getContext(), statusMessage);
    }

    @Override
    public void showNoDisturbSuccess(String msg) {
        finish();
    }

    @Override
    public void showCompleteSuccess(String msg) {
        Intent toIntent = new Intent(getContext(), ServerEvaluationActivity.class);
        toIntent.putExtra(Constants.IntentParams.DATA, mToChatUser);
        toIntent.putExtra(Constants.IntentParams.ID, mOrderId);
        startActivityForResult(toIntent,REQUEST_CODE_EVAL);
        chatMenull.setVisibility(View.GONE);
        gotoComment.setVisibility(View.VISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CODE_EVAL){
                finish();
            }
        }
    }
}
