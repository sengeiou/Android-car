package com.tgf.kcwc.driving.driv;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.HeaderBannerWebActivity;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.SmoothListView.AbsHeaderView;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/18.
 */

public class HeaderBannerView extends AbsHeaderView<List<BannerModel.Data>> {

    private Banner       drivingBanner;

    private  List<String> imgUrl = new ArrayList<>();

    private  Activity     activity;

    public HeaderBannerView(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void getView(final List<BannerModel.Data> datas, ListView listView) {
        View view = mInflate.inflate(R.layout.activity_drivi_banner, listView, false);
        drivingBanner = (Banner) view.findViewById(R.id.driving_banner);

        for (int i = 0; i < datas.size(); i++) {
            imgUrl.add(URLUtil.builderImgUrl(datas.get(i).image, 540, 270));

        }
        drivingBanner.setImages(imgUrl).setImageLoader(new FrescoImageLoader())
            .setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (datas.get(position-1).type==2){
                        if (!TextUtils.isEmpty(datas.get(position - 1).url)) {
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, datas.get(position - 1).url);
                            args.put(Constants.IntentParams.ID2, datas.get(position - 1).title);
                            CommonUtils.startNewActivity(activity, args, HeaderBannerWebActivity.class);
                        }
                    }else {

                    }
                }
            }).start();
        listView.addHeaderView(view);
    }

}
