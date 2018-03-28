package com.willbe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.willbe.builder.R

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView;

    init {


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK && webView!!.canGoBack()) {
            webView!!.goBack()// 返回前一个页面
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_wiew_layout)
        webView = findViewById(R.id.webView) as WebView
        webView!!.settings.cacheMode = WebSettings.LOAD_DEFAULT
        // 开启 DOM storage API 功能
        webView!!.settings.domStorageEnabled = true
        webView!!.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.loadWithOverviewMode = true
        webView!!.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true;
        webView.addJavascriptInterface(WebAppInterface(this), "Android");


//        WebView.setWebContentsDebuggingEnabled(true);  // since sdk 19


        webView!!.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)

                return true
            }

            override fun onPageFinished(view: WebView, url: String) {}
        })

        webView!!.loadUrl("http://10.0.2.2:4200/")
    }
}
