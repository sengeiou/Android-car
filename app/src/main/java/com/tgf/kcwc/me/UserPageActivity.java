package com.tgf.kcwc.me;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.globalchat.GlobalChatActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.UserBaseDataModel;
import com.tgf.kcwc.mvp.presenter.AttentionDataPresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.UserChangePresenter;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropUpEdtextPopupWindow;
import com.tgf.kcwc.view.DropUpPhonePopupWindow;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Author：Jenny
 * Date:2016/12/8 19:29
 * E-mail：fishloveqin@gmail.com
 * 个人主页
 */

public class UserPageActivity extends BaseActivity implements UserDataView<UserBaseDataModel> {

    protected SimpleDraweeView bigHeaderImg;
    protected SimpleDraweeView tagHeaderImg;
    protected SimpleDraweeView genderImg;
    protected RelativeLayout headerImgLayout;
    protected TextView userLevel;
    protected ImageView modelTv;
    protected ImageView popmanTv;
    protected SimpleDraweeView brandLogo;
    protected LinearLayout userTagLayout;
    protected TextView distanceTv;
    protected RelativeLayout userInfoLayout;
    protected TextView nametv;
    protected TextView descTv;
    protected TextView fansTv;
    protected RelativeLayout topLayout;
    protected ImageView attImage;
    protected TextView attentionTv;
    protected RelativeLayout attentionLayout;
    protected ImageView secretImage;
    protected TextView secretMsgTv;
    protected RelativeLayout secretMsgLayout;
    protected RelativeLayout relayOption;
    protected ImageView inputTxtImg;
    protected TextView releasePostedBtn;
    protected RelativeLayout relayOption2;
    private PagerSlidingTabStrip mTabs;
    private RelativeLayout mTitleLayout;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter = null;
    private LinearLayout sexlayout;
    private ImageView compile;
    private TextView yeartext;

    private int userId;
    private UserDataPresenter mPresenter;
    private UserChangePresenter mUserChangePresenter;
    private FileUploadPresenter mFileUploadPresenter;                           //上传图片
    private AttentionDataPresenter mAttentionPresenter;
    private AttentionDataPresenter mCancelPresenter;
    private int attentionType = -1;  //关注的类型  0未关注、1 已关注  2 双向关注
    private List<BaseFragment> fragments;
    private DropUpPhonePopupWindow mDropUpPhonePopupWindow;
    private List<DataItem> mDataList = new ArrayList<>();      //type数据
    private static final int IMAGE_PICKER = 1001;                   //图片
    private String type = "bg";
    private RelativeLayout mSalePersonLayout;
    UserBaseDataModel userBaseDataModel = null;

    public void gainTypeDataList() {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.name = "更换背景";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "本地上传";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.name = "拍照上传";
        mDataList.add(dataItem);
    }

    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        setTitleBarDrawable(R.drawable.shape_titlebar_bg);

        backEvent(back);

//        function.setImageResource(R.drawable.global_nav_n);
//        function.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    protected void setUpViews() {
        initView();
        gainTypeDataList();
        initProgressDialog();
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mTabs.setBackgroundColor(mRes.getColor(R.color.white));
        mPager = (ViewPager) findViewById(R.id.pager);
        fragments = new ArrayList<BaseFragment>();
        fragments.add(new DynamicFragment());
        fragments.add(new UserHomeFragment());
        fragments.add(new UserRideFragment());
        //fragments.add(new LoveCarFragment());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments,
                getResources().getStringArray(R.array.user_page_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();
        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getPersonalBaseData("" + userId, IOUtils.getToken(mContext));
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(uploadView);
        mUserChangePresenter = new UserChangePresenter();
        mUserChangePresenter.attachView(mUserDataView);

        mAttentionPresenter = new AttentionDataPresenter();
        mAttentionPresenter.attachView(mAttentionView);
        mCancelPresenter = new AttentionDataPresenter();
        mCancelPresenter.attachView(mCancelAttentionView);
    }


    private FileUploadView<DataItem> uploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem dataItem) {
            if (dataItem.code == 0) {
                String coverpath = dataItem.resp.data.path;
                mUserChangePresenter.getReplacepic(IOUtils.getToken(mContext), type, coverpath);
            }
        }

        @Override
        public void setLoadingIndicator(boolean active) {

            if (active) {
                mProgressDialog.show();
            } else {
                mProgressDialog.dismiss();
            }
        }

        @Override
        public void showLoadingTasksError() {

            mProgressDialog.dismiss();
        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    private ProgressDialog mProgressDialog = null;

    private void initProgressDialog() {

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在上传，请稍候...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        super.setContentView(R.layout.activity_user_page);

    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        mTabs.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        // mTabs.setDividerColor(getResources().getColor(R.color.color_80cbc4));
        // 设置分割线的上下的间距,传入的是dp
        mTabs.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        mTabs.setUnderlineHeight(0);
        //设置Tab底部线的颜色
        mTabs.setUnderlineColor(getResources().getColor(R.color.btn_color1));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTabs.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        mTabs.setIndicatorColorResource(R.color.btn_color1);

        // 设置Tab标题文字的大小,传入的是dp
        mTabs.setTextSize(16);
        // 设置选中Tab文字的颜色
        mTabs.setSelectedTextColorResource(R.color.text_color10);
        //设置正常Tab文字的颜色
        mTabs.setTextColorResource(R.color.text_color15);

    }

    private void initView() {
        mSalePersonLayout = findViewById(R.id.salePersonLayout);
        mSalePersonLayout.setOnClickListener(this);
        bigHeaderImg = (SimpleDraweeView) findViewById(R.id.bigHeaderImg);
        tagHeaderImg = (SimpleDraweeView) findViewById(R.id.tagHeaderImg);
        genderImg = (SimpleDraweeView) findViewById(R.id.genderImg);
        headerImgLayout = (RelativeLayout) findViewById(R.id.headerImgLayout);
        userLevel = (TextView) findViewById(R.id.userLevel);
        modelTv = (ImageView) findViewById(R.id.comment_model_tv);
        popmanTv = (ImageView) findViewById(R.id.comment_popman_tv);
        brandLogo = (SimpleDraweeView) findViewById(R.id.brandLogo);
        userTagLayout = (LinearLayout) findViewById(R.id.userTagLayout);
        distanceTv = (TextView) findViewById(R.id.distanceTv);
        userInfoLayout = (RelativeLayout) findViewById(R.id.userInfoLayout);
        nametv = (TextView) findViewById(R.id.nametv);
        descTv = (TextView) findViewById(R.id.descTv);
        fansTv = (TextView) findViewById(R.id.fansTv);
        topLayout = (RelativeLayout) findViewById(R.id.topLayout);
        attImage = (ImageView) findViewById(R.id.attImage);
        attentionTv = (TextView) findViewById(R.id.attentionTv);
        attentionLayout = (RelativeLayout) findViewById(R.id.attentionLayout);
        secretImage = (ImageView) findViewById(R.id.secretImage);
        secretMsgTv = (TextView) findViewById(R.id.secretMsgTv);
        secretMsgLayout = (RelativeLayout) findViewById(R.id.secretMsgLayout);
        relayOption = (RelativeLayout) findViewById(R.id.relay_option);
        inputTxtImg = (ImageView) findViewById(R.id.inputTxtImg);
        releasePostedBtn = (TextView) findViewById(R.id.releasePostedBtn);
        relayOption2 = (RelativeLayout) findViewById(R.id.relay_option2);
        mTitleLayout = (RelativeLayout) findViewById(R.id.titleBar);
        sexlayout = (LinearLayout) findViewById(R.id.sexlayout);
        compile = (ImageView) findViewById(R.id.compile);
        yeartext = (TextView) findViewById(R.id.yeartext);
        attentionLayout.setOnClickListener(this);
        secretMsgLayout.setOnClickListener(this);
        relayOption2.setOnClickListener(this);
        bigHeaderImg.setOnClickListener(this);
        headerImgLayout.setOnClickListener(this);
        compile.setVisibility(View.GONE);
        compile.setOnClickListener(this);
    }

    @Override
    public void showDatas(UserBaseDataModel model) {
        userBaseDataModel = model;
        String relation = model.relation + "";
        if ("myself".equals(relation)) {

            relayOption.setVisibility(View.GONE);
            relayOption2.setVisibility(View.VISIBLE);
            compile.setVisibility(View.VISIBLE);
        } else {
            relayOption.setVisibility(View.VISIBLE);
            relayOption2.setVisibility(View.GONE);
            attentionType = getAttentionType(model.relation);
            compile.setVisibility(View.GONE);
        }

        if (model.isSale == 1) {
            mSalePersonLayout.setVisibility(View.VISIBLE);
        } else {
            mSalePersonLayout.setVisibility(View.GONE);
        }
        if (attentionType == 0) {

            attImage.setImageResource(R.drawable.btn_attention);
            attentionTv.setText("关注");
        } else if (attentionType == 1) {
            attImage.setImageResource(R.drawable.attention_status_1);
            attentionTv.setText("已关注");
        } else if (attentionType == 2) {
            attImage.setImageResource(R.drawable.attention_status_2);
            attentionTv.setText("已关注");
        }
        showUserBaseInfo(model);
    }

    private int getAttentionType(String relation) {

        if ("not_concern".equals(relation)) {

            return 0;
        } else if ("already_concern".equals(relation)) {

            return 1;
        } else if ("mutual_concern".equals(relation)) {

            return 2;
        }

        return -1;
    }

    private void showUserBaseInfo(UserBaseDataModel model) {

        bigHeaderImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.bgImg, 360, 360)));
        tagHeaderImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.avatar, 144, 144)));
        nametv.setText(model.nickname + "");
        userLevel.setText("Lv." + model.level + "");

        //性别

        if (model.sex == 1) {
            sexlayout.setBackground(getResources().getDrawable(R.drawable.button_bg_sexblue));
            genderImg.setImageResource(R.drawable.icon_male);
        } else {
            sexlayout.setBackground(getResources().getDrawable(R.drawable.button_bg_sexpink));
            genderImg.setImageResource(R.drawable.icon_famale);
        }

        yeartext.setText(model.age + "");
        //模特
        if (model.isModel == 1) {
            modelTv.setVisibility(View.VISIBLE);
        } else {
            modelTv.setVisibility(View.GONE);
        }

        //达人
        if (model.isDoyen == 1) {
            popmanTv.setVisibility(View.VISIBLE);
        } else {
            popmanTv.setVisibility(View.GONE);
        }

        //车主

        if (model.isMaster == 1) {
            brandLogo.setVisibility(View.VISIBLE);
            brandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.logo, 144, 144)));
        } else {
            brandLogo.setVisibility(View.GONE);
        }

        //简介、个性签名
        descTv.setText(model.signText);

        String str = String.format(mRes.getString(R.string.fans_tv), model.followNum,
                model.fansNum);

        CommonUtils.customDisplayText(mRes, str, fansTv, R.color.text_color11);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        showLoadingIndicator(false);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFileUploadPresenter != null) {
            mFileUploadPresenter.detachView();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.salePersonLayout:
                SystemInvoker.launchDailPage(mContext, userBaseDataModel.tel);
                break;
            case R.id.relay_option2:
                CommonUtils.startNewActivity(mContext, PostingActivity.class);
                break;

            case R.id.attentionLayout:

                if (attentionType == -1) {
                    return;
                }
                if (attentionType == 0) { //关注
                    showAttention();
                } else {
                    showCancelAttention();
                }

                break;
            case R.id.secretMsgLayout:
                Intent intent = new Intent(mContext, GlobalChatActivity.class);
                intent.putExtra(Constant.EXTRA_USER_ID, userBaseDataModel.imId);
                intent.putExtra(Constant.EXTRA_USER_USERPIC, URLUtil.builderImgUrl(userBaseDataModel.avatar, 360, 360));
                intent.putExtra(Constant.EXTRA_USER_NICKNAME, userBaseDataModel.nickname);
                startActivity(intent);
                break;
            case R.id.compile:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, IOUtils.getUserId(mContext));
                CommonUtils.startNewActivity(mContext, args, UserInfoActivity.class);
                break;
            case R.id.bigHeaderImg:
                if ((userId + "").equals(IOUtils.getUserId(mContext))) {
                    type = "bg";
                    mDataList.get(0).name = "更换背景";
                    mDropUpPhonePopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                    mDropUpPhonePopupWindow.show(UserPageActivity.this);
                    mDropUpPhonePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            DataItem selectDataItem = mDropUpPhonePopupWindow.getSelectDataItem();
                            if (selectDataItem != null && mDropUpPhonePopupWindow.getIsSelec()) {
                                switch (selectDataItem.id) {
                                    case 2:
                                        ImagePicker.getInstance().setMultiMode(false);
                                        Intent intent = new Intent(UserPageActivity.this, ImageGridActivity.class);
                                        startActivityForResult(intent, IMAGE_PICKER);
                                        break;
                                    case 3:
                                        ImagePicker.getInstance().setMultiMode(false);
                                        intent = new Intent(UserPageActivity.this, ImageGridActivity.class);
                                        intent.putExtra(Constants.IntentParams.PHONETYPE, true);
                                        startActivityForResult(intent, IMAGE_PICKER);
                                        break;
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.headerImgLayout:
                if ((userId + "").equals(IOUtils.getUserId(mContext))) {
                    type = "avatar";
                    mDataList.get(0).name = "更换头像";
                    mDropUpPhonePopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                    mDropUpPhonePopupWindow.show(UserPageActivity.this);
                    mDropUpPhonePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            DataItem selectDataItem = mDropUpPhonePopupWindow.getSelectDataItem();
                            if (selectDataItem != null && mDropUpPhonePopupWindow.getIsSelec()) {
                                Intent intent = null;
                                switch (selectDataItem.id) {
                                    case 2:
                                        ImagePicker.getInstance().setMultiMode(false);
                                        intent = new Intent(UserPageActivity.this, ImageGridActivity.class);
                                        startActivityForResult(intent, IMAGE_PICKER);
                                        break;
                                    case 3:
                                        ImagePicker.getInstance().setMultiMode(false);
                                        intent = new Intent(UserPageActivity.this, ImageGridActivity.class);
                                        intent.putExtra(Constants.IntentParams.PHONETYPE, true);
                                        startActivityForResult(intent, IMAGE_PICKER);
                                        break;
                                }
                            }
                        }
                    });
                }
                break;
        }
    }

    public void showAttention() {
        DropUpEdtextPopupWindow mDropUpEdtextPopupWindow = new DropUpEdtextPopupWindow(mContext, userBaseDataModel.nickname, new DropUpEdtextPopupWindow.SeleListener() {
            @Override
            public void getString(String name) {
                mAttentionPresenter.execAttention(userId + "", IOUtils.getToken(mContext), name);
            }

            @Override
            public void getcancel() {
                mAttentionPresenter.execAttention(userId + "", IOUtils.getToken(mContext));
            }
        });
        mDropUpEdtextPopupWindow.show(UserPageActivity.this);
    }

    private void showCancelAttention() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.attention_dialog, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        TextView mEnddiary = (TextView) v.findViewById(R.id.appltlist_item_enddiary); //取消
        TextView mendgroup = (TextView) v.findViewById(R.id.appltlist_item_endgroup); //确认
        mEnddiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mendgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCancelPresenter.cancelAttention(userId + "", IOUtils.getToken(mContext));
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 300),
                BitmapUtils.dp2px(mContext, 250));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    uploadImage(item);
                }
            }

        }
    }


    private void uploadImage(ImageItem item) {
        File f = new File(item.path);
        compressWithRx(f, item);
    }

    /**
     * 图片压缩，RX链式处理
     *
     * @param file
     */
    private void compressWithRx(File file, final ImageItem item) {
        Observable.just(file).observeOn(Schedulers.io()).map(new Func1<File, File>() {
            @Override
            public File call(File file) {
                try {
                    return Luban.with(mContext).load(file).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
            @Override
            public void call(File file) {

                if (file != null) {
                    Logger.d("图片压缩文件路径:" + file.getAbsolutePath());
                    mFileUploadPresenter.uploadFile(RequestBody.create(MediaType.parse("image/png"), file),
                            RequestBody.create(MediaType.parse("text/plain"), "avatar"),
                            RequestBody.create(MediaType.parse("text/plain"), ""),
                            RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())),
                            item);
                }

            }
        });
    }

    //背景图头像

    UserDataView<Account.UserInfo> mUserDataView = new UserDataView<Account.UserInfo>() {
        @Override
        public void showDatas(Account.UserInfo userInfo) {
            Account account = IOUtils.getAccount(mContext);
            account.userInfo = userInfo;
            ObjectOutputStream objectOutputStream = null;
            FileOutputStream outputStream = null;
            try {
                outputStream = openFileOutput(Constants.KEY_ACCOUNT_FILE, Context.MODE_PRIVATE);
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(account);
                objectOutputStream.flush();

                IOUtils.clearData();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(objectOutputStream);
                IOUtils.close(outputStream);
            }
            mPresenter.getPersonalBaseData("" + userId, IOUtils.getToken(mContext));
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    //关注

    AttentionView<String> mAttentionView = new AttentionView<String>() {
        @Override
        public void showAddAttention(String string) {

            CommonUtils.showToast(mContext, "您已添加关注");
            mPresenter.getPersonalBaseData("" + userId,
                    IOUtils.getToken(mContext));
        }

        @Override
        public void showCancelAttention(String string) {

        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    //取消关注

    AttentionView<String> mCancelAttentionView = new AttentionView<String>() {
        @Override
        public void showAddAttention(String string) {

        }

        @Override
        public void showCancelAttention(String string) {
            CommonUtils.showToast(mContext, "您已取消关注");
            mPresenter.getPersonalBaseData("" + userId,
                    IOUtils.getToken(mContext));
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };
}
