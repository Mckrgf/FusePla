package com.supcon.supbeacon.beaconManage

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.internal.LinkedTreeMap
import com.supcon.supbeacon.BaseActivity
import com.supcon.supbeacon.DEVICE_ADDRESS
import com.supcon.supbeacon.R
import com.supcon.supbeacon.event.HttpEvent
import com.supcon.supbeacon.net.HttpRequest
import com.supcon.supbeacon.utils.NetUtil
import kotlinx.android.synthetic.main.activity_dialog_map.*
import kotlinx.android.synthetic.main.all_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DialogMapActivity : BaseActivity() {
    private var bleDevice: BluetoothDevice? = null
    //webViewClient主要帮助webView处理各种通知、请求事件
    private val wbTestClient: WebViewClient = object : WebViewClient() {}

    //WebChromeClient主要辅助webView处理Javascript的对话框、网站图标、网站title、加载进度等
    private val webChromeClient: WebChromeClient = object : WebChromeClient() {}
    //两个为gps坐标用
    private var id: String = ""
    private var label: String = ""
    private val headers: Map<String, String> = HashMap()

    private var sn:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_map)
        iv_close.setOnClickListener { finish() }
        initWebView()
        initData()
    }

    private fun initWebView() {
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true //将图片调整到适合webView的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该webView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE //关闭webView中缓存
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


    fun initData() {
        bleDevice = intent.getParcelableExtra("device")
        if (null == bleDevice) {
            val name = intent.getStringExtra("deviceName")
            val address = intent.getStringExtra("deviceAddress")
            fragment_title_mac.text = address
            fragment_title_des.text = name
            sn = name
        }else {
            fragment_title_mac.text = bleDevice?.address
            fragment_title_des.text = bleDevice?.name
            sn = bleDevice?.name.toString()
        }
        HttpRequest().getBeacon(this, getSN())
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HttpEvent?) {
        when (event?.requestCode) {
            HttpRequest.REQUEST_CODE_GET_BEACON -> {
                LogUtils.d(event.toString() + event.data)
                when (event.code) {
                    200 -> {
                        //查到信标，填充界面
                        val data = event.data as LinkedTreeMap<*, *>
                        label = data["sn"].toString()

                        val url = DEVICE_ADDRESS + label
                        webView.loadUrl(url)
                    }

                    else -> {
                        ToastUtils.showLong("查不到信标")
                    }
                }
            }
        }
    }

    private fun getSN(): String {
        if (TextUtils.isEmpty(sn)) {
            return ""

        }
        val name: String
        val a = sn.length - 8
        val b = sn.length
        name = sn.substring(a, b).toString()
        return name
    }

//    override fun getLayoutID(): Int {
//        return R.layout.activity_dialog_map
//    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            (webView.getParent() as ViewGroup).removeView(webView)
            webView.destroy()
//            webView = null
        }
        super.onDestroy()
    }
}