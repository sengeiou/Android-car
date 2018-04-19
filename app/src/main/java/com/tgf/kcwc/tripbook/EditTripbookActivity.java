package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.PoiItem;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.tgf.kcwc.R;
import com.tgf.kcwc.amap.MarkerPointActivity;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.entity.NodeEntity;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.mvp.model.EditbookModel;
import com.tgf.kcwc.mvp.model.RideDataNodeItem;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.model.TopicTagDataModel;
import com.tgf.kcwc.mvp.presenter.EidtBookPresenter;
import com.tgf.kcwc.mvp.presenter.TopicDataPresenter;
import com.tgf.kcwc.mvp.view.EditTripBookView;
import com.tgf.kcwc.mvp.view.TopicDataView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.posting.TopicListActivity;
import com.tgf.kcwc.service.ErgodicCityService;
import com.tgf.kcwc.service.LocationService;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.LocationUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/5/2 0002
 * E-mail:hekescott@qq.com
 */

public class EditTripbookActivity extends BaseActivity implements EditTripBookView {

    private static final int TAG_MAX = 5;
    private static final int REQUEST_CODE_SELECTLINE = 102;
    private static final int REQUEST_CODE_NODEDESC = 100;
    private FlowLayout mTagLayout;
    private List<Topic> mTags = new ArrayList<Topic>();            //帖子标签集合，最多只存取5个
    private LinearLayout noBookRootLv;
    private int rideId = -1;
    private EidtBookPresenter eidtBookPresenter;
    private EditbookModel mEditbookModel;
    private EditText titleEd;
    private EditText descEd;
    private String token = "";
    private SimpleDraweeView coverIv;
    private NestFullListView pointLv;
    private RelativeLayout mTagRootLayout;
    private String mLocationAddress;
    private TextView mPositionName;
    private TextView selectLineLv;
    private NodeEntity mNodeEntity;
    private KPlayCarApp kPlayCarApp;
    private Gson toGosn;
    ArrayList<NodeEntity> postLineList = new ArrayList<>();
    private String mStatus = "1";
    private View btnSave;
    private TextView submitText;
    private TextView editripContinueTv;
    private TextView tripLineStatusTv;
    private Intent toDringIntent;
    private NestFullListViewAdapter mNodeAdatpter;
    private TextView mAddressNameTv;
    private LinearLayout mAddressLayout;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        View btnCancel = findViewById(R.id.titlebar_cancel);
        btnSave = findViewById(R.id.titlebar_save);
        backEvent(btnCancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatus = "2";
                eidtBookPresenter.postEditbookInfo(builderParams());
            }
        });
    }

    @Override
    protected void setUpViews() {
        initLocation();
        token = IOUtils.getToken(getContext());
        kPlayCarApp = (KPlayCarApp) getApplication();

        editripContinueTv = (TextView) findViewById(R.id.edittripbook_continue);
        editripContinueTv.setOnClickListener(this);
        submitText = (TextView) findViewById(R.id.edittripbook_submit);
        submitText.setOnClickListener(this);
        mTagLayout = (FlowLayout) findViewById(R.id.tagLists);
        mTagRootLayout = (RelativeLayout) findViewById(R.id.tagLayout);
        mTagLayout.setVerticalSpacing(BitmapUtils.dp2px(mContext, 5));
        mTagLayout.setHorizontalSpacing(BitmapUtils.dp2px(mContext, 5));
        findViewById(R.id.editbook_addtagtv).setOnClickListener(this);
        noBookRootLv = (LinearLayout) findViewById(R.id.eidtbook_nobookroot);
        titleEd = (EditText) findViewById(R.id.eidtbook_titleed);
        descEd = (EditText) findViewById(R.id.eidtbook_desced);
        coverIv = (SimpleDraweeView) findViewById(R.id.edittrip_coveriv);
        pointLv = (NestFullListView) findViewById(R.id.eidtbook_pointlv);
        selectLineLv = (TextView) findViewById(R.id.edittripbook_seleclinerv);
        tripLineStatusTv = (TextView) findViewById(R.id.tripbook_linestatustv);

        mPositionName = (TextView) findViewById(R.id.name);
        mLocationAddress = mGlobalContext.getAddressInfo() + "";
        mPositionName.setText(mLocationAddress);
        mAddressNameTv = (TextView) findViewById(R.id.addressName);
        mAddressLayout = (LinearLayout) findViewById(R.id.addressLayout);
        selectLineLv.setOnClickListener(this);
        pointLv.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
            @Override
            public void onItemClick(NestFullListView parent, View view, int position) {
                Intent toIntent = new Intent(getContext(), NodeDescActivity.class);
                NodeEntity nodeEntity = postLineList.get(position);
                toIntent.putExtra(Constants.IntentParams.DATA, nodeEntity);
                toIntent.putExtra(Constants.IntentParams.TITLE, mEditbookModel.lineList.get(position).address);
                String LNG = "lng";              //经度
                String LAT = "lat";
                KPlayCarApp.putValue(Constants.IntentParams.LNG,mEditbookModel.lineList.get(position).lng);
                KPlayCarApp.putValue(Constants.IntentParams.LAT,mEditbookModel.lineList.get(position).lat);
                startActivityForResult(toIntent, REQUEST_CODE_NODEDESC);
            }
        });
        eidtBookPresenter = new EidtBookPresenter();
        eidtBookPresenter.attachView(this);
        mCreateTagPresenter = new TopicDataPresenter();
        mCreateTagPresenter.attachView(mCreateTagView);
        rideId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        if (rideId != -1) {
            eidtBookPresenter.getEditbookInfo(token, rideId);
        }
        setSpan();
        initAddressInfo();
    }


    private void setSpan() {
        TextView clickSpan = (TextView) findViewById(R.id.edittrip_span);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_pencil);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        String text = "点[smile]编辑结点描述";
        SpannableString spannable = new SpannableString(text);
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannable.setSpan(span, 1, 1 + "[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        clickSpan.setText(spannable);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittripbook);
        toGosn = new Gson();
        toDringIntent = new Intent(getContext(), DrivingHomeActivity.class);
        toDringIntent.putExtra(Constants.IntentParams.DATA, Constants.IntentParams.RIDE_BOOK_MODULE);
    }
    private boolean isSystemTag = false;
    private TopicDataPresenter mCreateTagPresenter;
    @Override
    protected void onResume() {
        super.onResume();
        TopicTagDataModel topicTagDataModel = KPlayCarApp.getValue(Constants.KeyParams.SYS_TAG_MODEL_VALUE);

        if (topicTagDataModel != null) {

            isSystemTag = true;
            mCreateTagPresenter.createTag(topicTagDataModel.id + "", topicTagDataModel.fromType, IOUtils.getToken(mContext));
            KPlayCarApp.removeValue(Constants.KeyParams.SYS_TAG_MODEL_VALUE);
        }
        int size = mTags.size();
        if (size > 0) {
            mTagRootLayout.setVisibility(View.VISIBLE);
        } else {
            mTagRootLayout.setVisibility(View.GONE);
        }

    }
    private TopicDataView<Topic> mCreateTagView = new TopicDataView<Topic>() {
        @Override
        public void showData(Topic topic) {

            if (isSystemTag) {
                mTagRootLayout
                        .setVisibility(View.VISIBLE);
                settingTag(topic);
                isSystemTag = false;

            } else {
                mTags.clear();
                mTags.add(topic);
                mTagRootLayout
                        .setVisibility(View.VISIBLE);
                addTagItems(mTags);
            }

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

    private void settingTag(Topic topic) {

        int size = mTags.size();
        if (size >= TAG_MAX) {
            CommonUtils.showToast(mContext, "最多只能添加5个标签");
        } else {

            addTag(topic);
        }
        addTagItems(mTags);
    }
    /**
     * 添加标签，有去重
     *
     * @param topic
     */
    private void addTag(Topic topic) {

        for (Topic t : mTags) {
            if (t.id == topic.id) {
                return;
            }
        }
        mTags.add(topic);
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
                    int size = mTags.size();
                    if (size > 0) {
                        mTagRootLayout.setVisibility(View.VISIBLE);
                    } else {
                        mTagRootLayout.setVisibility(View.GONE);
                    }
                }
            });

        }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editbook_addtagtv:
                CommonUtils.startResultNewActivity(this, null, TopicListActivity.class,
                        Constants.InteractionCode.REQUEST_CODE);
                break;
            case R.id.edittripbook_seleclinerv:
            case R.id.edittripbook_continue:
                if (isContintue) {
                    toDringIntent.putExtra(Constants.IntentParams.ID, rideId);
                    toDringIntent.putParcelableArrayListExtra(Constants.IntentParams.DATA3, nodeListRuned);
                    toDringIntent.putExtra(Constants.IntentParams.DATA2,
                            Constants.IntentParams.CONTINUE_CONTINUELINE);
                    toDringIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(toDringIntent);
                    finish();
                    isContintue = false;
                } else {
                    CommonUtils.showToast(getContext(), "正在加载数据，稍后操作");
                }
//                Intent toSelecRoadLine = new Intent(this, SelectRoadLineActivity.class);
//                startActivityForResult(toSelecRoadLine, REQUEST_CODE_SELECTLINE);
                break;
            case R.id.edittripbook_submit:
                //TODO 提交
                String titleStr = titleEd.getText().toString();
                if (TextUtils.isEmpty(titleStr)) {
                    CommonUtils.showToast(getContext(), "请填写标题");
                    return;
                }
                if (titleStr.length() < 2 || titleStr.length() > 30) {
                    CommonUtils.showToast(getContext(), "路书标题2-30字");
                    return;
                }
                if (TextUtils.isEmpty(descEd.getText())) {
                    CommonUtils.showToast(getContext(), "请填写描述");
                    return;
                }
                mStatus = "1";
                eidtBookPresenter.postEditbookInfo(builderParams());
//todo                eidtBookPresenter.postEditbookInfo();
                break;
            default:
                break;
        }
    }

    private Map<String, Object> builderParams() {


        String topicIds = null;
        if (mTags != null && mTags.size() != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (Topic t : mTags) {
                stringBuffer.append(t.id).append(",");
            }
            topicIds = stringBuffer.toString();
            if (topicIds.length() > 0) {
                topicIds = topicIds.substring(0, topicIds.length() - 1);
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("adcode", kPlayCarApp.getAdcode());
        params.put("token", token);
        params.put("title", titleEd.getText().toString());
        params.put("content", descEd.getText().toString());
//        params.put("cover", "http://img.i.cacf.cn/content/1706/20/9543bbd799e3398aa7e222c30931c179.jpg!540270");
        params.put("cover", mEditbookModel.cover);
        params.put("end_adds", mEditbookModel.end_adds);
        params.put("lat", kPlayCarApp.getLattitude());
        params.put("lines", toGosn.toJson(postLineList));
        params.put("lng", kPlayCarApp.getLongitude());
        params.put("ride_id", rideId + "");
        params.put("start_adds", mEditbookModel.start_adds);
        params.put("lines_type", "json");
        params.put("address", mPositionName.getText() + "");
        params.put("status", mStatus);
        if (topicIds != null)
            params.put("tag", topicIds);
        return params;
    }

    public Bitmap getNodeIcon(int position, int nodeCount,boolean isGray) {
        View markerPosView = LayoutInflater.from(mContext).inflate(R.layout.marker_pos_tripmap,
                null, false);
        TextView markerPosName = (TextView) markerPosView.findViewById(R.id.name);
        if(isGray){
            RelativeLayout markerLayout = (RelativeLayout) markerPosView.findViewById(R.id.currentPosLayout);
            markerLayout.setBackgroundResource(R.drawable.icon_place);
        }
        if (position == 0) {
            markerPosName.setText("起");
        } else if (position == nodeCount - 1) {
            if (mEditbookModel.status == 1) {
                markerPosName.setText("终");
            } else {
                markerPosName.setText(position + "");
            }
        } else {
            markerPosName.setText(position + "");
        }
        return convertViewToBitmap(mContext, markerPosView);
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
    private PoiItem mPoiItem;
    private String latitude = "", longitude = "";
    private String mEndAddress;
    private void initAddressInfo() {
        mPositionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtils.startResultNewActivity(EditTripbookActivity.this, null,
                        MarkerPointActivity.class, LOCATION_REQ_CODE);

            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.locimg);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAddressLayout.setVisibility(View.GONE);
                mAddressNameTv.setVisibility(View.VISIBLE);
            }
        });
        mAddressNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startResultNewActivity(EditTripbookActivity.this, null,
                        MarkerPointActivity.class, LOCATION_REQ_CODE);
//                mAddressLayout.setVisibility(View.VISIBLE);
//                mAddressNameTv.setVisibility(View.GONE);
            }
        });

    }
    private static final int LOCATION_REQ_CODE = 1111;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.InteractionCode.REQUEST_CODE:
                    Topic topic = data.getParcelableExtra(Constants.IntentParams.DATA);
                    int size = mTags.size();
                    if (size >= TAG_MAX) {
                        CommonUtils.showToast(mContext, "最多只能添加5个标签");
                    } else {
                        for (Topic tmpTopic : mTags) {
                            if (tmpTopic.id == topic.id) {
                                CommonUtils.showToast(mContext, "该标签已经添加过");
                                return;
                            }
                        }
                        mTags.add(topic);
                    }
                    addTagItems(mTags);
                    break;
                case REQUEST_CODE_NODEDESC:
                    mNodeEntity = data.getParcelableExtra(Constants.IntentParams.DATA);
                    for (int i = 0; i < postLineList.size(); i++) {
                        if (postLineList.get(i).ride_node_id == mNodeEntity.ride_node_id) {
                            postLineList.set(i, mNodeEntity);
                        }
                        if (mEditbookModel.lineList.get(i).id== mNodeEntity.ride_node_id) {
                            mEditbookModel.lineList.get(i).nodeEntity=mNodeEntity;
                        }
                    }
                    pointLv.notifyDataSetChange(mEditbookModel.lineList);
                    break;
                case REQUEST_CODE_SELECTLINE:
                    int tmpBookId = data.getIntExtra(Constants.IntentParams.ID, -1);
                    if (tmpBookId != rideId) {
                        rideId = tmpBookId;
                        eidtBookPresenter.getEditbookInfo(token, rideId);
                    }
                    break;
                default:
                    break;
            }
        }

        if (LOCATION_REQ_CODE == requestCode && resultCode == RESULT_OK) {

            String localAddress = "";
            mPoiItem = data.getParcelableExtra(Constants.IntentParams.DATA);
            if (mPoiItem != null) {
                latitude = mPoiItem.getLatLonPoint().getLatitude() + "";
                longitude = mPoiItem.getLatLonPoint().getLongitude() + "";
                String adName = mPoiItem.getAdName();
                String address = "";
                if (!TextUtils.isEmpty(adName)) {
                    localAddress = adName + "|";
                }
                localAddress += mPoiItem.getTitle();
                mLocationAddress = localAddress;
                mAddressLayout.setVisibility(View.VISIBLE);
                mAddressNameTv.setVisibility(View.GONE);
                mPositionName.setText(localAddress);
            }
        }
    }

    @Override
    public void showEditTripbookView(EditbookModel editbookModel) {
        mEditbookModel = editbookModel;
        if (mEditbookModel == null) {
            noBookRootLv.setVisibility(View.VISIBLE);
            return;
        }
        if (!TextUtil.isEmpty(mEditbookModel.title)) {
            titleEd.setText(mEditbookModel.title);
            titleEd.setSelection(mEditbookModel.title.length());
        }
        if (!TextUtil.isEmpty(mEditbookModel.content)) {
            descEd.setText(mEditbookModel.content);
            descEd.setSelection(mEditbookModel.content.length());
        }
        if (mEditbookModel.status == 1) { //路线已结束
            tripLineStatusTv.setText(Html.fromHtml("驾途路线 <font color='#666666' size='12'>(已结束)</font>"));
            if (mEditbookModel.book_status == 1) {
                btnSave.setVisibility(View.GONE);
                submitText.setText("修改并发布");
            } else {
                btnSave.setVisibility(View.VISIBLE);
            }
        } else {//路线未结束
            btnSave.setVisibility(View.VISIBLE);
            editripContinueTv.setVisibility(View.VISIBLE);
            selectLineLv.setVisibility(View.VISIBLE);
            tripLineStatusTv.setText(Html.fromHtml("驾途路线 <font color='#666666' size='12'>(进行中)</font>"));
            eidtBookPresenter.getRoadLinelist(token);
        }
        String coverUrl = URLUtil.builderImgUrl(editbookModel.cover, 540, 304);
        coverIv.setImageURI(Uri.parse(coverUrl));
        for (int i = 0; i < editbookModel.lineList.size(); i++) {
            NodeEntity tmp2 = editbookModel.lineList.get(i).nodeEntity;
            tmp2.ride_node_id = editbookModel.lineList.get(i).id;
            postLineList.add(tmp2);
        }
        mNodeAdatpter = new NestFullListViewAdapter<EditbookModel.RoadLine>(
                R.layout.listitem_eidtbook_point, mEditbookModel.lineList) {
            @Override
            public void onBind(int pos, EditbookModel.RoadLine roadLine,
                               NestFullViewHolder holder) {
                TextView textView = holder.getView(R.id.point_desctv);
                textView.setText(roadLine.address);
                boolean isEdit = false;
                if (roadLine.nodeEntity.book_line_content_list!=null&&roadLine.nodeEntity.book_line_content_list.size()!=0){
                    isEdit = true;
                }else  if(roadLine.nodeEntity.desc!=null&&roadLine.nodeEntity.desc.size()!=0){
                    isEdit = true;
                }
                if (isEdit) {
                    textView.setTextColor(mRes.getColor(R.color.text_color12));
                    ImageView iconIv = holder.getView(R.id.point_iconiv);
                    iconIv.setImageBitmap(getNodeIcon(pos, mEditbookModel.lineList.size(),false));
                } else {
                    textView.setTextColor(mRes.getColor(R.color.text_color2));
                    ImageView iconIv = holder.getView(R.id.point_iconiv);
                    iconIv.setImageBitmap(getNodeIcon(pos, mEditbookModel.lineList.size(),true));
                }
            }
        };
        pointLv.setAdapter(mNodeAdatpter);
    }

    @Override
    public void showTagList(ArrayList<Topic> topiclist) {
        if (topiclist != null && topiclist.size() != 0) {
            mTags = topiclist;
            addTagItems(mTags);
            mTagRootLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showSuccess(String statusMessage) {
        CommonUtils.showToast(getContext(), "操作成功");
        if(mStatus.equals("1")){
            Intent intent = new Intent(LocationService.CLEAR_DATA_ACTION);
            sendBroadcast(intent);
        }
        finish();
        if (mEditbookModel.status == 1 || mStatus.equals("1")) {//路线已经结束
            Intent intent = new Intent(getContext(), MyTripbookActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Constants.IntentParams.DATA,true);
            startActivity(intent);
        }

    }

    private ArrayList<RideDataNodeItem> nodeListRuned;

    @Override
    public void showUnStop(ArrayList<RideDataNodeItem> nodeList) {
        isContintue = true;
        nodeListRuned = nodeList;
    }

    boolean isContintue;

    @Override
    public void showContinue(ArrayList<RideDataNodeItem> nodeList) {
        nodeListRuned = nodeList;
        isContintue = true;
    }

    @Override
    public void showDoFailed(String statusMessage) {
        CommonUtils.showToast(getContext(), "操作失败:" + statusMessage);
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

    private void initLocation() {
        AMapLocationClient locationClient = LocationUtil.getGaodeLocationClient(getContext());
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation bdLocation) {
                if (!TextUtils.isEmpty(bdLocation.getCity())) {
                    kPlayCarApp.latitude = bdLocation.getLatitude() + "";
                    kPlayCarApp.longitude = bdLocation.getLongitude() + "";
                    kPlayCarApp.locCityName = bdLocation.getCity();
                    kPlayCarApp.mAddressInfo = bdLocation.getPoiName();
                    kPlayCarApp.adcode = bdLocation.getAdCode();
                    //对比当前城市
                    Intent intent = new Intent(getBaseContext(), ErgodicCityService.class);
                    startService(intent);
                }

            }
        });
        locationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        KPlayCarApp.removeValue(Constants.IntentParams.LNG);
        KPlayCarApp.removeValue(Constants.IntentParams.LAT);
        super.onDestroy();
    }
}
