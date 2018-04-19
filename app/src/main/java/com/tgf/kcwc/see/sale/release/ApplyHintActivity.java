package com.tgf.kcwc.see.sale.release;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ApplyHintModel;
import com.tgf.kcwc.mvp.presenter.AboutPresenter;
import com.tgf.kcwc.mvp.presenter.ApplyHintPresenter;
import com.tgf.kcwc.mvp.view.ApplyHintView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle 参展申请须知
 */
public class ApplyHintActivity extends BaseActivity implements ApplyHintView {
    WebView content;
    TextView title, exhibitionNameTv;
    ApplyHintPresenter mPresenter;
    private int eventId;
    private int type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_hint);
    }

    @Override
    protected void setUpViews() {
        eventId = getIntent().getIntExtra(Constants.IntentParams.ID,-1);
        type = getIntent().getIntExtra(Constants.IntentParams.INDEX,-1);
        title = (TextView) findViewById(R.id.title);
        exhibitionNameTv = (TextView) findViewById(R.id.exhibitionName);
        content = (WebView) findViewById(R.id.content);
        WebSettings settings = content.getSettings();
        content.setWebChromeClient(new WebChromeClient());
        content.setWebViewClient(new WebViewClient());

        mPresenter = new ApplyHintPresenter();
        mPresenter.attachView(this);
        mPresenter.getApplyHint(IOUtils.getToken(this),eventId, type);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("参展申请须知");
    }

    @Override
    public void showApplyHint(ApplyHintModel model) {
        if (model != null){
            content.getSettings().setDefaultTextEncodingName("UTF-8");
            content.loadDataWithBaseURL("", WebviewUtil.getHtmlData(model.content), "text/html", "UTF-8", "");
            title.setText(model.title);
            exhibitionNameTv.setText(model.eventName);
        }
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
        return this;
    }
}
