package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.DrivingMoreBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/11 0011
 * E-mail:hekescott@qq.com
 */

public class DropDownMoreTripbooPopwin extends PopupWindow {

    private Context mContext;
    private View catContentView;
    private Resources mRes;
    private int selectPos;

    private MyGridView onlyLookGridView;
    private WrapAdapter<DataItem> onlyLookAdapter = null;                 //只看的adapter
    ArrayList<DataItem> onlyLookData = new ArrayList<>();



    private RangeSeekBar mRangeSeekBar;
    DrivingMoreBean drivingMoreBean = new DrivingMoreBean();

    TextView confirm;
    LinearLayout seekbarlayout;

    public DropDownMoreTripbooPopwin(Context context, DrivingMoreBean drivingMore) {
        super(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mContext = context;
        //this.drivingMoreBean = drivingMoreBean;
        drivingMoreBean = new DrivingMoreBean();
//        Arrays.copyOf(drivingMore.onlyLookData,drivingMore.onlyLookData.size());
        try {
            drivingMoreBean.onlyLookData = (ArrayList<DataItem>) deepCopy(drivingMore.onlyLookData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        drivingMoreBean.distance = drivingMore.distance;
        drivingMoreBean.isDistance = drivingMore.isDistance;
        mRes = mContext.getResources();
        IsSelc = false;
        initView();
        setData();
        initAdapter();
    }

    public int getSelectPos() {
        return selectPos;
    }

    public DrivingMoreBean getSelectDataItem() {
        return drivingMoreBean;
    }

    public boolean IsSelc = false;

    public boolean getIsSelec() {
        return IsSelc;
    }

    private DrivingMoreBean mSelectDataItem;

    public void setData() {
        onlyLookData.clear();
        onlyLookData.addAll(drivingMoreBean.onlyLookData);
        mRangeSeekBar.setValue(drivingMoreBean.distance);
    }

    private void initAdapter() {
        onlyLookAdapter = new WrapAdapter<DataItem>(mContext, R.layout.driving_more_item,
                onlyLookData) {
            @Override
            public void convert(ViewHolder helper, final DataItem item) {
                TextView name = helper.getView(R.id.name);
                name.setText(item.name);
                if (item.isSelected) {
                    name.setBackgroundResource(R.drawable.button_bg_more_yes);
                } else {
                    name.setBackgroundResource(R.drawable.button_bg_more_no);
                }

                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isSelected) {
                            item.isSelected = false;
                        } else {
                            item.isSelected = true;
                        }
                        onlyLookAdapter.notifyDataSetChanged();
                    }
                });

            }
        };
        onlyLookGridView.setAdapter(onlyLookAdapter);

    }

    private void singleChecked(List<DataItem> items, DataItem item) {
        for (DataItem d : items) {
            if (d.id != item.id) {
                d.isSelected = false;
            }
        }
    }

    private void initView() {
        catContentView = View.inflate(mContext, R.layout.popwin_tripbook_more, null);
        this.setContentView(catContentView);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        onlyLookGridView = (MyGridView) catContentView.findViewById(R.id.onlylook);
        mRangeSeekBar = (RangeSeekBar) catContentView.findViewById(R.id.seekbar);
        confirm = (TextView) catContentView.findViewById(R.id.confirm);
        seekbarlayout = (LinearLayout) catContentView.findViewById(R.id.seekbarlayout);
        if (drivingMoreBean.isDistance) {
            seekbarlayout.setVisibility(View.VISIBLE);
        } else {
            seekbarlayout.setVisibility(View.GONE);
        }
        mRangeSeekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max,
                                       boolean isFromUser) {
                if (min == 100) {
                    mRangeSeekBar.setProgressDescription("不限");
                    drivingMoreBean.distance = 100;
                } else {
                    int min1 = (int) min;
                    mRangeSeekBar.setProgressDescription((int) min1 / 2 + "km以内");
                    drivingMoreBean.distance = min1;
                }
            }
        });

        catContentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DropDownMoreTripbooPopwin.this.dismiss();
                    }
                });
        catContentView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                IsSelc = true;
                DropDownMoreTripbooPopwin.this.dismiss();
            }
        });
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

    public <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
}
