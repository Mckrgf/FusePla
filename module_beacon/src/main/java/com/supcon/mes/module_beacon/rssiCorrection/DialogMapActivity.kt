package com.supcon.mes.module_beacon.rssiCorrection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebChromeClient
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.internal.LinkedTreeMap
import com.supcon.common.view.view.js.DefaultHandler
import com.supcon.mes.R
import com.supcon.mes.middleware.PLAApplication
import com.supcon.mes.middleware.util.NetUtil
import com.supcon.mes.middleware.util.UrlUtil
import com.supcon.mes.module_beacon.BaseWebActivity
import com.supcon.mes.middleware.event.HttpEvent
import com.supcon.mes.module_beacon.net.HttpRequest
import kotlinx.android.synthetic.main.activity_dialog_map.*
import kotlinx.android.synthetic.main.all_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DialogMapActivity : BaseWebActivity() {
    private var bleDevice: BluetoothDevice? = null

    //两个为gps坐标用
    private var id: String = ""
    private var label: String = ""
    private val headers: Map<String, String> = HashMap()

    private var sn:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        super.onCreate(savedInstanceState)
        initWebView()
    }

    override fun initView() {
        super.initView()
        iv_close.setOnClickListener { finish() }
        iv_return.visibility = View.GONE
        fragment_title_mac.visibility = View.VISIBLE
        fragment_title_des.setTextColor(resources.getColor(R.color.black))
        fragment_title_mac.setTextColor(resources.getColor(R.color.textColorGray))
    }

    private fun initWebView() {
        requireNotNull(webView) { "BridgeWebView is null!" }
        webView.setDefaultHandler(DefaultHandler())

//        webView.getSettings().setAppCacheEnabled(true);//启用localstorage本地存储api
//        webView.getSettings().setLightTouchEnabled(true);//启用选中功能
//        webView.getSettings().setDomStorageEnabled(true);//启用dom存储(关键就是这句)，貌似网上twitter显示有问题也是这个属性没有设置的原因
//        webView.getSettings().setDatabaseEnabled(true);//启用html5数据库功能
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

    override fun initData() {
        super.initData()
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
        fragment_title_des.setTextColor(resources.getColor(R.color.black))
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


                        id = data["spaceId"].toString()
                        label = data["sn"].toString()


                        val aa = NetUtil().getBeaconMapUrl(id, label)
                        val cookie = PLAApplication.getCooki()
                        val token = PLAApplication.getToken()
                        val bundle = UrlUtil.getNewWebBundle(aa)
                        val header: HashMap<String, String> = HashMap()
                        header["Cookie"] = cookie
                        header["Authorization"] = token
                        header.putAll(headers)
                        webView!!.loadUrl(aa, header)
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

    override fun getLayoutID(): Int {
        return R.layout.activity_dialog_map
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}