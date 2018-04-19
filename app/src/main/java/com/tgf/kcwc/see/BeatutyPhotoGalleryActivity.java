package com.tgf.kcwc.see;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ImgItem;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;

/**
 * Auther: Scott
 * Date: 2016/12/19 0019
 * E-mail:hekescott@qq.com
 */

public class BeatutyPhotoGalleryActivity extends BaseActivity {
    private ViewPager phtonbigPageVp;
    private ArrayList<ImgItem> imgurlList;
    private TextView photobigCountTv;
    private int index;
    private View closeTView;
    private TextView titleTv;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
    }

    @Override
    protected void setUpViews() {
        phtonbigPageVp = (ViewPager) findViewById(R.id.phtonbig_page_vp);
        photobigCountTv = (TextView) findViewById(R.id.photobig_count_tv);
        closeTView = findViewById(R.id.photobig_close_tv);
        titleTv = (TextView) findViewById(R.id.beautygallery_titletv);
        titleTv.setOnClickListener(this);
        closeTView.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_photoallery);
        Intent intent = getIntent();
        imgurlList = intent.getParcelableArrayListExtra(Constants.IntentParams.DATA);
        index = intent.getIntExtra(Constants.IntentParams.INDEX, 0);
        if (imgurlList.size() != 0) {
            phtonbigPageVp.setAdapter(new MyBigphotoAdapter());
            photobigCountTv.setText("1/" + imgurlList.size());
            phtonbigPageVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    index =position;
                    photobigCountTv.setText((position + 1) + "/" + imgurlList.size());
                    titleTv.setText(imgurlList.get(position).name);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        if (index != 0) {
            photobigCountTv.setText((index + 1) + "/" + imgurlList.size());
            phtonbigPageVp.setCurrentItem(index);
        }
        titleTv.setText(imgurlList.get(index).name);
    }

    private class MyBigphotoAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgurlList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(mContext, R.layout.viewpager_item_photobig, null);
//            MyPhotoView viewpagerItemPhotobig = (MyPhotoView) view
//                .findViewById(R.id.viewpager_item_photobig);
            PhotoView mPhotoView = (PhotoView) view
                    .findViewById(R.id.viewpager_item_photobig);
            Glide.with(mContext).load(URLUtil.builderImgUrl(imgurlList.get(position).linkUrl)).into(mPhotoView);
//            mPhotoView.setImageBitmap();
//            viewpagerItemPhotobig.setImageUri(Uri.parse(imgurlList.get(position)));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photobig_close_tv:
                finish();
                break;
            case R.id.beautygallery_titletv:
                Intent toThreadAc = new Intent(this,TopicDetailActivity.class);
                toThreadAc.putExtra(Constants.IntentParams.ID, imgurlList.get(index).threadId + "");
                startActivity(toThreadAc);
                break;
        }

    }
}
