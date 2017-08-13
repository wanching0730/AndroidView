package com.example.milka.m0802.Html.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.milka.m0802.R;

/**
 * Created by Milka on 2017/8/7.
 *
 * Html5首页
 *
 * 简单交互步骤
 *1、权限：<uses-permission android:name="android.permission.INTERNET"></uses-permission>
 * 2、设置允许webView执行javaScript代码
 * 3、webView加载页面
 * 4、在webView中允许执行通过this别名使用，调用@JavascriptInterface函数
 * 5-1、原生通过webView.load("javaScript: function(arg,...)")直接调用网页中的javaScript代码
 * 5-2、网页通过window.别名.function 调用原生代码
 */

public class HtmlMainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView contentWebView;
    private Button btnCallNoParamJs;
    private Button btnCallParamJs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_html);
        initView();
        initData();
        setProperty();
        setListener();
    }

    private void initView() {
        contentWebView = (WebView) findViewById(R.id.wv);
        btnCallNoParamJs = (Button) findViewById(R.id.btn_call_no_param_js);
        btnCallParamJs = (Button) findViewById(R.id.btn_call_param_js);
    }

    private void initData() {

    }

    private void setProperty() {
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.loadUrl("file:///android_asset/web.html");
        contentWebView.addJavascriptInterface(this,"milka");
    }

    private void setListener() {
        btnCallNoParamJs.setOnClickListener(this);
        btnCallParamJs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_call_no_param_js:
                /*调用无参JS*/
                contentWebView.loadUrl("javascript:javaCallJs()");
                break;
            case R.id.btn_call_param_js:
                /*调用有参JS*/
                String s = "有参JavaScript被调用";
                contentWebView.loadUrl("javascript:javaCallJsWith('"
                        + s
                        + "')"
                );
                break;
            default:
                break;
        }
    }

    @JavascriptInterface
    public void startFunction(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HtmlMainActivity.this, "无参android原生被调用", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @JavascriptInterface
    public void startFunction(final String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HtmlMainActivity.this, "有参android原生被调用,且参数为" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
