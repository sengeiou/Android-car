package com.tgf.kcwc.driving.driv;

import java.util.ArrayList;
import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.DrivDetailsBean;
import com.tgf.kcwc.mvp.presenter.AppListPresenter;
import com.tgf.kcwc.mvp.view.ApplyListView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ShareFragment extends BaseFragment {

    SimpleDraweeView          mSimpleDraweeViewHead;    //头像
    SimpleDraweeView          mSimpleDraweeViewExplain; //封面
    TextView                  mTitle;                   //标题
    TextView                  mTime;                    //时间
    TextView                  mLocation;                //地址
    TextView                  mName;                    //发布人名字
    DrivDetailsBean.ShareList shareList;

    public ShareFragment() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.drivdetail_share_item;
    }

    @Override
    protected void initView() {
        mSimpleDraweeViewExplain = findView(R.id.drivingdetails_explain);
        mSimpleDraweeViewHead = findView(R.id.drdetails_share_head);
        mTitle = findView(R.id.title);
        mTime = findView(R.id.time);
        mLocation = findView(R.id.location);
        mName = findView(R.id.name);
        mSimpleDraweeViewHead.setImageURI(
            Uri.parse("http://img3.duitang.com/uploads/item/201507/22/20150722075234_LBKyc.jpeg"));
        mSimpleDraweeViewExplain.setImageURI(Uri.parse(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493186782769&di=be4e12b279620315e5d495937a9c7691&imgtype=0&src=http%3A%2F%2Fimg3.redocn.com%2Ftupian%2F20150718%2Fbingshanpubu_4597534.jpg"));
    }

}
