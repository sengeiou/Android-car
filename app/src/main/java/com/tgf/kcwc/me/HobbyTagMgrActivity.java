package com.tgf.kcwc.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.adapter.WrapHideAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.HobbyTag;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/20
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 兴趣标签管理
 */

public class HobbyTagMgrActivity extends BaseActivity implements UserDataView<List<HobbyTag>> {

    protected ExpandableListView list;
    private UserDataPresenter mPresenter;

    private MyExpandableListViewAdapter mAdapter = null;

    private ArrayList<HobbyTag> mDatas = new ArrayList<HobbyTag>();

    @Override
    protected void setUpViews() {
        ArrayList<HobbyTag> mHobbyTags = getIntent().getParcelableArrayListExtra("mHobbyTags");
        initView();
        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);
        if (mHobbyTags!=null){
            mDatas.clear();
            mDatas.addAll(mHobbyTags);
            initTagStatus();
            mAdapter.refresh();
        }else {
            mPresenter.getHobbyTags(IOUtils.getToken(mContext));
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("兴趣标签管理");
        function.setTextResource("确定", R.color.white, 14);

        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();

                intent.putExtra(Constants.IntentParams.DATA, mDatas);

                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_hobby_tag_mgr);

    }

    @Override
    public void showDatas(List<HobbyTag> datas) {

        mDatas.clear();
        mDatas.addAll(datas);
        initTagStatus();
        mAdapter.refresh();
    }

    private void initTagStatus() {

        int size = mDatas.size();
        for (int i = 0; i < size; i++) {
            final List<HobbyTag> hobbyTags = mDatas.get(i).second;

            for (HobbyTag hobbyTag : hobbyTags) {

                if (hobbyTag.checked == 1) {
                    hobbyTag.isSelected = true;
                } else {
                    hobbyTag.isSelected = false;
                }
            }

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

    private void initView() {
        list = (ExpandableListView) findViewById(R.id.list);

        //        mAdapter = new WrapAdapter<HobbyTag>(mContext, R.layout.hobby_tag_list_item, mDatas) {
        //
        //            protected FlowLayout   tagLists;
        //            protected TextView     tagTitle;
        //            private RelativeLayout titleLayout;
        //
        //            @Override
        //            public void convert(final ViewHolder helper, final HobbyTag item) {
        //                tagTitle = helper.getView(R.id.tagTitle);
        //                tagLists = helper.getView(R.id.tagLists);
        //                titleLayout = helper.getView(R.id.titleLayout);
        //                tagTitle.setText(item.name);
        //                addTagItems(tagLists, item.second);
        //                titleLayout.setOnClickListener(new View.OnClickListener() {
        //                    @Override
        //                    public void onClick(View v) {
        //
        //                        Object obj = v.getTag();
        //
        //                        boolean flag = false;
        //
        //                        if (obj != null) {
        //                            flag = Boolean.parseBoolean(obj.toString());
        //                        }
        //
        //                        if (flag) {
        //                            v.setTag(false);
        //                            tagLists.setVisibility(View.GONE);
        //                        } else {
        //                            v.setTag(true);
        //                            tagLists.setVisibility(View.VISIBLE);
        //                        }
        //                        View convertView = helper.getConvertView();
        //                        convertView.measure(0, 0);//重新计算高度
        //                        int height = convertView.getMeasuredHeight();//获取当前高度
        //                        convertView.setMinimumHeight(height);
        //                        mAdapter.notifyDataSetChanged();
        //
        //                    }
        //                });
        //
        //            }
        //        };
        //        list.setAdapter(mAdapter);

        mAdapter = new MyExpandableListViewAdapter(mContext);

        list.setAdapter(mAdapter);
    }

    private class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

        private Context context;

        public MyExpandableListViewAdapter(Context context) {
            this.context = context;
        }

        public void refresh() {

            mHandler.sendEmptyMessage(0);

        }

        private Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                notifyDataSetChanged();

            }
        };

        /**
         * 获取组的个数
         *
         * @return
         * @see android.widget.ExpandableListAdapter#getGroupCount()
         */
        @Override
        public int getGroupCount() {
            return mDatas.size();
        }

        /**
         * 获取指定组中的子元素个数
         *
         * @param groupPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
         */
        @Override
        public int getChildrenCount(int groupPosition) {

            return 1;
        }

        /**
         * 获取指定组中的数据
         *
         * @param groupPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getGroup(int)
         */
        @Override
        public Object getGroup(int groupPosition) {
            return mDatas.get(groupPosition);
        }

        /**
         * 获取指定组中的指定子元素数据。
         *
         * @param groupPosition
         * @param childPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getChild(int, int)
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mDatas.get(groupPosition).second.get(childPosition);
        }

        /**
         * 获取指定组的ID，这个组ID必须是唯一的
         *
         * @param groupPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getGroupId(int)
         */
        @Override
        public long getGroupId(int groupPosition) {
            return mDatas.get(groupPosition).id;
        }

        /**
         * 获取指定组中的指定子元素ID
         *
         * @param groupPosition
         * @param childPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getChildId(int, int)
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            List<HobbyTag> list = mDatas.get(groupPosition).second;
            int size = list.size();
            if (size == 0) {

                return -1;
            }
            return list.get(childPosition).id;
        }

        /**
         * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
         *
         * @return
         * @see android.widget.ExpandableListAdapter#hasStableIds()
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        /**
         * 获取显示指定组的视图对象
         *
         * @param groupPosition 组位置
         * @param isExpanded    该组是展开状态还是伸缩状态
         * @param convertView   重用已有的视图对象
         * @param parent        返回的视图对象始终依附于的视图组
         * @return
         * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {

            WrapHideAdapter.ViewHolder holder = WrapHideAdapter.ViewHolder.get(context, convertView,
                    parent, R.layout.hobby_tag_list_item, groupPosition);

            TextView nametv = holder.getView(R.id.tagTitle);
            final TextView allTv = holder.getView(R.id.allTv);
            final HobbyTag tag = mDatas.get(groupPosition);
            nametv.setText(tag.name);

            ImageView imageView = holder.getView(R.id.groupIndicatorImg);
            if (isExpanded) {
                allTv.setVisibility(View.VISIBLE);
                imageView.setSelected(true);
            } else {
                allTv.setVisibility(View.GONE);
                imageView.setSelected(false);
            }

            if (tag.isAll) {
                allTv.setText("全部取消");
            } else {
                allTv.setText("全部关注");
            }
            allTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //                    Object obj = v.getTag();
                    //                    boolean flag = false;
                    //                    if (obj != null) {
                    //                        flag = Boolean.parseBoolean(obj.toString());
                    //                    }
                    //
                    //                    if (flag) {
                    //                        v.setTag(false);
                    //                        allTv.setText("全部关注");
                    //                        setAllSelected(tag.second, false, groupPosition);
                    //                    } else {
                    //                        v.setTag(true);
                    //                        allTv.setText("全部取消");
                    //                        setAllSelected(tag.second, true, groupPosition);
                    //
                    //                    }

                    tag.isAll = !tag.isAll;
                    if (tag.isAll) {
                        setAllSelected(tag.second, true, groupPosition);
                    } else {
                        setAllSelected(tag.second, false, groupPosition);
                    }

                }
            });

            return holder.getConvertView();
        }

        private void setAllSelected(List<HobbyTag> tags, boolean isSelected, int groupPostion) {

            for (HobbyTag tg : tags) {

                tg.isSelected = isSelected;
                if (isSelected == true) {
                    tg.checked = 1;
                } else {
                    tg.checked = 0;
                }
            }
            list.collapseGroup(groupPostion);
            list.expandGroup(groupPostion);
            refresh();
        }

        /**
         * 获取一个视图对象，显示指定组中的指定子元素数据。
         *
         * @param groupPosition 组位置
         * @param childPosition 子元素位置
         * @param isLastChild   子元素是否处于组中的最后一个
         * @param convertView   重用已有的视图(View)对象
         * @param parent        返回的视图(View)对象始终依附于的视图组
         * @return
         * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            WrapAdapter.ViewHolder holder = WrapAdapter.ViewHolder.get(mContext, convertView,
                    parent, R.layout.hobby_tag_list_item2, childPosition);

            FlowLayout flowLayout = holder.getView(R.id.tagLists);
            addTagItems(flowLayout, mDatas.get(groupPosition).second, groupPosition);

            return holder.getConvertView();
        }

        /**
         * 是否选中指定位置上的子元素。
         *
         * @param groupPosition
         * @param childPosition
         * @return
         * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
         */
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    /**
     * 添加标签
     *
     * @param datas
     */
    private void addTagItems(final FlowLayout tagLayout, final List<HobbyTag> datas, final int groupPosition) {
        tagLayout.removeAllViews();
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            final HobbyTag hobbyTag = datas.get(i);
            View v = LayoutInflater.from(mContext).inflate(R.layout.text_tag_item3, tagLayout,
                    false);
            v.setTag(hobbyTag.id);
            tagLayout.addView(v);
            final TextView tv = (TextView) v.findViewById(R.id.name);
            tv.setText(hobbyTag.name + "");
            tv.setTag(hobbyTag.id);

            if (hobbyTag.checked == 1) {
                hobbyTag.isSelected = true;
            } else {
                hobbyTag.isSelected = false;
            }

            if (hobbyTag.isSelected) {
                v.setBackgroundResource(R.drawable.oval_bg5);
                tv.setTextColor(mRes.getColor(R.color.white));

            } else {
                v.setBackgroundResource(R.drawable.oval_bg2);
                tv.setTextColor(mRes.getColor(R.color.text_color17));
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    hobbyTag.isSelected = !hobbyTag.isSelected;

                    if (hobbyTag.isSelected) {
                        hobbyTag.checked = 1;
                    } else {
                        hobbyTag.checked = 0;
                    }
                    if (hobbyTag.isSelected) {
                        v.setBackgroundResource(R.drawable.oval_bg5);
                        tv.setTextColor(mRes.getColor(R.color.white));
                    } else {
                        v.setBackgroundResource(R.drawable.oval_bg2);
                        tv.setTextColor(mRes.getColor(R.color.text_color17));
                    }

                    int num = 0;
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).isSelected) {
                            num++;
                        }
                    }

                    if (num == datas.size()) {
                        mDatas.get(groupPosition).isAll = true;
                    } else {
                        mDatas.get(groupPosition).isAll = false;
                    }
                    mAdapter.refresh();
                }
            });
        }
    }

}
