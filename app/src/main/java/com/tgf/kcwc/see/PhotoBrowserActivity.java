package com.tgf.kcwc.see;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片浏览器
 * Created by Administrator on 2016/5/5.
 */
public class PhotoBrowserActivity extends BaseActivity {
    private static final String KEY_IMGS = "key_imgs";
    private static final String KEY_IMG = "key_img";
    private ViewPager phtonbigPageVp;
    private ArrayList<Image> imgurlList;
    private TextView photobigCountTv, mTitleTv;
    private int mCurrentIndex;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        phtonbigPageVp = (ViewPager) findViewById(R.id.phtonbig_page_vp);
        photobigCountTv = (TextView) findViewById(R.id.photobig_count_tv);
        findViewById(R.id.photobig_close_tv).setOnClickListener(this);
        mTitleTv = (TextView) findViewById(R.id.title);
        mTitleTv.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_browser);
        Intent intent = getIntent();
        imgurlList = intent.getParcelableArrayListExtra("list");
        mCurrentIndex = intent.getIntExtra(Constants.IntentParams.DATA, -1);

        if (imgurlList != null && mCurrentIndex != -1) {
            final int size = imgurlList.size();
            phtonbigPageVp.setAdapter(new MyBigphotoAdapter());
            photobigCountTv.setText((mCurrentIndex + 1) + "/" + size);
            phtonbigPageVp.setCurrentItem(mCurrentIndex);
            mImg = imgurlList.get(mCurrentIndex);
            String name=mImg.name;
            if(TextUtils.isEmpty(name)){

                name="";
            }
            mTitleTv.setText(name);
            phtonbigPageVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mImg = imgurlList.get(position);
                    String name=mImg.name;
                    if(TextUtils.isEmpty(name)){

                        name="";
                    }
                    mTitleTv.setText(name);
                    photobigCountTv.setText((position + 1) + "/" + size);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private class MyBigphotoAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgurlList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(mContext, R.layout.viewpager_item_photobig_2, null);
            SimpleDraweeView viewpagerItemPhotobig = (SimpleDraweeView) view
                    .findViewById(R.id.viewpager_item_photobig);
           Image img = imgurlList.get(position);
            String imgName = img.name;
            if (TextUtils.isEmpty(imgName)) {
                imgName = "";
            }

            // mTitleTv.setText(imgName);
            String url =img.link;
            if (TextUtils.isEmpty(url)) {
                url = img.imgUrl;
            }
            viewpagerItemPhotobig.setImageURI(Uri.parse(URLUtil.builderImgUrl(url, 750, 1334)));
            container.addView(view);

            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private Image mImg;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photobig_close_tv:
                finish();
                break;
            case R.id.title:
                Map<String, Serializable> args = new HashMap<String, Serializable>();

                if (mImg != null&&!TextUtils.isEmpty(mImg.name)) {
                    args.put(Constants.IntentParams.ID, mImg.threadId + "");
                    CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                }

                break;
        }

    }
}
