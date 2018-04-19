package com.tgf.kcwc.play.topic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.TopicDetailsBean;
import com.tgf.kcwc.mvp.model.TopicDetailsListBean;
import com.tgf.kcwc.mvp.presenter.CompileTopicDetailsPresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.view.CompileTopicDetailsView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by Administrator on 2017/5/15.
 */

public class CompileTopocDetailsActivity extends BaseActivity implements CompileTopicDetailsView {

    private static final int IMAGE_PICKER = 1001;                   //图片
    public static final int REQUEST_CODE_PREVIEW = 1002;                    //编辑导语
    private FileUploadPresenter mFileUploadPresenter;                           //上传图片

    protected CompileTopicDetailsPresenter mTopicDetailsPresenter;
    private WrapAdapter<TopicDetailsListBean.DataList> mCommonAdapter;
    protected SimpleDraweeView mCover;//封面
    protected TextView mTitle;//标题
    protected TextView mAttentiontext;//关注
    protected TextView mMessage;//发帖
    protected TextView mIntroduction;//导语
    protected ImageView mAttentionbutt;//编辑按钮
    protected ImageView mPicture;//编辑按钮
    protected List<TopicDetailsListBean.DataList> dataLists = new ArrayList<>();
    protected MyListView mRecyclerView;
    protected String ID;
    protected int page = 1;
    protected boolean isRefresh = true;
    protected TopicDetailsBean mTopicDetailsBean;

    protected String introString = "";
    protected String coverpath = "";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFileUploadPresenter != null) {
            mFileUploadPresenter.detachView();
        }
        if (mTopicDetailsPresenter != null) {
            mTopicDetailsPresenter.detachView();
        }
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.topicmanage);
        function.setImageResource(R.drawable.btn_more);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compiletopicdetails);
        ID = (String) getIntent().getSerializableExtra("id");
        initRefreshLayout(mBGDelegate);
        mRecyclerView = (MyListView) findViewById(R.id.rvCity);
        mCover = (SimpleDraweeView) findViewById(R.id.cover);
        mTitle = (TextView) findViewById(R.id.title);
        mAttentiontext = (TextView) findViewById(R.id.attentiontext);
        mMessage = (TextView) findViewById(R.id.message);
        mIntroduction = (TextView) findViewById(R.id.introduction);
        mAttentionbutt = (ImageView) findViewById(R.id.attentionbutt);
        mPicture = (ImageView) findViewById(R.id.picture);
        setFocusb();
        mTopicDetailsPresenter = new CompileTopicDetailsPresenter();
        mTopicDetailsPresenter.attachView(this);
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(uploadView);

        setAdapter();
        initProgressDialog();
        mTopicDetailsPresenter.getTopicDetail(IOUtils.getToken(mContext), ID);
        mTopicDetailsPresenter.GetTopicListData(IOUtils.getToken(mContext), ID, page);
        setLoadingIndicator(true);
        mAttentionbutt.setOnClickListener(this);
        mPicture.setOnClickListener(this);
    }

    public void setFocusb() {
        mCover.setFocusable(true);
        mCover.setFocusableInTouchMode(true);
        mTitle.setFocusable(true);
        mTitle.setFocusableInTouchMode(true);
        mIntroduction.setFocusable(true);
        mIntroduction.setFocusableInTouchMode(true);
    }


    public void setAdapter() {
        mCommonAdapter = new WrapAdapter<TopicDetailsListBean.DataList>(mContext, R.layout.activity_compiledetails_item, dataLists) {
            @Override
            public void convert(ViewHolder holder, final TopicDetailsListBean.DataList dataList) {
                SimpleDraweeView mSimpleDraweeView = holder.getView(R.id.imageicon);
                SimpleDraweeView mGenderImg = holder.getView(R.id.genderImg);
               /* ImageView mEssence = holder.getView(R.id.essence);
                TextView mLike = holder.getView(R.id.like);
                TextView mInformation = holder.getView(R.id.information);*/
                TextView mName = holder.getView(R.id.name);
                TextView mDynamic = holder.getView(R.id.title);

                ImageView mModel = holder.getView(R.id.comment_model_tv);
                ImageView mConveneDa = holder.getView(R.id.drivdetails_convene_da);
                ImageView mAscending = holder.getView(R.id.ascending);

                SimpleDraweeView simpleDraweeView = holder.getView(R.id.motodetail_avatar_iv);
                SimpleDraweeView mConveneBrandLogo = holder.getView(R.id.drivdetails_convene_brandLogo);

                mSimpleDraweeView
                        .setImageURI(URLUtil.builderImgUrl(dataList.thread.cover, 144, 144));

                mDynamic.setText(dataList.thread.title);

                if (dataList.isTop == 1) {
                    mAscending.setImageResource(R.drawable.btn_3down);
                } else {
                    mAscending.setImageResource(R.drawable.btn_3up);
                }

                TopicDetailsListBean.CreateUser createBy = dataList.createUser;
                simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(createBy.avatar)));
                if (createBy.sex == 1) {
                    mGenderImg.setImageResource(R.drawable.icon_men);
                } else {
                    mGenderImg.setImageResource(R.drawable.icon_women);
                }
                if (createBy.isDoyen == 1) {
                    mConveneDa.setVisibility(View.VISIBLE);
                } else {
                    mConveneDa.setVisibility(View.GONE);
                }

                if (createBy.isModel == 1) {
                    mModel.setVisibility(View.VISIBLE);
                } else {
                    mModel.setVisibility(View.GONE);
                }

                if (createBy.isMaster == 1) {
                    mConveneBrandLogo.setVisibility(View.VISIBLE);
                    mConveneBrandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(createBy.avatar)));
                } else {
                    mConveneBrandLogo.setVisibility(View.GONE);
                }
                mName.setText(createBy.nickname);
                mAscending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTopicDetailsPresenter.GetTop(IOUtils.getToken(mContext), dataList.thread_id + "", dataList.topicId + "");
                    }
                });

            }
        };
        mRecyclerView.setAdapter(mCommonAdapter);
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

        if (resultCode == RESULT_OK) {
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                String datastring = data.getStringExtra(Constants.IntentParams.DATA);
                CommonUtils.showToast(mContext, "导语不能为空==" + datastring);
                introString = datastring;
                compile();
            }
        }
    }


    private void uploadImage(ImageItem item) {
        File f = new File(item.path);
        compressWithRx(f,item);
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
    public void compile() {
        mTopicDetailsPresenter.GetUpdate(IOUtils.getToken(mContext), coverpath, ID, introString);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.attentionbutt:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put("intro", mTopicDetailsBean.data.intro);
                CommonUtils.startResultNewActivity(this, args, CompieTopicLeadActivity.class, REQUEST_CODE_PREVIEW);

                break;
            case R.id.picture:
                ImagePicker.getInstance().setMultiMode(false);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void dataSucceed(TopicDetailsBean topicDetailsBean) {
        setLoadingIndicator(false);
        mTopicDetailsBean = topicDetailsBean;
        mCover.setImageURI(Uri.parse(URLUtil.builderImgUrl(topicDetailsBean.data.cover, 540, 270)));//item图片
        mTitle.setText(topicDetailsBean.data.title);
        mAttentiontext.setText("关注  " + topicDetailsBean.data.fansNum);
        mMessage.setText("发帖  " + topicDetailsBean.data.threadNum);
        setTextColors(mIntroduction, "[ 导语 ]" + topicDetailsBean.data.intro, 0, 6, R.color.text_def);
        introString = topicDetailsBean.data.intro;
        coverpath = topicDetailsBean.data.cover;
    }

    @Override
    public void dataListSucceed(TopicDetailsListBean topicDetailsListBean) {
        stopRefreshAll();
        setLoadingIndicator(false);
        if (page == 1) {
            dataLists.clear();
        }
        if (topicDetailsListBean.data.list == null || topicDetailsListBean.data.list.size() == 0) {
            CommonUtils.showToast(mContext, "暂时没有更多数据");
            isRefresh = false;
        }
        dataLists.addAll(topicDetailsListBean.data.list);
        if (page == 1) {
            setAdapter();
        } else {
            mCommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void UpdateSucceed(BaseArryBean baseArryBean) {
        CommonUtils.showToast(mContext, "更新数据成功");
        mTopicDetailsPresenter.getTopicDetail(IOUtils.getToken(mContext), ID);
    }

    @Override
    public void TopSucceed(BaseArryBean baseArryBean) {
        CommonUtils.showToast(mContext, "置顶成功");
        page = 1;
        mTopicDetailsPresenter.GetTopicListData(IOUtils.getToken(mContext), ID, page);
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                // 如果网络可用，则加载网络数据
            //
            //                loadMore();
            //            } else {
            //                // 网络不可用，结束下拉刷新
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //            }
            // mPageIndex = 1;
            page = 1;
            isRefresh = true;
            loadMore();

        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                loadMore();
            //                return true;
            //            } else {
            //                // 网络不可用，返回false，不显示正在加载更多
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //                return false;
            //            }
            //loadMore();
            if (isRefresh) {
                page++;
                loadMore();
            }
            return false;
        }
    };

    public void loadMore() {
        mTopicDetailsPresenter.GetTopicListData(IOUtils.getToken(mContext), ID, page);
    }


    private FileUploadView<DataItem> uploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem dataItem) {
            if (dataItem.code == 0) {
                coverpath = dataItem.resp.data.path;
                compile();
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

    private ProgressDialog mProgressDialog = null;

    private void initProgressDialog() {

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在上传，请稍候...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

}
