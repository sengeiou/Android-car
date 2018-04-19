package com.tgf.kcwc.me.exhibition;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.StoreBrandListsActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.CarColor;
import com.tgf.kcwc.mvp.model.CarSelectBean;
import com.tgf.kcwc.mvp.model.CompileExhibitionBean;
import com.tgf.kcwc.mvp.model.StoreBelowUpBean;
import com.tgf.kcwc.mvp.presenter.CompileStreExhibitionPresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.view.CompileStoreExhibitionView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropUpColourPopupWindow;
import com.tgf.kcwc.view.FunctionView;

import java.io.File;
import java.io.IOException;
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
 * Created by Administrator on 2017/7/19.
 */

public class CompileStoreExhibitionActivity extends BaseActivity implements CompileStoreExhibitionView {

    private static final int IMAGE_PICKER = 1001;              //图片
    private FileUploadPresenter mFileUploadPresenter;                    //上传图片
    private CompileStreExhibitionPresenter mCompileStreExhibitionPresenter;

    private TextView username;
    private TextView usertime;

    private RelativeLayout mTitleBar;
    private TextView title;                                 //展会标题
    private LinearLayout carType;                                 //车系类型
    private TextView type;                                    //车系类型名字

    private LinearLayout facadelayout;                            //车外观
    private TextView mFacadename;                             //车外观颜色
    private SimpleDraweeView facadeimage;                             //车外观图片
    private LinearLayout facadeselect;                            //车外观图片选择
    private RelativeLayout facadelay;                            //车外观图片更换

    private LinearLayout upholstery;                              //车内饰
    private TextView mUpholsteryname;                         //车内饰颜色
    private SimpleDraweeView upholsteryimage;                         //车内饰图片
    private LinearLayout upholsteryselect;                        //车内饰图片选择
    private LinearLayout userlayout;
    private RelativeLayout upholsterylay;                            //车内饰图片更换

    //  private EditText                  mEdtext;                                 //外观可见差异配置
    private TextView mUploading;                              //上传
    private TextView release;                                 //发布

    private boolean isSelect = true;              //ture 外观   false内饰
    private String facadeUrl = "";                //车外观URL
    private String upholsteryUrl = "";                //车内饰URL
    private int intExtra;

    private DropUpColourPopupWindow mDropUpColourPopupWindow;
    private List<DataItem> mDataList = new ArrayList<>(); //type数据

    private boolean isSelectColour = true;              //ture 外观   false内饰
    private CarSelectBean mCarSelectBean = null;              //选择的车系车型数据
    private DataItem mDataItemFacade = null;              //车外观颜色
    private DataItem mDataItemUpholstery = null;              //车内饰颜色
    private String typeString = "";                //1是上传 2是发布
    private String ExhibitionID = "";                //展会ID
    private int ID;

    @Override
    protected void setUpViews() {
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(uploadView);
        mCompileStreExhibitionPresenter = new CompileStreExhibitionPresenter();
        mCompileStreExhibitionPresenter.attachView(this);
        initProgressDialog();

        mTitleBar = (RelativeLayout) findViewById(R.id.titleBar);
        carType = (LinearLayout) findViewById(R.id.car_type);
        type = (TextView) findViewById(R.id.type);
        title = (TextView) findViewById(R.id.title);

        userlayout = (LinearLayout) findViewById(R.id.userlayout);
        username = (TextView) findViewById(R.id.username);
        usertime = (TextView) findViewById(R.id.usertime);

        facadelayout = (LinearLayout) findViewById(R.id.facadelayout);
        facadeimage = (SimpleDraweeView) findViewById(R.id.facadeimage);
        facadeselect = (LinearLayout) findViewById(R.id.facadeselect);
        mFacadename = (TextView) findViewById(R.id.facadename);
        facadelay = (RelativeLayout) findViewById(R.id.facadelay);

        upholstery = (LinearLayout) findViewById(R.id.upholstery);
        upholsteryimage = (SimpleDraweeView) findViewById(R.id.upholsteryimage);
        upholsteryselect = (LinearLayout) findViewById(R.id.upholsteryselect);
        mUpholsteryname = (TextView) findViewById(R.id.upholsteryname);
        upholsterylay = (RelativeLayout) findViewById(R.id.upholsterylay);

        mUploading = (TextView) findViewById(R.id.uploading);
        release = (TextView) findViewById(R.id.release);

        mTitleBar.setBackgroundColor(getResources().getColor(R.color.app_layout_bg_color));

        if (intExtra == 1) {
            mUploading.setVisibility(View.VISIBLE);
            mUploading.setText("保存");
            release.setVisibility(View.GONE);
        } else {
            mUploading.setVisibility(View.VISIBLE);
            mUploading.setText("保存但不发布");
            release.setVisibility(View.VISIBLE);
        }
        facadelayout.setOnClickListener(this);
        facadeselect.setOnClickListener(this);
        upholstery.setOnClickListener(this);
        upholsteryselect.setOnClickListener(this);
        carType.setOnClickListener(this);
        mUploading.setOnClickListener(this);
        release.setOnClickListener(this);
        facadelay.setOnClickListener(this);
        upholsterylay.setOnClickListener(this);
        mCompileStreExhibitionPresenter.getDetail(IOUtils.getToken(mContext), ID + "");
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        back.setImageResource(R.drawable.nav_back_selector2);
        backEvent(back);
        text.setText("编辑展会展车");
        text.setTextSize(16);
        text.setTextColor(getResources().getColor(R.color.text_color14));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intExtra = getIntent().getIntExtra(Constants.IntentParams.ID, 1);
        ID = getIntent().getIntExtra(Constants.IntentParams.ID2, 1);
        setContentView(R.layout.activity_sponsor_storeexhibition);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent = null;
        Map<String, Serializable> arg = null;
        switch (view.getId()) {
            case R.id.car_type:
                KPlayCarApp.putValue(Constants.KeyParams.IS_CONTRAST, false);
                arg = new HashMap<String, Serializable>();
                arg.put(Constants.IntentParams.INTENT_TYPE,
                        Constants.IntentParams.SINGLE_BRAND_TYPE);
                arg.put(Constants.IntentParams.IS_JUMP, true);
                arg.put(Constants.IntentParams.TITLE, true);
                arg.put(Constants.IntentParams.IS_SEPBRAND, true);
                KPlayCarApp.putValue(Constants.IntentParams.SPONSORPLEASE, Constants.CarSeriesBack.COMPILESTOREEXHIBITION_SUCCEED);
                CommonUtils.startNewActivity(this, arg, StoreBrandListsActivity.class);
                break;
            case R.id.upholstery:
                if (mCarSelectBean == null) {
                    CommonUtils.showToast(mContext, "请先选择车型");
                    return;
                }
                isSelectColour = false;
                if (mCarSelectBean.carId == 0) {
                    mCompileStreExhibitionPresenter.getCarCategoryColors(mCarSelectBean.seriesId + "",
                            "car_series", "in");
                } else {
                    mCompileStreExhibitionPresenter.getCarCategoryColors(mCarSelectBean.carId + "",
                            "car", "in");
                    //mSponsorStreBelowPresenter.getCarCategoryColors("17", "car", "in");
                }
                break;
            case R.id.upholsteryselect:
            case R.id.upholsterylay:
                isSelect = false;
                ImagePicker.getInstance().setMultiMode(false);
                intent = new Intent(this, ImageGridActivity.class);
                intent.putExtra(Constants.IntentParams.PHONETYPE, true);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.facadelayout:
                if (mCarSelectBean == null) {
                    CommonUtils.showToast(mContext, "请先选择车型");
                    return;
                }
                isSelectColour = true;
                if (mCarSelectBean.carId == 0) {
                    mCompileStreExhibitionPresenter.getCarCategoryColors(mCarSelectBean.seriesId + "",
                            "car_series", "out");
                } else {
                    mCompileStreExhibitionPresenter.getCarCategoryColors(mCarSelectBean.carId + "",
                            "car", "out");
                    // mSponsorStreBelowPresenter.getCarCategoryColors("17", "car", "out");
                }
                break;
            case R.id.facadeselect:
            case R.id.facadelay:
                isSelect = true;
                ImagePicker.getInstance().setMultiMode(false);
                intent = new Intent(this, ImageGridActivity.class);
                intent.putExtra(Constants.IntentParams.PHONETYPE, true);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.uploading:
                typeString = "1";
                uploading();
                break;
            case R.id.release:
                typeString = "2";
                uploading();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KPlayCarApp.removeValue(Constants.IntentParams.SPONSORPLEASE);
        if (mFileUploadPresenter != null) {
            mFileUploadPresenter.detachView();
        }
        if (mFileUploadPresenter != null) {
            mFileUploadPresenter.detachView();
        }
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
                if (isSelect) {
                    facadeselect.setVisibility(View.GONE);
                    facadelay.setVisibility(View.VISIBLE);
                    facadeimage.setImageURI(
                            Uri.parse(URLUtil.builderImgUrl(dataItem.resp.data.path, 270, 203)));
                    facadeUrl = dataItem.resp.data.path;
                } else {
                    upholsteryselect.setVisibility(View.GONE);
                    upholsterylay.setVisibility(View.VISIBLE);
                    upholsteryimage.setImageURI(
                            Uri.parse(URLUtil.builderImgUrl(dataItem.resp.data.path, 270, 203)));
                    upholsteryUrl = dataItem.resp.data.path;
                }
                // coverpath = dataItem.resp.data.path;
                //compile();
            }
            //  CommonUtils.showToast(mContext, dataItem.msg + "");
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

    public void uploading() {
        StoreBelowUpBean storeBelowUpBean = new StoreBelowUpBean();
        if (mCarSelectBean == null) {
            CommonUtils.showToast(mContext, "请选择车型");
            return;
        }
        if (mDataItemFacade == null) {
            CommonUtils.showToast(mContext, "请选择外观颜色");
            return;
        }
        if (TextUtil.isEmpty(facadeUrl)) {
            CommonUtils.showToast(mContext, "请选择外观图片");
            return;
        }

        if (mDataItemUpholstery == null) {
            CommonUtils.showToast(mContext, "请选择内饰颜色");
            return;
        }
        if (TextUtil.isEmpty(upholsteryUrl)) {
            CommonUtils.showToast(mContext, "请选择内饰图片");
            return;
        }

        storeBelowUpBean.carId = mCarSelectBean.carId + "";
        storeBelowUpBean.seriesId = mCarSelectBean.seriesId + "";
        storeBelowUpBean.carName = mCarSelectBean.name + "";
        storeBelowUpBean.appearanceColorName = mDataItemFacade.name + "";
        storeBelowUpBean.appearanceColorValue = mDataItemFacade.colours + "";
        storeBelowUpBean.appearanceImg = facadeUrl;
        storeBelowUpBean.interiorColorName = mDataItemUpholstery.name;
        storeBelowUpBean.interiorColorValue = mDataItemUpholstery.colours + "";
        storeBelowUpBean.interiorImg = upholsteryUrl;
        storeBelowUpBean.status = typeString;
        storeBelowUpBean.factoryId = mCarSelectBean.mFirmId + "";
        storeBelowUpBean.boothId = mCarSelectBean.boothId + "";
        storeBelowUpBean.eventId = ExhibitionID + "";
        storeBelowUpBean.hallId = mCarSelectBean.hallId + "";
        storeBelowUpBean.id = ID + "";
        mCompileStreExhibitionPresenter.getCreate(IOUtils.getToken(mContext), storeBelowUpBean);
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

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            CarSelectBean carSelectBean = new CarSelectBean();
            String brandName = KPlayCarApp.getValue(Constants.CarSeriesBack.NAME);
            if (StringUtils.checkNull(brandName)) {
                int carId = KPlayCarApp.getValue(Constants.CarSeriesBack.ID);
                int carSeriesId = KPlayCarApp.getValue(Constants.CarSeriesBack.CARID);
                int mFirmId = KPlayCarApp.getValue(Constants.CarSeriesBack.MFIRMID);
                String hallId = KPlayCarApp.getValue(Constants.CarSeriesBack.HALLID);
                String boothId = KPlayCarApp.getValue(Constants.CarSeriesBack.BOOTHID);
                carSelectBean.name = brandName;
                carSelectBean.carId = carId;
                carSelectBean.seriesId = carSeriesId;
                carSelectBean.mFirmId = mFirmId;
                carSelectBean.hallId = hallId;
                carSelectBean.boothId = boothId;
                mCarSelectBean = carSelectBean;
                type.setTextColor(getResources().getColor(R.color.text_color14));
                type.setText(brandName);
                mDataItemFacade = null;
                mFacadename.setText("请选择");
                mFacadename.setTextColor(getResources().getColor(R.color.text_content_color));
                mDataItemUpholstery = null;
                mUpholsteryname.setText("请选择");
                mUpholsteryname.setTextColor(getResources().getColor(R.color.text_content_color));
            }
        }
    }

    @Override
    public void dataSucceed(BaseBean basebean) {
        if (typeString.equals("1")) {
            CommonUtils.showToast(mContext, "上传成功");
        } else {
            CommonUtils.showToast(mContext, "发布成功");
        }
        finish();
    }

    @Override
    public void dataColourSucceed(List<CarColor> datalist) {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.name = "选择颜色";
        mDataList.add(dataItem);
        for (CarColor carColor : datalist) {
            dataItem = new DataItem();
            dataItem.name = carColor.name;
            dataItem.id = carColor.id;
            dataItem.colours = carColor.value;
            dataItem.nickname = carColor.type;
            mDataList.add(dataItem);
        }
        mDropUpColourPopupWindow = new DropUpColourPopupWindow(mContext, mDataList);
        //  mDropUpColourPopupWindow.showAsDropDownBelwBtnView(mTitleBar);
        mDropUpColourPopupWindow.show(CompileStoreExhibitionActivity.this);
        mDropUpColourPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                DataItem selectDataItem = mDropUpColourPopupWindow.getSelectDataItem();
                if (selectDataItem != null && mDropUpColourPopupWindow.getIsSelec()) {
                    if (isSelectColour) {
                        mDataItemFacade = selectDataItem;
                        mFacadename.setText(selectDataItem.name);
                        mFacadename.setTextColor(getResources().getColor(R.color.text_color14));
                    } else {
                        mDataItemUpholstery = selectDataItem;
                        mUpholsteryname.setText(selectDataItem.name);
                        mUpholsteryname.setTextColor(getResources().getColor(R.color.text_color14));
                    }

                }
            }
        });
    }

    @Override
    public void gainDataSucceed(CompileExhibitionBean baseBean) {
        CompileExhibitionBean.Detail detail = baseBean.data.detail;
        userlayout.setVisibility(View.VISIBLE);
        username.setText("上传人： " + detail.postNickname);
        usertime.setText("上传时间： " + detail.postTime);
        type.setTextColor(getResources().getColor(R.color.text_color14));
        // type.setText(detail.productName);
/*        if(detail.productId!=0){
            type.setText(detail.factoryName + " " + detail.seriesName + " " + detail.productName);
        }else {
            type.setText(detail.factoryName + " " + detail.seriesName + " 不限车型" );
        }*/
        if (detail.productId != 0) {
            type.setText(detail.seriesName + " " + detail.productName);
        } else {
            type.setText(detail.seriesName + " 不限车型");
        }
        mCarSelectBean = new CarSelectBean();
        mCarSelectBean.name = detail.productName;
        mCarSelectBean.carId = detail.productId;
        mCarSelectBean.seriesId = detail.seriesId;
        mCarSelectBean.mFirmId = detail.factoryId;
        mCarSelectBean.hallId = detail.hallId + "";
        mCarSelectBean.boothId = detail.boothId + "";

        ExhibitionID = baseBean.data.event.eventId + "";
        title.setText(baseBean.data.event.name);

        mDataItemFacade = new DataItem();
        mDataItemFacade.name = detail.appearanceColorName;
        mDataItemFacade.colours = detail.appearanceColorValue;
        mFacadename.setTextColor(getResources().getColor(R.color.text_color14));
        mFacadename.setText(detail.appearanceColorName);
        mDataItemUpholstery = new DataItem();
        mDataItemUpholstery.name = detail.interiorColorName;
        mDataItemUpholstery.colours = detail.interiorColorValue;

        mUpholsteryname.setTextColor(getResources().getColor(R.color.text_color14));
        mUpholsteryname.setText(detail.interiorColorName);

        facadeUrl = detail.appearanceImg;
        upholsteryUrl = detail.interiorImg;

        facadeselect.setVisibility(View.GONE);
        facadelay.setVisibility(View.VISIBLE);
        facadeimage.setVisibility(View.VISIBLE);
        upholsteryselect.setVisibility(View.GONE);
        upholsterylay.setVisibility(View.VISIBLE);
        upholsteryimage.setVisibility(View.VISIBLE);

        facadeimage.setImageURI(Uri.parse(URLUtil.builderImgUrl(detail.appearanceImg, 270, 203)));
        upholsteryimage.setImageURI(Uri.parse(URLUtil.builderImgUrl(detail.interiorImg, 270, 203)));

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
}
