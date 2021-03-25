package com.supcon.supbeacon.ui

import android.os.Bundle
import com.supcon.supbeacon.BaseActivity
import com.supcon.supbeacon.R
import com.supcon.supbeacon.webviewTest.AndroidToJS
import kotlinx.android.synthetic.main.activity_j_s_interactive.*


class JSInteractiveActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_j_s_interactive)

        val webSettings = mWebView.settings

        // 设置与Js交互的权限

        // 设置与Js交互的权限
        webSettings.javaScriptEnabled = true

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        mWebView.addJavascriptInterface(AndroidToJS(), "aaa") //AndroidtoJS类对象映射到js的test对象


        // 加载JS代码
        // 格式规定为:file:///android_asset/文件名.html

        // 加载JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/web02.html")
    }

}