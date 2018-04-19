package com.tgf.kcwc.coupon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;
import com.tgf.kcwc.mvp.presenter.CouponEvaluatePresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.view.CouponEvaluateView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.posting.character.ReleaseCharacterActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.dragview.DividerItemDecoration;
import com.tgf.kcwc.view.dragview.MyAdapter;
import com.tgf.kcwc.view.dragview.MyCouponEvaluateAdapter;
import com.tgf.kcwc.view.dragview.OnRecyclerItemClickListener;
import com.tgf.kcwc.view.dragview.SwipeRecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Auther: Scott
 * Date: 2017/8/14 0014
 * E-mail:hekescott@qq.com
 */

public class CouponEvaluateActivity extends BaseActivity implements CouponEvaluateView {

    private SimpleDraweeView couponCoverIv;
    private TextView couponTitle;
    private TextView couponDesc;
    private TextView couponNowprice;
    private TextView couponOldprice;
    private Intent fromIntent;
    private int mOrderId;
    private CouponEvaluatePresenter couponEvaluatePresenter;
    private SwipeRecyclerView mRecyclerView;
    private List<DataItem> datas = new ArrayList<>();
    private MyCouponEvaluateAdapter myAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private EditText mInputTextEd;
    private RatingBar mCouponevStar;
    private RatingBar mEvaluateCommitrating;
    private RatingBar mEvaluateAttitude;
    private String token;
    private float mCouponStar;
    private float mShopStar;
    private float mAttitudeStar;

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(uploadView);
        couponEvaluatePresenter = new CouponEvaluatePresenter();
        couponEvaluatePresenter.attachView(this);
        mOrderId = fromIntent.getIntExtra(Constants.IntentParams.ID, 0);
        couponEvaluatePresenter.getCouponHeaddetail(IOUtils.getToken(getContext()), mOrderId);
        couponCoverIv = (SimpleDraweeView) findViewById(R.id.couponlist_cover);
        couponTitle = (TextView) findViewById(R.id.listitem_recoment_coupontitle);
        couponDesc = (TextView) findViewById(R.id.couponlist_desc);
        couponNowprice = (TextView) findViewById(R.id.recyleitem_near_nowprice);
        couponOldprice = (TextView) findViewById(R.id.listviewitem_recomment_oldprice);
        mRecyclerView = (SwipeRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        myAdapter = new MyCouponEvaluateAdapter(datas, mContext);

        token = IOUtils.getToken(this);
        mInputTextEd = (EditText) findViewById(R.id.input_textEd);
        mCouponevStar = (RatingBar) findViewById(R.id.couponev_star);
        mEvaluateCommitrating = (RatingBar) findViewById(R.id.evaluate_shoprating);
        mEvaluateAttitude = (RatingBar) findViewById(R.id.evaluate_attitude);

        mCouponevStar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                mCouponStar = ratingCount;
            }
        });
        mEvaluateCommitrating.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                mShopStar = ratingCount;
            }
        });
        mEvaluateAttitude.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                mAttitudeStar = ratingCount;
            }
        });


        findViewById(R.id.coupon_commitEvalTv).setOnClickListener(this);
        myAdapter.setDeleteClickListener(new MyCouponEvaluateAdapter.DeleteClickListener() {
            @Override
            public void delete(int num) {
                datas.remove(num);
                myAdapter.notifyDataSetChanged();
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
                myAdapter.notifyDataSetChanged();
            }
        });
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));
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

    }

    private static final int IMAGE_PICKER = 1001;
    private int SelectNumber = 5;

    public void SelectImage() {
        ImagePicker.getInstance().setMultiMode(true);
        ImagePicker.getInstance().setSelectLimit(SelectNumber - datas.size()); //选中数量限制
        if (SelectNumber - datas.size() != 0) {
            Intent intent = new Intent(CouponEvaluateActivity.this, ImageGridActivity.class);
            startActivityForResult(intent, IMAGE_PICKER);
        } else {
            CommonUtils.showToast(mContext, "您已经选择了5张图片");
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("发表评价");
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponev);
    }

    @Override
    public void showHead(CouponOrderDetailModel.OrderDetailCoupon orderDetailCoupon) {
        couponCoverIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(orderDetailCoupon.cover, 270, 203)));
        couponTitle.setText(orderDetailCoupon.title);
        couponDesc.setText(orderDetailCoupon.desc);
        double nowPrice = Double.parseDouble(orderDetailCoupon.price);
        if (nowPrice == 0) {
            couponNowprice.setText("免费");
            couponNowprice.setTextColor(mRes.getColor(R.color.voucher_green));
        } else {
            couponNowprice.setText("￥ " + nowPrice);
            couponNowprice.setTextColor(mRes.getColor(R.color.voucher_yellow));
        }
        SpannableString demoPrice = SpannableUtil.getDelLineString("￥ " + orderDetailCoupon.denomination);
        couponOldprice.setText(demoPrice);
    }

    @Override
    public void showPostFailed(String statusMessage) {
        CommonUtils.showToast(getContext(), statusMessage);
    }

    @Override
    public void showPostSuccess() {
        CommonUtils.showToast(getContext(), "评价成功");
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
        return CouponEvaluateActivity.this;
    }

    private int UploadingNumber = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coupon_commitEvalTv:
                mCouponevStar = (RatingBar) findViewById(R.id.couponev_star);
                mEvaluateCommitrating = (RatingBar) findViewById(R.id.evaluate_shoprating);
                if (mAttitudeStar == 0) {
                    CommonUtils.showToast(getContext(), "服务态度请评分");
                    return;
                }
                if (mCouponStar == 0) {
                    CommonUtils.showToast(getContext(), "综合评价请评分");
                    return;
                }
                if (mShopStar == 0) {
                    CommonUtils.showToast(getContext(), "店铺环境请评分");
                    return;
                }
                if (TextUtil.isEmpty(mInputTextEd.getText().toString())) {
                    CommonUtils.showToast(getContext(), "请填写评论内容");
                    return;
                }
                StringBuffer stringBuffer = new StringBuffer();
                for (DataItem data : datas) {
                    stringBuffer.append(data.resp.data.path).append(",");
                }
                if (stringBuffer.length() > 0) {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1); //调用 字符串的deleteCharAt() 方法,删除最后一个多余的逗号
                }

                couponEvaluatePresenter.postCouponEvalue(token, (int) mAttitudeStar, (int) mCouponStar, (int) mShopStar, 1, mOrderId, "order_coupon", mInputTextEd.getText().toString(), stringBuffer.toString());
                break;
            default:
                break;
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
    }

    private void uploadImage(ImageItem item) {
        File f = new File(item.path);
        compressWithRx(f, item);
    }

    private FileUploadPresenter mFileUploadPresenter;

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
    };
}
