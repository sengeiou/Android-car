package com.tgf.kcwc.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.mvp.model.FilterItem;
import com.tgf.kcwc.mvp.presenter.FilterDataPresenter;
import com.tgf.kcwc.mvp.view.FilterDataView;

import java.util.List;

/**
 * 三级联动View
 *
 * @author Jenny
 */
public class ThreeLevelView extends LinearLayout implements FilterDataView<List<FilterItem>> {

    public ListView getRootLevelListView() {
        return mRootLevelListView;
    }

    public ListView getSecondLevelListView() {
        return mSecondLevelListView;
    }

    public ListView getThirdLevelListView() {
        return mThirdLevelListView;
    }

    private ListView   mRootLevelListView;
    private ListView   mSecondLevelListView;
    private ListView   mThirdLevelListView;
    private Context    mContext;
    private View       mParent;
    private FilterType mType;

    private View       mView;
    private int        mPosition;
    private FilterItem mItem;

    public ThreeLevelView(Context context, View parent, FilterType type, String tag) {
        super(context);
        this.mTag = tag;
        this.mContext = context;
        this.mParent = parent;
        mPresenter = new FilterDataPresenter();
        mPresenter.attachView(this);
        mLevel = 1;
        mType = type;

        init();
        loadFilterData();
    }

    public void setTag(String tag) {
        this.mTag = tag;

        loadFilterData();
    }

    private String mTag = "";

    private void loadFilterData() {
        switch (mType) {

            case AREA:
                mPresenter.loadFilterAreaList(mTag);
                break;
            case GOODS:
                mPresenter.loadFilterGoodsList(mTag);
                break;
        }

    }

    public FilterItem getRootItem() {
        return mRootItem;
    }

    private FilterItem mRootItem = null;

    private void init() {
        mRootLevelListView = (ListView) mParent.findViewById(R.id.listView);
        mSecondLevelListView = (ListView) mParent.findViewById(R.id.listView2);
        mSecondLevelListView.setVisibility(View.GONE);
        mThirdLevelListView = (ListView) mParent.findViewById(R.id.listView3);
        mThirdLevelListView.setVisibility(View.GONE);

        mRootLevelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FilterItem filterItem = (FilterItem) parent.getAdapter().getItem(position);
                mRootItem = filterItem;
                ThreeLevelView.this.mView = view;
                ThreeLevelView.this.mPosition = position;
                ThreeLevelView.this.mItem = filterItem;
                mLevel = 2;
                mTag = filterItem.id + "";
                loadFilterData();

            }
        });
        mSecondLevelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FilterItem filterItem = (FilterItem) parent.getAdapter().getItem(position);
                filterItem.isSelected = !filterItem.isSelected;
                if (mSelectListener != null) {
                    mSelectListener.onSelect(view, filterItem, position);
                    ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
                }
            }
        });
    }

    private int mLevel = -1;

    @Override
    public void showData(List<FilterItem> filterItems) {

        switch (mLevel) {
            case 1:
                mRootLevelListView.setAdapter(
                    new WrapAdapter<FilterItem>(mContext, R.layout.filter_list_item, filterItems) {
                        @Override
                        public void convert(ViewHolder helper, FilterItem item) {

                            String title = "";
                            if (TextUtils.isEmpty(item.name)) {
                                title = item.title;
                            }
                            if (TextUtils.isEmpty(item.title)) {
                                title = item.name;
                            }
                            helper.setText(R.id.title, title);

                            //                            ImageView selectImg = helper.getView(R.id.select_status_img);
                            //                            if (item.isSelected) {
                            //                                selectImg.setVisibility(View.VISIBLE);
                            //                            } else {
                            //                                selectImg.setVisibility(View.GONE);
                            //                            }

                        }
                    });
                break;
            case 2:

                if (filterItems == null || filterItems.size() == 0) {
                    mSecondLevelListView.setVisibility(View.GONE);

                    if (mSelectRootListener != null) {
                        mSelectRootListener.onSelect(mView, mItem, mPosition);
                    }
                } else {
                    mSecondLevelListView.setVisibility(VISIBLE);
                    mSecondLevelListView.setTag(filterItems);
                    mSecondLevelListView.setAdapter(new WrapAdapter<FilterItem>(mContext,
                        R.layout.filter_list_item, filterItems) {
                        @Override
                        public void convert(ViewHolder helper, FilterItem item) {

                            String title = "";
                            if (TextUtils.isEmpty(item.name)) {
                                title = item.title;
                            }
                            if (TextUtils.isEmpty(item.title)) {
                                title = item.name;
                            }
                            helper.setText(R.id.title, title);
                            ImageView selectImg = helper.getView(R.id.select_status_img);
                            if (item.isSelected) {
                                selectImg.setVisibility(View.VISIBLE);
                            } else {
                                selectImg.setVisibility(View.GONE);
                            }

                        }
                    });
                }

                break;
        }

    }

    public interface OnSelectListener {

        void onSelect(View v, FilterItem item, int position);

    }

    private OnSelectListener mSelectRootListener, mSelectListener = null;

    public void setOnSelectListener(OnSelectListener listener) {
        this.mSelectListener = listener;
    }

    public void setOnSelectRootListener(OnSelectListener listener) {
        this.mSelectRootListener = listener;
    }

    private FilterDataPresenter mPresenter;

    public enum FilterType {

                            AREA, GOODS
    }

    public void stop() {

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
