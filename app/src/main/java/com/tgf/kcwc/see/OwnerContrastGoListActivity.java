package com.tgf.kcwc.see;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.SpecCompareAdapter;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.SelectBrandActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.CarContrastModel;
import com.tgf.kcwc.mvp.model.ParamContrast;
import com.tgf.kcwc.mvp.model.ValueItem;
import com.tgf.kcwc.mvp.presenter.CarContrastPresenter;
import com.tgf.kcwc.mvp.view.CarContrastView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.VSScrollView;
import com.tgf.kcwc.view.pinnedheader.PinnedHeaderListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.tgf.kcwc.R.id.iv_item_header_close;

/**
 * Author：Jenny
 * Date:2016/12/20 13:41
 * E-mail：fishloveqin@gmail.com
 * 车型对比(PK)页面
 */

public class OwnerContrastGoListActivity extends BaseActivity implements CarContrastView {
    protected LinearLayout llContrastModelHeaderContent;
    protected VSScrollView headerContentScroll;
    protected LinearLayout llHeaderPart;
    protected TextView ownersubMainNavDivider;
    protected PinnedHeaderListView contrasrConfigListview;
    private RelativeLayout mTitleBar;
    private int mCompareCount;
    private int mMaxCount = 9;
    int mOffSexX = 0;
    private PopupWindow popupWindow;
    private Button menubtn;
    private GridView menuGridView;
    private CarContrastPresenter mPresenter;
    private List<ParamContrast> motoParamTitleItemList = new ArrayList<ParamContrast>();
    private boolean isOpenAdd = false;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.moto_type_pk_text);
        text.setTextColor(mRes.getColor(R.color.white));
    }

    @Override
    public void onClick(View view) {

        showMenu();
    }

    @Override
    protected void setUpViews() {

        menubtn = (Button) findViewById(R.id.param_menu_btn);
        menubtn.setOnClickListener(this);
        mPresenter = new CarContrastPresenter();
        mPresenter.attachView(this);

        mTitleBar = (RelativeLayout) findViewById(R.id.titleBar);
        llContrastModelHeaderContent = (LinearLayout) findViewById(
                R.id.ll_contrast_model_header_content);
        headerContentScroll = (VSScrollView) findViewById(R.id.header_content_scroll);
        headerContentScroll.mTouchView = headerContentScroll;
        headerContentScroll.setScrollChangedListener(new VSScrollView.ScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {

                synScrollItems(l, t);
            }
        });
        llHeaderPart = (LinearLayout) findViewById(R.id.ll_header_part);
        ownersubMainNavDivider = (TextView) findViewById(R.id.ownersub_main_nav_divider);
        contrasrConfigListview = (PinnedHeaderListView) findViewById(R.id.contrasr_config_listview);
        mOffSexX = BitmapUtils.dp2px(mContext, 100);

    }

    private void synScrollItems(int l, int t) {
        int size = mAdapter.mVSViews.size();

        for (int i = 0; i < size; i++) {
            mAdapter.mVSViews.get(i).smoothScrollTo(l, t);
        }
    }

    private SpecCompareAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mIds = getIntent().getStringExtra(Constants.IntentParams.DATA);
        setContentView(R.layout.activity_owner_contrast);
    }

    private void showMenu() {
        if (popupWindow != null) {
            popupWindow.showAtLocation(menubtn, Gravity.CENTER, BitmapUtils.dp2px(mContext, 15),
                    BitmapUtils.dp2px(mContext, 55));
            return;
        }
        View contentView = initDialogMenu();
        popupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_green_dialog_bg));
        popupWindow.showAtLocation(menubtn, Gravity.CENTER, BitmapUtils.dp2px(mContext, 15),
                BitmapUtils.dp2px(mContext, 55));
    }

    @NonNull
    private View initDialogMenu() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_motoparam, null,
                false);
        menuGridView = (GridView) contentView.findViewById(R.id.dialogparam_menu_gv);

        WrapAdapter motoparamMenuAdapter = new WrapAdapter<ParamContrast>(mContext,
                R.layout.griditem_dialog, motoParamTitleItemList) {
            @Override
            public void convert(WrapAdapter.ViewHolder helper, ParamContrast item) {
                Button textView = helper.getView(R.id.name);
                textView.setText(item.title);
                if (item.isSelected) {
                    textView.setSelected(true);
                    textView.setTextColor(mRes.getColor(R.color.white));
                } else {
                    textView.setSelected(false);
                    textView.setTextColor(mRes.getColor(R.color.text_color12));
                }
            }
        };
        menuGridView.setAdapter(motoparamMenuAdapter);
        menuGridView.setOnItemClickListener(menuItemClickListener);
        return contentView;
    }

    private AdapterView.OnItemClickListener menuItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //
            ParamContrast contrast = (ParamContrast) parent.getAdapter().getItem(position);

            contrast.isSelected = true;
            //计算当前要滚动的位置
            int pos = 0;
            for (int i = 0; i < position; i++) {
                pos += mAdapter.getCountForSection(i);
            }
            contrasrConfigListview.setSelection(pos + position);
            singleChecked(contrast);
            ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
        }
    };

    private void singleChecked(ParamContrast contrast) {

        for (ParamContrast pc : motoParamTitleItemList) {

            if (pc != contrast) {
                pc.isSelected = false;
            }
        }
    }

    private void refreshData() {
        List<CarBean> datas = KPlayCarApp.getValue(Constants.KeyParams.PK_DATAS);
        if (datas == null) {
            datas = new ArrayList<CarBean>();
            KPlayCarApp.putValue(Constants.KeyParams.PK_DATAS, datas);
        }
        StringBuilder builder = new StringBuilder();
        mCurrentLength = datas.size();
        for (CarBean moto : datas) {
            if (moto.isSelected) {
                builder.append(moto.id).append(",");
            }

        }
        String ids = builder.toString();
        if (ids.length() > 0) {
            mIds = ids.substring(0, ids.length() - 1);
            mPresenter.loadContrastList(mIds, IOUtils.getToken(mContext));
        }
    }

    private boolean isPKAll = false;

    @Override
    protected void onResume() {
        super.onResume();

        String ids = getIntent().getStringExtra(Constants.IntentParams.IDS);
        isOpenAdd = getIntent().getBooleanExtra(Constants.IntentParams.IS_OPEN_ADD, false);
        if (TextUtils.isEmpty(ids)) {
            refreshData();
        } else {

            isPKAll = true;
            if (isOpenAdd) {
                isPKAll = false;
            }
            mPresenter.loadContrastList(ids, IOUtils.getToken(mContext));
        }

    }

    private String mIds;

    private void addHeaderItemView(int index, ValueItem item) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.owner_contrast_model_item_header,
                llContrastModelHeaderContent, false);
        ImageView deleteImg = (ImageView) v.findViewById(iv_item_header_close);
        TextView nameText = (TextView) v.findViewById(R.id.tv_item_header_name);
        nameText.setText(item.carName);
        nameText.setTextColor(mRes.getColor(R.color.text_color12));
        nameText.setTag(item);
        nameText.setOnClickListener(mOnClickListener);
        deleteImg.setVisibility(View.VISIBLE);
        deleteImg.setOnClickListener(this.mOnClickListener);
        deleteImg.setBackgroundDrawable(
                this.mContext.getResources().getDrawable(R.drawable.iv_header_close));
        deleteImg.setTag(R.id.tv_item_header_name, Integer.valueOf(item.carId));

        llContrastModelHeaderContent.addView(v, index);
    }

    private List<ValueItem> mItems = new ArrayList<ValueItem>();
    private int mCurrentLength = 0;

    private void addLastItemView(int index, boolean isLast) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.owner_contrast_model_item_header,
                llContrastModelHeaderContent, false);
        ImageView deleteImg = (ImageView) v.findViewById(iv_item_header_close);
        deleteImg.setVisibility(View.GONE);
        TextView nameText = (TextView) v.findViewById(R.id.tv_item_header_name);
        nameText.setVisibility(View.VISIBLE);
        if (isLast && !isPKAll) {
            nameText.setVisibility(View.VISIBLE);
            nameText.setBackgroundDrawable(
                    this.mContext.getResources().getDrawable(R.drawable.btn_contrast_enable));
            nameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //addItemsData();
                    //updateUI();

                    if (mCurrentLength >= Constants.PK_MAX) {

                        CommonUtils.showToast(mContext, "亲，最多只能添加" + Constants.PK_MAX + "辆车进行PK");
                        return;
                    }
                    KPlayCarApp.putValue(Constants.KeyParams.IS_CONTRAST, true);
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(
                            Constants.IntentParams.MODULE_TYPE,
                            Constants.ModuleTypes.CONTRAST_PK);
                    CommonUtils
                            .startNewActivity(
                                    OwnerContrastGoListActivity.this,
                                    args,
                                    SelectBrandActivity.class);
                    CommonUtils.startNewActivity(OwnerContrastGoListActivity.this, args,
                           SelectBrandActivity.class);
                }
            });
        } else {
            nameText.setVisibility(View.GONE);
            nameText.setBackgroundDrawable(
                    this.mContext.getResources().getDrawable(R.drawable.btn_contrast_no_enable));
        }
        llContrastModelHeaderContent.addView(v, index);
    }

    /**
     * 增加或者删除了数据项需要更新整个界面，重绘View
     */
    private void updateUI() {
        buildHeaderItem();
        mAdapter.notifyDataSetChanged();

    }

    private int id = 0;

    private void addItemsData() {
        id++;
        ValueItem itemEntity = new ValueItem();
        itemEntity.carName = "宝马LL N" + new Random().nextInt(1000000);
        itemEntity.carId = id;
        mItems.add(itemEntity);
        this.mCompareCount = mCompareCount + 1;
    }

    private void buildHeaderItem() {

        llContrastModelHeaderContent.removeAllViews();
        int size = mItems.size();
        mCompareCount = 0;
        for (int i = 0; i < size; i++) {

            ValueItem item = mItems.get(i);
            addHeaderItemView(i, item);
            mCompareCount++;
        }
        addLastItemView(mItems.size(), true);

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Object tag = v.getTag(R.id.tv_item_header_name);
            if (tag != null) {

                int id = Integer.parseInt(tag.toString());
                removeItemById(id);
            }
        }
    };

    /**
     * 根据id 将对比的车型进行移出
     *
     * @param id
     */
    private void removeItemById(int id) {
        //        int size = mItems.size();
        int index = -1;
        //        for (int i = 0; i < size; i++) {
        //            ValueItem item = mItems.get(i);
        //            if (item.carId == id) {
        //                index = i;
        //                break;
        //            }
        //        }
        //        if (index >= 0) {
        //            mItems.remove(index);
        //           
        //            
        //            //updateUI();
        //        }

        List<CarBean> carBeans = KPlayCarApp.getValue(Constants.KeyParams.PK_DATAS);
        int size = carBeans.size();
        for (int i = 0; i < size; i++) {
            CarBean c = carBeans.get(i);
            if (c.id == id) {
                c.isSelected = false;
                break;
            }
        }
        refreshData();

    }

    private List<ParamContrast> mParams = new ArrayList<ParamContrast>();

    private boolean isFirst = true;

    @Override
    public void showContrastView(CarContrastModel model) {

        mItems.clear();
        mParams.clear();
        motoParamTitleItemList.clear();
        List<ParamContrast> paramContrasts = model.params;
        mParams.addAll(paramContrasts);
        mItems.addAll(model.baseInfos);
        motoParamTitleItemList.addAll(paramContrasts);

        buildHeaderItem();
        mAdapter = new SpecCompareAdapter(mParams);
        mAdapter.mScrollCallback = new SpecCompareAdapter.ScrollCallback() {
            @Override
            public void callback(int l, int t, int oldl, int oldt) {
                synScrollItems(l, t);
                headerContentScroll.smoothScrollTo(l, t);
            }
        };
        contrasrConfigListview.setAdapter(mAdapter);
        if (isFirst) {
            isFirst = false;
            View view = LayoutInflater.from(mContext).inflate(R.layout.contrast_footer_item, null,
                    false);
            contrasrConfigListview.addFooterView(view);
        }

        //解决删除不同步问题
        synScrollItems(0, 0);
        headerContentScroll.smoothScrollTo(0, 0);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
