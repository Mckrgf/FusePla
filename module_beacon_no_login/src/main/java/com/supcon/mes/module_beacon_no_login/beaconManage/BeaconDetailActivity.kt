package com.supcon.mes.module_beacon_no_login.beaconManage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.supcon.mes.module_beacon_no_login.*
import com.supcon.mes.module_beacon_no_login.bean.JSGPSCoordinate
import com.supcon.mes.module_beacon_no_login.event.HttpEvent
import com.supcon.mes.module_beacon_no_login.net.HttpRequest
import com.supcon.mes.module_beacon_no_login.ui.GPSActivity
import com.supcon.mes.module_beacon_no_login.utils.MyDateUtils
import com.supcon.supbeacon.*
import kotlinx.android.synthetic.main.activity_beacon_detail.*
import kotlinx.android.synthetic.main.all_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
class BeaconDetailActivity : BaseActivity() {
    private var currentCoordinate: JSGPSCoordinate.ParamBean.CoordinatesBean? = null
    private var beaconData = LinkedTreeMap<String, Any>()
    private var sn = ""
    private var n: Any? = null
    private var p0: Any? = null
    private var mac: Any? = null
    private val headers: Map<String, String> = HashMap()
    //webViewClient主要帮助webView处理各种通知、请求事件
    private val wbTestClient: WebViewClient = object : WebViewClient() {}

    //WebChromeClient主要辅助webView处理Javascript的对话框、网站图标、网站title、加载进度等
    private val webChromeClient: WebChromeClient = object : WebChromeClient() {}
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon_detail)
        val data: HashMap<String, Any> = intent.getSerializableExtra("data") as HashMap<String, Any>
        sn = data["sn"].toString()
        iv_return.setOnClickListener { finish() }
        initData(data)
        initwebView()
    }

    private fun initwebView() {
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
//        webView.loadUrl("https://www.baidu.com")
    }
    

    private fun initData(data: HashMap<String, Any>) {

        bt_choose_gps.setOnClickListener {
            val intent = Intent(this, GPSActivity::class.java)
            startActivityForResult(intent, REQUEST_GET_COORDINATE)
        }

        bt_commit.visibility = View.VISIBLE
        bt_commit.setOnClickListener {
            beaconData["floor"] = et_floor_content.text.toString()
            beaconData["x"] = et_gps_x_content.text.toString()
            beaconData["y"] = et_gps_y_content.text.toString()
            beaconData["z"] = et_gps_z_content.text.toString()
            beaconData["updatedAt"] = System.currentTimeMillis()
            beaconData["sn"] = sn
            beaconData["n"] = 2
            beaconData["p0"] = p0
            beaconData["mac"] = mac

            val jsonObject = Gson().toJson(beaconData)
            HttpRequest().updateBeacon(this, jsonObject, data["sn"].toString())
        }
        HttpRequest().getBeacon(this, data["sn"].toString())
        bt_delete.setOnClickListener {
            HttpRequest().deleteBeacon(this, sn)
        }
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HttpEvent?) {
        when (event?.requestCode) {
            HttpRequest.REQUEST_CODE_UPDATE_BEACON -> {
                when (event.code) {
                    200 -> {
                        ToastUtils.showShort("修改成功")
                        HttpRequest().getBeacon(this, sn)
                    }
                    else -> {
                        ToastUtils.showShort("修改失败" + event.msg)
                    }
                }
            }
            HttpRequest.REQUEST_CODE_DELETE_BEACON -> {
                when (event.code) {
                    200 -> {
                        ToastUtils.showShort("删除成功")
                        setResult(200)
                        finish()
                    }
                    else -> {
                        ToastUtils.showShort("删除失败" + event.msg)
                    }
                }
            }
            HttpRequest.REQUEST_CODE_GET_BEACON -> {
                when (event.code) {
                    200 -> {
                        val data: LinkedTreeMap<String, Any> = event.data as LinkedTreeMap<String, Any>
                        fragment_title_mac.visibility = View.VISIBLE
                        fragment_title_des.text = data["sn"].toString()
                        fragment_title_mac.text = data["mac"].toString()
                        et_floor_content.setText(data["floor"].toString())
                        et_gps_x_content.setText(data["x"].toString())
                        et_gps_y_content.setText(data["y"].toString())
                        et_gps_z_content.setText(data["z"].toString())
                        et_n_content.setText(data["n"].toString())
                        et_p0_content.setText(data["p0"].toString())
                        currentCoordinate?.lat = data["x"].toString().toDouble()
                        currentCoordinate?.lon = data["y"].toString().toDouble()
                        currentCoordinate?.hei = data["z"].toString().toDouble()
                        val time = MyDateUtils.getDateFromLong(data["updatedAt"].toString().toDouble().toLong(), MyDateUtils.date_Format)
                        et_time_content.setText(time)

                        n = data["n"]
                        p0 = data["p0"]
                        mac = data["mac"]


                        val url = DEVICE_ADDRESS + data["sn"].toString()
                        webView.loadUrl(url)
                    }
                    else -> {
                        ToastUtils.showShort("获取数据失败" + event.msg)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GET_COORDINATE -> {
                when (resultCode) {
                    RESPONSE_GET_COORDINATE -> {
                        currentCoordinate = data?.getSerializableExtra(COORDINATE_KEY) as JSGPSCoordinate.ParamBean.CoordinatesBean
                        et_gps_x_content.setText(currentCoordinate?.lat.toString())
                        et_gps_y_content.setText(currentCoordinate?.lon.toString())
                        et_gps_z_content.setText(currentCoordinate?.hei.toString())
                    }
                }
            }
        }
    }

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