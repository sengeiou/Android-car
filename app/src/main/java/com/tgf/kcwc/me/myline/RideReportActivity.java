package com.tgf.kcwc.me.myline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.entity.RichEditorEntity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.RideDataItem;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.RideDataPresenter;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.RideDataView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.tripbook.EditTripbookActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.ScreenShotHelper;
import com.tgf.kcwc.util.ThreadPoolProxy;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.OpenRideDataShareWindow;
import com.tgf.kcwc.view.VerticalProgressBar;
import com.tgf.kcwc.view.richeditor.SEditorData;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author:Jenny
 * Date:2017/5/4
 * E-mail:fishloveqin@gmail.com
 * 驾图报告
 */

public class RideReportActivity extends BaseActivity
        implements RideDataView<RideReportDetailModel> {

    protected MapView mMapView;
    protected SimpleDraweeView avatar;
    protected TextView name;
    protected TextView carName;
    protected TextView address;
    protected TextView time;
    protected RelativeLayout baseInfoLayout;
    protected TextView rideMileTv;
    protected TextView totalTime;
    protected TextView rideTime;
    protected RelativeLayout rideDataInfoLayout;
    protected TextView editRideTv;
    protected TextView shareTv;
    protected RelativeLayout relayOption;
    protected RelativeLayout linearlayoutMain;
    private GridView mGrid;

    private FileUploadPresenter mUploadPresenter = null, mCoverUploadPresenter = null;
    private List<RideDataItem> mItems = new ArrayList<RideDataItem>();

    private WrapAdapter<RideDataItem> mAdapter;

    private RideDataPresenter mPresenter, mCoverPresenter;
    private int progressColorsIds[] = {R.color.progres_green,
            R.color.progress_red,
            R.color.progress_blue,
            R.color.progress_gray,
            R.color.progress_yellow,
            R.color.progress_yellow};
    AMap aMap = null;
    private int mId;

    private String mType = "";
    private RichEditorEntity entity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mType = intent.getStringExtra(Constants.IntentParams.FROM_TYPE);
        super.setContentView(R.layout.activity_riding_report);
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。

        file = ImagePicker
                .createFile(
                        new File(
                                Environment
                                        .getExternalStorageDirectory()
                                        .getAbsolutePath()
                                        + "/kcwc/ridebook_share/"),
                        "rideshare",
                        ".png");

        mWbHandler = new WbShareHandler(this);
        mWbHandler.registerApp();
    }

    private WbShareHandler mWbHandler;
    private File file;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("驾途报告");
    }

    private void initMap() {
        //初始化地图控制器对象

        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setScaleControlsEnabled(false);

    }

    private void drawMarkPoint(LatLng latLng, String title, int drawableId) {

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title(title);
        //.snippet(title + latLng.latitude + "," + latLng.longitude);

        markerOption.icon(updateMarkerState(drawableId, title));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        aMap.addMarker(markerOption);
    }

    private View markerPosView;
    private TextView markerPosName;
    private TextView mMarkerImg;

    private BitmapDescriptor updateMarkerState(int drawableId, String name) {
        markerPosView = LayoutInflater.from(mContext).inflate(R.layout.marker_pos_layout, null,
                false);
        markerPosName = (TextView) markerPosView.findViewById(R.id.name);

        markerPosName.setText(name);
        RelativeLayout layout = (RelativeLayout) markerPosView.findViewById(R.id.currentPosLayout);
        int currentPosDrawableId = R.drawable.amap_marker_away;
        if ("起".equals(name)) {
            currentPosDrawableId = R.drawable.amap_marker_start;
        } else if ("终".equals(name)) {

            currentPosDrawableId = R.drawable.amap_marker_end;
        } else {
            currentPosDrawableId = R.drawable.amap_marker_away;
        }
        layout.setBackgroundResource(currentPosDrawableId);
        return BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(mContext, markerPosView));

    }

    //view 转bitmap
    public static Bitmap convertViewToBitmap(Context context, View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, BitmapUtils.dp2px(context, 21), BitmapUtils.dp2px(context, 29));
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    //根据中心点和自定义内容获取缩放bounds 
    private LatLngBounds getLatLngBounds(List<RideReportDetailModel.RideNodeKeyBean> beans) {

        LatLngBounds.Builder b = LatLngBounds.builder();
        int size = beans.size();
        if (size == 0) {
            return null;
        }
        int index = size - 1;
        RideReportDetailModel.RideNodeKeyBean nodeKeyBean = beans.get(index);
        LatLng centerLatLng = new LatLng(Double.parseDouble(nodeKeyBean.lat),
                Double.parseDouble(nodeKeyBean.lng));

        for (int i = 0; i < size; i++) {
            RideReportDetailModel.RideNodeKeyBean rideData = beans.get(i);
            LatLng p = new LatLng(Double.parseDouble(rideData.lat),
                    Double.parseDouble(rideData.lng));
            LatLng p1 = new LatLng((centerLatLng.latitude * 2) - p.latitude,
                    (centerLatLng.longitude * 2) - p.longitude);
            b.include(p);
            b.include(p1);

        }
        return b.build();
    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds2(List<LatLng> latLngs) {

        LatLngBounds.Builder b = LatLngBounds.builder();

        for (LatLng latLng : latLngs) {

            b.include(latLng);

        }
        return b.build();
    }

    private void showMapData(RideReportDetailModel rideReportDetailModel) {

        //画途经点

        List<RideReportDetailModel.RideNodeKeyBean> keyBeans = rideReportDetailModel.rideNodeKey;

        int index = 0;
        int drawableId = R.drawable.map_point_other;
        int size = keyBeans.size();
        for (RideReportDetailModel.RideNodeKeyBean keyBean : keyBeans) {

            String title = "";
            if (index == 0) {
                title = "起";
                drawableId = R.drawable.map_point_start;
            } else if (index == size - 1) {
                title = "终";
                drawableId = R.drawable.map_point_end;
            } else {
                title = "" + index;
                drawableId = R.drawable.map_point_other;
            }
            LatLng latLng = new LatLng(Double.parseDouble(keyBean.lat),
                    Double.parseDouble(keyBean.lng));
            drawMarkPoint(latLng, title, drawableId);
            index++;
        }

        //画轨迹
        List<RideReportDetailModel.RideNodeListBean> lists = rideReportDetailModel.rideNodeList;
        List<LatLng> latLngs = new ArrayList<LatLng>();
        for (RideReportDetailModel.RideNodeListBean bean : lists) {

            latLngs.add(new LatLng(Double.parseDouble(bean.lat), Double.parseDouble(bean.lng)));
        }

        Polyline polyline = aMap.addPolyline(new PolylineOptions().addAll(latLngs).geodesic(true)
                .color(mRes.getColor(R.color.style_bg1)));

        LatLngBounds bounds = getLatLngBounds2(latLngs);
        if (bounds != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }

    }

    private RelativeLayout mRoot, mRideLayout;

    @Override
    protected void setUpViews() {

        initView();
        initMap();
        initGridData();
        mPresenter = new RideDataPresenter();
        mPresenter.attachView(this);
        mCoverPresenter = new RideDataPresenter();
        mCoverPresenter.attachView(mCoverView);
        mUploadPresenter = new FileUploadPresenter();
        mUploadPresenter.attachView(fileUploadView);
        mCoverUploadPresenter = new FileUploadPresenter();
        mCoverUploadPresenter.attachView(fileCoverUploadView);
        int rideId = mId;
        String token = IOUtils.getToken(mContext);

        Intent intent = getIntent();
        //rideId=intent.getIntExtra(Constants.IntentParams.ID,-1);
        mPresenter.loadRideReportDatas(rideId + "", mType, token);
    }

    private RideDataView mCoverView = new RideDataView() {
        @Override
        public void showDatas(Object object) {

            Logger.d("上传封面成功!" + object);
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


    private FileUploadView<DataItem> fileCoverUploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem item) {

            String path = item.resp.data.path;

            mCoverPresenter.createCover(requestParams(path));
            Logger.d("报告封面路径:" + path);

        }


        @Override
        public void setLoadingIndicator(boolean active) {


        }

        @Override
        public void showLoadingTasksError() {

            dismissLoadingDialog2();
        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    private Map<String, String> requestParams(String path) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("ride_id", mId + "");
        params.put("cover", path);
        params.put("token", IOUtils.getToken(mContext));
        return params;
    }

    private void initView() {
        mRoot = (RelativeLayout) findViewById(R.id.root);
        mRideLayout = (RelativeLayout) findViewById(R.id.rideLayout);
        mMapView = (MapView) findViewById(R.id.map);
        avatar = (SimpleDraweeView) findViewById(R.id.avatar);
        name = (TextView) findViewById(R.id.name);
        carName = (TextView) findViewById(R.id.carName);
        address = (TextView) findViewById(R.id.address);
        time = (TextView) findViewById(R.id.time);
        baseInfoLayout = (RelativeLayout) findViewById(R.id.baseInfoLayout);
        rideMileTv = (TextView) findViewById(R.id.rideMileTv);
        rideMileTv.setTypeface(mGlobalContext.getTypeface());
        totalTime = (TextView) findViewById(R.id.totalTime);
        totalTime.setTypeface(mGlobalContext.getTypeface());
        rideTime = (TextView) findViewById(R.id.rideTime);
        rideTime.setTypeface(mGlobalContext.getTypeface());
        rideDataInfoLayout = (RelativeLayout) findViewById(R.id.rideDataInfoLayout);
        mGrid = (GridView) findViewById(R.id.gridView);
        editRideTv = (TextView) findViewById(R.id.editRideBtn);
        editRideTv.setOnClickListener(this);
        shareTv = (TextView) findViewById(R.id.shareTv);
        shareTv.setOnClickListener(this);
        relayOption = (RelativeLayout) findViewById(R.id.relay_option);
        linearlayoutMain = (RelativeLayout) findViewById(R.id.linearlayout_main);
        shareWindow = new OpenRideDataShareWindow(
                mContext);
        shareWindow
                .setOnItemClickListener(
                        mRideShareItemListener);
    }

    private OpenRideDataShareWindow shareWindow;

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.editRideBtn:

                Intent toEditBook = new Intent(getContext(), EditTripbookActivity.class);
                toEditBook.putExtra(Constants.IntentParams.ID, mId);
                startActivity(toEditBook);

                break;

            case R.id.shareTv:

                if (mUrl == null) {
                    showLoadingDialog("正在生成分享截图，请稍等！");
                    aMap.getMapScreenShot(aMapScreenShotListener);
                } else {
                    hadGeneratorShot();
                }

                // CommonUtils.showToast(mContext, "亲，此功能正在努力完善中，敬请期待!");

                break;
        }

    }


    /**
     * @param isShareFriend true 分享到朋友，false分享到朋友圈
     */
    private void share2Wx(boolean isShareFriend) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 96, 96, true);//缩略图大小
        bitmap.recycle();
        msg.thumbData = BitmapUtils.bmpToByteArray(thumbBmp, true);  // 设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = OpenShareUtil.buildTransaction("img");
        req.message = msg;
        req.scene = isShareFriend ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        mGlobalContext.getMsgApi().sendReq(req);
    }

    private AdapterView.OnItemClickListener mRideShareItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (position) {
                case 0:

                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.RIDE_ADDRESS, mEndAddress + "");
                    CommonUtils.startNewActivity(mContext, args, PostingActivity.class);
                    break;

                case 1:
                    share2Wx(true);
                    break;

                case 2:
                    share2Wx(false);
                    break;
                case 3:
                    shareWeiBo();
                    break;
                case 4:
                    OpenShareUtil.shareToQQ2(mGlobalContext.getTencent(), RideReportActivity.this, mBaseUIListener, "看车玩车", mUrl, "分享您的快乐!", mUrl);
                    break;
                case 5:

                    ArrayList<String> urls = new ArrayList<String>();
                    urls.add(mUrl);
                    OpenShareUtil.shareToQzone2(mGlobalContext.getTencent(), RideReportActivity.this, mBaseUIListener, urls, "看车玩车", "分享您的快乐!", mUrl);

                    break;
            }
            shareWindow.dismiss();

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //加上这一句才能回调
        Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUIListener);
    }

    private void initGridData() {

        String[] arrays = mRes.getStringArray(R.array.ride_items_titles);

        int index = 0;
        for (String s : arrays) {
            RideDataItem item = new RideDataItem();
            item.title = s;
            item.progressColorId = progressColorsIds[index];
            mItems.add(item);
            switch (index) {
                case 0:
                case 1:
                    item.max = 300;
                    break;
                case 2:
                    item.max = 8000;
                    break;
                case 3:
                    item.max = 70;
                    break;
                case 4:
                    item.max = 100;
                    break;

            }
            index++;
        }

        mAdapter = new WrapAdapter<RideDataItem>(mContext, R.layout.report_ride_item, mItems) {

            protected TextView title;
            protected TextView content;
            protected VerticalProgressBar progressBar;

            @Override
            public void convert(ViewHolder helper, RideDataItem item) {

                progressBar = helper.getView(R.id.progressBar);
                content = helper.getView(R.id.content);
                content.setTypeface(mGlobalContext.getTypeface());
                content.setText(item.content);
                title = helper.getView(R.id.title);
                title.setText(item.title);
                progressBar.setMax(item.max);
                progressBar.setProgress(item.progress);
                setProgressDrawable(progressBar, item.progressColorId);
            }
        };

        mGrid.setAdapter(mAdapter);
    }

    //private LatLngBounds.Builder latlngBuilder = new LatLngBounds.Builder();

    @Override
    public void showDatas(RideReportDetailModel rideReportDetailModel) {

        address.setText(rideReportDetailModel.ride.startAdds);
        mEndAddress = rideReportDetailModel.ride.endAdds;
        showUserInfo(rideReportDetailModel.ride);
        showRideInfo(rideReportDetailModel.ride);

        updateGirdData(rideReportDetailModel.ride);
        showMapData(rideReportDetailModel);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2 * 1000);
                    mHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            createMapShot();

        }
    };

    private void createMapShot() {

        aMap.getMapScreenShot(aMapCoverScreenShotListener);
    }

    private String mEndAddress;

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        CommonUtils.showToast(mContext, "网络异常，请稍候重试!");
        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private void showUserInfo(RideReportDetailModel.RideBean rideBean) {
        avatar.setImageURI(Uri.parse(URLUtil.builderImgUrl(rideBean.avatar, 144, 144)));
        name.setText(rideBean.nickname + "");
        time.setText(rideBean.startTime + "-" + rideBean.endTime);
    }

    private void showRideInfo(RideReportDetailModel.RideBean rideBean) {

        rideMileTv.setText(rideBean.mileage + "");
        rideTime.setText(rideBean.actualTime);
        totalTime.setText(rideBean.allTime);
    }

    /**
     * 更新GridView数据
     *
     * @param rideBean
     */
    private void updateGirdData(RideReportDetailModel.RideBean rideBean) {

        updateItemData(0, (int) Double.parseDouble(rideBean.speedMax), rideBean.speedMax);
        updateItemData(1, (int) Double.parseDouble(rideBean.speedAverage), rideBean.speedAverage);
        updateItemData(2, rideBean.altitudeMax, rideBean.altitudeMax + "");
        // updateItemData(3, rideBean.bendingAngleMax, rideBean.bendingAngleMax + "");
        updateItemData(3, rideBean.hundredTime, rideBean.hundredTime + "");
        updateItemData(4, rideBean.fourHundredTime, rideBean.fourHundredTime + "");
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 动态设置进度条状态值
     *
     * @param progressBar
     * @param colorId
     */
    private void setProgressDrawable(ProgressBar progressBar, int colorId) {

        LayerDrawable layered = (LayerDrawable) progressBar.getProgressDrawable();
        Drawable backgroudDrawable = layered.getDrawable(0);
        backgroudDrawable.setColorFilter(mRes.getColor(R.color.black), PorterDuff.Mode.SRC_IN);

        Drawable progressDrawable = layered.getDrawable(2);
        progressDrawable.setColorFilter(mRes.getColor(colorId), PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(layered);
    }

    private void updateItemData(int position, int progress, String content) {

        mItems.get(position).progress = progress;
        mItems.get(position).content = content;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        IOUtils.deleteFile(mContext, Constants.KeyParams.RICH_E_DATA);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private AMap.OnMapScreenShotListener aMapScreenShotListener = new AMap.OnMapScreenShotListener() {
        @Override
        public void onMapScreenShot(final Bitmap bitmap) {

            ThreadPoolProxy
                    .getInstance()
                    .executeTask(
                            new Runnable() {
                                @Override
                                public void run() {

                                    if (Environment
                                            .getExternalStorageState()
                                            .equals(
                                                    Environment.MEDIA_MOUNTED)) {

                                        file = ImagePicker
                                                .createFile(
                                                        new File(
                                                                Environment
                                                                        .getExternalStorageDirectory()
                                                                        .getAbsolutePath()
                                                                        + "/kcwc/ridebook_share/"),
                                                        "rideshare",
                                                        ".png");

                                        Bitmap bitmap1 = ScreenShotHelper
                                                .getMapAndViewScreenShot(
                                                        bitmap,
                                                        mRoot,
                                                        mMapView,
                                                        baseInfoLayout,
                                                        mRideLayout);

                                        BitmapUtils
                                                .saveBitmap(
                                                        bitmap1,
                                                        file.getAbsolutePath());

                                        uploadImage(file);
                                    }

                                }
                            });
        }

        @Override
        public void onMapScreenShot(Bitmap bitmap,
                                    int i) {

        }
    };


    private AMap.OnMapScreenShotListener aMapCoverScreenShotListener = new AMap.OnMapScreenShotListener() {
        @Override
        public void onMapScreenShot(final Bitmap bitmap) {

            ThreadPoolProxy
                    .getInstance()
                    .executeTask(
                            new Runnable() {
                                @Override
                                public void run() {

                                    if (Environment
                                            .getExternalStorageState()
                                            .equals(
                                                    Environment.MEDIA_MOUNTED)) {

                                        File file = ImagePicker
                                                .createFile(
                                                        new File(
                                                                Environment
                                                                        .getExternalStorageDirectory()
                                                                        .getAbsolutePath()
                                                                        + "/kcwc/ridebook_createcover/"),
                                                        "ridecover",
                                                        ".png");

                                        Bitmap bitmap1 = ScreenShotHelper
                                                .getMapAndViewScreenShot(
                                                        bitmap,
                                                        mMapView, mMapView.getWidth()
                                                        , mMapView.getHeight());

                                        BitmapUtils
                                                .saveBitmap(
                                                        bitmap1,
                                                        file.getAbsolutePath());

                                        uploadCoverImage(file);
                                    }

                                }
                            });
        }

        @Override
        public void onMapScreenShot(Bitmap bitmap,
                                    int i) {

        }
    };


    /**
     * 新浪微博分享
     */
    private void shareWeiBo() {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        imageObject.setImageObject(bitmap);
        weiboMessage.mediaObject = imageObject;


        mWbHandler.shareMessage(weiboMessage, false);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mWbHandler != null) {
            mWbHandler.doResultIntent(intent, new SinaWBCallback(mContext));
        }
    }

    private String mUrl = null;
    private FileUploadView<DataItem> fileUploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem item) {

            dismissLoadingDialog2();
            int code = item.code;
            if (code == Constants.NetworkStatusCode.SUCCESS) {
                mUrl = URLUtil.builderImgUrl(item.content);
                List<SEditorData> editList = new ArrayList<SEditorData>();
                SEditorData data = new SEditorData();
                data.setImageUrl(mUrl);
                data.setImagePath(file.getPath());
                data.setInputStr("");
                editList.add(data);
                entity = new RichEditorEntity();
                entity.mEditorDatas = editList;
                entity.title = "游记分享:" + mEndAddress;
                IOUtils.writeObject(mContext, entity, Constants.KeyParams.RICH_E_DATA);

                shareWindow.show(
                        RideReportActivity.this);


            } else {

                CommonUtils.showToast(mContext, "生成分享截图失败，请稍候重试!");
            }

        }


        @Override
        public void setLoadingIndicator(boolean active) {


        }

        @Override
        public void showLoadingTasksError() {

            dismissLoadingDialog2();
        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };


    private void uploadImage(File file) {

        mUploadPresenter.uploadAvatarFile(RequestBody.create(MediaType.parse("image/png"), file),
                RequestBody.create(MediaType.parse("text/plain"), "avatar"),
                RequestBody.create(MediaType.parse("text/plain"), ""),
                RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())));
    }

    private void uploadCoverImage(File file) {

        mCoverUploadPresenter.uploadAvatarFile(RequestBody.create(MediaType.parse("image/png"), file),
                RequestBody.create(MediaType.parse("text/plain"), "avatar"),
                RequestBody.create(MediaType.parse("text/plain"), ""),
                RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())));
    }

    /**
     * 有生成截图，从内存中读取并写入手机存储
     */
    private void hadGeneratorShot() {

        IOUtils.writeObject(mContext, entity, Constants.KeyParams.RICH_E_DATA);
        shareWindow.show(
                RideReportActivity.this);

    }


}
