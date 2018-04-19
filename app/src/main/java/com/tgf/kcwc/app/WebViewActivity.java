package com.tgf.kcwc.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/6/2 0002
 * E-mail:hekescott@qq.com
 */

public class WebViewActivity extends BaseActivity {

    private Intent fromIntent;
    private String tileStr="";
    private String loadUrl;
    private WebView webView;

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        loadUrl = fromIntent.getStringExtra(Constants.IntentParams.DATA);
        webView = (WebView) findViewById(R.id.loadhtml_webview);
        WebSettings settings = webView.getSettings();
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        settings.setJavaScriptEnabled(true);
        if(!TextUtils.isEmpty(loadUrl)){
            webView.loadUrl(loadUrl);
        }else {
            CommonUtils.showToast(WebViewActivity.this,"加载失败");
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        tileStr =   fromIntent.getStringExtra(Constants.IntentParams.TITLE);
        if(!TextUtils.isEmpty(tileStr)){
            text.setText(tileStr);
        }
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
            else
            {
                super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
