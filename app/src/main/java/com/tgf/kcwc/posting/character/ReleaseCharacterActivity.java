package com.tgf.kcwc.posting.character;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.amap.MarkerPointActivity;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.model.TopicTagDataModel;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.TopicDataPresenter;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.TopicDataView;
import com.tgf.kcwc.posting.TopicListActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.dragview.MyAdapter;
import com.tgf.kcwc.view.dragview.OnRecyclerItemClickListener;
import com.tgf.kcwc.view.dragview.SwipeRecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

import static com.tgf.kcwc.R.id.edtext;

/**
 * Created by Administrator on 2017/9/22.
 */

public class ReleaseCharacterActivity extends BaseActivity implements TopicDataView<List<String>> {
    private FileUploadPresenter mFileUploadPresenter;                           //上传图片
    private TopicDataPresenter mTopicDataPresenter;
    private static final int IMAGE_PICKER = 1001;                   //图片
    private static final int LOCATION_REQ_CODE = 1111;
    private SwipeRecyclerView mRecyclerView;
    private List<DataItem> datas = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private MyAdapter myAdapter;
    private EditText mEdtext;
    private TextView mEdtextNumber;
    private int SelectNumber = 9;
    private int UploadingNumber = 0;
    private List<Topic> mTags = new ArrayList<Topic>(); //帖子标签集合，最多只存取5个
    private ImageView label; //标签
    private static final int TAG_MAX = 5;
    private FlowLayout mTagLayout;
    private RelativeLayout mTagsLayout;
    private ImageView selectimage;

    private LinearLayout mAddressLayout;
    private TextView mAddressNameTv;
    private TextView mPositionName;

    private String mLocationAddress = "";  //地址
    private String latitude = "", longitude = ""; //经纬度
    private PoiItem mPoiItem;

    protected KPlayCarApp mGlobalContext;
    private TextView mCancel;//取消
    private TextView mRelease;//发布

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTitleBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_character);
        mGlobalContext = (KPlayCarApp) getApplication();
        initData();
        initView();
        initProgressDialog();
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(uploadView);
        mTopicDataPresenter = new TopicDataPresenter();
        mTopicDataPresenter.attachView(this);
    }

    private void initView() {

        mRecyclerView = (SwipeRecyclerView) findViewById(R.id.recyclerView);
        mEdtext = (EditText) findViewById(edtext);
        mEdtextNumber = (TextView) findViewById(R.id.edtextnumber);
        label = (ImageView) findViewById(R.id.label);
        mTagLayout = (FlowLayout) findViewById(R.id.tagLists);
        selectimage = (ImageView) findViewById(R.id.selectimage);
        mCancel = (TextView) findViewById(R.id.cancel);
        mRelease = (TextView) findViewById(R.id.title_function_btn);
        mTagsLayout = (RelativeLayout) findViewById(R.id.tagLayout);

        mAddressLayout = (LinearLayout) findViewById(R.id.addressLayout);
        mAddressNameTv = (TextView) findViewById(R.id.addressName);
        mPositionName = (TextView) findViewById(R.id.name);

        mTagLayout.setVerticalSpacing(BitmapUtils.dp2px(mContext, 5));
        mTagLayout.setHorizontalSpacing(BitmapUtils.dp2px(mContext, 5));

        label.setOnClickListener(this);
        selectimage.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mRelease.setOnClickListener(this);

        initAddressInfo();
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        myAdapter = new MyAdapter(datas, mContext);
        myAdapter.setDeleteClickListener(new MyAdapter.DeleteClickListener() {
            @Override
            public void delete(int num) {
                datas.remove(num);
                myAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int layoutPosition = vh.getLayoutPosition();

                if (layoutPosition == datas.size()) {
                    SelectImage();
                } else {
                    //Toast.makeText(mContext, "点击了第" + layoutPosition, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //判断是不是最后一个加号
                if (vh.getLayoutPosition() != datas.size()) {
                    mItemTouchHelper.startDrag(vh);

                    //获取系统震动服务
                    // Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                    //vib.vibrate(70);

                }
            }
        });

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                //判断是否拖拽到最后一个加号
                if (toPosition != datas.size()) {
                    if (fromPosition < toPosition) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(datas, i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(datas, i, i - 1);
                        }
                    }
                    myAdapter.notifyItemMoved(fromPosition, toPosition);
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                myAdapter.notifyItemRemoved(position);
//                datas.remove(position);
            }

            /**
             * 重写拖拽可用
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            /**
             * 长按选中Item的时候开始调用
             *
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
            }
        });

        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setRightClickListener(new SwipeRecyclerView.OnRightClickListener() {
            @Override
            public void onRightClick(int position, String id) {
                datas.remove(position);
//                myAdapter.notifyItemRemoved(position);
                myAdapter.notifyDataSetChanged();
                Toast.makeText(mContext, " position = " + position, Toast.LENGTH_SHORT).show();
            }
        });

        // 搜索框的文本变化实时监听
        mEdtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    mEdtextNumber.setText("0/140");
                } else {
                    mEdtextNumber.setText(s.toString().trim().length() + "/140");
                }
            }
        });

    }

    private void initAddressInfo() {
        mLocationAddress = mGlobalContext.getAddressInfo() + "";
        latitude = mGlobalContext.getLattitude();
        longitude = mGlobalContext.getLongitude();
        mPositionName.setText(mLocationAddress);
        mPositionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startResultNewActivity(ReleaseCharacterActivity.this, null,
                        MarkerPointActivity.class, LOCATION_REQ_CODE);

            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.img);

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
                mAddressLayout.setVisibility(View.VISIBLE);
                mAddressNameTv.setVisibility(View.GONE);
            }
        });

    }

    private void initData() {

    }


    public void SelectImage() {
        ImagePicker.getInstance().setMultiMode(true);
        ImagePicker.getInstance().setSelectLimit(SelectNumber - datas.size()); //选中数量限制
        if (SelectNumber - datas.size() != 0) {
            Intent intent = new Intent(ReleaseCharacterActivity.this, ImageGridActivity.class);
            startActivityForResult(intent, IMAGE_PICKER);
        } else {
            CommonUtils.showToast(mContext, "您已经选择了9张图片");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                UploadingNumber = images.size();
                for (ImageItem item : images) {
                    uploadImage(item);
                }
            }

        }

        if (Constants.InteractionCode.REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            boolean isExist = true;
            Topic topic = data.getParcelableExtra(Constants.IntentParams.DATA);
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
                mPositionName.setText(localAddress);
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
                            RequestBody.create(MediaType.parse("text/plain"), "thread"),
                            RequestBody.create(MediaType.parse("text/plain"), ""),
                            RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())),
                            item);
                }

            }
        });
    }


    /**
     * 添加标签
     *
     * @param datas
     */
    private void addTagItems(List<Topic> datas) {
        mTagLayout.removeAllViews();
        mTagsLayout.setVisibility(View.VISIBLE);
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
                    if (mTags.size() <= 0) {
                        mTagsLayout.setVisibility(View.GONE);
                    } else {
                        mTagsLayout.setVisibility(View.VISIBLE);
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
    public void showData(List<String> strings) {
        CommonUtils.showToast(mContext, "发布成功");
        finish();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private FileUploadView<DataItem> uploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem dataItem) {
            if (dataItem.code == 0) {
                datas.add(dataItem);
                myAdapter.notifyDataSetChanged();
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


    private ProgressDialog mProgressDialog = null;

    private void initProgressDialog() {

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在上传，请稍候...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

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
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.label:
                KPlayCarApp.putValue(Constants.KeyParams.TAGS_DATA, mTags);
                CommonUtils.startResultNewActivity(this, null, TopicListActivity.class,
                        Constants.InteractionCode.REQUEST_CODE);
                break;
            case R.id.selectimage:
                SelectImage();
                break;
            case R.id.cancel:
                if (datas.size() == 0) {
                    if (TextUtil.isEmpty(mEdtext.getText().toString().trim())) {
                        if (mTags.size() == 0) {
                            finish();
                        }else {
                            showQR();
                        }
                    }else {
                        showQR();
                    }
                }else {
                    showQR();
                }
                break;
            case R.id.title_function_btn:
                Map<String, String> stringStringMap = builderParams();
                if (stringStringMap == null) {
                    CommonUtils.showToast(mContext, "请至少选择一张图片或输入一段文字");
                } else {
                    mTopicDataPresenter.releasemood(stringStringMap);
                }
                break;
        }
    }


    private Map<String, String> builderParams() {


        if (TextUtil.isEmpty(mEdtext.getText().toString().trim()) && datas.size() == 0) {
            return null;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("content", mEdtext.getText().toString().trim());
        StringBuffer stringBuffer = new StringBuffer();
        for (Topic t : mTags) {
            stringBuffer.append(t.id).append(",");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); //调用 字符串的deleteCharAt() 方法,删除最后一个多余的逗号
        }
        String mTagString = stringBuffer.toString();
        params.put("topic", mTagString);
        stringBuffer = new StringBuffer();
        for (DataItem data : datas) {
            stringBuffer.append(data.resp.data.path).append(",");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); //调用 字符串的deleteCharAt() 方法,删除最后一个多余的逗号
        }
        String mImageString = stringBuffer.toString();
        params.put("image", mImageString);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("local_address", mLocationAddress);
        return params;
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
            if (datas.size() == 0) {
                if (TextUtil.isEmpty(mEdtext.getText().toString().trim())) {
                    if (mTags.size() == 0) {
                        finish();
                    }else {
                        showQR();
                    }

                }else {
                    showQR();
                }
            }else {
                showQR();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void showQR() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.driving_dialog, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        ImageView img = (ImageView) v.findViewById(R.id.img);
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText("是否放弃发布动态？");
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

}
