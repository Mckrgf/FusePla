package com.supcon.mes.module_beacon_no_login.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.supcon.mes.module_beacon_no_login.BaseActivity
import com.supcon.mes.module_beacon_no_login.COORDINATE_KEY
import com.supcon.mes.module_beacon_no_login.GPS_ADDRESS
import com.supcon.mes.module_beacon_no_login.RESPONSE_GET_COORDINATE
import com.supcon.mes.module_beacon_no_login.bean.JSGPSCoordinate
import com.supcon.supbeacon.*
import kotlinx.android.synthetic.main.activity_g_p_s_b.*
import java.lang.Exception


class GPSActivity : BaseActivity() {
    var coordinatesBean : JSGPSCoordinate.ParamBean.CoordinatesBean? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s_b)

        bt_sure.setOnClickListener {
            backToMain(coordinatesBean)
        }

        webView!!.webChromeClient = object : WebChromeClient() {}
        webView!!.loadUrl(GPS_ADDRESS)
        webView!!.registerHandler("mobilejs") { data, function ->
            try {
                function.onCallBack("submitFromWeb exe, response data from Java")

                val gpsCoordinate = Gson().fromJson(data, HashMap::class.java)
                val list:ArrayList<Double> = gpsCoordinate["param"] as ArrayList<Double>
                coordinatesBean = JSGPSCoordinate.ParamBean.CoordinatesBean()
                    coordinatesBean?.lat = list[0]
                    coordinatesBean?.lon = list[1]
                    coordinatesBean?.hei = 0.0
                ToastUtils.showShort(coordinatesBean?.hei.toString() + resources.getString(R.string.meter) + ", ${resources.getString(R.string.coordinate)}:" + coordinatesBean?.lon + "," + coordinatesBean?.lat)
                tv_coordinate.text = "x：${coordinatesBean?.lat} y: ${coordinatesBean?.lon} z: ${coordinatesBean?.hei}"
                bt_sure.isEnabled = true
            }catch (e : Exception) {}

        }
    }

    private fun backToMain(coordinatesBean: JSGPSCoordinate.ParamBean.CoordinatesBean?) {
        intent.putExtra(COORDINATE_KEY, coordinatesBean)
        intent.putExtra(COORDINATE_KEY, coordinatesBean)
        setResult(RESPONSE_GET_COORDINATE, intent)
        finish()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE //关闭webview中缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT //不使用缓存，只从网络获取数据.
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webView.webChromeClient = webChromeClient
        webView.webViewClient = wbTestClient
    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private val wbTestClient: WebViewClient = object : WebViewClient() {}

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private val webChromeClient: WebChromeClient = object : WebChromeClient() {}

    override fun onDestroy() {
        if (null != webView) {
            webView!!.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView!!.clearHistory()
            webView!!.destroy()
        }
        super.onDestroy()
    }

}