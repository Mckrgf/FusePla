package com.supcon.mes.module_beacon.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.supcon.common.view.base.activity.BaseWebViewActivity
import com.supcon.common.view.util.StatusBarUtils
import com.supcon.common.view.view.js.DefaultHandler
import com.supcon.mes.module_beacon.R
import com.supcon.mes.middleware.PLAApplication
import com.supcon.mes.middleware.util.NetUtil
import com.supcon.mes.middleware.util.Util.getStatusBarHeight
import com.supcon.mes.module_beacon.BaseWebActivity
import com.supcon.mes.module_beacon.COORDINATE_KEY
import com.supcon.mes.module_beacon.RESPONSE_GET_COORDINATE
import com.supcon.mes.module_beacon.bean.JSGPSCoordinate
import kotlinx.android.synthetic.main.activity_g_p_s.*
import kotlinx.android.synthetic.main.all_title.*


class GPSActivity : BaseWebActivity() {
    var coordinatesBean : JSGPSCoordinate.ParamBean.CoordinatesBean? = null
    private var bleDevice: BluetoothDevice? = null
    private val headers: Map<String, String> = HashMap()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_g_p_s)
        bleDevice = intent.getParcelableExtra("device")

        initWebView()

        fragment_title_des.setTextColor(resources.getColor(R.color.black))
        fragment_title_mac.visibility = View.VISIBLE
        fragment_title_des.text = bleDevice?.name
        fragment_title_mac.text = bleDevice?.address
        bt_commit.visibility = View.VISIBLE
        bt_commit.setOnClickListener {
            backToMain(coordinatesBean)
        }
        iv_return.setOnClickListener { finish() }

        val cookie = PLAApplication.getCooki()
        val token = PLAApplication.getToken()
        val header: HashMap<String, String> = HashMap()
        header["Cookie"] = cookie
        header["Authorization"] = token
        header.putAll(headers)
        webView!!.loadUrl(NetUtil().getBeaconMapUrlForCoor(),header)
        webView!!.registerHandler("mobilejs") { data, function ->
            function.onCallBack("submitFromWeb exe, response data from Java")
            val gpsCoordinate = Gson().fromJson(data, JSGPSCoordinate::class.java)
            coordinatesBean = gpsCoordinate.param?.coordinates?.get(0)
            ToastUtils.showShort(coordinatesBean?.hei.toString() + resources.getString(R.string.meter) + ", ${resources.getString(R.string.coordinate)}:" + coordinatesBean?.lon + "," + coordinatesBean?.lat)
            tv_coordinate.text = "x???${coordinatesBean?.lat} y: ${coordinatesBean?.lon} z: ${coordinatesBean?.hei}"
            bt_commit.isEnabled = true
        }
    }

    override fun initView() {
        super.initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT //???????????????????????????
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val firstChildAtDecorView = (window.decorView as ViewGroup).getChildAt(0) as ViewGroup
            val statusView = View(this)
            val statusViewLp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight()
            )
            //????????????????????????????????????????????????
            statusView.setBackgroundColor(resources.getColor(R.color.white))
            firstChildAtDecorView.addView(statusView, 0, statusViewLp)
        }
        setAndroidNativeLightStatusBar(this,true)
    }
    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor: View = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
    protected open fun getStatusBarHeight(): Int {
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) {
            resources.getDimensionPixelSize(resId)
        } else 0
    }
    private fun initWebView() {
        requireNotNull(webView) { "BridgeWebView is null!" }
        webView.setDefaultHandler(DefaultHandler())

//        webView.getSettings().setAppCacheEnabled(true);//??????localstorage????????????api
//        webView.getSettings().setLightTouchEnabled(true);//??????????????????
//        webView.getSettings().setDomStorageEnabled(true);//??????dom??????(??????????????????)???????????????twitter??????????????????????????????????????????????????????
//        webView.getSettings().setDatabaseEnabled(true);//??????html5???????????????
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        val settings = webView.settings
        settings.setAppCacheEnabled(false)
        //        settings.setJavaScriptEnabled(true);
        settings.allowFileAccess = true
        settings.databaseEnabled = true
        val dir = this.applicationContext.getDir("database", Context.MODE_PRIVATE).path
        settings.databasePath = dir
        settings.domStorageEnabled = true
        settings.setGeolocationEnabled(true)
        settings.lightTouchEnabled = true
        settings.allowUniversalAccessFromFileURLs = true
        webView.webChromeClient = object : WebChromeClient() {}
    }

    private fun backToMain(coordinatesBean: JSGPSCoordinate.ParamBean.CoordinatesBean?) {
        if (null == coordinatesBean) {
            ToastUtils.showShort(resources.getString(R.string.no_coordinate_was_chose))
            finish()
        }
        intent.putExtra(COORDINATE_KEY, coordinatesBean)
        intent.putExtra(COORDINATE_KEY, coordinatesBean)
        setResult(RESPONSE_GET_COORDINATE, intent)
        finish()
    }

    //WebViewClient????????????WebView?????????????????????????????????
    private val wbTestClient: WebViewClient = object : WebViewClient() {}

    //WebChromeClient????????????WebView??????Javascript????????????????????????????????????title??????????????????
    private val webChromeClient: WebChromeClient = object : WebChromeClient() {}

    override fun onDestroy() {
        if (null != webView) {
            webView!!.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView!!.clearHistory()
            webView!!.destroy()
        }
        super.onDestroy()
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_g_p_s    }

}