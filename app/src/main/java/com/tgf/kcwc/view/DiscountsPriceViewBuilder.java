package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/8/4
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 价格筛选PopupWindow
 */

public class DiscountsPriceViewBuilder extends PopupWindow implements AdapterView.OnItemClickListener, View.OnClickListener {


    protected View rootView;
    protected TextView priceText;
    protected EditText minPriceEdit;
    protected RelativeLayout minLayout;
    protected View splitView;
    protected EditText priceMaxEdit;
    protected RelativeLayout maxLayout;
    protected Button confirmBtn;
    protected RelativeLayout customPriceLayout;
    protected GridView priceGridView;
    private Context mContext;
    private Resources mRes;
    private List<DataItem> mDatas = new ArrayList<DataItem>();
    private WrapAdapter<DataItem> mAdapter = null;
    private DataItem mItem;
    private FilterPriceCallback mCallback;

    public DiscountsPriceViewBuilder(Context context, FilterPriceCallback callback) {
        super(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.mContext = context;
        mRes = context.getResources();
        mCallback = callback;
        initView();
    }


    public void clear() {
        priceMaxEdit.setText("");
        minPriceEdit.setText("");
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.common_newprice_layout, null);
        this.setContentView(rootView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);

        priceText = (TextView) rootView.findViewById(R.id.priceText);
        minPriceEdit = (EditText) rootView.findViewById(R.id.minPriceEdit);
        minLayout = (RelativeLayout) rootView.findViewById(R.id.minLayout);
        splitView = (View) rootView.findViewById(R.id.splitView);
        priceMaxEdit = (EditText) rootView.findViewById(R.id.priceMaxEdit);
        maxLayout = (RelativeLayout) rootView.findViewById(R.id.maxLayout);
        confirmBtn = (Button) rootView.findViewById(R.id.confirmBtn);
        customPriceLayout = (RelativeLayout) rootView.findViewById(R.id.customPriceLayout);
        priceGridView = (GridView) rootView.findViewById(R.id.priceGridView);
        priceGridView.setOnItemClickListener(this);
        confirmBtn.setOnClickListener(this);
        minPriceEdit.addTextChangedListener(minTextListener);
        priceMaxEdit.addTextChangedListener(maxTextListener);
        rootView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DiscountsPriceViewBuilder.this.dismiss();
                    }
                });
        initGridAndBindData();
    }

    private TextWatcher minTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String max = priceMaxEdit.getText().toString();
            if (!TextUtil.isEmpty(s.toString()) && !TextUtil.isEmpty(max)) {

                confirmBtn.setSelected(true);
            } else {
                confirmBtn.setSelected(false);
            }
        }
    };

    private TextWatcher maxTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String min = minPriceEdit.getText().toString();
            if (!TextUtil.isEmpty(s.toString()) && !TextUtil.isEmpty(min)) {

                confirmBtn.setSelected(true);
            } else {
                confirmBtn.setSelected(false);
            }
        }
    };

    public void gainDatalist() {
        mDatas.clear();
        setData(1, "", "", "不限");
        setData(2, "3", "0", "3万以下");
        setData(3, "5", "3", "3-5万");
        setData(4, "8", "5", "5-8万");
        setData(5, "10", "8", "8-10万");
        setData(6, "15", "10", "10-15万");
        setData(7, "20", "15", "15-20万");
        setData(8, "30", "20", "20-30万");
        setData(9, "50", "30", "30-50万");
        setData(10, "100", "50", "50-100万");
        setData(11, "10000000000", "100", "100万以上");
    }

    public void setData(int id, String priceMaxStr, String priceMinStr, String title) {
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = id;
        dataItem.priceMaxStr = priceMaxStr;
        dataItem.priceMinStr = priceMinStr;
        dataItem.title = title;
        mDatas.add(dataItem);
    }

    private void initGridAndBindData() {

        gainDatalist();
/*        String arrays[] = mRes.getStringArray(R.array.price_values);

        int index = 0;
        for (String str : arrays) {

            DataItem item = new DataItem();
            item.title = str;
            item.key = index + "";
            item.id = index + 1;
            index++;
            mDatas.add(item);
        }*/

        mAdapter = new WrapAdapter<DataItem>(mContext, R.layout.grid_item2, mDatas) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                showBtn(helper, item);

            }
        };

        priceGridView.setAdapter(mAdapter);
    }


    private void showBtn(WrapAdapter.ViewHolder helper, DataItem item) {
        Button btn = helper.getView(R.id.name);
        btn.setText(item.title);
        btn.setTextColor(mRes.getColor(R.color.text_color3));
//        if (item.isSelected) {
//            btn.setBackgroundColor(mRes.getColor(R.color.btn_select_color));
//            btn.setTextColor(mRes.getColor(R.color.white));
//        } else {
//            btn.setBackgroundResource(R.drawable.button_bg);
//            btn.setTextColor(mRes.getColor(R.color.text_color));
//        }
    }

    @Override
    public void onClick(View v) {


        String priceMaxStr = priceMaxEdit.getText().toString();
        String priceMinStr = minPriceEdit.getText().toString();

        if (TextUtil.isEmpty(priceMinStr)) {
            CommonUtils.showToast(mContext, "请输入最小价格值，单位万！");
            return;
        }

        if (TextUtil.isEmpty(priceMaxStr)) {
            CommonUtils.showToast(mContext, "请输入最大价格值，单位万！");
            return;
        }

        if (Integer.parseInt(priceMinStr) >= Integer.parseInt(priceMaxStr)) {
            CommonUtils.showToast(mContext, "最小价格不能等于或者大于最大价格值！");
            return;
        }
        if (mCallback != null) {
            mCallback.result(priceMinStr, priceMaxStr);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        mItem = (DataItem) parent.getAdapter().getItem(position);
        mItem.isSelected = true;
        if (mCallback != null) {
            mCallback.result(mItem);
        }
        //singleChecked(mDatas, mItem);
        //mAdapter.notifyDataSetChanged();
    }

    private void singleChecked(List<DataItem> items, DataItem item) {
        for (DataItem d : items) {
            if (d.id != item.id) {
                d.isSelected = false;
            }
        }

    }

    public void showAsDropDownBelwBtnView(View btnView) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            btnView.getGlobalVisibleRect(rect);
            int h = btnView.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(btnView, 0, 0);
    }

    /**
     * 价格筛选回调
     */
    public interface FilterPriceCallback {
        /**
         * @param customMin 自定义最小价格值
         * @param customMax 自定义最大价格值
         */
        public void result(String customMin, String customMax);

        /**
         * @param item 所选定默认提供的选项
         */
        public void result(DataItem item);
    }
}
