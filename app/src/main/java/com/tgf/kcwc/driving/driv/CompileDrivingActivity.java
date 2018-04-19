package com.tgf.kcwc.driving.driv;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.SelectAddressActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.entity.RichEditorEntity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.myline.MyLineMainActivity;
import com.tgf.kcwc.mvp.model.CompileDrivingBean;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.model.TopicTagDataModel;
import com.tgf.kcwc.mvp.presenter.CompileDrivingPresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.view.CompileDrivingView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.posting.TopicListActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.LocationUtil;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.DropUpPhonePopupWindow;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;
import com.tgf.kcwc.view.UISwitchButton;
import com.tgf.kcwc.view.link.Link;
import com.tgf.kcwc.view.richeditor.SEditorData;
import com.tgf.kcwc.view.selecttime.TimeSelector;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * Created by Administrator on 2017/5/5.
 * 编辑开车去详情
 */

public class CompileDrivingActivity extends BaseActivity implements CompileDrivingView {

    private FileUploadPresenter mFileUploadPresenter;                           //上传图片
    private CompileDrivingPresenter mCompileDrivingPresenter;
    private static final int IMAGE_PICKER = 1001;                   //图片
    public static final int REQUEST_CODE_PREVIEW = 101;                    //查看图片
    public static final int RELEASE_INTRODUCE = 1002;                   //活动介绍
    private final int KEY_REQUEST_STARTPOS = 1003;                   //开始地点
    private final int KEY_REQUEST_ENDPOS = 1004;                   //目的地
    private final int KEY_REQUEST_GATHERPOS = 1005;                   //集合地
    private UISwitchButton mOwnerSwitchButton;                             //仅限车主报名
    private UISwitchButton mApproveSwitchButton;                           //需要批准
    private LinearLayout mHintpicture;                                   //提示加载封面layout
    private RelativeLayout mDrivdetailslay;                                //图片layout
    private RelativeLayout mTitleLayout;                                   //标题layout
    private SimpleDraweeView mDrivdetailsCover;
    private ImageView mSelectCover;                                   //替换图片
    private LinearLayout mSceneType;                                     //场景类型layout

    private TextView mType;                                          //选择的类型
    private TextView mDepartText;                                    //开始地
    private TextView mDestinationText;                               //目的地
    private TextView mGatheringText;                                 //集合地
    private TextView mProjectText;                                   //路书
    private TextView mStarttimeText;                                 //开始时间
    private TextView mEndtimeText;                                   //结束时间
    private TextView mBudgetText;                                    //预算
    private TextView mUpperlimitText;                                //人数
    private TextView mAbortText;                                     //报名截止时间
    private TextView mRequireText;                                   //其他要求
    private TextView mConfirm;                                       //发布
    private EditText mTitle;                                         //标题

    private DropUpPhonePopupWindow mDropUpPopupWindow;
    private List<DataItem> mDataList = new ArrayList<>();      //type数据

    private LinearLayout mSponsordDepart;                                //出发地layout
    private TextView mSponsordDepartBx;                              //出发地必选
    private LinearLayout mSponsordDestination;                           //目的地layout
    private LinearLayout mSponsordGathering;                             //集合地layout
    private LinearLayout mSponsordProject;                               //计划路线layout
    private LinearLayout mStartTime;                                     //开始时间layout
    private LinearLayout mEndTime;                                       //结束时间layout
    private LinearLayout mUpperLimit;                                    //人数上限layout
    private LinearLayout mBudget;                                        //预算layout
    private LinearLayout mAbort;                                         //报名截止layout
    private LinearLayout mRequire;                                       //报名要求layout
    private LinearLayout mLabel;                                         //标签layout
    private LinearLayout mIntroduce;                                     //活动介绍layout
    private FlowLayout mTagLayout;
    private List<Topic> mTags = new ArrayList<Topic>(); //帖子标签集合，最多只存取5个
    private static final int TAG_MAX = 5;
    private RelativeLayout mTagRootLayout;
    private TextView mAgreement;
    private CheckBox mCheckBox;       //是否同意协议
    private MyListView mExplainList;                                   //活动说明
    private WrapAdapter<SEditorData> mExplainAdapter;
    private ArrayList<SEditorData> mEditorDatas = new ArrayList<>();
    private Tip startTip = null;                   //开始地点
    private Tip endTip = null;                   //结束地点
    private Tip gatherTip = null;                   //集合地
    private String mDrivCoverPath = "";                     //封面url
    private String limitMax = "";                     //人数上限
    private String limitMaxString = "";                     //人数上限
    private String limitMin = "";                     //人数下限
    private String otherCondition = "";                     //其他报名条件
    private String beginTime = "";                     //开始时间
    private String endTime = "";                     //结束时间
    private String deadlineTime = "";                     //报名截止时间
    private String budget = "";                     //预算
    private String introJson = "";                     //活动详情
    private String needReview = "0";                    //是否需要审核（0-否，1-是）
    private String onlyOwner = "0";                    //是否必须为车主（0-否，1-是）
    private String roadbookId = "";                     //路书id
    private String roadbooktitle = "";                     //路书名字
    private String title = "";                     //标题
    private String topic = "";                     //话题标签
    private String startCity = "";                     //开始城市
    private String endCity = "";                     //结束城市
    private String gatherCity = "";                     //目的地城市
    private DataItem mTypeDataItem;                                  //选择的类型

    private int startTiptype = -1;
    private int endTiptype = -1;
    private int gatherTiptype = -1;

    private String ID = "";
    CompileDrivingBean mCompileDrivingBean;

    public void gainTypeDataList() {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = -1;
        dataItem.name = "选择类型";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 6;
        dataItem.name = "自驾游";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 5;
        dataItem.name = "召集令";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "聚会";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 7;
        dataItem.name = "其它";
        mDataList.add(dataItem);

        mTypeDataItem = new DataItem();
        mTypeDataItem.id = 5;
        mTypeDataItem.name = "召集令";
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        // backEvent(back);
        text.setText(R.string.sponsordriv);
        text.setTextColor(getResources().getColor(R.color.text_color14));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQR();
            }
        });
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsordriving);
        KPlayCarApp.putValue(Constants.PathBack.SPONSORIS, false);
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        gainTypeDataList();
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(uploadView);
        mCompileDrivingPresenter = new CompileDrivingPresenter();
        mCompileDrivingPresenter.attachView(this);
        mOwnerSwitchButton = (UISwitchButton) findViewById(R.id.owner);
        mApproveSwitchButton = (UISwitchButton) findViewById(R.id.approve);
        mOwnerSwitchButton.setChecked(false);
        mApproveSwitchButton.setChecked(false);
        mHintpicture = (LinearLayout) findViewById(R.id.hintpicture);
        mDrivdetailsCover = (SimpleDraweeView) findViewById(R.id.drivdetails_cover);
        mDrivdetailslay = (RelativeLayout) findViewById(R.id.drivdetailslay);
        mTitleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        mSelectCover = (ImageView) findViewById(R.id.select_cover);
        mSceneType = (LinearLayout) findViewById(R.id.scene_type);
        mSponsordDepart = (LinearLayout) findViewById(R.id.sponsord_depart);
        mSponsordDestination = (LinearLayout) findViewById(R.id.sponsord_destination);
        mSponsordGathering = (LinearLayout) findViewById(R.id.sponsord_gathering);
        mSponsordProject = (LinearLayout) findViewById(R.id.sponsord_project);
        mStartTime = (LinearLayout) findViewById(R.id.starttime);
        mEndTime = (LinearLayout) findViewById(R.id.endtime);
        mUpperLimit = (LinearLayout) findViewById(R.id.upperlimit);
        mBudget = (LinearLayout) findViewById(R.id.budget);
        mAbort = (LinearLayout) findViewById(R.id.abort);
        mRequire = (LinearLayout) findViewById(R.id.require);
        mLabel = (LinearLayout) findViewById(R.id.label);
        mSponsordDepartBx = (TextView) findViewById(R.id.sponsord_depart_bx);
        mType = (TextView) findViewById(R.id.type);
        mTagLayout = (FlowLayout) findViewById(R.id.tagLists);
        mTagLayout.setVerticalSpacing(BitmapUtils.dp2px(mContext, 5));
        mTagLayout.setHorizontalSpacing(BitmapUtils.dp2px(mContext, 5));
        mTagRootLayout = (RelativeLayout) findViewById(R.id.tagLayout);
        mAgreement = (TextView) findViewById(R.id.agreement_text);
        mIntroduce = (LinearLayout) findViewById(R.id.introduce);
        mExplainList = (MyListView) findViewById(R.id.drivdetails_explain);
        mGatheringText = (TextView) findViewById(R.id.gatheringtext);
        mDepartText = (TextView) findViewById(R.id.departtext);
        mDestinationText = (TextView) findViewById(R.id.destinationtext);
        mEndtimeText = (TextView) findViewById(R.id.endtimetext);
        mStarttimeText = (TextView) findViewById(R.id.starttimetext);
        mBudgetText = (TextView) findViewById(R.id.budgettext);
        mUpperlimitText = (TextView) findViewById(R.id.upperlimittext);
        mAbortText = (TextView) findViewById(R.id.aborttext);
        mRequireText = (TextView) findViewById(R.id.requiretext);
        mProjectText = (TextView) findViewById(R.id.projecttext);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mTitle = (EditText) findViewById(R.id.title);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox_license);

        initProgressDialog();

        mHintpicture.setOnClickListener(this);
        mSelectCover.setOnClickListener(this);
        mSceneType.setOnClickListener(this);
        mStartTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
        mUpperLimit.setOnClickListener(this);
        mBudget.setOnClickListener(this);
        mAbort.setOnClickListener(this);
        mRequire.setOnClickListener(this);
        mLabel.setOnClickListener(this);
        mIntroduce.setOnClickListener(this);
        mSponsordDepart.setOnClickListener(this);
        mSponsordDestination.setOnClickListener(this);
        mSponsordGathering.setOnClickListener(this);
        mSponsordProject.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        //活动说明
        mExplainAdapter = new WrapAdapter<SEditorData>(mContext, R.layout.activity_drivdetails_item,
                mEditorDatas) {
            @Override
            public void convert(ViewHolder helper, SEditorData item) {
                final int position = helper.getPosition();
                TextView content = helper.getView(R.id.drivdetails_explain_content);
                SimpleDraweeView explain = helper.getView(R.id.drivingdetails_explain);
                ImageView delete = helper.getView(R.id.delete);
                if (item != null) {
                    if (item.getInputStr() == null) {
                        content.setVisibility(View.GONE);
                    } else {
                        if (item.getInputStr().equals("")) {
                            content.setVisibility(View.GONE);
                        } else {
                            content.setVisibility(View.VISIBLE);
                            content.setText(item.getInputStr());
                        }
                    }
                    if (item.getImageUrl() == null) {
                        explain.setVisibility(View.GONE);
                    } else {
                        if (item.getImageUrl().equals("")) {
                            explain.setVisibility(View.GONE);
                        } else {
                            explain.setVisibility(View.VISIBLE);
                            explain.setImageURI(
                                    Uri.parse(URLUtil.builderImgUrl(item.getImageUrl(), 540, 270)));
                        }
                    }
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEditorDatas.remove(position);
                            mExplainAdapter.notifyDataSetChanged();
                        }
                    });
                }

            }
        };
        mExplainList.setAdapter(mExplainAdapter);

        ViewUtil.link(" 活动协议", mAgreement, new Link.OnClickListener() {
            @Override
            public void onClick(Object o, String clickedText) {
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, "1");
                CommonUtils.startNewActivity(mContext, args, SignUpAgreementActivity.class);
            }
        }, mRes.getColor(R.color.text_color6), true);

        mOwnerSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    onlyOwner = "1";
                } else {
                    onlyOwner = "0";
                }

            }
        });
        mApproveSwitchButton
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            needReview = "1";
                        } else {
                            needReview = "0";
                        }

                    }
                });
        mCompileDrivingPresenter.getEdit(IOUtils.getToken(mContext), ID);

    }

    public void setTypeLay(int num) {
        switch (num) {
            case 6:
            case 7:
                mSponsordDepartBx.setVisibility(View.VISIBLE);
                mSponsordDepart.setVisibility(View.VISIBLE);
                mSponsordDestination.setVisibility(View.VISIBLE);
                mSponsordGathering.setVisibility(View.GONE);
                mSponsordProject.setVisibility(View.VISIBLE);
                break;
            case 5:
            case 2:
                // mSponsordDepartBx.setVisibility(View.VISIBLE);
                mSponsordDepart.setVisibility(View.GONE);
                mSponsordDestination.setVisibility(View.GONE);
                mSponsordGathering.setVisibility(View.VISIBLE);
                mSponsordProject.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    /* mHintpicture.setVisibility(View.GONE);
                    mDrivdetailsCover.setVisibility(View.VISIBLE);
                    Bitmap bm = BitmapFactory.decodeFile(item.path);
                    mDrivdetailsCover.setImageBitmap(bm);*/
                    uploadImage(item);
                }
            }

        }

        if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);

            }
        }

        if (Constants.InteractionCode.REQUEST_CODE == requestCode && resultCode == RESULT_OK) {

            Topic topic = data.getParcelableExtra(Constants.IntentParams.DATA);
            int size = mTags.size();
            if (size >= TAG_MAX) {
                CommonUtils.showToast(mContext, "最多只能添加5个标签");
            } else {
                mTags.add(topic);
            }
            addTagItems(mTags);
        }

        if (RELEASE_INTRODUCE == requestCode && resultCode == RESULT_OK) {
            RichEditorEntity entity = data.getParcelableExtra(Constants.IntentParams.DATA);
            mEditorDatas.clear();
            mEditorDatas.addAll(entity.mEditorDatas);
            mExplainAdapter.notifyDataSetChanged();
            setWhether();
            // CommonUtils.showToast(mContext, "添加了" + entity.mEditorDatas.size());
        }

        if (KEY_REQUEST_STARTPOS == requestCode && resultCode == RESULT_OK) {
            startTip = data.getParcelableExtra(Constants.IntentParams.DATA);
            startCity = data.getStringExtra(Constants.IntentParams.DATA2);
            double locs[] = {startTip.getPoint().getLatitude(),
                    startTip.getPoint().getLongitude()};
            LocationUtil.reverseGeo(mContext, locs, new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (regeocodeResult != null) {
                        startCity = regeocodeResult.getRegeocodeAddress().getCity();
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
            startTiptype = 1;
            mDepartText.setText(startTip.getDistrict() + startTip.getName());
            mDepartText.setTextColor(getResources().getColor(R.color.text_color14));
            setWhether();
        }

        if (KEY_REQUEST_ENDPOS == requestCode && resultCode == RESULT_OK) {
            endTip = data.getParcelableExtra(Constants.IntentParams.DATA);
            endCity = data.getStringExtra(Constants.IntentParams.DATA2);
            double locs[] = {endTip.getPoint().getLatitude(), endTip.getPoint().getLongitude()};
            LocationUtil.reverseGeo(mContext, locs, new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (regeocodeResult != null) {
                        endCity = regeocodeResult.getRegeocodeAddress().getCity();
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
            endTiptype = 1;
            mDestinationText.setText(endTip.getDistrict() + endTip.getName());
            mDestinationText.setTextColor(getResources().getColor(R.color.text_color14));
            setWhether();
        }

        if (KEY_REQUEST_GATHERPOS == requestCode && resultCode == RESULT_OK) {
            gatherTip = data.getParcelableExtra(Constants.IntentParams.DATA);
            gatherCity = data.getStringExtra(Constants.IntentParams.DATA2);
            double locs[] = {gatherTip.getPoint().getLatitude(),
                    gatherTip.getPoint().getLongitude()};
            LocationUtil.reverseGeo(mContext, locs, new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (regeocodeResult != null) {
                        gatherCity = regeocodeResult.getRegeocodeAddress().getCity();
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
            gatherTiptype = 1;
            mGatheringText.setText(gatherTip.getDistrict() + gatherTip.getName());
            mGatheringText.setTextColor(getResources().getColor(R.color.text_color14));
            setWhether();
        }

    }

    /**
     * 判断是否可以点击发布
     */
    public void setWhether() {

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
                            RequestBody.create(MediaType.parse("text/plain"), "thread"),
                            RequestBody.create(MediaType.parse("text/plain"), ""),
                            RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())),
                            item);
                }

            }
        });
    }

    private ProgressDialog mProgressDialog = null;

    private void initProgressDialog() {

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在上传，请稍候...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    private FileUploadView<DataItem> uploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem dataItem) {
            if (dataItem.code == 0) {
                mHintpicture.setVisibility(View.GONE);
                mDrivdetailslay.setVisibility(View.VISIBLE);
                mDrivCoverPath = dataItem.resp.data.path;
                mDrivdetailsCover.setImageURI(
                        Uri.parse(URLUtil.builderImgUrl(dataItem.resp.data.path, 540, 270)));
            } else {
                CommonUtils.showToast(mContext, dataItem.msg + "");
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

    @Override
    public void onClick(View view) {
        String dataString = "";
        TimeSelector TimePicKDialog = null;
        Map<String, Serializable> args = null;
        super.onClick(view);
        switch (view.getId()) {
            case R.id.hintpicture:
            case R.id.select_cover: //选择封面图
                ImagePicker.getInstance().setMultiMode(false);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.scene_type: //选择类型
                mDropUpPopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                //mDropUpPopupWindow.showAsDropDownBelwBtnView(mTitleLayout);
                mDropUpPopupWindow.show(CompileDrivingActivity.this);
                mDropUpPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPopupWindow.getIsSelec()) {
                            mTypeDataItem = selectDataItem;
                            mType.setText(mTypeDataItem.name);
                            setTypeLay(mTypeDataItem.id);
                        }
                    }
                });
                break;
            case R.id.starttime: //选择开始时间
                if (StringUtils.checkNull(beginTime)) {
                    dataString = beginTime;
                } else {
                    dataString = gainTime();
                }
                TimePicKDialog = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {

                        beginTime = time;
                        mStarttimeText.setText(time);
                        mStarttimeText.setTextColor(getResources().getColor(R.color.text_color14));
                        setWhether();
                    }
                }, dataString, "2199-12-31 23:59");

                TimePicKDialog.setScrollUnit(TimeSelector.SCROLLTYPE.YEAR,
                        TimeSelector.SCROLLTYPE.MONTH, TimeSelector.SCROLLTYPE.DAY,
                        TimeSelector.SCROLLTYPE.HOUR, TimeSelector.SCROLLTYPE.MINUTE);
                TimePicKDialog.show();
                break;
            case R.id.endtime: //选择结束时间
                if (StringUtils.checkNull(endTime)) {
                    dataString = endTime;
                } else {
                    dataString = gainTime();
                }
                TimePicKDialog = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        endTime = time;
                        mEndtimeText.setText(time);
                        mEndtimeText.setTextColor(getResources().getColor(R.color.text_color14));
                        setWhether();
                    }
                }, dataString, "2199-12-31 23:59");

                TimePicKDialog.setScrollUnit(TimeSelector.SCROLLTYPE.YEAR,
                        TimeSelector.SCROLLTYPE.MONTH, TimeSelector.SCROLLTYPE.DAY,
                        TimeSelector.SCROLLTYPE.HOUR, TimeSelector.SCROLLTYPE.MINUTE);
                TimePicKDialog.show();
                break;
            case R.id.abort: //选择报名截止时间
                if (StringUtils.checkNull(deadlineTime)) {
                    dataString = deadlineTime;
                } else {
                    dataString = gainTime();
                }
                TimePicKDialog = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        deadlineTime = time;
                        mAbortText.setText(time);
                        mAbortText.setTextColor(getResources().getColor(R.color.text_color14));
                        setWhether();
                    }
                }, dataString, "2199-12-31 23:59");

                TimePicKDialog.setScrollUnit(TimeSelector.SCROLLTYPE.YEAR,
                        TimeSelector.SCROLLTYPE.MONTH, TimeSelector.SCROLLTYPE.DAY,
                        TimeSelector.SCROLLTYPE.HOUR, TimeSelector.SCROLLTYPE.MINUTE);
                TimePicKDialog.show();

                /*                DateTimePickDialogUtil abortTimePicKDialog = new DateTimePickDialogUtil(
                        SponsorDrivingActivity.this, "");
                abortTimePicKDialog.dateTimePicKDialog(mStarttimeText,
                        new DateTimePickDialogUtil.onSelectTime() {
                            @Override
                            public void SelectTime(String time) {
                                deadlineTime = time;
                                mAbortText.setText(time);
                            }
                        });*/
                break;
            case R.id.upperlimit: //填写人数上限
                setUpperLimit();
                break;
            case R.id.budget: //预算填写
                setBudget();
                break;
            case R.id.require: //选择报名截止时间
                setRequire();
                break;
            case R.id.label: //标签
                KPlayCarApp.putValue(Constants.KeyParams.TAGS_DATA, mTags);
                CommonUtils.startResultNewActivity(this, null, TopicListActivity.class,
                        Constants.InteractionCode.REQUEST_CODE);
                break;
            case R.id.introduce: //活动介绍
                Intent intent1 = new Intent();
                intent1.putParcelableArrayListExtra(Constants.IntentParams.DATA, mEditorDatas);
                intent1.setClass(this, SponsorIntroduceActivity.class);
                startActivityForResult(intent1, RELEASE_INTRODUCE);
                break;
            case R.id.sponsord_depart: //开始地点
                CommonUtils.startResultNewActivity(this, null, SelectAddressActivity.class,
                        KEY_REQUEST_STARTPOS);
                break;
            case R.id.sponsord_destination: //目的地
                CommonUtils.startResultNewActivity(this, null, SelectAddressActivity.class,
                        KEY_REQUEST_ENDPOS);
                break;
            case R.id.sponsord_gathering: //集合地
                CommonUtils.startResultNewActivity(this, null, SelectAddressActivity.class,
                        KEY_REQUEST_GATHERPOS);
                break;
            case R.id.sponsord_project: //计划路线
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.INTENT_TYPE, Constants.PathBack.COMPILEDRIVING_SUCCEED);
                CommonUtils.startNewActivity(this, args, MyLineMainActivity.class);
                break;
            case R.id.confirm: //发布
                Confirm();
                break;
        }

    }

    public String gainTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public void setUpperLimit() {
        View diaEdtext = View.inflate(mContext, R.layout.dialog_limitedtext, null);
        final EditText limitMixEd = (EditText) diaEdtext.findViewById(R.id.limit_max);
        final EditText limitMinEd = (EditText) diaEdtext.findViewById(R.id.limit_min);
        if (!TextUtil.isEmpty(limitMin)) {
            limitMinEd.setText(limitMin);
        }
        if (!TextUtil.isEmpty(limitMax)) {
            if (!limitMax.equals("0")) {
                limitMixEd.setText(limitMax);
            } else {
                limitMixEd.setText("");
            }
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(diaEdtext).show();

        TextView mEnddiary = (TextView) diaEdtext.findViewById(R.id.appltlist_item_enddiary); //我在想想
        TextView mendgroup = (TextView) diaEdtext.findViewById(R.id.appltlist_item_endgroup); //确认取消
        mEnddiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mendgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String limitMaxs = "";
                String limitMins = "";
                if (limitMixEd.getText().toString().trim().equals("")
                        || limitMixEd.getText().toString().trim().equals("0")) {
                    limitMaxs = "0";
                } else {
                    limitMaxs = limitMixEd.getText().toString().trim();
                    if (Integer.parseInt(limitMaxs) > 10000) {
                        CommonUtils.showToast(mContext, "输入上限最多为10000");
                        return;
                    }
                }
                if (!limitMinEd.getText().toString().trim().equals("")) {
                    limitMins = limitMinEd.getText().toString().trim();
                    if (Integer.parseInt(limitMaxs) == 0) {
                        if (Integer.parseInt(limitMins) > 10000) {
                            CommonUtils.showToast(mContext, "输入下限最多为10000");
                            return;
                        }
                    } else {
                        if (Integer.parseInt(limitMins) > Integer.parseInt(limitMaxs)) {
                            CommonUtils.showToast(mContext, "下限不能高于上限");
                            return;
                        }
                    }
                } else {
                    CommonUtils.showToast(mContext, "请输入参与最少人数");
                    return;
                }


                if (limitMixEd.getText().toString().trim().equals("")
                        || limitMixEd.getText().toString().trim().equals("0")) {
                    limitMax = "0";
                    limitMaxString = "不限";
                } else {
                    limitMax = limitMixEd.getText().toString().trim();
                    limitMaxString = limitMixEd.getText().toString().trim();
                }
                limitMin = limitMinEd.getText().toString().trim();
                mUpperlimitText.setText(limitMin + "/" + limitMaxString);
                mUpperlimitText.setTextColor(getResources().getColor(R.color.text_color14));
                setWhether();
                alertDialog.dismiss();
            }
        });

    }


    public void setUpperLimitEdtext(final EditText limitMixEd, final EditText limitMinEd) {
        limitMixEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtil.isEmpty(editable.toString().trim())) {
                    int num = Integer.parseInt(editable.toString().trim());
                    if (num > 10000) {
                        limitMixEd.setText("10000");
                        limitMixEd.setSelection(limitMixEd.getSelectionEnd());//设置光标在最后
                        CommonUtils.showToast(mContext, "最大上限为10000人");
                    }
                }
            }
        });

        limitMinEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
/*                int num = Integer.parseInt(editable.toString().trim());
                int mixNum = Integer.parseInt(limitMixEd.getText().toString().trim());
                if (mixNum != 0) {
                    if (num > mixNum) {
                        limitMinEd.setText(mixNum + "");
                        CommonUtils.showToast(mContext, "最大上限为10000人");
                        limitMinEd.setSelection(limitMinEd.getSelectionEnd());//设置光标在最后
                    }
                } else {
                    if (num > 10000) {
                        limitMinEd.setText("10000");
                        CommonUtils.showToast(mContext, "最大上限为10000人");
                        limitMinEd.setSelection(limitMinEd.getSelectionEnd());//设置光标在最后
                    }
                }*/

            }
        });

    }

    public void setRequire() {
        View diaEdtext = View.inflate(mContext, R.layout.dialog_textedtext, null);
        final EditText editText = (EditText) diaEdtext.findViewById(R.id.edtext);
        TextView mTitle = (TextView) diaEdtext.findViewById(R.id.title);
        mTitle.setText("请输入其他报名要求");
        if (otherCondition != null) {
            editText.setText(otherCondition);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(diaEdtext)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        otherCondition = editText.getText().toString().trim();
                        if (!otherCondition.equals("")) {
                            mRequireText.setText(otherCondition);
                            mRequireText.setTextColor(getResources().getColor(R.color.text_color14));
                        } else {
                            mRequireText.setText("请输入");
                            mRequireText.setTextColor(getResources().getColor(R.color.text_content_color));
                        }
                    }
                }).setNegativeButton("取消", null).show();

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5); //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth()); //宽度设置为屏幕的0.5
        alertDialog.getWindow().setAttributes(p); //设置生效

    }

    public void setBudget() {
        View diaEdtext = View.inflate(mContext, R.layout.dialog_edtext, null);
        final EditText editText = (EditText) diaEdtext.findViewById(R.id.edtext);
        TextView mTitle = (TextView) diaEdtext.findViewById(R.id.title);
        mTitle.setText("请输入人均预算(元)");
        if (TextUtil.isEmpty(budget)) {
            editText.setText("");
        } else {
            editText.setText(budget);
        }
        new AlertDialog.Builder(this).setView(diaEdtext)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        budget = editText.getText().toString().trim();
                        if (!budget.equals("")) {
                            mBudgetText.setText(budget);
                            mBudgetText.setTextColor(getResources().getColor(R.color.text_color14));
                        } else {
                            mBudgetText.setText("请输入");
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * 添加标签
     *
     * @param datas
     */
    private void addTagItems(List<Topic> datas) {
        mTagLayout.removeAllViews();
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            Topic topic = datas.get(i);
            View v = LayoutInflater.from(mContext).inflate(R.layout.text_tag_item2, mTagLayout,
                    false);
            v.setTag(topic.id);
            mTagLayout.addView(v);
            TextView tv = (TextView) v.findViewById(R.id.name);
            tv.setText(topic.title + "");
            tv.setTag(topic.id);
            ImageView imgView = (ImageView) v.findViewById(R.id.img);
            imgView.setTag(v);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View parentView = (View) v.getTag();
                    mTagLayout.removeViewInLayout(parentView);
                    mTagLayout.invalidate();
                    removeTagById(Integer.parseInt(parentView.getTag().toString()));
                }
            });

        }
    }

    public void Confirm() {
        List<String> mTagId = new ArrayList<>();
        List<SEditorData> SEditorData = new ArrayList<>();

        for (Topic t : mTags) {
            mTagId.add(t.id + "");
        }

        if (StringUtils.checkNull(mDrivCoverPath)) {

        } else {
            CommonUtils.showToast(mContext, "请选择封面图");
            return;
        }

        if (StringUtils.checkNull(mTitle.getText().toString().trim())) {
            title = mTitle.getText().toString().trim();
        } else {
            CommonUtils.showToast(mContext, "请填写活动名称");
            return;
        }

        if (StringUtils.checkNull(beginTime)) {

        } else {
            CommonUtils.showToast(mContext, "请选择开始时间");
            return;
        }

        if (StringUtils.checkNull(endTime)) {

        } else {
            CommonUtils.showToast(mContext, "请选择结束时间");
            return;
        }

        if (StringUtils.checkNull(limitMax) && StringUtils.checkNull(limitMin)) {

        } else {
            CommonUtils.showToast(mContext, "请输入人数限制");
            return;
        }

        if (StringUtils.checkNull(deadlineTime)) {

        } else {
            CommonUtils.showToast(mContext, "请选择报名截止时间");
            return;
        }

        if (mTypeDataItem.id == 6 || mTypeDataItem.id == 7) {
            if (StringUtils.checkNull(startCity)) {
                if (startTip.getPoint() != null) {

                } else {
                    CommonUtils.showToast(mContext, "开始地址错误请重选开始地点");
                    return;
                }
            } else {
                CommonUtils.showToast(mContext, "请选择开始地点");
                return;
            }

            if (StringUtils.checkNull(endCity)) {
                if (endTip.getPoint() != null) {

                } else {
                    CommonUtils.showToast(mContext, "结束地址错误请重选开始地点");
                    return;
                }
            } else {
                CommonUtils.showToast(mContext, "请选择结束地点");
                return;
            }

        } else {
            if (StringUtils.checkNull(gatherCity)) {
                if (gatherTip.getPoint() != null) {

                } else {
                    CommonUtils.showToast(mContext, "集结地址错误请重选开始地点");
                    return;
                }
            } else {
                CommonUtils.showToast(mContext, "请选择集结地");
                return;
            }

        }

        Gson gson = new Gson();

        if (mEditorDatas.size() != 0) {
            Map<String, List<SEditorData>> map = new HashMap<String, List<SEditorData>>();
            map.put("mEditorDatas", mEditorDatas);
            introJson = gson.toJson(map);
        } else {
            CommonUtils.showToast(mContext, "请编辑活动介绍");
            return;
        }

        if (mEditorDatas.size() != 0) {
            topic = gson.toJson(mTagId);
        }

        if (mCheckBox.isChecked()) {

        } else {
            CommonUtils.showToast(mContext, "请查看活动协议并同意");
            return;
        }

        setLoadingIndicator(true);
        if (mTypeDataItem.id == 6 || mTypeDataItem.id == 7) {
            mCompileDrivingPresenter.getCreate(builderParams());
        } else {
            mCompileDrivingPresenter.getCreate(builderParams1());
        }

    }

    private Map<String, String> builderParams() {
        String destination = "";
        String start = "";
        if (endTiptype != -1) {
            destination = endTip.getName();
        } else {
            destination = endTip.getDistrict();
        }
        if (startTiptype != -1) {
            start = startTip.getDistrict() + startTip.getName();
        } else {
            start = startTip.getDistrict();
        }


        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("begin_time", beginTime);
        params.put("deadline_time", deadlineTime);
        params.put("end_time", endTime);
        params.put("budget", budget);
        params.put("cover", mDrivCoverPath);
        params.put("intro", introJson);
        params.put("limit_max", limitMax);
        params.put("limit_min", limitMin);
        params.put("need_review", needReview);
        params.put("only_owner", onlyOwner);
        params.put("other_condition", otherCondition);
        params.put("roadbook_id", roadbookId);
        params.put("scene_type", mTypeDataItem.id + "");
        params.put("title", title);
        params.put("topic", topic);
        params.put("dest_city", endCity);
        params.put("dest_latitude", endTip.getPoint().getLatitude() + "");
        params.put("dest_longitude", endTip.getPoint().getLongitude() + "");
        params.put("destination", destination);
        params.put("start", start);
        params.put("start_city", startCity);
        params.put("start_latitude", startTip.getPoint().getLatitude() + "");
        params.put("start_longitude", startTip.getPoint().getLongitude() + "");

        params.put("start_adcode", startTip.getAdcode());
        params.put("dest_adcode", endTip.getAdcode());

        params.put("longitude", mCompileDrivingBean.data.longitude);
        params.put("latitude", mCompileDrivingBean.data.latitude);
        params.put("local_address", mCompileDrivingBean.data.localAddress);
        params.put("adcode", mCompileDrivingBean.data.adcode);
        params.put("id", ID);
        return params;
    }

    private Map<String, String> builderParams1() {
        String gather = "";
        if (gatherTiptype != -1) {
            gather = gatherTip.getName();
        } else {
            gather = gatherTip.getDistrict();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("begin_time", beginTime);
        params.put("deadline_time", deadlineTime);
        params.put("end_time", endTime);
        params.put("budget", budget);
        params.put("cover", mDrivCoverPath);
        params.put("intro", introJson);
        params.put("limit_max", limitMax);
        params.put("limit_min", limitMin);
        params.put("need_review", needReview);
        params.put("only_owner", onlyOwner);
        params.put("other_condition", otherCondition);
        params.put("roadbook_id", roadbookId);
        params.put("scene_type", mTypeDataItem.id + "");
        params.put("title", title);
        params.put("topic", topic);
        params.put("dest_city", gatherCity);
        params.put("dest_latitude", gatherTip.getPoint().getLatitude() + "");
        params.put("dest_longitude", gatherTip.getPoint().getLongitude() + "");
        params.put("destination", gather);
        params.put("start", "");
        params.put("start_city", "");
        params.put("start_latitude", "");
        params.put("start_longitude", "");


        params.put("start_adcode", "");
        params.put("dest_adcode", gatherTip.getAdcode());

        params.put("longitude", mCompileDrivingBean.data.longitude);
        params.put("latitude", mCompileDrivingBean.data.latitude);
        params.put("local_address", mCompileDrivingBean.data.localAddress);
        params.put("adcode", mCompileDrivingBean.data.adcode);
        params.put("id", ID);
        return params;
    }

    @Override
    public void dataSucceed(CompileDrivingBean mCompileDrivingBean) {
        CompileDrivingBean.Data data = mCompileDrivingBean.data;
        this.mCompileDrivingBean = mCompileDrivingBean;
        if (data.cover != null) {
            mHintpicture.setVisibility(View.GONE);
            mDrivdetailslay.setVisibility(View.VISIBLE);
            mDrivCoverPath = data.cover;
            mDrivdetailsCover.setImageURI(Uri.parse(URLUtil.builderImgUrl(data.cover, 540, 270)));
        }
        mTitle.setText(data.title);
        for (DataItem dataItem : mDataList) {
            if (dataItem.id == data.sceneType) {
                mTypeDataItem = dataItem;
                mType.setText(mTypeDataItem.name);
                setTypeLay(mTypeDataItem.id);
            }
        }

        switch (data.sceneType) {
            case 6:
            case 7:
                startTip = new Tip();
                startTip.setAdcode(data.startAdcode);
                startTip.setDistrict(data.start);
                startTip.setPostion(new LatLonPoint(data.startLatitude, data.startLongitude));
                startCity = data.startCity;

                mDepartText.setText(startTip.getDistrict());
                mDepartText.setTextColor(getResources().getColor(R.color.text_color14));

                endTip = new Tip();
                endTip.setAdcode(data.destAdcode);
                endTip.setDistrict(data.destination);
                endTip.setPostion(new LatLonPoint(data.destLatitude, data.destLongitude));
                endCity = data.destCity;

                mDestinationText.setText(endTip.getDistrict());
                mDestinationText.setTextColor(getResources().getColor(R.color.text_color14));
                break;
            case 5:
            case 2:
                gatherTip = new Tip();
                gatherTip.setDistrict(data.destination);
                gatherTip.setPostion(new LatLonPoint(data.destLatitude, data.destLongitude));
                gatherCity = data.destCity;

                mGatheringText.setText(gatherTip.getDistrict());
                mGatheringText.setTextColor(getResources().getColor(R.color.text_color14));

                break;
        }
        startTiptype = -1;
        endTiptype = -1;
        gatherTiptype = -1;

        if (data.roadbookId != 0) {
            roadbookId = data.roadbookId + "";
            roadbooktitle = data.roadbookTitle;
            mProjectText.setText(roadbooktitle);
            mProjectText.setTextColor(getResources().getColor(R.color.text_color14));
        }
        //endTime = data.endTime;
        endTime = DateFormatUtil.formatTime5(data.endTime);
        mEndtimeText.setText(endTime);
        mEndtimeText.setTextColor(getResources().getColor(R.color.text_color14));

        //beginTime = data.beginTime;
        beginTime = DateFormatUtil.formatTime5(data.beginTime);
        mStarttimeText.setText(beginTime);
        mStarttimeText.setTextColor(getResources().getColor(R.color.text_color14));

        budget = data.budget;
        mBudgetText.setText(budget);
        mBudgetText.setTextColor(getResources().getColor(R.color.text_color14));

        limitMin = data.limitMin + "";
        limitMax = data.limitMax + "";
        if (data.limitMax == 0) {
            mUpperlimitText.setText(limitMin + "/不限");
        } else {
            mUpperlimitText.setText(limitMin + "/" + limitMax);
        }
        mUpperlimitText.setTextColor(getResources().getColor(R.color.text_color14));

        //deadlineTime = data.deadlineTime;
        deadlineTime = DateFormatUtil.formatTime5(data.deadlineTime);
        mAbortText.setText(deadlineTime);
        mAbortText.setTextColor(getResources().getColor(R.color.text_color14));
        if (data.onlyOwner == 1) {
            onlyOwner = "1";
            mOwnerSwitchButton.setChecked(true);
        } else {
            onlyOwner = "0";
            mOwnerSwitchButton.setChecked(false);
        }

        if (data.needReview == 1) {
            needReview = "1";
            mApproveSwitchButton.setChecked(true);
        } else {
            needReview = "0";
            mApproveSwitchButton.setChecked(false);
        }

        otherCondition = data.otherCondition;
        mRequireText.setText(otherCondition);
        mRequireText.setTextColor(getResources().getColor(R.color.text_color14));

        List<CompileDrivingBean.TopicList> topicList = data.topicList;
        for (CompileDrivingBean.TopicList topicbean : topicList) {
            Topic topic = new Topic();
            topic.id = topicbean.id;
            topic.title = topicbean.title;
            mTags.add(topic);
        }
        if (mTags.size() > 0) {
            mTagRootLayout.setVisibility(View.VISIBLE);
        } else {
            mTagRootLayout.setVisibility(View.GONE);
        }
        addTagItems(mTags);

        String intro = data.intro;
        List<SEditorData> list = new ArrayList<SEditorData>();

        JSONObject object = (JSONObject) JSON.parse(intro);
        JSONArray mEditorDataslist = object.getJSONArray("mEditorDatas");
        for (int i = 0; i < mEditorDataslist.size(); i++) {
            SEditorData sEditorData = new SEditorData();
            JSONObject jsonobject = (JSONObject) mEditorDataslist.get(i);
            sEditorData.setImagePath(jsonobject.getString("imagePath"));
            sEditorData.setImageUrl(jsonobject.getString("imageUrl"));
            sEditorData.setInputStr(jsonobject.getString("inputStr"));
            list.add(sEditorData);
        }
        mEditorDatas.addAll(list);
        mExplainAdapter.notifyDataSetChanged();
    }

    /**
     * 根据id,移出已选择的话题Tag
     *
     * @param id
     */
    private void removeTagById(int id) {

        List<Topic> temps = new ArrayList<Topic>();
        for (Topic t : mTags) {
            temps.add(t);
        }

        for (Topic t : temps) {
            if (id == t.id) {
                mTags.remove(t);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        TopicTagDataModel topicTagDataModel = KPlayCarApp.getValue(Constants.KeyParams.SYS_TAG_MODEL_VALUE);

        if (topicTagDataModel != null) {
            boolean isExist = true;
            Topic topic = new Topic();
            topic.id = topicTagDataModel.id;
            topic.title = topicTagDataModel.title;
            int size = mTags.size();
            if (size >= TAG_MAX) {
                CommonUtils.showToast(mContext, "最多只能添加5个标签");
            } else {
                for (Topic item : mTags) {
                    if (item.id == topic.id) {
                        isExist = false;
                    }
                }
                if (isExist) {
                    mTags.add(topic);
                    addTagItems(mTags);
                } else {
                    CommonUtils.showToast(mContext, "您已添加了该标签");
                }
            }
            KPlayCarApp.removeValue(Constants.KeyParams.SYS_TAG_MODEL_VALUE);
        }


        int size = mTags.size();
        if (size > 0) {
            mTagRootLayout.setVisibility(View.VISIBLE);
        } else {
            mTagRootLayout.setVisibility(View.GONE);
        }

        boolean isSPONSOR = KPlayCarApp.getValue(Constants.PathBack.SPONSORIS);

        if (isSPONSOR) {
            roadbookId = KPlayCarApp.getValue(Constants.PathBack.SPONSOR);
            String roadbooktitle = KPlayCarApp.getValue(Constants.PathBack.ROADBOOKTITLE);
            mProjectText.setText(roadbooktitle);
            mProjectText.setTextColor(getResources().getColor(R.color.text_color14));
            KPlayCarApp.putValue(Constants.PathBack.SPONSORIS, false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        KPlayCarApp.removeValue(Constants.PathBack.SPONSORIS);
        KPlayCarApp.removeValue(Constants.PathBack.SPONSOR);
        if (mCompileDrivingPresenter != null) {
            mCompileDrivingPresenter.detachView();
        }
        if (mFileUploadPresenter != null) {
            mFileUploadPresenter.detachView();
        }
    }

    @Override
    public void dataListSucceed() {
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext, "修改成功");
        finish();
    }

    @Override
    public void dataListDefeated(String msg) {
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void dataDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
        finish();
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

    private void showQR() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.driving_dialog, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        ImageView img = (ImageView) v.findViewById(R.id.img);
        TextView mEnddiary = (TextView) v.findViewById(R.id.appltlist_item_enddiary); //我在想想
        TextView mendgroup = (TextView) v.findViewById(R.id.appltlist_item_endgroup); //确认取消
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mEnddiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mendgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        alertDialog.show();
        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 300),
                BitmapUtils.dp2px(mContext, 300));
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            showQR();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
